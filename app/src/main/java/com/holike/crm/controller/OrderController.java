package com.holike.crm.controller;

import android.content.Context;
import android.graphics.Typeface;
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
import com.holike.crm.adapter.OrderListAdapter;
import com.holike.crm.bean.OrderListBean;
import com.holike.crm.bean.TypeListBean;
import com.holike.crm.dialog.CalendarPickerDialog;
import com.holike.crm.helper.CalendarDialogHelper;
import com.holike.crm.popupwindown.FilterPopupWindow;
import com.holike.crm.popupwindown.MultipleSelectPopupWindow;
import com.holike.crm.util.KeyBoardUtil;
import com.umeng.analytics.MobclickAgent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by gallop 2019/7/4
 * Copyright (c) 2019 holike
 */
public class OrderController extends MultiItemListController {
    public interface OrderControllerView {
        void getListByOrderType();

        void getListByOrderState();

        void onDateSelected(List<Date> selectedDates, Date start, Date end);

        void onGetSelectData();

        void getOrderList(String orderStatusId, String orderTypeId,
                          Date startDate, Date endDate, String pageNo, String pageSize);

        void onGetOrderOk(boolean isEmpty, boolean isLoadAll);

        void loadMoreSuccess(boolean isLoadMoreEnabled);

        void orderDetails(String orderId);

        void onCallPhone(String phone);

        void onCopy(String content);
    }

    private OrderControllerView mHelperListener;

    private TypeListBean mTypeListBean;
    private String mOrderTypeId, mOrderStatusId;
    private Date mStartDate, mEndDate;
    private int mPressedType = 0;

    public OrderController(Context context, OrderControllerView orderControllerView) {
        super(context, 1);
        this.mHelperListener = orderControllerView;
        this.mAdapter = new OrderListAdapter(this.mContext, this.mListBeans);
    }

    public void setOrderListAdapter(RecyclerView rvOrder) {
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

    public void onGetTypeListOk(TypeListBean typeListBean, ImageView ivOrderType, ImageView ivOrderState, View dvFilter, TextView tvOrderType, View contentView) {
        this.mTypeListBean = typeListBean;
        if (mPressedType == 1) {
            selectOrderType(mContext, typeListBean.getOrderTypeData(), mOrderTypeId, ivOrderType, dvFilter, tvOrderType, contentView);
        } else if (mPressedType == 2) {
            selectOrderState(typeListBean.getOrderStatusData(), ivOrderState, dvFilter, contentView);
        }
    }

    public void onClickOrderType(ImageView ivOrderType, View dvFilter, TextView tvOrderType, View mContentView) {
        if (mTypeListBean != null) {
            selectOrderType(mContext, mTypeListBean.getOrderTypeData(), mOrderTypeId, ivOrderType, dvFilter, tvOrderType, mContentView);
        } else {
            mPressedType = 1;
            mHelperListener.onGetSelectData();
        }
    }

    public void onClickOrderStatus(ImageView ivOrderState, View dvFilter, View contentView) {
        if (mTypeListBean != null) {
            selectOrderState(mTypeListBean.getOrderStatusData(), ivOrderState, dvFilter, contentView);
        } else {
            mPressedType = 2;
            mHelperListener.onGetSelectData();
        }
    }

    /**
     * 选择订单类型
     */
    private void selectOrderType(final Context context, final List<TypeListBean.OrderTypeDataBean> list,
                                 final String selectId, final View ivIcon, final View dvFilter,
                                 final TextView titleView, View contentView) {
        LinkedHashMap<String, String> datas = new LinkedHashMap<>();
        for (TypeListBean.OrderTypeDataBean bean : list) {
            datas.put(bean.getOrderTypeId(), bean.getOrderTypeName());
        }
        long delayed = hideKeyBoard(contentView);
        contentView.postDelayed(() -> {
            FilterPopupWindow popupWindow = new FilterPopupWindow(context).setData(datas, selectId, ivIcon).setSelectListener(new FilterPopupWindow.SelectListener() {
                @Override
                public void select(String id, String value) {
                    MobclickAgent.onEvent(mContext, "order_type");
                    mOrderTypeId = id;
                    mHelperListener.getListByOrderType();
                    if (!TextUtils.isEmpty(id)) {
                        titleView.setText(value);
                    } else {
                        titleView.setText(context.getString(R.string.order_center_order_type));
                    }
                }

                @Override
                public void onDismiss() {
                }
            });
            popupWindow.showAsDropDown(dvFilter);
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

    /**
     * 选择订单状态
     */
    private List<MultipleSelectPopupWindow.Item> mSelectItems = new ArrayList<>();

    private void selectOrderState(final List<TypeListBean.OrderStatusDataBean> list, final View ivIcon, final View dvFilter, View contentView) {
        MobclickAgent.onEvent(mContext, "order_state");
        long delayed = hideKeyBoard(contentView);
        List<MultipleSelectPopupWindow.Item> items = new ArrayList<>();
        for (TypeListBean.OrderStatusDataBean bean : list) {
            items.add(new MultipleSelectPopupWindow.Item(bean.getOrderStatusId(), bean.getOrderStatusName()));
        }
        ivIcon.setRotationX(180);
        contentView.postDelayed(() -> {
            MultipleSelectPopupWindow popupWindow = new MultipleSelectPopupWindow(mContext, items, mSelectItems);
            popupWindow.setMultipleChoiceListener(selectedItems -> {
                mSelectItems = selectedItems;
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < mSelectItems.size(); i++) {
                    sb.append(mSelectItems.get(i).code);
                    if (i < mSelectItems.size() - 1) {
                        sb.append("@");
                    }
                }
                mOrderStatusId = sb.toString();
                mHelperListener.getListByOrderState();
            });
            popupWindow.showAsDown(dvFilter);
            popupWindow.setOnDismissListener(() -> ivIcon.setRotationX(0));
        }, delayed);
    }

    private List<Date> mSelectedDates;

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
                mHelperListener.onDateSelected(selectedDates, start, end);
            }

            @Override
            public void onDismiss() {
                ivArrow.setRotation(0);
            }
        });
    }

    private void clearTime(TextView timeTextView) {
        mStartDate = null;
        mEndDate = null;
        timeTextView.setText(mContext.getString(R.string.all));
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

    public void onGetOrderListOk(List<OrderListBean> listBeans, boolean isShow, long count,
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
    public void onRefreshSuccess(boolean isEmpty, boolean isLoadAll) {
        mHelperListener.onGetOrderOk(isEmpty, isLoadAll);
    }

    @Override
    public void onLoadMoreSuccess(boolean isLoadMoreEnabled) {
        mHelperListener.loadMoreSuccess(isLoadMoreEnabled);
    }

    public void hideViews(View... views) {
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
