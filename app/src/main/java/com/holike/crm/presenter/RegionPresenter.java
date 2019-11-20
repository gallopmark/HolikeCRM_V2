package com.holike.crm.presenter;

import com.holike.crm.base.BasePresenter;
import com.holike.crm.bean.RegionBean;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.model.RegionModel;
import com.holike.crm.view.RegionView;

import java.util.List;

/**
 * Created by pony on 2019/8/2.
 * Copyright holike possess 2019.
 * 获取区域
 */
public class RegionPresenter extends BasePresenter<RegionView, RegionModel> {

    public void getProvince() {
        if (getModel() != null) {
            getModel().getProvince(new RequestCallBack<List<RegionBean>>() {
                @Override
                public void onFailed(String failReason) {
                    if (getView() != null) {
                        getView().onFailure(failReason);
                    }
                }

                @Override
                public void onSuccess(List<RegionBean> list) {
                    if (getView() != null) {
                        getView().onSuccess(list);
                    }
                }
            });
        }
    }

    /*获取市县(区) regionLevel：等级(2-市，3-区(县))*/
    public void getChildRegion(String parentRegionCode, String regionLevel) {
        if (getModel() != null) {
            getModel().getChildRegion(parentRegionCode, regionLevel, new RequestCallBack<List<RegionBean>>() {
                @Override
                public void onFailed(String failReason) {
                    if (getView() != null) {
                        getView().onFailure(failReason);
                    }
                }

                @Override
                public void onSuccess(List<RegionBean> list) {
                    if (getView() != null) {
                        getView().onSuccess(list);
                    }
                }
            });
        }
    }
}
