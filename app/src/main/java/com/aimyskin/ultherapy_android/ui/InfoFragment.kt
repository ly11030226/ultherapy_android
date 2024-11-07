package com.aimyskin.ultherapy_android.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.afollestad.materialdialogs.MaterialDialog
import com.aimyskin.miscmodule.utils.ClickSoundPoolUtils
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

        binding.tvInfoUltrafTotalusedValue.text = hifuTotal().toString()
        binding.tvInfoBoosterTotalusedValue.text = boosterTotal().toString()

        refreshTotalUsed()
    }

    override fun addListener() {
        binding.llLeftReset.setOnClickListener { v ->
            ClickSoundPoolUtils.play(v.context, R.raw.click)
            context?.let {
                MaterialDialog(it).show {
                    title(R.string.dialog_reminder)
                    message(res = R.string.dialog_is_zero)
                    positiveButton(R.string.dialog_commit) {
                        ClickSoundPoolUtils.play(v.context, R.raw.click)
                        knife15 = 0
                        knife20 = 0
                        knife30 = 0
                        knife45 = 0
                        knife60 = 0
                        knife90 = 0
                        knife130 = 0

                        initView()
                    }
                    negativeButton(R.string.dialog_cancel) {
                        ClickSoundPoolUtils.play(v.context, R.raw.click)
                    }
                }
            }
        }
        binding.llRightReset.setOnClickListener { v ->
            ClickSoundPoolUtils.play(v.context, R.raw.click)
            context?.let {
                MaterialDialog(it).show {
                    title(R.string.dialog_reminder)
                    message(res = R.string.dialog_is_zero)
                    positiveButton(R.string.dialog_commit) {
                        ClickSoundPoolUtils.play(v.context, R.raw.click)
                        circle15 = 0
                        circle30 = 0
                        circle45 = 0
                        binding.tvInfoBoosterTotalusedValue.text = boosterTotal().toString()
                        refreshTotalUsed()
                    }
                    negativeButton(R.string.dialog_cancel) {
                        ClickSoundPoolUtils.play(v.context, R.raw.click)
                    }
                }
            }
        }
    }

    override fun createObserver() {
    }

    override fun initData() {
    }

    private fun hifuTotal(): Int {
        return knife15 + knife20 + knife30 + knife45 + knife60 + knife90 + knife130
    }

    private fun boosterTotal(): Int {
        return circle15 + circle30 + circle45
    }

    private fun refreshTotalUsed() {
        val totalUsed = (hifuTotal() + boosterTotal()).toString()
        val str = getString(R.string.totalused_number)
        val result = String.format(str, totalUsed)
        binding.tvInfoTotalusedValue.text = result
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}