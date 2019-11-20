package com.holike.crm.activity.report;

import android.content.Intent;

import com.holike.crm.base.ActivityHelper;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.fragment.report.multiple.CategoryAnalysisFragment;
import com.holike.crm.fragment.report.multiple.ChannelAnalysisFragment;
import com.holike.crm.fragment.report.multiple.ColorsAnalysisFragment;
import com.holike.crm.fragment.report.multiple.HomeDeliveryAnalysisFragment;
import com.holike.crm.fragment.report.multiple.MultiCompleteRateFragment;
import com.holike.crm.fragment.report.multiple.SeriesAnalysisFragment;
import com.holike.crm.fragment.report.multiple.SpaceAnalysisFragment;
import com.holike.crm.util.ParseUtils;


/**
 * Created by pony on 2019/11/7.
 * Version v3.0 app报表
 * 业绩分析...
 */
class MultiPerformanceAnalysisHelper extends ActivityHelper {

    MultiPerformanceAnalysisHelper(BaseActivity<?, ?> activity) {
        super(activity);
        initialize();
    }

    private void initialize() {
        Intent intent = mActivity.getIntent();
        String subTitle = intent.getStringExtra("subTitle");
        String dimensionOf = intent.getStringExtra("dimensionOf");
        int analysisType = ParseUtils.parseInt(dimensionOf);
        switch (analysisType) {
            case 1:  //品类
                mActivity.startFragment(CategoryAnalysisFragment.newInstance(subTitle, dimensionOf));
                break;
            case 2: //空间
                mActivity.startFragment(SpaceAnalysisFragment.newInstance(subTitle, dimensionOf));
                break;
            case 3: //渠道
                mActivity.startFragment(ChannelAnalysisFragment.newInstance(subTitle, dimensionOf));
                break;
            case 4: //系列
                mActivity.startFragment(SeriesAnalysisFragment.newInstance(subTitle, dimensionOf));
                break;
            case 5: //花色
                mActivity.startFragment(ColorsAnalysisFragment.newInstance(subTitle, dimensionOf));
                break;
            case 6: //宅配
                mActivity.startFragment(HomeDeliveryAnalysisFragment.newInstance(subTitle, dimensionOf));
                break;
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:  //完成率及同比
                mActivity.startFragment(MultiCompleteRateFragment.newInstance(subTitle, dimensionOf));
                break;
        }
    }
}
