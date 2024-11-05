package com.aimyskin.ultherapy_android.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aimyskin.ultherapy_android.R
import com.aimyskin.ultherapy_android.base.BaseFragment
import com.aimyskin.ultherapy_android.databinding.FragmentUserRecordBinding
import com.aimyskin.ultherapy_android.util.GlobalVariable

class UserRecordFragment : BaseFragment() {
    private var _binding: FragmentUserRecordBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserRecordBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun initView() {
        binding.tvRecordName.text = GlobalVariable.currentUser?.name ?: "xxx"
        binding.tvRecordGender.text = GlobalVariable.currentUser?.gender ?: "xxx"
        binding.tvRecordBirthday.text = GlobalVariable.currentUser?.birth ?: "xxxx/xx/xx"
        binding.tvRecordTelephone.text = GlobalVariable.currentUser?.telephone?:"xxxxxxxxxxxxx"
        binding.tvRecordEmail.text = GlobalVariable.currentUser?.email?:"xxxxxxxxxx"
    }

    override fun addListener() {
    }

    override fun createObserver() {
    }

    override fun initData() {
    }
}