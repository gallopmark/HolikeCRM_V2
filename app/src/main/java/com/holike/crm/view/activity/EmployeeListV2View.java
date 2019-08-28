package com.holike.crm.view.activity;

import com.holike.crm.base.BaseView;
import com.holike.crm.bean.EmployeeBeanV2;

import java.util.List;

/**
 * Created by gallop on 2019/8/6.
 * Copyright holike possess 2019.
 */
public interface EmployeeListV2View extends BaseView {

    void onSuccess(List<EmployeeBeanV2> list);

    void onFailure(String failReason);
}
