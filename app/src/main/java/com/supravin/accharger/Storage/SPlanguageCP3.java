package com.supravin.accharger.Storage;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by admin on 05/01/2018.
 */


public class SPlanguageCP3 {

    private Context context;


    private SharedPreferences splanguageCP3;
    private SharedPreferences.Editor edlanguageCP3;
    private static final String languageCP3 = "languageCP3";
    private static final String ValuelanguageCP3 = "ValuelanguageCP3";




    public SPlanguageCP3(Context context) {
        this.context = context;

        //-----------------------------------------------------button1
        splanguageCP3 = this.context.getSharedPreferences(languageCP3, Context.MODE_PRIVATE);
        edlanguageCP3 = splanguageCP3.edit();
        edlanguageCP3.commit();


    }


    //------------button1
    public String getlanguageCP3() {
        return splanguageCP3.getString(ValuelanguageCP3, "");
    }

    public void setlanguageCP3(String ValuelanguageCP3s ) {
        edlanguageCP3.putString(ValuelanguageCP3, ValuelanguageCP3s);
        edlanguageCP3.commit();
        edlanguageCP3.apply();
    }

    public void clearlanguageCP3(){
        edlanguageCP3.clear();
        edlanguageCP3.commit();
    }

}