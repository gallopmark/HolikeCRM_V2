package com.holike.crm.customView;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import androidx.annotation.Nullable;

import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.holike.crm.R;

/**
 * Created by wqj on 2018/4/18.
 * 圆形进度条
 */

public class CircleProgressBar extends View {

    //进度条背景色
    private int mCircleBgColor = Color.BLUE;
    //进度条文字展示颜色
    private int mTextColor = Color.GRAY;
    //进度条宽度
    private float mCircleWidth = 50;
    //进度条颜色
    private int mProgressColor = Color.GREEN;
    //进度条展示文字大小
    private float mTextSize = 50;

    private float progress, animatorProgress;
    private float max = 100;
    private Paint paint = new Paint();
    private ValueAnimator mAnimator;
    private String text = "";

    public CircleProgressBar(Context context) {
        super(context);
        init(context, null);
    }

    public CircleProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircleProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar);
        mCircleBgColor = array.getColor(R.styleable.CircleProgressBar_mCircleBgColor, Color.BLUE);
        mTextColor = array.getColor(R.styleable.CircleProgressBar_mTextColor, Color.GRAY);
        mProgressColor = array.getColor(R.styleable.CircleProgressBar_mProgressColor, Color.GREEN);
        mCircleWidth = array.getDimension(R.styleable.CircleProgressBar_mCircleWidth, 50);
        mTextSize = array.getDimension(R.styleable.CircleProgressBar_mTextSize, 50);
        array.recycle();
        initAnimator();
    }

    private void initAnimator() {
        mAnimator = ValueAnimator.ofFloat(0, 1);
        mAnimator.setDuration(1000);
        mAnimator.setStartDelay(100);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                animatorProgress = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画背景
        paint.setColor(mCircleBgColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(mCircleWidth);
        int center = getWidth() / 2;

        int radius = (int) (center - mCircleWidth / 2);
        canvas.drawCircle(center, center, radius, paint);

        //写文字
        paint.setStrokeWidth(0);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(mTextColor);
        paint.setTextSize(mTextSize);
        Paint.FontMetricsInt fm = paint.getFontMetricsInt();
        canvas.drawText(text, center - paint.measureText(text) / 2, center + (fm.bottom - fm.top) / 2 - fm.bottom, paint);


        //画弧形
        RectF oval = new RectF(center - radius, center - radius, center + radius, center + radius);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(mCircleWidth);
        paint.setColor(mProgressColor);
        paint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawArc(oval, 270, 360 * progress / max * animatorProgress, false, paint);

    }

    //设置进度条
    public void setProgress(String text) {
        if(TextUtils.isEmpty(text)) return;
        if (text.equals("-")) {
            this.text = "- %";
        } else {
            this.text = text;
            try {
                progress = Float.parseFloat(text.replace("%", ""));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (progress > max) {
                progress = max;
            }
            if (mAnimator.isRunning()) {
                mAnimator.cancel();
            }
            mAnimator.start();
        }

    }
}
