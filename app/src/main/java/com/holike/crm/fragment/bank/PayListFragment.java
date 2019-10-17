package com.holike.crm.fragment.bank;


import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.holike.crm.R;
import com.holike.crm.activity.homepage.MessageV2Activity;
import com.holike.crm.base.IntentValue;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.HomepageBean;
import com.holike.crm.bean.PayListBean;
import com.holike.crm.dialog.CalendarPickerDialog;
import com.holike.crm.model.event.MessageEvent;
import com.holike.crm.presenter.fragment.HomePagePresenter;
import com.holike.crm.presenter.fragment.PayListPresenter;
import com.holike.crm.util.Constants;
import com.holike.crm.util.KeyBoardUtil;
import com.holike.crm.util.TimeUtil;
import com.holike.crm.view.fragment.PayListView;
import com.scwang.smartrefresh.header.WaterDropHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/*打款确认*/
public class PayListFragment extends MyFragment<PayListPresenter, PayListView> implements PayListView, View.OnClickListener {
    @BindView(R.id.rv_online_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.srl_online_list)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.v_select_line)
    View vSelectLine;
    private EditText etSearch;
    @BindView(R.id.tv_trading_time)
    TextView mTradingTimeTextView;
    @BindView(R.id.tv_documents_status)
    TextView mDocumentsStatusTextView;


    private int pageNo = 1;
    private String startTime = "";
    private String endTime = "";
    private String status = "";
    private String searchContent = "";
    private boolean isRefresh, isLoadCompleted;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_online_declaration;
    }

    @Override
    protected PayListPresenter attachPresenter() {
        return new PayListPresenter();
    }

    @Override
    protected void init() {
        super.init();
        etSearch = setSearchBar(R.string.bill_list_search_hint);
        List<HomepageBean.TypeListBean.BrankDataBean> beans = IntentValue.getInstance().getHomeBankData();
        if (beans != null && !beans.isEmpty()) {
            mPresenter.addBankDataBeans(beans);
        }
        mPresenter.setAdapter(mRecyclerView);
        refreshLayout.setRefreshHeader(new WaterDropHeader(mContext));
        refreshLayout.setRefreshFooter(new BallPulseFooter(mContext));
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshlayout) {
                loadmore();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshlayout) {
                refresh(false);
            }
        });
        refresh(true);
    }

    private void getPayList() {
        mPresenter.getData(pageNo, startTime, endTime, searchContent, status);
    }

    @Override
    protected void doSearch() {
        super.doSearch();
        KeyBoardUtil.hideKeyboard(etSearch);
        searchContent = etSearch.getText().toString();
        refresh(true);
    }

    @Override
    public void onSuccess(List<PayListBean> bean) {
        dismissLoading();
        onLoadComplete();
        if (!isLoadCompleted) {
            isLoadCompleted = true;
        }
        if (isRefresh) {
            refreshSuccess(bean);
        } else {
            if (bean.size() > 0) {
                loadmoreSuccess(bean);
            } else {
                loadAllSuccess();
            }
        }
        pageNo++;
    }

    private void onLoadComplete() {
        refreshLayout.finishLoadMore();
        refreshLayout.finishRefresh();
    }

    @Override
    public void onFail(String errorMsg) {
        dismissLoading();
        onLoadComplete();
        if (isNoAuth(errorMsg)) {
            noAuthority();
        } else {
            if (!isLoadCompleted) {
                noNetwork();
            } else {
                showShortToast(errorMsg);
            }
        }
    }

    @Override
    public void refresh(boolean showLoading) {
        pageNo = 1;
        isRefresh = true;
        if (showLoading) {
            showLoading();
        }
        getPayList();
    }

    @Override
    public void refreshSuccess(List<PayListBean> listBeans) {
        dismissLoading();
        refreshLayout.setEnableLoadMore(true);
        mPresenter.onRefreshCompleted(listBeans);
        if (listBeans.size() > 0) {
            hasData();
        } else {
            noResult();
        }
    }

    @Override
    public void loadmore() {
        isRefresh = false;
        getPayList();
    }

    @Override
    public void loadmoreSuccess(List<PayListBean> listBeans) {
        refreshLayout.setEnableLoadMore(true);
        mPresenter.onLoadMoreCompleted(listBeans);
    }

    @Override
    public void loadAllSuccess() {
        refreshLayout.setEnableLoadMore(false);
        mPresenter.noMoreData();
    }

    @Override
    public void onPopupWindowShowing() {
        mDocumentsStatusTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.choice_up), null);
    }

    @Override
    public void onFilterItemSelect(HomepageBean.TypeListBean.BrankDataBean bean) {
        if (TextUtils.isEmpty(bean.getBarkId())) {
            mDocumentsStatusTextView.setText(mContext.getString(R.string.bill_list_documents_state));
            mDocumentsStatusTextView.setTextColor(ContextCompat.getColor(mContext, R.color.textColor8));
        } else {
            mDocumentsStatusTextView.setText(bean.getBarkName());
            mDocumentsStatusTextView.setTextColor(ContextCompat.getColor(mContext, R.color.textColor4));
        }
        status = bean.getBarkId();
        refresh(true);
    }

    @Override
    public void onPopupWindowDismiss() {
        mDocumentsStatusTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.choice_down), null);
    }

    @OnClick({R.id.tv_trading_time, R.id.tv_documents_status})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_trading_time:
                showCalendarDialog();
                break;
            case R.id.tv_documents_status:
                mPresenter.showStatePopupWindow(mContext, mContentView, vSelectLine);
                break;
        }
    }

    private List<Date> mSelectedDates;

    private void showCalendarDialog() {
        new CalendarPickerDialog.Builder(mContext)
                .maxDate(new Date())
                .withSelectedDates(mSelectedDates)
                .clickToClear(true)
                .calendarRangeSelectedListener(new CalendarPickerDialog.OnCalendarRangeSelectedListener() {
                    @Override
                    public void onLeftClicked(CalendarPickerDialog dialog) {
                    }

                    @Override
                    public void onRightClick(CalendarPickerDialog dialog, List<Date> selectedDates, Date start, Date end) {
                        dialog.dismiss();
                        mSelectedDates = selectedDates;
                        if (selectedDates.size() >= 1) {
                            DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());
                            String startTxt = dateFormat.format(start);
                            String endTxt = dateFormat.format(end);
                            String text = startTxt + "-" + endTxt;
                            startTime = TimeUtil.dateToStamp(start, false);
                            endTime = TimeUtil.dateToStamp(end, true);
                            mTradingTimeTextView.setTextColor(ContextCompat.getColor(mContext, R.color.textColor4));
                            mTradingTimeTextView.setText(text);
                        } else {
                            resetParams();
                        }
                        refresh(true);
                    }
                }).onShowListener(dialogInterface -> mTradingTimeTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.choice_up), null))
                .dismissListener(dialogInterface -> mTradingTimeTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.choice_down), null))
                .show();
    }

    @Override
    protected void onFinishResult(int requestCode, int resultCode, Map<String, Serializable> result) {
        super.onFinishResult(requestCode, resultCode, result);
        if (resultCode == Constants.RESULT_CODE_OPERATE_SUCCESS) {
            refresh(true);
        }
    }

    private void resetParams() {
        pageNo = 1;
        startTime = "";
        endTime = "";
        mTradingTimeTextView.setText(mContext.getString(R.string.bill_list_trading_time));
        mTradingTimeTextView.setTextColor(ContextCompat.getColor(mContext, R.color.textColor8));
    }

    @Override
    public void onItemClick(PayListBean bean) {
        bean.setCategory(Constants.PAY_LIST);
        Map<String, Serializable> params = new HashMap<>();
        params.put(Constants.PAY_LIST, bean);
        startFragment(params, new DetailsFragment());
    }

    @Override
    protected void reload() {
        super.reload();
        refresh(true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(MessageEvent messageEvent) {
        if (messageEvent.getMessage().equals(Constants.EVENT_REFRESH)) {
            refresh(true);
        }
    }

    @Override
    protected void clickRightMenu(String menuText, View actionView) {
        super.clickRightMenu(menuText, actionView);
        startActivity(MessageV2Activity.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        setRightMenuMsg(HomePagePresenter.isNewMsg());
        if (!EventBus.getDefault().isRegistered(this)) //加上判断
            EventBus.getDefault().register(this);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(this))//加上判断
            EventBus.getDefault().unregister(this);
    }
}
