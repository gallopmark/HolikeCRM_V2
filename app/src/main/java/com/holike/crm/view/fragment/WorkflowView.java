package com.holike.crm.view.fragment;

import com.holike.crm.base.BaseView;

import java.util.Date;

/**
 * Created by wqj on 2018/8/2.
 * 所有流程view
 */

public interface WorkflowView extends BaseView {
    void success(Object success);

    void failed(String failed);
}
