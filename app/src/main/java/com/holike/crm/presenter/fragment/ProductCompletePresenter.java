package com.holike.crm.presenter.fragment;

import com.holike.crm.base.BasePresenter;
import com.holike.crm.bean.ProductCompleteBean;
import com.holike.crm.model.fragment.ProductCompleteModel;
import com.holike.crm.view.fragment.ProductCompleteView;

/**
 * Created by wqj on 2018/5/9.
 * 成品目标各月完成率
 */

public class ProductCompletePresenter extends BasePresenter<ProductCompleteView, ProductCompleteModel> {

    /**
     * 获取各月完成率数据
     *
     */
    public void getData(String cityCode, String type) {
        model.getData(cityCode == null ? "" : cityCode, type == null ? "" : type, new ProductCompleteModel.GetDataListener() {
            @Override
            public void success(ProductCompleteBean bean) {
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
