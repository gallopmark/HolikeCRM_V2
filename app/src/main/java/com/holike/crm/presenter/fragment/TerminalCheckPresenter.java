package com.holike.crm.presenter.fragment;

import com.holike.crm.base.BasePresenter;
import com.holike.crm.bean.TerminalCheckBean;
import com.holike.crm.model.fragment.TerminalCheckModel;
import com.holike.crm.view.fragment.TerminalCheckView;

/**
 * Created by wqj on 2018/5/31.
 * 终端大检查
 */

public class TerminalCheckPresenter extends BasePresenter<TerminalCheckView, TerminalCheckModel> {

    /**
     * 获取数据
     *
     * @param cityCode
     * @param type
     */
    public void getData(String cityCode, String type) {
        model.getData(cityCode == null ? "" : cityCode, type == null ? "" : type, new TerminalCheckModel.GetDataListener() {
            @Override
            public void success(TerminalCheckBean bean) {
                if (getView() != null)
                    getView().getDataSuccess(bean);
            }

            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().getDataFailed(failed);
            }
        });
    }
}
