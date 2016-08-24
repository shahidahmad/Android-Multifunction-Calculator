package com.example.tgk.integration;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;

/**
 * Created by Zhihui on 2015-04-11.
 */
public class RestaurantDBAdapter {
    public static final String KEY_ROWID = "_id";
    public static final String KEY_PAYMENT = "payment";
    public static final String KEY_PRICE = "price";
    public static final String KEY_TIP = "tips";
    public static final String KEY_NOTES = "notes";
    public static final String KEY_TOTAL = "total";

    private static final String TAG  = "RetaurantDBAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "RestaurantData";
    private static final String SQLITE_TABLE = "TipDetials";
    private static final int DATABASE_VERSION = 1;

    private final Context mCtx;

    private static final String DATABASE_CREATE =
            "Create Table if not exists " + SQLITE_TABLE + " ( " +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement, " +
                    KEY_PAYMENT + "," +
                    KEY_PRICE + "," +
                    KEY_TIP + ", " +
                    KEY_NOTES + ", " +
                    KEY_TOTAL + " );" ;

    private static class DatabaseHelper extends SQLiteOpenHelper {
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

    public RestaurantDBAdapter(Context ctx){
        this.mCtx = ctx;
    }

    public RestaurantDBAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        if(mDbHelper != null){
            mDbHelper.close();
        }
    }

    public long createRestaurantInfo(String payment, String price, String tip,
                                          String notes, String total){
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_PAYMENT, payment);
        initialValues.put(KEY_PRICE, price);
        initialValues.put(KEY_TIP, tip);
        initialValues.put(KEY_NOTES, notes);
        initialValues.put(KEY_TOTAL, total);
        return mDb.insert(SQLITE_TABLE, null, initialValues);
    }

    public Cursor fetchAllData(){
        Cursor mCursor = mDb.query(SQLITE_TABLE, new String[]{KEY_ROWID,
                        KEY_PAYMENT, KEY_PRICE, KEY_TIP, KEY_NOTES, KEY_TOTAL},
                null, null, null, null, null);
        if(mCursor != null){
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public boolean deleteInfo(){
        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE, null, null);
        return doneDelete > 0;
    }
    
    
    
}
