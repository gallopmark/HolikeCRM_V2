package com.holike.crm.view.activity;

import com.holike.crm.base.BaseView;
import com.holike.crm.bean.OrderListBean;
import com.holike.crm.bean.TypeListBean;

import java.util.List;

public interface OrderCenterV2View extends BaseView {
    void getTypeListSuccess(TypeListBean typeListBean);

    void getListSuccess(List<OrderListBean> listBeans, boolean isShow, long count, boolean isShow2, String money);

    void getListFailed(String failed);
}
