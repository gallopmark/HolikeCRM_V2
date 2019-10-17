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
import com.holike.crm.base.IntentValue;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.OriginalBoardBean;
import com.holike.crm.dialog.CalendarPickerDialog;
import com.holike.crm.presenter.fragment.OrderReportPresenter;
import com.holike.crm.presenter.fragment.OriginalBoardPresenter;
import com.holike.crm.util.Constants;
import com.holike.crm.util.ParseUtils;
import com.holike.crm.util.TimeUtil;
import com.holike.crm.view.fragment.OriginalBoardView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by wqj on 2018/5/25.
 * 原态板占比（非经销商）
 */

public class OriginalBoardRatioDefaultFragment extends MyFragment<OriginalBoardPresenter, OriginalBoardView> implements OriginalBoardView {
    public static OriginalBoardRatioDefaultFragment newInstance(OriginalBoardBean bean) {
        IntentValue.getInstance().put(Constants.ORIGINAL_BOARD_BEAN, bean);
        return new OriginalBoardRatioDefaultFragment();
    }

    @BindView(R.id.ll_original_board_main)
    LinearLayout llMain;
    @BindView(R.id.tab_original_board_type)
    CommonTabLayout tabType;
    @BindView(R.id.tv_original_board_table_time)
    TextView tvTableTime;
    @BindView(R.id.rv_original_board)
    RecyclerView rv;

    protected ArrayList<CustomTabEntity> mTabEntities;
    protected String cityCode;
    protected String type;
    protected String time;
    protected String title;
    private String startTime;
    private String endTime;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_original_board;
    }

    @Override
    protected OriginalBoardPresenter attachPresenter() {
        return new OriginalBoardPresenter();
    }

    @Override
    protected void init() {
        super.init();
        setStatusBar(R.color.bg_state_bar);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        Bundle bundle = getArguments();
        if (bundle != null) {
            cityCode = bundle.getString(Constants.CITY_CODE);
            startTime = bundle.getString(Constants.START_TIME);
            endTime = bundle.getString(Constants.END_TIME);
            type = bundle.getString(Constants.TYPE);
            title = bundle.getString(Constants.TITLE);
            time = bundle.getString(Constants.TIME);
            getData();
        } else {
            OriginalBoardBean bean = (OriginalBoardBean) IntentValue.getInstance().removeBy(Constants.ORIGINAL_BOARD_BEAN);
            getDataSuccess(bean);
        }
        setTitle(getString(R.string.report_item10_title) + (TextUtils.isEmpty(title) ? "" : "—" + title));
    }

    @Override
    public void getData() {
        showLoading();
        mPresenter.getData(cityCode, startTime, endTime, time, type);
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
     * 获取数据成功
     */
    @Override
    public void getDataSuccess(OriginalBoardBean originalBoardBean) {
        dismissLoading();
        initTab(originalBoardBean.getTime());
        tvTableTime.setText(originalBoardBean.getTimeData());
        rv.setAdapter(new CommonAdapter<OriginalBoardBean.PercentDataBean>(mContext, originalBoardBean.getPercentData()) {
            @Override
            protected int bindView(int viewType) {
                return R.layout.item_rv_original_board_report;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, OriginalBoardBean.PercentDataBean percentDataBean, int position) {
                LinearLayout ll = holder.obtainView(R.id.ll_item_rv_original_board);
                TextView tvArea = holder.obtainView(R.id.tv_item_rv_original_board_divede);
                TextView tvName = holder.obtainView(R.id.tv_item_rv_original_board_head);
                TextView tvProportion = holder.obtainView(R.id.tv_item_rv_original_board_proportion);
                ll.setBackgroundResource(position % 2 == 0 ? R.color.bg_item_order_report : R.color.color_while);
                tvArea.setTextColor(ContextCompat.getColor(mContext, percentDataBean.getIsClick() == 1 ? R.color.textColor5 : R.color.textColor8));
                tvName.setTextColor(ContextCompat.getColor(mContext, percentDataBean.getIsChange() == 1 ? R.color.textColor17 : R.color.textColor8));
                tvArea.setText(percentDataBean.getArea());
                tvName.setText(percentDataBean.getName());
                String percent = TextUtils.isEmpty(percentDataBean.getPercent()) ? "" + "%" : percentDataBean.getPercent() + "%";
                tvProportion.setText(percent);
                tvArea.setOnClickListener(v -> {
                    if (percentDataBean.getIsClick() == 1) {
                        Map<String, Serializable> params = new HashMap<>();
                        params.put(Constants.CITY_CODE, percentDataBean.getCityCode());
                        params.put(Constants.START_TIME, startTime);
                        params.put(Constants.END_TIME, endTime);
                        params.put(Constants.TYPE, String.valueOf(percentDataBean.getType()));
                        params.put(Constants.TITLE, percentDataBean.getArea());
                        params.put(Constants.TIME, time);
                        startFragment(params, new OriginalBoardRatioDefaultFragment());
                    }
                });
            }
        });
    }

    /**
     * 切换导航
     */
    private void initTab(String item) {
        if (mTabEntities == null) {
            mTabEntities = new ArrayList<>();
            mTabEntities.add(new TabEntity(item, 0, 0));
            mTabEntities.add(new TabEntity("全年", 0, 0));
            mTabEntities.add(new TabEntity("查询日期", 0, 0));
            tabType.setTabData(mTabEntities);
            tabType.setOnTabSelectListener(new OnTabSelectListener() {
                @Override
                public void onTabSelect(int position) {
                    tabSelect(position);
                }

                @Override
                public void onTabReselect(int position) {
                    tabSelect(position);
                }
            });
            tabType.setCurrentTab(TextUtils.isEmpty(time) ? 0 : ParseUtils.parseInt(time) - 1);
        }
    }

    /**
     * 导航选择
     */
    public void tabSelect(int position) {
        time = String.valueOf(position + 1);
        if (position == mTabEntities.size() - 1) {
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
                    time = String.valueOf(1);
                    startTime = null;
                    endTime = null;
                }
                getData();
            }
        });
    }
}
