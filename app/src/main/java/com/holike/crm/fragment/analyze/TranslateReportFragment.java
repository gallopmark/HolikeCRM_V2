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
import com.holike.crm.R;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.TranslateReportBean;
import com.holike.crm.dialog.CalendarPickerDialog;
import com.holike.crm.presenter.fragment.OrderReportPresenter;
import com.holike.crm.presenter.fragment.TranslateReportPresenter;
import com.holike.crm.util.Constants;
import com.holike.crm.util.TimeUtil;
import com.holike.crm.view.fragment.TranslateReportView;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by wqj on 2018/4/11.
 * 订金转化报表
 */

public class TranslateReportFragment extends MyFragment<TranslateReportPresenter, TranslateReportView> implements TranslateReportView {
    @BindView(R.id.tv_translate_report_date)
    TextView tvDate;
    @BindView(R.id.ll_translate_report_main)
    LinearLayout llMain;
    @BindView(R.id.rv_translate_report)
    RecyclerView rv;

    private String cityCode;
    private String startTime;
    private String endTime;
    private String type;
    private String title;

    @Override
    protected TranslateReportPresenter attachPresenter() {
        return new TranslateReportPresenter();
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_translate_report;
    }

    @Override
    protected void init() {
        super.init();
        rv.setLayoutManager(new LinearLayoutManager(mContext));
        Bundle bundle = getArguments();
        if (bundle != null) {
            cityCode = bundle.getString(Constants.CITY_CODE);
            startTime = bundle.getString(Constants.START_TIME);
            endTime = bundle.getString(Constants.END_TIME);
            type = bundle.getString(Constants.TYPE);
            title = bundle.getString(Constants.TITLE);
        }
        setTitle(getString(R.string.report_item3_title) + (TextUtils.isEmpty(title) ? "" : "—" + title));
        setRightMenu(getString(R.string.report_select_date));
        if (startTime != null) {
            showDate(startTime, endTime);
        }
        getData();
    }

    @Override
    public void getData() {
        mPresenter.getData(cityCode, startTime, endTime, type);
        showLoading();
    }

    /**
     * 获取数据成功
     */
    @Override
    public void getDataSuccess(final TranslateReportBean bean) {
        dismissLoading();
        showDate(bean.getTimeData().getStartTime(), bean.getTimeData().getEndTime());
        if (bean.getPercentData().size() == 0) {
            noData(R.drawable.no_result, R.string.tips_noorderdata, false);
            return;
        }
        rv.setAdapter(new CommonAdapter<TranslateReportBean.PercentDataBean>(mContext, bean.getPercentData()) {
            @Override
            protected int bindView(int viewType) {
                return R.layout.item_rv_translate_report;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, TranslateReportBean.PercentDataBean percentDataBean, int position) {
                LinearLayout ll = holder.obtainView(R.id.ll_item_rv_translate_report);
                TextView tvArea = holder.obtainView(R.id.tv_item_rv_translate_report_area);
                TextView tvName = holder.obtainView(R.id.tv_item_rv_translate_report_name);
                TextView tvOrderNum = holder.obtainView(R.id.tv_item_rv_translate_report_order_num);
                TextView tvTranslateNum = holder.obtainView(R.id.tv_item_rv_translate_report_translate_num);
                TextView tvTranslate = holder.obtainView(R.id.tv_item_rv_translate_report_translate);
                TextView tvUnTranslate = holder.obtainView(R.id.tv_item_rv_untranslate_report_translate_num);
                TextView tvRank = holder.obtainView(R.id.tv_item_rv_translate_report_rank);
                ll.setBackgroundResource(position % 2 == 0 ? R.color.bg_item_order_report : R.color.color_while);
                tvArea.setTextColor(ContextCompat.getColor(mContext, percentDataBean.getIsClick() == 1 ? R.color.textColor5 : R.color.textColor8));
                tvName.setTextColor(ContextCompat.getColor(mContext, percentDataBean.getIsChange() == 1 ? R.color.textColor17 : R.color.textColor8));
                tvArea.setText(percentDataBean.getArea());
                tvName.setText(percentDataBean.getName());
                tvOrderNum.setText(String.valueOf(percentDataBean.getCounts()));
                tvTranslateNum.setText(String.valueOf(percentDataBean.getMoney()));
                tvTranslate.setText(percentDataBean.getPercent());
                tvUnTranslate.setText(percentDataBean.getNotPercent());
                tvRank.setText(TextUtils.isEmpty(percentDataBean.getRank()) ? "-" : percentDataBean.getRank());
                tvArea.setOnClickListener(v -> {
                    if (percentDataBean.getIsClick() == 1) {
                        Map<String, Serializable> params = new HashMap<>();
                        params.put(Constants.CITY_CODE, percentDataBean.getCityCode());
                        params.put(Constants.TYPE, String.valueOf(percentDataBean.getType()));
                        params.put(Constants.START_TIME, startTime);
                        params.put(Constants.END_TIME, endTime);
                        params.put(Constants.TITLE, percentDataBean.getArea());
                        startFragment(params, new TranslateReportFragment());
                    }
                });
            }
        });
    }

    /**
     * 显示时间
     */
    @Override
    public void showDate(String startTime, String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        String dateTime = TimeUtil.stampToString(startTime, "yyyy.MM.dd") + "—" + TimeUtil.stampToString(endTime, "yyyy.MM.dd");
        tvDate.setText(dateTime);
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
     * 查询日期
     */
    private List<Date> mSelectedDates;

    @Override
    protected void clickRightMenu(CharSequence text, View actionView) {
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
                    startTime = TimeUtil.dateToStamp(start, false);
                    endTime = TimeUtil.dateToStamp(end, true);
//                    startTime = TimeUtil.dataToStamp(start, "yyyy年MM月dd日");
//                    endTime = TimeUtil.dataToStamp(end, "yyyy年MM月dd日");
                } else {
                    startTime = null;
                    endTime = null;
                }
                getData();
            }
        });
    }

}
