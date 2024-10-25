package com.aimyskin.laserserialmodule.interceptor;

import javolution.io.Struct;

public class Frame5AA5Struct extends Struct {

    public Unsigned16 header = new Unsigned16();// 帧头
    public Unsigned8 instruct = new Unsigned8();// 命令
    public Unsigned16 address = new Unsigned16();// 地址
    public Unsigned8 addressNum = new Unsigned8();// 长度
    public Unsigned16[] datas = array(new Unsigned16[2048]);


    @Override
    public boolean isPacked() {
        return true;
    }
}
