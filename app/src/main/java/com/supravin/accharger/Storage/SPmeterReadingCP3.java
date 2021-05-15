package com.supravin.accharger.Storage;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by admin on 05/01/2018.
 */


public class SPmeterReadingCP3 {

    private Context context;


    private SharedPreferences spmeterReadingCP3;
    private SharedPreferences.Editor edMeterReadingCP3;
    private static final String meterReadingCP3 = "meterReadingCP3";
    private static final String ValueMeterReadingCP3 = "ValueMeterReadingCP3";




    public SPmeterReadingCP3(Context context) {
        this.context = context;

        //-----------------------------------------------------button1
        spmeterReadingCP3 = this.context.getSharedPreferences(meterReadingCP3, Context.MODE_PRIVATE);
        edMeterReadingCP3 = spmeterReadingCP3.edit();
        edMeterReadingCP3.commit();


    }


    //------------button1
    public String getMeterReadingCP3() {
        return spmeterReadingCP3.getString(ValueMeterReadingCP3, "");
    }

    public void setMeterReadingCP3(String ValueMeterReadingCP3s ) {
        edMeterReadingCP3.putString(ValueMeterReadingCP3, ValueMeterReadingCP3s);
        edMeterReadingCP3.commit();
        edMeterReadingCP3.apply();
    }

    public void clearMeterReadingCP3(){
        edMeterReadingCP3.clear();
        edMeterReadingCP3.commit();
    }

}