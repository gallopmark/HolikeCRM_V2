package com.holike.crm.fragment.reportv3.business;

import androidx.annotation.NonNull;

import com.holike.crm.R;

/**
 * Created by gallop on 2019/10/22.
 * Copyright holike possess 2019.
 * 系列业绩
 */
public class SeriesPerformanceFragment extends BusinessReferenceFragment<SeriesPerformanceHelper> {

    @NonNull
    @Override
    protected SeriesPerformanceHelper newHelper() {
        return new SeriesPerformanceHelper(this);
    }

    @Override
    void setTitle() {
        setTitle(R.string.title_series_performance);
    }
}
