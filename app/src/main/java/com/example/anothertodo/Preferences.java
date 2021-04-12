package com.example.anothertodo;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;


public class Preferences {
    private static SharedPreferences sSharedPref;
    private static Preferences sInstance;
    private static final String KEY_SETTINGS_USE_CLOUD = "Setttings.UseCloud";
    private static final String KEY_SETTINGS_USER_EMAIL = "Settings.UserEmal";
    private static final String KEY_SETTINGS_USER_AVATAR = "Settings.UserAvatar";


    public static String getKeySettingsUserAvatar() {
        return KEY_SETTINGS_USER_AVATAR;
    }

    public static String getKeySettingsUserEmail() {
        return KEY_SETTINGS_USER_EMAIL;
    }

    public static String getKeySettingsUseCloud() {
        return KEY_SETTINGS_USE_CLOUD;
    }

    private Preferences(Context context) {
        sSharedPref = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
    }

    public static Preferences getInstance(@NonNull Context context) {
        Preferences instance = sInstance;
        if (instance == null) {
            synchronized (Preferences.class) {
                if (sInstance == null) {
                    instance = new Preferences(context);
                    sInstance = instance;
                }
            }
        }
        return instance;
    }

    public String read(String key, String defValue) {
        return sSharedPref.getString(key, defValue);
    }

    public boolean read(String key, boolean defValue) {
        return sSharedPref.getBoolean(key, defValue);
    }

    public int read(String key, int defValue) {
        return sSharedPref.getInt(key, defValue);
    }

    public void removeSetting(String key) {
        SharedPreferences.Editor prefsEditor = sSharedPref.edit();
        prefsEditor.remove(key);
        prefsEditor.commit();
    }

    public void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        sSharedPref.registerOnSharedPreferenceChangeListener(listener);
    }

    public void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        sSharedPref.unregisterOnSharedPreferenceChangeListener(listener);
    }

    public void write(String key, String value) {
        SharedPreferences.Editor prefsEditor = sSharedPref.edit();
        prefsEditor.putString(key, value);
        prefsEditor.commit();
    }

    public void write(String key, boolean value) {
        SharedPreferences.Editor prefsEditor = sSharedPref.edit();
        prefsEditor.putBoolean(key, value);
        prefsEditor.commit();
    }

    public void write(String key, Integer value) {
        SharedPreferences.Editor prefsEditor = sSharedPref.edit();
        prefsEditor.putInt(key, value).commit();
    }
}
