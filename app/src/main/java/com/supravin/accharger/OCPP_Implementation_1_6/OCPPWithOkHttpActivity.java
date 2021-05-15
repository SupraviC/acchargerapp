package com.supravin.accharger.OCPP_Implementation_1_6;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.supravin.accharger.Authentication;
import com.supravin.accharger.OCPP_Implementation_1_6.DataBase.DBManager;
import com.supravin.accharger.R;
import com.supravin.accharger.Storage.SharedPreferenceIPAddress;
import com.supravin.accharger.InitiatingActivityOcpp;
import com.supravin.accharger.LangSelection;
import com.supravin.accharger.LangString;
import com.supravin.accharger.OCPP_Implementation_1_6.DataBase.DBManager;
import com.supravin.accharger.OCPP_Implementation_1_6.DataBase.DatabaseHelper;
import com.supravin.accharger.OCPP_Implementation_1_6.Receiver.ConnectivityReceiver;
import com.supravin.accharger.OCPP_Implementation_1_6.Utils.InternetCheck;
import com.supravin.accharger.PowerPlugged.Power;
import com.supravin.accharger.R;
import com.supravin.accharger.Storage.SPOTPForCP1;
import com.supravin.accharger.Storage.SPTimeReadingCP1;
import com.supravin.accharger.Storage.SPTimeReadingCP2;
import com.supravin.accharger.Storage.SPTimeReadingCP3;
import com.supravin.accharger.Storage.SPisPluggedin;
import com.supravin.accharger.Storage.SPisPoweFail;
import com.supravin.accharger.Storage.SPlanguageCP1;
import com.supravin.accharger.Storage.SPlanguageCP2;
import com.supravin.accharger.Storage.SPlanguageCP3;
import com.supravin.accharger.Storage.SPmeterReadingCP1;
import com.supravin.accharger.Storage.SPmeterReadingCP1RO;
import com.supravin.accharger.Storage.SPmeterReadingCP2;
import com.supravin.accharger.Storage.SPmeterReadingCP2RO;
import com.supravin.accharger.Storage.SPmeterReadingCP3;
import com.supravin.accharger.Storage.SPmeterReadingCP3RO;
import com.supravin.accharger.Storage.SPsavemoneyafterpfCP1;
import com.supravin.accharger.Storage.SharedPreferenceIPAddress;
import com.supravin.accharger.Storage.SharedPreferenceUnitR;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.aflak.bluetooth.Bluetooth;
import me.aflak.bluetooth.BluetoothCallback;
import me.aflak.bluetooth.DeviceCallback;
import me.aflak.bluetooth.DiscoveryCallback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;


public class OCPPWithOkHttpActivity
        extends AppCompatActivity implements
        ConnectivityReceiver.ConnectivityReceiverListener{

    CardView toggleBtn,btn_p2onff,btn_p3onff,cardViewForNetwork;
    String _CMSCommand="NONE";
    //long intervalHeartBeat=15000;//15sec
    long intervalHeartBeat=300000;//5min
    TextView toggleValue,txt_p2btnValue,txt_p3btnValue;

    boolean flag=false,p2flag = false,p3flag= false;

    //---------- For Round Robin -------------//
    private int countC1,countC2,countC3;
    private boolean isAvailableC1,isAvailableC2,isAvailableC3;
    private boolean isStartC1,isStartC2,isStartC3;
    private boolean isStopC1,isStopC2,isStopC3;
    //---------- For Round Robin -------------//

    String red="#f21200";
    String green="#3DAA4C";
    static Animation anim,anim2,anim3;
    String command= "$000";

    LinearLayout connect1,connect2,connect3,parent1;
    TextView txt_c1round_off_mr, txt_c2round_off_mr, txt_c3round_off_mr; //unit consumption variables
    TextView txt_c1diff,txt_c2diff,txt_c3diff; //time difference
    TextView tv_status1,tv_status2,tv_status3,txt_pleasewait; // status

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
    private SPOTPForCP1 spotpForCP1;
    //private SharedPreferences sharedPreTransactionId;


    //-----------------------CP1
    private int plugedinCountcp1 = 0;
    private int plugedoutCountcp1 = 0;
    private boolean isResumedAftercp1 = false;
    private boolean isStillOnCP1 = false;

    int counterConnectorC1On=0;
    int counterConnectorC2On=0;
    int counterConnectorC3On=0;

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
    LinearLayout layout_detail,layout_pleasewait/*,layoutOTP*/;
    FrameLayout layout_main;
    ImageView imageView_close;
    private boolean flagEmergencyStopped=false;
    TextView txt_cpname,txt_rate,txt_voltage_detail,txt_current_detail,txt_power_detail,txt_unit_detail,txt_etime_detail,txt_status_detail;
    String voltage_detailCP1="000",current_detailCP1="00.00",power_detailCP1="000",unit_detailCP1="000", etime_detailCP1="00:00",status_detailCP1="IDLE";
    String voltage_detailCP2="000",current_detailCP2="00.00",power_detailCP2="000",unit_detailCP2="000", etime_detailCP2="00:00",status_detailCP2="IDLE";
    String voltage_detailCP3="000",current_detailCP3="00.00",power_detailCP3="000",unit_detailCP3="000", etime_detailCP3="00:00",status_detailCP3="IDLE";

    String unit_detailCP21="000",unit_detailCP22="000",unit_detailCP23="000";
    String etime_detailCP21="00:00",etime_detailCP22="00:00",etime_detailCP23="00:00";
    //----------
    String detail_flag = "";
    boolean isClickedCancel = false;
    boolean flagIsCancel=false;
    boolean flagResetVals=false;
    int countN=0;
    //--- for Rs
    String rsCP1 = "₹ 00.00", rsCP2 = "₹ 00.00", rsCP3 = "₹ 00.00",rsPOCP1 = "₹ 00.00", rsPOCP2 = "₹ 00.00", rsPOCP3 = "₹ 00.00",mrsPOCP1 = "₹ 00.00", mrsPOCP2 = "₹ 00.00", mrsPOCP3 = "₹ 00.00";
    String rsCP21 = "₹ 00.00", rsCP22 = "₹ 00.00", rsCP23 = "₹ 00.00",rsPOCP21 = "₹ 00.00", rsPOCP22 = "₹ 00.00", rsPOCP23 = "₹ 00.00",mrsPOCP21 = "₹ 00.00", mrsPOCP22 = "₹ 00.00", mrsPOCP23 = "₹ 00.00";
    //---for overlay________
    RelativeLayout relativeLayout1st,relativeLayout2nd,relativeLayout3rd;
    int overlayCounti = 0,overlayCountii = 0,overlayCountiii = 0;
    String _OtpIs="0";
    String isAuthReq="0";
    String isStatusZeroSend="0";
    String _StatusIS="0";
    String OTPIs="";
    String status="--";
    Dialog dialog;
    EditText edt_OTP0;
    private boolean checkAuth=false;
    //language Selection
    private String s1_CP1,s2_CP1,s3_CP1,s4_CP1,s5_CP1,s6_CP1,s7_CP1,s8_CP1,s9_CP1,s10_CP1,s11_CP1,s12_CP1,s13_CP1,s14_CP1,s15_CP1,s16_CP1,s17_CP1,s18_CP1,s19_CP1,s20_CP1,s21_CP1,s22_CP1,s23_CP1,s24_CP1;
    private String s1_CP2,s2_CP2,s3_CP2,s4_CP2,s5_CP2,s6_CP2,s7_CP2,s8_CP2,s9_CP2,s10_CP2,s11_CP2,s12_CP2,s13_CP2,s14_CP2,s15_CP2,s16_CP2,s17_CP2,s18_CP2,s19_CP2,s20_CP2,s21_CP2,s22_CP2,s23_CP2,s24_CP2;
    private String s1_CP3,s2_CP3,s3_CP3,s4_CP3,s5_CP3,s6_CP3,s7_CP3,s8_CP3,s9_CP3,s10_CP3,s11_CP3,s12_CP3,s13_CP3,s14_CP3,s15_CP3,s16_CP3,s17_CP3,s18_CP3,s19_CP3,s20_CP3,s21_CP3,s22_CP3,s23_CP3,s24_CP3;
    private TextView txt_touch1,txt_touch2,txt_touch3,txt_etime_display1,txt_etime_display2,txt_etime_display3,txt_etime_displayf,txt_unit_display,txt_rate_display;

    private SPlanguageCP1 sPlanguageCP1;
    private SPlanguageCP2 sPlanguageCP2;
    private SPlanguageCP3 sPlanguageCP3;
    private SharedPreferenceUnitR sharedPreference;
    private SharedPreferenceIPAddress sharedPreferenceIPAddress;

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

    SPmeterReadingCP1RO sPmeterReadingCP1RO;
    SPmeterReadingCP2RO sPmeterReadingCP2RO;
    SPmeterReadingCP3RO sPmeterReadingCP3RO;

    //-------save money
    SPsavemoneyafterpfCP1 sPsavemoneyafterpfCP1;
    private String globalP1meter = "0",globalP2meter="0",globalP3meter="0";
    private boolean sendAvailStatusForC1=true;
    private boolean sendAvailStatusForC2=true;
    private boolean sendAvailStatusForC3=true;
    String emergency01="0";
    String emergency02="0";
    String emergency03="0";

    String overCurrent01="0";
    String overCurrent02="0";
    String overCurrent03="0";

    String overVoltage01="0";
    String overVolatage02="0";
    String overVolatage03="0";

    String underVoltage01="0";
    String underVolatage02="0";
    String underVolatage03="0";

    TextView txt_maininstruction;
    Boolean isComefromPFUVOV = false;
    Boolean is99Comming = true;
    int counterPFUVOV = 0;

    //---TriggerProgram   //_______________________________________17-05-2018
    int i,clearcachecount;
    Timer timer;
    int threadCurrentCount = 0, threadPreviousCount = 0;

    boolean isMainthreadend;
    int countMainCalls=0;
    int mainMethodCall=0;
    private static final int NORMAL_CLOSURE_STATUS = 1000;

    boolean isOTPValid=false;
    //-----------------------------------
    Bluetooth bluetooth;
    //-----11-06-2018
    Button btn_tick1,btn_tick2,btn_tick3,btn_tick4;
    String sequence_match = "";
    private HashMap<String,String> hashMapRequestsStack=new HashMap<>();
    private HashMap<String,String> hashMapSTOPRequests=new HashMap<>();
    private List<String> listMapOtherRequests =new ArrayList<>();
    //-------OCPP Implementation--------------------//
    private DBManager dbManager;
    private String transactionIdOFFLINE;
    int uploadingCall=0;
    WebSocket webSocketokhttp;
    boolean isSendBootNotification=true;
    boolean internetFlag;
    boolean uploadingCalledFlag=true;
    boolean sendPowerFailStatus=true;
    boolean sendOverCurrentStatusC1 =true;
    boolean sendOverCurrentStatusC2=true;
    boolean sendOverCurrentStatusC3=true;
    boolean sendEmergencyStatus=true;
    boolean sendAvailAftrEmergencyStatus=true;
    boolean isSendEmergency=false;
    boolean sendUnderVoltageStatusC1=true;
    boolean sendUnderVoltageStatusC2=true;
    boolean sendUnderVoltageStatusC3=true;
    boolean sendOverVoltageStatusC1 =true;
    boolean sendOverVoltageStatusC2=true;
    boolean sendOverVoltageStatusC3=true;
    boolean isStartSendC1 =true;
    boolean isStopSendC1 =true;
    boolean isStartSendC2 =true;
    boolean isStopSendC2 =true;
    boolean isStartSendC3 =true;
    boolean isStopSendC3 =true;
    float meterreadingdiffC1=0f;
    float meterreadingdiffC2=0f;
    float meterreadingdiffC3=0f;
    String sessionID="0";
    String sessionNumberC1="0";
    String sessionNumberC2="0";
    String sessionNumberC3="0";
    String sessionNumberC1OTP="0";
    String sessionNumberC2OTP="0";
    String sessionNumberC3OTP="0";
    String reasonC="Local";
    Request requestWS;
    EchoWebSocketListener listenerWS;
    Handler pingHandlerMVC1;
    Handler pingHandlerMVC2;
    Runnable pingRunnableC1;
    Runnable pingRunnableC2;
    Handler pingHandlerMVC3;
    Runnable pingRunnableC3;
    Runnable pingRunnableHeartBeat;
    Handler pingHandlerHeartBeat;
    String transactionIDC1="0";
    String transactionIDC2="0";
    String transactionIDC3="0";
    boolean isInternetAvailableForC1=true;
    boolean isInternetAvailableForC2=true;
    boolean isInternetAvailableForC3=true;
    boolean isInternetINITForC1=true;
    boolean isInternetINITForC2=true;
    boolean isInternetINITForC3=true;
    String connectorIS;
   /* private ProgressDialog kioskProgressDialog;
    private SweetAlertDialog progressDialog;*/

    ProgressDialog progressBar;

    //private SweetAlertDialog informationDialog;

    private int countNotConnected=0;
    //-------OCPP Implementation--------------------//


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flagEmergencyStopped=false;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        setContentView(R.layout.newdesignm);
        bluetooth = new Bluetooth(OCPPWithOkHttpActivity.this);
        hashMapRequestsStack=new HashMap<>();
        countNotConnected=0;
        //-----------Changes Starts Here-------------//
        sharedPreferenceIPAddress = new SharedPreferenceIPAddress();
        dbManager=new DBManager(this);
        dbManager.open();
        webSocketokhttp=openConnection();

        startSendingHeartBeats();

        cardViewForNetwork = findViewById(R.id.cardViewForNetwork);
        //cardViewForNetwork.setCardBackgroundColor(Color.parseColor("#FF1000"));
        //cardViewForNetwork.startAnimation(anim);

        checkConnection();

/*
        new InternetCheck(new InternetCheck.Consumer() {
            @Override
            public void accept(Boolean internet) {
                Log.e("CHECKINTERNETNEW"," --> "+internet);
            }
        });
*/

    }

    // Method to manually check connection status
    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (isConnected){
            cardViewForNetwork.setCardBackgroundColor(Color.parseColor("#00FF0A"));
            Log.e("ISCCCCCCCCCCCCCC","IF - -> "+isConnected);

        }else {
            cardViewForNetwork.setCardBackgroundColor(Color.parseColor("#FF1000"));
            Log.e("ISCCCCCCCCCCCCCC"," ELSE - -> "+isConnected);
        }
    }


    //---------------------Method To Sending HB_____________________________//
    private void startSendingHeartBeats() {

        if (isSendBootNotification){
            isSendBootNotification=false;
            new InternetCheck(new InternetCheck.Consumer() {
                @Override
                public void accept(Boolean internet) {
                    Log.e("INSERTINTODB"," MAINTHREAD--> TOCHECKINTERNET");
                    if (internet){
                        webSocketokhttp=openConnection();
                        webSocketokhttp.send(JSONObjectsForRequest.sendBootNotificationRequest(generateSessionId()));
                        webSocketokhttp.send(JSONObjectsForRequest.sendStatusNotificationRequest("0",
                                "NoError","Available",getUTC(), generateSessionId()));
                    }else {
                        dbManager.insert(JSONObjectsForRequest.sendBootNotificationRequest(generateSessionId()),"BOOT","True","0");
                        dbManager.insert(JSONObjectsForRequest.sendStatusNotificationRequest("0",
                                "NoError","Available",getUTC(), generateSessionId()),"STATUS","True","0");
                    }
                    isSendBootNotification=false;
                }
            });
        }


        new InternetCheck(new InternetCheck.Consumer() {
            @Override
            public void accept(Boolean internet) {
                if (internet){
                    pingHandlerHeartBeat = new Handler();
                    pingHandlerHeartBeat.removeCallbacks(pingRunnableHeartBeat);
                    pingHandlerHeartBeat.removeCallbacksAndMessages(null);
                    pingRunnableHeartBeat = new Runnable() {
                        @Override public void run() {
                            webSocketokhttp=openConnection();
                            webSocketokhttp.send(new JSONArray().put(2).put(generateSessionId())
                                    .put("Heartbeat").put(new JSONObject()).toString());

                            if (intervalHeartBeat!=0){
                                pingHandlerHeartBeat.postDelayed(this, intervalHeartBeat);//Delay For HB Minutes
                            }else {
                                pingHandlerHeartBeat.postDelayed(this, 300000);//Delay For HB Minutes
                            }
                        }
                    };
                    if (intervalHeartBeat!=0){
                        pingHandlerHeartBeat.postDelayed(pingRunnableHeartBeat, intervalHeartBeat);//Delay For HB Minutes
                    }else {
                        pingHandlerHeartBeat.postDelayed(pingRunnableHeartBeat, 300000);//Delay For HB Minutes
                    }
                    //pingHandlerHeartBeat.postDelayed(pingRunnableHeartBeat, 300000);//Delay For HB Minutes
                }
            }
        });
    }


    //---------------------To Show Progress Dialog_____________________________
    private void showProgressDialog() {
        if (progressBar!=null){
            progressBar.show();
        }
    }

    //---------------------To Dismiss Progress Dialog_____________________________
    private void dismissProgressDialog() {

        if (dialog!=null){
            dialog.dismiss();
        }


        if (progressBar!=null){
            progressBar.dismiss();
        }

    }

    //----------Method To Generate Dummy Transaction ID's For Every Start Request--------//
    private String generateTransactionId() {
        //87349779
        String SALTCHARS = "1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 8) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        return salt.toString();
    }

    //----------Method To Generate Session ID's For Every OCPP Request--------//
    private String generateSessionId() {
        String SALTCHARS = "abcdefghijklmnopqrstuvwxyz1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        return salt.toString();
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        cid=myData.toString();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();

        bluetooth.onStart();
        if (!bluetooth.isEnabled()){
            bluetooth.enable();
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        IntentFilter batteryfilter = new IntentFilter();
        batteryfilter.addAction(Intent.ACTION_POWER_CONNECTED);
        batteryfilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        registerReceiver(batteryReceiver,batteryfilter);
    }

    @Override
    public void onResume() {
        super.onResume();


        /*progressDialog = new SweetAlertDialog(OCPPWithOkHttpActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        progressDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        progressDialog.setTitleText("Validating...!");
        progressDialog.setContentText("Please Wait...");*/
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("Validating...!");
       /* progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setProgress(0);//initially progress is 0
        progressBar.setMax(100);*///sets the maximum value 100
        //progressBar.show();//displays the progress bar

       /* progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);*/

        /*informationDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        informationDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        informationDialog.setTitleText("Error!");
        informationDialog.setContentText("We are facing some error\nPlease try again!");*/
        /*informationDialog.setCancelable(false);
        informationDialog.setCanceledOnTouchOutside(false);*/


        sharedPreferenceIPAddress = new SharedPreferenceIPAddress();
        webSocketokhttp=openConnection();
        isSendBootNotification=true;

        bluetooth.connectToName("HC-05");
        isMainthreadend = true;
        //=================================================================================================
        i = 0;
        clearcachecount = 0;
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        //TODO your background code
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                if (i>4 ){
                                    if (threadPreviousCount==threadCurrentCount
                                            && Power.isConnected(OCPPWithOkHttpActivity.this)){

                                       /* if(sPisPluggedin.getisPluggedinCP1().equals("t")){
                                            sPisPoweFail.setisPowerFailCP1("t");
                                        }

                                        if(sPisPluggedin.getisPluggedinCP2().equals("t")){
                                            sPisPoweFail.setisPowerFailCP2("t");
                                        }

                                        if(sPisPluggedin.getisPluggedinCP3().equals("t")){
                                            sPisPoweFail.setisPowerFailCP3("t");
                                        }*/

                                        if (bluetooth.isConnected()){
                                            bluetooth.disconnect();
                                        }

                                        startActivity(new Intent(OCPPWithOkHttpActivity.this,
                                                InitiatingActivityOcpp.class));

                                        finish();
                                    } else{
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

        timer.scheduleAtFixedRate(task, delay,
                intevalPeriod);
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

        //  Firebase.setAndroidContext(this);
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
                            Intent i = new Intent(OCPPWithOkHttpActivity.this, Authentication.class);
                            String C_ID = cid.toString();
//Create the bundle
                            Bundle bundle = new Bundle();
//Add your data to bundle
                            bundle.putString("Cid", C_ID);
//Add the bundle to the intent
                            i.putExtras(bundle);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            if (bluetooth.isConnected()){
                                bluetooth.disconnect();
                            }
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

        sPlanguageCP1 = new SPlanguageCP1(OCPPWithOkHttpActivity.this);
        sPlanguageCP2 = new SPlanguageCP2(OCPPWithOkHttpActivity.this);
        sPlanguageCP3 = new SPlanguageCP3(OCPPWithOkHttpActivity.this);
        sharedPreference = new SharedPreferenceUnitR();

        sharedPreferenceIPAddress = new SharedPreferenceIPAddress();


        if (sharedPreference.getValue(OCPPWithOkHttpActivity.this)== null){
            sharedPreference.save(OCPPWithOkHttpActivity.this,"7");
        }

        erate_s = sharedPreference.getValue(OCPPWithOkHttpActivity.this);
        erate = Float.parseFloat(erate_s);

        langSet();

        sPisPluggedin = new SPisPluggedin(OCPPWithOkHttpActivity.this); // initiating sharedepreferences
        sPisPoweFail = new SPisPoweFail(OCPPWithOkHttpActivity.this); // initiating sharedepreferences
        sPmeterReadingCP1 = new SPmeterReadingCP1(OCPPWithOkHttpActivity.this); // initiating sharedepreferences
        sPmeterReadingCP2 = new SPmeterReadingCP2(OCPPWithOkHttpActivity.this); // initiating sharedepreferences
        sPmeterReadingCP3 = new SPmeterReadingCP3(OCPPWithOkHttpActivity.this); // initiating sharedepreferences
        sPTimeReadingCP1 = new SPTimeReadingCP1(OCPPWithOkHttpActivity.this); // initiating sharedepreferences
        spotpForCP1 = new SPOTPForCP1(OCPPWithOkHttpActivity.this); // initiating sharedepreferences
        sPTimeReadingCP2 = new SPTimeReadingCP2(OCPPWithOkHttpActivity.this); // initiating sharedepreferences
        sPTimeReadingCP3 = new SPTimeReadingCP3(OCPPWithOkHttpActivity.this); // initiating sharedepreferences
        sPmeterReadingCP1RO = new SPmeterReadingCP1RO(OCPPWithOkHttpActivity.this); // initiating sharedepreferences
        sPmeterReadingCP2RO = new SPmeterReadingCP2RO(OCPPWithOkHttpActivity.this); // initiating sharedepreferences
        sPmeterReadingCP3RO = new SPmeterReadingCP3RO(OCPPWithOkHttpActivity.this); // initiating sharedepreferences
        sPsavemoneyafterpfCP1 = new SPsavemoneyafterpfCP1(OCPPWithOkHttpActivity.this); // initiating sharedepreferences
        txt_maininstruction = findViewById(R.id.txt_maininstruction);
        txt_maininstruction.setSelected(true);
        //sharedPreTransactionId=getSharedPreferences("TRANSACTIONID",MODE_PRIVATE);

        if (sPsavemoneyafterpfCP1.getMeterReadingCP1RO().isEmpty()){
            sPsavemoneyafterpfCP1.setMeterReadingCP1RO("0");
        }

        if(sPisPluggedin.getisPluggedinCP1().isEmpty()){
            sPisPluggedin.setisPluggedinCP1("f");
        }

        if(sPisPoweFail.getisPowerFailCP1().isEmpty()){
            sPisPoweFail.setisPowerFailCP1("f");
        }

        if(sPisPoweFail.getSessionIdForCP1().isEmpty()){
            sPisPoweFail.setSessionIdForCP1("0");
        }
        //----------------------------
        if(sPisPluggedin.getisPluggedinCP2().isEmpty()){
            sPisPluggedin.setisPluggedinCP2("f");
        }

        if(sPisPoweFail.getisPowerFailCP2().isEmpty()){
            sPisPoweFail.setisPowerFailCP2("f");
        }

        //----------------------------
        if(sPisPluggedin.getisPluggedinCP3().isEmpty()){
            sPisPluggedin.setisPluggedinCP3("f");
        }

        if(sPisPoweFail.getisPowerFailCP3().isEmpty()){
            sPisPoweFail.setisPowerFailCP3("f");
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

        cardViewForNetwork   =     findViewById(R.id.cardViewForNetwork);
       /* cardViewForNetwork.setCardBackgroundColor(Color.parseColor(red));
        cardViewForNetwork.startAnimation(anim);*/

        toggleBtn   =     findViewById(R.id.toggleBtn);
        //kioskProgressDialog = new ProgressDialog(this);
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

        //----------------variables
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
       // layoutOTP = findViewById(R.id.layoutOTP);
        layout_detail.setVisibility(View.GONE);
        //layoutOTP.setVisibility(View.GONE);
        layout_pleasewait = findViewById(R.id.layout_pleasewait);
        txt_pleasewait = findViewById(R.id.txt_pleasewait);
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

        txt_pleasewait.setText("Connecting To Bluetooth Please Wait...!");

        //txt_pleasewait.setText("Connecting To Internet...!");
        layout_pleasewait.setVisibility(View.VISIBLE);

        layout_main.setVisibility(View.GONE);
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

        //-------------11-06-2018
        btn_tick1  = findViewById(R.id.btn_tick1);
        btn_tick2  = findViewById(R.id.btn_tick2);
        btn_tick3  = findViewById(R.id.btn_tick3);
        btn_tick4  = findViewById(R.id.btn_tick4);

        btn_tick1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sequence_match = "1";
                Log.e("tick","1");
            }
        });
        btn_tick2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("tick","2");
                if (sequence_match.equals("1")){
                    sequence_match = "2";
                }
            }
        });

        btn_tick3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("tick","3");
                if (sequence_match.equals("2")){
                    sequence_match = "3";
                }
            }
        });

        btn_tick4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("tick","4");
                if (sequence_match.equals("3")){
                    layoutback_fun();
                }
            }
        });

        rg_language_m.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch(checkedId) {
                    case R.id.rb_en_m:
                        // TODO Something
                        switch (detail_flag) {
                            case "d_CP1":
                                sPlanguageCP1.setlanguageCP1("en");
                                break;
                            case "d_CP2":
                                sPlanguageCP2.setlanguageCP2("en");

                                break;
                            case "d_CP3":
                                sPlanguageCP3.setlanguageCP3("en");

                                break;
                        }

                        break;
                    case R.id.rb_hi_m:
                        // TODO Something
                        switch (detail_flag) {
                            case "d_CP1":
                                sPlanguageCP1.setlanguageCP1("hi");
                                break;
                            case "d_CP2":
                                sPlanguageCP2.setlanguageCP2("hi");

                                break;
                            case "d_CP3":
                                sPlanguageCP3.setlanguageCP3("hi");

                                break;
                        }
                        break;
                    case R.id.rb_ma_m:
                        // TODO Something
                        switch (detail_flag) {
                            case "d_CP1":
                                sPlanguageCP1.setlanguageCP1("ma");
                                break;
                            case "d_CP2":
                                sPlanguageCP2.setlanguageCP2("ma");
                                break;
                            case "d_CP3":
                                sPlanguageCP3.setlanguageCP3("ma");
                                break;
                        }
                        break;
                }
            }
        });

        relativeLayout1st.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if (dbManager.fetchConnectorAvailability("1").equals("Operative")){
                    isTappedC1 = true;
                    isPleaseWaitC1 = true;
                    showOTPWindow("1","To Start");
                }else {
                    showToast(" This Connector Is Currently Unavailable! ");
                }
            }
        });


        relativeLayout2nd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dbManager.fetchConnectorAvailability("2").equals("Operative")){
                    isTappedC2 = true;
                    isPleaseWaitC2 = true;
                    showOTPWindow("2","To Start");
                }else {
                    showToast(" This Connector Is Currently Unavailable! ");
                }
            }
        });


        relativeLayout3rd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dbManager.fetchConnectorAvailability("3").equals("Operative")){
                    isTappedC3 = true;
                    isPleaseWaitC3 = true;
                    showOTPWindow("3","To Start");
                }else {
                    showToast(" This Connector Is Currently Unavailable! ");
                }
            }
        });

        imageView_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_main.setVisibility(View.VISIBLE);
                layout_detail.setVisibility(View.GONE);
                detail_flag = "c";
                isClickedCancel=true;

                rsCP21="₹ 00.00";
                rsCP1="₹ 00.00";
                unit_detailCP21="00:00";
                unit_detailCP1="00:00";
                etime_detailCP21="00:00";
                etime_detailCP1="00:00";

                rsCP22="₹ 00.00";
                rsCP2="₹ 00.00";
                unit_detailCP22="00:00";
                unit_detailCP2="00:00";
                etime_detailCP22="00:00";
                etime_detailCP2="00:00";

                rsCP23="₹ 00.00";
                rsCP3="₹ 00.00";
                unit_detailCP23="00:00";
                unit_detailCP3="00:00";
                etime_detailCP23="00:00";
                etime_detailCP3="00:00";
                flagResetVals=true;
            }
        });

        connect1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {}
        });
        connect2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {}
        });
        connect3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {}
        });

        layout_goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutback_fun();
            }
        });

        final String s = "#234990.00+~";
        anim = new AlphaAnimation(0.1f, 1.0f);
        anim.setDuration(1000);
        anim.setRepeatMode(Animation.RESTART);
        anim.setRepeatCount(Animation.INFINITE);
        anim2 = new AlphaAnimation(0.1f, 1.0f);
        anim2.setDuration(1000);
        anim2.setRepeatMode(Animation.RESTART);
        anim2.setRepeatCount(Animation.INFINITE);
        anim3 = new AlphaAnimation(0.1f, 1.0f);
        anim3.setDuration(1000);
        anim3.setRepeatMode(Animation.RESTART);
        anim3.setRepeatCount(Animation.INFINITE);

        setToggleBtn();

        toggleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reasonC="Local";
                if (isAlreadyonm) {
                    if (_CMSCommand.equals("RemoteStopTransaction")){
                        reasonC="Remote";
                        funConnectori();
                    }else if (sPisPoweFail.getisPowerFailCP1().equals("t")){
                        reasonC="PowerLoss";
                        funConnectori();
                    }else {
                        showOTPWindow("1","To Stop");
                    }
                }else {
                    sPisPoweFail.setisPowerFailCP1("f");
                   /* StringBuilder newCommandp1 = new StringBuilder(command);
                    newCommandp1.setCharAt(1, '0');
                    command = ""+newCommandp1;*/
                    showOTPWindow("1","To Stop");
                    toggleBtn.setCardBackgroundColor(Color.parseColor(green));
                    toggleValue.setText("CP1 "+s22_CP1);
                    toggleBtn.clearAnimation();
                }
            }
        });


        btn_p2onff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reasonC="Local";
                if (isAlreadyonm) {
                    if (_CMSCommand.equals("RemoteStopTransaction")){
                        reasonC="Remote";
                        funConnectorii();
                    }else if (sPisPoweFail.getisPowerFailCP2().equals("t")){
                        reasonC="PowerLoss";
                        funConnectorii();
                    }else {
                        showOTPWindow("2","To Stop");
                    }
                }else {
                    sPisPoweFail.setisPowerFailCP2("f");
                    showOTPWindow("2","To Stop");
                    btn_p2onff.setCardBackgroundColor(Color.parseColor(green));
                    txt_p2btnValue.setText("CP2 "+s22_CP2);
                    txt_p2btnValue.clearAnimation();
                }
            }
        });

        btn_p3onff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reasonC="Local";
                if (isAlreadyonm) {
                    if (_CMSCommand.equals("RemoteStopTransaction")){
                        reasonC="Remote";
                        funConnectoriii();
                    }else if (sPisPoweFail.getisPowerFailCP3().equals("t")){
                        reasonC="PowerLoss";
                        funConnectoriii();
                    }else {
                        showOTPWindow("3","To Stop");
                    }
                }else {
                    sPisPoweFail.setisPowerFailCP3("f");
                    /*StringBuilder newCommandp3 = new StringBuilder(command);
                    newCommandp3.setCharAt(1, '0');
                    command = ""+newCommandp3;*/
                    showOTPWindow("3","To Stop");
                    btn_p3onff.setCardBackgroundColor(Color.parseColor(green));
                    txt_p3btnValue.setText("CP3 "+s22_CP3);
                    txt_p3btnValue.clearAnimation();
                }
            }
        });

        //===========================================================================
        bluetooth.setBluetoothCallback(new BluetoothCallback() {
            @Override
            public void onBluetoothTurningOn(){}
            @Override
            public void onBluetoothOn(){}
            @Override
            public void onBluetoothTurningOff(){}
            @Override
            public void onBluetoothOff(){}
            @Override
            public void onUserDeniedActivation(){}
        });

        List<BluetoothDevice> devices = bluetooth.getPairedDevices();
        Log.e("BluetoothDeviceList","BluetoothDeviceLISt"+devices);
        bluetooth.setDiscoveryCallback(new DiscoveryCallback() {
            @Override
            public void onDiscoveryStarted() {Log.e("onDiscoveryStarted","onDiscoveryStarted");}
            @Override
            public void onDiscoveryFinished() {Log.e("onDiscoveryFinished","onDiscoveryFinished");}
            @Override
            public void onDeviceFound(BluetoothDevice device) {Log.e("onDeviceFound","onDeviceFound"+device);}
            @Override
            public void onDevicePaired(BluetoothDevice device) {Log.e("onDevicePaired","onDevicePaired"+device);}
            @Override
            public void onDeviceUnpaired(BluetoothDevice device) {Log.e("onDeviceUnpaired","onDeviceUnpaired"+device);}
            @Override
            public void onError(String message) {Log.e("setDiscoveryCallback","onError"+message); }
        });

        bluetooth.setCallbackOnUI(new OCPPWithOkHttpActivity());
        bluetooth.setDeviceCallback(new DeviceCallback() {
            @Override
            public void onDeviceConnected(BluetoothDevice device) {
                Log.e("setDeviceCallback","onDeviceConnected"+device);
               /* if (layout_pleasewait!=null
                        && layout_main!=null){
                    layout_main.setVisibility(View.VISIBLE);
                    layout_pleasewait.setVisibility(View.GONE);
                }*/
            }

            @Override
            public void onDeviceDisconnected(BluetoothDevice device, String message) {
                Log.e("setDeviceCallback","onDeviceDisconnected"+device+", message :"+message);
                /*if (layout_pleasewait!=null
                && layout_main!=null){
                    layout_main.setVisibility(View.GONE);
                    layout_pleasewait.setVisibility(View.VISIBLE);
                }*/
            }

            @Override
            public void onMessage(String message) {
               // Log.e("BTONMESSAGECALL","MessageIs-> "+message);
                countMainCalls++;
                //checkConnection();
                if (isMainthreadend){
                    if (countMainCalls==1){
                        countMainCalls=0;
                        main_operation(message);
                    }
                }
            }

            @Override
            public void onError(String message) {
                Log.e("setDeviceCallback","onError"+message);}

            @Override
            public void onConnectError(BluetoothDevice device, String message) {
                Log.e("setDeviceCallback","onConnectError"+device+", message"+message);
                //bluetooth.disconnect();
            }
        });
    }

    private void showToast(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast_layout,
                (ViewGroup) findViewById(R.id.toast_layout_root));
        TextView toastTextView=layout.findViewById(R.id.toastTextView);
        toastTextView.setText(message);
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    private void display_details(final String rate_detail, final String charge_point_id,final String voltage_detail,final String current_detail,final String power_detail,final String unit_detail,final String etime_detail,final String status_detail,final String etime_displayf, final String unit_display, final String rate_display, final String lang){
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

        switch (lang) {
            case "en":
                rg_language_m.check(R.id.rb_en_m);
                break;
            case "ma":
                rg_language_m.check(R.id.rb_ma_m);
                break;
            case "hi":
                rg_language_m.check(R.id.rb_hi_m);
                break;
        }
    }

    public void funConnectori(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                {
                    if (isStillOnCP1){
                        flag = false;
                        setToggleBtn();
                        sPisPluggedin.setisPluggedinCP1("f");
                        sPisPoweFail.setisPowerFailCP1("f");
                    } else {
                        if (flag) {
                            StringBuilder newCommandp1 = new StringBuilder(command);
                            newCommandp1.setCharAt(1, '0');
                            Log.e("newCommandp1", "" + newCommandp1);
                            bluetooth.send("" + newCommandp1);
                            bluetooth.send("" + newCommandp1);
                        } else {
                            StringBuilder newCommandp1 = new StringBuilder(command);
                            newCommandp1.setCharAt(1, '1');
                            Log.e("newCommandp1", "" + newCommandp1);
                            bluetooth.send("" + newCommandp1);
                            bluetooth.send("" + newCommandp1);
                        }
                        setToggleBtn();
                    }
                }
            }
        });
    }
    public void funConnectorii(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                {
                    if (isStillOnCP2){
                        p2flag = false;
                        setP2Btn();
                        sPisPluggedin.setisPluggedinCP2("f");
                        sPisPoweFail.setisPowerFailCP2("f");
                    } else {
                        if (p2flag) {
                            StringBuilder newCommandp2 = new StringBuilder(command);
                            newCommandp2.setCharAt(2, '0');
                            Log.e("newCommandp2", "" + newCommandp2);
                            bluetooth.send("" + newCommandp2);
                            bluetooth.send("" + newCommandp2);
                        } else {
                            StringBuilder newCommandp2 = new StringBuilder(command);
                            newCommandp2.setCharAt(2, '1');
                            Log.e("newCommandp2", "" + newCommandp2);
                            bluetooth.send("" + newCommandp2);
                            bluetooth.send("" + newCommandp2);
                        }
                        setP2Btn();
                    }
                }
            }
        });
    }
    public void funConnectoriii(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                {
                    if (isStillOnCP3){
                        p3flag = false;
                        setP3Btn();
                        sPisPluggedin.setisPluggedinCP3("f");
                        sPisPoweFail.setisPowerFailCP3("f");
                    } else {
                        if (p3flag) {
                            StringBuilder newCommandp3 = new StringBuilder(command);
                            newCommandp3.setCharAt(3, '0');
                            Log.e("newCommandp3", "" + newCommandp3);
                            bluetooth.send("" + newCommandp3);
                            bluetooth.send("" + newCommandp3);
                        } else {
                            StringBuilder newCommandp3 = new StringBuilder(command);
                            newCommandp3.setCharAt(3, '1');
                            Log.e("newCommandp3", "" + newCommandp3);
                            bluetooth.send("" + newCommandp3);
                            bluetooth.send("" + newCommandp3);
                        }

                        setP3Btn();
                    }
                }
            }
        });
    }

    @Override
    protected void onStop() {
        timer.cancel();
        bluetooth.onStop();
        unregisterReceiver(batteryReceiver);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //isSendBootNotification=true;
        if (pingHandlerHeartBeat != null) {
            pingHandlerHeartBeat.removeCallbacks(pingRunnableHeartBeat);
            pingHandlerHeartBeat.removeCallbacksAndMessages(null);
        }

        if (pingHandlerMVC1 != null) {
            pingHandlerMVC1.removeCallbacks(pingRunnableC1);
            pingHandlerMVC1.removeCallbacksAndMessages(null);
        }

        if (pingHandlerMVC2 != null) {
            pingHandlerMVC2.removeCallbacks(pingRunnableC2);
            pingHandlerMVC2.removeCallbacksAndMessages(null);
        }

        if (pingHandlerMVC3 != null) {
            pingHandlerMVC3.removeCallbacks(pingRunnableC3);
            pingHandlerMVC3.removeCallbacksAndMessages(null);
        }
    }
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {}
            else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {}
            else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {}
            else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                tv_status1.setText(s6_CP1);
                String s = globalP1meter;
                float v = Float.parseFloat(s);
                int b=(int) v;
                sPsavemoneyafterpfCP1.setMeterReadingCP1RO(mrsPOCP1);
                toggleBtn.setCardBackgroundColor(Color.parseColor(red));
                toggleBtn.startAnimation(anim);

                tv_status2.setText(s6_CP2);
                String s2 = globalP2meter;
                float v2 = Float.parseFloat(s2);
                int b2=(int) v2;
                btn_p2onff.setCardBackgroundColor(Color.parseColor(red));
                btn_p2onff.startAnimation(anim);
                tv_status3.setText(s6_CP3);
                String s3 = globalP3meter;
                float v3 = Float.parseFloat(s3);
                int b3=(int) v3;
                btn_p3onff.setCardBackgroundColor(Color.parseColor(red));
                btn_p3onff.startAnimation(anim);

                if(sPisPluggedin.getisPluggedinCP1().equals("t")){
                    sPisPoweFail.setisPowerFailCP1("f");
                    sPTimeReadingCP1.setTimeReadingCP1(MillisCP1);
                    // isSavedTimeC1 = true;
                }

                if(sPisPluggedin.getisPluggedinCP2().equals("t")){
                    sPisPoweFail.setisPowerFailCP2("f");
                    sPTimeReadingCP2.setTimeReadingCP2(MillisCP2);
                    // isSavedTimeC2 = true;
                }

                if(sPisPluggedin.getisPluggedinCP3().equals("t")){
                    sPisPoweFail.setisPowerFailCP3("f");
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
                if (!isAlreadyonm){
                    if (bluetooth.isConnected()){
                        bluetooth.disconnect();
                    }
                    startActivity(new Intent(OCPPWithOkHttpActivity.this,InitiatingActivityOcpp.class));
                    finish();
                }
            } else {
                intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED);
                isAlreadyonm = false;
                tv_status1.setText(s6_CP1);
                sendAvailStatusForC1=true;
                sendAvailStatusForC2=true;
                sendAvailStatusForC3=true;
                /*isStopSendC1=true;
                isStopSendC2=true;
                isStopSendC3=true;*/
                if (txt_pleasewait!=null){
                    txt_pleasewait.setText("Fault Power Failure...!");
                }
                sendStatusNotificationRequest("0","PowerLoss",
                        "Unavailable",sessionNumberC1);
                sendStatusNotificationRequest("1","PowerLoss",
                        "Unavailable",sessionNumberC1);
                sendStatusNotificationRequest("2","PowerLoss",
                        "Unavailable",sessionNumberC2);
                sendStatusNotificationRequest("3","PowerLoss",
                        "Unavailable",sessionNumberC3);

                toggleBtn.setEnabled(false);
                btn_p2onff.setEnabled(false);
                btn_p3onff.setEnabled(false);
                relativeLayout1st.setEnabled(false);
                relativeLayout2nd.setEnabled(false);
                relativeLayout3rd.setEnabled(false);
                stopSendingMeterVals("All");
                String s = globalP1meter;
                float v = Float.parseFloat(s);
                int b=(int) v;
                sPsavemoneyafterpfCP1.setMeterReadingCP1RO(mrsPOCP1);
                toggleBtn.setCardBackgroundColor(Color.parseColor(red));
                toggleBtn.startAnimation(anim);
                tv_status2.setText(s6_CP2);
                String s2 = globalP2meter;
                float v2 = Float.parseFloat(s2);
                btn_p2onff.setCardBackgroundColor(Color.parseColor(red));
                btn_p2onff.startAnimation(anim);
                tv_status3.setText(s6_CP3);
                String s3 = globalP3meter;
                float v3 = Float.parseFloat(s3);
                int b3=(int) v3;
                btn_p3onff.setCardBackgroundColor(Color.parseColor(red));
                btn_p3onff.startAnimation(anim);

                if(sPisPluggedin.getisPluggedinCP1().equals("t")){
                    sPisPoweFail.setisPowerFailCP1("f");
                    sPTimeReadingCP1.setTimeReadingCP1(MillisCP1);
                    // isSavedTimeC1 = true;
                }
                if(sPisPluggedin.getisPluggedinCP2().equals("t")){
                    sPisPoweFail.setisPowerFailCP2("f");
                    sPTimeReadingCP2.setTimeReadingCP2(MillisCP2);
                    // isSavedTimeC2 = true;
                }
                if(sPisPluggedin.getisPluggedinCP3().equals("t")){
                    sPisPoweFail.setisPowerFailCP3("f");
                    sPTimeReadingCP3.setTimeReadingCP3(MillisCP3);
                    //  isSavedTimeC3 = true;
                }
            }
        }
    };

    private void setToggleBtn() {
        if(flag) {
            StringBuilder newCommandp3 = new StringBuilder(command);
            newCommandp3.setCharAt(1, '1');
            command = ""+newCommandp3;
            toggleBtn.setCardBackgroundColor(Color.parseColor(green));
            toggleValue.setText("C1\n"+s23_CP1);
        } else {
            StringBuilder newCommandp3 = new StringBuilder(command);
            newCommandp3.setCharAt(1, '0');
            command = ""+newCommandp3;
            toggleBtn.setCardBackgroundColor(Color.parseColor(green));
            toggleValue.setText("CP1 "+s22_CP1);
            toggleBtn.clearAnimation();
        }
    }


    private void setP2Btn() {
        if(p2flag) {
            StringBuilder newCommandp3 = new StringBuilder(command);
            newCommandp3.setCharAt(2, '1');
            command = ""+newCommandp3;
            btn_p2onff.setCardBackgroundColor(Color.parseColor(green));
            txt_p2btnValue.setText("C2\n"+s23_CP2);
        } else {
            StringBuilder newCommandp3 = new StringBuilder(command);
            newCommandp3.setCharAt(2, '0');
            command = ""+newCommandp3;
            btn_p2onff.setCardBackgroundColor(Color.parseColor(green));
            txt_p2btnValue.setText("CP2 "+s22_CP2);
            txt_p2btnValue.clearAnimation();
        }
    }
    private void setP3Btn() {
        if(p3flag) {
            StringBuilder newCommandp3 = new StringBuilder(command);
            newCommandp3.setCharAt(3, '1');
            command = ""+newCommandp3;
            btn_p3onff.setCardBackgroundColor(Color.parseColor(green));
            txt_p3btnValue.setText("C3\n"+s23_CP3);
        } else {
            StringBuilder newCommandp3 = new StringBuilder(command);
            newCommandp3.setCharAt(3, '0');
            command = ""+newCommandp3;
            btn_p3onff.setCardBackgroundColor(Color.parseColor(green));
            txt_p3btnValue.setText("CP3 "+s22_CP3);
            txt_p3btnValue.clearAnimation();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        timer.cancel();
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(!hasFocus) {
            Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            sendBroadcast(closeDialog);
        }
    }

    public void langSet(){
        switch (sPlanguageCP1.getlanguageCP1()) {
            case "en":
                s1_CP1 = LangString.s1_E;
                s2_CP1 = LangString.s2_E;
                s3_CP1 = LangString.s3_E;
                s4_CP1 = LangString.s4_E;
                s5_CP1 = LangString.s5_E;
                s6_CP1 = LangString.s6_E;
                s7_CP1 = LangString.s7_E;
                s8_CP1 = LangString.s8_E;
                s9_CP1 = LangString.s9_E;
                s10_CP1 = LangString.s10_E;
                s11_CP1 = LangString.s11_E;
                s12_CP1 = LangString.s12_E;
                s13_CP1 = LangString.s13_E;
                s14_CP1 = LangString.s14_E;
                s15_CP1 = LangString.s15_E;
                s16_CP1 = LangString.s16_E;
                s17_CP1 = LangString.s17_E;
                s21_CP1 = LangString.s21_E;
                s22_CP1 = LangString.s22_E;
                s23_CP1 = LangString.s23_E;
                s24_CP1 = LangString.s24_E;
                break;
            case "ma":
                s1_CP1 = LangString.s1_M;
                s2_CP1 = LangString.s2_M;
                s3_CP1 = LangString.s3_M;
                s4_CP1 = LangString.s4_M;
                s5_CP1 = LangString.s5_M;
                s6_CP1 = LangString.s6_M;
                s7_CP1 = LangString.s7_M;
                s8_CP1 = LangString.s8_M;
                s9_CP1 = LangString.s9_M;
                s10_CP1 = LangString.s10_M;
                s11_CP1 = LangString.s11_M;
                s12_CP1 = LangString.s12_M;
                s13_CP1 = LangString.s13_M;
                s14_CP1 = LangString.s14_M;
                s15_CP1 = LangString.s15_M;
                s16_CP1 = LangString.s16_M;
                s17_CP1 = LangString.s17_M;
                s21_CP1 = LangString.s21_M;
                s22_CP1 = LangString.s22_M;
                s23_CP1 = LangString.s23_M;
                s24_CP1 = LangString.s24_M;
                break;
            case "hi":
                s1_CP1 = LangString.s1_H;
                s2_CP1 = LangString.s2_H;
                s3_CP1 = LangString.s3_H;
                s4_CP1 = LangString.s4_H;
                s5_CP1 = LangString.s5_H;
                s6_CP1 = LangString.s6_H;
                s7_CP1 = LangString.s7_H;
                s8_CP1 = LangString.s8_H;
                s9_CP1 = LangString.s9_H;
                s10_CP1 = LangString.s10_H;
                s11_CP1 = LangString.s11_H;
                s12_CP1 = LangString.s12_H;
                s13_CP1 = LangString.s13_H;
                s14_CP1 = LangString.s14_H;
                s15_CP1 = LangString.s15_H;
                s16_CP1 = LangString.s16_H;
                s17_CP1 = LangString.s17_H;
                s21_CP1 = LangString.s21_H;
                s22_CP1 = LangString.s22_H;
                s23_CP1 = LangString.s23_H;
                s24_CP1 = LangString.s24_H;
                break;
        }


        switch (sPlanguageCP2.getlanguageCP2()) {
            case "en":
                s1_CP2 = LangString.s1_E;
                s2_CP2 = LangString.s2_E;
                s3_CP2 = LangString.s3_E;
                s4_CP2 = LangString.s4_E;
                s5_CP2 = LangString.s5_E;
                s6_CP2 = LangString.s6_E;
                s7_CP2 = LangString.s7_E;
                s8_CP2 = LangString.s8_E;
                s9_CP2 = LangString.s9_E;
                s10_CP2 = LangString.s10_E;
                s11_CP2 = LangString.s11_E;
                s12_CP2 = LangString.s12_E;
                s13_CP2 = LangString.s13_E;
                s14_CP2 = LangString.s14_E;
                s15_CP2 = LangString.s15_E;
                s16_CP2 = LangString.s16_E;
                s17_CP2 = LangString.s17_E;
                s21_CP2 = LangString.s21_E;
                s22_CP2 = LangString.s22_E;
                s23_CP2 = LangString.s23_E;
                s24_CP2 = LangString.s24_E;

                break;
            case "ma":
                s1_CP2 = LangString.s1_M;
                s2_CP2 = LangString.s2_M;
                s3_CP2 = LangString.s3_M;
                s4_CP2 = LangString.s4_M;
                s5_CP2 = LangString.s5_M;
                s6_CP2 = LangString.s6_M;
                s7_CP2 = LangString.s7_M;
                s8_CP2 = LangString.s8_M;
                s9_CP2 = LangString.s9_M;
                s10_CP2 = LangString.s10_M;
                s11_CP2 = LangString.s11_M;
                s12_CP2 = LangString.s12_M;
                s13_CP2 = LangString.s13_M;
                s14_CP2 = LangString.s14_M;
                s15_CP2 = LangString.s15_M;
                s16_CP2 = LangString.s16_M;
                s17_CP2 = LangString.s17_M;
                s21_CP2 = LangString.s21_M;
                s22_CP2 = LangString.s22_M;
                s23_CP2 = LangString.s23_M;
                s24_CP2 = LangString.s24_M;

                break;
            case "hi":
                s1_CP2 = LangString.s1_H;
                s2_CP2 = LangString.s2_H;
                s3_CP2 = LangString.s3_H;
                s4_CP2 = LangString.s4_H;
                s5_CP2 = LangString.s5_H;
                s6_CP2 = LangString.s6_H;
                s7_CP2 = LangString.s7_H;
                s8_CP2 = LangString.s8_H;
                s9_CP2 = LangString.s9_H;
                s10_CP2 = LangString.s10_H;
                s11_CP2 = LangString.s11_H;
                s12_CP2 = LangString.s12_H;
                s13_CP2 = LangString.s13_H;
                s14_CP2 = LangString.s14_H;
                s15_CP2 = LangString.s15_H;
                s16_CP2 = LangString.s16_H;
                s17_CP2 = LangString.s17_H;
                s21_CP2 = LangString.s21_H;
                s22_CP2 = LangString.s22_H;
                s23_CP2 = LangString.s23_H;
                s24_CP2 = LangString.s24_H;

                break;
        }


        switch (sPlanguageCP3.getlanguageCP3()) {
            case "en":
                s1_CP3 = LangString.s1_E;
                s2_CP3 = LangString.s2_E;
                s3_CP3 = LangString.s3_E;
                s4_CP3 = LangString.s4_E;
                s5_CP3 = LangString.s5_E;
                s6_CP3 = LangString.s6_E;
                s7_CP3 = LangString.s7_E;
                s8_CP3 = LangString.s8_E;
                s9_CP3 = LangString.s9_E;
                s10_CP3 = LangString.s10_E;
                s11_CP3 = LangString.s11_E;
                s12_CP3 = LangString.s12_E;
                s13_CP3 = LangString.s13_E;
                s14_CP3 = LangString.s14_E;
                s15_CP3 = LangString.s15_E;
                s16_CP3 = LangString.s16_E;
                s17_CP3 = LangString.s17_E;
                s21_CP3 = LangString.s21_E;
                s22_CP3 = LangString.s22_E;
                s23_CP3 = LangString.s23_E;
                s24_CP3 = LangString.s24_E;
                break;
            case "ma":
                s1_CP3 = LangString.s1_M;
                s2_CP3 = LangString.s2_M;
                s3_CP3 = LangString.s3_M;
                s4_CP3 = LangString.s4_M;
                s5_CP3 = LangString.s5_M;
                s6_CP3 = LangString.s6_M;
                s7_CP3 = LangString.s7_M;
                s8_CP3 = LangString.s8_M;
                s9_CP3 = LangString.s9_M;
                s10_CP3 = LangString.s10_M;
                s11_CP3 = LangString.s11_M;
                s12_CP3 = LangString.s12_M;
                s13_CP3 = LangString.s13_M;
                s14_CP3 = LangString.s14_M;
                s15_CP3 = LangString.s15_M;
                s16_CP3 = LangString.s16_M;
                s17_CP3 = LangString.s17_M;
                s21_CP3 = LangString.s21_M;
                s22_CP3 = LangString.s22_M;
                s23_CP3 = LangString.s23_M;
                s24_CP3 = LangString.s24_M;
                break;
            case "hi":
                s1_CP3 = LangString.s1_H;
                s2_CP3 = LangString.s2_H;
                s3_CP3 = LangString.s3_H;
                s4_CP3 = LangString.s4_H;
                s5_CP3 = LangString.s5_H;
                s6_CP3 = LangString.s6_H;
                s7_CP3 = LangString.s7_H;
                s8_CP3 = LangString.s8_H;
                s9_CP3 = LangString.s9_H;
                s10_CP3 = LangString.s10_H;
                s11_CP3 = LangString.s11_H;
                s12_CP3 = LangString.s12_H;
                s13_CP3 = LangString.s13_H;
                s14_CP3 = LangString.s14_H;
                s15_CP3 = LangString.s15_H;
                s16_CP3 = LangString.s16_H;
                s17_CP3 = LangString.s17_H;
                s21_CP3 = LangString.s21_H;
                s22_CP3 = LangString.s22_H;
                s23_CP3 = LangString.s23_H;
                s24_CP3 = LangString.s24_H;
                break;
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
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
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @SuppressLint("SetTextI18n")
    public void main_operation(String recDataString ){
        //Log.e("isMainthreadend","MIDDLE "+isMainthreadend);

        isMainthreadend = false;
        isAlreadyonm = true;

        if (Power.isConnected(OCPPWithOkHttpActivity.this)) {
            sendPowerFailStatus=true;
            int startOfLineIndex = recDataString.indexOf("#");
            int endOfLineIndex = recDataString.indexOf("~");

            if(endOfLineIndex > 0){
                langSet();
                if (isTappedC1) {
                    txt_touch1.setText("C1\n" + s24_CP1);
                } else {
                    txt_touch1.setText("C1\n" + s21_CP1);
                }

                if (isTappedC2) {
                    txt_touch2.setText("C2\n" + s24_CP2);
                } else {
                    txt_touch2.setText("C2\n" + s21_CP2);
                }

                if (isTappedC3) {
                    txt_touch3.setText("C3\n" + s24_CP3);
                } else {
                    txt_touch3.setText("C3\n" + s21_CP3);
                }

                txt_etime_display1.setText(s14_CP1);
                txt_etime_display2.setText(s14_CP2);
                txt_etime_display3.setText(s14_CP3);

                if (recDataString.charAt(0) == '#'){
                    //Log.e("isMainthreadend","D");
                    threadCurrentCount++;
                    String dataInPrint = recDataString.substring(0, endOfLineIndex);    // extract string

                    int dataLength = dataInPrint.length();
                    if (dataLength < 90) {
                        try {
                            /*if (layout_pleasewait.getVisibility()==View.VISIBLE){*/
                                layout_pleasewait.setVisibility(View.GONE);
                                layout_main.setVisibility(View.VISIBLE);
/*
                                if (isSendBootNotification){
                                    isSendBootNotification=false;
                                    new InternetCheck(new InternetCheck.Consumer() {
                                        @Override
                                        public void accept(Boolean internet) {
                                            Log.e("INSERTINTODB"," MAINTHREAD--> TOCHECKINTERNET");
                                            if (internet){
                                                webSocketokhttp=openConnection();
                                                webSocketokhttp.send(JSONObjectsForRequest.sendBootNotificationRequest(generateSessionId()));
                                                webSocketokhttp.send(JSONObjectsForRequest.sendStatusNotificationRequest("0",
                                                        "NoError","Available",getUTC(), generateSessionId()));
                                            }else {
                                                dbManager.insert(JSONObjectsForRequest.sendBootNotificationRequest(generateSessionId()),"BOOT","True","0");
                                                dbManager.insert(JSONObjectsForRequest.sendStatusNotificationRequest("0",
                                                        "NoError","Available",getUTC(), generateSessionId()),"STATUS","True","0");
                                            }
                                            isSendBootNotification=false;
                                        }
                                    });
                                }
*/

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
                            float p1MeterValue=(Float.parseFloat(p1meter)*1000);
                            globalP1meter = p1meter;
                            String p2OnOff = recDataString.substring(recDataString.indexOf("e") + 1, recDataString.indexOf("e") + 2);
                            String p2plugin = recDataString.substring(recDataString.indexOf("e") + 2, recDataString.indexOf("e") + 3);
                            String p2fault = recDataString.substring(recDataString.indexOf("e") + 3, recDataString.indexOf("f"));
                            String p2voltage = recDataString.substring(p2startV, p2endV);
                            String p2current = recDataString.substring(p2startC, p2endC);
                            String p2meter = recDataString.substring(p2startM, p2endM);
                            float p2MeterValue=(Float.parseFloat(p2meter)*1000);
                            globalP2meter = p2meter;
                            String p3OnOff = recDataString.substring(recDataString.indexOf("k") + 1, recDataString.indexOf("k") + 2);
                            String p3plugin = recDataString.substring(recDataString.indexOf("k") + 2, recDataString.indexOf("k") + 3);
                            String p3fault = recDataString.substring(recDataString.indexOf("k") + 3, recDataString.indexOf("l"));
                            String p3voltage = recDataString.substring(p3startV, p3endV);
                            String p3current = recDataString.substring(p3startC, p3endC);
                            String p3meter = recDataString.substring(p3startM, p3endM);
                            float p3MeterValue=(Float.parseFloat(p3meter)*1000);
                            globalP3meter = p3meter;
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

                            if (isComefromPFUVOV && p1fault.equals("99")) {
                                is99Comming = false;
                                counterPFUVOV++;
                                if (counterPFUVOV > 12) {
                                    isComefromPFUVOV = false;
                                    is99Comming = true;
                                    counterPFUVOV = 0;
                                }
                            }

                            //----------------------------------
                            if (is99Comming) {
                                if (p1OnOff.equals("0")) {
                                    if (sPisPoweFail.getisPowerFailCP1().equals("t")
                                            && sPisPluggedin.getisPluggedinCP1().equals("t")) {
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

                                if (emergency.equals("1")) {

                                    if (sendEmergencyStatus){
                                        reasonC="EmergencyStop";
                                        webSocketokhttp=openConnection();
                                        sendEmergencyStatus=false;
                                        isSendEmergency=true;
                                        sendAvailStatusForC1=true;
                                        sendAvailStatusForC2=true;
                                        sendAvailStatusForC3=true;

                                        sendStatusNotificationRequest("1",
                                                "EmergencyStop","Unavailable",sessionNumberC1);
                                        sendStatusNotificationRequest("2",
                                                "EmergencyStop","Unavailable",sessionNumberC2);
                                        sendStatusNotificationRequest("3",
                                                "EmergencyStop","Unavailable",sessionNumberC3);

                                        toggleBtn.setEnabled(false);
                                        btn_p2onff.setEnabled(false);
                                        btn_p3onff.setEnabled(false);

                                        relativeLayout1st.setEnabled(false);
                                        relativeLayout2nd.setEnabled(false);
                                        relativeLayout3rd.setEnabled(false);
                                        stopSendingMeterVals("All");
                                        //webSocketokhttp=openConnection();
                                    }

                                    txt_maininstruction.setText("Connectors not available");
                                    emergency01="1";
                                    emergency02="1";
                                    emergency03="1";
                                    sendPowerFailStatus=true;
                                    tv_status1.setText(s2_CP1);
                                    tv_status2.setText(s2_CP2);
                                    tv_status3.setText(s2_CP3);
                                    status_detailCP1 = s2_CP1;
                                    status_detailCP2 = s2_CP2;
                                    status_detailCP3 = s2_CP3;
                                    flagEmergencyStopped=true;
                                    sendEmergencyStatus=false;

                                    rsCP21="₹ 00.00";
                                    unit_detailCP21="00:00";
                                    etime_detailCP21="00:00";
                                    layout_main.setVisibility(View.VISIBLE);
                                    layout_detail.setVisibility(View.GONE);
                                    detail_flag = "c";
                                } else {
                                    //---------------------------------------------------------------------------------------------------------
                                    if (p1fault.equals("03")){
                                        overCurrent01="03";
                                        if (sendOverCurrentStatusC1){
                                            reasonC="Other";
                                            sendAvailStatusForC1=true;
                                            sendStatusNotificationRequest("1","OverCurrentFailure",
                                                    "Fault",generateSessionId());
                                            sendOverCurrentStatusC1 =false;
                                            stopSendingMeterVals("1");
                                        }
                                    }
                                    if (p2fault.equals("03")){
                                        overCurrent02="03";
                                        if (sendOverCurrentStatusC2){
                                            reasonC="Other";
                                            sendAvailStatusForC2=true;
                                            sendStatusNotificationRequest("2","OverCurrentFailure",
                                                    "Fault",sessionNumberC2);
                                            sendOverCurrentStatusC2 =false;
                                            stopSendingMeterVals("2");
                                        }
                                    }
                                    if (p3fault.equals("03")){
                                        overCurrent03="03";
                                        if (sendOverCurrentStatusC3){
                                            reasonC="Other";
                                            sendAvailStatusForC3=true;
                                            sendStatusNotificationRequest("3","OverCurrentFailure",
                                                    "Fault",sessionNumberC3);
                                            sendOverCurrentStatusC3 =false;
                                            stopSendingMeterVals("3");
                                        }
                                    }

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
                                    //sendEmergencyStatus=true;
                                    //fault--------------------------------------------------------------------
                                    if (p1fault.equals("05")) {
                                        isComefromPFUVOV = true;
                                        Float cp1v = Float.parseFloat(p1voltage);
                                        if (cp1v < 260 && cp1v > 195) {

                                        } else {
                                            toggleBtn.setCardBackgroundColor(Color.parseColor(red));
                                            toggleBtn.startAnimation(anim);
                                            if (sPisPluggedin.getisPluggedinCP1().equals("t")) {
                                                sPisPoweFail.setisPowerFailCP1("t");
                                                sPTimeReadingCP1.setTimeReadingCP1(MillisCP1);
                                            }

                                            if (cp1v > 263) {
                                                tv_status1.setText(s13_CP1);
                                                status_detailCP1 = s13_CP1;
                                                overVoltage01="1";

                                                if (sendOverVoltageStatusC1){
                                                    reasonC="Other";
                                                    sendAvailStatusForC1=true;
                                                    sendStatusNotificationRequest("1","OverVoltage",
                                                            "Fault",sessionNumberC1);
                                                    sendOverVoltageStatusC1 =false;
                                                    stopSendingMeterVals("1");
                                                }
                                            } else if (cp1v < 195 && cp1v > 150) {
                                                tv_status1.setText(s12_CP1);
                                                status_detailCP1 = s12_CP1;
                                                underVoltage01="1";
                                                if (sendUnderVoltageStatusC1){
                                                    reasonC="Other";
                                                    sendAvailStatusForC1=true;
                                                    sendStatusNotificationRequest("1","UnderVoltage",
                                                            "Fault",sessionNumberC1);
                                                    sendUnderVoltageStatusC1 =false;
                                                    stopSendingMeterVals("1");
                                                }
                                            } else {
                                                tv_status1.setText(s6_CP1);
                                                status_detailCP1 = s6_CP1;
                                            }
                                        }

                                    }
                                    if (p2fault.equals("05")) {
                                        isComefromPFUVOV = true;
                                        Float cp2v = Float.parseFloat(p2voltage);
                                        if (cp2v < 260 && cp2v > 195) {

                                        } else {
                                            btn_p2onff.setCardBackgroundColor(Color.parseColor(red));
                                            btn_p2onff.startAnimation(anim);

                                            if (sPisPluggedin.getisPluggedinCP2().equals("t")) {
                                                sPisPoweFail.setisPowerFailCP2("t");
                                                sPTimeReadingCP2.setTimeReadingCP2(MillisCP2);
                                            }

                                            if (cp2v > 263) {
                                                reasonC="Other";
                                                tv_status2.setText(s13_CP2);
                                                status_detailCP2 = s13_CP2;
                                                overVolatage02="1";
                                                if (sendOverVoltageStatusC2){
                                                    sendAvailStatusForC2=true;
                                                    sendStatusNotificationRequest("2","OverVoltage",
                                                            "Fault",sessionNumberC2);
                                                    sendOverVoltageStatusC2 =false;
                                                    stopSendingMeterVals("2");
                                                }
                                            } else if (cp2v < 195 && cp2v > 150) {
                                                tv_status2.setText(s12_CP2);
                                                status_detailCP2 = s12_CP2;
                                                underVolatage02="1";
                                                if (sendUnderVoltageStatusC2){
                                                    reasonC="Other";
                                                    sendAvailStatusForC2=true;
                                                    sendStatusNotificationRequest("2",
                                                            "UnderVoltage",
                                                            "Fault",sessionNumberC2);
                                                    sendUnderVoltageStatusC2 =false;
                                                    stopSendingMeterVals("2");
                                                }
                                            } else {
                                                tv_status2.setText(s6_CP2);
                                                status_detailCP2 = s6_CP2;
                                            }
                                        }
                                    }

                                    if (p3fault.equals("05")) {
                                        isComefromPFUVOV = true;

                                        Float cp3v = Float.parseFloat(p3voltage);
                                        if (cp3v < 260 && cp3v > 195) {

                                        } else {
                                            btn_p3onff.setCardBackgroundColor(Color.parseColor(red));

                                            btn_p3onff.startAnimation(anim);
                                            if (sPisPluggedin.getisPluggedinCP3().equals("t")) {
                                                sPisPoweFail.setisPowerFailCP3("t");
                                                sPTimeReadingCP3.setTimeReadingCP3(MillisCP3);
                                            }

                                            if (cp3v > 263) {
                                                tv_status3.setText(s13_CP3);
                                                status_detailCP3 = s13_CP3;
                                                overVolatage03="1";
                                                if (sendOverVoltageStatusC3){
                                                    reasonC="Other";
                                                    sendAvailStatusForC3=true;
                                                    sendStatusNotificationRequest("3","OverVoltage",
                                                            "Fault",sessionNumberC3);
                                                    sendOverVoltageStatusC3 =false;
                                                    stopSendingMeterVals("3");
                                                }
                                            } else if (cp3v < 195 && cp3v > 150) {
                                                tv_status3.setText(s12_CP3);
                                                status_detailCP3 = s12_CP3;
                                                underVolatage03="1";
                                                if (sendUnderVoltageStatusC3){
                                                    reasonC="Other";
                                                    sendAvailStatusForC3=true;
                                                    sendStatusNotificationRequest("3",
                                                            "UnderVoltage",
                                                            "Fault",sessionNumberC3);
                                                    sendUnderVoltageStatusC3 =false;
                                                    stopSendingMeterVals("3");
                                                }
                                            } else {
                                                tv_status3.setText(s6_CP3);
                                                status_detailCP3 = s6_CP3;
                                            }
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
                                        sendUnderVoltageStatusC1 =true;
                                        sendOverVoltageStatusC1 =true;

                                        sendPowerFailStatus=true;
                                        sendOverCurrentStatusC1 =true;

                                        if (sPisPluggedin.getisPluggedinCP1().equals("t")) {

                                            if (plugedoutCountcp1 >= 30) {
                                                sPisPluggedin.setisPluggedinCP1("f");
                                            } else {
                                                displayPlugoutCountC1 = 0;
                                                tv_status1.setText(s7_CP1);
                                                isAvailableC1 = true;
                                                txt_c1round_off_mr.setText(rsPOCP1);
                                                txt_c1diff.setText(etime_detailCP1);
                                                status_detailCP1 = s7_CP1;

                                                if (isStopSendC1){
                                                    webSocketokhttp=openConnection();
                                                    if (sessionNumberC1.equals("0")){
                                                        sessionNumberC1=generateSessionId();
                                                    }

                                                    if (spotpForCP1.getOTPForP1()!=null
                                                            && spotpForCP1.getTransactonIdP1()!=null){
                                                        sendStopRequest("1"
                                                                ,p1MeterValue,spotpForCP1.getTransactonIdP1(),
                                                                spotpForCP1.getOTPForP1(),sessionNumberC1);
                                                        sessionNumberC1OTP=spotpForCP1.getOTPForP1();
                                                    }else{
                                                        sendStopRequest("1"
                                                                ,p1MeterValue,transactionIDC1,
                                                                sessionNumberC1OTP,sessionNumberC1);
                                                        spotpForCP1.setOTPForCP1(sessionNumberC1OTP);
                                                    }
                                                    isStopSendC1 =false;
                                                    sendAvailStatusForC1=true;
                                                    stopSendingMeterVals("1");
                                                }
                                                etime_detailCP1 = etime_detailCP1;
                                                plugedinCountcp1 = 0;
                                                plugedoutCountcp1++;
                                                if (isResumedAftercp1) {
                                                    sPisPoweFail.setisPowerFailCP1("f");
                                                }

                                                //Hide_1 Snippet To Check In Idle State on 09-09-2019 12.59 PM
                                                //----Changed Here---By Pravin
                                                if (emergency01.equals("1")
                                                        || overCurrent01.equals("03")
                                                        || overVoltage01.equals("1")
                                                        || underVoltage01.equals("1")){
                                                    rsCP21="₹ 00.00";
                                                    rsCP1="₹ 00.00";
                                                    unit_detailCP21="00:00";
                                                    unit_detailCP1="00:00";
                                                    etime_detailCP21="00:00";
                                                    etime_detailCP1="00:00";
                                                    layout_main.setVisibility(View.VISIBLE);
                                                    layout_detail.setVisibility(View.GONE);
                                                    detail_flag = "c";
                                                }else {
                                                    rsCP21=rsCP1;
                                                    unit_detailCP21=unit_detailCP1;
                                                    etime_detailCP21=etime_detailCP1;
                                                    layout_main.setVisibility(View.GONE);
                                                    layout_detail.setVisibility(View.VISIBLE);
                                                    detail_flag = "d_CP1";
                                                }
                                            }
                                        } else {
                                            countidleCP1++;
                                            if (isPleaseWaitC1) {
                                                tv_status1.setText("Please Wait...");
                                                status_detailCP1 = "Please Wait...";
                                                toggleBtn.setVisibility(View.INVISIBLE);
                                            } else {
                                                tv_status1.setText(s8_CP1);
                                                status_detailCP1 = s8_CP1;
                                                counterConnectorC1On=0;

                                                //isClickedCancel=false;
                                                if (sendAvailStatusForC1){
                                                    sendStatusNotificationRequest("1","NoError",
                                                            "Available",sessionNumberC1);
                                                    sendAvailStatusForC1=false;
                                                    stopSendingMeterVals("1");
                                                }

                                                toggleBtn.setEnabled(true);
                                                btn_p2onff.setEnabled(true);
                                                btn_p3onff.setEnabled(true);


                                                if (status_detailCP1.equals(s8_CP1)
                                                        && status_detailCP2.equals(s8_CP2)
                                                        && status_detailCP3.equals(s8_CP3)){
                                                    uploadOfflineData();
                                                }

                                                emergency01="0";
                                                overCurrent01="0";
                                                overVoltage01="0";
                                                underVoltage01="0";
                                                transactionIDC1="0";

                                                if (pingHandlerMVC1 != null) {
                                                    pingHandlerMVC1.removeCallbacks(pingRunnableC1);
                                                    pingHandlerMVC1.removeCallbacksAndMessages(null);
                                                }

                                                _CMSCommand="NONE";
                                                checkAuth=true;
                                                sendEmergencyStatus=true;
                                                isStartSendC1=true;
                                                isStopSendC1=true;
                                                sendEmergencyStatus=true;
                                                sendAvailAftrEmergencyStatus=true;
                                                isSendEmergency=false;
                                            }

                                            if (isStopC1) {
                                                isAvailableC1 = true;
                                                isStartC1 = true;
                                                isStopC1 = false;
                                            }

                                            rsCP21=rsCP1;
                                            txt_c1round_off_mr.setText("₹ 00.00");
                                            rsPOCP21=rsPOCP1;
                                            rsPOCP1 = "₹ 00.00";
                                            float mrv = 0;
                                            mrsPOCP1 = "" + mrv;
                                            txt_c1diff.setText("00:00:00");
                                            unit_detailCP21=unit_detailCP1;
                                            etime_detailCP21 =etime_detailCP1;
                                            plugedinCountcp1 = 0;
                                            toggleBtn.clearAnimation();
                                            if (overlayCounti > 10) {
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

                                        if (isStartC1) {
                                            isStartC1 = false;
                                            countC1++;
                                            isStopC1 = true;
                                            isAvailableC1 = false;
                                        }

                                        txt_c1round_off_mr.setText("₹ 00.00");
                                        txt_c1diff.setText("00:00:00");
                                        status_detailCP1 = s1_CP1;
                                        unit_detailCP1 = "00.00";
                                        etime_detailCP1 = "00:00:00";
                                        isStillOnCP1 = false;

                                        counterConnectorC1On++;
                                        Log.e("COUNTERISCHARGING"," C1 --->"+ counterConnectorC1On++);
                                        if (counterConnectorC1On>=400){
                                            //relativeLayout1st.setVisibility(View.VISIBLE);
                                            toggleBtn.setVisibility(View.GONE);
                                            funConnectori();
                                        }else {
                                            relativeLayout1st.setVisibility(View.GONE);
                                            toggleBtn.setVisibility(View.VISIBLE);
                                        }

                                        if (displayPlugoutCountC1 == 12) {
                                            if (sPisPoweFail.getisPowerFailCP1().equals("t")
                                                    && sPisPluggedin.getisPluggedinCP1().equals("t")) {
                                                toggleBtn.performClick();
                                                sPisPoweFail.setisPowerFailCP1("f");
                                                sPisPluggedin.setisPluggedinCP1("f");
                                                tv_status1.setText(s7_CP1);
                                                status_detailCP1 = s7_CP1;
                                                sPisPoweFail.setisPowerFailCP1("f");
                                            }
                                        }
                                        displayPlugoutCountC1++;
                                        //-----------------------------------------===============================================
                                    }

                                    if (p1OnOff.equals("1") && p1plugin.equals("1") && p1fault.equals("99")) {
                                        countidleCP1 = 0;
                                        if (sPisPoweFail.getisPowerFailCP1().equals("t")) {

                                            //---------Status:resuming after power fail_______________
                                            if (plugedinCountcp1 >= 7) {
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
                                                float current_mrCP1 = 0.0f;
                                                try {
                                                    current_mrCP1 = Float.parseFloat(p1meter);
                                                    int b = (int) current_mrCP1;
                                                    sPmeterReadingCP1RO.setMeterReadingCP1RO("" + b);
                                                    float meterreadingdiff = current_mrCP1 - prev_mrCP1;
                                                    float meterreadingdiffP = 0.0f;
                                                    meterreadingdiffP = (current_mrCP1 - prev_mrCP1) * erate;
                                                    String meterreadingCP1 = String.format("%.2f", (current_mrCP1 - prev_mrCP1));
                                                    String meterreadingCP1P = String.format("%.2f", meterreadingdiffP);
                                                    tv_status1.setText(s10_CP1);
                                                    if (meterreadingdiffP > 0 && meterreadingdiffP < 200) {
                                                        txt_c1round_off_mr.setText("₹ " + meterreadingCP1P);
                                                        rsCP1 = "₹ " + meterreadingCP1P;
                                                        rsPOCP21 = "₹ " + meterreadingCP1P;
                                                        rsPOCP1 = "₹ " + meterreadingCP1P;
                                                        float mrv = meterreadingdiffP;
                                                        mrsPOCP1 = "" + mrv;
                                                    }
                                                    txt_c1diff.setText(time_readingCP1);
                                                    status_detailCP1 = s10_CP1;
                                                    unit_detailCP1 = meterreadingCP1;
                                                    etime_detailCP1 = time_readingCP1;
                                                    sPisPoweFail.setisPowerFailCP1("f");
                                                    relativeLayout1st.setVisibility(View.GONE);
                                                } catch (Exception e) {
                                                    Log.e("EXCEPTION","EXCPTN"+e);
                                                }
                                            } else if (plugedinCountcp1 == 0) {
                                                //Time reading_________________
                                                StartTimeCP1 = SystemClock.uptimeMillis() + (-sPTimeReadingCP1.getTimeReadingCP1());
                                                //-------------------------meter reading
                                                float prev_mrCP1 = Float.parseFloat(sPmeterReadingCP1.getMeterReadingCP1());
                                                float current_mrCP1 = 0.0f;
                                                try {
                                                    current_mrCP1 = Float.parseFloat(p1meter);
                                                    int b = (int) current_mrCP1;
                                                    sPmeterReadingCP1RO.setMeterReadingCP1RO("" + b);
                                                    float meterreadingdiff = current_mrCP1 - prev_mrCP1;
                                                    float meterreadingdiffP = 0.0f;
                                                    meterreadingdiffP = (current_mrCP1 - prev_mrCP1) * erate;
                                                    String meterreadingCP1 = String.format("%.2f", meterreadingdiff);
                                                    String meterreadingCP1P = String.format("%.2f", meterreadingdiffP);
                                                    //--------------------
                                                    tv_status1.setText(s9_CP1);

                                                    layout_detail.setVisibility(View.GONE);
                                                    layout_main.setVisibility(View.VISIBLE);
                                                    if (meterreadingdiffP > 0 && meterreadingdiffP < 200) {
                                                        txt_c1round_off_mr.setText("₹ " + meterreadingCP1P);
                                                        rsCP1 = "₹ " + meterreadingCP1P;
                                                        rsPOCP1 = "₹ " + meterreadingCP1P;
                                                        rsPOCP21 = "₹ " + meterreadingCP1P;
                                                        //------------
                                                        float mrv = meterreadingdiffP;
                                                        mrsPOCP1 = "" + mrv;
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
                                                    isAvailableC1 = false;
                                                    webSocketokhttp=openConnection();
                                                } catch (Exception e) {
                                                    Log.e("EXCEPTION","EXCPTN"+e);
                                                }
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
                                                float current_mrCP1 = 0.0f;
                                                try {
                                                    current_mrCP1 = Float.parseFloat(p1meter);
                                                    int b = (int) current_mrCP1;
                                                    sPmeterReadingCP1RO.setMeterReadingCP1RO("" + b);

                                                    float meterreadingdiff = current_mrCP1 - prev_mrCP1;
                                                    float meterreadingdiffP = 0.0f;
                                                    meterreadingdiffP = (current_mrCP1 - prev_mrCP1) * erate;
                                                    String meterreadingCP1 = String.format("%.2f", meterreadingdiff);
                                                    String meterreadingCP1P = String.format("%.2f", meterreadingdiffP);

                                                    tv_status1.setText(s9_CP1);
                                                    isAvailableC1 = false;

                                                    if (meterreadingdiffP > 0 && meterreadingdiffP < 200) {
                                                        txt_c1round_off_mr.setText("₹ " + meterreadingCP1P);
                                                        rsCP1 = "₹ " + meterreadingCP1P;
                                                        rsPOCP1 = "₹ " + meterreadingCP1P;
                                                        rsPOCP21 = "₹ " + meterreadingCP1P;
                                                        float mrv = meterreadingdiffP;
                                                        mrsPOCP1 = "" + mrv;
                                                    }
                                                    txt_c1diff.setText(time_readingCP1);
                                                    status_detailCP1 = s9_CP1;
                                                    layout_detail.setVisibility(View.GONE);
                                                    layout_main.setVisibility(View.VISIBLE);
                                                    unit_detailCP1 = meterreadingCP1;
                                                    etime_detailCP1 = time_readingCP1;
                                                    plugedinCountcp1++;
                                                    sPisPluggedin.setisPluggedinCP1("t");
                                                    isResumedAftercp1 = true;
                                                    relativeLayout1st.setVisibility(View.GONE);

                                                    //------Sending Start Request After Power Resuming-----------//
                                                    if (isStartSendC1){
                                                        connectorIS="0";
                                                        connectorIS="1";
                                                        if (spotpForCP1.getOTPForP1()!=""){
                                                            sendStartRequest("1",spotpForCP1.getOTPForP1(),
                                                                    p1MeterValue,
                                                                    sessionNumberC1);
                                                            sessionNumberC1OTP=spotpForCP1.getOTPForP1();
                                                        }else {
                                                            sendStartRequest("1",sessionNumberC1OTP,
                                                                    p1MeterValue,
                                                                    sessionNumberC1);
                                                            spotpForCP1.setOTPForCP1(sessionNumberC1OTP);
                                                        }

                                                        isStartSendC1 =false;
                                                        isStopSendC1 =true;
                                                        sendPowerFailStatus=true;
                                                        sendAvailStatusForC1=true;


                                                        new InternetCheck(new InternetCheck.Consumer() {
                                                            @Override
                                                            public void accept(Boolean internet) {
                                                                if (internet){

                                                                    pingHandlerMVC1 = new Handler();
                                                                    pingHandlerMVC1.removeCallbacks(pingRunnableC1);
                                                                    pingHandlerMVC1.removeCallbacksAndMessages(null);
                                                                    pingRunnableC1 = new Runnable() {
                                                                        @Override public void run() {
                                                                            new InternetCheck(new InternetCheck.Consumer() {
                                                                                @Override
                                                                                public void accept(Boolean internet) {
                                                                                    if (internet){
                                                                                        if (spotpForCP1.getTransactonIdP1()!=null
                                                                                                || spotpForCP1.getTransactonIdP1()!="0"){
                                                                                            webSocketokhttp.send(JSONObjectsForRequest.sendMeterValuesRequest(getUTC(),"1",voltage_detailCP1,
                                                                                                    spotpForCP1.getTransactonIdP1(),"L1",current_detailCP1,String.valueOf(meterreadingdiffC1*1000),generateSessionId()));
                                                                                        }else {
                                                                                            webSocketokhttp.send(JSONObjectsForRequest.sendMeterValuesRequest(getUTC(),"1",voltage_detailCP1,
                                                                                                    transactionIDC1,"L1",current_detailCP1,String.valueOf(meterreadingdiffC1*1000),generateSessionId()));
                                                                                        }
                                                                                        pingHandlerMVC1.postDelayed(pingRunnableC1, 120000);
                                                                                    }else {
                                                                                        if (pingHandlerMVC1 != null) {
                                                                                            pingHandlerMVC1.removeCallbacks(pingRunnableC1);
                                                                                            pingHandlerMVC1.removeCallbacksAndMessages(null);
                                                                                        }
                                                                                    }
                                                                                }
                                                                            });
                                                                        }
                                                                    };
                                                                    pingHandlerMVC1.postDelayed(pingRunnableC1, 120000);
                                                                }
                                                            }
                                                        });

                                                    }

                                                } catch (Exception e) {
                                                    Log.e("EXCEPTION","EXPTN"+e);
                                                }
                                            }
                                        } else {
                                            //--------------- Status:vehicle pugged in_________
                                            if (plugedinCountcp1 >= 7) {
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
                                                float current_mrCP1 = 0.0f;
                                                try {
                                                    current_mrCP1 = Float.parseFloat(p1meter);
                                                    int b = (int) current_mrCP1;
                                                    sPmeterReadingCP1RO.setMeterReadingCP1RO("" + b);
                                                    float meterreadingdiff = current_mrCP1 - prev_mrCP1;
                                                    meterreadingdiffC1=current_mrCP1/* - prev_mrCP1*/;
                                                    float meterreadingdiffP = 0.0f;
                                                    meterreadingdiffP = (current_mrCP1 - prev_mrCP1) * erate;
                                                    String meterreadingCP1 = String.format("%.2f", current_mrCP1 - prev_mrCP1);
                                                    meterreadingdiffC1=Float.parseFloat(meterreadingCP1);
                                                    String meterreadingCP1P = String.format("%.2f", meterreadingdiffP);

                                                    tv_status1.setText(s10_CP1);
                                                    if (meterreadingdiffP > 0 && meterreadingdiffP < 200) {
                                                        txt_c1round_off_mr.setText("₹ " + meterreadingCP1P);
                                                        rsCP1 = "₹ " + meterreadingCP1P;
                                                        rsPOCP1 = "₹ " + meterreadingCP1P;
                                                        rsPOCP21 = "₹ " + meterreadingCP1P;
                                                        float mrv = meterreadingdiffP;
                                                        mrsPOCP1 = "" + mrv;
                                                    }
                                                    txt_c1diff.setText(time_readingCP1);
                                                    status_detailCP1 = s10_CP1;
                                                    unit_detailCP1 = meterreadingCP1;
                                                    etime_detailCP1 = time_readingCP1;
                                                    relativeLayout1st.setVisibility(View.GONE);
                                                } catch (Exception e) {
                                                    Log.e("EXCEPTION","EXPTN"+e);
                                                }
                                            } else if (plugedinCountcp1 == 0) {
                                                tv_status1.setText(s11_CP1);
                                                isAvailableC1 = false;
                                                txt_c1round_off_mr.setText("₹ 00.00");
                                                txt_c1diff.setText("00:00:00");
                                                status_detailCP1 = s11_CP1;
                                                counterConnectorC1On=0;
                                                if (isStartSendC1){
                                                    sendStartRequest("1",sessionNumberC1OTP,
                                                            p1MeterValue,
                                                            sessionNumberC1);
                                                    spotpForCP1.setOTPForCP1(sessionNumberC1OTP);
                                                    spotpForCP1.setOTPForCP1(sessionNumberC1OTP);

                                                    isStartSendC1 =false;
                                                    isStopSendC1 =true;
                                                    sendPowerFailStatus=true;
                                                    sendAvailStatusForC1=true;

                                                    new InternetCheck(new InternetCheck.Consumer() {
                                                        @Override
                                                        public void accept(Boolean internet) {
                                                            if (internet){

                                                                pingHandlerMVC1 = new Handler();
                                                                pingHandlerMVC1.removeCallbacks(pingRunnableC1);
                                                                pingHandlerMVC1.removeCallbacksAndMessages(null);
                                                                pingRunnableC1 = new Runnable() {
                                                                    @Override public void run() {
                                                                        new InternetCheck(new InternetCheck.Consumer() {
                                                                            @Override
                                                                            public void accept(Boolean internet) {
                                                                                if (internet){
                                                                                    if (spotpForCP1.getTransactonIdP1()!=null
                                                                                            || spotpForCP1.getTransactonIdP1()!="0"){
                                                                                        webSocketokhttp.send(JSONObjectsForRequest.sendMeterValuesRequest(getUTC(),"1",voltage_detailCP1,
                                                                                                spotpForCP1.getTransactonIdP1(),"L1",current_detailCP1,String.valueOf(meterreadingdiffC1*1000),generateSessionId()));
                                                                                    }else {
                                                                                        webSocketokhttp.send(JSONObjectsForRequest.sendMeterValuesRequest(getUTC(),"1",voltage_detailCP1,
                                                                                                transactionIDC1,"L1",current_detailCP1,String.valueOf(meterreadingdiffC1*1000),generateSessionId()));
                                                                                    }
                                                                                    pingHandlerMVC1.postDelayed(pingRunnableC1, 120000);
                                                                                }else {
                                                                                    if (pingHandlerMVC1 != null) {
                                                                                        pingHandlerMVC1.removeCallbacks(pingRunnableC1);
                                                                                        pingHandlerMVC1.removeCallbacksAndMessages(null);
                                                                                    }
                                                                                }
                                                                            }
                                                                        });
                                                                    }
                                                                };
                                                                pingHandlerMVC1.postDelayed(pingRunnableC1, 120000);
                                                            }
                                                        }
                                                    });
                                                }

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
                                                float current_mrCP1 = 0.0f;
                                                try {
                                                    current_mrCP1 = Float.parseFloat(p1meter);
                                                    int b = (int) current_mrCP1;

                                                    sPmeterReadingCP1RO.setMeterReadingCP1RO("" + b);

                                                    float meterreadingdiff = current_mrCP1 - prev_mrCP1;
                                                    float meterreadingdiffP = 0.0f;
                                                    meterreadingdiffP = (current_mrCP1 - prev_mrCP1) * erate;

                                                    String meterreadingCP1 = String.format("%.2f", meterreadingdiff);
                                                    String meterreadingCP1P = String.format("%.2f", meterreadingdiffP);
                                                    tv_status1.setText(s11_CP1);
                                                    isAvailableC1 = false;

                                                    if (meterreadingdiffP > 0 && meterreadingdiffP < 200) {
                                                        txt_c1round_off_mr.setText("₹ " + meterreadingCP1P);
                                                        rsCP1 = "₹ " + meterreadingCP1P;
                                                        rsPOCP1 = "₹ " + meterreadingCP1P;
                                                        rsPOCP21 = "₹ " + meterreadingCP1P;
                                                        float mrv = meterreadingdiffP;
                                                        mrsPOCP1 = "" + mrv;
                                                    }
                                                    txt_c1diff.setText(time_readingCP1);
                                                    status_detailCP1 = s11_CP1;
                                                    unit_detailCP1 = meterreadingCP1;
                                                    etime_detailCP1 = time_readingCP1;
                                                    sPisPluggedin.setisPluggedinCP1("t");
                                                    plugedinCountcp1++;
                                                } catch (Exception e) {
                                                    Log.e("EXCEPTION","EXCTPN"+e);
                                                }
                                            }
                                        }
                                    }

//=======================================================================================================================================================================================================================================================================================

                                    //-----------------------------------------------------------CP2 Status-----------------------------------------------------------------------------------------------------------------------------
                                    if (p2OnOff.equals("0") && p2plugin.equals("0") && p2fault.equals("99")) {
                                        sendUnderVoltageStatusC2 =true;
                                        sendOverVoltageStatusC2=true;

                                        sendPowerFailStatus=true;
                                        sendOverCurrentStatusC2 =true;

                                        if (sPisPluggedin.getisPluggedinCP2().equals("t")) {

                                            if (plugedoutCountcp2 >= 30) {
                                                sPisPluggedin.setisPluggedinCP2("f");

                                            } else {
                                                displayPlugoutCountC2 = 0;
                                                tv_status2.setText(s7_CP2);
                                                isAvailableC2 = true;

                                                txt_c2round_off_mr.setText(rsPOCP2);

                                                txt_c2diff.setText(etime_detailCP2);

                                                status_detailCP2 = s7_CP2;
                                                if (isStopSendC2){
                                                    webSocketokhttp=openConnection();
                                                    if (spotpForCP1.getOTPForP2()!=null
                                                            && spotpForCP1.getTransactonIdP2()!=null){
                                                        sendStopRequest("2",
                                                                p2MeterValue,
                                                                spotpForCP1.getTransactonIdP2(),
                                                                spotpForCP1.getOTPForP2(),
                                                                sessionNumberC2);
                                                        sessionNumberC2OTP=spotpForCP1.getOTPForP2();
                                                    }else{
                                                        sendStopRequest("2",
                                                                p2MeterValue,
                                                                transactionIDC2,sessionNumberC2OTP,
                                                                sessionNumberC2);
                                                        spotpForCP1.setOTPForCP2(sessionNumberC2OTP);
                                                    }

                                                    isStopSendC2 =false;
                                                    stopSendingMeterVals("2");
                                                }

                                                unit_detailCP2 = unit_detailCP2;
                                                etime_detailCP2 = etime_detailCP2;

                                                plugedinCountcp2 = 0;
                                                plugedoutCountcp2++;
                                                if (isResumedAftercp2) {
                                                    sPisPoweFail.setisPowerFailCP2("f");

                                                }

                                                if (emergency02.equals("1")
                                                        || overCurrent02.equals("03")
                                                        || overVolatage02.equals("1")
                                                        || underVolatage02.equals("1")){

                                                    rsCP22="₹ 00.00";
                                                    rsCP2="₹ 00.00";
                                                    unit_detailCP22="00:00";
                                                    unit_detailCP2="00:00";
                                                    etime_detailCP22="00:00";
                                                    etime_detailCP2="00:00";
                                                    layout_main.setVisibility(View.VISIBLE);
                                                    layout_detail.setVisibility(View.GONE);
                                                    detail_flag = "c";
                                                }else {

                                                    rsCP22=rsCP2;
                                                    unit_detailCP22=unit_detailCP2;
                                                    etime_detailCP22=etime_detailCP2;
                                                    layout_main.setVisibility(View.GONE);
                                                    layout_detail.setVisibility(View.VISIBLE);
                                                    detail_flag = "d_CP2";
                                                }
                                            }

                                        } else {
                                            countidleCP2++;
                                            if (isPleaseWaitC2) {
                                                tv_status2.setText("Please Wait...");
                                                status_detailCP2 = "Please Wait...";
                                                btn_p2onff.setVisibility(View.INVISIBLE);

                                            } else {
                                                tv_status2.setText(s8_CP2);
                                                status_detailCP2 = s8_CP2;
                                                counterConnectorC2On=0;

                                                // isClickedCancel=false;
                                                if (sendAvailStatusForC2){
                                                    sendStatusNotificationRequest("2","NoError",
                                                            "Available",sessionNumberC2);
                                                    sendAvailStatusForC2=false;
                                                    stopSendingMeterVals("2");
                                                }

                                                toggleBtn.setEnabled(true);
                                                btn_p2onff.setEnabled(true);
                                                btn_p3onff.setEnabled(true);

                                                relativeLayout1st.setEnabled(true);
                                                relativeLayout2nd.setEnabled(true);
                                                relativeLayout3rd.setEnabled(true);

                                                if (status_detailCP1.equals(s8_CP1)
                                                        && status_detailCP2.equals(s8_CP2)
                                                        && status_detailCP3.equals(s8_CP3)){
                                                    uploadOfflineData();
                                                }

                                                //counterConnectorC1On=0;
                                                emergency02="0";
                                                overCurrent02="0";
                                                overVolatage02="0";
                                                underVolatage02="0";
                                                isStartSendC2 =true;
                                                isStopSendC2 =true;
                                                transactionIDC2="0";
                                                if (pingHandlerMVC2 != null) {
                                                    pingHandlerMVC2.removeCallbacks(pingRunnableC2);
                                                    pingHandlerMVC2.removeCallbacksAndMessages(null);
                                                }
                                            }

                                            if (isStopC2) {
                                                isAvailableC2 = true;
                                                isStartC2 = true;
                                                isStopC2 = false;
                                            }

                                            rsCP22=rsCP2;
                                            txt_c2round_off_mr.setText("₹ 00.00");
                                            rsPOCP22=rsPOCP2;
                                            rsPOCP2 = "₹ 00.00";
                                            //------------
                                            float mrv = 0;
                                            mrsPOCP2 = "" + mrv;
                                            //-----------
                                            txt_c2diff.setText("00:00:00");
                                            unit_detailCP2 = "00.00";
                                            etime_detailCP2 = "00:00:00";
                                            plugedinCountcp2 = 0;
                                            btn_p2onff.clearAnimation();

                                            if (overlayCountii > 10) {
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

                                        if (isStartC2) {
                                            isStartC2 = false;
                                            countC2++;
                                            isStopC2 = true;
                                            isAvailableC2 = false;
                                        }

                                        txt_c2round_off_mr.setText("₹ 00.00");
                                        txt_c2diff.setText("00:00:00");
                                        status_detailCP2 = s1_CP2;
                                        unit_detailCP2 = "00.00";
                                        etime_detailCP2 = "00:00:00";
                                        isStillOnCP2 = false;

                                        counterConnectorC2On++;
                                        Log.e("COUNTERISCHARGING"," C2 --->"+ counterConnectorC2On++);
                                        if (counterConnectorC2On>=400){
                                            //relativeLayout2nd.setVisibility(View.VISIBLE);
                                            btn_p2onff.setVisibility(View.GONE);
                                            funConnectorii();
                                        }else {
                                            relativeLayout2nd.setVisibility(View.GONE);
                                            btn_p2onff.setVisibility(View.VISIBLE);

                                        }

                                        if (displayPlugoutCountC2 == 12) {
                                            if (sPisPoweFail.getisPowerFailCP2().equals("t") && sPisPluggedin.getisPluggedinCP2().equals("t")) {
                                                btn_p2onff.performClick();
                                                sPisPoweFail.setisPowerFailCP2("f");
                                                sPisPluggedin.setisPluggedinCP2("f");
                                                tv_status2.setText(s7_CP2);
                                                status_detailCP2 = s7_CP2;
                                                sPisPoweFail.setisPowerFailCP2("f");
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
                                                float prev_mrCP2 = Float.parseFloat(
                                                        sPmeterReadingCP2.getMeterReadingCP2());
                                                float current_mrCP2 = 0.0f;
                                                try {
                                                    current_mrCP2 = Float.parseFloat(p2meter);
                                                    int b = (int) current_mrCP2;
                                                    sPmeterReadingCP2RO.setMeterReadingCP2RO("" + b);
                                                    float meterreadingdiff2 = current_mrCP2 - prev_mrCP2;
                                                    meterreadingdiffC2=meterreadingdiff2;
                                                    float meterreadingdiffP2 = 0.0f;
                                                    meterreadingdiffP2 = (current_mrCP2 - prev_mrCP2) * erate;
                                                    String meterreadingCP2 = String.format("%.2f", meterreadingdiff2);
                                                    meterreadingdiffC2=Float.parseFloat(String.format("%.2f", current_mrCP2));
                                                    String meterreadingCP2P = String.format("%.2f", meterreadingdiffP2);
                                                    //------------------
                                                    tv_status2.setText(s10_CP2);
                                                    if (meterreadingdiffP2 > 0 && meterreadingdiffP2 < 200) {
                                                        txt_c2round_off_mr.setText("₹ " + meterreadingCP2P);
                                                        rsCP2 = "₹ " + meterreadingCP2P;
                                                        rsPOCP2 = "₹ " + meterreadingCP2P;
                                                        rsPOCP22 = "₹ " + meterreadingCP2P;
                                                        mrsPOCP2 = "" + meterreadingdiffP2;
                                                    }
                                                    txt_c2diff.setText(time_readingCP2);
                                                    status_detailCP2 = s10_CP2;
                                                    unit_detailCP2 = meterreadingCP2;
                                                    etime_detailCP2 = time_readingCP2;
                                                    sPisPoweFail.setisPowerFailCP2("f");
                                                    relativeLayout2nd.setVisibility(View.GONE);
                                                } catch (NumberFormatException ex) {
                                                    ex.printStackTrace();
                                                }
                                            } else if (plugedinCountcp2 == 0) {
                                                //Time reading_________________
                                                StartTimeCP2 = SystemClock.uptimeMillis() + (-sPTimeReadingCP2.getTimeReadingCP2());
                                                //-------------------------meter reading
                                                float prev_mrCP2 = Float.parseFloat(sPmeterReadingCP2.getMeterReadingCP2());
                                                float current_mrCP2 = 0.0f;
                                                try {
                                                    current_mrCP2 = Float.parseFloat(p2meter);
                                                    int b = (int) current_mrCP2;
                                                    sPmeterReadingCP2RO.setMeterReadingCP2RO("" + b);
                                                    float meterreadingdiff2 = current_mrCP2 - prev_mrCP2;
                                                    float meterreadingdiffP2 = 0.0f;
                                                    meterreadingdiffP2 = (current_mrCP2 - prev_mrCP2) * erate;
                                                    String meterreadingCP2 = String.format("%.2f", meterreadingdiff2);
                                                    String meterreadingCP2P = String.format("%.2f", meterreadingdiffP2);
                                                    //--------------------
                                                    tv_status2.setText(s9_CP2);
                                                    if (meterreadingdiffP2 > 0 && meterreadingdiffP2 < 200) {
                                                        txt_c2round_off_mr.setText("₹ " + meterreadingCP2P);
                                                        rsCP2 = "₹ " + meterreadingCP2P;
                                                        rsPOCP2 = "₹ " + meterreadingCP2P;
                                                        rsPOCP22 = "₹ " + meterreadingCP2P;
                                                        float mrv = meterreadingdiffP2;
                                                        mrsPOCP2 = "" + mrv;
                                                    }
                                                    txt_c2diff.setText("00:00:00");
                                                    status_detailCP2 = s9_CP2;
                                                    unit_detailCP2 = meterreadingCP2;
                                                    etime_detailCP2 = "00:00:00";
                                                    plugedinCountcp2++;
                                                    sPisPluggedin.setisPluggedinCP2("t");
                                                    isResumedAftercp2 = true;
                                                    btn_p2onff.startAnimation(anim);
                                                    relativeLayout2nd.setVisibility(View.GONE);
                                                    isAvailableC2 = false;
                                                } catch (Exception e) {
                                                    Log.e("EXCEPTION","EXCTPN"+e);
                                                }
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
                                                float prev_mrCP2 = Float.parseFloat(sPmeterReadingCP2.getMeterReadingCP2());
                                                float current_mrCP2 = 0.0f;
                                                try {
                                                    current_mrCP2 = Float.parseFloat(p2meter);
                                                    int b = (int) current_mrCP2;
                                                    sPmeterReadingCP2RO.setMeterReadingCP2RO("" + b);
                                                    float meterreadingdiff2 = current_mrCP2 - prev_mrCP2;
                                                    float meterreadingdiffP2 = 0.0f;
                                                    meterreadingdiffP2 = (current_mrCP2 - prev_mrCP2) * erate;
                                                    String meterreadingCP2 = String.format("%.2f", meterreadingdiff2);
                                                    String meterreadingCP2P = String.format("%.2f", meterreadingdiffP2);
                                                    //--------------------
                                                    tv_status2.setText(s9_CP2);
                                                    isAvailableC2 = false;

                                                    if (meterreadingdiffP2 > 0 && meterreadingdiffP2 < 200) {
                                                        txt_c2round_off_mr.setText("₹ " + meterreadingCP2P);
                                                        rsCP2 = "₹ " + meterreadingCP2P;
                                                        rsPOCP2 = "₹ " + meterreadingCP2P;
                                                        rsPOCP22 = "₹ " + meterreadingCP2P;
                                                        float mrv = meterreadingdiffP2;
                                                        mrsPOCP2 = "" + mrv;
                                                    }
                                                    txt_c2diff.setText(time_readingCP2);
                                                    status_detailCP2 = s9_CP2;
                                                    unit_detailCP2 = meterreadingCP2;
                                                    etime_detailCP2 = time_readingCP2;
                                                    plugedinCountcp2++;
                                                    sPisPluggedin.setisPluggedinCP2("t");
                                                    isResumedAftercp2 = true;
                                                    relativeLayout2nd.setVisibility(View.GONE);

                                                    if (isStartSendC2){
                                                        connectorIS="0";
                                                        connectorIS="2";

                                                        /*sendStartRequest("2",sessionNumberC2OTP,
                                                                p2MeterValue,
                                                                sessionNumberC2);*/
                                                        if (spotpForCP1.getOTPForP2()!=""){
                                                            sendStartRequest("2",spotpForCP1.getOTPForP2(),
                                                                    p2MeterValue,
                                                                    sessionNumberC2);
                                                            sessionNumberC2OTP=spotpForCP1.getOTPForP2();

                                                        }else {
                                                            sendStartRequest("2",sessionNumberC2OTP,
                                                                    p2MeterValue,
                                                                    sessionNumberC2);
                                                            spotpForCP1.setOTPForCP2(sessionNumberC2OTP);
                                                        }

                                                        isStartSendC2 =false;
                                                        isStopSendC2 =true;
                                                        sendPowerFailStatus=true;
                                                        sendAvailStatusForC2=true;

                                                        new InternetCheck(new InternetCheck.Consumer() {
                                                            @Override
                                                            public void accept(Boolean internet) {
                                                                if (internet){

                                                                    pingHandlerMVC2 = new Handler();
                                                                    pingHandlerMVC2.removeCallbacks(pingRunnableC2);
                                                                    pingHandlerMVC2.removeCallbacksAndMessages(null);
                                                                    pingRunnableC2 = new Runnable() {
                                                                        @Override public void run() {
                                                                            new InternetCheck(new InternetCheck.Consumer() {
                                                                                @Override
                                                                                public void accept(Boolean internet) {
                                                                                    if (internet){
                                                                                        if (spotpForCP1.getTransactonIdP2()!=null
                                                                                                || spotpForCP1.getTransactonIdP2()!="0"){
                                                                                            webSocketokhttp.send(JSONObjectsForRequest.sendMeterValuesRequest(getUTC(),"2",voltage_detailCP2,
                                                                                                    spotpForCP1.getTransactonIdP2(),"L2",current_detailCP2,String.valueOf(meterreadingdiffC2*1000),generateSessionId()));
                                                                                        }else {
                                                                                            webSocketokhttp.send(JSONObjectsForRequest.sendMeterValuesRequest(getUTC(),"2",voltage_detailCP2,
                                                                                                    transactionIDC2,"L2",current_detailCP2,String.valueOf(meterreadingdiffC2*1000),generateSessionId()));
                                                                                        }
                                                                                        pingHandlerMVC2.postDelayed(pingRunnableC2, 120000);
                                                                                    }else {
                                                                                        if (pingHandlerMVC2 != null) {
                                                                                            pingHandlerMVC2.removeCallbacks(pingRunnableC2);
                                                                                            pingHandlerMVC2.removeCallbacksAndMessages(null);
                                                                                        }
                                                                                    }
                                                                                }
                                                                            });
                                                                        }
                                                                    };
                                                                    pingHandlerMVC2.postDelayed(pingRunnableC2, 120000);
                                                                }
                                                            }
                                                        });

                                                    }

                                                } catch (Exception e) {
                                                    Log.e("EXCEPTION","Exctpn"+e);
                                                }
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
                                                float prev_mrCP2 = Float.parseFloat(sPmeterReadingCP2.getMeterReadingCP2());
                                                float current_mrCP2 = 0.0f;
                                                try {
                                                    current_mrCP2 = Float.parseFloat(p2meter);
                                                    int b = (int) current_mrCP2;
                                                    sPmeterReadingCP2RO.setMeterReadingCP2RO("" + b);
                                                    float meterreadingdiff2 = current_mrCP2 - prev_mrCP2;
                                                    meterreadingdiffC2=current_mrCP2 - prev_mrCP2;
                                                    float meterreadingdiffP2 = 0.0f;
                                                    meterreadingdiffP2 = (current_mrCP2 - prev_mrCP2) * erate;
                                                    String meterreadingCP2 = String.format("%.2f", meterreadingdiff2);
                                                    meterreadingdiffC2=Float.parseFloat(meterreadingCP2);
                                                    String meterreadingCP2P = String.format("%.2f", meterreadingdiffP2);
                                                    tv_status2.setText(s10_CP2);
                                                    if (meterreadingdiffP2 > 0 && meterreadingdiffP2 < 200) {
                                                        txt_c2round_off_mr.setText("₹ " + meterreadingCP2P);
                                                        rsCP2 = "₹ " + meterreadingCP2P;
                                                        rsPOCP2 = "₹ " + meterreadingCP2P;
                                                        rsPOCP22 = "₹ " + meterreadingCP2P;
                                                        float mrv = meterreadingdiffP2;
                                                        mrsPOCP1 = "" + mrv;
                                                    }
                                                    txt_c2diff.setText(time_readingCP2);
                                                    status_detailCP2 = s10_CP2;
                                                    unit_detailCP2 = meterreadingCP2;
                                                    etime_detailCP2 = time_readingCP2;
                                                    relativeLayout2nd.setVisibility(View.GONE);
                                                } catch (Exception e) {
                                                    Log.e("EXCEPTION","EXCPTN "+e);
                                                }
                                            } else if (plugedinCountcp2 == 0) {
                                                tv_status2.setText(s11_CP2);
                                                isAvailableC2 = false;
                                                txt_c2round_off_mr.setText("₹ 00.00");
                                                txt_c2diff.setText("00:00:00");
                                                status_detailCP2 = s11_CP2;

                                                if (isStartSendC2){
                                                    connectorIS="0";
                                                    connectorIS="2";

                                                   /* sendStartRequest("2",sessionNumberC2OTP,
                                                            p2MeterValue,
                                                            sessionNumberC2);*/
                                                    /*if (spotpForCP1.getOTPForP2()!=""){
                                                        sendStartRequest("2",spotpForCP1.getOTPForP2(),
                                                                p2MeterValue,
                                                                sessionNumberC2);
                                                    }else {*/
                                                    sendStartRequest("2",sessionNumberC2OTP,
                                                            p2MeterValue,
                                                            sessionNumberC2);
                                                    spotpForCP1.setOTPForCP2(sessionNumberC2OTP);
                                                    /* }*/

                                                    isStartSendC2 =false;
                                                    isStopSendC2 =true;
                                                    sendPowerFailStatus=true;
                                                    sendAvailStatusForC2=true;

/*
                                                    new InternetCheck(new InternetCheck.Consumer() {
                                                        @Override
                                                        public void accept(Boolean internet) {
                                                            if (internet){
                                                                pingHandlerMVC2 = new Handler();
                                                                pingHandlerMVC2.removeCallbacks(pingRunnableC2);
                                                                pingHandlerMVC2.removeCallbacksAndMessages(null);
                                                                pingRunnableC2 = new Runnable() {
                                                                    @Override public void run() {
                                                                        //webSocketokhttp=openConnection();
                                                                        if (spotpForCP1.getTransactonIdP2()!=null
                                                                                || spotpForCP1.getTransactonIdP2()!="0"){
                                                                            webSocketokhttp.send(JSONObjectsForRequest.sendMeterValuesRequest(getUTC(),"2",voltage_detailCP2,
                                                                                    spotpForCP1.getTransactonIdP2(),"L2",current_detailCP2,String.valueOf(meterreadingdiffC2*1000),generateSessionId()));
                                                                        }else {
                                                                            webSocketokhttp.send(JSONObjectsForRequest.sendMeterValuesRequest(getUTC(),"2",voltage_detailCP2,
                                                                                    transactionIDC2,"L2",current_detailCP2,String.valueOf(meterreadingdiffC2*1000),generateSessionId()));
                                                                        }
                                                                        pingHandlerMVC2.postDelayed(this, 120000);
                                                                    }
                                                                };
                                                                pingHandlerMVC2.postDelayed(pingRunnableC2, 120000);
                                                            }
                                                        }
                                                    });
*/


                                                    new InternetCheck(new InternetCheck.Consumer() {
                                                        @Override
                                                        public void accept(Boolean internet) {
                                                            if (internet){

                                                                pingHandlerMVC2 = new Handler();
                                                                pingHandlerMVC2.removeCallbacks(pingRunnableC2);
                                                                pingHandlerMVC2.removeCallbacksAndMessages(null);
                                                                pingRunnableC2 = new Runnable() {
                                                                    @Override public void run() {
                                                                        new InternetCheck(new InternetCheck.Consumer() {
                                                                            @Override
                                                                            public void accept(Boolean internet) {
                                                                                if (internet){
                                                                                    if (spotpForCP1.getTransactonIdP2()!=null
                                                                                            || spotpForCP1.getTransactonIdP2()!="0"){
                                                                                        webSocketokhttp.send(JSONObjectsForRequest.sendMeterValuesRequest(getUTC(),"2",voltage_detailCP2,
                                                                                                spotpForCP1.getTransactonIdP2(),"L2",current_detailCP2,String.valueOf(meterreadingdiffC2*1000),generateSessionId()));
                                                                                    }else {
                                                                                        webSocketokhttp.send(JSONObjectsForRequest.sendMeterValuesRequest(getUTC(),"2",voltage_detailCP2,
                                                                                                transactionIDC2,"L2",current_detailCP2,String.valueOf(meterreadingdiffC2*1000),generateSessionId()));
                                                                                    }
                                                                                    pingHandlerMVC2.postDelayed(pingRunnableC2, 120000);
                                                                                }else {
                                                                                    if (pingHandlerMVC2 != null) {
                                                                                        pingHandlerMVC2.removeCallbacks(pingRunnableC2);
                                                                                        pingHandlerMVC2.removeCallbacksAndMessages(null);
                                                                                    }
                                                                                }
                                                                            }
                                                                        });
                                                                    }
                                                                };
                                                                pingHandlerMVC2.postDelayed(pingRunnableC2, 120000);
                                                            }
                                                        }
                                                    });
                                                }

                                                unit_detailCP2 = "00.00";
                                                etime_detailCP2 = "00:00:00";
                                                sPisPluggedin.setisPluggedinCP2("t");
                                                plugedinCountcp2++;
                                                //meter reading-------------------------
                                                sPmeterReadingCP2.setMeterReadingCP2(p2meter);
                                                //Time reading_________________
                                                StartTimeCP2 = SystemClock.uptimeMillis() + (-NewBeginMillsCP2);  //--> Start Time
                                                btn_p2onff.startAnimation(anim);
                                            } else {
                                                //time reading______
                                                MillisCP2 = (SystemClock.uptimeMillis() - StartTimeCP2);
                                                HoursCP2 = (int) (MillisCP2 / (1000 * 60 * 60));
                                                MinutesCP2 = (int) (MillisCP2 / (1000 * 60)) % 60;
                                                SecondsCP2 = (int) (MillisCP2 / 1000) % 60;
                                                String time_readingCP2 = "" + String.format("%02d", HoursCP2) + ":"
                                                        + String.format("%02d", MinutesCP2) + ":"
                                                        + String.format("%02d", SecondsCP2);

                                                //-----------meter Reading
                                                float prev_mrCP2 = Float.parseFloat(sPmeterReadingCP2.getMeterReadingCP2());
                                                float current_mrCP2 = 0.0f;
                                                try {
                                                    current_mrCP2 = Float.parseFloat(p2meter);
                                                    int b = (int) current_mrCP2;

                                                    sPmeterReadingCP2RO.setMeterReadingCP2RO("" + b);
                                                    float meterreadingdiff2 = current_mrCP2 - prev_mrCP2;
                                                    float meterreadingdiffP2 = 0.0f;
                                                    meterreadingdiffP2 = (current_mrCP2 - prev_mrCP2) * erate;
                                                    String meterreadingCP2 = String.format("%.2f", meterreadingdiff2);
                                                    String meterreadingCP2P = String.format("%.2f", meterreadingdiffP2);
                                                    //--------------------
                                                    tv_status2.setText(s11_CP2);
                                                    isAvailableC2 = false;

                                                    if (meterreadingdiffP2 > 0 && meterreadingdiffP2 < 200) {
                                                        txt_c2round_off_mr.setText("₹ " + meterreadingCP2P);
                                                        rsCP2 = "₹ " + meterreadingCP2P;
                                                        rsPOCP2 = "₹ " + meterreadingCP2P;
                                                        rsPOCP22 = "₹ " + meterreadingCP2P;
                                                        float mrv = meterreadingdiffP2;
                                                        mrsPOCP2 = "" + mrv;
                                                    }
                                                    txt_c2diff.setText(time_readingCP2);
                                                    status_detailCP2 = s11_CP2;
                                                    unit_detailCP2 = meterreadingCP2;
                                                    etime_detailCP2 = time_readingCP2;
                                                    sPisPluggedin.setisPluggedinCP2("t");
                                                    plugedinCountcp2++;
                                                } catch (NumberFormatException e) {
                                                    Log.e("Exception","NumberFormat2.1"+e);
                                                }
                                            }
                                        }
                                    }

//================================================================================================================================================================================================================

//=======================================================================================================================================================================================================================================================================================
                                    //-----------------------------------------------------------CP3 Status-----------------------------------------------------------------------------------------------------------------------------
                                    if (p3OnOff.equals("0") && p3plugin.equals("0") && p3fault.equals("99")) {
                                        sendUnderVoltageStatusC3 =true;
                                        sendOverVoltageStatusC3=true;
                                        sendPowerFailStatus=true;
                                        sendOverCurrentStatusC3 =true;
                                        if (sPisPluggedin.getisPluggedinCP3().equals("t")) {
                                            if (plugedoutCountcp3 >= 30) {
                                                sPisPluggedin.setisPluggedinCP3("f");
                                            } else {
                                                displayPlugoutCountC3 = 0;
                                                tv_status3.setText(s7_CP3);
                                                isAvailableC3 = true;
                                                txt_c3round_off_mr.setText(rsPOCP3);
                                                txt_c3diff.setText(etime_detailCP3);
                                                status_detailCP3 = s7_CP3;
                                                unit_detailCP3 = unit_detailCP3;
                                                etime_detailCP3 = etime_detailCP3;

                                                if (isStopSendC3){
                                                    webSocketokhttp=openConnection();
                                                    if (spotpForCP1.getOTPForP3()!=null
                                                            && spotpForCP1.getTransactonIdP3()!=null){
                                                        sendStopRequest("3",
                                                                p3MeterValue,
                                                                spotpForCP1.getTransactonIdP3(),
                                                                spotpForCP1.getOTPForP3(),
                                                                sessionNumberC3);
                                                        sessionNumberC3OTP=spotpForCP1.getOTPForP3();
                                                    }else{
                                                        sendStopRequest("3",
                                                                p3MeterValue,
                                                                transactionIDC3,sessionNumberC3OTP,
                                                                sessionNumberC3);
                                                        spotpForCP1.setOTPForCP3(sessionNumberC3OTP);

                                                    }
                                                    isStopSendC3 =false;
                                                    stopSendingMeterVals("3");
                                                }

                                                plugedinCountcp3 = 0;
                                                plugedoutCountcp3++;

                                                if (isResumedAftercp3) {
                                                    sPisPoweFail.setisPowerFailCP3("f");
                                                }

                                                if (emergency03.equals("1")
                                                        || overCurrent03.equals("03")
                                                        || overVolatage03.equals("1")
                                                        || underVolatage03.equals("1")){
                                                    rsCP23="₹ 00.00";
                                                    rsCP3="₹ 00.00";
                                                    unit_detailCP23="00:00";
                                                    unit_detailCP3="00:00";
                                                    etime_detailCP23="00:00";
                                                    etime_detailCP3="00:00";
                                                    layout_main.setVisibility(View.VISIBLE);
                                                    layout_detail.setVisibility(View.GONE);
                                                    detail_flag = "c";
                                                }else {
                                                    rsCP23=rsCP3;
                                                    unit_detailCP23=unit_detailCP3;
                                                    etime_detailCP23=etime_detailCP3;
                                                    layout_main.setVisibility(View.GONE);
                                                    layout_detail.setVisibility(View.VISIBLE);
                                                    detail_flag = "d_CP3";
                                                }
                                            }

                                        } else {
                                            countidleCP3++;
                                            if (isPleaseWaitC3) {
                                                tv_status3.setText("Please Wait...");
                                                status_detailCP3 = "Please Wait...";
                                                btn_p3onff.setVisibility(View.INVISIBLE);

                                            } else {
                                                tv_status3.setText(s8_CP3);
                                                status_detailCP3 = s8_CP3;
                                                counterConnectorC3On=0;
                                                if (sendAvailStatusForC3){
                                                    sendStatusNotificationRequest("3","NoError",
                                                            "Available",sessionNumberC3);
                                                    sendAvailStatusForC3=false;
                                                    stopSendingMeterVals("3");
                                                }

                                                toggleBtn.setEnabled(true);
                                                btn_p2onff.setEnabled(true);
                                                btn_p3onff.setEnabled(true);

                                                 if (status_detailCP1.equals(s8_CP1)
                                                        && status_detailCP2.equals(s8_CP2)
                                                        && status_detailCP3.equals(s8_CP3)) {
                                                    uploadOfflineData();
                                                }

                                                emergency03="0";
                                                overCurrent03="0";
                                                overVolatage03="0";
                                                underVolatage03="0";
                                                isStartSendC3 =true;
                                                isStopSendC3 =true;
                                                transactionIDC3="0";

                                                if (pingHandlerMVC3 != null) {
                                                    pingHandlerMVC3.removeCallbacks(pingRunnableC3);
                                                    pingHandlerMVC3.removeCallbacksAndMessages(null);
                                                }
                                            }

                                            if (isStopC3) {
                                                isAvailableC3 = true;
                                                isStartC3 = true;
                                                isStopC3 = false;
                                            }

                                            rsCP23=rsCP3;
                                            txt_c3round_off_mr.setText("₹ 00.00");
                                            rsPOCP23=rsPOCP3;
                                            rsPOCP3 = "₹ 00.00";
                                            float mrv = 0;
                                            mrsPOCP3 = "" + mrv;
                                            txt_c3diff.setText("00:00:00");
                                            unit_detailCP3 = "00.00";
                                            etime_detailCP3 = "00:00:00";
                                            plugedinCountcp3 = 0;
                                            btn_p3onff.clearAnimation();
                                            if (overlayCountiii > 10) {
                                                isPleaseWaitC3 = false;
                                                relativeLayout3rd.setVisibility(View.VISIBLE);
                                            }
                                            overlayCountiii++;
                                        }
                                    }

                                    if (p3OnOff.equals("1") && p3plugin.equals("0") && p3fault.equals("99")) {
                                        countidleCP3 = 0;
                                        plugedinCountcp3 = 0;
                                        plugedoutCountcp3 = 0;
                                        tv_status3.setText(s1_CP3);
                                        isTappedC3 = false;
                                        isPleaseWaitC3 = false;

                                        if (isStartC3) {
                                            isStartC3 = false;
                                            countC3++;
                                            isStopC3 = true;
                                            isAvailableC3 = false;
                                        }
                                        txt_c3round_off_mr.setText("₹ 00.00");
                                        txt_c3diff.setText("00:00:00");
                                        status_detailCP3 = s1_CP3;
                                        unit_detailCP3 = "00.00";
                                        etime_detailCP3 = "00:00:00";
                                        isStillOnCP3 = false;

                                        counterConnectorC3On++;
                                        Log.e("COUNTERISCHARGING"," C3 --->"+ counterConnectorC3On++);
                                        if (counterConnectorC3On>=400){
                                            relativeLayout3rd.setVisibility(View.VISIBLE);
                                            btn_p3onff.setVisibility(View.GONE);
                                            funConnectoriii();
                                        }else {
                                            relativeLayout3rd.setVisibility(View.GONE);
                                            btn_p3onff.setVisibility(View.VISIBLE);
                                        }

                                        if (displayPlugoutCountC3 == 12) {
                                            if (sPisPoweFail.getisPowerFailCP3().equals("t") && sPisPluggedin.getisPluggedinCP3().equals("t")) {
                                                btn_p3onff.performClick();
                                                sPisPoweFail.setisPowerFailCP3("f");
                                                sPisPluggedin.setisPluggedinCP3("f");
                                                tv_status3.setText(s7_CP3);
                                                status_detailCP3 = s7_CP3;
                                                sPisPoweFail.setisPowerFailCP3("f");
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
                                                float prev_mrCP3 = Float.parseFloat(sPmeterReadingCP3.getMeterReadingCP3());
                                                float current_mrCP3 = 0.0f;
                                                try {
                                                    current_mrCP3 = Float.parseFloat(p3meter);
                                                    int b = (int) current_mrCP3;
                                                    sPmeterReadingCP3RO.setMeterReadingCP3RO("" + b);

                                                    float meterreadingdiff3 = current_mrCP3 - prev_mrCP3;
                                                    float meterreadingdiffP3 = 0.0f;
                                                    meterreadingdiffC3=meterreadingdiff3;

                                                    meterreadingdiffP3 = (current_mrCP3 - prev_mrCP3) * erate;
                                                    String meterreadingCP3 = String.format("%.2f", meterreadingdiff3);
                                                    String meterreadingCP3P = String.format("%.2f", meterreadingdiffP3);
                                                    tv_status3.setText(s10_CP3);
                                                    if (meterreadingdiffP3 > 0 && meterreadingdiffP3 < 200) {
                                                        txt_c3round_off_mr.setText("₹ " + meterreadingCP3P);
                                                        rsCP3 = "₹ " + meterreadingCP3P;
                                                        rsPOCP3 = "₹ " + meterreadingCP3P;
                                                        rsPOCP23 = "₹ " + meterreadingCP3P;
                                                        float mrv = meterreadingdiffP3;
                                                        mrsPOCP3 = "" + mrv;
                                                    }
                                                    txt_c3diff.setText(time_readingCP3);
                                                    status_detailCP3 = s10_CP3;
                                                    unit_detailCP3 = meterreadingCP3;
                                                    etime_detailCP3 = time_readingCP3;
                                                    sPisPoweFail.setisPowerFailCP3("f");
                                                    relativeLayout3rd.setVisibility(View.GONE);
                                                } catch (NumberFormatException e) {
                                                    Log.e("NumberFormat","Excptn "+e);
                                                }
                                            } else if (plugedinCountcp3 == 0) {
                                                //Time reading_________________
                                                StartTimeCP3 = SystemClock.uptimeMillis() + (-sPTimeReadingCP3.getTimeReadingCP3());
                                                //-------------------------meter reading
                                                float prev_mrCP3 = Float.parseFloat(sPmeterReadingCP3.getMeterReadingCP3());
                                                float current_mrCP3 = 0.0f;
                                                try {
                                                    current_mrCP3 = Float.parseFloat(p3meter);
                                                    int b = (int) current_mrCP3;
                                                    sPmeterReadingCP3RO.setMeterReadingCP3RO("" + b);
                                                    float meterreadingdiff3 = current_mrCP3 - prev_mrCP3;
                                                    meterreadingdiffC3=meterreadingdiff3;
                                                    float meterreadingdiffP3 = 0.0f;
                                                    meterreadingdiffP3 = (current_mrCP3 - prev_mrCP3) * erate;
                                                    String meterreadingCP3 = String.format("%.2f", meterreadingdiff3);
                                                    String meterreadingCP3P = String.format("%.2f", meterreadingdiffP3);
                                                    tv_status3.setText(s9_CP3);
                                                    if (meterreadingdiffP3 > 0 && meterreadingdiffP3 < 200) {
                                                        txt_c3round_off_mr.setText("₹ " + meterreadingCP3P);
                                                        rsCP3 = "₹ " + meterreadingCP3P;
                                                        rsPOCP3 = "₹ " + meterreadingCP3P;
                                                        rsPOCP23 = "₹ " + meterreadingCP3P;
                                                        float mrv = meterreadingdiffP3;
                                                        mrsPOCP3 = "" + mrv;
                                                    }
                                                    txt_c3diff.setText("00:00:00");
                                                    status_detailCP3 = s9_CP3;
                                                    unit_detailCP3 = meterreadingCP3;
                                                    etime_detailCP3 = "00:00:00";
                                                    plugedinCountcp3++;
                                                    sPisPluggedin.setisPluggedinCP3("t");
                                                    isResumedAftercp3 = true;
                                                    btn_p3onff.startAnimation(anim);
                                                    relativeLayout3rd.setVisibility(View.GONE);
                                                    isAvailableC3 = false;
                                                } catch (NumberFormatException e) {
                                                    Log.e("NumberFormat5","Excptn "+e);
                                                }
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
                                                float prev_mrCP3 = Float.parseFloat(sPmeterReadingCP3.getMeterReadingCP3());
                                                float current_mrCP3 = 0.0f;
                                                try {
                                                    current_mrCP3 = Float.parseFloat(p3meter);
                                                    int b = (int) current_mrCP3;
                                                    sPmeterReadingCP3RO.setMeterReadingCP3RO("" + b);
                                                    float meterreadingdiff3 = current_mrCP3 - prev_mrCP3;
                                                    float meterreadingdiffP3 = 0.0f;
                                                    meterreadingdiffC3=meterreadingdiff3;
                                                    meterreadingdiffP3 = (current_mrCP3 - prev_mrCP3) * erate;
                                                    String meterreadingCP3 = String.format("%.2f", meterreadingdiff3);
                                                    String meterreadingCP3P = String.format("%.2f", meterreadingdiffP3);
                                                    tv_status3.setText(s9_CP3);
                                                    isAvailableC3 = false;
                                                    if (meterreadingdiffP3 > 0 && meterreadingdiffP3 < 200) {
                                                        txt_c3round_off_mr.setText("₹ " + meterreadingCP3P);
                                                        rsCP3 = "₹ " + meterreadingCP3P;
                                                        rsPOCP3 = "₹ " + meterreadingCP3P;
                                                        float mrv = meterreadingdiffP3;
                                                        mrsPOCP3 = "" + mrv;
                                                    }
                                                    txt_c3diff.setText(time_readingCP3);
                                                    status_detailCP3 = s9_CP3;
                                                    unit_detailCP3 = meterreadingCP3;
                                                    etime_detailCP3 = time_readingCP3;
                                                    plugedinCountcp3++;
                                                    sPisPluggedin.setisPluggedinCP3("t");
                                                    isResumedAftercp3 = true;
                                                    relativeLayout3rd.setVisibility(View.GONE);

                                                    if (isStartSendC3){
                                                        connectorIS="0";
                                                        connectorIS="3";

                                                        if (spotpForCP1.getOTPForP3()!=""){
                                                            sendStartRequest("3",spotpForCP1.getOTPForP3(),
                                                                    p3MeterValue,
                                                                    sessionNumberC3);
                                                            sessionNumberC3OTP=spotpForCP1.getOTPForP3();
                                                        }else {
                                                            sendStartRequest("3",sessionNumberC3OTP,
                                                                    p3MeterValue,
                                                                    sessionNumberC3);
                                                            spotpForCP1.setOTPForCP3(sessionNumberC3OTP);
                                                        }

                                                        isStartSendC3 =false;
                                                        isStopSendC3 =true;
                                                        sendPowerFailStatus=true;
                                                        sendAvailStatusForC3=true;

/*
                                                        new InternetCheck(new InternetCheck.Consumer() {
                                                            @Override
                                                            public void accept(Boolean internet) {
                                                                if (internet){
                                                                    pingHandlerMVC3 = new Handler();
                                                                    pingHandlerMVC3.removeCallbacks(pingRunnableC3);
                                                                    pingHandlerMVC3.removeCallbacksAndMessages(null);
                                                                    pingRunnableC3 = new Runnable() {
                                                                        @Override public void run() {
                                                                            if (spotpForCP1.getTransactonIdP3()!=null
                                                                                    || spotpForCP1.getTransactonIdP3()!="0"){
                                                                                webSocketokhttp.send(JSONObjectsForRequest.sendMeterValuesRequest(getUTC(),"3",voltage_detailCP3,
                                                                                        spotpForCP1.getTransactonIdP3(),"L3",current_detailCP3,String.valueOf(meterreadingdiffC2*1000),generateSessionId()));
                                                                            }else {
                                                                                webSocketokhttp.send(JSONObjectsForRequest.sendMeterValuesRequest(getUTC(),"3",voltage_detailCP3,
                                                                                        transactionIDC3,"L3",current_detailCP3,String.valueOf(meterreadingdiffC2*1000),generateSessionId()));
                                                                            }
                                                                            pingHandlerMVC3.postDelayed(this, 120000);
                                                                        }
                                                                    };
                                                                    pingHandlerMVC3.postDelayed(pingRunnableC3, 120000);
                                                                }
                                                            }
                                                        });
*/


                                                        new InternetCheck(new InternetCheck.Consumer() {
                                                            @Override
                                                            public void accept(Boolean internet) {
                                                                if (internet){

                                                                    pingHandlerMVC3 = new Handler();
                                                                    pingHandlerMVC3.removeCallbacks(pingRunnableC3);
                                                                    pingHandlerMVC3.removeCallbacksAndMessages(null);
                                                                    pingRunnableC3 = new Runnable() {
                                                                        @Override public void run() {
                                                                            new InternetCheck(new InternetCheck.Consumer() {
                                                                                @Override
                                                                                public void accept(Boolean internet) {
                                                                                    if (internet){
                                                                                        if (spotpForCP1.getTransactonIdP3()!=null
                                                                                                || spotpForCP1.getTransactonIdP3()!="0"){
                                                                                            webSocketokhttp.send(JSONObjectsForRequest.sendMeterValuesRequest(getUTC(),"3",voltage_detailCP3,
                                                                                                    spotpForCP1.getTransactonIdP3(),"L3",current_detailCP3,String.valueOf(meterreadingdiffC2*1000),generateSessionId()));
                                                                                        }else {
                                                                                            webSocketokhttp.send(JSONObjectsForRequest.sendMeterValuesRequest(getUTC(),"3",voltage_detailCP3,
                                                                                                    transactionIDC3,"L3",current_detailCP3,String.valueOf(meterreadingdiffC2*1000),generateSessionId()));
                                                                                        }
                                                                                        pingHandlerMVC3.postDelayed(pingRunnableC3, 120000);
                                                                                    }else {
                                                                                        if (pingHandlerMVC3!= null) {
                                                                                            pingHandlerMVC3.removeCallbacks(pingRunnableC3);
                                                                                            pingHandlerMVC3.removeCallbacksAndMessages(null);
                                                                                        }
                                                                                    }
                                                                                }
                                                                            });
                                                                        }
                                                                    };
                                                                    pingHandlerMVC3.postDelayed(pingRunnableC3, 120000);
                                                                }
                                                            }
                                                        });

                                                    }
                                                } catch (NumberFormatException e) {
                                                    Log.e("NumberFormat3","Excptn "+e);
                                                }
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
                                                float prev_mrCP3 = Float.parseFloat(sPmeterReadingCP3.getMeterReadingCP3());
                                                float current_mrCP3 = 0.0f;
                                                try {
                                                    current_mrCP3 = Float.parseFloat(p3meter);
                                                    int b = (int) current_mrCP3;
                                                    meterreadingdiffC3=current_mrCP3;
                                                    sPmeterReadingCP3RO.setMeterReadingCP3RO("" + b);
                                                    float meterreadingdiff3 = current_mrCP3 - prev_mrCP3;
                                                    float meterreadingdiffP3 = 0.0f;
                                                    meterreadingdiffP3 = (current_mrCP3 - prev_mrCP3) * erate;
                                                    meterreadingdiffC3 = Float.parseFloat(String.format("%.2f", current_mrCP3 - prev_mrCP3));
                                                    String meterreadingCP_3 = String.format("%.2f", current_mrCP3 - prev_mrCP3);
                                                    meterreadingdiffC3=Float.parseFloat(meterreadingCP_3);
                                                    String meterreadingCP3 = String.format("%.2f", meterreadingdiff3);
                                                    String meterreadingCP3P = String.format("%.2f", meterreadingdiffP3);

                                                    tv_status3.setText(s10_CP3);
                                                    if (meterreadingdiffP3 > 0 && meterreadingdiffP3 < 200) {
                                                        txt_c3round_off_mr.setText("₹ " + meterreadingCP3P);
                                                        rsCP3 = "₹ " + meterreadingCP3P;
                                                        rsPOCP3 = "₹ " + meterreadingCP3P;
                                                        rsPOCP23 = "₹ " + meterreadingCP3P;
                                                        float mrv = meterreadingdiffP3;
                                                        mrsPOCP3 = "" + mrv;
                                                    }
                                                    txt_c3diff.setText(time_readingCP3);
                                                    status_detailCP3 = s10_CP3;
                                                    unit_detailCP3 = meterreadingCP3;
                                                    etime_detailCP3 = time_readingCP3;
                                                    relativeLayout3rd.setVisibility(View.GONE);
                                                } catch (NumberFormatException e) {
                                                    Log.e("NumberFormat2","Exptn "+e);
                                                }
                                            } else if (plugedinCountcp3 == 0) {
                                                tv_status3.setText(s11_CP3);
                                                isAvailableC3 = false;
                                                txt_c3round_off_mr.setText("₹ 00.00");
                                                txt_c3diff.setText("00:00:00");
                                                status_detailCP3 = s11_CP3;

                                                if (isStartSendC3){
                                                    connectorIS="0";
                                                    connectorIS="3";

                                                    sendStartRequest("3",sessionNumberC3OTP,
                                                            p3MeterValue,
                                                            sessionNumberC3);
                                                    spotpForCP1.setOTPForCP3(sessionNumberC3OTP);

                                                    isStartSendC3 =false;
                                                    isStopSendC3 =true;
                                                    sendPowerFailStatus=true;
                                                    sendAvailStatusForC3=true;

/*
                                                    new InternetCheck(new InternetCheck.Consumer() {
                                                        @Override
                                                        public void accept(Boolean internet) {
                                                            if (internet){
                                                                pingHandlerMVC3 = new Handler();
                                                                pingHandlerMVC3.removeCallbacks(pingRunnableC3);
                                                                pingHandlerMVC3.removeCallbacksAndMessages(null);
                                                                pingRunnableC3 = new Runnable() {
                                                                    @Override public void run() {
                                                                       // webSocketokhttp=openConnection();
                                                                        if (spotpForCP1.getTransactonIdP3()!=null
                                                                                || spotpForCP1.getTransactonIdP3()!="0"){
                                                                            webSocketokhttp.send(JSONObjectsForRequest.sendMeterValuesRequest(getUTC(),"3",voltage_detailCP3,
                                                                                    spotpForCP1.getTransactonIdP3(),"L3",current_detailCP3,String.valueOf(meterreadingdiffC2*1000),generateSessionId()));
                                                                        }else {
                                                                            webSocketokhttp.send(JSONObjectsForRequest.sendMeterValuesRequest(getUTC(),"3",voltage_detailCP3,
                                                                                    transactionIDC3,"L3",current_detailCP3,String.valueOf(meterreadingdiffC2*1000),generateSessionId()));
                                                                        }
                                                                        pingHandlerMVC3.postDelayed(this, 120000);
                                                                    }
                                                                };
                                                                pingHandlerMVC3.postDelayed(pingRunnableC3, 120000);
                                                            }
                                                        }
                                                    });
*/


                                                    new InternetCheck(new InternetCheck.Consumer() {
                                                        @Override
                                                        public void accept(Boolean internet) {
                                                            if (internet){

                                                                pingHandlerMVC3 = new Handler();
                                                                pingHandlerMVC3.removeCallbacks(pingRunnableC3);
                                                                pingHandlerMVC3.removeCallbacksAndMessages(null);
                                                                pingRunnableC3 = new Runnable() {
                                                                    @Override public void run() {
                                                                        new InternetCheck(new InternetCheck.Consumer() {
                                                                            @Override
                                                                            public void accept(Boolean internet) {
                                                                                if (internet){
                                                                                    if (spotpForCP1.getTransactonIdP3()!=null
                                                                                            || spotpForCP1.getTransactonIdP3()!="0"){
                                                                                        webSocketokhttp.send(JSONObjectsForRequest.sendMeterValuesRequest(getUTC(),"3",voltage_detailCP3,
                                                                                                spotpForCP1.getTransactonIdP3(),"L3",current_detailCP3,String.valueOf(meterreadingdiffC2*1000),generateSessionId()));
                                                                                    }else {
                                                                                        webSocketokhttp.send(JSONObjectsForRequest.sendMeterValuesRequest(getUTC(),"3",voltage_detailCP3,
                                                                                                transactionIDC3,"L3",current_detailCP3,String.valueOf(meterreadingdiffC2*1000),generateSessionId()));
                                                                                    }
                                                                                    pingHandlerMVC3.postDelayed(pingRunnableC3, 120000);
                                                                                }else {
                                                                                    if (pingHandlerMVC3!= null) {
                                                                                        pingHandlerMVC3.removeCallbacks(pingRunnableC3);
                                                                                        pingHandlerMVC3.removeCallbacksAndMessages(null);
                                                                                    }
                                                                                }
                                                                            }
                                                                        });
                                                                    }
                                                                };
                                                                pingHandlerMVC3.postDelayed(pingRunnableC3, 120000);
                                                            }
                                                        }
                                                    });

                                                }

                                                unit_detailCP3 = "00.00";
                                                etime_detailCP3 = "00:00:00";
                                                sPisPluggedin.setisPluggedinCP3("t");
                                                //-----------------meter reading
                                                //-------------
                                                plugedinCountcp3++;

                                                //meter reading-------------------------
                                                sPmeterReadingCP3.setMeterReadingCP3(p3meter);
                                                //Time reading_________________
                                                StartTimeCP3 = SystemClock.uptimeMillis() + (-NewBeginMillsCP3);  //--> Start Time
                                                btn_p3onff.startAnimation(anim);

                                            } else {
                                                //time reading______
                                                MillisCP3 = (SystemClock.uptimeMillis() - StartTimeCP3);

                                                HoursCP3 = (int) (MillisCP3 / (1000 * 60 * 60));
                                                MinutesCP3 = (int) (MillisCP3 / (1000 * 60)) % 60;
                                                SecondsCP3 = (int) (MillisCP3 / 1000) % 60;
                                                String time_readingCP3 = "" + String.format("%02d", HoursCP3) + ":"
                                                        + String.format("%02d", MinutesCP3) + ":"
                                                        + String.format("%02d", SecondsCP3);

                                                //-----------meter Reading
                                                float prev_mrCP3 = Float.parseFloat(sPmeterReadingCP3.getMeterReadingCP3());
                                                float current_mrCP3 = 0.0f;
                                                try {
                                                    current_mrCP3 = Float.parseFloat(p3meter);
                                                    int b = (int) current_mrCP3;
                                                    sPmeterReadingCP3RO.setMeterReadingCP3RO("" + b);
                                                    meterreadingdiffC3=current_mrCP3;
                                                    float meterreadingdiff3 = current_mrCP3 - prev_mrCP3;
                                                    float meterreadingdiffP3 = 0.0f;
                                                    meterreadingdiffP3 = (current_mrCP3 - prev_mrCP3) * erate;
                                                    String meterreadingCP3 = String.format("%.2f", meterreadingdiff3);
                                                    meterreadingdiffC3 = Float.parseFloat(String.format("%.2f", current_mrCP3 - prev_mrCP3));
                                                    String meterreadingCP3P = String.format("%.2f", meterreadingdiffP3);

                                                    //--------------------
                                                    tv_status3.setText(s11_CP3);
                                                    isAvailableC3 = false;

                                                    if (meterreadingdiffP3 > 0 && meterreadingdiffP3 < 200) {
                                                        txt_c3round_off_mr.setText("₹ " + meterreadingCP3P);
                                                        rsCP3 = "₹ " + meterreadingCP3P;
                                                        rsPOCP3 = "₹ " + meterreadingCP3P;
                                                        rsPOCP23 = "₹ " + meterreadingCP3P;//------------
                                                        float mrv = meterreadingdiffP3;
                                                        mrsPOCP3 = "" + mrv;
                                                        //-----------
                                                    }
                                                    txt_c3diff.setText(time_readingCP3);
                                                    status_detailCP3 = s11_CP3;
                                                    unit_detailCP3 = meterreadingCP3;
                                                    etime_detailCP3 = time_readingCP3;
                                                    sPisPluggedin.setisPluggedinCP3("t");
                                                    plugedinCountcp3++;
                                                } catch (NumberFormatException e) {
                                                    Log.e("NumberFormat","Exptn "+e);
                                                }
                                            }
                                        }
                                    }

                                    switch (detail_flag) {
                                        case "d_CP1":
                                            display_details(rsCP21, s15_CP1 + " #01", voltage_detailCP1, current_detailCP1, power_detailCP1, unit_detailCP21, etime_detailCP21, status_detailCP1, s14_CP1, s17_CP1, " ₹ " + erate_s + " " + s16_CP1, sPlanguageCP1.getlanguageCP1());
                                            break;
                                        case "d_CP2":
                                            display_details(rsCP22, s15_CP2 + " #02", voltage_detailCP2, current_detailCP2, power_detailCP2, unit_detailCP22, etime_detailCP22, status_detailCP2, s14_CP2, s17_CP2, " ₹ " + erate_s + " " + s16_CP2, sPlanguageCP2.getlanguageCP2());
                                            break;
                                        case "d_CP3":
                                            display_details(rsCP23, s15_CP3 + " #03", voltage_detailCP3, current_detailCP3, power_detailCP3, unit_detailCP23, etime_detailCP23, status_detailCP3, s14_CP3, s17_CP3, " ₹ " + erate_s + " " + s16_CP3, sPlanguageCP3.getlanguageCP3());
                                            break;
                                    }

                                   /* Log.e("COMMING STRING",
                                            "\n\np1OnOff :" + p1OnOff + "\tp1plugin :" + p1plugin + "\tp1fault :" + p1fault + "\tp1voltage :" + p1voltage + "\tp1current :" + p1current + "\tp1meter :" + p1meter +
                                            "\n\np2OnOff : " + p2OnOff + "\tp2plugin : " + p2plugin + "\tp2fault : " + p2fault + "\tp2voltage : " + p2voltage + "\tp2current : " + p2current + "\tp2meter : " + p2meter +
                                            "\n\np3OnOff : " + p3OnOff + "\tp3plugin : " + p3plugin + "\tp3fault : " + p3fault + "\tp3voltage: " + p3voltage + "\tp3current : " + p3current + "\tp3meter : " + p3meter);*/

                                    //if (layout_pleasewait!=null){
                                        if (layout_pleasewait.getVisibility()==View.VISIBLE){
                                            layout_pleasewait.setVisibility(View.GONE);
                                            layout_main.setVisibility(View.VISIBLE);
                                        }
                                    //}

                                    if (layout_main.getVisibility()==View.GONE){
                                        layout_main.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                        } catch (StringIndexOutOfBoundsException e) {
                            Log.e("IndexOutOfBounds","Exptn "+e);
                            Log.e("CHECKINTERNETNEW","Exptn "+e);
                            if (layout_pleasewait!=null){
                                if (layout_pleasewait.getVisibility()==View.VISIBLE){
                                    layout_pleasewait.setVisibility(View.GONE);
                                    layout_main.setVisibility(View.VISIBLE);
                                }
                            }

                            if (layout_main.getVisibility()==View.GONE){
                                layout_pleasewait.setVisibility(View.GONE);
                                layout_main.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            }
        } else {
            tv_status1.setText(s6_CP1);
            String s = globalP1meter;
            float v = Float.parseFloat(s);
            sPsavemoneyafterpfCP1.setMeterReadingCP1RO(mrsPOCP1);
            tv_status2.setText(s6_CP2);
            String s2 = globalP2meter;
            float v2 = Float.parseFloat(s2);
            tv_status3.setText(s6_CP3);
            String s3 = globalP3meter;
            float v3 = Float.parseFloat(s3);
            toggleBtn.setCardBackgroundColor(Color.parseColor(red));
            toggleBtn.startAnimation(anim);
            btn_p2onff.setCardBackgroundColor(Color.parseColor(red));
            btn_p2onff.startAnimation(anim);
            btn_p3onff.setCardBackgroundColor(Color.parseColor(red));
            btn_p3onff.startAnimation(anim);

            if (sPisPluggedin.getisPluggedinCP1().equals("t")) {
                sPisPoweFail.setisPowerFailCP1("f");
                sPTimeReadingCP1.setTimeReadingCP1(MillisCP1);
            }
            if (sPisPluggedin.getisPluggedinCP2().equals("t")) {
                sPisPoweFail.setisPowerFailCP2("f");
                sPTimeReadingCP2.setTimeReadingCP2(MillisCP2);
            }
            if (sPisPluggedin.getisPluggedinCP3().equals("t")) {
                sPisPoweFail.setisPowerFailCP3("f");
                sPTimeReadingCP3.setTimeReadingCP3(MillisCP3);
            }
        }
        isMainthreadend = true;
    }

    private void uploadOfflineData() {
        uploadingCall++;

        if (uploadingCall==300){
            uploadingCall=0;
            /*final Handler handler1 = new Handler();
            handler1.removeCallbacksAndMessages("null");*/
            Log.e("CHECKINTERNETNEW"," 1 UPLOADING--> "+isNetworkConnected());
            if (isNetworkConnected()){
                new InternetCheck(new InternetCheck.Consumer() {
                    @Override
                    public void accept(Boolean internet) {
                        if (internet){
                            /*handler1.postDelayed(new Runnable() {
                                @Override
                                public void run() {*/

                            uploadingCall++;
                            final Cursor cursor = dbManager.fetchSEC();
                            Log.e("CHECKINTERNETNEW"," cursor1stCount--> "+cursor.getCount());

                            try {
                                Log.e("CHECKINTERNETNEW"," 2 UPLOADING--> "+isNetworkConnected());

                                if (cursor.getCount()>0){
                                    Log.e("CHECKINTERNETNEW"," 3 UPLOADINGSTART--> "+isNetworkConnected());

                                    uploadingCall++;
                                    if (cursor.moveToFirst()){
                                        webSocketokhttp=openConnection();

                                        do{
                                            final String data = cursor.getString(cursor.getColumnIndex(DatabaseHelper.REQUEST_STRING));
                                            final String tId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TRANSACTION_ID));
                                            final String id = cursor.getString(cursor.getColumnIndex(DatabaseHelper._ID));
                                            transactionIdOFFLINE=tId;
                                            Cursor cursor1 = dbManager.fetchTHRD(tId);
                                            Log.e("CHECKINTERNETNEW"," cursor1--> "+cursor.getCount());

                                            webSocketokhttp.send(data);
                                            if (dbManager.update(Long.parseLong(id),data,"False",tId)>0) {
                                                Log.e("IFSTARTFOUND"," --> UPDATED");
                                            }

                                            try{
                                                if (cursor1.getCount()>0){
                                                    // webSocketokhttp=openConnection();
                                                    do {
                                                        if (!cursor1.getString(cursor1.
                                                                getColumnIndex(DatabaseHelper.REQUEST_STRING))
                                                                .contains("StartTransaction")){
                                                            listMapOtherRequests.add(cursor1.getString(
                                                                    cursor1.getColumnIndex(DatabaseHelper.REQUEST_STRING)));
                                                            hashMapSTOPRequests.put(transactionIdOFFLINE,
                                                                    cursor1.getString(cursor1.
                                                                            getColumnIndex(DatabaseHelper.REQUEST_STRING)));
                                                            if (dbManager.update(Long.parseLong(cursor1.getString(cursor1.getColumnIndex(DatabaseHelper._ID))),
                                                                    cursor1.getString(cursor1.getColumnIndex(DatabaseHelper.REQUEST_STRING)),"False",cursor1.getString(cursor1.getColumnIndex(DatabaseHelper.TRANSACTION_ID)))>0) {

                                                                if (layout_pleasewait.getVisibility()!=View.VISIBLE){
                                                                    txt_pleasewait.setText("Uploading Data Please Wait...!");
                                                                    layout_pleasewait.setVisibility(View.VISIBLE);
                                                                    //layout_main.setVisibility(View.GONE);
                                                                }

                                                            }else {
                                                                layout_pleasewait.setVisibility(View.GONE);
                                                                layout_main.setVisibility(View.VISIBLE);
                                                            }
                                                        }
                                                    }while (cursor1.moveToNext());
                                                }


                                            }catch (Exception e){
                                                cursor1.close();
                                            }finally {
                                                if (cursor1!=null){
                                                    cursor1.close();
                                                }
                                            }

                                        }while(cursor.moveToNext());
                                    }
                                }else {
                                    webSocketokhttp=openConnection();
                                    Cursor cursor2 = dbManager.fetch(webSocketokhttp);
                                    Log.e("CHECKINTERNETNEW"," 4 UPLOADING--> "+cursor2.getCount());
                                    Log.e("CHECKINTERNETNEW"," 4 cursor2--> "+cursor2.getCount());


                               /* if (cursor.moveToFirst()) {
                                    do {
                                        String data2=cursor.getString(cursor.getColumnIndex(DatabaseHelper.REQUEST_STRING));
                                        String tId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TRANSACTION_ID));
                                        String id = cursor.getString(cursor.getColumnIndex(DatabaseHelper._ID));
                                        Log.e("DATAISSSSNIK"," data2---> "+data2);
                                        Log.e("DATAISSSSNIK"," tId---> "+tId);
                                        Log.e("DATAISSSSNIK"," id---> "+id);

                                    } while (cursor.moveToNext());
                                }*/

/*
                                try{
                                    //webSocketokhttp=openConnection();
                                    Log.e("CHECKINTERNETNEW"," 4 IFcursor2--> "+cursor2.getCount());
                                    if (cursor2.getCount()>0){
                                        if (!cursor.moveToFirst()){
                                            Log.e("CHECKINTERNETNEW"," HAVEVALS--> "+cursor2.getString(cursor2.getColumnIndex(DatabaseHelper.REQUEST_STRING)));
                                            do {
                                                String data2=cursor2.getString(cursor2.getColumnIndex(DatabaseHelper.REQUEST_STRING));
                                                final String tId = cursor2.getString(cursor2.getColumnIndex(DatabaseHelper.TRANSACTION_ID));
                                                final String id = cursor2.getString(cursor2.getColumnIndex(DatabaseHelper._ID));
                                                transactionIdOFFLINE=tId;

                                                webSocketokhttp=openConnection();
                                                webSocketokhttp.send(cursor2.getString(cursor2
                                                        .getColumnIndex(DatabaseHelper.REQUEST_STRING)));

                                                Log.e("CHECKINTERNETNEW"," TOUPLOADIF123--> "+cursor2.getString(cursor2.getColumnIndex(DatabaseHelper.REQUEST_STRING)));

                                                if (dbManager.update(Long.parseLong(id),data2,"False",tId)>0) {

                                                    if (layout_pleasewait.getVisibility()!=View.VISIBLE){
                                                        txt_pleasewait.setText("Uploading Data Please Wait...!");
                                                        layout_pleasewait.setVisibility(View.VISIBLE);
                                                        //layout_main.setVisibility(View.GONE);
                                                    }
                                                }else {
                                                    layout_pleasewait.setVisibility(View.GONE);
                                                    layout_main.setVisibility(View.VISIBLE);
                                                }


                                            }while (cursor2.moveToNext());
                                            uploadingCall=0;
                                        }else {
                                            Log.e("CHECKINTERNETNEW"," 4 NODATA--> "+cursor2.getCount());

                                        }

                                    }else {
                                        // Log.e("INSERTINTODB"," NOVALS--> "+cursor2.getString(cursor2.getColumnIndex(DatabaseHelper.REQUEST_STRING)));

                                        layout_pleasewait.setVisibility(View.GONE);
                                        layout_main.setVisibility(View.VISIBLE);
                                    }
                                }catch (Exception e){
                                    Log.e("CHECKINTERNETNEW"," 4 ELSEcursor2--> "+cursor2.getCount());
                                    Log.e("CHECKINTERNETNEW"," 4 ELSEEXP2--> "+e);

                                    cursor2.close();
                                }finally {
                                    if (cursor2!=null){
                                        cursor2.close();
                                    }
                                }
*/
                                }
                            }catch (Exception e){
                                Log.e("CHECKINTERNETNEW"," Exception UPLOADING--> "+e);

                                if (cursor!=null){
                                    cursor.close();
                                }
                                        /*if (handler1!=null){
                                            handler1.removeCallbacksAndMessages("ashfsa");
                                        }*/
                                e.getLocalizedMessage();
                            }finally {
                                if (cursor!=null){
                                    cursor.close();
                                }
                            /*if (handler1!=null){
                                handler1.removeCallbacksAndMessages("ashfsa");
                            }*/
                            }
                               /* }
                            }, 5000);*/

                        }else {
                            Log.e("CHECKINTERNETNEW"," 5 UPLOADING--> "+isNetworkConnected());

                            // uploadingCall=0;
                            //cardViewForNetwork.setCardBackgroundColor(Color.parseColor("#FF1000"));
                        }
                    }
                });
            }

        }



    }


    //------------Method Sending Authorization Request-----------//
    private void sendAuthorizationRequest(final String enteredOTP,
                                          String sessionNumber,final String connectorId) {
        uploadingCalledFlag=true;
        checkAuth=true;
        sessionNumber=generateSessionId();
        final String finalSessionNumber = sessionNumber;

        Log.e("CHECKINTERNETNEW"," sendAuthorizationRequest CID --> "+connectorId);


        new InternetCheck(new InternetCheck.Consumer() {
            @Override
            public void accept(Boolean internet) {
                if (internet){
                    dbManager=new DBManager(OCPPWithOkHttpActivity.this);
                    dbManager.open();

                    String LocalAuthStatusIs= dbManager.fetchLocalList(enteredOTP);
                    Log.e("CURSORCOUNT","STATUS --> "+LocalAuthStatusIs);
                    Log.e("CHECKINTERNETNEW","STATUS --> "+LocalAuthStatusIs);
                    if (LocalAuthStatusIs.equals("Accepted")){
                        switch (connectorId) {
                            case "1":
                                funConnectori();
                                break;
                            case "2":
                                funConnectorii();
                                break;
                            case "3":
                                funConnectoriii();
                                break;
                        }
                        dismissProgressDialog();
                    }else {
                        webSocketokhttp=openConnection();
                        webSocketokhttp.send(JSONObjectsForRequest.sendAuthorizeRequest(
                                enteredOTP, finalSessionNumber));
                    }
                }else {
                    String LocalAuthStatusIs= dbManager.fetchLocalList(edt_OTP0.getText().toString());
                    Log.e("CURSORCOUNT","STATUS --> "+LocalAuthStatusIs);
                    Log.e("CHECKINTERNETNEW","STATUS --> "+LocalAuthStatusIs);

                    if (LocalAuthStatusIs.equals("Accepted")){
                    //if (enteredOTP.equals("0000")){

                        switch (connectorIS) {
                            case "1":
                                funConnectori();
                                break;
                            case "2":
                                funConnectorii();
                                break;
                            case "3":
                                funConnectoriii();
                                break;
                        }
                        dismissProgressDialog();
                    }else {
                        edt_OTP0.setError("It seems like you have entered "+LocalAuthStatusIs+" OTP please try with valid one!");
                        if (progressBar!=null /*&& progressDialog.isShowing()*/){
                            progressBar.dismiss();
                        }
                        //kioskProgressDialog.dismiss();
                    }
                }
            }
        });

        isAuthReq=sessionNumber;
        _OtpIs=enteredOTP;
    }

    //------------Method Sending Start Request-----------//
    private void sendStartRequest(final String connectorID,
                                  final String _OtpIs,
                                  final float meterreadingdiffC,
                                  String s) {
        reasonC="Local";
        _CMSCommand="NONE";
        final String tranID =generateTransactionId();

        new InternetCheck(new InternetCheck.Consumer() {
            @Override
            public void accept(Boolean internet) {
                if (internet){
                    switch (connectorID) {
                        case "1":
                            isInternetAvailableForC1=internet;
                            isInternetINITForC1=internet;
                            break;
                        case "2":
                            isInternetAvailableForC2=internet;
                            isInternetINITForC2=internet;
                            break;
                        case "3":
                            isInternetAvailableForC3=internet;
                            isInternetINITForC3=internet;
                            break;
                    }
                    webSocketokhttp=openConnection();
                    webSocketokhttp.send(JSONObjectsForRequest.sendStartTransactionRequest(connectorID,
                            _OtpIs, String.valueOf(meterreadingdiffC),getUTC(),generateSessionId()));

                }else {
                    switch (connectorID) {
                        case "1":
                            transactionIDC1 = tranID;
                            spotpForCP1.setTransactonIdCP1(transactionIDC1);
                            spotpForCP1.setOTPForCP1(sessionNumberC1OTP);
                            isInternetAvailableForC1=internet;
                            isInternetAvailableForC2=internet;
                            isInternetAvailableForC3=internet;
                            isInternetINITForC1=internet;
                            break;
                        case "2":
                            transactionIDC2 = tranID;
                            spotpForCP1.setTransactonIdCP2(transactionIDC2);
                            spotpForCP1.setOTPForCP2(sessionNumberC2OTP);
                            isInternetAvailableForC1=internet;
                            isInternetAvailableForC2=internet;
                            isInternetAvailableForC3=internet;
                            isInternetINITForC2=internet;
                            break;
                        case "3":
                            transactionIDC3 = tranID;
                            spotpForCP1.setTransactonIdCP3(transactionIDC3);
                            spotpForCP1.setOTPForCP3(sessionNumberC3OTP);
                            isInternetAvailableForC1=internet;
                            isInternetAvailableForC2=internet;
                            isInternetAvailableForC3=internet;
                            isInternetINITForC3=internet;
                            break;
                    }
                    dbManager.insert(JSONObjectsForRequest.sendStartTransactionRequest(connectorID,
                            _OtpIs, String.valueOf(meterreadingdiffC),getUTC(),generateSessionId()),"START","True",tranID);
                }

                sendStatusNotificationRequest(connectorID,"No Error","Charging",generateSessionId());

            }
        });
    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm != null) {
            return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
        }else {
            return false;
        }
    }


    //------------Method Sending Stop Request-----------//
    private void sendStopRequest(final String connectorID,
                                 final float meterreadingdiffC,
                                 final String finalTransactionIDC,
                                 final String sessionNumberCOTP, String s) {

        final SharedPreferences tranIdPref=getSharedPreferences("TRNID",MODE_PRIVATE);

        Log.e("STOPREQUESTPARAMS",
                "CID -> "+connectorID+" TRNID -> "+finalTransactionIDC);

        Log.e("STOPREQUESTPARAMS",
                "CID -> "+connectorID+" SF TRNID -> "+tranIdPref.getString(connectorID,"000"));

        Log.e("CHECKINTERNETNEW",
                "STOPTRANSACTION ID"+finalTransactionIDC);

        new InternetCheck(new InternetCheck.Consumer() {
            @Override
            public void accept(Boolean internet) {
                if (internet){

                   /* webSocketokhttp=openConnection();*/
                    switch (connectorID) {
                        case "1":
                            if (isInternetAvailableForC1){
                                Log.e("CHECKINTERNETNEW",
                                        "IFSECSTOP"+tranIdPref.getString(connectorID,"000"));
                                if (!tranIdPref.getString(connectorID,"000").equals("000")){
                                    Log.e("CHECKINTERNETNEW",
                                            "IFSEC2STOP"+tranIdPref.getString(connectorID,"000"));
                                    webSocketokhttp.send(JSONObjectsForRequest.
                                            sendStopTrasactionRequest(generateSessionId(),sessionNumberC1OTP,
                                            String.valueOf(meterreadingdiffC),Integer.
                                                            parseInt(tranIdPref.getString(connectorID,"000")),getUTC(),
                                            reasonC,"L"+connectorID));
                                }else {
                                    Log.e("CHECKINTERNETNEW",
                                            "ELSESEC2STOP"+tranIdPref.getString(connectorID,"000"));
                                    webSocketokhttp.send(JSONObjectsForRequest.sendStopTrasactionRequest(generateSessionId(),sessionNumberC1OTP,
                                            String.valueOf(meterreadingdiffC),Integer.parseInt(transactionIDC1),getUTC(),
                                            reasonC,"L"+connectorID));
                                }
                            }else {
                                Log.e("CHECKINTERNETNEW",
                                        "ELSESEC0STOP"+tranIdPref.getString(connectorID,"000"));
                                if (!tranIdPref.getString(connectorID,"000").equals("000")){
                                    Log.e("CHECKINTERNETNEW",
                                            "ELSESEC01IFSTOP"+tranIdPref.getString(connectorID,"000"));
                                    dbManager.insert(JSONObjectsForRequest.sendStopTrasactionRequest(generateSessionId(),sessionNumberC1OTP,
                                            String.valueOf(meterreadingdiffC),Integer.
                                                    parseInt(tranIdPref.getString(connectorID,"000")),getUTC(),
                                            reasonC,"L"+connectorID),"STOP",
                                            "True", tranIdPref.getString(connectorID,"000"));
                                }else {
                                    Log.e("CHECKINTERNETNEW",
                                            "ELSESEC02ELSESTOP"+tranIdPref.getString(connectorID,"000"));
                                    dbManager.insert(JSONObjectsForRequest.
                                            sendStopTrasactionRequest(generateSessionId(),sessionNumberC1OTP,
                                            String.valueOf(meterreadingdiffC),
                                                    Integer.parseInt(transactionIDC1),getUTC(),
                                            reasonC,"L"+connectorID),"STOP","True",transactionIDC1);
                                }
                            }
                            break;
                        case "2":
                            if (isInternetAvailableForC2){
                                if (!tranIdPref.getString(connectorID,"000").equals("000")){
                                    webSocketokhttp.send(JSONObjectsForRequest.sendStopTrasactionRequest(generateSessionId(),sessionNumberC2OTP,
                                            String.valueOf(meterreadingdiffC),Integer.
                                                    parseInt(tranIdPref.getString(connectorID,"000")),getUTC(),
                                            reasonC,"L"+connectorID));
                                }else {
                                    webSocketokhttp.send(JSONObjectsForRequest.sendStopTrasactionRequest(generateSessionId(),sessionNumberC2OTP,
                                            String.valueOf(meterreadingdiffC),Integer.parseInt(transactionIDC2),getUTC(),
                                            reasonC,"L"+connectorID));
                                }
                            }else {
                                if (tranIdPref.getString(connectorID,"000").equals("")){
                                    dbManager.insert(JSONObjectsForRequest.sendStopTrasactionRequest(generateSessionId(),sessionNumberC2OTP,
                                            String.valueOf(meterreadingdiffC),Integer.parseInt(tranIdPref.getString(connectorID,"000")),getUTC(),
                                            reasonC,"L"+connectorID),"STOP","True", tranIdPref.getString(connectorID,"000"));
                                }else {
                                    dbManager.insert(JSONObjectsForRequest.sendStopTrasactionRequest(generateSessionId(),sessionNumberC2OTP,
                                            String.valueOf(meterreadingdiffC),Integer.parseInt(transactionIDC2),getUTC(),
                                            reasonC,"L"+connectorID),"STOP","True",transactionIDC2);
                                }
                            }
                            break;
                        case "3":
                            if (isInternetAvailableForC3){
                                if (!tranIdPref.getString(connectorID,"000").equals("000")){
                                    webSocketokhttp.send(JSONObjectsForRequest.sendStopTrasactionRequest(generateSessionId(),sessionNumberC3OTP,
                                            String.valueOf(meterreadingdiffC),Integer.parseInt(tranIdPref.getString(connectorID,"000")),getUTC(),
                                            reasonC,"L"+connectorID));
                                }else {
                                    webSocketokhttp.send(JSONObjectsForRequest.sendStopTrasactionRequest(generateSessionId(),sessionNumberC3OTP,
                                            String.valueOf(meterreadingdiffC),
                                            Integer.parseInt(transactionIDC3),getUTC(),
                                            reasonC,"L"+connectorID));
                                }
                            }else {
                                if (!tranIdPref.getString(connectorID,"000").equals("000")){
                                    dbManager.insert(JSONObjectsForRequest.sendStopTrasactionRequest(generateSessionId(),sessionNumberC3OTP,
                                            String.valueOf(meterreadingdiffC),Integer.parseInt(tranIdPref.getString(connectorID,"000")),getUTC(),
                                            reasonC,"L"+connectorID),"STOP","True", tranIdPref.getString(connectorID,"000"));
                                }else {
                                    dbManager.insert(JSONObjectsForRequest.sendStopTrasactionRequest(generateSessionId(),sessionNumberC3OTP,
                                            String.valueOf(meterreadingdiffC),Integer.parseInt(transactionIDC3),getUTC(),
                                            reasonC,"L"+connectorID),"STOP","True",transactionIDC3);
                                }
                            }
                            break;
                    }
                   sendStatusNotificationRequest(connectorID,"No Error",
                           "Finishing",generateSessionId());
                }else {
                    switch (connectorID) {
                        case "1":
                            if (!tranIdPref.getString(connectorID,"000").equals("000")){
                                dbManager.insert(JSONObjectsForRequest.sendStopTrasactionRequest(generateSessionId(),sessionNumberC1OTP,
                                        String.valueOf(meterreadingdiffC),Integer.parseInt(tranIdPref.getString(connectorID,"000")),getUTC(),
                                        reasonC,"L"+connectorID),"STOP","True", tranIdPref.getString(connectorID,"000"));
                            }else {
                                dbManager.insert(JSONObjectsForRequest.sendStopTrasactionRequest(generateSessionId(),sessionNumberC1OTP,
                                        String.valueOf(meterreadingdiffC),Integer.parseInt(transactionIDC1),getUTC(),
                                        reasonC,"L"+connectorID),"STOP","True",transactionIDC1);
                            }

                            break;
                        case "2":
                            if (!tranIdPref.getString(connectorID,"000").equals("000")){
                                dbManager.insert(JSONObjectsForRequest.sendStopTrasactionRequest(generateSessionId(),sessionNumberC2OTP,
                                        String.valueOf(meterreadingdiffC),Integer.parseInt(tranIdPref.getString(connectorID,"000")),getUTC(),
                                        reasonC,"L"+connectorID),"STOP","True", tranIdPref.getString(connectorID,"000"));
                            }else {
                                dbManager.insert(JSONObjectsForRequest.sendStopTrasactionRequest(generateSessionId(),sessionNumberC2OTP,
                                        String.valueOf(meterreadingdiffC),Integer.parseInt(transactionIDC2),getUTC(),
                                        reasonC,"L"+connectorID),"STOP","True",transactionIDC2);
                            }
                            break;
                        case "3":
                            if (!tranIdPref.getString(connectorID,"000").equals("000")){
                                dbManager.insert(JSONObjectsForRequest.sendStopTrasactionRequest(generateSessionId(),sessionNumberC3OTP,
                                        String.valueOf(meterreadingdiffC),Integer.parseInt(tranIdPref.getString(connectorID,"000")),getUTC(),
                                        reasonC,"L"+connectorID),"STOP","True", tranIdPref.getString(connectorID,"000"));
                            }else {
                                dbManager.insert(JSONObjectsForRequest.sendStopTrasactionRequest(generateSessionId(),sessionNumberC3OTP,
                                        String.valueOf(meterreadingdiffC),Integer.parseInt(transactionIDC3),getUTC(),
                                        reasonC,"L"+connectorID),"STOP","True",transactionIDC3);
                            }
                            break;
                    }
                    sendStatusNotificationRequest(connectorID,"NoError","Finishing",generateSessionId());
                }
            }
        });

        Log.e("STOPREQUESTPARAMS",
                "REQUEST -> "+webSocketokhttp.queueSize());

    }


    //------------Method Sending Status Notfctn Request-----------//
    private void sendStatusNotificationRequest(final String connectorID, final String errorCode,
                                               final String _Status,
                                               String _SessionId) {
        _SessionId=generateSessionId();
        final String final_SessionId = _SessionId;

        new InternetCheck(new InternetCheck.Consumer() {
            @Override
            public void accept(Boolean internet) {
                if (internet){
                    webSocketokhttp=openConnection();
                    switch (connectorID) {
                        case "1":
                            if (isInternetAvailableForC1){
                                webSocketokhttp.send(JSONObjectsForRequest.sendStatusNotificationRequest(connectorID,
                                        errorCode,_Status,getUTC(), final_SessionId));
                            }else {
                                dbManager.insert(JSONObjectsForRequest.sendStatusNotificationRequest(connectorID,
                                        errorCode, _Status, getUTC(), final_SessionId),"STATUS","True", transactionIDC1);
                            }
                            break;
                        case "2":
                            if (isInternetAvailableForC2) {

                                webSocketokhttp.send(JSONObjectsForRequest.sendStatusNotificationRequest(connectorID,
                                        errorCode,_Status,getUTC(), final_SessionId));
                            }else {
                                dbManager.insert(JSONObjectsForRequest.sendStatusNotificationRequest(connectorID,
                                        errorCode, _Status, getUTC(), final_SessionId), "STATUS", "True", transactionIDC2);
                            }
                            break;
                        case "3":
                            if (isInternetAvailableForC3) {

                                webSocketokhttp.send(JSONObjectsForRequest.sendStatusNotificationRequest(connectorID,
                                        errorCode,_Status,getUTC(), final_SessionId));
                            }else {
                                dbManager.insert(JSONObjectsForRequest.sendStatusNotificationRequest(connectorID,
                                        errorCode, _Status, getUTC(), final_SessionId), "STATUS", "True", transactionIDC3);
                            }
                            break;
                    }
                }else {
                    switch (connectorID) {
                        case "1":
                            dbManager.insert(JSONObjectsForRequest.sendStatusNotificationRequest(connectorID,
                                    errorCode, _Status, getUTC(), final_SessionId),"STATUS","True", transactionIDC1);
                            break;
                        case "2":
                            dbManager.insert(JSONObjectsForRequest.sendStatusNotificationRequest(connectorID,
                                    errorCode, _Status, getUTC(), final_SessionId), "STATUS", "True", transactionIDC2);
                            break;
                        case "3":
                            dbManager.insert(JSONObjectsForRequest.sendStatusNotificationRequest(connectorID,
                                    errorCode, _Status, getUTC(), final_SessionId), "STATUS", "True", transactionIDC3);
                            break;
                    }
                }
            }
        });

        if (connectorID.equals("0")){
            isStatusZeroSend=_SessionId;
        }

    }

    //------------Method To Stop Sending Meter Values After Any Fault Message
    private void stopSendingMeterVals(String stopMVFor) {
        switch (stopMVFor) {
            case "1":
                if (pingHandlerMVC1 != null) {
                    pingHandlerMVC1.removeCallbacks(pingRunnableC1);
                    pingHandlerMVC1.removeCallbacksAndMessages(null);
                }
                break;
            case "2":
                if (pingHandlerMVC2 != null) {
                    pingHandlerMVC2.removeCallbacks(pingRunnableC2);
                    pingHandlerMVC2.removeCallbacksAndMessages(null);
                }
                break;
            case "3":
                if (pingHandlerMVC3 != null) {
                    pingHandlerMVC3.removeCallbacks(pingRunnableC3);
                    pingHandlerMVC3.removeCallbacksAndMessages(null);
                }
                break;
            default:
                if (pingHandlerMVC1 != null) {
                    pingHandlerMVC1.removeCallbacks(pingRunnableC1);
                    pingHandlerMVC1.removeCallbacksAndMessages(null);
                }

                if (pingHandlerMVC2 != null) {
                    pingHandlerMVC2.removeCallbacks(pingRunnableC2);
                    pingHandlerMVC2.removeCallbacksAndMessages(null);
                }

                if (pingHandlerMVC3 != null) {
                    pingHandlerMVC3.removeCallbacks(pingRunnableC3);
                    pingHandlerMVC3.removeCallbacksAndMessages(null);
                }
                break;
        }
    }

    //------Method To Create And Open Web Socket Connection__----//
    private WebSocket openConnection() {
        if (sharedPreferenceIPAddress!=null){
            if (sharedPreferenceIPAddress.getIPaddress(OCPPWithOkHttpActivity.this)!=null
                    && sharedPreferenceIPAddress.getOCPPId(OCPPWithOkHttpActivity.this)!=null ){
                requestWS = new Request.Builder().url(sharedPreferenceIPAddress.getIPaddress(OCPPWithOkHttpActivity.this)
                        +"/"+sharedPreferenceIPAddress.getOCPPId(OCPPWithOkHttpActivity.this)).build();
            }else {
                Toast.makeText(getApplicationContext(),"IP Address Or Port Number Missing",Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(getApplicationContext(),"IP Address Or Port Number Missing",Toast.LENGTH_SHORT).show();
        }

        listenerWS = new EchoWebSocketListener();
        OkHttpClient okHttpClient = new OkHttpClient();
        webSocketokhttp = okHttpClient.newWebSocket(requestWS, listenerWS);
        okHttpClient.dispatcher().executorService().shutdown();
        return webSocketokhttp;
    }

    private String getUTC(){
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        DateFormat df = DateFormat.getTimeInstance();
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("gmt"));
        String gmtTime = simpleDateFormat.format(new Date());
        Log.e("TIMEUTCIS"," --> "+gmtTime);
        //return simpleDateFormat.format(new Date());
        return simpleDateFormat.format(new Date());
    }

    public void layoutback_fun(){
        String C_ID=cid.toString();
//Create the bundle
        Bundle bundle = new Bundle();
//Add your data to bundle
        bundle.putString("Cid", C_ID);
//Add the bundle to the intent
        if (bluetooth.isConnected()){
            bluetooth.disable();
        }
//Fire that second activity
        startActivity(new Intent(OCPPWithOkHttpActivity.this, LangSelection.class).putExtras(bundle));
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Log.e("ISCCCCCCCCCCCCCC"," - -> "+isConnected);

        if (isConnected){
            cardViewForNetwork.setCardBackgroundColor(Color.parseColor(green));
        }else {
            cardViewForNetwork.setCardBackgroundColor(Color.parseColor(red));
        }
    }

    //Makes Here
    private final class EchoWebSocketListener extends WebSocketListener {

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            super.onClosed(webSocket, code, reason);
            Log.e("CHECKINTERNETNEW"," onClosed--> "+reason);
        }

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            Log.e("CHECKINTERNETNEW"," onOpen--> "+response);
        }

        @Override
        public void onMessage(WebSocket webSocket, String response) {
            Log.e("PARSEMeSSAGE "," --> "+response);
            Log.e("CHECKINTERNETNEW"," onMessage--> "+response);
            Log.e("CHECKINTERNETNEW"," onMessage CID --> "+connectorIS);
            Log.e("TRAINIDCONNECTOR","CID -> "+connectorIS);
            try {
                final JSONArray jsonArray = new JSONArray(response);
                if (jsonArray.getString(2)!=null){
                    switch (jsonArray.getString(2)) {
                        case "RemoteStartTransaction":
                            _CMSCommand = jsonArray.getString(2);
                            break;
                        case "RemoteStopTransaction":
                            _CMSCommand = jsonArray.getString(2);
                            break;
                        case "SendLocalList":
                            String versionNo = null,
                                    idTag = null,
                                    pIdTag = null, status_ = null,
                                    type_ = null, expiry = null;
                            if (jsonArray.getJSONObject(3) != null) {
                                JSONObject jsonOLocalList = jsonArray.getJSONObject(3);

                                if (jsonOLocalList.has("listVersion")) {
                                    versionNo = jsonOLocalList.getString("listVersion");
                                }

                                if (jsonOLocalList.has("updateType")) {
                                    type_ = jsonOLocalList.getString("updateType");
                                }

                                if (jsonOLocalList.has("localAuthorizationList")) {
                                    JSONArray jsonArrayAuthList = jsonOLocalList.getJSONArray("localAuthorizationList");
                                    dbManager.insertLocalList2nd(jsonArrayAuthList,type_,versionNo);
                                }

                                Log.e("PARSEMeSSAGE","jsonArrayLIST"+jsonArray);
                            }
                            break;
                        case "ChangeAvailability":
                            String connector_Id = null, type_Is = null;
                            String sessionIDCA = jsonArray.getString(1);
                            String responseStatus = "";
                            if (jsonArray.getJSONObject(3) != null) {
                                JSONObject jsonOChangeAvailibility = jsonArray.getJSONObject(3);

                                if (jsonOChangeAvailibility.has("connectorId")) {
                                    connector_Id = jsonOChangeAvailibility.getString("connectorId");
                                }

                                if (jsonOChangeAvailibility.has("type")) {
                                    type_Is = jsonOChangeAvailibility.getString("type");
                                }
                                Log.e("PARSEMeSSAGE ", " --> " + connector_Id);
                                if (connector_Id != null) {

                                    switch (connector_Id) {
                                        case "1":
                                            if (status_detailCP1.equals(s10_CP1)) {
                                                responseStatus = "Scheduled";
                                            } else {
                                                responseStatus = "Accepted";
                                            }
                                            sendResponse(generateJSONARRAY(sessionIDCA,
                                                    responseStatus));
                                            break;
                                        case "2":
                                            if (status_detailCP2.equals(s10_CP2)) {
                                                responseStatus = "Scheduled";
                                            } else {
                                                responseStatus = "Accepted";
                                            }
                                            sendResponse(generateJSONARRAY(sessionIDCA, responseStatus));
                                            break;
                                        case "3":
                                            if (status_detailCP3.equals(s10_CP3)) {
                                                responseStatus = "Scheduled";
                                            } else {
                                                responseStatus = "Accepted";
                                            }
                                            sendResponse(generateJSONARRAY(sessionIDCA, responseStatus));
                                            break;
                                    }
                                    dbManager.insertChangeAvailability(connector_Id, type_Is);
                                } else {
                                    //SEND REJECTED STATUS

                                    sendResponse(generateJSONARRAY(sessionIDCA,
                                            "Rejected"));
                                }
                            }
                            break;
                        default:
                            if (jsonArray.getJSONObject(2)!=null){
                                JSONObject jsonObject = jsonArray.getJSONObject(2);
                                if (jsonObject.has("idTagInfo")){
                                    JSONObject  jsonObjectidInfo = jsonObject.getJSONObject("idTagInfo");
                                    if (jsonObject.has("transactionId")){
                                        sendOtherVals(jsonObject.getString("transactionId"));
                                       /* sharedPreTransactionId=getSharedPreferences("TRANSACTIONID",MODE_PRIVATE);
                                        SharedPreferences.Editor editor1 = sharedPreTransactionId.edit();
                                        editor1.putString("tranId"+connectorIS,jsonObject.getString("transactionId"));
                                        editor1.apply();*/

                                        Log.e("TRAINIDCONNECTOR",
                                                "CID -> "+connectorIS
                                                +" TRNID -> "+jsonObject.getString("transactionId"));

                                        SharedPreferences tranIdPref=getSharedPreferences("TRNID",MODE_PRIVATE);
                                        SharedPreferences.Editor editorTid=tranIdPref.edit();
                                        editorTid.putString(connectorIS,jsonObject.getString("transactionId"));
                                        editorTid.apply();

                                        switch (connectorIS) {
                                            case "1":
                                                transactionIDC1=jsonObject.getString("transactionId");
                                                spotpForCP1.setTransactonIdCP1(transactionIDC1);
                                                spotpForCP1.setOTPForCP1(sessionNumberC1OTP);
                                                connectorIS="0";
                                                break;
                                            case "2":
                                                transactionIDC2=jsonObject.getString("transactionId");
                                                spotpForCP1.setTransactonIdCP2(transactionIDC2);
                                                spotpForCP1.setOTPForCP2(sessionNumberC2OTP);
                                                connectorIS="0";
                                                break;
                                            case "3":
                                                transactionIDC3=jsonObject.getString("transactionId");
                                                spotpForCP1.setTransactonIdCP3(transactionIDC3);
                                                spotpForCP1.setOTPForCP3(sessionNumberC3OTP);
                                                connectorIS="0";
                                                break;
                                            default:
                                                if (transactionIDC1.equals("0")){
                                                    transactionIDC1=jsonObject.getString("transactionId");
                                                    spotpForCP1.setTransactonIdCP1(transactionIDC1);
                                                    spotpForCP1.setOTPForCP1(sessionNumberC1OTP);
                                                    connectorIS="0";
                                                }
                                                if (transactionIDC2.equals("0")){
                                                    transactionIDC2=jsonObject.getString("transactionId");
                                                    spotpForCP1.setTransactonIdCP2(transactionIDC2);
                                                    spotpForCP1.setOTPForCP2(sessionNumberC2OTP);
                                                    connectorIS="0";
                                                }
                                                if (transactionIDC3.equals("0")){
                                                    transactionIDC3=jsonObject.getString("transactionId");
                                                    spotpForCP1.setTransactonIdCP3(transactionIDC3);
                                                    spotpForCP1.setOTPForCP3(sessionNumberC3OTP);
                                                    connectorIS="0";
                                                }
                                        }
                                    }

                                    if (jsonObjectidInfo.has("status")){
                                        _StatusIS="";
                                        _StatusIS=jsonObjectidInfo.getString("status");
                                        if ((jsonObjectidInfo.getString("status").equals("Invalid")
                                                || jsonObjectidInfo.getString("status").equals("Blocked")
                                                || jsonObjectidInfo.getString("status").equals("Expired"))){
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    /*if (_StatusIS.equals("Invalid")){*/
                                                    if(edt_OTP0!=null){
                                                        //kioskProgressDialog.dismiss();
                                                        if (progressBar!=null /*&& progressDialog.isShowing()*/){
                                                            progressBar.dismiss();
                                                        }
                                                        edt_OTP0.setError("It seems like you have entered "+_StatusIS+" OTP please try with valid one!");
                                                        edt_OTP0.requestFocus();
                                                        checkAuth=false;
                                                    }
                                       /* }else if (_StatusIS.equals("Blocked")){
                                            if(edt_OTP0!=null){
                                                kioskProgressDialog.dismiss();
                                                edt_OTP0.setError("OTP "+_StatusIS+"Please Request For Another OTP!");
                                                edt_OTP0.requestFocus();
                                                checkAuth=false;
                                            }
                                        }*/

                                                }
                                            });
                                        }else {
                                            dismissProgressDialog();
                                            if (isAuthReq.equals(jsonArray.get(1))){
                                                switch (connectorIS) {
                                                    case "1":
                                                        funConnectori();
                                                        break;
                                                    case "2":
                                                        funConnectorii();
                                                        break;
                                                    case "3":
                                                        funConnectoriii();
                                                        break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            break;
                    }

                    if (_CMSCommand.equals("RemoteStopTransaction")){
                        reasonC="Remote";
                        if (jsonArray.getJSONObject(3)!=null){
                            final JSONObject jsonObject = jsonArray.getJSONObject(3);
                            final String sessionIDCA2 = jsonArray.getString(1);
                            if (jsonObject.has("transactionId")){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            String sessionId="\"" +jsonArray.get(1)+ "\"";
                                            if (jsonObject.getString("transactionId").equals(transactionIDC1)){
                                                if (status_detailCP1.equals(s10_CP1)){
                                                    funConnectori();
                                                    sendResponse(generateJSONARRAY(sessionIDCA2,
                                                            "Accepted"));

/*
                                                    webSocketokhttp.send(
                                                            "[3,"+sessionId+",{\"status\":\"Accepted\"}]");
*/
                                                }else {
/*
                                                    webSocketokhttp.send(
                                                            "[3,"+sessionId+",{\"status\":\"Accepted\"}]");
*/

                                                    sendResponse(generateJSONARRAY(sessionIDCA2,
                                                            "Rejected"));

                                                }
                                            }else if (jsonObject.getString("transactionId").equals(transactionIDC2)){
                                                if (status_detailCP2.equals(s10_CP1)){
                                                    funConnectorii();
                                                    sendResponse(generateJSONARRAY(sessionIDCA2,
                                                            "Accepted"));
                                                }else {
                                                    sendResponse(generateJSONARRAY(sessionIDCA2,
                                                            "Rejected"));

                                                }

                                                //  webSocketokhttp.send("[3,"+sessionId+",{\"status\":\"Accepted\"}]");
                                            }else if (jsonObject.getString("transactionId").equals(transactionIDC3)){
                                                if (status_detailCP3.equals(s10_CP1)){
                                                    funConnectoriii();
                                                    sendResponse(generateJSONARRAY(sessionIDCA2,
                                                            "Accepted"));
                                                }else {
                                                    sendResponse(generateJSONARRAY(sessionIDCA2,
                                                            "Rejected"));

                                                }
                                                //webSocketokhttp.send("[3,"+sessionId+",{\"status\":\"Accepted\"}]");
                                            }
                                        }catch (Exception e){
                                            Log.e("CHECKINTERNETNEW","RUNUITHREAD EX "+e);
                                        }

                                    }
                                });

                            }
                        }
                    }else if (_CMSCommand.equals("RemoteStartTransaction")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject jsonObject = jsonArray.getJSONObject(3);
                                    String sessionIDCA1 = jsonArray.getString(1);
                                    switch (jsonObject.getString("connectorId")) {
                                        case "1":
                                            if (status_detailCP1.equals(s8_CP1)){
                                                _OtpIs=jsonObject.getString("idTag");
                                                OTPIs=jsonObject.getString("idTag");
                                                sessionNumberC1 = generateSessionId();
                                                connectorIS="1";
                                                spotpForCP1.setOTPForCP1(OTPIs);

                                                sendAuthorizationRequest(jsonObject.getString("idTag"),
                                                        generateSessionId(),jsonObject.getString("connectorId"));

                                                sendResponse(generateJSONARRAY(sessionIDCA1,
                                                        "Accepted"));

                                            }else {
                                                sendResponse(generateJSONARRAY(sessionIDCA1,
                                                        "Rejected"));
                                            }
                                            break;
                                        case "2":
                                            if (status_detailCP2.equals(s8_CP2)){
                                                sessionNumberC2 = generateSessionId();
                                                _OtpIs=jsonObject.getString("idTag");
                                                OTPIs=jsonObject.getString("idTag");
                                                connectorIS="2";
                                                spotpForCP1.setOTPForCP2(OTPIs);
                                                sendAuthorizationRequest(jsonObject.getString("idTag"),
                                                        generateSessionId(),jsonObject.getString("connectorId"));

                                                sendResponse(generateJSONARRAY(sessionIDCA1,
                                                        "Accepted"));

                                            }else {
                                                sendResponse(generateJSONARRAY(sessionIDCA1,
                                                        "Rejected"));

                                            }
                                            break;
                                        case "3":
                                            if (status_detailCP3.equals(s8_CP3)){
                                                _OtpIs=jsonObject.getString("idTag");
                                                OTPIs=jsonObject.getString("idTag");
                                                sessionNumberC3 = generateSessionId();
                                                connectorIS="3";
                                                spotpForCP1.setOTPForCP3(OTPIs);
                                                sendAuthorizationRequest(jsonObject.getString("idTag"),
                                                        generateSessionId(),jsonObject.getString("connectorId"));
                                                sendResponse(generateJSONARRAY(sessionIDCA1,
                                                        "Accepted"));
                                            }else {
                                                sendResponse(generateJSONARRAY(sessionIDCA1,
                                                        "Rejected"));
                                            }
                                             break;
                                    }
                                }catch (Exception e){
                                    Log.e("CHECKINTERNETNEW","RUNUITHREAD EX 2nd"+e);
                                }
                            }
                        });
                    }
                }

            }catch (Exception e){
                Log.e("CHECKINTERNETNEW"," onMessage Exception--> "+e);
                e.printStackTrace();
                Log.e("PARSEDRESPONSE","-EXCEPTIONIS-> "+e);
            }
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes){}

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            Log.e("CHECKINTERNETNEW"," onClosing --> "+reason);
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
            webSocketokhttp=openConnection();
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
           // webSocketokhttp=openConnection();
        }
    }

    private String generateJSONARRAY(String sessionIDCA, String responseStatus) {
        try{
            JSONArray jsonArrayChangeAvail=new JSONArray();
            JSONObject jsonObjectChangeAvail = new JSONObject();
            jsonObjectChangeAvail.put("status",responseStatus);
            jsonArrayChangeAvail.put(3);
            jsonArrayChangeAvail.put(sessionIDCA);
            jsonArrayChangeAvail.put(jsonObjectChangeAvail);

            Log.e("PARSEMeSSAGE "," GENJSON--> "+jsonArrayChangeAvail);
            return jsonArrayChangeAvail.toString();
        }catch (Exception e) {
            Log.e("PARSEMeSSAGE "," GENJSONEXPTN--> "+e);
            return e.toString();
        }
    }

    private void sendResponse(String toString) {
        webSocketokhttp=openConnection();
        webSocketokhttp.send(toString);
        Log.e("PARSEMeSSAGE "," --> SENDING 2222...!!! "+ webSocketokhttp.send(toString()));
    }

    private void sendOtherVals(final String transactionId) {
        /*sendStopRequest("1"
                ,p1MeterValue,transactionIDC1,
                sessionNumberC1OTP,sessionNumberC1);
        spotpForCP1.setOTPForCP1(sessionNumberC1OTP);*/
        if (listMapOtherRequests!=null){
            for (int i=0;i<listMapOtherRequests.size();i++){
                Log.e("NISHADJ "," transactionIdOFFLINE--> "+transactionIdOFFLINE);
                Log.e("NISHADJ "," transactionId--> "+transactionId);
                if (listMapOtherRequests.get(i).contains("StopTransaction")){
                    //replaceString(transactionId);
                    String replaced=listMapOtherRequests.get(i)
                            .replace(transactionIdOFFLINE,transactionId);
                  //parseNSend(listMapOtherRequests.get(i),transactionId);
                    webSocketokhttp.send(replaced);
                    Log.e("NISHADJ "," replaced--> "+replaced);
                }else {
                    webSocketokhttp.send(listMapOtherRequests.get(i));
                    Log.e("IFSTARTFOUND "," PARSE--> "+
                            listMapOtherRequests.get(i));
                }
            }
            Log.e("INSERTINTODB","listMapOtherRequests"+listMapOtherRequests);

            listMapOtherRequests=new ArrayList<>();
            //uploadingCall=0;
        }
    }

    private void replaceString(String transactionId) {
        Log.e("NISHADJ "," transactionIdOFFLINE1212--> "+transactionIdOFFLINE);
        Log.e("NISHADJ "," transactionId1212--> "+transactionId);

        String replaced=listMapOtherRequests.get(i).replaceAll(transactionIdOFFLINE,transactionId);
        //parseNSend(listMapOtherRequests.get(i),transactionId);
        webSocketokhttp.send(replaced);
        Log.e("NISHADJ "," replaced--> "+replaced);
    }

    private void parseNSend(String s1, String transactionNUmber) {

        Log.e("IFSTARTFOUND "," PARSE--> "+s1);
        try {
            JSONArray jsonArray = new JSONArray(s1);
            Log.e("IFSTARTFOUND "," PARSEJARRAY--> "+jsonArray.toString());
            if (jsonArray.getJSONObject(3)!=null){
                Log.e("IFSTARTFOUND "," PARSEJOBJECT--> "+jsonArray.getJSONObject(3));
                JSONObject jsonObject = jsonArray.getJSONObject(3);

                webSocketokhttp.send(JSONObjectsForRequest.sendStopTrasactionRequest(generateSessionId(),sessionNumberC1OTP,
                        String.valueOf(jsonObject.getString("meterStop")),
                        Integer.parseInt(transactionNUmber),jsonObject.getString("timestamp"),
                        reasonC,"L"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("IFSTARTFOUND "," PException --> "+e);

        }
        /*webSocketokhttp.send(JSONObjectsForRequest.sendStopTrasactionRequest(generateSessionId(),sessionNumberC1OTP,
                String.valueOf(meterreadingdiffC),Integer.parseInt(transactionIDC),getUTC(),
                reasonC,"L"+connectorID));*/
    }


    @SuppressLint("SetTextI18n")
    private void showOTPWindow(final String connectorIs,
                               final String toAction) {

        connectorIS=connectorIs;
        OTPIs="";

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.otp_window_layout);

        edt_OTP0 =  dialog.findViewById(R.id.edt_OTP0);
        TextView textInfo =  dialog.findViewById(R.id.txt_Info);
        final TextView txt_INStatus =  dialog.findViewById(R.id.txt_INStatus);
        Button btn_Cancel =  dialog.findViewById(R.id.btn_Cancel);
        Button btn_Done =  dialog.findViewById(R.id.btn_Done);



        new InternetCheck(new InternetCheck.Consumer() {
            @Override
            public void accept(Boolean internet) {
                if (internet){
                    status="Online";
                    txt_INStatus.setText(status);
                }else {
                    status="Offline";
                    txt_INStatus.setText(status);
                }

                /*View parentLayout = findViewById(android.R.id.content);
                final Snackbar snackbar = Snackbar.make(parentLayout,
                        "You are "+status+"", Snackbar.LENGTH_LONG);
                Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout)snackbar.getView();
                layout.setMinimumHeight(150);//your custom height.
                layout.setMinimumWidth(150);//your custom height.

                snackbar.setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                snackbar.setActionTextColor(getResources().getColor(android.R.color.holo_red_light ));
                snackbar.show();*/

            }
        });



        textInfo.setText(toAction+" Connector "+connectorIs+" enter OTP received on your mobile number.");

        btn_Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if (edt_OTP0.getText().toString().length()>=4){
                    connectorIS=connectorIs;
                    showProgressDialog();
                    switch (connectorIs) {
                        case "1":
                            if (toAction.equals("To Stop")){
                                if (sessionNumberC1OTP.equals(edt_OTP0.getText().toString())
                                        || spotpForCP1.getOTPForP1().equals(edt_OTP0.getText().toString())){
                                    sessionNumberC1OTP = edt_OTP0.getText().toString();
                                    funConnectori();
                                    dismissProgressDialog();
                                }else {
                                    edt_OTP0.setError("It seems like you have entered invalid OTP please try with valid one!");
                                    edt_OTP0.requestFocus();
                                    checkAuth=false;
                                    if (progressBar!=null && progressBar.isShowing()){
                                        progressBar.dismiss();
                                    }
                                    //kioskProgressDialog.dismiss();
                                }
                            }else {
                                sessionNumberC1 = generateSessionId();
                                sessionNumberC1OTP = edt_OTP0.getText().toString();
                                connectorIS="1";
                                sendAuthorizationRequest(
                                        edt_OTP0.getText().toString(),
                                        sessionNumberC1,"1");
                            }
                            break;
                        case "2":
                            if (toAction.equals("To Stop")){
                                if (sessionNumberC2OTP.equals(edt_OTP0.getText().toString())
                                        || spotpForCP1.getOTPForP2().equals(edt_OTP0.getText().toString())){
                                    sessionNumberC2OTP = edt_OTP0.getText().toString();
                                    funConnectorii();
                                    dismissProgressDialog();
                                }else {
                                    //edt_OTP0.setText(spotpForCP1.getOTPForP2());
                                    edt_OTP0.setError("It seems like you entered invalid OTP please try with valid one!");
                                    edt_OTP0.requestFocus();
                                    checkAuth=false;
                                    if (progressBar!=null && progressBar.isShowing()){
                                        progressBar.dismiss();
                                    }

                                    //kioskProgressDialog.dismiss();
                                }
                            }else {
                                sessionNumberC2 = generateSessionId();
                                sessionNumberC2OTP = edt_OTP0.getText().toString();
                                connectorIS="2";
                                sendAuthorizationRequest(edt_OTP0.getText().toString(),
                                        sessionNumberC2,"2");
                            }
                            break;
                        case "3":
                            if (toAction.equals("To Stop")){
                                if (sessionNumberC3OTP.equals(edt_OTP0.getText().toString())
                                        || spotpForCP1.getOTPForP3().equals(edt_OTP0.getText().toString())){
                                    sessionNumberC3OTP = edt_OTP0.getText().toString();
                                    funConnectoriii();
                                    dismissProgressDialog();
                                }else {
                                    edt_OTP0.setError("It seems like you entered invalid OTP please try with valid one!");
                                    edt_OTP0.requestFocus();
                                    checkAuth=false;
                                    if (progressBar!=null && progressBar.isShowing()){
                                        progressBar.dismiss();
                                    }

                                    //kioskProgressDialog.dismiss();
                                    //edt_OTP0.setText(spotpForCP1.getOTPForP3());
                                }
                            }else {
                                sessionNumberC3 = generateSessionId();
                                sessionNumberC3OTP = edt_OTP0.getText().toString();
                                connectorIS="3";
                                sendAuthorizationRequest(edt_OTP0.getText().toString(),
                                        sessionNumberC3,"3");
                            }
                            break;
                    }
                }else {
                    if (edt_OTP0.getText().toString().length()==0){
                        edt_OTP0.setError("Please Enter OTP!");
                        edt_OTP0.requestFocus();
                    }else  if (edt_OTP0.getText().toString().length()<4){
                        edt_OTP0.setError("OTP Must Have Four Characters!");
                        edt_OTP0.requestFocus();
                    }
                }
            }
        });

        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

     /*{

        new InternetCheck(new InternetCheck.Consumer(){

            @Override
            public void accept(Boolean internet) {
                View parentLayout = findViewById(android.R.id.content);
                final Snackbar snackbar = Snackbar.make(parentLayout, "Internet Connection"+internet, Snackbar.LENGTH_LONG);
                Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout)snackbar.getView();
                layout.setMinimumHeight(150);//your custom height.
                layout.setMinimumWidth(150);//your custom height.

                snackbar.setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                snackbar.setActionTextColor(getResources().getColor(android.R.color.holo_red_light ));
                snackbar.show();
            }
        });

    }*/
}