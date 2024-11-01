package com.aimyskin.asentinel;

public class Asentinel {
    static {
        System.loadLibrary("asentinel");
    }

    public native String generateToken();

    public native boolean verifyToken(String token);
}
