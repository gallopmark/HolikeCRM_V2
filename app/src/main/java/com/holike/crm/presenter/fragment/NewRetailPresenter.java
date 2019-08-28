package com.holike.crm.presenter.fragment;

import com.holike.crm.base.BasePresenter;
import com.holike.crm.bean.NewRetailBean;
import com.holike.crm.model.fragment.NewRetailModel;
import com.holike.crm.view.fragment.NewRetailView;

import java.util.List;

/**
 * Created by wqj on 2018/5/31.
 * 新零售
 */

public class NewRetailPresenter extends BasePresenter<NewRetailView, NewRetailModel> {

    /**
     * 获取数据
     */
    public void getData(String cityCode, String time, String type) {
        model.getData(cityCode == null ? "" : cityCode, time == null ? "" : time, type == null ? "" : type, new NewRetailModel.GetDataListener() {
            @Override
            public void success(NewRetailBean bean) {
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
     * 获取选择的时间position
     */
    public int getSelectPosition(String time, List<NewRetailBean.SelectDataBean> selectDataBeans) {
        if (time == null) {
            return 0;
        } else {
            for (int i = 0, size = selectDataBeans.size(); i < size; i++) {
                NewRetailBean.SelectDataBean bean = selectDataBeans.get(i);
                if (bean != null && Integer.parseInt(time) == bean.getTime()) {
                    return i;
                }
            }
        }
        return 0;
    }
}
