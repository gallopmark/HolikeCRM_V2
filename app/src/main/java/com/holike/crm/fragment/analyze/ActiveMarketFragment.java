package com.holike.crm.fragment.analyze;

import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.grallopmark.tablayout.SlidingTabLayout;
import com.grallopmark.tablayout.listener.OnTabSelectListener;
import com.holike.crm.R;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.ActiveMarketBean;
import com.holike.crm.bean.OrderRankingBean;
import com.holike.crm.dialog.CalendarPickerDialog;
import com.holike.crm.dialog.SelectAreaDialog;
import com.holike.crm.presenter.fragment.ActiveMarketPresenter;
import com.holike.crm.presenter.fragment.DealerRankPresenter;
import com.holike.crm.presenter.fragment.OrderReportPresenter;
import com.holike.crm.util.TimeUtil;
import com.holike.crm.view.fragment.ActiveMarketView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wqj on 2018/6/21.
 * 主动营销数据报表
 */

public class ActiveMarketFragment extends MyFragment<ActiveMarketPresenter, ActiveMarketView> implements ActiveMarketView {
    @BindView(R.id.ll_active_market)
    LinearLayout llMain;
    @BindView(R.id.tab_active_market_type)
    SlidingTabLayout tabType;
    @BindView(R.id.vp_performance)
    ViewPager vp;
    @BindView(R.id.tv_active_market_area)
    TextView tvArea;
    @BindView(R.id.btn_active_market_area)
    LinearLayout btnArea;
    @BindView(R.id.tv_active_market_select_area)
    TextView tvSelectArea;
    @BindView(R.id.tv_active_market_days)
    TextView tvDays;
    @BindView(R.id.tv_active_market_order_num)
    TextView tvOrderNum;
    @BindView(R.id.ll_active_market_area)
    LinearLayout llArea;
    @BindView(R.id.ll_active_market_personal)
    LinearLayout llPersonal;
    @BindView(R.id.rv_active_market)
    RecyclerView rv;

    private String cityCode;
    private String time;
    private String startTime, endTime;
    protected List<String> tabTitles;
    private ActiveMarketBean activeMarketBean;
    private List<ActiveMarketBean.TimeDataBean> timeDataBeans;

    @Override
    protected void init() {
        super.init();
        setTitle(getString(R.string.report_item16_title));
        rv.setLayoutManager(new LinearLayoutManager(mContext));
        getData();
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_active_market;
    }

    @Override
    protected ActiveMarketPresenter attachPresenter() {
        return new ActiveMarketPresenter();
    }

    @Override
    protected void clickRightMenu(CharSequence text, View actionView) {
        startFragment(null, new WirteCityFragment());
    }

    @OnClick({R.id.btn_active_market_area})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.btn_active_market_area) {
            new SelectAreaDialog(mContext, getString(R.string.dialog_title_select_area), cityCode, activeMarketBean.getSelectData(), new SelectAreaDialog.SelectAreaListener() {
                @Override
                public void select(OrderRankingBean.SelectDataBean selectDataBean) {
                    cityCode = selectDataBean.getCityCode();
                    getData();
                }
            }).show();
        }
    }

    /**
     * 获取数据
     */
    @Override
    public void getData() {
        showLoading();
        mPresenter.getData(cityCode, startTime, endTime, time);
    }

    /**
     * 获取数据成功
     */
    @Override
    public void getDataSuccess(ActiveMarketBean bean) {
        dismissLoading();
        activeMarketBean = bean;
        String dayText = mContext.getString(R.string.report_active_market_days) + bean.getDayTotal();
        tvDays.setText(dayText);
        String numText = mContext.getString(R.string.report_active_market_order_num) + bean.getCountTotal();
        tvOrderNum.setText(numText);
        initTab(bean.getTimeData());
    }

    /**
     * 获取数据失败
     */
    @Override
    public void getDataFailed(String failed) {
        dismissLoading();
        showShortToast(failed);
    }

    /**
     * 显示填写服务城市
     */
    @Override
    public void showPersonal(ActiveMarketBean bean) {
        llPersonal.setVisibility(View.VISIBLE);
        setRightMenu(getString(R.string.report_active_market_write_city));
        showPersonalList(bean.getDataList());
    }

    /**
     * 全国主动营销数据报表
     */
    @Override
    public void showNational(ActiveMarketBean bean) {
        llArea.setVisibility(View.VISIBLE);
        btnArea.setVisibility(View.VISIBLE);
        tvSelectArea.setText(bean.getSelectName());
        showAreaList(bean.getDataList());
    }

    /**
     * 大区主动营销数据报表
     */
    @Override
    public void showArea(ActiveMarketBean bean) {
        llArea.setVisibility(View.VISIBLE);
        tvArea.setVisibility(View.VISIBLE);
        tvArea.setText(bean.getSelectName());
        showAreaList(bean.getDataList());
    }

    /**
     * 主动营销数据报表数据
     */
    @Override
    public void showAreaList(List<ActiveMarketBean.DataListBean> list) {
        rv.setAdapter(new CommonAdapter<ActiveMarketBean.DataListBean>(mContext, list) {
            @Override
            protected int bindView(int viewType) {
                return R.layout.item_rv_active_market_area;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, ActiveMarketBean.DataListBean dataListBean, int position) {
                TextView tvCenter = holder.obtainView(R.id.tv_item_rv_active_market_area_center);
                TextView tvActiveMarket = holder.obtainView(R.id.tv_item_rv_active_market_area_active_market);
                TextView tvCity = holder.obtainView(R.id.tv_item_rv_active_market_area_city);
                TextView tvData = holder.obtainView(R.id.tv_item_rv_active_market_area_data);
                TextView tvDays = holder.obtainView(R.id.tv_item_rv_active_market_area_days);
                TextView tvOrderNum = holder.obtainView(R.id.tv_item_rv_active_market_area_order_num);
                tvCenter.setText(dataListBean.getArea());
                tvActiveMarket.setText(dataListBean.getName());
                tvCity.setText(dataListBean.getDealerName());
                tvData.setText(dataListBean.getTime());
                tvDays.setText(dataListBean.getDay());
                tvOrderNum.setText(dataListBean.getCountsComplete());
            }
        });
    }

    /**
     * 显示服务城市列表
     */
    @Override
    public void showPersonalList(List<ActiveMarketBean.DataListBean> list) {
        rv.setAdapter(new CommonAdapter<ActiveMarketBean.DataListBean>(mContext, list) {
            @Override
            protected int bindView(int viewType) {
                return R.layout.item_rv_active_market_personal;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, ActiveMarketBean.DataListBean dataListBean, int position) {
                TextView tvCity = holder.obtainView(R.id.tv_item_rv_active_market_personal_city);
                TextView tvData = holder.obtainView(R.id.tv_item_rv_active_market_personal_data);
                TextView tvDays = holder.obtainView(R.id.tv_item_rv_active_market_personal_days);
                TextView tvOrderNum = holder.obtainView(R.id.tv_item_rv_active_market_personal_order_num);
                tvCity.setText(dataListBean.getDealerName());
                tvData.setText(dataListBean.getTime());
                tvDays.setText(dataListBean.getDay());
                tvOrderNum.setText(dataListBean.getCountsComplete());
            }
        });
    }

    /**
     * 切换月份
     */
    private void initTab(final List<ActiveMarketBean.TimeDataBean> timeDataBeans) {
        this.timeDataBeans = timeDataBeans;
        if (tabTitles == null) {
            tabTitles = new ArrayList<>();
            for (ActiveMarketBean.TimeDataBean bean : timeDataBeans) {
                tabTitles.add(bean.getName());
            }
            vp.setAdapter(DealerRankPresenter.getPagerAdapter(tabTitles.size()));
//            PerformancePresenter.setTabWidth(tabType, tabTitles.size());
            tabType.setupViewPager(vp, tabTitles.toArray(new String[tabTitles.size()]));
            tabType.setOnTabSelectListener(new OnTabSelectListener() {
                @Override
                public void onTabSelect(int position) {
                    selectTime(position, timeDataBeans);
                }

                @Override
                public void onTabReselect(int position) {
                    selectTime(position, timeDataBeans);
                }
            });
            tabType.setCurrentTab(mPresenter.getSelectPosition(time, timeDataBeans));
        }
    }

    /**
     * 切换月份操作
     */
    public void selectTime(int position, List<ActiveMarketBean.TimeDataBean> timeDataBeans) {
        time = String.valueOf(timeDataBeans.get(position).getTime());
        if (timeDataBeans.get(position).getName().equals("查询日期")) {
            checkDate();
        } else {
            getData();
        }
    }

    /**
     * 查询日期
     */
    private List<Date> mSelectedDates;

    public void checkDate() {
        OrderReportPresenter.selectDate(mContext, mSelectedDates, new CalendarPickerDialog.OnCalendarRangeSelectedListener() {
            @Override
            public void onLeftClicked(CalendarPickerDialog dialog) {
//                dialog.dismiss();
//                startTime = null;
//                endTime = null;
//                getData();
            }

            @Override
            public void onRightClick(CalendarPickerDialog dialog, List<Date> selectedDates, Date start, Date end) {
                dialog.dismiss();
                mSelectedDates = selectedDates;
                if (selectedDates.size() >= 1) {
                    startTime = TimeUtil.dateToStamp(start,false);
                    endTime = TimeUtil.dateToStamp(end,true);
//                    startTime = TimeUtil.dataToStamp(start, "yyyy年MM月dd日");
//                    endTime = TimeUtil.dataToStamp(end, "yyyy年MM月dd日");
                } else {
                    if (timeDataBeans != null && !timeDataBeans.isEmpty()) {
                        time = timeDataBeans.get(0).getTime();
                    }
                    startTime = null;
                    endTime = null;
                }
                getData();
            }
        });
    }
}
