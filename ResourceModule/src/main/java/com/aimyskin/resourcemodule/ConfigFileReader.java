package com.aimyskin.resourcemodule;

import android.text.TextUtils;

import com.aimyskin.miscmodule.utils.FileIOTaskUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import bolts.Continuation;
import bolts.Task;

public class ConfigFileReader {

    public static Map<String, Object> paramsMap;

    public ConfigFileReader() {
        paramsMap = new HashMap<>();
    }

    public static Map<String, Object> getParamsMap() {
        if (paramsMap == null){
            paramsMap = new HashMap<>();
        }
        return paramsMap;
    }

    /**
     * 读取配置文件
     *
     * @param file
     */
    public Task<ConfigFileReader> readFile(final File file) {
        if (file == null || file.length() == 0) {
            return Task.forError(new Exception("文件为无效文件或者文件大小为0"));
        }
        paramsMap = new HashMap<>();
        Task task = Task.forResult(null);
        task = task.continueWithTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                return FileIOTaskUtils.readFile2StringTask(file.getPath());
            }
        });
        // 解析配置文件
        task = task.onSuccessTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                String message = ((Task<String>) task).getResult();
                if (TextUtils.isEmpty(message) || !message.contains("=")) {
                    return Task.forError(new Exception("参数解析错误"));
                }
                String[] strings = message.split(";|；");
                for (int i = 0; i < strings.length; i++) {
                    if (strings[i].contains("=")) {
                        String[] content = strings[i].split("=");
                        if (content.length >=2){
                            paramsMap.put(content[0].trim(), content[1].trim());
                        }
                    }
                }
                return Task.forResult(ConfigFileReader.this);
            }
        });
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
     * 获取配置值
     *
     * @param name 参数名称
     * @return
     */
    public Task<String> getParam(String name) {
        if (paramsMap == null) {
            return Task.forError(new Exception("数据为空"));
        }
        Object result = paramsMap.get(name);
        if (result instanceof String) {
            return Task.forResult((String) result);
        } else {
            return Task.forError(new Exception("参数解析错误"));
        }
    }

}
