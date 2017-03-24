package com.xcm91.relation.util;

import android.content.Context;
import android.os.Build;
import android.os.Environment;

import java.io.File;

/**
 * Created by Administrator on 2017/2/22.
 */

public class FileDirectoryUtil {

    private static String myfilepath = "myfile";

    public static File getOwnFileDirectory(Context context, String fileDir) {
        File appFileDir = null;
        if ("mounted".equals(Environment.getExternalStorageState()) && hasExternalStoragePermission(context)) {
            appFileDir = new File(Environment.getExternalStorageDirectory(), fileDir);
        }

        if (appFileDir == null || !appFileDir.exists() && !appFileDir.mkdirs()) {
            if (Build.VERSION.SDK_INT >= 19) appFileDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
            else {
                if (hasExternalStoragePermission(context)) {
                    appFileDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileDir);
                } else appFileDir = context.getFilesDir();
            }
        }

        if (appFileDir == null || !appFileDir.exists() && !appFileDir.mkdirs()) {
            appFileDir = context.getFilesDir();
        }

        return appFileDir;
    }

    public static File getOwnFileDirectory(Context context) {
        return getOwnFileDirectory(context, myfilepath);
    }


    public static File getOwnCacheDirectory(Context context, String cacheDir) {
        File appCacheDir = null;
        if (Build.VERSION.SDK_INT >= 19) appCacheDir = context.getExternalCacheDir();
        else {
            if (hasExternalStoragePermission(context)) {
                appCacheDir = new File(context.getExternalCacheDir(), cacheDir);
            } else appCacheDir = context.getCacheDir();
        }

        if (appCacheDir == null || !appCacheDir.exists() && !appCacheDir.mkdirs()) {
            appCacheDir = context.getCacheDir();
        }
        return appCacheDir;
    }

    public static File getOwnCacheDirectory(Context context) {
        return getOwnCacheDirectory(context, myfilepath);
    }


    private static boolean hasExternalStoragePermission(Context context) {
        int perm = context.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE");
        return perm == 0;
    }

}
