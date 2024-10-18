package com.aimyskin.miscmodule.utils;

public class ClickUtils {

    private static long lastClick = 0;
    private static long lastClickView = 0;
    private static int viewClickId = 0;

    public static boolean fastClick(int time) {
        if (System.currentTimeMillis() - lastClick <= time) {
            return true;
        }
        lastClick = System.currentTimeMillis();
        return false;
    }

    public static boolean fastClick() {
        return ClickUtils.fastClick(1000);
    }


    public static boolean fastClickId(int viewId, int time) {
        if (viewClickId == viewId && System.currentTimeMillis() - lastClickView <= time) {
            return true;
        }
        viewClickId = viewId;
        lastClickView = System.currentTimeMillis();
        return false;
    }

    public static boolean fastClickId(int viewId) {
        return ClickUtils.fastClickId(viewId, 1000);
    }
}
