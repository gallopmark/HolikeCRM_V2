package com.holike.crm.customView;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.holike.crm.R;


/**
 * Created by wqj on 2017/10/30.
 * 首页底部导航栏
 */
@Deprecated
public class MainTableLayout extends LinearLayout {
    private int[] iconNor, iconSelect;
    private int currentPosition;
    private int norColor;
    private int selectColor;
    private TabSelectListener listener;

    public MainTableLayout(Context context) {
        super(context);
    }

    public MainTableLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MainTableLayout);
        norColor = a.getColor(R.styleable.MainTableLayout_mtlNorColor, ContextCompat.getColor(context, R.color.textColor6));
        selectColor = a.getColor(R.styleable.MainTableLayout_mtlSelectColor, ContextCompat.getColor(context, R.color.textColor5));
        a.recycle();
    }

    public MainTableLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initView(int[] iconNor, int[] iconSelect, String[] titles, final TabSelectListener listener) {
        this.iconNor = iconNor;
        this.iconSelect = iconSelect;
        this.listener = listener;
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER);
        for (int i = 0, length = this.iconNor.length; i < length; i++) {
            LinearLayout view = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.item_tablayout, this, false);
            view.setGravity(Gravity.CENTER);
            view.setLayoutParams(new LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f));
            ImageView iv = view.findViewById(R.id.iv_item_tablayout);
            TextView tv = view.findViewById(R.id.tv_item_tablayout);
            iv.setImageResource(this.iconNor[i]);
            tv.setText(titles[i]);
            addView(view);
            final int position = i;
            view.setOnClickListener(v -> selectTable(position));
        }
        selectTable(0);//默认选中第一个
    }

    public void selectTable(final int selectPosition) {
        final ViewGroup currentView = (ViewGroup) getChildAt(currentPosition);
        final ViewGroup selectView = (ViewGroup) getChildAt(selectPosition);
        final ImageView ivCurrent = (ImageView) currentView.getChildAt(0);
        final TextView tvCurrent = (TextView) currentView.getChildAt(1);
        final ImageView ivSelect = (ImageView) selectView.getChildAt(0);
        final TextView tvSelect = (TextView) selectView.getChildAt(1);
        ivCurrent.setImageResource(iconNor[currentPosition]);
        tvCurrent.setTextColor(norColor);
        ivSelect.setImageResource(iconSelect[selectPosition]);
        tvSelect.setTextColor(selectColor);
        ValueAnimator animator = ValueAnimator.ofFloat(0.2f, 1);
        animator.setDuration(1000).start();
        animator.addUpdateListener(animation -> {
            float alpha = (Float) animation.getAnimatedValue();
            selectView.setAlpha(alpha);
        });
        listener.onTabSelect(selectPosition);
        currentPosition = selectPosition;
    }

    public interface TabSelectListener {
        void onTabSelect(int position);
    }
}
