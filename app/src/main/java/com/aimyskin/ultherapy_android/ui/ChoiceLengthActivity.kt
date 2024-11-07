package com.aimyskin.ultherapy_android.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.aimyskin.miscmodule.utils.ClickSoundPoolUtils
import com.aimyskin.ultherapy_android.R
import com.aimyskin.ultherapy_android.base.BaseActivity
import com.aimyskin.ultherapy_android.databinding.ActivityChoiceLengthBinding
import com.aimyskin.ultherapy_android.pojo.DataBean
import com.aimyskin.ultherapy_android.pojo.DistanceLength
import com.aimyskin.ultherapy_android.pojo.Pitch

/**
 * 选择打点或者打直线的长度
 */
class ChoiceLengthActivity : BaseActivity() {
    private lateinit var binding: ActivityChoiceLengthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChoiceLengthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {
            initView()
            addListener()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun addListener() {
        binding.ctvLength1.setOnClickListener {
            ClickSoundPoolUtils.play(it.context, R.raw.click)
            if (DataBean.distanceLength != DistanceLength.MM5) {
                DataBean.distanceLength = DistanceLength.MM5
                setAllChoiceTabUnChoice()
                binding.ctvLength1.setChoice()
                setResult(RESULT_OK)
                finish()
            }
        }

        binding.ctvLength2.setOnClickListener {
            ClickSoundPoolUtils.play(it.context, R.raw.click)
            if (DataBean.distanceLength != DistanceLength.MM10) {
                DataBean.distanceLength = DistanceLength.MM10
                setAllChoiceTabUnChoice()
                binding.ctvLength2.setChoice()
                setResult(RESULT_OK)
                finish()
            }
        }

        binding.ctvLength3.setOnClickListener {
            ClickSoundPoolUtils.play(it.context, R.raw.click)
            if (DataBean.distanceLength != DistanceLength.MM15) {
                DataBean.distanceLength = DistanceLength.MM15
                setAllChoiceTabUnChoice()
                binding.ctvLength3.setChoice()
                setResult(RESULT_OK)
                finish()
            }
        }

        binding.ctvLength4.setOnClickListener {
            ClickSoundPoolUtils.play(it.context, R.raw.click)
            if (DataBean.distanceLength != DistanceLength.MM20) {
                DataBean.distanceLength = DistanceLength.MM20
                setAllChoiceTabUnChoice()
                binding.ctvLength4.setChoice()
                setResult(RESULT_OK)
                finish()
            }
        }

        binding.ctvLength5.setOnClickListener {
            ClickSoundPoolUtils.play(it.context, R.raw.click)
            if (DataBean.distanceLength != DistanceLength.MM25) {
                DataBean.distanceLength = DistanceLength.MM25
                setAllChoiceTabUnChoice()
                binding.ctvLength5.setChoice()
                setResult(RESULT_OK)
                finish()
            }
        }

        binding.tvLengthClose.setOnClickListener {
            ClickSoundPoolUtils.play(it.context, R.raw.click)
            setResult(RESULT_CANCELED)
            finish()
        }
    }

    private fun initView() {
        binding.ctvLength1.initData("5", "mm", R.drawable.length_5, false)
        binding.ctvLength2.initData("10", "mm", R.drawable.length_10, false)
        binding.ctvLength3.initData("15", "mm", R.drawable.length_15, false)
        binding.ctvLength4.initData("20", "mm", R.drawable.length_20, false)
        binding.ctvLength5.initData("25", "mm", R.drawable.length_25, false)

        when (DataBean.distanceLength) {
            DistanceLength.MM5 -> binding.ctvLength1.setChoice()
            DistanceLength.MM10 -> binding.ctvLength2.setChoice()
            DistanceLength.MM15 -> binding.ctvLength3.setChoice()
            DistanceLength.MM20 -> binding.ctvLength4.setChoice()
            DistanceLength.MM25 -> binding.ctvLength5.setChoice()
            else -> {
            }
        }
    }

    private fun setAllChoiceTabUnChoice() {
        binding.ctvLength1.setUnChoice()
        binding.ctvLength2.setUnChoice()
        binding.ctvLength3.setUnChoice()
        binding.ctvLength4.setUnChoice()
        binding.ctvLength5.setUnChoice()
    }

}