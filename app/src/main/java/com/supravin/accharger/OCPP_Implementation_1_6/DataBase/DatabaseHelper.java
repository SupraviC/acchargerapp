package com.supravin.accharger.OCPP_Implementation_1_6.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Table Name
    static final String TABLE_OFFLINE = "OFFLINEREQUETS";
    static final String LOCALISTDATA = "LOCALLIST";
    static final String CHANGEAVAILABILITYDATA = "CHANGEAVAILABILITY";

    // Table columns
    public static final String _ID = "_id";
    public static final String REQUEST_STRING = "requestedString";
    public static final String REQUEST_IS = "requestedIs";
    public static final String TRANSACTION_ID = "transactionId";
    public static final String STATUS_FLAG = "statusFlag";

    public static final String _ID_ = "_id_";
    public static final String VERSIONNO = "versionNo";
    public static final String IDTAG = "idTag";
    public static final String PIDTAG = "pIdTag";
    public static final String STATUS_ = "status";
    public static final String TYPE_ = "type";
    public static final String EXPIRY_ = "expiry";


    public static final String _IDC = "_idC";
    public static final String CONNECTORID = "connectorId";
    public static final String TYPEIS = "typeIs";

    // Database Information
    private static final String DB_NAME = "NEWDBOFFLINE.DB";

    // database version
    private static final int DB_VERSION = 1;

    // Creating table query
    private static final String CREATE_TABLE = "create table " + TABLE_OFFLINE + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + REQUEST_STRING + " TEXT, "
            + REQUEST_IS + " TEXT, "
            +TRANSACTION_ID + " TEXT, "
            + STATUS_FLAG + " TEXT);";

    private static final String CREATE_TABLE_SECOND = "create table " + LOCALISTDATA + "(" + _ID_
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + VERSIONNO + " TEXT, "
            + IDTAG + " TEXT, "  +PIDTAG + " TEXT, "
            +STATUS_ + " TEXT, " +TYPE_ + " TEXT, "
            + EXPIRY_ + " TEXT);";

    private static final String CREATE_TABLE_THIRD =
            "create table " + CHANGEAVAILABILITYDATA + "(" + _IDC
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    CONNECTORID + " TEXT, " + TYPEIS + " TEXT);";

    DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_TABLE_SECOND);
        db.execSQL(CREATE_TABLE_THIRD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OFFLINE);
        db.execSQL("DROP TABLE IF EXISTS " + LOCALISTDATA);
        db.execSQL("DROP TABLE IF EXISTS " + CHANGEAVAILABILITYDATA);
        onCreate(db);
    }
}