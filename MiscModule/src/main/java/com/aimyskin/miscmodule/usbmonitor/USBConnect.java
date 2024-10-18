package com.aimyskin.miscmodule.usbmonitor;

import android.hardware.usb.UsbDevice;

public interface USBConnect {
    boolean filterDevice(UsbDevice device);

    void processAttach(UsbDevice device);

    void processConnect(UsbDevice device);

    void processCancel(UsbDevice device);

    void processDetach(UsbDevice device);

    default boolean isSkipBreak(){//是否跳过break
        return false;
    }
}