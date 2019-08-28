package com.holike.crm.customView;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.holike.crm.R;
import com.holike.crm.activity.analyze.WeekReportActivity;
import com.holike.crm.base.MyApplication;
import com.holike.crm.bean.DayDepositBean;
import com.holike.crm.bean.WeekDepositBean;
import com.holike.crm.util.CanvasUtil;
import com.holike.crm.util.DensityUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by wqj on 2018/3/1.
 * 订单交易报表柱状图
 */

public class WeekReportChartView extends View {
    private Paint mPaint = new Paint();
    private Path mPath = new Path();
    private List<HashMap<String, Float>> mLocationList = new ArrayList<>();
    private List<WeekDepositBean.MoneyDataBean> weekDepositBeans;
    private List<DayDepositBean> dayDepositBeans;

    private int width;
    private int height;
    private int top, left, bottom, right;
    // 底部辅助横线条数
    private int lineCount = 5;
    // 柱形图的宽度
    private float itemWidth = DensityUtil.dp2px(40);
    // 水平线间隔
    private float lineMarge;
    // 柱状图间隔
    private float itemMarge = DensityUtil.dp2px(24);
    //回调
    private OnClickListener mListener;
    //画柱状图动画
    private ValueAnimator mAnimator;
    //动画进度
    private float progress;
    //点击的坐标
    private float downX, downY, upX, upY;
    private int orderType;
    private Paint textPaint = new Paint();

    public WeekReportChartView(Context context) {
        super(context);
        initAnimator();


    }

    public WeekReportChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAnimator();
    }

    public WeekReportChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAnimator();
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
            int max = getMaxGraded(getMaxValue());
            int average = max / (lineCount);

            //画y轴梯度
            mPaint.setColor(getResources().getColor(R.color.histogram_line));
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setPathEffect(new DashPathEffect(new float[]{5, 5}, 0));

            textPaint.setColor(Color.parseColor("#ffffff"));
            textPaint.setStyle(Paint.Style.STROKE);
            textPaint.setAntiAlias(true);
            textPaint.setTextSize(DensityUtil.dp2px(12));
            for (int i = 0; i <= lineCount; i++) {
                mPath.reset();
                mPath.moveTo(DensityUtil.dp2px(39), height - bottom - lineMarge * i);
                mPath.lineTo(width, height - bottom - lineMarge * i);
                canvas.drawPath(mPath, mPaint);//画虚线
                canvas.drawText(String.valueOf(average * i), DensityUtil.dp2px(12), height - bottom - lineMarge * i - 4, textPaint);//画刻度
            }
            mLocationList.clear();
            mPaint.setStyle(Paint.Style.FILL);
            for (int i = 0; i < getSize(); i++) {
                mPaint.setColor(getResources().getColor((R.color.histogram_column)));
                float value = getValue(i);
                canvas.drawRect(left + (itemMarge + itemWidth) * i, (height - bottom - (height - top - bottom) * (value / max) * progress), left + (itemMarge + itemWidth) * i + itemWidth, height - bottom, mPaint);
                mPaint.setColor(Color.WHITE);
                mPaint.setTextSize(DensityUtil.dp2px(12));
                String name = getName(i);
                String num = orderType == WeekReportActivity.TYPE_ORDER_REPORT ? String.valueOf((int) value) : String.valueOf(value);//订单不要小数点
                //x轴名字和y轴值
                drawName(canvas, name, i);
                if (value > 0) {
                    canvas.drawText(num, left + (itemMarge + itemWidth) * i - (CanvasUtil.getTextWidth(num, mPaint) - itemWidth) / 2, (height - bottom - (height - top - bottom) * (value / max) - DensityUtil.dp2px(5)), mPaint);
                }
                HashMap<String, Float> map = new HashMap<>();
                map.put("left", left + (itemMarge + itemWidth) * i);
                map.put("top", Float.valueOf((height - bottom - (height - top - bottom) * (value / max) - DensityUtil.dp2px(12 * 2))));
                map.put("right", left + (itemMarge + itemWidth) * i + itemWidth);
                map.put("bottom", Float.valueOf(height - bottom));
                mLocationList.add(map);
            }
        }
    }

    private void textCenter(String string, Canvas canvas, float x, float y) {//写文字
        TextPaint textPaint = new TextPaint();
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(Color.parseColor("#ffffff"));
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(DensityUtil.dp2px(14));
        StaticLayout staticLayout = new StaticLayout(string, textPaint, (int) (itemWidth + itemMarge), Layout.Alignment.ALIGN_CENTER, 1f, 0, false);
        canvas.save();
        canvas.translate(x, y);
        staticLayout.draw(canvas);
        canvas.restore();
    }

    private void drawName(Canvas canvas, String name, int i) {
        if (weekDepositBeans != null) {
            textCenter(name, canvas, left + (itemMarge + itemWidth) * i - itemMarge / 2, height - bottom + DensityUtil.dp2px(12));
        } else {//竖着写
            char[] names = name.toCharArray();
            for (int j = 0, length = names.length; j < length; j++) {
                String text = String.valueOf(names[j]);
                canvas.drawText(text, left + (itemMarge + itemWidth) * i - (CanvasUtil.getTextWidth(text, mPaint) - itemWidth) / 2, height - bottom + mPaint.getTextSize() * (2 + j), mPaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                upX = event.getX();
                upY = event.getY();
                if ((upX < downX + 5 && upX > downX - 5) && (upY < downY + 5 && upY > downY - 5)) {
                    for (int i = 0; i < mLocationList.size(); i++) {
                        if (downX >= mLocationList.get(i).get("left") && downX <= mLocationList.get(i).get("right") && downY >= mLocationList.get(i).get("top") && downY <= mLocationList.get(i).get("bottom")) {
                            if (null != mListener && getValue(i) > 0 && weekDepositBeans != null) {
                                mListener.onClick(i, getTimeStamp(i), String.valueOf(getValue(i)));
                            }
                        }
                    }
                }
                break;
        }
        return true;
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
        top = DensityUtil.dp2px(20);
        right = DensityUtil.dp2px(12);
        left = CanvasUtil.getTextWidth(String.valueOf(getMaxGraded(getMaxValue())), mPaint) + DensityUtil.dp2px(12 * 2);
        requestLayout();
        if (mAnimator.isRunning()) {
            mAnimator.cancel();
        }
        mAnimator.start();
    }

    public void setWeekDepositBeans(List<WeekDepositBean.MoneyDataBean> weekDepositBeans, int orderType) {
        this.weekDepositBeans = weekDepositBeans;
        this.orderType = orderType;
        dayDepositBeans = null;
        bottom = DensityUtil.dp2px(12) * 6;
        init();
    }

    public void setDayDepositBeans(List<DayDepositBean> dayDepositBeans) {
        this.dayDepositBeans = dayDepositBeans;
        weekDepositBeans = null;
        bottom = DensityUtil.dp2px(12) * 6;
        init();
    }

    private int getSize() {
        if (dayDepositBeans != null) {
            return dayDepositBeans.size();
        } else if (weekDepositBeans != null) {
            return weekDepositBeans.size();
        } else {
            return 0;
        }
    }

    private float getValue(int position) {
        if (dayDepositBeans != null) {
            return (int) dayDepositBeans.get(position).getMoney();
        } else if (weekDepositBeans != null) {
            return getValueByOrderType(weekDepositBeans.get(position));
        } else {
            return 0;
        }
    }

    private String getName(int position) {
        if (dayDepositBeans != null) {
            return dayDepositBeans.get(position).getName();
        } else if (weekDepositBeans != null) {
            WeekDepositBean.MoneyDataBean bean = weekDepositBeans.get(position);
            if (TextUtils.isEmpty(bean.getMonth())) {
                return bean.getStartTime() + "\n至\n" + bean.getEndTime();
            } else {
                return bean.getMonth();
            }
        } else {
            return "";
        }
    }

    private String getTimeStamp(int position) {
        if (weekDepositBeans != null) {
            return weekDepositBeans.get(position).getTimeStamp();
        } else {
            return "";
        }
    }

    /**
     * 获取集合中的最大值
     */
    private float getMaxValue() {
        float max = 0;
        if (weekDepositBeans != null) {
            for (WeekDepositBean.MoneyDataBean bean : weekDepositBeans) {
                if ((getValueByOrderType(bean)) > max) {
                    max = getValueByOrderType(bean);
                }
            }
        } else {
            for (DayDepositBean bean : dayDepositBeans) {
                if (bean.getMoney() > max) {
                    max = bean.getMoney();
                }
            }
        }
        return max;
    }

    /**
     * 根据选择订单数/订金额返回值
     *
     * @param bean
     * @return
     */
    private float getValueByOrderType(WeekDepositBean.MoneyDataBean bean) {
        return orderType == WeekReportActivity.TYPE_ORDER_REPORT ? bean.getCounts() : bean.getMoney();
    }

    /**
     * 获取最大梯度
     *
     * @param max
     * @return
     */
    public static int getMaxGraded(float max) {
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

    /**
     * 设置回调
     */
    public void setOnClickListener(OnClickListener mListener) {
        this.mListener = mListener;
    }

    public interface OnClickListener {
        void onClick(int position, String time, String money);
    }
}
