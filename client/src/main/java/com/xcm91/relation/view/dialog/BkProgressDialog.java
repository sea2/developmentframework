package com.xcm91.relation.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.TextView;

import com.xcm91.relation.R;


public class BkProgressDialog extends Dialog {


    private TextView mContentText;

    public BkProgressDialog(Context context, int theme) {
        super(context, theme);

    }

    public BkProgressDialog(Context context) {
        super(context);
    }

    public static BkProgressDialog getInstance(final Context context) {
        BkProgressDialog dialog = new BkProgressDialog(context, R.style.dialog);
        dialog.setContentView(R.layout.progress_layout);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });
        return dialog;
    }



    public void setContentText(int stringId) {
        if (null == mContentText) {
            mContentText = (TextView) findViewById(R.id.progress_content);
        }
        mContentText.setText(stringId);
    }

    public void setContentText(String msg) {
        if (null == mContentText) {
            mContentText = (TextView) findViewById(R.id.progress_content);
        }
        mContentText.setText(msg);
    }

}
