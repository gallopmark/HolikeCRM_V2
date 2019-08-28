package com.holike.crm.fragment.bank;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.holike.crm.R;
import com.holike.crm.activity.homepage.MessageV2Activity;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.PayListBean;
import com.holike.crm.dialog.CalendarPickerDialog;
import com.holike.crm.model.event.MessageEvent;
import com.holike.crm.presenter.fragment.HomePagePresenter;
import com.holike.crm.presenter.fragment.OnlineDeclarationPresenter;
import com.holike.crm.util.Constants;
import com.holike.crm.util.KeyBoardUtil;
import com.holike.crm.util.TimeUtil;
import com.holike.crm.view.fragment.OnlineDeclarationView;
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

/**
 * 在线申报
 */
public class OnlineDeclarationFragment extends MyFragment<OnlineDeclarationPresenter, OnlineDeclarationView> implements OnlineDeclarationView {

    @BindView(R.id.rv_bill_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.srl_bill_list)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.ll_main)
    LinearLayout llMain;
    @BindView(R.id.tv_select_date)
    TextView tvSelectDate;
    @BindView(R.id.ll_add)
    LinearLayout llAdd;
    @BindView(R.id.btn_add)
    TextView tvAdd;
    @BindView(R.id.iv_select_date)
    ImageView ivSelectDate;
    private EditText etSearch;

    private int pageNo = 1;
    private String startTime = "";
    private String endTime = "";
    private String searchContent = "";
    private boolean isRefresh, isLoadCompleted;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_online_declaration_list;
    }

    @Override
    protected OnlineDeclarationPresenter attachPresenter() {
        return new OnlineDeclarationPresenter();
    }

    @Override
    protected void init() {
        super.init();
        etSearch = setSearchBar(R.string.bill_list_search_hint);
//        setRightMenu(HomePagePresenter.getMsgNum());
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
        llAdd.setVisibility(View.VISIBLE);
        refresh(true);
    }

    @Override
    protected void doSearch() {
        super.doSearch();
        KeyBoardUtil.hideKeyboard(etSearch);
        searchContent = etSearch.getText().toString();
        refresh(true);
    }

    @Override
    protected void clickRightMenu(String menuText, View actionView) {
        super.clickRightMenu(menuText, actionView);
        startActivity(MessageV2Activity.class);
    }

    @Override
    public void success(List<PayListBean> beans) {
        onLoadCompleted();
        if (!isLoadCompleted) {
            isLoadCompleted = true;
        }
        if (isRefresh) {
            refreshSuccess(beans);
        } else {
            if (beans.size() > 0) {
                loadmoreSuccess(beans);
            } else {
                loadAllSuccess();
            }
        }
        pageNo++;
    }

    private void onLoadCompleted() {
        refreshLayout.finishLoadMore();
        refreshLayout.finishRefresh();
    }

    @Override
    protected void reload() {
        super.reload();
        refresh(true);
    }

    @Override
    public void fail(String errorMsg) {
        dismissLoading();
        onLoadCompleted();
        if (!isLoadCompleted) {
            dealWithFailed(errorMsg, true);
        } else {
            showShortToast(errorMsg);
        }
    }

    @Override
    public void refresh(boolean showLoading) {
        if (showLoading) {
            showLoading();
        }
        pageNo = 1;
        isRefresh = true;
        getDataList();
    }

    @Override
    public void refreshSuccess(List<PayListBean> listBeans) {
        dismissLoading();
        refreshLayout.setEnableLoadMore(true);
        mPresenter.onRefreshCompleted(listBeans);
        if (listBeans.size() > 0) {
            if (refreshLayout.getVisibility() != View.VISIBLE) {
                refreshLayout.setVisibility(View.VISIBLE);
            }
            hasData();
        } else {
            if (refreshLayout.getVisibility() != View.GONE) {
                refreshLayout.setVisibility(View.GONE);
            }
            noResult();
        }
    }

    @Override
    public void loadmore() {
        isRefresh = false;
        getDataList();
    }

    private void getDataList() {
        mPresenter.getData(pageNo, startTime, endTime, searchContent);
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

    @OnClick({R.id.ll_select_date, R.id.btn_add})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.ll_select_date:
                showCalenderDialog();
                break;
            case R.id.btn_add:
                startFragment(new AddOrModifyPayInfoFragment(), true);
                break;
        }
    }

    /**
     * 查询日期
     */
    private List<Date> mSelectedDates;

    public void showCalenderDialog() {
        new CalendarPickerDialog.Builder(mContext).maxDate(new Date())
                .withSelectedDates(mSelectedDates)
                .clickToClear(true)
                .calendarRangeSelectedListener(new CalendarPickerDialog.OnCalendarRangeSelectedListener() {
                    @Override
                    public void onLeftClicked(CalendarPickerDialog dialog) {
//                resetTime();
//                onRefresh(true);
//                dialog.dismiss();
                    }

                    @Override
                    public void onRightClick(CalendarPickerDialog dialog, List<Date> selectedDates, Date start, Date end) {
                        dialog.dismiss();
                        mSelectedDates = selectedDates;
                        if (selectedDates.size() >= 1) {
                            startTime = TimeUtil.dateToStamp(start, false);
                            endTime = TimeUtil.dateToStamp(end, true);
//                    startTime = TimeUtil.dateToStamp(start, false);
//                    endTime = TimeUtil.dateToStamp(end, true);
                            DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());
                            String startTxt = dateFormat.format(start);
                            String endTxt = dateFormat.format(end);
                            String text = startTxt + "-" + endTxt;
                            tvSelectDate.setTextColor(ContextCompat.getColor(mContext, R.color.textColor4));
                            tvSelectDate.setText(text);
                        } else {
                            resetTime();
                        }
                        refresh(true);
                    }
                }).onShowListener(dialogInterface -> {
            ivSelectDate.setRotation(180);
        }).dismissListener(dialogInterface -> {
            ivSelectDate.setRotation(0);
        }).show();
    }

    private void resetTime() {
        startTime = "";
        endTime = "";
        tvSelectDate.setTextColor(ContextCompat.getColor(mContext, R.color.textColor8));
        tvSelectDate.setText(mContext.getString(R.string.all));
    }

    @Override
    public void onItemClick(PayListBean bean) {
        bean.setCategory(Constants.ONLINE_DECLARATION);
        Map<String, Serializable> params = new HashMap<>();
        params.put(Constants.PAY_LIST, bean);
        startFragment(params, new DetailsFragment());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(MessageEvent messageEvent) {
        switch (messageEvent.getMessage()) {
            case Constants.EVENT_REFRESH:
                refresh(true);
                break;
        }
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

    @Override
    protected void onFinishResult(int requestCode, int resultCode, Map<String, Serializable> result) {
        super.onFinishResult(requestCode, resultCode, result);
        if (resultCode == Constants.RESULT_CODE_OPERATE_SUCCESS) {
            refresh(false);
        }
    }
}
