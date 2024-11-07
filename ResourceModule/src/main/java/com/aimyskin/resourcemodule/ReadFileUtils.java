package com.aimyskin.resourcemodule;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.text.TextUtils;

import androidx.annotation.ArrayRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import com.elvishew.xlog.XLog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bolts.Continuation;
import bolts.Task;


/**
 * @author kang
 * 读取文本及获取根据索引获取文本内容类
 * 2022年10月19日10:28:48
 */
public class ReadFileUtils {

    public static ReadFileUtils readFileUtils;
    private static Context mContext;
    private static StringsFileReader languagesFileReader;
    private static ColorsFileReader colorsFileReader;
    private static boolean isDefaultLanguage = true;//是否启用默认  默认状态为启用
    private static int themeColor = 0x40E0D0;//存放颜色
    private static int defaultThemeColor = 0x40E0D0;//默认颜色

    private ReadFileUtils() {
        languagesFileReader = new StringsFileReader();
        colorsFileReader = new ColorsFileReader();
    }

    public static void init(Context context) {
        init(context, defaultThemeColor);
    }

    public static void init(Context context, int themeColor) {
        mContext = context;
        defaultThemeColor = themeColor;
        if (readFileUtils == null) {
            readFileUtils = new ReadFileUtils();
        }
    }

    /**
     * 获取当前对象
     *
     * @return
     */
    public static ReadFileUtils INSTANCE() {
        return readFileUtils;
    }

    public Task<Map<String, Object>> readLanguageFile(final File file) {
        Task task = ReadFileUtils.INSTANCE().languagesFileReader.readFile(file);
        task = task.continueWithTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                if (task.isFaulted() || task.isCancelled()) {
                    ReadFileUtils.INSTANCE().restoreDefaultLanguage();
                } else {
                    isDefaultLanguage = false;
                }
                return task;
            }
        });
        return task;
    }

    public Task<Integer> readColorFile(final File file, String colorName) {
        Task task = Task.forResult(null);
        task = task.continueWithTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                return ReadFileUtils.INSTANCE().colorsFileReader.readFile(file);
            }
        });
        task = task.onSuccessTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                return ReadFileUtils.INSTANCE().colorsFileReader.getColor(colorName);
            }
        });
        task = task.onSuccessTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                Integer color = (Integer) task.getResult();
                setThemeColor(color);
                return task;
            }
        });
        return task;
    }

    /**
     * 根据资源id获取资源内容
     *
     * @param resId 资源id
     * @return
     */
    public String getString(@StringRes int resId) {
        if (mContext == null) {
            XLog.e("未在application中初始化ReadfileUtils");
            return null;
        }
        String content = mContext.getResources().getString(resId);
        if (isDefaultLanguage) {
//            if(content.contains("\\\\n")) {
//                content = content.replaceAll("\\\\n", "<br/>");
//            }
            return content;
        }
        String name = mContext.getResources().getResourceName(resId);
        name = name.substring(name.lastIndexOf("/") + 1);
        Task<String> task = languagesFileReader.getString(name);
        content = TextUtils.isEmpty(task.getResult()) ? content : task.getResult();
//        if(content.contains("\\n")) {//此方法确实可行，但是Html.fromHtml会导致换行符丢失故舍弃
//            content = content.replaceAll("\\\\n", "\n");
//        }
        if(content.contains("\\n")) {
            content = content.replaceAll("\\\\n", "<br/>");
        }
        if(content.contains("\\r")) {
            content = content.replaceAll("\\\\r", "<br/>");
        }
        if(content.contains("\\'")) {
            content = content.replaceAll("\\\\'", "\\'");
        }
        if(content.contains("\\u")) {
            content=convertUnicodeToCh(content);
        }
        return content;
    }
    private static String convertUnicodeToCh(String str) {
        Pattern pattern = Pattern.compile("(\\\\u(\\w{4}))");
        Matcher matcher = pattern.matcher(str);

        // 迭代，将str中的所有unicode转换为正常字符
        while (matcher.find()) {
            String unicodeFull = matcher.group(1); // 匹配出的每个字的unicode，比如\u67e5
            String unicodeNum = matcher.group(2); // 匹配出每个字的数字，比如\u67e5，会匹配出67e5

            // 将匹配出的数字按照16进制转换为10进制，转换为char类型，就是对应的正常字符了
            char singleChar = (char) Integer.parseInt(unicodeNum, 16);

            // 替换原始字符串中的unicode码
            str = str.replace(unicodeFull, singleChar + "");
        }
        return str;
    }


    /**
     * 根据资源id获取资源内容
     *
     * @param resId 资源id
     * @return
     */
    public String[] getStringArray(@ArrayRes int resId) {
        if (mContext == null) {
            XLog.e("未在application中初始化ReadfileUtils");
            return null;
        }
        String[] content = mContext.getResources().getStringArray(resId);
        if (isDefaultLanguage) {
            return content;
        }
        String name = mContext.getResources().getResourceName(resId);
        name = name.substring(name.lastIndexOf("/") + 1);
        Task<String[]> task = languagesFileReader.getStringArray(name);
        content = task.getResult() == null ? content : task.getResult();
        return content;
    }


    /**
     * 通过资源id或者drawable  适用于shape
     *
     * @param drawableResId 资源id
     * @return
     */
    public Drawable getDrawableByShapeUpdateBackground(@DrawableRes int drawableResId) {
        Drawable drawable = null;
        drawable = mContext.getResources().getDrawable(drawableResId);
        if (drawable instanceof GradientDrawable) {
            ((GradientDrawable) drawable).setColor(getThemeColor());
        }
        return drawable;
    }


    /**
     * 变更主色
     *
     * @param themeColor 变更后的颜色
     */
    public void setThemeColor(int themeColor) {
        this.themeColor = themeColor;
    }

    /**
     * 获取变更后的主色
     *
     * @return
     */
    public int getThemeColor() {
        String s = Integer.toHexString(themeColor);
        return Color.parseColor("#" + s);
    }

    /**
     * selector图片选择器单条实体类
     */
    public class SelectorDrawableItem {
        public int[] states;//需要加入的图片选择器的状态组
        public Drawable drawableRes;//状态组对应的图片展示 与状态组一一对应
    }

    /**
     * 获取图片选择器类型的图片
     *
     * @param items 其中SelectorDrawableItem的state参考这种写法"-android.R.attr.state_enabled",负号代表false
     * @return 返回处理好的selector图片
     */
    public Drawable getDrawableBySelector(SelectorDrawableItem... items) {
        StateListDrawable drawable = null;
        drawable = new StateListDrawable();
        if (items == null) return drawable;
        for (int i = 0; i < items.length; i++) {
            if (items[i].states == null) {
                items[i].states = new int[0];
            }
            drawable.addState(items[i].states, items[i].drawableRes);
        }
        return drawable;
    }

    public class SelectorColorItem {
        public int[] states;//需要加入的图片选择器的状态组
        public int colorRes;//状态组对应的图片展示 与状态组一一对应
    }

    public ColorStateList getColorBySelector(SelectorColorItem... items) {
        ColorStateList colorStateList = null;
        if(items==null)return colorStateList;
        int[][] states=new int[items.length][items.length];
        int[] colors=new int[items.length];
        for (int i=0;i<items.length;i++){
            states[i]=items[i].states;
            colors[i]=items[i].colorRes;
        }
        colorStateList=new ColorStateList(states,colors);
        return colorStateList;
    }

    /**
     * 获取资源包中apk的文件名
     *
     * @param file
     */
    public String getPackageName(File file) {
        if (file == null) {
            XLog.e("传入文件夹为空");
            return "";
        }
        if (file.isDirectory()) {
            String[] children = file.list();
            if (children == null) {
                XLog.e("传入的文件夹内容为null");
                return "";
            }
            for (int i = 0; i < children.length; i++) {
                //如果是apk则返回apk文件名字 包含后缀
                if (children[i].contains(".apk")) {
                    return children[i];
                }
            }
            XLog.e("不存在apk格式的文件");
            return "";
        } else {
            XLog.e("传入的file不是一个文件夹");
            return "";
        }
    }

    /**
     * 获取资源包中语言的文件夹列表内容
     *
     * @param file               语言文件夹路径file
     * @param standardTierNumber 分割标准
     * @return
     */
    public List<String> getLanguageList(File file, int standardTierNumber) {
        List<String> list = new ArrayList<>();
        if (file == null) {
            XLog.e("传入文件夹为空");
            return list;
        }
        if (file.isDirectory()) {
            String[] children = file.list();
            if (children == null) {
                XLog.e("传入的文件夹内容为null");
                return list;
            }
            for (int i = 0; i < children.length; i++) {
                //语言标准 分成几段
                //如果分割后符合定义段数标准，则添加到返回结果中
                if (children[i].split("_").length == standardTierNumber) {
                    list.add(children[i]);
                } else {
                    XLog.e("有不符合标准的文件夹" + children[i]);
                }
            }
        } else {
            XLog.e("传入的file不是一个文件夹");
        }
        return list;
    }

    /**
     * 重置默认语言
     */
    public void restoreDefaultLanguage() {
        isDefaultLanguage = true;
    }

    /**
     * 重置默认颜色
     */
    public void restoreDefaultThemeColor() {
        themeColor = defaultThemeColor;
    }

    /**
     * 设置默认颜色
     *
     * @param themeColor
     */
    public void setDefaultThemeColor(int themeColor) {
        defaultThemeColor = themeColor;
    }

}
