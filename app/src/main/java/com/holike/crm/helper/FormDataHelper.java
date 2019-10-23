package com.holike.crm.helper;


import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.holike.crm.R;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.base.BaseFragment;
import com.holike.crm.dialog.CalendarPickerDialog;

import java.util.Date;
import java.util.List;


/**
 * 表格类数据处理帮助类
 */
public abstract class FormDataHelper<T, V> {
    protected BaseActivity<?, ?> mActivity;
    protected V mCallback;
    protected View mFragmentView;
    protected ViewStub mViewStub;
    protected View mInflatedView;
    protected String mType, mCityCode;
    protected Date mStartDate, mEndDate;
    private List<Date> mSelectedDates;

    public FormDataHelper(BaseFragment<?, ?> fragment, V callback) {
        this.mActivity = (BaseActivity<?, ?>) fragment.getActivity();
        this.mCallback = callback;
        this.mFragmentView = fragment.getContentView();
        mViewStub = mFragmentView.findViewById(R.id.vs_form_data);
        Bundle bundle = fragment.getArguments();
        obtainBundleValue(bundle);
        boolean isAnimation = false;
        if (bundle != null) {
            isAnimation = bundle.getBoolean("isAnimation", false);
        }
        long delayMillis = 0;
        if (isAnimation) {
            delayMillis = 300;
        }
        mFragmentView.postDelayed(mAction, delayMillis);
    }

    private final Runnable mAction = this::startRequest;

    void onDestroy() {
        mFragmentView.removeCallbacks(mAction);
    }

    protected void obtainBundleValue(@Nullable Bundle bundle) {

    }

    protected abstract void startRequest();

    protected void startCalendarPicker() {
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
                            startRequest();
                        } else {
                            mStartDate = null;
                            mEndDate = null;
                            startRequest();
                        }
                    }
                }).show();
    }

    /*设置联动滚动的两个recyclerView*/
    protected void setLinkageScrolling(@NonNull RecyclerView target, @NonNull final RecyclerView follow) {
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

    protected abstract void onSuccess(T bean);

    protected void requestUpdate(T bean) {
        if (mInflatedView == null) {
            mInflatedView = mViewStub.inflate();
        }
        FrameLayout contentLayout = mInflatedView.findViewById(R.id.fl_form_data_container);
        if (contentLayout.getChildCount() >= 2) {
            contentLayout.removeViewAt(1);
        }
        final RecyclerView contentRecyclerView = mInflatedView.findViewById(R.id.rv_content);
        contentRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        contentRecyclerView.setNestedScrollingEnabled(false);
        View firstColumnView = addFirstColumn(bean, contentLayout);
        RecyclerView sideRecyclerView = firstColumnView.findViewById(R.id.rv_side);
        sideRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        bindSideAdapter(sideRecyclerView, bean);
        bindContentAdapter(contentRecyclerView, bean);
        contentLayout.addView(firstColumnView);
        /*添加第一行数据*/
        getScrollableLayout().addView(addFirstRow(bean), 0);
        setLinkageScrolling(sideRecyclerView, contentRecyclerView);
        setLinkageScrolling(contentRecyclerView, sideRecyclerView);
    }

    /*第一行数据*/
    @NonNull
    protected abstract View addFirstRow(T bean);

    /*第一列数据*/
    @NonNull
    protected abstract View addFirstColumn(T bean, FrameLayout contentLayout);

    protected abstract void bindSideAdapter(RecyclerView sideRecyclerView, T bean);

    protected abstract void bindContentAdapter(RecyclerView contentRecyclerView, T bean);

    protected LinearLayout getScrollableLayout() {
        LinearLayout scrollableLayout = mInflatedView.findViewById(R.id.ll_scrollable_content);
        if (scrollableLayout.getChildCount() >= 2) {
            scrollableLayout.removeViewAt(0);
        }
        return scrollableLayout;
    }
}
