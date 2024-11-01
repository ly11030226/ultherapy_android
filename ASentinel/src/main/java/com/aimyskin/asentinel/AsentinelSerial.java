package com.aimyskin.asentinel;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.text.TextUtils;

import com.aimyskin.serialasciicrlfimpl.AsciiCRLFCallInterceptor;
import com.aimyskin.serialasciicrlfimpl.FrameCheckInterceptor;
import com.aimyskin.serialasciicrlfimpl.FrameSetRandomIdInterceptor;
import com.aimyskin.serialasciicrlfimpl.FrameStruct;
import com.aimyskin.serialasciicrlfimpl.HexToAsciiInterceptor;
import com.aimyskin.serialasciicrlfimpl.XorOperateInterceptor;
import com.aimyskin.serialmodule.Request;
import com.aimyskin.serialmodule.Response;
import com.aimyskin.serialmodule.SerialClient;
import com.aimyskin.serialmodule.interceptor.LogAsciiInterceptor;
import com.aimyskin.serialmodule.interceptor.LogHexInterceptor;
import com.aimyskin.serialmodule.internal.Call;
import com.aimyskin.serialmodule.internal.Callback;
import com.aimyskin.serialmodule.serialport.SerialPortCommon;
import com.aimyskin.serialmodule.serialport.SerialPortUsb;
import com.blankj.utilcode.util.LogUtils;
import com.elvishew.xlog.XLog;

import java.nio.ByteBuffer;
import java.util.concurrent.Callable;

import bolts.Task;
import okio.ByteString;

public class AsentinelSerial {

    private static final Object lock = new Object();
    private static SerialClient serialClient = null;
    private static String defaultPath = "/dev/ttyS3";

    public interface SentinelCallback {
        void onResult(int code, String data, String message);
    }
    public static boolean isOpen(){
        if (serialClient != null &&  serialClient.serialPort()!= null ){
            return serialClient.serialPort().isOpen();
        }
        return false;
    }

    public static void open(UsbManager usbManager,UsbDevice usbDevice, boolean debug) {
        try {
            if (serialClient != null) {
                close();
            }
            SerialPortUsb serialPortUsb = SerialPortUsb.Builder.aSerialPortUsb()
                    .usbManager(usbManager)
                    .device(usbDevice)
                    .baudrate(115200)
                    .build();
            serialPortUsb.open();

            if (debug) {
                serialClient = SerialClient.Builder.aSerialClient()
                        .serialPort(serialPortUsb)
                        .addInterceptor(FrameCheckInterceptor.class)
                        .addInterceptor(FrameSetRandomIdInterceptor.class)
                        .addInterceptor(LogHexInterceptor.class)
                        .addInterceptor(XorOperateInterceptor.class)
                        .addInterceptor(HexToAsciiInterceptor.class)
                        .addInterceptor(LogAsciiInterceptor.class)
                        .addCallInterceptor(AsciiCRLFCallInterceptor.class)
                        .build();
            } else {
                serialClient = SerialClient.Builder.aSerialClient()
                        .serialPort(serialPortUsb)
                        .addInterceptor(FrameCheckInterceptor.class)
                        .addInterceptor(FrameSetRandomIdInterceptor.class)
                        .addInterceptor(XorOperateInterceptor.class)
                        .addInterceptor(HexToAsciiInterceptor.class)
                        .addCallInterceptor(AsciiCRLFCallInterceptor.class)
                        .build();
            }
        } catch (Exception e) {
            XLog.e("open: ", e);
        }
    }

    public static void open(String serialPath, boolean debug) {
        String path = TextUtils.isEmpty(serialPath) ? defaultPath : serialPath;
        try {
            if (serialClient != null) {
                close();
            }
            SerialPortCommon serialPort = SerialPortCommon.Builder.aSerialPortCommon()
                    .serialPath(path)
                    .baudrate(115200)
                    .build();
            serialPort.open();
            if (debug) {
                serialClient = SerialClient.Builder.aSerialClient()
                        .serialPort(serialPort)
                        .addInterceptor(FrameCheckInterceptor.class)
                        .addInterceptor(FrameSetRandomIdInterceptor.class)
                        .addInterceptor(LogHexInterceptor.class)
                        .addInterceptor(XorOperateInterceptor.class)
                        .addInterceptor(HexToAsciiInterceptor.class)
                        .addInterceptor(LogAsciiInterceptor.class)
                        .addCallInterceptor(AsciiCRLFCallInterceptor.class)
                        .build();
            } else {
                serialClient = SerialClient.Builder.aSerialClient()
                        .serialPort(serialPort)
                        .addInterceptor(FrameCheckInterceptor.class)
                        .addInterceptor(FrameSetRandomIdInterceptor.class)
                        .addInterceptor(XorOperateInterceptor.class)
                        .addInterceptor(HexToAsciiInterceptor.class)
                        .addCallInterceptor(AsciiCRLFCallInterceptor.class)
                        .build();
            }
        } catch (Exception e) {
            XLog.e("open: ", e);
        }
    }

    public static void close() {
        if (serialClient != null) {
            try {
                serialClient.serialPort().close();
            } catch (Exception e) {
                XLog.e("close: ", e);
            }
            serialClient = null;
        }
    }

    /**
     * 通过获取版本号检查 sentinel 是否通讯正常
     *
     * @param callback
     */
    public static void checkSentinel(SentinelCallback callback) {
        synchronized (lock) {
            String requestString = "7E7E 0009 FFFF 02 0002 0000 0004".replaceAll(" ", "");
            Request request = new Request(ByteString.decodeHex(requestString));

            callSentinel(request, callback, 2000);
        }
    }

    /**
     * 获取 sentinel ID
     *
     * @param callback
     */
    public static void getSentinelID(SentinelCallback callback) {
        synchronized (lock) {
            String requestString = "7E7E 0009 FFFF 02 0002 0010 0010".replaceAll(" ", "");
            Request request = new Request(ByteString.decodeHex(requestString));

            callSentinel(request, callback, 2000);
        }
    }

    /**
     * 执行 sentinel
     *
     * @param funcAddress
     * @param dataIn
     * @param callback
     */
    public static void doSentinel(int funcAddress, String dataIn, SentinelCallback callback) {
        synchronized (lock) {
//            LogUtils.d("****************** create requestFrame start ******************");
            byte[] requestData = new byte[11];
            FrameStruct requestFrame = new FrameStruct();
            requestFrame.setByteBuffer(ByteBuffer.wrap(requestData), 0);
            requestFrame.header.set(0x7e7e);
            requestFrame.length.set(0x0007 + ByteString.decodeHex(dataIn).toByteArray().length);
            requestFrame.id.set(0xFFFF);
            requestFrame.instruct.set((short) 0x01);
            requestFrame.deviceAddress.set(0x0002);
            requestFrame.functionAddress.set(funcAddress);

            String requestString = ByteString.of(requestFrame.getByteBuffer()).hex();
            requestString = requestString + dataIn;
            requestString = requestString.replaceAll(" ", "");
            Request request = new Request(ByteString.decodeHex(requestString));

//            LogUtils.d("****************** create requestFrame end ******************");

            callSentinel(request, callback, 3000);
        }
    }

    private static void callSentinel(Request request, SentinelCallback callback, int timeout) {
        SentinelCallback callbackOnUiThread = new SentinelCallback() {
            @Override
            public void onResult(int code, String data, String message) {
                Task.call(new Callable<Object>() {
                    @Override
                    public Object call() throws Exception {
                        if (callback != null) {
                            callback.onResult(code, data, message);
                        }
                        return null;
                    }
                }, Task.UI_THREAD_EXECUTOR);
            }
        };

        if (serialClient == null) {
            callbackOnUiThread.onResult(1, null, "Communicate port is null");
            return;
        }

        ByteBuffer requestByteBuffer = request.data.asByteBuffer();
        FrameStruct requestFrame = new FrameStruct();
        requestFrame.setByteBuffer(requestByteBuffer, 0);

//        LogUtils.d("****************** send requestFrame start ******************");

        serialClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, Exception e) {
//                LogUtils.e("****************** send requestFrame fail ******************");
                callbackOnUiThread.onResult(2, null, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws Exception {
//                LogUtils.d("****************** send requestFrame success ******************");
                // 解析数据
                byte[] datas = response.data.toByteArray();
                FrameStruct frame = new FrameStruct();
                frame.setByteBuffer(ByteBuffer.wrap(datas), 0);
                // 数据校验
                if (frame.instruct.get() != requestFrame.instruct.get() ||
                        frame.id.get() != requestFrame.id.get() ||
                        frame.deviceAddress.get() != requestFrame.deviceAddress.get() ||
                        frame.functionAddress.get() != requestFrame.functionAddress.get()) {
                    String message = "Response check error";
                    XLog.d("onResponse: " + message);
                    callbackOnUiThread.onResult(3, null, message);
                }
                // 返回数据
                else {
                    ByteString dataString = response.data.substring(frame.datas[0].position());
                    callbackOnUiThread.onResult(0, dataString.hex(), null);
                }
            }
        }, timeout);
    }
}
