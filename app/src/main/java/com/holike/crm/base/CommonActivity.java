package com.holike.crm.base;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by gallop on 2019/9/19.
 * Copyright holike possess 2019.
 */
public abstract class CommonActivity<P extends BasePresenter, V extends BaseView, H extends ActivityHelper> extends BaseActivity<P, V> {

    protected H mActivityHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityHelper = newHelper();
        setup(savedInstanceState);
    }

    @NonNull
    protected abstract H newHelper();

    protected void setup(Bundle savedInstanceState) {

    }

    @Deprecated
    @Override
    protected void init(Bundle savedInstanceState) {

    }
}
