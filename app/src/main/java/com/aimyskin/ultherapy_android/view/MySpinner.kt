package com.aimyskin.ultherapy_android.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.aimyskin.ultherapy_android.R
import com.aimyskin.ultherapy_android.pojo.Gender
import com.blankj.utilcode.util.LogUtils

class MySpinner @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleInt: Int = 0) : androidx.appcompat.widget.AppCompatSpinner(context, attributeSet, defStyleInt) {
    init {
        val genderList = arrayOf(Gender.FEMALE, Gender.MALE)
        adapter = ArrayAdapter(context, R.layout.layout_spinner_item, genderList)
        setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus()) {
                setBackgroundResource(R.drawable.shape_register_edittext_bg_focus)
            } else {
                setBackgroundResource(R.drawable.shape_register_edittext_bg)
            }
        }
    }
}
