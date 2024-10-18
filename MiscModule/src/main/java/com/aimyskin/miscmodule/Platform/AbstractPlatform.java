package com.aimyskin.miscmodule.Platform;

import android.content.Context;

public abstract class AbstractPlatform {
    public Context context;
    AbstractPlatform(Context context) {
        this.context = context;
    }
    public abstract boolean exec(final String command);
    public abstract String execWithOutput(final String command);
    public abstract void hiddenStatusBar(boolean hidden);
    public abstract void hiddenNavigationBar(boolean hidden);
    public abstract void hiddenStatusBarAndNavigationBar(boolean hidden);
    public abstract void shutDown();
    public abstract void reboot();
    public abstract void changeBootAnimation(String filePath);
    public abstract void installBusybox();
    public abstract void uninstallBusyBox();
    public abstract void changeLanguage(String language);

    interface Factory {
        AbstractPlatform build(Context context);
    }
}
