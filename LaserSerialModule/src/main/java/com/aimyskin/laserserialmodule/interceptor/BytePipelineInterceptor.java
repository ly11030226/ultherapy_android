package com.aimyskin.laserserialmodule.interceptor;

import com.aimyskin.serialmodule.Response;
import com.aimyskin.serialmodule.SerialClient;
import com.aimyskin.serialmodule.interceptor.Interceptor;

import okio.Buffer;
import okio.ByteString;

public class BytePipelineInterceptor implements Interceptor {

    final ByteString CRLF = ByteString.encodeUtf8("\r\n");
    private volatile boolean canceled;
    final SerialClient client;
    private Buffer readBuffer = new Buffer();

    public BytePipelineInterceptor(SerialClient client) {
        this.client = client;
    }

    @Override
    public boolean isCanceled() {
        return canceled;
    }

    @Override
    public void cancel() {
        canceled = true;
    }

    @Override
    public Response processRequest(Response response) throws Exception {
        // 发送数据
        client.serialPort().write(response.request.data.toByteArray());
        return response;
    }

    @Override
    public Response processResponse(Response response) throws Exception {
        int readBufferIndex = 0;
        while (true) {
            if (canceled) {
                throw new Exception("Pipeline canceled");
            }
            byte[] read = client.serialPort().read();
            readBuffer.write(read);
            ByteString byteString = readBuffer.clone().readByteString();
            int index = byteString.indexOf(CRLF);
            if (index >= 0) {
                readBufferIndex = index;
                break;
            }
        }
        response.data = readBuffer.readByteString(readBufferIndex + CRLF.toByteArray().length);
        return response;
    }
}
