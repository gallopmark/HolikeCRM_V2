package com.holike.crm.view.fragment;

import com.holike.crm.base.BaseView;
import com.holike.crm.bean.SpaceManifestBean;

import java.util.List;

public interface SpaceManifestView extends BaseView {
    void onSuccess(List<SpaceManifestBean> beans);
    void onFail(String errorMsg);
    void onSelectInfo(int position,SpaceManifestBean.DataListBean bean);
    void onSubTitleInfo(int position,SpaceManifestBean.DataListBean bean);
}
