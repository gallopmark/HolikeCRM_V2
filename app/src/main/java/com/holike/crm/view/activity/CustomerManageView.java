package com.holike.crm.view.activity;

import androidx.annotation.Nullable;

import com.holike.crm.base.BaseView;
import com.holike.crm.bean.AttBean;
import com.holike.crm.bean.CustomerListBean;
import com.holike.crm.bean.TypeIdBean;

import java.util.List;

/**
 * Created by wqj on 2018/2/25.
 * 客户管理
 */
@Deprecated
public interface CustomerManageView extends BaseView {
    void getCustomerListSuccess(List<CustomerListBean> customerListBeans, @Nullable AttBean attBean);

    void getCustomerListFailed(String failed);

    void getTypeIdSuccess(TypeIdBean typeIdBean);

    void getTypeIdFailure(String failed);

    void getCustomerListByType(String id, String value);

    void getCustomerListBySource(String id, String value);

    void refresh(boolean showLoading);

    void loadmore();

    void refreshSuccess(List<CustomerListBean> customerListBeans);

    void loadmoreSuccess(List<CustomerListBean> customerListBeans);

    void loadAll();

    void search();

    void adapterChildItemClick(CustomerListBean bean);

    void adapterChildItemLongClick(CustomerListBean bean);

    void adapterItemClick(CustomerListBean bean);

    void adapterItemLongClick(CustomerListBean bean,int position);

    void deleteCustomerSuccess(String result, int position);

    void deleteCustomerFailure(String message);
}
