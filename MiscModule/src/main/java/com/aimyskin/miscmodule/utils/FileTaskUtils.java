package com.aimyskin.miscmodule.utils;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.FileUtils;

import java.io.File;
import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;

import static com.blankj.utilcode.util.FileUtils.createOrExistsDir;

public class FileTaskUtils {

    public interface ProcessListener {
        default void onFile(String src, String dest) {
        }
    }

    public static boolean copy(final String srcPath, final String destPath, ProcessListener listener) {
        return copy(true, new File(srcPath), new File(destPath), listener);
    }

    public static boolean copy(boolean isCover, final File src, final File dest, ProcessListener listener) {
        if (src == null) return false;
        if (src.isDirectory()) {
            return copyDir(isCover, src, dest, listener);
        } else {
            if (listener != null) {
                Task.UI_THREAD_EXECUTOR.execute(new Runnable() {
                    @Override
                    public void run() {
                        listener.onFile(src.getAbsolutePath(), dest.getAbsolutePath());
                    }
                });
            }
            if (FileUtils.copy(src, dest, new FileUtils.OnReplaceListener() {
                @Override
                public boolean onReplace(File srcFile, File destFile) {
                    // true 删除在复制 false 不进行覆盖
                    return isCover;
                }
            })) {
                return true;
            }
            return false;
        }
    }

    private static boolean copyDir(boolean isCover, final File srcDir, final File destDir, ProcessListener listener) {
        if (srcDir == null || destDir == null) return false;
        String srcPath = srcDir.getPath() + File.separator;
        String destPath = destDir.getPath() + File.separator;
        if (destPath.contains(srcPath)) return false;
        if (!srcDir.exists() || !srcDir.isDirectory()) return false;
        if (!createOrExistsDir(destDir)) return false;
        File[] files = srcDir.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                File oneDestFile = new File(destPath + file.getName());
                if (file.isFile()) {
                    if (listener != null) {
                        Task.UI_THREAD_EXECUTOR.execute(new Runnable() {
                            @Override
                            public void run() {
                                listener.onFile(file.getAbsolutePath(), oneDestFile.getAbsolutePath());
                            }
                        });
                    }
                    if (!FileUtils.copy(file, oneDestFile, new FileUtils.OnReplaceListener() {
                        @Override
                        public boolean onReplace(File srcFile, File destFile) {
                            return isCover;
                        }
                    })) {
                        return false;
                    }
                } else if (file.isDirectory()) {
                    if (!copyDir(isCover, file, oneDestFile, listener)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static Task<Void> copyTask(final String src, final String dest, ProcessListener listener) {
        return copyTask(true, new File(src), new File(dest), listener);
    }

    /**
     * @param isCover true 先删除在复制 false 不进行操作
     */
    public static Task<Void> copyTask(boolean isCover, final String src, final String dest, ProcessListener listener) {
        return copyTask(isCover, new File(src), new File(dest), listener);
    }

    /**
     * @param isCover true 先删除在复制 false 不进行操作
     */
    public static Task<Void> copyTask(boolean isCover, final File src, final File dest, ProcessListener listener) {
        Task task = Task.forResult(null);
        task = task.continueWithTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                Boolean success = copy(isCover, src, dest, listener);
                if (success) {
                    return Task.forResult(null);
                } else {
                    return Task.forError(new Exception("删除错误:" + success));
                }
            }
        }, Task.BACKGROUND_EXECUTOR);
        task = task.continueWithTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                return task;
            }
        }, Task.UI_THREAD_EXECUTOR);
        return task;
    }

    public static Task<Void> deleteTask(final String filePath) {
        return deleteTask(new File(filePath));
    }

    public static Task<Void> deleteTask(final File file) {
        Task task = Task.forResult(null);
        task = task.continueWithTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                Boolean success = FileUtils.delete(file);
                if (success) {
                    return Task.forResult(null);
                } else {
                    return Task.forError(new Exception("删除失败:" + success));
                }
            }
        }, Task.BACKGROUND_EXECUTOR);
        task = task.continueWithTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                return task;
            }
        }, Task.UI_THREAD_EXECUTOR);
        return task;
    }

}
