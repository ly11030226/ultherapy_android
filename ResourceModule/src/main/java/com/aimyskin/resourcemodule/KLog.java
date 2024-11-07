package com.aimyskin.resourcemodule;

import android.util.Log;

import com.aimyskin.miscmodule.BuildConfig;

/**
 * Created by 吕培康 on 2018/11/29.
 * 本人原创
 * 日志输出类
 */

public class KLog {
    private static final boolean isGrab= BuildConfig.DEBUG;
//    private static final boolean isGrab= true;
    private static final String Tag="KLog";
    private static final boolean isClassMsg=true;
    //打印logE日志
    public static void e(String msg){
        if(isGrab) {
            if(isClassMsg){
                String[] strings=getAutoJumpLogInfos();
                Log.e(Tag,strings[0]+"---"+strings[1]+"---"+strings[2]+"---"+msg);
            }else {
                Log.e(Tag, msg);
            }
        }
    }
    //打印logD日志
    public static void d(String msg){
        if(isGrab){
            if(isClassMsg){
                String[] strings=getAutoJumpLogInfos();
                Log.d(Tag,strings[0]+"---"+strings[1]+"---"+strings[2]+"---"+msg);
            }else {
                Log.d(Tag, msg);
            }
        }
    }
    //打印logI日志
    public static void i(String msg){
        if(isGrab){
            if(isClassMsg){
                String[] strings=getAutoJumpLogInfos();
                Log.i(Tag,strings[0]+"---"+strings[1]+"---"+strings[2]+"---"+msg);
            }else {
                Log.i(Tag, msg);
            }
        }
    }
    //打印logV日志
    public static void v(String msg){
        if(isGrab){
            if(isClassMsg){
                String[] strings=getAutoJumpLogInfos();
                Log.v(Tag,strings[0]+"---"+strings[1]+"---"+strings[2]+"---"+msg);
            }else {
                Log.v(Tag, msg);
            }
        }
    }
    //打印logW日志
    public static void w(String msg){
        if(isGrab){
            if(isClassMsg){
                String[] strings=getAutoJumpLogInfos();
                Log.w(Tag,strings[0]+"---"+strings[1]+"---"+strings[2]+"---"+msg);
            }else {
                Log.w(Tag, msg);
            }
        }
    }
    //打印logWTF日志
    public static void wtf(String msg){
        if(isGrab){
            if(isClassMsg){
                String[] strings=getAutoJumpLogInfos();
                Log.wtf(Tag,strings[0]+"---"+strings[1]+"---"+strings[2]+"---"+msg);
            }else {
                Log.wtf(Tag, msg);
            }
        }
    }
    /**
     * 获取打印信息所在方法名，行号等信息
     *
     * @return
     */
    private static String[] getAutoJumpLogInfos() {
        String[] infos = new String[]{"", "", ""};
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        if (elements.length < 5) {
            return infos;
        } else {
            infos[0] = elements[4].getClassName().substring(
                    elements[4].getClassName().lastIndexOf(".") + 1);
            infos[1] = elements[4].getMethodName() + "()";
            infos[2] = " at (" + elements[4].getClassName() + ".java:"
                    + elements[4].getLineNumber() + ")";
            return infos;
        }
    }
}
