package com.aimyskin.nodeparser.viewModel

import androidx.lifecycle.ViewModel
import com.aimyskin.nodeparser.EventLiveData
import com.aimyskin.ultherapy_android.EventLiveData

/**
 * ViewModel的基类
 */
open class BaseViewModel : ViewModel() {

    val loadingChange: UiLoadingChange by lazy { UiLoadingChange() }

    /**
     * 内置封装好的可通知Activity/fragment 显示隐藏加载框 因为需要跟网络请求显示隐藏loading配套才加的
     */
    inner class UiLoadingChange {
        //显示加载框
        val showDialog by lazy { EventLiveData<String>() }
        //隐藏
        val dismissDialog by lazy { EventLiveData<Boolean>() }
    }

}