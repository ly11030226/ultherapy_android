package com.aimyskin.ultherapy_android.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.aimyskin.laserserialmodule.LaserSerialService
import com.aimyskin.ultherapy_android.DEFAULT_NEED_POINT_NUMBER
import com.aimyskin.ultherapy_android.KEY_FROM_WHERE_TO_SETUP
import com.aimyskin.ultherapy_android.LIMIT_DIFFERENCE_POINT
import com.aimyskin.ultherapy_android.R
import com.aimyskin.ultherapy_android.REMINDER_CAN_NOT_CLICK
import com.aimyskin.ultherapy_android.REMINDER_STANdBY_STATE
import com.aimyskin.ultherapy_android.STR_READY
import com.aimyskin.ultherapy_android.STR_STANDBY
import com.aimyskin.ultherapy_android.WHERE_FROM_AWAIT
import com.aimyskin.ultherapy_android.WHERE_FROM_MAIN
import com.aimyskin.ultherapy_android.base.BaseActivity
import com.aimyskin.ultherapy_android.base.DataReceiver
import com.aimyskin.ultherapy_android.databinding.ActivityMainBinding
import com.aimyskin.ultherapy_android.inter.ReceiveDataCallback
import com.aimyskin.ultherapy_android.pojo.AutoRecognition
import com.aimyskin.ultherapy_android.pojo.Command
import com.aimyskin.ultherapy_android.pojo.DataBean
import com.aimyskin.ultherapy_android.pojo.DistanceLength
import com.aimyskin.ultherapy_android.pojo.FootPress
import com.aimyskin.ultherapy_android.pojo.FrameBean
import com.aimyskin.ultherapy_android.pojo.HIFUBean
import com.aimyskin.ultherapy_android.pojo.KnifeState
import com.aimyskin.ultherapy_android.pojo.KnifeUsable
import com.aimyskin.ultherapy_android.pojo.OperatingSource
import com.aimyskin.ultherapy_android.pojo.PRESS
import com.aimyskin.ultherapy_android.pojo.Pitch
import com.aimyskin.ultherapy_android.pojo.PointOrLine
import com.aimyskin.ultherapy_android.pojo.Position
import com.aimyskin.ultherapy_android.pojo.RepeatTime
import com.aimyskin.ultherapy_android.pojo.SingleOrRepeat
import com.aimyskin.ultherapy_android.pojo.StandbyOrReady
import com.aimyskin.ultherapy_android.pojo.Type
import com.aimyskin.ultherapy_android.pojo.getFrameDataString
import com.aimyskin.ultherapy_android.util.GlobalVariable
import com.aimyskin.ultherapy_android.util.createFrameData
import com.aimyskin.ultherapy_android.util.getLengthValue
import com.aimyskin.ultherapy_android.util.getPitchValue
import com.aimyskin.ultherapy_android.util.getTotalusedByType
import com.blankj.utilcode.util.LogUtils
import es.dmoral.toasty.Toasty

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding

    //如果Remain = 0  就不让点击了
    private var isCanClickBtn = true

    //记录上一个Remain数值
    private var previousRemainNum = 0
    private val startChoiceActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            //此处是跳转的result回调方法
            if (it.resultCode == Activity.RESULT_OK) {
                //发送串口数据
                sendData()
                if (DataBean.singleOrRepeat == SingleOrRepeat.REPEAT) {
                    //更新Repeat
                    val value = DataBean.repeatTime.byteValue.toFloat() / 10
                    binding.tvSingleOrRepeat.text = "Repeat ${value}s"
                }
                //更新Length
                binding.tvLengthNum.text = getLengthValue().toString()
                //更新Pitch
                binding.tvChoicePitch.text = getPitchValue().toString()
            }
        }

    var laserSerialService: LaserSerialService? = null
    private var bound: Boolean = false
    private lateinit var receiver: DataReceiver
    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            LogUtils.d("onServiceConnected")
            laserSerialService = (service as LaserSerialService.MyBinder).service
            bound = true
            //给下位机发送数据，进行初始化
            sendData()
        }

        override fun onServiceDisconnected(name: ComponentName) {
            bound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {
            initView()
            addListener()
            registerReceiver()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initView() {
        //根据currentUseKnife设置中上部的刀头logo
        binding.miv.initImage()
        //根据自动感应开关判断是否显示返回按钮
        if (DataBean.isAutoRecognition == AutoRecognition.OPEN) {
            binding.ivMainBack.visibility = View.GONE
        } else {
            binding.ivMainBack.visibility = View.VISIBLE
        }
        binding.pbMain.isIndeterminate = false
        binding.pbMain.max = 100
        // 设置进度条的颜色为蓝色
        binding.pbMain.progressTintList =
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.progressbar_tint))
        binding.pbMain.progressBackgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.progressbar_bg_tint))
        //设置初始值
        setDefaultData()
    }

    private fun setDefaultData() {
        //设置单向
        DataBean.singleOrRepeat = SingleOrRepeat.SINGLE
        binding.ivSingleOrRepeat.setBackgroundResource(R.drawable.icon_single)
        binding.tvSingleOrRepeat.text = "Single"
        DataBean.repeatTime = RepeatTime.S0_0
        //设置打点长度
        DataBean.distanceLength = DistanceLength.MM25
        binding.tvLengthNum.text = getLengthValue().toString()
        //设置打点模式
        DataBean.pointOrLine = PointOrLine.POINT
        binding.ivMainPointOrLine.setBackgroundResource(R.drawable.icon_point)
        //设置间隔距离
        DataBean.pitch = Pitch.MM1_5
        binding.tvChoicePitch.text = getPitchValue().toString()
        //设置操作源
        DataBean.operatingSource = OperatingSource.BOTH
        //设置能量值
        DataBean.energy = 0x05
        val energy = DataBean.energy.toFloat() / 10
        binding.tvMainEnergy.text = energy.toString()
        //设置待机或者准备
        DataBean.standbyOrReady = StandbyOrReady.STANDBY
        binding.tvStandbyReady.text = STR_STANDBY
        binding.tvStandbyReady.paint.isUnderlineText = true
        //设置打点数的各个值
        setNumber()
    }

    private fun setNumber() {
        binding.tvMainStart.text = GlobalVariable.startNum.toString()
        binding.tvMainCurrent.text = GlobalVariable.currentNum.toString()
        binding.tvMainTotalused.text = getTotalusedByType(GlobalVariable.currentUseKnife).toString()
        //设置进度条
        binding.pbMain.progress =
            ((GlobalVariable.startNum.toFloat() / GlobalVariable.needNum.toFloat()) * 100).toInt()
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    private fun registerReceiver() {
        receiver = DataReceiver(object : ReceiveDataCallback {
            override fun parseSuccess(frameBean: FrameBean) {
                LogUtils.d("frameBean ... $frameBean")
                if (frameBean.frameId and 0x8000 == 0) {
                    //设置手柄图标是否点亮
                    if (DataBean.leftHIFU.press == PRESS.TRUE ||
                        DataBean.middleHIFU.press == PRESS.TRUE ||
                        DataBean.rightHIFU.press == PRESS.TRUE
                    ) {
                        binding.ivMainHand.setBackgroundResource(R.drawable.icon_right_finger_yellow)
                    } else {
                        binding.ivMainHand.setBackgroundResource(R.drawable.icon_right_finger)
                    }
                    //设置脚踏图标是否点亮
                    if (DataBean.footPress == FootPress.TRUE) {
                        binding.ivMainFoot.setBackgroundResource(R.drawable.icon_right_record_yellow)
                    } else {
                        binding.ivMainFoot.setBackgroundResource(R.drawable.icon_right_record)
                    }
                    //处理刀头数据
                    when (GlobalVariable.currentUseKnifePosition) {
                        Position.LEFT -> {
                            setStateByDAta(DataBean.leftHIFU)
                        }

                        Position.MIDDLE -> {
                            setStateByDAta(DataBean.middleHIFU)
                        }

                        else -> {
                            setStateByDAta(DataBean.rightHIFU)
                        }
                    }
                }
            }

            override fun parseFail(message: String) {
                LogUtils.e("************** parseFail **************")
            }
        })
        val fileIntentFilter = IntentFilter()
        fileIntentFilter.addAction("ACTION_SEND_DATA")
        registerReceiver(receiver, fileIntentFilter)
    }

    private fun addListener() {
        binding.ivMainBack.setOnClickListener {
            if (DataBean.standbyOrReady == StandbyOrReady.STANDBY) {
                resetNum()
                startActivity(Intent(this@MainActivity, AwaitActivity::class.java))
                finish()
            } else {
                Toasty.warning(this@MainActivity, REMINDER_STANdBY_STATE, Toast.LENGTH_SHORT, true)
                    .show();
            }
        }

        //更换 single 或者 repeat
        binding.ivSingleOrRepeat.setOnClickListener {
            if (DataBean.standbyOrReady == StandbyOrReady.STANDBY) {
                //当前是单向，需转变为重复模式
                if (DataBean.singleOrRepeat == SingleOrRepeat.SINGLE) {
                    DataBean.singleOrRepeat = SingleOrRepeat.REPEAT
                    //因为single模式repeatTime会设置0x00，切回Repeat模式这里要手动再给repeatTime赋值
                    DataBean.repeatTime = RepeatTime.S0_1
                    binding.ivSingleOrRepeat.setBackgroundResource(R.drawable.icon_repeat)
                    val value = DataBean.repeatTime.byteValue.toFloat() / 10
                    LogUtils.d("value ... $value")
                    binding.tvSingleOrRepeat.text = "Repeat ${value}s"
                }
                //当前是重复模式，需转变为单向
                else {
                    DataBean.singleOrRepeat = SingleOrRepeat.SINGLE
                    //单向模式将repeatTime改成0x00
                    DataBean.repeatTime = RepeatTime.S0_0
                    binding.ivSingleOrRepeat.setBackgroundResource(R.drawable.icon_single)
                    binding.tvSingleOrRepeat.text = "Single"
                }
                sendData()
            } else {
                Toasty.warning(this@MainActivity, REMINDER_STANdBY_STATE, Toast.LENGTH_SHORT, true).show();
            }
        }
        //设置repeat的时间
        binding.tvSingleOrRepeat.setOnClickListener {
            if (DataBean.standbyOrReady == StandbyOrReady.STANDBY) {
                //当前是单向，需转变为重复模式
                if (DataBean.singleOrRepeat == SingleOrRepeat.SINGLE) {
                    DataBean.singleOrRepeat = SingleOrRepeat.REPEAT
                    DataBean.repeatTime = RepeatTime.S0_1
                    binding.ivSingleOrRepeat.setBackgroundResource(R.drawable.icon_repeat)
                    val value = DataBean.repeatTime.byteValue.toFloat() / 10
                    binding.tvSingleOrRepeat.text = "Repeat ${value}s"
                    sendData()
                } else {
                    startChoiceActivity.launch(Intent(this@MainActivity, ChoiceRepeatActivity::class.java))
                }
            } else {
                Toasty.warning(this@MainActivity, REMINDER_STANdBY_STATE, Toast.LENGTH_SHORT, true).show()
            }
        }
        //设置打点或直线长度
        binding.llLength.setOnClickListener {
            if (DataBean.standbyOrReady == StandbyOrReady.STANDBY) {
                startChoiceActivity.launch(Intent(this@MainActivity, ChoiceLengthActivity::class.java))
            } else {
                Toasty.warning(this@MainActivity, REMINDER_STANdBY_STATE, Toast.LENGTH_SHORT, true).show()
            }
        }
        //设置打点或者直线
        binding.llPointOrLine.setOnClickListener {
            if (DataBean.standbyOrReady == StandbyOrReady.STANDBY) {
                //当前打点，改成打直线
                if (DataBean.pointOrLine == PointOrLine.POINT) {
                    binding.ivMainPointOrLine.setBackgroundResource(R.drawable.icon_line)
                    DataBean.pointOrLine = PointOrLine.LINE
                    binding.tvChoicePitch.text = "-"
                }
                //当前打直线，改成打点
                else {
                    binding.ivMainPointOrLine.setBackgroundResource(R.drawable.icon_point)
                    DataBean.pointOrLine = PointOrLine.POINT
                    binding.tvChoicePitch.text = getPitchValue().toString()
                }
                sendData()
            } else {
                Toasty.warning(this@MainActivity, REMINDER_STANdBY_STATE, Toast.LENGTH_SHORT, true).show()
            }
        }
        //设置打点间距
        binding.llPitch.setOnClickListener {
            if (DataBean.standbyOrReady == StandbyOrReady.STANDBY) {
                if (DataBean.pointOrLine == PointOrLine.POINT) {
                    startChoiceActivity.launch(
                        Intent(
                            this@MainActivity,
                            ChoicePitchActivity::class.java
                        )
                    )
                }
            } else {
                Toasty.warning(this@MainActivity, REMINDER_STANdBY_STATE, Toast.LENGTH_SHORT, true).show()
            }
        }
        //进入设置界面
        binding.llSetup.setOnClickListener {
            if (DataBean.standbyOrReady == StandbyOrReady.STANDBY) {
                val intent = Intent(this@MainActivity, SetupAndInfoActivity::class.java)
                intent.putExtra(KEY_FROM_WHERE_TO_SETUP, WHERE_FROM_MAIN)
                startActivity(intent)
                finish()
            } else {
                Toasty.warning(this@MainActivity, REMINDER_STANdBY_STATE, Toast.LENGTH_SHORT, true).show()
            }
        }
        binding.tvStandbyReady.setOnClickListener {
            if (isCanClickBtn) {
                if (DataBean.standbyOrReady == StandbyOrReady.STANDBY) {
                    DataBean.standbyOrReady = StandbyOrReady.READY
                    binding.tvStandbyReady.text = STR_READY
                } else {
                    DataBean.standbyOrReady = StandbyOrReady.STANDBY
                    binding.tvStandbyReady.text = STR_STANDBY
                }
                sendData()
            } else {
                Toasty.warning(this@MainActivity, REMINDER_CAN_NOT_CLICK, Toast.LENGTH_SHORT, true).show()
            }
        }
        binding.ivMainEnergyDown.setOnClickListener {
            if (DataBean.standbyOrReady == StandbyOrReady.STANDBY) {
                var currentValue = binding.tvMainEnergy.text.toString().toDouble()
                if (currentValue > 0.1) {
                    currentValue -= 0.1
                    DataBean.energy = (currentValue * 10).toInt().toByte()
                    //保留小数点后一位
                    binding.tvMainEnergy.text = String.format("%.1f", currentValue)
                    sendData()
                }
            } else {
                Toasty.warning(this@MainActivity, REMINDER_STANdBY_STATE, Toast.LENGTH_SHORT, true).show()
            }
        }
        binding.ivMainEnergyUp.setOnClickListener {
            if (DataBean.standbyOrReady == StandbyOrReady.STANDBY) {
                var currentValue = binding.tvMainEnergy.text.toString().toDouble()
                val maxValue = when (GlobalVariable.currentUseKnife) {
                    Type.KNIFE_15, Type.KNIFE_20, Type.CIRCLE_15 -> {
                        2.0
                    }

                    else -> {
                        3.0
                    }
                }
                if (currentValue < maxValue) {
                    currentValue += 0.1
                    DataBean.energy = (currentValue * 10).toInt().toByte()
                    //保留小数点后一位
                    binding.tvMainEnergy.text = String.format("%.1f", currentValue)
                    sendData()
                }
            } else {
                Toasty.warning(this@MainActivity, REMINDER_STANdBY_STATE, Toast.LENGTH_SHORT, true).show()
            }
        }

        binding.ivMainMinus.setOnClickListener {
            if (DataBean.standbyOrReady == StandbyOrReady.STANDBY) {
                var needValue = binding.tvMainNeed.text.toString().toInt()
                val startNum = binding.tvMainStart.text.toString().toInt()
                if (needValue - 10 > startNum) {
                    needValue -= 10
                    GlobalVariable.needNum = needValue
                    binding.tvMainNeed.text = needValue.toString()
                    binding.pbMain.progress =
                        ((GlobalVariable.startNum.toFloat() / GlobalVariable.needNum.toFloat()) * 100).toInt()
                }
            } else {
                Toasty.warning(this@MainActivity, REMINDER_STANdBY_STATE, Toast.LENGTH_SHORT, true).show()
            }
        }
        binding.ivMainPlus.setOnClickListener {
            if (DataBean.standbyOrReady == StandbyOrReady.STANDBY) {
                var needValue = binding.tvMainNeed.text.toString().toInt()
                needValue += 10
                GlobalVariable.needNum = needValue
                binding.tvMainNeed.text = needValue.toString()
                binding.pbMain.progress =
                    ((GlobalVariable.startNum.toFloat() / GlobalVariable.needNum.toFloat()) * 100).toInt()
            } else {
                Toasty.warning(this@MainActivity, REMINDER_STANdBY_STATE, Toast.LENGTH_SHORT, true).show()
            }
        }
    }

    /**
     * 发送数据
     */
    private fun sendData() {
        with(laserSerialService) {
            val result =
                getFrameDataString(createFrameData(command = Command.WRITE.byteValue.toByte()))
            this?.sendData(result, 0)
        }
    }

    private fun setStateByDAta(hifuBean: HIFUBean) {
        //当前手柄可用
        if (hifuBean.knifeUsable == KnifeUsable.USABLE) {
            //手柄处于摘起状态
            if (hifuBean.knifeState == KnifeState.UP) {
                //无刀头状态跳转
                if (hifuBean.type == Type.NONE || hifuBean.type == Type.EMPTY) {
                    //更新全局变量的值
                    GlobalVariable.currentUseKnife = hifuBean.type
                    jumpToNocartidge()
                } else {
                    //有刀头且处于摘机状态，设置打点数据
                    if (previousRemainNum > hifuBean.remain && previousRemainNum - hifuBean.remain <= LIMIT_DIFFERENCE_POINT) {
                        val differenceValue = previousRemainNum - hifuBean.remain
                        GlobalVariable.startNum += differenceValue
                        GlobalVariable.currentNum += differenceValue
                        var totalUsed = getTotalusedByType(GlobalVariable.currentUseKnife)
                        totalUsed += differenceValue
                        setNumber()
                    }
                    previousRemainNum = hifuBean.remain
                    //如果剩余打点数是0 或者 进度条是100% 则强制设置Standby状态 且给下位机发送数据
                    if (hifuBean.remain == 0 || GlobalVariable.startNum == GlobalVariable.needNum) {
                        isCanClickBtn = false
                        DataBean.standbyOrReady = StandbyOrReady.STANDBY
                        binding.tvStandbyReady.text = STR_STANDBY
                        sendData()
                    } else {
                        isCanClickBtn = true
                    }
                }
            }
            //手柄处于挂机状态
            else {
                //准备状态挂机要给下位机发送数据
                if (DataBean.standbyOrReady == StandbyOrReady.READY) {
                    sendData()
                }
                resetNum()
                jumpToAwait()
            }
        }
        //当前手柄不可用直接跳转到Await
        else {
            jumpToAwait()
        }
    }

    private fun resetNum() {
        GlobalVariable.startNum = 0
        GlobalVariable.needNum = DEFAULT_NEED_POINT_NUMBER
        GlobalVariable.currentNum = 0
    }

    private fun jumpToAwait() {
        val intent = Intent(this@MainActivity, AwaitActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun jumpToNocartidge() {
        val intent = Intent(this@MainActivity, NoCartidgeActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onStart() {
        super.onStart()
        LaserSerialService.action(this@MainActivity, serviceConnection)
    }

    override fun onStop() {
        super.onStop()
        unbindService(serviceConnection)
        bound = false
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}
