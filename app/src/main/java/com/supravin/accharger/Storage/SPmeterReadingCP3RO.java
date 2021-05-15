package com.supravin.accharger.Storage;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by admin on 05/01/2018.
 */


public class SPmeterReadingCP3RO {

    private Context context;


    private SharedPreferences spmeterReadingCP3RO;
    private SharedPreferences.Editor edMeterReadingCP3RO;
    private static final String meterReadingCP3RO = "meterReadingCP3RO";
    private static final String ValueMeterReadingCP3RO = "ValueMeterReadingCP3RO";




    public SPmeterReadingCP3RO(Context context) {
        this.context = context;

        //-----------------------------------------------------button1
        spmeterReadingCP3RO = this.context.getSharedPreferences(meterReadingCP3RO, Context.MODE_PRIVATE);
        edMeterReadingCP3RO = spmeterReadingCP3RO.edit();
        edMeterReadingCP3RO.commit();


    }


    //------------button1
    public String getMeterReadingCP3RO() {
        return spmeterReadingCP3RO.getString(ValueMeterReadingCP3RO, "");
    }

    public void setMeterReadingCP3RO(String ValueMeterReadingCP3s ) {
        edMeterReadingCP3RO.putString(ValueMeterReadingCP3RO, ValueMeterReadingCP3s);
        edMeterReadingCP3RO.commit();
        edMeterReadingCP3RO.apply();
    }

    public void clearMeterReadingCP3RO(){
        edMeterReadingCP3RO.clear();
        edMeterReadingCP3RO.commit();
    }

}