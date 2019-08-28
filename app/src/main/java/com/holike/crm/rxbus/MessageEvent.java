package com.holike.crm.rxbus;

import android.os.Bundle;

import androidx.annotation.Nullable;

/**
 * Created by gallop on 2019/8/23.
 * Copyright holike possess 2019.
 */
public class MessageEvent {
    public int arg1;
    public int arg2;
    public Object obj;

    private Bundle mArguments;

    public void setArguments(Bundle arguments) {
        this.mArguments = arguments;
    }

    @Nullable
    public Bundle getArguments() {
        return mArguments;
    }
}
