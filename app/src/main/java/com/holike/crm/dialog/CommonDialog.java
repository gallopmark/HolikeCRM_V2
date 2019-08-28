package com.holike.crm.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.holike.crm.R;
import com.holike.crm.util.DensityUtil;

/*dialog基础类*/
public abstract class CommonDialog extends Dialog {
    protected Context mContext;
    protected View mContentView;
    private int width = FrameLayout.LayoutParams.WRAP_CONTENT,
            height = FrameLayout.LayoutParams.WRAP_CONTENT; //默认height为自动填充
    private int windowAnimations = -1;
    private Drawable background; //窗口背景drawable
    private int gravity = Gravity.CENTER;  //默认弹窗位置为屏幕中央

    public CommonDialog(Context context) {
        this(context, R.style.Dialog);
    }

    public CommonDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        setWidth(getCommonWidth());
        mContentView = LayoutInflater.from(mContext).inflate(bindContentView(), null);
        initView(mContentView);
        if (getLayoutParams() != null) {
            setContentView(mContentView, getLayoutParams());
        } else {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(getWidth(), getHeight());
            setContentView(mContentView, params);
        }
    }

    protected int getCommonWidth() {
        return DensityUtil.getScreenWidth(mContext) - mContext.getResources().getDimensionPixelSize(R.dimen.dp_40) * 2;
    }

    protected int getCommonHeight() {
        return DensityUtil.getScreenHeight(mContext) - mContext.getResources().getDimensionPixelSize(R.dimen.dp_50) * 2;
    }

    public View getContentView() {
        return mContentView;
    }

    /*子类可以重写此方法自定义宽度*/
    public int getWidth() {
        return width;
    }

    /*窗口宽度默认左右边距30dp*/
    public void setWidth(int width) {
        this.width = width;
    }

    /*子类可以重写此方法自定义高度*/
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    /*子类可以重写此方法自定义窗口动画*/
    public int getWindowAnimations() {
        return windowAnimations;
    }

    /*对话框弹出动画*/
    public void setWindowAnimations(@StyleRes int windowAnimations) {
        this.windowAnimations = windowAnimations;
    }

    public void setBackgroundDrawable(Drawable background) {
        this.background = background;
    }

    /*重写此方法可以设置窗口background*/
    public Drawable getBackgroundDrawable() {
        return background;
    }

    public void setBackgroundDrawableResource(@DrawableRes int resId) {
        this.background = ContextCompat.getDrawable(mContext, resId);
    }

    @Nullable
    public ViewGroup.MarginLayoutParams getLayoutParams() {
        return null;
    }

    /*重写此方法可以设置窗口弹出位置*/
    public int getGravity() {
        return gravity;
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    @Override
    public void show() {
        Window window = getWindow();
        if (window != null) {
            renderWindow(window);
            super.show();
        }
    }

    private void renderWindow(Window window) {
        if (isFullScreen()) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        }
        if (fullWidth()) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }
        if (getWindowAnimations() != -1) {
            window.setWindowAnimations(getWindowAnimations());
        }
        if (getBackgroundDrawable() != null) {
            window.setBackgroundDrawable(getBackgroundDrawable());
        }
        if (getGravity() != -1) {
            window.setGravity(getGravity());
        }
    }

    protected abstract int bindContentView();

    protected void initView(View contentView) {
    }

    /*是否全屏显示*/
    protected boolean isFullScreen() {
        return false;
    }

    protected boolean fullWidth() {
        return false;
    }
}
