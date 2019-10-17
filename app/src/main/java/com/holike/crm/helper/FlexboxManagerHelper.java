package com.holike.crm.helper;

import android.content.Context;

import com.google.android.flexbox.FlexboxLayoutManager;

public class FlexboxManagerHelper {

    public static FlexboxLayoutManager getDefault(Context context) {
        return new FlexboxLayoutManager(context);
    }
}
