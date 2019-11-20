package com.holike.crm.fragment.employee.child;

import androidx.annotation.NonNull;

import java.util.Date;

/**
 * Created by pony on 2019/10/24.
 * Copyright holike possess 2019.
 * 下单排行-营销人员
 */
public class InternalOrderFragment extends CommonInternalFragment<InternalOrderHelper> implements CommonInternalHelper.Callback {
    @NonNull
    @Override
    protected InternalOrderHelper newHelper() {
        return new InternalOrderHelper(this, this);
    }

    @Override
    public void doRequest(String type, String cityCode, Date startDate, Date endDate) {
        showLoading();
        mPresenter.getEmployeeRanking(startDate, endDate, cityCode, type, "4");
    }
}
