package com.holike.crm.fragment.report.multiple;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.holike.crm.R;
import com.holike.crm.adapter.AbsFormAdapter;
import com.holike.crm.base.BaseFragment;
import com.holike.crm.bean.PerformanceAnalysisBean;

import java.util.List;

/**
 * Created by pony on 2019/11/7.
 * Version v3.0 app报表
 * 宅配业绩分析
 */
class HomeDeliveryAnalysisHelper extends AbsAnalysisHelper {

    HomeDeliveryAnalysisHelper(BaseFragment<?, ?> fragment, Callback callback) {
        super(fragment, callback);
        siteTitle(R.string.title_performance_analysis, R.string.title_homedelivery_performance_analysis);
    }

    @Override
    protected int getTitleId() {
        return R.string.title_homedelivery_performance_analysis;
    }

    @Override
    String getDefaultDimensionOf() {
        return "6";
    }

    void onSuccess(PerformanceAnalysisBean bean) {
        View contentView = inflate();
        ((TextView) contentView.findViewById(R.id.tv_time_detail)).setText(bean.dateToDate);
        addAreaView(contentView);
        View formView = LayoutInflater.from(mContext).inflate(R.layout.include_performance_analysis_homedelivery_content, contentView.findViewById(R.id.ll_analysis_content), true);
        RecyclerView rvForm = formView.findViewById(R.id.rv_form);
        rvForm.setAdapter(new SimpleFormAdapter(mContext, bean.getHouseData()));
    }

    private final class SimpleFormAdapter extends AbsFormAdapter<PerformanceAnalysisBean.HouseDataBean> {

        SimpleFormAdapter(Context context, List<PerformanceAnalysisBean.HouseDataBean> dataList) {
            super(context, dataList);
        }

        @Override
        protected void bindViewHolder(RecyclerHolder holder, PerformanceAnalysisBean.HouseDataBean bean, int position) {
            holder.setText(R.id.tv_ranking, bean.ranking); //排名
            holder.setText(R.id.tv_category, bean.category); //类别
            holder.setText(R.id.tv_performance, bean.performance); //业绩
            holder.setText(R.id.tv_proportion, bean.performanceRate); //占比
            holder.setText(R.id.tv_sales_count, bean.quantity); //销售数量
            setClickableCell(holder, position, R.id.tv_category, bean.isClickable(), view -> startNextLevel(bean.category));
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_performance_analysis_homedelivery_form_content;
        }
    }

    private void startNextLevel(String category) {
        Bundle bundle = new Bundle();
        bundle.putString("time", mTime);
        bundle.putSerializable("startDate", mStartDate);
        bundle.putSerializable("endDate", mEndDate);
        bundle.putString("dimensionOfCli", category);
        mFragment.startFragment(new HomeDeliveryAnalysisFragment(), bundle, true);
    }
}
