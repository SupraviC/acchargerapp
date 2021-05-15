package com.supravin.accharger.OCPP_Implementation_1_6.Utils;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;


public class InternetCheck extends AsyncTask<Void,Void,Boolean> {

    private Consumer mConsumer;
    public  interface Consumer { void accept(Boolean internet); }

    public InternetCheck(Consumer consumer) { mConsumer = consumer; execute(); }

    @Override protected Boolean doInBackground(Void... voids) {
        /*try {
            Socket sock = new Socket();
            sock.connect(new InetSocketAddress("8.8.8.8", 53), 1500);
            sock.close();
            Log.e("INSERTINTODB"," NetworkAvailable 333 2--> "+true);
            return true;
        } catch (IOException e) {
            Log.e("INSERTINTODB"," NetworkAvailable 333 3--> "+false);
            return false;
        }*/
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }

    @Override protected void onPostExecute(Boolean isAvailableInternet) {
        mConsumer.accept(isAvailableInternet);
        Log.e("CHECKINTERNETNEW"," --> "+isAvailableInternet);
        Log.e("IsAvailableInternet","internet MainClass "+isAvailableInternet);
    }
}

