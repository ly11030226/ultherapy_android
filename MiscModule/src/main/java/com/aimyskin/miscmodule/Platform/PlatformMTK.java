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

public class PlatformMTK extends AbstractPlatform {

    Process process = null;
    DataOutputStream os = null;

    PlatformMTK(Context context) {
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
        exec("mount -o rw,remount /system");
        exec("rm  /system/media/bootanimation.zip");
        exec("cp " + filePath + " /system/media/bootanimation.zip");
        exec("chmod 644 /system/media/bootanimation.zip");
        exec("mount -o ro,remount /system");
    }

    @Override
    public void installBusybox() {
        String SDCARD = Environment.getExternalStorageDirectory().getPath();
        String tmpFilePath = SDCARD + "/busybox";
        ResourceUtils.copyFileFromRaw(R.raw.busybox, tmpFilePath);
        execWithOutput("mount -o rw,remount /system");
        execWithOutput("rm  /system/xbin/busybox");
        execWithOutput("cp " + tmpFilePath + " /system/xbin");
        execWithOutput("chmod 755 /system/xbin/busybox");
        execWithOutput("mount -o ro,remount /system");
        execWithOutput("rm " + tmpFilePath);
    }

    @Override
    public void uninstallBusyBox() {
        execWithOutput("mount -o rw,remount /system");
        execWithOutput("rm  /system/xbin/busybox");
        execWithOutput("mount -o ro,remount /system");
    }

    @Override
    public void changeLanguage(String language) {

    }
}
