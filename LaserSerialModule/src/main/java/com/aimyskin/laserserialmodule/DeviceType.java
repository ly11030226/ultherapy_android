package com.aimyskin.laserserialmodule;

public enum DeviceType {
    DEVICE_808(0x01), //808
    DEVICE_ULTHERAPY(0x02); //超声刀
    private final int value;
    DeviceType(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }
}
