package com.holike.crm.activity.report;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.holike.crm.R;
import com.holike.crm.bean.BusinessObjectivesBean;


/**
 * Created by pony on 2019/10/28.
 * Version v3.0 app报表
 * 经营目标
 */
public class BusinessTargetActivity extends GeneralReportActivity<BusinessTargetHelper> {

    @NonNull
    @Override
    protected BusinessTargetHelper newHelper() {
        return new BusinessTargetHelper(this);
    }

    @Override
    protected int setContentViewId() {
        return R.layout.activity_business_objectives;
    }

    @Override
    protected void setup(Bundle savedInstanceState) {
        setTitle(R.string.business_target);
        initData();
    }

    private void initData(){
        showLoading();
        mPresenter.getBusinessObjectives("");
    }

    @Override
    public void onSuccess(Object obj) {
        super.onSuccess(obj);
        mActivityHelper.onSuccess((BusinessObjectivesBean) obj);
    }

    @Override
    protected void clickRightMenu(CharSequence menuText, View actionView) {
        startActivity(BusinessSetTargetActivity.class);
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
