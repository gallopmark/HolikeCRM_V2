package com.holike.crm.view.fragment;

import com.holike.crm.base.BaseView;
import com.holike.crm.bean.HomepageBean;

import java.util.List;

/**
 * Created by wqj on 2018/2/24.
 * 首页
 */

public interface HomePageView extends BaseView {

    void getHomepageDataSuccess(HomepageBean bean);

    void getHomepageDataFailed(String failed);

    void salesman(HomepageBean.OrderDataBean orderDataBean);

    void stores(HomepageBean.NewDataBean newDataBean, boolean isDesigner);

    void installer(HomepageBean.NewDataBean newDataBean);

    void noPermissions(List<HomepageBean.MessageListBean> listBeans);

    void showOperateList(List<HomepageBean.NewDataBean.ItemListBean> listBeans);
}
