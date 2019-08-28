package com.holike.crm.view.fragment;

import com.holike.crm.base.BaseView;
import com.holike.crm.bean.ProductTradingBean;

/**
 * Created by wqj on 2018/5/9.
 * 成品交易报表
 */

public interface ProductTradingView extends BaseView {
    void getData();

    void getDataFailed(String failed);

    void getDataSuccess(ProductTradingBean productTradingBean);
}
