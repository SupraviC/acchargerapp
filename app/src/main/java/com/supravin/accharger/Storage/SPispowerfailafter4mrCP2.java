package com.supravin.accharger.Storage;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by admin on 05/01/2018.
 */


public class SPispowerfailafter4mrCP2 {

    private Context context;


    private SharedPreferences spispowerfailafter4mrCP2;
    private SharedPreferences.Editor edispowerfailafter4mrCP2;
    private static final String ispowerfailafter4mrCP2 = "ispowerfailafter4mrCP2";
    private static final String Valueispowerfailafter4mrCP2 = "Valueispowerfailafter4mrCP2";




    public SPispowerfailafter4mrCP2(Context context) {
        this.context = context;

        //-----------------------------------------------------button1
        spispowerfailafter4mrCP2 = this.context.getSharedPreferences(ispowerfailafter4mrCP2, Context.MODE_PRIVATE);
        edispowerfailafter4mrCP2 = spispowerfailafter4mrCP2.edit();
        edispowerfailafter4mrCP2.commit();


    }


    //------------button1
    public String getispowerfailafter4mrCP2() {
        return spispowerfailafter4mrCP2.getString(Valueispowerfailafter4mrCP2, "");
    }

    public void setispowerfailafter4mrCP2(String ValueMeterReadingCP1s ) {
        edispowerfailafter4mrCP2.putString(Valueispowerfailafter4mrCP2, ValueMeterReadingCP1s);
        edispowerfailafter4mrCP2.commit();
        edispowerfailafter4mrCP2.apply();
    }

    public void clearispowerfailafter4mrCP2(){
        edispowerfailafter4mrCP2.clear();
        edispowerfailafter4mrCP2.commit();
    }

}