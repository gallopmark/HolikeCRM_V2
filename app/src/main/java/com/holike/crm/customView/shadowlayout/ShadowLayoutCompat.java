package com.holike.crm.customView.shadowlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import androidx.core.view.ViewCompat;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.holike.crm.R;

/*会引发OOM问题 改为layer-list方式实现*/
@Deprecated
public class ShadowLayoutCompat extends FrameLayout {
    private int shadowWidth;//阴影宽度
    private int backgroundRound;//背景圆角
    private int shadowColor;//阴影颜色
    private int backgroundColor;//背景颜色
    private int offsetX;//上下偏移(正数上偏移，负数下偏移)
    private int offsetY;//左右偏移(正数左偏移，负数右偏移)

    private boolean mInvalidateShadowOnSizeChanged = true;
    private boolean mForceInvalidateShadow = false;

    public ShadowLayoutCompat(Context context) {
        super(context);
        initView(context, null);
    }

    public void setOffset(int offsetX, int offsetY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public ShadowLayoutCompat(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public ShadowLayoutCompat(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        return 0;
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        return 0;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w > 0 && h > 0 && (getBackground() == null || mInvalidateShadowOnSizeChanged || mForceInvalidateShadow)) {
            mForceInvalidateShadow = false;
            setBackgroundCompat(w, h);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mForceInvalidateShadow) {
            mForceInvalidateShadow = false;
            setBackgroundCompat(right - left, bottom - top);
        }
    }

    public void setInvalidateShadowOnSizeChanged(boolean invalidateShadowOnSizeChanged) {
        mInvalidateShadowOnSizeChanged = invalidateShadowOnSizeChanged;
    }

    public void invalidateShadow() {
        mForceInvalidateShadow = true;
        requestLayout();
        invalidate();
    }

    private void initView(Context context, AttributeSet attrs) {
        initAttributes(context, attrs);
        int xPadding = shadowWidth + Math.abs(offsetX);
        int yPadding = shadowWidth + Math.abs(offsetY);
        setPadding(xPadding, yPadding, xPadding, yPadding);
    }

    private void setBackgroundCompat(int w, int h) {
        Bitmap bitmap = createShadowBitmap(w, h, backgroundRound, shadowWidth, offsetX, offsetY, shadowColor, backgroundColor);
        BitmapDrawable drawable = new BitmapDrawable(getResources(), bitmap);
        ViewCompat.setBackground(this, drawable);
    }

    public void setShadowColor(int shadowColor) {
        this.shadowColor = shadowColor;
    }

    public void setShadowWidth(int shadowWidth) {
        this.shadowWidth = shadowWidth;
    }

    private void initAttributes(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShadowLayoutCompat);
        shadowWidth = typedArray.getDimensionPixelSize(R.styleable.ShadowLayoutCompat_shadowRadius, 0);
        backgroundRound = typedArray.getDimensionPixelSize(R.styleable.ShadowLayoutCompat_shadowCorners, 0);
        shadowColor = typedArray.getColor(R.styleable.ShadowLayoutCompat_shadowColor, Color.parseColor("#ffffff"));
        backgroundColor = typedArray.getColor(R.styleable.ShadowLayoutCompat_shadowBackground, Color.parseColor("#ffffff"));
        offsetX = typedArray.getDimensionPixelSize(R.styleable.ShadowLayoutCompat_shadowDx, 0);
        offsetY = typedArray.getDimensionPixelSize(R.styleable.ShadowLayoutCompat_shadowDy, 0);
        typedArray.recycle();
    }

    private Bitmap createShadowBitmap(int shadowWidth, int shadowHeight, float cornerRadius, float shadowRadius, float dx, float dy, int shadowColor, int fillColor) {
        Bitmap output = Bitmap.createBitmap(shadowWidth, shadowHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        RectF shadowRect = new RectF(shadowRadius, shadowRadius, shadowWidth - shadowRadius, shadowHeight - shadowRadius);
        if (dy > 0) {
            shadowRect.top += dy;
            shadowRect.bottom -= dy;
        } else if (dy < 0) {
            shadowRect.top += Math.abs(dy);
            shadowRect.bottom -= Math.abs(dy);
        }
        if (dx > 0) {
            shadowRect.left += dx;
            shadowRect.right -= dx;
        } else if (dx < 0) {
            shadowRect.left += Math.abs(dx);
            shadowRect.right -= Math.abs(dx);
        }
        Paint shadowPaint = new Paint();
        shadowPaint.setAntiAlias(true);
        shadowPaint.setColor(fillColor);
        shadowPaint.setStyle(Paint.Style.FILL);
        if (!isInEditMode()) {
            shadowPaint.setShadowLayer(shadowRadius, dx, dy, shadowColor);
        }
        canvas.drawRoundRect(shadowRect, cornerRadius, cornerRadius, shadowPaint);
        return output;
    }

    //    public static final int ALL = 0x1111;
//
//    public static final int LEFT = 0x0001;
//
//    public static final int TOP = 0x0010;
//
//    public static final int RIGHT = 0x0100;
//
//    public static final int BOTTOM = 0x1000;
//
    public static final int SHAPE_RECTANGLE = 0x0001;

    public static final int SHAPE_OVAL = 0x0010;
//
//
//    /*背景色*/
//    private int mShadowBackground = Color.TRANSPARENT;
//    /**
//     * 阴影的颜色
//     */
//    private int mShadowColor = Color.TRANSPARENT;
//
//    /**
//     * 阴影的大小范围
//     */
//    private float mShadowRadius = 0;
//
//    /**
//     * 阴影 x 轴的偏移量
//     */
//    private float mShadowDx = 0;
//
//    /**
//     * 阴影 y 轴的偏移量
//     */
//    private float mShadowDy = 0;
//
//    /**
//     * 阴影显示的边界
//     */
//    private int mShadowSide = ALL;
//
//    /**
//     * 阴影的形状，圆形/矩形
//     */
//    private int mShadowShape = SHAPE_RECTANGLE;
//    /**
//     * 阴影的形状，圆形/矩形
//     */
//    private float cornerRadius = 0;
//    private RectF mRectF = new RectF();
//    private boolean mForceInvalidateShadow = false;
//
//    public ShadowLayoutCompat(Context context) {
//        this(context, null);
//    }
//
//    public ShadowLayoutCompat(Context context, AttributeSet attrs) {
//        this(context, attrs, 0);
//    }
//
//    public ShadowLayoutCompat(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        initContent(attrs);
//    }
//
//    /**
//     * 读取设置的阴影的属性
//     *
//     * @param attrs 从其中获取设置的值
//     */
//    private void initContent(AttributeSet attrs) {
//        setLayerType(View.LAYER_TYPE_SOFTWARE, null);  // 关闭硬件加速
//        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ShadowLayoutCompat);
//        if (typedArray != null) {
//            mShadowBackground = typedArray.getColor(R.styleable.ShadowLayoutCompat_shadowBackground, Color.TRANSPARENT);
//            mShadowColor = typedArray.getColor(R.styleable.ShadowLayoutCompat_shadowColor, Color.parseColor("#2a000000"));
//            mShadowRadius = typedArray.getDimension(R.styleable.ShadowLayoutCompat_shadowRadius, 0);
//            mShadowDx = typedArray.getDimension(R.styleable.ShadowLayoutCompat_shadowDx, 0);
//            mShadowDy = typedArray.getDimension(R.styleable.ShadowLayoutCompat_shadowDy, 0);
//            mShadowSide = typedArray.getInt(R.styleable.ShadowLayoutCompat_shadowSide, ALL);
//            mShadowShape = typedArray.getInt(R.styleable.ShadowLayoutCompat_shadowShape, SHAPE_RECTANGLE);
//            cornerRadius = typedArray.getDimension(R.styleable.ShadowLayoutCompat_shadowCorners, 0);
//            typedArray.recycle();
//        }
//        int xPadding = (int) (mShadowRadius + Math.abs(mShadowDx));
//        int yPadding = (int) (mShadowRadius + Math.abs(mShadowDy));
//        setPadding(xPadding, yPadding, xPadding, yPadding);
//    }
//
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        float effect = mShadowRadius;
//        float rectLeft = 0;
//        float rectTop = 0;
//        float rectRight = this.getMeasuredWidth();
//        float rectBottom = this.getMeasuredHeight();
//        int paddingLeft = 0;
//        int paddingTop = 0;
//        int paddingRight = 0;
//        int paddingBottom = 0;
//        this.getWidth();
//        if ((mShadowSide & LEFT) == LEFT) {
//            rectLeft = effect;
//            paddingLeft = (int) effect;
//        }
//        if ((mShadowSide & TOP) == TOP) {
//            rectTop = effect;
//            paddingTop = (int) effect;
//        }
//        if ((mShadowSide & RIGHT) == RIGHT) {
//            rectRight = this.getMeasuredWidth() - effect;
//            paddingRight = (int) effect;
//        }
//        if ((mShadowSide & BOTTOM) == BOTTOM) {
//            rectBottom = this.getMeasuredHeight() - effect;
//            paddingBottom = (int) effect;
//        }
//        if (mShadowDy < 0.0f || mShadowDy > 0.0f) {
//            rectBottom = rectBottom - mShadowDy;
//            paddingBottom = paddingBottom + (int) mShadowDy;
//        }
//        if (mShadowDx < 0.0f || mShadowDx > 0.0f) {
//            rectRight = rectRight - mShadowDx;
//            paddingRight = paddingRight + (int) mShadowDx;
//        }
//        mRectF.left = rectLeft;
//        mRectF.top = rectTop;
//        mRectF.right = rectRight;
//        mRectF.bottom = rectBottom;
//        this.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//    }
//
//    @Override
//    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        super.onSizeChanged(w, h, oldw, oldh);
//        if (w > 0 && h > 0 && (getBackground() == null || mForceInvalidateShadow)) {
//            mForceInvalidateShadow = false;
//            setShadowDrawable();
//        }
//    }
//
//    @Override
//    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        super.onLayout(changed, left, top, right, bottom);
//        if (mForceInvalidateShadow) {
//            mForceInvalidateShadow = false;
//            setShadowDrawable();
//        }
//    }
//
//    private void setShadowDrawable() {
//        ShadowDrawable mShadowDrawable = new ShadowDrawable(mShadowShape, mShadowColor, mShadowRadius, mShadowDx, mShadowDy,
//                cornerRadius, mShadowBackground, mRectF);
//        ViewCompat.setBackground(this, mShadowDrawable);
//    }
}
