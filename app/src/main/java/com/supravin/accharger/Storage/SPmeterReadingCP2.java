package com.supravin.accharger.Storage;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by admin on 05/01/2018.
 */


public class SPmeterReadingCP2 {

    private Context context;


    private SharedPreferences spmeterReadingCP2;
    private SharedPreferences.Editor edMeterReadingCP2;
    private static final String meterReadingCP2 = "meterReadingCP2";
    private static final String ValueMeterReadingCP2 = "ValueMeterReadingCP2";




    public SPmeterReadingCP2(Context context) {
        this.context = context;

        //-----------------------------------------------------button2
        spmeterReadingCP2 = this.context.getSharedPreferences(meterReadingCP2, Context.MODE_PRIVATE);
        edMeterReadingCP2 = spmeterReadingCP2.edit();
        edMeterReadingCP2.commit();


    }


    //------------button2
    public String getMeterReadingCP2() {
        return spmeterReadingCP2.getString(ValueMeterReadingCP2, "");
    }

    public void setMeterReadingCP2(String ValueMeterReadingCP2s ) {
        edMeterReadingCP2.putString(ValueMeterReadingCP2, ValueMeterReadingCP2s);
        edMeterReadingCP2.commit();
        edMeterReadingCP2.apply();
    }

    public void clearMeterReadingCP2(){
        edMeterReadingCP2.clear();
        edMeterReadingCP2.commit();
    }

}