package com.aimyskin.serialasciicrlfimpl;

import com.aimyskin.serialmodule.Response;
import com.aimyskin.serialmodule.interceptor.Interceptor;

import okio.ByteString;

public class HexToAsciiInterceptor implements Interceptor {

    @Override
    public boolean isCanceled() {
        return false;
    }

    @Override
    public void cancel() {

    }

    @Override
    public Response processRequest(Response response) throws Exception {
        String hexString = response.request.data.hex();
        hexString = hexString + "\r\n";
        ByteString asciiString = ByteString.encodeUtf8(hexString);
        asciiString = asciiString.toAsciiUppercase();
        response.request.data = asciiString;
        return response;
    }

    @Override
    public Response processResponse(Response response) throws Exception {
        String asciiString = response.data.utf8();
        String[] asciiStrings = asciiString.split("\r\n");
        asciiString = asciiStrings[0];
        asciiString = asciiString.replaceAll(" ", "");
        ByteString hexString = ByteString.decodeHex(asciiString);
        response.data = hexString;
        return response;
    }
}
