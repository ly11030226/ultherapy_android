package com.aimyskin.ultherapy_android.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aimyskin.ultherapy_android.R
import com.aimyskin.ultherapy_android.base.BaseActivity
import com.aimyskin.ultherapy_android.databinding.ActivityAwaitBinding
import com.aimyskin.ultherapy_android.pojo.AutoRecognition
import com.aimyskin.ultherapy_android.pojo.DataBean
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class AwaitActivity : BaseActivity() {
    private lateinit var binding: ActivityAwaitBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAwaitBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {
            startCircle()
            addListener()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun addListener() {
        //自动感应关闭才能进行点击
        if (DataBean.isAutoRecognition == AutoRecognition.CLOSE) {
            binding.ivLeft.setOnClickListener {

            }

            binding.ivMiddle.setOnClickListener {

            }

            binding.ivRight.setOnClickListener {

            }
        }

        binding.ivHome.setOnClickListener {
            startCircle()
        }
        binding.ivSetting.setOnClickListener {

        }
    }

    private fun startCircle() {
        Glide.with(this).asGif().load(R.drawable.circle_15).skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE).into(binding.ivLeft)

        Glide.with(this).asGif().load(R.drawable.knife_15).skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE).into(binding.ivRight)

        Glide.with(this).asGif().load(R.drawable.knife_30).skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE).into(binding.ivMiddle)
    }

}