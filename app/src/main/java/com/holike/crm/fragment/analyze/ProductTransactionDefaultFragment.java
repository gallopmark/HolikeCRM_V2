package com.holike.crm.fragment.analyze;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.grallopmark.tablayout.SlidingTabLayout;
import com.grallopmark.tablayout.listener.OnTabSelectListener;
import com.holike.crm.R;
import com.holike.crm.base.IntentValue;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.ProductTradingBean;
import com.holike.crm.presenter.fragment.DealerRankPresenter;
import com.holike.crm.presenter.fragment.ProductTradingPresenter;
import com.holike.crm.util.Constants;
import com.holike.crm.view.fragment.ProductTradingView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 成品交易报表(非经销商)
 */
public class ProductTransactionDefaultFragment extends MyFragment<ProductTradingPresenter, ProductTradingView> implements ProductTradingView {

    public static ProductTransactionDefaultFragment newInstance(ProductTradingBean bean) {
        IntentValue.getInstance().put("productTradingBean", bean);
        return new ProductTransactionDefaultFragment();
    }

    @BindView(R.id.tab_product_trading_type)
    SlidingTabLayout tabType;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.rv_product_trading)
    RecyclerView rv;
    @BindView(R.id.vp_product_trading)
    ViewPager vp;

    private String cityCode;
    private String type;
    private String time;
    private String title;
    private List<String> tabTitles;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_product_trading;
    }

    @Override
    protected ProductTradingPresenter attachPresenter() {
        return new ProductTradingPresenter();
    }

    @Override
    protected void init() {
        super.init();
        setStatusBar(R.color.bg_state_bar);
        setRightMenu(getString(R.string.translate_report_month_complete));
        rv.setLayoutManager(new LinearLayoutManager(mContext));
        Bundle bundle = getArguments();
        if (bundle != null) {
            cityCode = bundle.getString(Constants.CITY_CODE);
            time = bundle.getString(Constants.TIME);
            type = bundle.getString(Constants.TYPE);
            title = bundle.getString(Constants.TITLE);
            getData();
        } else {
            ProductTradingBean bean = (ProductTradingBean) IntentValue.getInstance().removeBy("productTradingBean");
            getDataSuccess(bean);
        }
        setTitle(getString(R.string.report_item5_title) + (TextUtils.isEmpty(title) ? "" : "—" + title));
    }

    @Override
    public void getData() {
        mPresenter.getData(cityCode, time, type);
        showLoading();
    }

    /**
     * 各月完成率
     */
    @Override
    protected void clickRightMenu(CharSequence text, View actionView) {
        startFragment(new ProductCompleteFragment());
    }

    @Override
    public void getDataFailed(String failed) {
        dismissLoading();
        showShortToast(failed);
    }

    /**
     * 获取数据成功
     */
    @Override
    public void getDataSuccess(ProductTradingBean productTradingBean) {
        dismissLoading();
        initTab(productTradingBean.getSelectData());
        tvDate.setText(productTradingBean.getTimeData());
        rv.setAdapter(new CommonAdapter<ProductTradingBean.PercentDataBean>(mContext, productTradingBean.getPercentData()) {
            @Override
            protected int bindView(int viewType) {
                return R.layout.item_rv_product_trading_report;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, ProductTradingBean.PercentDataBean percentDataBean, int position) {
                LinearLayout ll = holder.obtainView(R.id.ll_item_rv_product_trading_report);
                TextView tvArea = holder.obtainView(R.id.tv_item_rv_product_trading_report_area);
                TextView tvName = holder.obtainView(R.id.tv_item_rv_product_trading_report_name);
                TextView tvTarget = holder.obtainView(R.id.tv_item_rv_product_trading_report_target);
                TextView tvComplete = holder.obtainView(R.id.tv_item_rv_product_trading_report_complete);
                TextView tvCompletePercen = holder.obtainView(R.id.tv_item_rv_product_trading_report_complete_percen);
                TextView tvRank = holder.obtainView(R.id.tv_item_rv_product_trading_report_rank);
                ll.setBackgroundResource(position % 2 == 0 ? R.color.bg_item_order_report : R.color.color_while);
                tvArea.setTextColor(ContextCompat.getColor(mContext, percentDataBean.getIsClick() == 1 ? R.color.textColor5 : R.color.textColor8));
                tvArea.setText(percentDataBean.getArea());
                tvName.setText(percentDataBean.getName());
                tvTarget.setText(percentDataBean.getCountsTarget());
                tvCompletePercen.setText(percentDataBean.getPercent());
                tvComplete.setText(String.valueOf(percentDataBean.getCountsComplete()));
                tvRank.setText(percentDataBean.getRank());
                tvArea.setOnClickListener(v -> {
                    if (percentDataBean.getIsClick() == 1) {
                        Map<String, Serializable> params = new HashMap<>();
                        params.put(Constants.CITY_CODE, percentDataBean.getCityCode());
                        params.put(Constants.TIME, time);
                        params.put(Constants.TYPE, String.valueOf(percentDataBean.getType()));
                        params.put(Constants.TITLE, percentDataBean.getArea());
                        startFragment(params, new ProductTransactionDefaultFragment());
                    }
                });
            }
        });
    }

    /**
     * 切换导航
     */
    private void initTab(final List<ProductTradingBean.SelectDataBean> selectDataBeans) {
        if (tabTitles == null) {
            tabTitles = new ArrayList<>();
            for (ProductTradingBean.SelectDataBean bean : selectDataBeans) {
                tabTitles.add(bean.getName());
            }
            vp.setAdapter(DealerRankPresenter.getPagerAdapter(tabTitles.size()));
//            PerformancePresenter.setTabWidth(tabType, tabTitles.size());
            tabType.setupViewPager(vp, tabTitles.toArray(new String[0]));
            tabType.setOnTabSelectListener(new OnTabSelectListener() {
                @Override
                public void onTabSelect(int position) {
                    time = selectDataBeans.get(position).getTime();
                    getData();
                }

                @Override
                public void onTabReselect(int position) {
                }
            });
            tabType.setCurrentTab(mPresenter.getSelectPosition(time, selectDataBeans));
        }
    }
}
