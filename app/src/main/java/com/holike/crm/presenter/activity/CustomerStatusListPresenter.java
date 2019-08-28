package com.holike.crm.presenter.activity;

import android.text.TextUtils;

import com.holike.crm.base.BasePresenter;
import com.holike.crm.bean.CustomerStatusBean;
import com.holike.crm.enumeration.CustomerValue;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.model.customer.GeneralCustomerModel;
import com.holike.crm.view.activity.CustomerStatusListView;

/**
 * Created by gallop 2019/7/8
 * Copyright (c) 2019 holike
 */
public class CustomerStatusListPresenter extends BasePresenter<CustomerStatusListView, GeneralCustomerModel> {

    /**
     * @param statusName      01 潜在客户 02待量尺 3待出图 14待查房 04待签约 09待收全款 11订单客户 06待安装客户 07已安装客户08公海客户
     * @param earnestStatus   1有收取定金 0 无收取定金
     * @param intentionStatus 客户意向
     * @param customerStatus  客户状态 多个用@隔开
     * @param orderBy         desc 升序 asc 降序
     * @param tailStatus      尾款情况
     * @param seaStatus 公海分类
     * @param pageNo          页码
     * @param pageSize        页面大小
     */
    public void getCustomerStatusList(String statusName, String earnestStatus, String intentionStatus,
                                      String customerStatus, String tailStatus, String seaStatus,
                                      String orderBy, int pageNo, int pageSize) {
        String statusMove = "";
        if (TextUtils.equals(statusName, CustomerValue.POTENTIAL)) { //潜在客户
            statusMove = "01";
        } else if (TextUtils.equals(statusName, CustomerValue.STAY_MEASURE)) { //待量尺客户
            statusMove = "02";
        } else if (TextUtils.equals(statusName, CustomerValue.STAY_DRAWING)) { //待出图客户
            statusMove = "03";
        } else if (TextUtils.equals(statusName, CustomerValue.STAY_ROUNDS)) { //待查房客户
            statusMove = "14";
        } else if (TextUtils.equals(statusName, CustomerValue.STAY_SIGN)) { //待签约客户
            statusMove = "04";
        } else if (TextUtils.equals(statusName, CustomerValue.STAY_COLLECT_AMOUNT)) { //待收全款客户
            statusMove = "09";
        } else if (TextUtils.equals(statusName, CustomerValue.ORDER)) {//订单客户
            statusMove = "11";
        } else if(TextUtils.equals(statusName,CustomerValue.ORDER_PLACED)){  //已下单客户
            statusMove = "12";
        }else if (TextUtils.equals(statusName, CustomerValue.STAY_INSTALL)) { //待安装客户
            statusMove = "06";
        } else if (TextUtils.equals(statusName, CustomerValue.INSTALLED)) { //已安装客户
            statusMove = "07";
        } else if (TextUtils.equals(statusName, CustomerValue.HIGH_SEAS)) { //公海客户
            statusMove = "08";
        }
        if (getModel() != null) {
            getModel().getCustomerStatusList(statusMove, earnestStatus, intentionStatus, customerStatus, tailStatus, seaStatus,
                    orderBy, pageNo, pageSize, new RequestCallBack<CustomerStatusBean>() {
                        @Override
                        public void onFailed(String failReason) {
                            if (getView() != null) {
                                getView().onFailed(failReason);
                            }
                        }

                        @Override
                        public void onSuccess(CustomerStatusBean bean) {
                            if (getView() != null) {
                                getView().onSuccess(bean);
                            }
                        }
                    });
        }
    }
}
