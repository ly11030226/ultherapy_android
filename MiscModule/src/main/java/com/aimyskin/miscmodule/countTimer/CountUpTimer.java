package com.aimyskin.miscmodule.countTimer;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

import androidx.annotation.NonNull;

/**
 * 正计时器（不精准）
 */
public abstract class CountUpTimer {
    private static final int MSG = 1;
    private final long mCountdownInterval;
    private boolean isStop = false;
    private boolean isPause = false;
    private long times = 0;

    /**
     * @param countDownInterval 倒计时间隔时间
     */
    public CountUpTimer(long countDownInterval) {
        mCountdownInterval = countDownInterval;
    }


    /**
     * 开始倒计时
     */
    public synchronized void start() {
        isStop = false;
        isPause = false;
        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG), mCountdownInterval);
    }

    public boolean isStart() {
        return !(isPause || isStop);
    }

    /**
     * 停止倒计时
     */
    public synchronized void stop() {
        isStop = true;
        isPause = false;
        times = 0;
        mHandler.removeMessages(MSG);
    }

    /**
     * 暂时倒计时
     * 调用{@link #restart()}方法重新开始
     */
    public synchronized final void pause() {
        if (isStop) return;

        isPause = true;

        mHandler.removeMessages(MSG);
    }

    /**
     * 重新开始
     */
    public synchronized final void restart() {
        if (isStop || !isPause) return;

        isPause = false;
        mHandler.sendMessage(mHandler.obtainMessage(MSG));
    }

    /**
     * 倒计时间隔回调
     *
     * @param millisUntil 经过秒数
     */
    public abstract void onTick(long millisUntil);

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            synchronized (CountUpTimer.this) {
                if (isStop || isPause) {
                    return true;
                }
                times++;
                final long millisLeft = times * mCountdownInterval;
                long lastTickStart = SystemClock.elapsedRealtime();
                onTick(millisLeft);

                long lastTickDuration = SystemClock.elapsedRealtime() - lastTickStart;
                long delay = mCountdownInterval - lastTickDuration;

                while (delay < 0) delay += mCountdownInterval;
                mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG), delay);
            }
            return true;
        }
    });
}
