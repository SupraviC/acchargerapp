package com.supravin.accharger.Storage;

import android.content.Context;
import android.content.SharedPreferences;

public class SPOTPForCP1 {

    private Context context;


    private SharedPreferences spOTPFORCP1;
    private SharedPreferences.Editor edOTPFORCP1;
    private static final String OTPFORCP1 = "OTPFORCP1";
    private static final String ValueOTPFORCP1 = "ValueOTPFORCP1";

    private SharedPreferences spOTPFORCP2;
    private SharedPreferences.Editor edOTPFORCP2;
    private static final String OTPFORCP2 = "OTPFORCP2";
    private static final String ValueOTPFORCP2 = "ValueOTPFORCP2";

    private SharedPreferences spTransactonIdCP1;
    private SharedPreferences.Editor edTransactonIdCP1;
    private static final String TransactonIdCP1 = "TransactonId";
    private static final String ValueTransactonIdCP1 = "ValueTransactonIdCP1";

    private SharedPreferences spTransactonIdCP2;
    private SharedPreferences.Editor edTransactonIdCP2;
    private static final String TransactonIdCP2 = "TransactonId2";
    private static final String ValueTransactonIdCP2 = "ValueTransactonIdCP2";

    private SharedPreferences spOTPFORCP3;
    private SharedPreferences.Editor edOTPFORCP3;
    private static final String OTPFORCP3 = "OTPFORCP3";
    private static final String ValueOTPFORCP3 = "ValueOTPFORCP3";

    private SharedPreferences spTransactonIdCP3;
    private SharedPreferences.Editor edTransactonIdCP3;
    private static final String TransactonIdCP3 = "TransactonId3";
    private static final String ValueTransactonIdCP3 = "ValueTransactonIdCP3";




    public SPOTPForCP1(Context context) {
        this.context = context;
        spOTPFORCP1 = this.context.getSharedPreferences(OTPFORCP1, Context.MODE_PRIVATE);
        spOTPFORCP2 = this.context.getSharedPreferences(OTPFORCP2, Context.MODE_PRIVATE);
        spTransactonIdCP1 = this.context.getSharedPreferences(TransactonIdCP1, Context.MODE_PRIVATE);
        spTransactonIdCP2 = this.context.getSharedPreferences(TransactonIdCP2, Context.MODE_PRIVATE);
        spOTPFORCP3 = this.context.getSharedPreferences(OTPFORCP3, Context.MODE_PRIVATE);
        spTransactonIdCP3 = this.context.getSharedPreferences(TransactonIdCP3, Context.MODE_PRIVATE);
    }

    //------------OTP For CP1
    public String getOTPForP1() {
        return spOTPFORCP1.getString(ValueOTPFORCP1,"");
    }

    public void setOTPForCP1(String ValueTimeReadingCP1s ) {
        edOTPFORCP1 = spOTPFORCP1.edit();
        edOTPFORCP1.putString(ValueOTPFORCP1, ValueTimeReadingCP1s);
        edOTPFORCP1.commit();
        edOTPFORCP1.apply();
    }

    //------------OTP For CP1
    public String getOTPForP2() {
        return spOTPFORCP2.getString(ValueOTPFORCP2,"");
    }

    public void setOTPForCP2(String ValueTimeReadingCP1s ) {
        edOTPFORCP2 = spOTPFORCP2.edit();
        edOTPFORCP2.putString(ValueOTPFORCP2, ValueTimeReadingCP1s);
        edOTPFORCP2.commit();
        edOTPFORCP2.apply();
    }

    public String getTransactonIdP1() {
        return spTransactonIdCP1.getString(ValueTransactonIdCP1,"");
    }

    public void setTransactonIdCP1(String ValueTimeReadingCP1s ) {
        edTransactonIdCP1 = spTransactonIdCP1.edit();
        edTransactonIdCP1.putString(ValueTransactonIdCP1, ValueTimeReadingCP1s);
        edTransactonIdCP1.commit();
        edTransactonIdCP1.apply();
    }

    public String getTransactonIdP2() {
        return spTransactonIdCP2.getString(ValueTransactonIdCP2,"");
    }

    public void setTransactonIdCP2(String ValueTimeReadingCP1s ) {
        edTransactonIdCP2 = spTransactonIdCP2.edit();
        edTransactonIdCP2.putString(ValueTransactonIdCP2, ValueTimeReadingCP1s);
        edTransactonIdCP2.commit();
        edTransactonIdCP2.apply();
    }


    public String getOTPForP3() {
        return spOTPFORCP3.getString(ValueOTPFORCP3, "");
    }

    public void setOTPForCP3(String ValueTimeReadingCP1s ) {
        edOTPFORCP3 = spOTPFORCP3.edit();
        edOTPFORCP3.putString(ValueOTPFORCP3, ValueTimeReadingCP1s);
        edOTPFORCP3.commit();
        edOTPFORCP3.apply();
    }

    public String getTransactonIdP3() {
        return spTransactonIdCP3.getString(ValueTransactonIdCP3,"");
    }

    public void setTransactonIdCP3(String ValueTimeReadingCP1s ) {
        edTransactonIdCP3 = spTransactonIdCP3.edit();
        edTransactonIdCP3.putString(ValueTransactonIdCP3, ValueTimeReadingCP1s);
        edTransactonIdCP3.commit();
        edTransactonIdCP3.apply();
    }


    public void clearTimeReadingCP1(){
        edOTPFORCP1.clear();
        edOTPFORCP1.commit();
    }

}
