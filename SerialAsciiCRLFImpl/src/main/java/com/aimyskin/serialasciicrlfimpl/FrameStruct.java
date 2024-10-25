package com.aimyskin.serialasciicrlfimpl;

import java.util.Arrays;

import javolution.io.Struct;

public class FrameStruct extends Struct {
    public Unsigned16 header = new Unsigned16();
    public Unsigned16 length = new Unsigned16();
    public Unsigned16 id = new Unsigned16();
    public Unsigned8 instruct = new Unsigned8();
    public Unsigned16 deviceAddress = new Unsigned16();
    public Unsigned16 functionAddress = new Unsigned16();
    public Unsigned8[] datas = array(new Unsigned8[2048]);

    @Override
    public boolean isPacked() {
        return true;
    }

    public String getHeaderHex() {
        return String.format("%04X", header.get());
    }

    public String getLengthHex() {
        return String.format("%04X", length.get());
    }

    public String getIdHex() {
        return String.format("%04X", id.get());
    }

    public String getInstructHex() {
        return String.format("%02X", instruct.get());
    }

    public String getDeviceAddressHex() {
        return String.format("%04X", deviceAddress.get());
    }

    public String getFunctionAddressHex() {
        return String.format("%04X", functionAddress.get());
    }

    @Override
    public String toString() {
        return "frameStruct{" +
                "header=" + getHeaderHex() +
                ", length=" + getLengthHex() +
                ", id=" + getIdHex() +
                ", instruct=" + getInstructHex() +
                ", deviceAddress=" + getDeviceAddressHex() +
                ", functionAddress=" + getFunctionAddressHex() +
                ", datas=" + Arrays.toString(datas) +
                '}';
    }
}
