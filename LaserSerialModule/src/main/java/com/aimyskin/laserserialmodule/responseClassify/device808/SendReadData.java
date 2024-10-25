package com.aimyskin.laserserialmodule.responseClassify.device808;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class SendReadData {
    private static SendReadData instance;
    private ScheduledExecutorService scheduler;
    private Handler mainHandler;
    private CountDownLatch latch;

    public static SendReadData getSingle() {
        if (instance == null) {
            synchronized (SendReadData.class) {
                if (instance == null) {
                    instance = new SendReadData();
                }
            }
        }
        return instance;
    }

    private SendReadData() {
        scheduler = Executors.newScheduledThreadPool(1, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("SendReadData");
                return thread;
            }
        });
        mainHandler = new Handler(Looper.getMainLooper());
        latch = new CountDownLatch(1);
        scheduler.scheduleWithFixedDelay(myRunnable, 0, 500, TimeUnit.MILLISECONDS);
    }

    private Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (onReadListener != null){
                        onReadListener.sendReadData();
                    }
                    latch.countDown();
                }
            });
            try {
                latch.await();
            } catch (Exception e) {
                Thread.currentThread().interrupt(); // 恢复线程的中断状态
            }
        }
    };

    public void stopScheduler() {
        if (scheduler != null) {
            if (!scheduler.isShutdown()) {
                scheduler.shutdownNow();
            }
            scheduler = null;
        }
        if (mainHandler != null) {
            mainHandler.removeCallbacksAndMessages(null);
        }
        if (latch != null) {
            latch.countDown();
        }
        instance = null;
    }

    private OnReadListener onReadListener;

    public void setOnReadListener(OnReadListener onReadListener) {
        this.onReadListener = onReadListener;
    }

    public interface OnReadListener {

        default void sendReadData () {
        }
    }
}
