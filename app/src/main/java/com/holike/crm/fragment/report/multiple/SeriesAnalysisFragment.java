package com.holike.crm.fragment.report.multiple;

import android.os.Bundle;

import androidx.annotation.NonNull;


/**
 * Created by pony on 2019/11/7.
 * Version v3.0 app报表
 * 系列业绩分析
 */
public class SeriesAnalysisFragment extends AbsAnalysisFragment<SeriesAnalysisHelper> {
    public static SeriesAnalysisFragment newInstance(String subTitle, String dimensionOf) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("entry",true);
        bundle.putString("subTitle", subTitle);
        bundle.putString("dimensionOf", dimensionOf);
        SeriesAnalysisFragment fragment = new SeriesAnalysisFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @NonNull
    @Override
    protected SeriesAnalysisHelper newHelper() {
        return new SeriesAnalysisHelper(this, this);
    }
}
