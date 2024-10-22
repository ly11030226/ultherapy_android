package com.aimyskin.ultherapy_android.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aimyskin.ultherapy_android.Profile
import com.aimyskin.ultherapy_android.R
import com.aimyskin.ultherapy_android.base.BaseActivity
import com.aimyskin.ultherapy_android.databinding.ActivityAwaitBinding
import com.aimyskin.ultherapy_android.pojo.AutoRecognition
import com.aimyskin.ultherapy_android.pojo.DataBean
import com.aimyskin.ultherapy_android.pojo.Position
import com.aimyskin.ultherapy_android.pojo.Type
import com.aimyskin.ultherapy_android.util.GlobalVariable.currentUseKnife
import com.aimyskin.ultherapy_android.util.GlobalVariable.currentUseKnifePosition
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
            initData()
            addListener()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initData() {
        if (Profile.isAutoRecognition) {
            DataBean.isAutoRecognition = AutoRecognition.OPEN
        } else {
            DataBean.isAutoRecognition = AutoRecognition.CLOSE
        }
    }

    private fun addListener() {
        //自动感应关闭才能进行点击
        if (!Profile.isAutoRecognition) {
            binding.ivLeft.setOnClickListener {
                currentUseKnife = DataBean.leftHIFU.type
                currentUseKnifePosition = DataBean.leftHIFU.position
                jumpToPickupOrMainActivity()
            }

            binding.ivMiddle.setOnClickListener {
                currentUseKnife = DataBean.middleHIFU.type
                currentUseKnifePosition = DataBean.middleHIFU.position
                jumpToPickupOrMainActivity()
            }

            binding.ivRight.setOnClickListener {
                currentUseKnife = DataBean.rightHIFU.type
                currentUseKnifePosition = DataBean.rightHIFU.position
                jumpToPickupOrMainActivity()
            }
        }

        binding.ivHome.setOnClickListener {
            startCircle()
        }
        binding.ivSetting.setOnClickListener {

        }
    }

    /**
     * 跳转到摘机界面或者治疗界面
     */
    private fun jumpToPickupOrMainActivity(){
        if(Profile.isHaveAnimation){
            startActivity(Intent(this@AwaitActivity, PickupActivity::class.java))
        }else{
            startActivity(Intent(this@AwaitActivity, PickupActivity::class.java))
        }
        finish()
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