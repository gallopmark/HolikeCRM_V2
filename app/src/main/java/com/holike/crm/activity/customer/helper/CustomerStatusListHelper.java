package com.holike.crm.activity.customer.helper;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.holike.crm.R;
import com.holike.crm.activity.customer.CustomerDetailV2Activity;
import com.holike.crm.adapter.CustomerStatusListAdapter;
import com.holike.crm.adapter.customer.CustomerCollectMoneyAdapter;
import com.holike.crm.adapter.customer.CustomerDrawingAdapter;
import com.holike.crm.adapter.customer.CustomerHighSeasAdapter;
import com.holike.crm.adapter.customer.CustomerInstalledAdapter;
import com.holike.crm.adapter.customer.CustomerMeasureAdapter;
import com.holike.crm.adapter.customer.CustomerOrderAdapter;
import com.holike.crm.adapter.customer.CustomerOrderPlacedAdapter;
import com.holike.crm.adapter.customer.CustomerPotentialAdapter;
import com.holike.crm.adapter.customer.CustomerRoundsAdapter;
import com.holike.crm.adapter.customer.CustomerUninstallAdapter;
import com.holike.crm.adapter.customer.CustomerUnsignAdapter;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.bean.CustomerStatusBean;
import com.holike.crm.bean.NoMoreBean;
import com.holike.crm.customView.DrawableCenterTextView;
import com.holike.crm.enumeration.CustomerValue;
import com.holike.crm.controller.MultiItemListController;
import com.holike.crm.popupwindown.MultipleSelectPopupWindow;
import com.holike.crm.popupwindown.StringItemPopupWindow;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by gallop 2019/7/8
 * Copyright (c) 2019 holike
 * 客户状态列表帮助类
 */
public class CustomerStatusListHelper extends MultiItemListController {

    public interface CustomerStatusListCallback {

        void onGetCustomerStatusList(boolean isShowLoading, String statusName,
                                     String earnestStatus, String intentionStatus,
                                     String customerStatus, String tailStatus,
                                     String seaStatus, String orderBy,
                                     int pageNo, int pageSize);

        void refreshSuccess(boolean isEmpty, boolean isLoadAll);

        void loadMoreSuccess(boolean isLoadMoreEnabled);
    }

    private BaseActivity<?, ?> mActivity;
    /*状态名称*/
    private String mStatusName;
    private ViewStub mViewStub;
    private CustomerStatusListCallback mStatusCallback;
    //    private boolean isViewInflated;
    private View mTopView;
    private String mEarnestStatus; //1有收取定金 0 无收取定金
    private String mIntentionStatus; //意向评级
    private String mCustomerStatus; //客户状态 多个用@隔开
    private String mTailStatus; //尾款情况
    private String mSeaStatus; //公海分类
    private String mOrderBy; //desc 升序 asc 降序
    private int mSelectedDepositIndex = -1, //已选订金下标
            mSelectedIntentionIndex = -1;   //已选意向评级下标

    private int mSelectedTailIndex = -1;    //已选尾款情况下标
    private int mSelectedHighSeaIndex = -1; //已选公海分类下标

    private boolean isDescByTime = true;  //是否按创建时间升序查询 desc 升序 asc 降序

    private List<MultipleSelectPopupWindow.Item> mSelectedCustomerStatusItems; //已选客户状态集合

    public String getStateName() {
        return mStatusName;
    }

    public CustomerStatusListHelper(BaseActivity<?, ?> activity, String statusName, CustomerStatusListCallback callback) {
        super(activity, 1);
        this.mActivity = activity;
        this.mViewStub = this.mActivity.findViewById(R.id.viewStub);
        this.mStatusName = statusName;
        this.mNoMoreBean = new NoMoreBean();
        this.mStatusCallback = callback;
        setupAdapter();
    }

    private void setupAdapter() {
        if (TextUtils.equals(mStatusName, CustomerValue.POTENTIAL)) { //潜在客户
            mOrderBy = "desc"; //潜在客户可以按时间排序
            mAdapter = new CustomerPotentialAdapter(mActivity, mListBeans);
        } else if (TextUtils.equals(mStatusName, CustomerValue.STAY_MEASURE)) { //待量尺客户
            mAdapter = new CustomerMeasureAdapter(mActivity, mListBeans);
        } else if (TextUtils.equals(mStatusName, CustomerValue.STAY_DRAWING)) { //待出图客户
            mAdapter = new CustomerDrawingAdapter(mActivity, mListBeans);
        } else if (TextUtils.equals(mStatusName, CustomerValue.STAY_ROUNDS)) { //待查房客户
            mAdapter = new CustomerRoundsAdapter(mActivity, mListBeans);
        }
//        else if (TextUtils.equals(mStatusName, CustomerValue.STAY_REMEASURE)) { //待复尺客户
//            mAdapter = new CustomerRemeasureAdapter(mActivity, mListBeans);
//        }
        else if (TextUtils.equals(mStatusName, CustomerValue.STAY_SIGN)) { //待签约客户
            mAdapter = new CustomerUnsignAdapter(mActivity, mListBeans);
        } else if (TextUtils.equals(mStatusName, CustomerValue.STAY_COLLECT_AMOUNT)) { //待收全款客户
            mAdapter = new CustomerCollectMoneyAdapter(mActivity, mListBeans);
        } else if (TextUtils.equals(mStatusName, CustomerValue.ORDER)) { //订单客户
            mAdapter = new CustomerOrderAdapter(mActivity, mListBeans);
        } else if (TextUtils.equals(mStatusName, CustomerValue.ORDER_PLACED)) { //已下单客户
            mAdapter = new CustomerOrderPlacedAdapter(mActivity, mListBeans);
        } else if (TextUtils.equals(mStatusName, CustomerValue.STAY_INSTALL)) { //待安装客户
            mAdapter = new CustomerUninstallAdapter(mActivity, mListBeans);
        } else if (TextUtils.equals(mStatusName, CustomerValue.INSTALLED)) {    //已安装客户
            mAdapter = new CustomerInstalledAdapter(mActivity, mListBeans);
        } else if (TextUtils.equals(mStatusName, CustomerValue.HIGH_SEAS)) { //公海客户
            mOrderBy = "desc"; //公海客户可以按时间排序查询
            mAdapter = new CustomerHighSeasAdapter(mActivity, mListBeans);
        }
    }

    public void setAdapter(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        if (mAdapter != null) {
            recyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener((adapter, holder, view, position) -> {
                CustomerStatusBean.InnerBean bean = getItem(position);
                if (bean != null) {
                    CustomerDetailV2Activity.open(mActivity, bean.personalId, bean.houseId);
                }
            });
            mAdapter.setOnItemChildClickListener((adapter, holder, view, position) -> {
                CustomerStatusBean.InnerBean item = getItem(position);
                if (item != null && !TextUtils.isEmpty(item.phoneNumber)) {
                    mActivity.call(item.phoneNumber);
                }
            });
            mAdapter.setOnItemChildLongClickListener((adapter, holder, view, position) -> {
                CustomerStatusBean.InnerBean item = getItem(position);
                if (item != null && !TextUtils.isEmpty(item.phoneNumber)) {
                    mActivity.copy(item.phoneNumber);
                }
            });
        }
    }

    private CustomerStatusBean.InnerBean getItem(int position) {
        if (position >= 0 && position < mListBeans.size()) {
            if (mListBeans.get(position) instanceof CustomerStatusBean.InnerBean) {
                return (CustomerStatusBean.InnerBean) mListBeans.get(position);
            }
        }
        return null;
    }

    @Override
    public void onStartLoad(boolean isFirstLoading) {
        getCustomerStatusList(isFirstLoading);
    }

    private void getCustomerStatusList(boolean isShowLoading) {
        mStatusCallback.onGetCustomerStatusList(isShowLoading, mStatusName, mEarnestStatus, mIntentionStatus,
                mCustomerStatus, mTailStatus, mSeaStatus, mOrderBy, mPageNo, mPageSize);
    }

    @Override
    public void onRefreshSuccess(boolean isEmpty, boolean isLoadAll) {
        mStatusCallback.refreshSuccess(isEmpty, isLoadAll);
    }

    @Override
    public void onLoadMoreSuccess(boolean isLoadMoreEnabled) {
        mStatusCallback.loadMoreSuccess(isLoadMoreEnabled);
    }

    /*加载完成回调*/
    public void onHttpResponse(CustomerStatusBean bean, List<CustomerStatusBean.InnerBean> listBeans) {
        setupViewStub(bean);
        setTotalRows(bean.getTotal());
        onHttpResultOk(listBeans);
    }

    private void setupViewStub(CustomerStatusBean bean) {
        if (TextUtils.equals(mStatusName, CustomerValue.POTENTIAL)) {
            //潜在客户
            updatePotentialTop(bean);
        } else if (TextUtils.equals(mStatusName, CustomerValue.STAY_MEASURE)) {
            //待量尺客户
            updateUnMeasure(bean);
        } else if (TextUtils.equals(mStatusName, CustomerValue.STAY_COLLECT_AMOUNT)) {
            //待收全款客户
            updateCollectMoneyTop(bean);
        } else if (TextUtils.equals(mStatusName, CustomerValue.ORDER)) {
            //订单客户
            updateOrderTop(bean);
        } else if (TextUtils.equals(mStatusName, CustomerValue.ORDER_PLACED)) {
            //已下单客户
            updateOrderPlacedTop(bean);
        } else if (TextUtils.equals(mStatusName, CustomerValue.STAY_INSTALL)) {
            //待安装客户
            updateUninstallTop(bean);
        } else if (TextUtils.equals(mStatusName, CustomerValue.HIGH_SEAS)) {
            //公海客户
            updateHighSeasTop(bean);
        } else {
            updateOtherTop(bean);
        }
    }

    /*潜在客户头部布局*/
    private void updatePotentialTop(final CustomerStatusBean bean) {
        if (mTopView == null) {
            mViewStub.setLayoutResource(R.layout.viewstub_customer_potential_top);
            mTopView = mViewStub.inflate();
            String depositType = mContext.getString(R.string.customer_deposit_collection_arrow);
            for (int i = 0; i < bean.getCustomerEarnestStatus().size(); i++) {
                CustomerStatusBean.CodeBean codeBean = bean.getCustomerEarnestStatus().get(i);
                if (codeBean.isSelected()) {
                    mSelectedDepositIndex = i;
                    if (!TextUtils.equals(codeBean.name, mContext.getString(R.string.all))) {
                        depositType = codeBean.name;
                    }
                    break;
                }
            }
            DrawableCenterTextView tvDeposit = mTopView.findViewById(R.id.tv_deposit);
            tvDeposit.setText(depositType);
            String intentionType = mContext.getString(R.string.customer_intention_arrow);
            for (int i = 0; i < bean.getCustomerIntentionStatus().size(); i++) {
                CustomerStatusBean.CodeBean codeBean = bean.getCustomerIntentionStatus().get(i);
                if (codeBean.isSelected()) {
                    mSelectedIntentionIndex = i;
                    if (!TextUtils.equals(codeBean.name, mContext.getString(R.string.all))) {
                        intentionType = codeBean.name;
                    }
                    break;
                }
            }
            DrawableCenterTextView tvIntention = mTopView.findViewById(R.id.tv_intention);
            tvIntention.setText(intentionType);
        }
        DrawableCenterTextView tvDeposit = mTopView.findViewById(R.id.tv_deposit);
        DrawableCenterTextView tvIntention = mTopView.findViewById(R.id.tv_intention);
        final TextView tvCreateTime = mTopView.findViewById(R.id.tv_create_time);
        TextView tvCount = mTopView.findViewById(R.id.mCountTextView);
        setCustomerCount(tvCount, bean.total);
        View.OnClickListener listener = v -> {
            if (v.getId() == R.id.tv_deposit) {
                onSelectDeposit((TextView) v, bean.getCustomerEarnestStatus());
            } else if (v.getId() == R.id.tv_intention) {
                onSelectIntention((TextView) v, bean.getCustomerIntentionStatus());
            } else if (v.getId() == R.id.tv_create_time) {
                onSwitchTime(tvCreateTime);
            }
        };
        tvCreateTime.setOnClickListener(listener);
        tvDeposit.setOnClickListener(listener);
        tvIntention.setOnClickListener(listener);
    }

    /*时间排序*/
    private void onSwitchTime(TextView tv) {
        if (isDescByTime) {
            mOrderBy = "asc";
            isDescByTime = false;
            tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.po_cu_gr_do, 0);
        } else {
            mOrderBy = "desc";
            isDescByTime = true;
            tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.po_cu_as_or, 0);
        }
        if (mAdapter != null) {
            ((CustomerStatusListAdapter) mAdapter).setDesc(isDescByTime);
        }
        startFirstLoad();
    }

    /*选择订金*/
    private void onSelectDeposit(final TextView tv, final List<CustomerStatusBean.CodeBean> list) {
        setArrowUp(tv);
        List<String> items = new ArrayList<>();
        for (CustomerStatusBean.CodeBean bean : list) {
            items.add(bean.name);
        }
        StringItemPopupWindow popupWindow = new StringItemPopupWindow(mActivity, items, mSelectedDepositIndex)
                .setOnMenuItemClickListener((pw, position, content) -> {
                    mSelectedDepositIndex = position;
                    mEarnestStatus = list.get(position).code;
                    if (TextUtils.equals(content, mContext.getString(R.string.all))) {
                        tv.setText(mContext.getString(R.string.customer_deposit_collection_arrow));
                    } else {
                        tv.setText(content);
                    }
                    pw.dismiss();
                    startFirstLoad();
                });
        popupWindow.showAsDown(tv);
        popupWindow.setOnDismissListener(() -> setArrowDown(tv));
    }

    /*选择意向评级*/
    private void onSelectIntention(final TextView tv, final List<CustomerStatusBean.CodeBean> list) {
        setArrowUp(tv);
        List<String> items = new ArrayList<>();
        for (CustomerStatusBean.CodeBean bean : list) {
            items.add(bean.name);
        }
        StringItemPopupWindow popupWindow = new StringItemPopupWindow(mActivity, items, mSelectedIntentionIndex)
                .setOnMenuItemClickListener((pw, position, content) -> {
                    mSelectedIntentionIndex = position;
                    mIntentionStatus = list.get(position).code;
                    if (TextUtils.equals(content, mContext.getString(R.string.all))) {
                        tv.setText(mContext.getString(R.string.customer_intention_arrow));
                    } else {
                        tv.setText(content);
                    }
                    pw.dismiss();
                    startFirstLoad();
                });
        popupWindow.showAsDown(tv);
        popupWindow.setOnDismissListener(() -> setArrowDown(tv));
    }

    /*待量尺客户头部布局*/
    private void updateUnMeasure(CustomerStatusBean bean) {
        if (mTopView == null) {
            mViewStub.setLayoutResource(R.layout.viewstub_customer_public_top);
            mTopView = mViewStub.inflate();
        }
        TextView tvLeft = mTopView.findViewById(R.id.tv_left);
        tvLeft.setText(obtainText(bean.total));
        TextView tvRight = mTopView.findViewById(R.id.tv_right);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText(obtainRedBold(mActivity.getString(R.string.customer_today_demand_room), bean.today));
    }

    /*待收全款头部布局*/
    private void updateCollectMoneyTop(final CustomerStatusBean bean) {
        if (mTopView == null) {
            mViewStub.setLayoutResource(R.layout.viewstub_customer_collect_top);
            mTopView = mViewStub.inflate();
        }
        final TextView tvStatus = mTopView.findViewById(R.id.tv_status);
        tvStatus.setOnClickListener(v -> onSelectCustomerStatus(tvStatus, bean));
        TextView tvCount = mTopView.findViewById(R.id.mCountTextView);
        setCustomerCount(tvCount, bean.total);
    }

    /*选择客户状态*/
    private void onSelectCustomerStatus(final TextView parent, CustomerStatusBean bean) {
        setArrowUp(parent);
        List<MultipleSelectPopupWindow.Item> items = new ArrayList<>();
        for (CustomerStatusBean.CodeBean codeBean : bean.getCustomerStatusList()) {
            items.add(new MultipleSelectPopupWindow.Item(codeBean.code, codeBean.name));
        }
        if (mSelectedCustomerStatusItems == null) {
            mSelectedCustomerStatusItems = new ArrayList<>();
            for (CustomerStatusBean.CodeBean codeBean : bean.getCustomerStatusList()) {
                if (codeBean.isSelected()) {
                    mSelectedCustomerStatusItems.add(new MultipleSelectPopupWindow.Item(codeBean.code, codeBean.name));
                }
            }
        }
        MultipleSelectPopupWindow popupWindow = new MultipleSelectPopupWindow(mContext, items, mSelectedCustomerStatusItems);
        popupWindow.setMultipleChoiceListener(selectedItems -> {
            mSelectedCustomerStatusItems.clear();
            mSelectedCustomerStatusItems.addAll(selectedItems);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < selectedItems.size(); i++) {
                sb.append(selectedItems.get(i).code);
                if (i < selectedItems.size() - 1) {
                    sb.append("@");
                }
            }
            mCustomerStatus = sb.toString();
            startFirstLoad();
        });
        popupWindow.showAsDown(parent);
        popupWindow.setOnDismissListener(() -> setArrowDown(parent));
    }

    /*订单客户头部布局*/
    private void updateOrderTop(CustomerStatusBean bean) {
        if (mTopView == null) {
            mViewStub.setLayoutResource(R.layout.viewstub_customer_order_top);
            mTopView = mViewStub.inflate();
            String tailStatus = mContext.getString(R.string.customer_tail_status);
            for (int i = 0; i < bean.getCustomerTailStatus().size(); i++) {
                CustomerStatusBean.CodeBean codeBean = bean.getCustomerTailStatus().get(i);
                if (codeBean.isSelected()) {
                    mSelectedTailIndex = i;
                    if (!TextUtils.equals(codeBean.name, mContext.getString(R.string.all))) {
                        tailStatus = codeBean.name;
                    }
                    break;
                }
            }
            DrawableCenterTextView tvTail = mTopView.findViewById(R.id.tv_tail);
            tvTail.setText(tailStatus);
        }
        DrawableCenterTextView tvStatus = mTopView.findViewById(R.id.tv_status);
        DrawableCenterTextView tvTail = mTopView.findViewById(R.id.tv_tail);
        View.OnClickListener listener = v -> {
            if (v.getId() == R.id.tv_status) {
                //客户状态
                onSelectCustomerStatus(tvStatus, bean);
            } else if (v.getId() == R.id.tv_tail) {
                //尾款请款
                onSelectTailStatus(tvTail, bean.getCustomerTailStatus());
            }
        };
        tvStatus.setOnClickListener(listener);
        tvTail.setOnClickListener(listener);
        TextView tvCount = mTopView.findViewById(R.id.mCountTextView);
        setCustomerCount(tvCount, bean.total);
    }

    /*已下单客户头部布局*/
    private void updateOrderPlacedTop(CustomerStatusBean bean) {
        if (mTopView == null) {
            mViewStub.setLayoutResource(R.layout.viewstub_customer_public_top);
            mTopView = mViewStub.inflate();
        }
        TextView tvLeft = mTopView.findViewById(R.id.tv_left);
        tvLeft.setText(obtainText(mContext.getString(R.string.customer_pending_installation_tips), bean.total));
    }

    /*选择尾款情况*/
    private void onSelectTailStatus(TextView tv, List<CustomerStatusBean.CodeBean> list) {
        if (list.isEmpty()) return;
        setArrowUp(tv);
        List<String> items = new ArrayList<>();
        for (CustomerStatusBean.CodeBean bean : list) {
            items.add(bean.name);
        }
        StringItemPopupWindow popupWindow = new StringItemPopupWindow(mContext, items, mSelectedTailIndex);
        popupWindow.setOnMenuItemClickListener((pw, position, content) -> {
            mSelectedTailIndex = position;
            mTailStatus = list.get(position).code;
            if (TextUtils.equals(content, mContext.getString(R.string.all))) {
                tv.setText(mContext.getString(R.string.customer_tail_status));
            } else {
                tv.setText(content);
            }
            pw.dismiss();
            startFirstLoad();
        });
        popupWindow.showAsDown(tv);
        popupWindow.setOnDismissListener(() -> setArrowDown(tv));
    }

    /*待安装客户头部布局*/
    private void updateUninstallTop(CustomerStatusBean bean) {
        if (mTopView == null) {
            mViewStub.setLayoutResource(R.layout.viewstub_customer_public_top);
            mTopView = mViewStub.inflate();
        }
        TextView tvLeft = mTopView.findViewById(R.id.tv_left);
        tvLeft.setText(obtainText(bean.total));
        TextView tvRight = mTopView.findViewById(R.id.tv_right);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText(obtainRedBold(mActivity.getString(R.string.customer_today_need_install), bean.today));
    }

    /*公海客户头部布局*/
    private void updateHighSeasTop(CustomerStatusBean bean) {
        if (mTopView == null) {
            mViewStub.setLayoutResource(R.layout.viewstub_customer_highseas_top);
            mTopView = mViewStub.inflate();
            List<CustomerStatusBean.CodeBean> list = bean.getCustomerSeaStatus();
            String selectedHighSeasType = mContext.getString(R.string.customer_high_seas_type_tips2);
            for (int i = 0; i < list.size(); i++) {
                CustomerStatusBean.CodeBean codeBean = list.get(i);
                if (codeBean.isSelected()) {
                    mSelectedHighSeaIndex = i;
                    if (!TextUtils.equals(codeBean.name, mContext.getString(R.string.all))) {
                        selectedHighSeasType = codeBean.name;
                    }
                    break;
                }
            }
            TextView tvHighSeasType = mTopView.findViewById(R.id.tv_highSeasType);
            tvHighSeasType.setText(selectedHighSeasType);
        }
        final TextView tvCreateTime = mTopView.findViewById(R.id.tv_create_time);
        TextView tvHighSeasType = mTopView.findViewById(R.id.tv_highSeasType);
        View.OnClickListener listener = v -> {
            if (v.getId() == R.id.tv_create_time) {
                //创建时间
                onSwitchTime(tvCreateTime);
            } else if (v.getId() == R.id.tv_highSeasType) {
                //公海分类
                onSelectHighSeaSort(tvHighSeasType, bean.getCustomerSeaStatus());
            }
        };
        tvCreateTime.setOnClickListener(listener);
        tvHighSeasType.setOnClickListener(listener);
        TextView tvCount = mTopView.findViewById(R.id.mCountTextView);
        setCustomerCount(tvCount, bean.total);
    }

    /*选择公海分类*/
    private void onSelectHighSeaSort(final TextView tv, final List<CustomerStatusBean.CodeBean> list) {
        if (list.isEmpty()) return;
        setArrowUp(tv);
        List<String> items = new ArrayList<>();
        for (CustomerStatusBean.CodeBean bean : list) {
            items.add(bean.name);
        }
        StringItemPopupWindow popupWindow = new StringItemPopupWindow(mContext, items, mSelectedHighSeaIndex);
        popupWindow.setOnMenuItemClickListener((pw, position, content) -> {
            mSelectedHighSeaIndex = position;
            mSeaStatus = list.get(position).code;
            if (TextUtils.equals(content, mContext.getString(R.string.all))) {
                tv.setText(mContext.getString(R.string.customer_high_seas_type_tips2));
            } else {
                tv.setText(content);
            }
            pw.dismiss();
            startFirstLoad();
        });
        popupWindow.showAsDown(tv);
        popupWindow.setOnDismissListener(() -> setArrowDown(tv));
    }

    /*其他客户头部布局*/
    private void updateOtherTop(CustomerStatusBean bean) {
        if (mTopView == null) {
            mViewStub.setLayoutResource(R.layout.viewstub_customer_public_top);
            mTopView = mViewStub.inflate();
        }
        TextView tvLeft = mTopView.findViewById(R.id.tv_left);
        tvLeft.setText(obtainText(bean.total));
        TextView tvRight = mTopView.findViewById(R.id.tv_right);
        if (TextUtils.equals(mStatusName, CustomerValue.STAY_MEASURE)) {
            tvRight.setText(obtainRedBold(mActivity.getString(R.string.customer_today_demand_room), bean.today));
            tvRight.setVisibility(View.VISIBLE);
        } else {
            tvRight.setVisibility(View.GONE);
        }
    }

    /*设置列表总数*/
    private void setTotalRows(long totalRows) {
        if (mAdapter != null) {
            ((CustomerStatusListAdapter) mAdapter).setTotalRows(totalRows);
        }
    }

    private SpannableString obtainText(String content) {
        String origin = "";
        if (!TextUtils.isEmpty(mStatusName)) {
            origin += mStatusName;
        }
        origin += "：";
        return obtainText(origin, content);
    }

    private SpannableString obtainText(String origin, String content) {
        String source = origin;
        int start = source.length();
        if (!TextUtils.isEmpty(content)) {
            source += content;
        }
        int end = source.length();
        SpannableString ss = new SpannableString(source);
        ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mActivity, R.color.textColor13)), start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new StyleSpan(Typeface.BOLD), start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    private SpannableString obtainRedBold(String origin, String content) {
        int start = origin.length();
        String source = origin;
        if (!TextUtils.isEmpty(content)) {
            source += content;
        }
        int end = source.length();
        SpannableString ss = new SpannableString(source);
        ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mActivity, R.color.textColor15)), start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new StyleSpan(Typeface.BOLD), start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    /*显示客户数量*/
    private void setCustomerCount(TextView tvCount, String totalRows) {
        Context context = tvCount.getContext();
        String source = context.getString(R.string.customer_count);
        int start = source.length();
        if (!TextUtils.isEmpty(totalRows)) {
            source += totalRows;
        }
        int end = source.length();
        SpannableString ss = new SpannableString(source);
        int flags = SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE;
        ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.textColor13)), start, end, flags);
        ss.setSpan(new StyleSpan(Typeface.BOLD), start, end, flags);
        tvCount.setText(ss);
        tvCount.setVisibility(View.VISIBLE);
    }

    /*设置角标朝上*/
    private void setArrowUp(TextView textView) {
        textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.choice_up, 0);
    }

    /*设置角标朝下*/
    private void setArrowDown(TextView textView) {
        textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.choice_down, 0);
    }
}
