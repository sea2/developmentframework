package com.xcm91.relation.activity;

import android.os.Bundle;

import com.xcm91.relation.R;
import com.xcm91.relation.common.BaseActivity;
import com.xcm91.relation.config.MyConstants;
import com.xcm91.relation.config.UrlConstans;
import com.xcm91.relation.net.ResponseNewListener;
import com.xcm91.relation.util.LogManager;
import com.xcm91.relation.view.dialog.CommonDialog;

import java.util.HashMap;

public class Main2Activity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


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


    }


}
