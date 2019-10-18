package com.holike.crm.fragment.analyze;

import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.grallopmark.tablayout.CommonTabLayout;
import com.grallopmark.tablayout.TabEntity;
import com.grallopmark.tablayout.listener.CustomTabEntity;
import com.grallopmark.tablayout.listener.OnTabSelectListener;
import com.holike.crm.R;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.MonthCompleteBean;
import com.holike.crm.bean.FastLiveBean;
import com.holike.crm.bean.staticbean.JumpBean;
import com.holike.crm.dialog.CalendarPickerDialog;
import com.holike.crm.presenter.fragment.FastLivePresenter;
import com.holike.crm.util.Constants;
import com.holike.crm.util.TimeUtil;
import com.holike.crm.view.fragment.FastLiveView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by wqj on 2018/4/10.
 * 订金交易报表
 */

public class FastLiveFragment extends MyFragment<FastLivePresenter, FastLiveView> implements FastLiveView, OnTabSelectListener {
    @BindView(R.id.ll_order_report)
    LinearLayout llMain;
    @BindView(R.id.tv_order_report_date)
    TextView tvDate;
    @BindView(R.id.tab_order_report_type)
    CommonTabLayout tabType;
    @BindView(R.id.rv_order_report)
    RecyclerView rv;

    protected ArrayList<CustomTabEntity> mTabEntities;
    private String cityCode;
    private String type;
    private String title;
    private String startTime, endTime;
    protected String time;

    @Override
    protected void init() {
        super.init();
        setStatusBar(R.color.bg_state_bar);
        rv.setLayoutManager(new LinearLayoutManager(mContext));
        Bundle bundle = getArguments();
        if (bundle != null) {
            cityCode = bundle.getString(Constants.CITY_CODE);
            startTime = bundle.getString(Constants.START_TIME);
            endTime = bundle.getString(Constants.END_TIME);
            type = bundle.getString(Constants.TYPE);
            title = bundle.getString(Constants.TITLE);
            time = bundle.getString(Constants.TIME);
        } else {
            time = "1";
        }
        setTitle(getString(R.string.fast_live_report) + (TextUtils.isEmpty(title) ? "" : "—" + title));
        if (startTime != null) {
            showDate(startTime, endTime);
        }
        getData();
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_fast_live;
    }

    @Override
    protected FastLivePresenter attachPresenter() {
        return new FastLivePresenter();
    }

    @Override
    public void getData() {
        mPresenter.getOrderReport(cityCode, startTime, endTime, type, time);
        showLoading();
    }

    /**
     * 获取各月完成率数据成功
     */
    @Override
    public void getCompleteDataSuccess(List<MonthCompleteBean> bean, String title) {
        dismissLoading();
        Map<String, Serializable> params = new HashMap<>();
        params.put(Constants.MONTH_COMPLETE, (Serializable) bean);
        params.put(Constants.TITLE, title);
        startFragment(params, new MonthCompleteFragment());
    }

    /**
     * 显示时间
     */
    @Override
    public void showDate(String startTime, String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        startTime = TimeUtil.stampToString(startTime, "yyyy.MM.dd");
        endTime = TimeUtil.stampToString(endTime, "yyyy.MM.dd");
        tvDate.setText(startTime + "—" + endTime);
    }

    /**
     * 获取数据成功
     */
    private List<FastLiveBean.SelectDataBean> mSelectData;

    @Override
    public void getOrderReportSuccess(final FastLiveBean fastLiveBean) {
        dismissLoading();
        time = (time == null ? fastLiveBean.getTimeData() : time);
        initTab(fastLiveBean.getSelectData());
        tvDate.setText(fastLiveBean.getTimeData());
        if (fastLiveBean.getPercentData().size() == 0) {
            noData(R.drawable.no_result, R.string.tips_noorderdata, false);
            return;
        }
        rv.setAdapter(new CommonAdapter<FastLiveBean.PercentDataBean>(mContext, fastLiveBean.getPercentData()) {
            @Override
            protected int bindView(int viewType) {
                return R.layout.item_rv_fast_live;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, FastLiveBean.PercentDataBean depositListBean, int position) {
                LinearLayout ll = holder.obtainView(R.id.ll_item_rv_order_report);
                TextView tvArea = holder.obtainView(R.id.tv_item_rv_fast_live_area);
                TextView tvHead = holder.obtainView(R.id.tv_item_rv_fast_live_head);
                TextView tvTotal = holder.obtainView(R.id.tv_item_rv_fast_live_total);
                TextView tvSelect1 = holder.obtainView(R.id.tv_item_rv_fast_live_1);
                TextView tvSelect2 = holder.obtainView(R.id.tv_item_rv_fast_live_2);
                TextView tvSelect3 = holder.obtainView(R.id.tv_item_rv_fast_live_3);
                TextView tvSelect4 = holder.obtainView(R.id.tv_item_rv_fast_live_4);
                ll.setBackgroundResource(position % 2 == 0 ? R.color.bg_item_order_report : R.color.color_while);
                tvArea.setTextColor(ContextCompat.getColor(mContext, depositListBean.getIsClick().equals("1") ? R.color.textColor5 : R.color.textColor8));
                tvHead.setTextColor(ContextCompat.getColor(mContext, depositListBean.getIsChange().equals("1") ? R.color.textColor17 : R.color.textColor8));
                tvArea.setText(depositListBean.getArea());
                tvHead.setText(depositListBean.getName());
                tvTotal.setText(depositListBean.getTotalMoney());
                tvSelect1.setText(depositListBean.getSelect1());
                tvSelect2.setText(depositListBean.getSelect2());
                tvSelect3.setText(depositListBean.getSelect3());
                tvSelect4.setText(depositListBean.getSelect4());
                tvArea.setOnClickListener(v -> {
                    if (depositListBean.getIsClick().equals("1")) {
                        Map<String, Serializable> params = new HashMap<>();
                        params.put(Constants.CITY_CODE, depositListBean.getCityCode());
                        params.put(Constants.START_TIME, startTime);
                        params.put(Constants.END_TIME, endTime);
                        params.put(Constants.TIME, time);
                        params.put(Constants.TYPE, String.valueOf(depositListBean.getType()));
                        params.put(Constants.TITLE, depositListBean.getArea());
                        startFragment(params, new FastLiveFragment());
                    }
                });
            }
        });
    }

    /**
     * 获取数据失败
     */
    @Override
    public void getDataFailed(String failed) {
        dismissLoading();
        showShortToast(failed);
    }

    @Override
    protected void onFinishResult(int requestCode, int resultCode, Map<String, Serializable> result) {
        super.onFinishResult(requestCode, resultCode, result);
        if (resultCode == Constants.RESULT_CODE_REFRESH_ORDER_REPORT) {
            getData();
        }
    }

    @Override
    public boolean onBackPressed() {
        finishFragment(0, Constants.RESULT_CODE_REFRESH_ORDER_REPORT, null);
        return true;
    }

    @Override
    protected void back() {
        finishFragment(0, Constants.RESULT_CODE_REFRESH_ORDER_REPORT, null);
    }


    /**
     * 切换导航
     */
    private void initTab(final List<FastLiveBean.SelectDataBean> selectDataBeans) {
        mSelectData = selectDataBeans;
        if (mTabEntities == null) {
            mTabEntities = new ArrayList<>();
            for (FastLiveBean.SelectDataBean bean : selectDataBeans) {
                mTabEntities.add(new TabEntity(bean.getName(), 0, 0));
            }
            tabType.setTabData(mTabEntities);
            tabType.setOnTabSelectListener(this);
//            tabType.setOnTabSelectListener(new OnTabSelectListener() {
//                @Override
//                public void onTabSelect(int position) {
//                    tabSelect(position, selectDataBeans);
//                }
//
//                @Override
//                public void onTabReselect(int position) {
////                    tabSelect(position, selectDataBeans);
//                }
//            });
            tabType.setCurrentTab(FastLivePresenter.getSelectPosition(time, selectDataBeans));
        }
    }

    @Override
    public void onTabSelect(int position) {
        if (mSelectData == null) return;
        tabSelect(position, mSelectData);
    }

    @Override
    public void onTabReselect(int position) {
        if (mSelectData == null) return;
        tabSelect(position, mSelectData);
    }

    /**
     * 选择导航
     */
    public void tabSelect(int position, List<FastLiveBean.SelectDataBean> selectDataBeans) {
        time = selectDataBeans.get(position).getSelectTime();
        String name = selectDataBeans.get(position).getName();
        if (TextUtils.equals(name, "查询日期")) {
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
        FastLivePresenter.selectDate(mContext, mSelectedDates, new CalendarPickerDialog.OnCalendarRangeSelectedListener() {
            @Override
            public void onLeftClicked(CalendarPickerDialog dialog) {
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
                    if (mSelectData != null && !mSelectData.isEmpty()) {
                        time = mSelectData.get(0).getSelectTime();
                    }
                    startTime = null;
                    endTime = null;
                }
                getData();
            }
        });
    }

}
