package com.aimyskin.ultherapy_android;

import android.content.Context;

import com.aimyskin.miscmodule.Platform.AbstractPlatform;
import com.aimyskin.miscmodule.Platform.Platform;
import com.aimyskin.miscmodule.Platform.PlatformMTK;
import com.aimyskin.miscmodule.Platform.PlatformRK3399;
import com.aimyskin.miscmodule.Platform.PlatformSmartEVB;
import com.aimyskin.miscmodule.Platform.PlatformZC328;
import com.aimyskin.miscmodule.Platform.PlatformZC339;
import com.aimyskin.miscmodule.Platform.PlatformZC356;
import com.aimyskin.miscmodule.Platform.PlatformZCZ40;

public class  SerialPortControl {
    public static String getSerialPortSentinelControl(Context context) {
        String serialPath;
        AbstractPlatform platform = new Platform().build(context);
        if (platform instanceof PlatformMTK) {
            serialPath = "/dev/ttyMT1";
        } else if (platform instanceof PlatformZCZ40) {
            serialPath = "/dev/ttyS7";
        } else if (platform instanceof PlatformZC356) {
            serialPath = "/dev/ttyS7";
        } else   if (platform instanceof PlatformSmartEVB) {
            serialPath = "/dev/ttyS1";
        }else if (platform instanceof PlatformZC339) {
            serialPath = "/dev/ttyS3";
        }else if (platform instanceof PlatformZC328) {
            serialPath = "/dev/ttyS4";
        } else if (platform instanceof PlatformRK3399){
            serialPath = "/dev/ttyXRUSB2";
        } else{
            serialPath = "/dev/ttyMT1";
        }
        return serialPath;
    }

}
