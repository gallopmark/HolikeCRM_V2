package com.holike.crm.fragment.main;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.holike.crm.R;
import com.holike.crm.adapter.OrderListAdapter;
import com.holike.crm.bean.DictionaryBean;
import com.holike.crm.bean.OrderListBean;
import com.holike.crm.bean.TypeListBean;
import com.holike.crm.dialog.CalendarPickerDialog;
import com.holike.crm.helper.CalendarPickerHelper;
import com.holike.crm.helper.MultiItemListHelper;
import com.holike.crm.popupwindown.MultipleSelectPopupWindow;
import com.holike.crm.popupwindown.StringItemPopupWindow;
import com.holike.crm.util.KeyBoardUtil;
import com.umeng.analytics.MobclickAgent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by pony 2019/7/4
 * Copyright (c) 2019 holike
 */
public class OrderHelper extends MultiItemListHelper {
    public interface OrderControllerView {
        void getListByOrderType();

        void getListByOrderState();

        void onGetSelectData();

        void getOrderList(String orderStatusId, String orderTypeId,
                          Date startDate, Date endDate, String pageNo, String pageSize);

        void onGetOrderOk(boolean isEmpty, boolean isLoadAll);

        void loadMoreSuccess(boolean isLoadMoreEnabled);

        void orderDetails(String orderId);

        void onCallPhone(String phone);

        void onCopy(String content);
    }

    private String mTimeTemp;
    private OrderControllerView mHelperListener;

    private TypeListBean mTypeListBean;
    private String mOrderTypeId, mOrderStatusId = "";
    private Date mStartDate, mEndDate;
    private int mPressedType = 0;

    OrderHelper(Context context, OrderControllerView orderControllerView) {
        super(context, 1);
        mTimeTemp = context.getString(R.string.create_time_tips);
        this.mHelperListener = orderControllerView;
        this.mAdapter = new OrderListAdapter(this.mContext, this.mListBeans);
    }

    void setOrderListAdapter(RecyclerView rvOrder) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        rvOrder.setLayoutManager(layoutManager);
        rvOrder.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, holder, view, position) -> {
            OrderListBean bean = getItem(position);
            if (bean != null) {
                mHelperListener.orderDetails(bean.getOrderId());
            }
        });
        mAdapter.setOnItemChildClickListener((adapter, holder, view, position) -> {
            OrderListBean bean = getItem(position);
            if (bean != null && !TextUtils.isEmpty(bean.getPhone())) {
                mHelperListener.onCallPhone(bean.getPhone());
            }
        });
        mAdapter.setOnItemChildLongClickListener((adapter, holder, view, position) -> {
            OrderListBean bean = getItem(position);
            if (bean != null) {
                String content = "";
                if (view.getId() == R.id.tv_phone) {
                    content = bean.getPhone();
                } else if (view.getId() == R.id.tv_orderNumber) {
                    content = bean.getOrderId();
                }
                mHelperListener.onCopy(content);
            }
        });
    }

    private OrderListBean getItem(int position) {
        if (position >= 0 && position < mListBeans.size()) {
            if (mListBeans.get(position) instanceof OrderListBean) {
                return (OrderListBean) mListBeans.get(position);
            }
        }
        return null;
    }

    void onGetTypeListOk(TypeListBean typeListBean, View dvFilter, TextView tvOrderType, TextView tvState, View contentView) {
        this.mTypeListBean = typeListBean;
        if (mPressedType == 1) {
            selectOrderType(mContext, typeListBean.getOrderTypeData(), dvFilter, tvOrderType, contentView);
        } else if (mPressedType == 2) {
            selectOrderState(typeListBean.getOrderStatusData(), tvState, dvFilter, contentView);
        }
    }

    void onClickOrderType(View dvFilter, TextView tvOrderType, View mContentView) {
        if (mTypeListBean != null) {
            selectOrderType(mContext, mTypeListBean.getOrderTypeData(), dvFilter, tvOrderType, mContentView);
        } else {
            mPressedType = 1;
            mHelperListener.onGetSelectData();
        }
    }

    void onClickOrderStatus(View dvFilter, TextView tvState, View contentView) {
        if (mTypeListBean != null) {
            selectOrderState(mTypeListBean.getOrderStatusData(), tvState, dvFilter, contentView);
        } else {
            mPressedType = 2;
            mHelperListener.onGetSelectData();
        }
    }

    private int mSelectOrderTypeIndex;

    /**
     * 选择订单类型
     */
    private void selectOrderType(final Context context, final List<TypeListBean.OrderTypeDataBean> list, final View dvFilter,
                                 final TextView tvType, View contentView) {
        final List<DictionaryBean> myList = new ArrayList<>();
        myList.add(new DictionaryBean("", mContext.getString(R.string.order_all)));
        List<String> optionItems = new ArrayList<>();
        for (TypeListBean.OrderTypeDataBean bean : list) {
            myList.add(new DictionaryBean(bean.getOrderTypeId(), bean.getOrderTypeName()));
        }
        for (DictionaryBean bean : myList) {
            optionItems.add(bean.name);
        }
        long delayed = hideKeyBoard(contentView);
        contentView.postDelayed(() -> {
            setArrowUp(tvType);
            StringItemPopupWindow popupWindow = new StringItemPopupWindow(mContext, optionItems, mSelectOrderTypeIndex, mContext.getResources().getDimensionPixelSize(R.dimen.dp_50));
            popupWindow.setOnMenuItemClickListener((pw, position, content) -> {
                pw.dismiss();
                mSelectOrderTypeIndex = position;
                MobclickAgent.onEvent(mContext, "order_type");
                mOrderTypeId = myList.get(position).id;
                mHelperListener.getListByOrderType();
                if (!TextUtils.isEmpty(mOrderTypeId)) {
                    tvType.setText(myList.get(position).name);
                } else {
                    tvType.setText(context.getString(R.string.order_center_order_type));
                }
            });
            popupWindow.showAsDown(dvFilter);
            popupWindow.setOnDismissListener(() -> setArrowDown(tvType));
        }, delayed);
    }

    private long hideKeyBoard(View contentView) {
        long delayMillis = 0L;
        if (KeyBoardUtil.isKeyboardShown(contentView)) {
            KeyBoardUtil.hideKeyboard(contentView);
            delayMillis = 200L;
        }
        return delayMillis;
    }

    private void setArrowDown(TextView tv) {
        tv.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.choice_down), null);
    }

    private void setArrowUp(TextView tv) {
        tv.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.choice_up), null);
    }


    private List<MultipleSelectPopupWindow.Item> mSelectItems = new ArrayList<>();
    private boolean mFirstSelect = true;
    /**
     * 选择订单状态
     */
    private void selectOrderState(final List<TypeListBean.OrderStatusDataBean> list, final TextView tvState, final View dvFilter, View contentView) {
        MobclickAgent.onEvent(mContext, "order_state");
        long delayed = hideKeyBoard(contentView);
        List<MultipleSelectPopupWindow.Item> items = new ArrayList<>();
        for (TypeListBean.OrderStatusDataBean bean : list) {
            items.add(new MultipleSelectPopupWindow.Item(bean.getOrderStatusId(), bean.getOrderStatusName()));
        }
        contentView.postDelayed(() -> {
            setArrowUp(tvState);
            if (mFirstSelect) {
                mSelectItems.addAll(items);
                mFirstSelect = false;
            }
            MultipleSelectPopupWindow popupWindow = new MultipleSelectPopupWindow(mContext, items, mSelectItems, mContext.getResources().getDimensionPixelSize(R.dimen.dp_50));
            popupWindow.setMultipleChoiceListener(selectedItems -> {
                mSelectItems = selectedItems;
                if (mSelectItems.isEmpty()) {
                    mOrderStatusId = "-1";
                } else {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < mSelectItems.size(); i++) {
                        sb.append(mSelectItems.get(i).code);
                        if (i < mSelectItems.size() - 1) {
                            sb.append("@");
                        }
                    }
                    mOrderStatusId = sb.toString();
                }
                mHelperListener.getListByOrderState();
            });
            popupWindow.showAsDown(dvFilter);
            popupWindow.setOnDismissListener(() -> setArrowDown(tvState));
        }, delayed);
    }

    private List<Date> mSelectedDates;

    public void showCalendarDialog(Context context, final TextView timeTextView) {
        CalendarPickerHelper.showCalendarDialog(context, mSelectedDates, new CalendarPickerHelper.OnCalendarOperateListener() {
            @Override
            public void onShow() {
                timeTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_choice_date, 0, R.drawable.ic_choice_date_up, 0);
            }

            @Override
            public void onCalendarPicker(CalendarPickerDialog dialog, List<Date> selectedDates, Date start, Date end) {
                mSelectedDates = selectedDates;
                if (selectedDates.size() >= 1) {
                    mStartDate = start;
                    mEndDate = end;
                    String startSource, endSource;
                    try {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());
                        startSource = dateFormat.format(mStartDate);
                        endSource = dateFormat.format(mEndDate);
                    } catch (Exception e) {
                        startSource = "";
                        endSource = "";
                    }
                    String text = mTimeTemp + startSource + "-" + endSource;
                    timeTextView.setText(text);
                } else {
                    setDefaultTime(timeTextView);
                }
                dialog.dismiss();
                startFirstLoad();
            }

            @Override
            public void onDismiss() {
                timeTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_choice_date, 0, R.drawable.ic_choice_date_down, 0);
            }
        });
    }

    void setDefaultTime(TextView timeTextView) {
        mStartDate = null;
        mEndDate = null;
        String timeAll = mTimeTemp + mContext.getString(R.string.all);
        timeTextView.setText(timeAll);
    }

    public void resetParams(TextView tvOrderType, TextView tvOrderState) {
        onReset();
        this.mOrderTypeId = null;
        tvOrderType.setText(mContext.getString(R.string.order_center_order_type));
        this.mOrderStatusId = null;
        tvOrderState.setText(mContext.getString(R.string.order_center_order_state));
    }

    @Override
    public void onStartLoad(boolean isFirstLoading) {
        getOrderList();
    }

    public void getOrderList() {
        mHelperListener.getOrderList(mOrderStatusId, mOrderTypeId, mStartDate, mEndDate, String.valueOf(mPageNo), String.valueOf(mPageSize));
    }

    void onGetOrderListOk(List<OrderListBean> listBeans, boolean isShow, long count,
                          boolean isShow2, String money, final TextView tvAmount, final TextView tvCount) {
        onHttpResultOk(listBeans);
        if (isShow) {
            setOrderCount(tvCount, String.valueOf(count));
        } else {
            hideViews(tvCount);
        }
        if (isShow2) {
            setOrderAmount(tvAmount, money);
        } else {
            hideViews(tvAmount);
        }
    }

    @Override
    public void onRefreshSuccess(boolean isFirstLoad, boolean isEmpty, boolean isLoadAll) {
        mHelperListener.onGetOrderOk(isEmpty, isLoadAll);
    }

    @Override
    public void onLoadMoreSuccess(boolean isLoadMoreEnabled) {
        mHelperListener.loadMoreSuccess(isLoadMoreEnabled);
    }

    void hideViews(View... views) {
        for (View view : views) {
            view.setVisibility(View.GONE);
        }
    }

    private void setOrderAmount(TextView tvAmount, String money) {
        tvAmount.setVisibility(View.VISIBLE);
        Context context = tvAmount.getContext();
        String source = context.getString(R.string.order_amount_tips);
        int start = source.length();
        if (!TextUtils.isEmpty(money)) {
            source += money;
        }
        int end = source.length();
        SpannableString ss = new SpannableString(source);
        int flags = SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE;
        ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.bg_homepage_new)), start, end, flags);
        ss.setSpan(new StyleSpan(Typeface.BOLD), start, end, flags);
        tvAmount.setText(ss);
    }

    private void setOrderCount(TextView tvCount, String totalRows) {
        tvCount.setVisibility(View.VISIBLE);
        Context context = tvCount.getContext();
        String source = context.getString(R.string.order_count);
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
    }

}
