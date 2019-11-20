package com.holike.crm.dialog;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.base.IntentValue;
import com.holike.crm.bean.CurrentUserBean;
import com.holike.crm.bean.ErrorInfoBean;
import com.holike.crm.bean.ShopGroupBean;
import com.holike.crm.util.KeyBoardUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pony 2019/7/8
 * Copyright (c) 2019 holike
 * 重新分配客户
 */
public class CustomerRedistributionDialog extends ICustomerToolDialog {

    private OnConfirmListener mOnConfirmListener;
    private ErrorInfoBean mErrorInfoBean;
//    private EditText mReasonEditText;
    private TextView mShopTextView;
    private ProgressBar mProgressBar;
    private String mShopId, mGroupId;
    private LinearLayout mGroupLayout;
    private TextView mGroupTextView;
    private TextView mConfirmTextView;

    public CustomerRedistributionDialog(@NonNull Context context, ErrorInfoBean bean) {
        super(context);
        this.mErrorInfoBean = bean;
        init(context);
    }

    @Override
    protected int bindContentView() {
        return R.layout.dialog_customer_edit;
    }

    public CustomerRedistributionDialog setOnConfirmListener(OnConfirmListener listener) {
        this.mOnConfirmListener = listener;
        return this;
    }

    private void init(final Context context) {
        final LinearLayout llPrevious = mContentView.findViewById(R.id.llPrevious);
        TextView tvStatus = mContentView.findViewById(R.id.tvStatus);
        tvStatus.setText(obtain(R.string.dialog_tips_customer_edit_status, mErrorInfoBean.getStatus()));
        TextView tvTime = mContentView.findViewById(R.id.tvTime);
        tvTime.setText(obtain(R.string.dialog_tips_customer_edit_time, mErrorInfoBean.getStatusTime()));
        TextView tvDeposit = mContentView.findViewById(R.id.tvDeposit);
        String isOrNot = mErrorInfoBean.isChargeDeposit() ? mContext.getString(R.string.yes) : mContext.getString(R.string.no);
        tvDeposit.setText(obtain(R.string.dialog_tips_customer_edit_deposit, isOrNot));
        TextView tvNo = mContentView.findViewById(R.id.tvNo);
        TextView tvYes = mContentView.findViewById(R.id.tvYes);
        final LinearLayout llAfter = mContentView.findViewById(R.id.llAfter);
        ImageView ivBack = mContentView.findViewById(R.id.ivBack);
        final EditText reasonEditText = mContentView.findViewById(R.id.et_reason);
        TextView tvCancel = mContentView.findViewById(R.id.tv_cancel);
        mConfirmTextView = mContentView.findViewById(R.id.tv_confirm);
        mShopTextView = mContentView.findViewById(R.id.tv_shop);
        mProgressBar = mContentView.findViewById(R.id.pb_shop);
        mGroupLayout = mContentView.findViewById(R.id.ll_group_layout);
        mGroupTextView = mContentView.findViewById(R.id.tv_group);
        mShopTextView.setOnClickListener(view -> {
            CurrentUserBean bean = IntentValue.getInstance().getCurrentUser();
            if (bean == null) {
                getUserInfo();
            } else {
                onSelectShop(bean);
            }
        });
        mGroupTextView.setOnClickListener(view -> {
            KeyBoardUtil.hideKeyboard(reasonEditText);
            View v = mContentView.findViewById(R.id.rv_group);
            if (v.getVisibility() != View.VISIBLE) {
                onSelectGroup();
            } else {
                v.setVisibility(View.GONE);
            }
        });
        tvNo.setOnClickListener(view -> dismiss());
        tvYes.setOnClickListener(view -> {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.push_left_in);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    llPrevious.setVisibility(View.INVISIBLE);
                    llAfter.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            llAfter.startAnimation(animation);
        });
        ivBack.setOnClickListener(view -> {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.push_right_out);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    KeyBoardUtil.hideKeyboard(reasonEditText);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    llAfter.setVisibility(View.INVISIBLE);
                    llPrevious.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    llAfter.setVisibility(View.INVISIBLE);
                    llPrevious.setVisibility(View.VISIBLE);
                }
            });
            llAfter.startAnimation(animation);
        });
        tvCancel.setOnClickListener(view -> dismiss());
        mShopTextView.setOnClickListener(view -> {
            KeyBoardUtil.hideKeyboard(reasonEditText);
            CurrentUserBean bean = IntentValue.getInstance().getCurrentUser();
            if (bean == null) {
                getUserInfo();
            } else {
                onSelectShop(bean);
            }
        });
        mConfirmTextView.setOnClickListener(view -> {
            if (mOnConfirmListener != null) {
                mOnConfirmListener.onConfirm(CustomerRedistributionDialog.this, reasonEditText.getText().toString(), mShopId, mGroupId);
            }
            dismiss();
        });
    }

    private SpannableString obtain(int resId, String content) {
        String origin = mContext.getString(resId);
        int start = origin.length();
        String source = origin + (TextUtils.isEmpty(content) ? "" : content);
        int end = source.length();
        SpannableString ss = new SpannableString(source);
        int flag = SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE;
        ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.textColor4)), start, end, flag);
        ss.setSpan(new StyleSpan(Typeface.BOLD), start, end, flag);
        return ss;
    }

    @Override
    void onQueryUserInfoSuccess(CurrentUserBean userBean) {
        onSelectShop(userBean);
    }

    @Override
    void onQueryStart(int type) {
        if (type == 1) {
            mShopTextView.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mGroupLayout.setVisibility(View.GONE);
        }
    }

    @Override
    void onQueryCompleted(int type) {
        if (type == 1) {
            mShopTextView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
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
                    resetGroup();
                    getShopGroup(mShopId);
                });
            }
        });
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
            setConfirmEnabled();
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
        if (mCurrentGroupList.size() > 6) {  //最多显示6条
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
                    setConfirmEnabled();
                    rvGroup.setVisibility(View.GONE);
                });
            }
        });
    }

    private void setConfirmEnabled() {
        mConfirmTextView.setEnabled(true);
    }

    public interface OnConfirmListener {
        void onConfirm(CustomerRedistributionDialog dialog, String reason, String shopId, String groupId);
    }
}
