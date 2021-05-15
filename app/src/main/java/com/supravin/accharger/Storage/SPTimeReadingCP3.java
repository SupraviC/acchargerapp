package com.supravin.accharger.Storage;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by admin on 05/01/2018.
 */


public class SPTimeReadingCP3 {

    private Context context;


    private SharedPreferences sptimeReadingCP3;
    private SharedPreferences.Editor edTimeReadingCP3;
    private static final String timeReadingCP3 = "timeReadingCP3";
    private static final String ValueTimeReadingCP3 = "ValueTimeReadingCP3";



    public SPTimeReadingCP3(Context context) {
        this.context = context;

        //-----------------------------------------------------button1
        sptimeReadingCP3 = this.context.getSharedPreferences(timeReadingCP3, Context.MODE_PRIVATE);
        edTimeReadingCP3 = sptimeReadingCP3.edit();
        edTimeReadingCP3.commit();


    }


    //------------button1
    public long getTimeReadingCP3() {
        return sptimeReadingCP3.getLong(ValueTimeReadingCP3,0);
    }

    public void setTimeReadingCP3(long ValueTimeReadingCP3s ) {
        edTimeReadingCP3.putLong(ValueTimeReadingCP3, ValueTimeReadingCP3s);
        edTimeReadingCP3.commit();
        edTimeReadingCP3.apply();
    }

    public void clearTimeReadingCP3(){
        edTimeReadingCP3.clear();
        edTimeReadingCP3.commit();
    }

}