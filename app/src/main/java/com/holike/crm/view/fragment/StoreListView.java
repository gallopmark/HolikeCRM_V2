package com.holike.crm.view.fragment;

import com.holike.crm.base.BaseView;
import com.holike.crm.bean.DistributionStoreBean;

import java.util.List;

public interface StoreListView extends BaseView {
    void onShowLoading();
    void getStoreList(List<DistributionStoreBean>beans);
    void getStoreFailure(String errorMessage);
    void onHideLoading();
}
