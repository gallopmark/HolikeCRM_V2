package com.holike.crm.view.fragment;

import com.holike.crm.base.BaseView;
import com.holike.crm.bean.ActiveMarketBean;

import java.util.List;

/**
 * Created by wqj on 2018/6/21.
 * 主动营销数据报表
 */

public interface ActiveMarketView extends BaseView {
    void getData();

    void getDataSuccess(ActiveMarketBean bean);

    void getDataFailed(String failed);

    void showPersonal(ActiveMarketBean bean);

    void showNational(ActiveMarketBean bean);

    void showArea(ActiveMarketBean bean);

    void showAreaList(List<ActiveMarketBean.DataListBean> list);

    void showPersonalList(List<ActiveMarketBean.DataListBean> list);
}
