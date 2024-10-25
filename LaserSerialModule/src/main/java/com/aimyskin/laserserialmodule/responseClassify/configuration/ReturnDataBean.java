package com.aimyskin.laserserialmodule.responseClassify.configuration;

public class ReturnDataBean {
    private int hz;
    private int workCurrentPercent; // 工作电流百分比，用于发送电流数据
    private int energy;// 可能会重复
    private int pulseWidth;// 永不会重复，递增到最大值，

    public ReturnDataBean() {
    }

    public ReturnDataBean(int hz, int pulseWidth, int energy, int workCurrentPercent) {
        this.hz = hz;
        this.workCurrentPercent = workCurrentPercent;
        this.energy = energy;
        this.pulseWidth = pulseWidth;
    }

    public int getHz() {
        return hz;
    }

    public void setHz(int hz) {
        this.hz = hz;
    }

    public int getWorkCurrentPercent() {
        return workCurrentPercent;
    }

    public void setWorkCurrentPercent(int workCurrentPercent) {
        this.workCurrentPercent = workCurrentPercent;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public int getPulseWidth() {
        return pulseWidth;
    }

    public void setPulseWidth(int pulseWidth) {
        this.pulseWidth = pulseWidth;
    }
}
