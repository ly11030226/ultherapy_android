package com.aimyskin.ultherapy_android.inter

import com.aimyskin.ultherapy_android.pojo.FrameBean

/**
 * 接收到串口数据后的回调
 */
interface ReceiveDataCallback {
    fun parseSuccess(frameBean: FrameBean)
    fun parseFail(message: String)
}