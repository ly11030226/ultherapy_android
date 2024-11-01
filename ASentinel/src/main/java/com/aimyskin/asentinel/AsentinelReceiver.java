package com.aimyskin.asentinel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AsentinelReceiver extends BroadcastReceiver {

    private static int count = 0;
    public static VerifyListener myListener;

    public static void setListener(VerifyListener listener) {
        myListener = listener;
    }

    public interface VerifyListener {
        void onResult(AsentinelResult event);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int code = intent.getIntExtra("code", -1);
        boolean verify = intent.getBooleanExtra("verify", false);
        if (code == -1) return;
        if (code == 0 && verify) {
            count = 0;
            if (myListener != null) {
                myListener.onResult(new AsentinelResult(code, true));
            }
        } else {
            count++;
        }
        if (count >= 3) {
            if (myListener != null) {
                myListener.onResult(new AsentinelResult(code, false));
            }
        }
    }
}
