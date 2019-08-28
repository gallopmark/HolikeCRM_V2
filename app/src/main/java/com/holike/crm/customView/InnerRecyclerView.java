package com.holike.crm.customView;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class InnerRecyclerView extends RecyclerView {

    private float lastY;

    public InnerRecyclerView(Context context) {
        super(context);
    }

    public InnerRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public InnerRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // do nothing
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaY = (int) (event.getY() - lastY);
                if (deltaY > 0) { // pull down currently
                    // if view support pull down, then request parent do not intercept touch event.
                    getParent().requestDisallowInterceptTouchEvent(canScrollVertically(-1));
                } else if (deltaY < 0) { // pull up currently
                    getParent().requestDisallowInterceptTouchEvent(canScrollVertically(1));
                }
                break;
            case MotionEvent.ACTION_UP:
                // onReset state
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
            case MotionEvent.ACTION_CANCEL:
                // parent has intercepted the touch event
                break;
        }
        lastY = event.getY();
        // call super to process scrolling
        return super.onTouchEvent(event);
    }
}
