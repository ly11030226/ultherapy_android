package com.aimyskin.miscmodule.usbmonitor;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.HashMap;

public class USBMonitor implements USBConnect {

    private static final String TAG = "USBMonitor";
//    private final String USB_PERMISSION = "com.ifcloud.global.text.usb.USB_PERMISSION";
    private final String USB_PERMISSION = "android.hardware.usb.USB_PERMISSION";
    private final WeakReference<Context> weakContext;
    public UsbManager usbManager;
    private USBConnect usbConnect;
    private USBMonitorThreadHandler threadHandler;
    private PendingIntent pendingIntent;
    private boolean destroyed = false;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
            String action = intent.getAction();
            if (action.equals(USB_PERMISSION)) {
                if (intent.getExtras().getBoolean(UsbManager.EXTRA_PERMISSION_GRANTED)) {
                    processConnect(device);
                } else {
                    processCancel(device);
                }
            } else if (action.equals(UsbManager.ACTION_USB_DEVICE_ATTACHED)) {
                processAttach(device);
            } else if (action.equals(UsbManager.ACTION_USB_DEVICE_DETACHED)) {
                processDetach(device);
            }
        }
    };

    public USBMonitor(final Context context, final USBConnect usbConnect) {
        this.weakContext = new WeakReference<Context>(context);
        this.usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        this.threadHandler = USBMonitorThreadHandler.createHandler(TAG);
        this.usbConnect = usbConnect;
        this.destroyed = false;
    }

    public synchronized void unregister() throws IllegalStateException {
        threadHandler.removeCallbacks(mFilterDeviceRunnable);
        if (pendingIntent != null) {
            try {
                if (weakContext != null) {
                    weakContext.get().unregisterReceiver(broadcastReceiver);
                }
            } catch (final Exception e) {
            }
            pendingIntent = null;
        }
    }

    public synchronized void register() throws IllegalStateException {
        if (pendingIntent == null) {
            final Context context = weakContext.get();
            if (context != null) {
                pendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(USB_PERMISSION), 0);
                IntentFilter intentFilter = new IntentFilter(USB_PERMISSION);
                intentFilter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
                intentFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
                context.registerReceiver(broadcastReceiver, intentFilter);
            }
            threadHandler.postDelayed(mFilterDeviceRunnable, 1000);
        }
    }
    public synchronized void postDelayed(){
        if (pendingIntent != null) {
            threadHandler.postDelayed(mFilterDeviceRunnable, 1000);
        }
    }

    public synchronized void destroy() {
        unregister();
        if (!destroyed) {
            destroyed = true;
            try {
                threadHandler.getLooper().quit();
            } catch (final Exception e) {
            }
        }
    }

    private final Runnable mFilterDeviceRunnable = new Runnable() {
        @Override
        public void run() {
            // 尝试 attach 已插入 usb 设备
            final Context context = weakContext.get();
            if (context != null) {
                UsbManager usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
                HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();
                Collection<UsbDevice> devices = deviceList.values();
                for (UsbDevice device : devices) {
                    if (filterDevice(device)) {
                        processAttach(device);
                        if (usbConnect == null||!usbConnect.isSkipBreak()){
                            break;
                        }
                    }
                }
            }
        }
    };

    public boolean filterDevice(UsbDevice device) {
        if (usbConnect != null) {
            return usbConnect.filterDevice(device);
        } else {
            return true;
        }
    }

    public void processDetach(UsbDevice device) {
        Log.d(TAG, "processDetach: " + device.toString());
        threadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (filterDevice(device)) {
                    if (usbConnect != null) {
                        usbConnect.processDetach(device);
                    }
                }
            }
        });
    }

    public void processAttach(UsbDevice device) {
        Log.d(TAG, "processAttach: " + device.toString());
        threadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (filterDevice(device)) {
                    requestPermission(device);
                    if (usbConnect != null) {
                        usbConnect.processAttach(device);
                    }
                }
            }
        });
    }

    public void processCancel(UsbDevice device) {
        Log.d(TAG, "processCancel: " + device.toString());
        threadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (filterDevice(device)) {
                    if (usbConnect != null) {
                        usbConnect.processCancel(device);
                    }
                }
            }
        });
    }

    public void processConnect(UsbDevice device) {
        Log.d(TAG, "processConnect: " + device.toString());
        threadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (filterDevice(device)) {
                    if (usbConnect != null) {
                        usbConnect.processConnect(device);
                    }
                }
            }
        });
    }

    public synchronized boolean requestPermission(UsbDevice device) {
        if (usbManager.hasPermission(device)) {
            processConnect(device);
            return true;
        } else {
            try {
                usbManager.requestPermission(device, pendingIntent);
                return true;
            } catch (final Exception e) {
                processCancel(device);
                return false;
            }
        }
    }


    public static class USBMonitorThreadHandler extends Handler {

        public static final USBMonitorThreadHandler createHandler() {
            return createHandler("USBMonitorThreadHandler");
        }

        public static final USBMonitorThreadHandler createHandler(String name) {
            return createHandler(name, null);
        }

        public static final USBMonitorThreadHandler createHandler(Callback callback) {
            return createHandler("USBMonitorThreadHandler", callback);
        }

        public static final USBMonitorThreadHandler createHandler(String name, Callback callback) {
            HandlerThread thread = new HandlerThread(name);
            thread.start();
            return new USBMonitorThreadHandler(thread.getLooper(), callback);
        }

        private USBMonitorThreadHandler(Looper looper, Callback callback) {
            super(looper, callback);
        }
    }
}
