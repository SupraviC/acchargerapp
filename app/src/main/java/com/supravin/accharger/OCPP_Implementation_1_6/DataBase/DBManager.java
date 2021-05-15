package com.supravin.accharger.OCPP_Implementation_1_6.DataBase;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.WebSocket;

public class DBManager {

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String requestString,String requestIs, String statusFlag,String tranId) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.REQUEST_STRING, requestString);
        contentValue.put(DatabaseHelper.REQUEST_IS, requestIs);
        contentValue.put(DatabaseHelper.STATUS_FLAG, statusFlag);
        contentValue.put(DatabaseHelper.TRANSACTION_ID, tranId);
        database.insert(DatabaseHelper.TABLE_OFFLINE, null, contentValue);
        Log.e("CHECKINTERNETNEW","Inserted"+requestString);
        Log.e("IFSTARTFOUND","Inserted"+requestString);
    }

/*                                    for (int i=0;i<jsonArrayAuthList.length();i++){
                                        if (jsonArrayAuthList.getJSONObject(0) != null) {
                                            JSONObject jsonOIDTag = jsonArrayAuthList.getJSONObject(0);
                                            if (jsonOIDTag.has("idTag")) {
                                                idTag = jsonOIDTag.getString("idTag");
                                            }

                                            if (jsonOIDTag.has("idTagInfo")) {

                                                JSONObject jsonOIdTagInfo = jsonOIDTag.getJSONObject("idTagInfo");

                                                if (jsonOIdTagInfo.has("expiryDate")) {
                                                    expiry = jsonOIdTagInfo.getString("expiryDate");
                                                }

                                                if (jsonOIdTagInfo.has("parentIdTag")) {
                                                    pIdTag = jsonOIdTagInfo.getString("parentIdTag");
                                                }

                                                if (jsonOIdTagInfo.has("status")) {
                                                    status_ = jsonOIdTagInfo.getString("status");
                                                }
                                            }
                                            Log.e("CHECKINTERNETNEW","IDTAGISS"+idTag);

                                            dbManager.insertLocalList(versionNo, idTag, pIdTag, status_, type_, expiry);

                                        }
                                    }
*/

    public void insertLocalList2nd(JSONArray jsonArrayAuthList,
                                   String type_, String versionNo) {
        String idTag=null;
        String expiry=null;
        String pIdTag=null;
        String status_=null;
        try{
            for (int k=0;k<jsonArrayAuthList.length();k++){

                if (jsonArrayAuthList.getJSONObject(k) != null) {
                    JSONObject jsonOIDTag=jsonArrayAuthList.getJSONObject(k);
                    Log.e("CHECKINTERNETNEW","jsonOIDTag count "+k+" jsonOIDTag "+jsonOIDTag);
                    if (jsonOIDTag.has("idTag")) {
                        idTag = jsonOIDTag.getString("idTag");
                        Log.e("CHECKINTERNETNEW","idTag count "+k+" idTag "+idTag);
                    }

                    if (jsonOIDTag.has("idTagInfo")) {

                        JSONObject jsonOIdTagInfo = jsonOIDTag.getJSONObject("idTagInfo");

                        if (jsonOIdTagInfo.has("expiryDate")) {
                            expiry = jsonOIdTagInfo.getString("expiryDate");
                        }

                        if (jsonOIdTagInfo.has("parentIdTag")) {
                            pIdTag = jsonOIdTagInfo.getString("parentIdTag");
                        }

                        if (jsonOIdTagInfo.has("status")) {
                            status_ = jsonOIdTagInfo.getString("status");
                            Log.e("CHECKINTERNETNEW","status_ count "+k+" status_ "+idTag);

                        }
                    }


                    ContentValues contentValue = new ContentValues();

                    ContentValues contentValueToUpdate = new ContentValues();
                    ContentValues contentValueToUpdate2nd = new ContentValues();

                    contentValueToUpdate2nd.put(DatabaseHelper.VERSIONNO, versionNo);
                    contentValueToUpdate2nd.put(DatabaseHelper.IDTAG, idTag);
                    contentValueToUpdate2nd.put(DatabaseHelper.PIDTAG, pIdTag);
                    contentValueToUpdate2nd.put(DatabaseHelper.STATUS_, status_);
                    contentValueToUpdate2nd.put(DatabaseHelper.TYPE_, type_);
                    contentValueToUpdate2nd.put(DatabaseHelper.EXPIRY_, expiry);

                    contentValueToUpdate.put(DatabaseHelper.VERSIONNO, versionNo);

                    contentValue.put(DatabaseHelper.VERSIONNO, versionNo);
                    contentValue.put(DatabaseHelper.IDTAG, idTag);
                    contentValue.put(DatabaseHelper.PIDTAG, pIdTag);
                    contentValue.put(DatabaseHelper.STATUS_, status_);
                    contentValue.put(DatabaseHelper.TYPE_, type_);
                    contentValue.put(DatabaseHelper.EXPIRY_, expiry);


                    if (type_.equals("Differential")){

                        if (checkIdTagIsPresent(idTag)!=0) {
                            int i=database.update(DatabaseHelper.LOCALISTDATA,
                                    contentValueToUpdate2nd,null,
                                    null);
                            Log.e("CHECKINTERNETNEW","DIFFUPDATEDTAG"+i);
                        }else {
                            long j=database.insert(DatabaseHelper.LOCALISTDATA,
                                    null, contentValue);
                            Log.e("CHECKINTERNETNEW","DIFFINSERTEDTAG"+j);
                        }

                       /* int i=database.update(DatabaseHelper.SECOND_TABLE_NAME, contentValueToUpdate,null, null);
                        Log.e("CHECKINTERNETNEW","DIFFUPDATED"+i);*/

                    }else {

                        int s=database.delete(DatabaseHelper.LOCALISTDATA, null, null);
                        Log.e("CHECKINTERNETNEW","FULLDELETED"+s);
                        long l=database.insert(DatabaseHelper.LOCALISTDATA,


                                null, contentValue);
                        Log.e("CHECKINTERNETNEW","FULLINSERTED"+l);

                    }
                    //database.insert(DatabaseHelper.SECOND_TABLE_NAME, null, contentValue);
                    Log.e("CHECKINTERNETNEW","LOCAL_LIST_Inserted"+versionNo);
                    Log.e("CHECKINTERNETNEW","LOCAL_LIST_Inserted"+idTag);

                    Log.e("CHECKINTERNETNEW","IDTAGISS"+idTag);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void insertLocalList(String versionId,String idTag, String pIdTag,String status_,String type_,String expiry) {
        ContentValues contentValue = new ContentValues();

        ContentValues contentValueToUpdate = new ContentValues();
        ContentValues contentValueToUpdate2nd = new ContentValues();

        contentValueToUpdate2nd.put(DatabaseHelper.VERSIONNO, versionId);
        contentValueToUpdate2nd.put(DatabaseHelper.IDTAG, idTag);
        contentValueToUpdate2nd.put(DatabaseHelper.PIDTAG, pIdTag);
        contentValueToUpdate2nd.put(DatabaseHelper.STATUS_, status_);
        contentValueToUpdate2nd.put(DatabaseHelper.TYPE_, type_);
        contentValueToUpdate2nd.put(DatabaseHelper.EXPIRY_, expiry);

        contentValueToUpdate.put(DatabaseHelper.VERSIONNO, versionId);

        contentValue.put(DatabaseHelper.VERSIONNO, versionId);
        contentValue.put(DatabaseHelper.IDTAG, idTag);
        contentValue.put(DatabaseHelper.PIDTAG, pIdTag);
        contentValue.put(DatabaseHelper.STATUS_, status_);
        contentValue.put(DatabaseHelper.TYPE_, type_);
        contentValue.put(DatabaseHelper.EXPIRY_, expiry);


        if (type_.equals("Differential")){

            if (checkIdTagIsPresent(idTag)!=0) {
                int i=database.update(DatabaseHelper.LOCALISTDATA, contentValueToUpdate2nd,null, null);
                Log.e("CHECKINTERNETNEW","DIFFUPDATEDTAG"+i);
            }else {
                long j=database.insert(DatabaseHelper.LOCALISTDATA, null, contentValue);
                Log.e("CHECKINTERNETNEW","DIFFINSERTEDTAG"+j);
            }

            int i=database.update(DatabaseHelper.LOCALISTDATA, contentValueToUpdate,null, null);
            Log.e("CHECKINTERNETNEW","DIFFUPDATED"+i);

        }else {

            int k=database.delete(DatabaseHelper.LOCALISTDATA, null, null);
            Log.e("CHECKINTERNETNEW","FULLDELETED"+k);
            long l=database.insert(DatabaseHelper.LOCALISTDATA, null, contentValue);
            Log.e("CHECKINTERNETNEW","FULLINSERTED"+l);
        }
        //database.insert(DatabaseHelper.SECOND_TABLE_NAME, null, contentValue);
        Log.e("CHECKINTERNETNEW","LOCAL_LIST_Inserted"+versionId);
        Log.e("CHECKINTERNETNEW","LOCAL_LIST_Inserted"+idTag);
    }

    private int checkIdTagIsPresent(String idTag) {

        Cursor cursor =database.rawQuery("SELECT * FROM "
                +DatabaseHelper.LOCALISTDATA +" WHERE "+
                DatabaseHelper.IDTAG+" = "+idTag,null);
        try {
            return cursor.getCount();
        }catch (Exception e){
            cursor.close();
            return 0;
        }finally {
         if (cursor!=null){
             cursor.close();
         }
        }
    }


    public void insertChangeAvailability(String connectorID, String typeIS) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.CONNECTORID, connectorID);
        contentValues.put(DatabaseHelper.TYPEIS, typeIS);

        try {
            if (fetchConnectorStatus(connectorID)>0){
                int i=database.update(DatabaseHelper.CHANGEAVAILABILITYDATA, contentValues,
                        DatabaseHelper.CONNECTORID + " = " + connectorID,
                        null);
                Log.e("CHECKINTERNETNEW","CHANGE_Availability "+i+"th ROW UPDATED"+connectorID);
                Log.e("CHECKINTERNETNEW","CHANGE_Availability "+i+"th ROW UPDATED"+typeIS);
            }else {
                long i=database.insert(DatabaseHelper.CHANGEAVAILABILITYDATA, null, contentValues);
                Log.e("CHECKINTERNETNEW","CHANGE_Availability"+i+"th ROW INSERTED"+connectorID);
                Log.e("CHECKINTERNETNEW","CHANGE_Availability"+i+"th ROW INSERTED"+typeIS);
            }
            Log.e("CHECKINTERNETNEW","CHANGE_AvailabilityTRYBLOCK"+typeIS);

        }catch (Exception e){
            Log.e("CHECKINTERNETNEW","CHANGE_AvailabilityTRYBLOCKCATHCBLOCK"+e);
        }
    }

    @SuppressLint("Recycle")
    public String fetchConnectorAvailability(String connectorId) {
        Cursor cursor = database.rawQuery("SELECT "+DatabaseHelper.TYPEIS+" FROM "
                +DatabaseHelper.CHANGEAVAILABILITYDATA +" WHERE "
                +DatabaseHelper.CONNECTORID+" = "+connectorId,null);
        if (cursor!=null
        && cursor.moveToFirst()){
            return cursor.getString(cursor.getColumnIndex(DatabaseHelper.TYPEIS));
        }else {
            return "Operative";
        }
    }

    @SuppressLint("Recycle")
    public void checkIsExpiredOrNot(String IDTAG) {
        Cursor cursor = database.rawQuery("SELECT * FROM "+DatabaseHelper.LOCALISTDATA
                +" WHERE "+DatabaseHelper.EXPIRY_ +">= date('now')",null);
        if (cursor.getCount()>0){
            if (cursor.moveToFirst())
            do {
                Log.e("CURSORCOUNT","DATETIME --> "+cursor.getString(cursor.getColumnIndex(DatabaseHelper.EXPIRY_)));
                Log.e("CURSORCOUNT","IDTAG --> "+cursor.getString(cursor.getColumnIndex(DatabaseHelper.IDTAG)));
            }while (cursor.moveToNext());
        }
    }


    @SuppressLint("Recycle")
    private int fetchConnectorStatus(String connectorId) {

        return database.rawQuery("SELECT "+DatabaseHelper.TYPEIS+" FROM "
                +DatabaseHelper.CHANGEAVAILABILITYDATA +" WHERE "
                +DatabaseHelper.CONNECTORID+" = "+connectorId,null).getCount();
    }


    public Cursor fetch(WebSocket webSocketokhttp) {
        /*String[] columns = new String[] { DatabaseHelper._ID,
                DatabaseHelper.REQUEST_STRING,
                DatabaseHelper.TRANSACTION_ID,
                DatabaseHelper.STATUS_FLAG };
      return database.query(DatabaseHelper.TABLE_OFFLINE, columns,
              DatabaseHelper.STATUS_FLAG + "='True'",
              null, null, null, null);*/
       /* String flag="True";
        String query="SELECT * FROM "
                +DatabaseHelper.TABLE_OFFLINE*/
       /*+" WHERE "
                +DatabaseHelper.STATUS_FLAG+"='"+flag+"'"*/
       /*;
        Log.e("CHECKINTERNETNEW","FETCING Q "+query);

        return database.rawQuery(query,null);*/

        String[] columns = new String[] { DatabaseHelper._ID,
                DatabaseHelper.REQUEST_STRING,
                DatabaseHelper.TRANSACTION_ID,
                DatabaseHelper.STATUS_FLAG };

        Cursor cursor = database.query(DatabaseHelper.TABLE_OFFLINE, columns,
                /*DatabaseHelper.REQUEST_IS + "='START' AND "
                        +*/
                DatabaseHelper.STATUS_FLAG+"='True' ",
                null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String data2=cursor.getString(cursor.getColumnIndex(DatabaseHelper.REQUEST_STRING));
                String tId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TRANSACTION_ID));
                String id = cursor.getString(cursor.getColumnIndex(DatabaseHelper._ID));
                Log.e("DATAISSSS"," data2---> "+data2);
                Log.e("DATAISSSS"," tId---> "+tId);
                Log.e("DATAISSSS"," id---> "+id);


                //webSocketokhttp=openConnection();
                webSocketokhttp.send(cursor.getString(cursor
                        .getColumnIndex(DatabaseHelper.REQUEST_STRING)));

                Log.e("DATAISSSS"," UPLOADING--> "+
                        cursor.getString(cursor.
                                getColumnIndex(DatabaseHelper.REQUEST_STRING)));

                update(Long.parseLong(id),data2,"False",tId);


                } while (cursor.moveToNext());
        }
       // cursor.close();

        return  cursor;
    }


    @SuppressLint("Recycle")
    public String  fetchLocalList(String enteredOTP) {

        Cursor cursorFirst=database.rawQuery("SELECT "
                +DatabaseHelper.STATUS_+" , "+DatabaseHelper.EXPIRY_+" FROM "+DatabaseHelper.LOCALISTDATA
                +" WHERE "+DatabaseHelper.IDTAG+" = "+enteredOTP,null);

        String actualResult="Invalid2";

        try {
            if (cursorFirst.getCount()!=0){
                if (cursorFirst.moveToFirst()) {
                    do {
                        try {

                            actualResult= cursorFirst.getString(cursorFirst
                                    .getColumnIndex(DatabaseHelper.STATUS_));
                            String testCreationDate = cursorFirst.getString(cursorFirst
                                    .getColumnIndex(DatabaseHelper.EXPIRY_));
                            Log.e("CHECKINTERNETNEW "," EXPIRY_ --> "+cursorFirst.getString(cursorFirst
                                    .getColumnIndex(DatabaseHelper.EXPIRY_)));
                            Log.e("CHECKINTERNETNEW "," STATUS --> "+cursorFirst.getString(cursorFirst
                                    .getColumnIndex(DatabaseHelper.STATUS_)));


                            SimpleDateFormat sdf = new SimpleDateFormat(
                                    "yyyy-MM-dd", Locale.getDefault());
                            try {
                                Date d = sdf.parse(testCreationDate);
                                Date c = new Date();
                                String date = sdf.format(c);
                                Date dateC = sdf.parse(date);

                                if (d!=null){
                                    if(!d.equals(dateC) && d.before(dateC)){
                                        Log.e("CHECKINTERNETNEW","before "+dateC);
                                        actualResult="Expired";
                                    }else{
                                        Log.e("CHECKINTERNETNEW","after "+dateC);
                                        actualResult= cursorFirst.getString(cursorFirst
                                                .getColumnIndex(DatabaseHelper.STATUS_));
                                    }
                                }
                            } catch (Exception ex) {
                                Log.e("CHECKINTERNETNEW ", "EXP--> DATE "+ex);
                                Log.e("QRTIMESTAMP ", "EXP--> DATE "+ex);
                            }

                        }catch (Exception e){
                            Log.e("CHECKINTERNETNEW ", "1EXP--> DATE "+e);

                            e.printStackTrace();
                        }
                    } while (cursorFirst.moveToNext());


                    return  actualResult;
                }

            }else {
                return "Invalid";
            }
        }catch (Exception e){
            Log.e("CHECKINTERNETNEW ", "2EXP--> DATE "+e);

            cursorFirst.close();
            return "Invalid";
        }finally {
            if (cursorFirst!=null)
                cursorFirst.close();
        }

        /*Cursor cursor = database.rawQuery("SELECT * FROM "
                + DatabaseHelper.LOCALISTDATA +" WHERE "
                + DatabaseHelper.IDTAG +" = '"+enteredOTP*//*+"' AND "
                +DatabaseHelper.EXPIRY_ +" >= date('now') "*//*,null);

        if (cursor != null
        && cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex(DatabaseHelper.STATUS_));
        }else {
            return "Invalid";
        }*/

        Log.e("CHECKINTERNETNEW","actualResult --> "+actualResult);

        return actualResult;


    }


    public Cursor fetchCount() {

        String[] columns = new String[] { DatabaseHelper._ID, DatabaseHelper.REQUEST_STRING,
                DatabaseHelper.TRANSACTION_ID, DatabaseHelper.STATUS_FLAG };
        Cursor cursor = database.query(DatabaseHelper.TABLE_OFFLINE, columns,
                DatabaseHelper.STATUS_FLAG + "='True'", null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

       /* if (cursor != null) {
           int i = database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.TRANSACTION_ID + "=" + cursor.getColumnIndex(DatabaseHelper.TRANSACTION_ID), null);
            Log.e("INSERTINTODB","--> FETCHFRST-->i "+i);
            Log.e("INSERTINTODB","--> FETCHGTHRD-->STRINGREQ "+cursor.getString(cursor.getColumnIndex(DatabaseHelper.REQUEST_STRING)));

        }*/

        return cursor;
    }


    public Cursor fetchSEC() {

        String[] columns = new String[] { DatabaseHelper._ID,
                DatabaseHelper.REQUEST_STRING,
                DatabaseHelper.TRANSACTION_ID,
                DatabaseHelper.STATUS_FLAG };

        Log.e("CHECKINTERNETNEW","FETCHING WHOLESTRAN");
        return  database.query(DatabaseHelper.TABLE_OFFLINE, columns,
                DatabaseHelper.REQUEST_IS + "='START' AND "
                        +DatabaseHelper.STATUS_FLAG+"='True' LIMIT 1 ",
                null, null, null, null);
    }

    public Cursor fetchTHRD(String tId) {

        String[] columns = new String[] {
                DatabaseHelper._ID,
                DatabaseHelper.REQUEST_STRING,
                DatabaseHelper.TRANSACTION_ID,
                DatabaseHelper.STATUS_FLAG };

        Cursor cursor = database.query(DatabaseHelper.TABLE_OFFLINE, columns,
                /*DatabaseHelper.REQUEST_IS + "<>'START' AND "+*/
                DatabaseHelper.STATUS_FLAG+"='True' AND "
                        +DatabaseHelper.TRANSACTION_ID+"='"+tId+"'",
                null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount()>0){

                final String data = cursor.getString(
                        cursor.getColumnIndex(DatabaseHelper.REQUEST_STRING));
                final String id = cursor.getString(
                        cursor.getColumnIndex(DatabaseHelper._ID));

                int updatedData = update(Long.parseLong(id),data,"False",tId);
                Log.e("INSERTINTODB","UPDATEIS"+updatedData);

            }else {

                Log.e("INSERTINTODB","UPDATEIS - ZEEROOOOO");

            }
        }
        return cursor;
    }


/*
    public int update(long _id, String name, String desc, String transID) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.REQUEST_STRING, name);
        contentValues.put(DatabaseHelper.STATUS_FLAG, desc);
        contentValues.put(DatabaseHelper.TRANSACTION_ID, transID);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper._ID + " = " + _id, null);
        return i;
    }
*/

    public int update(long _id, String name, String desc, String transID) {
        ContentValues contentValues = new ContentValues();
        //contentValues.put(DatabaseHelper.REQUEST_STRING, "");
        contentValues.put(DatabaseHelper.STATUS_FLAG, desc);
        contentValues.put(DatabaseHelper.TRANSACTION_ID, transID);
        return database.update(DatabaseHelper.TABLE_OFFLINE, contentValues, DatabaseHelper._ID + " = " + _id, null);
    }


    public void delete(long _id) {
        database.delete(DatabaseHelper.TABLE_OFFLINE, DatabaseHelper._ID + "=" + _id, null);
    }

}
