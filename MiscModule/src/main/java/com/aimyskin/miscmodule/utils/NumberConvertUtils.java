package com.aimyskin.miscmodule.utils;

public class NumberConvertUtils {

    public static int byteToUnsignedInt(byte x) {
        return ((int) x) & 0xff;
    }

    public static long byteToUnsignedLong(byte x) {
        return ((long) x) & 0xffL;
    }

    public static long intToUnsignedLong(int x) {
        return ((long) x) & 0xffffffffL;
    }
}
