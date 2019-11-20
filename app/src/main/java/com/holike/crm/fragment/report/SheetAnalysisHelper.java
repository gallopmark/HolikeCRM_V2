package com.holike.crm.fragment.report;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.holike.crm.R;
import com.holike.crm.adapter.AbsFormAdapter;
import com.holike.crm.base.BaseFragment;
import com.holike.crm.bean.SheetAnalysisBean;
import com.holike.crm.dialog.PlainTextTipsDialog;
import com.holike.crm.helper.TextSpanHelper;

import java.util.Date;
import java.util.List;

/**
 * Created by pony on 2019/11/2.
 * Version v3.0 app报表
 * 板材分析
 */
class SheetAnalysisHelper extends AbsReportHelper {

    private Callback mCallback;

    SheetAnalysisHelper(BaseFragment<?, ?> fragment, Callback callback) {
        super(fragment);
        mCallback = callback;
        mFragment.setTitle(R.string.title_sheet_analysis);
        if (getBooleanValue("entry")) {
            doRequest();
        } else {
            requestByDelay();
        }
    }

    @Override
    protected int getTitleId() {
        return R.string.title_sheet_analysis;
    }

    @Override
    public void doRequest() {
        mCallback.onRequest(mTime, mType, mCityCode, mStartDate, mEndDate);
    }

    void onSuccess(SheetAnalysisBean bean) {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.include_sheetanalysis_content, getContainer(), true);
        ((TextView) contentView.findViewById(R.id.tv_time_detail)).setText(bean.dateToDate); //显示时间段
        contentView.findViewById(R.id.tv_question_mark).setOnClickListener(view -> showDescription());
        TextSpanHelper.setSquareText(contentView.findViewById(R.id.tv_particle));
        TextSpanHelper.setSquareText(contentView.findViewById(R.id.tv_original));
        TextSpanHelper.setSquareText(contentView.findViewById(R.id.tv_originalK));
        RecyclerView recyclerView = contentView.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(new FormDataAdapter(mContext, bean.getDatas()));
    }

    /*显示数据说明*/
    private void showDescription() {
        new PlainTextTipsDialog(mContext).setData(R.array.sheet_analysis_tips).show();
    }

    private final class FormDataAdapter extends AbsFormAdapter<SheetAnalysisBean.DataBean> {
        private int mMaxTextWidth;

        FormDataAdapter(Context context, List<SheetAnalysisBean.DataBean> dataList) {
            super(context, dataList);
            int dp10 = context.getResources().getDimensionPixelSize(R.dimen.dp_10);
            int dp2 = context.getResources().getDimensionPixelSize(R.dimen.dp_2);
            int sw = context.getResources().getDisplayMetrics().widthPixels - dp10 * 2;
            mMaxTextWidth = (int) (sw * ((84f - dp2 * 2) / 353f));
        }

        @Override
        protected void bindViewHolder(RecyclerHolder holder, SheetAnalysisBean.DataBean bean, int position) {
            holder.setText(R.id.tv_division, bean.division); //划分
            holder.setText(R.id.tv_principal, bean.principal); //负责人
            holder.setText(R.id.tv_particle, bean.granule);//颗粒板
            /* 原态板*/
            TextView tvOriginal = holder.obtainView(R.id.tv_original);
            if (getTextWidth(tvOriginal.getPaint(), bean.original) > mMaxTextWidth) {
                tvOriginal.setText(bean.getShowOriginal());
            } else {
                tvOriginal.setText(bean.original);
            }
            /*原态K板*/
            TextView tvOriginalK = holder.obtainView(R.id.tv_originalK);
            if (getTextWidth(tvOriginalK.getPaint(), bean.originalK) > mMaxTextWidth) {
                tvOriginalK.setText(bean.getShowOriginalK());
            } else {
                tvOriginalK.setText(bean.originalK);
            }
            setClickableCell(holder, position, R.id.tv_division, bean.isClickable(), view -> startNextLevel(bean.type, bean.cityCode));
        }

        private float getTextWidth(Paint paint, String text) {
            if (TextUtils.isEmpty(text)) return 0;
            return paint.measureText(text);
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_sheetanalysis_form_content;
        }
    }

    private void startNextLevel(String type, String cityCode) {
        Bundle bundle = new Bundle();
        bundle.putString("time", mTime);
        bundle.putString("type", type);
        bundle.putString("cityCode", cityCode);
        bundle.putSerializable("startDate", mStartDate);
        bundle.putSerializable("endDate", mEndDate);
        mFragment.startFragment(new SheetAnalysisFragment(), bundle, true);
    }

    interface Callback {
        void onRequest(String time, String type, String cityCode, Date startDate, Date endDate);
    }
}
