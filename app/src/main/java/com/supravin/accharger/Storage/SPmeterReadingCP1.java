package com.supravin.accharger.Storage;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by admin on 05/01/2018.
 */


public class SPmeterReadingCP1 {

    private Context context;


    private SharedPreferences spmeterReadingCP1;
    private SharedPreferences.Editor edMeterReadingCP1;
    private static final String meterReadingCP1 = "meterReadingCP1";
    private static final String ValueMeterReadingCP1 = "ValueMeterReadingCP1";




    public SPmeterReadingCP1(Context context) {
        this.context = context;

        //-----------------------------------------------------button1
        spmeterReadingCP1 = this.context.getSharedPreferences(meterReadingCP1, Context.MODE_PRIVATE);
        edMeterReadingCP1 = spmeterReadingCP1.edit();
        edMeterReadingCP1.commit();


    }


    //------------button1
    public String getMeterReadingCP1() {
        return spmeterReadingCP1.getString(ValueMeterReadingCP1, "");
    }

    public void setMeterReadingCP1(String ValueMeterReadingCP1s ) {
        edMeterReadingCP1.putString(ValueMeterReadingCP1, ValueMeterReadingCP1s);
        edMeterReadingCP1.commit();
        edMeterReadingCP1.apply();
    }

    public void clearMeterReadingCP1(){
        edMeterReadingCP1.clear();
        edMeterReadingCP1.commit();
    }

}