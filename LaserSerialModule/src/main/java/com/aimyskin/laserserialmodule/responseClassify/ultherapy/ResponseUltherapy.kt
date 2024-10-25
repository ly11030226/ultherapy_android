package com.aimyskin.laserserialmodule.responseClassify.ultherapy

import android.content.Context
import bolts.Task
import com.aimyskin.laserserialmodule.responseClassify.ResponseClassify
import com.aimyskin.laserserialmodule.responseClassify.ultherapy.interceptor.CheckFrame7E7EStructInterceptor
import com.aimyskin.laserserialmodule.responseClassify.ultherapy.interceptor.Frame7E7EStruct
import com.aimyskin.serialasciicrlfimpl.AsciiCRLFPipelineInterceptor
import com.aimyskin.serialasciicrlfimpl.HexToAsciiInterceptor
import com.aimyskin.serialasciicrlfimpl.XorOperateInterceptor
import com.aimyskin.serialmodule.EventListener
import com.aimyskin.serialmodule.RealPipeline
import com.aimyskin.serialmodule.Request
import com.aimyskin.serialmodule.Response
import com.aimyskin.serialmodule.SerialClient
import com.aimyskin.serialmodule.interceptor.LogHexInterceptor
import com.aimyskin.serialmodule.internal.Call
import com.aimyskin.serialmodule.internal.Callback
import com.aimyskin.serialmodule.serialport.SerialPortCommon
import com.blankj.utilcode.util.LogUtils
import okio.ByteString
import okio.ByteString.Companion.decodeHex
import java.nio.ByteBuffer

public class ResponseUltherapy(context: Context) : ResponseClassify {

    private var client: SerialClient? = null
    private var pipeline: RealPipeline? = null

    init {
        initLaserSerial(context)
    }

    private fun initLaserSerial(context: Context) {
        try {
            //先关闭连接 如果需要
            close()
            val serialPortCommon = SerialPortCommon.Builder.aSerialPortCommon()
                .baudrate(SerialPortHandler.getSerialBaudrateSentinelControl(context))
                .serialPath(SerialPortHandler.getSerialPathSentinelControl(context))
                .build()

            client = SerialClient.Builder.aSerialClient()
                .addInterceptor(CheckFrame7E7EStructInterceptor::class.java)
                .addInterceptor(LogHexInterceptor::class.java)
//            .addInterceptor(CRC16Interceptor::class.java)
//            .addInterceptor(LogHexInterceptor::class.java)
                .addInterceptor(XorOperateInterceptor::class.java)
                .addInterceptor(HexToAsciiInterceptor::class.java)
                .addCallInterceptor(AsciiCRLFPipelineInterceptor::class.java)
                .serialPort(serialPortCommon)
                .eventListener(object : EventListener() {
                    override fun callStart(call: Call?) {
                        super.callStart(call)
                    }

                    override fun callEnd(call: Call?) {
                        super.callEnd(call)
                    }

                    override fun callFailed(call: Call?, ioe: java.lang.Exception) {
                        super.callFailed(call, ioe)
                        ioe.printStackTrace()
                    }
                })
                .build()

            pipeline = (client as SerialClient).newPipeline(
                object : Callback {
                    override fun onFailure(call: Call, e: java.lang.Exception) {}

                    @Throws(java.lang.Exception::class)
                    override fun onResponse(call: Call, response: Response) {
                        Task.call<Any>({
                            setResponse(response)
                            null
                        }, Task.UI_THREAD_EXECUTOR)
                    }
                })
            pipeline!!.open()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setResponse(response: Response) {
        val datas = response.data.toByteArray()
        val frame = Frame7E7EStruct()
        frame.setByteBuffer(ByteBuffer.wrap(datas), 0)
//        val instruct = frame.instruct.get().toInt()
//        val address = frame.address.get()
//        val addressNum = frame.addressNum.get()
//        if (instruct == CommandWordEnum.COMMAND_WORD_94.commandWord
//            || instruct == CommandWordEnum.COMMAND_WORD_93.commandWord
//        ) {
//            refreshData(address, addressNum, frame.datas)
//        }
    }

    override fun sendData(data: String, delay: Int) {
        pipeline?.pipeout(Request(data.replace(" ".toRegex(), "").decodeHex()),delay)
    }

    override fun close() {
        try {
            client?.let {
                it.serialPort().close()
                client = null
            }
            pipeline?.let {
                it.close()
                pipeline = null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            LogUtils.e("SerialClient or RealPipeline close exception!!!")
        }
    }
}