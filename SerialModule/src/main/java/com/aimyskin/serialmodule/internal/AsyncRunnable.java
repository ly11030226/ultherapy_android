package com.aimyskin.serialmodule.internal;

import java.util.concurrent.ExecutorService;

/**
 * Runnable implementation which always sets its thread name.
 */
public abstract class AsyncRunnable implements Runnable {
    protected final String name;

    public AsyncRunnable(String format, Object... args) {
        this.name = Util.format(format, args);
    }

    @Override
    public final void run() {
        String oldName = Thread.currentThread().getName();
        Thread.currentThread().setName(name);
        try {
            execute();
        } finally {
            Thread.currentThread().setName(oldName);
        }
    }

    public abstract void executeOn(ExecutorService executorService);

    public abstract Call get();

    protected abstract void execute();
}
