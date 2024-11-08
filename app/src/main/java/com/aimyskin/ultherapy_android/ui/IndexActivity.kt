package com.aimyskin.ultherapy_android.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import com.aimyskin.miscmodule.Platform.Platform
import com.aimyskin.miscmodule.utils.ClickSoundPoolUtils
import com.aimyskin.resourcemodule.ReadFileUtils
import com.aimyskin.ultherapy_android.R
import com.aimyskin.ultherapy_android.base.BaseActivity
import com.aimyskin.ultherapy_android.databinding.ActivityIndexBinding
import com.blankj.utilcode.util.AppUtils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class IndexActivity : BaseActivity() {
    private lateinit var binding: ActivityIndexBinding
    private val handler = Handler(Looper.getMainLooper())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIndexBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {
            initView()
            addListener()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initView() {
        //开始更新时间
        getCurrentTime()
    }

    private fun getCurrentTime() {
        // 获取当前时间
        val currentTimeMillis = System.currentTimeMillis()
        val date = Date(currentTimeMillis)
        val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
        val time = simpleDateFormat.format(date)
        //如果是个位数前面加0
        val result = time.replace("(\\d)", "0$1")
        binding.tvIndexTime.text = result
        handler.postDelayed({
            getCurrentTime()
        }, 1000)
    }

    private fun addListener() {
        binding.ivIndexHome.setOnClickListener {
            ClickSoundPoolUtils.play(it.context, R.raw.click)
            startActivity(Intent(this@IndexActivity, AwaitActivity::class.java))
            finish()
        }
        binding.ivIndexTreatment.setOnClickListener {
            ClickSoundPoolUtils.play(it.context, R.raw.click)
            startActivity(Intent(this@IndexActivity, AwaitActivity::class.java))
            finish()
        }
        binding.ivIndexUser.setOnClickListener {
            ClickSoundPoolUtils.play(it.context, R.raw.click)
            startActivity(Intent(this@IndexActivity, UserManagerActivity::class.java))
            finish()
        }
        binding.ivIndexVideo.setOnClickListener {
            ClickSoundPoolUtils.play(it.context, R.raw.click)
            // 目标应用的包名
            val video = "android.rk.RockVideoPlayer"
            AppUtils.launchApp(video)
        }
        binding.ivIndexBrowser.setOnClickListener {
            ClickSoundPoolUtils.play(it.context, R.raw.click)
            val browser = "acr.browser.barebones"
            AppUtils.launchApp(browser)
        }
        binding.ivIndexGallery.setOnClickListener {
            ClickSoundPoolUtils.play(it.context, R.raw.click)
            val gallery = "com.android.gallery3d"
            AppUtils.launchApp(gallery)
        }
        binding.ivIndexMusic.setOnClickListener {
            ClickSoundPoolUtils.play(it.context, R.raw.click)
            val packageName = "com.android.music"
            AppUtils.launchApp(packageName)
        }
        binding.ivIndexDownloads.setOnClickListener {
            ClickSoundPoolUtils.play(it.context, R.raw.click)
            val downloads = "com.android.documentsui"
            AppUtils.launchApp(downloads)
        }
        binding.ivIndexFileexplorer.setOnClickListener {
            ClickSoundPoolUtils.play(it.context, R.raw.click)
            val exploer = "com.android.rk"
            AppUtils.launchApp(exploer)
        }
        binding.ivIndexIncrease.setOnClickListener {
            Platform().build(this).hiddenStatusBarAndNavigationBar(false)
        }

        binding.ivIndexIme.setOnClickListener {
            ClickSoundPoolUtils.play(it.context, R.raw.click)
            val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            intent.putExtra("extra_prefs_show_button_bar", true)
            intent.putExtra("extra_prefs_set_back_text", null as String?)
            intent.putExtra("extra_prefs_set_next_text", getString(R.string.str_complete))
            startActivity(intent)
        }

        binding.ivIndexWifi.setOnClickListener {
            ClickSoundPoolUtils.play(it.context, R.raw.click)
            val intentWifi = Intent(Settings.ACTION_WIFI_SETTINGS)
            intentWifi.putExtra("extra_prefs_show_button_bar", true)
            intentWifi.putExtra("extra_prefs_set_back_text", null as String?)
            intentWifi.putExtra("extra_prefs_set_next_text", "Finish")
            startActivity(intentWifi)
        }
        binding.ivIndexSetting.setOnClickListener {
            ClickSoundPoolUtils.play(it.context, R.raw.click)
            startActivity(Intent(this@IndexActivity, InstrumentSettingActivity::class.java))
            finish()
        }
    }
}