package com.aimyskin.ultherapy_android.base

import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.aimyskin.miscmodule.utils.ActivityStackUtils
import com.aimyskin.miscmodule.utils.ActivityUtil
import com.blankj.utilcode.util.LogUtils
import org.greenrobot.eventbus.EventBus


open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val windowInsetsController = WindowInsetsControllerCompat(window,window.decorView)
        windowInsetsController.hide(WindowInsetsCompat.Type.statusBars())
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
        ActivityStackUtils.getmInstance().addActivity(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityStackUtils.getmInstance().finishActivity(this)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            ActivityUtil.hideStatusBarBottomUIMenu(this)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        //点击空白处 隐藏软键盘
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (event.action == MotionEvent.ACTION_DOWN) {
            if (this.currentFocus != null) {
                if (this.currentFocus!!.windowToken != null) {
                    imm.hideSoftInputFromWindow(
                        this.currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
                    )
                }
            }
        }
        return super.onTouchEvent(event)
    }
}