package com.holike.crm.view.fragment;

import com.holike.crm.base.BaseView;
import com.holike.crm.bean.OriginalBoardBean;

/**
 * Created by wqj on 2018/5/25.
 * 原态板占比
 */

public interface OriginalBoardView extends BaseView {
    void getData();

    void getDataSuccess(OriginalBoardBean bean);

    void getDataFailed(String failed);
}
