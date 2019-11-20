package com.holike.crm.fragment.monthdata;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.holike.crm.base.BaseFragment;
import com.holike.crm.base.BaseHelper;
import com.holike.crm.dialog.CalendarPickerDialog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by pony on 2019/8/9.
 * Copyright holike possess 2019.
 */
abstract class MonthDataHelper implements BaseHelper {
    BaseFragment<?, ?> mFragment;
    Context mContext;
    View mFragmentView;
    private String mType, mCityCode;
    private Callback mCallback;
    Date mStartDate, mEndDate;
    private List<Date> mSelectedDates;

    MonthDataHelper(BaseFragment<?, ?> fragment, Callback callback) {
        this.mFragment = fragment;
        this.mContext = fragment.getContext();
        this.mCallback = callback;
        mFragmentView = fragment.getContentView();
        initView();
        Bundle bundle = fragment.getArguments();
        boolean isAnimation = false;
        if (bundle != null) {
            mType = bundle.getString("type");
            mCityCode = bundle.getString("cityCode");
            mStartDate = (Date) bundle.getSerializable("startDate");
            mEndDate = (Date) bundle.getSerializable("endDate");
            isAnimation = bundle.getBoolean("isAnimation", false);
        }
        obtainBundle(bundle);
        long delayMillis = 0;
        if (isAnimation) {
            delayMillis = 300;
        }
        mFragmentView.postDelayed(mAction, delayMillis);
    }

    abstract void initView();

    void obtainBundle(@Nullable Bundle bundle) {

    }

    private final Runnable mAction = this::onQuery;

    void onDestroy() {
        mFragmentView.removeCallbacks(mAction);
    }

    /*联动滚动*/
    void setScrollSynchronous(final RecyclerView target, final RecyclerView follow) {
        target.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
                    follow.scrollBy(dx, dy);
                }
            }
        });
        follow.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
                    target.scrollBy(dx, dy);
                }
            }
        });
    }

    void onQuery() {
        mCallback.onQuery(mType, mCityCode, mStartDate, mEndDate);
    }

    void onSelectCalendar() {
        new CalendarPickerDialog.Builder(mContext).maxDate(new Date())
                .withSelectedDates(mSelectedDates)
                .clickToClear(true)
                .calendarRangeSelectedListener(new CalendarPickerDialog.OnCalendarRangeSelectedListener() {
                    @Override
                    public void onLeftClicked(CalendarPickerDialog dialog) {

                    }

                    @Override
                    public void onRightClick(CalendarPickerDialog dialog, List<Date> selectedDates, Date start, Date end) {
                        dialog.dismiss();
                        mSelectedDates = new ArrayList<>(selectedDates);
                        if (selectedDates.size() >= 1) {
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
