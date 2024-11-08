package com.aimyskin.ultherapy_android.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.aimyskin.ultherapy_android.MyException
import com.aimyskin.ultherapy_android.inter.ReceiveDataCallback
import com.aimyskin.ultherapy_android.pojo.AutoRecognition
import com.aimyskin.ultherapy_android.pojo.DataBean
import com.aimyskin.ultherapy_android.pojo.DistanceLength
import com.aimyskin.ultherapy_android.pojo.FootPress
import com.aimyskin.ultherapy_android.pojo.FrameBean
import com.aimyskin.ultherapy_android.pojo.KnifeState
import com.aimyskin.ultherapy_android.pojo.KnifeUsable
import com.aimyskin.ultherapy_android.pojo.OperatingSource
import com.aimyskin.ultherapy_android.pojo.PRESS
import com.aimyskin.ultherapy_android.pojo.Pitch
import com.aimyskin.ultherapy_android.pojo.PointOrLine
import com.aimyskin.ultherapy_android.pojo.RepeatTime
import com.aimyskin.ultherapy_android.pojo.SingleOrRepeat
import com.aimyskin.ultherapy_android.pojo.StandbyOrReady
import com.aimyskin.ultherapy_android.pojo.Type
import com.aimyskin.ultherapy_android.pojo.WriteProtect
import com.blankj.utilcode.util.LogUtils
import java.lang.Exception
import java.nio.ByteBuffer
import java.util.Date

/**
 * 串口数据广播接受类
 */
class DataReceiver constructor(private val callback: ReceiveDataCallback) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent!!.action == "ACTION_SEND_DATA") {
            val dataArray = intent.getByteArrayExtra("KEY_SEND_DATA")
            dataArray?.let {
                try {
                    callback.parseSuccess(parserData(it))
                } catch (me: MyException) {
                    me.printStackTrace()
                    me.message?.let { msg ->
                        callback.parseFail(msg)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    e.message?.let { m ->
                        callback.parseFail(m)
                    }
                }
            }
        }
    }

    /**
     * 解析收到的字节数组
     * 一共64个字节，11个帧数据说明字节，53个data字节
     */
    private fun parserData(byteArray: ByteArray): FrameBean {
//        LogUtils.d("************* start parser data *************")
        // ================= 帧数据中用到的11给固定字节 ================= //
        val header = getIntFromTwoByte(byteArray[0], byteArray[1])
        val length = getIntFromTwoByte(byteArray[2], byteArray[3])
        val frameId = getIntFromTwoByte(byteArray[4], byteArray[5])
//        LogUtils.d("frameId ... $frameId")
        val command = byteArray[6]
        val deviceAddress = getIntFromTwoByte(byteArray[7], byteArray[8])
        val functionAddress = getIntFromTwoByte(byteArray[9], byteArray[10])
        // ================= 剩下的53字节 ================= //
        //版本号: 读取版本号:00 01 00 00 最后三个字节为单片机程序版本号, 表示1.0.0
        DataBean.version[0] = byteArray[11]
        DataBean.version[1] = byteArray[12]
        DataBean.version[2] = byteArray[13]
        DataBean.version[3] = byteArray[14]
        //写保护: 00 写保护开 无法写入 01写保护关 允许写入
        checkWriteProject(byteArray[15])
        //唯一编码: 16字节设备唯一编码
        for (i in 0 until DataBean.uniqueCode.size) {
            DataBean.uniqueCode[i] = byteArray[i + 16]
        }
        //打点或直线的长度
        checkLength(byteArray[32])
        //00:line  01: point
        checkPointOrLine(byteArray[33])
        //00: Single  01:Repeat
        checkSingleOrRepeat(byteArray[34])
        //06: 1.5mm 07: 1.6mm 08: 1.7mm 09: 1.8mm 0a: 1.9mm 0b: 2.0mm
        checkPitch(byteArray[35])
        //00: single 01: 0.1S 02: 0.2S 03: 0.3S 04: 0.4S 05: 0.5S 06: 0.6S 07: 0.7S 08: 0.8S 09: 0.9S 10: 1.0S
        checkRepeat(byteArray[36])
        //能量: 1~30  --> 0.1~3.0
        checkEnergy(byteArray[37])
        //00: both 01: hand 02: foot
        checkOperatingSource(byteArray[38])
        //00: 待机 01:准备
        checkStandbyOrReady(byteArray[39])
        //获取三个手柄的数据
        DataBean.leftHIFU.remain = getIntFromTwoByte(byteArray[40], byteArray[41])
        DataBean.leftHIFU.type = getHIFUType(byteArray[42])
        DataBean.middleHIFU.remain = getIntFromTwoByte(byteArray[43], byteArray[44])
        DataBean.middleHIFU.type = getHIFUType(byteArray[45])
        DataBean.rightHIFU.remain = getIntFromTwoByte(byteArray[46], byteArray[47])
        DataBean.rightHIFU.type = getHIFUType(byteArray[48])
        //手柄摘挂机状态
        DataBean.leftHIFU.knifeState = checkKnifeState(byteArray[49])
        DataBean.middleHIFU.knifeState = checkKnifeState(byteArray[50])
        DataBean.rightHIFU.knifeState = checkKnifeState(byteArray[51])
        //手柄是否插入
        DataBean.leftHIFU.knifeUsable = checkKnifeUsable(byteArray[52])
        DataBean.middleHIFU.knifeUsable = checkKnifeUsable(byteArray[53])
        DataBean.rightHIFU.knifeUsable = checkKnifeUsable(byteArray[54])
        //按键脚踏状态 01:按下  02:松开
        DataBean.leftHIFU.press = checkPressState(byteArray[55])
        DataBean.middleHIFU.press = checkPressState(byteArray[56])
        DataBean.rightHIFU.press = checkPressState(byteArray[57])
        //脚踏状态
        DataBean.footPress = checkFootPressState(byteArray[58])
        //自动识别 00:自动识别关,01自动识别开
        DataBean.isAutoRecognition = checkAutoRecognitionState(byteArray[59])
        //能量系数 0 默认值 不设置 10-200 表示能量值的10%-200% 修改成功返回1,否则返回0
        DataBean.energyCoefficient = byteArray[60]
        DataBean.leftHIFU.clientId = byteArray[61]
        DataBean.middleHIFU.clientId = byteArray[62]
        DataBean.rightHIFU.clientId = byteArray[63]
//        LogUtils.d("************* end parser data *************")
        return FrameBean(header, length, frameId, command, deviceAddress, functionAddress, DataBean)
    }

    private fun getIntFromTwoByte(first: Byte, second: Byte): Int {
        return (first.toInt() shl 8) or (second.toInt() and 0xFF)
    }

    private fun checkWriteProject(byte: Byte) {
        when (byte) {
            WriteProtect.OPEN.byteValue -> {
                DataBean.writeProtect = WriteProtect.OPEN
            }

            WriteProtect.CLOSE.byteValue -> {
                DataBean.writeProtect = WriteProtect.CLOSE
            }

            else -> {
//                throw MyException("写保护数据异常 current value ... $byte")
            }
        }
    }

    private fun checkLength(byte: Byte) {
        when (byte) {
            DistanceLength.MM5.byteValue -> {
                DataBean.distanceLength = DistanceLength.MM5
            }

            DistanceLength.MM10.byteValue -> {
                DataBean.distanceLength = DistanceLength.MM10
            }

            DistanceLength.MM15.byteValue -> {
                DataBean.distanceLength = DistanceLength.MM15
            }

            DistanceLength.MM20.byteValue -> {
                DataBean.distanceLength = DistanceLength.MM20
            }

            DistanceLength.MM25.byteValue -> {
                DataBean.distanceLength = DistanceLength.MM25
            }

            else -> {
                throw MyException("打点或直线的长度异常 current value ... $byte")
            }
        }
    }

    private fun checkPointOrLine(byte: Byte) {
        when (byte) {
            PointOrLine.POINT.byteValue -> {
                DataBean.pointOrLine = PointOrLine.POINT
            }

            PointOrLine.LINE.byteValue -> {
                DataBean.pointOrLine = PointOrLine.LINE
            }

            else -> {
                throw MyException("打点或直线的数据异常 current value ... $byte")
            }
        }
    }

    private fun checkSingleOrRepeat(byte: Byte) {
        when (byte) {
            SingleOrRepeat.SINGLE.byteValue -> {
                DataBean.singleOrRepeat = SingleOrRepeat.SINGLE
            }

            SingleOrRepeat.REPEAT.byteValue -> {
                DataBean.singleOrRepeat = SingleOrRepeat.REPEAT
            }

            else -> {
                throw MyException("单向或重复的数据异常 current value ... $byte")
            }
        }
    }

    private fun checkPitch(byte: Byte) {
        when (byte) {
            Pitch.MM1_5.byteValue -> {
                DataBean.pitch = Pitch.MM1_5
            }

            Pitch.MM1_6.byteValue -> {
                DataBean.pitch = Pitch.MM1_6
            }

            Pitch.MM1_7.byteValue -> {
                DataBean.pitch = Pitch.MM1_7
            }

            Pitch.MM1_8.byteValue -> {
                DataBean.pitch = Pitch.MM1_8
            }

            Pitch.MM1_9.byteValue -> {
                DataBean.pitch = Pitch.MM1_9
            }

            Pitch.MM2_0.byteValue -> {
                DataBean.pitch = Pitch.MM2_0
            }

            else -> {
                throw MyException("点距的数据异常 current value ... $byte")
            }
        }
    }

    private fun checkRepeat(byte: Byte) {
        when (byte) {
            RepeatTime.S0_0.byteValue -> {
                DataBean.repeatTime = RepeatTime.S0_0
            }

            RepeatTime.S0_1.byteValue -> {
                DataBean.repeatTime = RepeatTime.S0_1
            }

            RepeatTime.S0_2.byteValue -> {
                DataBean.repeatTime = RepeatTime.S0_2
            }

            RepeatTime.S0_3.byteValue -> {
                DataBean.repeatTime = RepeatTime.S0_3
            }

            RepeatTime.S0_4.byteValue -> {
                DataBean.repeatTime = RepeatTime.S0_4
            }

            RepeatTime.S0_5.byteValue -> {
                DataBean.repeatTime = RepeatTime.S0_5
            }

            RepeatTime.S0_6.byteValue -> {
                DataBean.repeatTime = RepeatTime.S0_6
            }

            RepeatTime.S0_7.byteValue -> {
                DataBean.repeatTime = RepeatTime.S0_7
            }

            RepeatTime.S0_8.byteValue -> {
                DataBean.repeatTime = RepeatTime.S0_8
            }

            RepeatTime.S0_9.byteValue -> {
                DataBean.repeatTime = RepeatTime.S0_9
            }

            RepeatTime.S1_0.byteValue -> {
                DataBean.repeatTime = RepeatTime.S1_0
            }

            else -> {
                throw MyException("往返时长的数据异常 current value ... $byte")
            }
        }
    }

    private fun checkEnergy(byte: Byte) {
        if (byte < 0 || byte >= 30) {
            throw MyException("能量的数据异常 current value ... $byte")
        }
    }

    private fun checkOperatingSource(byte: Byte) {
        when (byte) {
            OperatingSource.BOTH.byteValue -> {
                DataBean.operatingSource = OperatingSource.BOTH
            }

            OperatingSource.HAND.byteValue -> {
                DataBean.operatingSource = OperatingSource.HAND
            }

            OperatingSource.FOOT.byteValue -> {
                DataBean.operatingSource = OperatingSource.FOOT
            }

            else -> {
//                throw MyException("操作源的数据异常 current value ... $byte")
            }
        }
    }

    private fun checkStandbyOrReady(byte: Byte) {
        when (byte) {
            StandbyOrReady.STANDBY.byteValue -> {
                DataBean.standbyOrReady = StandbyOrReady.STANDBY
            }

            StandbyOrReady.READY.byteValue -> {
                DataBean.standbyOrReady = StandbyOrReady.READY
            }

            else -> {
                throw MyException("待机或准备的数据异常 current value ... $byte")
            }
        }
    }

    private fun getHIFUType(byte: Byte): Type {
        when (byte) {
            Type.KNIFE_15.byteValue -> {
                return Type.KNIFE_15
            }

            Type.KNIFE_20.byteValue -> {
                return Type.KNIFE_20
            }

            Type.KNIFE_30.byteValue -> {
                return Type.KNIFE_30
            }

            Type.KNIFE_45.byteValue -> {
                return Type.KNIFE_45
            }

            Type.KNIFE_60.byteValue -> {
                return Type.KNIFE_60
            }

            Type.KNIFE_90.byteValue -> {
                return Type.KNIFE_90
            }

            Type.KNIFE_130.byteValue -> {
                return Type.KNIFE_130
            }

            Type.CIRCLE_15.byteValue -> {
                return Type.CIRCLE_15
            }

            Type.CIRCLE_30.byteValue -> {
                return Type.CIRCLE_30
            }

            Type.CIRCLE_45.byteValue -> {
                return Type.CIRCLE_45
            }

            Type.NONE.byteValue -> {
                return Type.NONE
            }

            Type.EMPTY.byteValue -> {
                return Type.EMPTY
            }

            else -> {
                throw MyException("刀头类型的数据异常 current value ... $byte")
            }
        }
    }

    private fun checkKnifeState(byte: Byte): KnifeState {
        return when (byte) {
            KnifeState.DOWN.byteValue -> {
                KnifeState.DOWN
            }

            KnifeState.UP.byteValue -> {
                KnifeState.UP
            }

            else -> {
                throw MyException("手柄摘挂机状态的数据异常 current value ... $byte")
            }
        }
    }

    private fun checkKnifeUsable(byte: Byte): KnifeUsable {
        return when (byte) {
            KnifeUsable.USABLE.byteValue -> {
                KnifeUsable.USABLE
            }

            KnifeUsable.UNUSABLE.byteValue -> {
                KnifeUsable.UNUSABLE
            }

            else -> {
                throw MyException("手柄是否插入的数据异常 current value ... $byte")
            }
        }
    }

    private fun checkPressState(byte: Byte): PRESS {
        return when (byte) {
            PRESS.FALSE.byteValue -> {
                PRESS.FALSE
            }

            PRESS.TRUE.byteValue -> {
                PRESS.TRUE
            }

            PRESS.EMPTY.byteValue -> {
                PRESS.EMPTY
            }

            else -> {
                throw MyException("按键状态的数据异常 current value ... $byte")
            }
        }
    }

    private fun checkFootPressState(byte: Byte): FootPress {
        return when (byte) {
            FootPress.FALSE.byteValue -> {
                FootPress.FALSE
            }

            FootPress.TRUE.byteValue -> {
                FootPress.TRUE
            }

            FootPress.EMPTY.byteValue -> {
                FootPress.EMPTY
            }

            else -> {
                throw MyException("脚踏状态的数据异常 current value ... $byte")
            }
        }
    }

    private fun checkAutoRecognitionState(byte: Byte): AutoRecognition {
        return when (byte) {
            AutoRecognition.OPEN.byteValue -> {
                AutoRecognition.OPEN
            }

            AutoRecognition.CLOSE.byteValue -> {
                AutoRecognition.CLOSE
            }

            else -> {
                throw MyException("自动感应状态的数据异常 current value ... $byte")
            }
        }
    }
}