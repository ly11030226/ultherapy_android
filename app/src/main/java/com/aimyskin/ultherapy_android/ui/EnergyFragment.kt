package com.aimyskin.ultherapy_android.ui

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.IntentFilter
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aimyskin.laserserialmodule.LaserSerialService
import com.aimyskin.miscmodule.utils.ClickSoundPoolUtils
import com.aimyskin.ultherapy_android.Profile
import com.aimyskin.ultherapy_android.Profile.energy
import com.aimyskin.ultherapy_android.R
import com.aimyskin.ultherapy_android.base.BaseFragment
import com.aimyskin.ultherapy_android.base.DataReceiver
import com.aimyskin.ultherapy_android.databinding.FragmentEnergyBinding
import com.aimyskin.ultherapy_android.inter.ReceiveDataCallback
import com.aimyskin.ultherapy_android.pojo.FrameBean
import com.blankj.utilcode.util.LogUtils
import com.jaygoo.widget.OnRangeChangedListener
import com.jaygoo.widget.RangeSeekBar

class EnergyFragment : BaseFragment() {
    private var _binding: FragmentEnergyBinding? = null
    private val binding get() = _binding!!
    private var value = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEnergyBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun initView() {
        binding.rsbEnergy.setProgress(energy.toFloat())

        binding.rsbEnergy.leftSeekBar.setIndicatorTextDecimalFormat("0");
        binding.rsbEnergy.leftSeekBar.setIndicatorTextStringFormat("%s%%");
        binding.rsbEnergy.setOnRangeChangedListener(object : OnRangeChangedListener {
            override fun onRangeChanged(view: RangeSeekBar?, leftValue: Float, rightValue: Float, isFromUser: Boolean) {
                value = leftValue.toInt()
            }

            override fun onStartTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {
            }

            override fun onStopTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {
            }

        })
    }

    override fun addListener() {
        binding.btnEnergyApply.setOnClickListener {
            ClickSoundPoolUtils.play(it.context, R.raw.click)
            (activity as InstrumentSettingActivity).setEnergyCoefficient(value)
            Profile.energy = value
        }
    }

    override fun createObserver() {
    }

    override fun initData() {
    }

}