package com.holike.crm.base;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by gallop on 2019/9/20.
 * Copyright holike possess 2019.
 */
public abstract class CommonFragment<P extends BasePresenter, V extends BaseView, H extends BaseHelper> extends BaseFragment<P, V> {

    protected H mHelper;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mHelper == null) {
            mHelper = newHelper();
            setup();
        }
    }

    @Deprecated
    @Override
    protected void init() {
    }

    @NonNull
    protected abstract H newHelper();

    protected void setup() {
    }
}
