package com.holike.crm.model;

import com.holike.crm.base.BaseModel;
import com.holike.crm.bean.BusinessObjectivesBean;
import com.holike.crm.bean.BusinessReferenceTypeBean;
import com.holike.crm.bean.BusinessTargetBean;
import com.holike.crm.bean.CustomerConversionBean;
import com.holike.crm.bean.EmployeeRankingBean;
import com.holike.crm.bean.EvaluateTypeBean;
import com.holike.crm.bean.FactoryPerformanceBean;
import com.holike.crm.bean.HomeDollBean;
import com.holike.crm.bean.MonthDataBossBean;
import com.holike.crm.bean.MonthDataDesignBean;
import com.holike.crm.bean.OnlineDrainageBean;
import com.holike.crm.bean.PerformanceAnalysisBean;
import com.holike.crm.bean.PersonalPerformanceBean;
import com.holike.crm.bean.ReportV3IconBean;
import com.holike.crm.bean.SheetAnalysisBean;
import com.holike.crm.bean.ShopAnalysisBean;
import com.holike.crm.http.ParamHelper;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.http.UrlPath;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneralReportModel extends BaseModel {

    private static final int DEFAULT_TIMEOUT = 60;

    public void getEmployeeRanking(String startTime, String endTime, String cityCode,
                                   String type, String typeData, RequestCallBack<EmployeeRankingBean> callBack) {
        Map<String, String> params = new HashMap<>();
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("cityCode", ParamHelper.noNullWrap(cityCode));
        params.put("type", ParamHelper.noNullWrap(type));
        params.put("typedata", ParamHelper.noNullWrap(typeData));
        postByTimeout(UrlPath.URL_EMPLOYEE_RANKING, params, DEFAULT_TIMEOUT, callBack);
    }

    /*获取部门中心门店数据*/
    public void getEvaluateType(String type, String cityCode, RequestCallBack<List<EvaluateTypeBean>> callBack) {
        Map<String, String> params = new HashMap<>();
        params.put("type", ParamHelper.noNullWrap(type));
        params.put("cityCode", ParamHelper.noNullWrap(cityCode));
        postByTimeout(UrlPath.URL_EVALUATE_TYPE, params, 30, callBack);
    }

    /*本月数据-设计经理*/
    public void getDesignManagerData(String type, String cityCode, String startTime, String endTime, RequestCallBack<MonthDataDesignBean> callBack) {
        Map<String, String> params = new HashMap<>();
        params.put("type", ParamHelper.noNullWrap(type));
        params.put("cityCode", ParamHelper.noNullWrap(cityCode));
        params.put("startTime", ParamHelper.noNullWrap(startTime));
        params.put("endTime", ParamHelper.noNullWrap(endTime));
        postByTimeout(UrlPath.URL_DESIGN_MONTH_DATA, params, 30, callBack);
    }

    /*本月数据详情*/
    public void getPersonalPerformance(String type, String time, String startTime, String endTime, RequestCallBack<PersonalPerformanceBean> callBack) {
        Map<String, String> params = new HashMap<>();
        params.put("type", ParamHelper.noNullWrap(type));
        params.put("time", ParamHelper.noNullWrap(time));
        params.put("startTime", ParamHelper.noNullWrap(startTime));
        params.put("endTime", ParamHelper.noNullWrap(endTime));
        postByTimeout(UrlPath.URL_PERSONAL_PERFORMANCE, params, 30, callBack);
    }

    /*获取目标*/
    public void getBusinessTarget(RequestCallBack<BusinessTargetBean> callBack) {
        postByTimeout(UrlPath.URL_GET_TARGET, DEFAULT_TIMEOUT, callBack);
    }

    /*设置目标*/
    public void setBusinessTarget(String param, String ids, RequestCallBack<String> callBack) {
        Map<String, String> params = new HashMap<>();
        params.put("param", param);
        params.put("ids", ids);
        postByTimeout(UrlPath.URL_SET_TARGET, params, 30, callBack);
    }

    /*获取员工绩效*/
    public void getEmployeePerformance(String type, String cityCode, String startTime, String endTime, RequestCallBack<MonthDataBossBean> callBack) {
        Map<String, String> params = new HashMap<>();
        params.put("type", ParamHelper.noNullWrap(type));
        params.put("cityCode", ParamHelper.noNullWrap(cityCode));
        params.put("startTime", ParamHelper.noNullWrap(startTime));
        params.put("endTime", ParamHelper.noNullWrap(endTime));
        postByTimeout(UrlPath.URL_BOSS_MONTH_DATA, params, DEFAULT_TIMEOUT, callBack);
    }

    /*经营目标首页数据*/
    public void getBusinessObjectives(String shopId, RequestCallBack<BusinessObjectivesBean> callBack) {
        Map<String, String> params = new HashMap<>();
        params.put("shopId", ParamHelper.noNullWrap(shopId));
        postByTimeout(UrlPath.URL_BUSINESS_OBJECTIVES, params, DEFAULT_TIMEOUT, callBack);
    }

    public void getPowInfo(RequestCallBack<List<ReportV3IconBean>> callBack) {
        postByTimeout(UrlPath.URL_POWER_INFO, DEFAULT_TIMEOUT, callBack);
    }

    /*客户转化报表*/
    public void getCustomerConversion(String shopId, String startTime, String endTime, RequestCallBack<CustomerConversionBean> callBack) {
        Map<String, String> params = new HashMap<>();
        params.put("shopId", ParamHelper.noNullWrap(shopId));
        params.put("startTime", ParamHelper.noNullWrap(startTime));
        params.put("endTime", ParamHelper.noNullWrap(endTime));
        postByTimeout(UrlPath.URL_CUSTOMER_CONVERSION, params, 30, callBack);
    }

    /*经销商-出厂业绩*/
    public void getFactoryPerformance(String dimensionOf, String month, RequestCallBack<FactoryPerformanceBean> callBack) {
        Map<String, String> params = new HashMap<>();
        params.put("dimensionOf", ParamHelper.noNullWrap(dimensionOf));
        params.put("month", ParamHelper.noNullWrap(month));
        postByTimeout(UrlPath.URL_DEALER_FACTORY_PERFORMANCE, params, DEFAULT_TIMEOUT, callBack);
    }

    /*营销人员-出厂业绩*/
    public void getMarketPerformanceAnalysis(String time, String dimensionOf, String dimensionOfCli, String type,
                                             String cityCode, String startTime, String endTime,
                                             RequestCallBack<PerformanceAnalysisBean> callBack) {
        Map<String, String> params = new HashMap<>();
        params.put("time", ParamHelper.noNullWrap(time));
        params.put("dimensionOf", ParamHelper.noNullWrap(dimensionOf));
        params.put("dimensionOfCli", ParamHelper.noNullWrap(dimensionOfCli));
        params.put("type", ParamHelper.noNullWrap(type));
        params.put("cityCode", ParamHelper.noNullWrap(cityCode));
        params.put("startTime", ParamHelper.noNullWrap(startTime));
        params.put("endTime", ParamHelper.noNullWrap(endTime));
        postByTimeout(UrlPath.URL_MARKET_FACTORY_PERFORMANCE, params, DEFAULT_TIMEOUT, callBack);
    }

    /*门店分析*/
    public void getShopAnalysisData(String time, String type, String cityCode,
                                    String startTime, String endTime, RequestCallBack<ShopAnalysisBean> callBack) {
        Map<String, String> params = new HashMap<>();
        params.put("time", ParamHelper.noNullWrap(time));
        params.put("type", ParamHelper.noNullWrap(type));
        params.put("cityCode", ParamHelper.noNullWrap(cityCode));
        params.put("startTime", ParamHelper.noNullWrap(startTime));
        params.put("endTime", ParamHelper.noNullWrap(endTime));
        postByTimeout(UrlPath.URL_SHOP_ANALYSIS, params, DEFAULT_TIMEOUT, callBack);
    }

    /*线上引流*/
    public void getOnlineDrainageData(String time, String type, String cityCode,
                                      String startTime, String endTime, RequestCallBack<OnlineDrainageBean> callBack) {
        Map<String, String> params = new HashMap<>();
        params.put("time", ParamHelper.noNullWrap(time));
        params.put("type", ParamHelper.noNullWrap(type));
        params.put("cityCode", ParamHelper.noNullWrap(cityCode));
        params.put("startTime", ParamHelper.noNullWrap(startTime));
        params.put("endTime", ParamHelper.noNullWrap(endTime));
        postByTimeout(UrlPath.URL_ONLINE_DRAINAGE, params, DEFAULT_TIMEOUT, callBack);
    }

    /*板材分析*/
    public void getSheetAnalysisData(String time, String type, String cityCode,
                                     String startTime, String endTime, RequestCallBack<SheetAnalysisBean> callBack) {
        Map<String, String> params = new HashMap<>();
        params.put("time", ParamHelper.noNullWrap(time));
        params.put("type", ParamHelper.noNullWrap(type));
        params.put("cityCode", ParamHelper.noNullWrap(cityCode));
        params.put("startTime", ParamHelper.noNullWrap(startTime));
        params.put("endTime", ParamHelper.noNullWrap(endTime));
        postByTimeout(UrlPath.URL_SHEET_ANALYSIS, params, DEFAULT_TIMEOUT, callBack);
    }

    /*家装渠道-营销人员*/
    public void getHomeDollChannel(String type, String cityCode, RequestCallBack<HomeDollBean> callBack) {
        Map<String, String> params = new HashMap<>();
        params.put("type", ParamHelper.noNullWrap(type));
        params.put("cityCode", ParamHelper.noNullWrap(cityCode));
        postByTimeout(UrlPath.URL_HOME_DOLL_CHANNEL, params, DEFAULT_TIMEOUT, callBack);
    }

    /*家装渠道-经销商*/
    public void getDealerHomeDollChannel(RequestCallBack<HomeDollBean> callBack) {
        postByTimeout(UrlPath.URL_DEALER_HOME_DOLL_CHANNEL, DEFAULT_TIMEOUT, callBack);
    }

    /*花色/系列 业绩详情-经销商*/
    public void getDealerMultiPerformance(String month, String dimensionOf, String dimensionOfCli, String shopCode, RequestCallBack<BusinessReferenceTypeBean> callBack) {
        Map<String, String> params = new HashMap<>();
        params.put("month", ParamHelper.noNullWrap(month));
        params.put("dimensionOf", ParamHelper.noNullWrap(dimensionOf));
        params.put("dimensionOfCli", ParamHelper.noNullWrap(dimensionOfCli));
        params.put("shopCode", ParamHelper.noNullWrap(shopCode));
        postByTimeout(UrlPath.URL_DEALER_MULTI_PERFORMANCE, params, DEFAULT_TIMEOUT, callBack);
    }
}
