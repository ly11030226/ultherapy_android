package com.aimyskin.serialasciicrlfimpl;

import com.aimyskin.serialmodule.Response;
import com.aimyskin.serialmodule.interceptor.Interceptor;

import java.nio.ByteBuffer;
import java.util.Random;

import okio.ByteString;

public class XorOperateInterceptor implements Interceptor {
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
        byte[] randomBytes = new byte[2];
        Random random = new Random();
        random.nextBytes(randomBytes);
        if (randomBytes[0] == 0x00 && randomBytes[1] == 0x00) {
            randomBytes[0] = (byte) 0x95;
            randomBytes[1] = (byte) 0x27;
        }
        xorOperate(dataBytes, randomBytes);
        ByteBuffer allBytes = ByteBuffer.allocate(dataBytes.length + randomBytes.length);
        allBytes.put(dataBytes);
        allBytes.put(randomBytes);
        response.request.data = ByteString.of(allBytes.array());
        return response;
    }

    @Override
    public Response processResponse(Response response) throws Exception {
        byte[] allBytes = response.data.toByteArray();
        if (allBytes.length <= 2) {
            throw new Exception("response data length error");
        }
        byte[] dataBytes = new byte[allBytes.length - 2];
        byte[] randomBytes = new byte[2];
        ByteBuffer dataBuffer = ByteBuffer.wrap(allBytes);
        dataBuffer.get(dataBytes, 0, dataBytes.length);
        dataBuffer.get(randomBytes, 0, randomBytes.length);
        xorOperate(dataBytes, randomBytes);
        response.data = ByteString.of(dataBytes);
        return response;
    }

    private void xorOperate(byte[] lhs, byte[] rhs) {
        for (int i = 0; i < lhs.length; i++) {
            lhs[i] = (byte) (lhs[i] ^ rhs[i % 2]);
        }
    }
}
