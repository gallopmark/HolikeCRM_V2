package com.holike.crm.fragment.reportv3.business;

import androidx.annotation.NonNull;

import com.holike.crm.R;

/**
 * Created by gallop on 2019/10/22.
 * Copyright holike possess 2019.
 * 宅配业绩报表
 */
public class HomeDeliveryFragment extends BusinessReferenceFragment<HomeDeliveryHelper> {

    @NonNull
    @Override
    protected HomeDeliveryHelper newHelper() {
        return new HomeDeliveryHelper(this);
    }

    @Override
    void setTitle() {
        setTitle(R.string.title_home_delivery_performance);
    }
}
