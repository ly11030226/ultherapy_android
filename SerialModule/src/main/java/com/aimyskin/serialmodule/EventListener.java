package com.aimyskin.serialmodule;

import com.aimyskin.serialmodule.internal.Call;

public abstract class EventListener {
    public static final EventListener NONE = new EventListener() {
    };

    static EventListener.Factory factory(final EventListener listener) {
        return new EventListener.Factory() {
            public EventListener create(Call call) {
                return listener;
            }
        };
    }

    public void callStart(Call call) {
    }

    public void callEnd(Call call) {
    }

    public void callFailed(Call call, Exception ioe) {
    }

    public interface Factory {
        EventListener create(Call call);
    }
}
