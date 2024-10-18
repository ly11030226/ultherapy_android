package com.aimyskin.miscmodule.utils;

import android.content.Context;

import com.aimyskin.miscmodule.Platform.AbstractPlatform;
import com.aimyskin.miscmodule.Platform.Platform;

import java.util.ArrayList;
import java.util.List;

import bolts.Task;

public class UDiskFormatUtils {

    public static List<String> getDevBlockUsbStorageDirs(List<String> dirs) {
        ArrayList<String> paths = new ArrayList<>();
        if (dirs != null && dirs.size() != 0) {
            for (String s : dirs) {
                if (!s.isEmpty() && s.startsWith("sd")) {
                    paths.add(s);
                }
            }
        }
        return paths;
    }

    public static List<String> getDevBlockPaths(List<String> dirs) {
        ArrayList<String> paths = new ArrayList<>();
        if (dirs != null && dirs.size() != 0) {
            for (String s : dirs) {
                if (!s.isEmpty()) {
                    paths.add("/dev/block/" + s);
                }
            }
        }
        return paths;
    }

    public static Task<List<String>> getDevBlockDirs(Context context) {
        return SystemDirUtils.getDirs(context, "/dev/block/");
    }

    public static void formatUsbStorage(Context context, String path) {
        AbstractPlatform platform = new Platform().build(context);
        String cmd = "busybox mkfs.vfat " + path;
        platform.execWithOutput(cmd);
    }

}
