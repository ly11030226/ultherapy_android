package com.aimyskin.laserserialmodule.responseClassify.device808;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import com.aimyskin.laserserialmodule.responseClassify.device808Classify.DataTreatingUtil;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class RefreshData {

    private static final String TAG = "RefreshData";
    private static RefreshData instance;
    private ScheduledExecutorService scheduler;
    private OnViewListener onViewListener;
    private Handler mainHandler;
    private CountDownLatch latch;

    public void setOnViewListener(OnViewListener onViewListener) {
        this.onViewListener = onViewListener;
    }

    public static RefreshData getSingle() {
        if (instance == null) {
            synchronized (RefreshData.class) {
                if (instance == null) {
                    instance = new RefreshData();
                }
            }
        }
        return instance;
    }

    private RefreshData() {
        scheduler = Executors.newScheduledThreadPool(1, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("RefreshData");
                return thread;
            }
        });
        mainHandler = new Handler(Looper.getMainLooper());
        latch = new CountDownLatch(1);
        scheduler.scheduleWithFixedDelay(myRunnable, 0, 200, TimeUnit.MILLISECONDS);
    }

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


    private Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "run: + zhixingle");
                    refreshData();
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

    private void refreshData() {
        setHandConnect();// 手柄连接状态
        // 808 手柄已连接
        if (onViewListener != null) {
            onViewListener.refreshData();
        }
        setHandAdjustableHeadType();//手柄制冷头型号
        setStandbyReady();// StandbyReady按键状态
        setHandleRefrigerateGear();// 制冷等级
        setFootKey();//脚踏状态
        setFlowRate();// 流速
        setHandleTemp();//手柄温度
        setWaterTemp();// 水箱温度
        setWaterLevel();//水箱水位
        setLaserCounting();// 打光计数
        if (onViewListener != null) {
            onViewListener.detectionError();
        }
    }


    private void setHandAdjustableHeadType() {
        if (onViewListener != null) {
            onViewListener.getHandAdjustableHeadType(DataTreatingUtil.getHandAdjustableHeadType());
        }
    }

    private void setHandleRefrigerateGear() {
        if (onViewListener != null) {
            onViewListener.getRefrigeration(DataTreatingUtil.getHandleRefrigerateGear());
        }
    }

    private void setFrequencyHz() {
        if (onViewListener != null && onViewListener.getFrequencyHz() != null) {
            TextView tvHz = (TextView) onViewListener.getFrequencyHz();
            tvHz.setText(DataTreatingUtil.getFrequencyHz() + "");
        }
    }

    private void setPulseWidth() {
        if (onViewListener != null && onViewListener.getPulseWidth() != null) {
            TextView tvPulseWidth = (TextView) onViewListener.getPulseWidth();
            tvPulseWidth.setText(DataTreatingUtil.getPulseWidth() + "");
        }
    }


    private void setLaserCounting() {
        if (onViewListener != null) {
            onViewListener.getLaserCounting(DataTreatingUtil.getLaserCounting());
        }
    }

    private void setFootKey() {
        if (onViewListener != null) {
            onViewListener.getFootKey(DataTreatingUtil.getFootKey());
        }
    }

    private void setStandbyReady() {
        if (onViewListener != null) {
            onViewListener.getStandbyReady(DataTreatingUtil.getStandbyReady());
        }
    }

    private void setHandConnect() {
        if (onViewListener != null) {
            onViewListener.getHandConnect(DataTreatingUtil.getHandConnect());
        }
    }

    private void setWaterLevel() {
        if (onViewListener != null) {
            onViewListener.getWaterLevel(DataTreatingUtil.getWaterLevel());
        }
    }

    private void setWaterTemp() {
        if (onViewListener != null && onViewListener.getWaterTemp() != null) {
            TextView tvWaterTemp = (TextView) onViewListener.getWaterTemp();
            tvWaterTemp.setText(DataTreatingUtil.getWaterTemp() + "");
        }
    }

    private void setHandleTemp() {
        if (onViewListener != null && onViewListener.getHandleTemp() != null) {
            TextView tvHandleTemp = (TextView) onViewListener.getHandleTemp();
            tvHandleTemp.setText(DataTreatingUtil.getHandleTemp() + "");
        }
    }

    private void setFlowRate() {
        if (onViewListener != null && onViewListener.getFlowRate() != null) {
            TextView tvFlowRate = (TextView) onViewListener.getFlowRate();
            tvFlowRate.setText(DataTreatingUtil.getFlowRate());
        }
    }

}
