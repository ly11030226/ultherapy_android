package com.aimyskin.ultherapy_android.ui

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.aimyskin.miscmodule.utils.ClickSoundPoolUtils
import com.aimyskin.ultherapy_android.R
import com.aimyskin.ultherapy_android.base.BaseActivity
import com.aimyskin.ultherapy_android.databinding.ActivityChoiceRepeatBinding
import com.aimyskin.ultherapy_android.pojo.DataBean
import com.aimyskin.ultherapy_android.pojo.RepeatTime

/**
 * 选择repeat时长
 */
class ChoiceRepeatActivity : BaseActivity() {
    private lateinit var decorView: View


    private lateinit var binding: ActivityChoiceRepeatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChoiceRepeatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {
            initView()
            addListener()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun addListener() {
        binding.ctvRepeat1.setOnClickListener {
            ClickSoundPoolUtils.play(it.context, R.raw.click)
            if (DataBean.repeatTime != RepeatTime.S0_1) {
                DataBean.repeatTime = RepeatTime.S0_1
                setAllChoiceTabUnChoice()
                binding.ctvRepeat1.setChoice()
                setResult(RESULT_OK)
                finish()
            }
        }
        binding.ctvRepeat2.setOnClickListener {
            ClickSoundPoolUtils.play(it.context, R.raw.click)
            if (DataBean.repeatTime != RepeatTime.S0_2) {
                DataBean.repeatTime = RepeatTime.S0_2
                setAllChoiceTabUnChoice()
                binding.ctvRepeat2.setChoice()
                setResult(RESULT_OK)
                finish()
            }
        }
        binding.ctvRepeat3.setOnClickListener {
            ClickSoundPoolUtils.play(it.context, R.raw.click)
            if (DataBean.repeatTime != RepeatTime.S0_3) {
                DataBean.repeatTime = RepeatTime.S0_3
                setAllChoiceTabUnChoice()
                binding.ctvRepeat3.setChoice()
                setResult(RESULT_OK)
                finish()
            }
        }
        binding.ctvRepeat4.setOnClickListener {
            ClickSoundPoolUtils.play(it.context, R.raw.click)
            if (DataBean.repeatTime != RepeatTime.S0_4) {
                DataBean.repeatTime = RepeatTime.S0_4
                setAllChoiceTabUnChoice()
                binding.ctvRepeat4.setChoice()
                setResult(RESULT_OK)
                finish()
            }
        }
        binding.ctvRepeat5.setOnClickListener {
            ClickSoundPoolUtils.play(it.context, R.raw.click)
            if (DataBean.repeatTime != RepeatTime.S0_5) {
                DataBean.repeatTime = RepeatTime.S0_5
                setAllChoiceTabUnChoice()
                binding.ctvRepeat5.setChoice()
                setResult(RESULT_OK)
                finish()
            }
        }
        binding.ctvRepeat6.setOnClickListener {
            ClickSoundPoolUtils.play(it.context, R.raw.click)
            if (DataBean.repeatTime != RepeatTime.S0_6) {
                DataBean.repeatTime = RepeatTime.S0_6
                setAllChoiceTabUnChoice()
                binding.ctvRepeat6.setChoice()
                setResult(RESULT_OK)
                finish()
            }
        }
        binding.ctvRepeat7.setOnClickListener {
            ClickSoundPoolUtils.play(it.context, R.raw.click)
            if (DataBean.repeatTime != RepeatTime.S0_7) {
                DataBean.repeatTime = RepeatTime.S0_7
                setAllChoiceTabUnChoice()
                binding.ctvRepeat7.setChoice()
                setResult(RESULT_OK)
                finish()
            }
        }
        binding.ctvRepeat8.setOnClickListener {
            ClickSoundPoolUtils.play(it.context, R.raw.click)
            if (DataBean.repeatTime != RepeatTime.S0_8) {
                DataBean.repeatTime = RepeatTime.S0_8
                setAllChoiceTabUnChoice()
                binding.ctvRepeat8.setChoice()
                setResult(RESULT_OK)
                finish()
            }
        }
        binding.ctvRepeat9.setOnClickListener {
            ClickSoundPoolUtils.play(it.context, R.raw.click)
            if (DataBean.repeatTime != RepeatTime.S0_9) {
                DataBean.repeatTime = RepeatTime.S0_9
                setAllChoiceTabUnChoice()
                binding.ctvRepeat9.setChoice()
                setResult(RESULT_OK)
                finish()
            }
        }
        binding.ctvRepeat10.setOnClickListener {
            ClickSoundPoolUtils.play(it.context, R.raw.click)
            if (DataBean.repeatTime != RepeatTime.S1_0) {
                DataBean.repeatTime = RepeatTime.S1_0
                setAllChoiceTabUnChoice()
                binding.ctvRepeat10.setChoice()
                setResult(RESULT_OK)
                finish()
            }
        }

        binding.tvRepeatClose.setOnClickListener {
            ClickSoundPoolUtils.play(it.context, R.raw.click)
            setResult(RESULT_CANCELED)
            finish()
        }
    }

    private fun initView() {
        binding.ctvRepeat1.initData("0.1", "sec", R.drawable.interval_1, false)
        binding.ctvRepeat2.initData("0.2", "sec", R.drawable.interval_2, false)
        binding.ctvRepeat3.initData("0.3", "sec", R.drawable.interval_3, false)
        binding.ctvRepeat4.initData("0.4", "sec", R.drawable.interval_4, false)
        binding.ctvRepeat5.initData("0.5", "sec", R.drawable.interval_5, false)
        binding.ctvRepeat6.initData("0.6", "sec", R.drawable.interval_6, false)
        binding.ctvRepeat7.initData("0.7", "sec", R.drawable.interval_7, false)
        binding.ctvRepeat8.initData("0.8", "sec", R.drawable.interval_8, false)
        binding.ctvRepeat9.initData("0.9", "sec", R.drawable.interval_9, false)
        binding.ctvRepeat10.initData("1.0", "sec", R.drawable.interval_10, false)

        when (DataBean.repeatTime) {
            RepeatTime.S0_1 -> binding.ctvRepeat1.setChoice()
            RepeatTime.S0_2 -> binding.ctvRepeat2.setChoice()
            RepeatTime.S0_3 -> binding.ctvRepeat3.setChoice()
            RepeatTime.S0_4 -> binding.ctvRepeat4.setChoice()
            RepeatTime.S0_5 -> binding.ctvRepeat5.setChoice()
            RepeatTime.S0_6 -> binding.ctvRepeat6.setChoice()
            RepeatTime.S0_7 -> binding.ctvRepeat7.setChoice()
            RepeatTime.S0_8 -> binding.ctvRepeat8.setChoice()
            RepeatTime.S0_9 -> binding.ctvRepeat9.setChoice()
            RepeatTime.S1_0 -> binding.ctvRepeat10.setChoice()
            else -> {
            }
        }
    }

    private fun setAllChoiceTabUnChoice() {
        binding.ctvRepeat1.setUnChoice()
        binding.ctvRepeat2.setUnChoice()
        binding.ctvRepeat3.setUnChoice()
        binding.ctvRepeat4.setUnChoice()
        binding.ctvRepeat5.setUnChoice()
        binding.ctvRepeat6.setUnChoice()
        binding.ctvRepeat7.setUnChoice()
        binding.ctvRepeat8.setUnChoice()
        binding.ctvRepeat9.setUnChoice()
        binding.ctvRepeat10.setUnChoice()
    }
}