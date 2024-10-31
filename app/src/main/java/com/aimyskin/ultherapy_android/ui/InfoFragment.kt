package com.aimyskin.ultherapy_android.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aimyskin.ultherapy_android.Profile.circle15
import com.aimyskin.ultherapy_android.Profile.circle30
import com.aimyskin.ultherapy_android.Profile.circle45
import com.aimyskin.ultherapy_android.Profile.knife130
import com.aimyskin.ultherapy_android.Profile.knife15
import com.aimyskin.ultherapy_android.Profile.knife20
import com.aimyskin.ultherapy_android.Profile.knife30
import com.aimyskin.ultherapy_android.Profile.knife45
import com.aimyskin.ultherapy_android.Profile.knife60
import com.aimyskin.ultherapy_android.Profile.knife90
import com.aimyskin.ultherapy_android.R
import com.aimyskin.ultherapy_android.base.BaseFragment
import com.aimyskin.ultherapy_android.databinding.FragmentInfoBinding
import com.blankj.utilcode.util.ToastUtils


class InfoFragment : BaseFragment() {
    private var _binding: FragmentInfoBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initView() {
        binding.tvInfoCartridge15Shotcount.text = knife15.toString()
        binding.tvInfoCartridge20Shotcount.text = knife20.toString()
        binding.tvInfoCartridge30Shotcount.text = knife30.toString()
        binding.tvInfoCartridge45Shotcount.text = knife45.toString()
        binding.tvInfoCartridge60Shotcount.text = knife60.toString()
        binding.tvInfoCartridge90Shotcount.text = knife90.toString()
        binding.tvInfoCartridge130Shotcount.text = knife130.toString()
        binding.tvInfoBooster15Shotcount.text = circle15.toString()
        binding.tvInfoBooster30Shotcount.text = circle30.toString()
        binding.tvInfoBooster45Shotcount.text = circle45.toString()

        binding.tvInfoUltrafTotalusedValue.text = (knife15 + knife20 + knife30 + knife45 + knife60 + knife90 + knife130).toString()
        binding.tvInfoBoosterTotalusedValue.text = (circle15 + circle30 + circle45).toString()

        val totalUsed = (knife15 + knife20 + knife30 + knife45 + knife60 + knife90 + knife130 + circle15 + circle30 + circle45).toString()
        val str = getString(R.string.totalused_number)
        str.format(totalUsed)
        binding.tvInfoTotalusedValue.text = str
    }

    override fun addListener() {
        binding.llLeftReset.setOnClickListener {
        }
        binding.llRightReset.setOnClickListener {
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}