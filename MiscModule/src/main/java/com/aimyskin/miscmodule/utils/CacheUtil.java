package com.aimyskin.miscmodule.utils;

import android.os.Environment;

import java.io.File;

/**
 * 缓存目录
 * AndroidManifest.xml 中的application添加下行代码
 * android:requestLegacyExternalStorage="true"
 */
public class CacheUtil {

    /**
     * Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/7D_ifc/";
     * /storage/emulated/0/Android/data/com.ifcloud.global/files/Boshimei
     */
    public static String getPath(String filename) {
        File sdRoot;
        String state = Environment.getExternalStorageState();  // 获取sd状态
        if (Environment.MEDIA_MOUNTED.equals(state)) { // read/write
            sdRoot = Environment.getExternalStorageDirectory();
        } else {
            sdRoot = Environment.getDataDirectory();
        }
        String pathstr = sdRoot + File.separator + filename;
        new File(pathstr).mkdirs();
        return pathstr;
    }



}
