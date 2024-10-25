package com.aimyskin.serialmodule.interceptor;

import android.util.Log;

import com.aimyskin.serialmodule.BuildConfig;
import com.aimyskin.serialmodule.Response;

public class LogHexInterceptor implements Interceptor {

    private static final String TAG = "LogHexInterceptor";

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
            String string = response.request.data.hex();
            Log.d(TAG, "processRequest hex: " + string);
        }
        return response;
    }

    @Override
    public Response processResponse(Response response) throws Exception {
        if (BuildConfig.DEBUG) {
            String string = response.data.hex();
            Log.d(TAG, "processResponse hex: " + string);
        }
        return response;
    }
}
