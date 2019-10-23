package com.holike.crm.presenter.activity;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.bean.MultiItem;
import com.holike.crm.bean.NoMoreBean;
import com.holike.crm.bean.OrderListBean;
import com.holike.crm.bean.TypeListBean;
import com.holike.crm.http.MyJsonParser;
import com.holike.crm.model.activity.OrderCenterModel;
import com.holike.crm.popupwindown.FilterPopupWindow;
import com.holike.crm.util.KeyBoardUtil;
import com.holike.crm.view.activity.OrderCenterView;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * Created by wqj on 2018/2/25.
 * 订单中心
 */
@Deprecated
public class OrderCenterPresenter extends BasePresenter<OrderCenterView, OrderCenterModel> {
    private List<MultiItem> mBeans = new ArrayList<>();
    private NoMoreBean noMoreBean = new NoMoreBean();
    private OrderAdapter adapter;
    private FilterPopupWindow filterPopupWindow;

    private static class OrderAdapter extends CommonAdapter<MultiItem> {

        OrderAdapter(Context context, List<MultiItem> mDatas) {
            super(context, mDatas);
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, MultiItem multiItem, int position) {
            int viewType = holder.getItemViewType();
            if (viewType == 1) {
                OrderListBean bean = (OrderListBean) multiItem;
                TextView tvType = holder.obtainView(R.id.tv_item_rv_order_center_type);
                TextView tvState = holder.obtainView(R.id.tv_item_rv_order_center_state);
                TextView tvOrderNum = holder.obtainView(R.id.tv_item_rv_order_center_orderNum);
                TextView tvAddress = holder.obtainView(R.id.tv_item_rv_order_center_address);
                TextView tvPhone = holder.obtainView(R.id.tv_item_rv_order_center_phone);
                TextView tvCreateDate = holder.obtainView(R.id.tv_item_rv_order_center_create_date);
                TextView tvName = holder.obtainView(R.id.tv_item_rv_order_center_name);
                tvType.setText(bean.getOrderTypeName());
                tvState.setText(bean.getOrderStatusName());
                tvOrderNum.setText(bean.getOrderId());
                tvAddress.setText(bean.getVillage());
                tvPhone.setText(bean.getPhone());
                tvCreateDate.setText(bean.getCreateDate());
                tvName.setText(bean.getName());
                holder.bindChildClick(R.id.tv_item_rv_order_center_phone);
                holder.bindChildLongClick(R.id.tv_item_rv_order_center_phone);
                holder.bindChildLongClick(R.id.tv_item_rv_order_center_orderNum);
            } else {
                holder.setText(R.id.mNoMoreTextView, mContext.getString(R.string.list_no_more));
            }
        }

        @Override
        public int getItemViewType(int position) {
            return mDatas.get(position).getItemType();
        }

        @Override
        protected int bindView(int viewType) {
            if (viewType == 1) return R.layout.item_rv_order_center;
            return R.layout.item_nomore_data;
        }
    }

    public void setAdapter(RecyclerView recyclerView) {
        adapter = new OrderAdapter(recyclerView.getContext(), mBeans);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter, holder, view, position) -> {
            OrderListBean bean = getItem(position);
            if (bean != null && getView() != null) {
                getView().orderDetails(bean.getOrderId());
            }
        });
        adapter.setOnItemChildClickListener((adapter, holder, view, position) -> {
            if (getItem(position) != null) {
                if (getView() != null)
                    getView().adapterItemChildClick(getItem(position));
            }
        });
        adapter.setOnItemChildLongClickListener((adapter, holder, view, position) -> {
            if (getItem(position) != null) {
                if (getView() != null)
                    getView().adapterItemChildLongClick(view, getItem(position));
            }
        });
    }

    private OrderListBean getItem(int position) {
        if (position >= 0 && position < mBeans.size()) {
            if (mBeans.get(position) instanceof OrderListBean) {
                return (OrderListBean) mBeans.get(position);
            }
        }
        return null;
    }

    public void onRefreshCompleted(List<OrderListBean> customerListBeans) {
        this.mBeans.clear();
        onLoadMoreCompleted(customerListBeans);
    }

    public void onLoadMoreCompleted(List<OrderListBean> customerListBeans) {
        if (customerListBeans != null && !customerListBeans.isEmpty()) {
            this.mBeans.addAll(customerListBeans);
        }
        notifyDataSetChanged();
    }

    public void noMoreData() {
        mBeans.remove(noMoreBean);
        mBeans.add(noMoreBean);
        notifyDataSetChanged();
    }

    public void clearData() {
        mBeans.clear();
        notifyDataSetChanged();
    }

    private void notifyDataSetChanged() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 获取客户选择条件数据
     */
    public void getSelectData() {
        model.getTypeList(new OrderCenterModel.GetTypeListListener() {
            @Override
            public void success(TypeListBean result) {
                if (getView() != null)
                    getView().getTypeListSuccess(result);
            }

            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().getListFailed(failed);
            }
        });
    }

    /*
     * 搜索
     */
//    public void search(String content, String pageNo) {
//        model.getOrderList(content, "", "", pageNo, "10", new OrderCenterModel.GetOrderListListener() {
//            @Override
//            public void success(String result) {
//                if (getView() != null) {
//                    int isShow = MyJsonParser.getAsInt(result, "isShow");
//                    long count = MyJsonParser.getAsLong(result, "count");
//                    getView().getListSuccess(MyJsonParser.parseJsonToList(result, OrderListBean.class), isShow == 1, count);
//                }
//            }
//
//            @Override
//            public void failed(String failed) {
//                if (getView() != null)
//                    getView().getListFailed(failed);
//            }
//        });
//    }

    /**
     * 获取订单列表
     */
    public void getOrderList(String searchContent, String orderStatusId, String orderTypeId,
                             Date startDate, Date endDate, String pageNo, String pageSize) {
        if (TextUtils.isEmpty(searchContent)) searchContent = "";
        if (TextUtils.isEmpty(orderStatusId)) orderStatusId = "";
        if (TextUtils.isEmpty(orderTypeId)) orderTypeId = "";
        model.getOrderList(searchContent, orderStatusId, orderTypeId, startDate, endDate, pageNo, pageSize, new OrderCenterModel.GetOrderListListener() {
            @Override
            public void success(String result) {
                if (getView() != null) {
                    int isShow = MyJsonParser.getAsInt(result, "isShow");
                    long count = MyJsonParser.getAsLong(result, "count");
                    int isShow2 = MyJsonParser.getAsInt(result, "isShow2");
                    String money = MyJsonParser.getAsString(result, "money");
                    getView().getListSuccess(MyJsonParser.parseJsonToList(result, OrderListBean.class), isShow == 1, count, isShow2 == 1, money);
                }
            }

            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().getListFailed(failed);
            }
        });
    }

    /**
     * 选择订单类型
     */
    public void selectOrderType(final Context context, final List<TypeListBean.OrderTypeDataBean> list, final String selectId, final View ivIcon, final View dvFilter, final TextView titleView, View mContentView) {
        LinkedHashMap<String, String> datas = new LinkedHashMap<>();
        for (TypeListBean.OrderTypeDataBean bean : list) {
            datas.put(bean.getOrderTypeId(), bean.getOrderTypeName());
        }
        int delayed = 0;
        if (KeyBoardUtil.isKeyboardShown(mContentView)) {
            KeyBoardUtil.toggleSoftInput(mContentView);
            delayed = 200;
        }
        mContentView.postDelayed(() -> {
            if (filterPopupWindow == null) {
                filterPopupWindow = new FilterPopupWindow(context).setData(datas, selectId, ivIcon).setSelectListener(new FilterPopupWindow.SelectListener() {
                    @Override
                    public void select(String id, String value) {
                        if (getView() != null)
                            getView().getListByOrderType(id);
                        if (!TextUtils.isEmpty(id)) {
                            titleView.setText(value);
                        } else {
                            titleView.setText(context.getString(R.string.order_center_order_type));
                        }
                    }

                    @Override
                    public void onDismiss() {
                        filterPopupWindow = null;
                    }
                });
                filterPopupWindow.showAsDropDown(dvFilter);
            }
        }, delayed);


    }

    /**
     * 选择订单状态
     */
    public void selectOrderState(final Context context, final List<TypeListBean.OrderStatusDataBean> list, final String selectId, final View ivIcon, final View dvFilter, final TextView titleView, View mContentView) {
        final LinkedHashMap<String, String> datas = new LinkedHashMap<>();
        for (TypeListBean.OrderStatusDataBean bean : list) {
            datas.put(bean.getOrderStatusId(), bean.getOrderStatusName());
        }
        long delayed = 0;
        if (KeyBoardUtil.isKeyboardShown(mContentView)) {
            KeyBoardUtil.toggleSoftInput(mContentView);
            delayed = 200;
        }

        mContentView.postDelayed(() -> {
            if (filterPopupWindow == null) {
                filterPopupWindow = new FilterPopupWindow(context).setData(datas, selectId, ivIcon).setSelectListener(new FilterPopupWindow.SelectListener() {
                    @Override
                    public void select(String id, String value) {
                        if (getView() != null)
                            getView().getListByOrderState(id);
                        if (TextUtils.isEmpty(id)) {
                            titleView.setText(context.getString(R.string.order_center_order_state));
                        } else {
                            titleView.setText(value);
                        }
                    }

                    @Override
                    public void onDismiss() {
                        filterPopupWindow = null;
                    }
                });
                filterPopupWindow.showAsDropDown(dvFilter);
            }
        }, delayed);
    }
}
