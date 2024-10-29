package com.aimyskin.serialmodule.serialport;

import android.util.Log;

import java.io.File;
import java.util.EnumSet;

import okio.Buffer;

public class SerialPortCommon implements SerialPort {

    private android.serialport.SerialPort serialPort;
    private String serialPath;
    private byte[] mReadBuffer = new byte[14 * 1024]; // 值同 SerialPortUsb
    private int baudrate = 115200;
    private int dataBits = 8;
    private int parity = 0;
    private int stopBits = 1;

    @Override
    public boolean isOpen() {
        return serialPort != null;
    }

    @Override
    public void open() throws Exception {
        if (isOpen()) {
            return;
        }
        Log.e("SerialPortCommon", serialPath + baudrate + "---" + dataBits + "----" + parity + "----" + stopBits);
        serialPort = new android.serialport.SerialPort(new File(serialPath), baudrate, dataBits, parity, stopBits);
    }

    @Override
    public void close() throws Exception {
        try {
            if (serialPort != null && serialPort.getInputStream() != null) {
                serialPort.getInputStream().close();
            }
            if (serialPort != null && serialPort.getOutputStream() != null) {
                serialPort.getOutputStream().close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            serialPort = null;
        }
    }

    @Override
    public byte[] read() throws Exception {
        Buffer buffer = new Buffer();
        if (!isOpen() || serialPort.getInputStream().available() == 0) {
            return buffer.readByteArray();
        }
        int count = serialPort.getInputStream().read(mReadBuffer);
        if (count > 0) {
            buffer.write(mReadBuffer, 0, count);
        }
        return buffer.readByteArray();
    }

    @Override
    public void write(byte[] buffer) throws Exception {
        if (!isOpen()) {
            return;
        }
        Buffer writeBuffer = new Buffer();
        writeBuffer.write(buffer);
        writeBuffer.writeTo(serialPort.getOutputStream());
    }

    @Override
    public EnumSet<ControlLine> getSupportedControlLines() throws Exception {
        return EnumSet.noneOf(ControlLine.class);
    }

    @Override
    public boolean getControlLine(ControlLine controlLine) throws Exception {
        throw new Exception(String.format("Get %s unsupported", controlLine.getName()));
    }

    @Override
    public void setControlLine(ControlLine controlLine, boolean value) throws Exception {
        throw new Exception(String.format("Set %s unsupported: value = %s", controlLine.getName(), value));
    }

    public static final class Builder {
        private String serialPath;
        private int baudrate = 115200;
        private int dataBits = 8;
        private int parity = 0;
        private int stopBits = 1;

        private Builder() {
        }

        public static Builder aSerialPortCommon() {
            return new Builder();
        }

        public Builder serialPath(String serialPath) {
            this.serialPath = serialPath;
            return this;
        }

        public Builder baudrate(int baudrate) {
            this.baudrate = baudrate;
            return this;
        }

        public Builder dataBits(int dataBits) {
            this.dataBits = dataBits;
            return this;
        }

        public Builder parity(int parity) {
            this.parity = parity;
            return this;
        }

        public Builder stopBits(int stopBits) {
            this.stopBits = stopBits;
            return this;
        }

        public SerialPortCommon build() {
            SerialPortCommon serialPortCommon = new SerialPortCommon();
            serialPortCommon.serialPath = this.serialPath;
            serialPortCommon.baudrate = this.baudrate;
            serialPortCommon.parity = this.parity;
            serialPortCommon.dataBits = this.dataBits;
            serialPortCommon.stopBits = this.stopBits;
            return serialPortCommon;
        }
    }
}
