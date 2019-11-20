package com.holike.crm.fragment.employee.child;

import androidx.annotation.NonNull;

import java.util.Date;

/**
 * Created by pony on 2019/10/24.
 * Copyright holike possess 2019.
 * 成交排行-营销人员
 */
public class InternalTransactionFragment extends CommonInternalFragment<InternalTransactionHelper> implements CommonInternalHelper.Callback {
    @NonNull
    @Override
    protected InternalTransactionHelper newHelper() {
        return new InternalTransactionHelper(this, this);
    }

    @Override
    public void doRequest(String type, String cityCode, Date startDate, Date endDate) {
        showLoading();
        mPresenter.getEmployeeRanking(startDate, endDate, cityCode, type, "2");
    }
}
