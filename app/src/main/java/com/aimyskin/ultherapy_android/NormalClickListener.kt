package com.aimyskin.ultherapy_android

import android.view.View
import android.view.View.OnClickListener
import com.aimyskin.miscmodule.utils.ClickSoundPoolUtils

interface NormalClickListener : OnClickListener {
    fun click(v: View?)
    override fun onClick(v: View?) {
        ClickSoundPoolUtils.play(v?.context, R.raw.click)
        click(v)
    }
}