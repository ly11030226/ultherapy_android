package com.aimyskin.serialmodule;

import com.aimyskin.serialmodule.internal.Call;
import com.aimyskin.serialmodule.internal.Callback;

import bolts.Task;
import bolts.TaskCompletionSource;

public class RealCallTask implements Call {
    public RealCall realCall;

    public RealCallTask(RealCall realCall) {
        this.realCall = realCall;
    }

    public static RealCallTask newRealCallTask(RealCall realCall) {
        RealCallTask call = new RealCallTask(realCall);
        return call;
    }

    public Task<Response> execute() {
        return execute(0);
    }

    public Task<Response> execute(int timeout) {
        try {
            return Task.forResult(this.realCall.execute(timeout));
        } catch (Exception e) {
            return Task.forError(e);
        }
    }

    public Task<Response> enqueue() {
        return enqueue(0, 0);
    }

    public Task<Response> enqueue(int timeout) {
        return enqueue(timeout, 0);
    }

    public Task<Response> enqueue(int timeout, int delay) {
        TaskCompletionSource tcs = new TaskCompletionSource();
        this.realCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, Exception e) {
                tcs.trySetError(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws Exception {
                tcs.trySetResult(response);
            }
        }, timeout, delay);
        return tcs.getTask();
    }

    @Override
    public void cancel() {
        realCall.cancel();
    }
}
