package com.supravin.accharger.Storage;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by admin on 05/01/2018.
 */


public class SPTimeReadingCP2 {

    private Context context;


    private SharedPreferences sptimeReadingCP2;
    private SharedPreferences.Editor edTimeReadingCP2;
    private static final String timeReadingCP2 = "timeReadingCP2";
    private static final String ValueTimeReadingCP2 = "ValueTimeReadingCP2";



    public SPTimeReadingCP2(Context context) {
        this.context = context;

        //-----------------------------------------------------button1
        sptimeReadingCP2 = this.context.getSharedPreferences(timeReadingCP2, Context.MODE_PRIVATE);
        edTimeReadingCP2 = sptimeReadingCP2.edit();
        edTimeReadingCP2.commit();


    }


    //------------button1
    public long getTimeReadingCP2() {
        return sptimeReadingCP2.getLong(ValueTimeReadingCP2,0);
    }

    public void setTimeReadingCP2(long ValueTimeReadingCP2s ) {
        edTimeReadingCP2.putLong(ValueTimeReadingCP2, ValueTimeReadingCP2s);
        edTimeReadingCP2.commit();
        edTimeReadingCP2.apply();
    }

    public void clearTimeReadingCP2(){
        edTimeReadingCP2.clear();
        edTimeReadingCP2.commit();
    }

}