package com.holike.crm.view.activity;

import com.holike.crm.base.BaseView;
import com.holike.crm.bean.WoodenDoorBean;

/**
 * Created by pony on 2019/8/28.
 * Copyright holike possess 2019.
 */
public interface WoodenDoorView extends BaseView {
    void onSuccess(WoodenDoorBean bean);

    void onFailure(String failReason);
}
