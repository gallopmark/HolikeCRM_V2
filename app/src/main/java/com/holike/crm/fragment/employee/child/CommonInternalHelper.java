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
import com.holike.crm.bean.DictionaryBean;
import com.holike.crm.bean.EmployeeRankingBean;
import com.holike.crm.bean.EvaluateTypeBean;
import com.holike.crm.dialog.LevelAreaDialog;
import com.holike.crm.helper.PickerHelper;
import com.holike.crm.helper.TextSpanHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

abstract class CommonInternalHelper extends EmployeeRankHelper {

    private Callback mCallback;
    private String mType, mCityCode;
    private TextView mRegionTextView;
    private TextView mDatetimeTextView;
    private FrameLayout mShopLayout;
    TextView mDataTextView;
    private TextView mRankTextView;
    private FrameLayout mContentLayout;

    private LevelAreaDialog mDialog;

    CommonInternalHelper(BaseFragment<?, ?> fragment, Callback callback) {
        super(fragment);
        this.mCallback = callback;
        initView();
    }

    private void initView() {
        mDatetimeTextView = obtainTextView(R.id.tv_time_detail);
        mRegionTextView = obtainTextView(R.id.tv_select_region);
        mShopLayout = obtainView(R.id.fl_shop_layout);
        mDataTextView = obtainTextView(R.id.tv_data);
        mRankTextView = obtainTextView(R.id.tv_rank);
        mContentLayout = obtainView(R.id.fl_content_layout);
        mRegionTextView.setOnClickListener(view -> {
            if (mDialog == null) {
                mDialog = new LevelAreaDialog(mContext);
            }
            mDialog.setOnAreaSelectListener(new LevelAreaDialog.OnAreaSelectListener() {
                @Override
                public void onAreaSelected(String name, String type, String cityCode, boolean isSelectDealer) {
                    mType = type;
                    mCityCode = cityCode;
                    mRegionTextView.setText(name);
                    if (isSelectDealer) {
                        mShopLayout.setVisibility(View.VISIBLE);
                    } else {
                        mShopLayout.setVisibility(View.GONE);
                    }
                    doRequest();
                }

                @Override
                public void onDismissDealer() {

                }
            }).show();
        });
        mDatetimeTextView.setOnClickListener(view -> onCalendarPicker());
        obtainView(R.id.tv_select_shop).setOnClickListener(view -> mCallback.onQueryShop(mType, mCityCode));
    }

    @Override
    void onShowCalendar() {
        mDatetimeTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.ic_choice_date_up), null);
    }

    @Override
    void onDismissCalendar() {
        mDatetimeTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.ic_choice_date_down), null);
    }

    @Override
    void doRequest() {
        mCallback.doRequest(mType, mCityCode, mStartDate, mEndDate);
    }

    void showShopPicker(List<EvaluateTypeBean> dataList) {
        List<DictionaryBean> optionItems = new ArrayList<>();
        for (EvaluateTypeBean bean : dataList) {
            optionItems.add(new DictionaryBean(bean.type, bean.name, bean.cityCode));
        }
        PickerHelper.showOptionsPicker(mContext, optionItems, (position, bean) -> {
            mType = bean.id;
            mCityCode = bean.code;
            ((TextView) mShopLayout.findViewById(R.id.tv_select_shop)).setText(bean.name);
        });
    }

    void onSuccess(EmployeeRankingBean bean) {
        mDatetimeTextView.setEnabled(true);
        mDatetimeTextView.setText(bean.title);
        EmployeeRankingBean.MyRankingBean rankingBean = bean.getMyRank();
        String textRank = "";
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
        void doRequest(String type, String cityCode, Date startDate, Date endDate);

        void onQueryShop(String type, String cityCode);
    }

    void onDestroy() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }
}
