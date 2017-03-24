package com.xcm91.relation.config;


import com.xcm91.relation.BuildConfig;

/**
 * Created by Administrator on 2017/3/22.
 */

public class MyConstants {


    /**
     * 0:开发模式  DEV,
     * 1:生产模式  PRO,
     */
    public static final RunModel AppRunModel = ("release".equals(BuildConfig.BUILD_TYPE) && !"内部测试_1".equals(BuildConfig.FLAVOR)) ? RunModel.PRO : RunModel.DEV;
//      public static final RunModel AppRunModel =1;


    /**
     * APP所在运行环境
     */
    public static String getHost() {
        if (AppRunModel == RunModel.PRO) {// 生产地址
            return "https://app.91xcm.com/v1.0/app/";
        } else if (MyConstants.AppRunModel == RunModel.DEV) {//开发
            return "http://192.168.2.61:8080/v1.0/app/";
        }
        return "";
    }

    public enum RunModel {
        /**
         * 开发模式
         */
        DEV, /**
         * 预发布
         */
        UAT, /**
         * 生产模式
         */
        PRO;
    }


    public enum HttpMethod {
        HTTP_GET, HTTP_POST, HTTP_DELETE, HTTP_PUT;
    }


}
