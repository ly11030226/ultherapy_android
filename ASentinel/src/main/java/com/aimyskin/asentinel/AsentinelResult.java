package com.aimyskin.asentinel;

public class AsentinelResult {

    private int code;
    private boolean verify;

    public AsentinelResult(int code, boolean verify) {
        this.code = code;
        this.verify = verify;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isVerify() {
        return verify;
    }

    public void setVerify(boolean verify) {
        this.verify = verify;
    }
}
