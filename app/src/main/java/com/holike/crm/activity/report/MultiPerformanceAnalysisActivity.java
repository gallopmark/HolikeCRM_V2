package com.holike.crm.activity.report;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.holike.crm.R;
import com.holike.crm.base.BaseActivity;

/**
 * Created by pony on 2019/11/7.
 * Version v3.0 app报表
 * 业绩分析-包括：品类业绩分析、渠道业绩分析、系列业绩分析、
 * 空间业绩分析、花色业绩分析、宅配业绩分析
 * dimensionOf：(目前可用 1 2 4 5 ) 获取维度, 1:品类 2:空间 3:客户渠道
 * (4:系列 5:花色 6:宅配 7:完成率及同比-业绩 8:完成率及同比-定制
 * 9:完成率及同比-橱柜 10:完成率及同比-木门 11:完成率及同比-成品
 * 12:完成率及同比-大家居)
 */
public class MultiPerformanceAnalysisActivity extends GeneralReportActivity<MultiPerformanceAnalysisHelper> {

    /*打开品类业绩分析*/
    public static void renderCategory(BaseActivity<?, ?> activity, String subTitle) {
        renderByType(activity, "1", subTitle);
    }

    /*打开空间业绩分析*/
    public static void renderSpace(BaseActivity<?, ?> activity, String subTitle) {
        renderByType(activity,  "2", subTitle);
    }

    /*打开渠道业绩分析*/
    public static void renderChannel(BaseActivity<?, ?> activity, String subTitle) {
        renderByType(activity, "3", subTitle);
    }

    /*打开系列业绩分析*/
    public static void renderSeries(BaseActivity<?, ?> activity, String subTitle) {
        renderByType(activity, "4", subTitle);
    }

    public static void renderColors(BaseActivity<?, ?> activity, String subTitle) {
        renderByType(activity,"5", subTitle);
    }

    /*打开宅配业绩分析*/
    public static void renderDelivery(BaseActivity<?, ?> activity, String subTitle) {
        renderByType(activity,"6", subTitle);
    }

    /*打开完成率及同比-业绩*/
    public static void renderRatioPerformance(BaseActivity<?, ?> activity, String subTitle) {
        renderByType(activity, "7", subTitle);
    }

    /*打开完成率及同比-定制*/
    public static void renderRatioCustom(BaseActivity<?, ?> activity, String subTitle) {
        renderByType(activity, "8", subTitle);
    }

    /*打开完成率及同比-橱柜*/
    public static void renderRatioCupboard(BaseActivity<?, ?> activity, String subTitle) {
        renderByType(activity, "9", subTitle);
    }

    /*打开完成率及同比-木门*/
    public static void renderRatioWoodendoor(BaseActivity<?, ?> activity, String subTitle) {
        renderByType(activity,  "10", subTitle);
    }

    /*打开完成率及同比-成品*/
    public static void renderRatioProduct(BaseActivity<?, ?> activity, String subTitle) {
        renderByType(activity,  "11", subTitle);
    }

    /*打开完成率及同比-大家居*/
    public static void renderRatioBighome(BaseActivity<?, ?> activity, String subTitle) {
        renderByType(activity,"12", subTitle);
    }

    static void renderByType(BaseActivity<?, ?> activity, String dimensionOf, String subTitle) {
        Intent intent = new Intent(activity, MultiPerformanceAnalysisActivity.class);
        intent.putExtra("dimensionOf", dimensionOf);
        intent.putExtra("subTitle", subTitle);
        activity.openActivity(intent);
    }

    @NonNull
    @Override
    protected MultiPerformanceAnalysisHelper newHelper() {
        return new MultiPerformanceAnalysisHelper(this);
    }

    @Override
    protected int setContentViewId() {
        return R.layout.activity_common;
    }

    @Override
    protected void setup(Bundle savedInstanceState) {
//        String type = getIntent().getType();
//        if (TextUtils.equals(type, TYPE_CATEGORY)) {
//            siteTitle(R.string.title_performance_analysis, R.string.title_category_performance_analysis);
//        } else if (TextUtils.equals(type, TYPE_CHANNEL)) {
//            siteTitle(R.string.title_performance_analysis, R.string.title_channel_performance_analysis);
//        } else if (TextUtils.equals(type, TYPE_SERIES)) {
//            siteTitle(R.string.title_performance_analysis, R.string.title_series_performance_analysis);
//        } else if (TextUtils.equals(type, TYPE_SPACE)) {
//            siteTitle(R.string.title_performance_analysis, R.string.title_space_performance_analysis);
//        } else if (TextUtils.equals(type, TYPE_COLORS)) {
//            siteTitle(R.string.title_performance_analysis, R.string.title_colors_performance_analysis);
//        } else if (TextUtils.equals(type, TYPE_DELIVERY)) {
//            siteTitle(R.string.title_performance_analysis, R.string.title_homedelivery_performance_analysis);
//        } else if (TextUtils.equals(type, TYPE_RATIO_PERFORMANCE)) {
//            siteTitle(R.string.title_complete_rate, R.string.title_performance_complete_rate);
//        } else if (TextUtils.equals(type, TYPE_RATIO_CUSTOM)) {
//            siteTitle(R.string.title_complete_rate, R.string.title_custom_complete_rate);
//        } else if (TextUtils.equals(type, TYPE_RATIO_CUPBOARD)) {
//            siteTitle(R.string.title_complete_rate, R.string.title_cupboard_complete_rate);
//        } else if (TextUtils.equals(type, TYPE_RATIO_WOODENDOOR)) {
//            siteTitle(R.string.title_complete_rate, R.string.title_woodendoor_complete_rate);
//        } else if (TextUtils.equals(type, TYPE_RATIO_PRODUCT)) {
//            siteTitle(R.string.title_complete_rate, R.string.title_product_complete_rate);
//        } else if (TextUtils.equals(type, TYPE_RATIO_BIGHOME)) {
//            siteTitle(R.string.title_complete_rate, R.string.title_bighome_complete_rate);
//        }
    }

    private void siteTitle(int suffixTitleId, int defaultTitleId) {
        String subTitle = getIntent().getStringExtra("subTitle");
        if (!TextUtils.isEmpty(subTitle)) {
            setTitle(subTitle + getString(suffixTitleId));
        } else {
            setTitle(defaultTitleId);
        }
    }
}
