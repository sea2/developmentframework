package com.xcm91.relation.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.xcm91.relation.common.MyApplication;

/**
 * Created by Administrator on 2017/3/24.
 */

public class ManifestMetaDataHelper {

    private static String CHANNEL_NAME = "CHANNEL_NAME";
    private static String CHANNEL_KEY = "CHANNEL_KEY";


    /**
     * 获取Manifest中配置的渠道名称（可能在Manifest中配置为空）
     *
     * @return
     * @author xiehy
     */
    public static String getMetaDataChannelName() {
        String channel = ChannelUtil.getChannel(MyApplication.getInstance());
        if (!StringUtils.isEmpty(channel) && channel.contains("#@")) {
            String[] split = channel.split("#@");
            return split[0] + "";
        }
        return getManifestMetaDataValue(CHANNEL_NAME);
    }


    /**
     * 获取Manifest中配置的渠道Key（可能在Manifest中配置为空）
     *
     * @return
     * @author xiehy
     */
    public static String getMetaDataChannelKey() {

        String channel = ChannelUtil.getChannel(MyApplication.getInstance());
        if (!StringUtils.isEmpty(channel) && channel.contains("#@")) {
            String[] split = channel.split("#@");
            return split[1] + "";
        }
        return getManifestMetaDataValue(CHANNEL_KEY);
    }

    /**
     * 获取manifest文件中meta-data中配置的参数
     *
     * @return String
     */
    public static String getManifestMetaDataValue(String mateName) {
        if (TextUtils.isEmpty(mateName)) {
            LogManager.e("mateName is null");
            return "";
        }
        try {
            Context context = MyApplication.getInstance();
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Object oValue = appInfo.metaData.get(mateName);
            if (oValue != null) {
                return oValue.toString();
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }


}
