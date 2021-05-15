package com.supravin.accharger.Validations;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

// AsyncTask class to check internet connection
public class IsOnlineTask extends AsyncTask<Void, Void, Void> {

    public static boolean flag;                                             //flag for connection status

    protected Void doInBackground(Void... urls) {
        try {
            int timeoutMs = 1000;
            Socket sock = new Socket();
            SocketAddress sockaddr = new InetSocketAddress("8.8.8.8", 53);  //google socket port to check internet

            sock.connect(sockaddr, timeoutMs);                              //connect to socket for checking internet
            flag=sock.isConnected();                                        //get the internet status
            sock.close();


        } catch (IOException e) {
            flag=false;
        }

        return null;
    }
}
