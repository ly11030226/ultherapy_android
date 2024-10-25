package com.aimyskin.serialmodule;

import okio.ByteString;

public class Request implements Cloneable {
    public ByteString data;

    public Request() {
    }

    public Request(ByteString data) {
        this.data = data;
    }

    @Override
    public Request clone() {
        if (data != null) {
            return new Request(ByteString.of(data.toByteArray()));
        }
        return new Request();
    }
}
