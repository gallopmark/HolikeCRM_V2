package com.holike.crm.fragment.employee.child;

import androidx.annotation.NonNull;

import java.util.Date;

/**
 * Created by pony on 2019/10/23.
 * Copyright holike possess 2019.
 * 回款排行-经销商
 */
public class DealerPaybackFragment extends CommonDealerFragment<DealerPaybackHelper> implements CommonDealerHelper.Callback {

    @NonNull
    @Override
    protected DealerPaybackHelper newHelper() {
        return new DealerPaybackHelper(this,this);
    }

    @Override
    public void doRequest(Date startDate, Date endDate) {
      getData(startDate,endDate,"3");
    }
}
