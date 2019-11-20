package com.holike.crm.view.activity;

import androidx.annotation.Nullable;

import com.holike.crm.base.BaseView;
import com.holike.crm.bean.EmployeeEditResultBean;

/**
 * Created by pony on 2019/8/6.
 * Copyright holike possess 2019.
 */
public interface EmployeeEditV2View extends BaseView {
    void onSaveSuccess(@Nullable EmployeeEditResultBean resultBean);

    void onSaveFailure(String failReason);
}
