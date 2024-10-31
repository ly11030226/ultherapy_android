package com.aimyskin.ultherapy_android.ui

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import com.aimyskin.laserserialmodule.LaserSerialService
import com.aimyskin.ultherapy_android.KEY_FROM_WHERE_TO_SETUP
import com.aimyskin.ultherapy_android.KEY_NO_CARTIDGE_TYPE
import com.aimyskin.ultherapy_android.NO_CARTIDGE_BOOSTER
import com.aimyskin.ultherapy_android.NO_CARTIDGE_HIFU
import com.aimyskin.ultherapy_android.Profile
import com.aimyskin.ultherapy_android.R
import com.aimyskin.ultherapy_android.WHERE_FROM_AWAIT
import com.aimyskin.ultherapy_android.base.BaseActivity
import com.aimyskin.ultherapy_android.base.DataReceiver
import com.aimyskin.ultherapy_android.databinding.ActivityAwaitBinding
import com.aimyskin.ultherapy_android.inter.ReceiveDataCallback
import com.aimyskin.ultherapy_android.pojo.AutoRecognition
import com.aimyskin.ultherapy_android.pojo.Command
import com.aimyskin.ultherapy_android.pojo.DataBean
import com.aimyskin.ultherapy_android.pojo.FrameBean
import com.aimyskin.ultherapy_android.pojo.KnifeState
import com.aimyskin.ultherapy_android.pojo.KnifeUsable
import com.aimyskin.ultherapy_android.pojo.Type
import com.aimyskin.ultherapy_android.pojo.getFrameDataString
import com.aimyskin.ultherapy_android.util.GlobalVariable.currentUseKnife
import com.aimyskin.ultherapy_android.util.GlobalVariable.currentUseKnifePosition
import com.aimyskin.ultherapy_android.util.createFrameData
import com.aimyskin.ultherapy_android.util.getDrawableIdByType
import com.blankj.utilcode.util.LogUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class AwaitActivity : BaseActivity() {
    private lateinit var binding: ActivityAwaitBinding
    var laserSerialService: LaserSerialService? = null
    private var bound: Boolean = false
    private lateinit var receiver: DataReceiver

    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            LogUtils.d("onServiceConnected")
            laserSerialService = (service as LaserSerialService.MyBinder).service
            bound = true
            //Await页面上来不进行获取下位机数据，因为Splash已经获取了
//            with(laserSerialService) {
//                val result = getFrameDataString(createFrameData())
//                this?.sendData(result, 0)
//            }
        }

        override fun onServiceDisconnected(name: ComponentName) {
            bound = false
        }
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAwaitBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {
            initData()
            addListener()
            registerReceiver()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    private fun registerReceiver() {
        receiver = DataReceiver(object : ReceiveDataCallback {
            override fun parseSuccess(frameBean: FrameBean) {
                LogUtils.d("frameBean ... $frameBean")
                initData()
            }

            override fun parseFail(message: String) {
                LogUtils.e("************** parseFail **************")
            }
        })
        val fileIntentFilter = IntentFilter()
        fileIntentFilter.addAction("ACTION_SEND_DATA")
        registerReceiver(receiver, fileIntentFilter)
    }

    override fun onStart() {
        super.onStart()
        LaserSerialService.action(this@AwaitActivity, serviceConnection)
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

    private fun initData() {
        if (Profile.isAutoRecognition) {
            DataBean.isAutoRecognition = AutoRecognition.OPEN
        } else {
            DataBean.isAutoRecognition = AutoRecognition.CLOSE
        }
        initLeft()
        initMiddle()
        initRight()
    }

    private fun initLeft() {
        if (DataBean.leftHIFU.knifeUsable == KnifeUsable.USABLE) {
            binding.flLeft.visibility = View.VISIBLE
            if (DataBean.leftHIFU.knifeState == KnifeState.UP && DataBean.isAutoRecognition == AutoRecognition.OPEN) {
                currentUseKnife = DataBean.leftHIFU.type
                currentUseKnifePosition = DataBean.leftHIFU.position
                jumpToNext()
            }
            val leftDrawableId = getDrawableIdByType(DataBean.leftHIFU.type)
            Glide.with(this).asGif().load(leftDrawableId).skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE).into(binding.ivLeft)
        } else {
            binding.flLeft.visibility = View.GONE
        }
    }

    private fun initMiddle() {
        if (DataBean.middleHIFU.knifeUsable == KnifeUsable.USABLE) {
            binding.flMiddle.visibility = View.VISIBLE
            if (DataBean.middleHIFU.knifeState == KnifeState.UP && DataBean.isAutoRecognition == AutoRecognition.OPEN) {
                currentUseKnife = DataBean.middleHIFU.type
                currentUseKnifePosition = DataBean.middleHIFU.position
                jumpToNext()
            }
            val middleDrawableId = getDrawableIdByType(DataBean.middleHIFU.type)
            Glide.with(this).asGif().load(middleDrawableId).skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE).into(binding.ivMiddle)
        } else {
            binding.flMiddle.visibility = View.GONE
        }
    }

    private fun initRight() {
        if (DataBean.rightHIFU.knifeUsable == KnifeUsable.USABLE) {
            binding.flRight.visibility = View.VISIBLE
            if (DataBean.rightHIFU.knifeState == KnifeState.UP && DataBean.isAutoRecognition == AutoRecognition.OPEN) {
                currentUseKnife = DataBean.rightHIFU.type
                currentUseKnifePosition = DataBean.rightHIFU.position
                jumpToNext()
            }
            val rightDrawableId = getDrawableIdByType(DataBean.rightHIFU.type)
            Glide.with(this).asGif().load(rightDrawableId).skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE).into(binding.ivRight)
        } else {
            binding.flRight.visibility = View.GONE
        }
    }

    private fun jumpToNext() {
        if (Profile.isHaveAnimation) {
            startActivity(Intent(this@AwaitActivity, PickupActivity::class.java))
        } else {
            startActivity(Intent(this@AwaitActivity, MainActivity::class.java))
        }
        finish()
    }

    /**
     * 跳转到摘机界面或者治疗界面
     */
    private fun jumpToPickupOrMainActivity() {
        if (Profile.isHaveAnimation) {
            startActivity(Intent(this@AwaitActivity, PickupActivity::class.java))
        } else {
            startActivity(Intent(this@AwaitActivity, MainActivity::class.java))
        }
        finish()
    }

    private fun addListener() {
        //自动感应关闭才能进行点击
        if (!Profile.isAutoRecognition) {
            binding.flLeft.setOnClickListener {
                currentUseKnife = DataBean.leftHIFU.type
                currentUseKnifePosition = DataBean.leftHIFU.position
                if (DataBean.leftHIFU.type == Type.NONE || DataBean.leftHIFU.type == Type.EMPTY) {
                    //TODO: 这里如果是没刀头状态，目前是Left为炮头 进入无刀头页面显示炮头的闪烁图片，Middle和Right为刀头，进入无刀头页面显示刀头的闪烁图片
                    val intent = Intent(this@AwaitActivity, NoCartidgeActivity::class.java)
                    intent.putExtra(KEY_NO_CARTIDGE_TYPE, NO_CARTIDGE_BOOSTER)
                    startActivity(intent)
                    finish()
                } else {
                    //无感应模式手动填写相应的值
                    DataBean.leftHIFU.knifeState = KnifeState.UP
                    DataBean.middleHIFU.knifeState = KnifeState.DOWN
                    DataBean.rightHIFU.knifeState = KnifeState.DOWN
                    jumpToPickupOrMainActivity()
                }
            }

            binding.flMiddle.setOnClickListener {
                currentUseKnife = DataBean.middleHIFU.type
                currentUseKnifePosition = DataBean.middleHIFU.position
                if (DataBean.middleHIFU.type == Type.NONE || DataBean.middleHIFU.type == Type.EMPTY) {
                    val intent = Intent(this@AwaitActivity, NoCartidgeActivity::class.java)
                    intent.putExtra(KEY_NO_CARTIDGE_TYPE, NO_CARTIDGE_HIFU)
                    startActivity(intent)
                    finish()
                }else{
                    //无感应模式手动填写相应的值
                    DataBean.leftHIFU.knifeState = KnifeState.DOWN
                    DataBean.middleHIFU.knifeState = KnifeState.UP
                    DataBean.rightHIFU.knifeState = KnifeState.DOWN
                    jumpToPickupOrMainActivity()
                }
            }

            binding.flRight.setOnClickListener {
                currentUseKnife = DataBean.rightHIFU.type
                currentUseKnifePosition = DataBean.rightHIFU.position
                if (DataBean.rightHIFU.type == Type.NONE || DataBean.rightHIFU.type == Type.EMPTY) {
                    val intent = Intent(this@AwaitActivity, NoCartidgeActivity::class.java)
                    intent.putExtra(KEY_NO_CARTIDGE_TYPE, NO_CARTIDGE_HIFU)
                    startActivity(intent)
                    finish()
                }else{
                    //无感应模式手动填写相应的值
                    DataBean.leftHIFU.knifeState = KnifeState.DOWN
                    DataBean.middleHIFU.knifeState = KnifeState.DOWN
                    DataBean.rightHIFU.knifeState = KnifeState.UP
                    jumpToPickupOrMainActivity()
                }
            }
        }

        binding.ivHome.setOnClickListener {
            startActivity(Intent(this@AwaitActivity, IndexActivity::class.java))
            finish()
        }
        binding.ivSetting.setOnClickListener {
            val intent = Intent(this@AwaitActivity, SetupAndInfoActivity::class.java)
            intent.putExtra(KEY_FROM_WHERE_TO_SETUP, WHERE_FROM_AWAIT)
            startActivity(intent)
            finish()
        }
    }
}