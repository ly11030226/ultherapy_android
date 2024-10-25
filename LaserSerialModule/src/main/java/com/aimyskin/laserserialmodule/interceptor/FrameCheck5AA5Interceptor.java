package com.aimyskin.laserserialmodule.interceptor;

import com.aimyskin.serialmodule.Response;
import com.aimyskin.serialmodule.interceptor.Interceptor;

import java.nio.ByteBuffer;

public class FrameCheck5AA5Interceptor implements Interceptor {

    public static int header = 0x5AA5;

    @Override
    public boolean isCanceled() {
        return false;
    }

    @Override
    public void cancel() {

    }

    @Override
    public Response processRequest(Response response) throws Exception {
        byte[] dataBytes = response.request.data.toByteArray();
        Frame5AA5Struct frameStruct = new Frame5AA5Struct();
        frameStruct.setByteBuffer(ByteBuffer.wrap(dataBytes), 0);
        if (frameStruct.header.get() != header) {
            throw new Exception("frame header error");
        }
        return response;
    }

    @Override
    public Response processResponse(Response response) throws Exception {
        byte[] dataBytes = response.data.toByteArray();
        Frame5AA5Struct frameStruct = new Frame5AA5Struct();
        frameStruct.setByteBuffer(ByteBuffer.wrap(dataBytes), 0);
        if (frameStruct.header.get() != header) {
            throw new Exception("frame header error");
        }
        return response;
    }

}
