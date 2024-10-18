package com.aimyskin.miscmodule.Platform;

import android.content.Context;

/**
 * 12.7寸 联想
 */
public class PlatformTB240FC extends AbstractPlatform{

    PlatformTB240FC(Context context) {
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

    }

    @Override
    public void hiddenStatusBarAndNavigationBar(boolean hidden) {

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
    public void changeLanguage(String language) {

    }
}
