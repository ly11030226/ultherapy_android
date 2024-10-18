package com.aimyskin.miscmodule.utils;

public class ByteArrayUtils {
    public static void memset(byte[] dst, int dstPos, int value, int length) {
        int _length = Math.min((dst.length - dstPos), length);
        for (int i = 0; i < _length; i++) {
            dst[dstPos + i] = (byte) (value & 0xFF);
        }
    }

    public static void memcpy(byte[] dst, int dstPos, byte[] src, int srcIndex, int length) {
        int _length = Math.min(dst.length - dstPos, src.length - srcIndex);
        _length = Math.min(_length, length);
        System.arraycopy(src, srcIndex, dst, dstPos, length);
    }
}
