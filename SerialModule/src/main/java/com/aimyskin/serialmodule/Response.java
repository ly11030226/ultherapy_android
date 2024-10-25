package com.aimyskin.serialmodule;

import okio.ByteString;

public class Response implements Cloneable {
    public Request request = new Request();
    public ByteString data;
}
