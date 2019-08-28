package com.holike.crm.fragment.analyze;

import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
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
import com.holike.crm.bean.OrderReportBean;
import com.holike.crm.bean.staticbean.JumpBean;
import com.holike.crm.dialog.CalendarPickerDialog;
import com.holike.crm.presenter.fragment.OrderReportPresenter;
import com.holike.crm.util.Constants;
import com.holike.crm.util.TimeUtil;
import com.holike.crm.view.fragment.OrderReportView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wqj on 2018/4/10.
 * 订金交易报表
 */

public class OrderReportFragment extends MyFragment<OrderReportPresenter, OrderReportView> implements OrderReportView {
    @BindView(R.id.ll_order_report)
    LinearLayout llMain;
    @BindView(R.id.tv_order_report_date)
    TextView tvDate;
    @BindView(R.id.btn_translate_report_write_target)
    TextView btnWriteTarget;
    @BindView(R.id.tab_order_report_type)
    CommonTabLayout tabType;
    @BindView(R.id.rv_order_report)
    RecyclerView rv;

    protected ArrayList<CustomTabEntity> mTabEntities;
    private List<OrderReportBean.SelectDataBean> selectDataBeans;
    private String cityCode;
    private String type;
    private String title;
    private String startTime;
    private String endTime;
    protected String time;

    @Override
    protected void init() {
        super.init();
        setStatusBar(R.color.bg_state_bar);
        setRightMenu(getString(R.string.translate_report_month_complete));
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
            if (JumpBean.getJumpBack().equals(getString(R.string.homepage))) {
                setLeft(getString(R.string.homepage));
            } else {
                setLeft(getString(R.string.report_title));
            }
        }
        setTitle(getString(R.string.report_item2_title) + (TextUtils.isEmpty(title) ? "" : "—" + title));
        if (startTime != null) {
            showDate(startTime, endTime);
        }
        getData();
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_order_report;
    }

    @Override
    protected OrderReportPresenter attachPresenter() {
        return new OrderReportPresenter();
    }

    @Override
    public void getData() {
        showLoading();
        mPresenter.getOrderReport(cityCode, startTime, endTime, type, time);
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
        String dateTime = TimeUtil.stampToString(startTime, "yyyy.MM.dd") + "—" +  TimeUtil.stampToString(endTime, "yyyy.MM.dd");
        tvDate.setText(dateTime);
    }

    /**
     * 获取数据成功
     */
    @Override
    public void getOrderReportSuccess(final OrderReportBean orderReportBean) {
        dismissLoading();
        time = (time == null ? orderReportBean.getTime() : time);
        initTab(orderReportBean.getSelectData());
        btnWriteTarget.setVisibility(orderReportBean.getIsEdit().equals("1") ? View.VISIBLE : View.GONE);
        showDate(orderReportBean.getTimeData().getStartTime(), orderReportBean.getTimeData().getEndTime());
        if (orderReportBean.getDepositList().size() == 0) {
            noData(R.drawable.no_result, R.string.tips_noorderdata, false);
            return;
        }
        rv.setAdapter(new CommonAdapter<OrderReportBean.DepositListBean>(mContext, orderReportBean.getDepositList()) {
            @Override
            protected int bindView(int viewType) {
                return R.layout.item_rv_order_report;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, OrderReportBean.DepositListBean depositListBean, int position) {
                LinearLayout ll = holder.obtainView(R.id.ll_item_rv_order_report);
                TextView tvArea = holder.obtainView(R.id.tv_item_rv_order_report_area);
                TextView tvName = holder.obtainView(R.id.tv_item_rv_order_report_name);
                TextView tvCountstoday = holder.obtainView(R.id.tv_item_rv_order_report_countstoday);
                TextView tvTarget = holder.obtainView(R.id.tv_item_rv_order_report_target);
                TextView tvComplete = holder.obtainView(R.id.tv_item_rv_order_report_complete);
                TextView tvCompletePercen = holder.obtainView(R.id.tv_item_rv_order_report_complete_percen);
                TextView tvRank = holder.obtainView(R.id.tv_item_rv_order_report_rank);
                ll.setBackgroundResource(position % 2 == 0 ? R.color.bg_item_order_report : R.color.color_while);
                tvArea.setTextColor(ContextCompat.getColor(mContext, depositListBean.getIsClick() == 1 ? R.color.textColor5 : R.color.textColor8));
                tvName.setTextColor(ContextCompat.getColor(mContext, depositListBean.getIsChange() == 1 ? R.color.textColor17 : R.color.textColor8));
                tvArea.setText(depositListBean.getArea());
                tvName.setText(depositListBean.getName());
                tvTarget.setText(depositListBean.getTarget());
                tvCompletePercen.setText(depositListBean.getComplete());
                tvComplete.setText(String.valueOf(depositListBean.getCountstotal()));
                tvCountstoday.setText(String.valueOf(depositListBean.getCountstoday()));
                tvRank.setText(depositListBean.getRank() > 0 ? String.valueOf(depositListBean.getRank()) : "-");
                tvArea.setOnClickListener(v -> {
                    if (depositListBean.getIsClick() == 1) {
                        Map<String, Serializable> params = new HashMap<>();
                        params.put(Constants.CITY_CODE, depositListBean.getCityCode());
                        params.put(Constants.START_TIME, startTime);
                        params.put(Constants.END_TIME, endTime);
                        params.put(Constants.TIME, time);
                        params.put(Constants.TYPE, String.valueOf(depositListBean.getType()));
                        params.put(Constants.TITLE, depositListBean.getArea());
                        startFragment(params, new OrderReportFragment());
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

    @Override
    protected void clickRightMenu(String menuText, View actionView) {
        showLoading();
        mPresenter.getCompleteData(cityCode, type);
    }

    @OnClick({R.id.btn_translate_report_write_target})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.btn_translate_report_write_target) {
            Map<String, Serializable> params2 = new HashMap<>();
            params2.put(Constants.TIME, time);
            startFragment(params2, new OrderReportTargetFragment());
        }
    }

    /**
     * 切换导航
     */
    private void initTab(final List<OrderReportBean.SelectDataBean> selectDataBeans) {
        this.selectDataBeans = selectDataBeans;
        if (mTabEntities == null) {
            mTabEntities = new ArrayList<>();
            for (OrderReportBean.SelectDataBean bean : selectDataBeans) {
                mTabEntities.add(new TabEntity(bean.getName(), 0, 0));
            }
            tabType.setTabData(mTabEntities);
            tabType.setOnTabSelectListener(new OnTabSelectListener() {
                @Override
                public void onTabSelect(int position) {
                    tabSelect(position, selectDataBeans);
                }

                @Override
                public void onTabReselect(int position) {
                    tabSelect(position, selectDataBeans);
                }
            });
            tabType.setCurrentTab(OrderReportPresenter.getSelectPosition(time, selectDataBeans));
        }
    }

    /**
     * 选择导航
     */
    private void tabSelect(int position, List<OrderReportBean.SelectDataBean> selectDataBeans) {
        time = selectDataBeans.get(position).getTime();
        if (selectDataBeans.get(position).getName().equals("查询日期")) {
            checkDate();
        } else {
            getData();
        }
    }

    /**
     * 查询日期
     */
    private List<Date> mSelectedDates;

    private void checkDate() {
        OrderReportPresenter.selectDate(mContext, mSelectedDates, new CalendarPickerDialog.OnCalendarRangeSelectedListener() {
            @Override
            public void onLeftClicked(CalendarPickerDialog dialog) {
//                dialog.dismiss();
//                getData();
            }

            @Override
            public void onRightClick(CalendarPickerDialog dialog, List<Date> selectedDates, Date start, Date end) {
                dialog.dismiss();
                mSelectedDates = selectedDates;
                if (selectedDates.size() >= 1) {
                    startTime = TimeUtil.dateToStamp(start, false);
                    endTime = TimeUtil.dateToStamp(end, true);
//                    startTime = TimeUtil.dataToStamp(start, "yyyy年MM月dd日");
//                    endTime = TimeUtil.dataToStamp(end, "yyyy年MM月dd日");
                } else {
                    startTime = null;
                    endTime = null;
                    if (selectDataBeans != null && !selectDataBeans.isEmpty()) {
                        time = selectDataBeans.get(0).getTime();
                    }
                }
                getData();
            }
        });
    }
}
