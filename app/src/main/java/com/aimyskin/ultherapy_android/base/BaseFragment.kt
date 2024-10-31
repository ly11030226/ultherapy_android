package com.aimyskin.ultherapy_android.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {
    val TAG = this.javaClass.simpleName
    abstract fun initView()
    abstract fun addListener()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        addListener()
    }
}