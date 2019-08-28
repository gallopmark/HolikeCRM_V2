package com.holike.crm.presenter.fragment;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.bean.DistributionStoreBean;
import com.holike.crm.local.MainDataSource;
import com.holike.crm.model.fragment.EmployeeModel;
import com.holike.crm.view.fragment.StoreListView;

import java.util.ArrayList;
import java.util.List;

public class EmployeeStorePresenter extends BasePresenter<StoreListView, EmployeeModel> {

    private List<DistributionStoreBean> beans = new ArrayList<>();
    private StoreListAdapter adapter;

    public static class StoreListAdapter extends CommonAdapter<DistributionStoreBean> {

        private List<DistributionStoreBean> mSelectedBeans;
        private OnItemSelectedListener onItemSelectedListener;
        private boolean isBoss = false;

        void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
            this.onItemSelectedListener = onItemSelectedListener;
        }

        StoreListAdapter(Context context, List<DistributionStoreBean> mDatas) {
            super(context, mDatas);
            mSelectedBeans = new ArrayList<>();
        }

        public void addAll(List<DistributionStoreBean> beans, boolean isBoss) {
            mDatas.clear();
            mDatas.addAll(beans);
            mSelectedBeans.clear();
            for (DistributionStoreBean bean : beans) {
                if (bean.getIsSelect() == 1) {
                    mSelectedBeans.add(bean);
                }
            }
            this.isBoss = isBoss;
            notifyDataSetChanged();
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_checkable_storelist;
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, DistributionStoreBean bean, int position) {
            boolean isItemChecked = mSelectedBeans.contains(bean);
            setChecked(holder, isItemChecked);
            holder.setText(R.id.mStoreNameTv, bean.getShopName());
            holder.setText(R.id.mStatusTv, bean.getStatus());
            if (isItemChecked) {
                holder.setImageResource(R.id.mCheckImageView, R.drawable.cus_scale_space_sel);
            } else {
                holder.setImageResource(R.id.mCheckImageView, R.drawable.cus_scale_space_nor);
            }
            holder.itemView.setEnabled(!isBoss);  //经销商老板不能选门店
            holder.itemView.setOnClickListener(v -> {
                if (!mSelectedBeans.contains(bean)) {
                    mSelectedBeans.add(bean);
                    holder.setImageResource(R.id.mCheckImageView, R.drawable.cus_scale_space_sel);
                    setChecked(holder, true);
                } else {
                    mSelectedBeans.remove(bean);
                    holder.setImageResource(R.id.mCheckImageView, R.drawable.cus_scale_space_nor);
                    setChecked(holder, false);
                }
                if (onItemSelectedListener != null) {
                    onItemSelectedListener.onItemSelected(mSelectedBeans);
                }
            });
        }

        private void setChecked(RecyclerHolder holder, boolean isChecked) {
            if (isChecked) {
                holder.setTextColorRes(R.id.mStoreNameTv, R.color.textColor4);
            } else {
                holder.setTextColorRes(R.id.mStoreNameTv, R.color.textColor6);
            }
        }

        List<DistributionStoreBean> getSelectedBeans() {
            return mSelectedBeans;
        }

        public interface OnItemSelectedListener {
            void onItemSelected(List<DistributionStoreBean> mSelected);
        }
    }

    public void setAdapter(RecyclerView recyclerView, StoreListAdapter.OnItemSelectedListener onItemSelectedListener) {
        adapter = new StoreListAdapter(recyclerView.getContext(), beans);
        adapter.setOnItemSelectedListener(onItemSelectedListener);
        recyclerView.setAdapter(adapter);
    }

    public void addAndNotifyDataSetChanged(List<DistributionStoreBean> beans, boolean isBoss) {
        if (beans != null && !beans.isEmpty()) {
            adapter.addAll(beans, isBoss);
        }
    }

    public List<DistributionStoreBean> getSelectedBeans() {
        if (adapter == null) return new ArrayList<>();
        return adapter.getSelectedBeans();
    }

    public void getStoreList(Context context) {
        List<DistributionStoreBean> beans = MainDataSource.getShopData(context);
        if (beans != null) {
            if (getView() != null)
                getView().getStoreList(beans);
            return;
        }
        model.getStoreList(new EmployeeModel.OnGetStoreCallback() {
            @Override
            public void onGetStoreStart() {
                if (getView() != null) getView().onShowLoading();
            }

            @Override
            public void onGetStoreList(List<DistributionStoreBean> beans) {
                if (getView() != null) getView().getStoreList(beans);
            }

            @Override
            public void onGetStoreFalure(String message) {
                if (getView() != null) getView().getStoreFailure(message);
            }

            @Override
            public void onGetStoreComplete() {
                if (getView() != null) getView().onHideLoading();
            }
        });
    }
}
