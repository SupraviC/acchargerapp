package com.supravin.accharger.OCPP_Implementation_1_6;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.TimeZone;

public class JSONObjectsForRequest {
    public static String sendStartTransactionRequest(
            final String connectionIdstr,
            final String idTag,
            final String meterStartstr,
            String currentUTC,String sessionId){

        JSONArray jsonArrayMain = new JSONArray();
        JSONObject jsonObjectMain = new JSONObject();

        /*[2,"90","StartTransaction",{"connectorId":2, "idTag":"1111", "meterStart":1, "timestamp":"2019-09-25T14:35:26Z"}]*/
        try{

            jsonObjectMain.put("connectorId",connectionIdstr);
            jsonObjectMain.put("idTag",idTag);
            jsonObjectMain.put("meterStart",Math.round(Float.parseFloat(meterStartstr)));
            jsonObjectMain.put("timestamp",currentUTC);

            jsonArrayMain.put(2);
            jsonArrayMain.put(sessionId);
            jsonArrayMain.put("StartTransaction");
            jsonArrayMain.put(jsonObjectMain);

        }catch (Exception e){
            e.printStackTrace();
        }
        Log.e("SENDINGJSONTOCMSIS","  -> "+jsonArrayMain);
        return jsonArrayMain.toString();
    }/*{

        JSONObject jsonObject = new JSONObject();
        int meterstartfloat = Integer.parseInt(meterStartstr);
        String jsonString = "";
        int meterstartint = Math.round(meterstartfloat);;
        int connectionIdint = Integer.parseInt(connectionIdstr);

        try {
            jsonObject.put("connectorId",connectionIdint);
            jsonObject.put("idTag",idTag);
            jsonObject.put("meterStart",meterstartint);
            jsonObject.put("reservationId",0);

            jsonObject.put("timestamp",currentUTC);

            JSONArray jsonArray =  new JSONArray();
            jsonArray.put(2);
            jsonArray.put(sessionId);
            jsonArray.put("StartTransaction");
            jsonArray.put(jsonObject);
            jsonString = jsonArray.toString();
            Log.e("sendMessageToServer",jsonString);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonString;
    }*/



    public static String sendAuthorizeRequest(final String idTag,String sessionid){
        JSONObject jsonObject = new JSONObject();
        String jsonString = "";

        try {
            //jsonObject.put("$schema",authReqSchema);

            jsonObject.put("idTag",idTag);


            JSONArray jsonArray =  new JSONArray();
            jsonArray.put(2);
            jsonArray.put(sessionid);
            jsonArray.put("Authorize");
            jsonArray.put(jsonObject);
            jsonString = jsonArray.toString();


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  jsonString;
    }


    public static String sendBootNotificationRequest(String sessionId){
        JSONObject jsonObject = new JSONObject();
        String jsonString = "";

        try {

            jsonObject.put("chargePointVendor","M007");
            jsonObject.put("chargePointModel","M0007");

            jsonObject.put("chargeBoxSerialNumber", JSONObject.NULL);
            jsonObject.put("chargePointSerialNumber", JSONObject.NULL);
            jsonObject.put("firmwareVersion","1.0");
            jsonObject.put("iccid", JSONObject.NULL);
            jsonObject.put("imsi", JSONObject.NULL);
            jsonObject.put("meterSerialNumber", JSONObject.NULL);
            jsonObject.put("meterType","AC");
            JSONArray jsonArray = new JSONArray();

            jsonArray.put(2);
            jsonArray.put(sessionId);
            jsonArray.put("BootNotification");
            jsonArray.put(jsonObject);

            jsonString = jsonArray.toString().replace("\\", "");
            Log.e("JSON STRING === >  ",jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Exception ==> ", String.valueOf(e));

        }
        return  jsonString;
    }

    public static String sendDataTransferRequest(){
        JSONObject jsonObject = new JSONObject();
        String jsonString = "";

        try {
            jsonObject.put("vendorId","M007");
            jsonObject.put("messageId", JSONObject.NULL);
            jsonObject.put("data", JSONObject.NULL);

            JSONArray jsonArray =  new JSONArray();
            jsonArray.put(2);
          //  jsonArray.put(sessionid);
            jsonArray.put("DataTransfer");
            jsonArray.put(jsonObject);
            jsonString = jsonArray.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonString;
    }


    public static String sendDiagnosticsStatusNotificationRequest(){
        JSONObject jsonObject = new JSONObject();
        String jsonString = "";

        try {
            jsonObject.put("status","Idle");

            JSONArray jsonArray =  new JSONArray();
            jsonArray.put(2);
            //jsonArray.put();
            jsonArray.put("DiagnosticsStatusNotification");
            jsonArray.put(jsonObject);
            jsonString = jsonArray.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonString;
    }



    public static String sendFirmwareStatusNotificationRequest(){
        JSONObject jsonObject = new JSONObject();
        String jsonString = "";

        try {
            jsonObject.put("status","Idle");

            JSONArray jsonArray =  new JSONArray();
            jsonArray.put(2);
         //   jsonArray.put();
            jsonArray.put("FirmwareStatusNotification");
            jsonArray.put(jsonObject);
            jsonString = jsonArray.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonString;
    }


    public static String sendHeartbeatRequest(String sessionId){
        JSONObject jsonObject = new JSONObject();

        JSONArray jsonArray =  new JSONArray();
        jsonArray.put(2);
        jsonArray.put(sessionId);
        jsonArray.put("Heartbeat");
        jsonArray.put(jsonObject);
        String jsonString = jsonArray.toString();

        return jsonString;
    }

    public static String sendMeterValuesRequest(String currentUTC,
    String connectorId,String voltageC,String transactionID,
                                                String phaseIS,
                                                String currentC,
                                                String energyC,
                                                String sessionID)
    {
        String strSID = "\""+sessionID+"\"";
        String strCID = "\""+connectorId+"\"";
        String strUTC = "\""+currentUTC+"\"";
        DateFormat df = DateFormat.getTimeInstance();
        df.setTimeZone(TimeZone.getTimeZone("gmt"));
       // return "[2,"+sessionID+",\"MeterValues\",{\"connectorId\":"+connectorId+",\"transactionId\":"+transactionID+",\"meterValue\":[{\"timestamp\":"+currentUTC+",\"sampledValue\":[{\"value\":"+voltageC+",\"measurand\":\"Voltage\",\"unit\":\"V\"},{\"value\":\"37.00\",\"measurand\":\"Temperature\",\"unit\":\"Celsius\"},{\"value\":"+energyC+",\"measurand\":\"Power.Active.Import\",\"unit\":\"W\"},{\"value\":"+currentC+",\"measurand\":\"Current.Import\",\"unit\":\"A\"},{\"value\":"+energyC+",\"measurand\":\"Energy.Active.Import.Register\",\"unit\":\"kWh\"}]}]}]";
      //  return "[2,"+strSID+",\"MeterValues\",{\"connectorId\":"+strCID+",\"transactionId\":"+transactionID+",\"meterValue\":[{\"timestamp\": "+strUTC+",\"sampledValue\":[{\"value\":"+voltageC+",\"measurand\":\"Voltage\",\"unit\":\"V\"},{\"value\":\"37.00\",\"measurand\":\"Temperature\",\"unit\":\"Celsius\"},{\"value\":"+currentC+",\"measurand\":\"Current.Import\",\"unit\":\"A\"},{\"value\":"+energyC+",\"measurand\":\"Energy.Active.Import.Register\",\"unit\":\"kWh\"}]}]}]";
        return "[2,"+strSID+",\"MeterValues\",{\"connectorId\":"+strCID+",\"transactionId\":"+transactionID+",\"meterValue\":[{\"timestamp\": "+strUTC+",\"sampledValue\":[{\"value\":"+voltageC+",\"measurand\":\"Voltage\",\"unit\":\"V\"},{\"value\":\"37.00\",\"measurand\":\"Temperature\",\"unit\":\"Celsius\"},{\"value\":"+currentC+",\"measurand\":\"Current.Import\",\"unit\":\"A\"},{\"value\":"+energyC+",\"measurand\":\"Energy.Active.Import.Register\",\"unit\":\"Wh\"}]}]}]";
    }/*{

        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray =  new JSONArray();
        jsonArray.put(2);
        jsonArray.put(sessionID);
        jsonArray.put("MeterValues");
        try {
            jsonObject.put("connectorId",Integer.parseInt(connectorId)  );
            jsonObject.put("transactionId",Integer.parseInt(transactionID));
            JSONArray metervalueArray = new JSONArray();
            JSONArray sampleValueArray = new JSONArray();
            JSONObject jsonObject1 = new JSONObject();
            JSONObject voltage  = new JSONObject();
            voltage.put("value",voltageC);
            voltage.put("measurand","Voltage");
            voltage.put("phase",phaseIS);
            voltage.put("unit","V");
            voltage.put("timestamp",currentUTC);


            JSONObject current = new JSONObject();
            current.put("value",currentC);
            current.put("measurand","Current.Import");
            current.put("phase",phaseIS);
            current.put("unit","A");

            JSONObject energy = new JSONObject();
            energy.put("value",energyC);
            energy.put("measurand","Energy.Active.Import.Interval");
            energy.put("phase",phaseIS);
            energy.put("unit","kWh");

            JSONObject temprature = new JSONObject();
            temprature.put("value",34);
            temprature.put("measurand","Temperature");
            temprature.put("phase",phaseIS);
            temprature.put("unit","Celcius");

            sampleValueArray.put(voltage);
            sampleValueArray.put(current);
            sampleValueArray.put(energy);
            sampleValueArray.put(temprature);
            jsonObject1.put("sampledValue",sampleValueArray);
            metervalueArray.put(jsonObject1);
            jsonObject.put("meterValue",metervalueArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        jsonArray.put(jsonObject);
        String jsonString = jsonArray.toString();

        return jsonString;
    }*/


    public static String sendStatusNotificationRequest(final String connectorIdstr,
                                                       final String errorCode,
                                                       final String status,
                                                       String currentUTC,
                                                       String sessionId){
        //StatusNotificationRequest
        int connectorIdint = Integer.parseInt(connectorIdstr);
        JSONObject jsonObject = new JSONObject();
        String jsonString = "";
        try {
            jsonObject.put("connectorId",connectorIdint);
            jsonObject.put("errorCode",errorCode);
            jsonObject.put("info", JSONObject.NULL);
            jsonObject.put("status",status);
            jsonObject.put("timestamp",currentUTC);
            jsonObject.put("vendorId", JSONObject.NULL);
            jsonObject.put("vendorErrorCode", JSONObject.NULL);

            JSONArray jsonArray =  new JSONArray();
            jsonArray.put(2);
            jsonArray.put(sessionId);
            jsonArray.put("StatusNotification");
            jsonArray.put(jsonObject);
            jsonString = jsonArray.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  jsonString;
    }


    public static String sendStopTrasactionRequest(
            String sessionId,
            final String idTagstr,
            String meterStop,
            int transactionId,
            String currentUTC,
            String reason,
            String phaseIS){
        JSONArray jsonArrayMain= new JSONArray();
        JSONObject jsonObjectIn = new JSONObject();
        JSONArray jsonArrayTranData = new JSONArray();
        JSONArray jsonArraySampledVals = new JSONArray();
        JSONObject  jsonObjectTranData = new JSONObject();
        JSONObject  jsonObjectSampledVals = new JSONObject();

        try {
            jsonArrayMain.put(2);
            jsonArrayMain.put(sessionId);
            jsonArrayMain.put("StopTransaction");

            jsonObjectSampledVals.put("value",meterStop);
            jsonObjectSampledVals.put("unit","Wh");
            jsonObjectSampledVals.put("measurand","Energy.Active.Import.Register");
            jsonArraySampledVals.put(jsonObjectSampledVals);

            jsonObjectTranData.put("timestamp",currentUTC);
            jsonObjectTranData.put("sampledValue",jsonArraySampledVals);

            jsonArrayTranData.put(jsonObjectTranData);

            jsonObjectIn.put("timestamp",currentUTC);
            jsonObjectIn.put("transactionData",jsonArrayTranData);
            jsonObjectIn.put("meterStop",Math.round(Float.parseFloat(meterStop)));
            jsonObjectIn.put("transactionId",transactionId);
            jsonObjectIn.put("idTag",idTagstr);
            jsonObjectIn.put("reason",reason);



            jsonArrayMain.put(jsonObjectIn);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonArrayMain.toString();
    }

}

