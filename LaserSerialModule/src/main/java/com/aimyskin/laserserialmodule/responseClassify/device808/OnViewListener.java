package com.aimyskin.laserserialmodule.responseClassify.device808;

import android.widget.TextView;

public interface OnViewListener {

    default void refreshData() {
    }

    /**
     * 脉宽
     */
    default TextView getPulseWidth() {
        return null;
    }

    /**
     * 频率
     */
    default TextView getFrequencyHz() {
        return null;
    }

    /**
     * 能量
     */
    default TextView getEnergy() {
        return null;
    }

    /**
     * 制冷等级
     */
    default void getRefrigeration(int state) {
    }


    /**
     * 流速
     */
    default TextView getFlowRate() {
        return null;
    }

    /**
     * 手柄温度
     */
    default TextView getHandleTemp() {
        return null;
    }

    /**
     * 水温
     */
    default TextView getWaterTemp() {
        return null;
    }

    /**
     * 水位
     */
    default void getWaterLevel(int state) {
    }


    /**
     * 手柄连接状态
     */
    void getHandConnect(int state);

    /**
     * 手柄顶部 活动头型号
     */
    default void getHandAdjustableHeadType(int state) {
    }

    /**
     * 待机准备状态
     */
    default void getStandbyReady(int state) {
    }

    /**
     * 脚踏状态
     */
    default void getFootKey(int state) {
    }

    /**
     * 打光次数
     */
    default void getLaserCounting(int num) {


    }

    default void detectionError() {
    }
}
