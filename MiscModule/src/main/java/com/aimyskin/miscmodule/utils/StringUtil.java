package com.aimyskin.miscmodule.utils;

import android.text.TextUtils;

public class StringUtil {

    /**
     * 判断字符串是否相同
     *
     * @param string1
     * @param string2
     * @return
     */
    public static boolean equalsString(String string1, String string2) {
        if (string1 == null) return false;
        if (string2 == null) return false;
        return string1.equals(string2);
    }

    public static boolean equalsIgnoreCase(String string1, String string2) {
        if (TextUtils.isEmpty(string1)) return false;
        if (TextUtils.isEmpty(string2)) return false;
        return string1.equalsIgnoreCase(string2);
    }

    public static String maskPhoneNumber(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber)) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        if (phoneNumber.length() > 4) {
            sb.append(phoneNumber.substring(0, 3));
            sb.append("****");
            sb.append(phoneNumber.substring(phoneNumber.length() - 4));
            return sb.toString();
        } else {
            return phoneNumber;
        }
    }

}
