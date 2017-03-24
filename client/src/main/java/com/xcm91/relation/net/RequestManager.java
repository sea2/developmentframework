package com.xcm91.relation.net;

import com.google.gson.Gson;
import com.xcm91.relation.common.AppInfoHelper;
import com.xcm91.relation.common.MyApplication;
import com.xcm91.relation.config.MyConstants;
import com.xcm91.relation.util.LogManager;
import com.xcm91.relation.util.ManifestMetaDataHelper;
import com.xcm91.relation.util.NetworkUtils;
import com.xcm91.relation.util.StringUtils;
import com.xcm91.relation.util.SystemUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.OtherRequestBuilder;
import com.zhy.http.okhttp.builder.PostStringBuilder;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;


/**
 * Created by lhy 2016/4/15.
 */
public class RequestManager {


    public RequestManager() {
    }


    /**
     * 取消网络请求
     */
    public void cancelRequest(Object tag) {
        if (tag != null) {
            OkHttpUtils.getInstance().cancelTag(tag);
        }
    }

    public void getServerData(String url, HashMap<String, String> hasMap, MyConstants.HttpMethod method, int resquestCode, final ResponseListener listener) {
        RequestCall call = null;
        HashMap<String, String> map = addCommonParams(hasMap);
        switch (method) {
            case HTTP_GET://get
                GetBuilder mGetBuilder = OkHttpUtils.get().url(getUrl(url, hasMap)).tag(resquestCode);
                call = mGetBuilder.build();
                break;
            case HTTP_POST: //post
                PostStringBuilder post = OkHttpUtils.postString();
                post.mediaType(MediaType.parse("application/json; charset=utf-8")).url(url).content(new Gson().toJson(map)).tag(resquestCode);
                call = post.build();
                break;
            case HTTP_PUT: //put
                OtherRequestBuilder put = OkHttpUtils.put();
                put.url(url).tag(resquestCode).requestBody(new Gson().toJson(map));
                call = put.build();
                break;
            case HTTP_DELETE: //delete
                OtherRequestBuilder delete = OkHttpUtils.delete();
                delete.url(url).tag(resquestCode).requestBody(new Gson().toJson(map));
                call = delete.build();
                break;
            default:
                LogManager.e("method错误");
                break;
        }
        if (call != null) {
            call.execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e) {
                    if (call != null && (!call.isCanceled())) { //被取消的网络不在回调
                        if (listener != null) {
                            String error = "网络错误";
                            if (!NetworkUtils.IsNetWorkEnable(MyApplication.getInstance())) {
                                error = "当前网络不可用，请检查您的网络设置";
                            }
                            listener.OnError(error);
                        }
                    }
                }

                @Override
                public void onResponse(String response) {
                    if (listener != null) {
                        try {
                            JSONObject json = new JSONObject(response);
                            listener.OnSuccess(json);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.OnError("json数据格式错误");
                        }

                    }
                }
            });
        }
    }


    /**
     * Get地址拼接
     */
    private String getUrl(String uri, Map<String, String> map) {
        StringBuilder strBuffer = new StringBuilder();
        if (map != null && map.size() > 0) {
            strBuffer.append(uri.concat("?"));
            Iterator<Map.Entry<String, String>> iter = map.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, String> entry = iter.next();
                if (iter.hasNext()) {
                    if ((!StringUtils.isEmpty(entry.getKey())) && entry.getValue() != null)
                        strBuffer.append(entry.getKey().concat("=" + entry.getValue() + "&"));
                } else {
                    if ((!StringUtils.isEmpty(entry.getKey())) && entry.getValue() != null)
                        strBuffer.append(entry.getKey().concat("=" + entry.getValue()));
                }
            }
        }
        return strBuffer.toString();
    }


    private HashMap<String, String> addCommonParams(HashMap<String, String> map) {
        if (map == null) map = new HashMap<>();
        map.put("app_version", AppInfoHelper.getInstance().getAppVersionNumber());
        map.put("version", SystemUtil.getPhoneVersion());
        map.put("market", ManifestMetaDataHelper.getMetaDataChannelKey());
        map.put("mac", SystemUtil.getMacAddress(MyApplication.getInstance()));
        map.put("imei", SystemUtil.getImei());
        map.put("current_time", System.currentTimeMillis() + "");
        HashMap<String, String> paramss = new HashMap<>();
        if (map.size() > 0) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if ((!StringUtils.isEmpty(entry.getKey())) && entry.getValue() != null) {
                    paramss.put(entry.getKey().trim(), entry.getValue().trim());
                }
            }
        }
        return paramss;
    }

}
