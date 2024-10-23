package com.aimyskin.ultherapy_android.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.aimyskin.ultherapy_android.R
import com.aimyskin.ultherapy_android.pojo.Type
import com.aimyskin.ultherapy_android.util.GlobalVariable.currentUseKnife
import com.aimyskin.ultherapy_android.util.dpToPx
import com.aimyskin.ultherapy_android.util.pxToDp

class MyImageView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : AppCompatImageView(context, attributeSet, defStyleAttr) {
    fun initImage() {
        when (currentUseKnife) {
            Type.KNIFE_15 ->
                setBackgroundResource(R.drawable.knife15)

            Type.KNIFE_20 ->
                setBackgroundResource(R.drawable.knife20)

            Type.KNIFE_30 ->
                setBackgroundResource(R.drawable.knife30)

            Type.KNIFE_45 ->
                setBackgroundResource(R.drawable.knife45)

            Type.KNIFE_60 ->
                setBackgroundResource(R.drawable.knife60)

            Type.KNIFE_90 ->
                setBackgroundResource(R.drawable.knife90)

            Type.KNIFE_130 ->
                setBackgroundResource(R.drawable.knife130)

            Type.CIRCLE_15 ->
                setBackgroundResource(R.drawable.circle15)

            Type.CIRCLE_30 ->
                setBackgroundResource(R.drawable.circle30)

            Type.CIRCLE_45 ->
                setBackgroundResource(R.drawable.circle45)

            else -> {}
        }
        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var newWidthMeasureSpec: Int = 0
        var newHeightMeasureSpec: Int = 0
        when (currentUseKnife) {
            // 图片 px 526 * 196   实际设置  263 98
            Type.KNIFE_15, Type.KNIFE_20, Type.KNIFE_30, Type.KNIFE_45, Type.KNIFE_60, Type.KNIFE_90, Type.KNIFE_130 -> {
                newWidthMeasureSpec =
                    MeasureSpec.makeMeasureSpec(dpToPx(pxToDp(263f)).toInt(), MeasureSpec.EXACTLY)
                newHeightMeasureSpec =
                    MeasureSpec.makeMeasureSpec(dpToPx(pxToDp(98f)).toInt(), MeasureSpec.EXACTLY)
            }
            // px 354 * 244   实际设置 177 122
            Type.CIRCLE_15, Type.CIRCLE_30, Type.CIRCLE_45 -> {
                newWidthMeasureSpec =
                    MeasureSpec.makeMeasureSpec(dpToPx(pxToDp(177f)).toInt(), MeasureSpec.EXACTLY)
                newHeightMeasureSpec =
                    MeasureSpec.makeMeasureSpec(dpToPx(pxToDp(122f)).toInt(), MeasureSpec.EXACTLY)
            }

            else -> {}
        }
        setMeasuredDimension(newWidthMeasureSpec, newHeightMeasureSpec)
    }
}