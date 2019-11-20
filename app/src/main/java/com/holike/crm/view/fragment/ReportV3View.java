package com.holike.crm.view.fragment;

import com.holike.crm.bean.ReportV3IconBean;

import java.util.List;

/**
 * Created by pony on 2019/10/31.
 * Version v3.0 app报表
 */
public interface ReportV3View {

    void onSuccess(List<ReportV3IconBean> dataList);

    void onFailure(String failReason);
}
