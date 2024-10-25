package com.aimyskin.laserserialmodule.responseClassify.device808Classify;

import android.text.TextUtils;

import com.aimyskin.laserserialmodule.responseClassify.device808Classify.Response808;
import com.aimyskin.laserserialmodule.responseClassify.device808Classify.State808Enum;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class DataTreatingUtil {

    public static int getLease() {
        return Response808.getParameterByAddress(State808Enum.STATE_808_0000.getAddress());
    }

    public static String getHostVersion(String format) {
        if (TextUtils.isEmpty(format) || !format.matches(".*%02X.*%02X.*")) {
            format = "%02X.%02X";
        }
        int version = Response808.getParameterByAddress(State808Enum.STATE_808_0001.getAddress());
        int highPart = (version >> 8) & 0xFF;  // 高位部分
        int lowPart = version & 0xFF;  // 低位部分
        return String.format(format, highPart, lowPart);
    }

    public static int getHostType() {
        return Response808.getParameterByAddress(State808Enum.STATE_808_0003.getAddress());
    }

    public static String getHostTypeStr() {
        int type = getHostType();
        switch (type) {
            case 1:
                return "";
            default:
                return "808";
        }
    }

    public static int getHandleModel() {
        return Response808.getParameterByAddress(State808Enum.STATE_808_0002.getAddress());
    }

    public static String getHandleProgramModel(String format) {
        if (TextUtils.isEmpty(format) || !format.matches(".*%02X.*%02X.*")) {
            format = "%02X.%02X";
        }
        int version = Response808.getParameterByAddress(State808Enum.STATE_808_0023.getAddress());
        int highPart = (version >> 8) & 0xFF;  // 高位部分
        int lowPart = version & 0xFF;  // 低位部分
        return String.format(format, highPart, lowPart);
    }

    public static String getSerialNumber() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("%04X", Response808.getParameterByAddress(State808Enum.STATE_808_0004.getAddress())));
        stringBuilder.append(String.format("%04X", Response808.getParameterByAddress(State808Enum.STATE_808_0005.getAddress())));
        stringBuilder.append(String.format("%04X", Response808.getParameterByAddress(State808Enum.STATE_808_0006.getAddress())));
        stringBuilder.append(String.format("%04X", Response808.getParameterByAddress(State808Enum.STATE_808_0007.getAddress())));
        stringBuilder.append(String.format("%04X", Response808.getParameterByAddress(State808Enum.STATE_808_0008.getAddress())));
        stringBuilder.append(String.format("%04X", Response808.getParameterByAddress(State808Enum.STATE_808_0009.getAddress())));
        stringBuilder.append(String.format("%04X", Response808.getParameterByAddress(State808Enum.STATE_808_000A.getAddress())));
        stringBuilder.append(String.format("%04X", Response808.getParameterByAddress(State808Enum.STATE_808_000B.getAddress())));
        return stringBuilder.toString();
    }


    public static int getPulseWidth() {
        int value = Response808.getParameterByAddress(State808Enum.STATE_808_000D.getAddress());
        return (value >> 8) & 0xFF;
    }

    public static int getFrequencyHz() {
        int value = Response808.getParameterByAddress(State808Enum.STATE_808_000D.getAddress());
        return value & 0xFF;
    }

    public static int getEnergyState() {
        return Response808.getParameterByAddress(State808Enum.STATE_808_000E.getAddress());
    }

    /**
     * 手柄当前温度
     */
    public static String getHandleTemp() {
        int temp = Response808.getParameterByAddress(State808Enum.STATE_808_000F.getAddress());
        short signedValue = (short) temp;
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("0.0",symbols);
        String formattedValue = df.format((double) signedValue / 10);
        return formattedValue;
    }

    /**
     * 目标温度
     */
    public static int getHandleTargetTemp() {
        int temp = Response808.getParameterByAddress(State808Enum.STATE_808_0010.getAddress());
        int tempH = (temp >> 8) & 0xFF;
        short signedValue = (short) tempH;
        return signedValue;
    }

    /**
     * 制冷单位 0-100 0 为关闭
     */
    public static int getHandleRefrigerateGear() {
        int temp = Response808.getParameterByAddress(State808Enum.STATE_808_0010.getAddress());
        return temp & 0xFF;  // 低位部分
    }

    /**
     * 激光计数
     */
    public static int getLaserCounting() {
        return Response808.getParameterByAddress(State808Enum.STATE_808_0011.getAddress());
    }

    /**
     * 流速
     */
    public static String getFlowRate() {
        int flow = Response808.getParameterByAddress(State808Enum.STATE_808_0012.getAddress());
        flow = (flow >> 8) & 0xFF;
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("0.0", symbols);
        String flowStr = df.format((double) flow / 10);
        return flowStr;  // 流速
    }

    /**
     * 水位
     */
    public static int getWaterLevel() {
        int flow = Response808.getParameterByAddress(State808Enum.STATE_808_0012.getAddress());
        return flow & 0xFF;  // 低位部分
    }

    /**
     * 水温
     */
    public static String getWaterTemp() {
        int light = Response808.getParameterByAddress(State808Enum.STATE_808_0013.getAddress());
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("0.0",symbols);
        String formattedValue = df.format((double) light / 10);
        return formattedValue;
    }

    /**
     * 最大水温
     */
    public static int getWaterTempMax() {
        int flow = Response808.getParameterByAddress(State808Enum.STATE_808_0014.getAddress());
        return (flow >> 8) & 0xFF;  // 高位部分
    }

    /**
     * 最小水温
     */
    public static int getWaterTempMin() {
        int flow = Response808.getParameterByAddress(State808Enum.STATE_808_0014.getAddress());
        return flow & 0xFF;  // 低位部分
    }

    /**
     * 手柄按键按下的状态
     */
    public static int getHandKey() {
        int flow = Response808.getParameterByAddress(State808Enum.STATE_808_0015.getAddress());
        return (flow >> 0) & 1;
    }

    /**
     * 手柄连接状态
     */
    public static int getHandConnect() {
        int flow = Response808.getParameterByAddress(State808Enum.STATE_808_0015.getAddress());
//        Log.d("TAG", "getHandConnect: " + flow);
        return (flow >> 1) & 1;
    }

    /**
     * 脚踏状态
     */
    public static int getFootKey() {
        int flow = Response808.getParameterByAddress(State808Enum.STATE_808_0015.getAddress());
        return (flow >> 2) & 1;
    }

    /**
     * 待机准备状态
     */
    public static int getStandbyReady() {
        int state = Response808.getParameterByAddress(State808Enum.STATE_808_0016.getAddress());
        return state & 0xFF;  // 低位部分
    }

    /**
     * 激光头型号
     */
    public static int getHandAdjustableHeadType() {
        int state =  Response808.getParameterByAddress(State808Enum.STATE_808_0017.getAddress());
        return state & 0xFF;  // 低位部分
    }

}

