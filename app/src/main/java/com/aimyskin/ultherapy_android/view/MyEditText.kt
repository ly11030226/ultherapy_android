package com.aimyskin.ultherapy_android.view

import android.content.Context
import android.util.AttributeSet
import com.aimyskin.ultherapy_android.R
import com.blankj.utilcode.util.LogUtils

class MyEditText @JvmOverloads constructor (context: Context, attributeSet: AttributeSet? = null, defaultInt: Int = 0) : androidx.appcompat.widget.AppCompatEditText(context, attributeSet, defaultInt) {
    init {
        setHint(R.string.remind_input_name)
        setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus()) {
                LogUtils.d("EditText has Focus")
                setBackgroundResource(R.drawable.shape_register_edittext_bg_focus)
            } else {
                LogUtils.d("EditText has not Focus")
                setBackgroundResource(R.drawable.shape_register_edittext_bg)
            }
        }
    }
}