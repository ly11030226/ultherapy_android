package com.aimyskin.miscmodule.utils;

import android.util.Log;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.SDCardUtils;
import com.elvishew.xlog.XLog;

import java.io.File;
import java.util.List;

import bolts.Continuation;
import bolts.Task;

public class UsbStorageManager {
    private static final String TAG = "UsbStorageManager";
    public String udiskPath = "";
    public String sdPath = "";

    public UsbStorageManager() {
        List<String> mountedPath = SDCardUtils.getMountedSDCardPath();
        for (int i = 0; i < mountedPath.size(); i++) {
            String path = mountedPath.get(i);
            //card类型为存储卡直接插入，不通过读卡器识别
            if (path.contains("udisk") || path.contains("card")) {
                udiskPath = path + "/";
            } else if (path.contains("emulated/0")) {
                sdPath = path + "/";
            } else {
                udiskPath = path + "/";
            }
            Log.d(TAG, "UsbStorageManager: sdPath = " + sdPath);
            Log.d(TAG, "UsbStorageManager: udiskPath = " + udiskPath);
        }
    }
    public Task<Void> saveSdFileToUsb(String sPath, String uPath, FileTaskUtils.ProcessListener listener) {
        return FileTaskUtils.copyTask(sdPath + sPath, udiskPath + uPath, listener);
    }

    public Task<Void> saveUsbToSdFile(String uPath, String sPath, FileTaskUtils.ProcessListener listener) {
        return FileTaskUtils.copyTask(udiskPath + uPath, sdPath + sPath, listener);
    }

    public Task<Void> cleanFileFromSd(final String sPath) {
        return FileTaskUtils.deleteTask(sdPath + sPath);
    }

    public Task<Void> cleanFileFromUsb(String uPath) {
        return FileTaskUtils.deleteTask(udiskPath + uPath);
    }

    public Task<String> findFirstFileName(String path) {
        Task task = Task.forResult(FileUtils.listFilesInDir(path));
        task = task.continueWithTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                List<File> files = (List<File>) task.getResult();
                if (files.size() <= 0) {
                    return Task.forError(new Exception("Not Found apk"));
                } else {
                    return Task.forResult(files.get(0).getName());
                }
            }
        });
        return task;
    }

    public static Task<String> findFirstApkName(String path) {
        Task task = Task.forResult(FileUtils.listFilesInDir(path));
        task = task.continueWithTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                List<File> files = (List<File>) task.getResult();
                if (files != null) {
                    for (File file : files) {
                        if (file.getName().endsWith(".apk")) {
                            return Task.forResult(file.getName());
                        }
                    }
                }
                return Task.forError(new Exception("Not Found apk"));
            }
        });
        return task;
    }

}
