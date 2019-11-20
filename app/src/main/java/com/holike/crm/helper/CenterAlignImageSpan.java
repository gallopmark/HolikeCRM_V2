package com.holike.crm.helper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by pony on 2019/11/7.
 * Version v3.0 app报表
 * 图文混排，图片居中对齐方案
 */
public class CenterAlignImageSpan extends ImageSpan {
    public CenterAlignImageSpan(@NonNull Drawable drawable) {
        super(drawable);
    }

    public CenterAlignImageSpan(@NonNull Context context, int resourceId) {
        super(context, resourceId);
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
        Drawable drawable = getDrawable();
        Paint.FontMetricsInt fontMetricsInt = paint.getFontMetricsInt();
        int transY = (y + fontMetricsInt.descent + y + fontMetricsInt.ascent) / 2 - drawable.getBounds().bottom / 2;
        canvas.save();
        canvas.translate(x, transY);
        drawable.draw(canvas);
        canvas.restore();
    }
}
