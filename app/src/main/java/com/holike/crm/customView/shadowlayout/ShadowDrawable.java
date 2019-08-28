package com.holike.crm.customView.shadowlayout;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

@SuppressWarnings("unused,deprecation")
public class ShadowDrawable extends Drawable {

    private Paint mShadowPaint;
    private int mShape;
    private float cornerRadius;
    private RectF mRect;

    ShadowDrawable(int shape, int shadowColor, float shadowRadius, float offsetX, float offsetY, float cornerRadius, int mShadowBackground,RectF mRect) {
        this.mShape = shape;
        this.cornerRadius = cornerRadius;
        this.mRect = mRect;
        mShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mShadowPaint.setColor(mShadowBackground);
        mShadowPaint.setAntiAlias(true);
        mShadowPaint.setShadowLayer(shadowRadius, offsetX, offsetY, shadowColor);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (mShape == ShadowLayoutCompat.SHAPE_RECTANGLE) {
            canvas.drawRoundRect(mRect, cornerRadius, cornerRadius, mShadowPaint);
        } else if (mShape == ShadowLayoutCompat.SHAPE_OVAL) {
            canvas.drawCircle(mRect.centerX(), mRect.centerY(), Math.min(mRect.width(), mRect.height()) / 2, mShadowPaint);
        }
    }

    @Override
    public void setAlpha(int alpha) {
        mShadowPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mShadowPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}