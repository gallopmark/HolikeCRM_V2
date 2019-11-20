package com.holike.crm.fragment.report.multiple;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.holike.crm.R;
import com.holike.crm.adapter.AbsFormAdapter;
import com.holike.crm.base.BaseFragment;
import com.holike.crm.bean.PerformanceAnalysisBean;
import com.holike.crm.helper.FormDataHelper;
import com.holike.crm.util.ParseUtils;

import java.util.List;

/**
 * Created by pony on 2019/11/4.
 * Version v3.0 app报表
 * 业绩完成率及同比
 */
class MultiCompleteRateHelper extends AbsAnalysisHelper {
    private String mSubTitle;

    MultiCompleteRateHelper(BaseFragment<?, ?> fragment, Callback callback) {
        super(fragment, callback);
        mSubTitle = getStringValue("subTitle");
        siteTitle(R.string.title_complete_rate, getDefaultTitleId());
    }

    private int getDefaultTitleId() {
        int type = ParseUtils.parseInt(mDimensionOf);
        if (type == 7) {
            return R.string.title_performance_complete_rate;
        } else if (type == 8) {
            return R.string.title_custom_complete_rate;
        } else if (type == 9) {
            return R.string.title_cupboard_complete_rate;
        } else if (type == 10) {
            return R.string.title_woodendoor_complete_rate;
        } else if (type == 11) {
            return R.string.title_product_complete_rate;
        } else if (type == 12) {
            return R.string.title_bighome_complete_rate;
        }
        return R.string.title_complete_rate;
    }

    @Override
    String getDefaultDimensionOf() {
        return "";
    }

    @Override
    protected int getTitleId() {
        return R.string.title_complete_rate;
    }

    void onSuccess(PerformanceAnalysisBean bean) {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.include_performance_completerate_content, getContainer(), true);
        ((TextView) contentView.findViewById(R.id.tv_time_detail)).setText(bean.dateToDate); //显示时间段
        ViewStub viewStub = contentView.findViewById(R.id.viewStub);
        FormDataHelper.attachView(viewStub.inflate(), new FormDataHelper.FormDataBinder() {
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
                return R.layout.include_performance_completerate_firstrow;
            }

            @Override
            public RecyclerView.Adapter bindSideAdapter() {
                return new FormDataAdapter(mContext, bean.getCompleteRateData(), true);
            }

            @Override
            public RecyclerView.Adapter bindContentAdapter() {
                return new FormDataAdapter(mContext, bean.getCompleteRateData(), false);
            }
        }, new FormDataHelper.SimpleFormDataCallback() {
            @Override
            public void bindFirstColumn(View view) {
                ((TextView) view.findViewById(R.id.tv_second_name)).setText(mContext.getString(R.string.principal));
            }
        });
    }


    private final class FormDataAdapter extends AbsFormAdapter<PerformanceAnalysisBean.CompleteRateDataBean> {

        private boolean mSide;
        private int mLayoutResourceId;

        FormDataAdapter(Context context, List<PerformanceAnalysisBean.CompleteRateDataBean> dataList, boolean isSide) {
            super(context, dataList);
            this.mSide = isSide;
            mLayoutResourceId = mSide ? R.layout.item_performance_completerate_form_side : R.layout.item_performance_completerate_form_content;
        }

        @Override
        protected void bindViewHolder(RecyclerHolder holder, PerformanceAnalysisBean.CompleteRateDataBean bean, int position) {
            holder.setText(R.id.tv_division, bean.division); //划分
            holder.setText(R.id.tv_principal, bean.principal); //负责人
            holder.setText(R.id.tv_target, bean.target); //目标
            holder.setText(R.id.tv_complete, bean.complete); //已完成
            holder.setText(R.id.tv_complete_rate, bean.completeRate); //完成率
            holder.setText(R.id.tv_year_on_year, bean.yoy); //同比
            holder.setText(R.id.tv_ranking, bean.ranking); //排名
            holder.setText(R.id.tv_today, bean.completeDay); //今日
            if (bean.isClickable()) {
                holder.setTextColorRes(R.id.tv_division, R.color.colorAccent);
            } else {
                if (position == 0) {
                    holder.setTextColorRes(R.id.tv_division, R.color.textColor4);
                } else {
                    holder.setTextColorRes(R.id.tv_division, R.color.textColor8);
                }
            }
            holder.setEnabled(R.id.tv_division, mSide && bean.isClickable());
            holder.setOnClickListener(R.id.tv_division, view -> startNextLevel(bean.type, bean.cityCode));
        }

        @Override
        protected int bindView(int viewType) {
            return mLayoutResourceId;
        }
    }

    private void startNextLevel(String type, String cityCode) {
        Bundle bundle = new Bundle();
        bundle.putString("subTitle", mSubTitle);
        bundle.putString("time", mTime);
        bundle.putString("dimensionOf", mDimensionOf);
        bundle.putString("dimensionOfCli", mDimensionOfCli);
        bundle.putString("type", type);
        bundle.putString("cityCode", cityCode);
        bundle.putSerializable("startDate", mStartDate);
        bundle.putSerializable("endDate", mEndDate);
        mFragment.startFragment(new MultiCompleteRateFragment(), bundle, true);
    }
}
