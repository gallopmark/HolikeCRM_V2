package com.holike.crm.fragment.report;


import androidx.annotation.NonNull;

import com.holike.crm.R;
import com.holike.crm.bean.OnlineDrainageBean;

import java.util.Date;

/**
 * Created by pony on 2019/11/14.
 * Version v3.0 app报表
 * 线上引流报表-经销商、营销人员共用
 */
public class OnlineDrainageFragment extends GeneralReportFragment<OnlineDrainageHelper> implements OnlineDrainageHelper.Callback {

    @NonNull
    @Override
    protected OnlineDrainageHelper newHelper() {
        return new OnlineDrainageHelper(this, this);
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_online_drainage;
    }

    @Override
    public void onRequest(String time, String type, String cityCode, Date startDate, Date endDate) {
        showLoading();
        mPresenter.getOnlineDrainageData(time, type, cityCode, startDate, endDate);
    }

    @Override
    public void onSuccess(Object object) {
        super.onSuccess(object);
        mHelper.onSuccess((OnlineDrainageBean) object);
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
        mHelper.destroy();
        super.onDestroyView();
    }
}
