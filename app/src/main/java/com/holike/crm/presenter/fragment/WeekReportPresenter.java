package com.holike.crm.presenter.fragment;

import com.holike.crm.base.BasePresenter;
import com.holike.crm.bean.DayDepositBean;
import com.holike.crm.bean.WeekDepositBean;
import com.holike.crm.model.fragment.WeekReportModel;
import com.holike.crm.view.fragment.WeekReportView;

import java.util.List;

/**
 * Created by wqj on 2018/2/25.
 * 订单交易对趋势
 */

public class WeekReportPresenter extends BasePresenter<WeekReportView, WeekReportModel> {

    /**
     * 查询周/月订金
     *
     * @param type
     */
    public void getDepositList(String type) {
        model.getDepositList(type, new WeekReportModel.GetDepositListListener() {
            @Override
            public void success(WeekDepositBean weekDepositBean) {
                if (getView() != null)
                    getView().getDepositListSuccess(weekDepositBean);
            }

            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().getDepositListFailed(failed);
            }
        });
    }

    /**
     * 查询当天订金
     *
     * @param time
     * @param money
     */
    public void getDepositInfo(final String time, String type, final String money) {
        model.getDepositInfo(time, type, new WeekReportModel.GetDepositInfoListener() {
            @Override
            public void success(List<DayDepositBean> dayDepositBeans) {
                if (getView() != null)
                    getView().getDepositInfoSuccess(dayDepositBeans, time, money);
            }

            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().getDepositInfoFailed(failed);
            }
        });
    }
}
