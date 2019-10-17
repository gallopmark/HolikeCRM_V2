package com.holike.crm.dialog;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.holike.crm.R;
import com.holike.crm.bean.ErrorInfoBean;

/**
 * Created by gallop on 2019/10/14.
 * Copyright holike possess 2019.
 * 线上引流客户激活弹窗
 */
public class RepeatDigitalDialog extends DistributionDialog {

    public RepeatDigitalDialog(Context context, @Nullable ErrorInfoBean bean) {
        super(context);
        if (bean != null) {
            ((TextView) mContentView.findViewById(R.id.tv_returnTime)).setText(bean.getReturnTime());
            ((TextView) mContentView.findViewById(R.id.tv_cause)).setText(bean.efficientCause);
        }
    }

    @Override
    protected int bindContentView() {
        return R.layout.dialog_repeat_digital;
    }
}
