package com.holike.crm.presenter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.holike.crm.base.BasePresenter;
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
import com.holike.crm.http.MyJsonParser;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.model.GeneralReportModel;
import com.holike.crm.util.TimeUtil;
import com.holike.crm.view.GeneralReportView;
import com.holike.crm.view.fragment.ReportV3View;

import java.util.Date;
import java.util.List;

/**
 * Created by pony on 2019/10/22.
 * Copyright holike possess 2019.
 * 报表presenter v3.0
 */
public class GeneralReportPresenter extends BasePresenter<GeneralReportView, GeneralReportModel> {
    /**
     * @param startDate 10位开始时间戳
     * @param endDate   10位结束时间戳
     * @param cityCode  城市代码 默认空
     * @param type      类型 默认空
     * @param typeData  1个人签单 2成交 3回款 4下单
     */
    public void getEmployeeRanking(@Nullable Date startDate, @Nullable Date endDate, String cityCode,
                                   String type, String typeData) {
        String startTime = getStartTime(startDate);
        String endTime = getEndTime(endDate);
        if (getModel() != null) {
            getModel().getEmployeeRanking(startTime, endTime, cityCode, type, typeData, new RequestCallBack<EmployeeRankingBean>() {
                @Override
                public void onFailed(String failReason) {
                    onRequestFailure(failReason);
                }

                @Override
                public void onSuccess(EmployeeRankingBean bean) {
                    onRequestSuccess(bean);
                }
            });
        }
    }

    /*获取部门中心门店数据*/
    public void getEvaluateType(String type, String cityCode) {
        if (getModel() != null) {
            getModel().getEvaluateType(type, cityCode, new RequestCallBack<List<EvaluateTypeBean>>() {
                @Override
                public void onFailed(String failReason) {
                    onRequestFailure(failReason);
                }

                @Override
                public void onSuccess(List<EvaluateTypeBean> result) {
                    onRequestSuccess(result);
                }
            });
        }
    }

    public void getEvaluateType(String type, String cityCode, @NonNull final OnGetEvaluateTypeCallback callBack) {
        if (getModel() != null) {
            getModel().getEvaluateType(type, cityCode, new RequestCallBack<List<EvaluateTypeBean>>() {
                @Override
                public void onFailed(String failReason) {
                    callBack.onGetEvaluateTypeFailure(failReason);
                }

                @Override
                public void onSuccess(List<EvaluateTypeBean> result) {
                    callBack.onGetEvaluateTypeSuccess(result);
                }
            });
        }
    }

    public interface OnGetEvaluateTypeCallback {
        void onGetEvaluateTypeSuccess(List<EvaluateTypeBean> dataList);

        void onGetEvaluateTypeFailure(String failReason);
    }

    /*本月数据-设计经理*/
    public void getDesignManagerData(String type, String cityCode, @Nullable Date startDate, @Nullable Date endDate) {
        String startTime = getStartTime(startDate);
        String endTime = getEndTime(endDate);
        if (getModel() != null) {
            getModel().getDesignManagerData(type, cityCode, startTime, endTime, new RequestCallBack<MonthDataDesignBean>() {
                @Override
                public void onFailed(String failReason) {
                    onRequestFailure(failReason);
                }

                @Override
                public void onSuccess(MonthDataDesignBean bean) {
                    onRequestSuccess(bean);
                }
            });
        }
    }

    public void getPersonalPerformance(String type, String time, Date startDate, Date endDate) {
        String startTime = getStartTime(startDate);
        String endTime = getEndTime(endDate);
        if (getModel() != null) {
            getModel().getPersonalPerformance(type, time, startTime, endTime, new RequestCallBack<PersonalPerformanceBean>() {
                @Override
                public void onFailed(String failReason) {
                    onRequestFailure(failReason);
                }

                @Override
                public void onSuccess(PersonalPerformanceBean result) {
                    onRequestSuccess(result);
                }
            });
        }
    }

    /*获取目标*/
    public void getBusinessTarget() {
        if (getModel() != null) {
            getModel().getBusinessTarget(new RequestCallBack<BusinessTargetBean>() {
                @Override
                public void onFailed(String failReason) {
                    onRequestFailure(failReason);
                }

                @Override
                public void onSuccess(BusinessTargetBean result) {
                    onRequestSuccess(result);
                }
            });
        }
    }

    /*设置目标*/
    public void setBusinessTarget(String param, String ids) {
        if (getModel() != null) {
            getModel().setBusinessTarget(param, ids, new RequestCallBack<String>() {
                @Override
                public void onFailed(String failReason) {
                    onRequestFailure(failReason);
                }

                @Override
                public void onSuccess(String result) {
                    onRequestSuccess(MyJsonParser.getShowMessage(result));
                }
            });
        }
    }

    /*获取员工绩效*/
    public void getEmployeePerformance(String type, String cityCode, Date startDate, Date endDate) {
        String startTime = getStartTime(startDate);
        String endTime = getEndTime(endDate);
        if (getModel() != null) {
            getModel().getEmployeePerformance(type, cityCode, startTime, endTime, new RequestCallBack<MonthDataBossBean>() {
                @Override
                public void onFailed(String failReason) {
                    onRequestFailure(failReason);
                }

                @Override
                public void onSuccess(MonthDataBossBean result) {
                    onRequestSuccess(result);
                }
            });
        }
    }

    /*经营目标首页数据*/
    public void getBusinessObjectives(String shopId) {
        if (getModel() != null) {
            getModel().getBusinessObjectives(shopId, new RequestCallBack<BusinessObjectivesBean>() {
                @Override
                public void onFailed(String failReason) {
                    onRequestFailure(failReason);
                }

                @Override
                public void onSuccess(BusinessObjectivesBean result) {
                    onRequestSuccess(result);
                }
            });
        }
    }

    /*3.0获取分析图标*/
    public void getPowInfo(@NonNull final ReportV3View view) {
        if (getModel() != null) {
            getModel().getPowInfo(new RequestCallBack<List<ReportV3IconBean>>() {
                @Override
                public void onFailed(String failReason) {
                    view.onFailure(failReason);
                }

                @Override
                public void onSuccess(List<ReportV3IconBean> result) {
                    view.onSuccess(result);
                }
            });
        }
    }

    /*客户转化率报表*/
    public void getCustomerConversion(Date startDate, Date endDate, String shopId) {
        if (getModel() != null) {
            getModel().getCustomerConversion(shopId, getStartTime(startDate), getEndTime(endDate), new RequestCallBack<CustomerConversionBean>() {
                @Override
                public void onFailed(String failReason) {
                    onRequestFailure(failReason);
                }

                @Override
                public void onSuccess(CustomerConversionBean result) {
                    onRequestSuccess(result);
                }
            });
        }
    }

    /*经销商-出厂业绩*/
    public void getFactoryPerformance(String dimensionOf, String month) {
        if (getModel() != null) {
            getModel().getFactoryPerformance(dimensionOf, month, new RequestCallBack<FactoryPerformanceBean>() {
                @Override
                public void onFailed(String failReason) {
                    onRequestFailure(failReason);
                }

                @Override
                public void onSuccess(FactoryPerformanceBean result) {
                    onRequestSuccess(result);
                }
            });
        }
    }

    /**
     * 营销人员-出厂业绩
     *
     * @param time           1-本月 2-全年 3-选择日期
     * @param dimensionOf    (目前可用 1 2 4 5 ) 获取维度, 1:品类 2:空间 3:客户渠道
     *                       (4:系列 5:花色 6:宅配 7:完成率及同比-业绩 8:完成率及同比-定制
     *                       9:完成率及同比-橱柜 10:完成率及同比-木门 11:完成率及同比-成品
     *                       12:完成率及同比-大家居)
     * @param dimensionOfCli 1:定制 2:木门 3:橱柜
     * @param type           用户权限类型 1是大区经理 2是中心跟销售人员 3区域经理 4经销商 -1全国
     * @param cityCode       区域编码
     * @param startDate      开始日期
     * @param endDate        结束日期
     */
    public void getMarketPerformanceAnalysis(String time, String dimensionOf, String dimensionOfCli,
                                             String type, String cityCode, Date startDate, Date endDate) {
        if (getModel() != null) {
            getModel().getMarketPerformanceAnalysis(time, dimensionOf, dimensionOfCli, type, cityCode,
                    getStartTime(startDate), getEndTime(endDate), new RequestCallBack<PerformanceAnalysisBean>() {
                        @Override
                        public void onFailed(String failReason) {
                            onRequestFailure(failReason);
                        }

                        @Override
                        public void onSuccess(PerformanceAnalysisBean result) {
                            onRequestSuccess(result);
                        }
                    });
        }
    }

    /**
     * 获取门店分析数据
     *
     * @param time      1全年 2本月 3自定义时间
     * @param startDate 开始日期
     * @param endDate   结束日期
     */
    public void getShopAnalysisData(String time, String type, String cityCode, Date startDate, Date endDate) {
        if (getModel() != null) {
            getModel().getShopAnalysisData(time, type, cityCode, getStartTime(startDate), getEndTime(endDate),
                    new RequestCallBack<ShopAnalysisBean>() {
                        @Override
                        public void onFailed(String failReason) {
                            onRequestFailure(failReason);
                        }

                        @Override
                        public void onSuccess(ShopAnalysisBean result) {
                            onRequestSuccess(result);
                        }
                    });
        }
    }

    /**
     * 获取线上引流报表数据
     *
     * @param time      1全年 2本月 3自定义日期
     * @param startDate 开始日期
     * @param endDate   结束日期
     */
    public void getOnlineDrainageData(String time, String type, String cityCode, Date startDate, Date endDate) {
        if (getModel() != null) {
            getModel().getOnlineDrainageData(time, type, cityCode,
                    getStartTime(startDate), getEndTime(endDate), new RequestCallBack<OnlineDrainageBean>() {
                        @Override
                        public void onFailed(String failReason) {
                            onRequestFailure(failReason);
                        }

                        @Override
                        public void onSuccess(OnlineDrainageBean result) {
                            onRequestSuccess(result);
                        }
                    });
        }
    }

    /**
     * 获取板材分析数据
     *
     * @param time      时间维度(1:全年 2:月 3:范围)
     * @param startDate 开始日期
     * @param endDate   结束日期
     */
    public void getSheetAnalysisData(String time, String type, String cityCode, Date startDate, Date endDate) {
        if (getModel() != null) {
            getModel().getSheetAnalysisData(time, type, cityCode,
                    getStartTime(startDate), getEndTime(endDate), new RequestCallBack<SheetAnalysisBean>() {
                        @Override
                        public void onFailed(String failReason) {
                            onRequestFailure(failReason);
                        }

                        @Override
                        public void onSuccess(SheetAnalysisBean result) {
                            onRequestSuccess(result);
                        }
                    });
        }
    }

    /*家装渠道-营销人员*/
    public void getHomeDollChannel(String type, String cityCode) {
        if (getModel() != null) {
            getModel().getHomeDollChannel(type, cityCode, new RequestCallBack<HomeDollBean>() {
                @Override
                public void onFailed(String failReason) {
                    onRequestFailure(failReason);
                }

                @Override
                public void onSuccess(HomeDollBean result) {
                    onRequestSuccess(result);
                }
            });
        }
    }

    /*家装渠道-经销商*/
    public void getDealerHomeDollChannel() {
        if (getModel() != null) {
            getModel().getDealerHomeDollChannel(new RequestCallBack<HomeDollBean>() {
                @Override
                public void onFailed(String failReason) {
                    onRequestFailure(failReason);
                }

                @Override
                public void onSuccess(HomeDollBean result) {
                    onRequestSuccess(result);
                }
            });
        }
    }

    /*花色/系列 业绩详情-经销商*/
    public void getDealerMultiPerformance(String month, String dimensionOf, String dimensionOfCli, String shopCode) {
        if(getModel()!=null){
            getModel().getDealerMultiPerformance(month, dimensionOf, dimensionOfCli, shopCode, new RequestCallBack<BusinessReferenceTypeBean>() {
                @Override
                public void onFailed(String failReason) {
                    onRequestFailure(failReason);
                }

                @Override
                public void onSuccess(BusinessReferenceTypeBean result) {
                    onRequestSuccess(result);
                }
            });
        }
    }

    private String getStartTime(Date startDate) {
        if (startDate == null) return "";
        return TimeUtil.dateToStamp(startDate, false);
    }

    private String getEndTime(Date endDate) {
        if (endDate == null) return "";
        return TimeUtil.dateToStamp(endDate, true);
    }

    private void onRequestFailure(String failReason) {
        if (getView() != null) {
            getView().onFailure(failReason);
        }
    }

    private void onRequestSuccess(Object obj) {
        if (getView() != null) {
            getView().onSuccess(obj);
        }
    }
}
