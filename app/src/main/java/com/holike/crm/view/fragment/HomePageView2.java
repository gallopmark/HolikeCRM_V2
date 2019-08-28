package com.holike.crm.view.fragment;

import com.holike.crm.base.BaseView;
import com.holike.crm.bean.HomepageBean;

import java.util.List;

public interface HomePageView2 extends BaseView {

    void getHomepageDataSuccess(HomepageBean bean);

    void getHomepageDataFailed(String failed);
}
