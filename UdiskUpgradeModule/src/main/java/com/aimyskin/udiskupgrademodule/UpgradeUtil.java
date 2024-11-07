package com.aimyskin.udiskupgrademodule;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.aimyskin.miscmodule.Platform.Platform;
import com.aimyskin.miscmodule.utils.FileIOTaskUtils;
import com.aimyskin.miscmodule.utils.FileTaskUtils;
import com.aimyskin.miscmodule.utils.StringUtil;
import com.aimyskin.miscmodule.utils.UsbStorageManager;
import com.aimyskin.resourcemodule.ConfigFileReader;
import com.aimyskin.resourcemodule.ReadFileUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bolts.Continuation;
import bolts.Task;
import bolts.TaskCompletionSource;


public class UpgradeUtil {
    private static final String TAG = "UpgradeUtil";

    public static void upgrade(UsbStorageManager manager, FragmentActivity activity, UpgradeListener listener) throws Exception {
        if (manager == null) throw new Exception("manager为null");
        if (activity == null) throw new Exception("activity为null");
        if (listener == null) throw new Exception("listener为null");
        Task task = Task.forResult(null);
        task = task.continueWithTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                return task;
            }
        }, Task.BACKGROUND_EXECUTOR);

        // 1. 加载资源
        task = task.continueWithTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                return loadData(activity, manager, listener);
            }
        });

        // 2. 读取升级配置文件
        task = task.continueWithTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                ConfigFileReader configFileReader = new ConfigFileReader();
                return configFileReader.readFile(new File(manager.udiskPath + UpgradePath.UPDATE_CONFIGURATION_FILE_PATH));
            }
        });
        // 获取安装配置
        task = task.onSuccessTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                ConfigFileReader configFileReader = ((Task<ConfigFileReader>) task).getResult();
                UpgradeBean bean = getParamBean(configFileReader);
                if (bean.getInstall_mode() == 0) {
                    return Task.forError(new Exception("配置为空"));
                }
                return Task.forResult(bean);
            }
        });
        // 执行升级
        task = task.continueWithTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                if (task.isFaulted() || task.isCancelled()) {
                    listener.uDiskFailed();
                    return task;
                }
                UpgradeBean bean = ((Task<UpgradeBean>) task).getResult();
                listener.showInstallDialog(bean);
                return task;
            }
        });
    }

    public static int stringToIn(String str) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            return 0;
        }
    }

    public static boolean stringToBoolean(String str) {
        if (!TextUtils.isEmpty(str) && "false".equalsIgnoreCase(str)) {
            return false;
        }
        return true;
    }

    public static UpgradeBean getParamBean(ConfigFileReader configFileReader) {
        UpgradeBean bean = new UpgradeBean();
        if (configFileReader == null || ConfigFileReader.getParamsMap().size() <= 0) {
            return bean;
        }
        Map<String, Object> paramsMap = ConfigFileReader.getParamsMap();
        String install_mode = (String) paramsMap.get(bean.getInstallModeName());
        String copyPath = (String) paramsMap.get(bean.getCopyPathName());
        String isCover = (String) paramsMap.get(bean.getIsCoverName());
        bean.setInstall_mode(stringToIn(install_mode));
        if (!TextUtils.isEmpty(copyPath)) {
            List<String> list = new ArrayList<>();
            String[] strings = copyPath.split(",|，");
            for (int i = 0; i < strings.length; i++) {
                list.add(strings[i].trim());
            }
            bean.setCopyPath(list);
            bean.setCover(stringToBoolean(isCover));
        }
        return bean;
    }


    /**
     * 刷新开机动画
     *
     * @param isCopy 是否将U盘logo文件夹到本地，
     */
    private static Task bootAnimation(FragmentActivity activity, boolean isCopy, String udiskPath, String fileName) {
        Task task = Task.forResult(null);
        if (isCopy) {
            task = task.continueWithTask(new Continuation() {
                @Override
                public Object then(Task task) throws Exception {
                    return FileTaskUtils.copyTask(true, udiskPath + fileName + UpgradePath.BOOTANIMATION_SUBFILE_RELATIVE_PATH,
                            UpgradePath.STORAGE_LOCAL_LOGO_FILE_PATH, null);
                }
            });
        }
        task = task.continueWithTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                File file = new File(UpgradePath.STORAGE_LOCAL_LOGO_FILE_PATH);
                if (file.exists()){
                    new Platform().build(activity).changeBootAnimation(UpgradePath.BOOTANIMATION_FILE_PATH);
                }
                return task;
            }
        });
        return task;
    }

    /**
     * 拷贝color.xml文件，读取主题颜色，并保存到share中
     *
     * @param isCopy 是否将U盘color.xml文件拷贝到本地
     */
    private static Task reloadColor(FragmentActivity activity, boolean isCopy, String udiskPath, String fileName) {
        Task task = Task.forResult(null);
        if (isCopy) {
            task = task.continueWithTask(new Continuation() {
                @Override
                public Object then(Task task) throws Exception {
                    return FileTaskUtils.copyTask(true, udiskPath + fileName + UpgradePath.COLOR_SUB_PATH,
                            UpgradePath.STORAGE_LOCAL_COLOR_FILE_PATH, null);
                }
            });
        }
        task = task.continueWithTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                return ReadFileUtils.INSTANCE().readColorFile(new File(UpgradePath.STORAGE_LOCAL_COLOR_FILE_PATH), UpgradePath.LOCAL_COLOR_THEME_NAME);
            }
        });
        task = task.onSuccessTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                SharedPreferences preferences = activity.getSharedPreferences(UdiskShared.SHARED_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                String newColor = "#" + Integer.toHexString((Integer) task.getResult()).substring(2);
                editor.putString(UdiskShared.SHARED_COLOR, newColor);
                editor.commit();
                return task;
            }
        });
        return task;
    }


    /**
     * 获取语言列表
     *
     * @param isSetDefaultLanguage 是否设置默认语言
     */
    private static Task<List<String>> reloadLanguages(Activity activity, boolean isSetDefaultLanguage) {
        List<String> list = ReadFileUtils.INSTANCE().getLanguageList(new File(UpgradePath.STORAGE_LOCAL_LANGUAGE_DIR_PATH), 3);
        Task task = Task.forResult(null);
        if (isSetDefaultLanguage) {
            task = task.continueWithTask(new Continuation() {
                @Override
                public Object then(Task task) throws Exception {
                    String languageDir = "";
                    for (String languageDirName : list) {
                        // 0_中文_zh 将0设置为默认语言
                        if (StringUtil.equalsString("0", languageDirName.split("_")[0])) {
                            languageDir = languageDirName;
                            SharedPreferences preferences = activity.getSharedPreferences(UdiskShared.SHARED_NAME, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString(UdiskShared.SHARED_LANGUAGE, languageDirName);
                            editor.commit();
                        }
                    }
                    return languageDir;
                }
            });
        }
        task = task.continueWithTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                return Task.forResult(list);
            }
        });
        return task;
    }

    private static Task loadData(FragmentActivity activity, UsbStorageManager manager, UpgradeListener listener) {
        Task task = Task.forResult(null);
        // 首次安装
        if (UdiskSharedUtil.getNeedReloadData(activity)) {
            // 更新开机动画
            task = task.continueWithTask(new Continuation() {
                @Override
                public Object then(Task task) throws Exception {
                    return bootAnimation(activity, false, manager.udiskPath, UpgradePath.MAIN_DIR_NAME);
                }
            });
            task = task.continueWithTask(new Continuation() {
                @Override
                public Object then(Task task) throws Exception {
                    return reloadColor(activity, false, manager.udiskPath, UpgradePath.MAIN_DIR_NAME);
                }
            });
            task = task.continueWithTask(new Continuation() {
                @Override
                public Object then(Task task) throws Exception {
                    return loadScore(activity, false, manager.udiskPath, UpgradePath.MAIN_DIR_NAME, listener);
                }
            });
            task = task.continueWithTask(new Continuation() {
                @Override
                public Object then(Task task) throws Exception {
                    return reloadLanguages(activity, true);
                }
            });
            task = task.continueWithTask(new Continuation() {
                @Override
                public Object then(Task task) throws Exception {
                    UdiskSharedUtil.setNeedReloadData(activity, false);
                    return task;
                }
            });
        }
        // 初始化语言数据库
        if (UdiskSharedUtil.getNeedInitDb(activity)) {
            // 回调初始化语言数据库
            task=task.continueWithTask(new Continuation() {
                @Override
                public Object then(Task task) throws Exception {
                    return reloadLanguages(activity, false);
                }
            });
            task = task.continueWithTask(new Continuation() {
                @Override
                public Object then(Task task) throws Exception {
                    // 语言列表
                    List<String> languages = (List<String>) task.getResult();
                    listener.upgradeInitDb(languages);
                    return task;
                }
            });
            task = task.continueWithTask(new Continuation() {
                @Override
                public Object then(Task task) throws Exception {
                    UdiskSharedUtil.setNeedInitDb(activity, false);
                    return task;
                }
            });
        }
        // 每次打开软件必读本地资源文件
        task = task.continueWithTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                return loadLocalData(manager, listener, activity);
            }
        });
        return task;
    }

    /**
     * todo 特殊文件读取时需要添加
     * 每次打开软件必读取资源文件
     */
    private static Task<Void> loadLocalData(UsbStorageManager manager, UpgradeListener listener, FragmentActivity activity) {
        Task task = Task.forResult(null);
        // 读取主题颜色
        task = task.continueWithTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                return setThemeColor(activity);
            }
        });
        // 读取语言文件
        task = task.continueWithTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                return loadLanguageXml(activity);
            }
        });
        // 读取代理商编码
        task = task.continueWithTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                return loadConsumable(activity, false, manager.udiskPath, UpgradePath.MAIN_DIR_NAME, listener);
            }
        });
        // todo 特殊文件读取时需要添加
        task = task.continueWithTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                return loadBlue(activity, false, manager.udiskPath, UpgradePath.MAIN_DIR_NAME, listener);
            }
        });
        task = task.continueWithTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                return loadCamera(activity, false, manager.udiskPath, UpgradePath.MAIN_DIR_NAME, listener);
            }
        });

        // 刷新页面
        task = task.continueWithTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                if (listener != null) {
                    listener.upgradeRefreshView();
                }
                return null;
            }
        });
        return task;
    }

    /**
     * 设置颜色
     *
     * @param activity
     */
    private static Task<Void> setThemeColor(Activity activity) {
        SharedPreferences preferences = activity.getSharedPreferences(UdiskShared.SHARED_NAME, Context.MODE_PRIVATE);
        String color = preferences.getString(UdiskShared.SHARED_COLOR, "");
        //获取当前颜色
        if (TextUtils.isEmpty(color)) {
            ReadFileUtils.INSTANCE().restoreDefaultThemeColor();
        } else {
            ReadFileUtils.INSTANCE().setThemeColor(Color.parseColor(color));
        }
        return Task.forResult(null);
    }

    /**
     * 读取语言
     *
     * @param activity
     */
    private static Task<Void> loadLanguageXml(Activity activity) {
        SharedPreferences preferences = activity.getSharedPreferences(UdiskShared.SHARED_NAME, Context.MODE_PRIVATE);
        String language = preferences.getString(UdiskShared.SHARED_LANGUAGE, "");

        // 处理 language 判空操作
        Task task;
        if (TextUtils.isEmpty(language)) {
            task = Task.forError(new Exception(language));
        } else {
            task = Task.forResult(language);
        }
        // 查语言
        task = task.onSuccessTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                String language = (String) task.getResult();
                List<File> fileDirList = FileUtils.listFilesInDir(new File(UpgradePath.STORAGE_LOCAL_LANGUAGE_DIR_PATH));
                for (File file : fileDirList) {
                    if (file.isDirectory() && file.getName().split("_").length == 3 && file.getName().equals(language)) {
                        return Task.forResult(file.getName());
                    }
                }
                return Task.forError(new Exception("查找语言文件失败"));
            }
        });
        // 读取语言文件
        task = task.onSuccessTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                return ReadFileUtils.INSTANCE().readLanguageFile(new File(UpgradePath.STORAGE_LOCAL_LANGUAGE_DIR_PATH + "/" + task.getResult() + "/" + UpgradePath.LANGUAGE_FILE_NAME));
            }
        });
        // 返回读取语言名称
        task = task.onSuccessTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                return Task.forResult(language);
            }
        });
        return task;
    }

    /**
     * 读取耗材 显示代理商编码
     */
    private static Task<String> loadConsumable(Activity activity, boolean isCopy, String udiskPath, String fileName, UpgradeListener listener) {
        Task task = Task.forResult(null);
        if (isCopy) {
            task = task.continueWithTask(new Continuation() {
                @Override
                public Object then(Task task) throws Exception {
                    return FileTaskUtils.copyTask(true, udiskPath + fileName + File.separator + UpgradePath.AUTHENTICATION_SUB_FILE_PATH,
                            UpgradePath.STORAGE_LOCAL_QR_AUTHENTICATION_FILE_PATH, null);
                }
            });
        }
        task = FileIOTaskUtils.readFile2StringTask(UpgradePath.STORAGE_LOCAL_QR_AUTHENTICATION_FILE_PATH);
        task = task.continueWithTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                listener.upgradeDidConsumableLoaded((String) task.getResult());
                return task;
            }
        });
        return task;
    }

    private static Task<String> loadBlue(Activity activity, boolean isCopy, String udiskPath, String fileName, UpgradeListener listener) {
        Task task = Task.forResult(null);
        if (isCopy) {
            task = task.continueWithTask(new Continuation() {
                @Override
                public Object then(Task task) throws Exception {
                    return FileTaskUtils.copyTask(true, udiskPath + fileName + UpgradePath.BLUE_SUB_FILTER,
                            UpgradePath.STORAGE_LOCAL_PATH + File.separator + UpgradePath.BLUE_FILTER, null);
                }
            });
        }

        task = FileIOTaskUtils.readFile2StringTask(UpgradePath.STORAGE_LOCAL_PATH + File.separator + UpgradePath.BLUE_FILTER);
        task = task.continueWithTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                listener.upgradeBlueLoaded((String) task.getResult());
                return task;
            }
        });
        return task;
    }

    private static Task<String> loadCamera(Activity activity, boolean isCopy, String udiskPath, String fileName, UpgradeListener listener) {
        Task task = Task.forResult(null);
        if (isCopy) {
            task = task.continueWithTask(new Continuation() {
                @Override
                public Object then(Task task) throws Exception {
                    return FileTaskUtils.copyTask(true, udiskPath + fileName + UpgradePath.CAMERA_SUB_CONFIG,
                            UpgradePath.STORAGE_LOCAL_PATH + File.separator + UpgradePath.CAMERA_CONFIG, null);
                }
            });
        }

        task = FileIOTaskUtils.readFile2StringTask(UpgradePath.STORAGE_LOCAL_PATH + File.separator + UpgradePath.CAMERA_CONFIG);
        task = task.continueWithTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                listener.upgradeCameraLoaded((String) task.getResult());
                return task;
            }
        });
        return task;
    }

    private static Task<String> loadScore(Activity activity, boolean isCopy, String udiskPath, String fileName, UpgradeListener listener) {
        Task task = Task.forResult(null);
        if (isCopy) {
            task = task.continueWithTask(new Continuation() {
                @Override
                public Object then(Task task) throws Exception {
                    return FileTaskUtils.copyTask(true, udiskPath + fileName + UpgradePath.SCORE_SUB_FILE_PATH,
                            UpgradePath.STORAGE_LOCAL_PATH + File.separator + UpgradePath.SCORE_FILE_PATH, null);
                }
            });
        }
        task = FileIOTaskUtils.readFile2StringTask(UpgradePath.STORAGE_LOCAL_PATH + File.separator + UpgradePath.SCORE_FILE_PATH);
        task = task.continueWithTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                listener.upgradeScoreLoaded((String) task.getResult());
                return task;
            }
        });
        return task;
    }


    // ---------------------------------- -------------------------------------------------------

    /**
     * @param udiskPath copy设备源路径
     * @param fileName  copy父文件夹名称
     *                  install_mode
     *                  1，清空数据库所有数据，清空本地文件，U盘拷贝到本地。安装apk
     *                  2，清空数据库资源添加到数据库中的表数据，清空本地文件，U盘拷贝到本地。安装apk
     *                  3，安装apk
     *                  4，卸载apk
     *                  5，根据isCover同名是否替换，将copyPath指定目录拷贝到本地，并正对不同文件显示不同操作
     *                  6，将本地文件全部导出到U盘中
     */
    public static void affirmInstallMode(UpgradeBean bean, FragmentActivity activity, String udiskPath, String fileName, UpgradeListener listener) {
        if (bean.getInstall_mode() == 1) {
            clearAndInstallApk(true, activity, udiskPath, fileName, listener);
        } else if (bean.getInstall_mode() == 2) {
            clearAndInstallApk(false, activity, udiskPath, fileName, listener);
        } else if (bean.getInstall_mode() == 3) {
            justInstallApk(true, activity, udiskPath, fileName, listener);
        } else if (bean.getInstall_mode() == 4) {
            cleanAndUninstallApk();
        } else if (bean.getInstall_mode() == 5) {
            upgradeConfig(bean, activity, udiskPath, fileName, listener);
        } else if (bean.getInstall_mode() == 6) {
            exportDataToUdisk(bean, activity, udiskPath, fileName, listener);
        }
    }

    private static void exportDataToUdisk(UpgradeBean bean, FragmentActivity activity, String udiskPath, String fileName, UpgradeListener listener) {
        if (listener != null) {
            listener.showLoadingDialog(true, null, null);
        }
        Task task = Task.forResult(null);
        task = task.continueWithTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                return task;
            }
        }, Task.BACKGROUND_EXECUTOR);
        task = task.continueWithTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                return FileTaskUtils.deleteTask(udiskPath + fileName);
            }
        });
        task = task.continueWithTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                return FileTaskUtils.copyTask(true, UpgradePath.STORAGE_LOCAL_MAIN_DIR_PATH,
                        udiskPath + fileName,
                        new FileTaskUtils.ProcessListener() {
                            @Override
                            public void onFile(String src, String dest) {
                                if (listener != null) {
                                    listener.showLoadingDialog(true, src, src);
                                }
                            }
                        });
            }
        });
        task = task.continueWithTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                if (listener != null) {
                    listener.dataToUdisk();
                }
                return task;
            }
        });
        task = task.continueWithTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                if (listener != null) {
                    listener.showLoadingDialog(false, null, null);
                }
                if (task.isFaulted() || task.isCancelled()) {
                    if (listener != null) {
                        listener.showToast(task.getError().getMessage());
                    }
                }
                return task;
            }
        });
    }

    private static void clearAndInstallApk(boolean isClearAllDb, FragmentActivity activity, String udiskPath, String fileName, UpgradeListener listener) {
        if (listener != null) {
            listener.showLoadingDialog(true, null, null);
        }
        Task task = Task.forResult(null);
        task = task.continueWithTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                return task;
            }
        }, Task.BACKGROUND_EXECUTOR);
        // 查找是否有 apk
        task = task.onSuccessTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                return hasApkInUdisk(udiskPath + fileName);
            }
        });
        // 清空数据库数据
        task = task.onSuccessTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                //清空 SharedPreferences
                SharedPreferences preferences = activity.getSharedPreferences(UdiskShared.SHARED_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(UdiskShared.SHARED_LANGUAGE, "");
                editor.putString(UdiskShared.SHARED_COLOR, "");
                editor.putBoolean(UdiskShared.SHARED_NEED_RELOAD_DATA, true);
                editor.commit();

                //还原默认设置
                ReadFileUtils.INSTANCE().restoreDefaultLanguage();
                ReadFileUtils.INSTANCE().restoreDefaultThemeColor();

                //清空数据库
                if (listener != null) {
                    if (isClearAllDb) {
                        listener.upgradeCleanAllDb();
                    } else {
                        listener.upgradeCleanDb();
                    }
                    UdiskSharedUtil.setNeedInitDb(activity, true);
                }
                return task;
            }
        });
        //清空本地资源文件夹
        task = task.onSuccessTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                return FileTaskUtils.deleteTask(UpgradePath.STORAGE_LOCAL_MAIN_DIR_PATH);
            }
        });
        // 拷贝新资源到本地
        task = task.onSuccessTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                return FileTaskUtils.copyTask(true, udiskPath + fileName, UpgradePath.STORAGE_LOCAL_MAIN_DIR_PATH,
                        new FileTaskUtils.ProcessListener() {
                            @Override
                            public void onFile(String src, String dest) {
                                if (listener != null) {
                                    listener.showLoadingDialog(true, src, src);
                                }
                            }
                        });
            }
        });
        // 标记是否需要重新加载资源
        task = task.onSuccessTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                UdiskSharedUtil.setNeedReloadData(activity, true);
                return task;
            }
        });
        // 安装 apk
        task = task.onSuccessTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                return doInstallApk(activity);
            }
        });
        // 错误提示
        task = task.continueWithTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                if (listener != null) {
                    listener.showLoadingDialog(false, null, null);
                }
                if (task.isFaulted() || task.isCancelled()) {
                    if (listener != null) {
                        listener.showToast(task.getError().getMessage());
                    }
                }
                return task;
            }
        });

    }

    /**
     * 执行 apk 安装
     *
     * @return
     */
    private static Task doInstallApk(Activity activity) {
        Task task = Task.forResult(null);
        task = task.continueWithTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                List<File> list = FileUtils.listFilesInDir(new File(UpgradePath.STORAGE_LOCAL_APP_DIR_PATH));
                String apkName = "";
                for (int i = 0; i < list.size(); i++) {
                    //如果是apk则返回apk文件名字 包含后缀
                    if (list.get(i).getName().endsWith(".apk")) {
                        apkName = list.get(i).getName();
                        break;
                    }
                }
                AppUtils.installApp(UpgradePath.STORAGE_LOCAL_APP_DIR_PATH + "/" + apkName);
                return task;
            }
        });
        return task;
    }

    /**
     * 判断U盘是否有apk安装包
     *
     * @return
     */
    private static Task hasApkInUdisk(String dest) {
        Task task = UsbStorageManager.findFirstApkName(dest + File.separator + UpgradePath.APP_DIR_NAME);
        return task;
    }

    /**
     * 仅升级 apk
     *
     * @param activity
     * @param listener
     */
    private static void justInstallApk(boolean isInstall, FragmentActivity activity, String udiskPath, String fileName, UpgradeListener listener) {
        Task task = Task.forResult(null);
        task = task.continueWithTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                return task;
            }
        }, Task.BACKGROUND_EXECUTOR);
        // 查找是否有 apk
        task = task.onSuccessTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                return hasApkInUdisk(udiskPath + fileName);
            }
        });
        task = task.onSuccessTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                return FileTaskUtils.deleteTask(UpgradePath.STORAGE_LOCAL_APP_DIR_PATH);
            }
        });
        task = task.onSuccessTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                return FileTaskUtils.copyTask(true, udiskPath + fileName + File.separator + UpgradePath.APP_DIR_NAME,
                        UpgradePath.STORAGE_LOCAL_APP_DIR_PATH, null);
            }
        });
        // 安装 apk
        if (isInstall) {
            task = task.onSuccessTask(new Continuation() {
                @Override
                public Object then(Task task) throws Exception {
                    return doInstallApk(activity);
                }
            });
            task = task.continueWithTask(new Continuation() {
                @Override
                public Object then(Task task) throws Exception {
                    if (task.isCancelled() || task.isFaulted()) {
                        // 安装失败
                        if (listener != null) {
                            listener.showToast(ReadFileUtils.INSTANCE().getString(R.string.udisk_upgrade_install_fail));
                        }
                    }
                    return null;
                }
            });
        }

    }

    /**
     * 清空数据卸载 apk
     */
    private static void cleanAndUninstallApk() {
        Task task = Task.forResult(null);
        task = task.continueWithTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                return FileTaskUtils.deleteTask(UpgradePath.STORAGE_LOCAL_MAIN_DIR_PATH);
            }
        });
        task = task.continueWithTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                AppUtils.uninstallApp(AppUtils.getAppInfo().getPackageName());
                return null;
            }
        });
    }

    /**
     * todo 特殊文件读取时需要添加
     */
    private static void upgradeConfig(UpgradeBean bean, FragmentActivity activity, String udiskPath, String fileName, UpgradeListener listener) {
        if (listener != null) {
            listener.showLoadingDialog(true, null, null);
        }
        Task task = Task.forResult(null);
        task = task.continueWithTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                return task;
            }
        }, Task.BACKGROUND_EXECUTOR);
        // 检测U盘配置文件是否存在()
        task = task.continueWithTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                boolean isExist = FileUtils.isFileExists(new File(udiskPath + fileName));
                if (isExist) {
                    return Task.forResult(null);
                }
                return Task.forError(new Exception("无U盘数据"));
            }
        });
        task = task.onSuccessTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                TaskCompletionSource taskIsXlsx = new TaskCompletionSource();
                taskIsXlsx.trySetResult(false);
                Task task1 = Task.forResult(null);
                for (String s : bean.getCopyPath()) {
                    Log.d(TAG, "then: udiskPath + s = " + udiskPath + s);
                    TaskCompletionSource taskCompletionSource = new TaskCompletionSource();
                    task1 = task1.continueWithTask(new Continuation() {
                        @Override
                        public Object then(Task task) throws Exception {
                            if (s.startsWith(UpgradePath.APP_DIR_PATH)) {
                                if (bean.isCover()) {
                                    // apk 仅拷贝不安装
                                    justInstallApk(false, activity, udiskPath, fileName, null);
                                }
                            } else if (s.endsWith(UpgradePath.COLOR_FILE_NAME)) {
                                // color
                                task = task.continueWithTask(new Continuation() {
                                    @Override
                                    public Object then(Task task) throws Exception {
                                        return reloadColor(activity, bean.isCover(), udiskPath, fileName);
                                    }
                                });
                                task = task.continueWithTask(new Continuation() {
                                    @Override
                                    public Object then(Task task) throws Exception {
                                        return setThemeColor(activity);
                                    }
                                });
                            } else if (s.startsWith(UpgradePath.LOGO_DIR_PATH)) {
                                // 开机动画
                                task = task.continueWithTask(new Continuation() {
                                    @Override
                                    public Object then(Task task) throws Exception {
                                        return FileTaskUtils.copyTask(bean.isCover(), udiskPath + s,
                                                UpgradePath.STORAGE_LOCAL_PATH + File.separator + s, null);
                                    }
                                });
                                task = task.continueWithTask(new Continuation() {
                                    @Override
                                    public Object then(Task task) throws Exception {
                                        return bootAnimation(activity, false, udiskPath, fileName);
                                    }
                                });
                            } else if (s.endsWith(UpgradePath.AUTHENTICATION_SUB_FILE_PATH)) {
                                // 读取耗材并更新
                                task = task.continueWithTask(new Continuation() {
                                    @Override
                                    public Object then(Task task) throws Exception {
                                        return loadConsumable(activity, bean.isCover(), udiskPath, fileName, listener);
                                    }
                                });
                            } else if (s.endsWith(UpgradePath.BLUE_FILTER)) {
                                task = task.continueWithTask(new Continuation() {
                                    @Override
                                    public Object then(Task task) throws Exception {
                                        return loadBlue(activity, bean.isCover(), udiskPath, fileName, listener);
                                    }
                                });
                            } else if (s.endsWith(UpgradePath.CAMERA_CONFIG)) {
                                task = task.continueWithTask(new Continuation() {
                                    @Override
                                    public Object then(Task task) throws Exception {
                                        return loadCamera(activity, bean.isCover(), udiskPath, fileName, listener);
                                    }
                                });
                            } else if (s.endsWith(UpgradePath.SCORE_FILE_PATH)) {
                                task = task.continueWithTask(new Continuation() {
                                    @Override
                                    public Object then(Task task) throws Exception {
                                        return loadScore(activity, bean.isCover(), udiskPath, fileName, listener);
                                    }
                                });
                            } else {
                                task = task.continueWithTask(new Continuation() {
                                    @Override
                                    public Object then(Task task) throws Exception {
                                        Log.d(TAG, "then: udiskPath + s" + udiskPath + s);
                                        return FileTaskUtils.copyTask(bean.isCover(), udiskPath + s,
                                                UpgradePath.STORAGE_LOCAL_PATH + File.separator + s, new FileTaskUtils.ProcessListener() {
                                                    @Override
                                                    public void onFile(String src, String dest) {
                                                        if (s.endsWith(UpgradePath.OFFICE_CONFIGURATION_FILE_NAME)) {
                                                            taskIsXlsx.trySetResult(true);
                                                        }
                                                    }
                                                });
                                    }
                                });
                            }

                            return task;
                        }
                    });
                    task1 = task1.continueWithTask(new Continuation() {
                        @Override
                        public Object then(Task task) throws Exception {
                            taskCompletionSource.trySetResult(task);
                            return taskCompletionSource.getTask();
                        }
                    });
                    task1.waitForCompletion();
                }
                task1 = task1.continueWithTask(new Continuation() {
                    @Override
                    public Object then(Task task) throws Exception {
                        if ((boolean) taskIsXlsx.getTask().getResult()) {
                            if (listener != null) {
                                task = reloadLanguages(activity, false);
                                task = task.onSuccessTask(new Continuation() {
                                    @Override
                                    public Object then(Task task) throws Exception {
                                        listener.upgradeCleanDb();
                                        return task;
                                    }
                                });
                                task = task.onSuccessTask(new Continuation() {
                                    @Override
                                    public Object then(Task task) throws Exception {
                                        // 语言列表
                                        List<String> languages = (List<String>) task.getResult();
                                        listener.upgradeInitDb(languages);
                                        return task;
                                    }
                                });
                            }
                        }
                        return task;
                    }
                });

                return task;

            }
        }, Task.BACKGROUND_EXECUTOR);

        task = task.continueWithTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                if (listener != null) {
                    listener.showLoadingDialog(false, null, null);
                    listener.upgradeRefreshView();
                }
                return task;
            }
        });
    }

}
