package com.holike.crm.fragment.report;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.holike.crm.R;
import com.holike.crm.adapter.AbsFormAdapter;
import com.holike.crm.base.BaseFragment;
import com.holike.crm.bean.HomeDollBean;

import java.util.List;

/**
 * Created by pony on 2019/11/2.
 * Version v3.0 app报表
 * 家装渠道
 */
class HomeDollHelper extends GeneralReportHelper {

    private Callback mCallback;
    private String mType, mCityCode;
    private Handler mHandler;

    HomeDollHelper(BaseFragment<?, ?> fragment, Callback callback) {
        super(fragment);
        mCallback = callback;
        fragment.setTitle(R.string.title_home_doll);
        Bundle bundle = getBundle();
        initParams(bundle);
        if (bundle == null) {
            doRequest();
        } else {
            mHandler = new Handler();
            mHandler.postDelayed(this::doRequest, 300);
        }
    }

    private void initParams(Bundle bundle) {
        if (bundle != null) {
            mType = bundle.getString("type");
            mCityCode = bundle.getString("cityCode");
        }
    }

    void doRequest() {
        mCallback.onRequest(mType, mCityCode);
    }

    void onSuccess(HomeDollBean bean) {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.include_home_doll_content, getContainer(), true);
        ((TextView) contentView.findViewById(R.id.tv_time_detail)).setText(bean.dateToDate);  //显示时间段
        RecyclerView recyclerView = contentView.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(new FormDataAdapter(mContext, bean.getDatas()));
    }

    private final class FormDataAdapter extends AbsFormAdapter<HomeDollBean.DataBean> {

        FormDataAdapter(Context context, List<HomeDollBean.DataBean> dataList) {
            super(context, dataList);
        }

        @Override
        protected void bindViewHolder(RecyclerHolder holder, HomeDollBean.DataBean bean, int position) {
            holder.setText(R.id.tv_division, bean.division); //划分
            holder.setText(R.id.tv_principal, bean.principal); //负责人
            holder.setText(R.id.tv_month_performance, bean.forMonth);//本月业绩
            holder.setText(R.id.tv_annual_performance, bean.forYear); //全年业绩
            setClickableCell(holder, position, R.id.tv_division, bean.isClickable(), view -> startNextLevel(bean.type, bean.cityCode));
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_home_doll_form_content;
        }
    }

    private void startNextLevel(String type, String cityCode) {
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        bundle.putString("cityCode", cityCode);
        mFragment.startFragment(new HomeDollFragment(), bundle, true);
    }

    void detach() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    interface Callback {
        void onRequest(String type, String cityCode);
    }
}
