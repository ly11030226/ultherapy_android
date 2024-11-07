package com.aimyskin.resourcemodule;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 重写textview，用以实现获取资源文本方法修改
 *
 * @author kang
 * created by 2022年10月19日10:27:39
 */
public class KTextView extends TextView {
    public KTextView(Context context) {
        super(context);
    }

    public KTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initData(context, attrs);
    }


    public KTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData(context, attrs);
    }

    public KTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
                        setText(Html.fromHtml(ReadFileUtils.INSTANCE().getString(textResId)));
                    }
                }
                if (resId == android.R.attr.hint) {
                    int textResId = attrs.getAttributeResourceValue(i, -1);
                    if (textResId != -1) {
                        setHint(Html.fromHtml(ReadFileUtils.INSTANCE().getString(textResId)));
                    }
                }
                if (resId == android.R.attr.tag) {
                    int textResId = attrs.getAttributeResourceValue(i, -1);
                    if (textResId != -1) {
                        setTag(Html.fromHtml(ReadFileUtils.INSTANCE().getString(textResId)));
                    }
                }
            }
        }

    }

    public void setText(String text) {
        setText(TextUtils.isEmpty(text) ? text : Html.fromHtml(text));
    }

    public static boolean containsHtmlTags(String input) {
        String htmlPattern = "<.*?>"; // 匹配任何 HTML 标签
        Pattern pattern = Pattern.compile(htmlPattern);
        Matcher matcher = pattern.matcher(input);
        return matcher.find();
    }

}
