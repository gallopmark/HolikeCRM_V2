package com.holike.crm.fragment.employee.child;

import androidx.annotation.NonNull;

import java.util.Date;

/**
 * Created by pony on 2019/10/23.
 * Copyright holike possess 2019.
 * 个人签单排行-经销商
 */
public class DealerSigningFragment extends CommonDealerFragment<DealerSigningHelper> implements CommonDealerHelper.Callback {

    @NonNull
    @Override
    protected DealerSigningHelper newHelper() {
        return new DealerSigningHelper(this, this);
    }

    @Override
    public void doRequest(Date startDate, Date endDate) {
        getData(startDate,endDate,"1");
    }
}
