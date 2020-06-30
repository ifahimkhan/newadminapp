package com.fahim.newapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.fahim.newapp.application.MyApplication;
import com.fahim.newapp.holder.BookHolder;


public class DBHelpers extends SQLiteOpenHelper  implements DataBaseInterface {

    private static final String TAG = DBHelpers.class.getSimpleName();

    private static final String DB_NAME = "myAppDatabase.db";

    private static final int DB_VERSION_NO = 2;


    private static DBHelpers mInstance = null;
    public static final Object lock = new Object();


    DBHelpers(Context context) {


        super(context, DB_NAME, null, DB_VERSION_NO);
        if (context==null){
            Log.e(TAG, "getInstance: NULL" );
        }else{
            Log.e(TAG, "getInstance: NOT NULL" );

        }
    }


    public static DBHelpers getInstance() {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (mInstance == null) {
            //System.out.println("context=" + TagitApplication.getInstance() + "");

            mInstance = new DBHelpers(MyApplication.getInstance());
        }

        return mInstance;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL(new StringBuilder(" CREATE TABLE ").append(TABLE_STANDARD_HOLDER)
                .append(" (").append(_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
                .append(STANDARD_ID).append(" INTEGER,")
                .append(STANDARD_NAME).append(" VARCHAR(20) ")
                .append(");").toString());


        db.execSQL(new StringBuilder(" CREATE TABLE ").append(TABLE_SUBJECT_HOLDER)
                .append(" (").append(_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
                .append(SUBJECT_ID).append(" INTEGER,")
                .append(SUBJECT_STANDARD_ID).append(" INTEGER, ")
                .append(SUBJECT_NAME).append(" VARCHAR(20)")
                .append(");").toString());

        db.execSQL(new StringBuilder(" CREATE TABLE ").append(TABLE_BOOKS_HOLDER)
                .append(" (").append(_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
                .append(BOOKS_ID).append(" INTEGER,")
                .append(BOOKS_STANDARD_ID).append(" VARCHAR(20),")
                .append(Books_SUBJECT_ID).append(" VARCHAR(20),")
                .append(BOOKS_VIEW_COUNT).append(" VARCHAR(20),")
                .append(BOOKS_LINK).append(" VARCHAR(255) ")
                .append(");").toString());



        upgradeVersion2(db);


    }

    private void upgradeVersion2(SQLiteDatabase db) {

        db.execSQL("ALTER TABLE " + TABLE_BOOKS_HOLDER + " ADD " + BOOKS_NAME + " VARCHAR(20);");

    }

/*
    private void upgradeVersion3(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVE_HOLDER);
        db.execSQL(" CREATE TABLE " + TABLE_MOVE_HOLDER +
                " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                STOCK_ASSETID + " VARCHAR(20), " +
                STOCK_LOCATION + " VARCHAR(20), " +
                STOCK_BUILDING + " VARCHAR(20), " +
                STOCK_FLOOR + " VARCHAR(20), " +
                MOVE_REASON + " VARCHAR(20)" +
                ");");

        db.execSQL("ALTER TABLE " + TABLE_TOTAL_STOCK_HOLDER + " ADD " + STOCK_ROOM + " VARCHAR(20);");
        db.execSQL("ALTER TABLE " + TABLE_MISSING_HOLDER + " ADD " + STOCK_ROOM + " VARCHAR(20);");
        db.execSQL("ALTER TABLE " + TABLE_TEMP_STOCK_HOLDER + " ADD " + STOCK_ROOM + " VARCHAR(20);");
        db.execSQL("ALTER TABLE " + TABLE_PROCESS_STOCK_HOLDER + " ADD " + STOCK_ROOM + " VARCHAR(20);");
        db.execSQL("ALTER TABLE " + TABLE_FOUND_HOLDER + " ADD " + STOCK_ROOM + " VARCHAR(20);");
        db.execSQL("ALTER TABLE " + TABLE_EXTRA_HOLDER + " ADD " + STOCK_ROOM + " VARCHAR(20);");
        db.execSQL("ALTER TABLE " + TABLE_MISSMATCH_HOLDER + " ADD " + STOCK_ROOM + " VARCHAR(20);");
        db.execSQL("ALTER TABLE " + TABLE_ASSET_STOCK_HOLDER + " ADD " + STOCK_ROOM + " VARCHAR(20);");

    }
*/


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

       /* if (oldVersion < 4) {
            upgradeVersion3(db);
        }*/
        Log.e(TAG, "onUpgrade: "+oldVersion);
       if (oldVersion<2){
           upgradeVersion2(db);
       }
    }


}
