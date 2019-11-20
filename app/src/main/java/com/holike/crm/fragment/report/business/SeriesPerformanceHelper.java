package com.holike.crm.fragment.report.business;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.holike.crm.R;
import com.holike.crm.adapter.AbsFormAdapter;
import com.holike.crm.base.BaseFragment;
import com.holike.crm.bean.BusinessReferenceTypeBean;

import java.util.List;

class SeriesPerformanceHelper extends DealerMultiPerformanceHelper {

    SeriesPerformanceHelper(BaseFragment<?, ?> fragment, Callback callback) {
        super(fragment, callback);
    }

    @Override
    String getDimensionOf() {
        return "1";
    }

    @Override
    int getFirstRowLayoutResource() {
        return R.layout.include_firstrow_seriesperformance;
    }

    @Override
    void setFormData(RecyclerView rvContent, List<BusinessReferenceTypeBean.DataBean> dataList) {
        rvContent.setAdapter(new AbsAdapter(mContext, dataList));
    }

    private final class AbsAdapter extends AbsFormAdapter<BusinessReferenceTypeBean.DataBean> {

        AbsAdapter(Context context, List<BusinessReferenceTypeBean.DataBean> dataList) {
            super(context, dataList);
        }

        @Override
        protected void bindViewHolder(RecyclerHolder holder, BusinessReferenceTypeBean.DataBean bean, int position) {
            holder.setText(R.id.tv_ranking, bean.ranking);
            holder.setText(R.id.tv_series, bean.name);
            holder.setText(R.id.tv_performance, bean.performance);
            holder.setText(R.id.tv_proportion, bean.proportion);
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_seriesperformance;
        }
    }
}
