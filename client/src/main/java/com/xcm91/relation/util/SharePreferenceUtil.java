package com.xcm91.relation.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.xcm91.relation.common.MyApplication;

public class SharePreferenceUtil {
    private static final String SHARED_FILE = "lhy.shared.local.data";
    private static SharePreferenceUtil instance;

    public static SharePreferenceUtil getInstance() {
        if (null == instance) {
            instance = new SharePreferenceUtil();
        }
        return instance;
    }

    public boolean save(String key, String value, Context context) {
        boolean succ = false;
        Editor editor = getEditor(context);

        if (editor != null) {
            editor.putString(key, value).commit();
            succ = true;
        }

        return succ;
    }

    public String getValue(String key, Context context) {
        String value = null;
        SharedPreferences sharedPreferences = getSharedPreferences(context);

        if (sharedPreferences != null) {
            value = sharedPreferences.getString(key, "");
        }

        return value;
    }

    public boolean delete(String key, Context context) {
        boolean succ = false;
        Editor editor = getEditor(context);
        if (editor != null) {
            editor.remove(key).commit();
            succ = true;
        }
        return succ;
    }

    public boolean clear(Context context) {
        boolean succ = false;
        Editor editor = getEditor(context);
        if (editor != null) {
            editor.clear().commit();
            succ = true;
        }

        return succ;
    }

    private Editor getEditor(Context context) {
        Editor editor = null;
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
        }
        return editor;
    }

    private SharedPreferences getSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = null;
        if (context == null) context = MyApplication.getInstance();
        if (context != null) sharedPreferences = context.getSharedPreferences(SHARED_FILE, Activity.MODE_PRIVATE);
        return sharedPreferences;
    }

}
