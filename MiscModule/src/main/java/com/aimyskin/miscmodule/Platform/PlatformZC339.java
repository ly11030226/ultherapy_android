package com.aimyskin.miscmodule.Platform;

import android.content.Context;

import com.zcapi;

public class PlatformZC339 extends AbstractPlatform {

    private static zcapi zcApi;

    public PlatformZC339(Context context) {
        super(context);
        zcApi = new zcapi();
        zcApi.getContext(context);
    }

    @Override
    public boolean exec(String command) {
        zcApi.execShellCmd(command);
        return true;
    }

    @Override
    public String execWithOutput(String command) {
        zcApi.execShellCmd(command);
        return null;
    }


    @Override
    public void hiddenStatusBar(boolean hidden) {
        zcApi.setStatusBar(!hidden);
    }

    @Override
    public void hiddenNavigationBar(boolean hidden) {
        zcApi.setGestureStatusBar(!hidden);
    }

    @Override
    public void hiddenStatusBarAndNavigationBar(boolean hidden) {
        hiddenStatusBar(hidden);
        hiddenNavigationBar(hidden);
    }

    @Override
    public void shutDown() {
        zcApi.shutDown();
    }

    @Override
    public void reboot() {
        zcApi.reboot();
    }

    @Override
    public void changeBootAnimation(String filePath) {
        exec("cp " + filePath + " /data/bootanimation.zip");
        exec("chmod 644 /data/bootanimation.zip");
    }

    @Override
    public void installBusybox() {

    }

    @Override
    public void uninstallBusyBox() {

    }

    @Override
    public void changeLanguage(String language) {

    }
}
