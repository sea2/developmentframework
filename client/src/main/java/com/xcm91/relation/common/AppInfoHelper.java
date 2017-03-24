package com.xcm91.relation.common;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.xcm91.relation.util.StringUtils;

/**
 * Created by lhy on 2017/3/24.
 */

public class AppInfoHelper {

    private static AppInfoHelper instance = null;
    private String versionName = "";
    private String versionCode = "";

    public static AppInfoHelper getInstance() {
        if (instance == null) instance = new AppInfoHelper();
        return instance;
    }


    /**
     * 获取当前应用版本序号VersionNumber
     *
     * @return
     */
    public String getAppVersionNumber() {
        String versionName = "";
        if (StringUtils.isEmpty(this.versionName)) {
            try {
                PackageManager manager = MyApplication.getInstance().getPackageManager();
                PackageInfo info = manager.getPackageInfo(MyApplication.getInstance().getPackageName(), 0);
                versionName = info.versionName;
                this.versionName = info.versionName;
                this.versionCode = String.valueOf(info.versionCode);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else versionName = this.versionName;
        return versionName;
    }

    /**
     * 获取当前应用版本名称VersionCode
     *
     * @return
     */
    public String getAppVersionCode() {
        String versionCode = "";
        if (StringUtils.isEmpty(this.versionCode)) {
            try {
                PackageManager manager = MyApplication.getInstance().getPackageManager();
                PackageInfo info = manager.getPackageInfo(MyApplication.getInstance().getPackageName(), 0);
                versionCode = String.valueOf(info.versionCode);
                this.versionCode = String.valueOf(info.versionCode);
                this.versionName = info.versionName;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else versionCode = this.versionCode;
        return versionCode;
    }
}
