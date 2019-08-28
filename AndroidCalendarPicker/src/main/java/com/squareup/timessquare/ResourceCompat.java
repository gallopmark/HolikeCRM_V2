package com.squareup.timessquare;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;

public class ResourceCompat {

    public static ColorStateList getColorStateList(Context context, int id) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? context.getColorStateList(id) : context.getResources().getColorStateList(id);
    }

    public static int getColor(Context context, int id) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? context.getColor(id) : context.getResources().getColor(id);
    }
}
