package com.holike.crm.fragment.analyze;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.grallopmark.tablayout.SlidingTabLayout;
import com.grallopmark.tablayout.listener.OnTabSelectListener;
import com.holike.crm.R;
import com.holike.crm.base.IntentValue;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.WoodenDoorBean;
import com.holike.crm.presenter.activity.WoodenDoorPresenter;
import com.holike.crm.util.Constants;
import com.holike.crm.view.activity.WoodenDoorView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by gallop on 2019/8/28.
 * Copyright holike possess 2019.
 * 木门业绩报表（非经销商）
 */
public class WoodenDoorDefaultFragment extends MyFragment<WoodenDoorPresenter, WoodenDoorView> implements WoodenDoorView {

    public static WoodenDoorDefaultFragment newInstance(WoodenDoorBean bean) {
        IntentValue.getInstance().put("woodenDoorBean", bean);
        return new WoodenDoorDefaultFragment();
    }

    @BindView(R.id.ll_content_layout)
    LinearLayout mContentLayout;
    @BindView(R.id.sliding_tab_layout)
    SlidingTabLayout mTabLayout;
    @BindView(R.id.tv_date)
    TextView mDateTextView;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;

    private String mCityCode;
    private String mType;
    private String mTime;
    private String mTitle;
    private List<String> mTabTitles;

    @Override
    protected WoodenDoorPresenter attachPresenter() {
        return new WoodenDoorPresenter();
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_woodendoor_personal;
    }

    @Override
    protected void init() {
        super.init();
        setStatusBar(R.color.bg_state_bar);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        Bundle bundle = getArguments();
        if (bundle != null) {
            mCityCode = bundle.getString(Constants.CITY_CODE);
            mTime = bundle.getString(Constants.TIME);
            mType = bundle.getString(Constants.TYPE);
            mTitle = bundle.getString(Constants.TITLE);
            getData();
        } else {
            WoodenDoorBean bean = (WoodenDoorBean) IntentValue.getInstance().removeBy("woodenDoorBean");
            onSuccess(bean);
        }
        setTitle(getString(R.string.report_item18_title) + (TextUtils.isEmpty(mTitle) ? "" : "—" + mTitle));
    }

    private void getData() {
        showLoading();
        mPresenter.getData(mCityCode, mTime, mType);
    }

    @Override
    public void onSuccess(WoodenDoorBean bean) {
        dismissLoading();
        if (mContentLayout.getVisibility() != View.VISIBLE) {
            mContentLayout.setVisibility(View.VISIBLE);
        }
        initTab(bean.getSelectData());
        mDateTextView.setText(bean.timeData);
        setAdapter(bean.getPercentData());
    }

    /**
     * 切换导航
     */
    private void initTab(final List<WoodenDoorBean.SelectDataBean> selectDataBeans) {
        if (mTabTitles == null) {
            mTabTitles = new ArrayList<>();
            for (WoodenDoorBean.SelectDataBean bean : selectDataBeans) {
                mTabTitles.add(bean.name);
            }
            mTabLayout.setupViewPager(mViewpager, mTabTitles.toArray(new String[0]));
            mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
                @Override
                public void onTabSelect(int position) {
                    mTime = selectDataBeans.get(position).selectTime;
                    getData();
                }

                @Override
                public void onTabReselect(int position) {
                }
            });
            mTabLayout.setCurrentTab(mPresenter.getSelectPosition(mTime, selectDataBeans));
        }
    }

    private void setAdapter(List<WoodenDoorBean.PercentDataBean> list) {
        mRecyclerView.setAdapter(new CommonAdapter<WoodenDoorBean.PercentDataBean>(mContext, list) {
            @Override
            protected int bindView(int viewType) {
                return R.layout.item_rv_woodendoor_report;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, WoodenDoorBean.PercentDataBean bean, int position) {
                holder.itemView.setBackgroundResource(position % 2 == 0 ? R.color.bg_item_order_report : R.color.color_while);
                holder.setText(R.id.tv_area, bean.area);
                holder.setText(R.id.tv_name, bean.name);
                holder.setText(R.id.tv_target, bean.countsTarget);
                holder.setText(R.id.tv_complete, bean.countsComplete);
                holder.setText(R.id.tv_complete_percent, bean.percentComplete);
                holder.setText(R.id.tv_rank, bean.rank);
                holder.setTextColorRes(R.id.tv_area, bean.isClick() ? R.color.textColor5 : R.color.textColor8);
                holder.setEnabled(R.id.tv_area, bean.isClick());
                holder.setOnClickListener(R.id.tv_area, view -> {
                    Map<String, Serializable> params = new HashMap<>();
                    params.put(Constants.CITY_CODE, bean.cityCode);
                    params.put(Constants.TIME, mTime);
                    params.put(Constants.TYPE, bean.type);
                    params.put(Constants.TITLE, bean.area);
                    startFragment(params, new WoodenDoorDefaultFragment());
                });
            }
        });
    }

    @Override
    public void onFailure(String failReason) {
        dismissLoading();
        mContentLayout.setVisibility(View.GONE);
        noNetwork(failReason);
    }

    @Override
    protected void reload() {
        getData();
    }
}
