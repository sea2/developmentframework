package com.xcm91.relation.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Base64;

import com.xcm91.relation.common.AppInfoHelper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ChannelUtil {

    private static final String CHANNEL_KEY = "x!^cm@c~n^&l";
    private static final String CHANNEL_VERSION_KEY = "xcm_version";
    /**
     * eg:小米_10000
     */
    private static String mChannelPair;

    /**
     * 返回市场。  如果获取失败返回""
     *
     * @param context
     * @return
     */
    public static String getChannel(Context context) {
        return getChannel(context, "");
    }

    /**
     * 返回市场。  如果获取失败返回defaultChannel
     *
     * @param context
     * @param defaultChannel
     * @return
     */
    public static String getChannel(Context context, String defaultChannel) {
        //内存中获取
        if (!TextUtils.isEmpty(mChannelPair)) {
            return mChannelPair;
        }
//		//sp中获取
//		mChannelPair = getChannelBySharedPreferences(context);
//		if(!TextUtils.isEmpty(mChannelPair)){
//			return mChannelPair;
//		}
        //从apk中获取
        mChannelPair = getChannelFromApk(context, CHANNEL_KEY);
        if (!TextUtils.isEmpty(mChannelPair)) {
            //保存sp中备用
//			saveChannelBySharedPreferences(context, mChannelPair);
            return mChannelPair;
        }
        //全部获取失败
        return defaultChannel;
    }

    /**
     * 从apk中获取版本信息
     *
     * @param context
     * @param channelKey
     * @return
     */
    private static String getChannelFromApk(Context context, String channelKey) {
        //从apk包中获取
        ApplicationInfo appinfo = context.getApplicationInfo();
        String sourceDir = appinfo.sourceDir;
        //默认放在meta-inf/里， 所以需要再拼接一下
        String key = "META-INF/" + channelKey;
        String ret = "";
        ZipFile zipfile = null;
        try {
            zipfile = new ZipFile(sourceDir);
            Enumeration<?> entries = zipfile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = ((ZipEntry) entries.nextElement());
                String entryName = entry.getName();
                if (entryName.startsWith(key)) {
                    ret = entryName;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (zipfile != null) {
                try {
                    zipfile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        String channel = ret.replace("META-INF/x!^cm@c~n^&l#@", "");
        return channel;
    }

    /**
     * 本地保存channel & 对应版本号
     *
     * @param context
     * @param channel
     */
    private static void saveChannelBySharedPreferences(Context context, String channel) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        String encryptStr = channel;
        try {
            encryptStr = new String(Base64.encode(channel.getBytes("utf-8"), Base64.DEFAULT));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        editor.putString(CHANNEL_KEY, encryptStr);
        editor.putInt(CHANNEL_VERSION_KEY,StringUtils.toInt(AppInfoHelper.getInstance().getAppVersionCode()));
        editor.commit();
    }

    /**
     * 从sp中获取channel
     *
     * @param context
     * @return 为空表示获取异常、sp中的值已经失效、sp中没有此值
     */
    private static String getChannelBySharedPreferences(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        int currentVersionCode = StringUtils.toInt(AppInfoHelper.getInstance().getAppVersionCode());
        if (currentVersionCode == -1) {
            //获取错误
            return "";
        }
        int versionCodeSaved = sp.getInt(CHANNEL_VERSION_KEY, -1);
        if (versionCodeSaved == -1) {
            //本地没有存储的channel对应的版本号
            //第一次使用  或者 原先存储版本号异常
            return "";
        }
        if (currentVersionCode != versionCodeSaved) {
            return "";
        }
        String v = sp.getString(CHANNEL_KEY, "");
        try {
            return StringUtils.isEmpty(v) ? "" :
             /*MD5Util.convertMD5(*/ new String(Base64.decode(v.getBytes("utf-8"), Base64.DEFAULT), "utf-8")/*)*/;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return v;
    }

}
