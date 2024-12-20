package com.aimyskin.miscmodule.countTimer;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

import androidx.annotation.NonNull;

import com.elvishew.xlog.XLog;

/**
 * 使用android.os.CountDownTimer的源码
 * 倒计时器（精准）
 */
public abstract class CountDownTimer {
    private static final int MSG = 1;
    private long mMillisInFuture; // 总倒计时时间
    private final long mCountdownInterval; // 倒计时间隔时间
    private long mStopTimeInFuture;
    private long mPauseTimeInFuture;
    private boolean isStop = false;
    private boolean isPause = false;

    public boolean isStop() {
        return isStop;
    }

    public boolean isPause() {
        return isPause;
    }

    public long getMillisInFuture() {
        return mMillisInFuture;
    }

    public void setMillisInFuture(long mMillisInFuture) {
        this.mMillisInFuture = mMillisInFuture;
    }

    /**
     * @param millisInFuture    总倒计时时间
     * @param countDownInterval 倒计时间隔时间
     */
    public CountDownTimer(long millisInFuture, long countDownInterval) {
        // 解决秒数有时会一开始就减去了2秒问题（如10秒总数的，刚开始就8999，然后没有不会显示9秒，直接到8秒）
        if (countDownInterval > 1000) millisInFuture += 15;
        mMillisInFuture = millisInFuture;
        mCountdownInterval = countDownInterval;
    }

    private synchronized CountDownTimer start(long millisInFuture) {
        isStop = false;
        isPause = false;
        if (millisInFuture <= 0) {
            onFinish();
            return this;
        }
        mStopTimeInFuture = SystemClock.elapsedRealtime() + millisInFuture;
        mHandler.sendMessage(mHandler.obtainMessage(MSG));
        return this;
    }

    /**
     * 开始倒计时
     */
    public synchronized final void start() {
        start(mMillisInFuture);
    }

    /**
     * 停止倒计时
     */
    public synchronized final void stop() {
        isStop = true;
        isPause = false;
        mHandler.removeCallbacksAndMessages(MSG);
    }

    /**
     * 暂时倒计时
     * 调用{@link #restart()}方法重新开始
     */
    public synchronized final void pause() {
        if (isStop || isPause) return;

        isPause = true;
        mPauseTimeInFuture = mStopTimeInFuture - SystemClock.elapsedRealtime();
        mHandler.removeCallbacksAndMessages(MSG);
    }

    /**
     * 重新开始
     */
    public synchronized final void restart() {
        if (isStop || !isPause) return;
        isPause = false;
        start(mPauseTimeInFuture);
    }

    /**
     * 倒计时间隔回调
     *
     * @param millisUntilFinished 剩余毫秒数
     */
    public abstract void onTick(long millisUntilFinished);

    /**
     * 倒计时结束回调
     */
    public abstract void onFinish();

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            synchronized (CountDownTimer.this) {
                if (isStop || isPause) {
                    return true;
                }

                final long millisLeft = mStopTimeInFuture - SystemClock.elapsedRealtime();//剩余总时间
//                XLog.d("handleMessage: " + millisLeft + "  " + mStopTimeInFuture + "  " + SystemClock.elapsedRealtime());
                if (millisLeft <= 0) {
                    onFinish();
                } else {
                    long lastTickStart = SystemClock.elapsedRealtime();//当前时间
                    onTick(millisLeft);

                    // take into account user's onTick taking time to execute
                    long lastTickDuration = SystemClock.elapsedRealtime() - lastTickStart; // onTick 耗费时间
                    long delay;

                    if (millisLeft < mCountdownInterval) {
                        // just delay until done
                        delay = millisLeft - lastTickDuration;

                        // special case: user's onTick took more than interval to
                        // complete, trigger onFinish without delay
                        if (delay < 0) delay = 0;
                    } else {
                        delay = mCountdownInterval - lastTickDuration;

                        // special case: user's onTick took more than interval to
                        // complete, skip to next interval
                        while (delay < 0) delay += mCountdownInterval;
                    }
//                    XLog.d("handleMessage: " + delay);
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG), delay);
                }
            }
            return true;
        }
    });

}
