/*package com.virajmohite.a3phasecharger;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.database.FirebaseDatabase;
import com.virajmohite.a3phasecharger.PowerPlugged.Power;
import com.virajmohite.a3phasecharger.Storage.SPTimeReadingCP1;
import com.virajmohite.a3phasecharger.Storage.SPTimeReadingCP2;
import com.virajmohite.a3phasecharger.Storage.SPTimeReadingCP3;
import com.virajmohite.a3phasecharger.Storage.SPisPluggedin;
import com.virajmohite.a3phasecharger.Storage.SPisPoweFail;
import com.virajmohite.a3phasecharger.Storage.SPlanguageCP1;
import com.virajmohite.a3phasecharger.Storage.SPlanguageCP2;
import com.virajmohite.a3phasecharger.Storage.SPlanguageCP3;
import com.virajmohite.a3phasecharger.Storage.SPmeterReadingCP1;
import com.virajmohite.a3phasecharger.Storage.SPmeterReadingCP2;
import com.virajmohite.a3phasecharger.Storage.SPmeterReadingCP3;
import com.virajmohite.a3phasecharger.Storage.SharedPreferenceUnitR;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.UUID;


public class MaindisplayActivityMannual extends AppCompatActivity implements View.OnClickListener {

    CardView toggleBtn,btn_p2onff,btn_p3onff;

    TextView toggleValue,txt_p2btnValue,txt_p3btnValue;

    boolean flag=false,p2flag = false,p3flag= false;

    //---------- For Round Robin -------------//

    private int countC1,countC2,countC3;
    private boolean isAvailableC1,isAvailableC2,isAvailableC3;
    private boolean isStartC1,isStartC2,isStartC3;
    private boolean isStopC1,isStopC2,isStopC3;
    private Toast toast;

    //---------- For Round Robin -------------//

    //String red="#D74C34";
    String red="#f21200";
    String green="#3DAA4C";


    static Animation anim,anim2,anim3;


    private FirebaseDatabase mFirebaseInstance;
    Handler bluetoothIn;

    final int handlerState = 0;        				 //used to identify handler message
    int intervalcount = 0;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder recDataString = new StringBuilder();

    private ConnectedThread mConnectedThread;


    // SPP UUID service - this should work for most devices
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // String for MAC address
    private static String address;

    String command= "$000";

    //-------------design Variables
    int cnt=0;
    LinearLayout connect1,connect2,connect3,parent1;
    TextView txt_c1round_off_mr, txt_c2round_off_mr, txt_c3round_off_mr; //unit consumption variables
    TextView txt_c1diff,txt_c2diff,txt_c3diff; //time difference
    TextView tv_status1,tv_status2,tv_status3; // status

    //------------------ new architecture 09-01-2018
    private SPisPluggedin sPisPluggedin;
    private SPisPoweFail sPisPoweFail;
    private SPmeterReadingCP1 sPmeterReadingCP1;
    private SPmeterReadingCP2 sPmeterReadingCP2;
    private SPmeterReadingCP3 sPmeterReadingCP3;

    //------------------
    private SPTimeReadingCP1 sPTimeReadingCP1;
    private SPTimeReadingCP2 sPTimeReadingCP2;
    private SPTimeReadingCP3 sPTimeReadingCP3;


    //-----------------------CP1
    private int plugedinCountcp1 = 0;
    private int plugedoutCountcp1 = 0;
    private boolean isResumedAftercp1 = false;
    private boolean isStillOnCP1 = false;


    //-----------------------CP2
    private int plugedinCountcp2 = 0;
    private int plugedoutCountcp2 = 0;
    private boolean isResumedAftercp2 = false;
    private boolean isStillOnCP2 = false;

    //-----------------------CP3
    private int plugedinCountcp3 = 0;
    private int plugedoutCountcp3 = 0;
    private boolean isResumedAftercp3 = false;
    private boolean isStillOnCP3 = false;
    //time______________________CP1
    long MillisCP1 , DifferenceCP1 , NewBeginMillsCP1 , StartTimeCP1 = 0L ;
    int HoursCP1, MinutesCP1, SecondsCP1 ;
    //time______________________CP2
    long MillisCP2 , DifferenceCP2 , NewBeginMillsCP2 , StartTimeCP2 = 0L ;
    int HoursCP2, MinutesCP2, SecondsCP2 ;
    //time______________________CP3
    long MillisCP3 , DifferenceCP3 , NewBeginMillsCP3 , StartTimeCP3 = 0L ;
    int HoursCP3, MinutesCP3, SecondsCP3 ;

    //----------- optimize______20-01-2018
    LinearLayout layout_detail;
    FrameLayout layout_main;
    ImageView imageView_close;
    TextView txt_cpname,txt_rate,txt_voltage_detail,txt_current_detail,txt_power_detail,txt_unit_detail,txt_etime_detail,txt_status_detail;
    String voltage_detailCP1="000",current_detailCP1="00.00",power_detailCP1="000",unit_detailCP1="000", etime_detailCP1="00:00",status_detailCP1="IDLE";
    String voltage_detailCP2="000",current_detailCP2="00.00",power_detailCP2="000",unit_detailCP2="000", etime_detailCP2="00:00",status_detailCP2="IDLE";
    String voltage_detailCP3="000",current_detailCP3="00.00",power_detailCP3="000",unit_detailCP3="000", etime_detailCP3="00:00",status_detailCP3="IDLE";

    //----------
    String detail_flag = "";
    //--- for Rs
    String rsCP1 = "₹ 00.00", rsCP2 = "₹ 00.00", rsCP3 = "₹ 00.00",rsPOCP1 = "₹ 00.00", rsPOCP2 = "₹ 00.00", rsPOCP3 = "₹ 00.00";
    //---for overlay________
    RelativeLayout relativeLayout1st,relativeLayout2nd,relativeLayout3rd;
    int overlayCounti = 0,overlayCountii = 0,overlayCountiii = 0;


    //language Selection
    private String s1_CP1,s2_CP1,s3_CP1,s4_CP1,s5_CP1,s6_CP1,s7_CP1,s8_CP1,s9_CP1,s10_CP1,s11_CP1,s12_CP1,s13_CP1,s14_CP1,s15_CP1,s16_CP1,s17_CP1,s18_CP1,s19_CP1,s20_CP1,s21_CP1,s22_CP1,s23_CP1,s24_CP1;
    private String s1_CP2,s2_CP2,s3_CP2,s4_CP2,s5_CP2,s6_CP2,s7_CP2,s8_CP2,s9_CP2,s10_CP2,s11_CP2,s12_CP2,s13_CP2,s14_CP2,s15_CP2,s16_CP2,s17_CP2,s18_CP2,s19_CP2,s20_CP2,s21_CP2,s22_CP2,s23_CP2,s24_CP2;
    private String s1_CP3,s2_CP3,s3_CP3,s4_CP3,s5_CP3,s6_CP3,s7_CP3,s8_CP3,s9_CP3,s10_CP3,s11_CP3,s12_CP3,s13_CP3,s14_CP3,s15_CP3,s16_CP3,s17_CP3,s18_CP3,s19_CP3,s20_CP3,s21_CP3,s22_CP3,s23_CP3,s24_CP3;
    private TextView txt_touch1,txt_touch2,txt_touch3,txt_etime_display1,txt_etime_display2,txt_etime_display3,txt_etime_displayf,txt_unit_display,txt_rate_display;

    private SPlanguageCP1 sPlanguageCP1;
    private SPlanguageCP2 sPlanguageCP2;
    private SPlanguageCP3 sPlanguageCP3;
    private SharedPreferenceUnitR sharedPreference;

    private RadioGroup rg_language_m;

    public boolean isAlreadyonm = true;
    Button c1,c2,c3,c4;
    private File myExternalFile;
    String filename = "Config.txt";
    String filepath = "MyFileStorage";
    String cid;
    String hname="sour";
    Float erate;
    String erate_s;
    int clickCount = 0;
    //------------
    private int countidleCP1 = 0,countidleCP2 = 0,countidleCP3 = 0;
    private RelativeLayout layout_goback ;


    private int displayPlugoutCountC1 = 0;
    private int displayPlugoutCountC2 = 0;
    private int displayPlugoutCountC3 = 0;
    boolean isTappedC1 = false, isTappedC2 = false, isTappedC3 = false;
    boolean isPleaseWaitC1 = false, isPleaseWaitC2 = false, isPleaseWaitC3 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        setContentView(R.layout.newdesignm);
        Log.e("CHECK", "CHECKING");

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




    @Override
    protected void onRestart() {
        super.onRestart();
        // Toast.makeText(MaindisplayActivity.this,"onRestart Method",Toast.LENGTH_LONG).show();
        Log.e("METHOD","onRestart Method");
    }

    @Override
    protected void onStart() {
        super.onStart();
        //  Toast.makeText(MaindisplayActivity.this,"onStart Method",Toast.LENGTH_LONG).show();
        Log.e("METHOD","onStart Method");
    }

    @Override
    public void onResume() {
        super.onResume();

        countC1 = 0 ;
        countC2 = 0 ;
        countC3 = 0 ;

        isAvailableC1 = true;
        isAvailableC2 = true;
        isAvailableC3 = true;

        isStopC1 = true;
        isStopC2 = true;
        isStopC3 = true;

        isStartC3 = true;
        isStartC2 = true;
        isStartC1 = true;


        c1 = findViewById(R.id.checkBox);
        c2 = findViewById(R.id.checkBox2);
        c3 = findViewById(R.id.checkBox3);
        c4 = findViewById(R.id.checkBox4);
        layout_goback =  findViewById(R.id.layout_goback);

        GetCid();
        Firebase.setAndroidContext(this);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickCount==3) {
                    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (mBluetoothAdapter.isEnabled()) {
                        mBluetoothAdapter.disable();
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(MaindisplayActivityMannual.this, Authentication.class);
                            String C_ID = cid.toString();

//Create the bundle
                            Bundle bundle = new Bundle();

//Add your data to bundle
                            bundle.putString("Cid", C_ID);

//Add the bundle to the intent
                            i.putExtras(bundle);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

//Fire that second activity
                            startActivity(i);
                            clickCount=0;

                        }
                    }, 3000);
                }
                else {
                    clickCount++;
                }
            }
        });
        sPlanguageCP1 = new SPlanguageCP1(MaindisplayActivityMannual.this);
        sPlanguageCP2 = new SPlanguageCP2(MaindisplayActivityMannual.this);
        sPlanguageCP3 = new SPlanguageCP3(MaindisplayActivityMannual.this);
        sharedPreference = new SharedPreferenceUnitR();
        if (sharedPreference.getValue(MaindisplayActivityMannual.this)== null){
            sharedPreference.save(MaindisplayActivityMannual.this,"7");
        }
        erate_s = sharedPreference.getValue(MaindisplayActivityMannual.this);
        erate = Float.parseFloat(erate_s);
        langSet();
        //  Toast.makeText(MaindisplayActivity.this,"onResume Method",Toast.LENGTH_LONG).show();
        Log.e("METHOD","onResume Method");
        sPisPluggedin = new SPisPluggedin(MaindisplayActivityMannual.this); // initiating sharedepreferences
        sPisPoweFail = new SPisPoweFail(MaindisplayActivityMannual.this); // initiating sharedepreferences
        sPmeterReadingCP1 = new SPmeterReadingCP1(MaindisplayActivityMannual.this); // initiating sharedepreferences
        sPmeterReadingCP2 = new SPmeterReadingCP2(MaindisplayActivityMannual.this); // initiating sharedepreferences
        sPmeterReadingCP3 = new SPmeterReadingCP3(MaindisplayActivityMannual.this); // initiating sharedepreferences
        sPTimeReadingCP1 = new SPTimeReadingCP1(MaindisplayActivityMannual.this); // initiating sharedepreferences
        sPTimeReadingCP2 = new SPTimeReadingCP2(MaindisplayActivityMannual.this); // initiating sharedepreferences
        sPTimeReadingCP3 = new SPTimeReadingCP3(MaindisplayActivityMannual.this); // initiating sharedepreferences
        // Toast.makeText(MaindisplayActivity.this,"Before : "+sPisPluggedin.getisPluggedinCP1(),Toast.LENGTH_LONG).show();
        // Toast.makeText(MaindisplayActivity.this,"Before : "+sPisPluggedin.getisPluggedinCP2(),Toast.LENGTH_LONG).show();
        // Toast.makeText(MaindisplayActivity.this,"Before : "+sPisPluggedin.getisPluggedinCP2(),Toast.LENGTH_LONG).show();


        if(sPisPluggedin.getisPluggedinCP1().isEmpty()){
            sPisPluggedin.setisPluggedinCP1("f");
            // Toast.makeText(MaindisplayActivity.this,"sPisPluggedin.getisPluggedinCP1() "+sPisPluggedin.getisPluggedinCP1(),Toast.LENGTH_LONG).show();
        }
        // Toast.makeText(MaindisplayActivity.this,"Before : "+sPisPoweFail.getisPowerFailCP1(),Toast.LENGTH_LONG).show();

        if(sPisPoweFail.getisPowerFailCP1().isEmpty()){
            sPisPoweFail.setisPowerFailCP1("f");
            // Toast.makeText(MaindisplayActivity.this,"sPisPoweFail.getisPowerFailCP1() "+sPisPoweFail.getisPowerFailCP1(),Toast.LENGTH_LONG).show();

        }
        //----------------------------
        if(sPisPluggedin.getisPluggedinCP2().isEmpty()){
            sPisPluggedin.setisPluggedinCP2("f");
            // Toast.makeText(MaindisplayActivity.this,"getisPluggedinCP2() "+sPisPluggedin.getisPluggedinCP2(),Toast.LENGTH_LONG).show();
        }
        // Toast.makeText(MaindisplayActivity.this,"Before 2: "+sPisPoweFail.getisPowerFailCP2(),Toast.LENGTH_LONG).show();

        if(sPisPoweFail.getisPowerFailCP2().isEmpty()){
            sPisPoweFail.setisPowerFailCP2("f");
            // Toast.makeText(MaindisplayActivity.this,"sPisPoweFail.getisPowerFailCP1() "+sPisPoweFail.getisPowerFailCP2(),Toast.LENGTH_LONG).show();

        }

        //----------------------------
        if(sPisPluggedin.getisPluggedinCP3().isEmpty()){
            sPisPluggedin.setisPluggedinCP3("f");
            //Toast.makeText(MaindisplayActivity.this,"getisPluggedinCP3() "+sPisPluggedin.getisPluggedinCP3(),Toast.LENGTH_LONG).show();
        }
        // Toast.makeText(MaindisplayActivity.this,"Before 3: "+sPisPoweFail.getisPowerFailCP3(),Toast.LENGTH_LONG).show();

        if(sPisPoweFail.getisPowerFailCP3().isEmpty()){
            sPisPoweFail.setisPowerFailCP3("f");
            // Toast.makeText(MaindisplayActivity.this,"isPowerFailCP3() "+sPisPoweFail.getisPowerFailCP3(),Toast.LENGTH_LONG).show();

        }
        if (sPmeterReadingCP1.getMeterReadingCP1().isEmpty()){
            sPmeterReadingCP1.setMeterReadingCP1("00.00");
        }
        if (sPmeterReadingCP2.getMeterReadingCP2().isEmpty()){
            sPmeterReadingCP2.setMeterReadingCP2("00.00");
        }
        if (sPmeterReadingCP3.getMeterReadingCP3().isEmpty()){
            sPmeterReadingCP3.setMeterReadingCP3("00.00");
        }


        toggleBtn   =     findViewById(R.id.toggleBtn);
        btn_p2onff   =    findViewById(R.id.btn_p2onff);
        btn_p3onff   =    findViewById(R.id.btn_p3onff);

        toggleValue =     findViewById(R.id.toggleValue);
        txt_p2btnValue =  findViewById(R.id.txt_p2btnValue);
        txt_p3btnValue =  findViewById(R.id.txt_p3btnValue);



        //----------------------------------------------------------
        connect1=findViewById(R.id.ll_connect1);
        connect2=findViewById(R.id.ll_connect2);
        connect3=findViewById(R.id.ll_connect3);

        parent1=findViewById(R.id.ll_parent1);


        parent1.animate().translationY(0);

        //-------------------------------------------------------------
        /
******* variables
        txt_c1round_off_mr = findViewById(R.id.tv_unit1);
        txt_c2round_off_mr = findViewById(R.id.tv_unit2);
        txt_c3round_off_mr = findViewById(R.id.tv_unit3);
        //--------------------------
        txt_c1diff = findViewById(R.id.tv_time1);
        txt_c2diff = findViewById(R.id.tv_time2);
        txt_c3diff = findViewById(R.id.tv_time3);
        //----------------------
        tv_status1 = findViewById(R.id.tv_status1);
        tv_status2 = findViewById(R.id.tv_status2);
        tv_status3 = findViewById(R.id.tv_status3);

        //for optimise
        layout_detail = findViewById(R.id.layout_detail);
        layout_main = findViewById(R.id.layout_main);
        imageView_close = findViewById(R.id.imageView_close);
        txt_cpname = findViewById(R.id.txt_cpname);
        txt_rate = findViewById(R.id.txt_rate);
        txt_voltage_detail = findViewById(R.id.txt_voltage_detail);
        txt_current_detail = findViewById(R.id.txt_current_detail);
        txt_power_detail = findViewById(R.id.txt_power_detail);
        txt_unit_detail = findViewById(R.id.txt_unit_detail);
        txt_etime_detail = findViewById(R.id.txt_etime_detail);
        txt_status_detail = findViewById(R.id.txt_status_detail);
        layout_main.setVisibility(View.VISIBLE);
        layout_detail.setVisibility(View.GONE);
        //touch...
        txt_touch1 = findViewById(R.id.txt_touch1);
        txt_touch2 = findViewById(R.id.txt_touch2);
        txt_touch3 = findViewById(R.id.txt_touch3);
        txt_etime_display1 = findViewById(R.id.txt_etime_display1);
        txt_etime_display2 = findViewById(R.id.txt_etime_display2);
        txt_etime_display3 = findViewById(R.id.txt_etime_display3);
        txt_etime_displayf = findViewById(R.id.txt_etime_displayf);
        txt_unit_display = findViewById(R.id.txt_unit_display);
        txt_rate_display = findViewById(R.id.txt_rate_display);

        //----------for Overlay28-1-18
        relativeLayout1st =  findViewById(R.id.connectionLayout1st);
        relativeLayout2nd =  findViewById(R.id.connectionLayout2nd);
        relativeLayout3rd =  findViewById(R.id.connectionLayout3rd);

        //-------------lang 30-1-18
        rg_language_m = findViewById(R.id.rg_language_m);

        rg_language_m.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch(checkedId)
                {
                    case R.id.rb_en_m:
                        // TODO Something
                        if (detail_flag.equals("d_CP1")) {
                            sPlanguageCP1.setlanguageCP1("en");
                        } else if (detail_flag.equals("d_CP2")) {
                            sPlanguageCP2.setlanguageCP2("en");

                        } else if (detail_flag.equals("d_CP3")) {
                            sPlanguageCP3.setlanguageCP3("en");

                        }

                        break;
                    case R.id.rb_hi_m:
                        // TODO Something
                        if (detail_flag.equals("d_CP1")) {
                            sPlanguageCP1.setlanguageCP1("hi");
                        } else if (detail_flag.equals("d_CP2")) {
                            sPlanguageCP2.setlanguageCP2("hi");

                        } else if (detail_flag.equals("d_CP3")) {
                            sPlanguageCP3.setlanguageCP3("hi");

                        }
                        break;
                    case R.id.rb_ma_m:
                        // TODO Something
                        if (detail_flag.equals("d_CP1")) {
                            sPlanguageCP1.setlanguageCP1("ma");
                        } else if (detail_flag.equals("d_CP2")) {
                            sPlanguageCP2.setlanguageCP2("ma");
                        } else if (detail_flag.equals("d_CP3")) {
                            sPlanguageCP3.setlanguageCP3("ma");
                        }
                        break;
                }
            }
        });

        relativeLayout1st.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                isTappedC1 = true;
                isPleaseWaitC1 = true;
                if (!isAvailableC2 && !isAvailableC3 )
                {
                    funConnectori();
                    overlayCounti = 0;
                    relativeLayout1st.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"CP2 And Cp3 Not Available",Toast.LENGTH_SHORT).show();
                }
                else if (isAvailableC2 && countC2!=0){
                    // Toast.makeText(getApplicationContext(),"CP2  Available",Toast.LENGTH_SHORT).show();
                    customToast(" Now CP2 Available...!!! ");
                    countC2 = 0;
                }
                else if (isAvailableC3 && countC3 != 0){
                    // Toast.makeText(getApplicationContext()," Cp3 Available",Toast.LENGTH_SHORT).show();
                    customToast(" Now CP3 Available...!!! ");
                    countC3 = 0;
                }
                if (countC1 == countC2 && countC2 == countC3 )
                {
                    countC1 = 0;
                    countC2 = 0;
                    countC3 = 0;
                }

                if (countC1 == 0 && countC2 == 0 && countC3 == 0)
                {
                    funConnectori();
                    overlayCounti = 0;
                    relativeLayout1st.setVisibility(View.GONE);

                }
                else if (countC1 > countC2 && countC1 > countC3) {

                    if (isAvailableC2 && isAvailableC3){
                        customToast("Please Use C2 or C3...!!!");
                    }
                    else if (isAvailableC2) {
                        customToast("Please Use C2...!!!");
                    } else if (isAvailableC3) {
                        customToast("Please Use C3...!!!");
                    } else {
                        Toast.makeText(getApplicationContext(), "Not Available C2 Or C3...!!!", Toast.LENGTH_SHORT).show();
                        countC1 = 0;
                        funConnectori();
                        overlayCounti = 0;
                        relativeLayout1st.setVisibility(View.GONE);
                    }

                }
                else if (countC1 < countC2 && countC1 < countC3)
                {
                    funConnectori();
                    overlayCounti = 0;
                    relativeLayout1st.setVisibility(View.GONE);
                }
                else if (countC2 > countC1  && countC2 > countC3)
                {
                    funConnectori();
                    overlayCounti = 0;
                    relativeLayout1st.setVisibility(View.GONE);
                }
                else if (countC3 > countC1 && countC3 > countC2)
                {
                    funConnectori();
                    overlayCounti = 0;
                    relativeLayout1st.setVisibility(View.GONE);
                }
                else if (countC2 < countC1 && countC2 < countC3)
                {
                    if (isAvailableC2)
                    {
                        customToast("Please Use C2...!!!");
                    }
                }
                else if (countC3 < countC1 && countC3 < countC2)
                {
                    if (isAvailableC3)
                    {
                        customToast("Please Use C3...!!!");
                    }
                }

            }

        });


        relativeLayout2nd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isTappedC2 = true;
                isPleaseWaitC2 = true;

                if (!isAvailableC3 && !isAvailableC1)
                {
                    funConnectorii();
                    overlayCountii = 0;
                    relativeLayout2nd.setVisibility(View.GONE);
                }
                else if (isAvailableC1 && countC2!=0){
                    // Toast.makeText(getApplicationContext(),"CP1  Available",Toast.LENGTH_SHORT).show();
                    customToast(" Now CP1 Available...!!! ");
                    countC2 = 0;
                }
                else if (isAvailableC3 && countC3!=0){
                    //Toast.makeText(getApplicationContext()," Cp3 Available",Toast.LENGTH_SHORT).show();
                    customToast(" Now CP3 Available...!!! ");
                    countC3 = 0;
                }

                if (countC1 == countC2 && countC2 == countC3 )
                {
                    countC1 = 0;
                    countC2 = 0;
                    countC3 = 0;

                }

                if (countC1 == 0 && countC2 == 0 && countC3 == 0)
                {
                    funConnectorii();
                    overlayCountii = 0;
                    relativeLayout2nd.setVisibility(View.GONE);


                }
                else if (countC2 > countC1 && countC2 > countC3)
                {
                    if (isAvailableC1 && isAvailableC3){
                        customToast("Please Use C1 or C3...!!!");
                    }
                    else if (isAvailableC1)
                    {
                        customToast("Please Use C1...!!!");
                    }
                    else if (isAvailableC3)
                    {
                        customToast("Please Use C3...!!!");
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Not Available C1 Or C3...!!!",Toast.LENGTH_SHORT).show();
                        countC2 = 0;
                        funConnectorii();
                        overlayCountii = 0;
                        relativeLayout2nd.setVisibility(View.GONE);
                    }
                }
                else if (countC2 < countC1 && countC2 < countC3)
                {
                    funConnectorii();
                    overlayCountii = 0;
                    relativeLayout2nd.setVisibility(View.GONE);
                }
                else if (countC1 > countC2  && countC1 > countC3)
                {
                    funConnectorii();
                    overlayCountii = 0;
                    relativeLayout2nd.setVisibility(View.GONE);
                }
                else if (countC3 > countC1 && countC3 > countC2)
                {
                    funConnectorii();
                    overlayCountii = 0;
                    relativeLayout2nd.setVisibility(View.GONE);
                }
                else if (countC1 < countC2 && countC1 < countC3)
                {
                    if (isAvailableC1)
                    {
                        customToast("Please Use C1...!!!");
                    }
                }
                else if (countC3 < countC1 && countC3 < countC2)
                {
                    if (isAvailableC3)
                    {
                        customToast("Please Use C3...!!!");
                    }
                }


            }
        });


        relativeLayout3rd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isTappedC3 = true;
                isPleaseWaitC3 = true;

                if (!isAvailableC1 && !isAvailableC2)
                {
                    funConnectoriii();
                    overlayCountiii = 0;
                    relativeLayout3rd.setVisibility(View.GONE);
                }
                else if (isAvailableC1 && countC1!= 0){
                    // Toast.makeText(getApplicationContext(),"CP1  Available",Toast.LENGTH_SHORT).show();
                    customToast(" Now CP1 Available ...!!! ");
                    countC1 = 0;
                }
                else if (isAvailableC2 && countC2!=0){
                    //    Toast.makeText(getApplicationContext()," Cp2 Available",Toast.LENGTH_SHORT).show();
                    customToast(" Now CP2 Available...!!! ");
                    countC2 = 0;
                }
                if (countC1 == countC2 && countC2 == countC3) {
                    countC1 = 0;
                    countC2 = 0;
                    countC3 = 0;
                }

                if (countC1 == 0 && countC2 == 0 && countC3 == 0) {
                    funConnectoriii();
                    overlayCountiii = 0;
                    relativeLayout3rd.setVisibility(View.GONE);

                } else if (countC3 > countC1 && countC3 > countC2) {
                    if (isAvailableC2 && isAvailableC3){
                        customToast("Please Use C1 or C2...!!!");
                    }
                    else if (isAvailableC1) {
                        customToast("Please Use C1...!!!");
                    } else if (isAvailableC2) {
                        customToast("Please Use C2...!!!");
                    } else {
                        Toast.makeText(getApplicationContext(), " Not Available C1 Or C2...!!!", Toast.LENGTH_SHORT).show();
                        countC3 = 0;
                        funConnectoriii();
                        overlayCountiii = 0;
                        relativeLayout3rd.setVisibility(View.GONE);
                    }
                } else if (countC3 < countC1 && countC3 < countC2) {
                    funConnectoriii();
                    overlayCountiii = 0;
                    relativeLayout3rd.setVisibility(View.GONE);
                } else if (countC1 > countC2 && countC1 > countC3) {
                    funConnectoriii();
                    overlayCountiii = 0;
                    relativeLayout3rd.setVisibility(View.GONE);
                }
                else if (countC2 > countC1 && countC2 > countC3)
                {
                    funConnectoriii();
                    overlayCountiii = 0;
                    relativeLayout3rd.setVisibility(View.GONE);
                }
                else if (countC1 < countC2 && countC1 < countC3)
                {
                    if (isAvailableC1)
                    {
                        customToast("Please Use C1 ...!!!");
                    }

                }
                else if (countC2 < countC1 && countC2 < countC3)
                {
                    if (isAvailableC2)
                    {
                        customToast("Please Use C2...!!!");
                    }
                }




            }
        });

        imageView_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_main.setVisibility(View.VISIBLE);
                layout_detail.setVisibility(View.GONE);
                detail_flag = "c";

            }
        });

        connect1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_main.setVisibility(View.GONE);
                layout_detail.setVisibility(View.VISIBLE);
                detail_flag = "d_CP1";
            }
        });
        connect2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_main.setVisibility(View.GONE);
                layout_detail.setVisibility(View.VISIBLE);
                detail_flag = "d_CP2";
            }
        });
        connect3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_main.setVisibility(View.GONE);
                layout_detail.setVisibility(View.VISIBLE);
                detail_flag = "d_CP3";
            }
        });
        layout_goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MaindisplayActivityMannual.this, LangSelection.class);
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

        final String s = "#234990.00+~";

        anim = new AlphaAnimation(0.1f, 1.0f);
        anim.setDuration(300);
        anim.setRepeatMode(Animation.RESTART);
        anim.setRepeatCount(Animation.INFINITE);
        anim2 = new AlphaAnimation(0.1f, 1.0f);
        anim2.setDuration(300);
        anim2.setRepeatMode(Animation.RESTART);
        anim2.setRepeatCount(Animation.INFINITE);
        anim3 = new AlphaAnimation(0.1f, 1.0f);
        anim3.setDuration(300);
        anim3.setRepeatMode(Animation.RESTART);
        anim3.setRepeatCount(Animation.INFINITE);


        //
        setToggleBtn();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mConnectedThread.write("c");
                //  Toast.makeText(MaindisplayActivity.this,"Thread",Toast.LENGTH_LONG).show();

                Log.e("mConnectedThreadSTRING","c : "+intervalcount);
                intervalcount++;
            }
        }, 5000);

        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {

                if (msg.what == handlerState) {                                        //if message is what we want
                    isAlreadyonm = true;
                    if (Power.isConnected(MaindisplayActivityMannual.this)) {
                        String readMessage = (String) msg.obj;                                                                // msg.arg1 = bytes from connect thread
                        recDataString.append(readMessage);                                    //keep appending to string until ~
                        int endOfLineIndex = recDataString.indexOf("~");                    // determine the end-of-line
                        //if (endOfLineIndex > 0 && endOfLineIndex < 80) {
                        if (endOfLineIndex > 0) {
                            langSet();
                            if (isTappedC1){
                                txt_touch1.setText("C1\n"+s24_CP1);
                            }else{
                                txt_touch1.setText("C1\n"+s21_CP1);

                            }

                            if (isTappedC2){
                                txt_touch2.setText("C2\n"+s24_CP2);
                            }else{
                                txt_touch2.setText("C2\n"+s21_CP2);

                            }

                            if (isTappedC3){
                                txt_touch3.setText("C3\n"+s24_CP3);
                            }else{
                                txt_touch3.setText("C3\n"+s21_CP3);

                            }
                            txt_etime_display1.setText(s14_CP1);
                            txt_etime_display2.setText(s14_CP2);
                            txt_etime_display3.setText(s14_CP3);
                            // make sure there data before ~
                            String dataInPrint = recDataString.substring(0, endOfLineIndex);    // extract string
                            // txtString.setText("Data Received = " + dataInPrint);
                            int dataLength = dataInPrint.length();                            //get length of data received
                            //txtStringLength.setText("String Length = " + String.valueOf(dataLength));
                            if (recDataString.charAt(0) == '#')                                //if it starts with # we know it is what we are looking for
                            {
                                try {
                                    if(countidleCP1 > 400 && countidleCP2 > 400 && countidleCP3 > 400){
                                        layout_detail.setVisibility(View.GONE);
                                        layout_main.setVisibility(View.GONE);
                                        layout_goback.setVisibility(View.VISIBLE);
                                    }
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
                                    voltage_detailCP1 = p1voltage;
                                    current_detailCP1 = p1current;
                                    power_detailCP1 = p1meter;
                                    voltage_detailCP2 = p2voltage;
                                    current_detailCP2 = p2current;
                                    power_detailCP2 = p2meter;
                                    voltage_detailCP3 = p3voltage;
                                    current_detailCP3 = p3current;
                                    power_detailCP3 = p3meter;
                                    mFirebaseInstance.getReference(cid+"-CP1M").setValue(tv_status1.getText().toString());
                                    mFirebaseInstance.getReference(cid+"-CP2M").setValue(tv_status2.getText().toString());
                                    mFirebaseInstance.getReference(cid+"-CP3M").setValue(tv_status3.getText().toString());
                                    mFirebaseInstance.getReference(cid+"-CP1-VoltageM").setValue(p1voltage);
                                    mFirebaseInstance.getReference(cid+"-CP2-VoltageM").setValue(p2voltage);
                                    mFirebaseInstance.getReference(cid+"-CP3-VoltageM").setValue(p3voltage);
                                    mFirebaseInstance.getReference(cid+"-CP1-CurrentM").setValue(p1current);
                                    mFirebaseInstance.getReference(cid+"-CP2-CurrentM").setValue(p2current);
                                    mFirebaseInstance.getReference(cid+"-CP3-CurrentM").setValue(p3current);
                                    mFirebaseInstance.getReference(cid+"-CP1-PowerM").setValue(p1meter);
                                    mFirebaseInstance.getReference(cid+"-CP2-PowerM").setValue(p2meter);
                                    mFirebaseInstance.getReference(cid+"-CP3-PowerM").setValue(p3meter);

                                    mFirebaseInstance.getReference(cid+"-CP1-AmountM").setValue(txt_c1round_off_mr.getText().toString());
                                    mFirebaseInstance.getReference(cid+"-CP2-AmountM").setValue(txt_c2round_off_mr.getText().toString());
                                    mFirebaseInstance.getReference(cid+"-CP3-AmountM").setValue(txt_c3round_off_mr.getText().toString());


                                    //   txt_c1round_off_mr

                                    if (p1OnOff.equals("0")) {
                                        if (sPisPoweFail.getisPowerFailCP1().equals("t") && sPisPluggedin.getisPluggedinCP1().equals("t")) {
                                            toggleBtn.performClick();
                                        } else {
                                            flag = false;
                                            setToggleBtn();
                                        }
                                    } else {
                                        flag = true;
                                        setToggleBtn();
                                    }

                                    if (p2OnOff.equals("0")) {

                                        if (sPisPoweFail.getisPowerFailCP2().equals("t") && sPisPluggedin.getisPluggedinCP2().equals("t")) {
                                            btn_p2onff.performClick();
                                        } else {
                                            p2flag = false;
                                            setP2Btn();
                                        }

                                    } else {
                                        p2flag = true;
                                        setP2Btn();
                                    }

                                    if (p3OnOff.equals("0")) {
                                        if (sPisPoweFail.getisPowerFailCP3().equals("t") && sPisPluggedin.getisPluggedinCP3().equals("t")) {
                                            btn_p3onff.performClick();
                                        } else {
                                            p3flag = false;
                                            setP3Btn();
                                        }
                                    } else {
                                        p3flag = true;
                                        setP3Btn();
                                    }
                                    if (emergency.equals("1"))
                                    {
                                        tv_status1.setText(s2_CP1);
                                        tv_status2.setText(s2_CP2);
                                        tv_status3.setText(s2_CP2);
                                        status_detailCP1 = s2_CP1;
                                        status_detailCP2 = s2_CP2;
                                        status_detailCP3 = s2_CP3;

                                    } else {

                                        //---------------------------------------------------------------------------------------------------------

                                        if (p1OnOff.equals("0") && p1plugin.equals("0") && p1fault.equals("03")) {
                                            tv_status1.setText(s3_CP1);
                                            status_detailCP1 = s3_CP1;
                                        }
                                        if (p2OnOff.equals("0") && p2plugin.equals("0") && p2fault.equals("03")) {
                                            tv_status2.setText(s3_CP2);
                                            status_detailCP2 = s3_CP2;
                                        }
                                        if (p3OnOff.equals("0") && p3plugin.equals("0") && p3fault.equals("03")) {
                                            tv_status3.setText(s3_CP3);
                                            status_detailCP3 = s3_CP3;
                                        }
                                        //fault--------------------------------------------------------------------
                                        if (p1fault.equals("05")) {

                                            if (sPisPluggedin.getisPluggedinCP1().equals("t")) {
                                                sPisPoweFail.setisPowerFailCP1("t");
                                                sPTimeReadingCP1.setTimeReadingCP1(MillisCP1);
                                                isStillOnCP1 = true;
                                            }
                                            Float cp1v = Float.parseFloat(p1voltage);
                                            if (cp1v > 263)
                                            {
                                                tv_status1.setText(s13_CP1);
                                                status_detailCP1 = s13_CP1;
                                            }
                                            else if (cp1v < 186 && cp1v > 150)
                                            {
                                                tv_status1.setText(s12_CP1);
                                                status_detailCP1 = s12_CP1;
                                            }
                                            else
                                            {
                                                tv_status1.setText(s6_CP1);
                                                status_detailCP1 = s6_CP1;
                                                toggleBtn.setCardBackgroundColor(Color.parseColor(red));
                                                toggleBtn.startAnimation(anim);
                                            }
                                            //}

                                        }
                                        if (p2fault.equals("05")) {
                                            if (sPisPluggedin.getisPluggedinCP2().equals("t")) {
                                                sPisPoweFail.setisPowerFailCP2("t");
                                                sPTimeReadingCP2.setTimeReadingCP2(MillisCP2);
                                                isStillOnCP2 = true;
                                            }
                                            Float cp2v = Float.parseFloat(p2voltage);
                                            if (cp2v > 263) {
                                                tv_status2.setText(s13_CP2);
                                                status_detailCP2 = s13_CP2;

                                            } else if (cp2v < 186 && cp2v > 150) {
                                                tv_status2.setText(s12_CP2);
                                                status_detailCP2 = s12_CP2;


                                            } else {
                                                tv_status2.setText(s6_CP2);
                                                status_detailCP2 = s6_CP2;
                                                btn_p2onff.setCardBackgroundColor(Color.parseColor(red));
                                                btn_p2onff.startAnimation(anim);

                                            }


                                        }
                                        if (p3fault.equals("05")) {

                                            if (sPisPluggedin.getisPluggedinCP3().equals("t")) {
                                                sPisPoweFail.setisPowerFailCP3("t");
                                                sPTimeReadingCP3.setTimeReadingCP3(MillisCP3);
                                                isStillOnCP3 = true;
                                            }
                                            Float cp3v = Float.parseFloat(p3voltage);
                                            if (cp3v > 263) {
                                                tv_status3.setText(s13_CP3);
                                                status_detailCP3 = s13_CP3;

                                            } else if (cp3v < 186 && cp3v > 150) {
                                                tv_status3.setText(s12_CP3);
                                                status_detailCP3 = s12_CP3;

                                            } else {
                                                tv_status3.setText(s6_CP3);
                                                status_detailCP3 = s6_CP3;
                                                btn_p3onff.setCardBackgroundColor(Color.parseColor(red));
                                                btn_p3onff.startAnimation(anim);

                                            }

                                        }
                                        //--------------Earth Fault
                                        if (p1fault.equals("10")) {
                                            tv_status1.setText(s4_CP1);
                                            status_detailCP1 = s4_CP1;

                                        }
                                        if (p2fault.equals("10")) {
                                            tv_status2.setText(s4_CP2);
                                            status_detailCP2 = s4_CP2;

                                        }
                                        if (p3fault.equals("10")) {
                                            tv_status3.setText(s4_CP3);
                                            status_detailCP3 = s4_CP3;

                                        }
                                        //---------------TEMP HAZARD
                                        if (p1fault.equals("07")) {
                                            tv_status1.setText(s5_CP1);
                                            status_detailCP1 = s5_CP1;


                                        }
                                        if (p2fault.equals("07")) {
                                            tv_status2.setText(s5_CP2);
                                            status_detailCP2 = s5_CP2;

                                        }
                                        if (p3fault.equals("07")) {
                                            tv_status3.setText(s5_CP3);
                                            status_detailCP3 = s5_CP3;

                                        }


//-----------------------------------------------------------CP1 Status-----------------------------------------------------------------------------------------------------------------------------
                                        if (p1OnOff.equals("0") && p1plugin.equals("0") && p1fault.equals("99")) {

                                            if (sPisPluggedin.getisPluggedinCP1().equals("t")) {

                                                if (plugedoutCountcp1 >= 30) {
                                                    sPisPluggedin.setisPluggedinCP1("f");

                                                } else {
                                                    displayPlugoutCountC1 = 0;
                                                    tv_status1.setText(s7_CP1);
                                                    txt_c1round_off_mr.setText(rsPOCP1);
                                                    rsCP1 = "₹ 00.00";
                                                    txt_c1diff.setText(etime_detailCP1);

                                                    status_detailCP1 = s7_CP1;
                                                    unit_detailCP1 = unit_detailCP1;
                                                    etime_detailCP1 = etime_detailCP1;

                                                    plugedinCountcp1 = 0;
                                                    plugedoutCountcp1++;
                                                    if (isResumedAftercp1) {
                                                        sPisPoweFail.setisPowerFailCP1("f");
                                                    }
                                                }

                                            } else {
                                                countidleCP1++;
                                                if (isPleaseWaitC1){
                                                    tv_status1.setText("Please Wait...");
                                                    status_detailCP1 = "Please Wait...";
                                                    toggleBtn.setVisibility(View.INVISIBLE);

                                                }else{
                                                    tv_status1.setText(s8_CP1);
                                                    status_detailCP1 = s8_CP1;

                                                }


                                                if (isStopC1)
                                                {
                                                    //   countC1 = 0;
                                                    isAvailableC1 = true;

                                                    isStartC1 = true;
                                                    isStopC1 = false;
                                                }

                                                txt_c1round_off_mr.setText("₹ 00.00");
                                                rsCP1 = "₹ 00.00";
                                                rsPOCP1 = "₹ 00.00";
                                                txt_c1diff.setText("00:00:00");
                                                unit_detailCP1 = "00.00";
                                                etime_detailCP1 = "00:00:00";
                                                plugedinCountcp1 = 0;
                                                toggleBtn.clearAnimation();
                                                //overlay1
                                                if (overlayCounti> 10) {
                                                    isPleaseWaitC1 = false;
                                                    relativeLayout1st.setVisibility(View.VISIBLE);
                                                }
                                                overlayCounti++;
                                            }

                                        }
                                        if (p1OnOff.equals("1") && p1plugin.equals("0") && p1fault.equals("99")) {
                                            countidleCP1 = 0;
                                            plugedinCountcp1 = 0;
                                            plugedoutCountcp1 = 0;
                                            tv_status1.setText(s1_CP1);
                                            isTappedC1 = false;
                                            isPleaseWaitC1 = false;

                                            if (isStartC1)
                                            {
                                                isStartC1 = false;
                                                countC1++;
                                                isStopC1 = true;
                                                isAvailableC1 = false;
                                            }
                                            txt_c1round_off_mr.setText("₹ 00.00");
                                            rsCP1 = "₹ 00.00";
                                            txt_c1diff.setText("00:00:00");
                                            status_detailCP1 = s1_CP1;
                                            unit_detailCP1 = "00.00";
                                            etime_detailCP1 = "00:00:00";
                                            isStillOnCP1 = false;
                                            relativeLayout1st.setVisibility(View.GONE);
                                            toggleBtn.setVisibility(View.VISIBLE);

                                            if (displayPlugoutCountC1 == 12) {
                                                if (sPisPoweFail.getisPowerFailCP1().equals("t") && sPisPluggedin.getisPluggedinCP1().equals("t")) {
                                                    toggleBtn.performClick();
                                                    sPisPoweFail.setisPowerFailCP1("f");
                                                    sPisPluggedin.setisPluggedinCP1("f");
                                                    tv_status1.setText(s7_CP1);
                                                    status_detailCP1 = s7_CP1;
                                                    //-----------
                                                    sPisPoweFail.setisPowerFailCP1("f");
                                                    sPTimeReadingCP1.setTimeReadingCP1(MillisCP1);
                                                }
                                            }
                                            displayPlugoutCountC1++;
                                        }
                                        if (p1OnOff.equals("1") && p1plugin.equals("1") && p1fault.equals("99")) {
                                            countidleCP1 = 0;
                                            if (sPisPoweFail.getisPowerFailCP1().equals("t")) {
                                                //---------Status:resuming after power fail_______________
                                                if (plugedinCountcp1 >= 10) {
                                                    //time reading______
                                                    MillisCP1 = (SystemClock.uptimeMillis() - StartTimeCP1);

                                                    HoursCP1 = (int) (MillisCP1 / (1000 * 60 * 60));
                                                    MinutesCP1 = (int) (MillisCP1 / (1000 * 60)) % 60;
                                                    SecondsCP1 = (int) (MillisCP1 / 1000) % 60;
                                                    String time_readingCP1 = "" + String.format("%02d", HoursCP1) + ":"
                                                            + String.format("%02d", MinutesCP1) + ":"
                                                            + String.format("%02d", SecondsCP1);
                                                    //-------------------------meter reading
                                                    Float prev_mrCP1 = Float.parseFloat(sPmeterReadingCP1.getMeterReadingCP1());
                                                    Float current_mrCP1 = Float.parseFloat(p1meter);
                                                    float meterreadingdiff = current_mrCP1 - prev_mrCP1;
                                                    float meterreadingdiffP = (current_mrCP1 - prev_mrCP1) * erate;
                                                    String meterreadingCP1 = String.format("%.2f", meterreadingdiff);
                                                    String meterreadingCP1P = String.format("%.2f", meterreadingdiffP);

                                                    //------------------
                                                    tv_status1.setText(s10_CP1);
                                                    if (meterreadingdiffP > 0 && meterreadingdiffP < 100) {
                                                        txt_c1round_off_mr.setText("₹ " + meterreadingCP1P);
                                                        rsCP1 = "₹ " + meterreadingCP1P;
                                                        rsPOCP1 = "₹ " + meterreadingCP1P;
                                                    }
                                                    txt_c1diff.setText(time_readingCP1);
                                                    status_detailCP1 = s10_CP1;
                                                    unit_detailCP1 = meterreadingCP1;
                                                    etime_detailCP1 = time_readingCP1;
                                                    sPisPoweFail.setisPowerFailCP1("f");
                                                    relativeLayout1st.setVisibility(View.GONE);

                                                } else if (plugedinCountcp1 == 0) {
                                                    //Time reading_________________

                                                    StartTimeCP1 = SystemClock.uptimeMillis() + (-sPTimeReadingCP1.getTimeReadingCP1());
                                                    //-------------------------meter reading
                                                    Float prev_mrCP1 = Float.parseFloat(sPmeterReadingCP1.getMeterReadingCP1());
                                                    Float current_mrCP1 = Float.parseFloat(p1meter);
                                                    float meterreadingdiff = current_mrCP1 - prev_mrCP1;
                                                    float meterreadingdiffP = (current_mrCP1 - prev_mrCP1) * erate;
                                                    String meterreadingCP1 = String.format("%.2f", meterreadingdiff);
                                                    String meterreadingCP1P = String.format("%.2f", meterreadingdiffP);
                                                    //--------------------
                                                    tv_status1.setText(s9_CP1);
                                                    if (meterreadingdiffP > 0 && meterreadingdiffP < 100) {
                                                        txt_c1round_off_mr.setText("₹ " + meterreadingCP1P);
                                                        rsCP1 = "₹ " + meterreadingCP1P;
                                                        rsPOCP1 = "₹ " + meterreadingCP1P;
                                                    }
                                                    txt_c1diff.setText("00:00:00");
                                                    status_detailCP1 = s9_CP1;
                                                    unit_detailCP1 = meterreadingCP1;
                                                    etime_detailCP1 = "00:00:00";
                                                    plugedinCountcp1++;
                                                    sPisPluggedin.setisPluggedinCP1("t");
                                                    isResumedAftercp1 = true;
                                                    toggleBtn.startAnimation(anim);
                                                    relativeLayout1st.setVisibility(View.GONE);



                                                } else {
                                                    //time reading______
                                                    MillisCP1 = (SystemClock.uptimeMillis() - StartTimeCP1);

                                                    HoursCP1 = (int) (MillisCP1 / (1000 * 60 * 60));
                                                    MinutesCP1 = (int) (MillisCP1 / (1000 * 60)) % 60;
                                                    SecondsCP1 = (int) (MillisCP1 / 1000) % 60;
                                                    String time_readingCP1 = "" + String.format("%02d", HoursCP1) + ":"
                                                            + String.format("%02d", MinutesCP1) + ":"
                                                            + String.format("%02d", SecondsCP1);

                                                    //-------------------------meter reading
                                                    Float prev_mrCP1 = Float.parseFloat(sPmeterReadingCP1.getMeterReadingCP1());
                                                    Float current_mrCP1 = Float.parseFloat(p1meter);
                                                    float meterreadingdiff = current_mrCP1 - prev_mrCP1;
                                                    float meterreadingdiffP = (current_mrCP1 - prev_mrCP1) * erate;
                                                    String meterreadingCP1 = String.format("%.2f", meterreadingdiff);
                                                    String meterreadingCP1P = String.format("%.2f", meterreadingdiffP);
                                                    //--------------------

                                                    tv_status1.setText(s9_CP1);
                                                    if (meterreadingdiffP > 0 && meterreadingdiffP < 100) {
                                                        txt_c1round_off_mr.setText("₹ " + meterreadingCP1P);
                                                        rsCP1 = "₹ " + meterreadingCP1P;
                                                        rsPOCP1 = "₹ " + meterreadingCP1P;
                                                    }
                                                    txt_c1diff.setText(time_readingCP1);
                                                    status_detailCP1 = s9_CP1;
                                                    unit_detailCP1 = meterreadingCP1;
                                                    etime_detailCP1 = time_readingCP1;
                                                    plugedinCountcp1++;
                                                    sPisPluggedin.setisPluggedinCP1("t");
                                                    isResumedAftercp1 = true;
                                                    relativeLayout1st.setVisibility(View.GONE);


                                                }


                                            } else {
                                                //--------------- Status:vehicle pugged in_________
                                                if (plugedinCountcp1 >= 10) {
                                                    //time reading______
                                                    MillisCP1 = (SystemClock.uptimeMillis() - StartTimeCP1);

                                                    HoursCP1 = (int) (MillisCP1 / (1000 * 60 * 60));
                                                    MinutesCP1 = (int) (MillisCP1 / (1000 * 60)) % 60;
                                                    SecondsCP1 = (int) (MillisCP1 / 1000) % 60;
                                                    String time_readingCP1 = "" + String.format("%02d", HoursCP1) + ":"
                                                            + String.format("%02d", MinutesCP1) + ":"
                                                            + String.format("%02d", SecondsCP1);

                                                    //-------------------------meter reading
                                                    Float prev_mrCP1 = Float.parseFloat(sPmeterReadingCP1.getMeterReadingCP1());

                                                    Float current_mrCP1 = Float.parseFloat(p1meter);
                                                    float meterreadingdiff = current_mrCP1 - prev_mrCP1;
                                                    float meterreadingdiffP = (current_mrCP1 - prev_mrCP1) * erate;
                                                    String meterreadingCP1 = String.format("%.2f", meterreadingdiff);
                                                    String meterreadingCP1P = String.format("%.2f", meterreadingdiffP);

                                                    tv_status1.setText(s10_CP1);
                                                    if (meterreadingdiffP > 0 && meterreadingdiffP < 100) {
                                                        txt_c1round_off_mr.setText("₹ " + meterreadingCP1P);
                                                        rsCP1 = "₹ " + meterreadingCP1P;
                                                        rsPOCP1 = "₹ " + meterreadingCP1P;
                                                    }
                                                    txt_c1diff.setText(time_readingCP1);
                                                    status_detailCP1 = s10_CP1;
                                                    unit_detailCP1 = meterreadingCP1;
                                                    etime_detailCP1 = time_readingCP1;
                                                    // sPisPoweFail.setisPowerFailCP1("t");
                                                    relativeLayout1st.setVisibility(View.GONE);


                                                } else if (plugedinCountcp1 == 0) {
                                                    tv_status1.setText(s11_CP1);
                                                    txt_c1round_off_mr.setText("₹ 00.00");
                                                    rsCP1 = "₹ 00.00";
                                                    txt_c1diff.setText("00:00:00");
                                                    status_detailCP1 = s11_CP1;
                                                    unit_detailCP1 = "00.00";
                                                    etime_detailCP1 = "00:00:00";
                                                    sPisPluggedin.setisPluggedinCP1("t");
                                                    plugedinCountcp1++;

                                                    //meter reading-------------------------
                                                    sPmeterReadingCP1.setMeterReadingCP1(p1meter);
                                                    //Time reading_________________
                                                    StartTimeCP1 = SystemClock.uptimeMillis() + (-NewBeginMillsCP1);  //--> Start Time
                                                    toggleBtn.startAnimation(anim);

                                                } else {
                                                    //time reading______
                                                    MillisCP1 = (SystemClock.uptimeMillis() - StartTimeCP1);

                                                    HoursCP1 = (int) (MillisCP1 / (1000 * 60 * 60));
                                                    MinutesCP1 = (int) (MillisCP1 / (1000 * 60)) % 60;
                                                    SecondsCP1 = (int) (MillisCP1 / 1000) % 60;
                                                    String time_readingCP1 = "" + String.format("%02d", HoursCP1) + ":"
                                                            + String.format("%02d", MinutesCP1) + ":"
                                                            + String.format("%02d", SecondsCP1);


                                                    //-----------meter Reading
                                                    Float prev_mrCP1 = Float.parseFloat(sPmeterReadingCP1.getMeterReadingCP1());
                                                    Float current_mrCP1 = Float.parseFloat(p1meter);
                                                    float meterreadingdiff = current_mrCP1 - prev_mrCP1;
                                                    float meterreadingdiffP = (current_mrCP1 - prev_mrCP1) * erate;
                                                    String meterreadingCP1 = String.format("%.2f", meterreadingdiff);
                                                    String meterreadingCP1P = String.format("%.2f", meterreadingdiffP);

                                                    //--------------------
                                                    tv_status1.setText(s11_CP1);
                                                    if (meterreadingdiffP > 0 && meterreadingdiffP < 100) {
                                                        txt_c1round_off_mr.setText("₹ " + meterreadingCP1P);
                                                        rsCP1 = "₹ " + meterreadingCP1P;
                                                        rsPOCP1 = "₹ " + meterreadingCP1P;
                                                    }
                                                    txt_c1diff.setText(time_readingCP1);
                                                    status_detailCP1 = s11_CP1;
                                                    unit_detailCP1 = meterreadingCP1;
                                                    etime_detailCP1 = time_readingCP1;
                                                    sPisPluggedin.setisPluggedinCP1("t");
                                                    //---------------------
                                                    sPisPoweFail.setisPowerFailCP1("t");
                                                    sPTimeReadingCP1.setTimeReadingCP1(MillisCP1);
                                                    //-------------------------------
                                                    plugedinCountcp1++;
                                                }

                                            }

                                        }
//-----------------------------------------------------------CP2 Status-----------------------------------------------------------------------------------------------------------------------------
                                        if (p2OnOff.equals("0") && p2plugin.equals("0") && p2fault.equals("99")) {

                                            if (sPisPluggedin.getisPluggedinCP2().equals("t")) {

                                                if (plugedoutCountcp2 >= 30) {
                                                    sPisPluggedin.setisPluggedinCP2("f");

                                                } else {
                                                    displayPlugoutCountC2 = 0;
                                                    tv_status2.setText(s7_CP2);
                                                    txt_c2round_off_mr.setText(rsPOCP2);
                                                    rsCP2 = "₹ 00.00";
                                                    txt_c2diff.setText(etime_detailCP2);
                                                    status_detailCP2 = s7_CP2;
                                                    unit_detailCP2 = unit_detailCP2;
                                                    etime_detailCP2 = etime_detailCP2;
                                                    plugedinCountcp2 = 0;
                                                    plugedoutCountcp2++;
                                                    if (isResumedAftercp2) {
                                                        sPisPoweFail.setisPowerFailCP1("f");
                                                    }
                                                }

                                            } else {
                                                countidleCP2++;
                                                if (isPleaseWaitC2){
                                                    tv_status2.setText("Please Wait...");
                                                    status_detailCP2 = "Please Wait...";
                                                    btn_p2onff.setVisibility(View.INVISIBLE);


                                                }else{
                                                    tv_status2.setText(s8_CP2);
                                                    status_detailCP2 = s8_CP2;

                                                }
                                                //  isStopC2 = false;
                                                if (isStopC2)
                                                {
                                                    //  countC2 = 0;
                                                    isAvailableC2 = true;
                                                    isStartC2 = true;
                                                    isStopC2 = false;
                                                }

                                                // Toast.makeText(getApplicationContext(),"C2 is Available",Toast.LENGTH_SHORT).show();
                                                txt_c2round_off_mr.setText("₹ 00.00");
                                                rsCP2 = "₹ 00.00";
                                                rsPOCP2 = "₹ 00.00";
                                                txt_c2diff.setText("00:00:00");
                                                unit_detailCP2 = "00.00";
                                                etime_detailCP2 = "00:00:00";
                                                plugedinCountcp2 = 0;
                                                btn_p2onff.clearAnimation();
                                                //overlay2
                                                if (overlayCountii> 10) {
                                                    isPleaseWaitC2 = false;
                                                    relativeLayout2nd.setVisibility(View.VISIBLE);
                                                }
                                                overlayCountii++;
                                            }

                                        }
                                        if (p2OnOff.equals("1") && p2plugin.equals("0") && p2fault.equals("99")) {
                                            countidleCP2 = 0;
                                            plugedinCountcp2 = 0;
                                            plugedoutCountcp2 = 0;
                                            tv_status2.setText(s1_CP2);
                                            isTappedC2 = false;
                                            isPleaseWaitC2 = false;

                                            if (isStartC2)
                                            {
                                                isStartC2 = false;
                                                countC2++;
                                                isStopC2 = true;
                                                isAvailableC2 = false;
                                            }
                                            txt_c2round_off_mr.setText("₹ 00.00");
                                            rsCP2 = "₹ 00.00";
                                            txt_c2diff.setText("00:00:00");
                                            status_detailCP2 = s1_CP2;
                                            unit_detailCP2 = "00.00";
                                            etime_detailCP2 = "00:00:00";
                                            isStillOnCP2 = false;
                                            relativeLayout2nd.setVisibility(View.GONE);
                                            btn_p2onff.setVisibility(View.VISIBLE);


                                            if (displayPlugoutCountC2 == 12) {
                                                if (sPisPoweFail.getisPowerFailCP2().equals("t") && sPisPluggedin.getisPluggedinCP2().equals("t")) {
                                                    btn_p2onff.performClick();
                                                    sPisPoweFail.setisPowerFailCP2("f");
                                                    sPisPluggedin.setisPluggedinCP2("f");
                                                    tv_status2.setText(s7_CP2);
                                                    status_detailCP2 = s7_CP2;
                                                }
                                            }
                                            displayPlugoutCountC2++;
                                        }
                                        if (p2OnOff.equals("1") && p2plugin.equals("1") && p2fault.equals("99")) {
                                            countidleCP2 = 0;
                                            if (sPisPoweFail.getisPowerFailCP2().equals("t")) {
                                                //---------Status:resuming after power fail_______________
                                                if (plugedinCountcp2 >= 10) {
                                                    //time reading______
                                                    MillisCP2 = (SystemClock.uptimeMillis() - StartTimeCP2);

                                                    HoursCP2 = (int) (MillisCP2 / (1000 * 60 * 60));
                                                    MinutesCP2 = (int) (MillisCP2 / (1000 * 60)) % 60;
                                                    SecondsCP2 = (int) (MillisCP2 / 1000) % 60;
                                                    String time_readingCP2 = "" + String.format("%02d", HoursCP2) + ":"
                                                            + String.format("%02d", MinutesCP2) + ":"
                                                            + String.format("%02d", SecondsCP2);
                                                    //-------------------------meter reading
                                                    Float prev_mrCP2 = Float.parseFloat(sPmeterReadingCP2.getMeterReadingCP2());
                                                    Float current_mrCP2 = Float.parseFloat(p2meter);
                                                    float meterreadingdiffc2 = current_mrCP2 - prev_mrCP2;
                                                    float meterreadingdiffc2P = (current_mrCP2 - prev_mrCP2) * erate;
                                                    String meterreadingCP2 = String.format("%.2f", meterreadingdiffc2);
                                                    String meterreadingCP2P = String.format("%.2f", meterreadingdiffc2P);
                                                    //--------------------
                                                    tv_status2.setText(s10_CP2);
                                                    if (meterreadingdiffc2P > 0 && meterreadingdiffc2P < 100) {
                                                        txt_c2round_off_mr.setText("₹ " + meterreadingCP2P);
                                                        rsCP2 = "₹ " + meterreadingCP2P;
                                                        rsPOCP2 = "₹ " + meterreadingCP2P;
                                                    }
                                                    txt_c2diff.setText(time_readingCP2);
                                                    status_detailCP2 = s10_CP2;
                                                    unit_detailCP2 = meterreadingCP2;
                                                    etime_detailCP2 = time_readingCP2;
                                                    sPisPoweFail.setisPowerFailCP2("f");
                                                    relativeLayout2nd.setVisibility(View.GONE);

                                                } else if (plugedinCountcp2 == 0) {
                                                    //Time reading_________________

                                                    StartTimeCP2 = SystemClock.uptimeMillis() + (-sPTimeReadingCP2.getTimeReadingCP2());
                                                    //-------------------------meter reading
                                                    Float prev_mrCP2 = Float.parseFloat(sPmeterReadingCP2.getMeterReadingCP2());
                                                    Float current_mrCP2 = Float.parseFloat(p2meter);
                                                    float meterreadingdiffc2 = current_mrCP2 - prev_mrCP2;
                                                    float meterreadingdiffc2P = (current_mrCP2 - prev_mrCP2) * erate;
                                                    String meterreadingCP2 = String.format("%.2f", meterreadingdiffc2);
                                                    String meterreadingCP2P = String.format("%.2f", meterreadingdiffc2P);
                                                    //--------------------
                                                    tv_status2.setText(s9_CP2);
                                                    if (meterreadingdiffc2P > 0 && meterreadingdiffc2P < 100) {
                                                        txt_c2round_off_mr.setText("₹ " + meterreadingCP2P);
                                                        rsCP2 = "₹ " + meterreadingCP2P;
                                                        rsPOCP2 = "₹ " + meterreadingCP2P;
                                                    }
                                                    txt_c2diff.setText("00:00:00");
                                                    status_detailCP2 = s9_CP2;
                                                    unit_detailCP2 = meterreadingCP2;
                                                    etime_detailCP2 = "00:00:00";
                                                    plugedinCountcp2++;
                                                    sPisPluggedin.setisPluggedinCP2("t");
                                                    isResumedAftercp2 = true;
                                                    txt_p2btnValue.startAnimation(anim2);
                                                    relativeLayout2nd.setVisibility(View.GONE);


                                                } else {
                                                    //time reading______
                                                    MillisCP2 = (SystemClock.uptimeMillis() - StartTimeCP2);

                                                    HoursCP2 = (int) (MillisCP2 / (1000 * 60 * 60));
                                                    MinutesCP2 = (int) (MillisCP2 / (1000 * 60)) % 60;
                                                    SecondsCP2 = (int) (MillisCP2 / 1000) % 60;
                                                    String time_readingCP2 = "" + String.format("%02d", HoursCP2) + ":"
                                                            + String.format("%02d", MinutesCP2) + ":"
                                                            + String.format("%02d", SecondsCP2);
                                                    //-------------------------meter reading
                                                    Float prev_mrCP2 = Float.parseFloat(sPmeterReadingCP2.getMeterReadingCP2());
                                                    Float current_mrCP2 = Float.parseFloat(p2meter);
                                                    float meterreadingdiffc2 = current_mrCP2 - prev_mrCP2;
                                                    float meterreadingdiffc2P = (current_mrCP2 - prev_mrCP2) * erate;
                                                    String meterreadingCP2 = String.format("%.2f", meterreadingdiffc2);
                                                    String meterreadingCP2P = String.format("%.2f", meterreadingdiffc2P);
                                                    //--------------------
                                                    tv_status2.setText(s9_CP2);
                                                    if (meterreadingdiffc2P > 0 && meterreadingdiffc2P < 100) {
                                                        txt_c2round_off_mr.setText("₹ " + meterreadingCP2P);
                                                        rsCP2 = "₹ " + meterreadingCP2P;
                                                        rsPOCP2 = "₹ " + meterreadingCP2P;
                                                    }
                                                    txt_c2diff.setText(time_readingCP2);
                                                    status_detailCP2 = s9_CP2;
                                                    unit_detailCP2 = meterreadingCP2;
                                                    etime_detailCP2 = time_readingCP2;
                                                    plugedinCountcp2++;
                                                    sPisPluggedin.setisPluggedinCP2("t");
                                                    isResumedAftercp2 = true;
                                                    relativeLayout2nd.setVisibility(View.GONE);


                                                }

                                            } else {
                                                //--------------- Status:vehicle pugged in_________
                                                if (plugedinCountcp2 >= 10) {
                                                    //time reading______
                                                    MillisCP2 = (SystemClock.uptimeMillis() - StartTimeCP2);

                                                    HoursCP2 = (int) (MillisCP2 / (1000 * 60 * 60));
                                                    MinutesCP2 = (int) (MillisCP2 / (1000 * 60)) % 60;
                                                    SecondsCP2 = (int) (MillisCP2 / 1000) % 60;
                                                    String time_readingCP2 = "" + String.format("%02d", HoursCP2) + ":"
                                                            + String.format("%02d", MinutesCP2) + ":"
                                                            + String.format("%02d", SecondsCP2);
                                                    //-------------------------meter reading
                                                    Float prev_mrCP2 = Float.parseFloat(sPmeterReadingCP2.getMeterReadingCP2());
                                                    Float current_mrCP2 = Float.parseFloat(p2meter);
                                                    float meterreadingdiffc2 = current_mrCP2 - prev_mrCP2;
                                                    float meterreadingdiffc2P = (current_mrCP2 - prev_mrCP2) * erate;
                                                    String meterreadingCP2 = String.format("%.2f", meterreadingdiffc2);
                                                    String meterreadingCP2P = String.format("%.2f", meterreadingdiffc2P);
                                                    //--------------------
                                                    tv_status2.setText(s10_CP2);
                                                    if (meterreadingdiffc2P > 0 && meterreadingdiffc2P < 100) {
                                                        txt_c2round_off_mr.setText("₹ " + meterreadingCP2P);
                                                        rsCP2 = "₹ " + meterreadingCP2P;
                                                        rsPOCP2 = "₹ " + meterreadingCP2P;
                                                    }
                                                    txt_c2diff.setText(time_readingCP2);
                                                    status_detailCP2 = s10_CP2;
                                                    unit_detailCP2 = meterreadingCP2;
                                                    etime_detailCP2 = time_readingCP2;
                                                    // sPisPoweFail.setisPowerFailCP1("t");
                                                    relativeLayout2nd.setVisibility(View.GONE);


                                                } else if (plugedinCountcp2 == 0) {
                                                    tv_status2.setText(s11_CP2);
                                                    txt_c2round_off_mr.setText("₹ 00.00");
                                                    rsCP2 = "₹ 00.00";
                                                    txt_c2diff.setText("00:00:00");
                                                    status_detailCP2 = s11_CP2;
                                                    unit_detailCP2 = "00.00";
                                                    etime_detailCP2 = "00:00:00";
                                                    sPisPluggedin.setisPluggedinCP2("t");
                                                    plugedinCountcp2++;

                                                    //meter reading-------------------------
                                                    sPmeterReadingCP2.setMeterReadingCP2(p2meter);
                                                    //Time reading_________________
                                                    StartTimeCP2 = SystemClock.uptimeMillis() + (-NewBeginMillsCP2);  //--> Start Time
                                                    txt_p2btnValue.startAnimation(anim2);

                                                } else {
                                                    //time reading______
                                                    MillisCP2 = (SystemClock.uptimeMillis() - StartTimeCP2);

                                                    HoursCP2 = (int) (MillisCP2 / (1000 * 60 * 60));
                                                    MinutesCP2 = (int) (MillisCP2 / (1000 * 60)) % 60;
                                                    SecondsCP2 = (int) (MillisCP2 / 1000) % 60;
                                                    String time_readingCP2 = "" + String.format("%02d", HoursCP2) + ":"
                                                            + String.format("%02d", MinutesCP2) + ":"
                                                            + String.format("%02d", SecondsCP2);


                                                    //-------------------------meter reading
                                                    Float prev_mrCP2 = Float.parseFloat(sPmeterReadingCP2.getMeterReadingCP2());
                                                    Float current_mrCP2 = Float.parseFloat(p2meter);
                                                    float meterreadingdiffc2 = current_mrCP2 - prev_mrCP2;
                                                    float meterreadingdiffc2P = (current_mrCP2 - prev_mrCP2) * erate;
                                                    String meterreadingCP2 = String.format("%.2f", meterreadingdiffc2);
                                                    String meterreadingCP2P = String.format("%.2f", meterreadingdiffc2P);
                                                    //--------------------
                                                    tv_status2.setText(s11_CP2);
                                                    if (meterreadingdiffc2P > 0 && meterreadingdiffc2P < 100) {
                                                        txt_c2round_off_mr.setText("₹ " + meterreadingCP2P);
                                                        rsCP2 = "₹ " + meterreadingCP2P;
                                                        rsPOCP2 = "₹ " + meterreadingCP2P;
                                                    }
                                                    txt_c2diff.setText(time_readingCP2);
                                                    status_detailCP2 = s11_CP2;
                                                    unit_detailCP2 = meterreadingCP2;
                                                    etime_detailCP2 = time_readingCP2;
                                                    sPisPluggedin.setisPluggedinCP2("t");
                                                    plugedinCountcp2++;
                                                }

                                            }

                                        }

//-----------------------------------------------------------CP3 Status-----------------------------------------------------------------------------------------------------------------------------
                                        if (p3OnOff.equals("0") && p3plugin.equals("0") && p3fault.equals("99")) {

                                            if (sPisPluggedin.getisPluggedinCP3().equals("t")) {

                                                if (plugedoutCountcp3 >= 30) {
                                                    sPisPluggedin.setisPluggedinCP3("f");

                                                } else {
                                                    displayPlugoutCountC2 = 0;
                                                    tv_status3.setText(s7_CP3);
                                                    txt_c3round_off_mr.setText(rsPOCP3);
                                                    rsCP3 = "₹ 00.00";
                                                    txt_c3diff.setText(etime_detailCP3);
                                                    status_detailCP3 = s7_CP3;
                                                    unit_detailCP3 = unit_detailCP3;
                                                    etime_detailCP3 = etime_detailCP3;
                                                    plugedinCountcp3 = 0;
                                                    plugedoutCountcp3++;
                                                    if (isResumedAftercp3) {
                                                        sPisPoweFail.setisPowerFailCP3("f");
                                                    }
                                                    //
                                                }

                                            } else {
                                                countidleCP3++;
                                                if (isPleaseWaitC3){
                                                    tv_status3.setText("Please Wait...");
                                                    status_detailCP3 = "Please Wait...";
                                                    btn_p3onff.setVisibility(View.INVISIBLE);


                                                }else{
                                                    tv_status3.setText(s8_CP3);
                                                    status_detailCP3 = s8_CP3;

                                                }

                                                if (isStopC3){
                                                    //   countC3 = 0;
                                                    isAvailableC3=true;
                                                    isStartC3 = true;
                                                    isStopC3 = false;
                                                }

                                                //Toast.makeText(getApplicationContext(),"C3 Is Available Now",Toast.LENGTH_SHORT).show();
                                                txt_c3round_off_mr.setText("₹ 00.00");
                                                rsCP3 = "₹ 00.00";
                                                rsPOCP3 = "₹ 00.00";
                                                txt_c3diff.setText("00:00:00");
                                                unit_detailCP3 = "00.00";
                                                etime_detailCP3 = "00:00:00";
                                                plugedinCountcp3 = 0;
                                                btn_p3onff.clearAnimation();

                                                //overlay1
                                                if (overlayCountiii> 10) {
                                                    isPleaseWaitC3 = false;
                                                    relativeLayout3rd.setVisibility(View.VISIBLE);
                                                }
                                                overlayCountiii++;
                                            }
                                        }

                                        if (p3OnOff.equals("1") && p3plugin.equals("0") && p3fault.equals("99")) {
                                            plugedinCountcp3 = 0;
                                            plugedoutCountcp3 = 0;
                                            tv_status3.setText(s1_CP3);
                                            //countC3=1;\
                                            isTappedC3 = false;
                                            isPleaseWaitC3 = false;

                                            if (isStartC3)
                                            {
                                                isStartC3 = false;
                                                isAvailableC3 = false;
                                                countC3++;
                                                isStopC3 = true;
                                            }
                                            txt_c3round_off_mr.setText("₹ 00.00");
                                            countidleCP3 = 0;
                                            rsCP3 = "₹ 00.00";
                                            txt_c3diff.setText("00:00:00");
                                            status_detailCP3 = s1_CP3;
                                            unit_detailCP3 = "00.00";
                                            etime_detailCP3 = "00:00:00";
                                            relativeLayout3rd.setVisibility(View.GONE);
                                            btn_p3onff.setVisibility(View.VISIBLE);


                                            if (displayPlugoutCountC3 == 12) {
                                                if (sPisPoweFail.getisPowerFailCP3().equals("t") && sPisPluggedin.getisPluggedinCP3().equals("t")) {
                                                    btn_p3onff.performClick();
                                                    sPisPoweFail.setisPowerFailCP3("f");
                                                    sPisPluggedin.setisPluggedinCP3("f");
                                                    tv_status3.setText(s7_CP3);
                                                    status_detailCP3 = s7_CP3;
                                                }
                                            }
                                            displayPlugoutCountC3++;
                                        }
                                        if (p3OnOff.equals("1") && p3plugin.equals("1") && p3fault.equals("99")) {
                                            countidleCP3 = 0;
                                            if (sPisPoweFail.getisPowerFailCP3().equals("t")) {
                                                //---------Status:resuming after power fail_______________
                                                if (plugedinCountcp3 >= 10) {
                                                    //time reading______
                                                    MillisCP3 = (SystemClock.uptimeMillis() - StartTimeCP3);

                                                    HoursCP3 = (int) (MillisCP3 / (1000 * 60 * 60));
                                                    MinutesCP3 = (int) (MillisCP3 / (1000 * 60)) % 60;
                                                    SecondsCP3 = (int) (MillisCP3 / 1000) % 60;
                                                    String time_readingCP3 = "" + String.format("%02d", HoursCP3) + ":"
                                                            + String.format("%02d", MinutesCP3) + ":"
                                                            + String.format("%02d", SecondsCP3);
                                                    //-------------------------meter reading
                                                    Float prev_mrCP3 = Float.parseFloat(sPmeterReadingCP3.getMeterReadingCP3());
                                                    Float current_mrCP3 = Float.parseFloat(p3meter);
                                                    float meterreadingdiffc3 = current_mrCP3 - prev_mrCP3;
                                                    float meterreadingdiffc3P = (current_mrCP3 - prev_mrCP3) * erate;
                                                    String meterreadingCP3 = String.format("%.2f", meterreadingdiffc3);
                                                    String meterreadingCP3P = String.format("%.2f", meterreadingdiffc3P);
                                                    //--------------------
                                                    tv_status3.setText(s10_CP3);
                                                    if (meterreadingdiffc3P > 0 && meterreadingdiffc3P < 100) {
                                                        txt_c3round_off_mr.setText("₹ " + meterreadingCP3P);
                                                        rsCP3 = "₹ " + meterreadingCP3P;
                                                        rsPOCP3 = "₹ " + meterreadingCP3P;
                                                    }
                                                    txt_c3diff.setText(time_readingCP3);
                                                    status_detailCP3 = s10_CP3;
                                                    unit_detailCP3 = meterreadingCP3;
                                                    etime_detailCP3 = time_readingCP3;
                                                    sPisPoweFail.setisPowerFailCP3("f");
                                                    relativeLayout3rd.setVisibility(View.GONE);

                                                } else if (plugedinCountcp3 == 0) {
                                                    //Time reading_________________

                                                    StartTimeCP3 = SystemClock.uptimeMillis() + (-sPTimeReadingCP3.getTimeReadingCP3());
                                                    //-------------------------meter reading
                                                    Float prev_mrCP3 = Float.parseFloat(sPmeterReadingCP3.getMeterReadingCP3());
                                                    Float current_mrCP3 = Float.parseFloat(p3meter);
                                                    float meterreadingdiffc3 = current_mrCP3 - prev_mrCP3;
                                                    float meterreadingdiffc3P = (current_mrCP3 - prev_mrCP3) * erate;
                                                    String meterreadingCP3 = String.format("%.2f", meterreadingdiffc3);
                                                    String meterreadingCP3P = String.format("%.2f", meterreadingdiffc3P);
                                                    //--------------------
                                                    tv_status3.setText(s9_CP3);
                                                    if (meterreadingdiffc3P > 0 && meterreadingdiffc3P < 100) {
                                                        txt_c3round_off_mr.setText("₹ " + meterreadingCP3P);
                                                        rsCP3 = "₹ " + meterreadingCP3P;
                                                        rsPOCP3 = "₹ " + meterreadingCP3P;
                                                    }
                                                    txt_c3diff.setText("00:00:00");
                                                    status_detailCP3 = s9_CP3;
                                                    unit_detailCP3 = meterreadingCP3;
                                                    etime_detailCP3 = "00:00:00";
                                                    plugedinCountcp3++;
                                                    sPisPluggedin.setisPluggedinCP3("t");
                                                    isResumedAftercp3 = true;
                                                    txt_p3btnValue.startAnimation(anim3);
                                                    relativeLayout3rd.setVisibility(View.GONE);


                                                } else {
                                                    //time reading______
                                                    MillisCP3 = (SystemClock.uptimeMillis() - StartTimeCP3);

                                                    HoursCP3 = (int) (MillisCP3 / (1000 * 60 * 60));
                                                    MinutesCP3 = (int) (MillisCP3 / (1000 * 60)) % 60;
                                                    SecondsCP3 = (int) (MillisCP3 / 1000) % 60;
                                                    String time_readingCP3 = "" + String.format("%02d", HoursCP3) + ":"
                                                            + String.format("%02d", MinutesCP3) + ":"
                                                            + String.format("%02d", SecondsCP3);
                                                    //-------------------------meter reading
                                                    Float prev_mrCP3 = Float.parseFloat(sPmeterReadingCP3.getMeterReadingCP3());
                                                    Float current_mrCP3 = Float.parseFloat(p3meter);
                                                    float meterreadingdiffc3 = current_mrCP3 - prev_mrCP3;
                                                    float meterreadingdiffc3P = (current_mrCP3 - prev_mrCP3) * erate;
                                                    String meterreadingCP3 = String.format("%.2f", meterreadingdiffc3);
                                                    String meterreadingCP3P = String.format("%.2f", meterreadingdiffc3P);
                                                    //--------------------
                                                    tv_status3.setText(s9_CP3);
                                                    if (meterreadingdiffc3P > 0 && meterreadingdiffc3P < 100) {
                                                        txt_c3round_off_mr.setText("₹ " + meterreadingCP3P);
                                                        rsCP3 = "₹ " + meterreadingCP3P;
                                                        rsPOCP3 = "₹ " + meterreadingCP3P;
                                                    }
                                                    status_detailCP3 = s9_CP3;
                                                    unit_detailCP3 = meterreadingCP3;
                                                    etime_detailCP3 = time_readingCP3;
                                                    plugedinCountcp3++;
                                                    sPisPluggedin.setisPluggedinCP3("t");
                                                    isResumedAftercp3 = true;
                                                    relativeLayout3rd.setVisibility(View.GONE);


                                                }

                                            } else {
                                                //--------------- Status:vehicle pugged in_________
                                                if (plugedinCountcp3 >= 10) {
                                                    //time reading______
                                                    MillisCP3 = (SystemClock.uptimeMillis() - StartTimeCP3);

                                                    HoursCP3 = (int) (MillisCP3 / (1000 * 60 * 60));
                                                    MinutesCP3 = (int) (MillisCP3 / (1000 * 60)) % 60;
                                                    SecondsCP3 = (int) (MillisCP3 / 1000) % 60;
                                                    String time_readingCP3 = "" + String.format("%02d", HoursCP3) + ":"
                                                            + String.format("%02d", MinutesCP3) + ":"
                                                            + String.format("%02d", SecondsCP3);

                                                    //-------------------------meter reading
                                                    Float prev_mrCP3 = Float.parseFloat(sPmeterReadingCP3.getMeterReadingCP3());
                                                    Float current_mrCP3 = Float.parseFloat(p3meter);
                                                    float meterreadingdiffc3 = current_mrCP3 - prev_mrCP3;
                                                    //  String meterreadingCP3 = String.format("%.2f", meterreadingdiffc3);
                                                    float meterreadingdiffc3P = (current_mrCP3 - prev_mrCP3) * erate;
                                                    String meterreadingCP3 = String.format("%.2f", meterreadingdiffc3);
                                                    String meterreadingCP3P = String.format("%.2f", meterreadingdiffc3P);
                                                    //--------------------
                                                    tv_status3.setText(s10_CP3);
                                                    if (meterreadingdiffc3P > 0 && meterreadingdiffc3P < 100) {
                                                        txt_c3round_off_mr.setText("₹ " + meterreadingCP3P);
                                                        rsCP3 = "₹ " + meterreadingCP3P;
                                                        rsPOCP3 = "₹ " + meterreadingCP3P;
                                                    }
                                                    txt_c3diff.setText(time_readingCP3);
                                                    status_detailCP3 = s10_CP3;
                                                    unit_detailCP3 = meterreadingCP3;
                                                    etime_detailCP3 = time_readingCP3;
                                                    // sPisPoweFail.setisPowerFailCP1("t");
                                                    relativeLayout3rd.setVisibility(View.GONE);


                                                } else if (plugedinCountcp3 == 0) {
                                                    tv_status3.setText(s11_CP3);
                                                    txt_c3round_off_mr.setText("₹ 00.00");
                                                    rsCP3 = "₹ 00.00";
                                                    txt_c3diff.setText("00:00:00");
                                                    status_detailCP3 = s11_CP3;
                                                    unit_detailCP3 = "00.00";
                                                    etime_detailCP3 = "00:00:00";
                                                    sPisPluggedin.setisPluggedinCP3("t");
                                                    plugedinCountcp3++;

                                                    //meter reading-------------------------
                                                    sPmeterReadingCP3.setMeterReadingCP3(p3meter);
                                                    //Time reading_________________
                                                    StartTimeCP3 = SystemClock.uptimeMillis() + (-NewBeginMillsCP3);  //--> Start Time
                                                    txt_p3btnValue.startAnimation(anim3);

                                                } else {
                                                    //time reading______
                                                    MillisCP3 = (SystemClock.uptimeMillis() - StartTimeCP3);

                                                    HoursCP3 = (int) (MillisCP3 / (1000 * 60 * 60));
                                                    MinutesCP3 = (int) (MillisCP3 / (1000 * 60)) % 60;
                                                    SecondsCP3 = (int) (MillisCP3 / 1000) % 60;
                                                    String time_readingCP3 = "" + String.format("%02d", HoursCP3) + ":"
                                                            + String.format("%02d", MinutesCP3) + ":"
                                                            + String.format("%02d", SecondsCP3);

                                                    //-------------------------meter reading
                                                    Float prev_mrCP3 = Float.parseFloat(sPmeterReadingCP3.getMeterReadingCP3());
                                                    Float current_mrCP3 = Float.parseFloat(p3meter);
                                                    float meterreadingdiffc3 = current_mrCP3 - prev_mrCP3;
                                                    float meterreadingdiffc3P = (current_mrCP3 - prev_mrCP3) * erate;
                                                    String meterreadingCP3 = String.format("%.2f", meterreadingdiffc3);
                                                    String meterreadingCP3P = String.format("%.2f", meterreadingdiffc3P);
                                                    //--------------------
                                                    tv_status3.setText(s11_CP3);
                                                    if (meterreadingdiffc3P > 0 && meterreadingdiffc3P < 100) {
                                                        txt_c3round_off_mr.setText("₹ " + meterreadingCP3P);
                                                        rsCP3 = "₹ " + meterreadingCP3P;
                                                        rsPOCP3 = "₹ " + meterreadingCP3P;
                                                    }
                                                    txt_c3diff.setText(time_readingCP3);
                                                    status_detailCP3 = s11_CP3;
                                                    unit_detailCP3 = meterreadingCP3;
                                                    etime_detailCP3 = time_readingCP3;
                                                    sPisPluggedin.setisPluggedinCP3("t");
                                                    plugedinCountcp3++;
                                                }

                                            }

                                        }


                                        if (detail_flag.equals("d_CP1")) {
                                            display_details(rsCP1, s15_CP1+" #01", voltage_detailCP1, current_detailCP1, power_detailCP1, unit_detailCP1, etime_detailCP1, status_detailCP1,s14_CP1,s17_CP1," ₹ "+erate_s+" "+s16_CP1,sPlanguageCP1.getlanguageCP1());

                                        } else if (detail_flag.equals("d_CP2")) {
                                            display_details(rsCP2, s15_CP2+" #02", voltage_detailCP2, current_detailCP2, power_detailCP2, unit_detailCP2, etime_detailCP2, status_detailCP2,s14_CP2,s17_CP2," ₹ "+erate_s+" "+s16_CP2,sPlanguageCP2.getlanguageCP2());

                                        } else if (detail_flag.equals("d_CP3")) {
                                            display_details(rsCP3, s15_CP3+" #03", voltage_detailCP3, current_detailCP3, power_detailCP3, unit_detailCP3, etime_detailCP3, status_detailCP3,s14_CP3,s17_CP3," ₹ "+erate_s+" "+s16_CP3,sPlanguageCP3.getlanguageCP3());

                                        }

                                        Log.e("CHECKII", "CHECKING");

                                        Log.e("COMMING STRING", recDataString + "\n\np1OnOff :" + p1OnOff + "\tp1plugin :" + p1plugin + "\tp1fault :" + p1fault + "\tp1voltage :" + p1voltage + "\tp1current :" + p1current + "\tp1meter :" + p1meter +
                                                "\n\np2OnOff : " + p2OnOff + "\tp2plugin : " + p2plugin + "\tp2fault : " + p2fault + "\tp2voltage : " + p2voltage + "\tp2current : " + p2current + "\tp2meter : " + p2meter +
                                                "\n\np3OnOff : " + p3OnOff + "\tp3plugin : " + p3plugin + "\tp3fault : " + p3fault + "\tp3voltage: " + p3voltage + "\tp3current : " + p3current + "\tp3meter : " + p3meter);

                                    }
                                } catch (StringIndexOutOfBoundsException siobe) {
                                    //System.out.println("invalid input");
                                }

                            }

                            recDataString.delete(0, recDataString.length());                    //clear all string data

                        }
else{
                        recDataString.delete(0, recDataString.length());                    //clear all string data

                    }

                    }
                    else {
                        tv_status1.setText(s6_CP1);
                        tv_status2.setText(s6_CP2);
                        tv_status3.setText(s6_CP3);
                        toggleBtn.setCardBackgroundColor(Color.parseColor(red));
                        toggleBtn.startAnimation(anim);
                        btn_p2onff.setCardBackgroundColor(Color.parseColor(red));
                        btn_p2onff.startAnimation(anim);
                        btn_p3onff.setCardBackgroundColor(Color.parseColor(red));
                        btn_p3onff.startAnimation(anim);
                    }
                }
                else{
                    msg.what = handlerState;
                }

            }
        };

        btAdapter = BluetoothAdapter.getDefaultAdapter();       // get Bluetooth adapter
        checkBTState();

        toggleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAlreadyonm) {
                    funConnectori();
                }else {
                    sPisPoweFail.setisPowerFailCP1("f");
                    StringBuilder newCommandp1 = new StringBuilder(command);
                    newCommandp1.setCharAt(1, '0');


                    command = ""+newCommandp1;
                    toggleBtn.setCardBackgroundColor(Color.parseColor(green));
                    toggleValue.setText("CP1 "+s22_CP1);

                    toggleBtn.clearAnimation();
                }
            }
        });


        btn_p2onff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAlreadyonm) {
                    funConnectorii();
                }else {
                    sPisPoweFail.setisPowerFailCP2("f");
                    StringBuilder newCommandp2 = new StringBuilder(command);
                    newCommandp2.setCharAt(1, '0');


                    command = ""+newCommandp2;
                    btn_p2onff.setCardBackgroundColor(Color.parseColor(green));
                    txt_p2btnValue.setText("CP2 "+s22_CP2);
                    txt_p2btnValue.clearAnimation();
                }
            }
        });
        btn_p3onff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAlreadyonm) {
                    funConnectoriii();
                }else {
                    sPisPoweFail.setisPowerFailCP3("f");
                    StringBuilder newCommandp3 = new StringBuilder(command);
                    newCommandp3.setCharAt(1, '0');


                    command = ""+newCommandp3;
                    btn_p3onff.setCardBackgroundColor(Color.parseColor(green));
                    txt_p3btnValue.setText("CP3 "+s22_CP3);
                    txt_p3btnValue.clearAnimation();
                }
            }
        });



        //Get MAC address from InitiatingActivity via intent

        Intent intent = getIntent();

        //Get the MAC address from the DeviceListActivty via EXTRA
        address = intent.getStringExtra(InitiatingActivity.EXTRA_DEVICE_ADDRESS);

        //create device and set the MAC address
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            // Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_LONG).show();
        }
        // Establish the Bluetooth socket connection.
        try
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
        }
        mConnectedThread = new ConnectedThread(btSocket);
        mConnectedThread.start();

        //I send a character when resuming.beginning transmission to check device is connected
        //If it is not an exception will be thrown in the write method and finish() will be called
        mConnectedThread.write("x");
        // Toast.makeText(MaindisplayActivity.this,"OnResume",Toast.LENGTH_LONG).show();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        this.registerReceiver(mReceiver, filter);
        IntentFilter batteryfilter = new IntentFilter();
        batteryfilter.addAction(Intent.ACTION_POWER_CONNECTED);
        batteryfilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        registerReceiver(batteryReceiver,batteryfilter);

    }

    private void display_details(final String rate_detail, final String charge_point_id,final String voltage_detail,final String current_detail,final String power_detail,final String unit_detail,final String etime_detail,final String status_detail,final String etime_displayf, final String unit_display, final String rate_display, final String lang){
        // txt_cpname,txt_rate,txt_voltage_detail,txt_current_detail,txt_power_detail,txt_unit_detail,txt_etime_detail,txt_status_detail;
        txt_rate.setText(rate_detail);
        txt_cpname.setText(charge_point_id);
        txt_voltage_detail.setText(voltage_detail);
        txt_current_detail.setText(current_detail);
        txt_power_detail.setText(power_detail);
        txt_unit_detail.setText(unit_detail);
        txt_etime_detail.setText(etime_detail);
        txt_status_detail.setText(status_detail);
        txt_etime_displayf.setText(etime_displayf);
        txt_unit_display.setText(unit_display);
        txt_rate_display.setText(rate_display);
        if (lang.equals("en")){
            rg_language_m.check(R.id.rb_en_m);
        }else if (lang.equals("ma")){
            rg_language_m.check(R.id.rb_ma_m);
        }else if (lang.equals("hi")){
            rg_language_m.check(R.id.rb_hi_m);
        }

    }
    public void funConnectori(){
        if (isStillOnCP1){
            flag = false;
            setToggleBtn();
            sPisPluggedin.setisPluggedinCP1("f");
            sPisPoweFail.setisPowerFailCP1("f");
        }
        else {
            if (flag) {
                StringBuilder newCommandp1 = new StringBuilder(command);
                newCommandp1.setCharAt(1, '0');

                Log.e("newCommandp1", "" + newCommandp1);
                mConnectedThread.write("" + newCommandp1);    // Send "0" via Bluetooth
                //  Toast.makeText(getBaseContext(), "Turn off CP1 :" + newCommandp1, Toast.LENGTH_SHORT).show();


                //flag=false;

            } else {
                StringBuilder newCommandp1 = new StringBuilder(command);
                newCommandp1.setCharAt(1, '1');


                Log.e("newCommandp1", "" + newCommandp1);
                mConnectedThread.write("" + newCommandp1);    // Send "0" via Bluetooth
                // Toast.makeText(getBaseContext(), "Turn on CP1 :" + newCommandp1, Toast.LENGTH_SHORT).show();
                //flag=true;

            }

            setToggleBtn();
        }
    }
    public void funConnectorii(){
        if (isStillOnCP2){
            p2flag = false;
            setP2Btn();
            sPisPluggedin.setisPluggedinCP2("f");
            sPisPoweFail.setisPowerFailCP2("f");
        }
        else
        {
            if (p2flag)
            {
                StringBuilder newCommandp2 = new StringBuilder(command);
                newCommandp2.setCharAt(2, '0');
                Log.e("newCommandp2", "" + newCommandp2);
                mConnectedThread.write("" + newCommandp2);    // Send "0" via Bluetooth
                // Toast.makeText(getBaseContext(), "Turn off CP2 :" + newCommandp2, Toast.LENGTH_SHORT).show();
                //flag=false;

            }
            else
            {
                StringBuilder newCommandp2 = new StringBuilder(command);
                newCommandp2.setCharAt(2, '1');

                Log.e("newCommandp2", "" + newCommandp2);
                mConnectedThread.write("" + newCommandp2);    // Send "1" via Bluetooth
                //   Toast.makeText(getBaseContext(), "Turn on CP2 :" + newCommandp2, Toast.LENGTH_SHORT).show();
                //flag=true;

            }

            setP2Btn();
        }
    }
    public void funConnectoriii(){
        if (isStillOnCP3){
            p3flag = false;
            setP3Btn();
            sPisPluggedin.setisPluggedinCP3("f");
            sPisPoweFail.setisPowerFailCP3("f");
        }
        else {
            if (p3flag)
            {
                StringBuilder newCommandp3 = new StringBuilder(command);
                newCommandp3.setCharAt(3, '0');

                Log.e("newCommandp3", "" + newCommandp3);
                mConnectedThread.write("" + newCommandp3);    // Send "0" via Bluetooth
                // Toast.makeText(getBaseContext(), "Turn off CP3 :" + newCommandp3, Toast.LENGTH_SHORT).show();
                //flag=false;

            }
            else
            {
                StringBuilder newCommandp3 = new StringBuilder(command);
                newCommandp3.setCharAt(3, '1');

                Log.e("newCommandp3", "" + newCommandp3);
                mConnectedThread.write("" + newCommandp3);    // Send "0" via Bluetooth
                //mConnectedThread.write("$100990.00");    // Send "1" via Bluetooth
                // Toast.makeText(getBaseContext(), "Turn on CP3 :" + newCommandp3, Toast.LENGTH_SHORT).show();
                //flag=true;
            }

            setP3Btn();
        }
    }

    @Override
    protected void onStop()
    {
        // Toast.makeText(MaindisplayActivity.this,"onStop Method",Toast.LENGTH_LONG).show();
        Log.e("METHOD","onStop Method");
        unregisterReceiver(mReceiver);
        unregisterReceiver(batteryReceiver);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Toast.makeText(MaindisplayActivity.this,"onDestroy Method",Toast.LENGTH_LONG).show();
        Log.e("METHOD","onDestroy Method");
    }
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Toast.makeText(MaindisplayActivity.this, device.getName() + " Device found", Toast.LENGTH_LONG).show();
            }
            else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                //Toast.makeText(MaindisplayActivity.this, device.getName() + " Device is now connected", Toast.LENGTH_LONG).show();

            }
            else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
                //Toast.makeText(MaindisplayActivity.this, device.getName() + " Device is about to disconnect", Toast.LENGTH_LONG).show();
            }
            else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                //Toast.makeText(MaindisplayActivity.this, device.getName() + " Device has disconnected", Toast.LENGTH_LONG).show();
                tv_status1.setText(s6_CP1);
                toggleBtn.setCardBackgroundColor(Color.parseColor(red));
                toggleBtn.startAnimation(anim);

                tv_status2.setText(s6_CP2);
                btn_p2onff.setCardBackgroundColor(Color.parseColor(red));
                btn_p2onff.startAnimation(anim);
                tv_status3.setText(s6_CP3);
                btn_p3onff.setCardBackgroundColor(Color.parseColor(red));
                btn_p3onff.startAnimation(anim);
                mFirebaseInstance.getReference(cid+"-CP1M").setValue(tv_status1.getText().toString());
                mFirebaseInstance.getReference(cid+"-CP2M").setValue(tv_status2.getText().toString());
                mFirebaseInstance.getReference(cid+"-CP3M").setValue(tv_status3.getText().toString());
if(sPisPluggedin.getisPluggedinCP1().equals("t")){
                    sPisPoweFail.setisPowerFailCP1("t");
                    sPTimeReadingCP1.setTimeReadingCP1(MillisCP1);

                }
                if(sPisPluggedin.getisPluggedinCP2().equals("t")){
                    sPisPoweFail.setisPowerFailCP2("t");
                    sPTimeReadingCP2.setTimeReadingCP2(MillisCP2);

                }
                if(sPisPluggedin.getisPluggedinCP3().equals("t")){
                    sPisPoweFail.setisPowerFailCP3("t");
                    sPTimeReadingCP3.setTimeReadingCP3(MillisCP3);

                }


            }
        }
    };
    private final BroadcastReceiver batteryReceiver =  new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            if (intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)) {
                //   Toast.makeText(context, "The device is charging", Toast.LENGTH_SHORT).show();
                if (!isAlreadyonm){
                    startActivity(new Intent(MaindisplayActivityMannual.this,InitiatingActivity.class));
                    finish();

                }


            } else {
                intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED);
                isAlreadyonm = false;
                // Toast.makeText(context, "The device is not charging", Toast.LENGTH_SHORT).show();
                tv_status1.setText(s6_CP1);
                toggleBtn.setCardBackgroundColor(Color.parseColor(red));
                toggleBtn.startAnimation(anim);
                tv_status2.setText(s6_CP2);
                btn_p2onff.setCardBackgroundColor(Color.parseColor(red));
                btn_p2onff.startAnimation(anim);
                tv_status3.setText(s6_CP3);
                btn_p3onff.setCardBackgroundColor(Color.parseColor(red));
                btn_p3onff.startAnimation(anim);
                mFirebaseInstance.getReference(cid+"-CP1M").setValue(tv_status1.getText().toString());
                mFirebaseInstance.getReference(cid+"-CP2M").setValue(tv_status2.getText().toString());
                mFirebaseInstance.getReference(cid+"-CP3M").setValue(tv_status3.getText().toString());
                if(sPisPluggedin.getisPluggedinCP1().equals("t")){
                    sPisPoweFail.setisPowerFailCP1("t");
                    sPTimeReadingCP1.setTimeReadingCP1(MillisCP1);

                }
                if(sPisPluggedin.getisPluggedinCP2().equals("t")){
                    sPisPoweFail.setisPowerFailCP2("t");
                    sPTimeReadingCP2.setTimeReadingCP2(MillisCP2);

                }
                if(sPisPluggedin.getisPluggedinCP3().equals("t")){
                    sPisPoweFail.setisPowerFailCP3("t");
                    sPTimeReadingCP3.setTimeReadingCP3(MillisCP3);

                }
            }
        }
    };

    private void setToggleBtn() {

        if(flag)
        {
            StringBuilder newCommandp3 = new StringBuilder(command);
            newCommandp3.setCharAt(1, '1');

            command = ""+newCommandp3;
            toggleBtn.setCardBackgroundColor(Color.parseColor(green));
            toggleValue.setText("C1\n"+s23_CP1);

            // bulb_2.setCardBackgroundColor(Color.parseColor(AmberON));
            // toggleBtn.startAnimation(anim);
        }
        else
        {
            StringBuilder newCommandp3 = new StringBuilder(command);
            newCommandp3.setCharAt(1, '0');

            command = ""+newCommandp3;
            toggleBtn.setCardBackgroundColor(Color.parseColor(green));
            toggleValue.setText("CP1 "+s22_CP1);

            toggleBtn.clearAnimation();
            //bulb_2.setCardBackgroundColor(Color.parseColor(AmberOFF));

        }
    }
    private void setP2Btn() {

        if(p2flag)
        {
            StringBuilder newCommandp3 = new StringBuilder(command);
            newCommandp3.setCharAt(2, '1');

            command = ""+newCommandp3;
            btn_p2onff.setCardBackgroundColor(Color.parseColor(green));
            txt_p2btnValue.setText("C2\n"+s23_CP2);

            // bulb_2ii.setCardBackgroundColor(Color.parseColor(AmberON));
            // bulb_2ii.startAnimation(anim2);
        }
        else
        {
            StringBuilder newCommandp3 = new StringBuilder(command);
            newCommandp3.setCharAt(2, '0');

            command = ""+newCommandp3;
            btn_p2onff.setCardBackgroundColor(Color.parseColor(green));
            txt_p2btnValue.setText("CP2 "+s22_CP2);
            txt_p2btnValue.clearAnimation();

            // anim2.cancel();

            //bulb_2ii.setCardBackgroundColor(Color.parseColor(AmberOFF));

        }
    }
    private void setP3Btn() {

        if(p3flag)
        {
            StringBuilder newCommandp3 = new StringBuilder(command);
            newCommandp3.setCharAt(3, '1');

            command = ""+newCommandp3;
            btn_p3onff.setCardBackgroundColor(Color.parseColor(green));
            txt_p3btnValue.setText("C3\n"+s23_CP3);

            //  bulb_2iii.setCardBackgroundColor(Color.parseColor(AmberON));
            // bulb_2iii.startAnimation(anim3);
        }
        else
        {
            StringBuilder newCommandp3 = new StringBuilder(command);
            newCommandp3.setCharAt(3, '0');

            command = ""+newCommandp3;
            btn_p3onff.setCardBackgroundColor(Color.parseColor(green));
            txt_p3btnValue.setText("CP3 "+s22_CP3);
            txt_p3btnValue.clearAnimation();

            //anim3.cancel();

            // bulb_2iii.setCardBackgroundColor(Color.parseColor(AmberOFF));

        }
    }
    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {

        return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
        //creates secure outgoing connecetion with BT device using UUID
    }



    @Override
    public void onPause()
    {
        super.onPause();
        try
        {
            //Don't leave Bluetooth sockets open when leaving activity
            btSocket.close();
        } catch (IOException e2) {
            //insert code to deal with this
        }
    }
    //Checks that the Android device Bluetooth is available and prompts to be turned on if off
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
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }


        public void run() {
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
        //write method
        public void write(String input) {
            byte[] msgBuffer = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(msgBuffer);                //write bytes over BT connection via outstream
            } catch (IOException e) {
                //if you cannot write, close the application
                // Toast.makeText(getBaseContext(), "Connection Failure", Toast.LENGTH_LONG).show();
                finish();

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
    public void langSet(){
        if (sPlanguageCP1.getlanguageCP1().equals("en")){
            s1_CP1 = LangString.s1_E;  s2_CP1 = LangString.s2_E;  s3_CP1 = LangString.s3_E;  s4_CP1 = LangString.s4_E;  s5_CP1 = LangString.s5_E;  s6_CP1 = LangString.s6_E;  s7_CP1 = LangString.s7_E;  s8_CP1 = LangString.s8_E;  s9_CP1 = LangString.s9_E;  s10_CP1 = LangString.s10_E;  s11_CP1 = LangString.s11_E;  s12_CP1 = LangString.s12_E; s13_CP1 = LangString.s13_E; s14_CP1 = LangString.s14_E; s15_CP1 = LangString.s15_E; s16_CP1 = LangString.s16_E; s17_CP1 = LangString.s17_E; s21_CP1 = LangString.s21_E; s22_CP1 = LangString.s22_E; s23_CP1 = LangString.s23_E;s24_CP1 = LangString.s24_E;
        }
        else if (sPlanguageCP1.getlanguageCP1().equals("ma")){
            s1_CP1 = LangString.s1_M;  s2_CP1 = LangString.s2_M;  s3_CP1 = LangString.s3_M;  s4_CP1 = LangString.s4_M;  s5_CP1 = LangString.s5_M;  s6_CP1 = LangString.s6_M;  s7_CP1 = LangString.s7_M;  s8_CP1 = LangString.s8_M;  s9_CP1 = LangString.s9_M;  s10_CP1 = LangString.s10_M; s11_CP1 = LangString.s11_M;  s12_CP1 = LangString.s12_M; s13_CP1 = LangString.s13_M; s14_CP1 = LangString.s14_M; s15_CP1 = LangString.s15_M; s16_CP1 = LangString.s16_M; s17_CP1 = LangString.s17_M; s21_CP1 = LangString.s21_M;  s22_CP1 = LangString.s22_M; s23_CP1 = LangString.s23_M;s24_CP1 = LangString.s24_M;
        }
        else if (sPlanguageCP1.getlanguageCP1().equals("hi")){
            s1_CP1 = LangString.s1_H;  s2_CP1 = LangString.s2_H;  s3_CP1 = LangString.s3_H;  s4_CP1 = LangString.s4_H;  s5_CP1 = LangString.s5_H;  s6_CP1 = LangString.s6_H;  s7_CP1 = LangString.s7_H;  s8_CP1 = LangString.s8_H;  s9_CP1 = LangString.s9_H;  s10_CP1 = LangString.s10_H;  s11_CP1 = LangString.s11_H;  s12_CP1 = LangString.s12_H; s13_CP1 = LangString.s13_H; s14_CP1 = LangString.s14_H; s15_CP1 = LangString.s15_H; s16_CP1 = LangString.s16_H; s17_CP1 = LangString.s17_H; s21_CP1 = LangString.s21_H; s22_CP1 = LangString.s22_H; s23_CP1 = LangString.s23_H;s24_CP1 = LangString.s24_H;
        }


        if (sPlanguageCP2.getlanguageCP2().equals("en")){
            s1_CP2 = LangString.s1_E;  s2_CP2 = LangString.s2_E;  s3_CP2 = LangString.s3_E;  s4_CP2 = LangString.s4_E;  s5_CP2 = LangString.s5_E;  s6_CP2 = LangString.s6_E;  s7_CP2 = LangString.s7_E;  s8_CP2 = LangString.s8_E;  s9_CP2 = LangString.s9_E;  s10_CP2 = LangString.s10_E;  s11_CP2 = LangString.s11_E;  s12_CP2 = LangString.s12_E; s13_CP2 = LangString.s13_E; s14_CP2 = LangString.s14_E; s15_CP2 = LangString.s15_E; s16_CP2 = LangString.s16_E; s17_CP2 = LangString.s17_E; s21_CP2 = LangString.s21_E; s22_CP2 = LangString.s22_E; s23_CP2 = LangString.s23_E;s24_CP2 = LangString.s24_E;

        }
        else if (sPlanguageCP2.getlanguageCP2().equals("ma")){
            s1_CP2 = LangString.s1_M;  s2_CP2 = LangString.s2_M;  s3_CP2 = LangString.s3_M;  s4_CP2 = LangString.s4_M;  s5_CP2 = LangString.s5_M;  s6_CP2 = LangString.s6_M;  s7_CP2 = LangString.s7_M;  s8_CP2 = LangString.s8_M;  s9_CP2 = LangString.s9_M;  s10_CP2 = LangString.s10_M; s11_CP2 = LangString.s11_M;  s12_CP2 = LangString.s12_M; s13_CP2 = LangString.s13_M; s14_CP2 = LangString.s14_M; s15_CP2 = LangString.s15_M; s16_CP2 = LangString.s16_M; s17_CP2 = LangString.s17_M; s21_CP2 = LangString.s21_M;  s22_CP2 = LangString.s22_M; s23_CP2 = LangString.s23_M;s24_CP2 = LangString.s24_M;

        }
        else if (sPlanguageCP2.getlanguageCP2().equals("hi")){
            s1_CP2 = LangString.s1_H;  s2_CP2 = LangString.s2_H;  s3_CP2 = LangString.s3_H;  s4_CP2 = LangString.s4_H;  s5_CP2 = LangString.s5_H;  s6_CP2 = LangString.s6_H;  s7_CP2 = LangString.s7_H;  s8_CP2 = LangString.s8_H;  s9_CP2 = LangString.s9_H;  s10_CP2 = LangString.s10_H;  s11_CP2 = LangString.s11_H;  s12_CP2 = LangString.s12_H; s13_CP2 = LangString.s13_H; s14_CP2 = LangString.s14_H; s15_CP2 = LangString.s15_H; s16_CP2 = LangString.s16_H; s17_CP2 = LangString.s17_H; s21_CP2 = LangString.s21_H; s22_CP2 = LangString.s22_H; s23_CP2 = LangString.s23_H;s24_CP2 = LangString.s24_H;

        }


        if (sPlanguageCP3.getlanguageCP3().equals("en")){
            s1_CP3 = LangString.s1_E;  s2_CP3 = LangString.s2_E;  s3_CP3 = LangString.s3_E;  s4_CP3 = LangString.s4_E;  s5_CP3 = LangString.s5_E;  s6_CP3 = LangString.s6_E;  s7_CP3 = LangString.s7_E;  s8_CP3 = LangString.s8_E;  s9_CP3 = LangString.s9_E;  s10_CP3 = LangString.s10_E;  s11_CP3 = LangString.s11_E;  s12_CP3 = LangString.s12_E; s13_CP3 = LangString.s13_E; s14_CP3 = LangString.s14_E; s15_CP3 = LangString.s15_E; s16_CP3 = LangString.s16_E; s17_CP3 = LangString.s17_E; s21_CP3 = LangString.s21_E; s22_CP3 = LangString.s22_E; s23_CP3 = LangString.s23_E;s24_CP3 = LangString.s24_E;
        }
        else if (sPlanguageCP3.getlanguageCP3().equals("ma")){
            s1_CP3 = LangString.s1_M;  s2_CP3 = LangString.s2_M;  s3_CP3 = LangString.s3_M;  s4_CP3 = LangString.s4_M;  s5_CP3 = LangString.s5_M;  s6_CP3 = LangString.s6_M;  s7_CP3 = LangString.s7_M;  s8_CP3 = LangString.s8_M;  s9_CP3 = LangString.s9_M;  s10_CP3 = LangString.s10_M; s11_CP3 = LangString.s11_M;  s12_CP3 = LangString.s12_M; s13_CP3 = LangString.s13_M; s14_CP3 = LangString.s14_M; s15_CP3 = LangString.s15_M; s16_CP3 = LangString.s16_M; s17_CP3 = LangString.s17_M; s21_CP3 = LangString.s21_M;  s22_CP3 = LangString.s22_M; s23_CP3 = LangString.s23_M;s24_CP3 = LangString.s24_M;
        }
        else if (sPlanguageCP3.getlanguageCP3().equals("hi")){
            s1_CP3 = LangString.s1_H;  s2_CP3 = LangString.s2_H;  s3_CP3 = LangString.s3_H;  s4_CP3 = LangString.s4_H;  s5_CP3 = LangString.s5_H;  s6_CP3 = LangString.s6_H;  s7_CP3 = LangString.s7_H;  s8_CP3 = LangString.s8_H;  s9_CP3 = LangString.s9_H;  s10_CP3 = LangString.s10_H;  s11_CP3 = LangString.s11_H;  s12_CP3 = LangString.s12_H; s13_CP3 = LangString.s13_H; s14_CP3 = LangString.s14_H; s15_CP3 = LangString.s15_H; s16_CP3 = LangString.s16_H; s17_CP3 = LangString.s17_H; s21_CP3 = LangString.s21_H; s22_CP3 = LangString.s22_H; s23_CP3 = LangString.s23_H;s24_CP3 = LangString.s24_H;
        }
        // s1_CP2 = "CHARGER IS ON";  s2_CP2 = "EMERGENCY STOPPED";  s3_CP2 = "OVER CURRENT";  s4_CP2 = "EARTH FAULT";  s5_CP2 = "TEMP HAZARD";  s6_CP2 = "POWER FAILURE";  s7_CP2 = "  VEHICLE\n PLUGED OUT";  s8_CP2 = "IDLE";  s9_CP2 = "  RESUMING\n  AFTER PF";  s10_CP2 = " CHARGING IN\n   PROCESS";  s11_CP2 = "  VEHICLE\n PLUGED IN";  s12_CP2 = "UNDER VOLTAGE"; s13_CP2 = "OVER VOLTAGE";
        //s1_CP3 = "CHARGER IS ON";  s2_CP3 = "EMERGENCY STOPPED";  s3_CP3 = "OVER CURRENT";  s4_CP3 = "EARTH FAULT";  s5_CP3 = "TEMP HAZARD";  s6_CP3 = "POWER FAILURE";  s7_CP3 = "  VEHICLE\n PLUGED OUT";  s8_CP3 = "IDLE";  s9_CP3 = "  RESUMING\n  AFTER PF";  s10_CP3 = " CHARGING IN\n   PROCESS";  s11_CP3 = "  VEHICLE\n PLUGED IN";  s12_CP3 = "UNDER VOLTAGE"; s13_CP3 = "OVER VOLTAGE";
    }

    private void customToast(String message)
    {
        if (toast!=null)
        {
            toast.cancel();
        }
        LayoutInflater inflater = getLayoutInflater();
        View toastLayout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.llCustom));
        TextView textView = toastLayout.findViewById(R.id.customToastMsg);
        textView.setText(message);
        toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(toastLayout);
        toast.show();
    }
}*/











    /*AppCompatActivity implements View.OnClickListener {

    CardView toggleBtn,btn_p2onff,btn_p3onff;

    TextView toggleValue,txt_p2btnValue,txt_p3btnValue;

    boolean flag=false,p2flag = false,p3flag= false;

    //---------- For Round Robin -------------//

    private int countC1,countC2,countC3;
    private boolean isAvailableC1,isAvailableC2,isAvailableC3;
    private boolean isStartC1,isStartC2,isStartC3;

    //---------- For Round Robin -------------//

    //String red="#D74C34";
    String red="#f21200";
    String green="#3DAA4C";


    static Animation anim,anim2,anim3;


    private FirebaseDatabase mFirebaseInstance;
    Handler bluetoothIn;

    final int handlerState = 0;        				 //used to identify handler message
    int intervalcount = 0;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder recDataString = new StringBuilder();

    private ConnectedThread mConnectedThread;


    // SPP UUID service - this should work for most devices
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // String for MAC address
    private static String address;

    String command= "$000";

    //-------------design Variables
    int cnt=0;
    LinearLayout connect1,connect2,connect3,parent1;
    TextView txt_c1round_off_mr, txt_c2round_off_mr, txt_c3round_off_mr; //unit consumption variables
    TextView txt_c1diff,txt_c2diff,txt_c3diff; //time difference
    TextView tv_status1,tv_status2,tv_status3; // status

    //------------------ new architecture 09-01-2018
    private SPisPluggedin sPisPluggedin;
    private SPisPoweFail sPisPoweFail;
    private SPmeterReadingCP1 sPmeterReadingCP1;
    private SPmeterReadingCP2 sPmeterReadingCP2;
    private SPmeterReadingCP3 sPmeterReadingCP3;

    //------------------
    private SPTimeReadingCP1 sPTimeReadingCP1;
    private SPTimeReadingCP2 sPTimeReadingCP2;
    private SPTimeReadingCP3 sPTimeReadingCP3;


    //-----------------------CP1
    private int plugedinCountcp1 = 0;
    private int plugedoutCountcp1 = 0;
    private boolean isResumedAftercp1 = false;
    private boolean isStillOnCP1 = false;


    //-----------------------CP2
    private int plugedinCountcp2 = 0;
    private int plugedoutCountcp2 = 0;
    private boolean isResumedAftercp2 = false;
    private boolean isStillOnCP2 = false;

    //-----------------------CP3
    private int plugedinCountcp3 = 0;
    private int plugedoutCountcp3 = 0;
    private boolean isResumedAftercp3 = false;
    private boolean isStillOnCP3 = false;
    //time______________________CP1
    long MillisCP1 , DifferenceCP1 , NewBeginMillsCP1 , StartTimeCP1 = 0L ;
    int HoursCP1, MinutesCP1, SecondsCP1 ;
    //time______________________CP2
    long MillisCP2 , DifferenceCP2 , NewBeginMillsCP2 , StartTimeCP2 = 0L ;
    int HoursCP2, MinutesCP2, SecondsCP2 ;
    //time______________________CP3
    long MillisCP3 , DifferenceCP3 , NewBeginMillsCP3 , StartTimeCP3 = 0L ;
    int HoursCP3, MinutesCP3, SecondsCP3 ;

    //----------- optimize______20-01-2018
    LinearLayout layout_detail;
    FrameLayout layout_main;
    ImageView imageView_close;
    TextView txt_cpname,txt_rate,txt_voltage_detail,txt_current_detail,txt_power_detail,txt_unit_detail,txt_etime_detail,txt_status_detail;
    String voltage_detailCP1="000",current_detailCP1="00.00",power_detailCP1="000",unit_detailCP1="000", etime_detailCP1="00:00",status_detailCP1="IDLE";
    String voltage_detailCP2="000",current_detailCP2="00.00",power_detailCP2="000",unit_detailCP2="000", etime_detailCP2="00:00",status_detailCP2="IDLE";
    String voltage_detailCP3="000",current_detailCP3="00.00",power_detailCP3="000",unit_detailCP3="000", etime_detailCP3="00:00",status_detailCP3="IDLE";

    //----------
    String detail_flag = "";
    //--- for Rs
    String rsCP1 = "₹ 00.00", rsCP2 = "₹ 00.00", rsCP3 = "₹ 00.00",rsPOCP1 = "₹ 00.00", rsPOCP2 = "₹ 00.00", rsPOCP3 = "₹ 00.00";
    //---for overlay________
    RelativeLayout relativeLayout1st,relativeLayout2nd,relativeLayout3rd;
    int overlayCounti = 0,overlayCountii = 0,overlayCountiii = 0;


    //language Selection
    private String s1_CP1,s2_CP1,s3_CP1,s4_CP1,s5_CP1,s6_CP1,s7_CP1,s8_CP1,s9_CP1,s10_CP1,s11_CP1,s12_CP1,s13_CP1,s14_CP1,s15_CP1,s16_CP1,s17_CP1,s18_CP1,s19_CP1,s20_CP1,s21_CP1,s22_CP1,s23_CP1;
    private String s1_CP2,s2_CP2,s3_CP2,s4_CP2,s5_CP2,s6_CP2,s7_CP2,s8_CP2,s9_CP2,s10_CP2,s11_CP2,s12_CP2,s13_CP2,s14_CP2,s15_CP2,s16_CP2,s17_CP2,s18_CP2,s19_CP2,s20_CP2,s21_CP2,s22_CP2,s23_CP2;
    private String s1_CP3,s2_CP3,s3_CP3,s4_CP3,s5_CP3,s6_CP3,s7_CP3,s8_CP3,s9_CP3,s10_CP3,s11_CP3,s12_CP3,s13_CP3,s14_CP3,s15_CP3,s16_CP3,s17_CP3,s18_CP3,s19_CP3,s20_CP3,s21_CP3,s22_CP3,s23_CP3;
    private TextView txt_touch1,txt_touch2,txt_touch3,txt_etime_display1,txt_etime_display2,txt_etime_display3,txt_etime_displayf,txt_unit_display,txt_rate_display;

    private SPlanguageCP1 sPlanguageCP1;
    private SPlanguageCP2 sPlanguageCP2;
    private SPlanguageCP3 sPlanguageCP3;
    private SharedPreferenceUnitR sharedPreference;

    private RadioGroup rg_language_m;

    public boolean isAlreadyonm = true;
    Button c1,c2,c3,c4;
    private File myExternalFile;
    String filename = "Config.txt";
    String filepath = "MyFileStorage";
    String cid;
    String hname="sour";
    Float erate;
    String erate_s;
    int clickCount = 0;
    //------------
    private int countidleCP1 = 0,countidleCP2 = 0,countidleCP3 = 0;
    private RelativeLayout layout_goback ;


    private int displayPlugoutCountC1 = 0;
    private int displayPlugoutCountC2 = 0;
    private int displayPlugoutCountC3 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        setContentView(R.layout.newdesignm);
        Log.e("CHECK", "CHECKING");

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




    @Override
    protected void onRestart() {
        super.onRestart();
        // Toast.makeText(MaindisplayActivityMannual.this,"onRestart Method",Toast.LENGTH_LONG).show();
        Log.e("METHOD","onRestart Method");
    }

    @Override
    protected void onStart() {
        super.onStart();
        //  Toast.makeText(MaindisplayActivityMannual.this,"onStart Method",Toast.LENGTH_LONG).show();
        Log.e("METHOD","onStart Method");
    }

    @Override
    public void onResume() {
        super.onResume();

        countC1 = 0 ;
        countC2 = 0 ;
        countC3 = 0 ;

        isAvailableC1 = true;
        isAvailableC2 = true;
        isAvailableC3 = true;



        c1 = findViewById(R.id.checkBox);
        c2 = findViewById(R.id.checkBox2);
        c3 = findViewById(R.id.checkBox3);
        c4 = findViewById(R.id.checkBox4);
        layout_goback =  findViewById(R.id.layout_goback);

        GetCid();
        Firebase.setAndroidContext(this);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickCount==3) {
                    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (mBluetoothAdapter.isEnabled()) {
                        mBluetoothAdapter.disable();
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(MaindisplayActivityMannual.this, Authentication.class);
                            String C_ID = cid.toString();

//Create the bundle
                            Bundle bundle = new Bundle();

//Add your data to bundle
                            bundle.putString("Cid", C_ID);

//Add the bundle to the intent
                            i.putExtras(bundle);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

//Fire that second activity
                            startActivity(i);
                            clickCount=0;

                        }
                    }, 3000);
                }
                else {
                    clickCount++;
                }
            }
        });
        sPlanguageCP1 = new SPlanguageCP1(MaindisplayActivityMannual.this);
        sPlanguageCP2 = new SPlanguageCP2(MaindisplayActivityMannual.this);
        sPlanguageCP3 = new SPlanguageCP3(MaindisplayActivityMannual.this);
        sharedPreference = new SharedPreferenceUnitR();
        if (sharedPreference.getValue(MaindisplayActivityMannual.this)== null){
            sharedPreference.save(MaindisplayActivityMannual.this,"7");
        }
        erate_s = sharedPreference.getValue(MaindisplayActivityMannual.this);
        erate = Float.parseFloat(erate_s);
        langSet();
        //  Toast.makeText(MaindisplayActivityMannual.this,"onResume Method",Toast.LENGTH_LONG).show();
        Log.e("METHOD","onResume Method");
        sPisPluggedin = new SPisPluggedin(MaindisplayActivityMannual.this); // initiating sharedepreferences
        sPisPoweFail = new SPisPoweFail(MaindisplayActivityMannual.this); // initiating sharedepreferences
        sPmeterReadingCP1 = new SPmeterReadingCP1(MaindisplayActivityMannual.this); // initiating sharedepreferences
        sPmeterReadingCP2 = new SPmeterReadingCP2(MaindisplayActivityMannual.this); // initiating sharedepreferences
        sPmeterReadingCP3 = new SPmeterReadingCP3(MaindisplayActivityMannual.this); // initiating sharedepreferences
        sPTimeReadingCP1 = new SPTimeReadingCP1(MaindisplayActivityMannual.this); // initiating sharedepreferences
        sPTimeReadingCP2 = new SPTimeReadingCP2(MaindisplayActivityMannual.this); // initiating sharedepreferences
        sPTimeReadingCP3 = new SPTimeReadingCP3(MaindisplayActivityMannual.this); // initiating sharedepreferences
        // Toast.makeText(MaindisplayActivityMannual.this,"Before : "+sPisPluggedin.getisPluggedinCP1(),Toast.LENGTH_LONG).show();
        // Toast.makeText(MaindisplayActivityMannual.this,"Before : "+sPisPluggedin.getisPluggedinCP2(),Toast.LENGTH_LONG).show();
        // Toast.makeText(MaindisplayActivityMannual.this,"Before : "+sPisPluggedin.getisPluggedinCP2(),Toast.LENGTH_LONG).show();


        if(sPisPluggedin.getisPluggedinCP1().isEmpty()){
            sPisPluggedin.setisPluggedinCP1("f");
            // Toast.makeText(MaindisplayActivityMannual.this,"sPisPluggedin.getisPluggedinCP1() "+sPisPluggedin.getisPluggedinCP1(),Toast.LENGTH_LONG).show();
        }
        // Toast.makeText(MaindisplayActivityMannual.this,"Before : "+sPisPoweFail.getisPowerFailCP1(),Toast.LENGTH_LONG).show();

        if(sPisPoweFail.getisPowerFailCP1().isEmpty()){
            sPisPoweFail.setisPowerFailCP1("f");
            // Toast.makeText(MaindisplayActivityMannual.this,"sPisPoweFail.getisPowerFailCP1() "+sPisPoweFail.getisPowerFailCP1(),Toast.LENGTH_LONG).show();

        }
        //----------------------------
        if(sPisPluggedin.getisPluggedinCP2().isEmpty()){
            sPisPluggedin.setisPluggedinCP2("f");
            // Toast.makeText(MaindisplayActivityMannual.this,"getisPluggedinCP2() "+sPisPluggedin.getisPluggedinCP2(),Toast.LENGTH_LONG).show();
        }
        // Toast.makeText(MaindisplayActivityMannual.this,"Before 2: "+sPisPoweFail.getisPowerFailCP2(),Toast.LENGTH_LONG).show();

        if(sPisPoweFail.getisPowerFailCP2().isEmpty()){
            sPisPoweFail.setisPowerFailCP2("f");
            // Toast.makeText(MaindisplayActivityMannual.this,"sPisPoweFail.getisPowerFailCP1() "+sPisPoweFail.getisPowerFailCP2(),Toast.LENGTH_LONG).show();

        }

        //----------------------------
        if(sPisPluggedin.getisPluggedinCP3().isEmpty()){
            sPisPluggedin.setisPluggedinCP3("f");
            //Toast.makeText(MaindisplayActivityMannual.this,"getisPluggedinCP3() "+sPisPluggedin.getisPluggedinCP3(),Toast.LENGTH_LONG).show();
        }
        // Toast.makeText(MaindisplayActivityMannual.this,"Before 3: "+sPisPoweFail.getisPowerFailCP3(),Toast.LENGTH_LONG).show();

        if(sPisPoweFail.getisPowerFailCP3().isEmpty()){
            sPisPoweFail.setisPowerFailCP3("f");
            // Toast.makeText(MaindisplayActivityMannual.this,"isPowerFailCP3() "+sPisPoweFail.getisPowerFailCP3(),Toast.LENGTH_LONG).show();

        }
        if (sPmeterReadingCP1.getMeterReadingCP1().isEmpty()){
            sPmeterReadingCP1.setMeterReadingCP1("00.00");
        }
        if (sPmeterReadingCP2.getMeterReadingCP2().isEmpty()){
            sPmeterReadingCP2.setMeterReadingCP2("00.00");
        }
        if (sPmeterReadingCP3.getMeterReadingCP3().isEmpty()){
            sPmeterReadingCP3.setMeterReadingCP3("00.00");
        }


        toggleBtn   =     findViewById(R.id.toggleBtn);
        btn_p2onff   =    findViewById(R.id.btn_p2onff);
        btn_p3onff   =    findViewById(R.id.btn_p3onff);

        toggleValue =     findViewById(R.id.toggleValue);
        txt_p2btnValue =  findViewById(R.id.txt_p2btnValue);
        txt_p3btnValue =  findViewById(R.id.txt_p3btnValue);



        //----------------------------------------------------------
        connect1=(LinearLayout)findViewById(R.id.ll_connect1);
        connect2=(LinearLayout)findViewById(R.id.ll_connect2);
        connect3=(LinearLayout)findViewById(R.id.ll_connect3);

        parent1=(LinearLayout)findViewById(R.id.ll_parent1);


        parent1.animate().translationY(0);

        //-------------------------------------------------------------
        /*//******** variables
 txt_c1round_off_mr = findViewById(R.id.tv_unit1);
 txt_c2round_off_mr = findViewById(R.id.tv_unit2);
 txt_c3round_off_mr = findViewById(R.id.tv_unit3);
 //--------------------------
 txt_c1diff = findViewById(R.id.tv_time1);
 txt_c2diff = findViewById(R.id.tv_time2);
 txt_c3diff = findViewById(R.id.tv_time3);
 //----------------------
 tv_status1 = findViewById(R.id.tv_status1);
 tv_status2 = findViewById(R.id.tv_status2);
 tv_status3 = findViewById(R.id.tv_status3);

 //for optimise
 layout_detail = findViewById(R.id.layout_detail);
 layout_main = findViewById(R.id.layout_main);
 imageView_close = findViewById(R.id.imageView_close);
 txt_cpname = findViewById(R.id.txt_cpname);
 txt_rate = findViewById(R.id.txt_rate);
 txt_voltage_detail = findViewById(R.id.txt_voltage_detail);
 txt_current_detail = findViewById(R.id.txt_current_detail);
 txt_power_detail = findViewById(R.id.txt_power_detail);
 txt_unit_detail = findViewById(R.id.txt_unit_detail);
 txt_etime_detail = findViewById(R.id.txt_etime_detail);
 txt_status_detail = findViewById(R.id.txt_status_detail);
 layout_main.setVisibility(View.VISIBLE);
 layout_detail.setVisibility(View.GONE);
 //touch...
 txt_touch1 = findViewById(R.id.txt_touch1);
 txt_touch2 = findViewById(R.id.txt_touch2);
 txt_touch3 = findViewById(R.id.txt_touch3);
 txt_etime_display1 = findViewById(R.id.txt_etime_display1);
 txt_etime_display2 = findViewById(R.id.txt_etime_display2);
 txt_etime_display3 = findViewById(R.id.txt_etime_display3);
 txt_etime_displayf = findViewById(R.id.txt_etime_displayf);
 txt_unit_display = findViewById(R.id.txt_unit_display);
 txt_rate_display = findViewById(R.id.txt_rate_display);

 //----------for Overlay28-1-18
 relativeLayout1st =  findViewById(R.id.connectionLayout1st);
 relativeLayout2nd =  findViewById(R.id.connectionLayout2nd);
 relativeLayout3rd =  findViewById(R.id.connectionLayout3rd);

 //-------------lang 30-1-18
 rg_language_m = findViewById(R.id.rg_language_m);

 rg_language_m.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
@Override
public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
switch(checkedId)
{
case R.id.rb_en_m:
// TODO Something
if (detail_flag.equals("d_CP1")) {
sPlanguageCP1.setlanguageCP1("en");
} else if (detail_flag.equals("d_CP2")) {
sPlanguageCP2.setlanguageCP2("en");

} else if (detail_flag.equals("d_CP3")) {
sPlanguageCP3.setlanguageCP3("en");

}

break;
case R.id.rb_hi_m:
// TODO Something
if (detail_flag.equals("d_CP1")) {
sPlanguageCP1.setlanguageCP1("hi");
} else if (detail_flag.equals("d_CP2")) {
sPlanguageCP2.setlanguageCP2("hi");

} else if (detail_flag.equals("d_CP3")) {
sPlanguageCP3.setlanguageCP3("hi");

}
break;
case R.id.rb_ma_m:
// TODO Something
if (detail_flag.equals("d_CP1")) {
sPlanguageCP1.setlanguageCP1("ma");
} else if (detail_flag.equals("d_CP2")) {
sPlanguageCP2.setlanguageCP2("ma");
} else if (detail_flag.equals("d_CP3")) {
sPlanguageCP3.setlanguageCP3("ma");
}
break;
}
}
});

 relativeLayout1st.setOnClickListener(new View.OnClickListener()
 {
 @Override
 public void onClick(View view) {

 if (countC1 == 0 && countC2 == 0 && countC3 == 0)
 {
 funConnectori();
 overlayCounti = 0;
 relativeLayout1st.setVisibility(View.GONE);
 }
 else if (countC1 > countC2 && countC1 > countC3) {
 if (isAvailableC2) {
 customToast("Please Use C2...!!!");
 } else if (isAvailableC3) {
 customToast("Please Use C3...!!!");
 } else {
 Toast.makeText(getApplicationContext(), "Not Available C2 Or C3...!!!", Toast.LENGTH_SHORT).show();
 countC1 = 0;
 funConnectori();
 overlayCounti = 0;
 relativeLayout1st.setVisibility(View.GONE);
 }
 }
 else if (countC1 < countC2 && countC1 < countC3)
 {
 funConnectori();
 overlayCounti = 0;
 relativeLayout1st.setVisibility(View.GONE);
 }
 else if (countC2 > countC1  && countC2 > countC3)
 {
 funConnectori();
 overlayCounti = 0;
 relativeLayout1st.setVisibility(View.GONE);
 }
 else if (countC3 > countC1 && countC3 > countC2)
 {
 funConnectori();
 overlayCounti = 0;
 relativeLayout1st.setVisibility(View.GONE);
 }
 else if (countC2 < countC1 && countC2 < countC3)
 {
 if (isAvailableC2)
 {
 customToast("Please Use C2...!!!");
 }
 }
 else if (countC3 < countC1 && countC3 < countC2)
 {
 if (isAvailableC3)
 {
 customToast("Please Use C3...!!!");
 }
 }

 if (countC1 == 1 && countC2 == 1 && countC3 == 1)
 {
 countC1 = 0;
 countC2 = 0;
 countC3 = 0;
 }

 if (countC1 == countC2 && countC2 == countC3 )
 {
 countC1 = 0;
 countC2 = 0;
 countC3 = 0;
 }

 }

 });


 relativeLayout2nd.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {

if (countC1 == 0 && countC2 == 0 && countC3 == 0)
{
funConnectorii();
overlayCountii = 0;
relativeLayout2nd.setVisibility(View.GONE);
}
else if (countC2 > countC1 && countC2 > countC3)
{
if (isAvailableC1)
{
customToast("Please Use C1...!!!");
}
else if (isAvailableC3)
{
customToast("Please Use C3...!!!");
}
else
{
Toast.makeText(getApplicationContext(),"Not Available C1 Or C3...!!!",Toast.LENGTH_SHORT).show();
countC2 = 0;
funConnectorii();
overlayCountii = 0;
relativeLayout2nd.setVisibility(View.GONE);
}
}
else if (countC2 < countC1 && countC2 < countC3)
{
funConnectorii();
overlayCountii = 0;
relativeLayout2nd.setVisibility(View.GONE);
}
else if (countC1 > countC2  && countC1 > countC3)
{
funConnectorii();
overlayCountii = 0;
relativeLayout2nd.setVisibility(View.GONE);
}
else if (countC3 > countC1 && countC3 > countC2)
{
funConnectorii();
overlayCountii = 0;
relativeLayout2nd.setVisibility(View.GONE);
}
else if (countC1 < countC2 && countC1 < countC3)
{
if (isAvailableC1)
{
customToast("Please Use C1...!!!");
}
}
else if (countC3 < countC1 && countC3 < countC2)
{
if (isAvailableC3)
{
customToast("Please Use C3...!!!");
}
}

if (countC1 == 1 && countC2 == 1 && countC3 == 1)
{
countC1 = 0;
countC2 = 0;
countC3 = 0;
}
if (countC1 == countC2 && countC2 == countC3 )
{
countC1 = 0;
countC2 = 0;
countC3 = 0;

}
}
});


 relativeLayout3rd.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {

if (countC1 == 0 && countC2 == 0 && countC3 == 0) {
funConnectoriii();
overlayCountiii = 0;
relativeLayout3rd.setVisibility(View.GONE);
} else if (countC3 > countC1 && countC3 > countC2) {
if (isAvailableC1) {
customToast("Please Use C1...!!!");
} else if (isAvailableC2) {
customToast("Please Use C2...!!!");
} else {
Toast.makeText(getApplicationContext(), " Not Available C1 Or C2...!!!", Toast.LENGTH_SHORT).show();
countC3 = 0;
funConnectoriii();
overlayCountiii = 0;
relativeLayout3rd.setVisibility(View.GONE);
}
} else if (countC3 < countC1 && countC3 < countC2) {
funConnectoriii();
overlayCountiii = 0;
relativeLayout3rd.setVisibility(View.GONE);
} else if (countC1 > countC2 && countC1 > countC3) {
funConnectoriii();
overlayCountiii = 0;
relativeLayout3rd.setVisibility(View.GONE);
}
else if (countC2 > countC1 && countC2 > countC3)
{
funConnectoriii();
overlayCountiii = 0;
relativeLayout3rd.setVisibility(View.GONE);
}
else if (countC1 < countC2 && countC1 < countC3)
{
if (isAvailableC1)
{
customToast("Please Use C1 ...!!!");
}

}
else if (countC2 < countC1 && countC2 < countC3)
{
if (isAvailableC2)
{
customToast("Please Use C3 ...!!!");
}
}

if (countC1 == 1 && countC2 == 1 && countC3 == 1) {
countC1 = 0;
countC2 = 0;
countC3 = 0;

}
if (countC1 == countC2 && countC2 == countC3) {
countC1 = 0;
countC2 = 0;
countC3 = 0;
}
}
});

 imageView_close.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
layout_main.setVisibility(View.VISIBLE);
layout_detail.setVisibility(View.GONE);
detail_flag = "c";

}
});

 connect1.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
layout_main.setVisibility(View.GONE);
layout_detail.setVisibility(View.VISIBLE);
detail_flag = "d_CP1";
}
});
 connect2.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
layout_main.setVisibility(View.GONE);
layout_detail.setVisibility(View.VISIBLE);
detail_flag = "d_CP2";
}
});
 connect3.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
layout_main.setVisibility(View.GONE);
layout_detail.setVisibility(View.VISIBLE);
detail_flag = "d_CP3";
}
});
 layout_goback.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
Intent i = new Intent(MaindisplayActivityMannual.this, LangSelection.class);
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

 final String s = "#234990.00+~";

 anim = new AlphaAnimation(0.1f, 1.0f);
 anim.setDuration(300);
 anim.setRepeatMode(Animation.RESTART);
 anim.setRepeatCount(Animation.INFINITE);
 anim2 = new AlphaAnimation(0.1f, 1.0f);
 anim2.setDuration(300);
 anim2.setRepeatMode(Animation.RESTART);
 anim2.setRepeatCount(Animation.INFINITE);
 anim3 = new AlphaAnimation(0.1f, 1.0f);
 anim3.setDuration(300);
 anim3.setRepeatMode(Animation.RESTART);
 anim3.setRepeatCount(Animation.INFINITE);


 //
 setToggleBtn();
 new Handler().postDelayed(new Runnable() {
@Override
public void run() {
mConnectedThread.write("c");
//  Toast.makeText(MaindisplayActivityMannual.this,"Thread",Toast.LENGTH_LONG).show();

Log.e("mConnectedThreadSTRING","c : "+intervalcount);
intervalcount++;
}
}, 5000);

 bluetoothIn = new Handler() {
 public void handleMessage(android.os.Message msg) {

 if (msg.what == handlerState) {                                        //if message is what we want
 isAlreadyonm = true;
 if (Power.isConnected(MaindisplayActivityMannual.this)) {
 String readMessage = (String) msg.obj;                                                                // msg.arg1 = bytes from connect thread
 recDataString.append(readMessage);                                    //keep appending to string until ~
 int endOfLineIndex = recDataString.indexOf("~");                    // determine the end-of-line
 //if (endOfLineIndex > 0 && endOfLineIndex < 80) {
 if (endOfLineIndex > 0) {
 langSet();
 txt_touch1.setText(s21_CP1);
 txt_touch2.setText(s21_CP2);
 txt_touch3.setText(s21_CP3);
 txt_etime_display1.setText(s14_CP1);
 txt_etime_display2.setText(s14_CP2);
 txt_etime_display3.setText(s14_CP3);
 // make sure there data before ~
 String dataInPrint = recDataString.substring(0, endOfLineIndex);    // extract string
 // txtString.setText("Data Received = " + dataInPrint);
 int dataLength = dataInPrint.length();                            //get length of data received
 //txtStringLength.setText("String Length = " + String.valueOf(dataLength));
 if (recDataString.charAt(0) == '#')                                //if it starts with # we know it is what we are looking for
 {
 try {
 if(countidleCP1 > 200 && countidleCP2 > 200 && countidleCP3 > 200){
 layout_detail.setVisibility(View.GONE);
 layout_main.setVisibility(View.GONE);
 layout_goback.setVisibility(View.VISIBLE);
 }
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
 voltage_detailCP1 = p1voltage;
 current_detailCP1 = p1current;
 power_detailCP1 = p1meter;
 voltage_detailCP2 = p2voltage;
 current_detailCP2 = p2current;
 power_detailCP2 = p2meter;
 voltage_detailCP3 = p3voltage;
 current_detailCP3 = p3current;
 power_detailCP3 = p3meter;
 mFirebaseInstance.getReference(cid+"-CP1M").setValue(tv_status1.getText().toString());
 mFirebaseInstance.getReference(cid+"-CP2M").setValue(tv_status2.getText().toString());
 mFirebaseInstance.getReference(cid+"-CP3M").setValue(tv_status3.getText().toString());
 mFirebaseInstance.getReference(cid+"-CP1-VoltageM").setValue(p1voltage);
 mFirebaseInstance.getReference(cid+"-CP2-VoltageM").setValue(p2voltage);
 mFirebaseInstance.getReference(cid+"-CP3-VoltageM").setValue(p3voltage);
 mFirebaseInstance.getReference(cid+"-CP1-CurrentM").setValue(p1current);
 mFirebaseInstance.getReference(cid+"-CP2-CurrentM").setValue(p2current);
 mFirebaseInstance.getReference(cid+"-CP3-CurrentM").setValue(p3current);
 mFirebaseInstance.getReference(cid+"-CP1-PowerM").setValue(p1meter);
 mFirebaseInstance.getReference(cid+"-CP2-PowerM").setValue(p2meter);
 mFirebaseInstance.getReference(cid+"-CP3-PowerM").setValue(p3meter);

 mFirebaseInstance.getReference(cid+"-CP1-AmountM").setValue(txt_c1round_off_mr.getText().toString());
 mFirebaseInstance.getReference(cid+"-CP2-AmountM").setValue(txt_c2round_off_mr.getText().toString());
 mFirebaseInstance.getReference(cid+"-CP3-AmountM").setValue(txt_c3round_off_mr.getText().toString());


 //   txt_c1round_off_mr

 if (p1OnOff.equals("0")) {
 if (sPisPoweFail.getisPowerFailCP1().equals("t") && sPisPluggedin.getisPluggedinCP1().equals("t")) {
 toggleBtn.performClick();
 } else {
 flag = false;
 setToggleBtn();
 }
 } else {
 flag = true;
 setToggleBtn();
 }

 if (p2OnOff.equals("0")) {

 if (sPisPoweFail.getisPowerFailCP2().equals("t") && sPisPluggedin.getisPluggedinCP2().equals("t")) {
 btn_p2onff.performClick();
 } else {
 p2flag = false;
 setP2Btn();
 }

 } else {
 p2flag = true;
 setP2Btn();
 }

 if (p3OnOff.equals("0")) {
 if (sPisPoweFail.getisPowerFailCP3().equals("t") && sPisPluggedin.getisPluggedinCP3().equals("t")) {
 btn_p3onff.performClick();
 } else {
 p3flag = false;
 setP3Btn();
 }
 } else {
 p3flag = true;
 setP3Btn();
 }
 if (emergency.equals("1"))
 {
 tv_status1.setText(s2_CP1);
 tv_status2.setText(s2_CP2);
 tv_status3.setText(s2_CP2);
 status_detailCP1 = s2_CP1;
 status_detailCP2 = s2_CP2;
 status_detailCP3 = s2_CP3;

 } else {

 //---------------------------------------------------------------------------------------------------------

 if (p1OnOff.equals("0") && p1plugin.equals("0") && p1fault.equals("03")) {
 tv_status1.setText(s3_CP1);
 status_detailCP1 = s3_CP1;
 }
 if (p2OnOff.equals("0") && p2plugin.equals("0") && p2fault.equals("03")) {
 tv_status2.setText(s3_CP2);
 status_detailCP2 = s3_CP2;
 }
 if (p3OnOff.equals("0") && p3plugin.equals("0") && p3fault.equals("03")) {
 tv_status3.setText(s3_CP3);
 status_detailCP3 = s3_CP3;
 }
 //fault--------------------------------------------------------------------
 if (p1fault.equals("05")) {

 if (sPisPluggedin.getisPluggedinCP1().equals("t")) {
 sPisPoweFail.setisPowerFailCP1("t");
 sPTimeReadingCP1.setTimeReadingCP1(MillisCP1);
 isStillOnCP1 = true;
 }
 Float cp1v = Float.parseFloat(p1voltage);
 if (cp1v > 263)
 {
 tv_status1.setText(s13_CP1);
 status_detailCP1 = s13_CP1;
 }
 else if (cp1v < 186 && cp1v > 150)
 {
 tv_status1.setText(s12_CP1);
 status_detailCP1 = s12_CP1;
 }
 else
 {
 tv_status1.setText(s6_CP1);
 status_detailCP1 = s6_CP1;
 toggleBtn.setCardBackgroundColor(Color.parseColor(red));
 toggleBtn.startAnimation(anim);
 }
 //}

 }
 if (p2fault.equals("05")) {
 if (sPisPluggedin.getisPluggedinCP2().equals("t")) {
 sPisPoweFail.setisPowerFailCP2("t");
 sPTimeReadingCP2.setTimeReadingCP2(MillisCP2);
 isStillOnCP2 = true;
 }
 Float cp2v = Float.parseFloat(p2voltage);
 if (cp2v > 263) {
 tv_status2.setText(s13_CP2);
 status_detailCP2 = s13_CP2;

 } else if (cp2v < 186 && cp2v > 150) {
 tv_status2.setText(s12_CP2);
 status_detailCP2 = s12_CP2;


 } else {
 tv_status2.setText(s6_CP2);
 status_detailCP2 = s6_CP2;
 btn_p2onff.setCardBackgroundColor(Color.parseColor(red));
 btn_p2onff.startAnimation(anim);

 }


 }
 if (p3fault.equals("05")) {

 if (sPisPluggedin.getisPluggedinCP3().equals("t")) {
 sPisPoweFail.setisPowerFailCP3("t");
 sPTimeReadingCP3.setTimeReadingCP3(MillisCP3);
 isStillOnCP3 = true;
 }
 Float cp3v = Float.parseFloat(p3voltage);
 if (cp3v > 263) {
 tv_status3.setText(s13_CP3);
 status_detailCP3 = s13_CP3;

 } else if (cp3v < 186 && cp3v > 150) {
 tv_status3.setText(s12_CP3);
 status_detailCP3 = s12_CP3;

 } else {
 tv_status3.setText(s6_CP3);
 status_detailCP3 = s6_CP3;
 btn_p3onff.setCardBackgroundColor(Color.parseColor(red));
 btn_p3onff.startAnimation(anim);

 }

 }
 //--------------Earth Fault
 if (p1fault.equals("10")) {
 tv_status1.setText(s4_CP1);
 status_detailCP1 = s4_CP1;

 }
 if (p2fault.equals("10")) {
 tv_status2.setText(s4_CP2);
 status_detailCP2 = s4_CP2;

 }
 if (p3fault.equals("10")) {
 tv_status3.setText(s4_CP3);
 status_detailCP3 = s4_CP3;

 }
 //---------------TEMP HAZARD
 if (p1fault.equals("07")) {
 tv_status1.setText(s5_CP1);
 status_detailCP1 = s5_CP1;


 }
 if (p2fault.equals("07")) {
 tv_status2.setText(s5_CP2);
 status_detailCP2 = s5_CP2;

 }
 if (p3fault.equals("07")) {
 tv_status3.setText(s5_CP3);
 status_detailCP3 = s5_CP3;

 }


 //-----------------------------------------------------------CP1 Status-----------------------------------------------------------------------------------------------------------------------------
 if (p1OnOff.equals("0") && p1plugin.equals("0") && p1fault.equals("99")) {

 if (sPisPluggedin.getisPluggedinCP1().equals("t")) {

 if (plugedoutCountcp1 >= 30) {
 sPisPluggedin.setisPluggedinCP1("f");

 } else {
 displayPlugoutCountC1 = 0;
 tv_status1.setText(s7_CP1);
 txt_c1round_off_mr.setText(rsPOCP1);
 rsCP1 = "₹ 00.00";
 txt_c1diff.setText(etime_detailCP1);

 status_detailCP1 = s7_CP1;
 unit_detailCP1 = unit_detailCP1;
 etime_detailCP1 = etime_detailCP1;

 plugedinCountcp1 = 0;
 plugedoutCountcp1++;
 if (isResumedAftercp1) {
 sPisPoweFail.setisPowerFailCP1("f");
 }
 }

 } else {
 countidleCP1++;
 tv_status1.setText(s8_CP1);
 //countC1 = 0;
 //  Toast.makeText(getApplicationContext(),"C1 Is Available",Toast.LENGTH_SHORT).show();
 isAvailableC1 = true;
 isStartC1 = true;

 txt_c1round_off_mr.setText("₹ 00.00");
 rsCP1 = "₹ 00.00";
 txt_c1diff.setText("00:00:00");
 status_detailCP1 = s8_CP1;
 unit_detailCP1 = "00.00";
 etime_detailCP1 = "00:00:00";
 plugedinCountcp1 = 0;
 toggleBtn.clearAnimation();
 //overlay1
 if (overlayCounti> 10) {
 relativeLayout1st.setVisibility(View.VISIBLE);
 }
 overlayCounti++;
 }

 }
 if (p1OnOff.equals("1") && p1plugin.equals("0") && p1fault.equals("99")) {
 countidleCP1 = 0;
 plugedinCountcp1 = 0;
 plugedoutCountcp1 = 0;
 tv_status1.setText(s1_CP1);
 if (isStartC1)
 {
 isStartC1 = false;
 countC1++;
 }
 isAvailableC1 = false;
 txt_c1round_off_mr.setText("₹ 00.00");
 rsCP1 = "₹ 00.00";
 txt_c1diff.setText("00:00:00");
 status_detailCP1 = s1_CP1;
 unit_detailCP1 = "00.00";
 etime_detailCP1 = "00:00:00";
 isStillOnCP1 = false;
 relativeLayout1st.setVisibility(View.GONE);

 if (displayPlugoutCountC1 == 8) {
 if (sPisPoweFail.getisPowerFailCP1().equals("t") && sPisPluggedin.getisPluggedinCP1().equals("t")) {
 toggleBtn.performClick();
 sPisPoweFail.setisPowerFailCP1("f");
 sPisPluggedin.setisPluggedinCP1("f");
 tv_status1.setText(s7_CP1);
 status_detailCP1 = s7_CP1;
 }
 }
 displayPlugoutCountC1++;
 }
 if (p1OnOff.equals("1") && p1plugin.equals("1") && p1fault.equals("99")) {
 countidleCP1 = 0;
 if (sPisPoweFail.getisPowerFailCP1().equals("t")) {
 //---------Status:resuming after power fail_______________
 if (plugedinCountcp1 >= 10) {
 //time reading______
 MillisCP1 = (SystemClock.uptimeMillis() - StartTimeCP1);

 HoursCP1 = (int) (MillisCP1 / (1000 * 60 * 60));
 MinutesCP1 = (int) (MillisCP1 / (1000 * 60)) % 60;
 SecondsCP1 = (int) (MillisCP1 / 1000) % 60;
 String time_readingCP1 = "" + String.format("%02d", HoursCP1) + ":"
 + String.format("%02d", MinutesCP1) + ":"
 + String.format("%02d", SecondsCP1);
 //-------------------------meter reading
 Float prev_mrCP1 = Float.parseFloat(sPmeterReadingCP1.getMeterReadingCP1());
 Float current_mrCP1 = Float.parseFloat(p1meter);
 float meterreadingdiff = current_mrCP1 - prev_mrCP1;
 float meterreadingdiffP = (current_mrCP1 - prev_mrCP1) * erate;
 String meterreadingCP1 = String.format("%.2f", meterreadingdiff);
 String meterreadingCP1P = String.format("%.2f", meterreadingdiffP);

 //------------------
 tv_status1.setText(s10_CP1);
 if (meterreadingdiffP > 0 && meterreadingdiffP < 100) {
 txt_c1round_off_mr.setText("₹ " + meterreadingCP1P);
 rsCP1 = "₹ " + meterreadingCP1P;
 rsPOCP1 = "₹ " + meterreadingCP1P;
 }
 txt_c1diff.setText(time_readingCP1);
 status_detailCP1 = s10_CP1;
 unit_detailCP1 = meterreadingCP1;
 etime_detailCP1 = time_readingCP1;
 sPisPoweFail.setisPowerFailCP1("f");
 relativeLayout1st.setVisibility(View.GONE);

 } else if (plugedinCountcp1 == 0) {
 //Time reading_________________

 StartTimeCP1 = SystemClock.uptimeMillis() + (-sPTimeReadingCP1.getTimeReadingCP1());
 //-------------------------meter reading
 Float prev_mrCP1 = Float.parseFloat(sPmeterReadingCP1.getMeterReadingCP1());
 Float current_mrCP1 = Float.parseFloat(p1meter);
 float meterreadingdiff = current_mrCP1 - prev_mrCP1;
 float meterreadingdiffP = (current_mrCP1 - prev_mrCP1) * erate;
 String meterreadingCP1 = String.format("%.2f", meterreadingdiff);
 String meterreadingCP1P = String.format("%.2f", meterreadingdiffP);
 //--------------------
 tv_status1.setText(s9_CP1);
 if (meterreadingdiffP > 0 && meterreadingdiffP < 100) {
 txt_c1round_off_mr.setText("₹ " + meterreadingCP1P);
 rsCP1 = "₹ " + meterreadingCP1P;
 rsPOCP1 = "₹ " + meterreadingCP1P;
 }
 txt_c1diff.setText("00:00:00");
 status_detailCP1 = s9_CP1;
 unit_detailCP1 = meterreadingCP1;
 etime_detailCP1 = "00:00:00";
 plugedinCountcp1++;
 sPisPluggedin.setisPluggedinCP1("t");
 isResumedAftercp1 = true;
 toggleBtn.startAnimation(anim);
 relativeLayout1st.setVisibility(View.GONE);



 } else {
 //time reading______
 MillisCP1 = (SystemClock.uptimeMillis() - StartTimeCP1);

 HoursCP1 = (int) (MillisCP1 / (1000 * 60 * 60));
 MinutesCP1 = (int) (MillisCP1 / (1000 * 60)) % 60;
 SecondsCP1 = (int) (MillisCP1 / 1000) % 60;
 String time_readingCP1 = "" + String.format("%02d", HoursCP1) + ":"
 + String.format("%02d", MinutesCP1) + ":"
 + String.format("%02d", SecondsCP1);

 //-------------------------meter reading
 Float prev_mrCP1 = Float.parseFloat(sPmeterReadingCP1.getMeterReadingCP1());
 Float current_mrCP1 = Float.parseFloat(p1meter);
 float meterreadingdiff = current_mrCP1 - prev_mrCP1;
 float meterreadingdiffP = (current_mrCP1 - prev_mrCP1) * erate;
 String meterreadingCP1 = String.format("%.2f", meterreadingdiff);
 String meterreadingCP1P = String.format("%.2f", meterreadingdiffP);
 //--------------------

 tv_status1.setText(s9_CP1);
 if (meterreadingdiffP > 0 && meterreadingdiffP < 100) {
 txt_c1round_off_mr.setText("₹ " + meterreadingCP1P);
 rsCP1 = "₹ " + meterreadingCP1P;
 rsPOCP1 = "₹ " + meterreadingCP1P;
 }
 txt_c1diff.setText(time_readingCP1);
 status_detailCP1 = s9_CP1;
 unit_detailCP1 = meterreadingCP1;
 etime_detailCP1 = time_readingCP1;
 plugedinCountcp1++;
 sPisPluggedin.setisPluggedinCP1("t");
 isResumedAftercp1 = true;
 relativeLayout1st.setVisibility(View.GONE);


 }


 } else {
 //--------------- Status:vehicle pugged in_________
 if (plugedinCountcp1 >= 10) {
 //time reading______
 MillisCP1 = (SystemClock.uptimeMillis() - StartTimeCP1);

 HoursCP1 = (int) (MillisCP1 / (1000 * 60 * 60));
 MinutesCP1 = (int) (MillisCP1 / (1000 * 60)) % 60;
 SecondsCP1 = (int) (MillisCP1 / 1000) % 60;
 String time_readingCP1 = "" + String.format("%02d", HoursCP1) + ":"
 + String.format("%02d", MinutesCP1) + ":"
 + String.format("%02d", SecondsCP1);

 //-------------------------meter reading
 Float prev_mrCP1 = Float.parseFloat(sPmeterReadingCP1.getMeterReadingCP1());
 Float current_mrCP1 = Float.parseFloat(p1meter);
 float meterreadingdiff = current_mrCP1 - prev_mrCP1;
 float meterreadingdiffP = (current_mrCP1 - prev_mrCP1) * erate;
 String meterreadingCP1 = String.format("%.2f", meterreadingdiff);
 String meterreadingCP1P = String.format("%.2f", meterreadingdiffP);

 tv_status1.setText(s10_CP1);
 if (meterreadingdiffP > 0 && meterreadingdiffP < 100) {
 txt_c1round_off_mr.setText("₹ " + meterreadingCP1P);
 rsCP1 = "₹ " + meterreadingCP1P;
 rsPOCP1 = "₹ " + meterreadingCP1P;
 }
 txt_c1diff.setText(time_readingCP1);
 status_detailCP1 = s10_CP1;
 unit_detailCP1 = meterreadingCP1;
 etime_detailCP1 = time_readingCP1;
 // sPisPoweFail.setisPowerFailCP1("t");
 relativeLayout1st.setVisibility(View.GONE);


 } else if (plugedinCountcp1 == 0) {
 tv_status1.setText(s11_CP1);
 txt_c1round_off_mr.setText("₹ 00.00");
 rsCP1 = "₹ 00.00";
 txt_c1diff.setText("00:00:00");
 status_detailCP1 = s11_CP1;
 unit_detailCP1 = "00.00";
 etime_detailCP1 = "00:00:00";
 sPisPluggedin.setisPluggedinCP1("t");
 plugedinCountcp1++;

 //meter reading-------------------------
 sPmeterReadingCP1.setMeterReadingCP1(p1meter);
 //Time reading_________________
 StartTimeCP1 = SystemClock.uptimeMillis() + (-NewBeginMillsCP1);  //--> Start Time
 toggleBtn.startAnimation(anim);

 } else {
 //time reading______
 MillisCP1 = (SystemClock.uptimeMillis() - StartTimeCP1);

 HoursCP1 = (int) (MillisCP1 / (1000 * 60 * 60));
 MinutesCP1 = (int) (MillisCP1 / (1000 * 60)) % 60;
 SecondsCP1 = (int) (MillisCP1 / 1000) % 60;
 String time_readingCP1 = "" + String.format("%02d", HoursCP1) + ":"
 + String.format("%02d", MinutesCP1) + ":"
 + String.format("%02d", SecondsCP1);


 //-----------meter Reading
 Float prev_mrCP1 = Float.parseFloat(sPmeterReadingCP1.getMeterReadingCP1());
 Float current_mrCP1 = Float.parseFloat(p1meter);
 float meterreadingdiff = current_mrCP1 - prev_mrCP1;
 float meterreadingdiffP = (current_mrCP1 - prev_mrCP1) * erate;
 String meterreadingCP1 = String.format("%.2f", meterreadingdiff);
 String meterreadingCP1P = String.format("%.2f", meterreadingdiffP);

 //--------------------
 tv_status1.setText(s11_CP1);
 if (meterreadingdiffP > 0 && meterreadingdiffP < 100) {
 txt_c1round_off_mr.setText("₹ " + meterreadingCP1P);
 rsCP1 = "₹ " + meterreadingCP1P;
 rsPOCP1 = "₹ " + meterreadingCP1P;
 }
 txt_c1diff.setText(time_readingCP1);
 status_detailCP1 = s11_CP1;
 unit_detailCP1 = meterreadingCP1;
 etime_detailCP1 = time_readingCP1;
 sPisPluggedin.setisPluggedinCP1("t");
 plugedinCountcp1++;
 }

 }

 }
 //-----------------------------------------------------------CP2 Status-----------------------------------------------------------------------------------------------------------------------------
 if (p2OnOff.equals("0") && p2plugin.equals("0") && p2fault.equals("99")) {

 if (sPisPluggedin.getisPluggedinCP2().equals("t")) {

 if (plugedoutCountcp2 >= 30) {
 sPisPluggedin.setisPluggedinCP2("f");

 } else {
 displayPlugoutCountC2 = 0;
 tv_status2.setText(s7_CP2);
 txt_c2round_off_mr.setText(rsPOCP2);
 rsCP2 = "₹ 00.00";
 txt_c2diff.setText(etime_detailCP2);
 status_detailCP2 = s7_CP2;
 unit_detailCP2 = unit_detailCP2;
 etime_detailCP2 = etime_detailCP2;
 plugedinCountcp2 = 0;
 plugedoutCountcp2++;
 if (isResumedAftercp2) {
 sPisPoweFail.setisPowerFailCP1("f");
 }
 //


 }

 } else {
 countidleCP2++;
 tv_status2.setText(s8_CP2);
 //countC2 = 0;
 isAvailableC2 = true;
 isStartC2 = true;

 // Toast.makeText(getApplicationContext(),"C2 is Available",Toast.LENGTH_SHORT).show();
 txt_c2round_off_mr.setText("₹ 00.00");
 rsCP2 = "₹ 00.00";
 txt_c2diff.setText("00:00:00");
 status_detailCP2 = s8_CP2;
 unit_detailCP2 = "00.00";
 etime_detailCP2 = "00:00:00";
 plugedinCountcp2 = 0;
 btn_p2onff.clearAnimation();
 //overlay2
 if (overlayCountii> 10) {
 relativeLayout2nd.setVisibility(View.VISIBLE);
 }
 overlayCountii++;
 }

 }
 if (p2OnOff.equals("1") && p2plugin.equals("0") && p2fault.equals("99")) {
 countidleCP2 = 0;
 plugedinCountcp2 = 0;
 plugedoutCountcp2 = 0;
 tv_status2.setText(s1_CP2);
 //countC2=1;
 if (isStartC2)
 {
 isStartC2 = false;
 countC2++;
 }
 isAvailableC2=false;
 txt_c2round_off_mr.setText("₹ 00.00");
 rsCP2 = "₹ 00.00";
 txt_c2diff.setText("00:00:00");
 status_detailCP2 = s1_CP2;
 unit_detailCP2 = "00.00";
 etime_detailCP2 = "00:00:00";
 isStillOnCP2 = false;
 relativeLayout2nd.setVisibility(View.GONE);

 if (displayPlugoutCountC2 == 8) {
 if (sPisPoweFail.getisPowerFailCP2().equals("t") && sPisPluggedin.getisPluggedinCP2().equals("t")) {
 btn_p2onff.performClick();
 sPisPoweFail.setisPowerFailCP2("f");
 sPisPluggedin.setisPluggedinCP2("f");
 tv_status2.setText(s7_CP2);
 status_detailCP2 = s7_CP2;
 }
 }
 displayPlugoutCountC2++;
 }
 if (p2OnOff.equals("1") && p2plugin.equals("1") && p2fault.equals("99")) {
 countidleCP2 = 0;
 if (sPisPoweFail.getisPowerFailCP2().equals("t")) {
 //---------Status:resuming after power fail_______________
 if (plugedinCountcp2 >= 10) {
 //time reading______
 MillisCP2 = (SystemClock.uptimeMillis() - StartTimeCP2);

 HoursCP2 = (int) (MillisCP2 / (1000 * 60 * 60));
 MinutesCP2 = (int) (MillisCP2 / (1000 * 60)) % 60;
 SecondsCP2 = (int) (MillisCP2 / 1000) % 60;
 String time_readingCP2 = "" + String.format("%02d", HoursCP2) + ":"
 + String.format("%02d", MinutesCP2) + ":"
 + String.format("%02d", SecondsCP2);
 //-------------------------meter reading
 Float prev_mrCP2 = Float.parseFloat(sPmeterReadingCP2.getMeterReadingCP2());
 Float current_mrCP2 = Float.parseFloat(p2meter);
 float meterreadingdiffc2 = current_mrCP2 - prev_mrCP2;
 float meterreadingdiffc2P = (current_mrCP2 - prev_mrCP2) * erate;
 String meterreadingCP2 = String.format("%.2f", meterreadingdiffc2);
 String meterreadingCP2P = String.format("%.2f", meterreadingdiffc2P);
 //--------------------
 tv_status2.setText(s10_CP2);
 *//*  txt_c2round_off_mr.setText("₹ " + meterreadingCP2P);
                                                    rsCP2 = "₹ " + meterreadingCP2P;
                                                    rsPOCP2 = "₹ " + meterreadingCP2P;*//*
                                                    if (meterreadingdiffc2P<100) {
                                                        txt_c2round_off_mr.setText("₹ " + meterreadingCP2P);
                                                        rsCP2 = "₹ " + meterreadingCP2P;
                                                        rsPOCP2 = "₹ " + meterreadingCP2P;
                                                    }
                                                    txt_c2diff.setText(time_readingCP2);
                                                    status_detailCP2 = s10_CP2;
                                                    unit_detailCP2 = meterreadingCP2;
                                                    etime_detailCP2 = time_readingCP2;
                                                    sPisPoweFail.setisPowerFailCP2("f");
                                                    relativeLayout2nd.setVisibility(View.GONE);

                                                } else if (plugedinCountcp2 == 0) {
                                                    //Time reading_________________

                                                    StartTimeCP2 = SystemClock.uptimeMillis() + (-sPTimeReadingCP2.getTimeReadingCP2());
                                                    //-------------------------meter reading
                                                    Float prev_mrCP2 = Float.parseFloat(sPmeterReadingCP2.getMeterReadingCP2());
                                                    Float current_mrCP2 = Float.parseFloat(p2meter);
                                                    float meterreadingdiffc2 = current_mrCP2 - prev_mrCP2;
                                                    float meterreadingdiffc2P = (current_mrCP2 - prev_mrCP2) * erate;
                                                    String meterreadingCP2 = String.format("%.2f", meterreadingdiffc2);
                                                    String meterreadingCP2P = String.format("%.2f", meterreadingdiffc2P);
                                                    //--------------------
                                                    tv_status2.setText(s9_CP2);
                                                    if (meterreadingdiffc2P > 0 && meterreadingdiffc2P < 100) {
                                                        txt_c2round_off_mr.setText("₹ " + meterreadingCP2P);
                                                        rsCP2 = "₹ " + meterreadingCP2P;
                                                        rsPOCP2 = "₹ " + meterreadingCP2P;
                                                    }
                                                    txt_c2diff.setText("00:00:00");
                                                    status_detailCP2 = s9_CP2;
                                                    unit_detailCP2 = meterreadingCP2;
                                                    etime_detailCP2 = "00:00:00";
                                                    plugedinCountcp2++;
                                                    sPisPluggedin.setisPluggedinCP2("t");
                                                    isResumedAftercp2 = true;
                                                    txt_p2btnValue.startAnimation(anim2);
                                                    relativeLayout2nd.setVisibility(View.GONE);


                                                } else {
                                                    //time reading______
                                                    MillisCP2 = (SystemClock.uptimeMillis() - StartTimeCP2);

                                                    HoursCP2 = (int) (MillisCP2 / (1000 * 60 * 60));
                                                    MinutesCP2 = (int) (MillisCP2 / (1000 * 60)) % 60;
                                                    SecondsCP2 = (int) (MillisCP2 / 1000) % 60;
                                                    String time_readingCP2 = "" + String.format("%02d", HoursCP2) + ":"
                                                            + String.format("%02d", MinutesCP2) + ":"
                                                            + String.format("%02d", SecondsCP2);
                                                    //-------------------------meter reading
                                                    Float prev_mrCP2 = Float.parseFloat(sPmeterReadingCP2.getMeterReadingCP2());
                                                    Float current_mrCP2 = Float.parseFloat(p2meter);
                                                    float meterreadingdiffc2 = current_mrCP2 - prev_mrCP2;
                                                    float meterreadingdiffc2P = (current_mrCP2 - prev_mrCP2) * erate;
                                                    String meterreadingCP2 = String.format("%.2f", meterreadingdiffc2);
                                                    String meterreadingCP2P = String.format("%.2f", meterreadingdiffc2P);
                                                    //--------------------
                                                    tv_status2.setText(s9_CP2);
                                                    if (meterreadingdiffc2P > 0 && meterreadingdiffc2P < 100) {
                                                        txt_c2round_off_mr.setText("₹ " + meterreadingCP2P);
                                                        rsCP2 = "₹ " + meterreadingCP2P;
                                                        rsPOCP2 = "₹ " + meterreadingCP2P;
                                                    }
                                                    txt_c2diff.setText(time_readingCP2);
                                                    status_detailCP2 = s9_CP2;
                                                    unit_detailCP2 = meterreadingCP2;
                                                    etime_detailCP2 = time_readingCP2;
                                                    plugedinCountcp2++;
                                                    sPisPluggedin.setisPluggedinCP2("t");
                                                    isResumedAftercp2 = true;
                                                    relativeLayout2nd.setVisibility(View.GONE);


                                                }

                                            } else {
                                                //--------------- Status:vehicle pugged in_________
                                                if (plugedinCountcp2 >= 10) {
                                                    //time reading______
                                                    MillisCP2 = (SystemClock.uptimeMillis() - StartTimeCP2);

                                                    HoursCP2 = (int) (MillisCP2 / (1000 * 60 * 60));
                                                    MinutesCP2 = (int) (MillisCP2 / (1000 * 60)) % 60;
                                                    SecondsCP2 = (int) (MillisCP2 / 1000) % 60;
                                                    String time_readingCP2 = "" + String.format("%02d", HoursCP2) + ":"
                                                            + String.format("%02d", MinutesCP2) + ":"
                                                            + String.format("%02d", SecondsCP2);
                                                    //-------------------------meter reading
                                                    Float prev_mrCP2 = Float.parseFloat(sPmeterReadingCP2.getMeterReadingCP2());
                                                    Float current_mrCP2 = Float.parseFloat(p2meter);
                                                    float meterreadingdiffc2 = current_mrCP2 - prev_mrCP2;
                                                    float meterreadingdiffc2P = (current_mrCP2 - prev_mrCP2) * erate;
                                                    String meterreadingCP2 = String.format("%.2f", meterreadingdiffc2);
                                                    String meterreadingCP2P = String.format("%.2f", meterreadingdiffc2P);
                                                    //--------------------
                                                    tv_status2.setText(s10_CP2);
                                                    if (meterreadingdiffc2P > 0 && meterreadingdiffc2P < 100) {
                                                        txt_c2round_off_mr.setText("₹ " + meterreadingCP2P);
                                                        rsCP2 = "₹ " + meterreadingCP2P;
                                                        rsPOCP2 = "₹ " + meterreadingCP2P;
                                                    }
                                                    txt_c2diff.setText(time_readingCP2);
                                                    status_detailCP2 = s10_CP2;
                                                    unit_detailCP2 = meterreadingCP2;
                                                    etime_detailCP2 = time_readingCP2;
                                                    // sPisPoweFail.setisPowerFailCP1("t");
                                                    relativeLayout2nd.setVisibility(View.GONE);


                                                } else if (plugedinCountcp2 == 0) {
                                                    tv_status2.setText(s11_CP2);
                                                    txt_c2round_off_mr.setText("₹ 00.00");
                                                    rsCP2 = "₹ 00.00";
                                                    txt_c2diff.setText("00:00:00");
                                                    status_detailCP2 = s11_CP2;
                                                    unit_detailCP2 = "00.00";
                                                    etime_detailCP2 = "00:00:00";
                                                    sPisPluggedin.setisPluggedinCP2("t");
                                                    plugedinCountcp2++;

                                                    //meter reading-------------------------
                                                    sPmeterReadingCP2.setMeterReadingCP2(p2meter);
                                                    //Time reading_________________
                                                    StartTimeCP2 = SystemClock.uptimeMillis() + (-NewBeginMillsCP2);  //--> Start Time
                                                    txt_p2btnValue.startAnimation(anim2);

                                                } else {
                                                    //time reading______
                                                    MillisCP2 = (SystemClock.uptimeMillis() - StartTimeCP2);

                                                    HoursCP2 = (int) (MillisCP2 / (1000 * 60 * 60));
                                                    MinutesCP2 = (int) (MillisCP2 / (1000 * 60)) % 60;
                                                    SecondsCP2 = (int) (MillisCP2 / 1000) % 60;
                                                    String time_readingCP2 = "" + String.format("%02d", HoursCP2) + ":"
                                                            + String.format("%02d", MinutesCP2) + ":"
                                                            + String.format("%02d", SecondsCP2);


                                                    //-------------------------meter reading
                                                    Float prev_mrCP2 = Float.parseFloat(sPmeterReadingCP2.getMeterReadingCP2());
                                                    Float current_mrCP2 = Float.parseFloat(p2meter);
                                                    float meterreadingdiffc2 = current_mrCP2 - prev_mrCP2;
                                                    float meterreadingdiffc2P = (current_mrCP2 - prev_mrCP2) * erate;
                                                    String meterreadingCP2 = String.format("%.2f", meterreadingdiffc2);
                                                    String meterreadingCP2P = String.format("%.2f", meterreadingdiffc2P);
                                                    //--------------------
                                                    tv_status2.setText(s11_CP2);
                                                    if (meterreadingdiffc2P > 0 && meterreadingdiffc2P < 100) {
                                                        txt_c2round_off_mr.setText("₹ " + meterreadingCP2P);
                                                        rsCP2 = "₹ " + meterreadingCP2P;
                                                        rsPOCP2 = "₹ " + meterreadingCP2P;
                                                    }
                                                    txt_c2diff.setText(time_readingCP2);
                                                    status_detailCP2 = s11_CP2;
                                                    unit_detailCP2 = meterreadingCP2;
                                                    etime_detailCP2 = time_readingCP2;
                                                    sPisPluggedin.setisPluggedinCP2("t");
                                                    plugedinCountcp2++;
                                                }

                                            }

                                        }

//-----------------------------------------------------------CP3 Status-----------------------------------------------------------------------------------------------------------------------------
                                        if (p3OnOff.equals("0") && p3plugin.equals("0") && p3fault.equals("99")) {

                                            if (sPisPluggedin.getisPluggedinCP3().equals("t")) {

                                                if (plugedoutCountcp3 >= 30) {
                                                    sPisPluggedin.setisPluggedinCP3("f");

                                                } else {
                                                    displayPlugoutCountC2 = 0;
                                                    tv_status3.setText(s7_CP3);
                                                    txt_c3round_off_mr.setText(rsPOCP3);
                                                    rsCP3 = "₹ 00.00";
                                                    txt_c3diff.setText(etime_detailCP3);
                                                    status_detailCP3 = s7_CP3;
                                                    unit_detailCP3 = unit_detailCP3;
                                                    etime_detailCP3 = etime_detailCP3;
                                                    plugedinCountcp3 = 0;
                                                    plugedoutCountcp3++;
                                                    if (isResumedAftercp3) {
                                                        sPisPoweFail.setisPowerFailCP3("f");
                                                    }
                                                    //
                                                }

                                            } else {
                                                countidleCP3++;
                                                tv_status3.setText(s8_CP3);
                                                //countC3 = 0;
                                                isAvailableC3=true;

                                                    isStartC3 = true;

                                                //Toast.makeText(getApplicationContext(),"C3 Is Available Now",Toast.LENGTH_SHORT).show();
                                                txt_c3round_off_mr.setText("₹ 00.00");
                                                rsCP3 = "₹ 00.00";
                                                txt_c3diff.setText("00:00:00");
                                                status_detailCP3 = s8_CP3;
                                                unit_detailCP3 = "00.00";
                                                etime_detailCP3 = "00:00:00";
                                                plugedinCountcp3 = 0;
                                                btn_p3onff.clearAnimation();

                                                //overlay1
                                                if (overlayCountiii> 10) {
                                                    relativeLayout3rd.setVisibility(View.VISIBLE);
                                                }
                                                overlayCountiii++;

                                            }

                                        }
                                        if (p3OnOff.equals("1") && p3plugin.equals("0") && p3fault.equals("99")) {
                                            plugedinCountcp3 = 0;
                                            plugedoutCountcp3 = 0;
                                            tv_status3.setText(s1_CP3);
                                            //countC3=1;\
                                            if (isStartC3)
                                            {
                                                isStartC3 = false;
                                                countC3++;
                                            }
                                            isAvailableC3 = false;
                                            txt_c3round_off_mr.setText("₹ 00.00");
                                            countidleCP3 = 0;
                                            rsCP3 = "₹ 00.00";
                                            txt_c3diff.setText("00:00:00");
                                            status_detailCP3 = s1_CP3;
                                            unit_detailCP3 = "00.00";
                                            etime_detailCP3 = "00:00:00";
                                            relativeLayout3rd.setVisibility(View.GONE);

                                            if (displayPlugoutCountC3 == 8) {
                                                if (sPisPoweFail.getisPowerFailCP3().equals("t") && sPisPluggedin.getisPluggedinCP3().equals("t")) {
                                                    btn_p3onff.performClick();
                                                    sPisPoweFail.setisPowerFailCP3("f");
                                                    sPisPluggedin.setisPluggedinCP3("f");
                                                    tv_status3.setText(s7_CP3);
                                                    status_detailCP3 = s7_CP3;
                                                }
                                            }
                                            displayPlugoutCountC3++;
                                        }
                                        if (p3OnOff.equals("1") && p3plugin.equals("1") && p3fault.equals("99")) {
                                            countidleCP3 = 0;
                                            if (sPisPoweFail.getisPowerFailCP3().equals("t")) {
                                                //---------Status:resuming after power fail_______________
                                                if (plugedinCountcp3 >= 10) {
                                                    //time reading______
                                                    MillisCP3 = (SystemClock.uptimeMillis() - StartTimeCP3);

                                                    HoursCP3 = (int) (MillisCP3 / (1000 * 60 * 60));
                                                    MinutesCP3 = (int) (MillisCP3 / (1000 * 60)) % 60;
                                                    SecondsCP3 = (int) (MillisCP3 / 1000) % 60;
                                                    String time_readingCP3 = "" + String.format("%02d", HoursCP3) + ":"
                                                            + String.format("%02d", MinutesCP3) + ":"
                                                            + String.format("%02d", SecondsCP3);
                                                    //-------------------------meter reading
                                                    Float prev_mrCP3 = Float.parseFloat(sPmeterReadingCP3.getMeterReadingCP3());
                                                    Float current_mrCP3 = Float.parseFloat(p3meter);
                                                    float meterreadingdiffc3 = current_mrCP3 - prev_mrCP3;
                                                    float meterreadingdiffc3P = (current_mrCP3 - prev_mrCP3) * erate;
                                                    String meterreadingCP3 = String.format("%.2f", meterreadingdiffc3);
                                                    String meterreadingCP3P = String.format("%.2f", meterreadingdiffc3P);
                                                    //--------------------
                                                    tv_status3.setText(s10_CP3);
                                                    if (meterreadingdiffc3P > 0 && meterreadingdiffc3P < 100) {
                                                        txt_c3round_off_mr.setText("₹ " + meterreadingCP3P);
                                                        rsCP3 = "₹ " + meterreadingCP3P;
                                                        rsPOCP3 = "₹ " + meterreadingCP3P;
                                                    }
                                                    txt_c3diff.setText(time_readingCP3);
                                                    status_detailCP3 = s10_CP3;
                                                    unit_detailCP3 = meterreadingCP3;
                                                    etime_detailCP3 = time_readingCP3;
                                                    sPisPoweFail.setisPowerFailCP3("f");
                                                    relativeLayout3rd.setVisibility(View.GONE);

                                                } else if (plugedinCountcp3 == 0) {
                                                    //Time reading_________________

                                                    StartTimeCP3 = SystemClock.uptimeMillis() + (-sPTimeReadingCP3.getTimeReadingCP3());
                                                    //-------------------------meter reading
                                                    Float prev_mrCP3 = Float.parseFloat(sPmeterReadingCP3.getMeterReadingCP3());
                                                    Float current_mrCP3 = Float.parseFloat(p3meter);
                                                    float meterreadingdiffc3 = current_mrCP3 - prev_mrCP3;
                                                    float meterreadingdiffc3P = (current_mrCP3 - prev_mrCP3) * erate;
                                                    String meterreadingCP3 = String.format("%.2f", meterreadingdiffc3);
                                                    String meterreadingCP3P = String.format("%.2f", meterreadingdiffc3P);
                                                    //--------------------
                                                    tv_status3.setText(s9_CP3);
                                                    if (meterreadingdiffc3P > 0 && meterreadingdiffc3P < 100) {
                                                        txt_c3round_off_mr.setText("₹ " + meterreadingCP3P);
                                                        rsCP3 = "₹ " + meterreadingCP3P;
                                                        rsPOCP3 = "₹ " + meterreadingCP3P;
                                                    }
                                                    txt_c3diff.setText("00:00:00");
                                                    status_detailCP3 = s9_CP3;
                                                    unit_detailCP3 = meterreadingCP3;
                                                    etime_detailCP3 = "00:00:00";
                                                    plugedinCountcp3++;
                                                    sPisPluggedin.setisPluggedinCP3("t");
                                                    isResumedAftercp3 = true;
                                                    txt_p3btnValue.startAnimation(anim3);
                                                    relativeLayout3rd.setVisibility(View.GONE);


                                                } else {
                                                    //time reading______
                                                    MillisCP3 = (SystemClock.uptimeMillis() - StartTimeCP3);

                                                    HoursCP3 = (int) (MillisCP3 / (1000 * 60 * 60));
                                                    MinutesCP3 = (int) (MillisCP3 / (1000 * 60)) % 60;
                                                    SecondsCP3 = (int) (MillisCP3 / 1000) % 60;
                                                    String time_readingCP3 = "" + String.format("%02d", HoursCP3) + ":"
                                                            + String.format("%02d", MinutesCP3) + ":"
                                                            + String.format("%02d", SecondsCP3);
                                                    //-------------------------meter reading
                                                    Float prev_mrCP3 = Float.parseFloat(sPmeterReadingCP3.getMeterReadingCP3());
                                                    Float current_mrCP3 = Float.parseFloat(p3meter);
                                                    float meterreadingdiffc3 = current_mrCP3 - prev_mrCP3;
                                                    float meterreadingdiffc3P = (current_mrCP3 - prev_mrCP3) * erate;
                                                    String meterreadingCP3 = String.format("%.2f", meterreadingdiffc3);
                                                    String meterreadingCP3P = String.format("%.2f", meterreadingdiffc3P);
                                                    //--------------------
                                                    tv_status3.setText(s9_CP3);
                                                    if (meterreadingdiffc3P > 0 && meterreadingdiffc3P < 100) {
                                                        txt_c3round_off_mr.setText("₹ " + meterreadingCP3P);
                                                        rsCP3 = "₹ " + meterreadingCP3P;
                                                        rsPOCP3 = "₹ " + meterreadingCP3P;
                                                    }
                                                    status_detailCP3 = s9_CP3;
                                                    unit_detailCP3 = meterreadingCP3;
                                                    etime_detailCP3 = time_readingCP3;
                                                    plugedinCountcp3++;
                                                    sPisPluggedin.setisPluggedinCP3("t");
                                                    isResumedAftercp3 = true;
                                                    relativeLayout3rd.setVisibility(View.GONE);


                                                }

                                            } else {
                                                //--------------- Status:vehicle pugged in_________
                                                if (plugedinCountcp3 >= 10) {
                                                    //time reading______
                                                    MillisCP3 = (SystemClock.uptimeMillis() - StartTimeCP3);

                                                    HoursCP3 = (int) (MillisCP3 / (1000 * 60 * 60));
                                                    MinutesCP3 = (int) (MillisCP3 / (1000 * 60)) % 60;
                                                    SecondsCP3 = (int) (MillisCP3 / 1000) % 60;
                                                    String time_readingCP3 = "" + String.format("%02d", HoursCP3) + ":"
                                                            + String.format("%02d", MinutesCP3) + ":"
                                                            + String.format("%02d", SecondsCP3);

                                                    //-------------------------meter reading
                                                    Float prev_mrCP3 = Float.parseFloat(sPmeterReadingCP3.getMeterReadingCP3());
                                                    Float current_mrCP3 = Float.parseFloat(p3meter);
                                                    float meterreadingdiffc3 = current_mrCP3 - prev_mrCP3;
                                                    //  String meterreadingCP3 = String.format("%.2f", meterreadingdiffc3);
                                                    float meterreadingdiffc3P = (current_mrCP3 - prev_mrCP3) * erate;
                                                    String meterreadingCP3 = String.format("%.2f", meterreadingdiffc3);
                                                    String meterreadingCP3P = String.format("%.2f", meterreadingdiffc3P);
                                                    //--------------------
                                                    tv_status3.setText(s10_CP3);
                                                    if (meterreadingdiffc3P > 0 && meterreadingdiffc3P < 100) {
                                                        txt_c3round_off_mr.setText("₹ " + meterreadingCP3P);
                                                        rsCP3 = "₹ " + meterreadingCP3P;
                                                        rsPOCP3 = "₹ " + meterreadingCP3P;
                                                    }
                                                    txt_c3diff.setText(time_readingCP3);
                                                    status_detailCP3 = s10_CP3;
                                                    unit_detailCP3 = meterreadingCP3;
                                                    etime_detailCP3 = time_readingCP3;
                                                    // sPisPoweFail.setisPowerFailCP1("t");
                                                    relativeLayout3rd.setVisibility(View.GONE);


                                                } else if (plugedinCountcp3 == 0) {
                                                    tv_status3.setText(s11_CP3);
                                                    txt_c3round_off_mr.setText("₹ 00.00");
                                                    rsCP3 = "₹ 00.00";
                                                    txt_c3diff.setText("00:00:00");
                                                    status_detailCP3 = s11_CP3;
                                                    unit_detailCP3 = "00.00";
                                                    etime_detailCP3 = "00:00:00";
                                                    sPisPluggedin.setisPluggedinCP3("t");
                                                    plugedinCountcp3++;

                                                    //meter reading-------------------------
                                                    sPmeterReadingCP3.setMeterReadingCP3(p3meter);
                                                    //Time reading_________________
                                                    StartTimeCP3 = SystemClock.uptimeMillis() + (-NewBeginMillsCP3);  //--> Start Time
                                                    txt_p3btnValue.startAnimation(anim3);

                                                } else {
                                                    //time reading______
                                                    MillisCP3 = (SystemClock.uptimeMillis() - StartTimeCP3);

                                                    HoursCP3 = (int) (MillisCP3 / (1000 * 60 * 60));
                                                    MinutesCP3 = (int) (MillisCP3 / (1000 * 60)) % 60;
                                                    SecondsCP3 = (int) (MillisCP3 / 1000) % 60;
                                                    String time_readingCP3 = "" + String.format("%02d", HoursCP3) + ":"
                                                            + String.format("%02d", MinutesCP3) + ":"
                                                            + String.format("%02d", SecondsCP3);

                                                    //-------------------------meter reading
                                                    Float prev_mrCP3 = Float.parseFloat(sPmeterReadingCP3.getMeterReadingCP3());
                                                    Float current_mrCP3 = Float.parseFloat(p3meter);
                                                    float meterreadingdiffc3 = current_mrCP3 - prev_mrCP3;
                                                    float meterreadingdiffc3P = (current_mrCP3 - prev_mrCP3) * erate;
                                                    String meterreadingCP3 = String.format("%.2f", meterreadingdiffc3);
                                                    String meterreadingCP3P = String.format("%.2f", meterreadingdiffc3P);
                                                    //--------------------
                                                    tv_status3.setText(s11_CP3);
                                                    if (meterreadingdiffc3P > 0 && meterreadingdiffc3P < 100) {
                                                        txt_c3round_off_mr.setText("₹ " + meterreadingCP3P);
                                                        rsCP3 = "₹ " + meterreadingCP3P;
                                                        rsPOCP3 = "₹ " + meterreadingCP3P;
                                                    }
                                                    txt_c3diff.setText(time_readingCP3);
                                                    status_detailCP3 = s11_CP3;
                                                    unit_detailCP3 = meterreadingCP3;
                                                    etime_detailCP3 = time_readingCP3;
                                                    sPisPluggedin.setisPluggedinCP3("t");
                                                    plugedinCountcp3++;
                                                }

                                            }

                                        }


                                        if (detail_flag.equals("d_CP1")) {
                                            display_details(rsCP1, s15_CP1+" #01", voltage_detailCP1, current_detailCP1, power_detailCP1, unit_detailCP1, etime_detailCP1, status_detailCP1,s14_CP1,s17_CP1," ₹ "+erate_s+" "+s16_CP1,sPlanguageCP1.getlanguageCP1());

                                        } else if (detail_flag.equals("d_CP2")) {
                                            display_details(rsCP2, s15_CP2+" #02", voltage_detailCP2, current_detailCP2, power_detailCP2, unit_detailCP2, etime_detailCP2, status_detailCP2,s14_CP2,s17_CP2," ₹ "+erate_s+" "+s16_CP2,sPlanguageCP2.getlanguageCP2());

                                        } else if (detail_flag.equals("d_CP3")) {
                                            display_details(rsCP3, s15_CP3+" #03", voltage_detailCP3, current_detailCP3, power_detailCP3, unit_detailCP3, etime_detailCP3, status_detailCP3,s14_CP3,s17_CP3," ₹ "+erate_s+" "+s16_CP3,sPlanguageCP3.getlanguageCP3());

                                        }

                                        Log.e("CHECKII", "CHECKING");

                                        Log.e("COMMING STRING", recDataString + "\n\np1OnOff :" + p1OnOff + "\tp1plugin :" + p1plugin + "\tp1fault :" + p1fault + "\tp1voltage :" + p1voltage + "\tp1current :" + p1current + "\tp1meter :" + p1meter +
                                                "\n\np2OnOff : " + p2OnOff + "\tp2plugin : " + p2plugin + "\tp2fault : " + p2fault + "\tp2voltage : " + p2voltage + "\tp2current : " + p2current + "\tp2meter : " + p2meter +
                                                "\n\np3OnOff : " + p3OnOff + "\tp3plugin : " + p3plugin + "\tp3fault : " + p3fault + "\tp3voltage: " + p3voltage + "\tp3current : " + p3current + "\tp3meter : " + p3meter);

                                    }
                                } catch (StringIndexOutOfBoundsException siobe) {
                                    //System.out.println("invalid input");
                                }

                            }

                            recDataString.delete(0, recDataString.length());                    //clear all string data

                        }
                    *//*else{
                        recDataString.delete(0, recDataString.length());                    //clear all string data

                    }*//*
                    }
                    else {
                        tv_status1.setText(s6_CP1);
                        tv_status2.setText(s6_CP2);
                        tv_status3.setText(s6_CP3);
                        toggleBtn.setCardBackgroundColor(Color.parseColor(red));
                        toggleBtn.startAnimation(anim);
                        btn_p2onff.setCardBackgroundColor(Color.parseColor(red));
                        btn_p2onff.startAnimation(anim);
                        btn_p3onff.setCardBackgroundColor(Color.parseColor(red));
                        btn_p3onff.startAnimation(anim);
                    }
                }

            }
        };

        btAdapter = BluetoothAdapter.getDefaultAdapter();       // get Bluetooth adapter
        checkBTState();

        toggleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAlreadyonm) {
                    funConnectori();
                }else {
                    sPisPoweFail.setisPowerFailCP1("f");
                    StringBuilder newCommandp1 = new StringBuilder(command);
                    newCommandp1.setCharAt(1, '0');


                    command = ""+newCommandp1;
                    toggleBtn.setCardBackgroundColor(Color.parseColor(green));
                    toggleValue.setText("CP1 "+s22_CP1);

                    toggleBtn.clearAnimation();
                }
            }
        });


        btn_p2onff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAlreadyonm) {
                    funConnectorii();
                }else {
                    sPisPoweFail.setisPowerFailCP2("f");
                    StringBuilder newCommandp2 = new StringBuilder(command);
                    newCommandp2.setCharAt(1, '0');


                    command = ""+newCommandp2;
                    btn_p2onff.setCardBackgroundColor(Color.parseColor(green));
                    txt_p2btnValue.setText("CP2 "+s22_CP2);
                    txt_p2btnValue.clearAnimation();
                }
            }
        });
        btn_p3onff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAlreadyonm) {
                    funConnectoriii();
                }else {
                    sPisPoweFail.setisPowerFailCP3("f");
                    StringBuilder newCommandp3 = new StringBuilder(command);
                    newCommandp3.setCharAt(1, '0');


                    command = ""+newCommandp3;
                    btn_p3onff.setCardBackgroundColor(Color.parseColor(green));
                    txt_p3btnValue.setText("CP3 "+s22_CP3);
                    txt_p3btnValue.clearAnimation();
                }
            }
        });



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
        try
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
        }
        mConnectedThread = new ConnectedThread(btSocket);
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
        registerReceiver(batteryReceiver,batteryfilter);

    }

    private void display_details(final String rate_detail, final String charge_point_id,final String voltage_detail,final String current_detail,final String power_detail,final String unit_detail,final String etime_detail,final String status_detail,final String etime_displayf, final String unit_display, final String rate_display, final String lang){
        // txt_cpname,txt_rate,txt_voltage_detail,txt_current_detail,txt_power_detail,txt_unit_detail,txt_etime_detail,txt_status_detail;
        txt_rate.setText(rate_detail);
        txt_cpname.setText(charge_point_id);
        txt_voltage_detail.setText(voltage_detail);
        txt_current_detail.setText(current_detail);
        txt_power_detail.setText(power_detail);
        txt_unit_detail.setText(unit_detail);
        txt_etime_detail.setText(etime_detail);
        txt_status_detail.setText(status_detail);
        txt_etime_displayf.setText(etime_displayf);
        txt_unit_display.setText(unit_display);
        txt_rate_display.setText(rate_display);
        if (lang.equals("en")){
            rg_language_m.check(R.id.rb_en_m);
        }else if (lang.equals("ma")){
            rg_language_m.check(R.id.rb_ma_m);
        }else if (lang.equals("hi")){
            rg_language_m.check(R.id.rb_hi_m);
        }

    }
    public void funConnectori(){
        if (isStillOnCP1){
            flag = false;
            setToggleBtn();
            sPisPluggedin.setisPluggedinCP1("f");
            sPisPoweFail.setisPowerFailCP1("f");
        }
        else {
            if (flag) {
                StringBuilder newCommandp1 = new StringBuilder(command);
                newCommandp1.setCharAt(1, '0');

                Log.e("newCommandp1", "" + newCommandp1);
                mConnectedThread.write("" + newCommandp1);    // Send "0" via Bluetooth
                //  Toast.makeText(getBaseContext(), "Turn off CP1 :" + newCommandp1, Toast.LENGTH_SHORT).show();


                //flag=false;

            } else {
                StringBuilder newCommandp1 = new StringBuilder(command);
                newCommandp1.setCharAt(1, '1');


                Log.e("newCommandp1", "" + newCommandp1);
                mConnectedThread.write("" + newCommandp1);    // Send "0" via Bluetooth
                // Toast.makeText(getBaseContext(), "Turn on CP1 :" + newCommandp1, Toast.LENGTH_SHORT).show();
                //flag=true;

            }

            setToggleBtn();
        }
    }
    public void funConnectorii(){
        if (isStillOnCP2){
            p2flag = false;
            setP2Btn();
            sPisPluggedin.setisPluggedinCP2("f");
            sPisPoweFail.setisPowerFailCP2("f");
        }
        else
        {
            if (p2flag)
            {
                StringBuilder newCommandp2 = new StringBuilder(command);
                newCommandp2.setCharAt(2, '0');
                Log.e("newCommandp2", "" + newCommandp2);
                mConnectedThread.write("" + newCommandp2);    // Send "0" via Bluetooth
                // Toast.makeText(getBaseContext(), "Turn off CP2 :" + newCommandp2, Toast.LENGTH_SHORT).show();
                //flag=false;

            }
            else
            {
                StringBuilder newCommandp2 = new StringBuilder(command);
                newCommandp2.setCharAt(2, '1');

                Log.e("newCommandp2", "" + newCommandp2);
                mConnectedThread.write("" + newCommandp2);    // Send "1" via Bluetooth
                //   Toast.makeText(getBaseContext(), "Turn on CP2 :" + newCommandp2, Toast.LENGTH_SHORT).show();
                //flag=true;

            }

            setP2Btn();
        }
    }
    public void funConnectoriii(){
        if (isStillOnCP3){
            p3flag = false;
            setP3Btn();
            sPisPluggedin.setisPluggedinCP3("f");
            sPisPoweFail.setisPowerFailCP3("f");
        }
        else {
            if (p3flag)
            {
                StringBuilder newCommandp3 = new StringBuilder(command);
                newCommandp3.setCharAt(3, '0');

                Log.e("newCommandp3", "" + newCommandp3);
                mConnectedThread.write("" + newCommandp3);    // Send "0" via Bluetooth
                // Toast.makeText(getBaseContext(), "Turn off CP3 :" + newCommandp3, Toast.LENGTH_SHORT).show();
                //flag=false;

            }
            else
            {
                StringBuilder newCommandp3 = new StringBuilder(command);
                newCommandp3.setCharAt(3, '1');

                Log.e("newCommandp3", "" + newCommandp3);
                mConnectedThread.write("" + newCommandp3);    // Send "0" via Bluetooth
                //mConnectedThread.write("$100990.00");    // Send "1" via Bluetooth
                // Toast.makeText(getBaseContext(), "Turn on CP3 :" + newCommandp3, Toast.LENGTH_SHORT).show();
                //flag=true;
            }

            setP3Btn();
        }
    }

    @Override
    protected void onStop()
    {
        // Toast.makeText(MaindisplayActivityMannual.this,"onStop Method",Toast.LENGTH_LONG).show();
        Log.e("METHOD","onStop Method");
        unregisterReceiver(mReceiver);
        unregisterReceiver(batteryReceiver);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Toast.makeText(MaindisplayActivityMannual.this,"onDestroy Method",Toast.LENGTH_LONG).show();
        Log.e("METHOD","onDestroy Method");
    }
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Toast.makeText(MaindisplayActivityMannual.this, device.getName() + " Device found", Toast.LENGTH_LONG).show();
            }
            else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                //Toast.makeText(MaindisplayActivityMannual.this, device.getName() + " Device is now connected", Toast.LENGTH_LONG).show();

            }
            else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
                //Toast.makeText(MaindisplayActivityMannual.this, device.getName() + " Device is about to disconnect", Toast.LENGTH_LONG).show();
            }
            else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                //Toast.makeText(MaindisplayActivityMannual.this, device.getName() + " Device has disconnected", Toast.LENGTH_LONG).show();
                tv_status1.setText(s6_CP1);
                toggleBtn.setCardBackgroundColor(Color.parseColor(red));
                toggleBtn.startAnimation(anim);

                tv_status2.setText(s6_CP2);
                btn_p2onff.setCardBackgroundColor(Color.parseColor(red));
                btn_p2onff.startAnimation(anim);
                tv_status3.setText(s6_CP3);
                btn_p3onff.setCardBackgroundColor(Color.parseColor(red));
                btn_p3onff.startAnimation(anim);
                mFirebaseInstance.getReference(cid+"-CP1M").setValue(tv_status1.getText().toString());
                mFirebaseInstance.getReference(cid+"-CP2M").setValue(tv_status2.getText().toString());
                mFirebaseInstance.getReference(cid+"-CP3M").setValue(tv_status3.getText().toString());
                *//*if(sPisPluggedin.getisPluggedinCP1().equals("t")){
                    sPisPoweFail.setisPowerFailCP1("t");
                    sPTimeReadingCP1.setTimeReadingCP1(MillisCP1);

                }
                if(sPisPluggedin.getisPluggedinCP2().equals("t")){
                    sPisPoweFail.setisPowerFailCP2("t");
                    sPTimeReadingCP2.setTimeReadingCP2(MillisCP2);

                }
                if(sPisPluggedin.getisPluggedinCP3().equals("t")){
                    sPisPoweFail.setisPowerFailCP3("t");
                    sPTimeReadingCP3.setTimeReadingCP3(MillisCP3);

                }*//*

            }
        }
    };
    private final BroadcastReceiver batteryReceiver =  new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            if (intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)) {
                //   Toast.makeText(context, "The device is charging", Toast.LENGTH_SHORT).show();
                if (!isAlreadyonm){
                    startActivity(new Intent(MaindisplayActivityMannual.this,InitiatingActivityMannual.class));
                    finish();

                }


            } else {
                intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED);
                isAlreadyonm = false;
                // Toast.makeText(context, "The device is not charging", Toast.LENGTH_SHORT).show();
                tv_status1.setText(s6_CP1);
                toggleBtn.setCardBackgroundColor(Color.parseColor(red));
                toggleBtn.startAnimation(anim);
                tv_status2.setText(s6_CP2);
                btn_p2onff.setCardBackgroundColor(Color.parseColor(red));
                btn_p2onff.startAnimation(anim);
                tv_status3.setText(s6_CP3);
                btn_p3onff.setCardBackgroundColor(Color.parseColor(red));
                btn_p3onff.startAnimation(anim);
                mFirebaseInstance.getReference(cid+"-CP1M").setValue(tv_status1.getText().toString());
                mFirebaseInstance.getReference(cid+"-CP2M").setValue(tv_status2.getText().toString());
                mFirebaseInstance.getReference(cid+"-CP3M").setValue(tv_status3.getText().toString());
                if(sPisPluggedin.getisPluggedinCP1().equals("t")){
                    sPisPoweFail.setisPowerFailCP1("t");
                    sPTimeReadingCP1.setTimeReadingCP1(MillisCP1);

                }
                if(sPisPluggedin.getisPluggedinCP2().equals("t")){
                    sPisPoweFail.setisPowerFailCP2("t");
                    sPTimeReadingCP2.setTimeReadingCP2(MillisCP2);

                }
                if(sPisPluggedin.getisPluggedinCP3().equals("t")){
                    sPisPoweFail.setisPowerFailCP3("t");
                    sPTimeReadingCP3.setTimeReadingCP3(MillisCP3);

                }
            }
        }
    };

    private void setToggleBtn() {

        if(flag)
        {
            StringBuilder newCommandp3 = new StringBuilder(command);
            newCommandp3.setCharAt(1, '1');

            command = ""+newCommandp3;
            toggleBtn.setCardBackgroundColor(Color.parseColor(green));
            toggleValue.setText("CP1\n"+s23_CP1);

            // bulb_2.setCardBackgroundColor(Color.parseColor(AmberON));
            // toggleBtn.startAnimation(anim);
        }
        else
        {
            StringBuilder newCommandp3 = new StringBuilder(command);
            newCommandp3.setCharAt(1, '0');

            command = ""+newCommandp3;
            toggleBtn.setCardBackgroundColor(Color.parseColor(green));
            toggleValue.setText("CP1 "+s22_CP1);

            toggleBtn.clearAnimation();
            //bulb_2.setCardBackgroundColor(Color.parseColor(AmberOFF));

        }
    }
    private void setP2Btn() {

        if(p2flag)
        {
            StringBuilder newCommandp3 = new StringBuilder(command);
            newCommandp3.setCharAt(2, '1');

            command = ""+newCommandp3;
            btn_p2onff.setCardBackgroundColor(Color.parseColor(green));
            txt_p2btnValue.setText("CP2\n"+s23_CP2);

            // bulb_2ii.setCardBackgroundColor(Color.parseColor(AmberON));
            // bulb_2ii.startAnimation(anim2);
        }
        else
        {
            StringBuilder newCommandp3 = new StringBuilder(command);
            newCommandp3.setCharAt(2, '0');

            command = ""+newCommandp3;
            btn_p2onff.setCardBackgroundColor(Color.parseColor(green));
            txt_p2btnValue.setText("CP2 "+s22_CP2);
            txt_p2btnValue.clearAnimation();

            // anim2.cancel();

            //bulb_2ii.setCardBackgroundColor(Color.parseColor(AmberOFF));

        }
    }
    private void setP3Btn() {

        if(p3flag)
        {
            StringBuilder newCommandp3 = new StringBuilder(command);
            newCommandp3.setCharAt(3, '1');

            command = ""+newCommandp3;
            btn_p3onff.setCardBackgroundColor(Color.parseColor(green));
            txt_p3btnValue.setText("CP3\n"+s23_CP3);

            //  bulb_2iii.setCardBackgroundColor(Color.parseColor(AmberON));
            // bulb_2iii.startAnimation(anim3);
        }
        else
        {
            StringBuilder newCommandp3 = new StringBuilder(command);
            newCommandp3.setCharAt(3, '0');

            command = ""+newCommandp3;
            btn_p3onff.setCardBackgroundColor(Color.parseColor(green));
            txt_p3btnValue.setText("CP3 "+s22_CP3);
            txt_p3btnValue.clearAnimation();

            //anim3.cancel();

            // bulb_2iii.setCardBackgroundColor(Color.parseColor(AmberOFF));

        }
    }
    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {

        return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
        //creates secure outgoing connecetion with BT device using UUID
    }



    @Override
    public void onPause()
    {
        super.onPause();
        try
        {
            //Don't leave Bluetooth sockets open when leaving activity
            btSocket.close();
        } catch (IOException e2) {
            //insert code to deal with this
        }
    }
    //Checks that the Android device Bluetooth is available and prompts to be turned on if off
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
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }


        public void run() {
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
        //write method
        public void write(String input) {
            byte[] msgBuffer = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(msgBuffer);                //write bytes over BT connection via outstream
            } catch (IOException e) {
                //if you cannot write, close the application
                // Toast.makeText(getBaseContext(), "Connection Failure", Toast.LENGTH_LONG).show();
                finish();

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
    public void langSet(){
        if (sPlanguageCP1.getlanguageCP1().equals("en")){
            s1_CP1 = LangString.s1_E;  s2_CP1 = LangString.s2_E;  s3_CP1 = LangString.s3_E;  s4_CP1 = LangString.s4_E;  s5_CP1 = LangString.s5_E;  s6_CP1 = LangString.s6_E;  s7_CP1 = LangString.s7_E;  s8_CP1 = LangString.s8_E;  s9_CP1 = LangString.s9_E;  s10_CP1 = LangString.s10_E;  s11_CP1 = LangString.s11_E;  s12_CP1 = LangString.s12_E; s13_CP1 = LangString.s13_E; s14_CP1 = LangString.s14_E; s15_CP1 = LangString.s15_E; s16_CP1 = LangString.s16_E; s17_CP1 = LangString.s17_E; s21_CP1 = LangString.s21_E; s22_CP1 = LangString.s22_E; s23_CP1 = LangString.s23_E;
        }
        else if (sPlanguageCP1.getlanguageCP1().equals("ma")){
            s1_CP1 = LangString.s1_M;  s2_CP1 = LangString.s2_M;  s3_CP1 = LangString.s3_M;  s4_CP1 = LangString.s4_M;  s5_CP1 = LangString.s5_M;  s6_CP1 = LangString.s6_M;  s7_CP1 = LangString.s7_M;  s8_CP1 = LangString.s8_M;  s9_CP1 = LangString.s9_M;  s10_CP1 = LangString.s10_M; s11_CP1 = LangString.s11_M;  s12_CP1 = LangString.s12_M; s13_CP1 = LangString.s13_M; s14_CP1 = LangString.s14_M; s15_CP1 = LangString.s15_M; s16_CP1 = LangString.s16_M; s17_CP1 = LangString.s17_M; s21_CP1 = LangString.s21_M;  s22_CP1 = LangString.s22_M; s23_CP1 = LangString.s23_M;
        }
        else if (sPlanguageCP1.getlanguageCP1().equals("hi")){
            s1_CP1 = LangString.s1_H;  s2_CP1 = LangString.s2_H;  s3_CP1 = LangString.s3_H;  s4_CP1 = LangString.s4_H;  s5_CP1 = LangString.s5_H;  s6_CP1 = LangString.s6_H;  s7_CP1 = LangString.s7_H;  s8_CP1 = LangString.s8_H;  s9_CP1 = LangString.s9_H;  s10_CP1 = LangString.s10_H;  s11_CP1 = LangString.s11_H;  s12_CP1 = LangString.s12_H; s13_CP1 = LangString.s13_H; s14_CP1 = LangString.s14_H; s15_CP1 = LangString.s15_H; s16_CP1 = LangString.s16_H; s17_CP1 = LangString.s17_H; s21_CP1 = LangString.s21_H; s22_CP1 = LangString.s22_H; s23_CP1 = LangString.s23_H;
        }


        if (sPlanguageCP2.getlanguageCP2().equals("en")){
            s1_CP2 = LangString.s1_E;  s2_CP2 = LangString.s2_E;  s3_CP2 = LangString.s3_E;  s4_CP2 = LangString.s4_E;  s5_CP2 = LangString.s5_E;  s6_CP2 = LangString.s6_E;  s7_CP2 = LangString.s7_E;  s8_CP2 = LangString.s8_E;  s9_CP2 = LangString.s9_E;  s10_CP2 = LangString.s10_E;  s11_CP2 = LangString.s11_E;  s12_CP2 = LangString.s12_E; s13_CP2 = LangString.s13_E; s14_CP2 = LangString.s14_E; s15_CP2 = LangString.s15_E; s16_CP2 = LangString.s16_E; s17_CP2 = LangString.s17_E; s21_CP2 = LangString.s21_E; s22_CP2 = LangString.s22_E; s23_CP2 = LangString.s23_E;

        }
        else if (sPlanguageCP2.getlanguageCP2().equals("ma")){
            s1_CP2 = LangString.s1_M;  s2_CP2 = LangString.s2_M;  s3_CP2 = LangString.s3_M;  s4_CP2 = LangString.s4_M;  s5_CP2 = LangString.s5_M;  s6_CP2 = LangString.s6_M;  s7_CP2 = LangString.s7_M;  s8_CP2 = LangString.s8_M;  s9_CP2 = LangString.s9_M;  s10_CP2 = LangString.s10_M; s11_CP2 = LangString.s11_M;  s12_CP2 = LangString.s12_M; s13_CP2 = LangString.s13_M; s14_CP2 = LangString.s14_M; s15_CP2 = LangString.s15_M; s16_CP2 = LangString.s16_M; s17_CP2 = LangString.s17_M; s21_CP2 = LangString.s21_M;  s22_CP2 = LangString.s22_M; s23_CP2 = LangString.s23_M;

        }
        else if (sPlanguageCP2.getlanguageCP2().equals("hi")){
            s1_CP2 = LangString.s1_H;  s2_CP2 = LangString.s2_H;  s3_CP2 = LangString.s3_H;  s4_CP2 = LangString.s4_H;  s5_CP2 = LangString.s5_H;  s6_CP2 = LangString.s6_H;  s7_CP2 = LangString.s7_H;  s8_CP2 = LangString.s8_H;  s9_CP2 = LangString.s9_H;  s10_CP2 = LangString.s10_H;  s11_CP2 = LangString.s11_H;  s12_CP2 = LangString.s12_H; s13_CP2 = LangString.s13_H; s14_CP2 = LangString.s14_H; s15_CP2 = LangString.s15_H; s16_CP2 = LangString.s16_H; s17_CP2 = LangString.s17_H; s21_CP2 = LangString.s21_H; s22_CP2 = LangString.s22_H; s23_CP2 = LangString.s23_H;

        }


        if (sPlanguageCP3.getlanguageCP3().equals("en")){
            s1_CP3 = LangString.s1_E;  s2_CP3 = LangString.s2_E;  s3_CP3 = LangString.s3_E;  s4_CP3 = LangString.s4_E;  s5_CP3 = LangString.s5_E;  s6_CP3 = LangString.s6_E;  s7_CP3 = LangString.s7_E;  s8_CP3 = LangString.s8_E;  s9_CP3 = LangString.s9_E;  s10_CP3 = LangString.s10_E;  s11_CP3 = LangString.s11_E;  s12_CP3 = LangString.s12_E; s13_CP3 = LangString.s13_E; s14_CP3 = LangString.s14_E; s15_CP3 = LangString.s15_E; s16_CP3 = LangString.s16_E; s17_CP3 = LangString.s17_E; s21_CP3 = LangString.s21_E; s22_CP3 = LangString.s22_E; s23_CP3 = LangString.s23_E;
        }
        else if (sPlanguageCP3.getlanguageCP3().equals("ma")){
            s1_CP3 = LangString.s1_M;  s2_CP3 = LangString.s2_M;  s3_CP3 = LangString.s3_M;  s4_CP3 = LangString.s4_M;  s5_CP3 = LangString.s5_M;  s6_CP3 = LangString.s6_M;  s7_CP3 = LangString.s7_M;  s8_CP3 = LangString.s8_M;  s9_CP3 = LangString.s9_M;  s10_CP3 = LangString.s10_M; s11_CP3 = LangString.s11_M;  s12_CP3 = LangString.s12_M; s13_CP3 = LangString.s13_M; s14_CP3 = LangString.s14_M; s15_CP3 = LangString.s15_M; s16_CP3 = LangString.s16_M; s17_CP3 = LangString.s17_M; s21_CP3 = LangString.s21_M;  s22_CP3 = LangString.s22_M; s23_CP3 = LangString.s23_M;
        }
        else if (sPlanguageCP3.getlanguageCP3().equals("hi")){
            s1_CP3 = LangString.s1_H;  s2_CP3 = LangString.s2_H;  s3_CP3 = LangString.s3_H;  s4_CP3 = LangString.s4_H;  s5_CP3 = LangString.s5_H;  s6_CP3 = LangString.s6_H;  s7_CP3 = LangString.s7_H;  s8_CP3 = LangString.s8_H;  s9_CP3 = LangString.s9_H;  s10_CP3 = LangString.s10_H;  s11_CP3 = LangString.s11_H;  s12_CP3 = LangString.s12_H; s13_CP3 = LangString.s13_H; s14_CP3 = LangString.s14_H; s15_CP3 = LangString.s15_H; s16_CP3 = LangString.s16_H; s17_CP3 = LangString.s17_H; s21_CP3 = LangString.s21_H; s22_CP3 = LangString.s22_H; s23_CP3 = LangString.s23_H;
        }
        // s1_CP2 = "CHARGER IS ON";  s2_CP2 = "EMERGENCY STOPPED";  s3_CP2 = "OVER CURRENT";  s4_CP2 = "EARTH FAULT";  s5_CP2 = "TEMP HAZARD";  s6_CP2 = "POWER FAILURE";  s7_CP2 = "  VEHICLE\n PLUGED OUT";  s8_CP2 = "IDLE";  s9_CP2 = "  RESUMING\n  AFTER PF";  s10_CP2 = " CHARGING IN\n   PROCESS";  s11_CP2 = "  VEHICLE\n PLUGED IN";  s12_CP2 = "UNDER VOLTAGE"; s13_CP2 = "OVER VOLTAGE";
        //s1_CP3 = "CHARGER IS ON";  s2_CP3 = "EMERGENCY STOPPED";  s3_CP3 = "OVER CURRENT";  s4_CP3 = "EARTH FAULT";  s5_CP3 = "TEMP HAZARD";  s6_CP3 = "POWER FAILURE";  s7_CP3 = "  VEHICLE\n PLUGED OUT";  s8_CP3 = "IDLE";  s9_CP3 = "  RESUMING\n  AFTER PF";  s10_CP3 = " CHARGING IN\n   PROCESS";  s11_CP3 = "  VEHICLE\n PLUGED IN";  s12_CP3 = "UNDER VOLTAGE"; s13_CP3 = "OVER VOLTAGE";
    }

    private void customToast(String message)
    {
        LayoutInflater inflater = getLayoutInflater();
        View toastLayout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.llCustom));
        TextView textView = toastLayout.findViewById(R.id.customToastMsg);
        textView.setText(message);
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(toastLayout);
        toast.show();
    }
}
*/



///////----------------------------------------------------meterrerading change

/*


package com.virajmohite.a3phasecharger;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.database.FirebaseDatabase;
import com.virajmohite.a3phasecharger.PowerPlugged.Power;
import com.virajmohite.a3phasecharger.Storage.SPTimeReadingCP1;
import com.virajmohite.a3phasecharger.Storage.SPTimeReadingCP2;
import com.virajmohite.a3phasecharger.Storage.SPTimeReadingCP3;
import com.virajmohite.a3phasecharger.Storage.SPisPluggedin;
import com.virajmohite.a3phasecharger.Storage.SPisPoweFail;
import com.virajmohite.a3phasecharger.Storage.SPispowerfailafter4mrCP1;
import com.virajmohite.a3phasecharger.Storage.SPispowerfailafter4mrCP2;
import com.virajmohite.a3phasecharger.Storage.SPispowerfailafter4mrCP3;
import com.virajmohite.a3phasecharger.Storage.SPlanguageCP1;
import com.virajmohite.a3phasecharger.Storage.SPlanguageCP2;
import com.virajmohite.a3phasecharger.Storage.SPlanguageCP3;
import com.virajmohite.a3phasecharger.Storage.SPmeterReadingCP1;
import com.virajmohite.a3phasecharger.Storage.SPmeterReadingCP1RO;
import com.virajmohite.a3phasecharger.Storage.SPmeterReadingCP2;
import com.virajmohite.a3phasecharger.Storage.SPmeterReadingCP2RO;
import com.virajmohite.a3phasecharger.Storage.SPmeterReadingCP3;
import com.virajmohite.a3phasecharger.Storage.SPmeterReadingCP3RO;
import com.virajmohite.a3phasecharger.Storage.SPsavemoneyafterpfCP1;
import com.virajmohite.a3phasecharger.Storage.SharedPreferenceUnitR;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.UUID;


public class MaindisplayActivityMannual extends AppCompatActivity implements View.OnClickListener {

    CardView toggleBtn,btn_p2onff,btn_p3onff;

    TextView toggleValue,txt_p2btnValue,txt_p3btnValue;

    boolean flag=false,p2flag = false,p3flag= false;

    //---------- For Round Robin -------------//

    private int countC1,countC2,countC3;
    private boolean isAvailableC1,isAvailableC2,isAvailableC3;
    private boolean isStartC1,isStartC2,isStartC3;
    private boolean isStopC1,isStopC2,isStopC3;
    private Toast toast;

    //---------- For Round Robin -------------//

    //String red="#D74C34";
    String red="#f21200";
    String green="#3DAA4C";


    static Animation anim,anim2,anim3;


    private FirebaseDatabase mFirebaseInstance;
    Handler bluetoothIn;

    final int handlerState = 0;        				 //used to identify handler message
    int intervalcount = 0;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder recDataString = new StringBuilder();

    private ConnectedThread mConnectedThread;


    // SPP UUID service - this should work for most devices
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // String for MAC address
    private static String address;

    String command= "$000";

    //-------------design Variables
    int cnt=0;
    LinearLayout connect1,connect2,connect3,parent1;
    TextView txt_c1round_off_mr, txt_c2round_off_mr, txt_c3round_off_mr; //unit consumption variables
    TextView txt_c1diff,txt_c2diff,txt_c3diff; //time difference
    TextView tv_status1,tv_status2,tv_status3; // status

    //------------------ new architecture 09-01-2018
    private SPisPluggedin sPisPluggedin;
    private SPisPoweFail sPisPoweFail;
    private SPmeterReadingCP1 sPmeterReadingCP1;
    private SPmeterReadingCP2 sPmeterReadingCP2;
    private SPmeterReadingCP3 sPmeterReadingCP3;

    //------------------
    private SPTimeReadingCP1 sPTimeReadingCP1;
    private SPTimeReadingCP2 sPTimeReadingCP2;
    private SPTimeReadingCP3 sPTimeReadingCP3;


    //-----------------------CP1
    private int plugedinCountcp1 = 0;
    private int plugedoutCountcp1 = 0;
    private boolean isResumedAftercp1 = false;
    private boolean isStillOnCP1 = false;


    //-----------------------CP2
    private int plugedinCountcp2 = 0;
    private int plugedoutCountcp2 = 0;
    private boolean isResumedAftercp2 = false;
    private boolean isStillOnCP2 = false;

    //-----------------------CP3
    private int plugedinCountcp3 = 0;
    private int plugedoutCountcp3 = 0;
    private boolean isResumedAftercp3 = false;
    private boolean isStillOnCP3 = false;
    //time______________________CP1
    long MillisCP1 , DifferenceCP1 , NewBeginMillsCP1 , StartTimeCP1 = 0L ;
    int HoursCP1, MinutesCP1, SecondsCP1 ;
    //time______________________CP2
    long MillisCP2 , DifferenceCP2 , NewBeginMillsCP2 , StartTimeCP2 = 0L ;
    int HoursCP2, MinutesCP2, SecondsCP2 ;
    //time______________________CP3
    long MillisCP3 , DifferenceCP3 , NewBeginMillsCP3 , StartTimeCP3 = 0L ;
    int HoursCP3, MinutesCP3, SecondsCP3 ;

    //----------- optimize______20-01-2018
    LinearLayout layout_detail;
    FrameLayout layout_main;
    ImageView imageView_close;
    TextView txt_cpname,txt_rate,txt_voltage_detail,txt_current_detail,txt_power_detail,txt_unit_detail,txt_etime_detail,txt_status_detail;
    String voltage_detailCP1="000",current_detailCP1="00.00",power_detailCP1="000",unit_detailCP1="000", etime_detailCP1="00:00",status_detailCP1="IDLE";
    String voltage_detailCP2="000",current_detailCP2="00.00",power_detailCP2="000",unit_detailCP2="000", etime_detailCP2="00:00",status_detailCP2="IDLE";
    String voltage_detailCP3="000",current_detailCP3="00.00",power_detailCP3="000",unit_detailCP3="000", etime_detailCP3="00:00",status_detailCP3="IDLE";

    //----------
    String detail_flag = "";
    //--- for Rs
    String rsCP1 = "₹ 00.00", rsCP2 = "₹ 00.00", rsCP3 = "₹ 00.00",rsPOCP1 = "₹ 00.00", rsPOCP2 = "₹ 00.00", rsPOCP3 = "₹ 00.00",mrsPOCP1 = "₹ 00.00", mrsPOCP2 = "₹ 00.00", mrsPOCP3 = "₹ 00.00";
    //---for overlay________
    RelativeLayout relativeLayout1st,relativeLayout2nd,relativeLayout3rd;
    int overlayCounti = 0,overlayCountii = 0,overlayCountiii = 0;


    //language Selection
    private String s1_CP1,s2_CP1,s3_CP1,s4_CP1,s5_CP1,s6_CP1,s7_CP1,s8_CP1,s9_CP1,s10_CP1,s11_CP1,s12_CP1,s13_CP1,s14_CP1,s15_CP1,s16_CP1,s17_CP1,s18_CP1,s19_CP1,s20_CP1,s21_CP1,s22_CP1,s23_CP1,s24_CP1;
    private String s1_CP2,s2_CP2,s3_CP2,s4_CP2,s5_CP2,s6_CP2,s7_CP2,s8_CP2,s9_CP2,s10_CP2,s11_CP2,s12_CP2,s13_CP2,s14_CP2,s15_CP2,s16_CP2,s17_CP2,s18_CP2,s19_CP2,s20_CP2,s21_CP2,s22_CP2,s23_CP2,s24_CP2;
    private String s1_CP3,s2_CP3,s3_CP3,s4_CP3,s5_CP3,s6_CP3,s7_CP3,s8_CP3,s9_CP3,s10_CP3,s11_CP3,s12_CP3,s13_CP3,s14_CP3,s15_CP3,s16_CP3,s17_CP3,s18_CP3,s19_CP3,s20_CP3,s21_CP3,s22_CP3,s23_CP3,s24_CP3;
    private TextView txt_touch1,txt_touch2,txt_touch3,txt_etime_display1,txt_etime_display2,txt_etime_display3,txt_etime_displayf,txt_unit_display,txt_rate_display;

    private SPlanguageCP1 sPlanguageCP1;
    private SPlanguageCP2 sPlanguageCP2;
    private SPlanguageCP3 sPlanguageCP3;
    private SharedPreferenceUnitR sharedPreference;

    private RadioGroup rg_language_m;

    public boolean isAlreadyonm = true;
    Button c1,c2,c3,c4;
    private File myExternalFile;
    String filename = "Config.txt";
    String filepath = "MyFileStorage";
    String cid;
    String hname="sour";
    Float erate;
    String erate_s;
    int clickCount = 0;
    //------------
    private int countidleCP1 = 0,countidleCP2 = 0,countidleCP3 = 0;
    private RelativeLayout layout_goback ;


    private int displayPlugoutCountC1 = 0;
    private int displayPlugoutCountC2 = 0;
    private int displayPlugoutCountC3 = 0;
    boolean isTappedC1 = false, isTappedC2 = false, isTappedC3 = false;
    boolean isPleaseWaitC1 = false, isPleaseWaitC2 = false, isPleaseWaitC3 = false;

    //meterReadingChange-----------------------------------------------------------------------------------------------------------------------------------------
    SPispowerfailafter4mrCP1 sPispowerfailafter4mrCP1;
    SPispowerfailafter4mrCP2 sPispowerfailafter4mrCP2;
    SPispowerfailafter4mrCP3 sPispowerfailafter4mrCP3;
    //sharedPreferenceforNMeter data________________________________________________________________________________________
    SPmeterReadingCP1RO sPmeterReadingCP1RO;
    SPmeterReadingCP2RO sPmeterReadingCP2RO;
    SPmeterReadingCP3RO sPmeterReadingCP3RO;
    //-------save money
    SPsavemoneyafterpfCP1 sPsavemoneyafterpfCP1;
    private String globalP1meter = "0",globalP2meter="0",globalP3meter="0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        setContentView(R.layout.newdesignm);
        Log.e("CHECK", "CHECKING");

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




    @Override
    protected void onRestart() {
        super.onRestart();
        // Toast.makeText(MaindisplayActivity.this,"onRestart Method",Toast.LENGTH_LONG).show();
        Log.e("METHOD","onRestart Method");
    }

    @Override
    protected void onStart() {
        super.onStart();
        //  Toast.makeText(MaindisplayActivity.this,"onStart Method",Toast.LENGTH_LONG).show();
        Log.e("METHOD","onStart Method");
    }

    @Override
    public void onResume() {
        super.onResume();

        countC1 = 0 ;
        countC2 = 0 ;
        countC3 = 0 ;

        isAvailableC1 = true;
        isAvailableC2 = true;
        isAvailableC3 = true;

        isStopC1 = true;
        isStopC2 = true;
        isStopC3 = true;

        isStartC3 = true;
        isStartC2 = true;
        isStartC1 = true;


        c1 = findViewById(R.id.checkBox);
        c2 = findViewById(R.id.checkBox2);
        c3 = findViewById(R.id.checkBox3);
        c4 = findViewById(R.id.checkBox4);
        layout_goback =  findViewById(R.id.layout_goback);

        GetCid();
        Firebase.setAndroidContext(this);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickCount==3) {
                    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (mBluetoothAdapter.isEnabled()) {
                        mBluetoothAdapter.disable();
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(MaindisplayActivityMannual.this, Authentication.class);
                            String C_ID = cid.toString();

//Create the bundle
                            Bundle bundle = new Bundle();

//Add your data to bundle
                            bundle.putString("Cid", C_ID);

//Add the bundle to the intent
                            i.putExtras(bundle);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

//Fire that second activity
                            startActivity(i);
                            clickCount=0;

                        }
                    }, 3000);
                }
                else {
                    clickCount++;
                }
            }
        });
        sPlanguageCP1 = new SPlanguageCP1(MaindisplayActivityMannual.this);
        sPlanguageCP2 = new SPlanguageCP2(MaindisplayActivityMannual.this);
        sPlanguageCP3 = new SPlanguageCP3(MaindisplayActivityMannual.this);
        sharedPreference = new SharedPreferenceUnitR();
        if (sharedPreference.getValue(MaindisplayActivityMannual.this)== null){
            sharedPreference.save(MaindisplayActivityMannual.this,"7");
        }
        erate_s = sharedPreference.getValue(MaindisplayActivityMannual.this);
        erate = Float.parseFloat(erate_s);
        langSet();
        //  Toast.makeText(MaindisplayActivity.this,"onResume Method",Toast.LENGTH_LONG).show();
        Log.e("METHOD","onResume Method");
        sPisPluggedin = new SPisPluggedin(MaindisplayActivityMannual.this); // initiating sharedepreferences
        sPisPoweFail = new SPisPoweFail(MaindisplayActivityMannual.this); // initiating sharedepreferences
        sPmeterReadingCP1 = new SPmeterReadingCP1(MaindisplayActivityMannual.this); // initiating sharedepreferences
        sPmeterReadingCP2 = new SPmeterReadingCP2(MaindisplayActivityMannual.this); // initiating sharedepreferences
        sPmeterReadingCP3 = new SPmeterReadingCP3(MaindisplayActivityMannual.this); // initiating sharedepreferences
        sPTimeReadingCP1 = new SPTimeReadingCP1(MaindisplayActivityMannual.this); // initiating sharedepreferences
        sPTimeReadingCP2 = new SPTimeReadingCP2(MaindisplayActivityMannual.this); // initiating sharedepreferences
        sPTimeReadingCP3 = new SPTimeReadingCP3(MaindisplayActivityMannual.this); // initiating sharedepreferences
        sPispowerfailafter4mrCP1 = new SPispowerfailafter4mrCP1(MaindisplayActivityMannual.this); // initiating sharedepreferences
        sPispowerfailafter4mrCP2 = new SPispowerfailafter4mrCP2(MaindisplayActivityMannual.this); // initiating sharedepreferences
        sPispowerfailafter4mrCP3 = new SPispowerfailafter4mrCP3(MaindisplayActivityMannual.this); // initiating sharedepreferences
        sPmeterReadingCP1RO = new SPmeterReadingCP1RO(MaindisplayActivityMannual.this); // initiating sharedepreferences
        sPmeterReadingCP2RO = new SPmeterReadingCP2RO(MaindisplayActivityMannual.this); // initiating sharedepreferences
        sPmeterReadingCP3RO = new SPmeterReadingCP3RO(MaindisplayActivityMannual.this); // initiating sharedepreferences
        sPsavemoneyafterpfCP1 = new SPsavemoneyafterpfCP1(MaindisplayActivityMannual.this); // initiating sharedepreferences
        // Toast.makeText(MaindisplayActivity.this,"Before : "+sPisPluggedin.getisPluggedinCP1(),Toast.LENGTH_LONG).show();
        // Toast.makeText(MaindisplayActivity.this,"Before : "+sPisPluggedin.getisPluggedinCP2(),Toast.LENGTH_LONG).show();
        // Toast.makeText(MaindisplayActivity.this,"Before : "+sPisPluggedin.getisPluggedinCP2(),Toast.LENGTH_LONG).show();

        if (sPispowerfailafter4mrCP1.getispowerfailafter4mrCP1().isEmpty()){
            sPispowerfailafter4mrCP1.setispowerfailafter4mrCP1("f");
        }
        if (sPispowerfailafter4mrCP2.getispowerfailafter4mrCP2().isEmpty()){
            sPispowerfailafter4mrCP2.setispowerfailafter4mrCP2("f");
        }

        if(sPisPluggedin.getisPluggedinCP1().isEmpty()){
            sPisPluggedin.setisPluggedinCP1("f");
            // Toast.makeText(MaindisplayActivity.this,"sPisPluggedin.getisPluggedinCP1() "+sPisPluggedin.getisPluggedinCP1(),Toast.LENGTH_LONG).show();
        }
        // Toast.makeText(MaindisplayActivity.this,"Before : "+sPisPoweFail.getisPowerFailCP1(),Toast.LENGTH_LONG).show();

        if(sPisPoweFail.getisPowerFailCP1().isEmpty()){
            sPisPoweFail.setisPowerFailCP1("f");
            // Toast.makeText(MaindisplayActivity.this,"sPisPoweFail.getisPowerFailCP1() "+sPisPoweFail.getisPowerFailCP1(),Toast.LENGTH_LONG).show();

        }
        //----------------------------
        if(sPisPluggedin.getisPluggedinCP2().isEmpty()){
            sPisPluggedin.setisPluggedinCP2("f");
            // Toast.makeText(MaindisplayActivity.this,"getisPluggedinCP2() "+sPisPluggedin.getisPluggedinCP2(),Toast.LENGTH_LONG).show();
        }
        // Toast.makeText(MaindisplayActivity.this,"Before 2: "+sPisPoweFail.getisPowerFailCP2(),Toast.LENGTH_LONG).show();

        if(sPisPoweFail.getisPowerFailCP2().isEmpty()){
            sPisPoweFail.setisPowerFailCP2("f");
            // Toast.makeText(MaindisplayActivity.this,"sPisPoweFail.getisPowerFailCP1() "+sPisPoweFail.getisPowerFailCP2(),Toast.LENGTH_LONG).show();

        }

        //----------------------------
        if(sPisPluggedin.getisPluggedinCP3().isEmpty()){
            sPisPluggedin.setisPluggedinCP3("f");
            //Toast.makeText(MaindisplayActivity.this,"getisPluggedinCP3() "+sPisPluggedin.getisPluggedinCP3(),Toast.LENGTH_LONG).show();
        }
        // Toast.makeText(MaindisplayActivity.this,"Before 3: "+sPisPoweFail.getisPowerFailCP3(),Toast.LENGTH_LONG).show();

        if(sPisPoweFail.getisPowerFailCP3().isEmpty()){
            sPisPoweFail.setisPowerFailCP3("f");
            // Toast.makeText(MaindisplayActivity.this,"isPowerFailCP3() "+sPisPoweFail.getisPowerFailCP3(),Toast.LENGTH_LONG).show();

        }
        if (sPmeterReadingCP1.getMeterReadingCP1().isEmpty()){
            sPmeterReadingCP1.setMeterReadingCP1("00.00");
        }
        if (sPmeterReadingCP2.getMeterReadingCP2().isEmpty()){
            sPmeterReadingCP2.setMeterReadingCP2("00.00");
        }
        if (sPmeterReadingCP3.getMeterReadingCP3().isEmpty()){
            sPmeterReadingCP3.setMeterReadingCP3("00.00");
        }


        toggleBtn   =     findViewById(R.id.toggleBtn);
        btn_p2onff   =    findViewById(R.id.btn_p2onff);
        btn_p3onff   =    findViewById(R.id.btn_p3onff);

        toggleValue =     findViewById(R.id.toggleValue);
        txt_p2btnValue =  findViewById(R.id.txt_p2btnValue);
        txt_p3btnValue =  findViewById(R.id.txt_p3btnValue);



        //----------------------------------------------------------
        connect1=findViewById(R.id.ll_connect1);
        connect2=findViewById(R.id.ll_connect2);
        connect3=findViewById(R.id.ll_connect3);

        parent1=findViewById(R.id.ll_parent1);


        parent1.animate().translationY(0);

        //-------------------------------------------------------------
        /*/
/******** variables
 txt_c1round_off_mr = findViewById(R.id.tv_unit1);
 txt_c2round_off_mr = findViewById(R.id.tv_unit2);
 txt_c3round_off_mr = findViewById(R.id.tv_unit3);
 //--------------------------
 txt_c1diff = findViewById(R.id.tv_time1);
 txt_c2diff = findViewById(R.id.tv_time2);
 txt_c3diff = findViewById(R.id.tv_time3);
 //----------------------
 tv_status1 = findViewById(R.id.tv_status1);
 tv_status2 = findViewById(R.id.tv_status2);
 tv_status3 = findViewById(R.id.tv_status3);

 //for optimise
 layout_detail = findViewById(R.id.layout_detail);
 layout_main = findViewById(R.id.layout_main);
 imageView_close = findViewById(R.id.imageView_close);
 txt_cpname = findViewById(R.id.txt_cpname);
 txt_rate = findViewById(R.id.txt_rate);
 txt_voltage_detail = findViewById(R.id.txt_voltage_detail);
 txt_current_detail = findViewById(R.id.txt_current_detail);
 txt_power_detail = findViewById(R.id.txt_power_detail);
 txt_unit_detail = findViewById(R.id.txt_unit_detail);
 txt_etime_detail = findViewById(R.id.txt_etime_detail);
 txt_status_detail = findViewById(R.id.txt_status_detail);
 layout_main.setVisibility(View.VISIBLE);
 layout_detail.setVisibility(View.GONE);
 //touch...
 txt_touch1 = findViewById(R.id.txt_touch1);
 txt_touch2 = findViewById(R.id.txt_touch2);
 txt_touch3 = findViewById(R.id.txt_touch3);
 txt_etime_display1 = findViewById(R.id.txt_etime_display1);
 txt_etime_display2 = findViewById(R.id.txt_etime_display2);
 txt_etime_display3 = findViewById(R.id.txt_etime_display3);
 txt_etime_displayf = findViewById(R.id.txt_etime_displayf);
 txt_unit_display = findViewById(R.id.txt_unit_display);
 txt_rate_display = findViewById(R.id.txt_rate_display);

 //----------for Overlay28-1-18
 relativeLayout1st =  findViewById(R.id.connectionLayout1st);
 relativeLayout2nd =  findViewById(R.id.connectionLayout2nd);
 relativeLayout3rd =  findViewById(R.id.connectionLayout3rd);

 //-------------lang 30-1-18
 rg_language_m = findViewById(R.id.rg_language_m);

 rg_language_m.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
@Override
public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
switch(checkedId)
{
case R.id.rb_en_m:
// TODO Something
if (detail_flag.equals("d_CP1")) {
sPlanguageCP1.setlanguageCP1("en");
} else if (detail_flag.equals("d_CP2")) {
sPlanguageCP2.setlanguageCP2("en");

} else if (detail_flag.equals("d_CP3")) {
sPlanguageCP3.setlanguageCP3("en");

}

break;
case R.id.rb_hi_m:
// TODO Something
if (detail_flag.equals("d_CP1")) {
sPlanguageCP1.setlanguageCP1("hi");
} else if (detail_flag.equals("d_CP2")) {
sPlanguageCP2.setlanguageCP2("hi");

} else if (detail_flag.equals("d_CP3")) {
sPlanguageCP3.setlanguageCP3("hi");

}
break;
case R.id.rb_ma_m:
// TODO Something
if (detail_flag.equals("d_CP1")) {
sPlanguageCP1.setlanguageCP1("ma");
} else if (detail_flag.equals("d_CP2")) {
sPlanguageCP2.setlanguageCP2("ma");
} else if (detail_flag.equals("d_CP3")) {
sPlanguageCP3.setlanguageCP3("ma");
}
break;
}
}
});

 relativeLayout1st.setOnClickListener(new View.OnClickListener()
 {
 @Override
 public void onClick(View view) {
 isTappedC1 = true;
 isPleaseWaitC1 = true;
 if (!isAvailableC2 && !isAvailableC3 )
 {
 funConnectori();
 overlayCounti = 0;
 relativeLayout1st.setVisibility(View.GONE);
 Toast.makeText(getApplicationContext(),"CP2 And Cp3 Not Available",Toast.LENGTH_SHORT).show();
 }
 else if (isAvailableC2 && countC2!=0){
 // Toast.makeText(getApplicationContext(),"CP2  Available",Toast.LENGTH_SHORT).show();
 customToast(" Now CP2 Available...!!! ");
 countC2 = 0;
 }
 else if (isAvailableC3 && countC3 != 0){
 // Toast.makeText(getApplicationContext()," Cp3 Available",Toast.LENGTH_SHORT).show();
 customToast(" Now CP3 Available...!!! ");
 countC3 = 0;
 }
 if (countC1 == countC2 && countC2 == countC3 )
 {
 countC1 = 0;
 countC2 = 0;
 countC3 = 0;
 }

 if (countC1 == 0 && countC2 == 0 && countC3 == 0)
 {
 funConnectori();
 overlayCounti = 0;
 relativeLayout1st.setVisibility(View.GONE);

 }
 else if (countC1 > countC2 && countC1 > countC3) {

 if (isAvailableC2 && isAvailableC3){
 customToast("Please Use C2 or C3...!!!");
 }
 else if (isAvailableC2) {
 customToast("Please Use C2...!!!");
 } else if (isAvailableC3) {
 customToast("Please Use C3...!!!");
 } else {
 Toast.makeText(getApplicationContext(), "Not Available C2 Or C3...!!!", Toast.LENGTH_SHORT).show();
 countC1 = 0;
 funConnectori();
 overlayCounti = 0;
 relativeLayout1st.setVisibility(View.GONE);
 }

 }
 else if (countC1 < countC2 && countC1 < countC3)
 {
 funConnectori();
 overlayCounti = 0;
 relativeLayout1st.setVisibility(View.GONE);
 }
 else if (countC2 > countC1  && countC2 > countC3)
 {
 funConnectori();
 overlayCounti = 0;
 relativeLayout1st.setVisibility(View.GONE);
 }
 else if (countC3 > countC1 && countC3 > countC2)
 {
 funConnectori();
 overlayCounti = 0;
 relativeLayout1st.setVisibility(View.GONE);
 }
 else if (countC2 < countC1 && countC2 < countC3)
 {
 if (isAvailableC2)
 {
 customToast("Please Use C2...!!!");
 }
 }
 else if (countC3 < countC1 && countC3 < countC2)
 {
 if (isAvailableC3)
 {
 customToast("Please Use C3...!!!");
 }
 }

 }

 });


 relativeLayout2nd.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
isTappedC2 = true;
isPleaseWaitC2 = true;

if (!isAvailableC3 && !isAvailableC1)
{
funConnectorii();
overlayCountii = 0;
relativeLayout2nd.setVisibility(View.GONE);
}
else if (isAvailableC1 && countC2!=0){
// Toast.makeText(getApplicationContext(),"CP1  Available",Toast.LENGTH_SHORT).show();
customToast(" Now CP1 Available...!!! ");
countC2 = 0;
}
else if (isAvailableC3 && countC3!=0){
//Toast.makeText(getApplicationContext()," Cp3 Available",Toast.LENGTH_SHORT).show();
customToast(" Now CP3 Available...!!! ");
countC3 = 0;
}

if (countC1 == countC2 && countC2 == countC3 )
{
countC1 = 0;
countC2 = 0;
countC3 = 0;

}

if (countC1 == 0 && countC2 == 0 && countC3 == 0)
{
funConnectorii();
overlayCountii = 0;
relativeLayout2nd.setVisibility(View.GONE);


}
else if (countC2 > countC1 && countC2 > countC3)
{
if (isAvailableC1 && isAvailableC3){
customToast("Please Use C1 or C3...!!!");
}
else if (isAvailableC1)
{
customToast("Please Use C1...!!!");
}
else if (isAvailableC3)
{
customToast("Please Use C3...!!!");
}
else
{
Toast.makeText(getApplicationContext(),"Not Available C1 Or C3...!!!",Toast.LENGTH_SHORT).show();
countC2 = 0;
funConnectorii();
overlayCountii = 0;
relativeLayout2nd.setVisibility(View.GONE);
}
}
else if (countC2 < countC1 && countC2 < countC3)
{
funConnectorii();
overlayCountii = 0;
relativeLayout2nd.setVisibility(View.GONE);
}
else if (countC1 > countC2  && countC1 > countC3)
{
funConnectorii();
overlayCountii = 0;
relativeLayout2nd.setVisibility(View.GONE);
}
else if (countC3 > countC1 && countC3 > countC2)
{
funConnectorii();
overlayCountii = 0;
relativeLayout2nd.setVisibility(View.GONE);
}
else if (countC1 < countC2 && countC1 < countC3)
{
if (isAvailableC1)
{
customToast("Please Use C1...!!!");
}
}
else if (countC3 < countC1 && countC3 < countC2)
{
if (isAvailableC3)
{
customToast("Please Use C3...!!!");
}
}


}
});


 relativeLayout3rd.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
isTappedC3 = true;
isPleaseWaitC3 = true;

if (!isAvailableC1 && !isAvailableC2)
{
funConnectoriii();
overlayCountiii = 0;
relativeLayout3rd.setVisibility(View.GONE);
}
else if (isAvailableC1 && countC1!= 0){
// Toast.makeText(getApplicationContext(),"CP1  Available",Toast.LENGTH_SHORT).show();
customToast(" Now CP1 Available ...!!! ");
countC1 = 0;
}
else if (isAvailableC2 && countC2!=0){
//    Toast.makeText(getApplicationContext()," Cp2 Available",Toast.LENGTH_SHORT).show();
customToast(" Now CP2 Available...!!! ");
countC2 = 0;
}
if (countC1 == countC2 && countC2 == countC3) {
countC1 = 0;
countC2 = 0;
countC3 = 0;
}

if (countC1 == 0 && countC2 == 0 && countC3 == 0) {
funConnectoriii();
overlayCountiii = 0;
relativeLayout3rd.setVisibility(View.GONE);

} else if (countC3 > countC1 && countC3 > countC2) {
if (isAvailableC2 && isAvailableC3){
customToast("Please Use C1 or C2...!!!");
}
else if (isAvailableC1) {
customToast("Please Use C1...!!!");
} else if (isAvailableC2) {
customToast("Please Use C2...!!!");
} else {
Toast.makeText(getApplicationContext(), " Not Available C1 Or C2...!!!", Toast.LENGTH_SHORT).show();
countC3 = 0;
funConnectoriii();
overlayCountiii = 0;
relativeLayout3rd.setVisibility(View.GONE);
}
} else if (countC3 < countC1 && countC3 < countC2) {
funConnectoriii();
overlayCountiii = 0;
relativeLayout3rd.setVisibility(View.GONE);
} else if (countC1 > countC2 && countC1 > countC3) {
funConnectoriii();
overlayCountiii = 0;
relativeLayout3rd.setVisibility(View.GONE);
}
else if (countC2 > countC1 && countC2 > countC3)
{
funConnectoriii();
overlayCountiii = 0;
relativeLayout3rd.setVisibility(View.GONE);
}
else if (countC1 < countC2 && countC1 < countC3)
{
if (isAvailableC1)
{
customToast("Please Use C1 ...!!!");
}

}
else if (countC2 < countC1 && countC2 < countC3)
{
if (isAvailableC2)
{
customToast("Please Use C2...!!!");
}
}




}
});

 imageView_close.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
layout_main.setVisibility(View.VISIBLE);
layout_detail.setVisibility(View.GONE);
detail_flag = "c";

}
});

 connect1.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
layout_main.setVisibility(View.GONE);
layout_detail.setVisibility(View.VISIBLE);
detail_flag = "d_CP1";
}
});
 connect2.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
layout_main.setVisibility(View.GONE);
layout_detail.setVisibility(View.VISIBLE);
detail_flag = "d_CP2";
}
});
 connect3.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
layout_main.setVisibility(View.GONE);
layout_detail.setVisibility(View.VISIBLE);
detail_flag = "d_CP3";
}
});
 layout_goback.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
Intent i = new Intent(MaindisplayActivityMannual.this, LangSelection.class);
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

 final String s = "#234990.00+~";

 anim = new AlphaAnimation(0.1f, 1.0f);
 anim.setDuration(300);
 anim.setRepeatMode(Animation.RESTART);
 anim.setRepeatCount(Animation.INFINITE);
 anim2 = new AlphaAnimation(0.1f, 1.0f);
 anim2.setDuration(300);
 anim2.setRepeatMode(Animation.RESTART);
 anim2.setRepeatCount(Animation.INFINITE);
 anim3 = new AlphaAnimation(0.1f, 1.0f);
 anim3.setDuration(300);
 anim3.setRepeatMode(Animation.RESTART);
 anim3.setRepeatCount(Animation.INFINITE);


 //
 setToggleBtn();
 new Handler().postDelayed(new Runnable() {
@Override
public void run() {
mConnectedThread.write("c");
//  Toast.makeText(MaindisplayActivity.this,"Thread",Toast.LENGTH_LONG).show();

Log.e("mConnectedThreadSTRING","c : "+intervalcount);
intervalcount++;
}
}, 5000);

 bluetoothIn = new Handler() {
 public void handleMessage(android.os.Message msg) {

 if (msg.what == handlerState) {                                        //if message is what we want
 isAlreadyonm = true;
 if (Power.isConnected(MaindisplayActivityMannual.this)) {
 String readMessage = (String) msg.obj;                                                                // msg.arg1 = bytes from connect thread
 recDataString.append(readMessage);                                    //keep appending to string until ~
 int endOfLineIndex = recDataString.indexOf("~");                    // determine the end-of-line
 //if (endOfLineIndex > 0 && endOfLineIndex < 80) {
 if (endOfLineIndex > 0) {
 langSet();
 if (isTappedC1){
 txt_touch1.setText("C1\n"+s24_CP1);
 }else{
 txt_touch1.setText("C1\n"+s21_CP1);

 }

 if (isTappedC2){
 txt_touch2.setText("C2\n"+s24_CP2);
 }else{
 txt_touch2.setText("C2\n"+s21_CP2);

 }

 if (isTappedC3){
 txt_touch3.setText("C3\n"+s24_CP3);
 }else{
 txt_touch3.setText("C3\n"+s21_CP3);

 }
 txt_etime_display1.setText(s14_CP1);
 txt_etime_display2.setText(s14_CP2);
 txt_etime_display3.setText(s14_CP3);
 // make sure there data before ~
 String dataInPrint = recDataString.substring(0, endOfLineIndex);    // extract string
 // txtString.setText("Data Received = " + dataInPrint);
 int dataLength = dataInPrint.length();                            //get length of data received
 //txtStringLength.setText("String Length = " + String.valueOf(dataLength));
 if (recDataString.charAt(0) == '#')                                //if it starts with # we know it is what we are looking for
 {
 try {
 if(countidleCP1 > 400 && countidleCP2 > 400 && countidleCP3 > 400){
 layout_detail.setVisibility(View.GONE);
 layout_main.setVisibility(View.GONE);
 layout_goback.setVisibility(View.VISIBLE);
 }
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
 globalP1meter= p1meter;
 String p2OnOff = recDataString.substring(recDataString.indexOf("e") + 1, recDataString.indexOf("e") + 2);
 String p2plugin = recDataString.substring(recDataString.indexOf("e") + 2, recDataString.indexOf("e") + 3);
 String p2fault = recDataString.substring(recDataString.indexOf("e") + 3, recDataString.indexOf("f"));
 String p2voltage = recDataString.substring(p2startV, p2endV);
 String p2current = recDataString.substring(p2startC, p2endC);
 String p2meter = recDataString.substring(p2startM, p2endM);
 globalP2meter= p2meter;
 String p3OnOff = recDataString.substring(recDataString.indexOf("k") + 1, recDataString.indexOf("k") + 2);
 String p3plugin = recDataString.substring(recDataString.indexOf("k") + 2, recDataString.indexOf("k") + 3);
 String p3fault = recDataString.substring(recDataString.indexOf("k") + 3, recDataString.indexOf("l"));
 String p3voltage = recDataString.substring(p3startV, p3endV);
 String p3current = recDataString.substring(p3startC, p3endC);
 String p3meter = recDataString.substring(p3startM, p3endM);
 String emergency = recDataString.substring(p3endM + 1, p3endM + 2);
 voltage_detailCP1 = p1voltage;
 current_detailCP1 = p1current;
 power_detailCP1 = p1meter;
 voltage_detailCP2 = p2voltage;
 current_detailCP2 = p2current;
 power_detailCP2 = p2meter;
 voltage_detailCP3 = p3voltage;
 current_detailCP3 = p3current;
 power_detailCP3 = p3meter;
 mFirebaseInstance.getReference(cid+"-CP1M").setValue(tv_status1.getText().toString());
 mFirebaseInstance.getReference(cid+"-CP2M").setValue(tv_status2.getText().toString());
 mFirebaseInstance.getReference(cid+"-CP3M").setValue(tv_status3.getText().toString());
 mFirebaseInstance.getReference(cid+"-CP1-VoltageM").setValue(p1voltage);
 mFirebaseInstance.getReference(cid+"-CP2-VoltageM").setValue(p2voltage);
 mFirebaseInstance.getReference(cid+"-CP3-VoltageM").setValue(p3voltage);
 mFirebaseInstance.getReference(cid+"-CP1-CurrentM").setValue(p1current);
 mFirebaseInstance.getReference(cid+"-CP2-CurrentM").setValue(p2current);
 mFirebaseInstance.getReference(cid+"-CP3-CurrentM").setValue(p3current);
 mFirebaseInstance.getReference(cid+"-CP1-PowerM").setValue(p1meter);
 mFirebaseInstance.getReference(cid+"-CP2-PowerM").setValue(p2meter);
 mFirebaseInstance.getReference(cid+"-CP3-PowerM").setValue(p3meter);

 mFirebaseInstance.getReference(cid+"-CP1-AmountM").setValue(txt_c1round_off_mr.getText().toString());
 mFirebaseInstance.getReference(cid+"-CP2-AmountM").setValue(txt_c2round_off_mr.getText().toString());
 mFirebaseInstance.getReference(cid+"-CP3-AmountM").setValue(txt_c3round_off_mr.getText().toString());


 //   txt_c1round_off_mr

 if (p1OnOff.equals("0")) {
 if (sPisPoweFail.getisPowerFailCP1().equals("t") && sPisPluggedin.getisPluggedinCP1().equals("t")) {
 toggleBtn.performClick();
 } else {
 flag = false;
 setToggleBtn();
 }
 } else {
 flag = true;
 setToggleBtn();
 }

 if (p2OnOff.equals("0")) {

 if (sPisPoweFail.getisPowerFailCP2().equals("t") && sPisPluggedin.getisPluggedinCP2().equals("t")) {
 btn_p2onff.performClick();
 } else {
 p2flag = false;
 setP2Btn();
 }

 } else {
 p2flag = true;
 setP2Btn();
 }

 if (p3OnOff.equals("0")) {
 if (sPisPoweFail.getisPowerFailCP3().equals("t") && sPisPluggedin.getisPluggedinCP3().equals("t")) {
 btn_p3onff.performClick();
 } else {
 p3flag = false;
 setP3Btn();
 }
 } else {
 p3flag = true;
 setP3Btn();
 }
 if (emergency.equals("1"))
 {
 tv_status1.setText(s2_CP1);
 tv_status2.setText(s2_CP2);
 tv_status3.setText(s2_CP2);
 status_detailCP1 = s2_CP1;
 status_detailCP2 = s2_CP2;
 status_detailCP3 = s2_CP3;

 } else {

 //---------------------------------------------------------------------------------------------------------

 if (p1OnOff.equals("0") && p1plugin.equals("0") && p1fault.equals("03")) {
 tv_status1.setText(s3_CP1);
 status_detailCP1 = s3_CP1;
 }
 if (p2OnOff.equals("0") && p2plugin.equals("0") && p2fault.equals("03")) {
 tv_status2.setText(s3_CP2);
 status_detailCP2 = s3_CP2;
 }
 if (p3OnOff.equals("0") && p3plugin.equals("0") && p3fault.equals("03")) {
 tv_status3.setText(s3_CP3);
 status_detailCP3 = s3_CP3;
 }
 //fault--------------------------------------------------------------------
 if (p1fault.equals("05")) {

 if (sPisPluggedin.getisPluggedinCP1().equals("t")) {
 sPisPoweFail.setisPowerFailCP1("t");
 sPTimeReadingCP1.setTimeReadingCP1(MillisCP1);
 isStillOnCP1 = true;
 }
 Float cp1v = Float.parseFloat(p1voltage);
 if (cp1v > 263)
 {
 tv_status1.setText(s13_CP1);
 status_detailCP1 = s13_CP1;
 }
 else if (cp1v < 186 && cp1v > 150)
 {
 tv_status1.setText(s12_CP1);
 status_detailCP1 = s12_CP1;
 }
 else
 {
 tv_status1.setText(s6_CP1);
 status_detailCP1 = s6_CP1;
 toggleBtn.setCardBackgroundColor(Color.parseColor(red));
 toggleBtn.startAnimation(anim);
 //---------------
 String s = globalP1meter;
 float v = Float.parseFloat(s);
 int b=(int) v;
 if (b>=4) {
 sPispowerfailafter4mrCP1.setispowerfailafter4mrCP1("t");
 sPsavemoneyafterpfCP1.setMeterReadingCP1RO(mrsPOCP1);
 }
 }
 //}

 }
 if (p2fault.equals("05")) {
 if (sPisPluggedin.getisPluggedinCP2().equals("t")) {
 sPisPoweFail.setisPowerFailCP2("t");
 sPTimeReadingCP2.setTimeReadingCP2(MillisCP2);
 isStillOnCP2 = true;
 }
 Float cp2v = Float.parseFloat(p2voltage);
 if (cp2v > 263) {
 tv_status2.setText(s13_CP2);
 status_detailCP2 = s13_CP2;

 } else if (cp2v < 186 && cp2v > 150) {
 tv_status2.setText(s12_CP2);
 status_detailCP2 = s12_CP2;


 } else {
 tv_status2.setText(s6_CP2);
 status_detailCP2 = s6_CP2;
 btn_p2onff.setCardBackgroundColor(Color.parseColor(red));
 btn_p2onff.startAnimation(anim);

 String s = globalP2meter;
 float v = Float.parseFloat(s);
 int b=(int) v;
 if (b>=4) {
 sPispowerfailafter4mrCP2.setispowerfailafter4mrCP2("t");
 }

 }


 }
 if (p3fault.equals("05")) {

 if (sPisPluggedin.getisPluggedinCP3().equals("t")) {
 sPisPoweFail.setisPowerFailCP3("t");
 sPTimeReadingCP3.setTimeReadingCP3(MillisCP3);
 isStillOnCP3 = true;
 }
 Float cp3v = Float.parseFloat(p3voltage);
 if (cp3v > 263) {
 tv_status3.setText(s13_CP3);
 status_detailCP3 = s13_CP3;

 } else if (cp3v < 186 && cp3v > 150) {
 tv_status3.setText(s12_CP3);
 status_detailCP3 = s12_CP3;

 } else {
 tv_status3.setText(s6_CP3);
 status_detailCP3 = s6_CP3;
 btn_p3onff.setCardBackgroundColor(Color.parseColor(red));
 btn_p3onff.startAnimation(anim);

 }

 }
 //--------------Earth Fault
 if (p1fault.equals("10")) {
 tv_status1.setText(s4_CP1);
 status_detailCP1 = s4_CP1;

 }
 if (p2fault.equals("10")) {
 tv_status2.setText(s4_CP2);
 status_detailCP2 = s4_CP2;

 }
 if (p3fault.equals("10")) {
 tv_status3.setText(s4_CP3);
 status_detailCP3 = s4_CP3;

 }
 //---------------TEMP HAZARD
 if (p1fault.equals("07")) {
 tv_status1.setText(s5_CP1);
 status_detailCP1 = s5_CP1;


 }
 if (p2fault.equals("07")) {
 tv_status2.setText(s5_CP2);
 status_detailCP2 = s5_CP2;

 }
 if (p3fault.equals("07")) {
 tv_status3.setText(s5_CP3);
 status_detailCP3 = s5_CP3;

 }


 //-----------------------------------------------------------CP1 Status-----------------------------------------------------------------------------------------------------------------------------
 if (p1OnOff.equals("0") && p1plugin.equals("0") && p1fault.equals("99")) {

 if (sPisPluggedin.getisPluggedinCP1().equals("t")) {

 if (plugedoutCountcp1 >= 30) {
 sPisPluggedin.setisPluggedinCP1("f");

 } else {
 displayPlugoutCountC1 = 0;
 tv_status1.setText(s7_CP1);
 txt_c1round_off_mr.setText(rsPOCP1);

 rsCP1 = "₹ 00.00";
 txt_c1diff.setText(etime_detailCP1);

 status_detailCP1 = s7_CP1;
 unit_detailCP1 = unit_detailCP1;
 etime_detailCP1 = etime_detailCP1;
 sPispowerfailafter4mrCP1.setispowerfailafter4mrCP1("f");

 plugedinCountcp1 = 0;
 plugedoutCountcp1++;
 if (isResumedAftercp1) {
 sPisPoweFail.setisPowerFailCP1("f");
 }
 }

 } else {
 countidleCP1++;
 if (isPleaseWaitC1){
 tv_status1.setText("Please Wait...");
 status_detailCP1 = "Please Wait...";
 toggleBtn.setVisibility(View.INVISIBLE);

 }else{
 tv_status1.setText(s8_CP1);
 status_detailCP1 = s8_CP1;

 }


 if (isStopC1)
 {
 //   countC1 = 0;
 isAvailableC1 = true;

 isStartC1 = true;
 isStopC1 = false;
 }

 txt_c1round_off_mr.setText("₹ 00.00");
 rsCP1 = "₹ 00.00";
 rsPOCP1 = "₹ 00.00";
 //------------
 float mrv = 0;
 mrsPOCP1 = ""+mrv;
 //-----------
 txt_c1diff.setText("00:00:00");
 unit_detailCP1 = "00.00";
 etime_detailCP1 = "00:00:00";
 plugedinCountcp1 = 0;
 toggleBtn.clearAnimation();
 //overlay1
 if (overlayCounti> 10) {
 isPleaseWaitC1 = false;
 relativeLayout1st.setVisibility(View.VISIBLE);
 }
 overlayCounti++;
 }

 }
 if (p1OnOff.equals("1") && p1plugin.equals("0") && p1fault.equals("99")) {
 countidleCP1 = 0;
 plugedinCountcp1 = 0;
 plugedoutCountcp1 = 0;
 tv_status1.setText(s1_CP1);
 isTappedC1 = false;
 isPleaseWaitC1 = false;

 if (isStartC1)
 {
 isStartC1 = false;
 countC1++;
 isStopC1 = true;
 isAvailableC1 = false;
 }
 txt_c1round_off_mr.setText("₹ 00.00");
 rsCP1 = "₹ 00.00";
 txt_c1diff.setText("00:00:00");
 status_detailCP1 = s1_CP1;
 unit_detailCP1 = "00.00";
 etime_detailCP1 = "00:00:00";
 isStillOnCP1 = false;
 relativeLayout1st.setVisibility(View.GONE);
 toggleBtn.setVisibility(View.VISIBLE);

 if (displayPlugoutCountC1 == 12) {
 if (sPisPoweFail.getisPowerFailCP1().equals("t") && sPisPluggedin.getisPluggedinCP1().equals("t")) {
 toggleBtn.performClick();
 sPisPoweFail.setisPowerFailCP1("f");
 sPisPluggedin.setisPluggedinCP1("f");
 tv_status1.setText(s7_CP1);
 status_detailCP1 = s7_CP1;
 sPispowerfailafter4mrCP1.setispowerfailafter4mrCP1("f");

 //-----------
 sPisPoweFail.setisPowerFailCP1("f");
 sPTimeReadingCP1.setTimeReadingCP1(MillisCP1);
 }
 }
 displayPlugoutCountC1++;
 }
 if (p1OnOff.equals("1") && p1plugin.equals("1") && p1fault.equals("99")) {
 countidleCP1 = 0;
 if (sPisPoweFail.getisPowerFailCP1().equals("t")) {
 //---------Status:resuming after power fail_______________
 if (plugedinCountcp1 >= 10) {
 //time reading______
 MillisCP1 = (SystemClock.uptimeMillis() - StartTimeCP1);

 HoursCP1 = (int) (MillisCP1 / (1000 * 60 * 60));
 MinutesCP1 = (int) (MillisCP1 / (1000 * 60)) % 60;
 SecondsCP1 = (int) (MillisCP1 / 1000) % 60;
 String time_readingCP1 = "" + String.format("%02d", HoursCP1) + ":"
 + String.format("%02d", MinutesCP1) + ":"
 + String.format("%02d", SecondsCP1);
 //-------------------------meter reading
 float prev_mrCP1 = Float.parseFloat(sPmeterReadingCP1.getMeterReadingCP1());
 float current_mrCP1 = 0;
 if (sPispowerfailafter4mrCP1.getispowerfailafter4mrCP1().equals("t")){
 current_mrCP1 = Float.parseFloat(sPmeterReadingCP1RO.getMeterReadingCP1RO())+Float.parseFloat(p1meter);

 }
 if (sPispowerfailafter4mrCP1.getispowerfailafter4mrCP1().equals("f")){
 current_mrCP1 = Float.parseFloat(p1meter);
 int b=(int) current_mrCP1;
 sPmeterReadingCP1RO.setMeterReadingCP1RO(""+b);
 }
 float meterreadingdiff = prev_mrCP1 - current_mrCP1;
 Log.e("MeterReadingC1",""+(meterreadingdiff = prev_mrCP1 - current_mrCP1));
 float meterreadingdiffP = 0;
 if (sPispowerfailafter4mrCP1.getispowerfailafter4mrCP1().equals("t")){
 String s = sPsavemoneyafterpfCP1.getMeterReadingCP1RO();
 float v = Float.parseFloat(s);
 meterreadingdiffP = v + ((current_mrCP1 - prev_mrCP1) * erate);

 }
 if (sPispowerfailafter4mrCP1.getispowerfailafter4mrCP1().equals("f")){
 meterreadingdiffP = (current_mrCP1 - prev_mrCP1) * erate;

 }
 String meterreadingCP1 = String.format("%.2f", meterreadingdiff);
 String meterreadingCP1P = String.format("%.2f", meterreadingdiffP);

 //------------------
 tv_status1.setText(s10_CP1);
 if (meterreadingdiffP > 0 && meterreadingdiffP < 100) {
 txt_c1round_off_mr.setText("₹ " + meterreadingCP1P);
 rsCP1 = "₹ " + meterreadingCP1P;
 rsPOCP1 = "₹ " + meterreadingCP1P;
 //------------
 float mrv = meterreadingdiffP;
 mrsPOCP1 = ""+mrv;
 //-----------
 }
 txt_c1diff.setText(time_readingCP1);
 status_detailCP1 = s10_CP1;
 unit_detailCP1 = meterreadingCP1;
 etime_detailCP1 = time_readingCP1;
 sPisPoweFail.setisPowerFailCP1("f");
 relativeLayout1st.setVisibility(View.GONE);

 } else if (plugedinCountcp1 == 0) {
 //Time reading_________________

 StartTimeCP1 = SystemClock.uptimeMillis() + (-sPTimeReadingCP1.getTimeReadingCP1());
 //-------------------------meter reading
 float prev_mrCP1 = Float.parseFloat(sPmeterReadingCP1.getMeterReadingCP1());
 float current_mrCP1 = 0;
 if (sPispowerfailafter4mrCP1.getispowerfailafter4mrCP1().equals("t")){
 current_mrCP1 = Float.parseFloat(sPmeterReadingCP1RO.getMeterReadingCP1RO())+Float.parseFloat(p1meter);

 }
 if (sPispowerfailafter4mrCP1.getispowerfailafter4mrCP1().equals("f")){
 current_mrCP1 = Float.parseFloat(p1meter);
 int b=(int) current_mrCP1;
 sPmeterReadingCP1RO.setMeterReadingCP1RO(""+b);
 }
 float meterreadingdiff = current_mrCP1 - prev_mrCP1;
 float meterreadingdiffP = 0;
 if (sPispowerfailafter4mrCP1.getispowerfailafter4mrCP1().equals("t")){
 String s = sPsavemoneyafterpfCP1.getMeterReadingCP1RO();
 float v = Float.parseFloat(s);
 meterreadingdiffP = v + ((current_mrCP1 - prev_mrCP1) * erate);

 }
 if (sPispowerfailafter4mrCP1.getispowerfailafter4mrCP1().equals("f")){
 meterreadingdiffP = (current_mrCP1 - prev_mrCP1) * erate;

 }
 String meterreadingCP1 = String.format("%.2f", meterreadingdiff);
 String meterreadingCP1P = String.format("%.2f", meterreadingdiffP);
 //--------------------
 tv_status1.setText(s9_CP1);
 if (meterreadingdiffP > 0 && meterreadingdiffP < 100) {
 txt_c1round_off_mr.setText("₹ " + meterreadingCP1P);
 rsCP1 = "₹ " + meterreadingCP1P;
 rsPOCP1 = "₹ " + meterreadingCP1P;
 //------------
 float mrv = meterreadingdiffP;
 mrsPOCP1 = ""+mrv;
 //-----------
 }
 txt_c1diff.setText("00:00:00");
 status_detailCP1 = s9_CP1;
 unit_detailCP1 = meterreadingCP1;
 etime_detailCP1 = "00:00:00";
 plugedinCountcp1++;
 sPisPluggedin.setisPluggedinCP1("t");
 isResumedAftercp1 = true;
 toggleBtn.startAnimation(anim);
 relativeLayout1st.setVisibility(View.GONE);



 } else {
 //time reading______
 MillisCP1 = (SystemClock.uptimeMillis() - StartTimeCP1);

 HoursCP1 = (int) (MillisCP1 / (1000 * 60 * 60));
 MinutesCP1 = (int) (MillisCP1 / (1000 * 60)) % 60;
 SecondsCP1 = (int) (MillisCP1 / 1000) % 60;
 String time_readingCP1 = "" + String.format("%02d", HoursCP1) + ":"
 + String.format("%02d", MinutesCP1) + ":"
 + String.format("%02d", SecondsCP1);

 //-------------------------meter reading
 float prev_mrCP1 = Float.parseFloat(sPmeterReadingCP1.getMeterReadingCP1());
 float current_mrCP1 = 0;
 if (sPispowerfailafter4mrCP1.getispowerfailafter4mrCP1().equals("t")){
 current_mrCP1 = Float.parseFloat(sPmeterReadingCP1RO.getMeterReadingCP1RO())+Float.parseFloat(p1meter);

 }
 if (sPispowerfailafter4mrCP1.getispowerfailafter4mrCP1().equals("f")){
 current_mrCP1 = Float.parseFloat(p1meter);
 int b=(int) current_mrCP1;
 sPmeterReadingCP1RO.setMeterReadingCP1RO(""+b);
 }
 float meterreadingdiff = current_mrCP1 - prev_mrCP1;
 float meterreadingdiffP = 0;
 if (sPispowerfailafter4mrCP1.getispowerfailafter4mrCP1().equals("t")){
 String s = sPsavemoneyafterpfCP1.getMeterReadingCP1RO();
 float v = Float.parseFloat(s);
 meterreadingdiffP = v + ((current_mrCP1 - prev_mrCP1) * erate);

 }
 if (sPispowerfailafter4mrCP1.getispowerfailafter4mrCP1().equals("f")){
 meterreadingdiffP = (current_mrCP1 - prev_mrCP1) * erate;

 }
 String meterreadingCP1 = String.format("%.2f", meterreadingdiff);
 String meterreadingCP1P = String.format("%.2f", meterreadingdiffP);
 //--------------------

 tv_status1.setText(s9_CP1);
 if (meterreadingdiffP > 0 && meterreadingdiffP < 100) {
 txt_c1round_off_mr.setText("₹ " + meterreadingCP1P);
 rsCP1 = "₹ " + meterreadingCP1P;
 rsPOCP1 = "₹ " + meterreadingCP1P;
 //------------
 float mrv = meterreadingdiffP;
 mrsPOCP1 = ""+mrv;
 //-----------
 }
 txt_c1diff.setText(time_readingCP1);
 status_detailCP1 = s9_CP1;
 unit_detailCP1 = meterreadingCP1;
 etime_detailCP1 = time_readingCP1;
 plugedinCountcp1++;
 sPisPluggedin.setisPluggedinCP1("t");
 isResumedAftercp1 = true;
 relativeLayout1st.setVisibility(View.GONE);


 }


 } else {
 //--------------- Status:vehicle pugged in_________
 if (plugedinCountcp1 >= 10) {
 //time reading______
 MillisCP1 = (SystemClock.uptimeMillis() - StartTimeCP1);

 HoursCP1 = (int) (MillisCP1 / (1000 * 60 * 60));
 MinutesCP1 = (int) (MillisCP1 / (1000 * 60)) % 60;
 SecondsCP1 = (int) (MillisCP1 / 1000) % 60;
 String time_readingCP1 = "" + String.format("%02d", HoursCP1) + ":"
 + String.format("%02d", MinutesCP1) + ":"
 + String.format("%02d", SecondsCP1);

 //-------------------------meter reading
 float prev_mrCP1 = Float.parseFloat(sPmeterReadingCP1.getMeterReadingCP1());

 float current_mrCP1 = 0;
 if (sPispowerfailafter4mrCP1.getispowerfailafter4mrCP1().equals("t")){
 current_mrCP1 = Float.parseFloat(sPmeterReadingCP1RO.getMeterReadingCP1RO())+Float.parseFloat(p1meter);

 }
 if (sPispowerfailafter4mrCP1.getispowerfailafter4mrCP1().equals("f")){
 current_mrCP1 = Float.parseFloat(p1meter);
 int b=(int) current_mrCP1;
 sPmeterReadingCP1RO.setMeterReadingCP1RO(""+b);
 }
 float meterreadingdiff = current_mrCP1 - prev_mrCP1;
 float meterreadingdiffP = 0;
 if (sPispowerfailafter4mrCP1.getispowerfailafter4mrCP1().equals("t")){
 String s = sPsavemoneyafterpfCP1.getMeterReadingCP1RO();
 float v = Float.parseFloat(s);
 meterreadingdiffP = v + ((current_mrCP1 - prev_mrCP1) * erate);

 }
 if (sPispowerfailafter4mrCP1.getispowerfailafter4mrCP1().equals("f")){
 meterreadingdiffP = (current_mrCP1 - prev_mrCP1) * erate;

 }
 String meterreadingCP1 = String.format("%.2f", meterreadingdiff);
 String meterreadingCP1P = String.format("%.2f", meterreadingdiffP);

 tv_status1.setText(s10_CP1);
 if (meterreadingdiffP > 0 && meterreadingdiffP < 100) {
 txt_c1round_off_mr.setText("₹ " + meterreadingCP1P);
 rsCP1 = "₹ " + meterreadingCP1P;
 rsPOCP1 = "₹ " + meterreadingCP1P;
 //------------
 float mrv = meterreadingdiffP;
 mrsPOCP1 = ""+mrv;
 //-----------
 }
 txt_c1diff.setText(time_readingCP1);
 status_detailCP1 = s10_CP1;
 unit_detailCP1 = meterreadingCP1;
 etime_detailCP1 = time_readingCP1;
 // sPisPoweFail.setisPowerFailCP1("t");
 relativeLayout1st.setVisibility(View.GONE);


 } else if (plugedinCountcp1 == 0) {
 tv_status1.setText(s11_CP1);
 txt_c1round_off_mr.setText("₹ 00.00");
 rsCP1 = "₹ 00.00";
 txt_c1diff.setText("00:00:00");
 status_detailCP1 = s11_CP1;
 unit_detailCP1 = "00.00";
 etime_detailCP1 = "00:00:00";
 sPisPluggedin.setisPluggedinCP1("t");
 plugedinCountcp1++;

 //meter reading-------------------------
 sPmeterReadingCP1.setMeterReadingCP1(p1meter);
 //Time reading_________________
 StartTimeCP1 = SystemClock.uptimeMillis() + (-NewBeginMillsCP1);  //--> Start Time
 toggleBtn.startAnimation(anim);

 } else {
 //time reading______
 MillisCP1 = (SystemClock.uptimeMillis() - StartTimeCP1);

 HoursCP1 = (int) (MillisCP1 / (1000 * 60 * 60));
 MinutesCP1 = (int) (MillisCP1 / (1000 * 60)) % 60;
 SecondsCP1 = (int) (MillisCP1 / 1000) % 60;
 String time_readingCP1 = "" + String.format("%02d", HoursCP1) + ":"
 + String.format("%02d", MinutesCP1) + ":"
 + String.format("%02d", SecondsCP1);


 //-----------meter Reading
 float prev_mrCP1 = Float.parseFloat(sPmeterReadingCP1.getMeterReadingCP1());
 float current_mrCP1 = 0;
 if (sPispowerfailafter4mrCP1.getispowerfailafter4mrCP1().equals("t")){
 current_mrCP1 = Float.parseFloat(sPmeterReadingCP1RO.getMeterReadingCP1RO())+Float.parseFloat(p1meter);

 }
 if (sPispowerfailafter4mrCP1.getispowerfailafter4mrCP1().equals("f")){
 current_mrCP1 = Float.parseFloat(p1meter);
 int b=(int) current_mrCP1;
 sPmeterReadingCP1RO.setMeterReadingCP1RO(""+b);
 }
 float meterreadingdiff =  prev_mrCP1 - current_mrCP1;

 float meterreadingdiffP = 0;
 if (sPispowerfailafter4mrCP1.getispowerfailafter4mrCP1().equals("t")){
 String s = sPsavemoneyafterpfCP1.getMeterReadingCP1RO();
 float v = Float.parseFloat(s);
 meterreadingdiffP = v + ((current_mrCP1 - prev_mrCP1) * erate);

 }
 if (sPispowerfailafter4mrCP1.getispowerfailafter4mrCP1().equals("f")){
 meterreadingdiffP = (current_mrCP1 - prev_mrCP1) * erate;

 }
 String meterreadingCP1 = String.format("%.2f", meterreadingdiff);
 String meterreadingCP1P = String.format("%.2f", meterreadingdiffP);

 //--------------------
 tv_status1.setText(s11_CP1);
 if (meterreadingdiffP > 0 && meterreadingdiffP < 100) {
 txt_c1round_off_mr.setText("₹ " + meterreadingCP1P);
 rsCP1 = "₹ " + meterreadingCP1P;
 rsPOCP1 = "₹ " + meterreadingCP1P;
 //------------
 float mrv = meterreadingdiffP;
 mrsPOCP1 = ""+mrv;
 //-----------
 }
 txt_c1diff.setText(time_readingCP1);
 status_detailCP1 = s11_CP1;
 unit_detailCP1 = meterreadingCP1;
 etime_detailCP1 = time_readingCP1;
 sPisPluggedin.setisPluggedinCP1("t");
 //---------------------
 sPisPoweFail.setisPowerFailCP1("t");
 sPTimeReadingCP1.setTimeReadingCP1(MillisCP1);
 //-------------------------------
 plugedinCountcp1++;
 }

 }

 }
 //-----------------------------------------------------------CP2 Status-----------------------------------------------------------------------------------------------------------------------------
 if (p2OnOff.equals("0") && p2plugin.equals("0") && p2fault.equals("99")) {

 if (sPisPluggedin.getisPluggedinCP2().equals("t")) {

 if (plugedoutCountcp2 >= 30) {
 sPisPluggedin.setisPluggedinCP2("f");

 } else {
 displayPlugoutCountC2 = 0;
 tv_status2.setText(s7_CP2);
 txt_c2round_off_mr.setText(rsPOCP2);
 rsCP2 = "₹ 00.00";
 txt_c2diff.setText(etime_detailCP2);
 status_detailCP2 = s7_CP2;
 unit_detailCP2 = unit_detailCP2;
 etime_detailCP2 = etime_detailCP2;
 sPispowerfailafter4mrCP2.setispowerfailafter4mrCP2("f");

 plugedinCountcp2 = 0;
 plugedoutCountcp2++;
 if (isResumedAftercp2) {
 sPisPoweFail.setisPowerFailCP1("f");
 }
 }

 } else {
 countidleCP2++;
 if (isPleaseWaitC2){
 tv_status2.setText("Please Wait...");
 status_detailCP2 = "Please Wait...";
 btn_p2onff.setVisibility(View.INVISIBLE);


 }else{
 tv_status2.setText(s8_CP2);
 status_detailCP2 = s8_CP2;

 }
 //  isStopC2 = false;
 if (isStopC2)
 {
 //  countC2 = 0;
 isAvailableC2 = true;
 isStartC2 = true;
 isStopC2 = false;
 }

 // Toast.makeText(getApplicationContext(),"C2 is Available",Toast.LENGTH_SHORT).show();
 txt_c2round_off_mr.setText("₹ 00.00");
 rsCP2 = "₹ 00.00";
 rsPOCP2 = "₹ 00.00";
 txt_c2diff.setText("00:00:00");
 unit_detailCP2 = "00.00";
 etime_detailCP2 = "00:00:00";
 plugedinCountcp2 = 0;
 btn_p2onff.clearAnimation();
 //overlay2
 if (overlayCountii> 10) {
 isPleaseWaitC2 = false;
 relativeLayout2nd.setVisibility(View.VISIBLE);
 }
 overlayCountii++;
 }

 }
 if (p2OnOff.equals("1") && p2plugin.equals("0") && p2fault.equals("99")) {
 countidleCP2 = 0;
 plugedinCountcp2 = 0;
 plugedoutCountcp2 = 0;
 tv_status2.setText(s1_CP2);
 isTappedC2 = false;
 isPleaseWaitC2 = false;

 if (isStartC2)
 {
 isStartC2 = false;
 countC2++;
 isStopC2 = true;
 isAvailableC2 = false;
 }
 txt_c2round_off_mr.setText("₹ 00.00");
 rsCP2 = "₹ 00.00";
 txt_c2diff.setText("00:00:00");
 status_detailCP2 = s1_CP2;
 unit_detailCP2 = "00.00";
 etime_detailCP2 = "00:00:00";
 isStillOnCP2 = false;
 relativeLayout2nd.setVisibility(View.GONE);
 btn_p2onff.setVisibility(View.VISIBLE);


 if (displayPlugoutCountC2 == 12) {
 if (sPisPoweFail.getisPowerFailCP2().equals("t") && sPisPluggedin.getisPluggedinCP2().equals("t")) {
 btn_p2onff.performClick();
 sPisPoweFail.setisPowerFailCP2("f");
 sPisPluggedin.setisPluggedinCP2("f");
 tv_status2.setText(s7_CP2);
 status_detailCP2 = s7_CP2;
 sPispowerfailafter4mrCP2.setispowerfailafter4mrCP2("f");

 }
 }
 displayPlugoutCountC2++;
 }
 if (p2OnOff.equals("1") && p2plugin.equals("1") && p2fault.equals("99")) {
 countidleCP2 = 0;
 if (sPisPoweFail.getisPowerFailCP2().equals("t")) {
 //---------Status:resuming after power fail_______________
 if (plugedinCountcp2 >= 10) {
 //time reading______
 MillisCP2 = (SystemClock.uptimeMillis() - StartTimeCP2);

 HoursCP2 = (int) (MillisCP2 / (1000 * 60 * 60));
 MinutesCP2 = (int) (MillisCP2 / (1000 * 60)) % 60;
 SecondsCP2 = (int) (MillisCP2 / 1000) % 60;
 String time_readingCP2 = "" + String.format("%02d", HoursCP2) + ":"
 + String.format("%02d", MinutesCP2) + ":"
 + String.format("%02d", SecondsCP2);
 //-------------------------meter reading
 Float prev_mrCP2 = Float.parseFloat(sPmeterReadingCP2.getMeterReadingCP2());
 float current_mrCP2 = 0;
 if (sPispowerfailafter4mrCP2.getispowerfailafter4mrCP2().equals("t")){
 current_mrCP2 = Float.parseFloat(sPmeterReadingCP2RO.getMeterReadingCP2RO())+Float.parseFloat(p2meter);

 }
 if (sPispowerfailafter4mrCP2.getispowerfailafter4mrCP2().equals("f")){
 current_mrCP2 = Float.parseFloat(p2meter);
 int b=(int) current_mrCP2;
 sPmeterReadingCP2RO.setMeterReadingCP2RO(""+b);
 }
 float meterreadingdiffc2 = prev_mrCP2 - current_mrCP2;
 Log.e("MeterReadingC2",""+(meterreadingdiffc2 = prev_mrCP2 - current_mrCP2));
 float meterreadingdiffc2P = (current_mrCP2 - prev_mrCP2) * erate;
 String meterreadingCP2 = String.format("%.2f", meterreadingdiffc2);
 String meterreadingCP2P = String.format("%.2f", meterreadingdiffc2P);
 //--------------------
 tv_status2.setText(s10_CP2);
 if (meterreadingdiffc2P > 0 && meterreadingdiffc2P < 100) {
 txt_c2round_off_mr.setText("₹ " + meterreadingCP2P);
 rsCP2 = "₹ " + meterreadingCP2P;
 rsPOCP2 = "₹ " + meterreadingCP2P;
 }
 txt_c2diff.setText(time_readingCP2);
 status_detailCP2 = s10_CP2;
 unit_detailCP2 = meterreadingCP2;
 etime_detailCP2 = time_readingCP2;
 sPisPoweFail.setisPowerFailCP2("f");
 relativeLayout2nd.setVisibility(View.GONE);

 } else if (plugedinCountcp2 == 0) {
 //Time reading_________________

 StartTimeCP2 = SystemClock.uptimeMillis() + (-sPTimeReadingCP2.getTimeReadingCP2());
 //-------------------------meter reading
 Float prev_mrCP2 = Float.parseFloat(sPmeterReadingCP2.getMeterReadingCP2());
 float current_mrCP2 = 0;
 if (sPispowerfailafter4mrCP2.getispowerfailafter4mrCP2().equals("t")){
 current_mrCP2 = Float.parseFloat(sPmeterReadingCP2RO.getMeterReadingCP2RO())+Float.parseFloat(p2meter);

 }
 if (sPispowerfailafter4mrCP2.getispowerfailafter4mrCP2().equals("f")){
 current_mrCP2 = Float.parseFloat(p2meter);
 int b=(int) current_mrCP2;
 sPmeterReadingCP2RO.setMeterReadingCP2RO(""+b);
 }
 float meterreadingdiffc2 = prev_mrCP2 - current_mrCP2;
 Log.e("MeterReadingC2",""+(meterreadingdiffc2 = prev_mrCP2 - current_mrCP2));
 float meterreadingdiffc2P = (current_mrCP2 - prev_mrCP2) * erate;
 String meterreadingCP2 = String.format("%.2f", meterreadingdiffc2);
 String meterreadingCP2P = String.format("%.2f", meterreadingdiffc2P);
 //--------------------
 tv_status2.setText(s9_CP2);
 if (meterreadingdiffc2P > 0 && meterreadingdiffc2P < 100) {
 txt_c2round_off_mr.setText("₹ " + meterreadingCP2P);
 rsCP2 = "₹ " + meterreadingCP2P;
 rsPOCP2 = "₹ " + meterreadingCP2P;
 }
 txt_c2diff.setText("00:00:00");
 status_detailCP2 = s9_CP2;
 unit_detailCP2 = meterreadingCP2;
 etime_detailCP2 = "00:00:00";
 plugedinCountcp2++;
 sPisPluggedin.setisPluggedinCP2("t");
 isResumedAftercp2 = true;
 txt_p2btnValue.startAnimation(anim2);
 relativeLayout2nd.setVisibility(View.GONE);


 } else {
 //time reading______
 MillisCP2 = (SystemClock.uptimeMillis() - StartTimeCP2);

 HoursCP2 = (int) (MillisCP2 / (1000 * 60 * 60));
 MinutesCP2 = (int) (MillisCP2 / (1000 * 60)) % 60;
 SecondsCP2 = (int) (MillisCP2 / 1000) % 60;
 String time_readingCP2 = "" + String.format("%02d", HoursCP2) + ":"
 + String.format("%02d", MinutesCP2) + ":"
 + String.format("%02d", SecondsCP2);
 //-------------------------meter reading
 Float prev_mrCP2 = Float.parseFloat(sPmeterReadingCP2.getMeterReadingCP2());
 float current_mrCP2 = 0;
 if (sPispowerfailafter4mrCP2.getispowerfailafter4mrCP2().equals("t")){
 current_mrCP2 = Float.parseFloat(sPmeterReadingCP2RO.getMeterReadingCP2RO())+Float.parseFloat(p2meter);

 }
 if (sPispowerfailafter4mrCP2.getispowerfailafter4mrCP2().equals("f")){
 current_mrCP2 = Float.parseFloat(p2meter);
 int b=(int) current_mrCP2;
 sPmeterReadingCP2RO.setMeterReadingCP2RO(""+b);
 }
 float meterreadingdiffc2 = prev_mrCP2 - current_mrCP2;
 Log.e("MeterReadingC2",""+(meterreadingdiffc2 = prev_mrCP2 - current_mrCP2));
 float meterreadingdiffc2P = (current_mrCP2 - prev_mrCP2) * erate;
 String meterreadingCP2 = String.format("%.2f", meterreadingdiffc2);
 String meterreadingCP2P = String.format("%.2f", meterreadingdiffc2P);
 //--------------------
 tv_status2.setText(s9_CP2);
 if (meterreadingdiffc2P > 0 && meterreadingdiffc2P < 100) {
 txt_c2round_off_mr.setText("₹ " + meterreadingCP2P);
 rsCP2 = "₹ " + meterreadingCP2P;
 rsPOCP2 = "₹ " + meterreadingCP2P;
 }
 txt_c2diff.setText(time_readingCP2);
 status_detailCP2 = s9_CP2;
 unit_detailCP2 = meterreadingCP2;
 etime_detailCP2 = time_readingCP2;
 plugedinCountcp2++;
 sPisPluggedin.setisPluggedinCP2("t");
 isResumedAftercp2 = true;
 relativeLayout2nd.setVisibility(View.GONE);


 }

 } else {
 //--------------- Status:vehicle pugged in_________
 if (plugedinCountcp2 >= 10) {
 //time reading______
 MillisCP2 = (SystemClock.uptimeMillis() - StartTimeCP2);

 HoursCP2 = (int) (MillisCP2 / (1000 * 60 * 60));
 MinutesCP2 = (int) (MillisCP2 / (1000 * 60)) % 60;
 SecondsCP2 = (int) (MillisCP2 / 1000) % 60;
 String time_readingCP2 = "" + String.format("%02d", HoursCP2) + ":"
 + String.format("%02d", MinutesCP2) + ":"
 + String.format("%02d", SecondsCP2);
 //-------------------------meter reading
 Float prev_mrCP2 = Float.parseFloat(sPmeterReadingCP2.getMeterReadingCP2());
 float current_mrCP2 = 0;
 if (sPispowerfailafter4mrCP2.getispowerfailafter4mrCP2().equals("t")){
 current_mrCP2 = Float.parseFloat(sPmeterReadingCP2RO.getMeterReadingCP2RO())+Float.parseFloat(p2meter);

 }
 if (sPispowerfailafter4mrCP2.getispowerfailafter4mrCP2().equals("f")){
 current_mrCP2 = Float.parseFloat(p2meter);
 int b=(int) current_mrCP2;
 sPmeterReadingCP2RO.setMeterReadingCP2RO(""+b);
 }
 float meterreadingdiffc2 = prev_mrCP2 - current_mrCP2;
 Log.e("MeterReadingC2",""+(meterreadingdiffc2 = prev_mrCP2 - current_mrCP2));
 float meterreadingdiffc2P = (current_mrCP2 - prev_mrCP2) * erate;
 String meterreadingCP2 = String.format("%.2f", meterreadingdiffc2);
 String meterreadingCP2P = String.format("%.2f", meterreadingdiffc2P);
 //--------------------
 tv_status2.setText(s10_CP2);
 if (meterreadingdiffc2P > 0 && meterreadingdiffc2P < 100) {
 txt_c2round_off_mr.setText("₹ " + meterreadingCP2P);
 rsCP2 = "₹ " + meterreadingCP2P;
 rsPOCP2 = "₹ " + meterreadingCP2P;
 }
 txt_c2diff.setText(time_readingCP2);
 status_detailCP2 = s10_CP2;
 unit_detailCP2 = meterreadingCP2;
 etime_detailCP2 = time_readingCP2;
 // sPisPoweFail.setisPowerFailCP1("t");
 relativeLayout2nd.setVisibility(View.GONE);


 } else if (plugedinCountcp2 == 0) {
 tv_status2.setText(s11_CP2);
 txt_c2round_off_mr.setText("₹ 00.00");
 rsCP2 = "₹ 00.00";
 txt_c2diff.setText("00:00:00");
 status_detailCP2 = s11_CP2;
 unit_detailCP2 = "00.00";
 etime_detailCP2 = "00:00:00";
 sPisPluggedin.setisPluggedinCP2("t");
 plugedinCountcp2++;

 //meter reading-------------------------
 sPmeterReadingCP2.setMeterReadingCP2(p2meter);
 //Time reading_________________
 StartTimeCP2 = SystemClock.uptimeMillis() + (-NewBeginMillsCP2);  //--> Start Time
 txt_p2btnValue.startAnimation(anim2);

 } else {
 //time reading______
 MillisCP2 = (SystemClock.uptimeMillis() - StartTimeCP2);

 HoursCP2 = (int) (MillisCP2 / (1000 * 60 * 60));
 MinutesCP2 = (int) (MillisCP2 / (1000 * 60)) % 60;
 SecondsCP2 = (int) (MillisCP2 / 1000) % 60;
 String time_readingCP2 = "" + String.format("%02d", HoursCP2) + ":"
 + String.format("%02d", MinutesCP2) + ":"
 + String.format("%02d", SecondsCP2);


 //-------------------------meter reading
 Float prev_mrCP2 = Float.parseFloat(sPmeterReadingCP2.getMeterReadingCP2());
 float current_mrCP2 = 0;
 if (sPispowerfailafter4mrCP2.getispowerfailafter4mrCP2().equals("t")){
 current_mrCP2 = Float.parseFloat(sPmeterReadingCP2RO.getMeterReadingCP2RO())+Float.parseFloat(p2meter);

 }
 if (sPispowerfailafter4mrCP2.getispowerfailafter4mrCP2().equals("f")){
 current_mrCP2 = Float.parseFloat(p2meter);
 int b=(int) current_mrCP2;
 sPmeterReadingCP2RO.setMeterReadingCP2RO(""+b);
 }
 float meterreadingdiffc2 = prev_mrCP2 - current_mrCP2;
 Log.e("MeterReadingC2",""+(meterreadingdiffc2 = prev_mrCP2 - current_mrCP2));
 float meterreadingdiffc2P = (current_mrCP2 - prev_mrCP2) * erate;
 String meterreadingCP2 = String.format("%.2f", meterreadingdiffc2);
 String meterreadingCP2P = String.format("%.2f", meterreadingdiffc2P);
 //--------------------
 tv_status2.setText(s11_CP2);
 if (meterreadingdiffc2P > 0 && meterreadingdiffc2P < 100) {
 txt_c2round_off_mr.setText("₹ " + meterreadingCP2P);
 rsCP2 = "₹ " + meterreadingCP2P;
 rsPOCP2 = "₹ " + meterreadingCP2P;
 }
 txt_c2diff.setText(time_readingCP2);
 status_detailCP2 = s11_CP2;
 unit_detailCP2 = meterreadingCP2;
 etime_detailCP2 = time_readingCP2;
 sPisPluggedin.setisPluggedinCP2("t");
 plugedinCountcp2++;
 }

 }

 }

 //-----------------------------------------------------------CP3 Status-----------------------------------------------------------------------------------------------------------------------------
 if (p3OnOff.equals("0") && p3plugin.equals("0") && p3fault.equals("99")) {

 if (sPisPluggedin.getisPluggedinCP3().equals("t")) {

 if (plugedoutCountcp3 >= 30) {
 sPisPluggedin.setisPluggedinCP3("f");

 } else {
 displayPlugoutCountC2 = 0;
 tv_status3.setText(s7_CP3);
 txt_c3round_off_mr.setText(rsPOCP3);
 rsCP3 = "₹ 00.00";
 txt_c3diff.setText(etime_detailCP3);
 status_detailCP3 = s7_CP3;
 unit_detailCP3 = unit_detailCP3;
 etime_detailCP3 = etime_detailCP3;
 plugedinCountcp3 = 0;
 plugedoutCountcp3++;
 if (isResumedAftercp3) {
 sPisPoweFail.setisPowerFailCP3("f");
 }
 //
 }

 } else {
 countidleCP3++;
 if (isPleaseWaitC3){
 tv_status3.setText("Please Wait...");
 status_detailCP3 = "Please Wait...";
 btn_p3onff.setVisibility(View.INVISIBLE);


 }else{
 tv_status3.setText(s8_CP3);
 status_detailCP3 = s8_CP3;

 }

 if (isStopC3){
 //   countC3 = 0;
 isAvailableC3=true;
 isStartC3 = true;
 isStopC3 = false;
 }

 //Toast.makeText(getApplicationContext(),"C3 Is Available Now",Toast.LENGTH_SHORT).show();
 txt_c3round_off_mr.setText("₹ 00.00");
 rsCP3 = "₹ 00.00";
 rsPOCP3 = "₹ 00.00";
 txt_c3diff.setText("00:00:00");
 unit_detailCP3 = "00.00";
 etime_detailCP3 = "00:00:00";
 plugedinCountcp3 = 0;
 btn_p3onff.clearAnimation();

 //overlay1
 if (overlayCountiii> 10) {
 isPleaseWaitC3 = false;
 relativeLayout3rd.setVisibility(View.VISIBLE);
 }
 overlayCountiii++;
 }
 }

 if (p3OnOff.equals("1") && p3plugin.equals("0") && p3fault.equals("99")) {
 plugedinCountcp3 = 0;
 plugedoutCountcp3 = 0;
 tv_status3.setText(s1_CP3);
 //countC3=1;\
 isTappedC3 = false;
 isPleaseWaitC3 = false;

 if (isStartC3)
 {
 isStartC3 = false;
 isAvailableC3 = false;
 countC3++;
 isStopC3 = true;
 }
 txt_c3round_off_mr.setText("₹ 00.00");
 countidleCP3 = 0;
 rsCP3 = "₹ 00.00";
 txt_c3diff.setText("00:00:00");
 status_detailCP3 = s1_CP3;
 unit_detailCP3 = "00.00";
 etime_detailCP3 = "00:00:00";
 relativeLayout3rd.setVisibility(View.GONE);
 btn_p3onff.setVisibility(View.VISIBLE);


 if (displayPlugoutCountC3 == 12) {
 if (sPisPoweFail.getisPowerFailCP3().equals("t") && sPisPluggedin.getisPluggedinCP3().equals("t")) {
 btn_p3onff.performClick();
 sPisPoweFail.setisPowerFailCP3("f");
 sPisPluggedin.setisPluggedinCP3("f");
 tv_status3.setText(s7_CP3);
 status_detailCP3 = s7_CP3;
 }
 }
 displayPlugoutCountC3++;
 }
 if (p3OnOff.equals("1") && p3plugin.equals("1") && p3fault.equals("99")) {
 countidleCP3 = 0;
 if (sPisPoweFail.getisPowerFailCP3().equals("t")) {
 //---------Status:resuming after power fail_______________
 if (plugedinCountcp3 >= 10) {
 //time reading______
 MillisCP3 = (SystemClock.uptimeMillis() - StartTimeCP3);

 HoursCP3 = (int) (MillisCP3 / (1000 * 60 * 60));
 MinutesCP3 = (int) (MillisCP3 / (1000 * 60)) % 60;
 SecondsCP3 = (int) (MillisCP3 / 1000) % 60;
 String time_readingCP3 = "" + String.format("%02d", HoursCP3) + ":"
 + String.format("%02d", MinutesCP3) + ":"
 + String.format("%02d", SecondsCP3);
 //-------------------------meter reading
 Float prev_mrCP3 = Float.parseFloat(sPmeterReadingCP3.getMeterReadingCP3());
 Float current_mrCP3 = Float.parseFloat(p3meter);
 float meterreadingdiffc3 = current_mrCP3 - prev_mrCP3;
 float meterreadingdiffc3P = (current_mrCP3 - prev_mrCP3) * erate;
 String meterreadingCP3 = String.format("%.2f", meterreadingdiffc3);
 String meterreadingCP3P = String.format("%.2f", meterreadingdiffc3P);
 //--------------------
 tv_status3.setText(s10_CP3);
 if (meterreadingdiffc3P > 0 && meterreadingdiffc3P < 100) {
 txt_c3round_off_mr.setText("₹ " + meterreadingCP3P);
 rsCP3 = "₹ " + meterreadingCP3P;
 rsPOCP3 = "₹ " + meterreadingCP3P;
 }
 txt_c3diff.setText(time_readingCP3);
 status_detailCP3 = s10_CP3;
 unit_detailCP3 = meterreadingCP3;
 etime_detailCP3 = time_readingCP3;
 sPisPoweFail.setisPowerFailCP3("f");
 relativeLayout3rd.setVisibility(View.GONE);

 } else if (plugedinCountcp3 == 0) {
 //Time reading_________________

 StartTimeCP3 = SystemClock.uptimeMillis() + (-sPTimeReadingCP3.getTimeReadingCP3());
 //-------------------------meter reading
 Float prev_mrCP3 = Float.parseFloat(sPmeterReadingCP3.getMeterReadingCP3());
 Float current_mrCP3 = Float.parseFloat(p3meter);
 float meterreadingdiffc3 = current_mrCP3 - prev_mrCP3;
 float meterreadingdiffc3P = (current_mrCP3 - prev_mrCP3) * erate;
 String meterreadingCP3 = String.format("%.2f", meterreadingdiffc3);
 String meterreadingCP3P = String.format("%.2f", meterreadingdiffc3P);
 //--------------------
 tv_status3.setText(s9_CP3);
 if (meterreadingdiffc3P > 0 && meterreadingdiffc3P < 100) {
 txt_c3round_off_mr.setText("₹ " + meterreadingCP3P);
 rsCP3 = "₹ " + meterreadingCP3P;
 rsPOCP3 = "₹ " + meterreadingCP3P;
 }
 txt_c3diff.setText("00:00:00");
 status_detailCP3 = s9_CP3;
 unit_detailCP3 = meterreadingCP3;
 etime_detailCP3 = "00:00:00";
 plugedinCountcp3++;
 sPisPluggedin.setisPluggedinCP3("t");
 isResumedAftercp3 = true;
 txt_p3btnValue.startAnimation(anim3);
 relativeLayout3rd.setVisibility(View.GONE);


 } else {
 //time reading______
 MillisCP3 = (SystemClock.uptimeMillis() - StartTimeCP3);

 HoursCP3 = (int) (MillisCP3 / (1000 * 60 * 60));
 MinutesCP3 = (int) (MillisCP3 / (1000 * 60)) % 60;
 SecondsCP3 = (int) (MillisCP3 / 1000) % 60;
 String time_readingCP3 = "" + String.format("%02d", HoursCP3) + ":"
 + String.format("%02d", MinutesCP3) + ":"
 + String.format("%02d", SecondsCP3);
 //-------------------------meter reading
 Float prev_mrCP3 = Float.parseFloat(sPmeterReadingCP3.getMeterReadingCP3());
 Float current_mrCP3 = Float.parseFloat(p3meter);
 float meterreadingdiffc3 = current_mrCP3 - prev_mrCP3;
 float meterreadingdiffc3P = (current_mrCP3 - prev_mrCP3) * erate;
 String meterreadingCP3 = String.format("%.2f", meterreadingdiffc3);
 String meterreadingCP3P = String.format("%.2f", meterreadingdiffc3P);
 //--------------------
 tv_status3.setText(s9_CP3);
 if (meterreadingdiffc3P > 0 && meterreadingdiffc3P < 100) {
 txt_c3round_off_mr.setText("₹ " + meterreadingCP3P);
 rsCP3 = "₹ " + meterreadingCP3P;
 rsPOCP3 = "₹ " + meterreadingCP3P;
 }
 status_detailCP3 = s9_CP3;
 unit_detailCP3 = meterreadingCP3;
 etime_detailCP3 = time_readingCP3;
 plugedinCountcp3++;
 sPisPluggedin.setisPluggedinCP3("t");
 isResumedAftercp3 = true;
 relativeLayout3rd.setVisibility(View.GONE);


 }

 } else {
 //--------------- Status:vehicle pugged in_________
 if (plugedinCountcp3 >= 10) {
 //time reading______
 MillisCP3 = (SystemClock.uptimeMillis() - StartTimeCP3);

 HoursCP3 = (int) (MillisCP3 / (1000 * 60 * 60));
 MinutesCP3 = (int) (MillisCP3 / (1000 * 60)) % 60;
 SecondsCP3 = (int) (MillisCP3 / 1000) % 60;
 String time_readingCP3 = "" + String.format("%02d", HoursCP3) + ":"
 + String.format("%02d", MinutesCP3) + ":"
 + String.format("%02d", SecondsCP3);

 //-------------------------meter reading
 Float prev_mrCP3 = Float.parseFloat(sPmeterReadingCP3.getMeterReadingCP3());
 Float current_mrCP3 = Float.parseFloat(p3meter);
 float meterreadingdiffc3 = current_mrCP3 - prev_mrCP3;
 //  String meterreadingCP3 = String.format("%.2f", meterreadingdiffc3);
 float meterreadingdiffc3P = (current_mrCP3 - prev_mrCP3) * erate;
 String meterreadingCP3 = String.format("%.2f", meterreadingdiffc3);
 String meterreadingCP3P = String.format("%.2f", meterreadingdiffc3P);
 //--------------------
 tv_status3.setText(s10_CP3);
 if (meterreadingdiffc3P > 0 && meterreadingdiffc3P < 100) {
 txt_c3round_off_mr.setText("₹ " + meterreadingCP3P);
 rsCP3 = "₹ " + meterreadingCP3P;
 rsPOCP3 = "₹ " + meterreadingCP3P;
 }
 txt_c3diff.setText(time_readingCP3);
 status_detailCP3 = s10_CP3;
 unit_detailCP3 = meterreadingCP3;
 etime_detailCP3 = time_readingCP3;
 // sPisPoweFail.setisPowerFailCP1("t");
 relativeLayout3rd.setVisibility(View.GONE);


 } else if (plugedinCountcp3 == 0) {
 tv_status3.setText(s11_CP3);
 txt_c3round_off_mr.setText("₹ 00.00");
 rsCP3 = "₹ 00.00";
 txt_c3diff.setText("00:00:00");
 status_detailCP3 = s11_CP3;
 unit_detailCP3 = "00.00";
 etime_detailCP3 = "00:00:00";
 sPisPluggedin.setisPluggedinCP3("t");
 plugedinCountcp3++;

 //meter reading-------------------------
 sPmeterReadingCP3.setMeterReadingCP3(p3meter);
 //Time reading_________________
 StartTimeCP3 = SystemClock.uptimeMillis() + (-NewBeginMillsCP3);  //--> Start Time
 txt_p3btnValue.startAnimation(anim3);

 } else {
 //time reading______
 MillisCP3 = (SystemClock.uptimeMillis() - StartTimeCP3);

 HoursCP3 = (int) (MillisCP3 / (1000 * 60 * 60));
 MinutesCP3 = (int) (MillisCP3 / (1000 * 60)) % 60;
 SecondsCP3 = (int) (MillisCP3 / 1000) % 60;
 String time_readingCP3 = "" + String.format("%02d", HoursCP3) + ":"
 + String.format("%02d", MinutesCP3) + ":"
 + String.format("%02d", SecondsCP3);

 //-------------------------meter reading
 Float prev_mrCP3 = Float.parseFloat(sPmeterReadingCP3.getMeterReadingCP3());
 Float current_mrCP3 = Float.parseFloat(p3meter);
 float meterreadingdiffc3 = current_mrCP3 - prev_mrCP3;
 float meterreadingdiffc3P = (current_mrCP3 - prev_mrCP3) * erate;
 String meterreadingCP3 = String.format("%.2f", meterreadingdiffc3);
 String meterreadingCP3P = String.format("%.2f", meterreadingdiffc3P);
 //--------------------
 tv_status3.setText(s11_CP3);
 if (meterreadingdiffc3P > 0 && meterreadingdiffc3P < 100) {
 txt_c3round_off_mr.setText("₹ " + meterreadingCP3P);
 rsCP3 = "₹ " + meterreadingCP3P;
 rsPOCP3 = "₹ " + meterreadingCP3P;
 }
 txt_c3diff.setText(time_readingCP3);
 status_detailCP3 = s11_CP3;
 unit_detailCP3 = meterreadingCP3;
 etime_detailCP3 = time_readingCP3;
 sPisPluggedin.setisPluggedinCP3("t");
 plugedinCountcp3++;
 }

 }

 }


 if (detail_flag.equals("d_CP1")) {
 display_details(rsCP1, s15_CP1+" #01", voltage_detailCP1, current_detailCP1, power_detailCP1, unit_detailCP1, etime_detailCP1, status_detailCP1,s14_CP1,s17_CP1," ₹ "+erate_s+" "+s16_CP1,sPlanguageCP1.getlanguageCP1());

 } else if (detail_flag.equals("d_CP2")) {
 display_details(rsCP2, s15_CP2+" #02", voltage_detailCP2, current_detailCP2, power_detailCP2, unit_detailCP2, etime_detailCP2, status_detailCP2,s14_CP2,s17_CP2," ₹ "+erate_s+" "+s16_CP2,sPlanguageCP2.getlanguageCP2());

 } else if (detail_flag.equals("d_CP3")) {
 display_details(rsCP3, s15_CP3+" #03", voltage_detailCP3, current_detailCP3, power_detailCP3, unit_detailCP3, etime_detailCP3, status_detailCP3,s14_CP3,s17_CP3," ₹ "+erate_s+" "+s16_CP3,sPlanguageCP3.getlanguageCP3());

 }

 Log.e("CHECKII", "CHECKING");

 Log.e("COMMING STRING", recDataString + "\n\np1OnOff :" + p1OnOff + "\tp1plugin :" + p1plugin + "\tp1fault :" + p1fault + "\tp1voltage :" + p1voltage + "\tp1current :" + p1current + "\tp1meter :" + p1meter +
 "\n\np2OnOff : " + p2OnOff + "\tp2plugin : " + p2plugin + "\tp2fault : " + p2fault + "\tp2voltage : " + p2voltage + "\tp2current : " + p2current + "\tp2meter : " + p2meter +
 "\n\np3OnOff : " + p3OnOff + "\tp3plugin : " + p3plugin + "\tp3fault : " + p3fault + "\tp3voltage: " + p3voltage + "\tp3current : " + p3current + "\tp3meter : " + p3meter);

 }
 } catch (StringIndexOutOfBoundsException siobe) {
 //System.out.println("invalid input");
 }

 }

 recDataString.delete(0, recDataString.length());                    //clear all string data

 }
 */
/*else{
                        recDataString.delete(0, recDataString.length());                    //clear all string data

                    }*//*

                    }
                    else {
                        tv_status1.setText(s6_CP1);
                        String s = globalP1meter;
                        float v = Float.parseFloat(s);
                        int b=(int) v;
                        if (b>=4) {
                            sPispowerfailafter4mrCP1.setispowerfailafter4mrCP1("t");
                            sPsavemoneyafterpfCP1.setMeterReadingCP1RO(mrsPOCP1);

                        }
                        tv_status2.setText(s6_CP2);

                        String s2 = globalP2meter;
                        float v2 = Float.parseFloat(s2);
                        int b2=(int) v2;
                        if (b2>=4) {
                            sPispowerfailafter4mrCP2.setispowerfailafter4mrCP2("t");
                        }
                        tv_status3.setText(s6_CP3);
                        toggleBtn.setCardBackgroundColor(Color.parseColor(red));
                        toggleBtn.startAnimation(anim);
                        btn_p2onff.setCardBackgroundColor(Color.parseColor(red));
                        btn_p2onff.startAnimation(anim);
                        btn_p3onff.setCardBackgroundColor(Color.parseColor(red));
                        btn_p3onff.startAnimation(anim);
                    }
                }
                else{
                    msg.what = handlerState;
                }

            }
        };

        btAdapter = BluetoothAdapter.getDefaultAdapter();       // get Bluetooth adapter
        checkBTState();

        toggleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAlreadyonm) {
                    funConnectori();
                }else {
                    sPisPoweFail.setisPowerFailCP1("f");
                    StringBuilder newCommandp1 = new StringBuilder(command);
                    newCommandp1.setCharAt(1, '0');


                    command = ""+newCommandp1;
                    toggleBtn.setCardBackgroundColor(Color.parseColor(green));
                    toggleValue.setText("CP1 "+s22_CP1);

                    toggleBtn.clearAnimation();
                }
            }
        });


        btn_p2onff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAlreadyonm) {
                    funConnectorii();
                }else {
                    sPisPoweFail.setisPowerFailCP2("f");
                    StringBuilder newCommandp2 = new StringBuilder(command);
                    newCommandp2.setCharAt(1, '0');


                    command = ""+newCommandp2;
                    btn_p2onff.setCardBackgroundColor(Color.parseColor(green));
                    txt_p2btnValue.setText("CP2 "+s22_CP2);
                    txt_p2btnValue.clearAnimation();
                }
            }
        });
        btn_p3onff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAlreadyonm) {
                    funConnectoriii();
                }else {
                    sPisPoweFail.setisPowerFailCP3("f");
                    StringBuilder newCommandp3 = new StringBuilder(command);
                    newCommandp3.setCharAt(1, '0');


                    command = ""+newCommandp3;
                    btn_p3onff.setCardBackgroundColor(Color.parseColor(green));
                    txt_p3btnValue.setText("CP3 "+s22_CP3);
                    txt_p3btnValue.clearAnimation();
                }
            }
        });



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
        try
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
        }
        mConnectedThread = new ConnectedThread(btSocket);
        mConnectedThread.start();

        //I send a character when resuming.beginning transmission to check device is connected
        //If it is not an exception will be thrown in the write method and finish() will be called
        mConnectedThread.write("x");
        // Toast.makeText(MaindisplayActivity.this,"OnResume",Toast.LENGTH_LONG).show();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        this.registerReceiver(mReceiver, filter);
        IntentFilter batteryfilter = new IntentFilter();
        batteryfilter.addAction(Intent.ACTION_POWER_CONNECTED);
        batteryfilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        registerReceiver(batteryReceiver,batteryfilter);

    }

    private void display_details(final String rate_detail, final String charge_point_id,final String voltage_detail,final String current_detail,final String power_detail,final String unit_detail,final String etime_detail,final String status_detail,final String etime_displayf, final String unit_display, final String rate_display, final String lang){
        // txt_cpname,txt_rate,txt_voltage_detail,txt_current_detail,txt_power_detail,txt_unit_detail,txt_etime_detail,txt_status_detail;
        txt_rate.setText(rate_detail);
        txt_cpname.setText(charge_point_id);
        txt_voltage_detail.setText(voltage_detail);
        txt_current_detail.setText(current_detail);
        txt_power_detail.setText(power_detail);
        txt_unit_detail.setText(unit_detail);
        txt_etime_detail.setText(etime_detail);
        txt_status_detail.setText(status_detail);
        txt_etime_displayf.setText(etime_displayf);
        txt_unit_display.setText(unit_display);
        txt_rate_display.setText(rate_display);
        if (lang.equals("en")){
            rg_language_m.check(R.id.rb_en_m);
        }else if (lang.equals("ma")){
            rg_language_m.check(R.id.rb_ma_m);
        }else if (lang.equals("hi")){
            rg_language_m.check(R.id.rb_hi_m);
        }

    }
    public void funConnectori(){
        if (isStillOnCP1){
            flag = false;
            setToggleBtn();
            sPisPluggedin.setisPluggedinCP1("f");
            sPisPoweFail.setisPowerFailCP1("f");
        }
        else {
            if (flag) {
                StringBuilder newCommandp1 = new StringBuilder(command);
                newCommandp1.setCharAt(1, '0');

                Log.e("newCommandp1", "" + newCommandp1);
                mConnectedThread.write("" + newCommandp1);    // Send "0" via Bluetooth
                //  Toast.makeText(getBaseContext(), "Turn off CP1 :" + newCommandp1, Toast.LENGTH_SHORT).show();


                //flag=false;

            } else {
                StringBuilder newCommandp1 = new StringBuilder(command);
                newCommandp1.setCharAt(1, '1');


                Log.e("newCommandp1", "" + newCommandp1);
                mConnectedThread.write("" + newCommandp1);    // Send "0" via Bluetooth
                // Toast.makeText(getBaseContext(), "Turn on CP1 :" + newCommandp1, Toast.LENGTH_SHORT).show();
                //flag=true;

            }

            setToggleBtn();
        }
    }
    public void funConnectorii(){
        if (isStillOnCP2){
            p2flag = false;
            setP2Btn();
            sPisPluggedin.setisPluggedinCP2("f");
            sPisPoweFail.setisPowerFailCP2("f");
        }
        else
        {
            if (p2flag)
            {
                StringBuilder newCommandp2 = new StringBuilder(command);
                newCommandp2.setCharAt(2, '0');
                Log.e("newCommandp2", "" + newCommandp2);
                mConnectedThread.write("" + newCommandp2);    // Send "0" via Bluetooth
                // Toast.makeText(getBaseContext(), "Turn off CP2 :" + newCommandp2, Toast.LENGTH_SHORT).show();
                //flag=false;

            }
            else
            {
                StringBuilder newCommandp2 = new StringBuilder(command);
                newCommandp2.setCharAt(2, '1');

                Log.e("newCommandp2", "" + newCommandp2);
                mConnectedThread.write("" + newCommandp2);    // Send "1" via Bluetooth
                //   Toast.makeText(getBaseContext(), "Turn on CP2 :" + newCommandp2, Toast.LENGTH_SHORT).show();
                //flag=true;

            }

            setP2Btn();
        }
    }
    public void funConnectoriii(){
        if (isStillOnCP3){
            p3flag = false;
            setP3Btn();
            sPisPluggedin.setisPluggedinCP3("f");
            sPisPoweFail.setisPowerFailCP3("f");
        }
        else {
            if (p3flag)
            {
                StringBuilder newCommandp3 = new StringBuilder(command);
                newCommandp3.setCharAt(3, '0');

                Log.e("newCommandp3", "" + newCommandp3);
                mConnectedThread.write("" + newCommandp3);    // Send "0" via Bluetooth
                // Toast.makeText(getBaseContext(), "Turn off CP3 :" + newCommandp3, Toast.LENGTH_SHORT).show();
                //flag=false;

            }
            else
            {
                StringBuilder newCommandp3 = new StringBuilder(command);
                newCommandp3.setCharAt(3, '1');

                Log.e("newCommandp3", "" + newCommandp3);
                mConnectedThread.write("" + newCommandp3);    // Send "0" via Bluetooth
                //mConnectedThread.write("$100990.00");    // Send "1" via Bluetooth
                // Toast.makeText(getBaseContext(), "Turn on CP3 :" + newCommandp3, Toast.LENGTH_SHORT).show();
                //flag=true;
            }

            setP3Btn();
        }
    }

    @Override
    protected void onStop()
    {
        // Toast.makeText(MaindisplayActivity.this,"onStop Method",Toast.LENGTH_LONG).show();
        Log.e("METHOD","onStop Method");
        unregisterReceiver(mReceiver);
        unregisterReceiver(batteryReceiver);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Toast.makeText(MaindisplayActivity.this,"onDestroy Method",Toast.LENGTH_LONG).show();
        Log.e("METHOD","onDestroy Method");
    }
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Toast.makeText(MaindisplayActivity.this, device.getName() + " Device found", Toast.LENGTH_LONG).show();
            }
            else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                //Toast.makeText(MaindisplayActivity.this, device.getName() + " Device is now connected", Toast.LENGTH_LONG).show();

            }
            else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
                //Toast.makeText(MaindisplayActivity.this, device.getName() + " Device is about to disconnect", Toast.LENGTH_LONG).show();
            }
            else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                //Toast.makeText(MaindisplayActivity.this, device.getName() + " Device has disconnected", Toast.LENGTH_LONG).show();
                tv_status1.setText(s6_CP1);
                String s = globalP1meter;
                float v = Float.parseFloat(s);
                int b=(int) v;
                if (b>=4) {
                    sPispowerfailafter4mrCP1.setispowerfailafter4mrCP1("t");
                    sPsavemoneyafterpfCP1.setMeterReadingCP1RO(mrsPOCP1);

                }
                toggleBtn.setCardBackgroundColor(Color.parseColor(red));
                toggleBtn.startAnimation(anim);

                tv_status2.setText(s6_CP2);
                String s2 = globalP2meter;
                float v2 = Float.parseFloat(s2);
                int b2=(int) v2;
                if (b2>=4) {
                    sPispowerfailafter4mrCP2.setispowerfailafter4mrCP2("t");
                }
                btn_p2onff.setCardBackgroundColor(Color.parseColor(red));
                btn_p2onff.startAnimation(anim);
                tv_status3.setText(s6_CP3);
                btn_p3onff.setCardBackgroundColor(Color.parseColor(red));
                btn_p3onff.startAnimation(anim);
                mFirebaseInstance.getReference(cid+"-CP1M").setValue(tv_status1.getText().toString());
                mFirebaseInstance.getReference(cid+"-CP2M").setValue(tv_status2.getText().toString());
                mFirebaseInstance.getReference(cid+"-CP3M").setValue(tv_status3.getText().toString());
                */
/*if(sPisPluggedin.getisPluggedinCP1().equals("t")){
                    sPisPoweFail.setisPowerFailCP1("t");
                    sPTimeReadingCP1.setTimeReadingCP1(MillisCP1);

                }
                if(sPisPluggedin.getisPluggedinCP2().equals("t")){
                    sPisPoweFail.setisPowerFailCP2("t");
                    sPTimeReadingCP2.setTimeReadingCP2(MillisCP2);

                }
                if(sPisPluggedin.getisPluggedinCP3().equals("t")){
                    sPisPoweFail.setisPowerFailCP3("t");
                    sPTimeReadingCP3.setTimeReadingCP3(MillisCP3);

                }*//*


            }
        }
    };
    private final BroadcastReceiver batteryReceiver =  new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            if (intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)) {
                //   Toast.makeText(context, "The device is charging", Toast.LENGTH_SHORT).show();
                if (!isAlreadyonm){
                    startActivity(new Intent(MaindisplayActivityMannual.this,InitiatingActivityMannual.class));
                    finish();

                }


            } else {
                intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED);
                isAlreadyonm = false;
                // Toast.makeText(context, "The device is not charging", Toast.LENGTH_SHORT).show();
                tv_status1.setText(s6_CP1);
                String s = globalP1meter;
                float v = Float.parseFloat(s);
                int b=(int) v;
                if (b>=4) {
                    sPispowerfailafter4mrCP1.setispowerfailafter4mrCP1("t");
                    sPsavemoneyafterpfCP1.setMeterReadingCP1RO(mrsPOCP1);

                }
                toggleBtn.setCardBackgroundColor(Color.parseColor(red));
                toggleBtn.startAnimation(anim);
                tv_status2.setText(s6_CP2);
                String s2 = globalP2meter;
                float v2 = Float.parseFloat(s2);
                int b2=(int) v2;
                if (b2>=4) {
                    sPispowerfailafter4mrCP2.setispowerfailafter4mrCP2("t");
                }
                btn_p2onff.setCardBackgroundColor(Color.parseColor(red));
                btn_p2onff.startAnimation(anim);
                tv_status3.setText(s6_CP3);
                btn_p3onff.setCardBackgroundColor(Color.parseColor(red));
                btn_p3onff.startAnimation(anim);
                mFirebaseInstance.getReference(cid+"-CP1M").setValue(tv_status1.getText().toString());
                mFirebaseInstance.getReference(cid+"-CP2M").setValue(tv_status2.getText().toString());
                mFirebaseInstance.getReference(cid+"-CP3M").setValue(tv_status3.getText().toString());
                if(sPisPluggedin.getisPluggedinCP1().equals("t")){
                    sPisPoweFail.setisPowerFailCP1("t");
                    sPTimeReadingCP1.setTimeReadingCP1(MillisCP1);

                }
                if(sPisPluggedin.getisPluggedinCP2().equals("t")){
                    sPisPoweFail.setisPowerFailCP2("t");
                    sPTimeReadingCP2.setTimeReadingCP2(MillisCP2);

                }
                if(sPisPluggedin.getisPluggedinCP3().equals("t")){
                    sPisPoweFail.setisPowerFailCP3("t");
                    sPTimeReadingCP3.setTimeReadingCP3(MillisCP3);

                }
            }
        }
    };

    private void setToggleBtn() {

        if(flag)
        {
            StringBuilder newCommandp3 = new StringBuilder(command);
            newCommandp3.setCharAt(1, '1');

            command = ""+newCommandp3;
            toggleBtn.setCardBackgroundColor(Color.parseColor(green));
            toggleValue.setText("C1\n"+s23_CP1);

            // bulb_2.setCardBackgroundColor(Color.parseColor(AmberON));
            // toggleBtn.startAnimation(anim);
        }
        else
        {
            StringBuilder newCommandp3 = new StringBuilder(command);
            newCommandp3.setCharAt(1, '0');

            command = ""+newCommandp3;
            toggleBtn.setCardBackgroundColor(Color.parseColor(green));
            toggleValue.setText("CP1 "+s22_CP1);

            toggleBtn.clearAnimation();
            //bulb_2.setCardBackgroundColor(Color.parseColor(AmberOFF));

        }
    }
    private void setP2Btn() {

        if(p2flag)
        {
            StringBuilder newCommandp3 = new StringBuilder(command);
            newCommandp3.setCharAt(2, '1');

            command = ""+newCommandp3;
            btn_p2onff.setCardBackgroundColor(Color.parseColor(green));
            txt_p2btnValue.setText("C2\n"+s23_CP2);

            // bulb_2ii.setCardBackgroundColor(Color.parseColor(AmberON));
            // bulb_2ii.startAnimation(anim2);
        }
        else
        {
            StringBuilder newCommandp3 = new StringBuilder(command);
            newCommandp3.setCharAt(2, '0');

            command = ""+newCommandp3;
            btn_p2onff.setCardBackgroundColor(Color.parseColor(green));
            txt_p2btnValue.setText("CP2 "+s22_CP2);
            txt_p2btnValue.clearAnimation();

            // anim2.cancel();

            //bulb_2ii.setCardBackgroundColor(Color.parseColor(AmberOFF));

        }
    }
    private void setP3Btn() {

        if(p3flag)
        {
            StringBuilder newCommandp3 = new StringBuilder(command);
            newCommandp3.setCharAt(3, '1');

            command = ""+newCommandp3;
            btn_p3onff.setCardBackgroundColor(Color.parseColor(green));
            txt_p3btnValue.setText("C3\n"+s23_CP3);

            //  bulb_2iii.setCardBackgroundColor(Color.parseColor(AmberON));
            // bulb_2iii.startAnimation(anim3);
        }
        else
        {
            StringBuilder newCommandp3 = new StringBuilder(command);
            newCommandp3.setCharAt(3, '0');

            command = ""+newCommandp3;
            btn_p3onff.setCardBackgroundColor(Color.parseColor(green));
            txt_p3btnValue.setText("CP3 "+s22_CP3);
            txt_p3btnValue.clearAnimation();

            //anim3.cancel();

            // bulb_2iii.setCardBackgroundColor(Color.parseColor(AmberOFF));

        }
    }
    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {

        return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
        //creates secure outgoing connecetion with BT device using UUID
    }



    @Override
    public void onPause()
    {
        super.onPause();
        try
        {
            //Don't leave Bluetooth sockets open when leaving activity
            btSocket.close();
        } catch (IOException e2) {
            //insert code to deal with this
        }
    }
    //Checks that the Android device Bluetooth is available and prompts to be turned on if off
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
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }


        public void run() {
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
        //write method
        public void write(String input) {
            byte[] msgBuffer = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(msgBuffer);                //write bytes over BT connection via outstream
            } catch (IOException e) {
                //if you cannot write, close the application
                // Toast.makeText(getBaseContext(), "Connection Failure", Toast.LENGTH_LONG).show();
                finish();

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
    public void langSet(){
        if (sPlanguageCP1.getlanguageCP1().equals("en")){
            s1_CP1 = LangString.s1_E;  s2_CP1 = LangString.s2_E;  s3_CP1 = LangString.s3_E;  s4_CP1 = LangString.s4_E;  s5_CP1 = LangString.s5_E;  s6_CP1 = LangString.s6_E;  s7_CP1 = LangString.s7_E;  s8_CP1 = LangString.s8_E;  s9_CP1 = LangString.s9_E;  s10_CP1 = LangString.s10_E;  s11_CP1 = LangString.s11_E;  s12_CP1 = LangString.s12_E; s13_CP1 = LangString.s13_E; s14_CP1 = LangString.s14_E; s15_CP1 = LangString.s15_E; s16_CP1 = LangString.s16_E; s17_CP1 = LangString.s17_E; s21_CP1 = LangString.s21_E; s22_CP1 = LangString.s22_E; s23_CP1 = LangString.s23_E;s24_CP1 = LangString.s24_E;
        }
        else if (sPlanguageCP1.getlanguageCP1().equals("ma")){
            s1_CP1 = LangString.s1_M;  s2_CP1 = LangString.s2_M;  s3_CP1 = LangString.s3_M;  s4_CP1 = LangString.s4_M;  s5_CP1 = LangString.s5_M;  s6_CP1 = LangString.s6_M;  s7_CP1 = LangString.s7_M;  s8_CP1 = LangString.s8_M;  s9_CP1 = LangString.s9_M;  s10_CP1 = LangString.s10_M; s11_CP1 = LangString.s11_M;  s12_CP1 = LangString.s12_M; s13_CP1 = LangString.s13_M; s14_CP1 = LangString.s14_M; s15_CP1 = LangString.s15_M; s16_CP1 = LangString.s16_M; s17_CP1 = LangString.s17_M; s21_CP1 = LangString.s21_M;  s22_CP1 = LangString.s22_M; s23_CP1 = LangString.s23_M;s24_CP1 = LangString.s24_M;
        }
        else if (sPlanguageCP1.getlanguageCP1().equals("hi")){
            s1_CP1 = LangString.s1_H;  s2_CP1 = LangString.s2_H;  s3_CP1 = LangString.s3_H;  s4_CP1 = LangString.s4_H;  s5_CP1 = LangString.s5_H;  s6_CP1 = LangString.s6_H;  s7_CP1 = LangString.s7_H;  s8_CP1 = LangString.s8_H;  s9_CP1 = LangString.s9_H;  s10_CP1 = LangString.s10_H;  s11_CP1 = LangString.s11_H;  s12_CP1 = LangString.s12_H; s13_CP1 = LangString.s13_H; s14_CP1 = LangString.s14_H; s15_CP1 = LangString.s15_H; s16_CP1 = LangString.s16_H; s17_CP1 = LangString.s17_H; s21_CP1 = LangString.s21_H; s22_CP1 = LangString.s22_H; s23_CP1 = LangString.s23_H;s24_CP1 = LangString.s24_H;
        }


        if (sPlanguageCP2.getlanguageCP2().equals("en")){
            s1_CP2 = LangString.s1_E;  s2_CP2 = LangString.s2_E;  s3_CP2 = LangString.s3_E;  s4_CP2 = LangString.s4_E;  s5_CP2 = LangString.s5_E;  s6_CP2 = LangString.s6_E;  s7_CP2 = LangString.s7_E;  s8_CP2 = LangString.s8_E;  s9_CP2 = LangString.s9_E;  s10_CP2 = LangString.s10_E;  s11_CP2 = LangString.s11_E;  s12_CP2 = LangString.s12_E; s13_CP2 = LangString.s13_E; s14_CP2 = LangString.s14_E; s15_CP2 = LangString.s15_E; s16_CP2 = LangString.s16_E; s17_CP2 = LangString.s17_E; s21_CP2 = LangString.s21_E; s22_CP2 = LangString.s22_E; s23_CP2 = LangString.s23_E;s24_CP2 = LangString.s24_E;

        }
        else if (sPlanguageCP2.getlanguageCP2().equals("ma")){
            s1_CP2 = LangString.s1_M;  s2_CP2 = LangString.s2_M;  s3_CP2 = LangString.s3_M;  s4_CP2 = LangString.s4_M;  s5_CP2 = LangString.s5_M;  s6_CP2 = LangString.s6_M;  s7_CP2 = LangString.s7_M;  s8_CP2 = LangString.s8_M;  s9_CP2 = LangString.s9_M;  s10_CP2 = LangString.s10_M; s11_CP2 = LangString.s11_M;  s12_CP2 = LangString.s12_M; s13_CP2 = LangString.s13_M; s14_CP2 = LangString.s14_M; s15_CP2 = LangString.s15_M; s16_CP2 = LangString.s16_M; s17_CP2 = LangString.s17_M; s21_CP2 = LangString.s21_M;  s22_CP2 = LangString.s22_M; s23_CP2 = LangString.s23_M;s24_CP2 = LangString.s24_M;

        }
        else if (sPlanguageCP2.getlanguageCP2().equals("hi")){
            s1_CP2 = LangString.s1_H;  s2_CP2 = LangString.s2_H;  s3_CP2 = LangString.s3_H;  s4_CP2 = LangString.s4_H;  s5_CP2 = LangString.s5_H;  s6_CP2 = LangString.s6_H;  s7_CP2 = LangString.s7_H;  s8_CP2 = LangString.s8_H;  s9_CP2 = LangString.s9_H;  s10_CP2 = LangString.s10_H;  s11_CP2 = LangString.s11_H;  s12_CP2 = LangString.s12_H; s13_CP2 = LangString.s13_H; s14_CP2 = LangString.s14_H; s15_CP2 = LangString.s15_H; s16_CP2 = LangString.s16_H; s17_CP2 = LangString.s17_H; s21_CP2 = LangString.s21_H; s22_CP2 = LangString.s22_H; s23_CP2 = LangString.s23_H;s24_CP2 = LangString.s24_H;

        }


        if (sPlanguageCP3.getlanguageCP3().equals("en")){
            s1_CP3 = LangString.s1_E;  s2_CP3 = LangString.s2_E;  s3_CP3 = LangString.s3_E;  s4_CP3 = LangString.s4_E;  s5_CP3 = LangString.s5_E;  s6_CP3 = LangString.s6_E;  s7_CP3 = LangString.s7_E;  s8_CP3 = LangString.s8_E;  s9_CP3 = LangString.s9_E;  s10_CP3 = LangString.s10_E;  s11_CP3 = LangString.s11_E;  s12_CP3 = LangString.s12_E; s13_CP3 = LangString.s13_E; s14_CP3 = LangString.s14_E; s15_CP3 = LangString.s15_E; s16_CP3 = LangString.s16_E; s17_CP3 = LangString.s17_E; s21_CP3 = LangString.s21_E; s22_CP3 = LangString.s22_E; s23_CP3 = LangString.s23_E;s24_CP3 = LangString.s24_E;
        }
        else if (sPlanguageCP3.getlanguageCP3().equals("ma")){
            s1_CP3 = LangString.s1_M;  s2_CP3 = LangString.s2_M;  s3_CP3 = LangString.s3_M;  s4_CP3 = LangString.s4_M;  s5_CP3 = LangString.s5_M;  s6_CP3 = LangString.s6_M;  s7_CP3 = LangString.s7_M;  s8_CP3 = LangString.s8_M;  s9_CP3 = LangString.s9_M;  s10_CP3 = LangString.s10_M; s11_CP3 = LangString.s11_M;  s12_CP3 = LangString.s12_M; s13_CP3 = LangString.s13_M; s14_CP3 = LangString.s14_M; s15_CP3 = LangString.s15_M; s16_CP3 = LangString.s16_M; s17_CP3 = LangString.s17_M; s21_CP3 = LangString.s21_M;  s22_CP3 = LangString.s22_M; s23_CP3 = LangString.s23_M;s24_CP3 = LangString.s24_M;
        }
        else if (sPlanguageCP3.getlanguageCP3().equals("hi")){
            s1_CP3 = LangString.s1_H;  s2_CP3 = LangString.s2_H;  s3_CP3 = LangString.s3_H;  s4_CP3 = LangString.s4_H;  s5_CP3 = LangString.s5_H;  s6_CP3 = LangString.s6_H;  s7_CP3 = LangString.s7_H;  s8_CP3 = LangString.s8_H;  s9_CP3 = LangString.s9_H;  s10_CP3 = LangString.s10_H;  s11_CP3 = LangString.s11_H;  s12_CP3 = LangString.s12_H; s13_CP3 = LangString.s13_H; s14_CP3 = LangString.s14_H; s15_CP3 = LangString.s15_H; s16_CP3 = LangString.s16_H; s17_CP3 = LangString.s17_H; s21_CP3 = LangString.s21_H; s22_CP3 = LangString.s22_H; s23_CP3 = LangString.s23_H;s24_CP3 = LangString.s24_H;
        }
        // s1_CP2 = "CHARGER IS ON";  s2_CP2 = "EMERGENCY STOPPED";  s3_CP2 = "OVER CURRENT";  s4_CP2 = "EARTH FAULT";  s5_CP2 = "TEMP HAZARD";  s6_CP2 = "POWER FAILURE";  s7_CP2 = "  VEHICLE\n PLUGED OUT";  s8_CP2 = "IDLE";  s9_CP2 = "  RESUMING\n  AFTER PF";  s10_CP2 = " CHARGING IN\n   PROCESS";  s11_CP2 = "  VEHICLE\n PLUGED IN";  s12_CP2 = "UNDER VOLTAGE"; s13_CP2 = "OVER VOLTAGE";
        //s1_CP3 = "CHARGER IS ON";  s2_CP3 = "EMERGENCY STOPPED";  s3_CP3 = "OVER CURRENT";  s4_CP3 = "EARTH FAULT";  s5_CP3 = "TEMP HAZARD";  s6_CP3 = "POWER FAILURE";  s7_CP3 = "  VEHICLE\n PLUGED OUT";  s8_CP3 = "IDLE";  s9_CP3 = "  RESUMING\n  AFTER PF";  s10_CP3 = " CHARGING IN\n   PROCESS";  s11_CP3 = "  VEHICLE\n PLUGED IN";  s12_CP3 = "UNDER VOLTAGE"; s13_CP3 = "OVER VOLTAGE";
    }

    private void customToast(String message)
    {
        if (toast!=null)
        {
            toast.cancel();
        }
        LayoutInflater inflater = getLayoutInflater();
        View toastLayout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.llCustom));
        TextView textView = toastLayout.findViewById(R.id.customToastMsg);
        textView.setText(message);
        toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(toastLayout);
        toast.show();
    }
}*/
