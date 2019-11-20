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
import com.holike.crm.helper.FormDataHelper;

import java.util.List;

/**
 * Created by pony on 2019/11/7.
 * Version v3.0 app报表
 */
class CategoryAnalysisHelper extends AbsAnalysisHelper {

    CategoryAnalysisHelper(BaseFragment<?, ?> fragment, Callback callback) {
        super(fragment, callback);
        siteTitle(R.string.title_performance_analysis, R.string.title_category_performance_analysis);
    }

    @Override
    String getDefaultDimensionOf() {
        return "1";
    }

    @Override
    protected int getTitleId() {
        return R.string.title_category_performance_analysis;
    }

    void onSuccess(final PerformanceAnalysisBean bean) {
        View contentView = inflate();
        ((TextView) contentView.findViewById(R.id.tv_time_detail)).setText(bean.dateToDate);
        View formView = LayoutInflater.from(mContext).inflate(R.layout.include_form_data_content, contentView.findViewById(R.id.ll_analysis_content), true);
        FormDataHelper.attachView(formView, new FormDataHelper.FormDataBinder() {
            @Override
            public int bindFirstColumnLayoutRes() {
                return R.layout.include_form_data_column_special_h44;
            }

            @Override
            public CharSequence bindSideTitle() {
                return mContext.getString(R.string.division);
            }

            @Override
            public int bindFirstRowLayoutRes() {
                return R.layout.include_performance_analysis_category_firstrow;
            }

            @Override
            public RecyclerView.Adapter bindSideAdapter() {
                return new SimpleFormAdapter(mContext, bean.getCategoryData(), true);
            }

            @Override
            public RecyclerView.Adapter bindContentAdapter() {
                return new SimpleFormAdapter(mContext, bean.getCategoryData(), false);
            }
        }, new FormDataHelper.SimpleFormDataCallback() {
            @Override
            public void bindFirstColumn(View view) {
                ((TextView) view.findViewById(R.id.tv_second_name)).setText(mContext.getString(R.string.principal));
            }
        });
    }

    private final class SimpleFormAdapter extends AbsFormAdapter<PerformanceAnalysisBean.CategoryDataBean> {
        boolean isSide;
        private int mLayoutResourceId;

        SimpleFormAdapter(Context context, List<PerformanceAnalysisBean.CategoryDataBean> dataList, boolean isSide) {
            super(context, dataList);
            this.isSide = isSide;
            if (this.isSide) {
                mLayoutResourceId = R.layout.item_performance_analysis_category_form_side;
            } else {
                mLayoutResourceId = R.layout.item_performance_analysis_category_form_content;
            }
        }

        @Override
        protected void bindViewHolder(RecyclerHolder holder, PerformanceAnalysisBean.CategoryDataBean bean, int position) {
            holder.setText(R.id.tv_division, bean.division);  //划分
            holder.setText(R.id.tv_principal, bean.principal);  //负责人
            holder.setText(R.id.tv_total_performance, bean.total);  //总业绩
            holder.setText(R.id.tv_custom_ratio, bean.customize);  //定制
            holder.setText(R.id.tv_cupboard, bean.cupboard);  //橱柜
            holder.setText(R.id.tv_woodendoor_ratio, bean.woodenDoor);  //木门占比
            holder.setText(R.id.tv_product_ratio, bean.finished);  //成品占比
            holder.setText(R.id.tv_curtain_ratio, bean.curtain);  //窗帘占比
            holder.setText(R.id.tv_bighome_ratio, bean.bigHouse);  //大家居占比
            setClickableCell(holder, position, R.id.tv_division, isSide && bean.isClickable(), view -> startNextLevel(bean.type, bean.cityCode));
        }

        @Override
        protected int bindView(int viewType) {
            return mLayoutResourceId;
        }
    }

    private void startNextLevel(String type, String cityCode) {
        Bundle bundle = new Bundle();
        bundle.putString("time", mTime);
        bundle.putString("type", type);
        bundle.putString("cityCode", cityCode);
        bundle.putSerializable("startDate", mStartDate);
        bundle.putSerializable("endDate", mEndDate);
        mFragment.startFragment(new CategoryAnalysisFragment(), bundle, true);
    }
}
