package com.holike.crm.fragment.reportv3.business;

import androidx.annotation.NonNull;

import com.holike.crm.R;

/**
 * Created by gallop on 2019/10/22.
 * Copyright holike possess 2019.
 * 花色业绩
 */
public class ColorsPerformanceFragment extends BusinessReferenceFragment<ColorsPerformanceHelper>{

    @NonNull
    @Override
    protected ColorsPerformanceHelper newHelper() {
        return new ColorsPerformanceHelper(this);
    }

    @Override
    void setTitle() {
        setTitle(R.string.title_color_performance);
    }

}
