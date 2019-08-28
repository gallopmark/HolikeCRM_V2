package com.holike.crm.presenter.fragment;

import com.holike.crm.base.BasePresenter;
import com.holike.crm.bean.ActiveMarketRankBean;
import com.holike.crm.model.fragment.ActiveMarketRankModel;
import com.holike.crm.view.fragment.ActiveMarketRankView;

/**
 * Created by wqj on 2018/6/21.
 * 主动营销排行榜
 */

public class ActiveMarketRankPresenter extends BasePresenter<ActiveMarketRankView, ActiveMarketRankModel> {

    /**
     * 获取数据
     */
    public void getData(String start, String end) {
        model.getData(start == null ? "" : String.valueOf(Long.parseLong(start) ), end == null ? "" : String.valueOf(Long.parseLong(end) ), new ActiveMarketRankModel.GetDataListener() {
            @Override
            public void success(ActiveMarketRankBean bean) {
                if (getView() != null)
                    getView().getDataSuccess(bean);
            }

            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().getDataFailed(failed);
            }
        });
    }
}
