package com.aimyskin.ultherapy_android.ui

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.aimyskin.laserserialmodule.LaserSerialService
import com.aimyskin.miscmodule.utils.ClickSoundPoolUtils
import com.aimyskin.ultherapy_android.R
import com.aimyskin.ultherapy_android.base.BaseActivity
import com.aimyskin.ultherapy_android.base.DataReceiver
import com.aimyskin.ultherapy_android.databinding.ActivityInstrumentSettingBinding
import com.aimyskin.ultherapy_android.inter.ReceiveDataCallback
import com.aimyskin.ultherapy_android.pojo.Command
import com.aimyskin.ultherapy_android.pojo.DataBean
import com.aimyskin.ultherapy_android.pojo.FrameBean
import com.aimyskin.ultherapy_android.pojo.getFrameDataString
import com.aimyskin.ultherapy_android.stateCallback.InstrumentHandleDataCallback
import com.aimyskin.ultherapy_android.util.createFrameData
import com.blankj.utilcode.util.LogUtils
import es.dmoral.toasty.Toasty

/**
 * 一些设置页面
 */
class InstrumentSettingActivity : BaseActivity(), InstrumentHandleDataCallback {
    private lateinit var binding: ActivityInstrumentSettingBinding
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment

    var laserSerialService: LaserSerialService? = null
    private var bound: Boolean = false
    private lateinit var receiver: DataReceiver

    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            LogUtils.d("onServiceConnected")
            laserSerialService = (service as LaserSerialService.MyBinder).service
            bound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            bound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInstrumentSettingBinding.inflate(layoutInflater)
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
        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_instrument) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun addListener() {
        binding.ivInstrumentBack.setOnClickListener {
            ClickSoundPoolUtils.play(it.context, R.raw.click)
            startActivity(Intent(this@InstrumentSettingActivity, IndexActivity::class.java))
            finish()
        }
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    private fun registerReceiver() {
        receiver = DataReceiver(object : ReceiveDataCallback {
            override fun parseSuccess(frameBean: FrameBean) {
                LogUtils.d("frameBean ... $frameBean")
                if (frameBean.frameId and 0x7fff == 0x7fff) {
                    if (DataBean.energyCoefficient == 0x01.toByte()) {
                        Toasty.success(this@InstrumentSettingActivity, "Set the coefficient successfully").show()
                    } else {
                        Toasty.error(this@InstrumentSettingActivity, "Failed to set coefficient").show()
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

    override fun onStart() {
        super.onStart()
        LaserSerialService.action(this@InstrumentSettingActivity, serviceConnection)
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

    override fun setEnergyCoefficient(value: Int) {
        //修改能量系数并保存在DataBean中
        DataBean.energyCoefficient = value.toByte()
        with(laserSerialService) {
            val result = getFrameDataString(createFrameData(command = Command.WRITE.byteValue.toByte()))
            this?.sendData(result, 0)
        }
        //发送其他数据时保证该值是0
        DataBean.energyCoefficient = 0x00
    }
}