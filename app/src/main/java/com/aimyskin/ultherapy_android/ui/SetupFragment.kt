package com.aimyskin.ultherapy_android.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aimyskin.ultherapy_android.Profile
import com.aimyskin.ultherapy_android.R
import com.aimyskin.ultherapy_android.base.BaseFragment
import com.aimyskin.ultherapy_android.databinding.FragmentSetupBinding
import com.aimyskin.ultherapy_android.pojo.AutoRecognition
import com.aimyskin.ultherapy_android.pojo.DataBean
import com.aimyskin.ultherapy_android.pojo.OperatingSource


class SetupFragment : BaseFragment() {
    private var _binding: FragmentSetupBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    }

    override fun addListener() {
        binding.ivSetupRecognitionBtn.setOnClickListener {
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
            if (Profile.isHaveAnimation) {
                Profile.isHaveAnimation = false
                binding.ivSetupAnimationBtn.setBackgroundResource(R.drawable.icon_off)
            } else {
                Profile.isHaveAnimation = true
                binding.ivSetupAnimationBtn.setBackgroundResource(R.drawable.icon_on)
            }
        }

        binding.llSettingBoth.setOnClickListener {
            if (DataBean.operatingSource != OperatingSource.BOTH) {
                binding.ivSetupBoth.setBackgroundResource(R.drawable.icon_setting_choice)
                binding.ivSetupHand.setBackgroundResource(R.drawable.icon_setting_unchoice)
                binding.ivSetupFoot.setBackgroundResource(R.drawable.icon_setting_unchoice)
                DataBean.operatingSource = OperatingSource.BOTH
                needActivitySendData()
            }
        }
        binding.llSettingHand.setOnClickListener {
            if (DataBean.operatingSource != OperatingSource.HAND) {
                binding.ivSetupBoth.setBackgroundResource(R.drawable.icon_setting_unchoice)
                binding.ivSetupHand.setBackgroundResource(R.drawable.icon_setting_choice)
                binding.ivSetupFoot.setBackgroundResource(R.drawable.icon_setting_unchoice)
                DataBean.operatingSource = OperatingSource.HAND
                needActivitySendData()
            }
        }
        binding.llSettingFoot.setOnClickListener {
            if (DataBean.operatingSource != OperatingSource.FOOT) {
                binding.ivSetupBoth.setBackgroundResource(R.drawable.icon_setting_unchoice)
                binding.ivSetupHand.setBackgroundResource(R.drawable.icon_setting_unchoice)
                binding.ivSetupFoot.setBackgroundResource(R.drawable.icon_setting_choice)
                DataBean.operatingSource = OperatingSource.FOOT
                needActivitySendData()
            }
        }
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