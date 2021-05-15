package com.supravin.accharger;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;



import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by User on 08-07-2017.
 */

public class InternetConnectivity {
    private final Context c;

    public InternetConnectivity(Context context, WelcomeActivity mainActivity){
        this.c = context;
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public boolean StartInternet()
    {

        boolean HaveConnectedtoi = false;
        final ConnectivityManager conman = (ConnectivityManager)c.getSystemService(Context.CONNECTIVITY_SERVICE);

        Method dataMtd = null;
        try
        {
            dataMtd = ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled", boolean.class);
        }
        catch (NoSuchMethodException e)
        {
            e.printStackTrace();
        }
        dataMtd.setAccessible(true);
        try
        {
            dataMtd.invoke(conman, true);
            HaveConnectedtoi = true;
        }
        catch (IllegalAccessException | InvocationTargetException e)
        {
            e.printStackTrace();
        }
        return HaveConnectedtoi;
    }

    public void OffInternet()
    {
        final ConnectivityManager conman = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        Method dataMtd = null;
        try
        {
            dataMtd = ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled", boolean.class);
        }
        catch (NoSuchMethodException e)
        {
            e.printStackTrace();
        }
//        Toast.makeText(MainActivity.this," INTERNET off",Toast.LENGTH_SHORT).show();
        dataMtd.setAccessible(false);

        try
        {
            dataMtd.invoke(conman, false);
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch (InvocationTargetException e)
        {
            e.printStackTrace();
        }
        Log.d("Internet","Internet Off");
    }

}
