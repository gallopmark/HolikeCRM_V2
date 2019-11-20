package com.holike.crm.fragment.report;


import android.os.Bundle;

import androidx.annotation.NonNull;

import com.holike.crm.R;
import com.holike.crm.bean.ShopAnalysisBean;

import java.util.Date;

/**
 * Created by pony on 2019/11/4.
 * Version v3.0 app报表
 * 门店分析
 */
public class ShopAnalysisFragment extends GeneralReportFragment<ShopAnalysisHelper> implements ShopAnalysisHelper.Callback {

    public static ShopAnalysisFragment newInstance() {
        ShopAnalysisFragment fragment = new ShopAnalysisFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("entry", true);
        fragment.setArguments(bundle);
        return fragment;
    }

    @NonNull
    @Override
    protected ShopAnalysisHelper newHelper() {
        return new ShopAnalysisHelper(this, this);
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_report_general;
    }

    @Override
    public void onRequest(String time, String type, String cityCode, Date startDate, Date endDate) {
        showLoading();
        mPresenter.getShopAnalysisData(time, type, cityCode, startDate, endDate);
    }

    @Override
    public void onSuccess(Object object) {
        super.onSuccess(object);
        mHelper.onSuccess((ShopAnalysisBean) object);
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
