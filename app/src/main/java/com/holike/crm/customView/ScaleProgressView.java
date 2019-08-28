package com.holike.crm.customView;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.holike.crm.util.DensityUtil;

/**
 * Created by wqj on 2018/6/21.
 * 圆形刻度进度图
 */

public class ScaleProgressView extends View {
    private int len;
    private final int startAngle = 40;
    private final int sweepAngle = 280;
    private final int rotateAngleNum = 70;
    private Paint bgPaint;
    private Paint linePaint;
    private Paint arrowPaint;
    private int percentRotateAngleNum;
    private float currentProgress;
    private int arrowWidth = DensityUtil.dp2px(6);
    private int arrowHeight = DensityUtil.dp2px(10);
    private int radius;
    private int scaleWidth = DensityUtil.dp2px(3);
    private int scaleHeight = DensityUtil.dp2px(8);
    private float rotateAngle;
    private ValueAnimator animator;

    public ScaleProgressView(Context context) {
        this(context, null, 0);
    }

    public ScaleProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScaleProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        bgPaint = new Paint();
        bgPaint.setColor(Color.parseColor("#66ffffff"));
        bgPaint.setAntiAlias(true);
        bgPaint.setStyle(Paint.Style.STROKE);
        bgPaint.setStrokeWidth(scaleWidth);
        bgPaint.setStrokeCap(Paint.Cap.ROUND);
        linePaint = new Paint();
        linePaint.setColor(Color.GREEN);
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(scaleWidth);
        linePaint.setStrokeCap(Paint.Cap.ROUND);
        arrowPaint = new Paint();
        arrowPaint.setColor(Color.parseColor("#FFE400"));
        arrowPaint.setAntiAlias(true);
        arrowPaint.setStyle(Paint.Style.FILL);
        rotateAngle = sweepAngle / rotateAngleNum;
    }

    private void startAnimator() {
        if (animator == null) {
            animator = ValueAnimator.ofFloat(0, 1);
        } else {
            animator.cancel();
        }
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentProgress = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        animator.setDuration(2000);
        animator.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        len = Math.min(width, height);
        radius = len / 2 - arrowHeight;
        setMeasuredDimension(len, len);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackgroundLine(canvas);
        drawcurrentLine(canvas);
    }

    private void drawBackgroundLine(Canvas canvas) {
        canvas.save();
        canvas.translate(len / 2, len / 2);
        canvas.rotate(startAngle);
        canvas.drawLine(0, radius, 0, radius - scaleHeight, bgPaint);
        for (int i = 0; i < rotateAngleNum; i++) {
            canvas.rotate(rotateAngle);
            canvas.drawLine(0, radius, 0, radius - scaleHeight, bgPaint);
        }
        canvas.restore();
    }

    private void drawcurrentLine(Canvas canvas) {
        canvas.save();
        canvas.translate(len / 2, len / 2);
        canvas.rotate(startAngle);
        canvas.drawLine(0, radius, 0, radius - scaleHeight, linePaint);
        for (int i = 0, size = (int) (percentRotateAngleNum * currentProgress); i < size; i++) {
            canvas.rotate(rotateAngle);
            canvas.drawLine(0, radius, 0, radius - scaleHeight, linePaint);
        }
        Path path = new Path();
        path.moveTo(arrowWidth / 2, radius + arrowHeight);
        path.lineTo(-arrowWidth / 2, radius + arrowHeight);
        path.lineTo(-arrowWidth / 2, radius + DensityUtil.dp2px(3));
        path.lineTo(0, radius);
        path.lineTo(arrowWidth / 2, radius + DensityUtil.dp2px(3));
        path.close();
        canvas.drawPath(path, arrowPaint);
        canvas.restore();
    }

    public void setProgress(String progress) {
        if (TextUtils.isEmpty(progress)) {
            percentRotateAngleNum = rotateAngleNum;
        } else {
            percentRotateAngleNum = (int) (Float.parseFloat(progress) * rotateAngleNum / 100);
        }
        startAnimator();
    }
}
