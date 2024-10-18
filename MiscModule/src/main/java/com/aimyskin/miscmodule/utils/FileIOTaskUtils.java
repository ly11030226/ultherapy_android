package com.aimyskin.miscmodule.utils;

import com.blankj.utilcode.util.FileIOUtils;

import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;

public class FileIOTaskUtils {

    public static Task<String> readFile2StringTask(final String filePath) {
        Task task = Task.callInBackground(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                return FileIOUtils.readFile2String(filePath);
            }
        });
        task = task.continueWithTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                return task;
            }
        }, Task.UI_THREAD_EXECUTOR);
        return (Task<String>) task;
    }
}
