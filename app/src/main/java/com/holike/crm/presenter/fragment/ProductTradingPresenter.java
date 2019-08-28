package com.holike.crm.presenter.fragment;

import com.holike.crm.base.BasePresenter;
import com.holike.crm.bean.ProductTradingBean;
import com.holike.crm.model.fragment.ProductTradingModel;
import com.holike.crm.view.fragment.ProductTradingView;

import java.util.List;

/**
 * Created by wqj on 2018/5/9.
 * 成品交易报表
 */

public class ProductTradingPresenter extends BasePresenter<ProductTradingView, ProductTradingModel> {

    /**
     * 获取报表数据
     */
    public void getData(String cityCode, String time, String type) {
        model.getData(cityCode == null ? "" : cityCode, time == null ? "0" : time, type == null ? "" : type, new ProductTradingModel.GetDataListener() {
            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().getDataFailed(failed);
            }

            @Override
            public void success(ProductTradingBean bean) {
                if (getView() != null)
                    getView().getDataSuccess(bean);
            }
        });
    }

    /**
     * 获取选择位置
     *
     */
    public int getSelectPosition(String time, List<ProductTradingBean.SelectDataBean> selectDataBeans) {
        if (time == null) {
            return 1;
        } else {
            for (int i = 0, size = selectDataBeans.size(); i < size; i++) {
                ProductTradingBean.SelectDataBean bean = selectDataBeans.get(i);
                if (bean.getTime().equals(time)) {
                    return i;
                }
            }
        }
        return 1;
    }
}
