package com.aimyskin.serialmodule.internal;

import com.aimyskin.serialmodule.Request;

public interface Call extends Cloneable {
    void cancel();
    interface Factory {
        Call newCall(Request request);
        Call newCallTask(Request request);
        Call newPipeline(Callback callback);
    }
}
