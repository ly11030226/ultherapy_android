package com.aimyskin.laserserialmodule.responseClassify.ultherapy.interceptor

import javolution.io.Struct

class Frame7E7EStruct: Struct() {
    // 帧头 2字节
    var header = Unsigned16()
    // 长度 2字节 （length = 帧Id + 命令 + 设备地址 + 功能地址 + 数据）
    var addressNum = Unsigned16()
    // 帧Id 2字节
    var frameId = Unsigned16()
    // 指令 1字节
    var instruct = Unsigned8()
    // 设备地址 2字节
    var deviceAddr = Unsigned16()
    // 功能地址 2字节
    var methodAddr = Unsigned16()

    var data = array(arrayOfNulls<Unsigned8>(1024))

    override fun isPacked(): Boolean {
        return true
    }
}