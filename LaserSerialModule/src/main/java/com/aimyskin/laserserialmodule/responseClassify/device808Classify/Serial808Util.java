package com.aimyskin.laserserialmodule.responseClassify.device808Classify;

import android.content.Context;

import com.aimyskin.miscmodule.Platform.AbstractPlatform;
import com.aimyskin.miscmodule.Platform.Platform;
import com.aimyskin.miscmodule.Platform.PlatformRK3399;

public class Serial808Util {


    public static String getSerialPathSentinelControl(Context context) {
        String serialPath;
        AbstractPlatform platform = new Platform().build(context);
        if (platform instanceof PlatformRK3399) {
//            serialPath = "/dev/ttyS0";
            serialPath = "/dev/ttyXRUSB0";//正式包
        } else {
            serialPath = "/dev/ttyXRUSB0";
        }
        return serialPath;
    }

    public static int getSerialBaudrateSentinelControl(Context context) {
        int baudrate;
        AbstractPlatform platform = new Platform().build(context);
        if (platform instanceof PlatformRK3399) {
            baudrate = 115200;
        } else {
            baudrate = 115200;
        }
        return baudrate;
    }

}
