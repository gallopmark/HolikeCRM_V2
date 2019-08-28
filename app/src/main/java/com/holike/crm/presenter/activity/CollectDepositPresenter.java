package com.holike.crm.presenter.activity;

import com.holike.crm.base.BasePresenter;
import com.holike.crm.bean.CollectDepositListBean;
import com.holike.crm.bean.CustomerDetailBean;
import com.holike.crm.model.activity.CollectDepositModel;
import com.holike.crm.view.activity.CollectDepositView;

import java.util.List;

/**
 * Created by wqj on 2018/8/20.
 * 首页进入收取订金页
 */

public class CollectDepositPresenter extends BasePresenter<CollectDepositView, CollectDepositModel> {

    /**
     * 获取收取定金列表数据
     */
    public void getData(int pageIndex, String searchContent) {
        model.getData(String.valueOf(pageIndex), "10", searchContent, new CollectDepositModel.GetDataListener() {
            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().failed(failed);
            }

            @Override
            public void success(List<CollectDepositListBean> beans) {
                if (getView() != null)
                    getView().success(beans);
            }
        });
    }

    /**
     * 获取客户详情
     *
     * @param personalId
     */
    public void getCustomerDetail(String personalId) {
        model.getCustomerDetail(personalId, new CollectDepositModel.GetCustomerDetailListener() {
            @Override
            public void success(CustomerDetailBean customerDetailBean) {
                if (getView() != null)
                    getView().getCustomerSuccess(customerDetailBean);
            }

            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().getCustomerFailed(failed);
            }
        });
    }
}
