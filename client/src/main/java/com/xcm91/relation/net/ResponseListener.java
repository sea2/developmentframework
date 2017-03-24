package com.xcm91.relation.net;

import org.json.JSONObject;

/**
 * Created by 171842474@qq.com on 2016/4/15.
 */
public interface ResponseListener {
    void OnSuccess(JSONObject json);
    void OnError(String json);
}
