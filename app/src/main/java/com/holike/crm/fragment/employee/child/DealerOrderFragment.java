package com.holike.crm.fragment.employee.child;

import androidx.annotation.NonNull;

import java.util.Date;

/**
 * Created by pony on 2019/10/23.
 * Copyright holike possess 2019.
 * 下单排行-经销商
 */
public class DealerOrderFragment extends CommonDealerFragment<DealerOrderHelper> implements CommonDealerHelper.Callback {

    @NonNull
    @Override
    protected DealerOrderHelper newHelper() {
        return new DealerOrderHelper(this, this);
    }

    @Override
    public void doRequest(Date startDate, Date endDate) {
        getData(startDate,endDate,"4");
    }
}
