package com.holike.crm.customView;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;

import com.holike.crm.R;
import com.holike.crm.util.DensityUtil;

/**
 * 列表底部视图
 */
public class FooterTextView extends AppCompatTextView {
    public FooterTextView(Context context) {
        super(context);
        init();
    }

    public FooterTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FooterTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dp2px(28));
        setLayoutParams(layoutParams);
        setText(getContext().getString(R.string.list_no_more));
        setTextColor(getResources().getColor(R.color.textColor11));
        setGravity(Gravity.CENTER);
    }


}
