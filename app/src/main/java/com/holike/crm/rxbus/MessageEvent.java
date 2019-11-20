package com.holike.crm.rxbus;

import android.os.Bundle;

import androidx.annotation.Nullable;

/**
 * Created by pony on 2019/8/23.
 * Copyright holike possess 2019.
 */
public class MessageEvent {
    public int arg1;
    public int arg2;
    public Object obj;

    private Bundle mArguments;

    public String type;

    public MessageEvent() {
    }

    public MessageEvent(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setArguments(Bundle arguments) {
        this.mArguments = arguments;
    }

    @Nullable
    public Bundle getArguments() {
        return mArguments;
    }
}
