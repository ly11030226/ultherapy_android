package com.aimyskin.serialmodule;

import android.util.Log;

import androidx.annotation.Nullable;

import com.aimyskin.serialmodule.interceptor.Interceptor;
import com.aimyskin.serialmodule.internal.AsyncRunnable;
import com.aimyskin.serialmodule.internal.Call;
import com.aimyskin.serialmodule.internal.Callback;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import okio.AsyncTimeout;

import static java.util.concurrent.TimeUnit.MILLISECONDS;


public final class RealCall implements Call {
    final SerialClient client;
    final AsyncTimeout timeout;
    final Request originalRequest;
    private Interceptor callInterceptor;
    private @Nullable
    EventListener eventListener;

    // Guarded by this.
    private boolean executed;

    private RealCall(SerialClient client, Request originalRequest) {
        this.client = client;
        this.originalRequest = originalRequest;
        this.timeout = new AsyncTimeout() {
            @Override
            protected void timedOut() {
                cancel();
            }
        };
    }

    static RealCall newRealCall(SerialClient client, Request originalRequest) {
        // Safely publish the Call instance to the EventListener.
        RealCall call = new RealCall(client, originalRequest);
        call.eventListener = client.eventListenerFactory().create(call);
        return call;
    }

    public Request request() {
        return originalRequest;
    }

    public Response execute() throws Exception {
        return this.execute(0);
    }

    public Response execute(int timeout) throws Exception {
        synchronized (this) {
            if (executed) throw new IllegalStateException("Already Executed");
            executed = true;
        }
        this.timeout.timeout(timeout, MILLISECONDS);
        this.timeout.enter();
        eventListener.callStart(this);
        try {
            client.dispatcher().executed(this);
            Response result = getResponseWithInterceptorChain();
            if (result == null) throw new IOException("Canceled");
            return result;
        } catch (Exception e) {
            e = timeoutExit(e);
            eventListener.callFailed(this, e);
            throw e;
        } finally {
            client.dispatcher().finished(this);
        }
    }

    @Nullable
    Exception timeoutExit(@Nullable Exception cause) {
        if (!timeout.exit()) return cause;
        InterruptedIOException e = new InterruptedIOException("timeout");
        if (cause != null) {
            e.initCause(cause);
        }
        return e;
    }

    public void enqueue(Callback responseCallback) {
        this.enqueue(responseCallback, 0, 0);
    }

    public void enqueue(Callback responseCallback, int timeout) {
        this.enqueue(responseCallback, timeout, 0);
    }

    public void enqueue(Callback responseCallback, int timeout, int delay) {
        synchronized (this) {
            if (executed) throw new IllegalStateException("Already Executed");
            executed = true;
        }
        eventListener.callStart(this);
        client.dispatcher().enqueue(new AsyncCall(responseCallback, timeout, delay));
    }

    @Override
    public void cancel() {
        if (callInterceptor != null) {
            callInterceptor.cancel();
        }
    }

    public AsyncTimeout timeout() {
        return timeout;
    }

    public synchronized boolean isExecuted() {
        return executed;
    }

    public boolean isCanceled() {
        if (callInterceptor == null) {
            return true;
        }
        return callInterceptor.isCanceled();
    }

    final class AsyncCall extends AsyncRunnable {
        private final Callback responseCallback;
        int timeout;
        int delay;

        AsyncCall(Callback responseCallback, int timeout, int delay) {
            super("RealCall");
            this.responseCallback = responseCallback;
            this.timeout=timeout;
            this.delay = delay;
        }

        @Override
        public Call get() {
            return RealCall.this;
        }

        /**
         * Attempt to enqueue this async call on {@code executorService}. This will attempt to clean up
         * if the executor has been shut down by reporting the call as failed.
         */
        @Override
        public void executeOn(ExecutorService executorService) {
            assert (!Thread.holdsLock(client.dispatcher()));
            boolean success = false;
            try {
                ScheduledThreadPoolExecutor service = (ScheduledThreadPoolExecutor) executorService;
                service.schedule(this, delay, TimeUnit.MILLISECONDS);
                success = true;
            } catch (RejectedExecutionException e) {
                InterruptedIOException ioException = new InterruptedIOException("executor rejected");
                ioException.initCause(e);
                eventListener.callFailed(RealCall.this, ioException);
                responseCallback.onFailure(RealCall.this, ioException);
            } finally {
                if (!success) {
                    client.dispatcher().finished(this); // This call is no longer running!
                }
            }
        }

        @Override
        protected void execute() {
            boolean signalledCallback = false;
            timeout().timeout(this.timeout, MILLISECONDS);
            timeout().enter();
            try {
                Response response = getResponseWithInterceptorChain();
                signalledCallback = true;
                eventListener.callEnd(RealCall.this);
                responseCallback.onResponse(RealCall.this, response);
            } catch (Exception e) {
                e = timeoutExit(e);
                if (signalledCallback) {
                    // Do not signal the callback twice!
                    Log.i("RealCall", "Callback failure for " + toLoggableString(), e);
                } else {
                    eventListener.callFailed(RealCall.this, e);
                    responseCallback.onFailure(RealCall.this, e);
                }
            } catch (Throwable t) {
                cancel();
                if (!signalledCallback) {
                    IOException canceledException = new IOException("canceled due to " + t);
                    responseCallback.onFailure(RealCall.this, canceledException);
                }
                throw t;
            } finally {
                client.dispatcher().finished(this);
            }
        }
    }

    String toLoggableString() {
        return (isCanceled() ? "canceled " : "") + "call";
    }

    Response getResponseWithInterceptorChain() throws Exception {

        client.serialPort().open();

        /**
         * 创建拦截器
         */
        List<Interceptor> interceptors = new ArrayList<>();
        for (Class<? extends Interceptor> interceptorClass : client.interceptors) {
            try {
                Interceptor interceptor = interceptorClass.getConstructor().newInstance();
                interceptors.add(interceptor);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 请求拦截器
         */
        Class<? extends Interceptor> callInterceptorClass = client.callInterceptor;
        if (callInterceptorClass != null) {
            try {
                callInterceptor = callInterceptorClass.getConstructor(SerialClient.class).newInstance(client);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 创建返回数据
         */
        Response response = new Response();
        response.request = request().clone();
        if (response.request.data == null) {
            return response;
        }

        /**
         * 处理请求数据
         */
        for (int i = 0; i < interceptors.size(); i++) {
            response = interceptors.get(i).processRequest(response);
        }

        /**
         * 读写数据
         */
        if (callInterceptor != null) {
            response = callInterceptor.processRequest(response);
            response = callInterceptor.processResponse(response);
        }

        /**
         * 处理响应数据
         */
        for (int i = interceptors.size() - 1; i >= 0; i--) {
            response = interceptors.get(i).processResponse(response);
        }

        /**
         * 恢复原始请求数据
         */
        response.request = request().clone();

        return response;
    }
}
