package com.supravin.accharger.Storage;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by admin on 05/01/2018.
 */


public class SPlanguageCP2 {

    private Context context;


    private SharedPreferences splanguageCP2;
    private SharedPreferences.Editor edlanguageCP2;
    private static final String languageCP2 = "languageCP2";
    private static final String ValuelanguageCP2 = "ValuelanguageCP2";




    public SPlanguageCP2(Context context) {
        this.context = context;

        //-----------------------------------------------------button1
        splanguageCP2 = this.context.getSharedPreferences(languageCP2, Context.MODE_PRIVATE);
        edlanguageCP2 = splanguageCP2.edit();
        edlanguageCP2.commit();


    }


    //------------button1
    public String getlanguageCP2() {
        return splanguageCP2.getString(ValuelanguageCP2, "");
    }

    public void setlanguageCP2(String ValuelanguageCP2s ) {
        edlanguageCP2.putString(ValuelanguageCP2, ValuelanguageCP2s);
        edlanguageCP2.commit();
        edlanguageCP2.apply();
    }

    public void clearlanguageCP2(){
        edlanguageCP2.clear();
        edlanguageCP2.commit();
    }

}