package com.aimyskin.miscmodule.utils;

public class XorCryptUtils {

    /**
     * 异或算法加密/解密
     *
     * @param str 数据（密文/明文）
     * @return 返回解密/加密后的数据
     */
    //XOR加密解密16进制字符串
    public static String encrypt(String str) {
        //判断二维码信息是否符合标准
        str = str.replace(" ", "");
        String ccc = "^[0-9A-Z]+$";
        if (str == null || str.length() == 0 || str.length() % 2 != 0 || !(str.matches(ccc))) {
            return "0000";
        }
        //每两个字符拆分成一个16进制字符
        int len = str.length() / 2;
        char[] chars = new char[len];
        for (int i = 0; i < len; i++) {
            chars[i] = (char) (0xff & Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16));
        }
        //XOR密码表
        char[] send = {
                0xC2, 0x6F, 0x57, 0x53, 0x57, 104, 28, 42, 186, 189, 223, 119, 229, 102, 231, 27, 212, 234, 220, 167, 216, 101, 110, 246, 209, 147, 156, 236, 25,
                165, 14, 38, 150, 97, 171, 253, 85, 219, 154, 245, 2, 165, 62, 205, 101, 36, 155, 51, 228, 174, 6, 201, 90, 113, 122, 100, 131, 25, 87, 221,
                197, 175, 112, 141, 107, 212, 108, 14, 248, 162, 206, 33, 155, 125, 207, 145, 172, 218, 188, 81, 224, 128, 9, 220, 114, 3, 47, 144, 75, 126,
                255, 152, 183, 177, 64, 22, 91, 4, 106, 59, 13, 157, 249, 137, 122, 8, 52, 81, 41, 107, 31, 159, 190, 250, 52, 89, 233, 163, 25, 166, 218, 158,
                43, 147, 41, 186, 236, 4};
        //加密解密索引号
        int initrandom = chars[len - 1] % (128 - len);
        //遍历加密解密数据源
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < len - 1; i++) {
            int i1 = chars[i] ^ send[initrandom];
            buffer.append(String.format("%02X", i1));
            initrandom++;
        }
        //结果尾部添加初始序号（16进制）
        buffer.append(String.format("%02X", chars[len - 1] & 0xFF));
        //返回加密16进制字符串
        return buffer.toString();
    }

}
