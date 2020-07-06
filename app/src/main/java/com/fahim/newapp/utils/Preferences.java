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

    public void putNovelsId(Context context, int id) {
        SharedPreferences.Editor editor = getSharedPreferenceInstanced(context).edit();
        editor.putInt("putNovelsId", id);
        editor.apply();
    }

    public int getNovelsId(Context context) {
        return getSharedPreferenceInstanced(context).getInt("putNovelsId", 0);
    }

    public void putPosition(Context context, int id) {
        SharedPreferences.Editor editor = getSharedPreferenceInstanced(context).edit();
        editor.putInt("putPosition", id);
        editor.apply();
    }

    public int getPosition(Context context) {
        return getSharedPreferenceInstanced(context).getInt("putPosition", 0);
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

    public void putSelectedSubjectId(Context context, int id) {
        SharedPreferences.Editor editor = getSharedPreferenceInstanced(context).edit();
        editor.putInt("putSelectedSubjectId", id);
        editor.apply();
    }

    public int getSelectedSubjectId(Context context) {
        return getSharedPreferenceInstanced(context).getInt("putSelectedSubjectId", 0);
    }

    public void putSelectedSubjectName(Context context, String name) {
        SharedPreferences.Editor editor = getSharedPreferenceInstanced(context).edit();
        editor.putString("putSelectedSubjectName", name);
        editor.apply();
    }

    public String getSelectedSubjectName(Context context) {
        return getSharedPreferenceInstanced(context).getString("putSelectedSubjectName", null);
    }

    //BOOK
    public void putSelectedBookId(Context context, int id) {
        SharedPreferences.Editor editor = getSharedPreferenceInstanced(context).edit();
        editor.putInt("putSelectedBookId", id);
        editor.apply();
    }

    public int getSelectedBookId(Context context) {
        return getSharedPreferenceInstanced(context).getInt("putSelectedBookId", 0);
    }

    public void putSelectedBookName(Context context, String name) {
        SharedPreferences.Editor editor = getSharedPreferenceInstanced(context).edit();
        editor.putString("putSelectedBookName", name);
        editor.apply();
    }

    public String getSelectedBookName(Context context) {
        return getSharedPreferenceInstanced(context).getString("putSelectedBookName", "");
    }

    public void putSelectedBookLink(Context context, String id) {
        SharedPreferences.Editor editor = getSharedPreferenceInstanced(context).edit();
        editor.putString("putSelectedBookLink", id);
        editor.apply();
    }

    public String getSelectedBookLink(Context context) {
        return getSharedPreferenceInstanced(context).getString("putSelectedBookLink", "");
    }

    public void putSelectedBookCount(Context context, int id) {
        SharedPreferences.Editor editor = getSharedPreferenceInstanced(context).edit();
        editor.putInt("putSelectedBookCount", id);
        editor.apply();
    }

    public int getSelectedBookCount(Context context) {
        return getSharedPreferenceInstanced(context).getInt("putSelectedBookCount", 0);
    }


}
