package com.holike.crm.fragment.report.business;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.holike.crm.R;
import com.holike.crm.adapter.AbsFormAdapter;
import com.holike.crm.base.BaseFragment;
import com.holike.crm.bean.BusinessReferenceTypeBean;

import java.util.List;

class HomeDeliveryHelper extends DealerMultiPerformanceHelper {

    HomeDeliveryHelper(BaseFragment<?, ?> fragment, Callback callback) {
        super(fragment, callback);
    }

    @Override
    String getDimensionOf() {
        return null;
    }

    @Override
    int getFirstRowLayoutResource() {
        return R.layout.include_firstrow_homedelivery;
    }

    @Override
    void setFormData(RecyclerView rvContent, List<BusinessReferenceTypeBean.DataBean> dataList) {
        rvContent.setAdapter(new FormDataAdapter(mContext, dataList));
    }

    private final class FormDataAdapter extends AbsFormAdapter<BusinessReferenceTypeBean.DataBean> {

        FormDataAdapter(Context context, List<BusinessReferenceTypeBean.DataBean> dataList) {
            super(context, dataList);
        }

        @Override
        protected void bindViewHolder(RecyclerHolder holder, BusinessReferenceTypeBean.DataBean bean, int position) {
            holder.setText(R.id.tv_ranking, bean.ranking);
//            holder.setText(R.id.tv_category, bean.category);
            holder.setText(R.id.tv_performance, bean.performance);
            holder.setText(R.id.tv_proportion, bean.proportion);
//            holder.setText(R.id.tv_sales_volume, bean.salesVolume);
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_homedelivery_performance;
        }
    }
}
