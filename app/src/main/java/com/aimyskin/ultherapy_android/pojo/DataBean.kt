package com.aimyskin.ultherapy_android.pojo


/**
 * 串口通信数据实体类
 */
object DataBean {
    //版本号是四个字节 00 01 00 00,后三个字节是单片机版本, 这里是1.0.0
    var version = ByteArray(4) { _ -> 0x00 }
    var writeProtect: WriteProtect = WriteProtect.OPEN

    //16字节的唯一编码
    var uniqueCode = ByteArray(16) { _ -> 0x00 }

    var distanceLength: DistanceLength = DistanceLength.MM25
    var pointOrLine: PointOrLine = PointOrLine.POINT
    var singleOrRepeat: SingleOrRepeat = SingleOrRepeat.SINGLE
    var pitch: Pitch = Pitch.MM1_5
    var repeatTime: RepeatTime = RepeatTime.S0_1

    //能量大小 0.1 ~ 3.0
    var energy: Byte = 0x01
    var operatingSource: OperatingSource = OperatingSource.BOTH
    var standbyOrReady: StandbyOrReady = StandbyOrReady.STANDBY

    var leftHIFU: HIFUBean = HIFUBean(
        Position.LEFT,
        Type.CIRCLE_15,
        1000,
        KnifeState.DOWN,
        KnifeUsable.USABLE,
        PRESS.FALSE,
        0x00
    )
    var middleHIFU: HIFUBean = HIFUBean(
        Position.MIDDLE,
        Type.KNIFE_15,
        1000,
        KnifeState.DOWN,
        KnifeUsable.USABLE,
        PRESS.FALSE,
        0x00
    )
    var rightHIFU: HIFUBean = HIFUBean(
        Position.RIGHT,
        Type.KNIFE_30,
        1000,
        KnifeState.DOWN,
        KnifeUsable.USABLE,
        PRESS.FALSE,
        0x00
    )

    var footPress: FootPress = FootPress.FALSE
    var isAutoRecognition: AutoRecognition = AutoRecognition.CLOSE

    //能量系数，平时默认为0，只有更改的时候才会设置 10-200 表示能量值的10%-200% 修改成功返回1,否则返回0
    var energyCoefficient: Byte = 0x00

    /**
     * 将数据包装成data
     */
    fun packageAsData(): ByteArray {
        val data = version +
                byteArrayOf(writeProtect.byteValue) +
                uniqueCode +

                byteArrayOf(distanceLength.byteValue) +
                byteArrayOf(pointOrLine.byteValue) +
                byteArrayOf(singleOrRepeat.byteValue) +
                byteArrayOf(pitch.byteValue) +
                byteArrayOf(repeatTime.byteValue) +
                byteArrayOf(energy) +
                byteArrayOf(operatingSource.byteValue) +
                byteArrayOf(standbyOrReady.byteValue) +

                byteArrayOf(leftHIFU.getByteArrayFromRemainNumber()[0])+
                byteArrayOf(leftHIFU.getByteArrayFromRemainNumber()[1])+
                byteArrayOf(leftHIFU.type.byteValue) +
                byteArrayOf(middleHIFU.getByteArrayFromRemainNumber()[0])+
                byteArrayOf(middleHIFU.getByteArrayFromRemainNumber()[1])+
                byteArrayOf(middleHIFU.type.byteValue) +
                byteArrayOf(rightHIFU.getByteArrayFromRemainNumber()[0])+
                byteArrayOf(rightHIFU.getByteArrayFromRemainNumber()[1])+
                byteArrayOf(rightHIFU.type.byteValue) +

                byteArrayOf(leftHIFU.knifeState.byteValue) +
                byteArrayOf(middleHIFU.knifeState.byteValue) +
                byteArrayOf(rightHIFU.knifeState.byteValue) +

                byteArrayOf(leftHIFU.knifeUsable.byteValue) +
                byteArrayOf(middleHIFU.knifeUsable.byteValue) +
                byteArrayOf(rightHIFU.knifeUsable.byteValue) +

                byteArrayOf(leftHIFU.press.byteValue) +
                byteArrayOf(middleHIFU.press.byteValue) +
                byteArrayOf(rightHIFU.press.byteValue) +
                byteArrayOf(footPress.byteValue) +

                byteArrayOf(isAutoRecognition.byteValue) +
                byteArrayOf(energyCoefficient) +

                byteArrayOf(leftHIFU.clientId) +
                byteArrayOf(middleHIFU.clientId) +
                byteArrayOf(rightHIFU.clientId)
        return data
    }
}

/**
 * 写保护
 * 00 写保护开 无法写入
 * 01 写保护关 允许写入
 */
enum class WriteProtect(val intValue: Int, val byteValue: Byte) {
    OPEN(0x00, 0x00),
    CLOSE(0x01, 0x01)
}


/**
 * 打点或直线走的距离长度
 */
enum class DistanceLength(val intValue: Int, val byteValue: Byte) {
    MM5(0x01, 0x01),
    MM10(0x02, 0x02),
    MM15(0x03, 0x03),
    MM20(0x04, 0x04),
    MM25(0x05, 0x05)
}

/**
 * 打点或打直线
 * 00:直线   01:点
 */
enum class PointOrLine(val intValue: Int, val byteValue: Byte) {
    LINE(0x00, 0x00),
    POINT(0x01, 0x01)
}

/**
 * 一个来回或者重复
 * 00: Single  01:Repeat
 */
enum class SingleOrRepeat(val intValue: Int, val byteValue: Byte) {
    SINGLE(0x00, 0x00),
    REPEAT(0x01, 0x01)
}

/**
 * 点距
 * 06: 1.5mm   07: 1.6mm    08: 1.7mm    09: 1.8mm   10: 1.9mm    11: 2.0mm
 */
enum class Pitch(val intValue: Int, val byteValue: Byte) {
    MM1_5(0x06, 0x06),
    MM1_6(0x07, 0x07),
    MM1_7(0x08, 0x08),
    MM1_8(0x09, 0x09),
    MM1_9(0x0a, 0x0a),
    MM2_0(0x0b, 0x0b)
}

/**
 * 间隔时间
 * 00: single模式下默认此值
 * 01: 0.1S    02: 0.2S    03: 0.3S    04: 0.4S    05: 0.5S
 * 06: 0.6S    07: 0.7S    08: 0.8S    09: 0.9S    10: 1.0S
 */
enum class RepeatTime(val intValue: Int, val byteValue: Byte) {
    S0_0(0x00, 0x00),
    S0_1(0x01, 0x01),
    S0_2(0x02, 0x02),
    S0_3(0x03, 0x03),
    S0_4(0x04, 0x04),
    S0_5(0x05, 0x05),
    S0_6(0x06, 0x06),
    S0_7(0x07, 0x07),
    S0_8(0x08, 0x08),
    S0_9(0x09, 0x09),
    S1_0(0x0a, 0x0a)
}

/**
 * 操作源
 * 00: both   01: hand   02: foot
 */
enum class OperatingSource(val intValue: Int, val byteValue: Byte) {
    BOTH(0x00, 0x00),
    HAND(0x01, 0x01),
    FOOT(0x02, 0x02)
}

/**
 * 待机或准备
 * 00:待机   01:准备
 */
enum class StandbyOrReady(val intValue: Int, val byteValue: Byte) {
    STANDBY(0x00, 0x00),
    READY(0x01, 0x01)
}

/**
 * 脚踏是否按下
 * 01:踩下  02:松开
 */
enum class FootPress(val intValue: Int, val byteValue: Byte) {
    TRUE(0x01, 0x01),
    FALSE(0x02, 0x02)
}

/**
 * 自动识别功能
 * 00:自动识别关   01自动识别开
 */
enum class AutoRecognition(val intValue: Int, val byteValue: Byte) {
    OPEN(0x01, 0x01),
    CLOSE(0x00, 0x00)
}