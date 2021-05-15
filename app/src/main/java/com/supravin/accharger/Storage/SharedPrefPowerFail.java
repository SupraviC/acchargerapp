package com.supravin.accharger.Storage;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by admin on 05/01/2018.
 */


public class SharedPrefPowerFail {
    private SharedPreferences preferencesFlag;
    private SharedPreferences.Editor editorFlag;
    private Context context;
    private static final String PREFERENCES_pfFlag = "pfFlag";
    private static final String PFFLAG = "PFFlag";

    private SharedPreferences preferencesBtn_CP1;
    private SharedPreferences.Editor editorBtn_CP1;
    private static final String PREFERENCES_Btn_CP1 = "Btn_CP1";
    private static final String Btn_CP1 = "Btn_CP1";

    private SharedPreferences preferencesBtn_CP2;
    private SharedPreferences.Editor editorBtn_CP2;
    private static final String PREFERENCES_Btn_CP2 = "Btn_CP2";
    private static final String Btn_CP2 = "Btn_CP2";

    private SharedPreferences preferencesBtn_CP3;
    private SharedPreferences.Editor editorBtn_CP3;
    private static final String PREFERENCES_Btn_CP3 = "Btn_CP3";
    private static final String Btn_CP3 = "Btn_CP3";

    private SharedPreferences preferencesTime_CP1;
    private SharedPreferences.Editor editorTime_CP1;
    private static final String PREFERENCES_Time_CP1 = "Btn_CP1";
    private static final String Time_CP1 = "Btn_CP1";

    private SharedPreferences preferencesTime_CP2;
    private SharedPreferences.Editor editorTime_CP2;
    private static final String PREFERENCES_Time_CP2 = "Btn_CP2";
    private static final String Time_CP2 = "Btn_CP2";

    private SharedPreferences preferencesTime_CP3;
    private SharedPreferences.Editor editorTime_CP3;
    private static final String PREFERENCES_Time_CP3 = "Btn_CP3";
    private static final String Time_CP3 = "Btn_CP3";


    public SharedPrefPowerFail(Context context) {
        this.context = context;
        preferencesFlag = this.context.getSharedPreferences(PREFERENCES_pfFlag, Context.MODE_PRIVATE);
        editorFlag = preferencesFlag.edit();
        editorFlag.commit();

        //-----------------------------------------------------button1
        preferencesBtn_CP1 = this.context.getSharedPreferences(PREFERENCES_Btn_CP1, Context.MODE_PRIVATE);
        editorBtn_CP1 = preferencesBtn_CP1.edit();
        editorBtn_CP1.commit();

        //-----------------------------------------------------button2
        preferencesBtn_CP2 = this.context.getSharedPreferences(PREFERENCES_Btn_CP2, Context.MODE_PRIVATE);
        editorBtn_CP2 = preferencesBtn_CP2.edit();
        editorBtn_CP2.commit();

        //-----------------------------------------------------button3
        preferencesBtn_CP3 = this.context.getSharedPreferences(PREFERENCES_Btn_CP3, Context.MODE_PRIVATE);
        editorBtn_CP3 = preferencesBtn_CP3.edit();
        editorBtn_CP3.commit();

        //-----------------------------------------------------Time1
        preferencesTime_CP1 = this.context.getSharedPreferences(PREFERENCES_Time_CP1, Context.MODE_PRIVATE);
        editorTime_CP1 = preferencesTime_CP1.edit();
        editorTime_CP1.commit();

        //-----------------------------------------------------Time2
        preferencesTime_CP2 = this.context.getSharedPreferences(PREFERENCES_Time_CP2, Context.MODE_PRIVATE);
        editorTime_CP2 = preferencesTime_CP2.edit();
        editorTime_CP2.commit();

        //-----------------------------------------------------Time3
        preferencesTime_CP3 = this.context.getSharedPreferences(PREFERENCES_Time_CP3, Context.MODE_PRIVATE);
        editorTime_CP3 = preferencesTime_CP3.edit();
        editorTime_CP3.commit();
    }

    public String getPFFlag() {
        return preferencesFlag.getString(PFFLAG, "");
    }

    public void setPFFlag(String PFFlag) {
        editorFlag.putString(PFFLAG, PFFlag);
        editorFlag.commit();
        editorFlag.apply();
    }

    public void clearPreferenceFlag(){
        editorFlag.clear();
        editorFlag.commit();
    }
    //------------button1
    public String getBtn_CP1() {
        return preferencesBtn_CP1.getString(Btn_CP1, "");
    }

    public void setBtn_CP1(String Btn_CP1s) {
        editorBtn_CP1.putString(Btn_CP1, Btn_CP1s);
        editorBtn_CP1.commit();
        editorBtn_CP1.apply();
    }

    public void clearPreferenceBtn_CP1(){
        editorBtn_CP1.clear();
        editorBtn_CP1.commit();
    }
    //------------button2
    public String getBtn_CP2() {
        return preferencesBtn_CP2.getString(Btn_CP2, "");
    }

    public void setBtn_CP2(String Btn_CP2s) {
        editorBtn_CP2.putString(Btn_CP2, Btn_CP2s);
        editorBtn_CP2.commit();
        editorBtn_CP2.apply();
    }

    public void clearPreferenceBtn_CP2(){
        editorBtn_CP2.clear();
        editorBtn_CP2.commit();
    }
    //------------button3
    public String getBtn_CP3() {
        return preferencesBtn_CP3.getString(Btn_CP3, "");
    }

    public void setBtn_CP3(String Btn_CP3s) {
        editorBtn_CP3.putString(Btn_CP3, Btn_CP3s);
        editorBtn_CP3.commit();
        editorBtn_CP3.apply();
    }

    public void clearPreferenceBtn_CP3(){
        editorBtn_CP3.clear();
        editorBtn_CP3.commit();
    }

    //------------Time1
    public String getTime_CP1() {
        return preferencesTime_CP1.getString(Time_CP1, "");
    }

    public void setTime_CP1(String Time_CP1s) {
        editorTime_CP1.putString(Time_CP1, Time_CP1s);
        editorTime_CP1.commit();
        editorTime_CP1.apply();
    }

    public void clearPreferenceTime_CP1(){
        editorTime_CP1.clear();
        editorTime_CP1.commit();
    }
    //------------button2
    public String getTime_CP2() {
        return preferencesTime_CP2.getString(Time_CP2, "");
    }

    public void setTime_CP2(String Time_CP2s) {
        editorTime_CP2.putString(Time_CP2, Time_CP2s);
        editorTime_CP2.commit();
        editorTime_CP2.apply();
    }

    public void clearPreferenceTime_CP2(){
        editorTime_CP2.clear();
        editorTime_CP2.commit();
    }
    //------------button3
    public String getTime_CP3() {
        return preferencesTime_CP3.getString(Time_CP3, "");
    }

    public void setTime_CP3(String Time_CP3s) {
        editorTime_CP3.putString(Time_CP3, Time_CP3s);
        editorTime_CP3.commit();
        editorTime_CP3.apply();
    }

    public void clearPreferenceTime_CP3(){
        editorTime_CP3.clear();
        editorTime_CP3.commit();
    }
}