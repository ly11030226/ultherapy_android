package com.aimyskin.miscmodule.Platform;

import android.content.Context;
import android.util.Log;

public class Platform implements AbstractPlatform.Factory {

    private static final String TAG = "Platform";

    @Override
    public AbstractPlatform build(Context context) {
        String model = android.os.Build.MODEL;
        Log.d(TAG, "build: " + model);
        switch (model) {
            case "ZC-40M":
            case "ZC-40A":
                return new PlatformZCZ40(context);
            case "ZC-356":
                return new PlatformZC356(context);
            case "TEG9300":// 520
                return new PlatformMTK(context);
            case "SmartEVB":// 8788
                return new PlatformSmartEVB(context);
            case "ZC-339V":
                return new PlatformZC339(context);
            case "ZC-328":
                return new PlatformZC328(context);
            case "rk3399":
                return new PlatformRK3399(context);
            case "rk3562_t":
                return new PlatformRK3562T(context);
            case "PlatformTB240FC":
                return new PlatformTB240FC(context);
            default:
                return new PlatformDefault(context);
        }
    }
}
