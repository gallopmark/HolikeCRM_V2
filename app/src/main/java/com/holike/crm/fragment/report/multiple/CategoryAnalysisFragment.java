package com.holike.crm.fragment.report.multiple;

import android.os.Bundle;

import androidx.annotation.NonNull;

/**
 * Created by pony on 2019/11/7.
 * Version v3.0 app报表
 * 品类业绩分析
 */
public class CategoryAnalysisFragment extends AbsAnalysisFragment<CategoryAnalysisHelper> implements AbsAnalysisHelper.Callback {

    public static CategoryAnalysisFragment newInstance(String subTitle, String dimensionOf) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("entry", true);
        bundle.putString("subTitle", subTitle);
        bundle.putString("dimensionOf", dimensionOf);
        CategoryAnalysisFragment fragment = new CategoryAnalysisFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @NonNull
    @Override
    protected CategoryAnalysisHelper newHelper() {
        return new CategoryAnalysisHelper(this, this);
    }
}
