package com.aimyskin.ultherapy_android

import android.app.Application
import android.content.Intent
import bolts.Continuation
import bolts.Task
import com.aimyskin.miscmodule.utils.ActivityUtil
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
//        AsentinelService.startASentinel(this, SerialPortControl.getSerialPortSentinelControl(this))
//        AsentinelReceiver.setListener(object : VerifyListener() {
//            fun onResult(result: AsentinelResult) {
//                val activity = ActivityUtil.getActivity()
//                if (activity !is MainActivity && !result.isVerify()) {
//                    val intent = Intent(MyApplication.app, MainActivity::class.java)
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                    startActivity(intent)
//                }
//                Task.delay(1000).continueWithTask<Any>(object : Continuation<Any?, Any?> {
//                    @Throws(Exception::class)
//                    fun then(task: Task<*>?): Any? {
//                        EventBus.getDefault().post(result)
//                        return null
//                    }
//                })
//            }
//        })
    }
}