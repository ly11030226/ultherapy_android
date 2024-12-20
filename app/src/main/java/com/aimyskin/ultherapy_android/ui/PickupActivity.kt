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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

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
            initView()
            handler.postDelayed({
                    startActivity(Intent(this@PickupActivity, MainActivity::class.java))
                    finish()
                }, 6300)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initView() {
        when (currentUseKnife) {
            Type.KNIFE_15 ->
                Glide.with(this).asGif().load(R.drawable.pickup_k15).skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE).into(binding.pickupIv)

            Type.KNIFE_20 ->
                Glide.with(this).asGif().load(R.drawable.pickup_k20).skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE).into(binding.pickupIv)

            Type.KNIFE_30 ->
                Glide.with(this).asGif().load(R.drawable.pickup_k30).skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE).into(binding.pickupIv)

            Type.KNIFE_45 ->
                Glide.with(this).asGif().load(R.drawable.pickup_k45).skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE).into(binding.pickupIv)

            Type.KNIFE_60 ->
                Glide.with(this).asGif().load(R.drawable.pickup_k60).skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE).into(binding.pickupIv)

            Type.KNIFE_90 ->
                Glide.with(this).asGif().load(R.drawable.pickup_k90).skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE).into(binding.pickupIv)

            Type.KNIFE_130 ->
                Glide.with(this).asGif().load(R.drawable.pickup_k130).skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE).into(binding.pickupIv)

            Type.CIRCLE_15 ->
                Glide.with(this).asGif().load(R.drawable.pickup_c15).skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE).into(binding.pickupIv)
            Type.CIRCLE_30 ->
                Glide.with(this).asGif().load(R.drawable.pickup_c30).skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE).into(binding.pickupIv)
            Type.CIRCLE_45 ->
                Glide.with(this).asGif().load(R.drawable.pickup_c45).skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE).into(binding.pickupIv)
            else -> {}
        }
    }

    private fun startAnimation() {
        when (currentUseKnife) {
            Type.KNIFE_15 ->
                binding.pickupIv.setBackgroundResource(R.drawable.animation_pickup_k15)

            Type.KNIFE_20 ->
                binding.pickupIv.setBackgroundResource(R.drawable.animation_pickup_k20)

            Type.KNIFE_30 ->
                binding.pickupIv.setBackgroundResource(R.drawable.animation_pickup_k30)

            Type.KNIFE_45 ->
                binding.pickupIv.setBackgroundResource(R.drawable.animation_pickup_k45)

            Type.KNIFE_60 ->
                binding.pickupIv.setBackgroundResource(R.drawable.animation_pickup_k60)

            Type.KNIFE_90 ->
                binding.pickupIv.setBackgroundResource(R.drawable.animation_pickup_k90)

            Type.KNIFE_130 ->
                binding.pickupIv.setBackgroundResource(R.drawable.animation_pickup_k130)

            Type.CIRCLE_15 ->
                binding.pickupIv.setBackgroundResource(R.drawable.animation_pickup_c15)

            Type.CIRCLE_30 ->
                binding.pickupIv.setBackgroundResource(R.drawable.animation_pickup_c30)

            Type.CIRCLE_45 ->
                binding.pickupIv.setBackgroundResource(R.drawable.animation_pickup_c45)

            else -> {}
        }
        val rocketAnimation = binding.pickupIv.background
        if (rocketAnimation is Animatable) {
            rocketAnimation.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}