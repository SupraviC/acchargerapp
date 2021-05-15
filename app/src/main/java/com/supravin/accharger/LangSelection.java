package com.supravin.accharger;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.supravin.accharger.Storage.SPlanguageCP1;
import com.supravin.accharger.Storage.SPlanguageCP2;
import com.supravin.accharger.Storage.SPlanguageCP3;
import com.supravin.accharger.Storage.SharedPreferenceIPAddress;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import me.aflak.bluetooth.Bluetooth;

public class LangSelection extends AppCompatActivity {
    private Button btn_submitlang;
    private RadioGroup rg_language;
    private TextView txt_langinst;
    private SPlanguageCP1 sPlanguageCP1;
    private SPlanguageCP2 sPlanguageCP2;
    private SPlanguageCP3 sPlanguageCP3;
    private ImageView massTechLogoConfigure;
    private SharedPreferenceIPAddress sharedPreference;
    private int tapCount = 0;
    Button c1,c2,c3,c4;
    String hname="sour";
    String cid;
    AlertDialog builder;
    Bluetooth bluetooth;
    private TextView txt_VersionNoI;
    SharedPreferenceIPAddress sharedPreferenceIPAddress;
    private int countTap=0;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bluetooth = new Bluetooth(LangSelection.this);
        sharedPreferenceIPAddress = new SharedPreferenceIPAddress();
        sPlanguageCP1 = new SPlanguageCP1(LangSelection.this);
        sPlanguageCP2 = new SPlanguageCP2(LangSelection.this);
        sPlanguageCP3 = new SPlanguageCP3(LangSelection.this);
        setContentView(R.layout.activity_lang_selection);
        btn_submitlang = findViewById(R.id.btn_submitlang);
        txt_langinst = findViewById(R.id.txt_langinst);
        rg_language = findViewById(R.id.rg_language);
        txt_langinst.setSelected(true);
        WifiManager wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        boolean wifiEnabled = wifiManager.isWifiEnabled();

        txt_VersionNoI=findViewById(R.id.txt_VersionNoI);
        Toast.makeText(getApplicationContext()," URL --> "+sharedPreferenceIPAddress.getIPaddress(this),Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext()," ID --> "+sharedPreferenceIPAddress.getOCPPId(this),Toast.LENGTH_SHORT).show();
        findViewById(R.id.masstechLogo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LangSelection.this,SettingActivity.class));
                finish();
            }
        });
        try {
            txt_VersionNoI.setVisibility(View.VISIBLE);
            txt_VersionNoI.setText("V"+getApplicationContext().getPackageManager()
                    .getPackageInfo(getApplicationContext().getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            txt_VersionNoI.setVisibility(View.INVISIBLE);
        }
        /* if(wifiEnabled)
        {
            wifiManager.setWifiEnabled(false);
          //  Toast.makeText(this, "WIFI DISABLED", Toast.LENGTH_SHORT).show();
        }*/
        Bundle bundle = getIntent().getExtras();

        sharedPreference = new SharedPreferenceIPAddress();

        //massTechLogoConfigure = findViewById(R.id.masstechLogo);

/*
        findViewById(R.id.masstechLogo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tapCount == 4){
                    //dialogEnterotpforstart();
                    startActivity(new Intent(LangSelection.this,InitiatingActivityNext.class));
                  //  Toast.makeText(getApplicationContext(),"Tap Count ==> "+tapCount,Toast.LENGTH_SHORT).show();
                    tapCount = 0;
                }else {
                    tapCount++;
                    //Toast.makeText(getApplicationContext(),"Tap Count ==> "+tapCount,Toast.LENGTH_SHORT).show();
                }

            }
        });
*/

        c1 = findViewById(R.id.checkBox);
        c2 = findViewById(R.id.checkBox2);
        c3 = findViewById(R.id.checkBox3);
        c4 = findViewById(R.id.checkBox4);

//Extract the data…
        //if (!bundle.getString("cid").equals("")){
        if (bundle != null) {
            cid = bundle.getString("Cid");
        }
        //}



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

                    Intent i = new Intent(LangSelection.this, Authentication.class);
                    String C_ID = null;
                    if (!TextUtils.isEmpty(cid)) {
                         C_ID = cid.toString();
                    }
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


        btn_submitlang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int selectedRadioButtonID = rg_language.getCheckedRadioButtonId();

                // If nothing is selected from Radio Group, then it return -1
                if (selectedRadioButtonID != -1) {

                    RadioButton selectedRadioButton = (RadioButton) findViewById(selectedRadioButtonID);
                    String selectedRadioButtonText = selectedRadioButton.getText().toString().trim();
                    String selectedRadioButtonHint = selectedRadioButton.getHint().toString();
                    String selectedRadioid = String.valueOf(selectedRadioButton.getId());

                    switch (selectedRadioButtonHint) {
                        case "en":
                            sPlanguageCP1.setlanguageCP1("en");
                            sPlanguageCP2.setlanguageCP2("en");
                            sPlanguageCP3.setlanguageCP3("en");
                            break;
                        case "hi":
                            sPlanguageCP1.setlanguageCP1("hi");
                            sPlanguageCP2.setlanguageCP2("hi");
                            sPlanguageCP3.setlanguageCP3("hi");
                            break;
                        case "ma":
                            sPlanguageCP1.setlanguageCP1("ma");
                            sPlanguageCP2.setlanguageCP2("ma");
                            sPlanguageCP3.setlanguageCP3("ma");
                            break;
                    }

                  /* if (ConnectivityReceiver.isConnected()){*/
                       if (sharedPreferenceIPAddress.getIPaddress(LangSelection.this)!=null
                       || sharedPreferenceIPAddress.getOCPPId(LangSelection.this)!=null){
                           /*if (countTap>=1){*/
                               startActivity(new Intent(LangSelection.this, InstruSelection.class));
                               finish();
                           /*}else {
                               countTap++;
                               Toast.makeText(LangSelection.this, "Are You Sure You Want To Continue With"+sharedPreferenceIPAddress.getIPaddress(LangSelection.this)+"This WebSocket Address", Toast.LENGTH_SHORT).show();
                           }*/
                       }else {
                           Toast.makeText(LangSelection.this, "Please Configure WebSocket Address and Id", Toast.LENGTH_SHORT).show();
                       }
                   /*}else {
                       Toast.makeText(LangSelection.this, "Please Check Internet Connection & Try Again", Toast.LENGTH_SHORT).show();
                   }*/
                } else{
                    Toast.makeText(LangSelection.this, "Please Select Language", Toast.LENGTH_SHORT).show();

                }
            }
        });

       // throw new RuntimeException("This is a crash");
      /*  startActivity(new Intent(LangSelection.this, InstruSelection.class));
        finish();*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("Bluetooth","Enable");
        bluetooth.onStart();
        if (!bluetooth.isEnabled()){
            bluetooth.enable();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("Bluetooth","Disable");
        bluetooth.onStop();

    }

    @Override
    protected void onResume() {
        super.onResume();
       // String myIp = getLocalIpAddress();
        //Toast.makeText(this, "MyIpaddress : "+myIp, Toast.LENGTH_SHORT).show();
        //Log.e("MyIpaddress",myIp);
    }
    public String getLocalIpAddress(){
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
                 en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (Exception ex) {
            Log.e("IP Address", ex.toString());
        }
        return null;
    }

    public void dialogEnterotpforstart(){
        LayoutInflater layoutInflater = this.getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.layout_ip_address,null);
        builder = new AlertDialog.Builder(this).create();
        builder.setView(view);
        final EditText editIP = (EditText)view.findViewById(R.id.edit_ServerIpAddress);
        final EditText editPORT = (EditText)view.findViewById(R.id.edit_ServerPort);
        final Button btn_config = (Button)view.findViewById(R.id.btn_config);
        ImageView close_dialog = (ImageView) view.findViewById(R.id.imageView_custom_dialog_close_config);
        close_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.dismiss();
                builder.cancel();
            }
        });
        btn_config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( editIP.getText().toString().length() == 0 )
                {
                    editIP.setError( "Please Enter IP Address" );
                    editIP.requestFocus();
                }else if (editIP.getText().toString().length() < 9){
                    editIP.setError( "Please Enter Valid IP Address" );
                    editIP.requestFocus();
                }
                else if (editPORT.getText().toString().length() == 0 ){
                    editPORT.setError( "Enter Valid Port Number" );
                    editPORT.requestFocus();
                }else if (editPORT.getText().toString().length() < 4){
                    editPORT.setError( "Please Enter Valid Port Number" );
                    editPORT.requestFocus();
                }
                else                                                    //if all validations are correct then
                {
                    String ipAddressAndPort = editIP.getText().toString().trim() +":"+editPORT.getText().toString().trim();

                    Toast.makeText(getApplicationContext(),ipAddressAndPort,Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.show();
    }
}
