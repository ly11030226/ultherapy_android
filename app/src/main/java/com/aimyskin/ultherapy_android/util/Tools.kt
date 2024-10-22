package com.aimyskin.ultherapy_android.util

import android.graphics.drawable.Drawable
import android.util.TypedValue
import com.aimyskin.ultherapy_android.MyApplication
import com.aimyskin.ultherapy_android.pojo.Position
import com.aimyskin.ultherapy_android.pojo.Type
import com.blankj.utilcode.util.LogUtils
import java.io.File
import java.util.Arrays

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