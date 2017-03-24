package com.xcm91.relation.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xcm91.relation.R;
import com.xcm91.relation.common.BaseActivity;
import com.xcm91.relation.util.LogManager;
import com.xcm91.relation.util.SharePreferenceUtil;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;

public class WelcomeActivity extends BaseActivity {

    private android.widget.LinearLayout llmain;
    boolean isFirstResume = true;
    private android.widget.TextView tvnum;
    Subscription subscription = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        isUserDefinedColorForStatusBar = false;
        isUserDefinedTitle = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        this.tvnum = (TextView) findViewById(R.id.tv_num);
        this.llmain = (LinearLayout) findViewById(R.id.ll_main);
        llmain.setBackgroundResource(R.drawable.bg_welcome);
        llmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAc(MainActivity.class);
                finish();
            }
        });

        SharePreferenceUtil.getInstance().save("key", "123", this);

        LogManager.e("fgghj" + SharePreferenceUtil.getInstance().getValue("buyt", this));

        SharePreferenceUtil.getInstance().delete("yyyyy", this);
        SharePreferenceUtil.getInstance().clear(this);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(0);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (isFirstResume) {
            final int count = 3;
            subscription = Observable.interval(0, 1, TimeUnit.SECONDS).take(count + 1).map(new Func1<Long, Long>() {
                @Override
                public Long call(Long aLong) {
                    return count - aLong;
                }
            }).doOnSubscribe(new Action0() {
                @Override
                public void call() {
                    //在call之前执行一些初始化操作
                    Log.d(TAG, "doOnSubscribe: ");
                }
            }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Long>() {
                @Override
                public void onCompleted() {
                    Log.d(TAG, "onCompleted: ");
                    if (subscription != null) subscription.unsubscribe();
                    startAc(MainActivity.class);
                    finish();
                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                }

                @Override
                public void onNext(Long aLong) { //接受到一条就是会操作一次UI
                    Log.d(TAG, "onNext: " + aLong);
                    tvnum.setText(String.valueOf(aLong).concat("S"));
                }
            });

        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscription != null) subscription.unsubscribe();
    }
}