/* Copyright © 2011-2013 mysamplecode.com, All rights reserved.
  This source code is provided to students of CST2335 for educational purposes only.
 */
package com.example.tgk.integration;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ContactDbAdapter {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_FIRSTNAME = "firstName";
    public static final String KEY_LASTNAME = "lastName";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_NOTE = "note";

    private static final String TAG = "ContactDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "Contacts";
    private static final String SQLITE_TABLE = "List";
    private static final int DATABASE_VERSION = 1;

    private final Context mCtx;

    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_FIRSTNAME + "," +
                    KEY_LASTNAME + "," +
                    KEY_PHONE + "," +
                    KEY_EMAIL + "," +
                    KEY_NOTE + "," +
                    " UNIQUE (" + KEY_FIRSTNAME + ", " + KEY_LASTNAME + "));";

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
            db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE);
            onCreate(db);
        }
    }

    public ContactDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public ContactDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public long createContact(String firstName, String lastName,
                              String phone, String email, String note) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_FIRSTNAME, firstName);
        initialValues.put(KEY_LASTNAME, lastName);
        initialValues.put(KEY_PHONE, phone);
        initialValues.put(KEY_EMAIL, email);
        initialValues.put(KEY_NOTE, note);

        return mDb.insert(SQLITE_TABLE, null, initialValues);
    }

    public boolean deleteAllContacts() {

        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE, null , null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;

    }

    public Cursor fetchContactById(long id) throws SQLException {
    Cursor mCursor = null;
    Log.d(TAG, "id is " + id);
    mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ROWID,
                    KEY_FIRSTNAME, KEY_LASTNAME, KEY_PHONE, KEY_EMAIL, KEY_NOTE},
            KEY_ROWID + " = ?", new String[] {String.valueOf(id)}, null, null, null);
    if (mCursor != null){
        mCursor.moveToFirst();
    }
    return mCursor;
}
    public Cursor fetchContactsByName(String inputText) throws SQLException {
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        if (inputText == null  ||  inputText.length () == 0)  {
            mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ROWID,
                            KEY_FIRSTNAME, KEY_LASTNAME, KEY_PHONE, KEY_EMAIL, KEY_NOTE},
                    null, null, null, null, null);

        }
        else {
            mCursor = mDb.query(true, SQLITE_TABLE, new String[] {KEY_ROWID,
                            KEY_FIRSTNAME, KEY_LASTNAME, KEY_PHONE, KEY_EMAIL, KEY_NOTE},
                    KEY_LASTNAME + " like '%" + inputText + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public long deleteContactById(String id){
        return mDb.delete(SQLITE_TABLE, KEY_ROWID + " = " + id, null);
    }

    public Cursor fetchAllContacts() {

        Cursor mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ROWID,
                        KEY_FIRSTNAME, KEY_LASTNAME, KEY_PHONE, KEY_EMAIL, KEY_NOTE},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public void insertSomeContacts() {
        createContact("Steve", "Messina", "6139839104","stevemessina@hotmail.com", "62");
        createContact("Nick", "Briglio", "6138508776", "nickbriglio@hotmail.com", "19");

    }

}
