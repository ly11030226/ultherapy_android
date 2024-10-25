package com.aimyskin.serialmodule.serialport;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.lang.ref.WeakReference;
import java.util.EnumSet;

import okio.Buffer;
import okio.ByteString;

/**
 * adb shell am broadcast -a com.aimyskin.serialmodule.ACTION_SERIAL_PORT_ADB_DEBUG --es hex "7E7E0101"
 * adb shell am broadcast -a com.aimyskin.serialmodule.ACTION_SERIAL_PORT_ADB_DEBUG --es ascii "7E7E0101\\r\\n"
 */
public class SerialPortADBDebug implements SerialPort {

    public static final String ACTION_SERIAL_PORT_ADB_DEBUG = "com.aimyskin.serialmodule.ACTION_SERIAL_PORT_ADB_DEBUG";

    private boolean serialPortOpened;
    private WeakReference<Context> weakContext;

    private byte[] mReadBuffer = new byte[14 * 1024]; // 值同 SerialPortUsb
    private PipedOutputStream outputStream;
    private PipedInputStream inputStream;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!ACTION_SERIAL_PORT_ADB_DEBUG.equals(intent.getAction())) {
                return;
            }
            Buffer buffer = new Buffer();

            String hexData = intent.getStringExtra("hex");
            hexData = (hexData == null) ? "" : hexData;
            buffer.write(ByteString.decodeHex(hexData));

            String asciiData = intent.getStringExtra("ascii");
            asciiData = (asciiData == null) ? "" : asciiData;
            // adb windows:"rn"  mac:"\r\n"
            String targetR = asciiData.contains("\\r") ? "\\r" : "r";
            String targetN = asciiData.contains("\\n") ? "\\n" : "n";
            asciiData = asciiData.replace(targetR, "\r");
            asciiData = asciiData.replace(targetN, "\n");
            // 写入 buffer
            buffer.write(ByteString.encodeUtf8(asciiData));

            try {
                outputStream.write(buffer.readByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public boolean isOpen() {
        return serialPortOpened;
    }

    @Override
    public void open() throws Exception {
        if (isOpen()) {
            return;
        }
        if (weakContext != null) {
            outputStream = new PipedOutputStream();
            inputStream = new PipedInputStream(outputStream);
            weakContext.get().registerReceiver(broadcastReceiver, new IntentFilter(ACTION_SERIAL_PORT_ADB_DEBUG));
        }
        serialPortOpened = true;
    }

    @Override
    public void close() throws Exception {
        if (isOpen()) {
            if (weakContext != null) {
                weakContext.get().unregisterReceiver(broadcastReceiver);
                outputStream.close();
                inputStream.close();
            }
        }
        serialPortOpened = false;
    }

    @Override
    public byte[] read() throws Exception {
        Buffer buffer = new Buffer();
        if (!isOpen() || inputStream.available() == 0) {
            return buffer.readByteArray();
        }
        int count = inputStream.read(mReadBuffer);
        if (count > 0) {
            buffer.write(mReadBuffer, 0, count);
        }
        return buffer.readByteArray();
    }

    @Override
    public void write(byte[] buffer) throws Exception {

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
        private Context context;

        private Builder() {
        }

        public static Builder aSerialPortADBDebug() {
            return new Builder();
        }

        public Builder context(Context context) {
            this.context = context;
            return this;
        }

        public SerialPortADBDebug build() {
            SerialPortADBDebug serialPortADBDebug = new SerialPortADBDebug();
            serialPortADBDebug.weakContext = new WeakReference<Context>(this.context);
            return serialPortADBDebug;
        }
    }
}
