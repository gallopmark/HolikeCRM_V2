package com.holike.crm.view;


import com.holike.crm.base.BaseView;
import com.holike.crm.bean.RegionBean;

import java.util.List;

public interface RegionView extends BaseView {
    void onSuccess(List<RegionBean> list);

    void onFailure(String failReason);
}
