package com.holike.crm.view.activity;

import android.view.View;

import com.holike.crm.base.BaseView;
import com.holike.crm.bean.OrderListBean;
import com.holike.crm.bean.TypeListBean;

import java.util.List;

/**
 * Created by wqj on 2018/2/25.
 * 订单中心
 */

public interface OrderCenterView extends BaseView {
    void getTypeListSuccess(TypeListBean typeListBean);

    void getListSuccess(List<OrderListBean> listBeans, boolean isShow, long count, boolean isShow2, String money);

    void getListFailed(String failed);

    void getListByOrderType(String orderTypeId);

    void getListByOrderState(String orderStatusId);

    void refresh(boolean showLoading);

    void loadmore();

    void refreshSuccess(List<OrderListBean> listBeans);

    void loadmoreSuccess(List<OrderListBean> listBeans);

    void loadAll();

    void orderDetails(String orderId);

    void search();

    void adapterItemChildClick(OrderListBean bean);

    void adapterItemChildLongClick(View view, OrderListBean bean);
}
