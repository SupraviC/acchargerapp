package com.supravin.accharger;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.supravin.accharger.Storage.SPisgonetoNext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends AppCompatActivity {

    //private static int SPLASH_TIME_OUT = 2770;
    private static int SPLASH_TIME_OUT = 5000;
    private BluetoothAdapter bluetoothAdapter;
    private JSONArray resAuth;
    String xno;
    private String InputFileText = "";
    private String cid = "";
    private File myExternalFile;
    String filename = "Config.txt";
    String filepath = "MyFileStorage";
    private String inputtext;
    private InternetConnectivity iscn;
    Button Start,End;
    Handler authentication=new Handler();
    int flag1=0,flag2=0;
    long o;
    Runnable runnable;
    long mButtonOnePressed,mButtonTwoPressed;
    boolean isGonetonextScreen = false;
    private SPisgonetoNext sPisgonetoNext;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        setContentView(R.layout.activity_welcome);
        iscn = new InternetConnectivity(WelcomeActivity.this, this);
        sPisgonetoNext  = new SPisgonetoNext(WelcomeActivity.this);
        sPisgonetoNext.setisgonetoNext(false);

        //StartInternet();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (!bluetoothAdapter.enable()){
            bluetoothAdapter.enable();
        }
        AppApplication app = (AppApplication) this.getApplicationContext();
        // close_dialog = (LinearLayout) findViewById(R.id.imageView_custom_dialog_close);


      /*  *//**
         *  Initialisation for broadcast receiver for power button press
         * *//*
     
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        BroadcastReceiver mReceiver = new ScreenReceiverBroadcast();
        registerReceiver(mReceiver, filter);*/
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        StartupItem();

/** Checks whether app has started or not
 * This piece of code works once when the app is opened
 * */
        if (app.hasRefreshed) {
            //do nothing
//            Toast.makeText(MainActivity.this, "REFRESHED", Toast.LENGTH_LONG).show();
            String myData = "";
            try {
                FileInputStream fis = new FileInputStream(myExternalFile);
                DataInputStream in = new DataInputStream(fis);
                BufferedReader br =
                        new BufferedReader(new InputStreamReader(in));
                String strLine;
                while ((strLine = br.readLine()) != null) {
                    myData = myData + strLine;
                }
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            inputtext = myData.toString();
            cid=inputtext;


                Handler handler = new Handler();
                handler.postDelayed(new Runnable()
                {
                    public void run()
                    {
                        if(iscn.StartInternet()) {
                        login(cid);
                        }
                        else
                        {
                            if (!sPisgonetoNext.getisgonetoNext()){
                                nextScreen();
                            }
                        }

                    }
                //}, 1000 * 30 );
                }, 1000 * 5 );





        } else {
            Process suProcess = null;
            try {
                suProcess = Runtime.getRuntime().exec("su");
                DataOutputStream os = new DataOutputStream(suProcess.getOutputStream());
                os.writeBytes("adb shell" + "\n");
                os.writeBytes("service call activity 42 s16 com.android.systemui \n");
                os.flush();
                os.writeBytes("settings put global device_provisioned 0 \n");

                myExternalFile = new File(getExternalFilesDir(filepath), filename);
                /**
                 * Code only for first time running while performing text update
                 */
                try {
                    FileOutputStream fos = new FileOutputStream(myExternalFile);
                    fos.write(cid.toString().getBytes());
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                /**
                 *
                 */
                String myData = "";
                try {
                    FileInputStream fis = new FileInputStream(myExternalFile);
                    DataInputStream in = new DataInputStream(fis);
                    BufferedReader br =
                            new BufferedReader(new InputStreamReader(in));
                    String strLine;
                    while ((strLine = br.readLine()) != null) {
                        myData = myData + strLine;
                    }
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                inputtext = myData.toString();

                cid=inputtext;

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable()
                    {
                        public void run()
                        {

                            if(iscn.StartInternet()) {
                                login(cid);
                            }
                            else
                            {
                                if (!sPisgonetoNext.getisgonetoNext()){
                                    nextScreen();
                                }
                            }



                        }
                    //}, 1000 * 30 );
                    }, 1000 * 5 );




            } catch (IOException e) {
                e.printStackTrace();
            }


            app.hasRefreshed = true;
        }




//        if(iscn.StartInternet()) {
//            Handler handler = new Handler();
//            handler.postDelayed(new Runnable()
//            {
//                public void run()
//                {
//
//                    login(cid);
//
//
//                }
//            }, 1000 * 30 );
//
//
//        }

//        new Handler().postDelayed(new Runnable() {
//
//
//            /* * Showing splash screen with a timer. This will be useful when you
//             * want to show case your app logo / company*/
//
//
//            @Override
//            public void run() {
//                // This method will be executed once the timer is over
//                // Start your app main activity
//                Intent i = new Intent(WelcomeActivity.this, InitiatingActivityMannual.class);
//                startActivity(i);
//
//                // close this activity
//                finish();
//            }
//        }, SPLASH_TIME_OUT);

    }

    private void checkIfBothPressed()
    {


    }

    private void StartupItem()
    {
        myExternalFile = new File(getExternalFilesDir(filepath), filename);
        File file = myExternalFile;
        if(file.exists() && file.length()==0)
        {
            //Toast.makeText(this, "file exist length 0", Toast.LENGTH_SHORT).show();
            Naming();

        }
         if(file.exists())
        {
            String myData = "";
            try {
                FileInputStream fis = new FileInputStream(myExternalFile);
                DataInputStream in = new DataInputStream(fis);
                BufferedReader br =
                        new BufferedReader(new InputStreamReader(in));
                String strLine;
                while ((strLine = br.readLine()) != null) {
                    myData = myData + strLine;
                }
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            cid=myData.toString();
//            Toast.makeText(this, "file exist length 1", Toast.LENGTH_SHORT).show();

        }
        else
        {
  //          Toast.makeText(this, "file won exist", Toast.LENGTH_SHORT).show();

            Naming();
        }
    }

    private void Naming()
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String name = preferences.getString("Config", "");
//        Toast.makeText(MainActivity.this, name, Toast.LENGTH_SHORT).show();
        if(!name.equalsIgnoreCase(""))
        {
            cid = name.toString();  /* Edit the value here*/
        }
        else
        {
            cid="AC-3P-002";
        }
    }

//    private void StartInternet() {
//    private void StartInternet() {
//    }

    /**
     * App Update Condition checking
     */

    private void login(final String cid) {

        class LoginAsync extends AsyncTask<String, Void, String> {

            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                loadingDialog = ProgressDialog.show(MainActivity.this, "Please wait", "Loading...");
            }

            @Override
            protected String doInBackground(String... params) {
                String uname = params[0];


                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("chargyfi_id", uname));


                String result = null;

                try {
                    //noinspection deprecation
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(
                            "https://www.chargyfi.com/supracharge/app_update.php");
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpClient.execute(httpPost);

                    HttpEntity entity = response.getEntity();

                    is = entity.getContent();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            }

            protected void onPostExecute(String response) {
                Log.e("Server Response", response + "--");
                if (response != null) {
                    int flag = 0;
                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(response);
                        resAuth = jsonObj.getJSONArray("data");
                        JSONObject feedObj = resAuth.getJSONObject(0);
                        flag = feedObj.getInt("success");
                        //Toast.makeText(LoginActivity.this, String.valueOf(flag), Toast.LENGTH_SHORT).show();
                        Log.e("log in flag ", String.valueOf(flag));
                    } catch (JSONException e) {

                        e.printStackTrace();
                    }

                    if (resAuth.length() == 0) {
//                        loadingDialog.dismiss();
//                        Toast.makeText(MainActivity.this,"RESPONSE null", Toast.LENGTH_LONG).show();
                        if (!sPisgonetoNext.getisgonetoNext()){
                            nextScreen();
                        }
                    }else {
                        if (flag != 0) {
                            SaveUserDataProfile(jsonObj);
                        } else {
                            if (!sPisgonetoNext.getisgonetoNext()){
                                nextScreen();
                            }
                        }
                    }

                }
                else
                    {
                        if (!sPisgonetoNext.getisgonetoNext()){
                            nextScreen();
                        }
                    }

            }
        }

        LoginAsync la = new LoginAsync();
        la.execute(cid);

    }
    private void SaveUserDataProfile(JSONObject response)
    {
        try {
            JSONArray feedArray = response.getJSONArray("user");
            Log.e("STATUS", response.toString());


            JSONObject feedObj = (JSONObject) feedArray.get(0);
            //JSONObject feedObj = resAuth.getJSONObject(0);
            Log.e("status", feedObj.getString("Status"));
            xno=(feedObj.getString("Status"));
//            idstatus=feedObj.getString("Status");
            if(xno.toString().matches("1"))
            {
//                Intent i = new Intent(WelcomeActivity.this, AppUpdateActivity.class);
//                startActivity(i);



                Intent i = new Intent(this, AppUpdateActivity.class);
                String C_ID=cid.toString();

//Create the bundle
                Bundle bundle = new Bundle();

//Add your data to bundle
                bundle.putString("Cid", C_ID);

//Add the bundle to the intent
                i.putExtras(bundle);

//Fire that second activity
                startActivity(i);

            }
            else
            {
               // Toast.makeText(WelcomeActivity.this, "STATUS IS 0", Toast.LENGTH_SHORT).show();
                if (!sPisgonetoNext.getisgonetoNext()){
                    nextScreen();
                }
            }

        }
        catch (JSONException e)
        {
            e.printStackTrace();
            if (!sPisgonetoNext.getisgonetoNext()){
                nextScreen();
            }
        }
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(!hasFocus) {
            // Close every kind of system dialog
            Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            sendBroadcast(closeDialog);
        }
    }

    public void nextScreen(){

        Intent i = new Intent(this, LangSelection.class);
        String C_ID=cid.toString();

//Create the bundle
        Bundle bundle = new Bundle();

//Add your data to bundle
        bundle.putString("Cid", C_ID);

//Add the bundle to the intent
        i.putExtras(bundle);

//Fire that second activity
        startActivity(i);
   //     Toast.makeText(this, cid, Toast.LENGTH_SHORT).show();
        finish();
        sPisgonetoNext.setisgonetoNext(true);
    }

}

