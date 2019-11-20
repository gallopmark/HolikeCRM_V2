package com.holike.crm.fragment.report.multiple;

import android.os.Bundle;

import androidx.annotation.NonNull;

/**
 * Created by pony on 2019/11/7.
 * Version v3.0 app报表
 * 花色业绩分析
 */
public class ColorsAnalysisFragment extends AbsAnalysisFragment<ColorsAnalysisHelper> {

    public static ColorsAnalysisFragment newInstance(String subTitle, String dimensionOf) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("entry",true);
        bundle.putString("subTitle", subTitle);
        bundle.putString("dimensionOf", dimensionOf);
        ColorsAnalysisFragment fragment = new ColorsAnalysisFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @NonNull
    @Override
    protected ColorsAnalysisHelper newHelper() {
        return new ColorsAnalysisHelper(this, this);
    }
}
