package com.aimyskin.miscmodule.usbmonitor;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.aimyskin.miscmodule.R;

import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

/**
 * Usb管理服务
 */
public class UsbManagerService extends Service {
    private static final String TAG = "UsbManagerService";
    private MyBinder binder=new MyBinder();
    private Handler handler;
    public UsbManager usbManager;
    private static final String CHANNEL_ID = "UsbManagerService";
    private final String USB_PERMISSION = "android.hardware.usb.USB_PERMISSION";
    private static final int NOTIFICATION_ID = 0xfffc;
    private Notification notification = null;
    private static Vector<UsbDevice> usableList=new Vector<>();
    private static Vector<UsbDevice> usabledList=new Vector<>();
    private Vector<UsbDevice> list=new Vector<>();
    private PendingIntent pendingIntent;
    private static Vector<UsbCallBack> usbCallBacks=new Vector<>();
    private MyReceiver receiver;

    public static void action(Context context, ServiceConnection coon){
        Intent intent=new Intent(context,UsbManagerService.class);
        context.bindService(intent,coon,Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startNotification();
        handler=new Handler();
        usbManager = (UsbManager)getSystemService(Context.USB_SERVICE);
        receiver=new MyReceiver();
        IntentFilter filter=new IntentFilter(USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        registerReceiver(receiver,filter);
    }

    private void startNotification() {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel mChannel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(CHANNEL_ID, getString(R.string.usb_service_name),
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
            if (notification == null){
                notification = new Notification.Builder(getApplicationContext(), CHANNEL_ID).build();
            }
            startForeground(NOTIFICATION_ID, notification);
        }
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();
        Collection<UsbDevice> devices = deviceList.values();
        usableList.clear();
        usabledList.clear();
        for (UsbDevice device : devices) {
            list.add(device);
            if(usbManager.hasPermission(device)){
                usableList.add(device);
            }else {
                usabledList.add(device);
                createPendingIntent(device);
            }
        }
        requestPermission();
        return START_STICKY;
    }

    /**
     * 创建意图
     * @param device
     */
    private void createPendingIntent(UsbDevice device){
        pendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(USB_PERMISSION), 0);
    }

    /**
     * 请求权限
     */
    private void requestPermission(){
        if(usabledList.size()!=0){
            createPendingIntent(usabledList.get(0));
            usbManager.requestPermission(usabledList.get(0),pendingIntent);
        }
    }

    /**
     * 添加回调监听
     */
    public static void addCallBack(UsbCallBack usbCallBack){
        if(usbCallBack==null)return;
        for (int i=0;i<usableList.size();i++){
            UsbDevice usbDevice=usableList.get(i);
            if(usbCallBack.filterDevice(usbDevice)){
                usbCallBack.processConnect(usbDevice);
                if(!usbCallBack.isSkipBreak()){
                    break;
                }
            }
        }
        usbCallBacks.add(usbCallBack);
    }

    public static Vector<UsbCallBack> getUsbCallBacks() {
        if (usbCallBacks ==null){
            return new Vector<>();
        }
        return usbCallBacks;
    }

    /**
     * 移除回调监听
     * @param usbCallBack
     */
    public static void removeCallBack(UsbCallBack usbCallBack){
        if(usbCallBack==null)return;
        usbCallBacks.remove(usbCallBack);
    }

    @Override
    public void onDestroy() {
        if(handler!=null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        super.onDestroy();
    }

    public class MyBinder extends Binder {
        public UsbManagerService getSerivce(){
            return UsbManagerService.this;
        }
    }

    public class MyReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
            String action = intent.getAction();
            if (USB_PERMISSION.equals(action)) {
                Bundle bundle=intent.getExtras();
                if(bundle!=null) {
                    if (bundle.getBoolean(UsbManager.EXTRA_PERMISSION_GRANTED)) {
                        usableList.add(device);
                        usabledList.remove(device);
                        for (int i=0;i<usbCallBacks.size();i++){
                            UsbCallBack usbCallBack=usbCallBacks.get(i);
                            if(usbCallBack.filterDevice(device)){
                                usbCallBack.processConnect(device);
                            }
                        }
                    } else {

                    }
                }
                requestPermission();
            } else if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
                list.add(device);
                if(usbManager.hasPermission(device)){
                    usableList.add(device);
                    for (int i=0;i<usbCallBacks.size();i++){
                        UsbCallBack usbCallBack=usbCallBacks.get(i);
                        if(usbCallBack.filterDevice(device)){
                            usbCallBack.processConnect(device);
                        }
                    }
                }else {
                    if(usabledList.size()==0){
                        usabledList.add(device);
                        requestPermission();
                    }else {
                        usabledList.add(device);
                    }
                }

            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                list.remove(device);
                usabledList.remove(device);
                usableList.remove(device);
                for (int i=0;i<usbCallBacks.size();i++){
                    UsbCallBack usbCallBack=usbCallBacks.get(i);
                    if(usbCallBack.filterDevice(device)){
                        usbCallBack.processCancel(device);
                    }
                }
            }
        }
    }
}
