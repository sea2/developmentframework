package com.xcm91.relation.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Base64;

import com.xcm91.relation.common.MyApplication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Store {


    private Store() {
    }

    public static void put(String key, String value) {
        puts(MyApplication.getInstance(), key, value);
    }

    public static String getString(String key, String defVal) {
        return gets(MyApplication.getInstance(), key, defVal);
    }

    public static void puts(Context context, String key, String value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putString(key, value).commit();
    }

    public static void puts(Context context, String key, int value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putInt(key, value).commit();
    }

    public static void puts(Context context, String key, long value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putLong(key, value).commit();
    }

    public static void clear(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().clear().commit();
    }

    public static String gets(Context context, String key, String defVal) {
        if (context != null) return PreferenceManager.getDefaultSharedPreferences(context).getString(key, defVal);
        else return "";
    }

    public static int gets(Context context, String key, int defVal) {
        if (context != null) return PreferenceManager.getDefaultSharedPreferences(context).getInt(key, defVal);
        else return 0;
    }

    public static long gets(Context context, String key, long defVal) {
        return PreferenceManager.getDefaultSharedPreferences(context).getLong(key, defVal);
    }

    public static void putsBoolean(Context context, String key, Boolean value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putBoolean(key, value).commit();
    }

    public static Boolean getsBoolean(Context context, String key, Boolean defVal) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(key, defVal);
    }

    // 存储对象类型
    public static boolean saveObject(Context context, String key, Object obj) throws IOException {
        if (obj == null) return false;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sharedPreferences.edit();
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            String str64 = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            editor.putString(key, str64);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (oos != null) {
                oos.close();
            }
            if (baos != null) {
                baos.close();
            }
        }

        return editor.commit();
    }

    // 获取对象类型
    public static Object getObject(Context context, String key) throws IOException {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String str64 = preferences.getString(key, "");
        ObjectInputStream ois = null;
        ByteArrayInputStream bais = null;
        try {
            byte[] base64Bytes = Base64.decode(str64.getBytes(), Base64.DEFAULT);
            bais = new ByteArrayInputStream(base64Bytes);
            ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {
            return null;
        }
    }

    public static void remove(Context context, String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = preferences.edit();
        editor.remove(key);
        editor.commit();
    }


}
