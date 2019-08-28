package com.holike.crm.view.fragment;

import com.holike.crm.base.BaseView;
import com.holike.crm.bean.OpreationLogBean;
import com.holike.crm.bean.ProductInfoBean;

import java.util.List;

public interface OperationLogView extends BaseView {
    void onSuccess(List<OpreationLogBean> bean);
    void onFail(String errorMsg);

}
