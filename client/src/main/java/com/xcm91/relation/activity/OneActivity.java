package com.xcm91.relation.activity;

import android.os.Bundle;

import com.xcm91.relation.R;
import com.xcm91.relation.common.BaseActivity;
import com.xcm91.relation.eventbus.MessageEvent;

import org.greenrobot.eventbus.EventBus;

public class OneActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one);
        EventBus.getDefault().post(new MessageEvent("Hello everyone!"));
    }
}
