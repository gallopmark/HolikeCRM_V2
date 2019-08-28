package com.holike.crm.customView.listener;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * 点击文字变色触摸监听
 */
public class TouchPressListener implements View.OnTouchListener {
    private Context ctx;
    private int selectColor;
    private int pressColor;
    private int releaseColor;
    private View[] tv;
    private OnPressListener clickListener;

    public TouchPressListener(Context ctx, int selectColor, int releaseColor, int pressColor, View[] tv, OnPressListener clickListener) {
        this.ctx = ctx;
        this.selectColor = selectColor;
        this.pressColor = pressColor;
        this.releaseColor = releaseColor;
        this.tv = tv;
        this.clickListener = clickListener;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                for (View view : tv) {
                    if (view instanceof TextView) {
                        ((TextView) view).setTextColor(view.isSelected() ? ctx.getResources().getColor(selectColor) : ctx.getResources().getColor(pressColor));
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
                for (View view : tv) {
                    if (view instanceof TextView) {
                        ((TextView) view).setTextColor(ctx.getResources().getColor(releaseColor));
                    }
                }
                clickListener.onClick(v);
                break;
            case MotionEvent.ACTION_CANCEL:
                for (View view : tv) {
                    if (view instanceof TextView) {
                        ((TextView) view).setTextColor(view.isSelected() ? ctx.getResources().getColor(selectColor) : ctx.getResources().getColor(releaseColor));
                    }
                }
                break;
        }
        return false;
    }

    public interface OnPressListener {
        void onClick(View v);
    }

}

