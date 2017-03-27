package com.xcm91.relation.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.xcm91.relation.R;
import com.xcm91.relation.common.BaseActivity;
import com.xcm91.relation.config.MyConstants;
import com.xcm91.relation.config.UrlConstans;
import com.xcm91.relation.eventbus.BaseEvent;
import com.xcm91.relation.eventbus.MessageEvent;
import com.xcm91.relation.net.ResponseNewListener;
import com.xcm91.relation.util.LogManager;
import com.xcm91.relation.util.SharePreferenceUtil;
import com.xcm91.relation.view.dialog.CommonDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class Main2Activity extends BaseActivity {
    private Subscription subscription = null;
    private android.widget.TextView tvtest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        this.tvtest = (TextView) findViewById(R.id.tv_test);


        //  ImageLoader.getInstance().displayImage(accountresultInfo.getHead_portrait(), mIv_head_portrait, DisplayImageOptionsUtil.getDisplayImageOptions(-1, true, true));


        HashMap<String, String> map = new HashMap<String, String>();
        map.put("uid", "");
        map.put("sign ", "b19c4a1ec97fb0b5b8a9b3ac032baa9c");
        map.put("channel", "4");
        map.put("current_time", " 1490325103773");
        map.put("spm", "2017.1.4.0.0.0");


        requestNetData(UrlConstans.ACCOUNT_TEST_GET, map, true, MyConstants.HttpMethod.HTTP_GET, 100, new ResponseNewListener() {
            @Override
            public void OnResponse(String json, boolean successorfail) {
                LogManager.e(json);
                if (successorfail) {

                }
            }
        });

        clearAllRequest();


        CommonDialog mDialog = new CommonDialog(this);
        mDialog.setContentHtml("<font color='#4d4d4d'>我的人天赋的感觉</font>");
        mDialog.setTitle("标题内容");
        mDialog.setPositiveButton("确定");
        mDialog.show();


        showProgressDialog();
        SharePreferenceUtil.getInstance().save("key", "123", this);
        subscription = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                //可以做各种复杂的操作,然后进行回调
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                subscriber.onNext("jack : 我是最后的执行结果");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                subscriber.onNext("jack : 我被计算出来了");
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe( //此行为订阅,只有真正的被订阅,整个流程才算生效
                new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted");
                        dismissProgressDialog();
                        startAc(OneActivity.class);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, e.toString());
                    }

                    @Override
                    public void onNext(String s) {
                        Log.d(TAG, s);
                    }
                });


        EventBus.getDefault().register(this);
        // EventBus.getDefault().post(new MessageEvent("Hello everyone!"));
    }


    /* ThreadMode.MAIN  //主线程
     ThreadMode.BACKGROUND//后台线程
     ThreadMode.ASYNC //开辟新独立线程，用来执行耗时操作，例如网络访问*/
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowMessageEvent(BaseEvent messageEvent) {
        if (messageEvent instanceof MessageEvent) {
            tvtest.setText(((MessageEvent) messageEvent).message);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscription != null) subscription.unsubscribe();
        EventBus.getDefault().unregister(this);
    }

}
