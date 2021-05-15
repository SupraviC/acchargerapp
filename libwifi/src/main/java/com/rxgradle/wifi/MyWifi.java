package com.rxgradle.wifi;

import android.annotation.SuppressLint;
import android.content.Context;

/**
 * Created by LRXx on 2017/11/30.
 */

public class MyWifi {

    @SuppressLint("StaticFieldLeak")
    public static Context context;

    public static void init(Context context){
        MyWifi.context = context.getApplicationContext();
    }
}
