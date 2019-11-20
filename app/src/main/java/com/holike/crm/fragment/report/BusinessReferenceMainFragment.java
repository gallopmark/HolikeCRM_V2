package com.holike.crm.fragment.report;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.holike.crm.R;
import com.holike.crm.bean.BusinessReferenceMainBean;

/**
 * Created by pony on 2019/11/4.
 * Version v3.0 app报表
 * 生意内参首页
 */
public class BusinessReferenceMainFragment extends GeneralReportFragment<BusinessReferenceMainHelper> {

    public static BusinessReferenceMainFragment newInstance(String title) {
        Bundle bundle = new Bundle();
        bundle.putString("title",title);
        BusinessReferenceMainFragment fragment = new BusinessReferenceMainFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @NonNull
    @Override
    protected BusinessReferenceMainHelper newHelper() {
        return new BusinessReferenceMainHelper(this);
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_business_reference_main;
    }

    @Override
    public void onSuccess(Object object) {
        super.onSuccess(object);
        mHelper.onSuccess((BusinessReferenceMainBean) object);
    }

    @Override
    public void onFailure(String failReason) {
        super.onFailure(failReason);
        mHelper.onFailure(failReason);
    }
}
