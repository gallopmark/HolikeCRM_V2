package com.holike.crm.fragment.report;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.grallopmark.tablayout.CommonTabLayout;
import com.grallopmark.tablayout.TabEntity;
import com.grallopmark.tablayout.listener.CustomTabEntity;
import com.grallopmark.tablayout.listener.OnTabSelectListener;
import com.holike.crm.R;
import com.holike.crm.adapter.AbsFormAdapter;
import com.holike.crm.base.BaseFragment;
import com.holike.crm.base.FragmentHelper;
import com.holike.crm.base.IntentValue;
import com.holike.crm.bean.OnlineDrainageBean;
import com.holike.crm.dialog.CalendarPickerDialog;
import com.holike.crm.dialog.PlainTextTipsDialog;
import com.holike.crm.helper.CalendarPickerHelper;
import com.holike.crm.helper.FormDataHelper;
import com.holike.crm.util.NumberUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class OnlineDrainageHelper extends FragmentHelper {
    private Callback mCallback;
    private String mArea;
    private String mTime;
    private String mType, mCityCode;
    private Date mStartDate, mEndDate;
    private List<Date> mSelectDate;
    private FrameLayout mContainer;
    private int mScreenWidth; //屏幕宽度
    private int mMaxTextWidth; //做多文字的textView总长度
    private int mBarMaxWidth; //最大值所占的屏幕宽度
    private long mMaxValue; //最大值
    private Handler mHandler;

    OnlineDrainageHelper(BaseFragment<?, ?> fragment, Callback callback) {
        super(fragment);
        this.mCallback = callback;
        String title = mContext.getString(R.string.online_drainage_report_title);
        String subTitle = initBundle(fragment.getArguments());
        if (!TextUtils.isEmpty(subTitle)) {
            title += "-" + subTitle;
        }
        fragment.setTitle(title);
        initTabLayout();
        mContainer = obtainView(R.id.fl_container);
        mScreenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
        Object obj = IntentValue.getInstance().removeBy("online-drainage");
        if (obj != null) {
            onSuccess((OnlineDrainageBean) obj);
        } else {
            requestByDelay();
        }
    }

    private String initBundle(Bundle bundle) {
        if (bundle != null) {
            mArea = bundle.getString("area");
            mTime = bundle.getString("time");
            mType = bundle.getString("type");
            mCityCode = bundle.getString("cityCode");
            mStartDate = (Date) bundle.getSerializable("startDate");
            mEndDate = (Date) bundle.getSerializable("endDate");
            return bundle.getString("title");
        }
        return "";
    }

    private void initTabLayout() {
        CommonTabLayout tabLayout = obtainView(R.id.tab_layout);
        List<CustomTabEntity> tabEntities = new ArrayList<>();
        tabEntities.add(new TabEntity(mContext.getString(R.string.tab_this_month)));
        tabEntities.add(new TabEntity(mContext.getString(R.string.tab_annual)));
        tabEntities.add(new TabEntity(mContext.getString(R.string.tab_query_date)));
        tabLayout.setTabData(tabEntities);
        if (TextUtils.equals(mTime, "1")) { //全年
            tabLayout.setCurrentTab(1);
        } else if (TextUtils.equals(mTime, "2")) { //本月
            tabLayout.setCurrentTab(0);
        } else if (TextUtils.equals(mTime, "3")) { //查询日期
            tabLayout.setCurrentTab(2);
        }
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                if (position == 0) { //本月
                    mTime = "2";
                    doRequest();
                } else if (position == 1) { //全年
                    mTime = "1";
                    doRequest();
                } else {
                    mTime = "3";
                    onSelectDates();
                }
            }

            @Override
            public void onTabReselect(int position) {
                if (position == 2) {
                    onSelectDates();
                }
            }
        });
    }

    private void onSelectDates() {
        CalendarPickerHelper.showCalendarDialog(mContext, mSelectDate, new CalendarPickerHelper.SimpleCalendarPickerListener() {

            @Override
            public void onCalendarPicker(CalendarPickerDialog dialog, List<Date> selectedDates, Date start, Date end) {
                mSelectDate = selectedDates;
                if (!selectedDates.isEmpty()) {
                    mStartDate = start;
                    mEndDate = end;
                    doRequest();
                } else {
                    mStartDate = null;
                    mEndDate = null;
                }
            }
        });
    }

    private void requestByDelay() {
        if (mHandler == null)
            mHandler = new Handler();
        mHandler.postDelayed(this::doRequest, 300);
    }

    void doRequest() {
        mCallback.onRequest(mTime, mType, mCityCode, mStartDate, mEndDate);
    }

    void onSuccess(OnlineDrainageBean bean) {
        mContainer.removeAllViews();
        View contentView = updateTopLayout(bean);
        setFormData(bean.getPercentData(), contentView, bean.isPrice());
    }

    private View updateTopLayout(OnlineDrainageBean bean) {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.include_online_drainage_content, mContainer, true);
        contentView.findViewById(R.id.tv_question_mark).setOnClickListener(view -> showDescription(bean.isDealer() ? R.array.online_drainage_dealer_tips : R.array.online_drainage_personal_tips));  //点击“？”弹出数据说明对话框
        ((TextView) contentView.findViewById(R.id.tv_time_detail)).setText(bean.timeData);  //显示时间段
        TextView tvShop = contentView.findViewById(R.id.tv_shop_detail);
        if (!TextUtils.isEmpty(mArea)) {  //显示门店
            tvShop.setText(mArea);
            tvShop.setVisibility(View.VISIBLE);
        } else {
            tvShop.setVisibility(View.GONE);
        }
        TextView measureTextView = contentView.findViewById(R.id.tv_effective_customers_tips);
        mMaxTextWidth = (int) measureTextView.getPaint().measureText(measureTextView.getText().toString());
        TextView rateTextView = contentView.findViewById(R.id.tv_rate_one);
        Paint paint = rateTextView.getPaint();
        String rateOne = setPercent(rateTextView, bean.getCustomerTotal(), bean.getCustomerYes());
        String rateTwo = setPercent(contentView.findViewById(R.id.tv_rate_two), bean.getScaleCount(), bean.getCustomerYes());
        String rateThree = setPercent(contentView.findViewById(R.id.tv_rate_three), bean.getEarnestCount(), bean.getScaleCount());
        String rateFour = setPercent(contentView.findViewById(R.id.tv_rate_four), bean.getSigningCount(), bean.getEarnestCount());
        /*颜色块最大长度 即最大值所占的宽度*/
        mBarMaxWidth = mScreenWidth - mContext.getResources().getDimensionPixelSize(R.dimen.dp_10)
                - mMaxTextWidth - getMaxPercentTextWidth(paint, rateOne, rateTwo, rateThree, rateFour);
        mMaxValue = getTotal(bean.getCustomerTotal(), bean.getCustomerYes(), bean.getScaleCount(), bean.getEarnestCount(), bean.getSigningCount());
        int cbOneWidth = setColorBlock(contentView.findViewById(R.id.fl_first_layout), bean.getCustomerTotal());
        setText(contentView.findViewById(R.id.tv_first_count), bean.customerTotal); //下发客户数
        int cbTwoWidth = setColorBlock(contentView.findViewById(R.id.fl_effective_customers), bean.getCustomerYes());
        setText(contentView.findViewById(R.id.tv_effective_customers), bean.customerYes); //有效客户数
        setRateWidth(contentView.findViewById(R.id.tv_rate_one), Math.max(cbOneWidth, cbTwoWidth));
        int cbThreeWidth = setColorBlock(contentView.findViewById(R.id.fl_scaleCount), bean.getScaleCount());
        setText(contentView.findViewById(R.id.tv_scaleCount), bean.scaleCount); //量尺数
        setRateWidth(contentView.findViewById(R.id.tv_rate_two), Math.max(cbTwoWidth, cbThreeWidth));
        int cbFourWidth = setColorBlock(contentView.findViewById(R.id.fl_deposit_count), bean.getEarnestCount());
        setText(contentView.findViewById(R.id.tv_deposit_count), bean.earnestCount); //订金数
        setRateWidth(contentView.findViewById(R.id.tv_rate_three), Math.max(cbThreeWidth, cbFourWidth));
        int cbFiveWidth = setColorBlock(contentView.findViewById(R.id.fl_sign_count), bean.getSigningCount());
        setText(contentView.findViewById(R.id.tv_sign_count), bean.signingCount);
        setRateWidth(contentView.findViewById(R.id.tv_rate_four), Math.max(cbFourWidth, cbFiveWidth));
        ((TextView) contentView.findViewById(R.id.tv_pie_one_rate)).setText(bean.signingCountPercent); //分配转化率
        ((TextView) contentView.findViewById(R.id.tv_pie_tow_rate)).setText(bean.customerYesPercent); //有效客户转化率
        return contentView;
    }

    private void showDescription(int arrayId) {
        new PlainTextTipsDialog(mContext).setData(arrayId).show();
    }

    private void setFormData(final List<OnlineDrainageBean.PercentDataBean> dataList, View contentView, final boolean isPrice) {
        FormDataHelper.attachView(contentView, new FormDataHelper.FormDataBinder() {
            @Override
            public int bindFirstColumnLayoutRes() {
                return R.layout.include_form_data_column_60dp;
            }

            @Override
            public CharSequence bindSideTitle() {
                return mContext.getString(R.string.division);
            }

            @Override
            public int bindFirstRowLayoutRes() {
                return R.layout.include_online_drainage_firstrow;
            }

            @Override
            public RecyclerView.Adapter bindSideAdapter() {
                return new SimpleFormAdapter(mContext, dataList, true, isPrice);
            }

            @Override
            public RecyclerView.Adapter bindContentAdapter() {
                return new SimpleFormAdapter(mContext, dataList, false, isPrice);
            }
        }, new FormDataHelper.SimpleFormDataCallback() {
            @Override
            public void bindFirstRow(View view) {
                view.findViewById(R.id.ll_order_layout).setVisibility(isPrice ? View.VISIBLE : View.GONE);
            }
        });
    }

    private final class SimpleFormAdapter extends AbsFormAdapter<OnlineDrainageBean.PercentDataBean> {
        private boolean mSide;
        private int mLayoutResourceId;
        private boolean isPrice;

        SimpleFormAdapter(Context context, List<OnlineDrainageBean.PercentDataBean> dataList, boolean isSide, boolean isPrice) {
            super(context, dataList);
            mSide = isSide;
            this.isPrice = isPrice;
            if (mSide) {
                mLayoutResourceId = R.layout.item_online_drainage_form_side;
            } else {
                mLayoutResourceId = R.layout.item_online_drainage_form_content;
            }
        }

        @Override
        protected void bindViewHolder(RecyclerHolder holder, OnlineDrainageBean.PercentDataBean bean, int position) {
            holder.setText(R.id.tv_division, bean.area); //划分
            holder.setText(R.id.tv_issued_count, bean.customerTotal); //下发客户数
            holder.setText(R.id.tv_effective_customers, bean.customerYes); //有效客户数
            holder.setText(R.id.tv_invalid_customers, bean.customerNo); //无效客户数
            holder.setText(R.id.tv_invalid_rate, bean.customerNoPercent); //无效率
            holder.setText(R.id.tv_scale_count, bean.scaleCount); //量尺数
            holder.setText(R.id.tv_scale_rate, bean.scaleCountPercent); //量尺率
            holder.setText(R.id.tv_earnest_count, bean.earnestCount); //订金数
            holder.setText(R.id.tv_earnest_rate, bean.earnestPercent); //订金转化率
            holder.setText(R.id.tv_earnest_money, bean.earnestMoney); //订金金额
            holder.setText(R.id.tv_order_count, bean.orderCounts); //订单数
            holder.setText(R.id.tv_sign_count, bean.signingCount); //签约数
            holder.setText(R.id.tv_sign_rate, bean.signingCountPercent); //合同转化率
            holder.setText(R.id.tv_sign_money, bean.signingMoney); //签约金额
            if (isPrice) {
                holder.setVisibility(R.id.ll_order_layout, View.VISIBLE);
                holder.setText(R.id.tv_order_count, bean.orderCounts); //订单数
                holder.setText(R.id.tv_order_rate, bean.orderPercent); //订单转化率
                holder.setText(R.id.tv_order_money, bean.orderMoney); //订单金额
            } else {
                holder.setVisibility(R.id.ll_order_layout, View.GONE);
            }
            if (bean.isClickable()) {
                holder.setTextColorRes(R.id.tv_division, R.color.colorAccent);
            } else {
                holder.setTextColorRes(R.id.tv_division, R.color.textColor8);
            }
            holder.setEnabled(R.id.tv_division, mSide && bean.isClickable());
            holder.setOnClickListener(R.id.tv_division, view -> startNextLevel(bean.area, bean.type, bean.cityCode));
        }

        @Override
        protected int bindView(int viewType) {
            return mLayoutResourceId;
        }
    }

    private void startNextLevel(String area, String type, String cityCode) {
        Bundle bundle = new Bundle();
        bundle.putString("area", area);
        bundle.putString("time", mTime);
        bundle.putString("type", type);
        bundle.putString("cityCode", cityCode);
        bundle.putSerializable("startDate", mStartDate);
        bundle.putSerializable("endDate", mEndDate);
        mFragment.startFragment(new OnlineDrainageFragment(), bundle, true);
    }

    /*最大占比文本所占的宽度*/
    private int getMaxPercentTextWidth(Paint paint, String... texts) {
        int textMaxWidth = mContext.getResources().getDimensionPixelSize(R.dimen.dp_80);
        for (String text : texts) {
            float textWidth = paint.measureText(text);
            if (textWidth > textMaxWidth) {
                textMaxWidth = (int) textWidth;
            }
        }
        return textMaxWidth + mContext.getResources().getDimensionPixelSize(R.dimen.dp_10);
    }

    /*获取数值的最大值*/
    private long getTotal(long... values) {
        long max = 0;
        for (long value : values) {
            max = Math.max(value, max);
        }
        return max;
    }

    /*设置色块占比*/
    private int setColorBlock(FrameLayout layout, long value) {
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) layout.getLayoutParams();
        int barWidth;
        if (mMaxValue <= 0) {
            barWidth = 0;
        } else {
            float percent = (float) value / mMaxValue;
            barWidth = (int) (percent * mBarMaxWidth);
        }
        params.width = barWidth;
        return barWidth;
    }

    /*显示数值*/
    private void setText(TextView tv, String value) {
        tv.setText(value);
    }

    /*显示百分比*/
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

    /*转化率textView所占宽度*/
    private void setRateWidth(TextView tv, int barWidth) {
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) tv.getLayoutParams();
        params.width = mScreenWidth - mContext.getResources().getDimensionPixelSize(R.dimen.dp_10)
                - mMaxTextWidth - barWidth
                - mContext.getResources().getDimensionPixelSize(R.dimen.dp_12)
                - mContext.getResources().getDimensionPixelSize(R.dimen.dp_4);
    }

    void onFailure(String failReason) {
        mContainer.removeAllViews();
        LayoutInflater.from(mContext).inflate(R.layout.include_empty_page, mContainer, true);
        mFragment.noNetwork(failReason);
    }

    void destroy() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    interface Callback {
        void onRequest(String time, String type, String cityCode, Date startDate, Date endDate);
    }
}
