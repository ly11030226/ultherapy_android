package com.aimyskin.ultherapy_android.util

import android.graphics.drawable.Drawable
import android.util.TypedValue
import com.aimyskin.ultherapy_android.MyApplication
import com.aimyskin.ultherapy_android.pojo.Command
import com.aimyskin.ultherapy_android.pojo.DataBean
import com.aimyskin.ultherapy_android.pojo.FrameBean
import com.aimyskin.ultherapy_android.pojo.Position
import com.aimyskin.ultherapy_android.pojo.Type
import com.blankj.utilcode.util.LogUtils
import java.io.File

/**
 * 保存一些全局变量
 */
object GlobalVariable {
    //当前用的刀头类型
    var currentUseKnife = Type.NONE

    //当前用的刀头位置
    var currentUseKnifePosition = Position.LEFT
}

fun loadDrawableListFromFolder(folderName: String): MutableList<Drawable> {
    val drawables = mutableListOf<Drawable>()
    try {
        val filesNames: Array<String>? = MyApplication.INSTANCE.assets.list(folderName)
        // 对文件名列表进行排序
        val sortedFileNames = filesNames?.sortedWith(naturalOrder())
        LogUtils.d("sortedFileNames ... $sortedFileNames")
        sortedFileNames?.let {
            for (file in it) {
                val inputStream =
                    MyApplication.INSTANCE.assets.open(folderName + File.separator + file)
                val drawable = Drawable.createFromStream(inputStream, null)
                drawable?.let { d ->
                    drawables.add(d);
                }
                inputStream.close();
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return drawables
}


fun dpToPx(dp: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, dp, MyApplication.INSTANCE.resources.displayMetrics
    )
}

fun pxToDp(px: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_PX, px, MyApplication.INSTANCE.resources.displayMetrics
    )
}

/**
 * 将Byte转成Hex字符串
 * @param withPrefix：布尔值，表示是否要在结果字符串前加上"0x"前缀。
 * @param minLength：结果字符串的最小长度。如果转换后的字符串长度小于这个值，则会在前面补0。
 */
fun Byte.toHexString(withPrefix: Boolean = false, minLength: Int = 2): String {
    val hex = if (withPrefix) "0x%0${minLength}X" else "%0${minLength}X"
    return String.format(hex, this)
}

/**
 * 创建一条帧数据
 * 注意：不含异或码
 */
fun createFrameData(
    frameId: Int = 0x8000,
    command: Byte = Command.READ.byteValue.toByte(),
    deviceAddress: Int = 0x0000,
    functionAddress: Int = 0x0000
): FrameBean {
    val length = 2 + 1 + 2 + 2 + DataBean.packageAsData().size
    return FrameBean(
        0x7e7e,
        length,
        frameId,
        command,
        deviceAddress,
        functionAddress,
        DataBean
    )
}


fun hexStringToByteArray(hexString: String): ByteArray {
    val byteArray = ByteArray(hexString.length / 2)
    for (i in byteArray.indices) {
        val index = i * 2
        val hex = hexString.substring(index, index + 2)
        byteArray[i] = hex.toInt(16).toByte()
    }
    return byteArray
}