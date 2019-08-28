package com.holike.crm.presenter.fragment;

import com.holike.crm.base.BasePresenter;
import com.holike.crm.bean.ReportPermissionsBean;
import com.holike.crm.model.fragment.ReportModel;
import com.holike.crm.view.fragment.ReportView;

import java.util.List;

/**
 * Created by wqj on 2018/2/24.
 * 分析
 */

public class ReportPresenter extends BasePresenter<ReportView, ReportModel> {
    public void getPermissions() {
        model.getPermissions(new ReportModel.GetPermissionsListener() {
            @Override
            public void success(List<ReportPermissionsBean> list) {
                if (list == null || list.size() == 0) {
                    if (getView() != null)
                    getView().noPromissions();
                } else {
                    if (getView() != null)
                    getView().getPermissionsSuccess(list);
                }
            }

            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().getPermissionsFailed(failed);
            }
        });
    }
}
