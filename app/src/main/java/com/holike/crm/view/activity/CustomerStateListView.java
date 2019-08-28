package com.holike.crm.view.activity;

import com.holike.crm.base.BaseView;
import com.holike.crm.bean.CustomerStateListBean;

import java.util.List;

/**
 * Created by wqj on 2018/8/22.
 * 客户状态列表
 */

public interface CustomerStateListView extends BaseView {
    void success(CustomerStateListBean bean);

    void failed(String failed);

    void refresh(boolean b);

    void refreshSuccess(List<CustomerStateListBean.DateBean> listBeans);

    void loadmore();

    void loadmoreSuccess(List<CustomerStateListBean.DateBean> listBeans);

    void loadAllSuccess();
}
