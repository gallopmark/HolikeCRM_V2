package com.holike.crm.view.activity;


import com.holike.crm.base.BaseView;
import com.holike.crm.bean.UpdateBean;

/**
 * Created by wqj on 2017/11/27.
 * 主页view
 */

public interface MainView extends BaseView {
    void onGetVersion(UpdateBean updateBean,boolean hasNewVersion);

    void onFailure();
}
