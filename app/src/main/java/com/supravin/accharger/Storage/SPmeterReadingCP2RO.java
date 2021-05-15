package com.supravin.accharger.Storage;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by admin on 05/01/2018.
 */


public class SPmeterReadingCP2RO {

    private Context context;


    private SharedPreferences spmeterReadingCP2RO;
    private SharedPreferences.Editor edMeterReadingCP2RO;
    private static final String meterReadingCP2RO = "meterReadingCP2RO";
    private static final String ValueMeterReadingCP2RO = "ValueMeterReadingCP2RO";




    public SPmeterReadingCP2RO(Context context) {
        this.context = context;

        //-----------------------------------------------------button2
        spmeterReadingCP2RO = this.context.getSharedPreferences(meterReadingCP2RO, Context.MODE_PRIVATE);
        edMeterReadingCP2RO = spmeterReadingCP2RO.edit();
        edMeterReadingCP2RO.commit();


    }


    //------------button2
    public String getMeterReadingCP2RO() {
        return spmeterReadingCP2RO.getString(ValueMeterReadingCP2RO, "");
    }

    public void setMeterReadingCP2RO(String ValueMeterReadingCP2s ) {
        edMeterReadingCP2RO.putString(ValueMeterReadingCP2RO, ValueMeterReadingCP2s);
        edMeterReadingCP2RO.commit();
        edMeterReadingCP2RO.apply();
    }

    public void clearMeterReadingCP2RO(){
        edMeterReadingCP2RO.clear();
        edMeterReadingCP2RO.commit();
    }

}