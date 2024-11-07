package com.aimyskin.resourcemodule;

import android.text.TextUtils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bolts.Continuation;
import bolts.Task;

public class StringsFileReader {

    private static Map<String, Object> paramsMap;

    public StringsFileReader() {
        paramsMap = new HashMap<>();
    }

    /**
     * 读取文件（可追加，但是不可加入打包时不存在的字段）
     *
     * @param file
     */
    public Task<StringsFileReader> readFile(final File file) {
        if (file == null || file.length() == 0) {
            return Task.forError(new Exception("文件为无效文件或者文件大小为0"));
        }
        paramsMap = new HashMap<>();
        Task task = Task.forResult(null);
        task = task.continueWithTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();
                InputStream inputStream = new FileInputStream(file);
                parser.setInput(inputStream, "utf-8");
                int eventType = parser.getEventType();
                String name = "", content = "";
                String labeType = "";
                List<String> list = new ArrayList<>();
                StringBuilder stringBuilder=new StringBuilder();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_TAG:
                            labeType = parser.getName();
                            if ("string".equals(labeType)) {
                                stringBuilder=new StringBuilder();
                                name = parser.getAttributeValue(null, "name");
                            }
                            if ("string-array".equals(labeType)) {
                                name = parser.getAttributeValue(null, "name");
                                list = new ArrayList<>();
                            }
                            if(!"string".equals(labeType)&&!"string-array".equals(labeType)){
                                stringBuilder.append("<");
                                stringBuilder.append(labeType);
                                stringBuilder.append(">");
                            }
                            break;
                        case XmlPullParser.TEXT:
                            if (!TextUtils.isEmpty(name)) {
                                content = parser.getText();
                                stringBuilder.append(content);
                                content = "";
                            }
                            if (!TextUtils.isEmpty(name) && "item".equals(labeType)) {
                                content = parser.getText();
                                if (!TextUtils.isEmpty(content) && !TextUtils.isEmpty(content.trim())) {
                                    list.add(content);
                                }
                                content = "";
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            labeType = parser.getName();
                            if(!"string".equals(labeType)&&!"string-array".equals(labeType)){
                                stringBuilder.append("</");
                                stringBuilder.append(labeType);
                                stringBuilder.append(">");
                            }
                            if ("string".equals(labeType)) {
                                paramsMap.put(name, stringBuilder.toString());
                                name = "";
                            }
                            if ("string-array".equals(labeType)) {
                                paramsMap.put(name, list);
                                name = "";
                            }
                            break;
                    }
                    eventType = parser.next();
                }
                inputStream.close();
                return Task.forResult(this);
            }
        }, Task.BACKGROUND_EXECUTOR);
        task = task.continueWithTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                if (task.isFaulted()) {
                    task.getError().printStackTrace();
                }
                return task;
            }
        }, Task.UI_THREAD_EXECUTOR);
        return task;
    }

    /**
     * 获取标签文字内容
     *
     * @param name 标签的索引名字
     * @return
     */
    public Task<String> getString(String name) {
        if (paramsMap == null) {
            return Task.forError(new Exception("数据为空"));
        }
        Object result = paramsMap.get(name);
        if (result instanceof String) {
            return Task.forResult((String) result);
        } else {
            return Task.forError(new Exception("参数类型错误"));
        }
    }

    /**
     * 获取标签文字内容数组
     *
     * @param name 标签的索引名字
     * @return
     */
    public Task<String[]> getStringArray(String name) {
        if (paramsMap == null) {
            return Task.forError(new Exception("数据为空"));
        }
        Object result = paramsMap.get(name);
        if (result instanceof String[]) {
            return Task.forResult((String[]) result);
        } else if (result instanceof List) {
            return Task.forResult((String[]) ((List) result).toArray(new String[0]));
        } else {
            return Task.forError(new Exception("参数解析错误"));
        }
    }
}
