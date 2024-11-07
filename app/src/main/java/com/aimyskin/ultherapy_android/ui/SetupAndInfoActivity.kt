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
import com.aimyskin.ultherapy_android.KEY_FROM_WHERE_TO_SETUP
import com.aimyskin.ultherapy_android.R
import com.aimyskin.ultherapy_android.WHERE_FROM_AWAIT
import com.aimyskin.ultherapy_android.WHERE_FROM_MAIN
import com.aimyskin.ultherapy_android.base.BaseActivity
import com.aimyskin.ultherapy_android.base.DataReceiver
import com.aimyskin.ultherapy_android.databinding.ActivitySetupAndInfoBinding
import com.aimyskin.ultherapy_android.inter.ReceiveDataCallback
import com.aimyskin.ultherapy_android.pojo.Command
import com.aimyskin.ultherapy_android.pojo.FrameBean
import com.aimyskin.ultherapy_android.pojo.getFrameDataString
import com.aimyskin.ultherapy_android.util.createFrameData
import com.blankj.utilcode.util.LogUtils

class SetupAndInfoActivity : BaseActivity(),SetupFragment.ChangeSettingCallBack {
    private lateinit var binding: ActivitySetupAndInfoBinding
    private var isShowSetupFragment = true
    private lateinit var navController: NavController

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
        binding = ActivitySetupAndInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {
            initNav()
            addListener()
            registerReceiver()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private fun initNav() {
        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host_fragment
        ) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun addListener() {
        binding.llSettingBack.setOnClickListener {
            ClickSoundPoolUtils.play(it.context, R.raw.click)
            intent?.let {
                if (it.hasExtra(KEY_FROM_WHERE_TO_SETUP)) {
                    when (it.getStringExtra(KEY_FROM_WHERE_TO_SETUP)) {
                        WHERE_FROM_AWAIT -> {startActivity(Intent(this@SetupAndInfoActivity,AwaitActivity::class.java))
                            finish()
                        }

                        WHERE_FROM_MAIN -> {startActivity(Intent(this@SetupAndInfoActivity,MainActivity::class.java))
                            finish()
                        }
                    }
                }
            }
        }
        binding.tvSettingSetup.setOnClickListener {
            ClickSoundPoolUtils.play(it.context, R.raw.click)
            if (!isShowSetupFragment) {
                binding.ivSettingLine.setBackgroundResource(R.drawable.icon_setup_light)
                isShowSetupFragment = true
                navController.navigate(R.id.action_infoFragment_to_setupFragment)
            }
        }
        binding.tvSettingInfo.setOnClickListener {
            ClickSoundPoolUtils.play(it.context, R.raw.click)
            if (isShowSetupFragment) {
                binding.ivSettingLine.setBackgroundResource(R.drawable.icon_info_light)
                isShowSetupFragment = false
                navController.navigate(R.id.action_setupFragment_to_infoFragment)
            }
        }
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    private fun registerReceiver() {
        receiver = DataReceiver(object : ReceiveDataCallback {
            override fun parseSuccess(frameBean: FrameBean) {
            }

            override fun parseFail(message: String) {
                LogUtils.e("************** SetupAndInfoActivity parseFail **************")
            }
        })
        val fileIntentFilter = IntentFilter()
        fileIntentFilter.addAction("ACTION_SEND_DATA")
        registerReceiver(receiver, fileIntentFilter)
    }

    override fun onStart() {
        super.onStart()
        LaserSerialService.action(this@SetupAndInfoActivity, serviceConnection)
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

    override fun sendData() {
        with(laserSerialService) {
            val result = getFrameDataString(createFrameData(command = Command.WRITE.byteValue.toByte()))
            this?.sendData(result, 0)
        }
    }
}