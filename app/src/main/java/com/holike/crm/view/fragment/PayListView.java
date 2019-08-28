package com.holike.crm.view.fragment;

import com.holike.crm.base.BaseView;
import com.holike.crm.bean.HomepageBean;
import com.holike.crm.bean.PayListBean;

import java.util.List;

public interface PayListView extends BaseView {
    void onSuccess(List<PayListBean> bean);

    void onFail(String errorMsg);


    void refresh(boolean b);

    void refreshSuccess(List<PayListBean> listBeans);

    void loadmore();

    void loadmoreSuccess(List<PayListBean> listBeans);

    void loadAllSuccess();

    void onPopupWindowShowing();

    void onFilterItemSelect(HomepageBean.TypeListBean.BrankDataBean barkId);

    void onPopupWindowDismiss();

    void onItemClick(PayListBean bean);
}
