package com.holike.crm.fragment.employee.child;


import androidx.annotation.NonNull;

import java.util.Date;

/**
 * Created by pony on 2019/10/24.
 * Copyright holike possess 2019.
 * 个人签单排行-营销人员
 */
public class InternalSigningFragment extends CommonInternalFragment<InternalSigningHelper> implements CommonInternalHelper.Callback {

    @NonNull
    @Override
    protected InternalSigningHelper newHelper() {
        return new InternalSigningHelper(this, this);
    }


    @Override
    public void doRequest(String type, String cityCode, Date startDate, Date endDate) {
        showLoading();
        mPresenter.getEmployeeRanking(startDate, endDate, cityCode, type, "1");
    }
}
