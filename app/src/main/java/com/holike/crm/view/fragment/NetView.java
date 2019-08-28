package com.holike.crm.view.fragment;

import com.holike.crm.base.BaseView;
import com.holike.crm.bean.NetBean;
import com.holike.crm.bean.NetDetailBean;

/**
 * Created by wqj on 2018/6/5.
 * 拉网
 */

public interface NetView extends BaseView {
    void getData();

    void getDataSuccess(NetBean netBean);

    void getDataFailed(String failed);

    void getNetDetailSuccess(NetDetailBean bean);

    void getNetDetailFailed(String failed);
}
