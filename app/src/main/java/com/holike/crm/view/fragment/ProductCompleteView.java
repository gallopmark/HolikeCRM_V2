package com.holike.crm.view.fragment;

import com.holike.crm.base.BaseView;
import com.holike.crm.bean.ProductCompleteBean;

/**
 * Created by wqj on 2018/5/9.
 * 成品目标各月完成率
 */

public interface ProductCompleteView extends BaseView {
    void getData();

    void getDataSuccess(ProductCompleteBean productCompleteBean);

    void getDataFailed(String failed);
}
