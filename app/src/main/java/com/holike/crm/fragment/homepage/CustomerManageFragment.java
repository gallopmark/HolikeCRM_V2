package com.holike.crm.fragment.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.holike.crm.R;
import com.holike.crm.activity.customer.CustomerDetailV2Activity;
import com.holike.crm.activity.homepage.AddCustomerActivity;
import com.holike.crm.activity.homepage.MessageV2Activity;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.AttBean;
import com.holike.crm.bean.CustomerListBean;
import com.holike.crm.bean.TypeIdBean;
import com.holike.crm.dialog.CalendarPickerDialog;
import com.holike.crm.dialog.SimpleDialog;
import com.holike.crm.dialog.TipViewDialog;
import com.holike.crm.popupwindown.DistributionStorePopupWindow;
import com.holike.crm.presenter.activity.CustomerManagePresenter;
import com.holike.crm.presenter.fragment.HomePagePresenter;
import com.holike.crm.util.Constants;
import com.holike.crm.view.activity.CustomerManageView;
import com.scwang.smartrefresh.header.WaterDropHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.umeng.analytics.MobclickAgent;

import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/*旧版客户列表页面*/
@Deprecated
public class CustomerManageFragment extends MyFragment<CustomerManagePresenter, CustomerManageView> implements CustomerManageView {
    @BindView(R.id.ll_parent)
    LinearLayout llParent;
    @BindView(R.id.et_customer_manage_search)
    EditText etSearch;
    @BindView(R.id.tv_customer_manage_customer_state)
    TextView tvCustomerState;
    @BindView(R.id.iv_customer_manage_customer_state)
    ImageView ivCustomerState;
    @BindView(R.id.tv_customer_manage_customer_source)
    TextView tvCustomerSource;
    @BindView(R.id.tv_customer_msg)
    TextView tvMsg;
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


    private int pageNo = Constants.DEFAULT_PAGE;
    private final int pageSize = 10;

    //    private List<CustomerListBean> customerListBeans;
    private AttBean attBean;
    private TypeIdBean typeIdBean;
    private String customerSource, customerState;
    private Date startDate, endDate;
    private boolean isLoadCompleted, isRefresh;
    private int pressedType = 0;
    private DistributionStorePopupWindow popupWindow;

    @Override
    protected CustomerManagePresenter attachPresenter() {
        return new CustomerManagePresenter();
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_customer_manage;
    }

    @Override
    protected void init() {
        mPresenter.setAdapter(mCustomerRv);
        mRefreshLayout.setRefreshHeader(new WaterDropHeader(mContext));
        mRefreshLayout.setRefreshFooter(new BallPulseFooter(mContext));
        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshlayout) {
                refresh(false);
            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshlayout) {
                loadmore();
            }
        });
        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideSoftInput(etSearch);
                search();
            }
            return false;
        });
        getType();
        refresh(true);
    }

    private void getType() {
        if (pressedType == 1 || pressedType == 2) showLoading();
        mPresenter.getTypeId();
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
            refresh(true);
        }
    }

    @OnClick({R.id.iv_customer_manage_search_add_customer, R.id.ll_customer_manage_customer_state,
            R.id.ll_customer_manage_customer_source, R.id.tv_customer_msg,
            R.id.mTimeLayout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_customer_manage_search_add_customer:
                MobclickAgent.onEvent(mContext, "homepage_add_customer");
                Intent intent = new Intent(mContext, AddCustomerActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constants.TYPE_ID, Parcels.wrap(typeIdBean));
                bundle.putString(Constants.IS_EARNEST, "0");
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.ll_customer_manage_customer_source:
                if (typeIdBean == null) {
                    pressedType = 1;
                    getType();
                } else {
                    mPresenter.showPopupWindow(mContext, mContentView, dvFilter, ivCustomerSource, typeIdBean.getCUSTOMER_SOURCE_CODE(), customerSource, 1);
                }
                break;
            case R.id.ll_customer_manage_customer_state:
                if (typeIdBean == null) {
                    pressedType = 2;
                    getType();
                } else {
                    mPresenter.showPopupWindow(mContext, mContentView, dvFilter, ivCustomerState, typeIdBean.getCUSTOMER_STATUS_MOVE(), customerState, 2);
                }
                break;
            case R.id.tv_customer_msg:
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
//                        refresh(true);
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

    /**
     * 搜索
     */
    public void search() {
        MobclickAgent.onEvent(mContext, "customer_search");
        resetParams();
        refresh(true);
    }

    private void resetParams() {
        this.customerState = null;
        tvCustomerState.setText(mContext.getString(R.string.customer_manage_customer_state));
        this.customerSource = null;
        tvCustomerSource.setText(mContext.getString(R.string.receive_deposit_customerSource));
    }

    private void getCustomerList() {
        String searchContent = etSearch.getText().toString();
        mPresenter.getCustomerList(searchContent, customerSource, customerState, startDate, endDate, pageNo, pageSize);
    }

    /**
     * 获取客户列表成功
     */
    @Override
    public void getCustomerListSuccess(List<CustomerListBean> customerListBeans, @Nullable AttBean attBean) {
        dismissLoading();
        if (!isLoadCompleted) {
            isLoadCompleted = true;
        }
        onLoadComplete();
        if (isRefresh) {
            refreshSuccess(customerListBeans);
        } else {
            if (customerListBeans.size() < pageSize) {
                loadmoreSuccess(customerListBeans);
                loadAll();
            } else {
                loadmoreSuccess(customerListBeans);
            }
        }
        pageNo++;
        if (attBean != null) {
            this.attBean = attBean;
            setTextCount(attBean.totalRows);
        } else {
            mCountTextView.setVisibility(View.GONE);
        }
    }

    private void setTextCount(String totalRows) {
        String source = mContext.getString(R.string.customer_count);
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
        mCountTextView.setVisibility(View.VISIBLE);
    }

    /**
     * 获取客户列表失败
     */
    @Override
    public void getCustomerListFailed(String failed) {
        dismissLoading();
        onLoadComplete();
        if (isNoAuth(failed)) {
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

    /**
     * 重新加载
     */
    @Override
    public void reload() {
        super.reload();
        refresh(true);
    }

    /**
     * 获取类型id成功
     */
    @Override
    public void getTypeIdSuccess(TypeIdBean typeIdBean) {
        if (pressedType == 1 || pressedType == 2)
            dismissLoading();
        this.typeIdBean = typeIdBean;
        if (pressedType == 1) {
            mPresenter.showPopupWindow(mContext, mContentView, dvFilter, ivCustomerSource, typeIdBean.getCUSTOMER_SOURCE_CODE(), customerSource, 1);
        } else if (pressedType == 2) {
            mPresenter.showPopupWindow(mContext, mContentView, dvFilter, ivCustomerState, typeIdBean.getCUSTOMER_STATUS_MOVE(), customerState, 2);
        }
    }

    @Override
    public void getTypeIdFailure(String failed) {
        if (pressedType == 1 || pressedType == 2)
            dismissLoading();
        if (pressedType == 1 || pressedType == 2) {
            showShortToast(failed);
        }
    }

    /**
     * 筛选客户状态
     */
    @Override
    public void getCustomerListByType(String id, String value) {
        MobclickAgent.onEvent(mContext, "customer_state");
        this.customerState = id;
        tvCustomerState.setText(TextUtils.isEmpty(id) ? mContext.getString(R.string.customer_manage_customer_state) : value);
//        this.type = TYPE_GET_LIST;
        clearSearch();
        refresh(true);
    }

    /**
     * 筛选客户来源
     */
    @Override
    public void getCustomerListBySource(String id, String value) {
        MobclickAgent.onEvent(mContext, "customer_source"); //receive_deposit_customerSource
        this.customerSource = id;
        tvCustomerSource.setText(TextUtils.isEmpty(id) ? mContext.getString(R.string.receive_deposit_customerSource) : value);
        clearSearch();
        refresh(true);
    }

    private void clearSearch() {
        etSearch.setText("");
    }

    /**
     * 刷新
     */
    @Override
    public void refresh(boolean showLoading) {
        if (showLoading) {
            showLoading();
        }
        pageNo = Constants.DEFAULT_PAGE;
        isRefresh = true;
        getCustomerList();
    }

    /**
     * 加载更多
     */
    @Override
    public void loadmore() {
        isRefresh = false;
        getCustomerList();
    }

    /**
     * 刷新成功
     */
    @Override
    public void refreshSuccess(List<CustomerListBean> customerListBeans) {
        if (customerListBeans.size() > 0) {
            hasData();
            mPresenter.onRefreshCompleted(customerListBeans);
            if (customerListBeans.size() < pageSize) {
                loadAll();
            } else {
                mRefreshLayout.setEnableLoadMore(true);
            }
        } else {
            noResult();
        }
    }

    /**
     * 加载更多成功
     */
    @Override
    public void loadmoreSuccess(List<CustomerListBean> customerListBeans) {
        mRefreshLayout.setEnableLoadMore(true);
        mPresenter.onLoadMoreCompleted(customerListBeans);
    }

    /**
     * 加载全部完成
     */
    @Override
    public void loadAll() {
        mRefreshLayout.setEnableLoadMore(false);
        mPresenter.noMoreData();
    }

    /*item 点击手机号码TextView*/
    @Override
    public void adapterChildItemClick(CustomerListBean bean) {
        call(bean.getPhoneNumber());
    }

    /*item 长按手机号码TextView*/
    @Override
    public void adapterChildItemLongClick(CustomerListBean bean) {
        copy(bean.getPhoneNumber());
    }

    /*item 点击*/
    @Override
    public void adapterItemClick(CustomerListBean bean) {
        //shopId未空均为未分配门店
        if (TextUtils.isEmpty(bean.getShopId())) {
            customerPopupDialog(bean);
        } else {
            customerDeatil((BaseActivity) mContext, bean.getPersonalId(), bean.getHouseId());
        }
    }

    /*item 长按*/
    @Override
    public void adapterItemLongClick(CustomerListBean bean, int position) {
        List<String> items = new ArrayList<>();
        items.add(mContext.getString(R.string.delete_customer));
        new TipViewDialog(mContext).setItems(items).setOnItemClickListener((dialog, items1, pos) -> {
            dialog.dismiss();
            new SimpleDialog(mContext).setDate(R.string.tips, R.string.delete_customer_tips,
                    R.string.cancel, R.string.confirm).setListener(new SimpleDialog.ClickListener() {
                @Override
                public void left() {

                }

                @Override
                public void right() {
                    showLoading();
                    mPresenter.deleteCustomer(bean.getHouseId(), position);
                }
            }).show();
        }).show();
    }

    @Override
    public void deleteCustomerSuccess(String result, int position) {
        dismissLoading();
        mPresenter.removeCustomer(position);
        showShortToast(R.string.delete_customer_success_tips);
        if (attBean != null) {
            try {
                long totalCount = Long.parseLong(attBean.totalRows) - 1;
                if (totalCount <= 0) totalCount = 0;
                attBean.totalRows = String.valueOf(totalCount);
                setTextCount(attBean.totalRows);
            } catch (Exception ignored) {
            }
        }
    }

    @Override
    public void deleteCustomerFailure(String message) {
        dismissLoading();
        showShortToast(message);
    }

    /**
     * 客户详情
     */
    public static void customerDeatil(BaseActivity context, String personalId, String houseId) {
        Intent intent = new Intent(context, CustomerDetailV2Activity.class);
        intent.putExtra(Constants.PERSONAL_ID, personalId);
        intent.putExtra(Constants.HOUSE_ID, houseId);
        context.startActivityForResult(intent, context.REQUEST_CODE);
    }

    public void customerPopupDialog(CustomerListBean bean) {
        //防止重复创建popupWindow
        if (popupWindow == null) {
            popupWindow = new DistributionStorePopupWindow(mContext, bean, new DistributionStorePopupWindow.StateCallback() {
                @Override
                public void onUpgradeSuccess() {
                    refresh(true);
                }

                @Override
                public void onDismiss() {
                    popupWindow = null;
                }
            });
            popupWindow.showAtLocation(llParent, Gravity.BOTTOM, 0, 0);

        }

    }
}