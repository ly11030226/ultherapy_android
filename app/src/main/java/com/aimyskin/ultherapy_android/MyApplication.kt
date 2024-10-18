package com.aimyskin.ultherapy_android

import android.app.Application
import com.blankj.utilcode.util.LogUtils
import com.chibatching.kotpref.Kotpref

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
    }
}