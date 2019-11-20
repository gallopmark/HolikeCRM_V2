package com.holike.crm.popupwindown;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import androidx.core.content.ContextCompat;
import androidx.core.widget.PopupWindowCompat;

import com.holike.crm.R;
import com.holike.crm.util.DensityUtil;

/**
 * Created by pony on 2019/7/11.
 * Copyright holike possess 2019.
 */
public abstract class CommonPopupWindow extends PopupWindow {
    protected Context mContext;
    protected View mContentView;
    private int mBottomMargin = 0;

    public CommonPopupWindow(Context context) {
        this.mContext = context;
        init();
    }

    private void init() {
        setFocusable(isFocusable());
        setOutsideTouchable(true);
        mContentView = LayoutInflater.from(mContext).inflate(bindLayoutId(), null, false);
        setContentView(mContentView);
        setWidth(getWidth());
        setHeight(getHeight());
        setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(mContext, getColorDrawable())));
    }

    public abstract int bindLayoutId();

    @Override
    public boolean isFocusable() {
        return true;
    }

    public int getColorDrawable() {
        return R.color.bg_transparent;
    }

    @Override
    public int getWidth() {
        return ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    @Override
    public int getHeight() {
        return ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    public void setBottomMargin(int bottomMargin) {
        this.mBottomMargin = bottomMargin;
    }

    public int getBottomMargin() {
        return mBottomMargin;
    }

    public void showAsDown(View anchor) {
        showAsDown(anchor, 0, 0, Gravity.NO_GRAVITY);
    }

    /*适配7.0以上版本*/
    public void showAsDown(View anchor, int xoff, int yoff, int gravity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Rect visibleFrame = new Rect();
            anchor.getGlobalVisibleRect(visibleFrame);
            int height = anchor.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
            setHeight(height - getBottomMargin());
            showAsDropDown(anchor, xoff, yoff, gravity);
        } else {
            PopupWindowCompat.showAsDropDown(this, anchor, 0, 0, gravity);
        }
    }

    protected void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
        lp.alpha = bgAlpha;
        ((Activity) mContext).getWindow().setAttributes(lp);
    }

}
