package com.aimyskin.miscmodule.Platform;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import com.aimyskin.miscmodule.R;
import com.blankj.utilcode.util.ResourceUtils;
import com.blankj.utilcode.util.ShellUtils;
import com.elvishew.xlog.XLog;

import java.io.DataOutputStream;
import java.io.IOException;

public class PlatformSmartEVB extends AbstractPlatform {

    Process process = null;
    DataOutputStream os = null;

    Process processWithOutput = null;
    DataOutputStream osWithOutput = null;

    PlatformSmartEVB(Context context) {
        super(context);
    }

    @Override
    public boolean exec(String command) {
        if (process == null) {
            try {
                if (os != null) {
                    os.close();
                }
                process = Runtime.getRuntime().exec("su");
                os = new DataOutputStream(process.getOutputStream());
            } catch (IOException e) {
                XLog.e("exec: ", e);
                return false;
            }
        }
        if (os != null) {
            try {
                os.writeBytes(command + "\n");
                os.flush();
                return true;
            } catch (IOException e) {
                XLog.e("exec: ", e);
                return false;
            }
        } else {
            return false;
        }
    }

    public String execWithOutput(String command) {
        ShellUtils.CommandResult result = ShellUtils.execCmd(command, true);
        XLog.d("exec command:" + command);
        XLog.d("exec success msg:" + result.successMsg);
        if (result.result != 0) {
            XLog.e("exec error msg:" + result.errorMsg);
        }
        return result.successMsg;
    }

    @Override
    public void hiddenStatusBar(boolean hidden) {
        hiddenNavigationBar(hidden);
    }

    @Override
    public void hiddenNavigationBar(boolean hidden) {
        if (hidden) {
            Intent intent = new Intent("ifcloud.systemui.hide_StatusBarAndNavigationBar");
            this.context.sendBroadcast(intent);
        } else {
            Intent intent = new Intent("ifcloud.systemui.show_StatusBarAndNavigationBar");
            this.context.sendBroadcast(intent);
        }
    }

    @Override
    public void hiddenStatusBarAndNavigationBar(boolean hidden) {
        hiddenNavigationBar(hidden);
    }

    @Override
    public void shutDown() {
        exec("am broadcast -a ifcloud.systempower.hide_dialog.shutdown");
    }

    @Override
    public void reboot() {
        exec("am broadcast -a ifcloud.systempower.reboot");
    }

    @Override
    public void changeBootAnimation(String filePath) {
        /**
         * 8788 无法执行 mount -o rw,remount / 和MTK那边沟通过 可以默认解锁 但是无法禁用AVB
         * 需要连接 adb 执行 adb disable-verity 才可以操作
          */
        exec("mount -o rw,remount /");
        exec("rm  /system/media/bootanimation.zip");
        exec("cp " + filePath + " /system/media/bootanimation.zip");
        exec("chmod 644 /system/media/bootanimation.zip");
        exec("mount -o ro,remount /");
    }

    @Override
    public void installBusybox() {
        String SDCARD = Environment.getExternalStorageDirectory().getPath();
        String tmpFilePath = SDCARD + "/busybox";
        ResourceUtils.copyFileFromRaw(R.raw.busybox, tmpFilePath);
        /**
         * 8788 无法执行 mount -o rw,remount / 和MTK那边沟通过 可以默认解锁 但是无法禁用AVB
         * 需要连接 adb 执行 adb disable-verity 才可以操作
         */
        execWithOutput("mount -o rw,remount /");
        execWithOutput("rm  /system/xbin/busybox");
        execWithOutput("cp " + tmpFilePath + " /system/xbin");
        execWithOutput("chmod 755 /system/xbin/busybox");
        execWithOutput("mount -o ro,remount /");
        execWithOutput("rm " + tmpFilePath);
    }

    @Override
    public void uninstallBusyBox() {
        /**
         * 8788 无法执行 mount -o rw,remount / 和MTK那边沟通过 可以默认解锁 但是无法禁用AVB
         * 需要连接 adb 执行 adb disable-verity 才可以操作
         */
        execWithOutput("mount -o rw,remount /");
        execWithOutput("rm  /system/xbin/busybox");
        execWithOutput("mount -o ro,remount /");
    }

    @Override
    public void changeLanguage(String language) {

    }
}
