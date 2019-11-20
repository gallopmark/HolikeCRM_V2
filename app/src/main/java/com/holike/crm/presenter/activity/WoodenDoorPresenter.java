package com.holike.crm.presenter.activity;

import android.text.TextUtils;

import com.holike.crm.base.BasePresenter;
import com.holike.crm.bean.WoodenDoorBean;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.model.activity.WoodenDoorModel;
import com.holike.crm.view.activity.WoodenDoorView;

import java.util.List;

/**
 * Created by pony on 2019/8/28.
 * Copyright holike possess 2019.
 */
public class WoodenDoorPresenter extends BasePresenter<WoodenDoorView, WoodenDoorModel> {

    public void getData(String cityCode, String time, String type) {
        model.getData(cityCode == null ? "" : cityCode, time == null ? "0" : time, type == null ? "" : type,
                new RequestCallBack<WoodenDoorBean>() {
                    @Override
                    public void onFailed(String result) {
                        if (getView() != null) {
                            getView().onFailure(result);
                        }
                    }

                    @Override
                    public void onSuccess(WoodenDoorBean result) {
                        if (getView() != null) {
                            getView().onSuccess(result);
                        }
                    }
                });
    }

    public int getSelectPosition(String time, List<WoodenDoorBean.SelectDataBean> selectDataBeans) {
        if (TextUtils.isEmpty(time)) {
            return 1;
        } else {
            for (int i = 0, size = selectDataBeans.size(); i < size; i++) {
                WoodenDoorBean.SelectDataBean bean = selectDataBeans.get(i);
                if (TextUtils.equals(time, bean.selectTime)) {
                    return i;
                }
            }
        }
        return 1;
    }
}
