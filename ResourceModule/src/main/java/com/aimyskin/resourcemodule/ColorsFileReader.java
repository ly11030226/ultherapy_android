package com.aimyskin.resourcemodule;

import android.graphics.Color;
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

public class ColorsFileReader {

    private static Map<String, Integer> paramsMap;

    public ColorsFileReader() {
        paramsMap = new HashMap<>();
    }

    /**
     * 读 Color 文件
     *
     * @param file
     * @return
     */
    public Task<ColorsFileReader> readFile(final File file) {
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
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_TAG:
                            labeType = parser.getName();
                            if ("color".equals(labeType)) {
                                name = parser.getAttributeValue(null, "name");
                            }
                            break;
                        case XmlPullParser.TEXT:
                            if (!TextUtils.isEmpty(name) && "color".equals(labeType)) {
                                content = parser.getText();
                                //写入数据，重新置空
                                if (content != null && content.length() >= 1) {
                                    int color = Color.parseColor(content);
                                    paramsMap.put(name, color);
                                    break;
                                }
                                content = "";
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            labeType = parser.getName();
                            if ("color".equals(labeType)) {
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
     * 获取标签颜色
     *
     * @param name 标签的索引名字
     * @return
     */
    public Task<Integer> getColor(String name) {
        if (paramsMap == null) {
            return Task.forError(new Exception("数据为空"));
        }
        Object result = paramsMap.get(name);
        if (result instanceof Integer) {
            return Task.forResult((Integer) result);
        } else {
            return Task.forError(new Exception("参数解析错误"));
        }
    }
}
