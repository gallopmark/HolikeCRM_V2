package com.holike.crm.dialog;

import android.content.Context;
import android.view.View;

import com.holike.crm.R;

/**
 * Created by pony on 2019/8/9.
 * Copyright holike possess 2019.
 * 公海客户说明
 */
public class MonthDataTipsDialog extends CommonDialog {
    public MonthDataTipsDialog(Context context) {
        super(context);
    }

    @Override
    protected int bindContentView() {
        return R.layout.dialog_monthdata_tips;
    }

    @Override
    protected void initView(View contentView) {
        contentView.findViewById(R.id.tv_know).setOnClickListener(view -> dismiss());
    }
}
