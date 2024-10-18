package com.aimyskin.miscmodule.utils;


import android.text.TextUtils;

import com.elvishew.xlog.XLog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 时间 日期
 */
public class TimeUtil {

    /**
     * 时间格式转换 秒 --> 分:秒
     *
     * @param time
     * @return
     */
    public static String toTimeMMss(long time) {
        long minute = time / 60;
        long second = time % 60;
        minute %= 60;
        return String.format(Locale.getDefault(), "%02d:%02d", minute, second);
    }

    /**
     * 获取当前时间
     *
     * @param pattern
     * @return
     */
    public static String getCurDate(String pattern) {
        if (TextUtils.isEmpty(pattern)) {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sDateFormat = new SimpleDateFormat(pattern);
        return sDateFormat.format(new Date());
    }

    /**
     * 获取当前的时间戳
     *
     * @return
     */
    public static long getTimeMillis() {
        long timeStampa = System.currentTimeMillis();
        return timeStampa;
    }

    /**
     * 将字符串 转化成时间戳 long
     *
     * @param dateString
     * @param pattern
     * @return
     */
    public static long getStringToDate(String dateString, String pattern) {
        if (TextUtils.isEmpty(dateString)) {
            return 0;
        }
        if (TextUtils.isEmpty(pattern)) {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date date = new Date();
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            XLog.e("getStringToDate: ", e);
        }
        return date.getTime();
    }

    public static String getTimeString(long start) {
        long end = System.currentTimeMillis();
        long interval = end - start;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss_SSS");
        String startStr = dateFormat.format(new Date(start));
        String endStr = dateFormat.format(new Date(end));

        long millisecond = interval % 1000;
        int secondsTotal = (int) (interval / 1000);
        long second = secondsTotal % 60; // 秒数
        long minute = secondsTotal / 60;

        return " time interval:" + String.format("%02d:%02d:%03d", minute, second ,millisecond) + " start time: "+startStr  + " end time: "+endStr;
    }

    /**
     * 时间戳转化为字符串时间
     *
     * @param date_str
     * @param format
     * @return
     */
    public static String timeToDateStamp(String date_str, String format) {
        if (TextUtils.isEmpty(date_str) || TextUtils.equals("0", date_str)) {
            return "";
        }
        if (TextUtils.isEmpty(format)) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        long date = Long.parseLong(date_str);
        return dateFormat.format(new Date(date));
    }

}
