package com.holike.crm.fragment.report;

import androidx.annotation.NonNull;

import com.holike.crm.R;
import com.holike.crm.bean.HomeDollBean;

/**
 * Created by pony on 2019/11/2.
 * Version v3.0 app报表
 * 家装渠道-营销人员
 */
public class HomeDollFragment extends GeneralReportFragment<HomeDollHelper> implements HomeDollHelper.Callback {

    @NonNull
    @Override
    protected HomeDollHelper newHelper() {
        return new HomeDollHelper(this, this);
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_home_doll;
    }

    @Override
    public void onRequest(String type, String cityCode) {
        showLoading();
        mPresenter.getHomeDollChannel(type, cityCode);
    }

    @Override
    public void onSuccess(Object object) {
        super.onSuccess(object);
        mHelper.onSuccess((HomeDollBean) object);
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
