package com.holike.crm.view.fragment;

import com.holike.crm.base.BaseView;
import com.holike.crm.bean.ReportPermissionsBean;

import java.util.List;

/**
 * Created by wqj on 2018/2/24.
 * 分析
 */

public interface ReportView extends BaseView {
    void getPermissionsSuccess(List<ReportPermissionsBean> list);

    void getPermissionsFailed(String failed);
}
