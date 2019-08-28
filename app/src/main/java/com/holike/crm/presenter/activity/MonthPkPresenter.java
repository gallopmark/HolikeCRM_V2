package com.holike.crm.presenter.activity;

import com.holike.crm.base.BasePresenter;
import com.holike.crm.bean.MonthPkBean;
import com.holike.crm.model.activity.MonthPkModel;
import com.holike.crm.view.activity.MonthPkView;

/**
 * Created by wqj on 2018/6/7.
 * 月度PK
 */

public class MonthPkPresenter extends BasePresenter<MonthPkView, MonthPkModel> {
    /**
     * 获取月度PK数据
     *
     * @param group
     */
    public void getData(String group) {
        model.getData(group == null ? "1" : group, new MonthPkModel.GetDataListener() {
            @Override
            public void success(MonthPkBean bean) {
                if (bean.getIsProvince() == 1) {
                    if (getView() != null)
                        getView().openMonthPkPersonal(bean);
                } else {
                    if (getView() != null)
                        getView().openMonthPk(bean);
                }
            }

            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().getDatafailed(failed);
            }
        });
    }
}
