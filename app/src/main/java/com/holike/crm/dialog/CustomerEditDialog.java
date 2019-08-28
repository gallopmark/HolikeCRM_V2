package com.holike.crm.dialog;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.base.IntentValue;
import com.holike.crm.base.SimpleTextWatcher;
import com.holike.crm.bean.CurrentUserBean;
import com.holike.crm.bean.ErrorInfoBean;
import com.holike.crm.bean.ShopGroupBean;
import com.holike.crm.customView.AppToastCompat;
import com.holike.crm.http.CustomerUrlPath;
import com.holike.crm.http.MyHttpClient;
import com.holike.crm.http.ParamHelper;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.util.KeyBoardUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by gallop 2019/7/8
 * Copyright (c) 2019 holike
 * 重新分配客户
 */
public class CustomerEditDialog extends CommonDialog {

    private OnConfirmListener mOnConfirmListener;
    private ErrorInfoBean mErrorInfoBean;
    private EditText mReasonEditText;
    private TextView mShopTextView;
    private ProgressBar mProgressBar;
    private String mShopId, mGroupId;
    private LinearLayout mGroupLayout;
    private TextView mGroupTextView;
    private TextView mConfirmTextView;
    private CompositeDisposable mDisposables;
    private List<ShopGroupBean> mCurrentGroupList; //当前门店下的组织

    public CustomerEditDialog(@NonNull Context context, ErrorInfoBean bean) {
        super(context);
        this.mErrorInfoBean = bean;
        mDisposables = new CompositeDisposable();
        init(context);
    }

    @Override
    protected int bindContentView() {
        return R.layout.dialog_customer_edit;
    }

    public CustomerEditDialog setOnConfirmListener(OnConfirmListener listener) {
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
        mReasonEditText = mContentView.findViewById(R.id.et_reason);
        TextView tvCancel = mContentView.findViewById(R.id.tv_cancel);
        mConfirmTextView = mContentView.findViewById(R.id.tv_confirm);
        mShopTextView = mContentView.findViewById(R.id.tv_shop);
        mProgressBar = mContentView.findViewById(R.id.pb_shop);
        mGroupLayout = mContentView.findViewById(R.id.ll_group_layout);
        mGroupTextView = mContentView.findViewById(R.id.tv_group);
        mReasonEditText.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int i, int i1, int i2) {
                onSelected();
            }
        });
        mShopTextView.setOnClickListener(view -> {
            CurrentUserBean bean = IntentValue.getInstance().getCurrentUser();
            if (bean == null) {
                getUserInfo();
            } else {
                onSelectShop(bean);
            }
        });
        mGroupTextView.setOnClickListener(view -> {
            hideKeyboard();
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
                    hideKeyboard();
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
            hideKeyboard();
            CurrentUserBean bean = IntentValue.getInstance().getCurrentUser();
            if (bean == null) {
                getUserInfo();
            } else {
                onSelectShop(bean);
            }
        });
        mConfirmTextView.setOnClickListener(view -> {
            if (mOnConfirmListener != null) {
                mOnConfirmListener.onConfirm(CustomerEditDialog.this, mReasonEditText.getText().toString(), mShopId, mGroupId);
            }
            dismiss();
        });
    }

    private void hideKeyboard() {
        KeyBoardUtil.hideKeyboard(mReasonEditText);
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

    private void getUserInfo() {
        mDisposables.add(MyHttpClient.getByTimeout(CustomerUrlPath.URL_GET_USER_INFO, 60, new RequestCallBack<CurrentUserBean>() {
            @Override
            public void onStart(Disposable d) {
                mShopTextView.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailed(String failReason) {
                showToast(failReason);
            }

            @Override
            public void onSuccess(CurrentUserBean bean) {
                onSelectShop(bean);
            }

            @Override
            public void onFinished() {
                mShopTextView.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
            }
        }));
    }

    private void onSelectShop(CurrentUserBean userBean) {
        final RecyclerView rvShop = mContentView.findViewById(R.id.rv_shop);
        rvShop.setVisibility(View.VISIBLE);
        rvShop.setLayoutManager(new LinearLayoutManager(getContext()));
        List<CurrentUserBean.ShopInfo> list = userBean.getShopInfo();
        LinearLayout.LayoutParams params;
        if (list.size() > 6) {  //最多显示6条
            Resources resources = mContext.getResources();
            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) ((resources.getDimensionPixelSize(R.dimen.dp_40) + resources.getDimensionPixelSize(R.dimen.dp_0_5)) * 6.5f));
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
                    getShopGroup();
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

    /*获取门店分组信息*/
    private void getShopGroup() {
        Map<String, String> params = new HashMap<>();
        params.put("shopId", ParamHelper.noNullWrap(mShopId));
        mDisposables.add(MyHttpClient.get(CustomerUrlPath.URL_GET_SHOP_GROUP, params, new RequestCallBack<List<ShopGroupBean>>() {

            @Override
            public void onStart(Disposable d) {
                mGroupLayout.setVisibility(View.GONE);
            }

            @Override
            public void onFailed(String failReason) {
                showToast(failReason);
            }

            @Override
            public void onSuccess(List<ShopGroupBean> result) {
                if (result == null || result.isEmpty()) { //如果没有门店组织
                    onSelected();
                } else {
                    mCurrentGroupList = new ArrayList<>(result);
                    mGroupLayout.setVisibility(View.VISIBLE);
                }
            }
        }));
    }

    /*选择组织*/
    private void onSelectGroup() {
        if (mCurrentGroupList == null || mCurrentGroupList.isEmpty()) return;
        RecyclerView rvGroup = mContentView.findViewById(R.id.rv_group);
        rvGroup.setVisibility(View.VISIBLE);
        rvGroup.setLayoutManager(new LinearLayoutManager(mContext));
        LinearLayout.LayoutParams params;
        if (mCurrentGroupList.size() > 6) {  //最多显示6条
            Resources resources = mContext.getResources();
            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) ((resources.getDimensionPixelSize(R.dimen.dp_40) + resources.getDimensionPixelSize(R.dimen.dp_0_5)) * 6.5f));
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

    private void showToast(String text) {
        AppToastCompat.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    private void onSelected() {
        if (!TextUtils.isEmpty(mReasonEditText.getText().toString().trim()) && !TextUtils.isEmpty(mShopId) && !TextUtils.isEmpty(mGroupId)) {
            mConfirmTextView.setEnabled(true);
        } else {
            mConfirmTextView.setEnabled(false);
        }
    }

    @Override
    public void onDetachedFromWindow() {
        mDisposables.dispose();
        super.onDetachedFromWindow();
    }

    @Override
    public int getWindowAnimations() {
        return R.style.Dialog_Anim;
    }

    @Nullable
    @Override
    public ViewGroup.MarginLayoutParams getLayoutParams() {
        return new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected boolean fullWidth() {
        return true;
    }

    @Override
    public int getGravity() {
        return Gravity.BOTTOM;
    }

    public interface OnConfirmListener {
        void onConfirm(CustomerEditDialog dialog, String reason, String shopId, String groupId);
    }
}
