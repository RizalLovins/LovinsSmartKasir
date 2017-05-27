package com.rizal.lovins.smartkasir.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferenceUtil {
    private static SharedPreferences sharedPreferences;

    private static Editor editor;
    public static String PREF_NAME = "Lovins";

    public static void createLoginSession(Context context, String username) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, 0);
        editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.apply();
    }

    public static void logout(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, 0);
        editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}


