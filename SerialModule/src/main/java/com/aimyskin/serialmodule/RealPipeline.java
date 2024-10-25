package com.aimyskin.serialmodule;

import android.util.Log;

import androidx.annotation.Nullable;

import com.aimyskin.serialmodule.interceptor.Interceptor;
import com.aimyskin.serialmodule.internal.AsyncRunnable;
import com.aimyskin.serialmodule.internal.Call;
import com.aimyskin.serialmodule.internal.Callback;

import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public final class RealPipeline implements Call {
    final SerialClient client;
    private Interceptor callInterceptor = null;
    private PipeinThread pipeinThread;
    private @Nullable
    EventListener eventListener;
    private @Nullable
    Callback responseCallback;
    private boolean canceled = true;

    private RealPipeline(SerialClient client, @Nullable Callback callback) {
        this.client = client;
        this.responseCallback = callback;
    }

    static RealPipeline newPipeline(SerialClient client, @Nullable Callback callback) {
        RealPipeline call = new RealPipeline(client, callback);
        call.eventListener = client.eventListenerFactory().create(call);
        return call;
    }

    public void pipeout(Request request) {
        pipeout(request, 0);
    }

    public void pipeout(Request request, int delay) {
        if (!canceled) {
            eventListener.callStart(this);
            client.dispatcher().enqueue(new AsyncCall(request, delay));
        }
    }

    public void open() throws Exception {
        canceled = false;
        client.serialPort.open();
        if (client.callInterceptor != null && this.responseCallback != null) {
            this.pipeinThread = new PipeinThread();
            this.pipeinThread.start();
        }
    }

    public void close() {
        this.cancel();
    }

    @Override
    public void cancel() {
        Log.e("RealPipeline","取消了");
        try {
            canceled = true;
            callInterceptor.cancel();
            client.serialPort.close();
            this.pipeinThread = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isCanceled() {
        return canceled;
    }

    final class AsyncCall extends AsyncRunnable {
        private final Request request;
        private final int delay;

        AsyncCall(Request request, int delay) {
            super("RealPipeline");
            this.request = request;
            this.delay = delay;
        }

        @Override
        public Call get() {
            return RealPipeline.this;
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
                eventListener.callFailed(RealPipeline.this, ioException);
            } finally {
                if (!success) {
                    client.dispatcher().finished(this); // This call is no longer running!
                }
            }
        }

        @Override
        protected void execute() {
            boolean signalledCallback = false;
            try {
                pipeoutRequestWithInterceptorChain(request.clone());
                signalledCallback = true;
                eventListener.callEnd(RealPipeline.this);
            } catch (Exception e) {
                if (signalledCallback) {
                    // Do not signal the callback twice!
                    Log.i("RealPipeline", "Callback failure for " + toLoggableString(), e);
                } else {
                    eventListener.callFailed(RealPipeline.this, e);
                }
            } finally {
                client.dispatcher().finished(this);
            }
        }
    }

    String toLoggableString() {
        return (isCanceled() ? "canceled " : "") + "call";
    }

    void pipeoutRequestWithInterceptorChain(Request request) throws Exception {

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
        Interceptor callInterceptor = null;
        Class<? extends Interceptor> requestInterceptorClass = client.callInterceptor;
        if (requestInterceptorClass != null) {
            try {
                callInterceptor = requestInterceptorClass.getConstructor(SerialClient.class).newInstance(client);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 创建返回数据
         */
        Response response = new Response();
        response.request = request.clone();

        /**
         * 处理请求数据
         */
        for (int i = 0; i < interceptors.size(); i++) {
            response = interceptors.get(i).processRequest(response);
        }

        /**
         * 写数据
         */
        if (callInterceptor != null) {
            callInterceptor.processRequest(response);
        }
    }

    final class PipeinThread extends Thread {
        List<Interceptor> interceptors = new ArrayList<>();

        PipeinThread() {
            super("PipeinThread");
            Class<? extends Interceptor> requestInterceptorClass = client.callInterceptor;
            if (requestInterceptorClass != null) {
                try {
                    callInterceptor = requestInterceptorClass.getConstructor(SerialClient.class).newInstance(client);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            for (Class<? extends Interceptor> interceptorClass : client.interceptors) {
                try {
                    Interceptor interceptor = interceptorClass.getConstructor().newInstance();
                    interceptors.add(interceptor);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void run() {
            super.run();
            while (true) {
                if (canceled) {
                    break;
                }
                try {
                    /**
                     * 读取响应数据
                     */
                    Response response = new Response();
                    response = callInterceptor.processResponse(response);

                    /**
                     * 处理响应数据
                     */
                    for (int i = interceptors.size() - 1; i >= 0; i--) {
                        response = interceptors.get(i).processResponse(response);
                    }
                    responseCallback.onResponse(RealPipeline.this, response);
                } catch (Exception e) {
                    responseCallback.onFailure(RealPipeline.this, e);
                }
            }
        }
    }
}

