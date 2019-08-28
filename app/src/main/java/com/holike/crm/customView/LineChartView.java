package com.holike.crm.customView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.holike.crm.base.MyApplication;
import com.holike.crm.bean.OriginalBoardBean;
import com.holike.crm.util.CanvasUtil;
import com.holike.crm.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wqj on 2018/6/23.
 * 折线图
 */

public class LineChartView extends View {
    private int width;
    private int height;
    private int lineNum = 5;
    private int lineLeft = DensityUtil.dp2px(49);
    private int lineRight = DensityUtil.dp2px(12);
    private int lineTop = DensityUtil.dp2px(36);
    private int lineBottom = DensityUtil.dp2px(61);
    private int lineTextLeft = DensityUtil.dp2px(12);
    private int xSpace = DensityUtil.dp2px(58);
    private int ySpace;
    private int pointRadius = DensityUtil.dp2px(5);
    private int lineWidth = DensityUtil.dp2px(1);
    private int textSize = DensityUtil.dp2px(12);
    private int textWidth;

    private Paint linePaint;
    private Paint paint;
    private int downX;
    private int downY;
    private int max;

    private List<OriginalBoardBean.DealerDataBean> datas;
    private boolean isSelect;
    private boolean needPercen;
    private int select;

    public LineChartView(Context context) {
        super(context);
    }

    public LineChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LineChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setDatas(List<OriginalBoardBean.DealerDataBean> datas, boolean needPercen) {
        this.datas = new ArrayList<>();
        this.needPercen = needPercen;
        for (int i = datas.size() - 1; i >= 0; i--) {
            OriginalBoardBean.DealerDataBean bean = datas.get(i);
            if (!bean.getMonth().equals("全年")) {
                this.datas.add(bean);
            }
        }
        requestLayout();
        init();
    }

    private void init() {
        width = MyApplication.getInstance().screenWidth;
        xSpace = (width - lineLeft - lineRight) / 11;
        linePaint = new Paint();
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(Color.parseColor("#66ffffff"));
        linePaint.setPathEffect(new DashPathEffect(new float[]{5, 5}, 0));
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (datas.size() > 0) {
            ySpace = (height - lineTop - lineBottom) / lineNum;
            drawXY(canvas);
            drawLine(canvas);
            drawSelect(canvas);
        }
    }

    /**
     * 画XY轴、梯度
     *
     * @param canvas
     */
    private void drawXY(Canvas canvas) {
        //画梯度虚线
        paint.setColor(Color.parseColor("#ffffff"));
        paint.setTextSize(textSize);
        paint.setStyle(Paint.Style.FILL);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        Path mPath = new Path();
        for (int i = 0; i <= lineNum; i++) {
            mPath.reset();
            mPath.moveTo(lineLeft, height - lineBottom - (i * ySpace));
            mPath.lineTo(width - lineRight, height - lineBottom - (i * ySpace));
            canvas.drawPath(mPath, linePaint);
        }
        //画y轴刻度
        max = WeekReportChartView.getMaxGraded(getMaxValue());
        int average = max / (lineNum);
        for (int i = 0; i <= lineNum; i++) {
            canvas.drawText(i * average + (needPercen ? "%" : ""), lineTextLeft, height - lineBottom - (i * ySpace), paint);
        }
        //画x轴刻度
        for (int i = 0; i < 12; i++) {
            String text = (i + 1) + "月";
            CanvasUtil.textCenter(text, canvas, lineLeft + xSpace * i - CanvasUtil.getTextWidth(text, paint) / 2, height - lineBottom + DensityUtil.dp2px(16), CanvasUtil.getTextWidth(text, paint), 12, Color.parseColor("#ffffff"));
        }
    }

    /**
     * 画折线
     *
     * @param canvas
     */
    private void drawLine(Canvas canvas) {
        //画折线
        paint.setStrokeWidth(lineWidth);
        paint.setColor(Color.parseColor("#FFFF00"));
//        int start = 0;
//        for (int i = 0, size = datas.size(); i < size; i++) {
//            if (!TextUtils.isEmpty(datas.getInstance(i).getCountsComplete())) {
//                if (!TextUtils.isEmpty(datas.getInstance(start).getCountsComplete())) {
//            canvas.drawLine(xSpace * (start) + lineLeft, getPointY(getPercent(datas.getInstance(start).getCountsComplete())), xSpace * i + lineLeft, getPointY(getPercent(datas.getInstance(i).getCountsComplete())), paint);
//                }
//            start = i;
//            }
//        }
        drawArea(canvas);
    }

    //画区域
    private void drawArea(Canvas canvas) {
        paint.setColor(Color.parseColor("#66008aff"));
        Path path = new Path();
        boolean isFirst = true;
        float start = lineLeft, end = lineLeft;
        for (int i = 0, size = datas.size(); i < size; i++) {
//            if (!TextUtils.isEmpty(datas.getInstance(i).getCountsComplete())) {
            if (isFirst) {
                start = xSpace * i + lineLeft;
                path.moveTo(xSpace * i + lineLeft, getPointY(getPercent(datas.get(i).getCountsComplete())));
                isFirst = false;
            } else {
                end = xSpace * i + lineLeft;
                path.lineTo(xSpace * i + lineLeft, getPointY(getPercent(datas.get(i).getCountsComplete())));
            }
//            }
        }
        path.lineTo(end, height - lineBottom);
        path.lineTo(start, height - lineBottom);
        path.close();
        canvas.drawPath(path, paint);
    }

    /**
     * 画选中部分
     *
     * @param canvas
     */
    private void drawSelect(Canvas canvas) {
        //画选中显示
        if (isSelect) {
            //画竖线
            paint.setColor(Color.parseColor("#ffffff"));
            paint.setStrokeWidth(DensityUtil.dp2px(1));
            canvas.drawLine(xSpace * select + lineLeft, DensityUtil.dp2px(21), xSpace * select + lineLeft, height - lineBottom, paint);
            //画实心底色圆
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.parseColor("#0045A7"));
            canvas.drawCircle(xSpace * select + lineLeft, getPointY(getPercent(datas.get(select).getCountsComplete())), pointRadius, paint);
            //画点
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(DensityUtil.dp2px(2));
            paint.setColor(Color.parseColor("#FFFF00"));
            canvas.drawCircle(xSpace * select + lineLeft, getPointY(getPercent(datas.get(select).getCountsComplete())), pointRadius, paint);
            //画箭头框
            Path path1 = new Path();
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.FILL);
            int textLeft;
            textWidth = CanvasUtil.getTextWidth(getText(), paint) + DensityUtil.dp2px(13 + 4 + 7);
            if (select == 0) {
                textLeft = lineLeft;
                path1.moveTo(lineLeft, 0);
                path1.lineTo(lineLeft + textWidth, 0);
                path1.lineTo(lineLeft + textWidth, DensityUtil.dp2px(16));
                path1.lineTo(lineLeft + DensityUtil.dp2px(4), DensityUtil.dp2px(16));
                path1.lineTo(lineLeft, DensityUtil.dp2px(21));
                path1.close();
                canvas.drawPath(path1, paint);
            } else if (select == datas.size() - 1) {
                textLeft = lineLeft + select * xSpace - textWidth;
                path1.moveTo(lineLeft + select * xSpace - textWidth, 0);
                path1.lineTo(lineLeft + select * xSpace, 0);
                path1.lineTo(lineLeft + select * xSpace, DensityUtil.dp2px(21));
                path1.lineTo(lineLeft + select * xSpace - DensityUtil.dp2px(4), DensityUtil.dp2px(16));
                path1.lineTo(lineLeft + select * xSpace - textWidth, DensityUtil.dp2px(16));
                path1.close();
                canvas.drawPath(path1, paint);
            } else {
                textLeft = lineLeft + select * xSpace - textWidth / 2;
                path1.moveTo(lineLeft + select * xSpace - textWidth / 2, 0);
                path1.lineTo(lineLeft + select * xSpace + textWidth / 2, 0);
                path1.lineTo(lineLeft + select * xSpace + textWidth / 2, DensityUtil.dp2px(16));
                path1.lineTo(lineLeft + select * xSpace + DensityUtil.dp2px(4), DensityUtil.dp2px(16));
                path1.lineTo(lineLeft + select * xSpace, DensityUtil.dp2px(21));
                path1.lineTo(lineLeft + select * xSpace - DensityUtil.dp2px(4), DensityUtil.dp2px(16));
                path1.lineTo(lineLeft + select * xSpace - textWidth / 2, DensityUtil.dp2px(16));
                path1.close();
                canvas.drawPath(path1, paint);
            }
            //写文字
            paint.setColor(Color.parseColor("#0045A7"));
            paint.setTypeface(Typeface.DEFAULT);
            canvas.drawText(getText(), textLeft + DensityUtil.dp2px(13 + 4), textSize, paint);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(lineWidth);
            canvas.drawCircle(textLeft + DensityUtil.dp2px(8), DensityUtil.dp2px(8), pointRadius, paint);

        }
    }

    @NonNull
    private String getText() {
        return datas.get(select).getMonth() + "：" + (TextUtils.isEmpty(datas.get(select).getCountsComplete()) ? "-" : datas.get(select).getCountsComplete());
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                downY = (int) event.getY();
                break;
            case MotionEvent.ACTION_UP:
                int upX = (int) event.getX();
                int upY = (int) event.getY();
                if (downX == upX && downY == upY) {
                    for (int i = 0, size = datas.size(); i < size; i++) {
                        if (downX < i * xSpace + lineLeft + xSpace / 2) {
                            if (i == 0 || downX > i * xSpace + lineLeft - xSpace / 2) {
                                isSelect = true;
                                select = i;
                                postInvalidate();
                            }
                        } else if (downX > i * xSpace + lineLeft - xSpace / 2 && downX < width - lineRight && i == size - 1) {
                            isSelect = true;
                            select = i;
                            postInvalidate();
                        }
                    }
                }
                break;
        }

        return true;
    }

    private float getPointY(float point) {
        return (max - point) * (height - lineTop - lineBottom) / max + lineTop;
    }

    private float getPercent(String text) {
        float percent = 0;
        if (!TextUtils.isEmpty(text)) {
            try {
                percent = Float.parseFloat(text.replace("%", ""));
            } catch (Exception e) {
                return percent;
            }
        } else {
            return 0;
        }
        return percent;
    }

    private float getMaxValue() {
        float max = 0;
        for (OriginalBoardBean.DealerDataBean bean : datas) {
            if (getPercent(bean.getCountsComplete()) > max) {
                max = getPercent(bean.getCountsComplete());
            }
        }
        return max;
    }
}
