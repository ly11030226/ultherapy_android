package com.aimyskin.serialmodule.serialport;

import java.util.EnumSet;

public interface SerialPort {
    enum ControlLine {
        RTS("RTS"),
        CTS("CTS"),
        DTR("DTR"),
        DSR("DSR"),
        CD("CD"),
        RI("RI");

        String name;

        ControlLine(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    boolean isOpen();

    void open() throws Exception;

    void close() throws Exception;

    byte[] read() throws Exception;

    void write(byte[] buffer) throws Exception;

    EnumSet<ControlLine> getSupportedControlLines() throws Exception;

    boolean getControlLine(ControlLine controlLine) throws Exception;

    void setControlLine(ControlLine controlLine, boolean value) throws Exception;
}
