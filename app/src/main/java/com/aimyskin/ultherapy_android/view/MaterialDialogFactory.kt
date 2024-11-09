package com.aimyskin.ultherapy_android.view

import android.app.Activity
import android.content.Context
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.aimyskin.miscmodule.utils.ClickSoundPoolUtils
import com.aimyskin.ultherapy_android.R
import com.aimyskin.ultherapy_android.inter.ReminderDialogClickApplyCallback
import com.blankj.utilcode.util.LogUtils

class MaterialDialogFactory {
    companion object {
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            MaterialDialogFactory()
        }
    }

    /**
     * @param reminder 提醒的内容
     */
    fun openReminderDialog(activity: Activity, reminder: String, callback: ReminderDialogClickApplyCallback) {
        val dialog = MaterialDialog(activity).show {
            customView(R.layout.dialog_normal_reminder)
        }
        val customView = dialog.getCustomView()
        val btnApply = customView.findViewById<Button>(R.id.btn_dialog_reminder_apply)
        val btnCancel = customView.findViewById<Button>(R.id.btn_dialog_reminder_cancel)
        val tvContent = customView.findViewById<TextView>(R.id.tv_dialog_reminder_content)

        tvContent.text = reminder

        btnApply.setOnClickListener {
            ClickSoundPoolUtils.play(it.context, R.raw.click)
            dialog.dismiss()
            callback.clickApply()
        }
        btnCancel.setOnClickListener {
            ClickSoundPoolUtils.play(it.context, R.raw.click)
            dialog.dismiss()
        }
        dialog.show()
    }
}