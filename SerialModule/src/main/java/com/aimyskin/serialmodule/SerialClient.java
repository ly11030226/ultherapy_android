package com.aimyskin.serialmodule;

import com.aimyskin.serialmodule.interceptor.Interceptor;
import com.aimyskin.serialmodule.internal.Call;
import com.aimyskin.serialmodule.internal.Callback;
import com.aimyskin.serialmodule.serialport.SerialPort;

import java.util.ArrayList;
import java.util.List;

public class SerialClient implements Call.Factory {
    Dispatcher dispatcher = new Dispatcher();
    EventListener.Factory eventListenerFactory = EventListener.factory(EventListener.NONE);
    List<Class<? extends Interceptor>> interceptors;
    Class<? extends Interceptor> callInterceptor;
    SerialPort serialPort;

    public SerialClient() {
    }

    public Dispatcher dispatcher() {
        return dispatcher;
    }

    public EventListener.Factory eventListenerFactory() {
        return eventListenerFactory;
    }

    public SerialPort serialPort() {
        return serialPort;
    }

    @Override
    public RealCall newCall(Request request) {
        return RealCall.newRealCall(this, request);
    }

    @Override
    public RealCallTask newCallTask(Request request) {
        return RealCallTask.newRealCallTask(RealCall.newRealCall(this, request));
    }

    @Override
    public RealPipeline newPipeline(Callback callback) {
        return RealPipeline.newPipeline(this, callback);
    }

    public static final class Builder {
        EventListener.Factory eventListenerFactory = EventListener.factory(EventListener.NONE);
        List<Class<? extends Interceptor>> interceptors = new ArrayList<>();
        Class<? extends Interceptor> callInterceptor;
        SerialPort serialPort;

        private Builder() {
        }

        public static Builder aSerialClient() {
            return new Builder();
        }

        public Builder eventListener(EventListener eventListener) {
            if (eventListener == null) throw new NullPointerException("eventListener == null");
            this.eventListenerFactory = EventListener.factory(eventListener);
            return this;
        }

        public Builder eventListenerFactory(EventListener.Factory eventListenerFactory) {
            if (eventListenerFactory == null) {
                throw new NullPointerException("eventListenerFactory == null");
            }
            this.eventListenerFactory = eventListenerFactory;
            return this;
        }

        public Builder addInterceptor(Class<? extends Interceptor> interceptor) {
            if (interceptor == null) throw new IllegalArgumentException("interceptor == null");
            interceptors.add(interceptor);
            return this;
        }

        public Builder addCallInterceptor(Class<? extends Interceptor> callInterceptor) {
            if (callInterceptor == null) throw new IllegalArgumentException("interceptor == null");
            this.callInterceptor = callInterceptor;
            return this;
        }

        public Builder serialPort(SerialPort serialPort) {
            this.serialPort = serialPort;
            return this;
        }

        public SerialClient build() {
            SerialClient serialClient = new SerialClient();
            serialClient.serialPort = this.serialPort;
            serialClient.interceptors = this.interceptors;
            serialClient.callInterceptor = this.callInterceptor;
            serialClient.eventListenerFactory = this.eventListenerFactory;
            serialClient.interceptors = this.interceptors;
            return serialClient;
        }
    }
}
