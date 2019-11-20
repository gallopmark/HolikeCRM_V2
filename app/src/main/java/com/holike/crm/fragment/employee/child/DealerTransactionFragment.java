package com.holike.crm.fragment.employee.child;

import androidx.annotation.NonNull;

import java.util.Date;

/**
 * Created by pony on 2019/10/23.
 * Copyright holike possess 2019.
 * 成交排行-经销商
 */
public class DealerTransactionFragment extends CommonDealerFragment<DealerTransactionHelper> implements CommonDealerHelper.Callback {

    @NonNull
    @Override
    protected DealerTransactionHelper newHelper() {
        return new DealerTransactionHelper(this, this);
    }

    @Override
    public void doRequest(Date startDate, Date endDate) {
        getData(startDate, endDate, "2");
    }
}
