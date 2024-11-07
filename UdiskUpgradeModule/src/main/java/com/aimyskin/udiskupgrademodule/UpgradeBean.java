package com.aimyskin.udiskupgrademodule;


import java.util.ArrayList;
import java.util.List;

/**
 * 配置文件中读一行一组数据，用分号隔开
 * 文件同名替换或者不替换
 * install_mode
 * 1，清空数据库所有数据，清空本地文件，U盘拷贝到本地。安装apk
 * 2，清空数据库资源添加到数据库中的表数据，清空本地文件，U盘拷贝到本地。安装apk
 * 3，安装apk
 * 4，卸载apk
 * 5，根据isCover同名是否替换，将copyPath指定目录拷贝到本地，并正对不同文件显示不同操作
 * 6，将本地文件全部导出到U盘中
 */
public class UpgradeBean {

    private int install_mode = 0;
    private List<String> copy_path = new ArrayList<>();// ,分割 /斜线路径
    private boolean is_cover = true;// true则先删除在复制 false 不进行复制文件

    public String getInstallModeName() {
        return "install_mode";
    }

    public String getCopyPathName() {
        return "copy_path";
    }

    public String getIsCoverName() {
        return "is_cover";
    }


    public List<String> getCopyPath() {
        return copy_path;
    }

    public void setCopyPath(List<String> copyPath) {
        this.copy_path = copyPath;
    }

    public boolean isCover() {
        return is_cover;
    }

    public void setCover(boolean cover) {
        is_cover = cover;
    }

    public int getInstall_mode() {
        return install_mode;
    }

    public void setInstall_mode(int install_mode) {
        this.install_mode = install_mode;
    }
}
