package com.holike.crm.view.fragment;

import com.holike.crm.base.BaseView;
import com.holike.crm.bean.BuildStoreBean;

/**
 * Created by wqj on 2018/5/9.
 * 建店数据报表
 */

public interface BuildStoreView extends BaseView {
    void getData();

    void getDataFailed(String failed);

    void getDataSuccess(BuildStoreBean buildStoreBean);
}
