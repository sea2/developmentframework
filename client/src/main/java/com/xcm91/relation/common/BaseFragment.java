package com.xcm91.relation.common;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xcm91.relation.config.MyConstants;
import com.xcm91.relation.net.RequestManager;
import com.xcm91.relation.net.ResponseListener;
import com.xcm91.relation.net.ResponseNewListener;
import com.xcm91.relation.util.LogManager;
import com.xcm91.relation.util.StringUtils;
import com.xcm91.relation.view.ToastShow;
import com.xcm91.relation.view.dialog.BkProgressDialog;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by lhy on 2017/3/22.
 */

public class BaseFragment extends LazyFragment {

    protected Context context;
    private BkProgressDialog progressDialog;
    protected RequestManager mRequestManager = null;
    protected ToastShow toastShow = null;
    private HashSet<Integer> requestList = new HashSet<Integer>();
    protected String TAG = "";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = this.getClass().getSimpleName();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void lazyLoad() {

    }


    // -------------------------Activity跳转-------------------------

    /**
     * 无参数跳转Activity
     */
    public void startAc(Class<?> classAc) {
        startAc(classAc, null);
    }

    public void startAc(Class<?> classAc, Bundle bundle) {
        if (context != null) {
            Intent it = new Intent(context, classAc);
            if (bundle != null) {
                it.putExtras(bundle);
                LogManager.i(bundle.toString());
            }
            startActivity(it);
        }
    }

    // -------------------------提示信息-------------------------


    /**
     * 圈圈提示框
     *
     * @param content
     * @author windy 2014-8-16 下午2:41:47
     */
    public void showProgressDialog(String content) {
        if (null == context) {
            return;
        }
        if (null == progressDialog) {
            progressDialog = BkProgressDialog.getInstance(context);
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                }
            });
        } else {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
        progressDialog.setContentText(content);
        try {
            if (getUserVisibleHint()) progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 默认圈圈提示框，显示文字：加载中，请稍后…………
     */
    public void showProgressDialog() {
        showProgressDialog("加载中……");
    }

    /**
     * 圈圈提示框消失
     */
    public void dismissProgressDialog() {
        if (null != progressDialog && progressDialog.isShowing()) {
            try {
                progressDialog.dismiss();
            } catch (Exception e) {
                LogManager.e("网络提示框失效");
            }
        }
    }

    /**
     * 弹出提示框
     *
     * @param conent 提示语句
     * @author windy 2014-8-12 下午7:56:53
     */
    public void showToast(String conent) {
        if (null == context) {
            return;
        }
        if (toastShow == null) toastShow = new ToastShow(context);
        if (!TextUtils.isEmpty(conent)) {
            toastShow.show(conent);
        }
    }

    /**
     * 弹出提示框
     *
     * @param str 本地文本资源id
     * @author windy 2014-8-12 下午7:58:14
     */
    public void showToast(int str) {
        if (null == context) {
            return;
        }
        if (toastShow == null) toastShow = new ToastShow(context);
        toastShow.show(getString(str));
    }

    public void cancelToast() {
        if (toastShow != null) toastShow.cancel();
    }


    // -------------------------网络-------------------------

    public void requestNetData(String url, HashMap<String, String> hasMap, MyConstants.HttpMethod method, int resquestCode) {
        requestNetData(url, hasMap, false, method, resquestCode, null);
    }

    public void requestNetData(String url, MyConstants.HttpMethod method, int resquestCode, ResponseNewListener listener) {
        requestNetData(url, null, false, method, resquestCode, listener);
    }

    public void requestNetData(String url, HashMap<String, String> hasMap, final boolean showDialog, MyConstants.HttpMethod method, final int resquestCode, final ResponseNewListener listener) {
        if (requestList != null) requestList.add(resquestCode);
        if (showDialog) showProgressDialog();
        if (mRequestManager == null) mRequestManager = new RequestManager();
        mRequestManager.getServerData(url, hasMap, method, resquestCode, new ResponseListener() {
            @Override
            public void OnSuccess(JSONObject json) {
                String jsonStr = "";
                String resultStr = "";//为空时 code不等于200或者不存在result

                if (json != null) jsonStr = json.toString();
                LogManager.d(jsonStr);
                if (listener != null) {
                    try {
                        JSONObject mJSONObject = new JSONObject(jsonStr);
                        if (mJSONObject.has("code")) {
                            if (StringUtils.isEquals(mJSONObject.getString("code"), "200")) {
                                if (mJSONObject.has("result")) {
                                    resultStr = mJSONObject.getString("result");
                                }
                            } else {
                                if (mJSONObject.has("message")) {
                                    if (!StringUtils.isEmpty(mJSONObject.getString("message"))) showToast(mJSONObject.getString("message"));
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    listener.OnResponse(resultStr, true);
                }
                if (showDialog) dismissProgressDialog();
                if (requestList != null) requestList.remove(resquestCode);
            }

            @Override
            public void OnError(String error) {
                if (listener != null) {
                    listener.OnResponse(error, false);
                }
                if (showDialog) dismissProgressDialog();
                if (requestList != null) requestList.remove(resquestCode);
            }
        });
    }

    /**
     * 取消网络请求
     *
     * @param requestCode
     */
    public void cancelRequest(int requestCode) {
        dismissProgressDialog();
        if (requestList != null) requestList.remove(requestCode);
        OkHttpUtils.getInstance().cancelTag(requestCode);
    }

    //查询网络是否正在执行网络请求
    public boolean isInRequestQueue(int requestCode) {
        return OkHttpUtils.getInstance().isInRequestQueue(requestCode);
    }


    public void clearAllRequest() {
        if (requestList != null) {
            for (Integer intger : requestList) {
                cancelRequest(intger);
            }
        }
    }


}
