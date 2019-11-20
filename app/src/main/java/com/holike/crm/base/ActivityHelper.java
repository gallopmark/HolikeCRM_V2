package com.holike.crm.base;


import android.view.View;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.IdRes;
import androidx.annotation.StringRes;

/**
 * Created by pony on 2019/9/19.
 * Copyright holike possess 2019.
 */
public abstract class ActivityHelper extends HelperWrapper implements BaseHelper {

    protected BaseActivity<?, ?> mActivity;

    public ActivityHelper(BaseActivity<?, ?> activity) {
        this.mActivity = activity;
    }

    protected <T extends View> T obtainView(@IdRes int id) {
        return mActivity.findViewById(id);
    }

    protected void setText(@IdRes int id, @StringRes int resId) {
        setText(id, getString(mActivity, resId));
    }

    protected TextView obtainTextView(@IdRes int id) {
        return obtainView(mActivity, id);
    }

    protected void setText(@IdRes int id, CharSequence text) {
        obtainTextView(id).setText(text);
    }

    protected void setTextColor(@IdRes int id, @ColorRes int colorId) {
        obtainTextView(id).setTextColor(getColor(mActivity, colorId));
    }
}
