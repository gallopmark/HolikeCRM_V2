package com.holike.crm.base;

import android.app.Activity;
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

class HelperWrapper {

    <T extends View> T obtainView(Activity activity, @IdRes int id) {
        return activity.findViewById(id);
    }

    <T extends View> T obtainView(View view, @IdRes int id) {
        return view.findViewById(id);
    }

    protected int getColor(Context context, @ColorRes int id) {
        return ContextCompat.getColor(context, id);
    }

    protected Drawable getDrawable(Context context, @DrawableRes int id) {
        return ContextCompat.getDrawable(context, id);
    }

    protected String getString(Context context, @StringRes int resId) {
        return context.getString(resId);
    }

    protected int getDimensionPixelSize(Context context, @DimenRes int id) {
        return context.getResources().getDimensionPixelSize(id);
    }

    protected String getText(TextView tv) {
        return tv.getText().toString();
    }

    protected void clearText(TextView tv) {
        tv.setText(null);
    }
}
