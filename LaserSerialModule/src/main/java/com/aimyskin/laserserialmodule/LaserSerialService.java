package com.aimyskin.laserserialmodule;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.aimyskin.laserserialmodule.responseClassify.ResponseClassify;
import com.aimyskin.laserserialmodule.responseClassify.ResponseControl;
import com.aimyskin.laserserialmodule.responseClassify.device808.OnButtonListener;
import com.blankj.utilcode.util.LogUtils;


public class LaserSerialService extends Service {


    private static final String CHANNEL_ID = "LaserSerialService";
    private static final int NOTIFICATION_ID = 0xfffc;
    private Notification notification = null;
    private MyBinder binder = new MyBinder();
    private ResponseClassify responseClassify;


    public static void action(Context context, ServiceConnection coon) {
        Intent intent = new Intent(context, LaserSerialService.class);
        intent.putExtra("deviceClassify",DeviceType.DEVICE_ULTHERAPY.getValue());
        context.bindService(intent, coon, Context.BIND_AUTO_CREATE);
    }

    public static void startLaserSerialService(Context context, DeviceType deviceType) {
        Intent intent = new Intent(context, LaserSerialService.class);
        intent.putExtra("deviceClassify", deviceType.getValue());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }

    public class MyBinder extends Binder {
        public LaserSerialService getService() {
            return LaserSerialService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        startNotification();
    }


    private void startNotification() {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel mChannel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID,
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
            if (notification == null) {
                notification = new Notification.Builder(getApplicationContext(), CHANNEL_ID).build();
            }
            startForeground(NOTIFICATION_ID, notification);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        int deviceClassify = intent.getIntExtra("deviceClassify", -1);
        if (deviceClassify == DeviceType.DEVICE_808.getValue()) {
            startSerialPort(DeviceType.DEVICE_808);
        } else if (deviceClassify == DeviceType.DEVICE_ULTHERAPY.getValue()) {
            startSerialPort(DeviceType.DEVICE_ULTHERAPY);
        }
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.d("LaserSerialService onStartCommand");
        if (intent != null) {
            int deviceClassify = intent.getIntExtra("deviceClassify", -1);
            if (deviceClassify == DeviceType.DEVICE_808.getValue()) {
                startSerialPort(DeviceType.DEVICE_808);
            } else if (deviceClassify == DeviceType.DEVICE_ULTHERAPY.getValue()) {
                startSerialPort(DeviceType.DEVICE_ULTHERAPY);
            }
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (responseClassify != null) {
            responseClassify.close();
            responseClassify = null;
        }
        ResponseControl.classify = null;
        super.onDestroy();
    }


    public static final String SERIAL_BROADCAST = "com.example.SERIAL_BROADCAST_RECEIVER";
    public static final String SEND_DATA = "SEND_DATA";
    public static final String KEY_SEND_DATA = "KEY_SEND_DATA";

    public class MySerialBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 在这里处理接收到的广播
            String action = intent.getAction();
            if (action != null) {
                if (action.equals(SERIAL_BROADCAST)) {
                    // 重新开启
                    int deviceClassify = intent.getIntExtra("deviceClassify", -1);
                    if (responseClassify != null) {
                        responseClassify.close();
                        responseClassify = null;
                    }
                    ResponseControl.classify = null;
                    if (deviceClassify == DeviceType.DEVICE_808.getValue()) {
                        startSerialPort(DeviceType.DEVICE_808);
                    } else if (deviceClassify == DeviceType.DEVICE_ULTHERAPY.getValue()) {
                        startSerialPort(DeviceType.DEVICE_ULTHERAPY);
                    }
                } else if (action.equals(SEND_DATA)) {
                    String data = intent.getStringExtra(KEY_SEND_DATA);
                    sendData(data, 0);
                }
            }
        }
    }

    public void startSerialPort(DeviceType deviceType) {
        responseClassify = ResponseControl.getResponseControl(getApplicationContext(), deviceType);
    }

    public void sendData(String data, int delay) {
        if (responseClassify != null) {
            responseClassify.sendData(data, delay);
        }
    }

    public void setOnButtonListener(OnButtonListener listener) {
        if (responseClassify != null) {
            responseClassify.setOnButtonListener(listener);
        }
    }
}
