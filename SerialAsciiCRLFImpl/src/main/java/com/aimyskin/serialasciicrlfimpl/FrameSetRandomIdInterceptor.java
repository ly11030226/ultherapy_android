package com.aimyskin.serialasciicrlfimpl;

import com.aimyskin.serialmodule.Response;
import com.aimyskin.serialmodule.interceptor.Interceptor;

import java.nio.ByteBuffer;
import java.util.Random;

public class FrameSetRandomIdInterceptor implements Interceptor {
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
        frameStruct.id.set(randomId());
        return response;
    }

    @Override
    public Response processResponse(Response response) throws Exception {
        return response;
    }


    /**
     * 生产随机码
     *
     * @return
     */
    private int randomId() {
        byte[] randomBytes = new byte[2];
        Random random = new Random();
        random.nextBytes(randomBytes);
        randomBytes[0] = (byte) (randomBytes[0] | 0x80);
        if (randomBytes[0] == 0x00 && randomBytes[1] == 0x00) {
            randomBytes[0] = (byte) 0x95;
            randomBytes[1] = (byte) 0x27;
        }
        int ret = randomBytes[0] << 8 | randomBytes[1];
        return ret;
    }
}
