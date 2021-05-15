package com.supravin.accharger;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.database.FirebaseDatabase;
import com.supravin.accharger.Storage.SPbatteryStatus;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;




public class InitiatingActivityMannual extends Activity {
    // Debugging for LOGCAT
    private static final String TAG = "InitiatingActivityMannual";
    private static final boolean D = true;
    
  
    // declare button for launching website and textview for connection status
    Button tlbutton;
    TextView textView1,txt_devices;
    
    // EXTRA string to send on to mainactivity
    public static String EXTRA_DEVICE_ADDRESS = "device_address";

    // Member fields
    private BluetoothAdapter mBtAdapter;

    boolean isDeviceAvailable = false;
    boolean deviceStatus = false;
    int timercount = 1;
    private SPbatteryStatus sPbatteryStatus;
    private boolean isAlreadyrunning = false;
    private InternetConnectivity iscn;
    String hname="sour";
    Button c1,c2,c3,c4;
    private File myExternalFile;
    String filename = "Config.txt";
    String filepath = "MyFileStorage";
    String cid;
    private FirebaseDatabase mFirebaseInstance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        setContentView(R.layout.activity_initiating);
        //OffInternet();
        GetCid();
//Firebase
        Firebase.setAndroidContext(this);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseInstance.getReference(cid+"-STATUS").setValue("MANUAL");
        mFirebaseInstance.getReference(cid+"-CP1").setValue("ABSENT");
        mFirebaseInstance.getReference(cid+"-CP2").setValue("ABSENT");
        mFirebaseInstance.getReference(cid+"-CP3").setValue("ABSENT");

// Voltage
        mFirebaseInstance.getReference(cid+"-CP1-Voltage").setValue("ABSENT");
        mFirebaseInstance.getReference(cid+"-CP2-Voltage").setValue("ABSENT");
        mFirebaseInstance.getReference(cid+"-CP3-Voltage").setValue("ABSENT");


//Current
        mFirebaseInstance.getReference(cid+"-CP1-Current").setValue("ABSENT");
        mFirebaseInstance.getReference(cid+"-CP2-Current").setValue("ABSENT");
        mFirebaseInstance.getReference(cid+"-CP3-Current").setValue("ABSENT");

 //Power
        mFirebaseInstance.getReference(cid+"-CP1-Power").setValue("ABSENT");
        mFirebaseInstance.getReference(cid+"-CP2-Power").setValue("ABSENT");
        mFirebaseInstance.getReference(cid+"-CP3-Power").setValue("ABSENT");

        //Amount
        mFirebaseInstance.getReference(cid+"-CP1-Amount").setValue("ABSENT");
        mFirebaseInstance.getReference(cid+"-CP2-Amount").setValue("ABSENT");
        mFirebaseInstance.getReference(cid+"-CP3-Amount").setValue("ABSENT");
        //----Full Screen
//        Process suProcess = null;
//        try {
//            suProcess = Runtime.getRuntime().exec("su");
//            DataOutputStream os = new DataOutputStream(suProcess.getOutputStream());
//            os.writeBytes("adb shell" + "\n");
//            os.writeBytes("service call activity 42 s16 com.android.systemui \n");
//            os.flush();
//            os.writeBytes("settings put global device_provisioned 0 \n");
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//--------------
        //--------------
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);



        c1 = findViewById(R.id.checkBox);
        c2 = findViewById(R.id.checkBox2);
        c3 = findViewById(R.id.checkBox3);
        c4 = findViewById(R.id.checkBox4);

        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // update your model (or other business logic) based on isChecked
                hname="s";
            }
        });
        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // update your model (or other business logic) based on isChecked
                hname=hname+"so";
            }
        });
        c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // update your model (or other business logic) based on isChecked
                hname=hname+"soa";
            }
        });
        c4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // update your model (or other business logic) based on isChecked
                if(hname.matches("ssosoa"))
                {







                    Intent i = new Intent(InitiatingActivityMannual.this, Authentication.class);
                    String C_ID=cid.toString();

//Create the bundle
                    Bundle bundle = new Bundle();

//Add your data to bundle
                    bundle.putString("Cid", C_ID);

//Add the bundle to the intent
                    i.putExtras(bundle);

//Fire that second activity
                    startActivity(i);
                    finish();
                }
                else
                {

                    hname="";
                }
            }
        });
    }

    private void GetCid()
    {
        myExternalFile = new File(getExternalFilesDir(filepath), filename);
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
            //      Toast.makeText(this, myData, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        cid=myData.toString();
        Log.e("CIID",cid);
    }


    public void OffInternet()
    {
        final ConnectivityManager conman = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
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


    
    @Override
    public void onResume() 
    {
    	super.onResume();

        isAlreadyrunning = false;
        sPbatteryStatus = new SPbatteryStatus(InitiatingActivityMannual.this);

    	//*************** 
    	checkBTState();

    	textView1 = (TextView) findViewById(R.id.connecting);
        txt_devices = (TextView) findViewById(R.id.txt_devices);
    	textView1.setTextSize(40);
    	textView1.setText("Please Wait to Power Up...");



       Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        wait(5000);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(!isAlreadyrunning) {
                                    reRun();
                                }
                            }
                        });

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //Intent mainActivity = new Intent(getApplicationContext(),InitiatingActivityMannual.class);
               // startActivity(mainActivity);
            };
        };
        thread.start();




    	// Get the local Bluetooth adapter
    	mBtAdapter = BluetoothAdapter.getDefaultAdapter();

    	// Get a set of currently paired devices and append to 'pairedDevices'
    	Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

    	// Add previosuly paired devices to the array
        int count = 1;
    	if (pairedDevices.size() > 0) {

    		for (BluetoothDevice device : pairedDevices) {
                if(device.getName().equals("HC-05"))
                {

                    txt_devices.setText(device.getName() + "\n" + device.getAddress());
                    isDeviceAvailable = true;
                }
                else{

                    count++;
                    isDeviceAvailable = false;
                    textView1.setText("PLEASE WAIT FOR INITIAL CONFIGURATION...");
                    startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                }

    		}
    	} else {

            isDeviceAvailable = false;
            textView1.setText("PLEASE WAIT FOR INITIAL CONFIGURATION...");
            startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);

        }
  }

  public void reRun(){
        if (sPbatteryStatus.getbatteryStatus().equals("Charging")
                || sPbatteryStatus.getbatteryStatus().equals("Full")){
            if (!txt_devices.getText().toString().equals("devices")) {

                //Toast.makeText(InitiatingActivityMannual.this,"loop count : " +timercount,Toast.LENGTH_LONG).show();

                textView1.setText("INITIALISING...");
                // Get the device MAC address, which is the last 17 chars in the View
                String info = txt_devices.getText().toString();
                String address = info.substring(info.length() - 17);

                // Make an intent to start next activity while taking an extra which is the MAC address.
                Intent i = new Intent(InitiatingActivityMannual.this, MaindisplayActivityMannual.class);
                i.putExtra(EXTRA_DEVICE_ADDRESS, address);
                startActivity(i);
                isAlreadyrunning = true;
            }

        }else if (sPbatteryStatus.getbatteryStatus().equals("NCharging")) {
            textView1.setText("POWER FAILURE, WAIT FOR POWER ON...");
            isAlreadyrunning = false;
        }
        else {
            // Toast.makeText(Main2Activity.this, "batteryStatus : "+sPbatteryStatus.getbatteryStatus(), Toast.LENGTH_SHORT).show();

        }
      /*if (!txt_devices.getText().toString().equals("devices")) {

          //Toast.makeText(InitiatingActivityMannual.this,"loop count : " +timercount,Toast.LENGTH_LONG).show();

          textView1.setText("INITIALISING...");
          // Get the device MAC address, which is the last 17 chars in the View
          String info = txt_devices.getText().toString();
          String address = info.substring(info.length() - 17);

          // Make an intent to start next activity while taking an extra which is the MAC address.
          Intent i = new Intent(InitiatingActivityMannual.this, MaindisplayActivityMannual.class);
          i.putExtra(EXTRA_DEVICE_ADDRESS, address);
          startActivity(i);
      }*/
  }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        this.registerReceiver(battery_receiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(battery_receiver);

    }

    private void checkBTState() {
        // Check device has Bluetooth and that it is turned on
    	 mBtAdapter= BluetoothAdapter.getDefaultAdapter(); // CHECK THIS OUT THAT IT WORKS!!!
        if(mBtAdapter==null) { 
        	Toast.makeText(getBaseContext(), "Device does not support Bluetooth", Toast.LENGTH_SHORT).show();
        } else {
          if (mBtAdapter.isEnabled()) {
            Log.d("BluetoothInitiate", "...Bluetooth ON...");
          } else {
            //Prompt user to turn on Bluetooth
           /* Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);*/
              mBtAdapter.enable();
              Log.d("BluetoothInitiate", "...Bluetooth being ON...");


          }
          }
        }

    private final BroadcastReceiver battery_receiver = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onReceive(Context context, Intent intent) {

            final int status = intent.getIntExtra("status", 0);
            Bundle bundle = intent.getExtras();

            Log.i("BatteryLevel", bundle.toString());
            if((getStatusString(status) == "Charging")){
               // Toast.makeText(context, "I Charging", Toast.LENGTH_SHORT).show();
                SPbatteryStatus sPbatteryStatus = new SPbatteryStatus(InitiatingActivityMannual.this);
                sPbatteryStatus.setbatteryStatus("Charging");
                if (!isAlreadyrunning){
                    reRun();
                    isAlreadyrunning = true;
                }
            }

            if(getStatusString(status) == "Full"){
               // Toast.makeText(context, "I Full", Toast.LENGTH_SHORT).show();
                SPbatteryStatus sPbatteryStatus = new SPbatteryStatus(InitiatingActivityMannual.this);
                sPbatteryStatus.setbatteryStatus("Full");
                if (!isAlreadyrunning){
                    reRun();
                    isAlreadyrunning = true;
                }
            }

            if(getStatusString(status) == "Not Charging"){
               // Toast.makeText(context, "I Not Charging", Toast.LENGTH_SHORT).show();
                SPbatteryStatus sPbatteryStatus = new SPbatteryStatus(InitiatingActivityMannual.this);
                sPbatteryStatus.setbatteryStatus("NCharging");
                if (isAlreadyrunning){
                    reRun();
                    isAlreadyrunning = false;
                }
            }
//            }
        }
    };

    private String getStatusString(int status) {
        String statusString = "Unknown";

        switch (status) {
            case BatteryManager.BATTERY_STATUS_CHARGING:
                statusString = "Charging";
                break;
            case BatteryManager.BATTERY_STATUS_DISCHARGING:
                statusString = "Discharging";
                break;
            case BatteryManager.BATTERY_STATUS_FULL:
                statusString = "Full";
                break;
            case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                statusString = "Not Charging";
                break;
        }

        return statusString;
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

}