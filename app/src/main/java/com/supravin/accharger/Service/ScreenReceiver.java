package com.supravin.accharger.Service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

/**
 * Created by admin on 07/02/2017.
 */
public class ScreenReceiver extends BroadcastReceiver {
    private static boolean wasScreenOn = true;
    //private final WelcomeActivity activity;
    /*public ScreenReceiverBroadcast(WelcomeActivity maContext){
        activity=maContext;
    }*/
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {

            wasScreenOn = false;

                PowerManager newPM = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

                //noinspection deprecation
                PowerManager.WakeLock wakeLock = newPM.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "TEST");
                wakeLock.acquire();

                AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                //noinspection UnnecessaryLocalVariable
                Intent ExistingIntent = intent;
                PendingIntent pi = PendingIntent.getActivity(context, 0, ExistingIntent, 0);
                alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, 10, pi);


        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {

            wasScreenOn = true;

        }
    }
}
