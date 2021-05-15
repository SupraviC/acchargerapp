package com.supravin.accharger.Storage;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by admin on 05/01/2018.
 */


public class SPisPluggedin {

    private Context context;


    private SharedPreferences spisPluggedinCP1;
    private SharedPreferences.Editor edisPluggedinCP1;
    private static final String isPluggedinCP1 = "isPluggedinCP1";
    private static final String ValueisPluggedinCP1 = "ValueisPluggedinCP1";

    //-----------for CP2
    private SharedPreferences spisPluggedinCP2;
    private SharedPreferences.Editor edisPluggedinCP2;
    private static final String isPluggedinCP2 = "isPluggedinCP2";
    private static final String ValueisPluggedinCP2 = "ValueisPluggedinCP2";

    //-----------for CP3
    private SharedPreferences spisPluggedinCP3;
    private SharedPreferences.Editor edisPluggedinCP3;
    private static final String isPluggedinCP3 = "isPluggedinCP3";
    private static final String ValueisPluggedinCP3 = "ValueisPluggedinCP3";


    public SPisPluggedin(Context context) {
        this.context = context;

        //-----------------------------------------------------button1
        spisPluggedinCP1 = this.context.getSharedPreferences(isPluggedinCP1, Context.MODE_PRIVATE);
        edisPluggedinCP1 = spisPluggedinCP1.edit();
        edisPluggedinCP1.commit();

        //--------------------for cp2
        spisPluggedinCP2 = this.context.getSharedPreferences(isPluggedinCP2, Context.MODE_PRIVATE);
        edisPluggedinCP2 = spisPluggedinCP2.edit();
        edisPluggedinCP2.commit();

        //--------------------for cp3
        spisPluggedinCP3 = this.context.getSharedPreferences(isPluggedinCP3, Context.MODE_PRIVATE);
        edisPluggedinCP3 = spisPluggedinCP3.edit();
        edisPluggedinCP3.commit();


    }


    //------------button1
    public String getisPluggedinCP1() {
        return spisPluggedinCP1.getString(ValueisPluggedinCP1, "");
    }

    public void setisPluggedinCP1(String ValueisPluggedinCP1s) {
        edisPluggedinCP1.putString(ValueisPluggedinCP1, ValueisPluggedinCP1s);
        edisPluggedinCP1.commit();
        edisPluggedinCP1.apply();
    }

    public void clearisPluggedinCP1(){
        edisPluggedinCP1.clear();
        edisPluggedinCP1.commit();
    }


    //------------button2
    public String getisPluggedinCP2() {
        return spisPluggedinCP2.getString(ValueisPluggedinCP2, "");
    }

    public void setisPluggedinCP2(String ValueisPluggedinCP2s) {
        edisPluggedinCP2.putString(ValueisPluggedinCP2, ValueisPluggedinCP2s);
        edisPluggedinCP2.commit();
        edisPluggedinCP2.apply();
    }

    public void clearisPluggedinCP2(){
        edisPluggedinCP2.clear();
        edisPluggedinCP2.commit();
    }

    //------------button3
    public String getisPluggedinCP3() {
        return spisPluggedinCP3.getString(ValueisPluggedinCP3, "");
    }

    public void setisPluggedinCP3(String ValueisPluggedinCP3s) {
        edisPluggedinCP3.putString(ValueisPluggedinCP3, ValueisPluggedinCP3s);
        edisPluggedinCP3.commit();
        edisPluggedinCP3.apply();
    }

    public void clearisPluggedinCP3(){
        edisPluggedinCP3.clear();
        edisPluggedinCP3.commit();
    }
}