package com.aimyskin.ultherapy_android.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.aimyskin.miscmodule.utils.ClickSoundPoolUtils
import com.aimyskin.resourcemodule.ReadFileUtils
import com.aimyskin.ultherapy_android.Profile
import com.aimyskin.ultherapy_android.R
import com.aimyskin.ultherapy_android.base.BaseFragment
import com.aimyskin.ultherapy_android.databinding.FragmentSetupBinding
import com.aimyskin.ultherapy_android.pojo.AutoRecognition
import com.aimyskin.ultherapy_android.pojo.DataBean
import com.aimyskin.ultherapy_android.pojo.OperatingSource
import com.blankj.utilcode.util.LogUtils
import com.jaygoo.widget.OnRangeChangedListener
import com.jaygoo.widget.RangeSeekBar


class SetupFragment : BaseFragment() {
    private lateinit var audioManager: AudioManager
    private var progressAudio = 0
    private var maxAudio = 100

    private var _binding: FragmentSetupBinding? = null
    private val binding get() = _binding!!

    private val startActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            //此处是跳转的result回调方法
            if (it.resultCode == Activity.RESULT_OK) {
                LogUtils.d("ok")
            } else {
                LogUtils.d("no ok")
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSetupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initView() {
        //操作源
        when (DataBean.operatingSource) {
            OperatingSource.BOTH -> {
                binding.ivSetupBoth.setBackgroundResource(R.drawable.icon_setting_choice)
                binding.ivSetupHand.setBackgroundResource(R.drawable.icon_setting_unchoice)
                binding.ivSetupFoot.setBackgroundResource(R.drawable.icon_setting_unchoice)
            }

            OperatingSource.HAND -> {
                binding.ivSetupBoth.setBackgroundResource(R.drawable.icon_setting_unchoice)
                binding.ivSetupHand.setBackgroundResource(R.drawable.icon_setting_choice)
                binding.ivSetupFoot.setBackgroundResource(R.drawable.icon_setting_unchoice)
            }

            else -> {
                binding.ivSetupBoth.setBackgroundResource(R.drawable.icon_setting_unchoice)
                binding.ivSetupHand.setBackgroundResource(R.drawable.icon_setting_unchoice)
                binding.ivSetupFoot.setBackgroundResource(R.drawable.icon_setting_choice)
            }
        }
        //自动感应开关
        if (DataBean.isAutoRecognition == AutoRecognition.OPEN) {
            binding.ivSetupRecognitionBtn.setBackgroundResource(R.drawable.icon_on)
        } else {
            binding.ivSetupRecognitionBtn.setBackgroundResource(R.drawable.icon_off)
        }
        //摘机动画开关
        if (Profile.isHaveAnimation) {
            binding.ivSetupAnimationBtn.setBackgroundResource(R.drawable.icon_on)
        } else {
            binding.ivSetupAnimationBtn.setBackgroundResource(R.drawable.icon_off)
        }

        //音量设置回调
        binding.rsbSetup.setOnRangeChangedListener(object : OnRangeChangedListener {
            override fun onRangeChanged(view: RangeSeekBar?, leftValue: Float, rightValue: Float, isFromUser: Boolean) {
//                LogUtils.d("leftValue ... $leftValue ")
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, leftValue.toInt(), AudioManager.FLAG_PLAY_SOUND)
            }

            override fun onStartTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {
            }

            override fun onStopTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {
            }

        })

        // 音量控制
        audioManager = requireContext().getSystemService(Context.AUDIO_SERVICE) as AudioManager
        if (Settings.System.canWrite(activity)) {
            //获取最大音量
            maxAudio = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
            //获取当前音量
            progressAudio = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
            //设置一遍当前音量
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progressAudio, AudioManager.FLAG_PLAY_SOUND)
            //显示进度条
            val minInterval = maxAudio / 5
            binding.rsbSetup.setRange(0f, maxAudio.toFloat(), minInterval.toFloat());
            binding.rsbSetup.setProgress(progressAudio.toFloat())
//            LogUtils.d("maxAudio ... $maxAudio | progressAudio ... $progressAudio | minInterval ... $minInterval")
        } else {
            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
            intent.data = Uri.parse("package:${activity?.packageName}")
            intent.putExtra("extra_prefs_show_button_bar", true)
            intent.putExtra("extra_prefs_set_back_text", null as String?)
            intent.putExtra("extra_prefs_set_next_text", activity?.getString(R.string.str_complete))
            startActivity.launch(intent)
        }
    }

    override fun addListener() {
        binding.ivSetupRecognitionBtn.setOnClickListener {
            ClickSoundPoolUtils.play(it.context, R.raw.click)
            if (DataBean.isAutoRecognition == AutoRecognition.OPEN) {
                DataBean.isAutoRecognition = AutoRecognition.CLOSE
                binding.ivSetupRecognitionBtn.setBackgroundResource(R.drawable.icon_off)
            } else {
                DataBean.isAutoRecognition = AutoRecognition.OPEN
                binding.ivSetupRecognitionBtn.setBackgroundResource(R.drawable.icon_on)
            }
            needActivitySendData()
        }
        binding.ivSetupAnimationBtn.setOnClickListener {
            ClickSoundPoolUtils.play(it.context, R.raw.click)
            if (Profile.isHaveAnimation) {
                Profile.isHaveAnimation = false
                binding.ivSetupAnimationBtn.setBackgroundResource(R.drawable.icon_off)
            } else {
                Profile.isHaveAnimation = true
                binding.ivSetupAnimationBtn.setBackgroundResource(R.drawable.icon_on)
            }
        }

        binding.llSettingBoth.setOnClickListener {
            ClickSoundPoolUtils.play(it.context, R.raw.click)
            if (DataBean.operatingSource != OperatingSource.BOTH) {
                binding.ivSetupBoth.setBackgroundResource(R.drawable.icon_setting_choice)
                binding.ivSetupHand.setBackgroundResource(R.drawable.icon_setting_unchoice)
                binding.ivSetupFoot.setBackgroundResource(R.drawable.icon_setting_unchoice)
                DataBean.operatingSource = OperatingSource.BOTH
                needActivitySendData()
            }
        }
        binding.llSettingHand.setOnClickListener {
            ClickSoundPoolUtils.play(it.context, R.raw.click)
            if (DataBean.operatingSource != OperatingSource.HAND) {
                binding.ivSetupBoth.setBackgroundResource(R.drawable.icon_setting_unchoice)
                binding.ivSetupHand.setBackgroundResource(R.drawable.icon_setting_choice)
                binding.ivSetupFoot.setBackgroundResource(R.drawable.icon_setting_unchoice)
                DataBean.operatingSource = OperatingSource.HAND
                needActivitySendData()
            }
        }
        binding.llSettingFoot.setOnClickListener {
            ClickSoundPoolUtils.play(it.context, R.raw.click)
            if (DataBean.operatingSource != OperatingSource.FOOT) {
                binding.ivSetupBoth.setBackgroundResource(R.drawable.icon_setting_unchoice)
                binding.ivSetupHand.setBackgroundResource(R.drawable.icon_setting_unchoice)
                binding.ivSetupFoot.setBackgroundResource(R.drawable.icon_setting_choice)
                DataBean.operatingSource = OperatingSource.FOOT
                needActivitySendData()
            }
        }
    }

    override fun createObserver() {
    }

    override fun initData() {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun needActivitySendData() {
        val activity = context as SetupAndInfoActivity
        activity.sendData()
    }

    /**
     * 用于Fragment跟Activity通信
     */
    interface ChangeSettingCallBack {
        fun sendData()
    }
}