package com.holike.crm.presenter.fragment;

import androidx.annotation.Nullable;

import com.holike.crm.base.BasePresenter;
import com.holike.crm.bean.MonthDataInstallManagerBean;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.model.fragment.MonthDataModel;
import com.holike.crm.util.TimeUtil;
import com.holike.crm.view.fragment.MonthDataBossInstallManagerView;

import java.util.Date;

/**
 * Created by pony on 2019/8/9.
 * Copyright holike possess 2019.
 */
public class MonthDataInstallManagerPresenter extends BasePresenter<MonthDataBossInstallManagerView, MonthDataModel> {

    public void getMonthData(String type, String cityCode, @Nullable Date startDate, @Nullable Date endDate) {
        String startTime = "";
        String endTime = "";
        if (startDate != null) {
            startTime = TimeUtil.dateToStamp(startDate, false);
        }
        if (endDate != null) {
            endTime = TimeUtil.dateToStamp(endDate, true);
        }
        if (getModel() != null) {
            getModel().getMonthDataInstallManager(type, cityCode, startTime, endTime, new RequestCallBack<MonthDataInstallManagerBean>() {
                @Override
                public void onFailed(String failReason) {
                    if (getView() != null) {
                        getView().onFailure(failReason);
                    }
                }

                @Override
                public void onSuccess(MonthDataInstallManagerBean result) {
                    if (getView() != null) {
                        getView().onSuccess(result);
                    }
                }
            });
        }
    }
}
