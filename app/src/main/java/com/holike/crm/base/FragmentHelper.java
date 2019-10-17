package com.holike.crm.base;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

/**
 * Created by gallop on 2019/9/20.
 * Copyright holike possess 2019.
 */
public abstract class FragmentHelper extends HelperWrapper implements BaseHelper {

    protected Context mContext;
    protected BaseFragment<?, ?> mFragment;
    protected View mFragmentView;

    public FragmentHelper(BaseFragment<?, ?> fragment) {
        this.mFragment = fragment;
        this.mContext = fragment.getContext();
        this.mFragmentView = fragment.getContentView();
    }

    protected <T extends View> T obtainView(@IdRes int id) {
        return mFragmentView.findViewById(id);
    }

    protected void setText(@IdRes int id, @StringRes int resId) {
        setText(id, getString(mContext, resId));
    }

    protected TextView obtainTextView(@IdRes int id) {
        return obtainView(mFragmentView, id);
    }

    protected void setText(@IdRes int id, CharSequence text) {
        obtainTextView(id).setText(text);
    }

    protected void setTextColor(@IdRes int id, @ColorRes int colorId) {
        obtainTextView(id).setTextColor(getColor(mContext, colorId));
    }
}
