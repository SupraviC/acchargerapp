package com.supravin.accharger.Storage;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by admin on 05/01/2018.
 */


public class SPispowerfailafter4mrCP1 {

    private Context context;


    private SharedPreferences spispowerfailafter4mrCP1;
    private SharedPreferences.Editor edispowerfailafter4mrCP1;
    private static final String ispowerfailafter4mrCP1 = "ispowerfailafter4mrCP1";
    private static final String Valueispowerfailafter4mrCP1 = "Valueispowerfailafter4mrCP1";




    public SPispowerfailafter4mrCP1(Context context) {
        this.context = context;

        //-----------------------------------------------------button1
        spispowerfailafter4mrCP1 = this.context.getSharedPreferences(ispowerfailafter4mrCP1, Context.MODE_PRIVATE);
        edispowerfailafter4mrCP1 = spispowerfailafter4mrCP1.edit();
        edispowerfailafter4mrCP1.commit();


    }


    //------------button1
    public String getispowerfailafter4mrCP1() {
        return spispowerfailafter4mrCP1.getString(Valueispowerfailafter4mrCP1, "");
    }

    public void setispowerfailafter4mrCP1(String ValueMeterReadingCP1s ) {
        edispowerfailafter4mrCP1.putString(Valueispowerfailafter4mrCP1, ValueMeterReadingCP1s);
        edispowerfailafter4mrCP1.commit();
        edispowerfailafter4mrCP1.apply();
    }

    public void clearispowerfailafter4mrCP1(){
        edispowerfailafter4mrCP1.clear();
        edispowerfailafter4mrCP1.commit();
    }

}