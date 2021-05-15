package com.supravin.accharger.Service;


import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;

//import com.virajmohite.a3phasecharger.InitiatingActivityMannual;
import com.supravin.accharger.AppApplication;

import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by jarvis on 6/2/17.
 */

public class Registerbatterylevelreceiver {
    private Context context;
    public String battery_stat = null;
    AppApplication activity;

    private int battery_flag = 0;
    private int overlay_flag = 0;
    public int dirtyflag = 0;
    private int nflag = 0;
    private int lflag = 0;
    private int mflag = 0;
    private int oflag = 0;
    private int pflag = 0;
    private String lev;
    int notificationflag = 0;

    public Registerbatterylevelreceiver(AppApplication mainActivity) {
        activity = mainActivity;
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        activity.registerReceiver(battery_receiver, filter);
    }


    public void registerBatteryLevelReceiver(Context context) {
        this.context = context;
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        activity.registerReceiver(battery_receiver, filter);
    }

    private final BroadcastReceiver battery_receiver = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isPresent = intent.getBooleanExtra("present", false);
            int scale = intent.getIntExtra("scale", -1);
            int health = intent.getIntExtra("health", 0);
            final int status = intent.getIntExtra("status", 0);
            int rawlevel = intent.getIntExtra("level", -1);
            double voltage = intent.getIntExtra("voltage", 0);
            double temperature = intent.getIntExtra("temperature", 0) / 10;
            int level = 0;
            String Level = "";

            Bundle bundle = intent.getExtras();

            Log.i("BatteryLevel", bundle.toString());

            if (isPresent) {
                if (rawlevel >= 0 && scale > 0) {
                    level = (rawlevel * 100) / scale;
                    Level = String.valueOf(level);
                }
                lev = Level;

                if ((Integer.toString(level)).matches("90") && getStatusString(status) == "Not Charging") {


                } else if ((Integer.toString(level)).matches("70") && getStatusString(status) == "Not Charging") {


                } else if ((Integer.toString(level)).matches("50") && getStatusString(status) == "Not Charging") {


                } else if ((Integer.toString(level)).matches("30") && getStatusString(status) == "Not Charging") {


                } else if ((Integer.toString(level)).matches("20") && getStatusString(status) == "Not Charging") {

                } else {

                }


                if (level >= 50) {

                } else if (level < 50) {

                }

                //     activity.battery_percnt.setText(Level + "%");

                if (level <= 25 && getStatusString(status) == "Not Charging") {
//                    if(activity.didmidflag==1){
//                    activity.ShowOverlay();
                    ///  activity.layouto.setVisibility(View.VISIBLE);

                    //  }
                    try {
                        Process proc = Runtime.getRuntime()
                                .exec(new String[]{"su", "-c", "reboot -p"});
                        proc.waitFor();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                if (((Integer.toString(level)).matches("26")) && overlay_flag == 1) {

                }


                if (level < 25) {
//                    activity.ShowOverlay();
                }

                if (level <= 10 && getStatusString(status) == "Not Charging") {
                    try {
                        Process proc = Runtime.getRuntime()
                                .exec(new String[]{"su", "-c", "reboot -p"});
                        proc.waitFor();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
//                    Process suProcess = null;
//                    try {
//                        suProcess = Runtime.getRuntime().exec("su");
//                        DataOutputStream os = new DataOutputStream(suProcess.getOutputStream());
//                        os.writeBytes("adb shell" + "\n");
//                        os.flush();
//                        os.writeBytes("reboot -p \n");
////            os.writeBytes("settings put global device_provisioned 1 \n");
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                }
//                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                String currentDateandTime = sdf.format(new Date());
//                battery_stat = level + "#" + getHealthString(health) + "#" + getStatusString(status) + "#" + voltage + "#" + temperature + "#" + currentDateandTime;
//
//
//
//                if (getStatusString(status) == "Not Charging" && level < 98){
//                    activity.battery_percnt.setVisibility(View.VISIBLE);
//                } else {
//                    activity.battery_percnt.setVisibility(View.INVISIBLE);
//                }

//                if(getStatusString(status) == "Charging"){
//                    activity.gifTextView.setVisibility(View.VISIBLE);
//                    activity.gifvisiblityflag=1;
//                   activity.ischarging();
//                    if(getStatusString(status) == "Charging" && battery_flag == 0) {
//                        activity.gifTextView.setVisibility(View.VISIBLE);
//                        activity.gifvisiblityflag=1;
//                        activity.ischarging();
//                        writeToSDFile(battery_stat);
//                        battery_flag = 1;
////                        Toast.makeText(activity, "Writing to file 'charging'" , Toast.LENGTH_LONG).show();
//
//                    }
//                } else {
//                    if(getStatusString(status) == "Not Charging" && battery_flag == 1 ) {
//                        writeToSDFile(battery_stat);
//                        activity.gifTextView.setVisibility(View.INVISIBLE);
//                        activity.gifvisiblityflag=0;
//                        activity.isnotcharging();
//                        battery_flag = 0;
////                        Toast.makeText(activity, "Writing to file 'not charging'" , Toast.LENGTH_LONG).show();
//                    }
//                }


                if ((getStatusString(status) == "Charging")) {
                  /* Toast.makeText(context, "B Charging", Toast.LENGTH_SHORT).show();
                   SPbatteryStatus sPbatteryStatus = new SPbatteryStatus(context);
                   sPbatteryStatus.setbatteryStatus("Charging");*/
                }

                if (getStatusString(status) == "Full") {
                   /* Toast.makeText(context, "B Full", Toast.LENGTH_SHORT).show();
                    SPbatteryStatus sPbatteryStatus = new SPbatteryStatus(context);
                    sPbatteryStatus.setbatteryStatus("Full");*/
                }

                if (getStatusString(status) == "Not Charging") {
                    /*Toast.makeText(context, "B Not Charging", Toast.LENGTH_SHORT).show();
                    SPbatteryStatus sPbatteryStatus = new SPbatteryStatus(context);
                    sPbatteryStatus.setbatteryStatus("NCharging");*/
                }
            }
            }
        };

        public void writeToSDFile(String string) {
            try {

                OutputStreamWriter out = new OutputStreamWriter(activity.openFileOutput("Battery_Stats", Context.MODE_APPEND));
                out.write(string);
                out.write("\n");
                out.close();
//            Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                Log.d("Suceess", "Sucess");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private String getHealthString(int health) {
            String healthString = "Unknown";

            switch (health) {
                case BatteryManager.BATTERY_HEALTH_DEAD:
                    healthString = "Dead";
                    break;
                case BatteryManager.BATTERY_HEALTH_GOOD:
                    healthString = "Good";
                    break;
                case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                    healthString = "Over Voltage";
                    break;
                case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                    healthString = "Over Heat";
                    break;
                case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                    healthString = "Failure";
                    break;
            }

            return healthString;
        }

        private String getStatusString(int status) {
            String statusString = "Unknown";

            switch (status) {
                case BatteryManager.BATTERY_STATUS_CHARGING:
                    statusString = "Charging";
                    break;
                case BatteryManager.BATTERY_STATUS_DISCHARGING:
                    statusString = "Discharging";
                    break;
                case BatteryManager.BATTERY_STATUS_FULL:
                    statusString = "Full";
                    break;
                case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                    statusString = "Not Charging";
                    break;
            }

            return statusString;
        }
    }
