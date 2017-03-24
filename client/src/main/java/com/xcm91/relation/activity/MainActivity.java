package com.xcm91.relation.activity;

import android.os.Bundle;
import android.util.Log;

import com.xcm91.relation.R;
import com.xcm91.relation.common.BaseActivity;
import com.xcm91.relation.util.SharePreferenceUtil;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MainActivity extends BaseActivity {
    private Subscription subscription = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isUserDefinedTitle = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                        startAc(Main2Activity.class);
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


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscription != null) subscription.unsubscribe();
    }
}
