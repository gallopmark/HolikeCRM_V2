package com.holike.crm.controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
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
import com.holike.crm.base.IntentValue;
import com.holike.crm.bean.CustomerListBeanV2;
import com.holike.crm.bean.SysCodeItemBean;
import com.holike.crm.dialog.CalendarPickerDialog;
import com.holike.crm.dialog.MaterialDialog;
import com.holike.crm.dialog.TipViewDialog;
import com.holike.crm.enumeration.CustomerValue;
import com.holike.crm.helper.CalendarDialogHelper;
import com.holike.crm.popupwindown.FilterPopupWindow;
import com.holike.crm.popupwindown.MultipleSelectPopupWindow;
import com.holike.crm.rxbus.MessageEvent;
import com.holike.crm.rxbus.RxBus;
import com.holike.crm.util.AppUtils;
import com.holike.crm.util.Constants;
import com.holike.crm.util.KeyBoardUtil;
import com.umeng.analytics.MobclickAgent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/*客户列表辅助类*/
public class CustomerController extends MultiItemListController {

    public interface CustomerControllerView {
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

        void onRequestCallPhone(String phoneNumber);

        void onCallPhoneFinish(String body);

        void onItemChildLongClick(CustomerListBeanV2.CustomerBean bean);

        void onDataEmpty();
    }

    private CustomerControllerView mCustomerControllerView;
    //    private AttBean mAttBean;
//    private TypeIdBean mTypeIdBean;
    private String mTimeTextDefault;
    private SysCodeItemBean mSystemCode;
    private String mCustomerSource, mCustomerStatus;
    private Date mStartDate, mEndDate;
    private String mOrderBy = "desc"; //排序 默认降序
    private boolean mDesc = true;
    private boolean isShowTotalRows;
    private int mPressedType = 0;
    private long mTotalRows;

    /*监听通话记录*/
    private CustomerListBeanV2 mCustomerListBean;

    @Override
    public void onReset() {
        super.onReset();
        this.isShowTotalRows = false;
    }

    public int getPressedType() {
        return mPressedType;
    }

    public CustomerController(Context context, CustomerControllerView customerControllerView) {
        super(context, 1);
        this.mCustomerControllerView = customerControllerView;
        this.mAdapter = new CustomerListAdapter(mContext, this.mListBeans);
        mSystemCode = IntentValue.getInstance().getSystemCode();
        mTimeTextDefault = mContext.getString(R.string.create_time_tips) + mContext.getString(R.string.all);
    }

    public void setDefaultTimeText(TextView tv) {
        tv.setText(mTimeTextDefault);
    }

    public void setCustomerAdapter(RecyclerView rvCustomer) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        rvCustomer.setLayoutManager(layoutManager);
        rvCustomer.setAdapter(this.mAdapter);
        this.mAdapter.setOnItemChildClickListener((adapter, holder, view, position) -> {
            CustomerListBeanV2.CustomerBean bean = getItem(position);
            if (bean != null && bean.isPhoneNumber() && !TextUtils.isEmpty(bean.phoneNumber)) {  //手机号才能拨打
                MessageEvent event = new MessageEvent();
                Bundle bundle = new Bundle();
                bundle.putString("phoneNumber", bean.phoneNumber);
                bundle.putString(CustomerValue.PERSONAL_ID, bean.personalId);
                bundle.putString(CustomerValue.HOUSE_ID, bean.houseId);
                event.setArguments(bundle);
                RxBus.getInstance().post(event);
                onRequestCall(bean.phoneNumber);
            }
        });
        this.mAdapter.setOnItemChildLongClickListener((adapter, holder, view, position) -> {
            if (getItem(position) != null) {
                mCustomerControllerView.onItemChildLongClick(getItem(position));
            }
        });
        this.mAdapter.setOnItemClickListener((adapter, holder, view, position) -> {
            if (getItem(position) != null) {
                mCustomerControllerView.onItemClick(getItem(position));
            }
        });
//        this.mAdapter.setOnItemLongClickListener((adapter, holder, view, position) -> {
//            if (getItem(position) != null) {
//                onCustomerItemLongClick(getItem(position), position);
//            }
//        });
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
    public void getCustomerList(boolean isShowLoading) {
        mCustomerControllerView.onGetCustomerList(isShowLoading, mCustomerSource, mCustomerStatus,
                mStartDate, mEndDate, mOrderBy, mPageNo, mPageSize);
    }

    /*加载客户列表数据成功*/
    public void onGetCustomerOk(CustomerListBeanV2 bean, String total, TextView tvCount) {
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
                this.mTotalRows = Long.parseLong(total);
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
            totalRows = Long.parseLong(total);
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
    public void onRefreshSuccess(boolean isEmpty, boolean isLoadAll) {
        mCustomerControllerView.onGetCustomerOk(isEmpty, isLoadAll);
    }

    @Override
    public void onLoadMoreSuccess(boolean isLoadMoreEnabled) {
        mCustomerControllerView.loadMoreSuccess(isLoadMoreEnabled);
    }

    /*移除客户*/
    private void removeCustomer(int position) {
        this.mListBeans.remove(position);
        notifyItemRemoved(position);
        if (this.mListBeans.contains(mNoMoreBean) && this.mListBeans.size() == 1) {
            this.mListBeans.clear();
            notifyDataSetChanged();
            /*列表数据全部删除，则页面需展示没有结果页*/
            mCustomerControllerView.onDataEmpty();
        }
    }

    /*已选日期集合*/
    private List<Date> mSelectedDates;

    /*显示日历选择对话框*/
    public void showCalendarDialog(Context context, final ImageView ivArrow, final TextView timeTextView) {
        CalendarDialogHelper.showCalendarDialog(context, mSelectedDates, new CalendarDialogHelper.OnCalendarOperateListener() {
            @Override
            public void onShow() {
                ivArrow.setRotation(180);
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
                mCustomerControllerView.onDateSelected(selectedDates, start, end);
            }

            @Override
            public void onDismiss() {
                ivArrow.setRotation(0);
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
    public void resetParams(TextView tvCustomerState, TextView tvCustomerSource) {
        onReset();
        this.mCustomerStatus = null;
        tvCustomerState.setText(mContext.getString(R.string.customer_manage_customer_state));
        this.mCustomerSource = null;
        tvCustomerSource.setText(mContext.getString(R.string.receive_deposit_customerSource));
    }

    /*点击添加客户按钮*/
    public void onClickAddCustomer() {
        MobclickAgent.onEvent(mContext, "homepage_add_customer");
        Intent intent = new Intent(mContext, CustomerEditActivity.class);
        Bundle bundle = new Bundle();
//        bundle.putParcelable(Constants.TYPE_ID, Parcels.wrap(mTypeIdBean));
        bundle.putString(Constants.IS_EARNEST, "0");
        intent.putExtras(bundle);
        mCustomerControllerView.onAddCustomer(intent);
    }

    /*点击客户来源按钮*/
    public void onClickSource(View contentView, View dvFilter, ImageView ivCustomerSource) {
        if (mSystemCode == null) {
            mPressedType = 1;
            mCustomerControllerView.onQuerySystemCode(true);
        } else {
            showPopupWindow(contentView, dvFilter, ivCustomerSource, mSystemCode.getCustomerSourceCode());
        }
    }

    /*点击客户状态按钮*/
    public void onClickStatus(View contentView, View dvFilter, ImageView ivCustomerState) {
        if (mSystemCode == null) {
            mPressedType = 2;
            mCustomerControllerView.onQuerySystemCode(true);
        } else {
            onSelectCustomerStatus(contentView, dvFilter, ivCustomerState, mSystemCode.getCustomerStatusMove());
        }
    }

    /*获取客户来源、状态等信息成功*/
    public void onGetSystemCode(SysCodeItemBean systemCode, View contentView, View dvFilter, ImageView ivCustomerSource, ImageView ivCustomerStatus) {
        this.mSystemCode = systemCode;
        if (mPressedType == 1) {
            showPopupWindow(contentView, dvFilter, ivCustomerSource, mSystemCode.getCustomerSourceCode());
        } else if (mPressedType == 2) {
            onSelectCustomerStatus(contentView, dvFilter, ivCustomerStatus, mSystemCode.getCustomerStatusMove());
        }
    }

    private void showPopupWindow(View contentView, View parent, View arrowView, Map<String, String> data) {
        long delayMillis = hideKeyBoard(contentView);
        parent.postDelayed(() -> {
            FilterPopupWindow filterPopupWindow = new FilterPopupWindow(mContext).
                    setData(data, mCustomerSource, arrowView).setSelectListener(new FilterPopupWindow.SelectListener() {
                @Override
                public void select(String selectId, String selectValue) {
                    MobclickAgent.onEvent(mContext, "customer_source"); //receive_deposit_customerSource
                    mCustomerSource = selectId;
                    mCustomerControllerView.onSourceSelected(selectId, selectValue);
                }

                @Override
                public void onDismiss() {
                }
            });
            filterPopupWindow.showAsDropDown(parent);
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
    private void onSelectCustomerStatus(View contentView, View parent, View arrowView, Map<String, String> data) {
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
        arrowView.setRotationX(180);
        parent.postDelayed(() -> {
            MultipleSelectPopupWindow popupWindow = new MultipleSelectPopupWindow(mContext, items, mSelectItems);
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
                mCustomerControllerView.onStatusSelected(mCustomerStatus, null);
            });
            popupWindow.showAsDown(parent);
            popupWindow.setOnDismissListener(() -> arrowView.setRotationX(0));
        }, delayMillis);
    }

    public void onOrderBy(ImageView iv) {
        if (mDesc) {
            iv.setImageResource(R.drawable.po_cu_gr_do);
            mOrderBy = "asc";
            mDesc = false;
        } else {
            iv.setImageResource(R.drawable.po_cu_as_or);
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
                        mCustomerControllerView.onDeleteCustomer(bean, position);
                    }).show();
//            new SimpleDialog(mContext).setDate(R.string.tips, R.string.delete_customer_tips,
//                    R.string.cancel, R.string.confirm).setListener(new SimpleDialog.ClickListener() {
//                @Override
//                public void left() {
//
//                }
//
//                @Override
//                public void right() {
//                    mCustomerControllerView.onDeleteCustomer(bean, position);
//                }
//            }).show();
        }).show();
    }

    public void onDeleteCustomer(int position, TextView tvCount) {
        removeCustomer(position);
        mTotalRows = mTotalRows - 1;
        if (mTotalRows < 0) {
            mTotalRows = 0;
        }
        setCustomerCount(tvCount, String.valueOf(mTotalRows));
    }

    private void onRequestCall(final String phoneNumber) {
        new MaterialDialog.Builder(mContext)
                .title(R.string.tips_call)
                .message(phoneNumber)
                .negativeButton(R.string.cancel, null)
                .positiveButton(R.string.call, (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    mCustomerControllerView.onRequestCallPhone(phoneNumber);
                }).show();
//        new SimpleDialog(mContext).setDate(mContext.getString(R.string.tips_call)
//                , phoneNumber, mContext.getString(R.string.cancel),
//                mContext.getString(R.string.call)).setListener(new SimpleDialog.ClickListener() {
//            @Override
//            public void left() {
//            }
//
//            @Override
//            public void right() {
//                mCustomerControllerView.onRequestCallPhone();
//            }
//        }).show();
    }

    @SuppressLint("MissingPermission")
    public void callPhone(String phoneNumber) {
        try {
            Intent intent = new Intent(Intent.ACTION_CALL);
            Uri data = Uri.parse("tel:" + phoneNumber);
            intent.setData(data);
            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onDismissCallPermission(boolean isProhibit) {
        if (isProhibit) {
            new MaterialDialog.Builder(mContext)
                    .title(R.string.dialog_title_default)
                    .message(R.string.tips_dismiss_callPhone)
                    .negativeButton(R.string.cancel, null)
                    .positiveButton(R.string.call, (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                        AppUtils.openSettings(mContext);
                    }).show();
//            new SimpleDialog(mContext).setDate(mContext.getString(R.string.tips)
//                    , mContext.getString(R.string.tips_dismiss_callPhone), mContext.getString(R.string.cancel),
//                    mContext.getString(R.string.confirm)).setListener(new SimpleDialog.ClickListener() {
//                @Override
//                public void left() {
//                }
//
//                @Override
//                public void right() {
//                    AppUtils.openSettings(mContext);
//                }
//            }).show();
        }
    }
//
//    final class SimplePhoneListener extends PhoneStateListener {
//        private final Context context;
//        //获取本次通话的时间(单位:秒)
//        private int time = 0;
//        //判断是否正在通话
//        private boolean isCalling;
//        //控制循环是否结束
//        boolean isFinish;
//        private ExecutorService service;
//
//        SimplePhoneListener(Context context) {
//            this.context = context;
//            service = Executors.newSingleThreadExecutor();
//        }
//
//        @Override
//        public void onCallStateChanged(int state, String phoneNumber) {
//            switch (state) {
//                case TelephonyManager.CALL_STATE_IDLE:
//                    LogCat.e("电话处于休闲状态中...");
//                    if (isCalling) {
//                        isCalling = false;
//                        isFinish = true;
//                        service.shutdown();
//                        Toast.makeText(context, "本次通话" + time + "秒",
//                                Toast.LENGTH_LONG).show();
//                        onCallFinish(phoneNumber, time);
//                        time = 0;
//                    }
//                    break;
//                case TelephonyManager.CALL_STATE_OFFHOOK:
//                    LogCat.e("电话处于通话中...");
//                    isCalling = true;
//                    service.execute(() -> {
//                        while (!isFinish) {
//                            try {
//                                Thread.sleep(1000);
//                                time++;
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    });
//                    break;
//                case TelephonyManager.CALL_STATE_RINGING:
//                    LogCat.e("电话处于响铃中...");
//                    isFinish = false;
//                    if (service.isShutdown()) {
//                        service = Executors.newSingleThreadExecutor();
//                    }
//                    break;
//            }
//        }
//    }

//    /**
//     * 通话结束
//     *
//     * @param second 通话秒数
//     */
//    private void onCallFinish(String phoneNumber, int second) {
//        if (!TextUtils.isEmpty(mCallPhoneNumber)) {
//            String talkTime = TimeUtil.getTime(second);
//            String dialPersonId = SharedPreferencesUtils.getUserId();
//            mCustomerControllerView.onCallPhoneFinish(ParamHelper.Customer.savePhoneRecord(mPersonalId, mHouseId, dialPersonId,
//                    "", talkTime, phoneNumber));
//            mCallPhoneNumber = null;
//            mPersonalId = null;
//            mHouseId = null;
//        }
//    }
//
//    public void deDeath() {
//        mDisposable.dispose();
//        mTelephonyManager.listen(null, PhoneStateListener.LISTEN_NONE);
//    }
}
