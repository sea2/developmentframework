package com.xcm91.relation.common;

import android.app.Application;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.squareup.leakcanary.LeakCanary;
import com.xcm91.relation.config.MyConstants;
import com.xcm91.relation.util.CrashHandler;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * Created by lhy on 2017/3/22.
 */

public class MyApplication extends Application {

    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        if (MyConstants.AppRunModel != MyConstants.RunModel.PRO) {
            LeakCanary.install(this);
            CrashHandler crashHandler = CrashHandler.getInstance();
            crashHandler.init(getApplicationContext());
        }


        // 网络接口配置信息初始化
        OkHttpUtils.getInstance().debug("OkHttpUtils").setConnectTimeout(10000, TimeUnit.MILLISECONDS);
        //使用https，但是默认信任全部证书
        OkHttpUtils.getInstance().setCertificates();
        initImageLoader();
    }

    public void initImageLoader() {
        File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), "xcm/ImgCache");
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(getApplicationContext()).memoryCacheSize(1024 * 1024 * 10).threadPoolSize(5).threadPriority(Thread.MIN_PRIORITY + 2).denyCacheImageMultipleSizesInMemory().diskCache(new UnlimitedDiskCache(cacheDir)).defaultDisplayImageOptions(options).diskCacheFileNameGenerator(new Md5FileNameGenerator()).defaultDisplayImageOptions(DisplayImageOptions.createSimple());
        ImageLoaderConfiguration config = builder.build();
        ImageLoader.getInstance().init(config);
    }

    public static MyApplication getInstance() {
        return instance;
    }


}
