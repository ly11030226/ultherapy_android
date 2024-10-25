package com.aimyskin.serialasciicrlfimpl;

import com.aimyskin.serialmodule.Response;
import com.aimyskin.serialmodule.interceptor.Interceptor;

import java.nio.ByteBuffer;

public class FrameCheckInterceptor implements Interceptor {
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
        FrameStruct frameStruct = new FrameStruct();
        frameStruct.setByteBuffer(ByteBuffer.wrap(dataBytes), 0);
        if (frameStruct.header.get() != 0x7E7E) {
            throw new Exception("frame header error");
        }
        if (frameStruct.length.get() != dataBytes.length - frameStruct.id.offset()) {
            throw new Exception("frame length error");
        }
        return response;
    }

    @Override
    public Response processResponse(Response response) throws Exception {
        byte[] dataBytes = response.data.toByteArray();
        FrameStruct frameStruct = new FrameStruct();
        frameStruct.setByteBuffer(ByteBuffer.wrap(dataBytes), 0);
        if (frameStruct.header.get() != 0x7E7E) {
            throw new Exception("frame header error");
        }
        if (frameStruct.length.get() != dataBytes.length - frameStruct.id.offset()) {
            throw new Exception("frame length error");
        }
        return response;
    }
}

