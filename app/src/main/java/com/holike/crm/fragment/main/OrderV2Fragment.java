package com.holike.crm.fragment.main;

import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.holike.crm.R;
import com.holike.crm.activity.homepage.MessageV2Activity;
import com.holike.crm.activity.homepage.OrderDetailsActivity;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.OrderListBean;
import com.holike.crm.bean.TypeListBean;
import com.holike.crm.customView.DrawableCenterTextView;
import com.holike.crm.presenter.activity.OrderListPresenter;
import com.holike.crm.presenter.fragment.HomePagePresenter2;
import com.holike.crm.view.activity.OrderCenterV2View;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.umeng.analytics.MobclickAgent;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/*v2.0版本订单列表*/
public class OrderV2Fragment extends MyFragment<OrderListPresenter, OrderCenterV2View> implements OrderCenterV2View, OrderHelper.OrderControllerView {
    private EditText mSearchEditText;
    @BindView(R.id.tv_select_order_type)
    DrawableCenterTextView mOrderTypeTextView;
    @BindView(R.id.tv_select_order_state)
    DrawableCenterTextView mOrderStateTextView;
    @BindView(R.id.dv_order_center_filter)
    View mDropDownView;
    @BindView(R.id.srl_order_center)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.mOrderRv)
    RecyclerView mOrderRv;
    @BindView(R.id.tv_select_time)
    TextView mTimeTextView;
    @BindView(R.id.tv_amount)
    TextView mAmountTextView;
    @BindView(R.id.tv_count)
    TextView mCountTextView;

    private OrderHelper mOrderHelper;

    @Override
    protected OrderListPresenter attachPresenter() {
        return new OrderListPresenter();
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_order_centerv2;
    }

    @Override
    protected void init() {
        mOrderHelper = new OrderHelper(mContext, this);
        mOrderHelper.setDefaultTime(mTimeTextView);
        mOrderHelper.setOrderListAdapter(mOrderRv);
        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshlayout) {
                mOrderHelper.onLoadMore();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshlayout) {
                mOrderHelper.onRefresh();
            }
        });
        mSearchEditText = setSearchBar(R.string.hint_fragment_order_search, R.drawable.bg_search);
        mSearchEditText.setGravity(Gravity.CENTER | Gravity.START);
        mSearchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideSoftInput(mSearchEditText);
                search();
            }
            return false;
        });
        getOrderList();
    }

    private void getOrderList() {
        showLoading();
        mOrderHelper.getOrderList();
    }

    @Override
    public void onResume() {
        super.onResume();
//        tvMsg.setText(HomePagePresenter.getMsgNum());
        setRightMenuMsg(HomePagePresenter2.isNewMsg());
//        ivRedPoint.setVisibility(HomePagePresenter.isNewMsg() ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void clickRightMenu(String menuText, View actionView) {
        startActivity(MessageV2Activity.class);
    }

    @Override
    public void reload() {
        super.reload();
        getOrderList();
    }

    @OnClick({R.id.tv_select_time, R.id.tv_select_order_type,
            R.id.tv_select_order_state})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_select_order_type:
                mOrderHelper.onClickOrderType(mDropDownView, mOrderTypeTextView, mContentView);
                break;
            case R.id.tv_select_order_state:
                mOrderHelper.onClickOrderStatus(mDropDownView, mOrderStateTextView, mContentView);
                break;
            case R.id.tv_select_time:
                mOrderHelper.showCalendarDialog(mContext, mTimeTextView);
                break;
        }
    }

    @Override
    public void onGetSelectData() {
        showLoading();
        mPresenter.getSelectData();
    }

    @Override
    public void onDateSelected(List<Date> selectedDates, Date start, Date end) {
        getOrderList();
    }

    private void search() {
        MobclickAgent.onEvent(mContext, "order_search");
        mOrderHelper.resetParams(mOrderTypeTextView, mOrderStateTextView);
        getOrderList();
    }

    /*获取订单列表*/
    @Override
    public void getOrderList(String orderStatusId, String orderTypeId,
                             Date startDate, Date endDate, String pageNo, String pageSize) {
        String searchContent = mSearchEditText.getText().toString();
        mPresenter.getOrderList(searchContent, orderStatusId, orderTypeId, startDate, endDate, String.valueOf(pageNo), String.valueOf(pageSize));
    }

    @Override
    public void getTypeListSuccess(TypeListBean typeListBean) {
        dismissLoading();
        mOrderHelper.onGetTypeListOk(typeListBean, mDropDownView, mOrderTypeTextView, mOrderStateTextView, mContentView);
    }

    @Override
    public void getListSuccess(List<OrderListBean> listBeans, boolean isShow, long count, boolean isShow2, String money) {
        dismissLoading();
        onLoadComplete();
        mOrderHelper.onGetOrderListOk(listBeans, isShow, count, isShow2, money, mAmountTextView, mCountTextView);
    }

    @Override
    public void getListFailed(String failed) {
        dismissLoading();
        onLoadComplete();
        if (isNoAuth(failed)) {
            mOrderHelper.hideViews(mAmountTextView, mCountTextView);
            mOrderHelper.clearData();
            setContentEnabled(false);
            noAuthority();
        } else {
            if (mOrderHelper.isFirstLoading()) {
                mOrderHelper.hideViews(mAmountTextView, mCountTextView);
                showEmptyView(true, failed);
            } else {
                showShortToast(failed);
            }
        }
    }

    private void onLoadComplete() {
        mRefreshLayout.finishLoadMore();
        mRefreshLayout.finishRefresh();
    }

    @Override
    public void getListByOrderType() {
        reset();
        getOrderList();
    }

    @Override
    public void getListByOrderState() {
        reset();
        getOrderList();
    }

    private void reset() {
        mSearchEditText.setText("");
        mOrderHelper.onReset();
    }

    @Override
    public void onGetOrderOk(boolean isEmpty, boolean isLoadAll) {
        setLoadMoreEnabled(true);
        setContentEnabled(true);
        if (!isEmpty) {
            hasData();
            setLoadMoreEnabled(!isLoadAll);
        } else {
            if (mOrderHelper.isFirstLoading()) {  //首次加载完成（没有加载到数据）
                showEmptyView(false, null);
            }
        }
    }

    /*显示缺省页*/
    private void showEmptyView(boolean isNetworkError, String failReason) {
        setContentEnabled(false);
        if (isNetworkError) {
            noNetwork(failReason);
        } else {
            noResult();
        }
    }

    /*列表页面显示或隐藏*/
    private void setContentEnabled(boolean isEnabled) {
        if (isEnabled) {
            if (mRefreshLayout.getVisibility() != View.VISIBLE) {
                mRefreshLayout.setVisibility(View.VISIBLE);
            }
        } else {
            mRefreshLayout.setVisibility(View.GONE);
        }
    }

    private void setLoadMoreEnabled(boolean isLoadMoreEnabled) {
        mRefreshLayout.setEnableLoadMore(isLoadMoreEnabled);
    }

    @Override
    public void loadMoreSuccess(boolean isLoadMoreEnabled) {
        setLoadMoreEnabled(isLoadMoreEnabled);
    }

    @Override
    public void onCallPhone(String phone) {
        call(phone);
    }

    @Override
    public void onCopy(String content) {
        copy(content);
    }

    @Override
    public void orderDetails(String orderId) {
        OrderDetailsActivity.open(this, orderId, null);
//        NotifyFragment.startOrderDetails(this, orderId, null, REQUEST_CODE);
    }
}
