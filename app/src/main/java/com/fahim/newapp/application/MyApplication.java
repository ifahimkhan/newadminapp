package com.fahim.newapp.application;

import android.app.Application;
import android.util.Log;

import com.fahim.newapp.database.DAO;

public class MyApplication extends Application  {
    private static MyApplication mInstance;

    public static MyApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance=MyApplication.this;
        new DAO();
        Log.e("TAG", "onCreate: "+"APPLICATION" );
        if (mInstance==null){
            Log.e("TAG", "onCreate: "+"NULL" );
        }else{
            Log.e("TAG", "onCreate: "+" Not NULL" );

        }
    }
}
