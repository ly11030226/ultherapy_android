package com.aimyskin.ultherapy_android.pojo

/**
 * 超声刀单个手柄的实体类
 */
data class HIFUBean(
    var position: Position, var type: Type,
    //剩余发数
    var remain: Int, var knifeState: KnifeState, var knifeUsable: KnifeUsable, var press: PRESS,
    //00-255
    var clientId: Byte
) {
    fun getByteArrayFromRemainNumber(): ByteArray {
        val result = byteArrayOf(2)
        result[0] = (remain shr 8).toByte()
        result[1] = remain.toByte()
        return result
    }

    fun getRemainIntFromByteArray(byteArray: ByteArray): Int {
        var result = 0
        if (byteArray.size == 2) {
            (byteArray[0].toInt() shl 8) or byteArray[1].toInt()
        }
        return result
    }
}


/**
 * 手柄处于机器的位置
 */
enum class Position {
    LEFT, MIDDLE, RIGHT
}

/**
 * 刀头的类型
 * NONE 和  EMPTY 都代表没刀头
 * KNIFE - 刀头  CIRCLE - 炮头
 */
enum class Type(val intValue: Int, val byteValue: Byte) {
    NONE(0x00, 0x00), EMPTY(0x20, 0x20), KNIFE_15(0x21, 0x21), KNIFE_20(0x22, 0x22), KNIFE_30(
        0x23, 0x23
    ),
    KNIFE_45(0x24, 0x24), KNIFE_60(0x25, 0x25), KNIFE_90(0x26, 0x26), KNIFE_130(
        0x27, 0x27
    ),
    CIRCLE_15(0x28, 0x28), CIRCLE_30(0x29, 0x29), CIRCLE_45(0x2a, 0x2a)
}

/**
 * 手柄摘挂机状态
 * 00:挂手柄   01:摘手柄
 * 自动识别开时,下位机上传手柄摘挂机状态
 * 自动识别关时,上位机下发手柄摘挂机状态
 */
enum class KnifeState(val intValue: Int, val byteValue: Byte) {
    UP(0x01, 0x01), DOWN(0x00, 0x00)
}

/**
 * 手柄是否可用
 * 00:没有   01:插入
 */
enum class KnifeUsable(val intValue: Int, val byteValue: Byte) {
    USABLE(0x01, 0x01), UNUSABLE(0x00, 0x00)
}

/**
 * 按键是否按下
 * 01:按下  02:松开
 * 注：脚踏的标识在DataBean里封装
 */
enum class PRESS(val intValue: Int, val byteValue: Byte) {
    TRUE(0x01, 0x01), FALSE(0x02, 0x02)
}