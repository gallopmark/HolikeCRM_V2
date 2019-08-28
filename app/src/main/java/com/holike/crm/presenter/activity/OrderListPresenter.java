package com.holike.crm.presenter.activity;

import android.text.TextUtils;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.bean.OrderListBean;
import com.holike.crm.bean.TypeListBean;
import com.holike.crm.http.MyJsonParser;
import com.holike.crm.model.activity.OrderCenterModel;
import com.holike.crm.view.activity.OrderCenterV2View;

import java.util.Date;

/**
 * Created by gallop 2019/7/5
 * Copyright (c) 2019 holike
 */
public class OrderListPresenter extends BasePresenter<OrderCenterV2View, OrderCenterModel> {


    /**
     * 获取客户选择条件数据
     */
    public void getSelectData() {
        model.getTypeList(new OrderCenterModel.GetTypeListListener() {
            @Override
            public void success(TypeListBean result) {
                if (getView() != null)
                    getView().getTypeListSuccess(result);
            }

            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().getListFailed(failed);
            }
        });
    }

    /**
     * 获取订单列表
     */
    public void getOrderList(String searchContent, String orderStatusId, String orderTypeId,
                             Date startDate, Date endDate, String pageNo, String pageSize) {
        if (TextUtils.isEmpty(searchContent)) searchContent = "";
        if (TextUtils.isEmpty(orderStatusId)) orderStatusId = "";
        if (TextUtils.isEmpty(orderTypeId)) orderTypeId = "";
        model.getOrderList(searchContent, orderStatusId, orderTypeId, startDate, endDate, pageNo, pageSize, new OrderCenterModel.GetOrderListListener() {
            @Override
            public void success(String result) {
                if (getView() != null) {
                    int isShow = MyJsonParser.getAsInt(result, "isShow");
                    long count = MyJsonParser.getAsLong(result, "count");
                    int isShow2 = MyJsonParser.getAsInt(result, "isShow2");
                    String money = MyJsonParser.getAsString(result, "money");
                    getView().getListSuccess(MyJsonParser.parseJsonToList(result, OrderListBean.class), isShow == 1, count, isShow2 == 1, money);
                }
            }

            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().getListFailed(failed);
            }
        });
    }


}
