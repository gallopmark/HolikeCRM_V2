package com.holike.crm.fragment.report.multiple;


import com.holike.crm.R;
import com.holike.crm.base.BaseFragment;
import com.holike.crm.bean.PerformanceAnalysisBean;

/**
 * Created by pony on 2019/11/7.
 * Version v3.0 app报表
 */
class SpaceAnalysisHelper extends AbsAnalysisHelper {

    SpaceAnalysisHelper(BaseFragment<?, ?> fragment, Callback callback) {
        super(fragment, callback);
        siteTitle(R.string.title_performance_analysis, R.string.title_space_performance_analysis);
    }

    @Override
    String getDefaultDimensionOf() {
        return "2";
    }

    @Override
    protected int getTitleId() {
        return R.string.title_space_performance_analysis;
    }

    void onSuccess(PerformanceAnalysisBean bean) {
        multipleUpdate(bean, mContext.getString(R.string.space));
    }
}
