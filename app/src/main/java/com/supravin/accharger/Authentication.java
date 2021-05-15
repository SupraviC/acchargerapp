package com.supravin.accharger;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.supravin.accharger.AlertDialog.CustomAlertDialog;
import com.supravin.accharger.AlertDialog.DialogClickInterface;
import com.supravin.accharger.Bluetooth.DeviceListFragment;
import com.supravin.accharger.Storage.SharedPreferenceUnitR;

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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Authentication extends AppCompatActivity implements DeviceListFragment.OnFragmentInteractionListener,DialogClickInterface {

    private DeviceListFragment mDeviceListFragment;
    private BluetoothAdapter BTAdapter;
    Button b3,PowerOff,Rebootd,CheckUpdate,Internet;
    RelativeLayout rl1;
    String versionnumber,versionname;
    TextView versionNamet,versionNumbert,modelnumber,UnitRate;
    String filename = "Config.txt";
    String filepath = "MyFileStorage";
    private File myExternalFile;
    private String InputFileText = "";
    private String cid ;
    private String inputtext;
    private JSONArray resAuth;
    String xno;
    private int cinr = -120;
    private MyPhoneStateListener MyListener;
    private TelephonyManager Tel;
    EditText edittext2;
    Button b6;
    private int identifier = 0;
    private ImageView zero;
    private ImageView one;
    private ImageView two;
    private ImageView three;
    private ImageView four;
    private ImageView five;
    private ImageView six;
    public static int REQUEST_BLUETOOTH = 1;
    private ToggleButton toggleButton1;
    private InternetConnectivity iscn;
    private SharedPreferenceUnitR sharedPreference;
    EditText ed13;
    Button button30,WifiSettings;
    Activity context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        BTAdapter = BluetoothAdapter.getDefaultAdapter();
              b3 = findViewById(R.id.button3);
        PowerOff = findViewById(R.id.button4);
        Rebootd = findViewById(R.id.button5);
        Internet = findViewById(R.id.button8);
        edittext2 = findViewById(R.id.editText2);
        UnitRate = findViewById(R.id.editText);
        b6 = findViewById(R.id.button6);
        WifiSettings = findViewById(R.id.button80);

        button30 = findViewById(R.id.button30);
        ed13 = findViewById(R.id.editText3);

        rl1 = findViewById(R.id.container);
        toggleButton1=(ToggleButton)findViewById(R.id.toggleButton);
        CheckUpdate = findViewById(R.id.button7);
        versionNamet   = findViewById(R.id.textView8);
        versionNumbert = findViewById(R.id.textView6);
        modelnumber = findViewById(R.id.textView9);
        zero            = (ImageView) findViewById(R.id.zero);
        one             = (ImageView) findViewById(R.id.one);
        two             = (ImageView) findViewById(R.id.two);
        three           = (ImageView) findViewById(R.id.three);
        four            = (ImageView) findViewById(R.id.four);
        five            = (ImageView) findViewById(R.id.five);
        six             = (ImageView) findViewById(R.id.six);
        sharedPreference = new SharedPreferenceUnitR();
        Bundle bundle = getIntent().getExtras();

//Extract the data…
        cid = bundle.getString("Cid");
     //   modelnumber.setText(cid);
     //   Toast.makeText(this, cid, Toast.LENGTH_SHORT).show();
        BluetoothDiscoverable();
        GetVersionDetails();
        GetDataConnDetails();
        UnitRate.setText("1 Unit = Rs "+sharedPreference.getValue(context));
        myExternalFile = new File(getExternalFilesDir(filepath), filename);

        getCid();
        PowerOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //To Power Off Device
                try {
                    Process proc = Runtime.getRuntime()
                            .exec(new String[]{ "su", "-c", "reboot -p" });
                    proc.waitFor();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        WifiSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //To Power Off Device
                startActivity(new Intent("com.haier.wifi.list"));
            }
        });


        button30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //To Power Off Device
//                File dir = getFilesDir();
//                File file = new File(dir, "my_filename");
                if(ed13.getText().toString().length()>1) {
                    boolean deleted = myExternalFile.delete();


                    try {
                        FileOutputStream fos = new FileOutputStream(myExternalFile);
                        fos.write(ed13.getText().toString().getBytes());
                        fos.close();
                        
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(Authentication.this, "Charger Id has been Set", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(Authentication.this, "Please Enter Charger ID", Toast.LENGTH_SHORT).show();
                }
            }
        });


        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String text = edittext2.getText().toString();

                // Hides the soft keyboard
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(textEtxt.getWindowToken(), 0);

                // Save the text in SharedPreference
                sharedPreference.save(context, text);
                Toast.makeText(context,
                       "Rates Saved",
                        Toast.LENGTH_LONG).show();
            }
        });


        Internet.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {

                final ConnectivityManager conman = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

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

                }

                catch (IllegalAccessException | InvocationTargetException e)
                {
                    e.printStackTrace();
                }
                GetDataConnDetails();
                       }
        });


        CheckUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //To CheckFor Update
//                String yourFilePath = getFilesDir() + "/" + "Config.txt";
//
//                File yourFile = new File( yourFilePath );
//
//                File yourFile = new File( yourFilePath );
//
//                String data = getTextFileData(filename);


             //   cid=("AC-03-07");
                Log.e("Server", cid + "--");
            login(cid);
            }
        });

        Rebootd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //To Power Off Device
                try {
                    Process proc = Runtime.getRuntime()
                            .exec(new String[]{ "su", "-c", "reboot" });
                    proc.waitFor();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // update your model (or other business logic) based on isChecked




                Intent i = new Intent(Authentication.this, LangSelection.class);
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
        });

        // Phone does not support Bluetooth so let the user know and exit.
        if (BTAdapter == null) {
            new AlertDialog.Builder(this)
                    .setTitle("Not compatible")
                    .setMessage("Your phone does not support Bluetooth")
                    .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        if (!BTAdapter.isEnabled()) {
            Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBT, REQUEST_BLUETOOTH);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();

        mDeviceListFragment = DeviceListFragment.newInstance(BTAdapter);
        fragmentManager.beginTransaction().replace(R.id.container, mDeviceListFragment).commit();
        getRSSI();
    }

    private void getCid() {

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
       // cid=myData.toString();
        modelnumber.setText(myData.toString());
    }

    private void GetDataConnDetails()
    {
        boolean mobileDataEnabled = false; // Assume disabled
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            Class cmClass = Class.forName(cm.getClass().getName());
            Method method = cmClass.getDeclaredMethod("getMobileDataEnabled");
            method.setAccessible(true); // Make the method callable
            // get the setting for "mobile data"
            mobileDataEnabled = (Boolean)method.invoke(cm);
           // Toast.makeText(this, "Enabled", Toast.LENGTH_SHORT).show();
           toggleButton1.setChecked(true);
        } catch (Exception e) {
            // Some problem accessible private API
            // TODO do whatever error handling you want here
            toggleButton1.setChecked(false);
        }
    }

    private void getRSSI()
    {
        MyListener = new MyPhoneStateListener();
        Context context = getApplicationContext();
        Tel = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        Tel.listen(MyListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        //noinspection UnnecessaryLocalVariable
        int GSMCinr = MyListener.getCinr();

    }

    public class MyPhoneStateListener extends PhoneStateListener
    {
        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength)
        {
            super.onSignalStrengthsChanged(signalStrength);
            cinr = (signalStrength.getGsmSignalStrength());
            int value = -(cinr * 2 - 113);
            int check_sim = Tel.getSimState();
            if (value <= 70 && check_sim == 5)
            {
                zero.setVisibility(View.VISIBLE);
                one.setVisibility(View.VISIBLE);
                two.setVisibility(View.VISIBLE);
                three.setVisibility(View.VISIBLE);
                four.setVisibility(View.VISIBLE);
                five.setVisibility(View.VISIBLE);
                six.setVisibility(View.VISIBLE);

            }  else if (value > 70 && value <= 89 && check_sim == 5)
            {
                zero.setVisibility(View.VISIBLE);
                one.setVisibility(View.VISIBLE);
                two.setVisibility(View.VISIBLE);
                three.setVisibility(View.VISIBLE);
                four.setVisibility(View.VISIBLE);
                five.setVisibility(View.VISIBLE);
                six.setVisibility(View.INVISIBLE);

            }
            else if (value > 89 && value <= 95 && check_sim == 5)
            {
                zero.setVisibility(View.VISIBLE);
                one.setVisibility(View.VISIBLE);
                two.setVisibility(View.VISIBLE);
                three.setVisibility(View.VISIBLE);
                four.setVisibility(View.VISIBLE);
                five.setVisibility(View.INVISIBLE);
                six.setVisibility(View.INVISIBLE);


            }
            else if (value > 95 && value <= 101 && check_sim == 5)
            {
                zero.setVisibility(View.VISIBLE);
                one.setVisibility(View.VISIBLE);
                two.setVisibility(View.VISIBLE);
                three.setVisibility(View.VISIBLE);
                four.setVisibility(View.INVISIBLE);
                five.setVisibility(View.INVISIBLE);
                six.setVisibility(View.INVISIBLE);

            }
            else if (value > 101 && value <= 107 && check_sim == 5)
            {
                zero.setVisibility(View.INVISIBLE);
                one.setVisibility(View.INVISIBLE);
                two.setVisibility(View.VISIBLE);
                three.setVisibility(View.INVISIBLE);
                four.setVisibility(View.INVISIBLE);
                five.setVisibility(View.INVISIBLE);
                six.setVisibility(View.INVISIBLE);
            } else if (value > 107 && value <= 125 && check_sim == 5)
            {
                zero.setVisibility(View.VISIBLE);
                one.setVisibility(View.VISIBLE);
                two.setVisibility(View.INVISIBLE);
                three.setVisibility(View.INVISIBLE);
                four.setVisibility(View.INVISIBLE);
                five.setVisibility(View.INVISIBLE);
                six.setVisibility(View.INVISIBLE);            }
            else if (value > 125 && value <= 150 && check_sim == 5)
            {
                zero.setVisibility(View.VISIBLE);
                one.setVisibility(View.INVISIBLE);
                two.setVisibility(View.INVISIBLE);
                three.setVisibility(View.INVISIBLE);
                four.setVisibility(View.INVISIBLE);
                five.setVisibility(View.INVISIBLE);
                six.setVisibility(View.INVISIBLE);
            } else
            {
                zero.setVisibility(View.INVISIBLE);
                one.setVisibility(View.INVISIBLE);
                two.setVisibility(View.INVISIBLE);
                three.setVisibility(View.INVISIBLE);
                four.setVisibility(View.INVISIBLE);
                five.setVisibility(View.INVISIBLE);
                six.setVisibility(View.INVISIBLE);
            }
        }

        public int getCinr()
        {
            return (cinr);
        }
    }

    @Override
    public void onClickPositiveButton(DialogInterface pDialog, int pDialogIntefier) {
        if (pDialogIntefier == 0) {
            Intent i = new Intent(this, AppUpdateActivity.class);
            String C_ID = cid.toString();

//Create the bundle
            Bundle bundle = new Bundle();

//Add your data to bundle
            bundle.putString("Cid", C_ID);

//Add the bundle to the intent
            i.putExtras(bundle);

//Fire that second activity
            startActivity(i);
        }
    }

    @Override
    public void onClickNegativeButton(DialogInterface pDialog, int pDialogIntefier) {
        if (pDialogIntefier == 0) {
            pDialog.dismiss();
                  }
    }

    public String getTextFileData(String fileName) {

        try {
            FileInputStream fis = openFileInput("hello.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (FileNotFoundException e) {
            return "File Not Found";
        } catch (UnsupportedEncodingException e) {
            return "UnsupportedEncodingException";
        } catch (IOException e) {
            return "IOException";
        }
    }
    private void login(final String cid) {

        class LoginAsync extends AsyncTask<String, Void, String> {

            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(Authentication.this, "Please wait checking for update", "Loading...");
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
                        loadingDialog.dismiss();
                        Toast.makeText(Authentication.this,"Something Went Wrong", Toast.LENGTH_LONG).show();

                    }else {
                        if (flag != 0) {
                            loadingDialog.dismiss();
                            SaveUserDataProfile(jsonObj);
                        } else {
                            loadingDialog.dismiss();
                            Toast.makeText(Authentication.this,"Something Went Wrong", Toast.LENGTH_LONG).show();
                        }
                    }

                }
                else
                {
                    loadingDialog.dismiss();
                    Toast.makeText(Authentication.this,"Something Went Wrong", Toast.LENGTH_LONG).show();
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
////                Intent i = new Intent(WelcomeActivity.this, AppUpdateActivity.class);
////                startActivity(i);
//
//
//
//                Intent i = new Intent(this, AppUpdateActivity.class);
//                String C_ID=cid.toString();
//
////Create the bundle
//                Bundle bundle = new Bundle();
//
////Add your data to bundle
//                bundle.putString("Cid", C_ID);
//
////Add the bundle to the intent
//                i.putExtras(bundle);
//
////Fire that second activity
//                startActivity(i);

                CustomAlertDialog.getInstance().showConfirmDialog("Confirmation", "New Update Available, Do you want to update", "Yes", "No", this, identifier);

            }
            else
            {
                 Toast.makeText(Authentication.this, "Update Not Available", Toast.LENGTH_SHORT).show();

            }

        }
        catch (JSONException e)
        {
            e.printStackTrace();
            Toast.makeText(Authentication.this,"Something Went Wrong", Toast.LENGTH_LONG).show();
        }
    }

    private void GetVersionDetails()
    {
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            versionname = pInfo.versionName;
            versionNamet.setText(versionname.toString());
            versionnumber= String.valueOf(pInfo.versionCode);
            versionNumbert.setText(versionnumber.toString());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


    }

    private void BluetoothDiscoverable()
    {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 100);
        startActivity(discoverableIntent);
        Log.i("Log", "Discoverable ");
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_list, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onFragmentInteraction(String id) {

    }
}
