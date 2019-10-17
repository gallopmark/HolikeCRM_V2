package com.holike.crm.fragment.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.holike.crm.R;
import com.holike.crm.activity.customer.CustomerEditActivity;
import com.holike.crm.adapter.CustomerListAdapter;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.base.IntentValue;
import com.holike.crm.bean.CustomerListBeanV2;
import com.holike.crm.bean.DictionaryBean;
import com.holike.crm.bean.SysCodeItemBean;
import com.holike.crm.dialog.CalendarPickerDialog;
import com.holike.crm.dialog.MaterialDialog;
import com.holike.crm.dialog.TipViewDialog;
import com.holike.crm.enumeration.CustomerValue;
import com.holike.crm.helper.CalendarDialogHelper;
import com.holike.crm.helper.ICallPhoneHelper;
import com.holike.crm.helper.MultiItemListHelper;
import com.holike.crm.popupwindown.MultipleSelectPopupWindow;
import com.holike.crm.popupwindown.StringItemPopupWindow;
import com.holike.crm.rxbus.MessageEvent;
import com.holike.crm.rxbus.RxBus;
import com.holike.crm.util.Constants;
import com.holike.crm.util.KeyBoardUtil;
import com.holike.crm.util.ParseUtils;
import com.umeng.analytics.MobclickAgent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.reactivex.disposables.Disposable;

/*客户列表辅助类*/
public class CustomerHelper extends MultiItemListHelper {

    public interface Callback {
        void onQuerySystemCode(boolean showLoading);

        void onAddCustomer(Intent intent);

        void onSourceSelected(String id, String value);

        void onStatusSelected(String id, String value);

        void onDateSelected(List<Date> selectedDates, Date start, Date end);

        void onGetCustomerList(boolean isShowLoading, String source, String status,
                               Date startDate, Date endDate, String orderBy,
                               int pageNo, int pageSize);

        void onGetCustomerOk(boolean isEmpty, boolean isLoadAll);

        void loadMoreSuccess(boolean isLoadMoreEnabled);

        void onItemClick(CustomerListBeanV2.CustomerBean bean);

        void onDeleteCustomer(CustomerListBeanV2.CustomerBean bean, int position);

//        void onRequestCallPhone(CustomerListBeanV2.CustomerBean bean);

        void onCallPhoneFinish(String body);

        void onItemChildLongClick(CustomerListBeanV2.CustomerBean bean);

        void onDataEmpty();
    }

    private Callback mCallback;
    //    private AttBean mAttBean;
//    private TypeIdBean mTypeIdBean;
    private String mTimeTextDefault;
    private SysCodeItemBean mSystemCode;
    private String mCustomerSource, mCustomerStatus;
    private Date mStartDate, mEndDate;
    private String mOrderBy = "desc"; //排序 默认降序
    private boolean mDesc = true;  //默认降序
    private boolean isShowTotalRows;
    private int mPressedType = 0;
    private long mTotalRows;

    private int mViewPosition = -1;  //被点击的item位置，用于在客户管理页面流失房屋时，从客户列表数据中移除该条数据
    private Disposable mDisposable;

    private CustomerListBeanV2 mCustomerListBean;

    @Override
    public void onReset() {
        super.onReset();
        this.isShowTotalRows = false;
    }

    public int getPressedType() {
        return mPressedType;
    }

    public CustomerHelper(Context context, Callback callback) {
        super(context, 1);
        this.mCallback = callback;
        this.mAdapter = new CustomerListAdapter(mContext, this.mListBeans);
        mSystemCode = IntentValue.getInstance().getSystemCode();
        mTimeTextDefault = mContext.getString(R.string.create_time_tips) + mContext.getString(R.string.all);
        mDisposable = RxBus.getInstance().toObservable(MessageEvent.class).subscribe(event -> {
            if (TextUtils.equals(event.getType(), CustomerValue.EVENT_TYPE_ADD_CUSTOMER) ||  //添加客户
                    TextUtils.equals(event.getType(), CustomerValue.EVENT_TYPE_ALTER_CUSTOMER) //编辑客户
                    || TextUtils.equals(event.getType(), CustomerValue.EVENT_TYPE_RECEIVE_HOUSE)) { //领取房屋
                onRefresh();
            } else if (TextUtils.equals(event.getType(), CustomerValue.EVENT_TYPE_CONFIRM_LOST_HOUSE)) { //流失房屋
                if (mViewPosition >= 0 && mViewPosition < this.mListBeans.size()) {
                    this.mListBeans.remove(mViewPosition);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    void setDefaultTimeText(TextView tv) {
        tv.setText(mTimeTextDefault);
    }

    void setCustomerAdapter(RecyclerView rvCustomer) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        rvCustomer.setLayoutManager(layoutManager);
        rvCustomer.setAdapter(this.mAdapter);
        this.mAdapter.setOnItemChildClickListener((adapter, holder, view, position) -> {
            CustomerListBeanV2.CustomerBean bean = getItem(position);
            if (bean != null) {
                if (!TextUtils.isEmpty(bean.phoneNumber)) { //手机号才能拨打
                    ICallPhoneHelper.with((BaseActivity<?, ?>) mContext).requestCallPhone(bean.personalId, bean.houseId, bean.phoneNumber);
                } else {
                    onItemClick(position, bean);
                }
            }
        });
        this.mAdapter.setOnItemChildLongClickListener((adapter, holder, view, position) -> {
            if (getItem(position) != null) {
                mCallback.onItemChildLongClick(getItem(position));
            }
        });
        this.mAdapter.setOnItemClickListener((adapter, holder, view, position) -> {
            CustomerListBeanV2.CustomerBean bean = getItem(position);
            if (bean != null) {
                onItemClick(position, bean);
            }
        });
//        this.mAdapter.setOnItemLongClickListener((adapter, holder, view, position) -> {
//            if (getItem(position) != null) {
//                onCustomerItemLongClick(getItem(position), position);
//            }
//        });
    }

    private void onItemClick(int position, CustomerListBeanV2.CustomerBean bean) {
        mViewPosition = position;
        mCallback.onItemClick(bean);
    }

    private CustomerListBeanV2.CustomerBean getItem(int position) {
        if (position >= 0 && position < this.mListBeans.size()) {
            if (this.mListBeans.get(position) instanceof CustomerListBeanV2.CustomerBean) {
                return (CustomerListBeanV2.CustomerBean) this.mListBeans.get(position);
            }
        }
        return null;
    }

    @Override
    public void onStartLoad(boolean isFirstLoading) {
        getCustomerList(isFirstLoading);
    }

    /*加载客户列表*/
    private void getCustomerList(boolean isShowLoading) {
        mCallback.onGetCustomerList(isShowLoading, mCustomerSource, mCustomerStatus,
                mStartDate, mEndDate, mOrderBy, mPageNo, mPageSize);
    }

    /*加载客户列表数据成功*/
    void onGetCustomerOk(CustomerListBeanV2 bean, String total, TextView tvCount) {
        this.mCustomerListBean = bean;
        if (!isShowTotalRows) {
            showTotalRows(total, tvCount);
            isShowTotalRows = true;
        }
        onHttpResultOk(bean.getList());
    }

    private void showTotalRows(String total, TextView tvCount) {
        if (!TextUtils.isEmpty(total)) {
            try {
                this.mTotalRows = ParseUtils.parseLong(total);
            } catch (Exception ignored) {
            }
            setCustomerCount(tvCount, total);
            setTotalRows(total);
        } else {
            tvCount.setVisibility(View.GONE);
        }
    }

    /*设置列表总数*/
    private void setTotalRows(String total) {
        //客户总数
        long totalRows;
        try {
            totalRows = ParseUtils.parseLong(total);
        } catch (Exception e) {
            totalRows = 0;
        }
        ((CustomerListAdapter) this.mAdapter).setTotalRows(totalRows);
    }

    /*显示客户数量*/
    private void setCustomerCount(TextView tvCount, String totalRows) {
        Context context = tvCount.getContext();
        String source = context.getString(R.string.tips_total);
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

    @Override
    public void onRefreshSuccess(boolean isFirstLoad, boolean isEmpty, boolean isLoadAll) {
        mCallback.onGetCustomerOk(isEmpty, isLoadAll);
    }

    @Override
    public void onLoadMoreSuccess(boolean isLoadMoreEnabled) {
        mCallback.loadMoreSuccess(isLoadMoreEnabled);
    }

    /*移除客户*/
    private void removeCustomer(int position) {
        this.mListBeans.remove(position);
        notifyItemRemoved(position);
        if (this.mListBeans.contains(mNoMoreBean) && this.mListBeans.size() == 1) {
            this.mListBeans.clear();
            notifyDataSetChanged();
            /*列表数据全部删除，则页面需展示没有结果页*/
            mCallback.onDataEmpty();
        }
    }

    /*已选日期集合*/
    private List<Date> mSelectedDates;

    /*显示日历选择对话框*/
    void showCalendarDialog(Context context, final TextView timeTextView) {
        CalendarDialogHelper.showCalendarDialog(context, mSelectedDates, new CalendarDialogHelper.OnCalendarOperateListener() {
            @Override
            public void onShow() {
                timeTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.ic_choice_date_up), null);
            }

            @Override
            public void onCalendarPicker(CalendarPickerDialog dialog, List<Date> selectedDates, Date start, Date end) {
                mSelectedDates = selectedDates;
                if (selectedDates.size() >= 1) {
                    mStartDate = start;
                    mEndDate = end;
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());
                    String startSource = dateFormat.format(mStartDate);
                    String endSource = dateFormat.format(mEndDate);
                    String text = startSource + "-" + endSource;
                    timeTextView.setText(text);
                } else {
                    clearTime(timeTextView);
                }
                dialog.dismiss();
                mCallback.onDateSelected(selectedDates, start, end);
            }

            @Override
            public void onDismiss() {
                timeTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.ic_choice_date_down), null);
            }
        });
    }

    /*清除时间*/
    private void clearTime(TextView timeTextView) {
        mStartDate = null;
        mEndDate = null;
        setDefaultTimeText(timeTextView);
    }

    /*点击搜索后，客户来源、状态恢复原始状态*/
    @Deprecated
    void resetParams(TextView tvCustomerState, TextView tvCustomerSource) {
        onReset();
        this.mCustomerStatus = null;
        tvCustomerState.setText(mContext.getString(R.string.customer_manage_customer_state));
        this.mCustomerSource = null;
        tvCustomerSource.setText(mContext.getString(R.string.receive_deposit_customerSource));
    }

    /*点击添加客户按钮*/
    void onClickAddCustomer() {
        MobclickAgent.onEvent(mContext, "homepage_add_customer");
        Intent intent = new Intent(mContext, CustomerEditActivity.class);
        Bundle bundle = new Bundle();
//        bundle.putParcelable(Constants.TYPE_ID, Parcels.wrap(mTypeIdBean));
        bundle.putString(Constants.IS_EARNEST, "0");
        intent.putExtras(bundle);
        mCallback.onAddCustomer(intent);
    }

    /*点击客户来源按钮*/
    void onClickSource(View contentView, View parent, TextView tvSource) {
        if (mSystemCode == null) {
            mPressedType = 1;
            mCallback.onQuerySystemCode(true);
        } else {
            showPopupWindow(contentView, parent, tvSource, mSystemCode.getCustomerSourceCode());
        }
    }

    /*点击客户状态按钮*/
    void onClickStatus(View contentView, View parent, TextView tvStatus) {
        if (mSystemCode == null) {
            mPressedType = 2;
            mCallback.onQuerySystemCode(true);
        } else {
            onSelectCustomerStatus(contentView, parent, tvStatus, mSystemCode.getCustomerStatusMove());
        }
    }

    /*获取客户来源、状态等信息成功*/
    void onGetSystemCode(SysCodeItemBean systemCode, View contentView, View parent,
                         TextView tvSource, TextView tvStatus) {
        this.mSystemCode = systemCode;
        if (mPressedType == 1) {
            showPopupWindow(contentView, parent, tvSource, mSystemCode.getCustomerSourceCode());
        } else if (mPressedType == 2) {
            onSelectCustomerStatus(contentView, parent, tvStatus, mSystemCode.getCustomerStatusMove());
        }
    }

    private int mSelectSourceIndex;

    private void showPopupWindow(View contentView, View parent, final TextView tv, Map<String, String> data) {
        long delayMillis = hideKeyBoard(contentView);
        parent.postDelayed(() -> {
            final List<DictionaryBean> list = new ArrayList<>();
            list.add(new DictionaryBean("", mContext.getString(R.string.all)));
            for (Map.Entry<String, String> entry : data.entrySet()) {
                list.add(new DictionaryBean(entry.getKey(), entry.getValue()));
            }
            List<String> items = new ArrayList<>();
            for (DictionaryBean bean : list) {
                items.add(bean.name);
            }
            arrowUp(tv);
            StringItemPopupWindow popupWindow = new StringItemPopupWindow(mContext, items, mSelectSourceIndex);
            popupWindow.setOnMenuItemClickListener((pw, position, content) -> {
                mSelectSourceIndex = position;
                MobclickAgent.onEvent(mContext, "customer_source"); //receive_deposit_customerSource
                mCustomerSource = list.get(position).id;
                mCallback.onSourceSelected(list.get(position).id, list.get(position).name);
                pw.dismiss();
            });
            popupWindow.showAsDown(parent);
            popupWindow.setOnDismissListener(() -> arrowDown(tv));
        }, delayMillis);
    }

    private long hideKeyBoard(View contentView) {
        long delayMillis = 0L;
        if (KeyBoardUtil.isKeyboardShown(contentView)) {
            KeyBoardUtil.hideKeyboard(contentView);
            delayMillis = 200L;
        }
        return delayMillis;
    }

    private List<MultipleSelectPopupWindow.Item> mSelectItems;

    /*选择客户状态*/
    private void onSelectCustomerStatus(View contentView, View parent, final TextView tv, Map<String, String> data) {
        MobclickAgent.onEvent(mContext, "customer_state");
        List<MultipleSelectPopupWindow.Item> items = new ArrayList<>();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            items.add(new MultipleSelectPopupWindow.Item(entry.getKey(), entry.getValue()));
        }
        if (mSelectItems == null) {
            if (mCustomerListBean != null) { //默认被选中的客户状态
                mSelectItems = new ArrayList<>();
                for (MultipleSelectPopupWindow.Item item : items) {
                    for (String id : mCustomerListBean.getCustomerStatusId()) {
                        if (TextUtils.equals(item.code, id)) {
                            mSelectItems.add(item);
                        }
                    }
                }
            }
        }
        long delayMillis = hideKeyBoard(contentView);
        parent.postDelayed(() -> {
            arrowUp(tv);
            MultipleSelectPopupWindow popupWindow = new MultipleSelectPopupWindow(mContext, items, mSelectItems, mContext.getResources().getDimensionPixelSize(R.dimen.dp_50));
            popupWindow.setMultipleChoiceListener(selectedItems -> {
                if (mSelectItems == null) {
                    mSelectItems = new ArrayList<>(selectedItems);
                } else {
                    mSelectItems.clear();
                    mSelectItems.addAll(selectedItems);
                }
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < mSelectItems.size(); i++) {
                    sb.append(mSelectItems.get(i).code);
                    if (i < mSelectItems.size() - 1) {
                        sb.append("@");
                    }
                }
                mCustomerStatus = sb.toString();
                mCallback.onStatusSelected(mCustomerStatus, null);
            });
            popupWindow.showAsDown(parent);
            popupWindow.setOnDismissListener(() -> arrowDown(tv));
        }, delayMillis);
    }

    private void arrowUp(TextView tv) {
        tv.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.choice_up), null);
    }

    private void arrowDown(TextView tv) {
        tv.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.choice_down), null);
    }

    void onOrderBy(ImageView iv) {
        if (mDesc) {
            iv.setImageResource(R.drawable.ic_orderby_asc);
            mOrderBy = "asc";
            mDesc = false;
        } else {
            iv.setImageResource(R.drawable.ic_orderby_desc);
            mOrderBy = "desc";
            mDesc = true;
        }
        ((CustomerListAdapter) mAdapter).setDesc(mDesc);
        startFirstLoad();
    }

    @Deprecated
    protected void onCustomerItemLongClick(final CustomerListBeanV2.CustomerBean bean, final int position) {
        List<String> items = new ArrayList<>();
        items.add(mContext.getString(R.string.delete_customer));
        new TipViewDialog(mContext).setItems(items).setOnItemClickListener((dialog, items1, pos) -> {
            dialog.dismiss();
            new MaterialDialog.Builder(mContext)
                    .title(R.string.dialog_title_default)
                    .message(R.string.delete_customer_tips)
                    .negativeButton(R.string.cancel, null)
                    .positiveButton(R.string.confirm, (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                        mCallback.onDeleteCustomer(bean, position);
                    }).show();
        }).show();
    }

    void onDeleteCustomer(int position, TextView tvCount) {
        removeCustomer(position);
        mTotalRows = mTotalRows - 1;
        if (mTotalRows < 0) {
            mTotalRows = 0;
        }
        setCustomerCount(tvCount, String.valueOf(mTotalRows));
    }

    public void onDispose() {
        mDisposable.dispose();
    }
}
