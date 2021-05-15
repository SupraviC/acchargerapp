package com.supravin.accharger;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;
import android.support.multidex.MultiDex;


import com.rxgradle.wifi.MyWifi;
import com.supravin.accharger.OCPP_Implementation_1_6.Receiver.ConnectivityReceiver;
import com.supravin.accharger.Service.Registerbatterylevelreceiver;


/**
 * Created by admin on 09/02/2017.
 */
public class AppApplication extends Application {
    Registerbatterylevelreceiver rblr;
    private static AppApplication instance;
    private PowerManager.WakeLock wakeLock;
    private ScreenReceiverBroadcast screenReceiverBroadcast1;

    public boolean hasRefreshed;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        hasRefreshed=false;
        rblr = new Registerbatterylevelreceiver(AppApplication.this);
        registerKioskModeScreenOffReceiver();
        MyWifi.init(this);

    }


    public static synchronized AppApplication getInstance() {
        return instance;
    }

    /*public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }*/

    private void registerKioskModeScreenOffReceiver() {
        // register screen off receiver
        final IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        screenReceiverBroadcast1 = new ScreenReceiverBroadcast();
        registerReceiver(screenReceiverBroadcast1, filter);
    }

    @SuppressLint("InvalidWakeLockTag")
    public PowerManager.WakeLock getWakeLock() {
        if(wakeLock == null) {
            // lazy loading: first call, create wakeLock via PowerManager.
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "wakeup");
        }
        return wakeLock;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}
