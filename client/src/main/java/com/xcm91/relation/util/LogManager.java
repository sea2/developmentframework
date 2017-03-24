package com.xcm91.relation.util;

import android.util.Log;

import com.xcm91.relation.config.MyConstants;

public class LogManager {

    public static boolean isLogOpen = (MyConstants.AppRunModel != MyConstants.RunModel.PRO);
    private static String TAG = "com.log";
    private static String TAG_http = "http_json_out";

    public static void i(String msg) {
        if (isLogOpen && null != msg) {
            Log.i(TAG, msg);
        }
    }

    public static void i(String TagMsg,String msg) {
        if (isLogOpen && null != msg) {
            if(StringUtils.isEmpty(TagMsg))
            Log.i(TAG, msg);
            else     Log.i(TagMsg, msg);
        }
    }
    public static void http_i(String TagMsg,String msg) {
        if (isLogOpen && null != msg) {
            if(StringUtils.isEmpty(TagMsg))
            Log.i(TAG_http, msg);
            else     Log.i(TagMsg, msg);
        }
    }
    public static void http_i(String msg) {
        if (isLogOpen && null != msg) {
            Log.i(TAG_http, msg);
        }
    }

    public static void d(String msg) {
        if (isLogOpen && null != msg) {
            Log.d(TAG, msg);
        }
    }
    public static void d(Class clazz,String msg) {
        if (isLogOpen && null != msg) {
            Log.d(clazz.getSimpleName(), msg);
        }
    }

    public static void d(String tag,String msg) {
        if (isLogOpen && null != msg&&tag!=null) {
            Log.d(tag, msg);
        }
    }

    public static void e(String msg) {
        if (isLogOpen && null != msg) {
            Log.e(TAG, msg);
        }
    }

    public static void e(String tag,String msg) {
        if (isLogOpen && null != msg&&tag!=null) {
            Log.e(tag, msg);
        }
    }

}
