package com.aimyskin.udiskupgrademodule;

import java.util.List;

/**
 * 升级监听
 *
 * @author kang
 * 2023年2月6日16:12:53
 */
public interface UpgradeListener {

    /** todo 主线程
     * 获取U盘中配置文件信息进行弹窗提醒
     */
    void showInstallDialog(UpgradeBean bean);

    /**
     * U盘检测失败执行该方法，将不在执行showInstallDialog方法
     * 可以进行网络判断等。
     */
    default void uDiskFailed() {
    }

    /** todo 主线程
     * 显示加载弹窗，
     * 更新文字内容
     * 关闭弹窗
     */
    void showLoadingDialog(boolean isShow, String srcPath, String destPath);


    /**
     * 读取json到数据库中
     *
     * @param languages
     */
    void upgradeInitDb(List<String> languages);

    /**
     * 清理数据库
     */
    void upgradeCleanAllDb();

    void upgradeCleanDb();
    default void dataToUdisk(){}

    /**
     * 读取耗材
     *
     * @param result
     */
    default void upgradeDidConsumableLoaded(String result){}

    /**
     * 读取蓝牙过滤
     * @param result
     */
    default void upgradeBlueLoaded(String result){}

    /**
     * 读取相机参数
     * @param result
     */
    default void upgradeCameraLoaded(String result){}

    /**
     * 读取检测分数
     */
    default void upgradeScoreLoaded(String result){}

    /**todo 主线程
     * 刷新view
     */
    void upgradeRefreshView();

    /**
     * todo 主线程
     * @param result
     */
    default void showToast(String result){}
}
