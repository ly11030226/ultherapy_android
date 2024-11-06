package com.aimyskin.ultherapy_android.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import com.aimyskin.ultherapy_android.base.BaseActivity
import com.aimyskin.ultherapy_android.databinding.ActivityIndexBinding
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
            startActivity(Intent(this@IndexActivity, AwaitActivity::class.java))
            finish()
        }
        binding.ivIndexTreatment.setOnClickListener {
            startActivity(Intent(this@IndexActivity, AwaitActivity::class.java))
            finish()
        }
        binding.ivIndexUser.setOnClickListener {
            startActivity(Intent(this@IndexActivity, UserManagerActivity::class.java))
            finish()
        }
        binding.ivIndexVideo.setOnClickListener {}
        binding.ivIndexBrowser.setOnClickListener {}
        binding.ivIndexWifi.setOnClickListener {
            val intentWifi = Intent(Settings.ACTION_WIFI_SETTINGS)
            intentWifi.putExtra("extra_prefs_show_button_bar", true)
            intentWifi.putExtra("extra_prefs_set_back_text", null as String?)
            intentWifi.putExtra("extra_prefs_set_next_text", "Finish")
            startActivity(intentWifi)
        }
    }
}