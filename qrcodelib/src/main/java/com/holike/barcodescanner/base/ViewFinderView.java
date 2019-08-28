package com.holike.barcodescanner.base;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.holike.barcodescanner.R;
import com.holike.barcodescanner.util.DensityUtil;

/**
 * 覆盖在相机预览上的view，包含扫码框、扫描线、扫码框周围的阴影遮罩等
 */
public class ViewFinderView extends View implements IViewFinder {
    private Rect framingRect;//扫码框所占区域
    private float widthRatio = 0.77f;//扫码框宽度占view总宽度的比例 290dp/333dp
    private float heightWidthRatio = 1.0f;//扫码框的高宽比
    private int leftOffset = -1;//扫码框相对于左边的偏移量，若为负值，则扫码框会水平居中
    private int topOffset = -DensityUtil.dp2px(189);//扫码框相对于顶部的偏移量，若为负值，则扫码框会竖直居中

    private boolean isLaserEnabled = true;//是否显示扫描线
    private static final int[] laserAlpha = {0, 64, 128, 192, 255, 192, 128, 64};
    private int laserAlphaIndex;
    private static final int pointSize = 10;
    private static final long animationDelay = 80l;
    //扫描线颜色
    private final int laserColor = Color.parseColor("#007AFF");

    private final int maskColor = Color.parseColor("#CC000000");
    private final int maskLineColor = Color.parseColor("#007AFF");
    private final int borderColor = Color.parseColor("#007AFF");
    private final int borderStrokeWidth = DensityUtil.dp2px(6);
    private final int maskLineStrokeWidth = DensityUtil.dp2px(2);
    protected int borderLineLength = 100;

    protected Paint laserPaint;
    protected Paint maskPaint;
    protected Paint borderPaint;
    protected Paint maskLinePaint;

    private Bitmap scanLight;
    private ValueAnimator valueAnimator;
    private int scanLineTop;

    public ViewFinderView(Context context) {
        super(context);
        init();
    }

    public void setTopOffset(int topOffset) {
        this.topOffset = topOffset;
    }

    public ViewFinderView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    private void init() {
        //扫描线画笔
        laserPaint = new Paint();
        laserPaint.setColor(laserColor);
        laserPaint.setStyle(Paint.Style.FILL);

        //阴影遮罩画笔
        maskPaint = new Paint();
        maskPaint.setColor(maskColor);

        //边框画笔
        borderPaint = new Paint();
        borderPaint.setColor(borderColor);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(borderStrokeWidth);
        borderPaint.setAntiAlias(true);

        //阴影边框边线画笔
        maskLinePaint = new Paint();
        maskLinePaint.setColor(maskLineColor);
        maskLinePaint.setStyle(Paint.Style.STROKE);
        maskLinePaint.setStrokeWidth(maskLineStrokeWidth);
        maskLinePaint.setAntiAlias(true);

        scanLight = BitmapFactory.decodeResource(getResources(),
                R.drawable.scanning_line);
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (getFramingRect() == null) {
            return;
        }
        initAnimator(framingRect);
        drawViewFinderMask(canvas);
        drawViewFinderBorder(canvas);
        drawViewFinderMaskLine(canvas);

        if (isLaserEnabled) {
            drawLaser(canvas);
        }
    }

    private void drawViewFinderMaskLine(Canvas canvas) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        Rect framingRect = getFramingRect();
        Path path = new Path();
        path.moveTo(framingRect.left - borderStrokeWidth / 2 + 1, framingRect.top - borderStrokeWidth / 2);
        path.lineTo(framingRect.left - borderStrokeWidth / 2, framingRect.bottom + borderStrokeWidth / 2);
        path.lineTo(framingRect.right + borderStrokeWidth / 2, framingRect.bottom + borderStrokeWidth / 2);
        path.lineTo(framingRect.right + borderStrokeWidth / 2, framingRect.top - borderStrokeWidth / 2);
        path.lineTo(framingRect.left - borderStrokeWidth / 2 - 1, framingRect.top - borderStrokeWidth / 2);
        canvas.drawPath(path, maskLinePaint);

    }

    /**
     * 绘制扫码框四周的阴影遮罩
     */
    public void drawViewFinderMask(Canvas canvas) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        Rect framingRect = getFramingRect();

        canvas.drawRect(0, 0, width, framingRect.top - borderStrokeWidth / 2, maskPaint);//扫码框顶部阴影
        canvas.drawRect(0, framingRect.top - borderStrokeWidth / 2, framingRect.left - borderStrokeWidth / 2, framingRect.bottom + borderStrokeWidth / 2, maskPaint);//扫码框左边阴影
        canvas.drawRect(framingRect.right + borderStrokeWidth / 2, framingRect.top - borderStrokeWidth / 2, width + borderStrokeWidth, framingRect.bottom + borderStrokeWidth / 2, maskPaint);//扫码框右边阴影
        canvas.drawRect(0, framingRect.bottom + borderStrokeWidth / 2, width, height + borderStrokeWidth, maskPaint);//扫码框底部阴影
    }

    /**
     * 绘制扫码框的边框
     */
    public void drawViewFinderBorder(Canvas canvas) {
        Rect framingRect = getFramingRect();

        // Top-left corner
        Path path = new Path();
        path.moveTo(framingRect.left, framingRect.top + borderLineLength);
        path.lineTo(framingRect.left, framingRect.top);
        path.lineTo(framingRect.left + borderLineLength, framingRect.top);
        canvas.drawPath(path, borderPaint);

        // Top-right corner
        path.moveTo(framingRect.right, framingRect.top + borderLineLength);
        path.lineTo(framingRect.right, framingRect.top);
        path.lineTo(framingRect.right - borderLineLength, framingRect.top);
        canvas.drawPath(path, borderPaint);

        // Bottom-right corner
        path.moveTo(framingRect.right, framingRect.bottom - borderLineLength);
        path.lineTo(framingRect.right, framingRect.bottom);
        path.lineTo(framingRect.right - borderLineLength, framingRect.bottom);
        canvas.drawPath(path, borderPaint);

        // Bottom-left corner
        path.moveTo(framingRect.left, framingRect.bottom - borderLineLength);
        path.lineTo(framingRect.left, framingRect.bottom);
        path.lineTo(framingRect.left + borderLineLength, framingRect.bottom);
        canvas.drawPath(path, borderPaint);
    }

    private void initAnimator(Rect frame) {
        if (valueAnimator == null) {
            valueAnimator = ValueAnimator.ofInt(frame.top + borderStrokeWidth, frame.bottom - borderStrokeWidth);
            valueAnimator.setDuration(2500);
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.setRepeatMode(ValueAnimator.RESTART);
            valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {

                    scanLineTop = (int) animation.getAnimatedValue();
                    invalidate();

                }
            });

            valueAnimator.start();
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void setAnimatorEnd() {
        valueAnimator.pause();
    }


    public void setAnimatorStart() {
        valueAnimator.start();
    }


    /**
     * 绘制扫描线
     */
    public void drawLaser(Canvas canvas) {
        Rect framingRect = getFramingRect();
        Rect scanRect = new Rect(framingRect.left, scanLineTop, framingRect.right,
                scanLineTop + 10);
        canvas.drawBitmap(scanLight, null, scanRect, maskLinePaint);

    }

    @Override
    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld) {
        updateFramingRect();
    }

    /**
     * 设置framingRect的值（扫码款所占的区域）
     */
    public synchronized void updateFramingRect() {
        Point viewSize = new Point(getWidth(), getHeight());
        int width, height;
        width = (int) (getWidth() * widthRatio);
        height = (int) (heightWidthRatio * width);

        int left, top;
        if (leftOffset < 0) {
            left = (viewSize.x - width) / 2;//水平居中
        } else {
            left = leftOffset;
        }
        if (topOffset < 0) {
            top = (viewSize.y - height) / 2;//竖直居中
        } else {
            top = topOffset;
        }
        framingRect = new Rect(left, top, left + width, top + height);
    }

    public Rect getFramingRect() {
        return framingRect;
    }
}