package com.holike.crm.fragment.report.multiple;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.holike.crm.R;
import com.holike.crm.util.ParseUtils;

/**
 * Created by pony on 2019/11/4.
 * Version v3.0 app报表
 * 完成率及同比-业绩、橱柜、定制、木门、成品、大家居报表格式及字段均相同
 */
public class MultiCompleteRateFragment extends AbsAnalysisFragment<MultiCompleteRateHelper> {

    public static MultiCompleteRateFragment newInstance(String subTitle, String dimensionOf) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("entry",true);
        bundle.putString("subTitle", subTitle);
        bundle.putString("dimensionOf", dimensionOf);
        MultiCompleteRateFragment fragment = new MultiCompleteRateFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @NonNull
    @Override
    protected MultiCompleteRateHelper newHelper() {
        return new MultiCompleteRateHelper(this, this);
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_report_general;
    }
}
