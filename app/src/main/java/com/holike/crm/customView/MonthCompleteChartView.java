package com.holike.crm.customView;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.holike.crm.R;
import com.holike.crm.base.MyApplication;
import com.holike.crm.bean.MonthCompleteBean;
import com.holike.crm.util.CanvasUtil;
import com.holike.crm.util.DensityUtil;
import com.holike.crm.util.ParseUtils;

import java.util.List;

/**
 * Created by wqj on 2018/4/18.
 * 各月完成率柱状图
 */

public class MonthCompleteChartView extends View {
    private int MAX = 100;
    private int width;
    private int height;
    private int top, left, bottom, right;
    // 底部辅助横线条数
    private int lineCount = 5;
    // 刻度距离左边
    private float lineLeft = DensityUtil.dp2px(56);
    // 柱形图的宽度
    private float itemWidth = DensityUtil.dp2px(48);
    // 水平线间隔
    private float lineMarge;
    // 柱状图间隔
    private float itemMarge = DensityUtil.dp2px(24);
    //画柱状图动画
    private ValueAnimator mAnimator;
    //动画进度
    private float progress;
    private Paint mPaint = new Paint();
    private Paint linePaint = new Paint();
    private Paint textPaint = new Paint();
    private Path mPath = new Path();
    private Context mContext;
    private List<? extends MonthCompleteBean> beans;

    public MonthCompleteChartView(Context context) {
        super(context);
        this.mContext = context;
        initAnimator();
    }

    public MonthCompleteChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initAnimator();
    }

    public MonthCompleteChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initAnimator();
    }

    private void initAnimator() {
        mAnimator = ValueAnimator.ofFloat(0, 1);
        mAnimator.setDuration(1000);
        mAnimator.setStartDelay(100);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                progress = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
    }

    private void init() {
        progress = 0;
        mPaint.setTextSize(DensityUtil.dp2px(12));
        mPaint.setAntiAlias(true);
        top = DensityUtil.dp2px(20);
        right = DensityUtil.dp2px(12);
        left = CanvasUtil.getTextWidth(String.valueOf(MAX), mPaint) + DensityUtil.dp2px(12 + 36);
        requestLayout();
        if (mAnimator.isRunning()) {
            mAnimator.cancel();
        }
        mAnimator.start();
    }

    public void setBeans(List<? extends MonthCompleteBean> beans) {
        this.beans = beans;
        bottom = DensityUtil.dp2px(12) * 8;
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = (int) (left + getSize() * (itemWidth + itemMarge));
        height = MeasureSpec.getSize(heightMeasureSpec);
        if (width < MyApplication.getInstance().screenWidth) {
            width = MyApplication.getInstance().screenWidth;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (getSize() > 0) {
            //绘制底部阶梯标准线
            lineMarge = (height - bottom - top) / lineCount;
            MAX = getMaxGraded(getMaxValue());
            int average = MAX / (lineCount);

            //画y轴梯度
            linePaint.setColor(mContext.getResources().getColor(R.color.histogram_line));
            linePaint.setStyle(Paint.Style.STROKE);
            linePaint.setPathEffect(new DashPathEffect(new float[]{5, 5}, 0));

            for (int i = 0; i <= lineCount; i++) {
                mPath.reset();
                mPath.moveTo(lineLeft, height - bottom - (lineMarge + 1) * i);
                mPath.lineTo(width, height - bottom - (lineMarge + 1) * i);
                canvas.drawPath(mPath, linePaint);//画虚线
                canvas.drawText(average * i + "%", DensityUtil.dp2px(12), height - bottom - lineMarge * i - 4, mPaint);//画刻度
            }

            mPaint.setStyle(Paint.Style.FILL);
            for (int i = 0; i < getSize(); i++) {
                //进度
                mPaint.setColor(mContext.getResources().getColor((R.color.histogram_column)));
                mPaint.setStyle(Paint.Style.FILL);
                float value = getValue(i);
                canvas.drawRect(left + (itemMarge + itemWidth) * i, (height - bottom - (height - top - bottom) * (value / MAX) * progress), left + (itemMarge + itemWidth) * i + itemWidth, height - bottom, mPaint);
                mPaint.setColor(Color.WHITE);
                mPaint.setTextSize(DensityUtil.dp2px(12));
                //x轴名字和y轴值
                CanvasUtil.textCenter(beans.get(i).getMonth(), canvas, left + (itemMarge + itemWidth) * i - itemMarge / 2, height - bottom + DensityUtil.dp2px(12), (int) (itemWidth + itemMarge), 14, Color.parseColor("#ffffff"));
                CanvasUtil.textCenter(beans.get(i).getDay().replace("-", "\n至\n"), canvas, left + (itemMarge + itemWidth) * i - itemMarge / 2, height - bottom + DensityUtil.dp2px(12 * 2 + 14), (int) (itemWidth + itemMarge), 12, Color.parseColor("#ffffff"));
                if (!TextUtils.isEmpty(getText(i))) {
                    canvas.drawText(getText(i), left + (itemMarge + itemWidth) * i - (CanvasUtil.getTextWidth(getText(i), mPaint) - itemWidth) / 2, (height - bottom - (height - top - bottom) * (value / MAX) - DensityUtil.dp2px(5)), mPaint);
                }
            }
        }
    }

    private int getSize() {
        if (beans != null) {
            return beans.size();
        } else {
            return 0;
        }
    }

    private float getValue(int position) {
        if (beans != null && !TextUtils.isEmpty(beans.get(position).getDepositPercent())) {
            return ParseUtils.parseFloat(beans.get(position).getDepositPercent().replace("%", ""));
        } else {
            return 0;
        }
    }

    private String getText(int position) {
        if (beans != null) {
            return beans.get(position).getDepositPercent();
        } else {
            return "";
        }
    }

    /**
     * 获取集合中的最大值
     */
    private float getMaxValue() {
        float max = 0;
        if (beans != null) {
            for (MonthCompleteBean bean : beans) {
                if (!TextUtils.isEmpty(bean.getDepositPercent())) {
                    float i = ParseUtils.parseFloat(bean.getDepositPercent().replace("%", ""));
                    if (i > max) {
                        max = i;
                    }
                }
            }
        }
        return max;
    }

    /**
     * 获取最大梯度
     *
     * @param max
     * @return
     */
    private int getMaxGraded(float max) {
        if (max > 10) {
            int i = 1;
            for (; max > 10; ) {
                max = max / 10;
                i = i * 10;
            }
            return (int) (i * (Math.ceil(max)));
        } else {
            return 10;
        }
    }
}
