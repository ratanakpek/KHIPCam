package com.kshrd.ipcam.entities;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by rina on 1/22/17.
 */

/**
 * @Class UserPreference
 * @Used for save session ipcam app
 */
public class UserPreference {

    private static final String PREFS_NAME ="USER_PREF";

     public boolean setPreference(Context c, String value, String key) {
        SharedPreferences settings = c.getSharedPreferences(PREFS_NAME, 0);
        settings = c.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        return editor.commit();
    }

     public String getPreference(Context c, String key) {
        SharedPreferences settings = c.getSharedPreferences(PREFS_NAME, 0);
        settings = c.getSharedPreferences(PREFS_NAME, 0);
        String value = settings.getString(key, "");
        return value;
    }
}
