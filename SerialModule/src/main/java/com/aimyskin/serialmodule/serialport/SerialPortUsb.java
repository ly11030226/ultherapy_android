package com.aimyskin.serialmodule.serialport;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;

import java.nio.ByteBuffer;
import java.util.EnumSet;

import okio.Buffer;

public class SerialPortUsb implements SerialPort {

    private UsbManager usbManager;
    public UsbDevice device;
    private UsbSerialDriver usbSerialDriver;
    private UsbSerialPort port;
    private ByteBuffer mReadBuffer = ByteBuffer.allocate(16 * 1024); // 大小取 UsbDeviceConnection.bulkTransfer 最大值
    private int baudrate = 115200;
    private int dataBits = 8;
    private int parity = 0;
    private int stopBits = 1;

    public UsbDevice getDevice() {
        return device;
    }

    public void setDevice(UsbDevice device) {
        this.device = device;
        this.usbSerialDriver = UsbSerialProber.getDefaultProber().probeDevice(this.device);
    }

    public UsbManager getUsbManager() {
        return usbManager;
    }

    public void setUsbManager(UsbManager usbManager) {
        this.usbManager = usbManager;
    }

    @Override
    public boolean isOpen() {
        if (port == null) {
            return false;
        }
        return port.isOpen();
    }

    @Override
    public void open() throws Exception {
        for (UsbSerialPort port : usbSerialDriver.getPorts()) {
            if (port.getDevice().equals(device)) {
                if (port.isOpen()) {
                    this.port = port;
                    return;
                }
                UsbDeviceConnection connection = usbManager.openDevice(port.getDriver().getDevice());
                if (connection == null) {
                    this.port = null;
                    return;
                }
                port.open(connection);
                port.setParameters(baudrate, dataBits, stopBits, parity);
                this.port = port;
                return;
            }
        }
    }

    @Override
    public void close() throws Exception {
        port.close();
        port = null;
    }

    @Override
    public byte[] read() throws Exception {
        Buffer buffer = new Buffer();
        if (!isOpen()) {
            return buffer.readByteArray();
        }
        int bytePreSecond = baudrate / 10;
        int timeoutMS = ((mReadBuffer.array().length + bytePreSecond - 1) / bytePreSecond) * 1000;
        int count = this.port.read(mReadBuffer.array(), timeoutMS);
        if (count > 0) {
            buffer.write(mReadBuffer.array(), 0, count);
            mReadBuffer.clear();
        }
        return buffer.readByteArray();
    }

    @Override
    public void write(byte[] buffer) throws Exception {
        if (!isOpen()) {
            return;
        }
        port.write(buffer, 0);
    }

    @Override
    public EnumSet<ControlLine> getSupportedControlLines() throws Exception {
        EnumSet<UsbSerialPort.ControlLine> usbControlLines = this.port.getSupportedControlLines();
        EnumSet<ControlLine> controlLines = EnumSet.noneOf(ControlLine.class);
        for (UsbSerialPort.ControlLine usbControlLine : usbControlLines) {
            ControlLine controlLine = usbControlLine2ControlLine(usbControlLine);
            if (controlLine != null) {
                controlLines.add(controlLine);
            }
        }
        return controlLines;
    }

    @Override
    public boolean getControlLine(ControlLine controlLine) throws Exception {
        switch (controlLine) {
            case RTS:
                return this.port.getRTS();
            case CTS:
                return this.port.getCTS();
            case DTR:
                return this.port.getDTR();
            case DSR:
                return this.port.getDSR();
            case CD:
                return this.port.getCD();
            case RI:
                return this.port.getRI();
            default:
                throw new Exception(String.format("Get %s unsupported", controlLine.getName()));
        }
    }

    @Override
    public void setControlLine(ControlLine controlLine, boolean value) throws Exception {
        switch (controlLine) {
            case RTS:
                this.port.setRTS(value);
                break;
            case DTR:
                this.port.setDTR(value);
                break;
            default:
                throw new Exception(String.format("Set %s unsupported: value = %s", controlLine.getName(), value));
        }
    }

    private ControlLine usbControlLine2ControlLine(UsbSerialPort.ControlLine controlLine) {
        switch (controlLine) {
            case RTS:
                return ControlLine.RTS;
            case CTS:
                return ControlLine.CTS;
            case DTR:
                return ControlLine.DTR;
            case DSR:
                return ControlLine.DSR;
            case CD:
                return ControlLine.CD;
            case RI:
                return ControlLine.RI;
        }
        return null;
    }

    public static final class Builder {
        private UsbManager usbManager;
        private UsbDevice device;
        private int baudrate = 115200;
        private int dataBits = 8;
        private int parity = 0;
        private int stopBits = 1;

        private Builder() {
        }

        public static Builder aSerialPortUsb() {
            return new Builder();
        }

        public Builder usbManager(UsbManager usbManager) {
            this.usbManager = usbManager;
            return this;
        }

        public Builder device(UsbDevice device) {
            this.device = device;
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

        public SerialPortUsb build() {
            SerialPortUsb serialPortUsb = new SerialPortUsb();
            serialPortUsb.setUsbManager(usbManager);
            serialPortUsb.setDevice(device);
            serialPortUsb.baudrate = this.baudrate;
            serialPortUsb.dataBits = this.dataBits;
            serialPortUsb.stopBits = this.stopBits;
            serialPortUsb.parity = this.parity;
            return serialPortUsb;
        }
    }
}
