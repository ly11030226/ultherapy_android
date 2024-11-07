package com.aimyskin.resourcemodule;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import androidx.annotation.Nullable;

/**
 * 重写EditText，用以实现获取资源文本方法修改
 *
 * @author kang
 * created by 2022年10月26日15:03:13
 */
public class KEditText extends EditText {
    public KEditText(Context context) {
        super(context);
    }

    public KEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData(context, attrs);
    }

    public KEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData(context, attrs);
    }

    public KEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initData(context, attrs);
    }

    private void initData(Context context, @Nullable AttributeSet attrs) {
        if (attrs != null) {
            int count = attrs.getAttributeCount();
            for (int i = 0; i < count; i++) {
                int resId = attrs.getAttributeNameResource(i);
                if (resId == android.R.attr.text) {
                    int textResId = attrs.getAttributeResourceValue(i, -1);
                    if (textResId != -1) {
                        setText(ReadFileUtils.INSTANCE().getString(textResId));
                    }
                }
                if (resId == android.R.attr.hint) {
                    int textResId = attrs.getAttributeResourceValue(i, -1);
                    if (textResId != -1) {
                        setHint(ReadFileUtils.INSTANCE().getString(textResId));
                    }
                }
            }
        }

    }
}
