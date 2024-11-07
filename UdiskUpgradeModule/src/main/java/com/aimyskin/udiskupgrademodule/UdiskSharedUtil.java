package com.aimyskin.udiskupgrademodule;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class UdiskSharedUtil {

    /**
     * 是否需要重新加载文件
     */
    public static Boolean getNeedReloadData(Activity activity) {
        SharedPreferences preferences = activity.getSharedPreferences(UdiskShared.SHARED_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(UdiskShared.SHARED_NEED_RELOAD_DATA, true);
    }
    public static void setNeedReloadData(Activity activity, Boolean needReloadData) {
        SharedPreferences preferences = activity.getSharedPreferences(UdiskShared.SHARED_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(UdiskShared.SHARED_NEED_RELOAD_DATA, needReloadData);
        editor.commit();
    }
    /**
     * 是否需要初始化数据库
     */
    public static Boolean getNeedInitDb(Activity activity) {
        SharedPreferences preferences = activity.getSharedPreferences(UdiskShared.SHARED_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(UdiskShared.SHARED_NEED_INIT_DB, true);
    }
    public static void setNeedInitDb(Activity activity, Boolean needInitDb) {
        SharedPreferences preferences = activity.getSharedPreferences(UdiskShared.SHARED_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(UdiskShared.SHARED_NEED_INIT_DB, needInitDb);
        editor.commit();
    }


}
