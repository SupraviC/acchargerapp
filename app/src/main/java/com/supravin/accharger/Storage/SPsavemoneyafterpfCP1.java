package com.supravin.accharger.Storage;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by admin on 05/01/2018.
 */


public class SPsavemoneyafterpfCP1 {

    private Context context;


    private SharedPreferences spsavemoneyafterpfCP1;
    private SharedPreferences.Editor edsavemoneyafterpfCP1;
    private static final String savemoneyafterpfCP1 = "savemoneyafterpfCP1";
    private static final String ValuesavemoneyafterpfCP1 = "ValuesavemoneyafterpfCP1";




    public SPsavemoneyafterpfCP1(Context context) {
        this.context = context;

        //-----------------------------------------------------button1
        spsavemoneyafterpfCP1 = this.context.getSharedPreferences(savemoneyafterpfCP1, Context.MODE_PRIVATE);
        edsavemoneyafterpfCP1 = spsavemoneyafterpfCP1.edit();
        edsavemoneyafterpfCP1.commit();


    }


    //------------button1
    public String getMeterReadingCP1RO() {
        return spsavemoneyafterpfCP1.getString(ValuesavemoneyafterpfCP1, "");
    }

    public void setMeterReadingCP1RO(String ValueMeterReadingCP1s ) {
        edsavemoneyafterpfCP1.putString(ValuesavemoneyafterpfCP1, ValueMeterReadingCP1s);
        edsavemoneyafterpfCP1.commit();
        edsavemoneyafterpfCP1.apply();
    }

    public void clearMeterReadingCP1RO(){
        edsavemoneyafterpfCP1.clear();
        edsavemoneyafterpfCP1.commit();
    }

}