package com.holike.crm.view.fragment;

import com.holike.crm.base.BaseView;
import com.holike.crm.bean.LineAttractBean;

public interface OnlineAttractReportView extends BaseView {
    void getDataSuccess(LineAttractBean bean);

    void getDataFail(String errorMsg);

    void getData();

    void onSmoothScroll(int position);
    void onTagSelect(int position,LineAttractBean datas);
}
