package com.supravin.accharger.Storage;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by admin on 05/01/2018.
 */


public class SPispowerfailafter4mrCP3 {

    private Context context;


    private SharedPreferences spispowerfailafter4mrCP3;
    private SharedPreferences.Editor edispowerfailafter4mrCP3;
    private static final String ispowerfailafter4mrCP3 = "ispowerfailafter4mrCP3";
    private static final String Valueispowerfailafter4mrCP3 = "Valueispowerfailafter4mrCP3";




    public SPispowerfailafter4mrCP3(Context context) {
        this.context = context;

        //-----------------------------------------------------button1
        spispowerfailafter4mrCP3 = this.context.getSharedPreferences(ispowerfailafter4mrCP3, Context.MODE_PRIVATE);
        edispowerfailafter4mrCP3 = spispowerfailafter4mrCP3.edit();
        edispowerfailafter4mrCP3.commit();


    }


    //------------button1
    public String getispowerfailafter4mrCP3() {
        return spispowerfailafter4mrCP3.getString(Valueispowerfailafter4mrCP3, "");
    }

    public void setispowerfailafter4mrCP3(String ValueMeterReadingCP1s ) {
        edispowerfailafter4mrCP3.putString(Valueispowerfailafter4mrCP3, ValueMeterReadingCP1s);
        edispowerfailafter4mrCP3.commit();
        edispowerfailafter4mrCP3.apply();
    }

    public void clearispowerfailafter4mrCP3(){
        edispowerfailafter4mrCP3.clear();
        edispowerfailafter4mrCP3.commit();
    }

}