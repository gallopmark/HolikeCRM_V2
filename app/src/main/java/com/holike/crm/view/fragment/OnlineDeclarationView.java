package com.holike.crm.view.fragment;

import com.holike.crm.base.BaseView;
import com.holike.crm.bean.PayListBean;

import java.util.List;

public interface OnlineDeclarationView extends BaseView {
    void success(List<PayListBean> bean);

    void fail(String errorMsg);

    void refresh(boolean b);

    void refreshSuccess(List<PayListBean> listBeans);

    void loadmore();

    void loadmoreSuccess(List<PayListBean> listBeans);

    void loadAllSuccess();

    void onItemClick(PayListBean bean);
}
