package com.holike.crm.view.activity;

import com.holike.crm.base.BaseView;

/**
 * Created by wqj on 2018/4/13.
 * 启动页
 */

public interface BootingView extends BaseView {

    void getAdSuccess(String url);

    void getAdFailed(String failed);
}
