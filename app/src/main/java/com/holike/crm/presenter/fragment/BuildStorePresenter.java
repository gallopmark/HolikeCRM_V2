package com.holike.crm.presenter.fragment;

import com.holike.crm.base.BasePresenter;
import com.holike.crm.bean.BuildStoreBean;
import com.holike.crm.model.fragment.BuildStoreModel;
import com.holike.crm.view.fragment.BuildStoreView;

/**
 * Created by wqj on 2018/5/9.
 * 建店数据报表
 */

public class BuildStorePresenter extends BasePresenter<BuildStoreView, BuildStoreModel> {
    /**
     * 获取建店报表数据
     */
    public void getData(String cityCode, String time, String type) {
        model.getData(cityCode == null ? "" : cityCode, time == null ? "1" : time, type == null ? "" : type, new BuildStoreModel.GetDataListener() {
            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().getDataFailed(failed);
            }

            @Override
            public void success(BuildStoreBean bean) {
                if (getView() != null)
                    getView().getDataSuccess(bean);
            }
        });
    }
}
