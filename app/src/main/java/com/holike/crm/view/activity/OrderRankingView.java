package com.holike.crm.view.activity;

import com.holike.crm.base.BaseView;
import com.holike.crm.bean.OrderRankingBean;

/**
 * Created by wqj on 2018/4/27.
 * 签单排行榜
 */

public interface OrderRankingView extends BaseView {
    void getDataSuccess(OrderRankingBean orderRankingBean);

    void getDataFailed(String failed);
}
