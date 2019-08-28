package com.holike.crm.presenter.activity;

import com.holike.crm.base.BasePresenter;
import com.holike.crm.bean.OrderRankingBean;
import com.holike.crm.model.activity.OrderRankingModel;
import com.holike.crm.view.activity.OrderRankingView;

/**
 * Created by wqj on 2018/4/27.
 * 签单排行榜
 */

public class OrderRankingPresenter extends BasePresenter<OrderRankingView, OrderRankingModel> {

    /**
     * 获取数据
     *
     * @param cityCode
     * @param type
     * @param startTime
     * @param endTime
     */
    public void getData(String cityCode, String type, String startTime, String endTime) {
        model.getData(cityCode == null ? "" : cityCode, type == null ? "" : type, "1000", "1", startTime == null ? "" : startTime, endTime == null ? "" : endTime, new OrderRankingModel.GetDataListener() {
            @Override
            public void success(OrderRankingBean orderRankingBean) {
                if (getView() != null)
                    getView().getDataSuccess(orderRankingBean);
            }

            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().getDataFailed(failed);
            }
        });
    }
}
