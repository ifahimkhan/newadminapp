package com.fahim.newapp.utils;

import android.content.Context;
import android.os.Debug;
import android.util.Log;

import java.io.File;

public class FileUtils {

    public static boolean isDownladable(String remainingPath) {


        if (remainingPath.endsWith(".pdf") || remainingPath.endsWith(".zip")) {
            Log.e("TAG", "isDownladable: " + true+remainingPath);
            return true;
        } else{
            Log.e("TAG", "isDownladable: " + false+remainingPath);

            return false;
        }

    }

    public static String getFileExtension(String remainingPath) {
        if (remainingPath.lastIndexOf(".") != -1 && remainingPath.lastIndexOf(".") != 0)
            return remainingPath.substring(remainingPath.lastIndexOf(".") + 1);
        else return "";
    }

    public static File getDataDir(Context context) {

        String path = context.getFilesDir().getAbsolutePath() + "/SampleZip";

        File file = new File(path);

        if (!file.exists()) {

            file.mkdirs();
        }

        return file;
    }

    public static File getDataDir(Context context, String folder) {

        String path = context.getFilesDir().getAbsolutePath() + "/" + folder;

        File file = new File(path);

        if (!file.exists()) {

            file.mkdirs();
        }

        return file;
    }
}