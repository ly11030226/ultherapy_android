package com.aimyskin.serialasciicrlfimpl;

import com.aimyskin.serialasciicrlfimpl.serialport.SerialPortMock;
import com.aimyskin.serialmodule.EventListener;
import com.aimyskin.serialmodule.RealPipeline;
import com.aimyskin.serialmodule.Request;
import com.aimyskin.serialmodule.Response;
import com.aimyskin.serialmodule.SerialClient;
import com.aimyskin.serialmodule.internal.Call;
import com.aimyskin.serialmodule.internal.Callback;
import com.aimyskin.serialmodule.serialport.SerialPort;
import com.elvishew.xlog.XLog;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import bolts.Continuation;
import bolts.Task;
import okio.ByteString;

import static java.lang.Thread.sleep;
import static javolution.testing.TestContext.assertNotNull;

public class ExampleUnitTest {

    @Test
    public void test_serialClient() throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);
        SerialClient client = SerialClient.Builder.aSerialClient()
                .serialPort(new SerialPortMock())
                .addInterceptor(FrameCheckInterceptor.class)
                .addInterceptor(XorOperateInterceptor.class)
                .addInterceptor(HexToAsciiInterceptor.class)
                .addCallInterceptor(AsciiCRLFCallInterceptor.class)
                .eventListener(new EventListener() {
                    @Override
                    public void callStart(Call call) {
                        super.callStart(call);
                    }

                    @Override
                    public void callEnd(Call call) {
                        super.callEnd(call);
                    }

                    @Override
                    public void callFailed(Call call, Exception ioe) {
                        super.callFailed(call, ioe);
                    }
                })
                .build();
        Request request = new Request();
        // 写指令
        ByteString byteString = ByteString.decodeHex("7E7E0008FFFF010006000401");
        request.data = byteString;
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(com.aimyskin.serialmodule.internal.Call call, Exception e) {

            }

            @Override
            public void onResponse(com.aimyskin.serialmodule.internal.Call call, Response response) throws Exception {
                assertNotNull(response);
                latch.countDown();
            }

        }, 100, 200);
        latch.await();
    }

    @Test
    public void test_serialClientSync() {
        SerialClient client = SerialClient.Builder.aSerialClient()
                .serialPort(new SerialPortMock())
                .addInterceptor(FrameCheckInterceptor.class)
                .addInterceptor(XorOperateInterceptor.class)
                .addInterceptor(HexToAsciiInterceptor.class)
                .addCallInterceptor(AsciiCRLFCallInterceptor.class)
                .build();
        Request request = new Request();
        // 写指令
        ByteString byteString = ByteString.decodeHex("7E7E0008FFFF010006000401");
        request.data = byteString;
        Response response = null;
        try {
            response = client.newCall(request).execute(1000);
        } catch (Exception e) {
            XLog.e("test_serialClientSync: ", e);
        }
        assertNotNull(response);
    }

    @Test
    public void test_serialClientTask() throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);
        SerialClient client = SerialClient.Builder.aSerialClient()
                .serialPort(new SerialPortMock())
                .addInterceptor(FrameCheckInterceptor.class)
                .addInterceptor(XorOperateInterceptor.class)
                .addInterceptor(HexToAsciiInterceptor.class)
                .addCallInterceptor(AsciiCRLFCallInterceptor.class)
                .build();
        Task task = Task.forResult(null);
        task = task.continueWithTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                client.serialPort().setControlLine(SerialPort.ControlLine.RTS, false);
                client.serialPort().setControlLine(SerialPort.ControlLine.DSR, false);
                return client.newCallTask(new Request(ByteString.decodeHex("7E7E0008FFFF010006000401"))).enqueue();
            }
        });
        task = task.continueWithTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                client.serialPort().setControlLine(SerialPort.ControlLine.RTS, true);
                client.serialPort().setControlLine(SerialPort.ControlLine.DSR, true);
                return client.newCallTask(new Request(ByteString.decodeHex("7E7E0008FFFF010006000401"))).enqueue(100);
            }
        });
        task = task.continueWithTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                client.serialPort().setControlLine(SerialPort.ControlLine.RTS, false);
                client.serialPort().setControlLine(SerialPort.ControlLine.DSR, true);
                return client.newCallTask(new Request(ByteString.decodeHex("7E7E0008FFFF010006000401"))).enqueue(100, 100);
            }
        });
        task = task.continueWithTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                client.serialPort().setControlLine(SerialPort.ControlLine.RTS, true);
                client.serialPort().setControlLine(SerialPort.ControlLine.DSR, false);
                return client.newCallTask(new Request(ByteString.decodeHex("7E7E0008FFFF010006000401"))).enqueue();
            }
        });
        task = task.continueWithTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                if (!task.isCancelled() && !task.isFaulted()) {
                    assertNotNull(task.getResult());
                    latch.countDown();
                }
                return null;
            }
        });
        latch.await(1000, TimeUnit.MILLISECONDS);
    }

    /**
     * pipeline 请求测试
     */
    @Test
    public void test_pipelineSerialClient() throws InterruptedException, Exception {
        final CountDownLatch latch = new CountDownLatch(3);
        SerialClient client = SerialClient.Builder.aSerialClient()
                .serialPort(new SerialPortMock())
                .addInterceptor(FrameCheckInterceptor.class)
                .addInterceptor(XorOperateInterceptor.class)
                .addInterceptor(HexToAsciiInterceptor.class)
                .addCallInterceptor(AsciiCRLFPipelineInterceptor.class)
                .build();
        RealPipeline pipeline = client.newPipeline(new Callback() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws Exception {
                assertNotNull(response);
                latch.countDown();
            }
        });
        Request request = new Request();
        // 写指令
        ByteString byteString = ByteString.decodeHex("7E7E0008FFFF010006000401");
        request.data = byteString;
        for (int i = 0; i < 3; i++) {
            sleep(10);
            pipeline.pipeout(request, 100);
        }
        latch.await();
    }

    /**
     * 通用串口调用示例
     */
//    @Test
//    public void test_serialClientCommon() throws InterruptedException {
//        final CountDownLatch latch = new CountDownLatch(1);
//        SerialPortCommon serialPortCommon = SerialPortCommon.Builder.aSerialPortCommon()
//                .serialPath("")
//                .baudrate(115200)
//                .build();
//        SerialClient client = SerialClient.Builder.aSerialClient()
//                .serialPort(serialPortCommon)
//                .addInterceptor(FrameCheckInterceptor.class)
//                .addInterceptor(XorOperateInterceptor.class)
//                .addInterceptor(HexToAsciiInterceptor.class)
//                .addCallInterceptor(AsciiCRLFCallInterceptor.class)
//                .build();
//        Request request = new Request();
//        // 写指令
//        ByteString byteString = ByteString.decodeHex("7E7E0008FFFF010006000401");
//        request.data = byteString;
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, Exception e) {
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws Exception {
//                latch.countDown();
//            }
//        });
//        latch.await();
//    }
}
