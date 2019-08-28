package com.holike.crm.view.fragment;

import com.holike.crm.base.BaseView;
import com.holike.crm.bean.NewRetailBean;

/**
 * Created by wqj on 2018/5/31.
 * 新零售
 */

public interface NewRetailView extends BaseView {
    void getData();

    void getDataSuccess(NewRetailBean bean);

    void getDataFailed(String failed);
}
