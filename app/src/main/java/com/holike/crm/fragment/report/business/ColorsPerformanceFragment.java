package com.holike.crm.fragment.report.business;

import androidx.annotation.NonNull;

import com.holike.crm.R;

/**
 * Created by pony on 2019/10/22.
 * Copyright holike possess 2019.
 * 花色业绩-经销商
 */
public class ColorsPerformanceFragment extends DealerMultiPerformanceFragment<ColorsPerformanceHelper> {

    @NonNull
    @Override
    protected ColorsPerformanceHelper newHelper() {
        return new ColorsPerformanceHelper(this,this);
    }

    @Override
    void setTitle() {
        setTitle(R.string.title_color_performance);
    }

}
