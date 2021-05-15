package com.supravin.accharger.Storage;

/**
 * Created by Admin on 1/31/2018.
 **/

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferenceIPAddress {

    public static final String PREFS_SERVER_IP = "SERVER_IP_ADDRESS";
    public static final String PREFS_OCPP_ID = "SERVER_OCPP_ID";
    public static final String PREFS_KEY = "UNIT_PREFS_String";
    public static final String PREFS_KEY11 = "UNIT_PREFS_String11";

    public SharedPreferenceIPAddress() {
        super();
    }

    public void saveIPaddress(Context context, String text) {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PREFS_SERVER_IP, Context.MODE_PRIVATE); //1
        editor = settings.edit(); //2
        editor.putString(PREFS_KEY, text); //3
        editor.apply(); //4
    }

    public void saveOCPPId(Context context, String text) {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PREFS_OCPP_ID, Context.MODE_PRIVATE); //1
        editor = settings.edit(); //2
        editor.putString(PREFS_KEY11, text); //3
        editor.apply(); //4
    }


    public String getIPaddress(Context context) {
        SharedPreferences settings;
        String text;
        settings = context.getSharedPreferences(PREFS_SERVER_IP, Context.MODE_PRIVATE);
        text = settings.getString(PREFS_KEY, null);
        return text;
    }

    public String getOCPPId(Context context) {
        SharedPreferences settings;
        String text;
        settings = context.getSharedPreferences(PREFS_OCPP_ID, Context.MODE_PRIVATE);
        text = settings.getString(PREFS_KEY11, null);
        return text;
    }

    public void clearSharedPreference(Context context) {
        SharedPreferences settings;
        Editor editor;

        //settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings = context.getSharedPreferences(PREFS_SERVER_IP, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.clear();
        editor.commit();
    }

    public void removeValue(Context context) {
        SharedPreferences settings;
        Editor editor;

        settings = context.getSharedPreferences(PREFS_SERVER_IP, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.remove(PREFS_KEY);
        editor.commit();
    }
}