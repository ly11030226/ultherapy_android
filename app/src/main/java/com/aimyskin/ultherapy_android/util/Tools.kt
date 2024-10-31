package com.aimyskin.ultherapy_android.util

import android.graphics.drawable.Drawable
import android.util.TypedValue
import com.aimyskin.ultherapy_android.DEFAULT_NEED_POINT_NUMBER
import com.aimyskin.ultherapy_android.MyApplication
import com.aimyskin.ultherapy_android.Profile.circle15
import com.aimyskin.ultherapy_android.Profile.circle30
import com.aimyskin.ultherapy_android.Profile.circle45
import com.aimyskin.ultherapy_android.Profile.knife130
import com.aimyskin.ultherapy_android.Profile.knife15
import com.aimyskin.ultherapy_android.Profile.knife20
import com.aimyskin.ultherapy_android.Profile.knife30
import com.aimyskin.ultherapy_android.Profile.knife45
import com.aimyskin.ultherapy_android.Profile.knife60
import com.aimyskin.ultherapy_android.Profile.knife90
import com.aimyskin.ultherapy_android.R
import com.aimyskin.ultherapy_android.pojo.Command
import com.aimyskin.ultherapy_android.pojo.DataBean
import com.aimyskin.ultherapy_android.pojo.DistanceLength
import com.aimyskin.ultherapy_android.pojo.FrameBean
import com.aimyskin.ultherapy_android.pojo.Pitch
import com.aimyskin.ultherapy_android.pojo.Position
import com.aimyskin.ultherapy_android.pojo.Type
import com.blankj.utilcode.util.LogUtils
import com.chibatching.kotpref.Kotpref
import com.chibatching.kotpref.KotprefModel
import java.io.File

/**
 * 保存一些全局变量
 */
object GlobalVariable {
    //当前用的刀头类型
    var currentUseKnife = Type.NONE
    //当前用的刀头位置
    var currentUseKnifePosition = Position.LEFT
    //治疗页面开始打点的数值
    var startNum = 100
    //治疗页面当前打点的数值
    var currentNum = 0
    //治疗页面需要共打点的数量
    var needNum = DEFAULT_NEED_POINT_NUMBER
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
    deviceAddress: Int = 0x0001,
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

fun getDrawableIdByType(type: Type): Int {
    return when (type) {
        Type.KNIFE_15 -> {
            R.drawable.knife_15
        }

        Type.KNIFE_20 -> {
            R.drawable.knife_20
        }

        Type.KNIFE_30 -> {
            R.drawable.knife_30
        }

        Type.KNIFE_45 -> {
            R.drawable.knife_45
        }

        Type.KNIFE_60 -> {
            R.drawable.knife_60
        }

        Type.KNIFE_90 -> {
            R.drawable.knife_90
        }

        Type.KNIFE_130 -> {
            R.drawable.knife_130
        }

        Type.CIRCLE_15 -> {
            R.drawable.circle_15
        }

        Type.CIRCLE_30 -> {
            R.drawable.circle_30
        }

        Type.CIRCLE_45 -> {
            R.drawable.circle_45
        }

        else -> {
            R.drawable.knife_no_cartidge
        }
    }
}

fun getPitchValue(): Float {
    return when (DataBean.pitch) {
        Pitch.MM1_5 -> {
            1.5f
        }

        Pitch.MM1_6 -> {
            1.6f
        }

        Pitch.MM1_7 -> {
            1.7f
        }

        Pitch.MM1_8 -> {
            1.8f
        }

        Pitch.MM1_9 -> {
            1.9f
        }

        Pitch.MM2_0 -> {
            2.0f
        }
    }
}

fun getLengthValue(): Int {
    //更新Length
    return when (DataBean.distanceLength) {
        DistanceLength.MM5 -> 5
        DistanceLength.MM10 -> 10
        DistanceLength.MM15 -> 15
        DistanceLength.MM20 -> 20
        DistanceLength.MM25 -> 25
    }
}

fun getTotalusedByType(type:Type):Int{
    return when (type) {
        Type.KNIFE_15 -> {
            knife15
        }
        Type.KNIFE_20 -> {
           knife20
        }
        Type.KNIFE_30 -> {
            knife30
        }
        Type.KNIFE_45 -> {
            knife45
        }
        Type.KNIFE_60 -> {
            knife60
        }
        Type.KNIFE_90 -> {
            knife90
        }
        Type.KNIFE_130 -> {
            knife130
        }
        Type.CIRCLE_15 -> {
            circle15
        }
        Type.CIRCLE_30 -> {
            circle30
        }
        Type.CIRCLE_45 -> {
            circle45
        }
        else -> {
            0
        }
    }
}
