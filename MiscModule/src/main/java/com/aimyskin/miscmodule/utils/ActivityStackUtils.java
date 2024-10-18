package com.aimyskin.miscmodule.utils;

import android.app.Activity;

import com.elvishew.xlog.XLog;

import java.util.Stack;

/**
 * Created by 秋 on 2022/9/17
 * email 1029496119@qq.com
 */
public class ActivityStackUtils {

    private static ActivityStackUtils mInstance;
    private static Stack<Activity> sActivityStack;

    public static ActivityStackUtils getmInstance() {
        if (mInstance == null) {
            mInstance = new ActivityStackUtils();
        }
        return mInstance;
    }

    /**
     * 返回最后添加的Activity即当前
     */
    public Activity currentActivity() {
        Activity activity = sActivityStack.lastElement();
        return activity;
    }

    /**
     * 上一个Activity
     */
    public Activity lastActivity() {
        Activity activity = sActivityStack.get(sActivityStack.size() - 2);
        return activity;
    }

    /**
     * 添加
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        if (sActivityStack == null) {
            sActivityStack = new Stack<>();
        }
        sActivityStack.add(activity);
    }

    /**
     * 删除
     */
    public void finishActivity() {
        Activity activity = sActivityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 删除指定activity 重复Activity仅删除一个
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            try {
                if (sActivityStack.contains(activity)) {
                    sActivityStack.remove(activity);
                }
                activity.finish();
                activity = null;
            } catch (Exception e) {
                XLog.e("finishActivity: ", e);
            }
        }
    }


    /**
     * 删除所有指定Activity
     *
     * @param cls
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : sActivityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 删除所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, stackSize = sActivityStack.size(); i < stackSize; i++) {
            if (sActivityStack.get(i) != null) {
                sActivityStack.get(i).finish();
            }
        }
        sActivityStack.clear();
    }

    public void AppExit() {
        try {
            finishAllActivity();
        } catch (Exception e) {
            XLog.e("AppExit: ", e);
        }
    }

}
