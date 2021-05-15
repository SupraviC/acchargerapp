package com.supravin.accharger;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import android.view.inputmethod.InputMethodManager;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.database.FirebaseDatabase;
import com.supravin.accharger.PowerPlugged.Power;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.util.UUID;


public class MaindisplayActivityOcpp extends AppCompatActivity implements View.OnClickListener {
    Handler bluetoothIn;
    final int handlerState = 0;
    int intervalcount = 0;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder recDataString = new StringBuilder();

    private MaindisplayActivityOcpp.ConnectedThread mConnectedThread;
    private FirebaseDatabase mFirebaseInstance;

    // SPP UUID service - this should work for most devices
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    //String for MAC address
    private static String address;

    String command = "$000";

    private File myExternalFile;
    String filename = "Config.txt";
    String filepath = "MyFileStorage";
    String cid;
    String hname = "sour";

    static Handler handler = new Handler();
    String Charge_point_no = "";

    Button btn_send2,btn_close2;
    EditText edt_commandname;
    TextView txt_comingdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        setContentView(R.layout.debuglayout);
        Log.e("CHECK", "CHECKING");
       // AppCrash.get().showDialog();
    }

    private void GetCid() {
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
        cid = myData.toString();
        Log.e("CIID", cid);

    }


    @Override
    protected void onRestart() {
        super.onRestart();
        // Toast.makeText(MaindisplayActivityMannual.this,"onRestart Method",Toast.LENGTH_LONG).show();
        Log.e("METHOD", "onRestart Method");
    }

    @Override
    protected void onStart() {
        super.onStart();
        //  Toast.makeText(MaindisplayActivityMannual.this,"onStart Method",Toast.LENGTH_LONG).show();
        Log.e("METHOD", "onStart Method");
    }

    @Override
    public void onResume() {
        super.onResume();
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        btn_send2 = findViewById(R.id.btn_send2);
        btn_close2 = findViewById(R.id.btn_close2);
        edt_commandname = findViewById(R.id.edt_commandname);
        txt_comingdata = findViewById(R.id.txt_comingdata);
        txt_comingdata.setMovementMethod(new ScrollingMovementMethod());


        GetCid();
        Firebase.setAndroidContext(this);
        mFirebaseInstance = FirebaseDatabase.getInstance();




        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mConnectedThread.write("c");
                //  Toast.makeText(MaindisplayActivityMannual.this,"Thread",Toast.LENGTH_LONG).show();

                Log.e("mConnectedThreadSTRING", "c : " + intervalcount);
                intervalcount++;
            }
        }, 5000);*/

        btn_close2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);

                Toast.makeText(MaindisplayActivityOcpp.this, "..."+edt_commandname.getText().toString(), Toast.LENGTH_SHORT).show();

                startActivity(new Intent(MaindisplayActivityOcpp.this,LangSelection.class));
                finish();
            }
        });
        btn_send2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edt_commandname.getText().toString().equals("")){
                    mConnectedThread.write(edt_commandname.getText().toString());
                    Toast.makeText(MaindisplayActivityOcpp.this, "Command = "+edt_commandname.getText().toString(), Toast.LENGTH_SHORT).show();
                    imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);

                }
                else{
                    Toast.makeText(MaindisplayActivityOcpp.this, "Command is empty", Toast.LENGTH_SHORT).show();
                    imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);

                }
            }
        });
        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {

                if (msg.what == handlerState) {                                        //if message is what we want
                    if (Power.isConnected(MaindisplayActivityOcpp.this)) {
                        String readMessage = (String) msg.obj;                                                                // msg.arg1 = bytes from connect thread
                        recDataString.append(readMessage);                                    //keep appending to string until ~
                        int endOfLineIndex = recDataString.indexOf("~");                    // determine the end-of-line
                        //if (endOfLineIndex > 0 && endOfLineIndex < 80) {
                        if (endOfLineIndex > 0) {

                            // make sure there data before ~
                            String dataInPrint = recDataString.substring(0, endOfLineIndex);    // extract string
                            // txtString.setText("Data Received = " + dataInPrint);
                            int dataLength = dataInPrint.length();                            //get length of data received
                            //txtStringLength.setText("String Length = " + String.valueOf(dataLength));
                            if (recDataString.charAt(0) == '#')                                //if it starts with # we know it is what we are looking for
                            {
                                try {


                                    mFirebaseInstance.getReference("DebugModeCharger").setValue(dataInPrint);
                                    txt_comingdata.setText(txt_comingdata.getText().toString()+"\n\n"+dataInPrint);


                                } catch (StringIndexOutOfBoundsException siobe) {
                                    //System.out.println("invalid input");
                                }

                            }

                            recDataString.delete(0, recDataString.length());                    //clear all string data

                        }

                    }

                } else {
                    msg.what = handlerState;
                }

            }
        };

        btAdapter = BluetoothAdapter.getDefaultAdapter();       // get Bluetooth adapter
        checkBTState();


        //Get MAC address from InitiatingActivityMannual via intent

        Intent intent = getIntent();

        //Get the MAC address from the DeviceListActivty via EXTRA
        address = intent.getStringExtra(InitiatingActivityMannual.EXTRA_DEVICE_ADDRESS);

        //create device and set the MAC address
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            // Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_LONG).show();
        }
        // Establish the Bluetooth socket connection.
        try {
            btSocket.connect();
        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {
                //insert code to deal with this
            }
        }
        mConnectedThread = new MaindisplayActivityOcpp.ConnectedThread(btSocket);
        mConnectedThread.start();

        //I send a character when resuming.beginning transmission to check device is connected
        //If it is not an exception will be thrown in the write method and finish() will be called
        mConnectedThread.write("x");
        // Toast.makeText(MaindisplayActivityMannual.this,"OnResume",Toast.LENGTH_LONG).show();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        this.registerReceiver(mReceiver, filter);
        IntentFilter batteryfilter = new IntentFilter();
        batteryfilter.addAction(Intent.ACTION_POWER_CONNECTED);
        batteryfilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        registerReceiver(batteryReceiver, batteryfilter);

    }


    @Override
    protected void onStop() {
        // Toast.makeText(MaindisplayActivityMannual.this,"onStop Method",Toast.LENGTH_LONG).show();
        Log.e("METHOD", "onStop Method");
        unregisterReceiver(mReceiver);
        unregisterReceiver(batteryReceiver);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Toast.makeText(MaindisplayActivityMannual.this,"onDestroy Method",Toast.LENGTH_LONG).show();
        Log.e("METHOD", "onDestroy Method");
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Toast.makeText(MaindisplayActivityMannual.this, device.getName() + " Device found", Toast.LENGTH_LONG).show();
            } else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                //Toast.makeText(MaindisplayActivityMannual.this, device.getName() + " Device is now connected", Toast.LENGTH_LONG).show();

            } else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
                //Toast.makeText(MaindisplayActivityMannual.this, device.getName() + " Device is about to disconnect", Toast.LENGTH_LONG).show();
            } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
            }
        }
    };
    private final BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            if (intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)) {

            } else {
                intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED);

            }
        }
    };


    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {

        return device.createRfcommSocketToServiceRecord(BTMODULEUUID);
        //creates secure outgoing connecetion with BT device using UUID
    }


    @Override
    public void onPause() {
        super.onPause();
        try {
            //Don't leave Bluetooth sockets open when leaving activity
            btSocket.close();
        } catch (IOException e2) {
            //insert code to deal with this
        }
    }

    //Checks that the Android device Bluetooth is available and prompts to be turned on if off
    private void checkBTState() {

        if (btAdapter == null) {
            Toast.makeText(getBaseContext(), "Device does not support bluetooth", Toast.LENGTH_LONG).show();
        } else {
            if (btAdapter.isEnabled()) {
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    @Override
    public void onClick(View view) {


    }


    //create new class for connect thread
    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        //creation of the connect thread
        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                //Create I/O streams for connection
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }


        public void run() {
            byte[] buffer = new byte[256];
            int bytes;

            // Keep looping to listen for received messages
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);           //read bytes from input buffer
                    String readMessage = new String(buffer, 0, bytes);
                    // Send the obtained bytes to the UI Activity via handler
                    bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }

        //write method
        public void write(String input) {
            byte[] msgBuffer = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(msgBuffer);                //write bytes over BT connection via outstream
            } catch (IOException e) {
                finish();
            }
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!hasFocus) {
            // Close every kind of system dialog
            Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            sendBroadcast(closeDialog);
        }
    }
}


