package com.supravin.accharger.Storage;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by admin on 05/01/2018.
 */


public class SPmeterReadingCP1RO {

    private Context context;


    private SharedPreferences spmeterReadingCP1RO;
    private SharedPreferences.Editor edMeterReadingCP1RO;
    private static final String meterReadingCP1RO = "meterReadingCP1RO";
    private static final String ValueMeterReadingCP1RO = "ValueMeterReadingCP1RO";




    public SPmeterReadingCP1RO(Context context) {
        this.context = context;

        //-----------------------------------------------------button1
        spmeterReadingCP1RO = this.context.getSharedPreferences(meterReadingCP1RO, Context.MODE_PRIVATE);
        edMeterReadingCP1RO = spmeterReadingCP1RO.edit();
        edMeterReadingCP1RO.commit();


    }


    //------------button1
    public String getMeterReadingCP1RO() {
        return spmeterReadingCP1RO.getString(ValueMeterReadingCP1RO, "");
    }

    public void setMeterReadingCP1RO(String ValueMeterReadingCP1s ) {
        edMeterReadingCP1RO.putString(ValueMeterReadingCP1RO, ValueMeterReadingCP1s);
        edMeterReadingCP1RO.commit();
        edMeterReadingCP1RO.apply();
    }

    public void clearMeterReadingCP1RO(){
        edMeterReadingCP1RO.clear();
        edMeterReadingCP1RO.commit();
    }

}