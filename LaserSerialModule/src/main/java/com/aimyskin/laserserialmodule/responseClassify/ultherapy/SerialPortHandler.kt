package com.aimyskin.laserserialmodule.responseClassify.ultherapy

import android.content.Context
import com.aimyskin.miscmodule.Platform.Platform
import com.aimyskin.miscmodule.Platform.PlatformRK3399

/**
 * 处理串口数据的操作类
 */
class SerialPortHandler {
    companion object{
        fun getSerialPathSentinelControl(context: Context): String {
            val serialPath: String
            val platform = Platform().build(context)
            serialPath = if (platform is PlatformRK3399) {
                "/dev/ttyS0"
            } else {
                "/dev/ttyS0"
            }
            return serialPath
        }

        fun getSerialBaudrateSentinelControl(context: Context): Int {
            val baudrate: Int
            val platform = Platform().build(context)
            baudrate = if (platform is PlatformRK3399) {
                115200
            } else {
                115200
            }
            return baudrate
        }
    }
}