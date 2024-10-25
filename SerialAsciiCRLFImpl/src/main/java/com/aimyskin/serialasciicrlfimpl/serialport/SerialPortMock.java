package com.aimyskin.serialasciicrlfimpl.serialport;

import com.aimyskin.serialasciicrlfimpl.FrameCheckInterceptor;
import com.aimyskin.serialasciicrlfimpl.FrameStruct;
import com.aimyskin.serialasciicrlfimpl.HexToAsciiInterceptor;
import com.aimyskin.serialasciicrlfimpl.XorOperateInterceptor;
import com.aimyskin.serialmodule.Response;
import com.aimyskin.serialmodule.interceptor.Interceptor;
import com.aimyskin.serialmodule.serialport.SerialPort;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;

import okio.ByteString;

public class SerialPortMock implements SerialPort {
    private Queue<ByteString> respMessages = new LinkedBlockingQueue<>();
    private boolean opened;
    private Timer timer;

    /**
     * 测试 pipeline 可以打开测试串口不完整帧接收
     */
    private void startTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                respMessages.offer(ByteString.encodeUtf8("7E7E000BFFFF02"));
                respMessages.offer(ByteString.encodeUtf8("0006000000010000"));
                respMessages.offer(ByteString.encodeUtf8("0000"));
                respMessages.offer(ByteString.encodeUtf8("\r\n"));
            }
        }, 0, 1000);
    }

    private void stopTimer() {
        timer.cancel();
    }

    @Override
    public boolean isOpen() {
        return opened;
    }

    @Override
    public void open() throws Exception {
        opened = true;
//        startTimer();
    }

    @Override
    public void close() throws Exception {
        opened = false;
//        stopTimer();
    }

    @Override
    public byte[] read() throws Exception {
        if (!isOpen() || respMessages.size() == 0) {
            return new byte[0];
        }
        ByteString respMessage = respMessages.poll();
        if (respMessage == null) {
            return new byte[0];
        } else {
            byte[] byteDatas = respMessage.toByteArray();
            return byteDatas;
        }
    }

    @Override
    public void write(byte[] buffer) throws Exception {
        if (!isOpen()) {
            return;
        }
        ByteString byteString = ByteString.of(buffer);
        handleCmd(byteString);
    }

    @Override
    public EnumSet<ControlLine> getSupportedControlLines() throws Exception {
        return EnumSet.noneOf(ControlLine.class);
    }

    @Override
    public boolean getControlLine(ControlLine controlLine) throws Exception {
        return true;
    }

    @Override
    public void setControlLine(ControlLine controlLine, boolean value) throws Exception {

    }

    private void handleCmd(ByteString byteString) throws Exception {
        Response response = new Response();
        response.data = byteString;

        List<Interceptor> interceptors = new ArrayList() {{
            add(new HexToAsciiInterceptor());
            add(new XorOperateInterceptor());
            add(new FrameCheckInterceptor());
        }};

        for (int i = 0; i < interceptors.size(); i++) {
            response = interceptors.get(i).processResponse(response);
        }

        FrameStruct frameStruct = new FrameStruct();
        byte[] dataBytes = response.data.toByteArray();
        frameStruct.setByteBuffer(ByteBuffer.wrap(dataBytes), 0);

        if (frameStruct.deviceAddress.get() != 0x0006) {
            return;
        }

        if (frameStruct.instruct.get() == 0x01) {
            frameStruct.length.set(frameStruct.functionAddress.offset() - frameStruct.id.offset() + 2);
            ByteBuffer resultBuffer = frameStruct.getByteBuffer();
            ByteString result = ByteString.of(resultBuffer.array(), 0, frameStruct.length.get() + 4);
            response.request.data = result;
        }
        if (frameStruct.instruct.get() == 0x02) {

        }

        for (int i = interceptors.size() - 1; i >= 0; i--) {
            response = interceptors.get(i).processRequest(response);
        }

        respMessages.offer(response.request.data);
    }
}
