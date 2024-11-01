package com.aimyskin.asentinel;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;

import com.aimyskin.miscmodule.usbmonitor.USBConnect;
import com.aimyskin.miscmodule.usbmonitor.USBMonitor;
import com.aimyskin.serialmodule.SerialClient;
import com.blankj.utilcode.util.LogUtils;
import com.elvishew.xlog.XLog;

/**
 * app加密认证服务
 */
public class AsentinelService extends Service {

    private static final String CHANNEL_ID = "AsentinelService";
    private static final int NOTIFICATION_ID = 0xffff;
    private SerialClient serialClient = null;
    private Asentinel asentinel = null;
    private Handler sentinelHandler = null;
    private static String path;
    private static int pId;
    private static int vId;
    private static boolean debug;
    private Runnable sentinelRunnable;

    /**
     * 启动app认证服务
     *
     * @param context
     */
    public static void startASentinel(Context context, String path) {
        startASentinel(context, path, 0, 0, false);
    }

    public static void startASentinel(Context context, int pId, int vId) {
        startASentinel(context, "", pId, vId, false);
    }

    /**
     * 启动app认证服务
     *
     * @param context
     */
    public static void startASentinel(Context context, String path, int pId, int vId, boolean debug) {
        Intent intent = new Intent(context, AsentinelService.class);
        intent.putExtra("path", path);
        intent.putExtra("pId", pId);
        intent.putExtra("vId", vId);
        intent.putExtra("debug", debug);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }

    private Notification notification = null;

    @Override
    public void onCreate() {
        super.onCreate();
        // 创建Notification对象
        startNotification();
        asentinel = new Asentinel();
        sentinelHandler = new Handler(getMainLooper());
    }

    private void startNotification() {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel mChannel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(CHANNEL_ID, "Ultherapy",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
            if (notification == null) {
                notification = new Notification.Builder(getApplicationContext(), CHANNEL_ID).build();
            }
            startForeground(NOTIFICATION_ID, notification);
        }
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        startNotification();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private USBMonitor usbMonitor;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            this.path = intent.getStringExtra("path");
            this.pId = intent.getIntExtra("pId", 0);
            this.vId = intent.getIntExtra("vId", 0);
            this.debug = intent.getBooleanExtra("debug", false);
        }
        if (serialClient != null) {
            doClean();
        }
        if (pId != 0 && vId != 0) {
            usbMonitor = new USBMonitor(this, new USBConnect() {
                @Override
                public boolean filterDevice(UsbDevice device) {
                    if (device.getProductId() == pId && device.getVendorId() == vId) {
                        return true;
                    }
                    return false;
                }

                @Override
                public void processAttach(UsbDevice device) {

                }

                @Override
                public void processConnect(UsbDevice device) {
                    AsentinelSerial.open(usbMonitor.usbManager, device, debug);
                }

                @Override
                public void processCancel(UsbDevice device) {
                    AsentinelSerial.close();
                }

                @Override
                public void processDetach(UsbDevice device) {
                    AsentinelSerial.close();
                }
            });
            usbMonitor.register();

        } else {
//            LogUtils.d("****************** open asentinel serial start ******************");
            AsentinelSerial.open(path, debug);
//            LogUtils.d("****************** open asentinel serial end ******************");
        }
        sentinelRunnable = new Runnable() {
            @Override
            public void run() {
//                LogUtils.d("****************** doSentinel start ******************");
                doSentinel();
//                LogUtils.d("****************** doSentinel end ******************");
                sentinelHandler.postDelayed(sentinelRunnable, 1000 * 5);
            }
        };
        sentinelHandler.postDelayed(sentinelRunnable, 2500);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        doClean();
        super.onDestroy();
    }

    public void doClean() {
        if (sentinelHandler != null) {
            sentinelHandler.removeCallbacksAndMessages(null);
            sentinelHandler = null;
        }
        if (serialClient != null) {
            try {
                serialClient.serialPort().close();
            } catch (Exception e) {
                XLog.e("doClean: ", e);
            }
            serialClient = null;
        }
        if (usbMonitor != null) {
            usbMonitor.destroy();
            usbMonitor = null;
        }
        asentinel = null;
    }

    public void doSentinel() {
        String token = asentinel.generateToken();
//        LogUtils.d("doSentinel token ... " + token);
        AsentinelSerial.doSentinel(0x8000, token, new AsentinelSerial.SentinelCallback() {
            @Override
            public void onResult(int code, String data, String message) {
//                Log.e("KLog",code+"-----"+data+"-----"+message);
                Intent intent = new Intent(AsentinelService.this, AsentinelReceiver.class);
                if (code == 0) {
                    boolean verify = asentinel.verifyToken(data);
                    intent.putExtra("code", code);
                    intent.putExtra("verify", verify);
                } else {
                    intent.putExtra("code", code);
                    intent.putExtra("message", message);
                }
                sendBroadcast(intent);
            }
        });
    }
}
