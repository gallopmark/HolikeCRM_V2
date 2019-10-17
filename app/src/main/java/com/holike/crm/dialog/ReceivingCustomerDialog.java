package com.holike.crm.dialog;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.base.IntentValue;
import com.holike.crm.bean.CurrentUserBean;
import com.holike.crm.bean.ShopGroupBean;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by gallop on 2019/8/26.
 * Copyright holike possess 2019.
 * 领取客户
 */
public class ReceivingCustomerDialog extends ICustomerToolDialog {

    private String mShopId, mGroupId;
    private TextView mShopTextView;
    private ProgressBar mShopProgressBar;
    private LinearLayout mGroupLayout;
    private TextView mGroupTextView;
    private TextView mConfirmTextView;

    public ReceivingCustomerDialog(Context context) {
        super(context);
        setup();
    }

    private void setup() {
        mShopTextView = mContentView.findViewById(R.id.tv_shop);
        mShopProgressBar = mContentView.findViewById(R.id.pb_shop);
        mGroupLayout = mContentView.findViewById(R.id.ll_group_layout);
        mGroupTextView = mContentView.findViewById(R.id.tv_group);
        mConfirmTextView = mContentView.findViewById(R.id.tv_confirm);
        mShopTextView.setOnClickListener(view -> {
            CurrentUserBean bean = IntentValue.getInstance().getCurrentUser();
            if (bean == null) {
                getUserInfo();
            } else {
                onSelectShop(bean);
            }
        });
        mGroupTextView.setOnClickListener(view -> {
            View v = mContentView.findViewById(R.id.rv_group);
            if (v.getVisibility() != View.VISIBLE) {
                onSelectGroup();
            } else {
                v.setVisibility(View.GONE);
            }
        });
        mConfirmTextView.setOnClickListener(view -> {
            if (mSelectedListener != null) {
                mSelectedListener.onSelected(mShopId, mGroupId);
            }
            dismiss();
        });
    }

    @Override
    void onQueryStart(int type) {
        if (type == 1) {
            mShopTextView.setVisibility(View.GONE);
            mShopProgressBar.setVisibility(View.VISIBLE);
        } else {
            mGroupLayout.setVisibility(View.GONE);
        }
    }

    @Override
    void onQueryUserInfoSuccess(CurrentUserBean userBean) {
        onSelectShop(userBean);
    }

    @Override
    void onQueryCompleted(int type) {
        if (type == 1) {
            mShopTextView.setVisibility(View.VISIBLE);
            mShopProgressBar.setVisibility(View.GONE);
        }
    }

    private void onSelectShop(CurrentUserBean userBean) {
        final RecyclerView rvShop = mContentView.findViewById(R.id.rv_shop);
        rvShop.setVisibility(View.VISIBLE);
        rvShop.setLayoutManager(new LinearLayoutManager(getContext()));
        List<CurrentUserBean.ShopInfo> list = userBean.getShopInfo();
        LinearLayout.LayoutParams params;
        if (list.size() > SHOW_ENTRY) {  //最多显示6条
            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MAX_HEIGHT);
        } else {
            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        rvShop.setLayoutParams(params);
        rvShop.setAdapter(new CommonAdapter<CurrentUserBean.ShopInfo>(getContext(), list) {
            @Override
            protected int bindView(int viewType) {
                return R.layout.dialog_item_distribution_shop;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, CurrentUserBean.ShopInfo bean, int position) {
                holder.setText(R.id.tv_name, bean.shopName);
                holder.itemView.setOnClickListener(view -> {
                    mShopId = bean.shopId;
                    mShopTextView.setText(bean.shopName);
                    mShopTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.textColor4));
                    rvShop.setVisibility(View.GONE);
                    resetLayout();
                    getShopGroup(mShopId);
                });
            }
        });
    }

    /*点击选择门店后重置*/
    private void resetLayout() {
        resetGroup();
    }

    private void resetGroup() {
        mGroupId = null;
        mCurrentGroupList = null;
        mGroupTextView.setText(null);
        mGroupLayout.setVisibility(View.GONE);
        mConfirmTextView.setEnabled(false);
    }

    @Override
    void onQueryShopGroupSuccess(List<ShopGroupBean> list) {
        if (list == null || list.isEmpty()) { //如果没有门店组织
            onSelected();
        } else {
            mCurrentGroupList = new ArrayList<>(list);
            mGroupLayout.setVisibility(View.VISIBLE);
        }
    }

    /*选择组织*/
    private void onSelectGroup() {
        if (mCurrentGroupList == null || mCurrentGroupList.isEmpty()) return;
        RecyclerView rvGroup = mContentView.findViewById(R.id.rv_group);
        rvGroup.setVisibility(View.VISIBLE);
        rvGroup.setLayoutManager(new LinearLayoutManager(mContext));
        LinearLayout.LayoutParams params;
        if (mCurrentGroupList.size() > SHOW_ENTRY) {  //最多显示5条
            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MAX_HEIGHT);
        } else {
            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        rvGroup.setLayoutParams(params);
        rvGroup.setAdapter(new CommonAdapter<ShopGroupBean>(getContext(), mCurrentGroupList) {
            @Override
            protected int bindView(int viewType) {
                return R.layout.dialog_item_distribution_shop;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, ShopGroupBean bean, int position) {
                holder.setText(R.id.tv_name, bean.groupName);
                holder.itemView.setOnClickListener(view -> {
                    mGroupId = bean.id;
                    mGroupTextView.setText(bean.groupName);
                    mGroupTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.textColor4));
                    onSelected();
                    rvGroup.setVisibility(View.GONE);
                });
            }
        });
    }

    private void onSelected() {
        mConfirmTextView.setEnabled(true);
    }

    @Override
    protected int bindContentView() {
        return R.layout.dialog_receiving_customer;
    }
}
