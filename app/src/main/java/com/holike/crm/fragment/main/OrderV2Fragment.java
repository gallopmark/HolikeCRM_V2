package com.holike.crm.fragment.main;

import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.holike.crm.R;
import com.holike.crm.activity.homepage.MessageV2Activity;
import com.holike.crm.activity.homepage.OrderDetailsActivity;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.OrderListBean;
import com.holike.crm.bean.TypeListBean;
import com.holike.crm.controller.OrderController;
import com.holike.crm.presenter.activity.OrderListPresenter;
import com.holike.crm.presenter.fragment.HomePagePresenter;
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
public class OrderV2Fragment extends MyFragment<OrderListPresenter, OrderCenterV2View> implements OrderCenterV2View, OrderController.OrderControllerView {
    @BindView(R.id.et_order_center_search)
    EditText etSearch;
    @BindView(R.id.tv_order_center_order_type)
    TextView tvOrderType;
    @BindView(R.id.iv_order_center_order_type)
    ImageView ivOrderType;
    @BindView(R.id.tv_order_center_order_state)
    TextView tvOrderState;
    @BindView(R.id.iv_order_center_order_state)
    ImageView ivOrderState;
    @BindView(R.id.iv_home_red_point_msg)
    ImageView ivRedPoint;
    @BindView(R.id.dv_order_center_filter)
    View dvFilter;
    @BindView(R.id.srl_order_center)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.mOrderRv)
    RecyclerView mOrderRv;
    @BindView(R.id.mArrowImageView)
    ImageView mArrowImageView;
    @BindView(R.id.mTimeTextView)
    TextView mTimeTextView;
    @BindView(R.id.tv_amount)
    TextView mAmountTextView;
    @BindView(R.id.mCountTextView)
    TextView mCountTextView;

    private OrderController mOrderHelper;

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
        mOrderHelper = new OrderController(mContext, this);
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
        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideSoftInput(etSearch);
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
        ivRedPoint.setVisibility(HomePagePresenter.isNewMsg() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void reload() {
        super.reload();
        getOrderList();
    }

    @OnClick({R.id.mTimeLayout, R.id.ll_order_center_order_type,
            R.id.ll_order_center_order_state, R.id.tv_homepage_msg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_order_center_order_type:
                mOrderHelper.onClickOrderType(ivOrderType, dvFilter, tvOrderType, mContentView);
                break;
            case R.id.ll_order_center_order_state:
                mOrderHelper.onClickOrderStatus(ivOrderState, dvFilter, mContentView);
                break;
            case R.id.tv_homepage_msg:
                startActivity(MessageV2Activity.class);
                break;
            case R.id.mTimeLayout:
                showCalendarDialog();
                break;
        }
    }

    @Override
    public void onGetSelectData() {
        showLoading();
        mPresenter.getSelectData();
    }

    private void showCalendarDialog() {
        mOrderHelper.showCalendarDialog(mContext, mArrowImageView, mTimeTextView);
    }

    @Override
    public void onDateSelected(List<Date> selectedDates, Date start, Date end) {
        getOrderList();
    }

    private void search() {
        MobclickAgent.onEvent(mContext, "order_search");
        mOrderHelper.resetParams(tvOrderType, tvOrderState);
        getOrderList();
    }

    /*获取订单列表*/
    @Override
    public void getOrderList(String orderStatusId, String orderTypeId,
                             Date startDate, Date endDate, String pageNo, String pageSize) {
        String searchContent = etSearch.getText().toString();
        mPresenter.getOrderList(searchContent, orderStatusId, orderTypeId, startDate, endDate, String.valueOf(pageNo), String.valueOf(pageSize));
    }

    @Override
    public void getTypeListSuccess(TypeListBean typeListBean) {
        dismissLoading();
        mOrderHelper.onGetTypeListOk(typeListBean, ivOrderType, ivOrderState, dvFilter, tvOrderType, mContentView);
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
                showEmptyView(true);
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
        etSearch.setText("");
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
                showEmptyView(false);
            }
        }
    }

    /*显示缺省页*/
    private void showEmptyView(boolean isNetworkError) {
        setContentEnabled(false);
        if (isNetworkError) {
            noNetwork();
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
