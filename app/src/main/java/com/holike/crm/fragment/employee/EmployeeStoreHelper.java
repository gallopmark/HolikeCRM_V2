package com.holike.crm.fragment.employee;

import android.content.Context;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.base.IntentValue;
import com.holike.crm.bean.DistributionStoreBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pony on 2019/8/6.
 * Copyright holike possess 2019.
 * 新增员工或编辑员工 门店选择帮助类
 */
class EmployeeStoreHelper {
    private Context mContext;
    private boolean mIsBoss; //是否是老板（老板不能编辑门店信息）
    private StoreAdapter mAdapter;
    private List<DistributionStoreBean> mOldSelectedShops; //起始被选中的门店集合（从员工详情点击进来）

    private List<DistributionStoreBean.Group> mOldSelectedGroups;  //起始被选中门店下的组织

    private List<DistributionStoreBean> mSelectedShop;  //已选择的门店集合
    private List<DistributionStoreBean.Group> mSelectedGroup; //已选的组织集合

//    private Map<Integer, List<DistributionStoreBean.Group>> mCacheMap;

    //    @SuppressLint("UseSparseArrays")
    EmployeeStoreHelper(Context context, @Nullable List<DistributionStoreBean> list, boolean isBoss) {
        this.mContext = context;
        this.mIsBoss = isBoss;
        List<DistributionStoreBean> items;
        if (list != null) {
            items = new ArrayList<>(list);
        } else {
            items = new ArrayList<>(IntentValue.getInstance().getShopData());  //新增员工
            for (DistributionStoreBean bean : items) {
                if (bean.isSelected()) {
                    bean.setIsSelect(0);
                }
            }
        }
//        mCacheMap = new HashMap<>();
        List<DistributionStoreBean> selectShops = new ArrayList<>();
        List<DistributionStoreBean.Group> selectGroups = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            DistributionStoreBean bean = items.get(i);
            if (bean.isSelected()) {
                selectShops.add(bean);
            }
            for (DistributionStoreBean.Group group : bean.getGroupList()) {
                if (group.isSelected()) {
                    selectGroups.add(group);
                }
            }
//            mCacheMap.put(i, innerList);
        }
        mOldSelectedShops = new ArrayList<>(selectShops);
        mOldSelectedGroups = new ArrayList<>(selectGroups);
        mSelectedShop = new ArrayList<>(selectShops);
        mSelectedGroup = new ArrayList<>(selectGroups);
        mAdapter = new StoreAdapter(mContext, items);
    }

    void attach(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(mAdapter);
    }

    /*选择的门店与刚进入页面已选的门店集合是否发生了变化*/
    private boolean isShopChanged() {
        if (mSelectedShop.size() != mOldSelectedShops.size()) {  //两者size不一样 则视为发生了改变
            return true;
        }
        return !mSelectedShop.containsAll(mOldSelectedShops);  //已选的门店 是否包含所有刚进入页面的已选门店数据
    }

    /*选择的组织是否发生了改变*/
    private boolean isGroupChanged() {
//        for (Map.Entry<Integer, List<DistributionStoreBean.Group>> entry : mCacheMap.entrySet()) {
//            List<DistributionStoreBean.Group> groupList = entry.getValue();
//            if (groupList != null && !groupList.isEmpty()) {
//                for (int j = 0; j < groupList.size(); j++) {
//                    if (groupList.get(j).isSelected()) {
//                        selectGroups.add(groupList.get(j));
//                    }
//                }
//            }
//        }
        if (mSelectedGroup.size() != mOldSelectedGroups.size()) {
            return true;
        }
        return !mSelectedGroup.containsAll(mOldSelectedGroups);
    }

    boolean isDataChanged() {
        return isShopChanged() || isGroupChanged();
    }

    /*是否选择了门店*/
    boolean isSelected() {
        return !mSelectedShop.isEmpty();
    }

    /*获取所选的门店id*/
    String getShopIds() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mSelectedShop.size(); i++) {
            sb.append(mSelectedShop.get(i).shopId);
            if (i < mSelectedShop.size() - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    /*子门店id*/
    String getGroupIds() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mSelectedGroup.size(); i++) {
            sb.append(mSelectedGroup.get(i).groupId);
            if (i < mSelectedGroup.size() - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    /*门店列表适配器*/
    class StoreAdapter extends CommonAdapter<DistributionStoreBean> {

        StoreAdapter(Context context, List<DistributionStoreBean> mDatas) {
            super(context, mDatas);
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_employee_store_parent;
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, final DistributionStoreBean bean, int position) {
            holder.setText(R.id.tv_store, bean.getShowText());
            if (mSelectedShop.contains(bean)) {
                setCheckedItem(holder);
            } else {
                setUncheckedItem(holder);
            }
            holder.itemView.setEnabled(!mIsBoss);  //不是老板本人可以编辑
            holder.itemView.setOnClickListener(view -> {
                if (mSelectedShop.contains(bean)) { //取消选择门店，则清除该门店所选的所有组
                    mSelectedShop.remove(bean);
                    for (DistributionStoreBean.Group group : bean.getGroupList()) {
                        mSelectedGroup.remove(group);
                    }
                } else {    //勾选门店，默认组织全选
                    mSelectedShop.add(bean);
                    for (DistributionStoreBean.Group group : bean.getGroupList()) {
                        if (!mSelectedGroup.contains(group)) {
                            mSelectedGroup.add(group);
                        }
                    }
                }
                notifyDataSetChanged();
            });
            if (position == getItemCount() - 1) {
                if (bean.getGroupList().isEmpty()) {
                    holder.setVisibility(R.id.v_divider1, View.GONE);
                } else {
                    holder.setVisibility(R.id.v_divider1, View.VISIBLE);
                }
            } else {
                holder.setVisibility(R.id.v_divider1, View.VISIBLE);
            }
            if (bean.getGroupList().isEmpty()) {
                holder.setVisibility(R.id.ll_child_layout, View.GONE);
            } else {
                holder.setVisibility(R.id.ll_child_layout, View.VISIBLE);
                RecyclerView recyclerView = holder.obtainView(R.id.rv_child_list);
                recyclerView.setNestedScrollingEnabled(false);
                GroupAdapter adapter = new GroupAdapter(mContext, bean.getGroupList(), position);
                recyclerView.setAdapter(adapter);
                if (position == getItemCount() - 1) {
                    holder.setVisibility(R.id.v_divider2, View.GONE);
                } else {
                    holder.setVisibility(R.id.v_divider2, View.VISIBLE);
                }
            }
        }

        private void setCheckedItem(RecyclerHolder holder) {
            holder.setTextColorRes(R.id.tv_store, R.color.textColor4);
            holder.setImageResource(R.id.iv_select, R.drawable.cus_scale_space_sel);
        }

        private void setUncheckedItem(RecyclerHolder holder) {
            holder.setTextColorRes(R.id.tv_store, R.color.textColor6);
            holder.setImageResource(R.id.iv_select, R.drawable.cus_scale_space_nor);
        }

        private void onGroupSelected(int position, List<DistributionStoreBean.Group> innerSelectedGroup) {
            if (position >= 0 && position < mDatas.size()) {
                DistributionStoreBean bean = mDatas.get(position);
                if (innerSelectedGroup.isEmpty()) {     //没有选择任何组织，则取消门店选择
                    mSelectedShop.remove(bean);
                } else { //选择了至少一个组织，则默认勾选门店
                    if (!mSelectedShop.contains(bean)) {
                        mSelectedShop.add(bean);
                    }
                }
                notifyDataSetChanged();
            }
        }

        /*门店下的分组列表适配器*/
        class GroupAdapter extends CommonAdapter<DistributionStoreBean.Group> {
            int mParentPosition;
            List<DistributionStoreBean.Group> mInnerSelectedGroup;  //已选的分组

            GroupAdapter(Context context, List<DistributionStoreBean.Group> mDatas, int parentPosition) {
                super(context, mDatas);
                this.mParentPosition = parentPosition;
                mInnerSelectedGroup = new ArrayList<>();
                /*将已被选择的组织添加到集合中*/
                for (DistributionStoreBean.Group group : mDatas) {
                    if (mSelectedGroup.contains(group)) {
                        mInnerSelectedGroup.add(group);
                    }
                }
            }

            @Override
            protected int bindView(int viewType) {
                return R.layout.item_employee_store_child;
            }

            @Override
            public void onBindHolder(final RecyclerHolder holder, DistributionStoreBean.Group group, int position) {
                holder.setText(R.id.tv_store, group.groupName);
                if (mSelectedGroup.contains(group)) {
                    setCheckedItem(holder);
                } else {
                    setUncheckedItem(holder);
                }
                holder.itemView.setEnabled(!mIsBoss);
                holder.itemView.setOnClickListener(view -> {
                    if (mSelectedGroup.contains(group)) {
                        mSelectedGroup.remove(group);
                        mInnerSelectedGroup.remove(group);
                        setUncheckedItem(holder);
                    } else {
                        mSelectedGroup.add(group);
                        mInnerSelectedGroup.add(group);
                        setCheckedItem(holder);
                    }
                    onGroupSelected(mParentPosition, mInnerSelectedGroup);
                });
            }

            private void setCheckedItem(RecyclerHolder holder) {
                holder.setTextColorRes(R.id.tv_store, R.color.textColor4);
                holder.setImageResource(R.id.iv_select, R.drawable.cus_scale_space_sel);
            }

            private void setUncheckedItem(RecyclerHolder holder) {
                holder.setTextColorRes(R.id.tv_store, R.color.textColor6);
                holder.setImageResource(R.id.iv_select, R.drawable.cus_scale_space_nor);
            }
        }
    }
}
