package com.xcm91.relation.util;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * Created by Administrator on 2017/3/24.
 */

public class DisplayImageOptionsUtil {


    /**
     * 默认图片配置
     *
     * @param defaultImageId
     * @param isCacheInMemory
     * @param isCacheOnDisc
     * @return
     */
    public static DisplayImageOptions getDisplayImageOptions(int defaultImageId, boolean isCacheInMemory, boolean isCacheOnDisc) {
        return getImgBuilder(defaultImageId, isCacheInMemory, isCacheOnDisc).build();
    }

    /**
     * 渐变图片配置
     *
     * @param defaultImageId
     * @param isCacheInMemory
     * @param isCacheOnDisc
     * @return
     */
    public static DisplayImageOptions getDisplayFadeImageOptions(int defaultImageId, boolean isCacheInMemory, boolean isCacheOnDisc, int fadeTime) {
        return getImgBuilder(defaultImageId, isCacheInMemory, isCacheOnDisc).displayer(new FadeInBitmapDisplayer(fadeTime)).build();
    }

    /**
     * 圆角图片配置
     *
     * @param defaultImageId
     * @param isCacheInMemory
     * @param isCacheOnDisc
     * @param roundPixels
     * @return
     */
    public static DisplayImageOptions getDisplayRoundImageOptions(int defaultImageId, boolean isCacheInMemory, boolean isCacheOnDisc, int roundPixels) {
        return getImgBuilder(defaultImageId, isCacheInMemory, isCacheOnDisc).displayer(new RoundedBitmapDisplayer(roundPixels)).build();
    }

    private static DisplayImageOptions.Builder getImgBuilder(int defaultImageId, boolean isCacheInMemory, boolean isCacheOnDisc) {
        DisplayImageOptions.Builder displayImageOptionsBuilder = new DisplayImageOptions.Builder();
        displayImageOptionsBuilder.imageScaleType(ImageScaleType.EXACTLY);
        if (defaultImageId != -1) {
            displayImageOptionsBuilder.showImageOnFail(defaultImageId);
            // displayImageOptionsBuilder.showStubImage(defaultImageId);
            displayImageOptionsBuilder.showImageForEmptyUri(defaultImageId);
            displayImageOptionsBuilder.showImageOnFail(defaultImageId);
        }

        displayImageOptionsBuilder.cacheInMemory(isCacheInMemory);
        displayImageOptionsBuilder.cacheOnDisk(isCacheOnDisc);
        displayImageOptionsBuilder.bitmapConfig(Bitmap.Config.RGB_565);
        return displayImageOptionsBuilder;
    }
}
