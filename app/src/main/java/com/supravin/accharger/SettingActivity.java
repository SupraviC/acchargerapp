package com.supravin.accharger;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.supravin.accharger.Storage.SharedPreferenceIPAddress;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SettingActivity extends AppCompatActivity {
    // private CardView toggleBtn;
    private TextView txt_GoBack,txtPreviousURL/*,toggleValue*/;
    boolean flag=false;


    private EditText editIpAddress,editPortNumber;
    private Button btn_configip;


    //------------ CheckBox -------------//
    private CheckBox chbx_Earth, chbx_Tempt;
    //------------ CheckBox -------------//


    String red="#F44336";
    String green="#76FF03";

    //integrated 30102017
    //Handler bluetoothIn;
    /*private StringBuilder recDataString = new StringBuilder();
    private ConnectedThread mConnectedThread;
    final int handlerState = 0;
    private static String address;*/
   /* private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    String edcommand= "*CD";*/
    SharedPreferenceIPAddress sharedPreferenceIPAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        sharedPreferenceIPAddress = new SharedPreferenceIPAddress();
        //  toggleBtn =  findViewById(R.id.toggleBtn);
      //  txt_arduinoop  =  findViewById(R.id.txt_arduinoop);
        // toggleValue =  findViewById(R.id.toggleValue);
        // btAdapter = BluetoothAdapter.getDefaultAdapter();       // get Bluetooth adapter

        //-------------- CheckBox------------//
        txtPreviousURL = findViewById(R.id.txtPreviousURL);
        chbx_Earth = findViewById(R.id.chbx_Earth);
        chbx_Tempt = findViewById(R.id.chbx_Tempt);
        //-------------- CheckBox------------//

        txt_GoBack = findViewById(R.id.btnGoBack);

        btn_configip = findViewById(R.id.btnConfigure);
        editIpAddress = findViewById(R.id.edit_IPAddress);
        editPortNumber = findViewById(R.id.edit_Port);

        if (sharedPreferenceIPAddress.getIPaddress(SettingActivity.this)!=null){
            editIpAddress.setText(sharedPreferenceIPAddress.getIPaddress(SettingActivity.this));
        }

        btn_configip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( editIpAddress.getText().toString().length() == 0 ) {
                    editIpAddress.setError( "Enter Websocket URL" );
                    editIpAddress.requestFocus();
                }/*else if (editIpAddress.getText().toString().length() < 9){
                    editIpAddress.setError( "Please Enter Valid IP Address" );
                    editIpAddress.requestFocus();
                }*/
                else if (editPortNumber.getText().toString().length() == 0 ){
                    editPortNumber.setError( "Enter OCPP ID" );
                    editPortNumber.requestFocus();
                }/*else if (editPortNumber.getText().toString().length() < 4){
                    editPortNumber.setError( "Please Enter Valid Port Number" );
                    editPortNumber.requestFocus();
                }*/
                else                                                    //if all validations are correct then
                {
                    //"ws://139.59.67.244:8282"
                    //String ipAddressAndPort = "ws://"+editIpAddress.getText().toString().trim() +":"+editPortNumber.getText().toString().trim();
                    //ws://cms.chargezone.co/WSEVCharge.ashx/MH0001
                    String ipAddressAndPort = editIpAddress.getText().toString().trim();
                    String OCPPId= editPortNumber.getText().toString().trim();
                    sharedPreferenceIPAddress.saveIPaddress(SettingActivity.this,ipAddressAndPort);
                    sharedPreferenceIPAddress.saveOCPPId(SettingActivity.this,OCPPId);
                    Toast.makeText(getApplicationContext(),sharedPreferenceIPAddress.getIPaddress(SettingActivity.this),Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(),sharedPreferenceIPAddress.getOCPPId(SettingActivity.this),Toast.LENGTH_SHORT).show();
                }
            }
        });
        txt_GoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingActivity.this,LangSelection.class));
            }
        });




/*
        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {

                if (msg.what == handlerState) {                                        //if message is what we want

                    String readMessage = (String) msg.obj;                                                                // msg.arg1 = bytes from connect thread
                    recDataString.append(readMessage);                                    //keep appending to string until ~
                    int endOfLineIndex = recDataString.indexOf("~");                    // determine the end-of-line
                    //if (endOfLineIndex > 0 && endOfLineIndex < 80) {
                    if (endOfLineIndex > 0) {

                        // make sure there data before ~
                        String dataInPrint = recDataString.substring(0, endOfLineIndex);    // extract string
                        //txt_arduinoop.setText("Data Received = " + dataInPrint);
                        int dataLength = dataInPrint.length();                            //get length of data received
                        //txtStringLength.setText("String Length = " + String.valueOf(dataLength));
                        if (recDataString.charAt(0) == '#')                                //if it starts with # we know it is what we are looking for
                        {
                            try {


                                int p1startV = recDataString.indexOf("a") + 1;
                                int p1startC = recDataString.indexOf("b") + 1;
                                int p1startM = recDataString.indexOf("d") + 1;
                                int p2startV = recDataString.indexOf("f") + 1;
                                int p2startC = recDataString.indexOf("i") + 1;
                                int p2startM = recDataString.indexOf("j") + 1;
                                int p3startV = recDataString.indexOf("l") + 1;
                                int p3startC = recDataString.indexOf("m") + 1;
                                int p3startM = recDataString.indexOf("n") + 1;

                                int p1endV = recDataString.indexOf("b");
                                int p1endC = recDataString.indexOf("d");
                                int p1endM = recDataString.indexOf("e");
                                int p2endV = recDataString.indexOf("i");
                                int p2endC = recDataString.indexOf("j");
                                int p2endM = recDataString.indexOf("k");
                                int p3endV = recDataString.indexOf("m");
                                int p3endC = recDataString.indexOf("n");
                                int p3endM = recDataString.indexOf("%");

                                String p1OnOff = recDataString.substring(recDataString.indexOf("#") + 1, recDataString.indexOf("#") + 2);
                                String p1plugin = recDataString.substring(recDataString.indexOf("#") + 2, recDataString.indexOf("#") + 3);
                                String p1fault = recDataString.substring(3, recDataString.indexOf("a"));
                                String p1voltage = recDataString.substring(p1startV, p1endV);

                                String p1current = recDataString.substring(p1startC, p1endC);


                                String p1meter = recDataString.substring(p1startM, p1endM);

                                String p2OnOff = recDataString.substring(recDataString.indexOf("e") + 1, recDataString.indexOf("e") + 2);
                                String p2plugin = recDataString.substring(recDataString.indexOf("e") + 2, recDataString.indexOf("e") + 3);
                                String p2fault = recDataString.substring(recDataString.indexOf("e") + 3, recDataString.indexOf("f"));
                                String p2voltage = recDataString.substring(p2startV, p2endV);

                                String p2current = recDataString.substring(p2startC, p2endC);
                                String p2meter = recDataString.substring(p2startM, p2endM);
                                String p3OnOff = recDataString.substring(recDataString.indexOf("k") + 1, recDataString.indexOf("k") + 2);
                                String p3plugin = recDataString.substring(recDataString.indexOf("k") + 2, recDataString.indexOf("k") + 3);
                                String p3fault = recDataString.substring(recDataString.indexOf("k") + 3, recDataString.indexOf("l"));
                                String p3voltage = recDataString.substring(p3startV, p3endV);

                                String p3current = recDataString.substring(p3startC, p3endC);
                                String p3meter = recDataString.substring(p3startM, p3endM);
                                String emergency = recDataString.substring(p3endM + 1, p3endM + 2);




                                Log.e("CHECKII", "CHECKING");

                                Log.e("COMMING STRING", recDataString + "\n\np1OnOff :" + p1OnOff + "\tp1plugin :" + p1plugin + "\tp1fault :" + p1fault + "\tp1voltage :" + p1voltage + "\tp1current :" + p1current + "\tp1meter :" + p1meter +
                                        "\n\np2OnOff : " + p2OnOff + "\tp2plugin : " + p2plugin + "\tp2fault : " + p2fault + "\tp2voltage : " + p2voltage + "\tp2current : " + p2current + "\tp2meter : " + p2meter +
                                        "\n\np3OnOff : " + p3OnOff + "\tp3plugin : " + p3plugin + "\tp3fault : " + p3fault + "\tp3voltage: " + p3voltage + "\tp3current : " + p3current + "\tp3meter : " + p3meter);


                            } catch (StringIndexOutOfBoundsException siobe) {
                                //System.out.println("invalid input");
                            }

                        }

                        recDataString.delete(0, recDataString.length());                    //clear all string data

                    }

                }

            }


        };
*/




       /* btAdapter = BluetoothAdapter.getDefaultAdapter();
        checkBTState();*/

/*
        chbx_Earth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Toast.makeText(getApplicationContext(),"Earth Fault Detection Enable...",Toast.LENGTH_SHORT).show();

                    StringBuilder newCommandp1 = new StringBuilder(edcommand);
                    newCommandp1.setCharAt(1, 'A');
                    edcommand = ""+newCommandp1;
                    mConnectedThread.write(edcommand);
                    Toast.makeText(getApplicationContext(),"edcommand : "+edcommand,Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(getApplicationContext(),"Earth Fault Detection Disable...",Toast.LENGTH_SHORT).show();
                    StringBuilder newCommandp1 = new StringBuilder(edcommand);
                    newCommandp1.setCharAt(1, 'C');
                    edcommand = ""+newCommandp1;
                    mConnectedThread.write(edcommand);
                    Toast.makeText(getApplicationContext(),"edcommand : "+edcommand,Toast.LENGTH_SHORT).show();

                }
            }
        });
*/

/*
        chbx_Tempt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Toast.makeText(getApplicationContext(),"Temperature Hazard Detection Enable...",Toast.LENGTH_SHORT).show();
                    StringBuilder newCommandp1 = new StringBuilder(edcommand);
                    newCommandp1.setCharAt(2, 'B');
                    edcommand = ""+newCommandp1;
                    mConnectedThread.write(edcommand);
                    Toast.makeText(getApplicationContext(),"edcommand : "+edcommand,Toast.LENGTH_SHORT).show();


                }else {
                    Toast.makeText(getApplicationContext(),"Temperature Hazard Detection Disable...",Toast.LENGTH_SHORT).show();
                    StringBuilder newCommandp1 = new StringBuilder(edcommand);
                    newCommandp1.setCharAt(2, 'D');
                    edcommand = ""+newCommandp1;
                    mConnectedThread.write(edcommand);
                    Toast.makeText(getApplicationContext(),"edcommand : "+edcommand,Toast.LENGTH_SHORT).show();

                }
            }
        });

*/
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
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }


/*        public void run() {
            byte[] buffer = new byte[256];
            int bytes;

            // Keep looping to listen for received messages
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);        	//read bytes from input buffer
                    String readMessage = new String(buffer, 0, bytes);
                    // Send the obtained bytes to the UI Activity via handler
                    bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }
 */       //write method
/*
        public void write(String input) {
            byte[] msgBuffer = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(msgBuffer);                //write bytes over BT connection via outstream
            } catch (IOException e) {
                //if you cannot write, close the application
                Toast.makeText(getBaseContext(), "Connection Failure", Toast.LENGTH_LONG).show();
                startActivity(new Intent(SettingActivity.this,LangSelection.class));
                finish();

            }
        }
*/
    }

   /* private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {

        return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
        //creates secure outgoing connecetion with BT device using UUID
    }*/
    @Override
    public void onResume() {
        super.onResume();

        //Get MAC address from InitiatingActivityMannual via intent
        //Intent intent = getIntent();

        //Get the MAC address from the DeviceListActivty via EXTRA
        //address = intent.getStringExtra(InitiatingActivityMannual.EXTRA_DEVICE_ADDRESS);

        //create device and set the MAC address
//        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        /*try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_LONG).show();
        }*/
        // Establish the Bluetooth socket connection.
        /*try
        {
            btSocket.connect();
        } catch (IOException e) {
            try
            {
                btSocket.close();
            } catch (IOException e2)
            {
                //insert code to deal with this
            }
        }*/
        /*mConnectedThread = new ConnectedThread(btSocket);
        mConnectedThread.start();

        //I send a character when resuming.beginning transmission to check device is connected
        //If it is not an exception will be thrown in the write method and finish() will be called
        mConnectedThread.write("x");*/
    }

    @Override
    public void onPause()
    {
        super.onPause();
      /*  try
        {
            //Don't leave Bluetooth sockets open when leaving activity
            btSocket.close();
        } catch (IOException e2) {
            //insert code to deal with this
        }*/
    }
    //Checks that the Android device Bluetooth is available and prompts to be turned on if off
/*
    private void checkBTState() {

        if(btAdapter==null) {
            Toast.makeText(getBaseContext(), "Device does not support bluetooth", Toast.LENGTH_LONG).show();
        } else {
            if (btAdapter.isEnabled()) {
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }
*/


}

