package com.holike.crm.fragment.report.business;

import androidx.annotation.NonNull;

import com.holike.crm.R;

/**
 * Created by pony on 2019/10/22.
 * Copyright holike possess 2019.
 * 宅配业绩报表-经销商
 */
public class HomeDeliveryFragment extends DealerMultiPerformanceFragment<HomeDeliveryHelper> {

    @NonNull
    @Override
    protected HomeDeliveryHelper newHelper() {
        return new HomeDeliveryHelper(this, this);
    }

    @Override
    void setTitle() {
        setTitle(R.string.title_home_delivery_performance);
    }
}
