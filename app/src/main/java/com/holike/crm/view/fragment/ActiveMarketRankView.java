package com.holike.crm.view.fragment;

import com.holike.crm.base.BaseView;
import com.holike.crm.bean.ActiveMarketRankBean;

/**
 * Created by wqj on 2018/6/21.
 * 主动营销排行榜
 */

public interface ActiveMarketRankView extends BaseView {
    void getData();

    void getDataSuccess(ActiveMarketRankBean bean);

    void getDataFailed(String failed);
}
