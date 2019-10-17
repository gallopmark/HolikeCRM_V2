package com.holike.crm.fragment.monthdata;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.holike.crm.base.BaseActivity;
import com.holike.crm.dialog.CalendarPickerDialog;

import java.util.Date;
import java.util.List;

/**
 * Created by gallop on 2019/8/9.
 * Copyright holike possess 2019.
 */
abstract class MonthDataHelper {
    BaseActivity<?, ?> mActivity;
    String mType, mCityCode;
    private Callback mCallback;
    Date mStartDate, mEndDate;
    private List<Date> mSelectedDates;

    MonthDataHelper(BaseActivity<?, ?> activity, Callback callback) {
        this.mActivity = activity;
        this.mCallback = callback;
    }

    /*联动滚动*/
    void setScrollSynchronous(RecyclerView target, final RecyclerView follow) {
        target.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
                    follow.scrollBy(dx, dy);
                }
            }
        });
    }

    void onQuery() {
        mCallback.onQuery(mType, mCityCode, mStartDate, mEndDate);
    }

    void onSelectCalendar() {
        new CalendarPickerDialog.Builder(mActivity).maxDate(new Date())
                .withSelectedDates(mSelectedDates)
                .clickToClear(true)
                .calendarRangeSelectedListener(new CalendarPickerDialog.OnCalendarRangeSelectedListener() {
                    @Override
                    public void onLeftClicked(CalendarPickerDialog dialog) {

                    }

                    @Override
                    public void onRightClick(CalendarPickerDialog dialog, List<Date> selectedDates, Date start, Date end) {
                        dialog.dismiss();
                        if (selectedDates.size() >= 1) {
                            mSelectedDates = selectedDates;
                            mStartDate = start;
                            mEndDate = end;
                            onQuery();
                        } else {
                            mStartDate = null;
                            mEndDate = null;
                            onQuery();
                        }
                    }
                }).show();
    }

    interface Callback {
        void onQuery(String type, String cityCode, Date startDate, Date endDate);
    }
}
