package com.aimyskin.udiskupgrademodule.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.aimyskin.udiskupgrademodule.R;


/**
 * Toast工具类
 */
public class ToastUtil {
    // 显示Toast
    private static Toast toast;

    public static void showToast(Context context, CharSequence message) {
        showToast(context, false, message);
    }

    public static void showToastCenter(Context context, CharSequence message) {
        showToast(context, true, message);
    }

    public static void showToast(Context context, boolean isCenter, CharSequence message) {
        if (toast != null) {
            toast.cancel();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.toast_mini_module, null);
        ((TextView) view.findViewById(R.id.message)).setText(message);
        toast = new Toast(context);
        if (isCenter) {
            toast.setGravity(Gravity.CENTER, 0, 0); // 将Toast设置在屏幕中央
        }
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void showLongToast(Context context, CharSequence message) {
        showLongToast(context, false, message);
    }

    public static void showLongToastCenter(Context context, CharSequence message) {
        showLongToast(context, true, message);
    }

    public static void showLongToast(Context context, boolean isCenter, CharSequence message) {
        if (toast != null) {
            toast.cancel();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.toast_mini_module, null);
        ((TextView) view.findViewById(R.id.message)).setText(message);
        toast = new Toast(context);
        if (isCenter) {
            toast.setGravity(Gravity.CENTER, 0, 0); // 将Toast设置在屏幕中央
        }
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }
}
