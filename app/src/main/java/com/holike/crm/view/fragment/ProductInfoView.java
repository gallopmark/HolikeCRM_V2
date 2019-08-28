package com.holike.crm.view.fragment;

import com.holike.crm.base.BaseView;
import com.holike.crm.bean.ProductInfoBean;

import java.util.List;

public interface ProductInfoView extends BaseView {
    void onSuccess(List<ProductInfoBean> bean);
    void onFail(String errorMsg);

    void onTagSelect(int position, List<ProductInfoBean> listBean);
    void onTagClickStart(boolean showDialog);
}
