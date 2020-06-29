package com.fahim.newapp.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.HashMap;


public class Preferences {

    private static final String PREFS_NAME = "MY_NEW_APP";
    private static SharedPreferences getSharedPreferenceInstanced(Context context) {
        return context.getSharedPreferences(getDefaultSharedPreferencesName(context),
                getDefaultSharedPreferencesMode());
    }

    private static int getDefaultSharedPreferencesMode() {
        return Context.MODE_PRIVATE;
    }

    private static String getDefaultSharedPreferencesName(Context context) {
        return context.getPackageName() + "_preferences";
    }


    public Preferences() {
        super();
    }


    public void putUsername(Context context, String username) {
        SharedPreferences.Editor editor = getSharedPreferenceInstanced(context).edit();
        editor.putString("putUsername", username);
        editor.apply();
    }
    public static String getUserName(Context context) {
        return getSharedPreferenceInstanced(context).getString("putUsername", null);
    }

    public void putSelectedStandardId(Context context, int id) {
        SharedPreferences.Editor editor = getSharedPreferenceInstanced(context).edit();
        editor.putInt("putSelectedStandardId", id);
        editor.apply();
    }
    public int getSelectedStandardId(Context context) {
        return getSharedPreferenceInstanced(context).getInt("putSelectedStandardId", 0);
    }
    public void putSelectedStandardName(Context context, String name) {
        SharedPreferences.Editor editor = getSharedPreferenceInstanced(context).edit();
        editor.putString("putSelectedStandardName", name);
        editor.apply();
    }
    public String getSelectedStandardName(Context context) {
        return getSharedPreferenceInstanced(context).getString("putSelectedStandardName", null);
    }
}
