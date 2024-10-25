package com.aimyskin.serialmodule.interceptor;

import com.aimyskin.serialmodule.Response;

public interface Interceptor {
    boolean isCanceled();
    void cancel();
    Response processRequest(Response response) throws Exception;
    Response processResponse(Response response) throws Exception;
}
