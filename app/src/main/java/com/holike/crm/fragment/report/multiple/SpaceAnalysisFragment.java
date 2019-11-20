package com.holike.crm.fragment.report.multiple;

import android.os.Bundle;

import androidx.annotation.NonNull;

/**
 * Created by pony on 2019/11/7.
 * Version v3.0 app报表
 * 空间业绩分析
 */
public class SpaceAnalysisFragment extends AbsAnalysisFragment<SpaceAnalysisHelper> {

    public static SpaceAnalysisFragment newInstance(String subTitle, String dimensionOf) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("entry", true);
        bundle.putString("subTitle", subTitle);
        bundle.putString("dimensionOf", dimensionOf);
        SpaceAnalysisFragment fragment = new SpaceAnalysisFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @NonNull
    @Override
    protected SpaceAnalysisHelper newHelper() {
        return new SpaceAnalysisHelper(this, this);
    }
}
