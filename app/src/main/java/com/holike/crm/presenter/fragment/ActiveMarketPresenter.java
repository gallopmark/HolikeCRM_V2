package com.holike.crm.presenter.fragment;

import com.holike.crm.base.BasePresenter;
import com.holike.crm.bean.ActiveMarketBean;
import com.holike.crm.model.fragment.ActiveMarketModel;
import com.holike.crm.util.ParseUtils;
import com.holike.crm.view.fragment.ActiveMarketView;

import java.util.List;

/**
 * Created by wqj on 2018/6/21.
 * 主动营销数据报表
 */

public class ActiveMarketPresenter extends BasePresenter<ActiveMarketView, ActiveMarketModel> {

    /**
     * 获取数据
     */
    public void getData(String cityCode, String start, String end, String time) {
        model.getData(cityCode == null ? "" : cityCode, start == null ? "" : String.valueOf(ParseUtils.parseLong(start)), end == null ? "" : String.valueOf(ParseUtils.parseLong(end)), time == null ? "0" : time, new ActiveMarketModel.GetDataListener() {
            @Override
            public void success(ActiveMarketBean bean) {
                if (getView() != null)
                    getView().getDataSuccess(bean);
                if (bean.getIsActive() == 1) {
                    if (getView() != null)
                        getView().showPersonal(bean);
                } else if (bean.getIsSelect() == 1) {
                    if (getView() != null)
                        getView().showNational(bean);
                } else {
                    if (getView() != null)
                        getView().showArea(bean);
                }
            }

            @Override
            public void fainled(String failed) {
                if (getView() != null)
                    getView().getDataFailed(failed);
            }
        });
    }

    /**
     * 设置选中类型
     */
    public int getSelectPosition(String time, List<ActiveMarketBean.TimeDataBean> timeDataBeans) {
        if (time == null) {
            return 1;
        } else {
            for (int i = 0, size = timeDataBeans.size(); i < size; i++) {
                ActiveMarketBean.TimeDataBean bean = timeDataBeans.get(i);
                if (String.valueOf(bean.getTime()).equals(time)) {
                    return i;
                }
            }
        }
        return 1;
    }
}
