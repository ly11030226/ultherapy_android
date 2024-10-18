package com.aimyskin.miscmodule.utils;

import android.content.Context;
import android.text.TextUtils;

import com.aimyskin.miscmodule.Platform.AbstractPlatform;
import com.aimyskin.miscmodule.Platform.Platform;

import java.util.ArrayList;
import java.util.List;

import bolts.Continuation;
import bolts.Task;

public class SystemDirUtils {
    public static Task<List<String>> getDirs(Context context, String path) {
        Task task = Task.forResult(null);
        task = task.continueWithTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                AbstractPlatform platform = new Platform().build(context);
                String output = platform.execWithOutput("ls " + path);
                ArrayList<String> dirs = new ArrayList<>();
                if (!TextUtils.isEmpty(output)){
                    String[] dirArray = output.split("\n");
                    if (dirArray != null && dirArray.length != 0) {
                        for (String s : dirArray) {
                            if (!s.isEmpty()) {
                                dirs.add(s);
                            }
                        }
                    }
                }
                return Task.forResult(dirs);
            }
        }, Task.BACKGROUND_EXECUTOR);
        return task;
    }
}
