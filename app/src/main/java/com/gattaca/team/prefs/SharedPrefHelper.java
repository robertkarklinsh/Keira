package com.gattaca.team.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;

public final class SharedPrefHelper {
    public static final String DEFAULT_String = null;
    public static final int DEFAULT_Integer = 0;
    public static final float DEFAULT_Float = 0.0F;
    public static final long DEFAULT_Long = 0L;
    public static final boolean DEFAULT_Boolean = false;
    public static final HashSet<String> DEFAULT_Hash = new HashSet<>();

    private static SharedPreferences instance = null;

    public static SharedPreferences getInstance(final Context cx, final String name) {
        if (instance == null) instance = cx.getSharedPreferences(name, Context.MODE_PRIVATE);
        return instance;
    }

    public static synchronized void saveToSharedPref(final String key, final Class<?> type, final Object v) {
        final SharedPreferences.Editor editor = instance.edit();
        final String value = v != null ? v.toString() : "";

        if (type.equals(String.class)) {
            editor.putString(key, value);
        } else if (type.equals(Integer.class)) {
            editor.putInt(key, Integer.valueOf(value));
        } else if (type.equals(Boolean.class)) {
            editor.putBoolean(key, Boolean.valueOf(value));
        } else if (type.equals(Long.class)) {
            editor.putLong(key, Long.valueOf(value));
        } else if (type.equals(Float.class)) {
            editor.putFloat(key, Float.valueOf(value));
        } else if (type.equals(Set.class)) {
            editor.putStringSet(key, (Set<String>) v);
        }
        Log.d("SharedPrefHelper", "set " + key + " " + v);
        editor.apply();
    }

    public static Object getFromSharedPref(final String key, final Class<?> type, final Object defValue) {
        Object a = null;
        if (type.equals(String.class)) {
            a = instance.getString(key, "");
            if (defValue == null && a.equals("")) {
                a = null;
            }
        } else if (type.equals(Integer.class)) {
            a = instance.getInt(key, (Integer) defValue);
        } else if (type.equals(Boolean.class)) {
            a = instance.getBoolean(key, (Boolean) defValue);
        } else if (type.equals(Long.class)) {
            a = instance.getLong(key, (Long) defValue);
        } else if (type.equals(Float.class)) {
            a = instance.getFloat(key, (Float) defValue);
        } else if (type.equals(Set.class)) {
            a = instance.getStringSet(key, (Set<String>) defValue);
        }
        Log.d("SharedPrefHelper", "get " + key + " " + (a == null ? "null" : a.toString()));
        return a;
    }
}
