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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.jcmore2.appcrash.AppCrash;
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

import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import me.aflak.bluetooth.Bluetooth;
import me.aflak.bluetooth.BluetoothCallback;
import me.aflak.bluetooth.DeviceCallback;
import me.aflak.bluetooth.DiscoveryCallback;


public class MaindisplayActivityMannual extends AppCompatActivity {

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

    TextView txt_maininstruction;
    // Boolean isSavedTimeC1 = false,isSavedTimeC2 = false,isSavedTimeC3 = false;
    // Boolean isComeFrom10C1 = false,isComeFrom10C2 = false,isComeFrom10C3 = false;
    Boolean isComefromPFUVOV = false;
    Boolean is99Comming = true;
    int counterPFUVOV = 0;


    //---TriggerProgram   //_______________________________________17-05-2018
    int i,clearcachecount;
    Timer timer;
    int threadCurrentCount = 0, threadPreviousCount = 0;
    Bluetooth bluetooth;


   */
/* private final Context mAcivityContext;



    private MaindisplayActivityMannual(Context context) {
        mAcivityContext = context.getApplicationContext();
    }*//*



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("CRASH REPORT","onCreate Method");

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        setContentView(R.layout.newdesignm);
        Log.e("CHECK", "CHECKING");
        // AppCrash.get().showDialog();
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
        Log.e("CRASH REPORT","onRestart Method");

    }

    @Override
    protected void onStart() {
        super.onStart();
        bluetooth = new Bluetooth(getApplicationContext());
        bluetooth.onStart();


        //  Toast.makeText(MaindisplayActivity.this,"onStart Method",Toast.LENGTH_LONG).show();
        Log.e("METHOD","onStart Method");
        Log.e("CRASH REPORT","onStart Method");

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("CRASH REPORT","onResume Method");

        //=================================================================================================
        i = 0;
        clearcachecount = 0;
        */
/*TimerTask task = new TimerTask() {
            @Override
            public void run() {

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        //TODO your background code
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {

                                Toast.makeText(MaindisplayActivityMannual.this, "Count = "+i+"\nthreadPreviousCount : "+threadPreviousCount+"\nthreadCurrentCount"+threadCurrentCount+"\nclearCachecount :"+clearcachecount, Toast.LENGTH_SHORT).show();
                                if (i>4 ){
                                    if (threadPreviousCount==threadCurrentCount && Power.isConnected(MaindisplayActivityMannual.this)){
                                        //trigger
                                        if(sPisPluggedin.getisPluggedinCP1().equals("t")){
                                            sPisPoweFail.setisPowerFailCP1("t");
                                            sPTimeReadingCP1.setTimeReadingCP1(MillisCP1);
                                            // isSavedTimeC1 = true;
                                        }
                                        if(sPisPluggedin.getisPluggedinCP2().equals("t")){
                                            sPisPoweFail.setisPowerFailCP2("t");
                                            sPTimeReadingCP2.setTimeReadingCP2(MillisCP2);
                                            // isSavedTimeC2 = true;
                                        }
                                        if(sPisPluggedin.getisPluggedinCP3().equals("t")){
                                            sPisPoweFail.setisPowerFailCP3("t");
                                            sPTimeReadingCP3.setTimeReadingCP3(MillisCP3);
                                            //  isSavedTimeC3 = true;
                                        }

                                        startActivity(new Intent(MaindisplayActivityMannual.this,InitiatingActivityMannual.class));
                                        finish();
                                    }
                                    else{

                                        threadPreviousCount = 0;
                                        threadCurrentCount  = 0;
                                        i = 5;


                                    }


                                }
                                i++;



                                if (clearcachecount > 150){
                                    clearCache();
                                    Log.e("Cache","cleared");
                                    clearcachecount = 0;
                                }else {
                                    clearcachecount++;
                                }
                            }
                        });
                    }
                });
            }
        };
        timer = new Timer();
        long delay = 0;
        long intevalPeriod = 1 * 8000;
        // schedules the task to be run in an interval
        timer.scheduleAtFixedRate(task, delay,
                intevalPeriod);
*//*




        //======================================================================================================
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
        txt_maininstruction = findViewById(R.id.txt_maininstruction);
        txt_maininstruction.setSelected(true);

        if (sPsavemoneyafterpfCP1.getMeterReadingCP1RO().isEmpty()){
            sPsavemoneyafterpfCP1.setMeterReadingCP1RO("0");
        }


        if (sPispowerfailafter4mrCP1.getispowerfailafter4mrCP1().isEmpty()){
            sPispowerfailafter4mrCP1.setispowerfailafter4mrCP1("f");
        }
        if (sPispowerfailafter4mrCP2.getispowerfailafter4mrCP2().isEmpty()){
            sPispowerfailafter4mrCP2.setispowerfailafter4mrCP2("f");
        }
        if (sPispowerfailafter4mrCP3.getispowerfailafter4mrCP3().isEmpty()){
            sPispowerfailafter4mrCP3.setispowerfailafter4mrCP3("f");
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



        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        this.registerReceiver(mReceiver, filter);
        IntentFilter batteryfilter = new IntentFilter();
        batteryfilter.addAction(Intent.ACTION_POWER_CONNECTED);
        batteryfilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        registerReceiver(batteryReceiver,batteryfilter);
//=======================================================================================================================
        bluetooth.setBluetoothCallback(new BluetoothCallback() {
            @Override
            public void onBluetoothTurningOn() {

            }

            @Override
            public void onBluetoothOn() {

            }

            @Override
            public void onBluetoothTurningOff() {

            }

            @Override
            public void onBluetoothOff() {

            }

            @Override
            public void onUserDeniedActivation() {

            }
        });


        List<BluetoothDevice> devices = bluetooth.getPairedDevices();

        Log.e("BluetoothDeviceList","BluetoothDeviceLISt"+devices);
        bluetooth.connectToName("HC-05");

        bluetooth.setDiscoveryCallback(new DiscoveryCallback() {
            @Override
            public void onDiscoveryStarted() {
                Log.e("onDiscoveryStarted","onDiscoveryStarted");

            }

            @Override
            public void onDiscoveryFinished() {
                Log.e("onDiscoveryFinished","onDiscoveryFinished");

            }

            @Override
            public void onDeviceFound(BluetoothDevice device) {
                Log.e("onDeviceFound","onDeviceFound"+device);

            }

            @Override
            public void onDevicePaired(BluetoothDevice device) {
                Log.e("onDevicePaired","onDevicePaired"+device);

            }

            @Override
            public void onDeviceUnpaired(BluetoothDevice device) {
                Log.e("onDeviceUnpaired","onDeviceUnpaired"+device);

            }

            @Override
            public void onError(String message) {
                Log.e("setDiscoveryCallback","onError"+message);

            }
        });

        bluetooth.setDeviceCallback(new DeviceCallback() {
            @Override
            public void onDeviceConnected(BluetoothDevice device) {
                Log.e("setDeviceCallback","onDeviceConnected"+device);
            }

            @Override
            public void onDeviceDisconnected(BluetoothDevice device, String message) {
                Log.e("setDeviceCallback","onDeviceDisconnected"+device+", message :"+message);

            }

            @Override
            public void onMessage(String message) {
                Log.e("setDeviceCallback","onMessage"+message);

            }

            @Override
            public void onError(String message) {
                Log.e("setDeviceCallback","onError"+message);

            }

            @Override
            public void onConnectError(BluetoothDevice device, String message) {
                Log.e("setDeviceCallback","onConnectError"+device+", message"+message);

            }
        });

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
//=                mConnectedThread.write("" + newCommandp1);    // Send "0" via Bluetooth


            } else {
                StringBuilder newCommandp1 = new StringBuilder(command);
                newCommandp1.setCharAt(1, '1');


                Log.e("newCommandp1", "" + newCommandp1);
//=                mConnectedThread.write("" + newCommandp1);    // Send "0" via Bluetooth

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
//=                mConnectedThread.write("" + newCommandp2);    // Send "0" via Bluetooth
            }
            else
            {
                StringBuilder newCommandp2 = new StringBuilder(command);
                newCommandp2.setCharAt(2, '1');

                Log.e("newCommandp2", "" + newCommandp2);
//=                mConnectedThread.write("" + newCommandp2);    // Send "1" via Bluetooth
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
                //=               mConnectedThread.write("" + newCommandp3);    // Send "0" via Bluetooth
            }
            else
            {
                StringBuilder newCommandp3 = new StringBuilder(command);
                newCommandp3.setCharAt(3, '1');

                Log.e("newCommandp3", "" + newCommandp3);
                //=               mConnectedThread.write("" + newCommandp3);    // Send "0" via Bluetooth

            }

            setP3Btn();
        }
    }

    @Override
    protected void onStop()
    {
        timer.cancel();
        // Toast.makeText(MaindisplayActivity.this,"onStop Method",Toast.LENGTH_LONG).show();
        Log.e("CRASH REPORT","onStop Method");

        Log.e("METHOD","onStop Method");
        bluetooth.onStop();


        unregisterReceiver(mReceiver);
        unregisterReceiver(batteryReceiver);

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Toast.makeText(MaindisplayActivity.this,"onDestroy Method",Toast.LENGTH_LONG).show();
        Log.e("METHOD","onDestroy Method");
        Log.e("CRASH REPORT","onDestroy Method");
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
                    //sPsavemoneyafterpfCP1.setMeterReadingCP1RO(mrsPOCP1);

                    sPsavemoneyafterpfCP1.setMeterReadingCP1RO("4.00");

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
                String s3 = globalP3meter;
                float v3 = Float.parseFloat(s3);
                int b3=(int) v3;
                if (b3>=4) {
                    sPispowerfailafter4mrCP3.setispowerfailafter4mrCP3("t");
                }
                btn_p3onff.setCardBackgroundColor(Color.parseColor(red));
                btn_p3onff.startAnimation(anim);
                mFirebaseInstance.getReference(cid+"-CP1M").setValue(tv_status1.getText().toString());
                mFirebaseInstance.getReference(cid+"-CP2M").setValue(tv_status2.getText().toString());
                mFirebaseInstance.getReference(cid+"-CP3M").setValue(tv_status3.getText().toString());
                if(sPisPluggedin.getisPluggedinCP1().equals("t")){
                    sPisPoweFail.setisPowerFailCP1("t");
                    sPTimeReadingCP1.setTimeReadingCP1(MillisCP1);
                    // isSavedTimeC1 = true;
                }
                if(sPisPluggedin.getisPluggedinCP2().equals("t")){
                    sPisPoweFail.setisPowerFailCP2("t");
                    sPTimeReadingCP2.setTimeReadingCP2(MillisCP2);
                    // isSavedTimeC2 = true;
                }
                if(sPisPluggedin.getisPluggedinCP3().equals("t")){
                    sPisPoweFail.setisPowerFailCP3("t");
                    sPTimeReadingCP3.setTimeReadingCP3(MillisCP3);
                    // isSavedTimeC3 = true;
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
                    sPsavemoneyafterpfCP1.setMeterReadingCP1RO("4.00");

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
                String s3 = globalP3meter;
                float v3 = Float.parseFloat(s3);
                int b3=(int) v3;
                if (b3>=4) {
                    sPispowerfailafter4mrCP3.setispowerfailafter4mrCP3("t");
                }
                btn_p3onff.setCardBackgroundColor(Color.parseColor(red));
                btn_p3onff.startAnimation(anim);
                mFirebaseInstance.getReference(cid+"-CP1M").setValue(tv_status1.getText().toString());
                mFirebaseInstance.getReference(cid+"-CP2M").setValue(tv_status2.getText().toString());
                mFirebaseInstance.getReference(cid+"-CP3M").setValue(tv_status3.getText().toString());
                if(sPisPluggedin.getisPluggedinCP1().equals("t")){
                    sPisPoweFail.setisPowerFailCP1("t");
                    sPTimeReadingCP1.setTimeReadingCP1(MillisCP1);
                    // isSavedTimeC1 = true;
                }
                if(sPisPluggedin.getisPluggedinCP2().equals("t")){
                    sPisPoweFail.setisPowerFailCP2("t");
                    sPTimeReadingCP2.setTimeReadingCP2(MillisCP2);
                    // isSavedTimeC2 = true;
                }
                if(sPisPluggedin.getisPluggedinCP3().equals("t")){
                    sPisPoweFail.setisPowerFailCP3("t");
                    sPTimeReadingCP3.setTimeReadingCP3(MillisCP3);
                    //  isSavedTimeC3 = true;
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



    @Override
    public void onPause()
    {
        super.onPause();
        timer.cancel();

        Log.e("CRASH REPORT","onPause Method");

        if(sPisPluggedin.getisPluggedinCP1().equals("t")){
            sPisPoweFail.setisPowerFailCP1("t");
           */
/* if (!isSavedTimeC1) {
                sPTimeReadingCP1.setTimeReadingCP1(MillisCP1);
            }*//*


        }
        if(sPisPluggedin.getisPluggedinCP2().equals("t")){
            sPisPoweFail.setisPowerFailCP2("t");
           */
/* if (!isSavedTimeC2) {
                sPTimeReadingCP2.setTimeReadingCP2(MillisCP2);
            }
*//*

        }
        if(sPisPluggedin.getisPluggedinCP3().equals("t")){
            sPisPoweFail.setisPowerFailCP3("t");
            */
/*if (!isSavedTimeC3) {
                sPTimeReadingCP3.setTimeReadingCP3(MillisCP3);
            }*//*


        }

        try
        {
            //Don't leave Bluetooth sockets open when leaving activity
            btSocket.close();
        } catch (IOException e2) {
            //insert code to deal with this
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



    public boolean clearCache() {
        try {
            File[] files = getBaseContext().getCacheDir().listFiles();

            for (File file : files) {

                // delete returns boolean we can use
                if (!file.delete()) {
                    return false;
                }
            }

            // if for completes all
            return true;

        } catch (Exception e) {}

        // try stops clearing cache
        return false;
    }
}*/
