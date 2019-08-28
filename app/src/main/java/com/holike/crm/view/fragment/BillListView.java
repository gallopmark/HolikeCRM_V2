package com.holike.crm.view.fragment;

import com.holike.crm.base.BaseView;
import com.holike.crm.bean.BillListBean;

import java.util.List;

public interface BillListView extends BaseView {
    void success(BillListBean bean);

    void fail(String errorMsg);

    void refresh(boolean b);

    void refreshSuccess(List<BillListBean.PageDataBean> listBeans);

    void loadmore();

    void loadmoreSuccess(List<BillListBean.PageDataBean> listBeans);

    void loadAllSuccess();

    void onItemClick(BillListBean.PageDataBean pageDataBean);
    void onItemLongClick(String orderId);
}
