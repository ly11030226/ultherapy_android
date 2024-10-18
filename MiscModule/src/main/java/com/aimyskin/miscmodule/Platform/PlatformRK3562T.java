package com.aimyskin.miscmodule.Platform;

import android.content.Context;

public class PlatformRK3562T extends AbstractPlatform {
    private Context context;

    PlatformRK3562T(Context context) {
        super(context);
        this.context = context;
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
//        if (hidden) {
//            Intent intent = new Intent("android.ido.intent.action.statusbar.HIDE");
//            intent.putExtra("save", true);  //true,断电保存；false，断电不保存
//            context.sendBroadcast(intent);
//        } else {
//            Intent intent = new Intent("android.ido.intent.action.statusbar.SHOW");
//            intent.putExtra("save", true);  //true,断电保存；false，断电不保存
//            context.sendBroadcast(intent);
//        }
    }

    @Override
    public void hiddenNavigationBar(boolean hidden) {
//        if (hidden) {
//            Intent intent = new Intent("android.ido.intent.action.navigation.HIDE");
//            intent.putExtra("save", true);  //true,断电保存；false，断电不保存
//            context.sendBroadcast(intent);
//        } else {
//            Intent intent = new Intent("android.ido.intent.action.navigation.SHOW");
//            intent.putExtra("save", true);  //true,断电保存；false，断电不保存
//            context.sendBroadcast(intent);
//        }
    }

    @Override
    public void hiddenStatusBarAndNavigationBar(boolean hidden) {
//        hiddenStatusBar(hidden);
//        hiddenNavigationBar(hidden);
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
