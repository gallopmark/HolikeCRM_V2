package com.holike.crm.fragment.report.multiple;

import android.os.Bundle;

import androidx.annotation.NonNull;



/**
 * Created by pony on 2019/11/7.
 * Version v3.0 app报表
 * 渠道业绩分析
 */
public class ChannelAnalysisFragment extends AbsAnalysisFragment<ChannelAnalysisHelper> {

    public static ChannelAnalysisFragment newInstance(String subTitle, String dimensionOf) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("entry",true);
        bundle.putString("subTitle", subTitle);
        bundle.putString("dimensionOf", dimensionOf);
        ChannelAnalysisFragment fragment = new ChannelAnalysisFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @NonNull
    @Override
    protected ChannelAnalysisHelper newHelper() {
        return new ChannelAnalysisHelper(this, this);
    }
}
