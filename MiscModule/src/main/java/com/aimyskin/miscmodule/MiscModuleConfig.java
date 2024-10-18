package com.aimyskin.miscmodule;

import com.aimyskin.miscmodule.log.LogConfig;

public class MiscModuleConfig {

    private static MiscModuleConfig config;
    private static MiscModuleConfigInitData _initData;

    public LogConfig logConfig;

    private MiscModuleConfig() {
        logConfig = new LogConfig(_initData.versionCode, _initData.versionName);
    }

    public static void init(MiscModuleConfigInitData initData) {
        _initData = initData;
        if (config == null) {
            config = new MiscModuleConfig();
        }
    }

    public static MiscModuleConfig getInstance() {
        return config;
    }
}