package com.supravin.accharger.Storage;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by admin on 05/01/2018.
 */


public class SPisPoweFail {

    private Context context;

    private SharedPreferences sessionIdCP1;
    private SharedPreferences.Editor edsessionIdCP1;

    private static final String issessionIdCP1 = "sessionIdCP1";
    private static final String ValueisessionIdCP1 = "ValuesessionIdCP1";

    private SharedPreferences sessiontransIDCP1;
    private SharedPreferences.Editor edsessiontransIDCP1;
    private static final String transID = "transID";

    private SharedPreferences sessionoTPCP1;
    private SharedPreferences.Editor edsessionoTPCP1;
    private static final String oTP = "oTP";

    private SharedPreferences sessionconnectorIDCP1;
    private SharedPreferences.Editor edsessionconnectorIDCP1;
    private static final String connectorID = "connectorID";

    private SharedPreferences spisPowerFailCP1;
    private SharedPreferences.Editor edisPowerFailCP1;
    private static final String isPowerFailCP1 = "isPowerFailCP1";
    private static final String ValueisPowerFailCP1 = "ValueisPowerFailCP1";

    private SharedPreferences spSwitchStatusCP1;
    private SharedPreferences.Editor edSwitchStatusCP1;
    private static final String switchStatusCP1 = "switchStatusCP1";
    private static final String ValueswitchStatusCP1 = "ValueswitchStatusCP1";

    //-------------------for button cp2
    private SharedPreferences spisPowerFailCP2;
    private SharedPreferences.Editor edisPowerFailCP2;
    private static final String isPowerFailCP2 = "isPowerFailCP2";
    private static final String ValueisPowerFailCP2 = "ValueisPowerFailCP2";

    private SharedPreferences spSwitchStatusCP2;
    private SharedPreferences.Editor edSwitchStatusCP2;
    private static final String switchStatusCP2 = "switchStatusCP2";
    private static final String ValueswitchStatusCP2 = "ValueswitchStatusCP2";

    //-------------------for button cp3
    private SharedPreferences spisPowerFailCP3;
    private SharedPreferences.Editor edisPowerFailCP3;
    private static final String isPowerFailCP3 = "isPowerFailCP3";
    private static final String ValueisPowerFailCP3 = "ValueisPowerFailCP3";

    private SharedPreferences spSwitchStatusCP3;
    private SharedPreferences.Editor edSwitchStatusCP3;
    private static final String switchStatusCP3 = "switchStatusCP3";
    private static final String ValueswitchStatusCP3 = "ValueswitchStatusCP3";


    public SPisPoweFail(Context context) {
        this.context = context;

        sessionconnectorIDCP1 = this.context.getSharedPreferences(connectorID, Context.MODE_PRIVATE);
        edsessionconnectorIDCP1 = sessionconnectorIDCP1.edit();
        edsessionconnectorIDCP1.commit();

        sessiontransIDCP1 = this.context.getSharedPreferences(transID, Context.MODE_PRIVATE);
        edsessiontransIDCP1 = sessiontransIDCP1.edit();
        edsessiontransIDCP1.commit();

        sessionoTPCP1 = this.context.getSharedPreferences(oTP, Context.MODE_PRIVATE);
        edsessionoTPCP1 = sessionoTPCP1.edit();
        edsessionoTPCP1.commit();

        //-----------------------------------------------------SessionIdCP1
        sessionIdCP1 = this.context.getSharedPreferences(issessionIdCP1, Context.MODE_PRIVATE);
        edsessionIdCP1 = sessionIdCP1.edit();
        edsessionIdCP1.commit();

        //-----------------------------------------------------button1
        spisPowerFailCP1 = this.context.getSharedPreferences(isPowerFailCP1, Context.MODE_PRIVATE);
        edisPowerFailCP1 = spisPowerFailCP1.edit();
        edisPowerFailCP1.commit();

        //-------------switchstatuscp3--------------
        spSwitchStatusCP1 = this.context.getSharedPreferences(switchStatusCP1, Context.MODE_PRIVATE);
        edSwitchStatusCP1 = spSwitchStatusCP1.edit();
        edSwitchStatusCP1.commit();

        //-----------------------------------------------------button2
        spisPowerFailCP2 = this.context.getSharedPreferences(isPowerFailCP2, Context.MODE_PRIVATE);
        edisPowerFailCP2 = spisPowerFailCP2.edit();
        edisPowerFailCP2.commit();

        //-------------switchstatuscp2--------------
        spSwitchStatusCP2 = this.context.getSharedPreferences(switchStatusCP2, Context.MODE_PRIVATE);
        edSwitchStatusCP2 = spSwitchStatusCP2.edit();
        edSwitchStatusCP2.commit();

        //-----------------------------------------------------button3
        spisPowerFailCP3 = this.context.getSharedPreferences(isPowerFailCP3, Context.MODE_PRIVATE);
        edisPowerFailCP3 = spisPowerFailCP3.edit();
        edisPowerFailCP3.commit();

        //-------------switchstatuscp2--------------
        spSwitchStatusCP3 = this.context.getSharedPreferences(switchStatusCP3, Context.MODE_PRIVATE);
        edSwitchStatusCP3 = spSwitchStatusCP3.edit();
        edSwitchStatusCP3.commit();


    }


    //------------Session1

    public String getoTPCP1() {
        return sessionoTPCP1.getString(oTP, "");
    }

    public void setoTPCP1(String valueForSessionIdCP1) {
        edsessionoTPCP1.putString(oTP, valueForSessionIdCP1);
        edsessionoTPCP1.commit();
        edsessionoTPCP1.apply();
    }

    public String getConnectorIDCP1() {
        return sessionconnectorIDCP1.getString(connectorID, "");
    }

    public void setConnectorIDCP1(String valueForSessionIdCP1) {
        edsessionconnectorIDCP1.putString(connectorID, valueForSessionIdCP1);
        edsessionconnectorIDCP1.commit();
        edsessionconnectorIDCP1.apply();
    }

    public String getTransIDCP1() {
        return sessiontransIDCP1.getString(transID, "");
    }

    public void setTransIDCP1(String valueForSessionIdCP1) {
        edsessiontransIDCP1.putString(transID, valueForSessionIdCP1);
        edsessiontransIDCP1.commit();
        edsessiontransIDCP1.apply();
    }

    public String getSessionIdForCP1() {
        return sessionIdCP1.getString(ValueisessionIdCP1, "");
    }

    public void setSessionIdForCP1(String valueForSessionIdCP1) {
        edsessionIdCP1.putString(ValueisessionIdCP1, valueForSessionIdCP1);
        edsessionIdCP1.commit();
        edsessionIdCP1.apply();
    }


    //------------button1
    public String getisPowerFailCP1() {
        return spisPowerFailCP1.getString(ValueisPowerFailCP1, "");
    }



    public void setisPowerFailCP1(String ValueisPowerFailCP1s) {
        edisPowerFailCP1.putString(ValueisPowerFailCP1, ValueisPowerFailCP1s);
        edisPowerFailCP1.commit();
        edisPowerFailCP1.apply();
    }

    public void clearisPowerFailCP1(){
        edisPowerFailCP1.clear();
        edisPowerFailCP1.commit();
    }

    //-----------------SwitchStatus
    public String getSwitchStatusCP1() {
        return spSwitchStatusCP1.getString(ValueswitchStatusCP1, "");
    }

    public void setSwitchStatusCP1(String ValueSwitchStatusCP1s) {
        edSwitchStatusCP1.putString(ValueswitchStatusCP1, ValueSwitchStatusCP1s);
        edSwitchStatusCP1.commit();
        edSwitchStatusCP1.apply();
    }

    public void clearSwitchStatusCP1(){
        edSwitchStatusCP1.clear();
        edSwitchStatusCP1.commit();
    }

    //------------button2
    public String getisPowerFailCP2() {
        return spisPowerFailCP2.getString(ValueisPowerFailCP2, "");
    }

    public void setisPowerFailCP2(String ValueisPowerFailCP2s) {
        edisPowerFailCP2.putString(ValueisPowerFailCP2, ValueisPowerFailCP2s);
        edisPowerFailCP2.commit();
        edisPowerFailCP2.apply();
    }

    public void clearisPowerFailCP2(){
        edisPowerFailCP2.clear();
        edisPowerFailCP2.commit();
    }

    //-----------------SwitchStatus
    public String getSwitchStatusCP2() {
        return spSwitchStatusCP2.getString(ValueswitchStatusCP2, "");
    }

    public void setSwitchStatusCP2(String ValueSwitchStatusCP2s) {
        edSwitchStatusCP2.putString(ValueswitchStatusCP2, ValueSwitchStatusCP2s);
        edSwitchStatusCP2.commit();
        edSwitchStatusCP2.apply();
    }

    public void clearSwitchStatusCP2(){
        edSwitchStatusCP2.clear();
        edSwitchStatusCP2.commit();
    }
    //------------button3
    public String getisPowerFailCP3() {
        return spisPowerFailCP3.getString(ValueisPowerFailCP3, "");
    }

    public void setisPowerFailCP3(String ValueisPowerFailCP3s) {
        edisPowerFailCP3.putString(ValueisPowerFailCP3, ValueisPowerFailCP3s);
        edisPowerFailCP3.commit();
        edisPowerFailCP3.apply();
    }

    public void clearisPowerFailCP3(){
        edisPowerFailCP3.clear();
        edisPowerFailCP3.commit();
    }

    //-----------------SwitchStatus
    public String getSwitchStatusCP3() {
        return spSwitchStatusCP3.getString(ValueswitchStatusCP3, "");
    }

    public void setSwitchStatusCP3(String ValueSwitchStatusCP3s) {
        edSwitchStatusCP3.putString(ValueswitchStatusCP3, ValueSwitchStatusCP3s);
        edSwitchStatusCP3.commit();
        edSwitchStatusCP3.apply();
    }

    public void clearSwitchStatusCP3(){
        edSwitchStatusCP3.clear();
        edSwitchStatusCP3.commit();
    }

}