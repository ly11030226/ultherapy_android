package com.aimyskin.udiskupgrademodule;

import android.os.Environment;

public class UpgradePath {
    public static String MAIN_DIR_NAME = BuildConfig.packageName; // 父文件夹
    public static String LOCAL_COLOR_THEME_NAME = "mini_module_theme_color"; // 主题颜色名称

    public static String APP_DIR_NAME = "app";
    public static String COLOR_DIR_NAME = "color";
    public static String CONSUMABLE_DIR_NAME = "consumable";
    public static String LANGUAGE_DIR_NAME = "language";
    public static String LOGO_DIR_NAME = "logo";
    public static String PICTURE_DIR_NAME = "picture";
    public static String VIDEO_DIR_NAME = "video";
    public static String OTHER_FILE = "other_file"; // 其他文件
    public static String LANGUAGE_AUDIO_DIR_NAME = "audio"; // 语言文件下的音频文件

    public static String COLOR_FILE_NAME = "color.xml";
    public static String AUTHENTICATION_FILE_NAME = "authentication.json";
    public static String AGREEMENT_FILE_NAME = "agreement.html";
    public static String ANALYSE_FILE_NAME = "analyse.json";
    public static String GUIDE_FILE_NAME = "guide.json";
    public static String LANGUAGE_FILE_NAME = "language.xml";
    public static String PRODUCT_FILE_NAME = "product.json";
    public static String BOOT_ANIMATION_FILE_NAME = "bootanimation.zip";
    public static String UPDATE_CONFIG_FILE_NAME = "update_config.cfg";
    public static String SCORE_FILE_NAME = "score.json";
    public static String OFFICE_CONFIGURATION_FILE_NAME = "office_configuration.xlsx";


    public static String APP_DIR_PATH = MAIN_DIR_NAME+"/app";//安装包路径
    public static String CONSUMABLE_DIR_PATH = MAIN_DIR_NAME+"/consumable"; // 代理商文件夹
    public static String SELECT_PICTURE_DIR_PATH = "/sdcard/"+MAIN_DIR_NAME+"/picture";//产品图选择路径
    public static String PICTURE_DIR_PATH = MAIN_DIR_NAME+"/picture";//产品图选择路径
    public static String VIDEO_DIR_PATH = MAIN_DIR_NAME+"/video";//视频路径
    public static String LOGO_DIR_PATH = MAIN_DIR_NAME+"/logo";//logo路径
    public static String LANGUAGE_DIR_PATH = MAIN_DIR_NAME+"/language";//logo路径

    public static String CAMERA_CONFIG = MAIN_DIR_NAME+"/otherFile/camera_config.json";// 相机配置参数
    public static String BLUE_FILTER = MAIN_DIR_NAME+"/otherFile/blue_filter.json";// 蓝牙判断数据
    public static String SCORE_FILE_PATH = MAIN_DIR_NAME+"/otherFile/score.json";// 分数列表 最大值 最小值
    public static String AUTHENTICATION_FILE_PATH = MAIN_DIR_NAME+"/consumable/authentication.json";

    public static String CAMERA_SUB_CONFIG = "/otherFile/camera_config.json";// 相机配置参数
    public static String BLUE_SUB_FILTER = "/otherFile/blue_filter.json";// 蓝牙判断数据
    public static String SCORE_SUB_FILE_PATH = "/otherFile/score.json";// 分数列表 最大值 最小值
    public static String AUTHENTICATION_SUB_FILE_PATH = "/consumable/authentication.json";

    public static String UPDATE_CONFIGURATION_FILE_PATH = MAIN_DIR_NAME+"/update_config.cfg"; //升级配置文件
    public static String BOOTANIMATION_FILE_PATH = "/sdcard/"+MAIN_DIR_NAME+"/logo/bootanimation.zip";//动画
    public static String BOOTANIMATION_FILE_RELATIVE_PATH = MAIN_DIR_NAME+"/logo/bootanimation.zip";//动画
    public static String BOOTANIMATION_SUBFILE_RELATIVE_PATH = "/logo/bootanimation.zip";//动画



    public static String STORAGE_LOCAL_PATH = Environment.getExternalStorageDirectory().getPath();
    public static String STORAGE_LOCAL_MAIN_DIR_PATH = STORAGE_LOCAL_PATH + "/"+MAIN_DIR_NAME ;
    public static String STORAGE_LOCAL_APP_DIR_PATH = STORAGE_LOCAL_PATH + "/"+MAIN_DIR_NAME+"/app" ;
    public static String STORAGE_LOCAL_CONSUMABLE_DIR_PATH = STORAGE_LOCAL_PATH + "/"+MAIN_DIR_NAME+"/consumable" ;
    public static String STORAGE_LOCAL_LANGUAGE_DIR_PATH = STORAGE_LOCAL_PATH + "/"+MAIN_DIR_NAME+"/language";
    public static String STORAGE_LOCAL_LOGO_DIR_PATH = STORAGE_LOCAL_PATH + "/"+MAIN_DIR_NAME+"/logo/";
    public static String STORAGE_LOCAL_LOGO_FILE_PATH = STORAGE_LOCAL_PATH + "/" + BOOTANIMATION_FILE_RELATIVE_PATH;

    public static String STORAGE_LOCAL_DB_DIR_PATH = STORAGE_LOCAL_PATH + "/"+MAIN_DIR_NAME+"/db";

    public static String STORAGE_LOCAL_QR_AUTHENTICATION_FILE_PATH = STORAGE_LOCAL_PATH + "/" + AUTHENTICATION_FILE_PATH; // 耗材认证
    public static String COLOR_PATH = MAIN_DIR_NAME+"/color/color.xml";
    public static String COLOR_SUB_PATH = "/color/color.xml";
    public static String STORAGE_LOCAL_COLOR_FILE_PATH = STORAGE_LOCAL_PATH + "/" + COLOR_PATH;


    public static String AIMYSKIN_TEMPORARY_ZIP_FILE_NAME = "aimyskin_temporary.zip";
    public static String AIMYSKIN_TEMPORARY_DIR_NAME = "aimyskin_temporary";

    public static String LANGUAGE_CATALOG_ITEM_MULTI_FILE_NAME = "catalogItemMultiLanguage.json";
    public static String LANGUAGE_CATALOG_MULTI_FILE_NAME = "catalogMultiLanguage.json";
    public static String LANGUAGE_FUNCTION_MULTI_FILE_NAME = "functionMultiLanguage.json";
    public static String LANGUAGE_TREATMENT_ITEM_MULTI_FILE_NAME = "treatmentItemMultiLanguage.json";
    public static String LANGUAGE_TREATMENT_MULTI_FILE_NAME = "treatmentMultiLanguage.json";
    public static String DB_CATALOG_FILE_NAME = "catalog.json";
    public static String DB_CONSUMABLE_FILE_NAME = "consumable.json";
    public static String DB_FUNCTION_FILE_NAME = "function.json";
    public static String DB_TREATMENT_FILE_NAME = "treatment.json";
    public static String DB_TREATMENT_ITEM_FILE_NAME = "treatmentItem.json";



}
