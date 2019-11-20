package com.holike.crm.bean;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pony on 2019/11/8.
 * Version v3.0 app报表
 * 业绩分析数据
 */
public class PerformanceAnalysisBean {
    public String dateToDate; //返回查询范围
    public String format; //返回数据格式 1:analysisData(空间/系列/花色)
    // 2:categoryData(品类) 3:completeRateData(业绩/定制/橱柜/木门/成品/大家居)
    // 4:channelData(渠道) 5:houseData(宅配)
    public String dimensionOf; //
    public String dimensionOfCli;
    public String dimensionOfCliName;
    @SuppressWarnings("WeakerAccess")
    List<DimensionOfCliBean> dimensionOfCliList;

    @SuppressWarnings("WeakerAccess")
    List<AnalysisDataBean> analysisData;

    @SuppressWarnings("WeakerAccess")
    List<CategoryDataBean> categoryData;
    @SuppressWarnings("WeakerAccess")
    List<CompleteRateDataBean> completeRateData;
    @SuppressWarnings("WeakerAccess")
    List<ChannelDataBean> channelData;
    @SuppressWarnings("WeakerAccess")
    List<HouseDataBean> houseData;

    public List<AnalysisDataBean> getAnalysisData() {
        return analysisData == null ? new ArrayList<>() : analysisData;
    }

    public List<CategoryDataBean> getCategoryData() {
        return categoryData == null ? new ArrayList<>() : categoryData;
    }

    public List<DimensionOfCliBean> getDimensionOfCliList() {
        return dimensionOfCliList == null ? new ArrayList<>() : dimensionOfCliList;
    }

    public List<CompleteRateDataBean> getCompleteRateData() {
        return completeRateData == null ? new ArrayList<>() : completeRateData;
    }

    public List<ChannelDataBean> getChannelData() {
        return channelData == null ? new ArrayList<>() : channelData;
    }

    public List<HouseDataBean> getHouseData() {
        return houseData == null ? new ArrayList<>() : houseData;
    }

    public static class AnalysisDataBean {
        public String performance; //业绩(万)
        public String proportion; //占比
        public String name;
        public String ranking; //序号
        public String type; //权限类型
        public String cityCode; //城市编码
        String isClick; //可否点击

        public boolean isClickable() {
            return TextUtils.equals(isClick, "1");
        }
    }

    public static class DimensionOfCliBean {
        public String type;
        public String name;
    }

    /*品类分析数据*/
    public static class CategoryDataBean {
        public String division; //划分
        public String principal; //负责人
        String isClick; //可否点击
        public String total; //总业绩
        public String type; //权限类型
        public String cityCode; //城市编码
        @SerializedName("wooden_door")
        public String woodenDoor; //木门占比
        public String finished; //成品占比
        public String customize; //定制
        public String cupboard; //橱柜
        public String curtain; //窗帘
        public String bigHouse; //大家居
        public String userCode; //
        @SerializedName("division_code")
        public String divisionCode;

        public boolean isClickable() {
            return TextUtils.equals(isClick, "1");
        }
    }

    /*业绩完成率数据*/
    public static class CompleteRateDataBean {
        public String division; //划分
        public String principal; //负责人
        String isClick;
        @SerializedName("year_on_year")
        public String yoy; //同比
        @SerializedName("complete_day")
        public String completeDay; //今日
        @SerializedName("complete_rate")
        public String completeRate; //完成率
        public String complete; //已完成
        public String ranking; //排名
        public String cityCode;
        public String type;
        @SerializedName("division_code")
        public String divisionCode;
        public String target; //目标

        public boolean isClickable() {
            return TextUtils.equals(isClick, "1");
        }
    }

    /*渠道分析数据*/
    public static class ChannelDataBean {
        public String type;
        public String cityCode;
        String isClick;
        public String division; //划分
        public String principal; //负责人
        public String total; //总业绩(万)
        public String home; //家装
        @SerializedName("new_retail")
        public String newRetail; //新零售
        public String wholesale; //整装
        public String conventional; //常规
        public String bag; //拎包
        public String traditional; //传统

        public String userCode;
        @SerializedName("division_code")
        public String divisionCode;

        public boolean isClickable() {
            return TextUtils.equals(isClick, "1");
        }
    }

    /*宅配数据*/
    public static class HouseDataBean {
        public String category; //类别
        public String performance; //业绩
        public String quantity; //数量
        @SerializedName("performance_rate")
        public String performanceRate; //占比
        public String ranking; //排名
        String isClick;

        public boolean isClickable() {
            return TextUtils.equals(isClick, "1");
        }
    }
}
