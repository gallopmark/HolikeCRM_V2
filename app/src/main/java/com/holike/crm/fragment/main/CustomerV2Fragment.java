package com.holike.crm.fragment.main;

import android.Manifest;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.holike.crm.R;
import com.holike.crm.activity.customer.CustomerDetailV2Activity;
import com.holike.crm.activity.homepage.MessageV2Activity;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.base.MyFragment;
import com.holike.crm.base.OnRequestPermissionsCallback;
import com.holike.crm.bean.CustomerListBeanV2;
import com.holike.crm.bean.SysCodeItemBean;
import com.holike.crm.controller.CustomerController;
import com.holike.crm.dialog.DistributionShopDialog;
import com.holike.crm.presenter.activity.CustomerListPresenter;
import com.holike.crm.presenter.fragment.HomePagePresenter;
import com.holike.crm.util.Constants;
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
public class CustomerV2Fragment extends MyFragment<CustomerListPresenter, CustomerV2View> implements CustomerV2View, CustomerController.CustomerControllerView {
    @BindView(R.id.ll_parent)
    LinearLayout llParent;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.tv_customer_manage_customer_state)
    TextView tvCustomerState;
    @BindView(R.id.iv_customer_manage_customer_state)
    ImageView ivCustomerState;
    @BindView(R.id.tv_customer_manage_customer_source)
    TextView tvCustomerSource;
    @BindView(R.id.iv_red_point_msg)
    ImageView ivRedPoint;
    @BindView(R.id.iv_customer_manage_customer_source)
    ImageView ivCustomerSource;
    @BindView(R.id.dv_customer_manage_filter)
    View dvFilter;
    @BindView(R.id.mCustomerRv)
    RecyclerView mCustomerRv;
    @BindView(R.id.srl_customer_manage)
    SmartRefreshLayout mRefreshLayout;

    @BindView(R.id.mTimeTextView)
    TextView mTimeTextView;
    @BindView(R.id.mArrowImageView)
    ImageView mArrowImageView;
    @BindView(R.id.mCountTextView)
    TextView mCountTextView;

    private CustomerController mCustomerController;
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
    protected void init() {
        mCustomerController = new CustomerController(mContext, this);
        mCustomerController.setDefaultTimeText(mTimeTextView);
        mCustomerController.setCustomerAdapter(mCustomerRv);
        mRefreshLayout.setRefreshHeader(new WaterDropHeader(mContext));
        mRefreshLayout.setRefreshFooter(new BallPulseFooter(mContext));
        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshlayout) {
                mCustomerController.onRefresh();
            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshlayout) {
                mCustomerController.onLoadMore();
            }
        });
        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideSoftInput(etSearch);
                search();
            }
            return false;
        });
        getCustomerList();
    }

    private void getCustomerList() {
        mCustomerController.startFirstLoad();
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
        tvCustomerSource.setText(TextUtils.isEmpty(id) ? mContext.getString(R.string.receive_deposit_customerSource) : value);
        reset();
        getCustomerList();
    }

    @Override
    public void onStatusSelected(String id, String value) {
//        tvCustomerState.setText(TextUtils.isEmpty(id) ? mContext.getString(R.string.customer_manage_customer_state) : value);
        reset();
        getCustomerList();
    }

    @Override
    public void onDateSelected(List<Date> selectedDates, Date start, Date end) {
        getCustomerList();
    }

    private void reset() {
        etSearch.setText("");
        mCustomerController.onReset();
    }

    @Override
    public void onGetCustomerList(boolean isShowLoading, String source, String status, Date startDate,
                                  Date endDate, String orderBy, int pageNo, int pageSize) {
        if (isShowLoading) {
            showLoading();
        }
        String searchContent = etSearch.getText().toString();
        mPresenter.getCustomerList(searchContent, source, status, startDate, endDate, orderBy, pageNo, pageSize);
    }

    @Override
    public void onResume() {
        super.onResume();
//        tvMsg.setText(HomePagePresenter.getMsgNum());
        ivRedPoint.setVisibility(HomePagePresenter.isNewMsg() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constants.RESULT_CODE_EDIT_SUCCESS || resultCode == Constants.RESULT_CODE_ADD_SUCCESS) {
            getCustomerList();
        }
    }

    @OnClick({R.id.iv_addCustomer, R.id.ll_customer_manage_customer_state,
            R.id.ll_customer_manage_customer_source, R.id.tv_customer_msg,
            R.id.mTimeLayout, R.id.iv_orderBy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_addCustomer:
                mCustomerController.onClickAddCustomer();
                break;
            case R.id.ll_customer_manage_customer_source:
                mCustomerController.onClickSource(mContentView, dvFilter, ivCustomerSource);
                break;
            case R.id.ll_customer_manage_customer_state:
                mCustomerController.onClickStatus(mContentView, dvFilter, ivCustomerState);
                break;
            case R.id.tv_customer_msg:
                startActivity(MessageV2Activity.class);
                break;
            case R.id.mTimeLayout:
                mCustomerController.showCalendarDialog(mContext, mArrowImageView, mTimeTextView);
                break;
            case R.id.iv_orderBy:
                mCustomerController.onOrderBy((ImageView) view);
                break;
        }
    }

    @Override
    public void onAddCustomer(Intent intent) {
        openActivityForResult(intent);
    }

    /**
     * 搜索
     */
    private void search() {
        MobclickAgent.onEvent(mContext, "customer_search");
        mCustomerController.resetParams(tvCustomerState, tvCustomerSource);
        getCustomerList();
    }

    /**
     * 获取客户列表成功
     */
    @Override
    public void getCustomerListSuccess(CustomerListBeanV2 bean) {
        dismissLoading();
        onLoadComplete();
        mCustomerController.onGetCustomerOk(bean, bean.total, mCountTextView);
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
            if (mCustomerController.isFirstLoading()) {  //首次加载完成（没有加载到数据）
                showEmptyView(false);
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
            mCustomerController.clearData();
            setContentEnabled(false);
            noAuthority();
        } else {
            if (mCustomerController.isFirstLoading()) {
                mCustomerController.clearData();
                showEmptyView(true);
            } else {
                showShortToast(failed);
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
        int pressedType = mCustomerController.getPressedType();
        if (pressedType == 1 || pressedType == 2)
            dismissLoading();
        mCustomerController.onGetSystemCode(bean, mContentView, dvFilter, ivCustomerSource, ivCustomerState);
    }

    @Override
    public void onGetSystemCodeFailure(String failed) {
        int pressedType = mCustomerController.getPressedType();
        if (pressedType == 1 || pressedType == 2)
            dismissLoading();
        if (pressedType == 1 || pressedType == 2) {
            showShortToast(failed);
        }
    }

    @Override
    public void onRequestCallPhone(final String phoneNumber) {
        requestPermission(Manifest.permission.CALL_PHONE, new OnRequestPermissionsCallback() {
            @Override
            public void onGranted(int requestCode, @NonNull String[] permissions) {
                mCustomerController.callPhone(phoneNumber);
            }

            @Override
            public void onDenied(int requestCode, @NonNull String[] permissions, boolean isProhibit) {
                mCustomerController.onDismissCallPermission(isProhibit);
            }
        });
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
            CustomerDetailV2Activity.open((BaseActivity) mContext, bean.personalId, bean.houseId);
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
        mCustomerController.onDeleteCustomer(position, mCountTextView);
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
//        //防止重复创建popupWindow
//        DistributionStorePopupWindow2 popupWindow = new DistributionStorePopupWindow2(mContext, bean, new DistributionStorePopupWindow2.StateCallback() {
//            @Override
//            public void onUpgradeSuccess() {
//                getCustomerList();
//            }
//
//            @Override
//            public void onDismiss() {
//
//            }
//        });
//        popupWindow.showAtLocation(llParent, Gravity.BOTTOM, 0, 0);
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
        super.onDestroyView();
    }
}
