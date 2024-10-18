package com.aimyskin.miscmodule.usbmonitor;

import android.hardware.usb.UsbDevice;

public interface UsbCallBack {
    boolean filterDevice(UsbDevice device);

    void processConnect(UsbDevice device);

    void processCancel(UsbDevice device);

    default boolean isSkipBreak(){//是否跳过break
        return false;
    }
}
