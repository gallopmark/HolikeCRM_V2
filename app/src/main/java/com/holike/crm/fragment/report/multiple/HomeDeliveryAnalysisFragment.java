package com.holike.crm.fragment.report.multiple;

import android.os.Bundle;

import androidx.annotation.NonNull;


/**
 * Created by pony on 2019/11/7.
 * Version v3.0 app报表
 * 宅配业绩分析
 */
public class HomeDeliveryAnalysisFragment extends AbsAnalysisFragment<HomeDeliveryAnalysisHelper> {

    public static HomeDeliveryAnalysisFragment newInstance(String subTitle, String dimensionOf) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("entry",true);
        bundle.putString("subTitle", subTitle);
        bundle.putString("dimensionOf", dimensionOf);
        HomeDeliveryAnalysisFragment fragment = new HomeDeliveryAnalysisFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @NonNull
    @Override
    protected HomeDeliveryAnalysisHelper newHelper() {
        return new HomeDeliveryAnalysisHelper(this, this);
    }
}
