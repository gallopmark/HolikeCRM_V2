package com.holike.crm.fragment.report.multiple;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gallopmark.recycler.adapterhelper.BaseRecyclerAdapter;
import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.adapter.AbsFormAdapter;
import com.holike.crm.base.BaseFragment;
import com.holike.crm.bean.PerformanceAnalysisBean;
import com.holike.crm.dialog.LevelAreaDialog;
import com.holike.crm.fragment.report.AbsReportHelper;

import java.util.Date;
import java.util.List;

abstract class AbsAnalysisHelper extends AbsReportHelper {

    String mDimensionOf;
    String mDimensionOfCli;
    private int mSelectDimensionOfCli; //选择的tab位置
    private LevelAreaDialog mAreaDialog;
    private String mSelectRegion;  //选择后的区域名称
    private Callback mCallback;

    AbsAnalysisHelper(BaseFragment<?, ?> fragment, Callback callback) {
        super(fragment);
        mCallback = callback;
        Bundle bundle = getBundle();
        boolean isEntry = false;
        if (bundle != null) {
            isEntry = bundle.getBoolean("entry");
            mDimensionOf = bundle.getString("dimensionOf", getDefaultDimensionOf());
            mDimensionOfCli = bundle.getString("dimensionOfCli");
        } else {
            mDimensionOf = getDefaultDimensionOf();
        }
        if (isEntry) {
            doRequest();
        } else {
            requestByDelay();
        }
    }


    void siteTitle(int suffixTitleId, int defaultTitleId) {
        String subTitle = getStringValue("subTitle");
        if (!TextUtils.isEmpty(subTitle)) {
            mFragment.setTitle(subTitle + mContext.getString(suffixTitleId));
        } else {
            mFragment.setTitle(defaultTitleId);
        }
    }

    @Override
    protected boolean withSubTitle() {
        return false;
    }

    abstract String getDefaultDimensionOf();

    @Override
    public void doRequest() {
        mCallback.onRequest(mTime, mDimensionOf, mDimensionOfCli, mType, mCityCode, mStartDate, mEndDate);
    }

    abstract void onSuccess(PerformanceAnalysisBean bean);

    //空间、系列、花色通用
    void multipleUpdate(PerformanceAnalysisBean bean, String name) {
        View contentView = inflate();
        ((TextView) contentView.findViewById(R.id.tv_time_detail)).setText(bean.dateToDate);
        addAreaView(contentView);
        RecyclerView rvTab = addSelectableTab(contentView);
        final MultipleTabAdapter tabAdapter = new MultipleTabAdapter(mContext, bean.getDimensionOfCliList(), mSelectDimensionOfCli);
        rvTab.setAdapter(tabAdapter);
        View formView = LayoutInflater.from(mContext).inflate(R.layout.include_performance_analysis_multi_content, contentView.findViewById(R.id.ll_analysis_content), true);
        ((TextView) formView.findViewById(R.id.tv_name)).setText(name);
        RecyclerView rvForm = formView.findViewById(R.id.rv_form);
        rvForm.setAdapter(new MultipleFormAdapter(mContext, bean.getAnalysisData()));
    }

    private final class MultipleTabAdapter extends CommonAdapter<PerformanceAnalysisBean.DimensionOfCliBean> {

        private int selectPosition;

        MultipleTabAdapter(Context context, List<PerformanceAnalysisBean.DimensionOfCliBean> dataList, int selectPosition) {
            super(context, dataList);
            this.selectPosition = selectPosition;
        }

        void setSelectPosition(int position) {
            mSelectDimensionOfCli = position;
            this.selectPosition = position;
            notifyDataSetChanged();
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_form_data_tabhost;
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, PerformanceAnalysisBean.DimensionOfCliBean bean, int position) {
            if (selectPosition == position) {
                holder.setTextColorRes(R.id.tv_role, R.color.color_while);
                holder.itemView.setBackgroundResource(R.drawable.bg_corners5dp_top_coloraccent);
            } else {
                holder.setTextColorRes(R.id.tv_role, R.color.textColor5);
                holder.itemView.setBackgroundResource(R.drawable.bg_corners5dp_top_bg);
            }
            holder.setText(R.id.tv_role, bean.name);
            holder.itemView.setOnClickListener(view -> {
                mDimensionOfCli = bean.type;
                setSelectPosition(position);
                doRequest();
            });
        }
    }

    private final class MultipleFormAdapter extends AbsFormAdapter<PerformanceAnalysisBean.AnalysisDataBean> {
        MultipleFormAdapter(Context context, List<PerformanceAnalysisBean.AnalysisDataBean> dataList) {
            super(context, dataList);
        }

        @Override
        protected void bindViewHolder(BaseRecyclerAdapter.RecyclerHolder holder, PerformanceAnalysisBean.AnalysisDataBean bean, int position) {
            holder.setText(R.id.tv_ranking, bean.ranking);
            holder.setText(R.id.tv_name, bean.name);
            holder.setText(R.id.tv_performance, bean.performance);
            holder.setText(R.id.tv_proportion, bean.proportion);
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_performance_analysis_multi_form_content;
        }
    }

    View inflate() {
        return LayoutInflater.from(mContext).inflate(R.layout.include_performance_analysis_content, getContainer(), true);
    }

    void addAreaView(View contentView) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.include_select_region_view, contentView.findViewById(R.id.ll_analysis_content), true);
        final TextView tvSelectRegion = view.findViewById(R.id.tv_select_region);
        tvSelectRegion.setText(TextUtils.isEmpty(mSelectRegion) ? mContext.getString(R.string.select_area) : mSelectRegion);
        tvSelectRegion.setOnClickListener(v -> onSelectRegion(tvSelectRegion));
    }

    private RecyclerView addSelectableTab(View contentView) {
        RecyclerView recyclerView = new RecyclerView(mContext);
        recyclerView.setClipToPadding(false);
        recyclerView.setPadding(0, 0, mContext.getResources().getDimensionPixelSize(R.dimen.dp_12), 0);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        recyclerView.setLayoutParams(params);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
        ((LinearLayout) contentView.findViewById(R.id.ll_analysis_content)).addView(recyclerView);
        return recyclerView;
    }

    private void onSelectRegion(final TextView tv) {
        if (mAreaDialog == null) {
            mAreaDialog = new LevelAreaDialog(mContext).setRequired(true).setOnAreaSelectListener(new LevelAreaDialog.OnAreaSelectListener() {
                @Override
                public void onAreaSelected(String name, String type, String cityCode, boolean isSelectDealer) {
                    mSelectRegion = name;
                    tv.setText(name);
                    mType = type;
                    mCityCode = cityCode;
                    doRequest();
                }

                @Override
                public void onDismissDealer() {
                    mFragment.showShortToast(R.string.please_select_dealer);
                }
            });
        }
        mAreaDialog.show();
    }

    interface Callback {
        void onRequest(String time, String dimensionOf, String dimensionOfCli,
                       String type, String cityCode, Date startDate, Date endDate);
    }

    @Override
    public void detach() {
        super.detach();
        if (mAreaDialog != null) {
            mAreaDialog.dismiss();
        }
    }
}
