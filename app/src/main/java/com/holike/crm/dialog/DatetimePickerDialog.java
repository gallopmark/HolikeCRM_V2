package com.holike.crm.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.contrarywind.adapter.WheelAdapter;
import com.contrarywind.view.WheelView;
import com.holike.crm.R;
import com.holike.crm.util.ParseUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import galloped.xcode.widget.TitleBar;

/*日期时间选择*/
public class DatetimePickerDialog extends CommonDialog {

    public static class Builder {
        Context mContext;
        CharSequence mTitle;
        String mType = TYPE_DEFAULT;
        Date mMinDate, mMaxDate;
        Date mSelectDate;
        CharSequence mNegativeText;
        CharSequence mPositiveText;
        OnDatetimePickerListener mListener;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setTitle(@Nullable CharSequence title) {
            this.mTitle = title;
            return this;
        }

        public Builder withType(String type) {
            this.mType = type;
            return this;
        }

        public Builder minDate(@Nullable Date minDate) {
            this.mMinDate = minDate;
            return this;
        }

        public Builder maxDate(@Nullable Date maxDate) {
            this.mMaxDate = maxDate;
            return this;
        }

        public Builder selectDate(@Nullable Date date) {
            this.mSelectDate = date;
            return this;
        }

        public Builder negativeButton(@StringRes int id) {
            return negativeButton(mContext.getString(id));
        }

        public Builder negativeButton(CharSequence text) {
            this.mNegativeText = text;
            return this;
        }

        public Builder positiveButton(@StringRes int id) {
            return negativeButton(mContext.getString(id));
        }

        public Builder positiveButton(CharSequence text) {
            this.mPositiveText = text;
            return this;
        }

        public Builder listener(OnDatetimePickerListener listener) {
            this.mListener = listener;
            return this;
        }

        public DatetimePickerDialog build() {
            return new DatetimePickerDialog(this);
        }

        public void show() {
            build().show();
        }
    }

    @SuppressWarnings("WeakerAccess")
    public static final String TYPE_DEFAULT = "Y-M-D-HMS"; //默认“年月日时分秒”
    public static final String TYPE_Y_M_D = "Y-M-D";  //年月日
    public static final String TYPE_Y_M = "Y-M"; //年月
    @SuppressWarnings("WeakerAccess")
    public static final String TYPE_Y_M_D_HM = "Y-M-D-HM"; //年月日时分
    public static final String TYPE_HM = "HM"; //时分
    @SuppressWarnings("WeakerAccess")
    public static final String TYPE_HMS = "HMS"; //时分秒

    private static final String UNIT_YEAR = "年";
    private static final String UNIT_MONTH = "月";
    private static final String UNIT_DAY = "日";
    private static final String UNIT_HOUR = "时";
    private static final String UNIT_MINUTE = "分";
    private static final String UNIT_SECOND = "秒";

    private CharSequence mTitle;
    private WheelView mYearWv, mMonthWv, mDayWv, mHourWv, mMinuteWv, mSecondWv;
    private String mType;
    private Date mMinDate, mMaxDate;  //仅针对“年月日” 的最小值和最大值
    private Date mSelectDate;
    private CharSequence mNegativeText;
    private CharSequence mPositiveText;
    private OnDatetimePickerListener mListener;

    private List<String> mYearDataList;
    private List<String> mMonthDataList;
    private List<String> mDayDataList;
    private List<String> mHourDataList;
    private List<String> mMinuteDataList;
    private List<String> mSecondDataList;

    private DatetimePickerDialog(Builder builder) {
        super(builder.mContext);
        this.mTitle = builder.mTitle;
        this.mType = builder.mType;
        this.mMinDate = builder.mMinDate;
        this.mMaxDate = builder.mMaxDate;
        this.mSelectDate = builder.mSelectDate;
        this.mNegativeText = TextUtils.isEmpty(builder.mNegativeText) ? mContext.getString(R.string.cancel) : mNegativeText;
        this.mPositiveText = TextUtils.isEmpty(builder.mPositiveText) ? mContext.getString(R.string.confirm) : mPositiveText;
        this.mListener = builder.mListener;
        initData();
        wheelInvalidate();
    }

    private void initData() {
        mYearDataList = new ArrayList<>();
        mMonthDataList = new ArrayList<>();
        mDayDataList = new ArrayList<>();
        mHourDataList = new ArrayList<>();
        mMinuteDataList = new ArrayList<>();
        mSecondDataList = new ArrayList<>();
    }

    @Override
    protected int bindContentView() {
        return R.layout.dialog_datetime_picker;
    }

    @Override
    public void setTitle(@Nullable CharSequence title) {
        ((TitleBar) mContentView.findViewById(R.id.titleBar)).setTitle(title);
    }

    /*更新*/
    private void wheelInvalidate() {
        setTitle(mTitle);
        bindView();
        setType();
        initDateWheel();
        initTimeWheel();
        setNegativeButton(mNegativeText);
        setPositiveButton(mPositiveText);
    }

    private void bindView() {
        mYearWv = mContentView.findViewById(R.id.wv_year);
        mMonthWv = mContentView.findViewById(R.id.wv_month);
        mDayWv = mContentView.findViewById(R.id.wv_day);
        mHourWv = mContentView.findViewById(R.id.wv_hour);
        mMinuteWv = mContentView.findViewById(R.id.wv_minute);
        mSecondWv = mContentView.findViewById(R.id.wv_second);
        mYearWv.setCyclic(false);
        mMonthWv.setCyclic(false);
        mDayWv.setCyclic(false);
        mHourWv.setCyclic(false);
        mMinuteWv.setCyclic(false);
        mSecondWv.setCyclic(false);
    }

    private void setType() {
        if (TextUtils.equals(mType, TYPE_Y_M_D)) {  //仅显示“年月日”
            hideHMS();
        } else if (TextUtils.equals(mType, TYPE_Y_M)) { //仅显示“年月”
            hideHMS();
            mContentView.findViewById(R.id.wv_day).setVisibility(View.GONE);
        } else if (TextUtils.equals(mType, TYPE_Y_M_D_HM)) { //仅显示“年月日时分”
            mContentView.findViewById(R.id.wv_second).setVisibility(View.GONE);
        } else if (TextUtils.equals(mType, TYPE_HM)) { //仅显示时分
            hideYMD();
            mContentView.findViewById(R.id.wv_second).setVisibility(View.GONE);
        } else if (TextUtils.equals(mType, TYPE_HMS)) {  //仅显示“时分秒”
            hideYMD();
        }
    }

    private void hideYMD() {
        mYearWv.setVisibility(View.GONE);
        mMonthWv.setVisibility(View.GONE);
        mDayWv.setVisibility(View.GONE);
    }

    private void hideHMS() {
        mHourWv.setVisibility(View.GONE);
        mMinuteWv.setVisibility(View.GONE);
        mSecondWv.setVisibility(View.GONE);
    }

    private int mOldMonthIndex = -1;
    private int mOldDayIndex = -1;

    private void initDateWheel() {
        Calendar calendar = Calendar.getInstance();
        if (mSelectDate != null) {
            calendar.setTime(mSelectDate);
        }
        int selectYear = calendar.get(Calendar.YEAR);  //当前选中的年份
        int selectMonth = calendar.get(Calendar.MONTH) + 1; //当前选中的月份
        int selectDay = calendar.get(Calendar.DAY_OF_MONTH); //当前选中的天
        mYearDataList = new ArrayList<>(getYearData());
        mYearWv.setAdapter(new CustomWheelAdapter(mYearDataList));
        int yearIndex = mYearDataList.indexOf(selectYear + UNIT_YEAR);
        if (yearIndex >= 0) {
            mYearWv.setCurrentItem(yearIndex);
        }
        mMonthDataList = new ArrayList<>(getMonthData(selectYear));
        mMonthWv.setAdapter(new CustomWheelAdapter(mMonthDataList));
        int monthIndex = mMonthDataList.indexOf(selectMonth + UNIT_MONTH);
        if (monthIndex >= 0) {
            mMonthWv.setCurrentItem(monthIndex);
        }
        mYearWv.setOnItemSelectedListener(index -> {
            int year = ParseUtils.parseInt(mYearDataList.get(index).replace(UNIT_YEAR, ""));
            setMonthWheel(year);
            setDayWheel(year, getMonth());
        });
        mMonthWv.setOnItemSelectedListener(index -> {  //月份滚动时天数要跟着改变
            int year = getYear();
            int month = ParseUtils.parseInt(mMonthDataList.get(index).replace(UNIT_MONTH, ""));
            setDayWheel(year, month);
            mOldMonthIndex = index;
        });
        mDayDataList = new ArrayList<>(getDayData(selectYear, selectMonth));
        mDayWv.setAdapter(new CustomWheelAdapter(mDayDataList));
        mDayWv.setOnItemSelectedListener(index -> mOldDayIndex = index);
        int dayIndex = mDayDataList.indexOf(selectDay + UNIT_DAY);
        if (dayIndex >= 0) {
            mDayWv.setCurrentItem(dayIndex);
        }
    }

    private List<String> getYearData() {
        List<String> list = new ArrayList<>();
        int minYear = getMinYear();
        int maxYear = getMaxYear();
        for (int i = minYear; i <= maxYear; i++) {
            list.add(i + UNIT_YEAR);
        }
        return list;
    }

    /*获取最小年份*/
    private int getMinYear() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int minYear = year - 100;  //往后100年
        if (mMinDate != null) {
            calendar.setTime(mMinDate);
            minYear = calendar.get(Calendar.YEAR);
        }
        return minYear;
    }

    /*获取最大年份*/
    private int getMaxYear() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int maxYear = year + 100;  //往后100年
        if (mMaxDate != null) {
            calendar.setTime(mMaxDate);
            maxYear = calendar.get(Calendar.YEAR);
        }
        return maxYear;
    }

    private void setMonthWheel(int year) {
        mMonthDataList = new ArrayList<>(getMonthData(year));
        mMonthWv.setAdapter(new CustomWheelAdapter(mMonthDataList));
        if (mOldMonthIndex >= 0) {
            int position = mOldMonthIndex;
            if (mOldMonthIndex >= mMonthDataList.size()) {
                position = mMonthDataList.size() - 1;
            }
            mMonthWv.setCurrentItem(position);
        }
    }

    private List<String> getMonthData(int year) {
        List<String> list = new ArrayList<>();
        int startMonth = 1;
        int endMonth = 12;
        if ((mMinDate != null && getMinYear() == year)
                || (mMaxDate != null && getMaxYear() == year)) {
            Calendar calendar = Calendar.getInstance();
            if (mMinDate != null) {
                calendar.setTime(mMinDate);
                startMonth = calendar.get(Calendar.MONTH) + 1;
            } else {
                calendar.setTime(mMaxDate);
                endMonth = calendar.get(Calendar.MONTH) + 1;
            }
        }
        for (int i = startMonth; i <= endMonth; i++) {
            list.add(i + UNIT_MONTH);
        }
        return list;
    }

    private int getMinDateMonth() {
        if (mMinDate != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(mMinDate);
            return calendar.get(Calendar.MONTH) + 1;
        }
        return 1;
    }

    private int getMaxDateMonth() {
        if (mMaxDate != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(mMaxDate);
            return calendar.get(Calendar.MONTH) + 1;
        }
        return 12;
    }

    private void setDayWheel(int year, int month) {
        mDayDataList = new ArrayList<>(getDayData(year, month));
        mDayWv.setAdapter(new CustomWheelAdapter(mDayDataList));
        if (mOldDayIndex >= 0) {
            int position = mOldDayIndex;
            if (mOldDayIndex >= mDayDataList.size()) {
                position = mDayDataList.size() - 1;
            }
            mDayWv.setCurrentItem(position);
        }
    }

    private List<String> getDayData(int year, int month) {
        List<String> list = new ArrayList<>();
        int startDay = 1;
        int endDay = getDaysBy(year, month);
        if ((mMinDate != null && getMinYear() == year && getMinDateMonth() == month)
                || (mMaxDate != null && getMaxYear() == year && getMaxDateMonth() == month)) {
            Calendar calendar = Calendar.getInstance();
            if (mMinDate != null) {
                calendar.setTime(mMinDate);
                startDay = calendar.get(Calendar.DAY_OF_MONTH);
            } else {
                calendar.setTime(mMaxDate);
                endDay = calendar.get(Calendar.DAY_OF_MONTH);
            }
        }
        for (int i = startDay; i <= endDay; i++) {
            list.add(i + UNIT_DAY);
        }
        return list;
    }

    /*获取天数*/
    private int getDaysBy(int year, int month) {
        int day;
        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
            day = 29;
        } else {
            day = 28;
        }
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            case 2:
                return day;
        }
        return 0;
    }

    private void initTimeWheel() {
        Calendar calendar = Calendar.getInstance();
        if (mSelectDate != null) {
            calendar.setTime(mSelectDate);
        }
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        mHourDataList = new ArrayList<>(getHourData());
        mHourWv.setAdapter(new CustomWheelAdapter(mHourDataList));
        int hourIndex = mHourDataList.indexOf(getTemp(hour) + UNIT_HOUR);
        if (hourIndex >= 0) {
            mHourWv.setCurrentItem(hourIndex);
        }
        mMinuteDataList = new ArrayList<>(getMinuteOrSecondData(UNIT_MINUTE));
        mMinuteWv.setAdapter(new CustomWheelAdapter(mMinuteDataList));
        int minuteIndex = mMinuteDataList.indexOf(getTemp(minute) + UNIT_MINUTE);
        if (minuteIndex >= 0) {
            mMinuteWv.setCurrentItem(minuteIndex);
        }
        mSecondDataList = new ArrayList<>(getMinuteOrSecondData(UNIT_SECOND));
        mSecondWv.setAdapter(new CustomWheelAdapter(mSecondDataList));
        int secondIndex = mSecondDataList.indexOf(getTemp(second) + UNIT_SECOND);
        if (secondIndex >= 0) {
            mSecondWv.setCurrentItem(secondIndex);
        }
    }

    private List<String> getHourData() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            list.add(getTemp(i) + UNIT_HOUR);
        }
        return list;
    }

    private List<String> getMinuteOrSecondData(String unit) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            list.add(getTemp(i) + unit);
        }
        return list;
    }

    private String getTemp(int i) {
        if (i < 10) {
            return "0" + i;
        }
        return String.valueOf(i);
    }

    public void setNegativeButton(@Nullable CharSequence text) {
        TextView tvNegative = mContentView.findViewById(R.id.tv_negative);
        if (TextUtils.isEmpty(text)) {
            tvNegative.setVisibility(View.INVISIBLE);
        } else {
            tvNegative.setVisibility(View.VISIBLE);
            tvNegative.setText(text);
            tvNegative.setOnClickListener(view -> dismiss());
        }
    }

    public void setPositiveButton(@Nullable CharSequence text) {
        TextView tvPositive = mContentView.findViewById(R.id.tv_positive);
        if (TextUtils.isEmpty(text)) {
            tvPositive.setVisibility(View.INVISIBLE);
        } else {
            tvPositive.setVisibility(View.VISIBLE);
            tvPositive.setText(text);
            tvPositive.setOnClickListener(view -> {
                if (mListener != null) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, getYear());
                    calendar.set(Calendar.MONTH, getMonth() - 1);
                    calendar.set(Calendar.DAY_OF_MONTH, getDay());
                    calendar.set(Calendar.HOUR_OF_DAY, getHour());
                    calendar.set(Calendar.MINUTE, getMinute());
                    calendar.set(Calendar.SECOND, getSecond());
                    mListener.onDatetimePicker(calendar.getTime());
                }
                dismiss();
            });
        }
    }

    public int getYear() {
        int index = mYearWv.getCurrentItem();
        if (index < 0 || index >= mYearDataList.size()) {
            Calendar calendar = Calendar.getInstance();
            return calendar.get(Calendar.YEAR);
        }
        return ParseUtils.parseInt(mYearDataList.get(index).replace(UNIT_YEAR, ""));
    }

    public int getMonth() {
        int index = mMonthWv.getCurrentItem();
        if (index < 0 || index >= mMonthDataList.size()) {
            Calendar calendar = Calendar.getInstance();
            return calendar.get(Calendar.MONTH) + 1;
        }
        return ParseUtils.parseInt(mMonthDataList.get(index).replace(UNIT_MONTH, ""));
    }

    @SuppressWarnings("WeakerAccess")
    public int getDay() {
        int index = mDayWv.getCurrentItem();
        if (index < 0 || index >= mDayDataList.size()) {
            Calendar calendar = Calendar.getInstance();
            return calendar.get(Calendar.DAY_OF_MONTH);
        }
        return ParseUtils.parseInt(mDayDataList.get(index).replace(UNIT_DAY, ""));
    }

    @SuppressWarnings("WeakerAccess")
    public int getHour() {
        int index = mHourWv.getCurrentItem();
        if (index < 0 || index >= mHourDataList.size()) {
            Calendar calendar = Calendar.getInstance();
            return calendar.get(Calendar.HOUR_OF_DAY);
        }
        return ParseUtils.parseInt(mHourDataList.get(index).replace(UNIT_HOUR, ""));
    }

    @SuppressWarnings("WeakerAccess")
    public int getMinute() {
        int index = mMinuteWv.getCurrentItem();
        if (index < 0 || index >= mMinuteDataList.size()) {
            Calendar calendar = Calendar.getInstance();
            return calendar.get(Calendar.MINUTE);
        }
        return ParseUtils.parseInt(mMinuteDataList.get(index).replace(UNIT_MINUTE, ""));
    }

    public int getSecond() {
        int index = mSecondWv.getCurrentItem();
        if (index < 0 || index >= mSecondDataList.size()) {
            Calendar calendar = Calendar.getInstance();
            return calendar.get(Calendar.SECOND);
        }
        return ParseUtils.parseInt(mSecondDataList.get(index).replace(UNIT_SECOND, ""));
    }

    @Nullable
    @Override
    public ViewGroup.MarginLayoutParams getLayoutParams() {
        return new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected boolean fullWidth() {
        return true;
    }

    @Override
    public int getWindowAnimations() {
        return R.style.Dialog_Anim;
    }

    @Override
    public int getGravity() {
        return Gravity.BOTTOM;
    }

    class CustomWheelAdapter implements WheelAdapter<String> {
        List<String> mDataList;

        CustomWheelAdapter(List<String> data) {
            this.mDataList = data;
        }

        @Override
        public int getItemsCount() {
            return mDataList.size();
        }

        @Override
        public String getItem(int index) {
            return mDataList.get(index);
        }

        @Override
        public int indexOf(String o) {
            return mDataList.indexOf(o);
        }
    }

    public interface OnDatetimePickerListener {
        void onDatetimePicker(Date date);
    }
}
