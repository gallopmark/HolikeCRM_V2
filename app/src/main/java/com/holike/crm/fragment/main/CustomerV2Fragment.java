package com.holike.crm.fragment.main;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.holike.crm.R;
import com.holike.crm.activity.customer.CustomerDetailV2Activity;
import com.holike.crm.activity.homepage.MessageV2Activity;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.CustomerListBeanV2;
import com.holike.crm.bean.SysCodeItemBean;
import com.holike.crm.dialog.DistributionShopDialog;
import com.holike.crm.presenter.activity.CustomerListPresenter;
import com.holike.crm.presenter.fragment.HomePagePresenter2;
import com.holike.crm.view.activity.CustomerV2View;
import com.scwang.smartrefresh.header.WaterDropHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.umeng.analytics.MobclickAgent;


import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/*客户列表fragment v2.0*/
public class CustomerV2Fragment extends MyFragment<CustomerListPresenter, CustomerV2View> implements CustomerV2View, CustomerHelper.Callback {
    private EditText mSearchEditText;
    @BindView(R.id.tv_source)
    TextView mSourceTextView;
    @BindView(R.id.tv_status)
    TextView mStatusTextView;
    @BindView(R.id.v_parent)
    View mDropDownView;
    @BindView(R.id.mCustomerRv)
    RecyclerView mCustomerRv;
    @BindView(R.id.srl_customer_manage)
    SmartRefreshLayout mRefreshLayout;

    @BindView(R.id.tv_select_time)
    TextView mTimeTextView;
    @BindView(R.id.tv_count)
    TextView mCountTextView;

    private CustomerHelper mHelper;
    private Handler mHandler;

    @Override
    protected CustomerListPresenter attachPresenter() {
        return new CustomerListPresenter();
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_customer;
    }

    @Override
    protected void onNavigationClick(View view) {
        mHelper.onClickAddCustomer();
    }

    @Override
    protected void init() {
        mHelper = new CustomerHelper(mContext, this);
        mHelper.setDefaultTimeText(mTimeTextView);
        mHelper.setCustomerAdapter(mCustomerRv);
        mRefreshLayout.setRefreshHeader(new WaterDropHeader(mContext));
        mRefreshLayout.setRefreshFooter(new BallPulseFooter(mContext));
        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshlayout) {
                mHelper.onRefresh();
            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshlayout) {
                mHelper.onLoadMore();
            }
        });
        mSearchEditText = setSearchBar(R.string.hint_fragment_customer_search, R.drawable.bg_search);
        mSearchEditText.setGravity(Gravity.CENTER | Gravity.START);
        getCustomerList();
    }

    private void getCustomerList() {
        mHelper.startFirstLoad();
    }

    /*搜索*/
    @Override
    protected void doSearch() {
        MobclickAgent.onEvent(mContext, "customer_search");
//        mHelper.resetParams(tvCustomerState, tvCustomerSource);
        getCustomerList();
//        mSearchEditText.setText(null);
    }

    @Override
    public void onQuerySystemCode(boolean showLoading) {
        if (showLoading) {
            showLoading();
        }
        mPresenter.getSystemCode();
    }

    @Override
    public void onSourceSelected(String id, String value) {
        mSourceTextView.setText(TextUtils.isEmpty(id) ? mContext.getString(R.string.receive_deposit_customerSource) : value);
//        reset();
        getCustomerList();
    }

    @Override
    public void onStatusSelected(String id, String value) {
//        tvCustomerState.setText(TextUtils.isEmpty(id) ? mContext.getString(R.string.customer_manage_customer_state) : value);
//        reset();
        getCustomerList();
    }

    @Override
    public void onDateSelected(List<Date> selectedDates, Date start, Date end) {
        getCustomerList();
    }

//    private void reset() {
//        mSearchContent = null;
//        mSearchEditText.setText(null);
//        mHelper.onReset();
//    }

    @Override
    public void onGetCustomerList(boolean isShowLoading, String source, String status, Date startDate,
                                  Date endDate, String orderBy, int pageNo, int pageSize) {
        if (isShowLoading) {
            showLoading();
        }
        mPresenter.getCustomerList(mSearchEditText.getText().toString(), source, status, startDate, endDate, orderBy, pageNo, pageSize);
    }

    @Override
    public void onResume() {
        super.onResume();
//        tvMsg.setText(HomePagePresenter.getMsgNum());
        setRightMenuMsg(HomePagePresenter2.isNewMsg());
//        ivRedPoint.setVisibility(HomePagePresenter.isNewMsg() ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void clickRightMenu(CharSequence menuText, View actionView) {
        startActivity(MessageV2Activity.class);
    }

    @OnClick({R.id.tv_source, R.id.tv_status, R.id.tv_select_time, R.id.iv_orderBy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_source:
                mHelper.onClickSource(mContentView, mDropDownView, mSourceTextView);
                break;
            case R.id.tv_status:
                mHelper.onClickStatus(mContentView, mDropDownView, mStatusTextView);
                break;
            case R.id.tv_select_time:
                mHelper.showCalendarDialog(mContext, mTimeTextView);
                break;
            case R.id.iv_orderBy:
                mHelper.onOrderBy((ImageView) view);
                break;
        }
    }

    @Override
    public void onAddCustomer(Intent intent) {
        openActivityForResult(intent);
    }

    /**
     * 获取客户列表成功
     */
    @Override
    public void getCustomerListSuccess(CustomerListBeanV2 bean) {
        dismissLoading();
        onLoadComplete();
        if (bean.isShow()) { //是否显示"添加客户"icon
            setNavigationIcon(R.drawable.icon_add);
        } else {
            setNavigationIcon(null);
        }
        mHelper.onGetCustomerOk(bean, bean.total, mCountTextView);
    }

    private void onLoadComplete() {
        mRefreshLayout.finishLoadMore();
        mRefreshLayout.finishRefresh();
    }

    @Override
    public void onGetCustomerOk(boolean isEmpty, boolean isLoadAll) {
        setLoadMoreEnabled(true);
        setContentEnabled(true);
        if (!isEmpty) {
            hasData();
            setLoadMoreEnabled(!isLoadAll);
        } else {
            if (mHelper.isFirstLoading()) {  //首次加载完成（没有加载到数据）
                showEmptyView(false, null);
            }
        }
    }

    @Override
    public void loadMoreSuccess(boolean isLoadMoreEnabled) {
        setLoadMoreEnabled(isLoadMoreEnabled);
    }

    private void setLoadMoreEnabled(boolean isLoadMoreEnabled) {
        mRefreshLayout.setEnableLoadMore(isLoadMoreEnabled);
    }

    /**
     * 获取客户列表失败
     */
    @Override
    public void getCustomerListFailed(String failed) {
        dismissLoading();
        onLoadComplete();
        if (isNoAuth(failed)) {
            mHelper.clearData();
            setContentEnabled(false);
            noAuthority();
        } else {
            if (mHelper.isFirstLoading()) {
                mHelper.clearData();
                showEmptyView(true, failed);
            } else {
                showShortToast(failed);
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

    /**
     * 重新加载
     */
    @Override
    public void reload() {
        getCustomerList();
    }

    /**
     * 获取类型id成功
     */
    @Override
    public void onGetSystemCodeSuccess(SysCodeItemBean bean) {
        int pressedType = mHelper.getPressedType();
        if (pressedType == 1 || pressedType == 2)
            dismissLoading();
        mHelper.onGetSystemCode(bean, mContentView, mDropDownView, mSourceTextView, mStatusTextView);
    }

    @Override
    public void onGetSystemCodeFailure(String failed) {
        int pressedType = mHelper.getPressedType();
        if (pressedType == 1 || pressedType == 2)
            dismissLoading();
        if (pressedType == 1 || pressedType == 2) {
            showShortToast(failed);
        }
    }

    @Override
    public void onItemChildLongClick(CustomerListBeanV2.CustomerBean bean) {
        copy(bean.phoneNumber);
    }

    @Override
    public void onCallPhoneFinish(String body) {
        mPresenter.saveCallRecord(body);
    }

    @Override
    public void onDataEmpty() {
        noResult();
    }

    @Override
    public void onItemClick(CustomerListBeanV2.CustomerBean bean) {
        //shopId未空均为未分配门店
        if (TextUtils.isEmpty(bean.shopId)) {
            customerPopupDialog(bean);
        } else {
            CustomerDetailV2Activity.open((BaseActivity) mContext, bean.personalId, bean.houseId, bean.isHighSeasHouse());
        }
    }

    @Override
    public void onDeleteCustomer(CustomerListBeanV2.CustomerBean bean, int position) {
        showLoading();
        mPresenter.deleteCustomer(bean.houseId, position);
    }

    @Override
    public void deleteCustomerSuccess(String result, int position) {
        dismissLoading();
        showShortToast(R.string.delete_customer_success_tips);
        mHelper.onDeleteCustomer(position, mCountTextView);
    }

    @Override
    public void deleteCustomerFailure(String message) {
        dismissLoading();
        showShortToast(message);
    }

    @Override
    public void onSavePhoneRecordSuccess(String message) {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    /*保存通话记录失败,3s后重试*/
    @Override
    public void onSavePhoneRecordFailure(String body, String failReason) {
        if (mHandler == null) {
            mHandler = new Handler();
        }
        mHandler.postDelayed(() -> onCallPhoneFinish(body), 3000);
    }

    private void customerPopupDialog(final CustomerListBeanV2.CustomerBean bean) {
        DistributionShopDialog dialog = new DistributionShopDialog(mContext, bean);
        dialog.setOnConfirmListener((shopId, groupId, guideId) -> onDistributionShop(bean.houseId, shopId, groupId, guideId));
        dialog.show();
    }

    /*分配门店*/
    private void onDistributionShop(String houseId, String shopId, String groupId, String guideId) {
        showLoading();
        mPresenter.assignShop(houseId, shopId, groupId, guideId);
    }

    @Override
    public void distributionShopSuccess(String message) {
        dismissLoading();
        showShortToast(message);
        getCustomerList();
    }

    @Override
    public void distributionShopFailure(String failReason) {
        dismissLoading();
        showShortToast(failReason);
    }

    @Override
    public void onDestroyView() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        mHelper.onDispose();
        super.onDestroyView();
    }
}
