package com.example.tgk.integration;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;

/**
 * Note:    The code in this file is based on the code of Project FragmentBasicsM25 that
 *          Todd Kelly has given us through his personal communication.
 * File:    CarbonFootprintDBAdapter.java
 * Author:  Shahid
 * Date:    April 2015
 */
public class CarbonFootprintDBAdapter {
    public static final String KEY_ROWID = "_id";
    public static final String KEY_RIDE = "ride";
    public static final String KEY_DISTANCE = "distance";
    public static final String KEY_DATE = "date";
    public static final String KEY_NOTES = "notes";
    public static final String KEY_EMISSION = "emission";

    private static final String TAG  = "CarbonFootprintDBAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "CO2EmissionDatabase";
    private static final String SQLITE_TABLE = "TripDetail";
    private static final int DATABASE_VERSION = 1;

    private  Context mCtx ;

    private static final String DATABASE_CREATE =
            "Create Table if not exists " + SQLITE_TABLE + " ( " +
                    KEY_ROWID + "," +
                    KEY_RIDE + "," +
                    KEY_DISTANCE + "," +
                    KEY_DATE + ", " +
                    KEY_NOTES + ", " +
                    KEY_EMISSION + " );" ;

    private  static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(TAG, DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("Drop Table If Exist " + SQLITE_TABLE);
            onCreate(db);
        }
    }

    public CarbonFootprintDBAdapter(Context ctx){
            this.mCtx = ctx;

    }

    public CarbonFootprintDBAdapter open() throws SQLException {

        mDbHelper = new DatabaseHelper(mCtx);

            mDb = mDbHelper.getWritableDatabase();


        return this;
    }

    public void close() throws SQLException{
        if(mDbHelper != null){
            mDbHelper.close();
        }
    }

    public long createCarbonFootprintInfo(String id, String ride, String distance, String date,
                                          String notes, String emission){
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ROWID, id);
        initialValues.put(KEY_RIDE, ride);
        initialValues.put(KEY_DISTANCE, distance);
        initialValues.put(KEY_DATE, date);
        initialValues.put(KEY_NOTES, notes);
        initialValues.put(KEY_EMISSION, emission);
        return mDb.insert(SQLITE_TABLE, null, initialValues);
    }

    public Cursor fetchAllData() throws SQLException{
        Cursor mCursor = mDb.query(SQLITE_TABLE, new String[]{KEY_ROWID,
                KEY_RIDE, KEY_DISTANCE, KEY_DATE, KEY_NOTES, KEY_EMISSION},
            null, null, null, null, null);
        if(mCursor != null){
            mCursor.moveToFirst();
        }
        return mCursor;
    }


    public boolean deleteInfo(String id){
        int doneDelete ;
        doneDelete = mDb.delete(SQLITE_TABLE, KEY_ROWID + "=?",
                new String[]{id});

        return doneDelete > 0;
    }

    public boolean rowCount(){
        Cursor mCursor = null;
        boolean rowCount = true;
        mCursor = mDb.rawQuery("Select * From " + SQLITE_TABLE, null);
        int count = mCursor.getCount();

        if(count == 0){
             rowCount = false;
        }

        return rowCount;
    }

    public Cursor fetchDataById(String id){
        Cursor mCursor = null;
        Log.d(TAG, "id is " + id);


        mCursor = mDb.rawQuery("Select * From " + SQLITE_TABLE + " Where _id=?",
                new String[]{id});

        if(mCursor != null){
            mCursor.moveToFirst();
        }

        return mCursor;
    }

    public Cursor fetchIds(){
        Cursor mCursor = null;

        mCursor = mDb.rawQuery("Select _id From " + SQLITE_TABLE, null);

        return mCursor;
    }

}
