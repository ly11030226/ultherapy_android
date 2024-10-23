package com.aimyskin.ultherapy_android.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.aimyskin.ultherapy_android.databinding.ViewChoiceTabBinding

class ChoiceTabView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defaultStyle: Int = 0
) : FrameLayout(context, attributeSet, defaultStyle) {
    private val binding = ViewChoiceTabBinding.inflate(LayoutInflater.from(context), this, true)

    fun initData(value: String, unit: String, resId:Int,choice: Boolean) {
        binding.tvChoiceTabValue.text = value
        binding.tvChoiceTabUnit.text = unit
        binding.ivChoiceTab.setBackgroundResource(resId)
        if(choice){
            binding.ivChoiceTab.visibility = VISIBLE
        }else{
            binding.ivChoiceTab.visibility = GONE
        }
    }

    fun setUnChoice(){
        binding.ivChoiceTab.visibility = GONE
    }

    fun setChoice(){
        binding.ivChoiceTab.visibility = VISIBLE
    }
}