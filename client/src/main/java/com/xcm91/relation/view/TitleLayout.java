package com.xcm91.relation.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xcm91.relation.R;
import com.xcm91.relation.util.ViewUtils;

import java.lang.reflect.Field;

/**
 * 含顶部bar的界面布局
 *
 * @author yejingjie
 */
public class TitleLayout extends LinearLayout {
    private Context context = null;//
    private LinearLayout bgview_top;
    private LinearLayout titleBar = null;
    private LinearLayout ll_right = null;
    private TextView main_title_name = null;
    private TextView main_title_second_name = null;
    private ImageButton ib_back = null;
    private TextView tv_right = null;
    private View view_title_line = null;
    private Button html_close = null;
    private boolean isUserDefinedColorForStatusBar = false;//是否采用自定义的颜色设置状态栏，
    private View ll_left;
    // true:是，false：否(因为最外层布局没有使用蓝色也没必要使用蓝色所以即便使用fits状态栏也不会成为蓝色所以舍去设置fits的效果，直接设置padding)

    public TitleLayout(Context context, boolean isUserDefinedColorForStatusBar) {
        super(context);
        this.isUserDefinedColorForStatusBar = isUserDefinedColorForStatusBar;
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//Android4.4以上
            if (isUserDefinedColorForStatusBar) {//采用了自定义的状态栏颜色
                setFitsSystemWindows(true);//设置为true表示TitleLayou应用中的最外层布局）距离上方有状态栏的高度，这只有这样在全屏情况才能弹出键盘
                setClipToPadding(true);
            } else {//没有采用自定义的状态栏颜色，就是用padding做的，不能设置setFitsSystemWindows()，否则上方就有一个空白，这情况下弹出键盘有问题

            }
        }
        setOrientation(LinearLayout.VERTICAL);
    }

    /**
     * 显示titleBar,一般不需要手动调用该方法
     */
    public void showTitleBar() {
        if (titleBar == null) {
            synchronized (TitleLayout.class) {
                if (titleBar == null) {
                    titleBar = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.include_titlebar, null);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        if (!isUserDefinedColorForStatusBar) {//不是自定义的颜色做为状态栏
                            //ViewUtils.resetStatusTitle(titleBar);//采用Padding作为状态栏的颜色
                        }
                    }
                    bgview_top = (LinearLayout) findView(R.id.bgview_top);
                    view_title_line = findView(R.id.view_title_line);
                    main_title_name = (TextView) findView(R.id.main_title_name);
                    main_title_second_name = (TextView) findView(R.id.main_title_second_name);
                    tv_right = (TextView) findView(R.id.tv_right);
                    ll_right = (LinearLayout) findView(R.id.ll_right);
                    ib_back = (ImageButton) findView(R.id.ib_back);
                    html_close = (Button) findView(R.id.tv_html_close);
                    ll_left = findView(R.id.ll_left);
                    addView(titleBar, 0);
                }
            }
        }
        titleBar.setVisibility(View.VISIBLE);
    }

    public void setTitleTextCol(int col) {
        if (null != main_title_name) {
            main_title_name.setTextColor(getResources().getColor(col));
        }
    }

    public void setHtmlClose(OnClickListener finish) {
        html_close.setVisibility(View.VISIBLE);
        html_close.setOnClickListener(finish);
    }

    public void hideBackView() {
        if (ll_left == null) {
            ll_left = findView(R.id.ll_left);
        }
        ll_left.setVisibility(GONE);
        requestLayout();
        postInvalidate();
    }

    /**
     * 设置返回键
     *
     * @param listener
     */
    public void setBack(final OnBackListener listener) {
        showTitleBar();
        ib_back.setVisibility(View.VISIBLE);
        ib_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onBack();
                }
            }
        });
    }

    /**
     * 设置标题栏颜色
     *
     * @param resourceId
     */
    public void setBgViewTopBackgroundResource(int resourceId) {
        bgview_top.setBackgroundResource(0);
        bgview_top.setBackgroundColor(getResources().getColor(resourceId));
        bgview_top.requestLayout();
        bgview_top.postInvalidate();
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        showTitleBar();
        if (title != null) {
            main_title_name.setText(title);
        }
    }

    public void setTitleLine(boolean lineShow) {
        showTitleBar();
        if (view_title_line != null) {
            if (lineShow) view_title_line.setVisibility(View.VISIBLE);
            else view_title_line.setVisibility(View.GONE);
        }
    }

    public void setTitleMaxLeng(int maxe) {
        main_title_name.setMaxEms(maxe);
    }

    public String getTitle() {
        if (null != main_title_name) {
            return main_title_name.getText().toString();
        }
        return null;
    }

    /**
     * 设置第二标题
     *
     * @param title
     */
    public void setSecondTitle(String title) {
        showTitleBar();
        if (title != null) {
            main_title_second_name.setText(title);
        }
    }

    /**
     * 设置返回图片
     */
    public void setBackImg(int resid, final OnLeftListener listener) {
        showTitleBar();
        ib_back.setVisibility(View.VISIBLE);
        ib_back.setImageResource(resid);
        if (listener != null) {
            ib_back.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    listener.onClick();
                }
            });
        }
    }

    public void setLeftTextNoneImg(String text, final OnLeftListener listener) {
        showTitleBar();
        ib_back.setVisibility(View.GONE);
        html_close.setText(text);
        html_close.setVisibility(VISIBLE);
        if (listener != null) {
            html_close.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    listener.onClick();
                }
            });
        }
    }

    /**
     * 设置右边文字
     *
     * @param text
     */
    public void setRightText(String text, final OnRightListener listener) {
        showTitleBar();
        if (text != null) {
            tv_right.setVisibility(View.VISIBLE);
            tv_right.setText(text);
            tv_right.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent));
            if (listener != null) {
                ll_right.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        listener.onClick();
                    }
                });
            }
        }
    }

    public void setRightText(String text) {
        if (text != null) {
            tv_right.setText(text);
        }
    }

    /**
     * 设置右边文字
     *
     * @param text
     */
    public void setRightText(String text, ColorStateList colors, OnRightListener listener) {
        setRightText(text, listener);
        if (text != null && colors != null) {
            tv_right.setTextColor(colors);
        }
    }

    /**
     * 设置右边文字
     *
     * @param text
     */
    public void setRightText(String text, int color, OnRightListener listener) {
        setRightText(text, listener);
        if (text != null) {
            tv_right.setTextColor(color);
        }
    }

    /**
     * 设置右边图片
     */
    public void setRightImg(int resid, final OnRightListener listener) {
        showTitleBar();
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(resid), null, null, null);
        if (listener != null) {
            ll_right.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    listener.onClick();
                }
            });
        }
    }

    /**
     * 设置右边图片
     */
    public void setRightImg(Drawable resid, final OnRightListener listener) {
        showTitleBar();
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText("");
        tv_right.setCompoundDrawablesWithIntrinsicBounds(resid, null, null, null);
        if (listener != null) {
            ll_right.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    listener.onClick();
                }
            });
        }
    }

    public TextView getRightTextView() {
        return tv_right;
    }

    public void setRightImg(int resid) {
        tv_right.setBackgroundResource(resid);
    }

    public void setRightImgVisibility(int visibility) {
        ll_right.setVisibility(visibility);
    }

    /**
     * 隐藏返回键
     */
    public void hideBackBtn() {
        ib_back.setVisibility(View.GONE);
    }

    /**
     * 显示左按钮
     */
    public void showBackBtn() {
        ib_back.setVisibility(View.VISIBLE);
    }

    /**
     * 显示右按钮
     */
    public void showRightBtn() {
        tv_right.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏右按钮
     */
    public void hideRightBtn() {
        tv_right.setVisibility(View.GONE);
    }

    /**
     * 隐藏头
     */
    public void hideTitleBar() {
        if (null != titleBar) {
            titleBar.setVisibility(View.GONE);
        }
    }

    public LinearLayout getTitleBar() {
        return titleBar;
    }

    public int getTitleHieght() {
        if (View.GONE == titleBar.getVisibility()) {
            return 0;
        }
        return ViewUtils.getHeight(titleBar);
    }


    private View findView(int id) {
        return titleBar.findViewById(id);
    }


    @Override
    public void addView(View child) {
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1);
        super.addView(child, lp);
    }

    /**
     * 返回键监听
     *
     * @author yejingjie
     */
    public static interface OnBackListener {
        public void onBack();
    }

    /**
     * 左监听
     *
     * @author yejingjie
     */
    public static interface OnLeftListener {
        public void onClick();
    }

    /**
     * 右监听
     *
     * @author yejingjie
     */
    public static interface OnRightListener {
        public void onClick();
    }

    /**
     * 绑定监听
     *
     * @author yejingjie
     */
    public static interface OnBinderListener {
        public void onFinish();
    }


    /**
     * 用于获取状态栏的高度。
     *
     * @return 返回状态栏高度的像素值。
     */
    private int getStatusBarHeight() {
        int statusBarHeight = 0;
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object o = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = (Integer) field.get(o);
            statusBarHeight = getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
            Rect frame = new Rect();
            ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            statusBarHeight = frame.top;
        }
        return statusBarHeight;
    }
}
