package com.holike.crm.fragment.employee.child;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.holike.crm.R;
import com.holike.crm.base.BaseFragment;
import com.holike.crm.bean.EmployeeRankingBean;
import com.holike.crm.helper.TextSpanHelper;

import java.util.Date;
import java.util.List;

abstract class CommonDealerHelper extends EmployeeRankHelper {

    private Callback mCallback;
    private TextView mDatetimeTextView;
    TextView mDataTextView;
    private TextView mRankTextView;
    private FrameLayout mContentLayout;

    CommonDealerHelper(BaseFragment<?, ?> fragment, Callback callback) {
        super(fragment);
        this.mCallback = callback;
        initView();
    }

    private void initView() {
        mDatetimeTextView = obtainView(R.id.tv_time_detail);
        mDatetimeTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.ic_choice_date_down), null);
        mDataTextView = obtainView(R.id.tv_data);
        mRankTextView = obtainView(R.id.tv_rank);
        mContentLayout = obtainView(R.id.fl_content_layout);
        mDatetimeTextView.setOnClickListener(view -> onCalendarPicker());
        mDatetimeTextView.setEnabled(false);
    }

    @Override
    void onShowCalendar() {
        mDatetimeTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.ic_choice_date_up), null);
    }

    @Override
    void onDismissCalendar() {
        mDatetimeTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.ic_choice_date_down), null);
    }

    void doRequest() {
        mCallback.doRequest(mStartDate, mEndDate);
    }

    void onSuccess(EmployeeRankingBean bean) {
        mDatetimeTextView.setEnabled(true);
        mDatetimeTextView.setText(bean.title);
        String textRank = "";
        EmployeeRankingBean.MyRankingBean rankingBean = bean.getMyRank();
        if (rankingBean != null) {
            textRank += TextUtils.isEmpty(bean.getMyRank().myselfRank) ? "" : bean.getMyRank().myselfRank;
        }
        mRankTextView.setText(TextSpanHelper.from(mContext).obtainColorBoldStyle(R.string.tips_ranking, textRank, R.color.textColor6));
        requestUpdate(rankingBean);
        mContentLayout.removeAllViews();
        List<EmployeeRankingBean.RankingDataBean> dataList = bean.getRankData();
        if (dataList.isEmpty()) {
            LayoutInflater.from(mContext).inflate(R.layout.include_empty_textview, mContentLayout, true);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.include_employee_rank_content, mContentLayout, true);
            ViewStub viewStub = view.findViewById(R.id.vs_top);
            viewStub.setLayoutResource(requestRowId());
            viewStub.inflate();
            RecyclerView recyclerView = view.findViewById(R.id.rv_content);
            setAdapter(recyclerView, dataList);
        }
    }

    abstract void requestUpdate(@Nullable EmployeeRankingBean.MyRankingBean bean);

    abstract int requestRowId();

    abstract void setAdapter(RecyclerView recyclerView, List<EmployeeRankingBean.RankingDataBean> dataList);

    void onFailure(String failReason) {
        mContentLayout.removeAllViews();
        View view = LayoutInflater.from(mContext).inflate(R.layout.include_empty_page, mContentLayout, true);
        view.findViewById(R.id.ll_empty_page).setBackgroundResource(R.color.color_while);
        mFragment.noNetwork(failReason);
    }

    interface Callback {
        void doRequest(Date startDate, Date endDate);
    }
}
