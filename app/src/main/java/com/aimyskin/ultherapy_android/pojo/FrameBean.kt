package com.aimyskin.ultherapy_android.pojo

import com.aimyskin.ultherapy_android.util.toHexString
import com.blankj.utilcode.util.LogUtils

/**
 * 帧数据实体类
 */
data class FrameBean(
    val header: Int,
    //数据长度 = 帧ID + 指令 + 设备地址 + 功能地址 + 数据
    val length: Int,
    val frameId: Int,
    val command: Byte,
    val deviceAddress: Int,
    val functionAddress: Int,
    val dataBean: DataBean
)

/**
 * 指令
 * 0x01	写入指令：修改变量值、更改状态、获取运算数据
 * 0x02	读取指令：获取状态
 * 0xFF	执行异常应答指令
 */
enum class Command(val intValue: Int, val byteValue: Int) {
    WRITE(0x01, 0x01),
    READ(0x02, 0x02),
    EXEC(0xFF, 0xFF)
}

fun getFrameDataString(frameBean: FrameBean): String {
    val headerStr = getLow16Bits(frameBean.header)
    val lengthStr = getLow16Bits(frameBean.length)
    val frameIdStr = getLow16Bits(frameBean.frameId)
    val commandStr = frameBean.command.toHexString(minLength = 2)
    val deviceAddressStr = getLow16Bits(frameBean.deviceAddress)
    val functionAddressStr = getLow16Bits(frameBean.functionAddress)
    val dataStr = DataBean.packageAsData().joinToString(separator = "") {"%02x".format(it) }
    val result =
        headerStr + lengthStr + frameIdStr + commandStr + deviceAddressStr + functionAddressStr + dataStr
//    LogUtils.d("headerStr ... $headerStr")
//    LogUtils.d("lengthStr ... $lengthStr")
//    LogUtils.d("frameIdStr ... $frameIdStr")
//    LogUtils.d("commandStr ... $commandStr")
//    LogUtils.d("deviceAddressSTr ... $deviceAddressStr")
//    LogUtils.d("functionAddressStr ... $functionAddressStr")
//    LogUtils.d("dataStr ... $dataStr")
//    LogUtils.d("result ... $result")
    return result
}

fun getLow16Bits(value: Int): String {
    val low16Bits: Int = value and 0xFFFF
    low16Bits.toString(16)
    //%04X指定了格式化输出为4位数的十六进制数，不足位数的前面会补上0
    return String.format("%04X", low16Bits)
}


