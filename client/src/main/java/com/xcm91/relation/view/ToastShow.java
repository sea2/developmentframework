package com.xcm91.relation.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.xcm91.relation.R;


/**
 * 消息提示的显现与消失
 *
 * @author
 */
public class ToastShow {
    private Toast toast;

    public ToastShow(Context context) {
        if (context != null) {
            if (toast == null) {
                this.toast = new Toast(context);
            }
            this.toast.setGravity(Gravity.CENTER, 0, 0);
        }
    }


    // 消息框的显现
    public void show(CharSequence text) {
        if (!TextUtils.isEmpty(text) && toast != null) {
            View view = toast.getView();
            view.setBackgroundResource(R.drawable.toast_background);
            toast.setView(view);
            toast.setText(text);
            if (view.isShown()) {
                toast.setDuration(Toast.LENGTH_LONG);
            }
            toast.show();
        }

    }


    // 消息框的消失
    public void cancel() {
        toast.cancel();
    }

}
