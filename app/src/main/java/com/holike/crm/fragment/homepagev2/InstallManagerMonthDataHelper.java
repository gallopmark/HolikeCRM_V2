package com.holike.crm.fragment.homepagev2;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.base.BaseFragment;
import com.holike.crm.bean.MonthDataInstallManagerBean;
import com.holike.crm.helper.TextSpanHelper;

import java.util.List;

/**
 * Created by gallop on 2019/8/9.
 * Copyright holike possess 2019.
 * 安装经理本月数据帮助类
 */
class InstallManagerMonthDataHelper extends MonthDataHelper {

    private ViewStub mContentViewStub;
    private View mFragmentView;
    private View mContentView;

    InstallManagerMonthDataHelper(BaseFragment<?, ?> fragment, Callback callback) {
        super((BaseActivity<?, ?>) fragment.getActivity(), callback);
        mFragmentView = fragment.getContentView();
        Bundle bundle = fragment.getArguments();
        if (bundle != null) {
            mType = bundle.getString("type");
            mCityCode = bundle.getString("cityCode");
        }
        mContentViewStub = mFragmentView.findViewById(R.id.vs_content);
        onQuery();
    }

    void onSuccess(MonthDataInstallManagerBean bean) {
        if (mContentView == null) {
            mContentView = mContentViewStub.inflate();
        }
        FrameLayout contentLayout = mContentView.findViewById(R.id.fl_month_data_content);
        if (contentLayout.getChildCount() >= 2) {
            contentLayout.removeViewAt(1);
        }
        View firstLayout = LayoutInflater.from(mActivity).inflate(R.layout.include_month_data_layout_first70dp, contentLayout, false);
        TextView tvFirst = firstLayout.findViewById(R.id.tv_first_name);
        tvFirst.setText(mActivity.getString(R.string.name));
        contentLayout.addView(firstLayout, 1);  //动态添加layout 第一列数据展示布局
        LinearLayout scrollableLayout = mContentView.findViewById(R.id.ll_scrollable_content);
        if (scrollableLayout.getChildCount() >= 2) {
            scrollableLayout.removeViewAt(0);
        }
        View view = LayoutInflater.from(mActivity).inflate(R.layout.include_monthdata_installmanager_tablist, scrollableLayout, false);
        TextView tvInstallArea = view.findViewById(R.id.tv_install_area);
        tvInstallArea.setText(TextSpanHelper.getSquareMeter(tvInstallArea.getText().toString()));
        scrollableLayout.addView(view, 0);
        RecyclerView sideRecyclerView = mContentView.findViewById(R.id.rv_side);
        RecyclerView contentRecyclerView = mFragmentView.findViewById(R.id.rv_content);
        sideRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        contentRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        contentRecyclerView.setNestedScrollingEnabled(false);
        setScrollSynchronous(sideRecyclerView, contentRecyclerView);
        setScrollSynchronous(contentRecyclerView, sideRecyclerView);
        sideRecyclerView.setAdapter(new SideAdapter(mActivity, bean.getArr()));
        ContentAdapter adapter = new ContentAdapter(mActivity, bean.getArr());
        contentRecyclerView.setAdapter(adapter);
    }

    class SideAdapter extends CommonAdapter<MonthDataInstallManagerBean.ArrBean> {

        SideAdapter(Context context, List<MonthDataInstallManagerBean.ArrBean> mDatas) {
            super(context, mDatas);
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_month_data_installmanager_side;
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, MonthDataInstallManagerBean.ArrBean bean, int position) {
            if (position % 2 == 0) {
                holder.itemView.setBackgroundResource(R.color.textColor24);
            } else {
                holder.itemView.setBackgroundResource(R.color.color_while);
            }
            holder.setText(R.id.tv_name, bean.name);
            holder.setText(R.id.tv_install_count, bean.installed); //安装数
            holder.setText(R.id.tv_install_area, bean.area);
            holder.setText(R.id.tv_completion_rate, bean.firstSuccess);//一次安装完成率
            holder.setText(R.id.tv_satisfaction, bean.Satisfied); //客户满意度
        }
    }

    static class ContentAdapter extends CommonAdapter<MonthDataInstallManagerBean.ArrBean> {


        ContentAdapter(Context context, List<MonthDataInstallManagerBean.ArrBean> mDatas) {
            super(context, mDatas);
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, MonthDataInstallManagerBean.ArrBean bean, int position) {
            if (position % 2 == 0) {
                holder.itemView.setBackgroundResource(R.color.textColor24);
            } else {
                holder.itemView.setBackgroundResource(R.color.color_while);
            }
            holder.setText(R.id.tv_name, bean.name);
            holder.setText(R.id.tv_install_count, bean.installed); //安装数
            holder.setText(R.id.tv_install_area, bean.area);
            holder.setText(R.id.tv_completion_rate, bean.firstSuccess);//一次安装完成率
            holder.setText(R.id.tv_satisfaction, bean.Satisfied); //客户满意度
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_month_data_installmanager_content;
        }
    }
}
