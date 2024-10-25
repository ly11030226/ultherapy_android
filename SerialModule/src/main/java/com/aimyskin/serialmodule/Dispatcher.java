package com.aimyskin.serialmodule;

import androidx.annotation.Nullable;

import com.aimyskin.serialmodule.internal.AsyncRunnable;
import com.aimyskin.serialmodule.internal.Call;
import com.aimyskin.serialmodule.internal.Callback;
import com.aimyskin.serialmodule.internal.Util;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * Policy on when async requests are executed.
 *
 * <p>Each dispatcher uses an {@link ExecutorService} to run calls internally. If you supply your
 * own executor, it should be able to run {@linkplain #maxRequests the configured maximum} number
 * of calls concurrently.
 */
public final class Dispatcher {
    private int maxRequests = 1;
    private @Nullable
    Runnable idleCallback;

    /**
     * Executes calls. Created lazily.
     */
    private @Nullable
    ExecutorService executorService;

    /**
     * Ready async calls in the order they'll be run.
     */
    private final Deque<AsyncRunnable> readyAsyncCalls = new ArrayDeque<>();

    /**
     * Running asynchronous calls. Includes canceled calls that haven't finished yet.
     */
    private final Deque<AsyncRunnable> runningAsyncCalls = new ArrayDeque<>();

    /**
     * Running synchronous calls. Includes canceled calls that haven't finished yet.
     */
    private final Deque<Call> runningSyncCalls = new ArrayDeque<>();

    public Dispatcher(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public Dispatcher() {
    }

    public synchronized ExecutorService executorService() {
        if (executorService == null) {
            executorService = new ScheduledThreadPoolExecutor(0, Util.threadFactory("SerialClient Dispatcher", false));
        }
        return executorService;
    }

    /**
     * Set a callback to be invoked each time the dispatcher becomes idle (when the number of running
     * calls returns to zero).
     *
     * <p>Note: The time at which a {@linkplain Call call} is considered idle is different depending
     * on whether it was run {@linkplain Call#enqueue(Callback) asynchronously} or
     * {@linkplain Call#execute() synchronously}. Asynchronous calls become idle after the
     * {@link Callback#onResponse onResponse} or {@link Callback#onFailure onFailure} callback has
     * returned. Synchronous calls become idle once {@link Call#execute() execute()} returns. This
     * means that if you are doing synchronous calls the network layer will not truly be idle until
     * every returned {@link Response} has been closed.
     */
    public synchronized void setIdleCallback(@Nullable Runnable idleCallback) {
        this.idleCallback = idleCallback;
    }

    void enqueue(AsyncRunnable call) {
        synchronized (this) {
            readyAsyncCalls.add(call);
        }
        promoteAndExecute();
    }

    /**
     * Cancel all calls currently enqueued or executing. Includes calls executed both {@linkplain
     * Call#execute() synchronously} and {@linkplain Call#enqueue asynchronously}.
     */
    public synchronized void cancelAll() {
        for (AsyncRunnable call : readyAsyncCalls) {
            call.get().cancel();
        }

        for (AsyncRunnable call : runningAsyncCalls) {
            call.get().cancel();
        }

        for (Call call : runningSyncCalls) {
            call.cancel();
        }
    }

    /**
     * Promotes eligible calls from {@link #readyAsyncCalls} to {@link #runningAsyncCalls} and runs
     * them on the executor service. Must not be called with synchronization because executing calls
     * can call into user code.
     *
     * @return true if the dispatcher is currently running calls.
     */
    private boolean promoteAndExecute() {
        assert (!Thread.holdsLock(this));

        List<AsyncRunnable> executableCalls = new ArrayList<>();
        boolean isRunning;
        synchronized (this) {
            for (Iterator<AsyncRunnable> i = readyAsyncCalls.iterator(); i.hasNext(); ) {
                AsyncRunnable asyncCall = i.next();

                if (runningAsyncCalls.size() >= maxRequests) break; // Max capacity.

                i.remove();
                executableCalls.add(asyncCall);
                runningAsyncCalls.add(asyncCall);
            }
            isRunning = runningCallsCount() > 0;
        }

        for (int i = 0, size = executableCalls.size(); i < size; i++) {
            AsyncRunnable asyncCall = executableCalls.get(i);
            asyncCall.executeOn(executorService());
        }

        return isRunning;
    }

    /**
     * Used by {@code Call#execute} to signal it is in-flight.
     */
    synchronized void executed(Call call) {
        runningSyncCalls.add(call);
    }

    /**
     * Used by {@code AsyncRunnable#run} to signal completion.
     */
    void finished(AsyncRunnable call) {
        finished(runningAsyncCalls, call);
    }

    /**
     * Used by {@code Call#execute} to signal completion.
     */
    void finished(Call call) {
        finished(runningSyncCalls, call);
    }

    private <T> void finished(Deque<T> calls, T call) {
        Runnable idleCallback;
        synchronized (this) {
            if (!calls.remove(call)) throw new AssertionError("Call wasn't in-flight!");
            idleCallback = this.idleCallback;
        }

        boolean isRunning = promoteAndExecute();

        if (!isRunning && idleCallback != null) {
            idleCallback.run();
        }
    }

    /**
     * Returns a snapshot of the calls currently awaiting execution.
     */
    public synchronized List<Call> queuedCalls() {
        List<Call> result = new ArrayList<>();
        for (AsyncRunnable asyncCall : readyAsyncCalls) {
            result.add(asyncCall.get());
        }
        return Collections.unmodifiableList(result);
    }

    /**
     * Returns a snapshot of the calls currently being executed.
     */
    public synchronized List<Call> runningCalls() {
        List<Call> result = new ArrayList<>();
        result.addAll(runningSyncCalls);
        for (AsyncRunnable asyncCall : runningAsyncCalls) {
            result.add(asyncCall.get());
        }
        return Collections.unmodifiableList(result);
    }

    public synchronized int queuedCallsCount() {
        return readyAsyncCalls.size();
    }

    public synchronized int runningCallsCount() {
        return runningAsyncCalls.size() + runningSyncCalls.size();
    }
}
