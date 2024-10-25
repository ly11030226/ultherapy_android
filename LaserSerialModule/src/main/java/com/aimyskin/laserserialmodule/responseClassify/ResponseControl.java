package com.aimyskin.laserserialmodule.responseClassify;

import android.content.Context;

import com.aimyskin.laserserialmodule.DeviceType;
import com.aimyskin.laserserialmodule.responseClassify.device808Classify.Response808;

public class ResponseControl {

    public static ResponseClassify classify;

    public static ResponseClassify getResponseControl(Context context, DeviceType deviceType) {
        if (deviceType == DeviceType.DEVICE_808) {
            if (classify == null) {
                classify = new Response808(context);
            }
            if (!(classify instanceof Response808)) {
                classify = new Response808(context);
            }
            return classify;
        }
        //处理超声刀
        else if(deviceType == DeviceType.DEVICE_ULTHERAPY){
            if (classify == null) {
                classify = new Response808(context);
            }
        }
        if (!(classify instanceof ResponseCommon)) {
            classify = new ResponseCommon();
        }
        return classify;
    }



}
