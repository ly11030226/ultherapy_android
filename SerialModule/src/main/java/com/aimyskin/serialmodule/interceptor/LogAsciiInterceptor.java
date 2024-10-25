package com.aimyskin.serialmodule.interceptor;

import android.util.Log;

import com.aimyskin.serialmodule.BuildConfig;
import com.aimyskin.serialmodule.Response;

public class LogAsciiInterceptor implements Interceptor {

    private static final String TAG = "LogAsciiInterceptor";

    @Override
    public boolean isCanceled() {
        return false;
    }

    @Override
    public void cancel() {

    }

    @Override
    public Response processRequest(Response response) throws Exception {
        if (BuildConfig.DEBUG) {
            String string = response.request.data.utf8();
            Log.d(TAG, "processRequest utf8: " + string);
        }
        return response;
    }

    @Override
    public Response processResponse(Response response) throws Exception {
        if (BuildConfig.DEBUG) {
            String string = response.data.utf8();
            Log.d(TAG, "processResponse utf8: " + string);
        }
        return response;
    }
}
