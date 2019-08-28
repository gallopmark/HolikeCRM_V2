package com.holike.crm.view.fragment;

import com.holike.crm.base.BaseView;
import com.holike.crm.bean.WriteCityBean;

/**
 * Created by wqj on 2018/6/26.
 * 主动营销-填写城市
 */

public interface WirteCityView extends BaseView {

    void getDataSuccess(WriteCityBean bean);

    void failed(String failed);

    void saveSucess(String success, WriteCityBean.ActiveRecordBean bean);

    void delSuccess(String success, int position);

    void tips(String text);

    void loading();
}
