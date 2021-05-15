package com.supravin.accharger.Storage;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by admin on 05/01/2018.
 */


public class SPlanguageCP1 {

    private Context context;


    private SharedPreferences splanguageCP1;
    private SharedPreferences.Editor edlanguageCP1;
    private static final String languageCP1 = "languageCP1";
    private static final String ValuelanguageCP1 = "ValuelanguageCP1";




    public SPlanguageCP1(Context context) {
        this.context = context;

        //-----------------------------------------------------button1
        splanguageCP1 = this.context.getSharedPreferences(languageCP1, Context.MODE_PRIVATE);
        edlanguageCP1 = splanguageCP1.edit();
        edlanguageCP1.commit();


    }


    //------------button1
    public String getlanguageCP1() {
        return splanguageCP1.getString(ValuelanguageCP1, "");
    }

    public void setlanguageCP1(String ValuelanguageCP1s ) {
        edlanguageCP1.putString(ValuelanguageCP1, ValuelanguageCP1s);
        edlanguageCP1.commit();
        edlanguageCP1.apply();
    }

    public void clearlanguageCP1(){
        edlanguageCP1.clear();
        edlanguageCP1.commit();
    }

}