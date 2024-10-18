package com.aimyskin.miscmodule.utils;

import android.content.Context;

import com.aimyskin.miscmodule.Platform.AbstractPlatform;
import com.aimyskin.miscmodule.Platform.Platform;
import com.blankj.utilcode.util.StringUtils;

import java.util.List;

import bolts.Task;

public class BusyboxUtils {
    public static void install(Context context) {
        AbstractPlatform platform = new Platform().build(context);
        platform.installBusybox();
    }

    public static void uninstall(Context context) {
        AbstractPlatform platform = new Platform().build(context);
        platform.uninstallBusyBox();
    }

    public static boolean isInstalled(Context context) {
        Task<List<String>> dirsTask = SystemDirUtils.getDirs(context, "/system/xbin/");
        try {
            dirsTask.waitForCompletion();
            List<String> dirs = dirsTask.getResult();
            for (String dir : dirs) {
                if (StringUtils.equals(dir, "busybox")) {
                    return true;
                }
            }
        } catch (InterruptedException e) {
            return false;
        }
        return false;
    }
}
