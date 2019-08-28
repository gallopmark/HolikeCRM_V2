package com.holike.crm.view.fragment;

import com.holike.crm.base.BaseView;
import com.holike.crm.bean.CreditInquiryBean;

import java.util.List;

public interface CreditInquiryView extends BaseView {
    void onSuccess(CreditInquiryBean bean);

    void onFail(String errorMsg);


    void refresh(boolean b);

    void refreshSuccess(List<CreditInquiryBean.PageDataBean> listBeans);

    void loadmore();

    void loadmoreSuccess(List<CreditInquiryBean.PageDataBean> listBeans);

    void loadAllSuccess();

    void onItemLongClick(String orderId);
}
