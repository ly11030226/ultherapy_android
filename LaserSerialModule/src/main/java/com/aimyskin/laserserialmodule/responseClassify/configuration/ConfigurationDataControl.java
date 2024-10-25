package com.aimyskin.laserserialmodule.responseClassify.configuration;

import com.aimyskin.laserserialmodule.BuildConfig;


public class ConfigurationDataControl {

    public static ConfigurationDataClassify classify;
    private static int handType = -1;

    public static ConfigurationDataClassify getConfigurationDataControl() {
        if (BuildConfig.defaultDataAllocation == 1) {
            classify = new ConfigurationDataNiuAnMei800W();
        } else if (BuildConfig.defaultDataAllocation == 2) {
            classify = new ConfigurationDataHome300W();
        } else {
            classify = new ConfigurationDataDefault800W();
        }
        return classify;
    }

    public static ConfigurationDataClassify getConfigurationDataControl(int handType) {
        if (handType == 1) {
            if (classify == null || ConfigurationDataControl.handType != handType) {
                classify = new ConfigurationDataDefault800W();
            }
        } else if (handType == 2) {
            if (classify == null || ConfigurationDataControl.handType != handType) {
                classify = new ConfigurationDataHome300W();
            }
        } else if (handType == 3) {
            if (classify == null || ConfigurationDataControl.handType != handType) {
                classify = new ConfigurationDataNiuAnMei800W();
            }
        } else if (handType == 4) {
            if (classify == null || ConfigurationDataControl.handType != handType) {
                classify = new ConfigurationDataNiuAnMei1000W();
            }
        } else {
            classify = new ConfigurationDataDefault800W();
        }

        ConfigurationDataControl.handType = handType;
        return classify;
    }
}
