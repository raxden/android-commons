package com.raxdenstudios.commons.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 *
 * @author Angel Gomez
 */
public class PreferenceUtils {

    public static Object getPreference(Context context, String key) {
        return getPreference(PreferenceManager.getDefaultSharedPreferences(context), key);
    }

    public static Object getPreference(Context context, String key, int mode) {
        return getPreference(context.getSharedPreferences("defaultSharedPreferences", mode), key);
    }

    public static Object getPreference(SharedPreferences settings, String key) {
        return settings.getAll().get(key);
    }

    public static void setPreference(Context context, String key, Object value) {
        setPreference(PreferenceManager.getDefaultSharedPreferences(context), key, value);
    }

    public static void setPreference(Context context, String key, Object value, int mode) {
        setPreference(context.getSharedPreferences("defaultSharedPreferences", mode), key, value);
    }

    public static void setPreference(SharedPreferences settings, String key, Object value) {
        SharedPreferences.Editor editor = settings.edit();
        if (value instanceof Integer) editor.putInt(key, (Integer)value);
        if (value instanceof String) editor.putString(key, (String)value);
        if (value instanceof Boolean) editor.putBoolean(key, (Boolean)value);
        if (value instanceof Long) editor.putLong(key, (Long)value);
        editor.commit();
    }

}
