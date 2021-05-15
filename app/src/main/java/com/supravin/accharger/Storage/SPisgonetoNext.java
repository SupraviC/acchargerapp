package com.supravin.accharger.Storage;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by admin on 05/01/2018.
 */


public class SPisgonetoNext {

    private Context context;


    private SharedPreferences spisgonetoNext;
    private SharedPreferences.Editor edisgonetoNext;
    private static final String str_isgonetoNext = "isgonetoNext";
    private static final String ValueisgonetoNext = "ValueisgonetoNext";
    boolean value = false;




    public SPisgonetoNext(Context context) {
        this.context = context;

        //-----------------------------------------------------button1
        spisgonetoNext = this.context.getSharedPreferences(str_isgonetoNext, Context.MODE_PRIVATE);
        edisgonetoNext = spisgonetoNext.edit();
        edisgonetoNext.commit();


    }


    //------------button1
    public boolean getisgonetoNext() {
        return spisgonetoNext.getBoolean(ValueisgonetoNext, value);
    }

    public void setisgonetoNext(boolean ValueisgonetoNexts ) {
        edisgonetoNext.putBoolean(ValueisgonetoNext, ValueisgonetoNexts);
        edisgonetoNext.commit();
        edisgonetoNext.apply();
    }

    public void clearisgonetoNext(){
        edisgonetoNext.clear();
        edisgonetoNext.commit();
    }

}