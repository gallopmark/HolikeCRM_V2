package com.holike.crm.view.fragment;

import com.holike.crm.base.BaseView;
import com.holike.crm.bean.TerminalCheckBean;

/**
 * Created by wqj on 2018/5/31.
 * 终端大检查
 */

public interface TerminalCheckView extends BaseView {
    void getData();

    void getDataSuccess(TerminalCheckBean terminalCheckBean);

    void getDataFailed(String failed);
}
