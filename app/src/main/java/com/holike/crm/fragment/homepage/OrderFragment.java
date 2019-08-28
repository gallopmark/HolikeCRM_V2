package com.holike.crm.fragment.homepage;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.holike.crm.R;
import com.holike.crm.activity.homepage.MessageV2Activity;
import com.holike.crm.activity.homepage.OrderDetailsActivity;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.OrderListBean;
import com.holike.crm.bean.TypeListBean;
import com.holike.crm.dialog.CalendarPickerDialog;
import com.holike.crm.presenter.activity.OrderCenterPresenter;
import com.holike.crm.presenter.fragment.HomePagePresenter;
import com.holike.crm.view.activity.OrderCenterView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.umeng.analytics.MobclickAgent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wqj on 2018/7/26.
 * 订单
 */
@Deprecated
public class OrderFragment extends MyFragment<OrderCenterPresenter, OrderCenterView> implements OrderCenterView {

    @BindView(R.id.et_order_center_search)
    EditText etSearch;
    @BindView(R.id.tv_order_center_order_type)
    TextView tvOrderType;
    @BindView(R.id.iv_order_center_order_type)
    ImageView ivOrderType;
    @BindView(R.id.tv_order_center_order_state)
    TextView tvOrderState;
    @BindView(R.id.tv_homepage_msg)
    TextView tvMsg;
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
    @BindView(R.id.mCountTextView)
    TextView mCountTextView;

    private int pageNo = 1;
    private final int pageSize = 10;
    private TypeListBean typeListBean;
    private String orderTypeId, orderStatusId;
    private Date startDate, endDate;
    private boolean isLoadCompleted, isRefresh;
    private int pressedType = 0;

    @Override
    protected OrderCenterPresenter attachPresenter() {
        return new OrderCenterPresenter();
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_order_center;
    }

    @Override
    protected void init() {
        mPresenter.setAdapter(mOrderRv);
//        mRefreshLayout.setRefreshHeader(new WaterDropHeader(mContext));
//        mRefreshLayout.setRefreshFooter(new BallPulseFooter(mContext));
        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshlayout) {
                loadmore();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshlayout) {
                refresh(false);
            }
        });
        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideSoftInput(etSearch);
                search();
            }
            return false;
        });
        refresh(true);
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
        refresh(true);
    }

    @OnClick({R.id.mTimeLayout, R.id.ll_order_center_order_type,
            R.id.ll_order_center_order_state, R.id.tv_homepage_msg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_order_center_order_type:
                if (typeListBean != null) {
                    mPresenter.selectOrderType(mContext, typeListBean.getOrderTypeData(), orderTypeId, ivOrderType, dvFilter, tvOrderType, mContentView);
                } else {
                    pressedType = 1;
                    showLoading();
                    mPresenter.getSelectData();
                }
                break;
            case R.id.ll_order_center_order_state:
                if (typeListBean != null) {
                    mPresenter.selectOrderState(mContext, typeListBean.getOrderStatusData(), orderStatusId, ivOrderState, dvFilter, tvOrderState, mContentView);
                } else {
                    pressedType = 2;
                    showLoading();
                    mPresenter.getSelectData();
                }
                break;
            case R.id.tv_homepage_msg:
                startActivity(MessageV2Activity.class);
                break;
            case R.id.mTimeLayout:
                showCalendarDialog();
                break;
        }
    }

    private List<Date> mSelectedDates;

    private void showCalendarDialog() {
        new CalendarPickerDialog.Builder(mContext).maxDate(new Date())
                .withSelectedDates(mSelectedDates)
                .clickToClear(true)
                .calendarRangeSelectedListener(new CalendarPickerDialog.OnCalendarRangeSelectedListener() {
                    @Override
                    public void onLeftClicked(CalendarPickerDialog dialog) {
//                        dialog.dismiss();
//                        clearTime();
//                        onRefresh(true);
                    }

                    @Override
                    public void onRightClick(CalendarPickerDialog dialog, List<Date> selectedDates, Date start, Date end) {
                        mSelectedDates = selectedDates;
                        if (selectedDates.size() >= 1) {
                            startDate = start;
                            endDate = end;
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());
                            String startSource = dateFormat.format(startDate);
                            String endSource = dateFormat.format(endDate);
                            String text = startSource + "-" + endSource;
                            mTimeTextView.setText(text);
                        } else {
                            clearTime();
                        }
                        dialog.dismiss();
                        refresh(true);
                    }
                }).onShowListener(dialogInterface -> mArrowImageView.setRotation(180)).dismissListener(dialogInterface -> mArrowImageView.setRotation(0)).show();
    }

    private void clearTime() {
        startDate = null;
        endDate = null;
        mTimeTextView.setText(mContext.getString(R.string.all));
    }

    public void search() {
        MobclickAgent.onEvent(mContext, "order_search");
        resetParams();
        refresh(true);
    }

    private void resetParams() {
        this.orderTypeId = null;
        tvOrderType.setText(mContext.getString(R.string.order_center_order_type));
        this.orderStatusId = null;
        tvOrderState.setText(mContext.getString(R.string.order_center_order_state));
    }

    /*获取订单列表*/
    private void getOrderList() {
        String searchContent = etSearch.getText().toString();
        mPresenter.getOrderList(searchContent, orderStatusId, orderTypeId, startDate, endDate, String.valueOf(pageNo), String.valueOf(pageSize));
    }

    @Override
    public void getTypeListSuccess(TypeListBean typeListBean) {
        dismissLoading();
        this.typeListBean = typeListBean;
        if (pressedType == 1) {
            mPresenter.selectOrderType(mContext, typeListBean.getOrderTypeData(), orderTypeId, ivOrderType, dvFilter, tvOrderType, mContentView);
        } else if (pressedType == 2) {
            mPresenter.selectOrderState(mContext, typeListBean.getOrderStatusData(), orderStatusId, ivOrderState, dvFilter, tvOrderState, mContentView);
        }
    }

    @Override
    public void getListSuccess(List<OrderListBean> listBeans, boolean isShow, long count, boolean isShow2, String money) {
        dismissLoading();
        if (!isLoadCompleted) {
            isLoadCompleted = true;
        }
        onLoadComplete();
        if (isRefresh) {
            refreshSuccess(listBeans);
        } else {
            if (listBeans.size() < pageSize) {
                loadmoreSuccess(listBeans);
                loadAll();
            } else {
                loadmoreSuccess(listBeans);
            }
        }
        pageNo++;
        if (isShow) {
            mCountTextView.setVisibility(View.VISIBLE);
            setTextCount(String.valueOf(count));
        } else {
            hideCountTextView();
        }
    }

    private void setTextCount(String totalRows) {
        String source = mContext.getString(R.string.order_count);
        int start = source.length();
        if (!TextUtils.isEmpty(totalRows)) {
            source += totalRows;
        }
        int end = source.length();
        SpannableString ss = new SpannableString(source);
        int flags = SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE;
        ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.textColor13)), start, end, flags);
//        ss.setSpan(new StyleSpan(Typeface.NORMAL), start, end, flags);
        mCountTextView.setText(ss);
    }

    private void hideCountTextView() {
        mCountTextView.setVisibility(View.GONE);
    }

    @Override
    public void getListFailed(String failed) {
        dismissLoading();
        onLoadComplete();
        if (isNoAuth(failed)) {
            hideCountTextView();
            mPresenter.clearData();
            noAuthority();
        } else {
            if (!isLoadCompleted) {
                noNetwork();
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
    public void getListByOrderType(String orderTypeId) {
        MobclickAgent.onEvent(mContext, "order_type");
        this.orderTypeId = orderTypeId;
        clearSearch();
        refresh(true);
    }

    @Override
    public void getListByOrderState(String orderStatusId) {
        MobclickAgent.onEvent(mContext, "order_state");
        this.orderStatusId = orderStatusId;
        clearSearch();
        refresh(true);
    }

    private void clearSearch() {
        etSearch.setText("");
    }

    @Override
    public void refresh(boolean showLoading) {
        if (showLoading) {
            showLoading();
        }
        pageNo = 1;
        isRefresh = true;
        getOrderList();
    }

    @Override
    public void loadmore() {
        isRefresh = false;
        getOrderList();
    }

    @Override
    public void refreshSuccess(List<OrderListBean> listBeans) {
        mRefreshLayout.setEnableLoadMore(true);
        if (listBeans.size() > 0) {
            hasData();
            mPresenter.onRefreshCompleted(listBeans);
            if (listBeans.size() < pageSize) {
                loadAll();
            } else {
                mRefreshLayout.setEnableLoadMore(true);
            }
        } else {
            noResult();
        }
    }

    @Override
    public void loadmoreSuccess(List<OrderListBean> listBeans) {
        mRefreshLayout.setEnableLoadMore(true);
        mPresenter.onLoadMoreCompleted(listBeans);
    }

    @Override
    public void loadAll() {
        mRefreshLayout.setEnableLoadMore(false);
        mPresenter.noMoreData();
    }

    @Override
    public void adapterItemChildClick(OrderListBean bean) {
        call(bean.getPhone());
    }

    @Override
    public void adapterItemChildLongClick(View view, OrderListBean bean) {
        String content = "";
        if (view.getId() == R.id.tv_item_rv_order_center_phone) {
            content = bean.getPhone();
        } else if (view.getId() == R.id.tv_item_rv_order_center_orderNum) {
            content = bean.getOrderId();
        }
        copy(content);
    }

    @Override
    public void orderDetails(String orderId) {
        OrderDetailsActivity.open(this, orderId, null);
//        NotifyFragment.startOrderDetails(this, orderId, null, REQUEST_CODE);
    }
}
