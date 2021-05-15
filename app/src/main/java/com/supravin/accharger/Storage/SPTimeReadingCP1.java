package com.supravin.accharger.Storage;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by admin on 05/01/2018.
 */


public class SPTimeReadingCP1 {

    private Context context;


    private SharedPreferences sptimeReadingCP1;
    private SharedPreferences.Editor edTimeReadingCP1;
    private static final String timeReadingCP1 = "timeReadingCP1";
    private static final String ValueTimeReadingCP1 = "ValueTimeReadingCP1";



    public SPTimeReadingCP1(Context context) {
        this.context = context;

        //-----------------------------------------------------button1
        sptimeReadingCP1 = this.context.getSharedPreferences(timeReadingCP1, Context.MODE_PRIVATE);
        edTimeReadingCP1 = sptimeReadingCP1.edit();
        edTimeReadingCP1.commit();


    }


    //------------button1
    public long getTimeReadingCP1() {
        return sptimeReadingCP1.getLong(ValueTimeReadingCP1,0);
    }

    public void setTimeReadingCP1(long ValueTimeReadingCP1s ) {
        edTimeReadingCP1.putLong(ValueTimeReadingCP1, ValueTimeReadingCP1s);
        edTimeReadingCP1.commit();
        edTimeReadingCP1.apply();
    }

    public void clearTimeReadingCP1(){
        edTimeReadingCP1.clear();
        edTimeReadingCP1.commit();
    }

}