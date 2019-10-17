package com.holike.crm.presenter.activity;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.adapter.CustomerListAdapter;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.AttBean;
import com.holike.crm.bean.CustomerListBean;
import com.holike.crm.bean.MultiItem;
import com.holike.crm.bean.NoMoreBean;
import com.holike.crm.bean.TypeIdBean;
import com.holike.crm.model.activity.CustomerManageModel;
import com.holike.crm.popupwindown.FilterPopupWindow;
import com.holike.crm.util.KeyBoardUtil;
import com.holike.crm.util.ParseUtils;
import com.holike.crm.view.activity.CustomerManageView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * Created by wqj on 2018/2/25.
 * 客户管理
 */
@Deprecated
public class CustomerManagePresenter extends BasePresenter<CustomerManageView, CustomerManageModel> {
    private List<MultiItem> mBeans = new ArrayList<>();
    private NoMoreBean noMoreBean = new NoMoreBean();
    private CustomerAdapter adapter;
    private CustomerListAdapter mCustomerAdapter;

    private static class CustomerAdapter extends CommonAdapter<MultiItem> {

        CustomerAdapter(Context context, List<MultiItem> mDatas) {
            super(context, mDatas);
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, MultiItem multiItem, int position) {
            int itemType = holder.getItemViewType();
            if (itemType == 1) {
                CustomerListBean bean = (CustomerListBean) multiItem;
                TextView tvCreate = holder.obtainView(R.id.tv_item_rv_customer_manage_create);
                TextView tvUpdate = holder.obtainView(R.id.tv_item_rv_customer_manage_update);
                TextView tvName = holder.obtainView(R.id.tv_item_rv_customer_manage_name);
                TextView tvPhone = holder.obtainView(R.id.tv_item_rv_customer_manage_phone);
                TextView tvAddress = holder.obtainView(R.id.tv_item_rv_customer_manage_address);
                TextView tvDeposit = holder.obtainView(R.id.tv_item_rv_customer_manage_deposit);
                TextView tvSource = holder.obtainView(R.id.tv_item_rv_customer_manage_source);
                TextView tvState = holder.obtainView(R.id.tv_item_rv_customer_manage_state);
                tvCreate.setText(bean.getCreateDate());
                tvUpdate.setText(bean.getUpdateTime());
                tvAddress.setText(bean.getAddress());
                tvName.setText(bean.getUserName());
                tvPhone.setText(bean.getPhoneNumber());
                tvSource.setText(bean.getSourceName());
                tvState.setText(bean.getStatusMoveName());
                tvState.setTextColor(ContextCompat.getColor(mContext, R.color.textColor8));
                tvDeposit.setText((TextUtils.isEmpty(bean.getAntecedentPrice()) || bean.getAntecedentPrice().equals("0")) ? "无" : MyFragment.textEmptyNumber(bean.getAntecedentPrice()));
                if (TextUtils.isEmpty(bean.getShopId())) {
                    tvState.setText(mContext.getString(R.string.receive_deposit_click_assign_shop));
                    tvState.setTextColor(ContextCompat.getColor(mContext, R.color.textColor14));
                }
                holder.bindChildClick(R.id.tv_item_rv_customer_manage_phone);
                holder.bindChildLongClick(R.id.tv_item_rv_customer_manage_phone);
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
            if (viewType == 1) {
                return R.layout.item_rv_customer_manage;
            }
            return R.layout.item_nomore_data;
        }
    }

    public void setAdapter(RecyclerView recyclerView) {
        adapter = new CustomerAdapter(recyclerView.getContext(), mBeans);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemChildClickListener((adapter, holder, view, position) -> {
            if (getItem(position) != null) {
                if (getView() != null)
                    getView().adapterChildItemClick(getItem(position));
            }
        });
        adapter.setOnItemChildLongClickListener((adapter, holder, view, position) -> {
            if (getItem(position) != null) {
                if (getView() != null)
                    getView().adapterChildItemLongClick(getItem(position));
            }
        });
        adapter.setOnItemClickListener((adapter, holder, view, position) -> {
            if (getItem(position) != null) {
                if (getView() != null)
                    getView().adapterItemClick(getItem(position));
            }
        });
        adapter.setOnItemLongClickListener((adapter, holder, view, position) -> {
            if (getItem(position) != null) {
                if (getView() != null)
                    getView().adapterItemLongClick(getItem(position), position);
            }
        });
    }

    public void setCustomerAdapter(RecyclerView recyclerView) {
        mCustomerAdapter = new CustomerListAdapter(recyclerView.getContext(), mBeans);
        recyclerView.setAdapter(mCustomerAdapter);
        mCustomerAdapter.setOnItemChildClickListener((adapter, holder, view, position) -> {
            if (getItem(position) != null) {
                if (getView() != null)
                    getView().adapterChildItemClick(getItem(position));
            }
        });
        mCustomerAdapter.setOnItemChildLongClickListener((adapter, holder, view, position) -> {
            if (getItem(position) != null) {
                if (getView() != null)
                    getView().adapterChildItemLongClick(getItem(position));
            }
        });
        mCustomerAdapter.setOnItemClickListener((adapter, holder, view, position) -> {
            if (getItem(position) != null) {
                if (getView() != null)
                    getView().adapterItemClick(getItem(position));
            }
        });
        mCustomerAdapter.setOnItemLongClickListener((adapter, holder, view, position) -> {
            if (getItem(position) != null) {
                if (getView() != null)
                    getView().adapterItemLongClick(getItem(position), position);
            }
        });
    }

    private CustomerListBean getItem(int position) {
        if (position >= 0 && position < mBeans.size()) {
            if (mBeans.get(position) instanceof CustomerListBean) {
                return (CustomerListBean) mBeans.get(position);
            }
        }
        return null;
    }

    public void setTotalRows(String totalRows) {
        //客户总数
        int totalRows1;
        try {
            totalRows1 = ParseUtils.parseInt(totalRows);
        } catch (Exception e) {
            totalRows1 = 0;
        }
        if (mCustomerAdapter != null) {
            mCustomerAdapter.setTotalRows(totalRows1);
        }
    }

    public void onRefreshCompleted(List<CustomerListBean> customerListBeans) {
        this.mBeans.clear();
        onLoadMoreCompleted(customerListBeans);
    }

    public void onLoadMoreCompleted(List<CustomerListBean> customerListBeans) {
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

    public void removeCustomer(int position) {
        this.mBeans.remove(position);
        notifyItemRemoved(position);
        if (this.mBeans.contains(noMoreBean) && this.mBeans.size() == 1) {
            this.mBeans.clear();
            notifyDataSetChanged();
            if (getView() != null) getView().refreshSuccess(new ArrayList<>());
        }
    }

    public void clearData() {
        mBeans.clear();
        notifyDataSetChanged();
    }

    private void notifyItemRemoved(int position) {
        if (adapter != null) {
            adapter.notifyItemRemoved(position);
        } else if (mCustomerAdapter != null) {
            mCustomerAdapter.notifyItemRemoved(position);
        }
    }

    private void notifyDataSetChanged() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        } else if (mCustomerAdapter != null) {
            mCustomerAdapter.notifyDataSetChanged();
        }
    }

    public void getCustomerList(String searchContent, String source, String followId,
                                Date startDate, Date endDate,
                                int pageNo, int pageSize) {
        if (TextUtils.isEmpty(searchContent)) searchContent = "";
        if (TextUtils.isEmpty(source)) source = "";
        if (TextUtils.isEmpty(followId)) followId = "";
        model.getCustomerList(searchContent, source, followId, String.valueOf(pageNo), String.valueOf(pageSize), startDate, endDate, new CustomerManageModel.GetCustomerListListener() {
            @Override
            public void success(List<CustomerListBean> list, @Nullable AttBean attBean) {
                if (getView() != null)
                    getView().getCustomerListSuccess(list, attBean);
            }

            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().getCustomerListFailed(failed);
            }
        });
    }

    /**
     * 获取客户选择条件数据
     */
    public void getTypeId() {
        model.getTypeId(new CustomerManageModel.GetTypeIdListener() {
            @Override
            public void success(TypeIdBean bean) {
                if (getView() != null)
                    getView().getTypeIdSuccess(bean);
            }

            @Override
            public void failed(String failed) {
                if (getView() != null) {
                    getView().getTypeIdFailure(failed);
                }
            }
        });
    }

    public void deleteCustomer(String houseId, final int position) {
        model.deleteCustomer(houseId, new CustomerManageModel.DeleteCustomerCallback() {
            @Override
            public void deleteSuccess(String result) {
                if (getView() != null) getView().deleteCustomerSuccess(result, position);
            }

            @Override
            public void deleteFailure(String message) {
                if (getView() != null) getView().deleteCustomerFailure(message);
            }
        });
    }

    public void showPopupWindow(Context mContext, View mContentView, View parent, View arrowView, Map<String, String> data, String selectId, int type) {
        long delayMillis = 0L;
        if (KeyBoardUtil.isKeyboardShown(mContentView)) {
            KeyBoardUtil.hideKeyboard(mContentView);
            delayMillis = 200L;
        }
        parent.postDelayed(() -> {
            FilterPopupWindow filterPopupWindow = new FilterPopupWindow(mContext).
                    setData(data, selectId, arrowView).setSelectListener(new FilterPopupWindow.SelectListener() {
                @Override
                public void select(String selectId, String selectValue) {
                    if (getView() != null) {
                        if (type == 1) {
                            getView().getCustomerListBySource(selectId, selectValue);
                        } else {
                            getView().getCustomerListByType(selectId, selectValue);
                        }
                    }
                }

                @Override
                public void onDismiss() {

                }
            });
            filterPopupWindow.showAsDropDown(parent);
        }, delayMillis);
    }
}
