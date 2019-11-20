package com.holike.crm.fragment.report;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.holike.crm.R;
import com.holike.crm.bean.SheetAnalysisBean;

import java.util.Date;

/**
 * Created by pony on 2019/11/2.
 * Version v3.0 app报表
 * 板材分析
 */
public class SheetAnalysisFragment extends GeneralReportFragment<SheetAnalysisHelper> implements SheetAnalysisHelper.Callback {

    public static SheetAnalysisFragment newInstance(String title) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("entry", true);
        bundle.putString("title", title);
        SheetAnalysisFragment fragment = new SheetAnalysisFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @NonNull
    @Override
    protected SheetAnalysisHelper newHelper() {
        return new SheetAnalysisHelper(this, this);
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_report_general;
    }

    @Override
    public void onRequest(String time, String type, String cityCode, Date startDate, Date endDate) {
        showLoading();
        mPresenter.getSheetAnalysisData(time, type, cityCode, startDate, endDate);
    }

    @Override
    public void onSuccess(Object object) {
        super.onSuccess(object);
        mHelper.onSuccess((SheetAnalysisBean) object);
    }

    @Override
    public void onFailure(String failReason) {
        super.onFailure(failReason);
        mHelper.onFailure(failReason);
    }

    @Override
    protected void reload() {
        mHelper.doRequest();
    }

    @Override
    public void onDestroyView() {
        mHelper.detach();
        super.onDestroyView();
    }

}
