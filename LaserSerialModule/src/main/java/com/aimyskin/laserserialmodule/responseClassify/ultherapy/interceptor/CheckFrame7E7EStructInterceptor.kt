package com.aimyskin.laserserialmodule.responseClassify.ultherapy.interceptor

import com.aimyskin.laserserialmodule.interceptor.Frame5AA5Struct
import com.aimyskin.laserserialmodule.interceptor.FrameCheck5AA5Interceptor
import com.aimyskin.serialmodule.Response
import com.aimyskin.serialmodule.interceptor.Interceptor
import java.nio.ByteBuffer

class CheckFrame7E7EStructInterceptor :Interceptor{
    companion object{
        var header = 0x7E7E
    }
    override fun isCanceled(): Boolean {
        return false
    }
    override fun cancel() {
    }

    override fun processRequest(response: Response): Response {
        val dataBytes = response.request.data.toByteArray()
        val frameStruct = Frame7E7EStruct()
        frameStruct.setByteBuffer(ByteBuffer.wrap(dataBytes), 0)
        if (frameStruct.header.get() != header) {
            throw Exception("frame header error on processRequest")
        }
        return response
    }

    override fun processResponse(response: Response): Response {
        val dataBytes = response.data.toByteArray()
        val frameStruct = Frame7E7EStruct()
        frameStruct.setByteBuffer(ByteBuffer.wrap(dataBytes), 0)
        if (frameStruct.header.get() != header) {
            throw Exception("frame header error on processResponse")
        }
        return response
    }
}