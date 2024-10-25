package com.aimyskin.laserserialmodule.responseClassify.device808Classify;

public enum CommandWordEnum {

    COMMAND_WORD_92(0x92, "写入"),
    COMMAND_WORD_93(0x93, "读取"),
    COMMAND_WORD_94(0x94, "主动上报"),
    COMMAND_WORD_80(0x80, "功能码错误"),
    COMMAND_WORD_81(0x81, "命令长度错误"),
    COMMAND_WORD_82(0x82, "读出寄存器个数错误"),
    COMMAND_WORD_83(0x83, "寄存器地址错误"),
    COMMAND_WORD_84(0x84, "帧头错误"),
    COMMAND_WORD_85(0x85, "CRC校验错误");

    int commandWord;
    String describe;

    CommandWordEnum(int commandWord, String describe) {
        this.commandWord = commandWord;
        this.describe = describe;
    }

    public int getCommandWord() {
        return commandWord;
    }

}
