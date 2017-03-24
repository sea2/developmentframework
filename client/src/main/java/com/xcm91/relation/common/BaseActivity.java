package com.xcm91.relation.common;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.xcm91.relation.R;
import com.xcm91.relation.config.MyConstants;
import com.xcm91.relation.net.RequestManager;
import com.xcm91.relation.net.ResponseListener;
import com.xcm91.relation.net.ResponseNewListener;
import com.xcm91.relation.util.LogManager;
import com.xcm91.relation.util.StringUtils;
import com.xcm91.relation.util.SystemBarTintManager;
import com.xcm91.relation.view.TitleLayout;
import com.xcm91.relation.view.ToastShow;
import com.xcm91.relation.view.dialog.BkProgressDialog;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by lhy on 2017/3/22.
 */

public abstract class BaseActivity extends FragmentActivity {
    protected String TAG = "";
    private SystemBarTintManager tintManager = null;
    protected boolean isUserDefinedColorForStatusBar = true;//是否采用自定义的颜色设置状态栏，true:是，false：否
    protected boolean isUserDefinedTitle = true;//是否默认标题
    protected TitleLayout basePageView = null;// 页面所对应的View
    protected View pageView;
    private BkProgressDialog progressDialog;
    protected boolean isRunning = true;// 该activity是在运行（未被销毁）true：在运行 false：已销毁
    protected RequestManager mRequestManager = null;
    protected ToastShow toastShow = null;
    private HashSet<Integer> requestList = new HashSet<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = this.getClass().getSimpleName();
        LogManager.i(TAG, "onCreate");
        AcStackManager.getInstance().pushActivity(this);
        if (isUserDefinedColorForStatusBar) setStatusBarTintResource(R.color.color_theme);

    }


    // -------------------------设置页面所对应的View-------------------------
    @Override
    public void setContentView(int layoutResID) {

        if (null == basePageView) {
            initBasePageView();
        }
        if (3 == basePageView.getChildCount()) {
            basePageView.removeViewAt(2);
        }
        pageView = LayoutInflater.from(this).inflate(layoutResID, null);
        basePageView.addView(pageView);
        super.setContentView(basePageView);
    }

    @Override
    public void setContentView(View view) {
        if (null == basePageView) {
            initBasePageView();
        }
        if (3 == basePageView.getChildCount()) {
            basePageView.removeViewAt(2);
        }
        basePageView.addView(view);
        super.setContentView(basePageView);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        if (null == basePageView) {
            initBasePageView();
        }
        if (3 == basePageView.getChildCount()) {
            basePageView.removeViewAt(2);
        }
        basePageView.addView(view);

        super.setContentView(basePageView, params);
    }

    protected void hiddenContextView() {
        if (null != pageView) {
            pageView.setVisibility(View.INVISIBLE);
        }
    }

    protected void showContextView() {
        if (null != pageView && pageView.getVisibility() != View.VISIBLE) {
            pageView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 初始化TitleBar（所有页面的基准LinearLayout，所有子页面都添加到TitleBar中）
     */
    private void initBasePageView() {
        basePageView = new TitleLayout(this, isUserDefinedColorForStatusBar);
        if (isUserDefinedTitle) {
            basePageView.setBack(new TitleLayout.OnBackListener() {
                @Override
                public void onBack() {
                    finish();
                }
            });
        }
    }
    // -------------------------自定义状态栏颜色-------------------------

    /**
     * 设置状态栏颜色
     *
     * @param colorInt
     */
    public void setStatusBarTintResource(int colorInt) {
        if (isUserDefinedColorForStatusBar) {//自定义状态栏的颜色
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setLopStatBar(colorInt);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                initSystemBar(colorInt);
            }
        }
    }

    /**
     * 安卓5.0以上版本设置状态栏颜色配合如下两条属性使用
     * android:clipToPadding="true"
     * android:fitsSystemWindows="true"
     */
    private void setLopStatBar(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(color));
            // window.setNavigationBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * 状态栏半透明 4.4 以上有效配合如下两条属性使用
     * android:clipToPadding="true"
     * android:fitsSystemWindows="true"
     */
    private void initSystemBar(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        if (null == tintManager) {
            tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(color);
        } else tintManager.setStatusBarTintResource(color);
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    public Resources getResources() {  //不受系统字体影响
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }
    // -------------------------Activity跳转-------------------------

    /**
     * 无参数跳转Activity
     */
    public void startAc(Class<?> classAc) {
        startAc(classAc, null);
    }

    public void startAc(Class<?> classAc, Bundle bundle) {
        Intent it = new Intent(this, classAc);
        if (bundle != null) {
            it.putExtras(bundle);
            LogManager.i(bundle.toString());
        }
        startActivity(it);
    }

    // -------------------------提示信息-------------------------


    /**
     * 圈圈提示框
     *
     * @param content
     * @author windy 2014-8-16 下午2:41:47
     */
    public void showProgressDialog(String content) {
        if (isRunning) {
            if (null == progressDialog) {
                progressDialog = BkProgressDialog.getInstance(this);
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
            progressDialog.show();
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
            if (isFinishing()) {
                return;
            }
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
        if (toastShow == null) toastShow = new ToastShow(this);
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
        if (toastShow == null) toastShow = new ToastShow(this);
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

    // -------------------------生命周期-------------------------

    @Override
    protected void onRestart() {
        super.onRestart();
        LogManager.i(TAG, "onRestart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogManager.i(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        AcStackManager.getInstance().pushActivity(this);
        isRunning = true;
        LogManager.i(TAG, "onResume");

    }

    @Override
    protected void onPause() {
        super.onPause();
        LogManager.i(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogManager.i(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogManager.i(TAG, "onDestroy");
        isRunning = false;
        dismissProgressDialog();
        AcStackManager.getInstance().popActivity(this, false);
    }

    @Override
    public void finish() {
        isRunning = false;
        AcStackManager.getInstance().popActivity(this, false);
        dismissProgressDialog();
        super.finish();
    }

}
