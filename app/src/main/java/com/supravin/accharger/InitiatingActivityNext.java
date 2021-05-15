package com.supravin.accharger;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class InitiatingActivityNext extends Activity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initiating_next);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        //***************
        checkBTState();

        textView1 = (TextView) findViewById(R.id.connecting);
        txt_devices = (TextView) findViewById(R.id.txt_devices);
        textView1.setTextSize(40);
        textView1.setText(" ");



        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        wait(5000);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!txt_devices.getText().toString().equals("devices")) {

                                    //Toast.makeText(InitiatingActivityMannual.this,"loop count : " +timercount,Toast.LENGTH_LONG).show();

                                    textView1.setText("Please wait...");
                                    // Get the device MAC address, which is the last 17 chars in the View
                                    String info = txt_devices.getText().toString();
                                    String address = info.substring(info.length() - 17);

                                    // Make an intent to start next activity while taking an extra which is the MAC address.
                                    Intent i = new Intent(InitiatingActivityNext.this, SettingActivity.class);
                                    i.putExtra(EXTRA_DEVICE_ADDRESS, address);
                                    startActivity(i);
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
                    textView1.setText("Please pair to the device");
                }

            }
        } else {

            isDeviceAvailable = false;
            textView1.setText("Please pair to the device");

        }
    }





    private void checkBTState() {
        // Check device has Bluetooth and that it is turned on
        mBtAdapter= BluetoothAdapter.getDefaultAdapter(); // CHECK THIS OUT THAT IT WORKS!!!
        if(mBtAdapter==null) {
            Toast.makeText(getBaseContext(), "Device does not support Bluetooth", Toast.LENGTH_SHORT).show();
        } else {
            if (mBtAdapter.isEnabled()) {
                Log.d(TAG, "...Bluetooth ON...");
            } else {
                //Prompt user to turn on Bluetooth
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);

            }
        }
    }

}