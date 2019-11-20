package com.holike.crm.fragment.report.business;

import androidx.annotation.NonNull;

import com.holike.crm.R;

/**
 * Created by pony on 2019/10/22.
 * Copyright holike possess 2019.
 * 系列业绩-经销商
 */
public class SeriesPerformanceFragment extends DealerMultiPerformanceFragment<SeriesPerformanceHelper> {

    @NonNull
    @Override
    protected SeriesPerformanceHelper newHelper() {
        return new SeriesPerformanceHelper(this, this);
    }

    @Override
    void setTitle() {
        setTitle(R.string.title_series_performance);
    }
}
