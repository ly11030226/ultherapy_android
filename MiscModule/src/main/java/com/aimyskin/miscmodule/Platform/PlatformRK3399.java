package com.aimyskin.miscmodule.Platform;

import android.content.Context;

import com.aimyskin.miscmodule.utils.ShellUtils;


public class PlatformRK3399 extends AbstractPlatform {

    PlatformRK3399(Context context) {
        super(context);
    }

    @Override
    public boolean exec(String command) {
        return false;
    }

    @Override
    public String execWithOutput(String command) {
        return null;
    }

    @Override
    public void hiddenStatusBar(boolean hidden) {

    }

    @Override
    public void hiddenNavigationBar(boolean hidden) {
        String cmd="";
        if (!hidden) {
            cmd="settings put global  policy_control \"immersive.status=*\"";
        } else {
            cmd="settings put global  policy_control  \"immersive.full=*\"";
        }
        execCommand(cmd);

    }

    private String execCommand(String cmd) {
        ShellUtils.CommandResult commandResult = ShellUtils.execCommand(cmd, true, true);
        if(commandResult.result != 0) {

            return null;
        } else {
            return commandResult.successMsg;
        }
    }

    @Override
    public void hiddenStatusBarAndNavigationBar(boolean hidden) {
        String cmd="";
        if (!hidden) {
            cmd="settings put global  policy_control \"\"";
        } else {
            cmd="settings put global  policy_control  \"immersive.full=*\"";
        }
        execCommand(cmd);
    }

    @Override
    public void shutDown() {

    }

    @Override
    public void reboot() {

    }

    @Override
    public void changeBootAnimation(String filePath) {

    }

    @Override
    public void installBusybox() {

    }

    @Override
    public void uninstallBusyBox() {

    }

    @Override
    public void changeLanguage(String language){
        String cmd="";
        if("zh".equals(language)) {
            cmd = "settings put system system_locales zh-CN";
        }else if("en".equals(language)){
            cmd = "settings put system system_locales en-US";
        }else {
            return;
        }
        execCommand(cmd);
    }
}
