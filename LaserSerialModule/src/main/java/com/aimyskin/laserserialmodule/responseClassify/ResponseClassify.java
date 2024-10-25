package com.aimyskin.laserserialmodule.responseClassify;


import com.aimyskin.laserserialmodule.responseClassify.device808.OnButtonListener;

/**
 * 处理返回的数据
 */
public interface ResponseClassify {

    default void setOnButtonListener(OnButtonListener onButtonListener){}

    void sendData(String data, int delay);

    void close();


}
