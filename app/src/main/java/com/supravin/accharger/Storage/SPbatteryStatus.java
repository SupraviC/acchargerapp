package com.supravin.accharger.Storage;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by admin on 05/01/2018.
 */


public class SPbatteryStatus {

    private Context context;


    private SharedPreferences spbatteryStatus;
    private SharedPreferences.Editor edbatteryStatus;
    private static final String str_batteryStatus = "batteryStatus";
    private static final String ValuebatteryStatus = "ValuebatteryStatus";




    public SPbatteryStatus(Context context) {
        this.context = context;

        //-----------------------------------------------------button1
        spbatteryStatus = this.context.getSharedPreferences(str_batteryStatus, Context.MODE_PRIVATE);
        edbatteryStatus = spbatteryStatus.edit();
        edbatteryStatus.commit();


    }


    //------------button1
    public String getbatteryStatus() {
        return spbatteryStatus.getString(ValuebatteryStatus, "");
    }

    public void setbatteryStatus(String ValuebatteryStatuss ) {
        edbatteryStatus.putString(ValuebatteryStatus, ValuebatteryStatuss);
        edbatteryStatus.commit();
        edbatteryStatus.apply();
    }

    public void clearbatteryStatus(){
        edbatteryStatus.clear();
        edbatteryStatus.commit();
    }

}