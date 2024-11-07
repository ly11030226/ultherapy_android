package com.aimyskin.ultherapy_android.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.aimyskin.miscmodule.utils.ClickSoundPoolUtils
import com.aimyskin.ultherapy_android.R
import com.aimyskin.ultherapy_android.base.BaseActivity
import com.aimyskin.ultherapy_android.databinding.ActivityChoicePitchBinding
import com.aimyskin.ultherapy_android.pojo.DataBean
import com.aimyskin.ultherapy_android.pojo.Pitch
import com.aimyskin.ultherapy_android.pojo.RepeatTime

/**
 * 选择打点间距的也买你
 */
class ChoicePitchActivity : BaseActivity() {
    private lateinit var binding: ActivityChoicePitchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChoicePitchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {
            initView()
            addListener()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun addListener() {
        binding.ctvPitch1.setOnClickListener {
            ClickSoundPoolUtils.play(it.context, R.raw.click)
            if (DataBean.pitch != Pitch.MM1_5) {
                DataBean.pitch = Pitch.MM1_5
                setAllChoiceTabUnChoice()
                binding.ctvPitch1.setChoice()
                setResult(RESULT_OK)
                finish()
            }
        }

        binding.ctvPitch2.setOnClickListener {
            ClickSoundPoolUtils.play(it.context, R.raw.click)
            if (DataBean.pitch != Pitch.MM1_6) {
                DataBean.pitch = Pitch.MM1_6
                setAllChoiceTabUnChoice()
                binding.ctvPitch2.setChoice()
                setResult(RESULT_OK)
                finish()
            }
        }

        binding.ctvPitch3.setOnClickListener {
            ClickSoundPoolUtils.play(it.context, R.raw.click)
            if (DataBean.pitch != Pitch.MM1_7) {
                DataBean.pitch = Pitch.MM1_7
                setAllChoiceTabUnChoice()
                binding.ctvPitch3.setChoice()
                setResult(RESULT_OK)
                finish()
            }
        }

        binding.ctvPitch4.setOnClickListener {
            ClickSoundPoolUtils.play(it.context, R.raw.click)
            if (DataBean.pitch != Pitch.MM1_8) {
                DataBean.pitch = Pitch.MM1_8
                setAllChoiceTabUnChoice()
                binding.ctvPitch4.setChoice()
                setResult(RESULT_OK)
                finish()
            }
        }
        binding.ctvPitch5.setOnClickListener {
            ClickSoundPoolUtils.play(it.context, R.raw.click)
            if (DataBean.pitch != Pitch.MM1_9) {
                DataBean.pitch = Pitch.MM1_9
                setAllChoiceTabUnChoice()
                binding.ctvPitch5.setChoice()
                setResult(RESULT_OK)
                finish()
            }
        }
        binding.ctvPitch6.setOnClickListener {
            ClickSoundPoolUtils.play(it.context, R.raw.click)
            if (DataBean.pitch != Pitch.MM2_0) {
                DataBean.pitch = Pitch.MM2_0
                setAllChoiceTabUnChoice()
                binding.ctvPitch6.setChoice()
                setResult(RESULT_OK)
                finish()
            }
        }
        binding.tvPitchClose.setOnClickListener {
            ClickSoundPoolUtils.play(it.context, R.raw.click)
            setResult(RESULT_CANCELED)
            finish()
        }

    }

    private fun initView() {
        binding.ctvPitch1.initData("1.5", "mm", R.drawable.pitch_15, false)
        binding.ctvPitch2.initData("1.6", "mm", R.drawable.pitch_16, false)
        binding.ctvPitch3.initData("1.7", "mm", R.drawable.pitch_17, false)
        binding.ctvPitch4.initData("1.8", "mm", R.drawable.pitch_18, false)
        binding.ctvPitch5.initData("1.9", "mm", R.drawable.pitch_19, false)
        binding.ctvPitch6.initData("2.0", "mm", R.drawable.pitch_20, false)

        when (DataBean.pitch) {
            Pitch.MM1_5 -> binding.ctvPitch1.setChoice()
            Pitch.MM1_6 -> binding.ctvPitch2.setChoice()
            Pitch.MM1_7 -> binding.ctvPitch3.setChoice()
            Pitch.MM1_8 -> binding.ctvPitch4.setChoice()
            Pitch.MM1_9 -> binding.ctvPitch5.setChoice()
            Pitch.MM2_0 -> binding.ctvPitch6.setChoice()
            else -> {
            }
        }
    }

    private fun setAllChoiceTabUnChoice() {
        binding.ctvPitch1.setUnChoice()
        binding.ctvPitch2.setUnChoice()
        binding.ctvPitch3.setUnChoice()
        binding.ctvPitch4.setUnChoice()
        binding.ctvPitch5.setUnChoice()
        binding.ctvPitch6.setUnChoice()
    }
}