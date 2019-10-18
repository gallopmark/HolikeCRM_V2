package com.holike.crm.fragment.analyze;

import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grallopmark.tablayout.CommonTabLayout;
import com.grallopmark.tablayout.TabEntity;
import com.grallopmark.tablayout.listener.CustomTabEntity;
import com.grallopmark.tablayout.listener.OnTabSelectListener;
import com.holike.crm.R;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.LineAttractBean;
import com.holike.crm.dialog.CalendarPickerDialog;
import com.holike.crm.dialog.OnlineDrainageTipDialog;
import com.holike.crm.presenter.fragment.OnlineAttractReportPresenter;
import com.holike.crm.util.Constants;
import com.holike.crm.util.TimeUtil;
import com.holike.crm.view.fragment.OnlineAttractReportView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/*线上引流*/
public class OnlineAttractReportFragment extends MyFragment<OnlineAttractReportPresenter, OnlineAttractReportView> implements OnlineAttractReportView, OnlineAttractReportPresenter.ClickListener {

    @BindView(R.id.rv_online_drainage_tab)
    RecyclerView rvTab;
    @BindView(R.id.rv_online_drainage_side)
    RecyclerView rvSide;
    @BindView(R.id.rv_online_drainage_title)
    RecyclerView rvTitle;
    @BindView(R.id.rv_online_drainage_center)
    RecyclerView rvCenter;
    @BindView(R.id.tab_online_drainage_type)
    CommonTabLayout tabType;
    @BindView(R.id.tv_online_drainage_date)
    TextView tvDate;
    @BindView(R.id.tv_online_drainage_title_first)
    TextView tvTitleFirst;
    @BindView(R.id.tv_question_mark)
    TextView tvQuestionMark;
    @BindView(R.id.tv_online_drainage_shop_name)
    TextView tvShopName;
    @BindView(R.id.ll_order_report)
    LinearLayout llMain;
    @BindView(R.id.ll_form)
    LinearLayout llForm;
    @BindView(R.id.ll_date_tip)
    LinearLayout llDateTip;
    @BindView(R.id.v_line)
    View vLine;
    @BindView(R.id.mHsv)
    HorizontalScrollView mHsv;

    protected ArrayList<CustomTabEntity> mTabEntities;
    private List<LineAttractBean.SelectDataBean> selectDataBeans;

    private String cityCode;
    private String startTime;
    private String endTime;
    private String type;
    private String title;
    private String time;

    @Override
    protected void init() {
        super.init();
        Bundle bundle = getArguments();
        if (bundle != null) {
            cityCode = bundle.getString(Constants.CITY_CODE);
            startTime = bundle.getString(Constants.START_TIME);
            endTime = bundle.getString(Constants.END_TIME);
            type = bundle.getString(Constants.TYPE);
            time = bundle.getString(Constants.TIME);
            title = bundle.getString(Constants.TITLE);
        } else {
            time = "1";
        }
//        if (startTime != null) {
//            showDate(startTime, endTime);
//        }
        String mTitleInside = mContext.getString(R.string.online_drainage_report_title);
        if (!TextUtils.isEmpty(title)) {
            setTitle(mTitleInside + "-" + title);
        } else {
            setTitle(mTitleInside);
        }
        mPresenter.setStoreAdapter(rvTab);
        mPresenter.setTitleListAdapter(rvTitle);
        mPresenter.setScrollSynchronous(rvSide, rvCenter);
        mPresenter.setScrollSynchronous(rvCenter, rvSide);
        llForm.setVisibility(View.INVISIBLE);
        llDateTip.setVisibility(View.INVISIBLE);
        vLine.setVisibility(View.INVISIBLE);
        initData();
    }

    private void initData() {
        showLoading();
        mPresenter.getData(cityCode, startTime, endTime, time, type);
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_online_drainage_report;
    }

    @Override
    protected OnlineAttractReportPresenter attachPresenter() {
        return new OnlineAttractReportPresenter();
    }

    @Override
    public void getDataSuccess(LineAttractBean bean) {
        dismissLoading();
        initTab(bean.getSelectData());
        tvDate.setText(bean.getTimeData());
        mPresenter.addTitleData(mContext, bean.isDealer());
        tvShopName.setVisibility(View.GONE);
        if (bean.isDealer()) { //是经销商
            tvTitleFirst.setText(mContext.getString(R.string.online_drainage_guide));
            if (bean.getDealerData() != null && bean.getDealerData().size() > 1) {
                rvTab.setVisibility(View.VISIBLE);
                mPresenter.addStoreBean(bean);
            }
            mPresenter.setListSide(mContext, rvSide, bean, 0);
            mPresenter.setListContent(mContext, rvCenter, bean, 0);
            tvShopName.setVisibility(View.VISIBLE);
            if (bean.getDealerData() != null && bean.getDealerData().size() > 0) {
                tvShopName.setText(textEmpty(bean.getDealerData().get(0).getShopName()));
            }
        } else { //非经销商
            rvTab.setVisibility(View.GONE);
            tvTitleFirst.setText(mContext.getString(R.string.online_drainage_area));
            mPresenter.setListSide(mContext, rvSide, bean, this);
            mPresenter.setListContent(mContext, rvCenter, bean);
        }
        llForm.setVisibility(View.VISIBLE);
        llDateTip.setVisibility(View.VISIBLE);
        vLine.setVisibility(View.VISIBLE);
        scrollToTop();
        tvQuestionMark.setOnClickListener(v -> new OnlineDrainageTipDialog(mContext, bean.isDealer()).show());
    }

    private void scrollToTop() {
        mHsv.postDelayed(scrollUpRunnable, 20);
    }

    private Runnable scrollUpRunnable = () ->
            mHsv.scrollTo(0, 0);
//            mHsv.fullScroll(HorizontalScrollView.FOCUS_UP);

    /**
     * 切换导航
     */
    private void initTab(final List<LineAttractBean.SelectDataBean> selectDataBeans) {
        this.selectDataBeans = selectDataBeans;
        if (mTabEntities == null) {
            mTabEntities = new ArrayList<>();
            for (LineAttractBean.SelectDataBean bean : selectDataBeans) {
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
            tabType.setCurrentTab(OnlineAttractReportPresenter.getSelectPosition(time, selectDataBeans));
        }
    }

    /**
     * 选择导航
     */
    public void tabSelect(int position, List<LineAttractBean.SelectDataBean> selectDataBeans) {
        time = selectDataBeans.get(position).getSelectTime();
        if (!TextUtils.isEmpty(selectDataBeans.get(position).getName()) && selectDataBeans.get(position).getName().equals("查询日期")) {
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
        OnlineAttractReportPresenter.selectDate(mContext, mSelectedDates, new CalendarPickerDialog.OnCalendarRangeSelectedListener() {
            @Override
            public void onLeftClicked(CalendarPickerDialog dialog) {

            }

            @Override
            public void onRightClick(CalendarPickerDialog dialog, List<Date> selectedDates, Date start, Date end) {
                dialog.dismiss();
                mSelectedDates = selectedDates;
                if (selectedDates.size() >= 1) {
                    startTime = TimeUtil.dateToStamp(start, false);
                    endTime = TimeUtil.dateToStamp(end, true);
//                    startTime = TimeUtil.dataToStamp(start, "yyyy年MM月dd日");
//                    endTime = String.valueOf(mPresenter.convertEndDate(end) / 1000);
                    getData();
                } else {
                    if (selectDataBeans != null && !selectDataBeans.isEmpty()) {
                        startTime = null;
                        endTime = null;
                        time = selectDataBeans.get(0).getSelectTime();
                        getData();
                    }
                }
            }
        });
    }

    @Override
    public void getDataFail(String errorMsg) {
        showShortToast(errorMsg);
        dismissLoading();
    }

    @Override
    public void getData() {
        initData();
    }

    @Override
    public void onSmoothScroll(int position) {
        rvTab.smoothScrollToPosition(position);
    }

    @Override
    public void onTagSelect(int position, LineAttractBean bean) {
        mPresenter.setListSide(mContext, rvSide, bean, position);
        mPresenter.setListContent(mContext, rvCenter, bean, position);
//        mPresenter.setTabAdapter(mContext, rvTab, bean);
        if (bean.getDealerData() != null && position >= 0 && position < bean.getDealerData().size())
            tvShopName.setText(bean.getDealerData().get(position).getShopName());
        scrollToTop();
    }

    @Override
    public void onSideClick(LineAttractBean.PercentDataBean bean) {
        Map<String, Serializable> params = new HashMap<>();
        params.put(Constants.CITY_CODE, bean.getCityCode());
        params.put(Constants.START_TIME, startTime);
        params.put(Constants.END_TIME, endTime);
        params.put(Constants.TIME, time);
        params.put(Constants.TYPE, String.valueOf(bean.getType()));
        params.put(Constants.TITLE, bean.getArea());
        startFragment(params, new OnlineAttractReportFragment());
    }

    @Override
    public void onDestroyView() {
        mHsv.removeCallbacks(scrollUpRunnable);
        super.onDestroyView();
    }
}
