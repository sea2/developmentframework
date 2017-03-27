package com.xcm91.relation.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.xcm91.relation.R;
import com.xcm91.relation.common.MyApplication;
import com.xcm91.relation.util.ScreenUtil;
import com.xcm91.relation.util.StringUtils;

/**
 * 普通dialog
 *
 * @author yejingjie
 */
public class CommonDialog extends Dialog {
    private View mView;
    private View view_line_button_center;
    private TextView tvTitle = null;// 标题
    private TextView tvContent = null;// 内容

    private String sureText;
    private View.OnClickListener sureClickListener;

    private View chooseBtnGroup;// 两个按扭
    private Button btnConfirm = null;// PositiveButton
    private Button btnCancel = null;// NegativeButton

    public CommonDialog(Context context) {
        super(context, R.style.commonDialog);
        setCanceledOnTouchOutside(false);
        setCancelable(true);
        mView = getLayoutInflater().inflate(R.layout.dialog_common_for_ios, null);
        setContentView(mView);
        initViews();
    }

    /**
     * 设置标题头
     *
     * @param title
     */
    public void setTitle(String title) {
        if (title != null) {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(title);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        if (null != title) {
            setTitle(title.toString());
        }
    }


    /**
     * 设置内容
     *
     * @param content
     */
    public void setContent(String content) {
        if (content != null) {
            tvContent.setVisibility(View.VISIBLE);
            String dialogContent = StringUtils.formatText(content);
            /*if (dialogContent.indexOf("\n") >= 0) {
                tvContent.setGravity(Gravity.LEFT);
			}*/
            tvContent.setText(dialogContent);
        }
    }

    @SuppressWarnings("deprecation")
    public void setContentHtml(String content) {
        if (content != null) {
            tvContent.setVisibility(View.VISIBLE);
            tvContent.setText(Html.fromHtml(content));
        }
    }

    //内容的布局
    public void setContentGravity(int gravityInt) {
        if (tvContent != null) tvContent.setGravity(gravityInt);
    }

    public void setContent(Spanned spanned) {
        if (null != spanned) {
            tvContent.setVisibility(View.VISIBLE);
            tvContent.setText(spanned);
        }
    }

    /**
     * 设置确定按钮
     *
     * @param strConfirm
     */
    public void setPositiveButton(String strConfirm) {
        setPositiveButton(strConfirm, null);
    }

    /**
     * 设置确定按钮
     *
     * @param strConfirm
     * @param listener
     */
    public void setPositiveButton(String strConfirm, final View.OnClickListener listener) {
        sureText = strConfirm;
        sureClickListener = listener;
        chooseBtnGroup.setVisibility(View.VISIBLE);
        if (View.GONE == btnCancel.getVisibility()) {// 单个按扭
            view_line_button_center.setVisibility(View.GONE);
        }

        if (null != strConfirm) {
            btnConfirm.setText(strConfirm);
        }

        btnConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
                if (listener != null) {
                    listener.onClick(btnConfirm);
                }
            }
        });
    }

    /**
     * 设置取消按钮
     *
     * @param strCancel
     */
    public void setNegativeButton(String strCancel) {
        setNegativeButton(strCancel, null);
    }

    /**
     * 设置取消按钮
     *
     * @param strCancel
     * @param listener
     */
    public void setNegativeButton(String strCancel, final View.OnClickListener listener) {
        btnCancel.setVisibility(View.VISIBLE);
        chooseBtnGroup.setVisibility(View.VISIBLE);
        view_line_button_center.setVisibility(View.VISIBLE);
        if (null != strCancel) {
            btnCancel.setText(strCancel);
        }
        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
                if (listener != null) {
                    listener.onClick(btnCancel);
                }
            }
        });

        btnConfirm.setText(sureText);
        btnConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
                if (null != sureClickListener) {
                    sureClickListener.onClick(btnConfirm);
                }
            }
        });

    }


    /**
     * window层显示dialog,不依附与Activity(注：必须加入 <uses-permission
     * android:name="android.permission.SYSTEM_ALERT_WINDOW" />)
     */
    public void setShowOnWindow() {
        getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);// windows层显示
    }

    private void initViews() {
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvContent = (TextView) findViewById(R.id.tvContent);
        chooseBtnGroup = findViewById(R.id.ll_chooseBtn);
        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        view_line_button_center = findViewById(R.id.view_line_button_center);


    }

    @Override
    public void show() {
        if (getWindow() != null) {
            WindowManager.LayoutParams p = getWindow().getAttributes(); // 获取对话框当前的参数值
            p.width = (int) (new ScreenUtil(MyApplication.getInstance()).getWidth() * 0.9); // 宽度设置为屏幕的0.9
            this.getWindow().setAttributes(p);
        }
        super.show();
    }

}
