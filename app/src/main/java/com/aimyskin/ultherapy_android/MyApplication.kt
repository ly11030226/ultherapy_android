package com.aimyskin.ultherapy_android

import android.app.Application
import android.content.Intent
import bolts.Continuation
import bolts.Task
import com.aimyskin.asentinel.AsentinelReceiver
import com.aimyskin.asentinel.AsentinelReceiver.VerifyListener
import com.aimyskin.asentinel.AsentinelResult
import com.aimyskin.asentinel.AsentinelService
import com.aimyskin.miscmodule.utils.ActivityUtil
import com.aimyskin.ultherapy_android.ui.AwaitActivity
import com.aimyskin.ultherapy_android.ui.ErrorActivity
import com.aimyskin.ultherapy_android.ui.MainActivity
import com.blankj.utilcode.util.LogUtils
import com.chibatching.kotpref.Kotpref
import org.greenrobot.eventbus.EventBus

class MyApplication : Application() {

    companion object {
        lateinit var INSTANCE: MyApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        //初始化SharePreference工具类
        Kotpref.init(this)
        LogUtils.getConfig().globalTag = GLOBAL_TAG
//        LaserSerialService.startLaserSerialService(this,DeviceType.DEVICE_ULTHERAPY)
        startASentinel()
    }

    private fun startASentinel() {
        AsentinelService.startASentinel(this, SerialPortControl.getSerialPortSentinelControl(this))
        AsentinelReceiver.setListener {
//            LogUtils.d("ASentinel result ... ${it.isVerify}")
            val activity = ActivityUtil.getActivity()
            if (activity !is ErrorActivity && !it.isVerify) {
                val intent = Intent(INSTANCE, ErrorActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
//            Task.delay(1000).continueWithTask<Any>(object : Continuation<Any?, Any?> {
//                @Throws(Exception::class)
//                fun then(task: Task<*>?): Any? {
//                    EventBus.getDefault().post(result)
//                    return null
//                }
//            })
        }
    }
}