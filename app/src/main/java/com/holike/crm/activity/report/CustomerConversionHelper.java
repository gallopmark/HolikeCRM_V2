package com.holike.crm.activity.report;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.grallopmark.tablayout.CommonTabLayout;
import com.grallopmark.tablayout.TabEntity;
import com.grallopmark.tablayout.listener.CustomTabEntity;
import com.grallopmark.tablayout.listener.OnTabSelectListener;
import com.holike.crm.R;
import com.holike.crm.base.ActivityHelper;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.bean.CustomerConversionBean;
import com.holike.crm.bean.DictionaryBean;
import com.holike.crm.dialog.CalendarPickerDialog;
import com.holike.crm.helper.PickerHelper;
import com.holike.crm.util.NumberUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by pony on 2019/10/31.
 * Version v3.0 app报表
 */
class CustomerConversionHelper extends ActivityHelper {

    private Callback mCallback;
    private CommonTabLayout mTabLayout;
    private TextView mSelectShopTextView;
    private FrameLayout mContainer;
    private Date mStartDate, mEndDate;
    private List<Date> mSelectDates;
    private String mShopId;
    private int mScreenWidth;
    private int mMaxTextWidth;
    private int mBarMaxWidth;
    private long mMaxValue;  //最大值
    private List<CustomerConversionBean.SelectShopBean> mShopList;

    CustomerConversionHelper(BaseActivity<?, ?> activity, Callback callback) {
        super(activity);
        this.mCallback = callback;
        mContainer = obtainView(R.id.fl_container);
        mTabLayout = obtainView(R.id.tab_layout);
        setTabLayout();
        mSelectShopTextView = obtainView(R.id.tv_select_shop);
        mSelectShopTextView.setOnClickListener(view -> selectShop());
        mScreenWidth = activity.getResources().getDisplayMetrics().widthPixels;
        doRequest();
    }

    private void setTabLayout() {
        List<CustomTabEntity> tabEntities = new ArrayList<>();
        tabEntities.add(new TabEntity(mActivity.getString(R.string.tab_annual)));
        tabEntities.add(new TabEntity(mActivity.getString(R.string.tab_query_date)));
        mTabLayout.setTabData(tabEntities);
        mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                if (position == 0) {  //全年
                    mStartDate = null;
                    mEndDate = null;
                    doRequest();
                } else {
                    showCalendar();
                }
            }

            @Override
            public void onTabReselect(int position) {
                if (position == 1) {
                    showCalendar();
                }
            }
        });
    }

    void doRequest() {
        mCallback.onRequest(mStartDate, mEndDate, mShopId);
    }

    /*选择日期*/
    private void showCalendar() {
        new CalendarPickerDialog.Builder(mActivity)
                .clickToClear(true)
                .withSelectedDates(mSelectDates)
                .maxDate(new Date())
                .calendarRangeSelectedListener(new CalendarPickerDialog.OnCalendarRangeSelectedListener() {
                    @Override
                    public void onLeftClicked(CalendarPickerDialog dialog) {

                    }

                    @Override
                    public void onRightClick(CalendarPickerDialog dialog, List<Date> selectedDates, Date start, Date end) {
                        mSelectDates = selectedDates;
                        if (selectedDates.size() >= 1) {
                            mStartDate = start;
                            mEndDate = end;
                            doRequest();
                        } else {
                            mStartDate = null;
                            mEndDate = null;
                        }
                        dialog.dismiss();
                    }
                }).show();
    }

    /*选择门店*/
    private void selectShop() {
        if (mShopList != null && !mShopList.isEmpty()) {
            List<DictionaryBean> optionItems = new ArrayList<>();
            for (CustomerConversionBean.SelectShopBean bean : mShopList) {
                optionItems.add(new DictionaryBean(bean.shopId, bean.shopName));
            }
            PickerHelper.showOptionsPicker(mActivity, optionItems, ((position, bean) -> {
                mShopId = bean.id;
                mSelectShopTextView.setText(bean.name);
                doRequest();
            }));
        }
    }

    /**
     * 量尺转化率=签约数/量尺客户数
     * 订金转化率=签约数/订金数
     * 进店成交率=签约数/新建客户数
     * 量尺成交率=签约数/量尺数
     */
    void onSuccess(CustomerConversionBean bean) {
        mShopList = new ArrayList<>(bean.getSelectShop());
        mContainer.removeAllViews();
        View contentView = LayoutInflater.from(mActivity).inflate(R.layout.include_customer_conversion_content, mContainer, true);
        ((TextView) contentView.findViewById(R.id.tv_time_detail)).setText(bean.timeDetail);
        TextView measureTextView = contentView.findViewById(R.id.tv_newCount_tips);
        mMaxTextWidth = (int) measureTextView.getPaint().measureText(measureTextView.getText().toString());
        /*颜色块最大长度 即“新建客户数”所占的宽度*/
        TextView rateTextView = contentView.findViewById(R.id.tv_rate_one);
        Paint paint = rateTextView.getPaint();
        String rateOne = setPercent(rateTextView, bean.getPreScaleCount(), bean.getNewCount()); //预约量尺数/新建客户数
        String rateTwo = setPercent(contentView.findViewById(R.id.tv_rate_two), bean.getScaleCount(), bean.getPreScaleCount()); //量尺数/预约量尺数
        String rateThree = setPercent(contentView.findViewById(R.id.tv_rate_three), bean.getOrdersCustomer(), bean.getScaleCount());
        String rateFour = setPercent(contentView.findViewById(R.id.tv_rate_four), bean.getContractCount(), bean.getOrdersCustomer());
        mBarMaxWidth = mScreenWidth - mActivity.getResources().getDimensionPixelSize(R.dimen.dp_10) - mMaxTextWidth - getMaxPercentTextWidth(paint, rateOne, rateTwo, rateThree, rateFour);
        mMaxValue = getTotal(bean);
        int cbOneWidth = setColorBlock(contentView.findViewById(R.id.fl_newCount), bean.getNewCount());
        setText(contentView.findViewById(R.id.tv_newCount), bean.getNewCount());  //新建客户数
        int cbTwoWidth = setColorBlock(contentView.findViewById(R.id.fl_preScaleCount), bean.getPreScaleCount()); //预约量尺色块
        setText(contentView.findViewById(R.id.tv_preScaleCount), bean.getPreScaleCount()); //预约量尺数
        setRateWidth(contentView.findViewById(R.id.tv_rate_one), Math.max(cbOneWidth, cbTwoWidth));
        int cbThreeWidth = setColorBlock(contentView.findViewById(R.id.fl_scaleCount), bean.getScaleCount()); //预约量尺色块
        setText(contentView.findViewById(R.id.tv_scaleCount), bean.getScaleCount()); //量尺数
        setRateWidth(contentView.findViewById(R.id.tv_rate_two), Math.max(cbTwoWidth, cbThreeWidth));
        int cbFourWidth = setColorBlock(contentView.findViewById(R.id.fl_deposit_count), bean.getOrdersCustomer()); //订金数色块
        setText(contentView.findViewById(R.id.tv_deposit_count), bean.getOrdersCustomer()); //订金数
        setRateWidth(contentView.findViewById(R.id.tv_rate_three), Math.max(cbThreeWidth, cbFourWidth));
        int cbFiveWidth = setColorBlock(contentView.findViewById(R.id.fl_sign_count), bean.getContractCount());
        setRateWidth(contentView.findViewById(R.id.tv_rate_four), Math.max(cbFourWidth, cbFiveWidth)); //订金数/签约数
        setText(contentView.findViewById(R.id.tv_sign_count), bean.getContractCount());
        //转化率
        ((TextView) contentView.findViewById(R.id.tv_scale_conversion_rate)).setText(bean.scaleCountPercent); //量尺转化率
        ((TextView) contentView.findViewById(R.id.tv_deposit_conversion_rate)).setText(bean.ordersCustomerPercent); //订金转化率
        ((TextView) contentView.findViewById(R.id.tv_entry_turnover_rate)).setText(bean.entryPercent); //进店成交率
        ((TextView) contentView.findViewById(R.id.tv_scale_turnover_rate)).setText(bean.scalePercent); //量尺成家率
    }

    /*最大占比文本所占的宽度*/
    private int getMaxPercentTextWidth(Paint paint, String... texts) {
        int textMaxWidth = mActivity.getResources().getDimensionPixelSize(R.dimen.dp_80);
        for (String text : texts) {
            float textWidth = paint.measureText(text);
            if (textWidth > textMaxWidth) {
                textMaxWidth = (int) textWidth;
            }
        }
        return textMaxWidth + mActivity.getResources().getDimensionPixelSize(R.dimen.dp_10);
    }

    /*获取最大值*/
    private long getTotal(CustomerConversionBean bean) {
        long max = Math.max(bean.getNewCount(), bean.getPreScaleCount());
        max = Math.max(max, bean.getScaleCount());
        max = Math.max(max, bean.getOrdersCustomer());
        max = Math.max(max, bean.getContractCount());
        return max;
    }

    /*展示颜色条*/
    private int setColorBlock(FrameLayout layout, long value) {
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) layout.getLayoutParams();
        int barWidth;
        if (mMaxValue == 0) {
            barWidth = 0;
        } else {
            float percent = (float) value / mMaxValue;
            barWidth = (int) (percent * mBarMaxWidth);
        }
        params.width = barWidth;
        return barWidth;
    }

    private void setText(TextView tv, long count) {
        tv.setText(String.valueOf(count));
    }

    private String setPercent(TextView tv, long value, long total) {
        if (total == 0) {  //除数为0
            tv.setVisibility(View.GONE);
            return "";
        }
        String percent = NumberUtil.doubleValue((float) value * 100 / total, 1) + "%";
        tv.setVisibility(View.VISIBLE);
        tv.setText(percent);
        return percent;
    }

    private void setRateWidth(TextView tv, int max) {
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) tv.getLayoutParams();
        params.width = mScreenWidth - mActivity.getResources().getDimensionPixelSize(R.dimen.dp_10)
                - mMaxTextWidth - max
                - mActivity.getResources().getDimensionPixelSize(R.dimen.dp_12)
                - mActivity.getResources().getDimensionPixelSize(R.dimen.dp_4);
    }

    void onFailure(String failReason) {
        mContainer.removeAllViews();
        LayoutInflater.from(mActivity).inflate(R.layout.include_empty_page, mContainer, true);
        mActivity.noNetwork(failReason);
    }

    interface Callback {
        void onRequest(Date startDate, Date endDate, String shopId);
    }
}
