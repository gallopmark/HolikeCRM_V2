package com.holike.crm.fragment.analyze;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.grallopmark.tablayout.SlidingTabLayout;
import com.grallopmark.tablayout.listener.OnTabSelectListener;
import com.holike.crm.R;
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
 */
public class WoodenDoorPersonalFragment extends MyFragment<WoodenDoorPresenter, WoodenDoorView> implements WoodenDoorView {
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
            WoodenDoorBean bean = (WoodenDoorBean) bundle.getSerializable("bean");
            if (bean != null) {
                onSuccess(bean);
            } else {
                mCityCode = bundle.getString(Constants.CITY_CODE);
                mTime = bundle.getString(Constants.TIME);
                mType = bundle.getString(Constants.TYPE);
                mTitle = bundle.getString(Constants.TITLE);
                getData();
            }
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
            mViewpager.setAdapter(obtain(mTabTitles.size()));
//            PerformancePresenter.setTabWidth(tabType, mTabTitles.size());
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

    private PagerAdapter obtain(final int size) {
        return new PagerAdapter() {
            @Override
            public int getCount() {
                return size;
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                View view = new View(container.getContext());
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView((View) object);
            }
        };
    }

    private void setAdapter(List<WoodenDoorBean.PercentDataBean> list) {
        mRecyclerView.setAdapter(new CommonAdapter<WoodenDoorBean.PercentDataBean>(mContext, list) {
            @Override
            protected int bindView(int viewType) {
                return R.layout.item_rv_product_trading_report;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, WoodenDoorBean.PercentDataBean bean, int position) {
                LinearLayout ll = holder.obtainView(R.id.ll_item_rv_product_trading_report);
                TextView tvArea = holder.obtainView(R.id.tv_item_rv_product_trading_report_area);
                TextView tvName = holder.obtainView(R.id.tv_item_rv_product_trading_report_name);
                TextView tvTarget = holder.obtainView(R.id.tv_item_rv_product_trading_report_target);
                TextView tvComplete = holder.obtainView(R.id.tv_item_rv_product_trading_report_complete);
                TextView tvCompletePercent = holder.obtainView(R.id.tv_item_rv_product_trading_report_complete_percen);
                TextView tvRank = holder.obtainView(R.id.tv_item_rv_product_trading_report_rank);
                ll.setBackgroundResource(position % 2 == 0 ? R.color.bg_item_order_report : R.color.color_while);
                tvArea.setTextColor(ContextCompat.getColor(mContext, bean.isClick() ? R.color.textColor5 : R.color.textColor8));
                tvArea.setText(bean.area);
                tvName.setText(bean.name);
                tvTarget.setText(bean.countsTarget);
                tvCompletePercent.setText(bean.percentComplete);
                tvComplete.setText(bean.countsComplete);
                tvRank.setText(bean.rank);
                tvArea.setOnClickListener(v -> {
                    if (bean.isClick()) {
                        Map<String, Serializable> params = new HashMap<>();
                        params.put(Constants.CITY_CODE, bean.cityCode);
                        params.put(Constants.TIME, mTime);
                        params.put(Constants.TYPE, bean.type);
                        params.put(Constants.TITLE, bean.area);
                        startFragment(params, new WoodenDoorPersonalFragment());
                    }
                });
            }
        });
    }

    @Override
    public void onFailure(String failReason) {
        dismissLoading();
        showShortToast(failReason);
    }
}
