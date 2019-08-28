package com.holike.crm.popupwindown;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow;

import com.holike.crm.R;


/**
 * Created by wqj on 2018/1/3.
 */

public abstract class BasePopupWindow extends PopupWindow {
    protected Context mContext;
    protected View mContentView;

    public BasePopupWindow(Context context) {
        super(context);
        this.mContext = context;
        mContentView = LayoutInflater.from(context).inflate(setContentView(), null, false);
        setContentView(mContentView);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(mContext, setColorDrawable())));
        setFocusable(setFocusable());
        setOutsideTouchable(setOutsideTouchable());
        update();
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha 屏幕透明度0.0-1.0 1表示完全不透明
     */
    protected void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        ((Activity) mContext).getWindow().setAttributes(lp);
    }

    /**
     * 隐藏键盘
     *
     * @param view
     */
    protected void hideSoftInput(View view) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    abstract int setContentView();

    protected int setColorDrawable() {
        return R.color.color_while;
    }

    protected boolean setFocusable() {
        return true;
    }

    protected boolean setOutsideTouchable() {
        return true;
    }
}
