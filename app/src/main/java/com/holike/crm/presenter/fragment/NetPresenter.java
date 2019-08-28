package com.holike.crm.presenter.fragment;

import com.holike.crm.base.BasePresenter;
import com.holike.crm.bean.NetBean;
import com.holike.crm.bean.NetDetailBean;
import com.holike.crm.model.fragment.NetModel;
import com.holike.crm.view.fragment.NetView;

/**
 * Created by wqj on 2018/6/5.
 * 拉网
 */

public class NetPresenter extends BasePresenter<NetView, NetModel> {
    /**
     * 获取数据
     */
    public void getData(String cityCode, String type) {
        model.getData(cityCode == null ? "" : cityCode, type == null ? "" : type, new NetModel.GetDataListener() {
            @Override
            public void success(NetBean bean) {
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

    /**
     * 获取拉网明细
     */
    public void getNetDetail(String cityCode, String type) {
        model.getNetDetail(cityCode == null ? "" : cityCode, type == null ? "" : type, new NetModel.GetNetDetailListener() {
            @Override
            public void success(NetDetailBean bean) {
                if (getView() != null)
                    getView().getNetDetailSuccess(bean);
            }

            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().getNetDetailFailed(failed);
            }
        });
    }
}
