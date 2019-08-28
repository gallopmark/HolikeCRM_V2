package com.holike.crm.model.fragment;

import com.holike.crm.base.BaseModel;
import com.holike.crm.bean.MonthDataBossBean;
import com.holike.crm.bean.MonthDataFinanceBean;
import com.holike.crm.bean.MonthDataInstallManagerBean;
import com.holike.crm.http.ParamHelper;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.http.UrlPath;


/**
 * Created by gallop on 2019/8/9.
 * Copyright holike possess 2019.
 */
public class MonthDataModel extends BaseModel {

    /*获取老板本月数据*/
    public void getMonthDataBoss(String type, String cityCode, String startTime, String endTime, RequestCallBack<MonthDataBossBean> callBack) {
        post(UrlPath.URL_BOSS_MONTH_DATA, ParamHelper.Homepage.monthData(type, cityCode, startTime, endTime), callBack);
    }

    /*获取安装经理本月数据*/
    public void getMonthDataInstallManager(String type, String cityCode, String startTime, String endTime, RequestCallBack<MonthDataInstallManagerBean> callBack) {
        post(UrlPath.URL_INSTALL_MANAGER_MONTH_DATA, ParamHelper.Homepage.monthData(type, cityCode, startTime, endTime), callBack);
    }

    /*获取财务本月数据*/
    public void getMonthDataFinance(String type, String cityCode, String startTime, String endTime, RequestCallBack<MonthDataFinanceBean> callBack) {
        post(UrlPath.URL_FINANCE_MONTH_DATA, ParamHelper.Homepage.monthData(type, cityCode, startTime, endTime), callBack);
    }
}
