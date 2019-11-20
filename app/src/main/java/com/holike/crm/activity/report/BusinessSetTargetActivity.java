package com.holike.crm.activity.report;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.holike.crm.R;
import com.holike.crm.bean.BusinessTargetBean;
import com.holike.crm.presenter.GeneralReportPresenter;
import com.holike.crm.view.GeneralReportView;

/**
 * Created by pony on 2019/10/28.
 * Version v3.0 app报表
 * 设置目标
 */
public class BusinessSetTargetActivity extends GeneralReportActivity<BusinessSetTargetHelper>{

    @Override
    protected GeneralReportPresenter attachPresenter() {
        return new GeneralReportPresenter();
    }

    @Override
    protected int setContentViewId() {
        return R.layout.activity_business_settarget;
    }

    @NonNull
    @Override
    protected BusinessSetTargetHelper newHelper() {
        return new BusinessSetTargetHelper(this);
    }

    @Override
    protected void setup(Bundle savedInstanceState) {
        setTitle(getString(R.string.title_set_target));
        initData();
    }

    private void initData(){
        showLoading();
        mPresenter.getBusinessTarget();
    }

    @Override
    public void onSuccess(Object obj) {
        super.onSuccess(obj);
        mActivityHelper.onSuccess((BusinessTargetBean) obj);
    }

    @Override
    public void onFailure(String failReason) {
        super.onFailure(failReason);
        mActivityHelper.onFailure(failReason);
    }

    @Override
    public void reload() {
        initData();
    }
}
