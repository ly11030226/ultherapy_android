package com.aimyskin.ultherapy_android.ui

import android.content.Intent
import android.graphics.drawable.Animatable
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.aimyskin.ultherapy_android.R
import com.aimyskin.ultherapy_android.base.BaseActivity
import com.aimyskin.ultherapy_android.databinding.ActivityPickupBinding
import com.aimyskin.ultherapy_android.pojo.Type
import com.aimyskin.ultherapy_android.util.GlobalVariable.currentUseKnife
import com.aimyskin.ultherapy_android.util.loadDrawableListFromFolder

/**
 * 摘机动画界面
 */
class PickupActivity : BaseActivity() {
    private lateinit var binding: ActivityPickupBinding
    private val handler: Handler = Handler(Looper.getMainLooper())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPickupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {
            startAnimation()
            //5100 = 34（34张图片） * 150 （duration是150毫秒）
            handler.postDelayed({
                startActivity(Intent(this@PickupActivity, MainActivity::class.java))
                finish()
            }, 5100)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun startAnimation() {
        when(currentUseKnife){
            Type.KNIFE_15->
                binding.pickupIv.setBackgroundResource(R.drawable.animation_pickup_k15)
            Type.KNIFE_20->
                binding.pickupIv.setBackgroundResource(R.drawable.animation_pickup_k20)
            Type.KNIFE_30->
                binding.pickupIv.setBackgroundResource(R.drawable.animation_pickup_k30)
            Type.KNIFE_45->
                binding.pickupIv.setBackgroundResource(R.drawable.animation_pickup_k45)
            Type.KNIFE_60->
                binding.pickupIv.setBackgroundResource(R.drawable.animation_pickup_k60)
            Type.KNIFE_90->
                binding.pickupIv.setBackgroundResource(R.drawable.animation_pickup_k90)
            Type.KNIFE_130->
                binding.pickupIv.setBackgroundResource(R.drawable.animation_pickup_k130)
            Type.CIRCLE_15->
                binding.pickupIv.setBackgroundResource(R.drawable.animation_pickup_c15)
            Type.CIRCLE_30->
                binding.pickupIv.setBackgroundResource(R.drawable.animation_pickup_c30)
            Type.CIRCLE_45->
                binding.pickupIv.setBackgroundResource(R.drawable.animation_pickup_c45)
            else -> {}
        }
        val rocketAnimation = binding.pickupIv.background
        if (rocketAnimation is Animatable) {
            rocketAnimation.start()
        }
    }
}