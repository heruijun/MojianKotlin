package com.hrj.mojian;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreference {
	
	private static Context getContext(){
		return App.getInstance();
	}
	
	private static String getNameSpace(){
		return App.getInstance().getPackageName();
	}

    public static int getInt(String key, int defaultValue) {
        SharedPreferences sp = getContext().getSharedPreferences(getNameSpace(), Context.MODE_PRIVATE);
        int value = sp.getInt(key, defaultValue);
        return value;
    }

    public static String getString(String key, String defaultValue) {
        SharedPreferences sp = getContext().getSharedPreferences(getNameSpace(), Context.MODE_PRIVATE);
        String value = sp.getString(key, defaultValue);
        return value;
    }

    public static boolean getBoolean(String key, boolean defValue) {
        SharedPreferences sp = getContext().getSharedPreferences(getNameSpace(), Context.MODE_PRIVATE);
        boolean value = sp.getBoolean(key, defValue);
        return value;
    }

    public static void setInt(String key, int value) {
        SharedPreferences sp = getContext().getSharedPreferences(getNameSpace(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void setIntArray(String key1, int value1,
            String key2, int value2, String key3, int value3, String key4, int value4) {
        SharedPreferences sp = getContext().getSharedPreferences(getNameSpace(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key1, value1);
        editor.putInt(key2, value2);
        editor.putInt(key3, value3);
        editor.putInt(key4, value4);
        editor.commit();
    }

    public static void setIntArray(Object... kv) {
        SharedPreferences sp = getContext().getSharedPreferences(getNameSpace(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        for (int i = 0; i < kv.length; i += 2) {
            editor.putInt((String) kv[i], (Integer) kv[i + 1]);
        }
        editor.commit();
    }

    public static void setStringArray(String file, String... kv) {
        SharedPreferences sp = getContext().getSharedPreferences(file, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        for (int i = 0; i < kv.length; i += 2) {
            editor.putString((String) kv[i], (String) kv[i + 1]);
        }
        editor.commit();
    }

    public static String[] getStringArray(String... keys) {
        SharedPreferences sp = getContext().getSharedPreferences(getNameSpace(), Context.MODE_PRIVATE);
        String[] arr = new String[keys.length];
        for (int i = 0; i < keys.length; i++) {
            arr[i] = sp.getString(keys[i], null);
        }
        return arr;
    }

    public static int[] getIntArray(int defVal, String... keys) {
        SharedPreferences sp = getContext().getSharedPreferences(getNameSpace(), Context.MODE_PRIVATE);
        int[] arr = new int[keys.length];
        for (int i = 0; i < keys.length; i++) {
            arr[i] = sp.getInt(keys[i], defVal);
        }
        return arr;
    }

    public static boolean[] getBooleanArray(boolean defVal,
            String... keys) {
        SharedPreferences sp = getContext().getSharedPreferences(getNameSpace(), Context.MODE_PRIVATE);
        boolean[] arr = new boolean[keys.length];
        for (int i = 0; i < keys.length; i++) {
            arr[i] = sp.getBoolean(keys[i], defVal);
        }
        return arr;
    }

    public static int[] getIntArray(String key1, String key2,
            String key3, String key4, int defVal) {
        SharedPreferences sp = getContext().getSharedPreferences(getNameSpace(), Context.MODE_PRIVATE);
        int[] arr = new int[4];
        arr[0] = sp.getInt(key1, defVal);
        arr[1] = sp.getInt(key2, defVal);
        arr[2] = sp.getInt(key3, defVal);
        arr[3] = sp.getInt(key4, defVal);
        return arr;
    }

    public static void setFloat(String key, float value) {
        SharedPreferences sp = getContext().getSharedPreferences(getNameSpace(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    public static float getFloat(String key, float defVal) {
        SharedPreferences sp = getContext().getSharedPreferences(getNameSpace(), Context.MODE_PRIVATE);
        float value = sp.getFloat(key, defVal);
        return value;
    }

    public static void setBoolean(String key, boolean value) {
        SharedPreferences sp = getContext().getSharedPreferences(getNameSpace(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void setString(String key, String value) {
        SharedPreferences sp = getContext().getSharedPreferences(getNameSpace(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }


}
