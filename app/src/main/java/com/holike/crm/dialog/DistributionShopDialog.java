package com.holike.crm.dialog;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.base.IntentValue;
import com.holike.crm.bean.CurrentUserBean;
import com.holike.crm.bean.CustomerListBeanV2;
import com.holike.crm.bean.ShopGroupBean;
import com.holike.crm.bean.ShopRoleUserBean;
import com.holike.crm.customView.AppToastCompat;
import com.holike.crm.http.CustomerUrlPath;
import com.holike.crm.http.MyHttpClient;
import com.holike.crm.http.ParamHelper;
import com.holike.crm.http.RequestCallBack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by gallop on 2019/8/21.
 * Copyright holike possess 2019.
 * 分配门店dialog
 */
public class DistributionShopDialog extends CommonDialog {

    private CompositeDisposable mDisposables = new CompositeDisposable();
    private TextView mSelectShopTextView, mSelectGroupTextView, mSelectGuideTextView;
    private LinearLayout mGroupLayout, mGuideLayout;
    private ProgressBar mShopProgressBar, mGroupProgressBar, mGuideProgressBar;
    private TextView mConfirmTextView;
    private String mSelectShopId, mSelectGroupId, mSelectGuideId;
    private List<ShopGroupBean> mCurrentGroupList; //当前门店下的组织
    private List<ShopRoleUserBean.UserBean> mCurrentGuideList; //当前门店下的导购人员

    private OnConfirmListener mListener;

    public DistributionShopDialog(Context context, CustomerListBeanV2.CustomerBean bean) {
        super(context);
        setText(bean);
    }

    public void setOnConfirmListener(OnConfirmListener listener) {
        this.mListener = listener;
    }

    @Nullable
    @Override
    public ViewGroup.MarginLayoutParams getLayoutParams() {
        return new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public int getWindowAnimations() {
        return R.style.Dialog_Anim;
    }

    @Override
    protected boolean fullWidth() {
        return true;
    }

    @Override
    public int getGravity() {
        return Gravity.BOTTOM;
    }

    @Override
    protected int bindContentView() {
        return R.layout.dialog_distribution_shop;
    }

    private void setText(CustomerListBeanV2.CustomerBean bean) {
        TextView tvUserName = mContentView.findViewById(R.id.tv_userName);
        tvUserName.setText(bean.userName);
        TextView tvArea = mContentView.findViewById(R.id.tv_area);
        tvArea.setText(bean.detailsAddress);
        TextView tvAddress = mContentView.findViewById(R.id.tv_address);
        tvAddress.setText(bean.address);
        TextView tvEmptyRemark = mContentView.findViewById(R.id.tv_empty_remark);
        TextView tvRemark = mContentView.findViewById(R.id.tv_remark);
        if (TextUtils.isEmpty(bean.remark)) {
            tvEmptyRemark.setVisibility(View.VISIBLE);
            tvRemark.setVisibility(View.GONE);
        } else {
            tvEmptyRemark.setVisibility(View.GONE);
            tvRemark.setVisibility(View.VISIBLE);
            tvRemark.setText(bean.remark);
        }
        mSelectShopTextView = mContentView.findViewById(R.id.tv_select_shop);
        mShopProgressBar = mContentView.findViewById(R.id.pb_shop);
        mGroupLayout = mContentView.findViewById(R.id.ll_group_layout);
        mSelectGroupTextView = mContentView.findViewById(R.id.tv_select_group);
        mGroupProgressBar = mContentView.findViewById(R.id.pb_group);
        mGuideLayout = mContentView.findViewById(R.id.ll_guide_layout);
        mSelectGuideTextView = mContentView.findViewById(R.id.tv_select_guide);
        mGuideProgressBar = mContentView.findViewById(R.id.pb_guide);
        mSelectShopTextView.setOnClickListener(view -> {
            mContentView.findViewById(R.id.rv_group).setVisibility(View.GONE);
            mContentView.findViewById(R.id.rv_guide).setVisibility(View.GONE);
            CurrentUserBean currentUser = IntentValue.getInstance().getCurrentUser();
            if (currentUser == null) {
                getUserInfo();
            } else {
                onSelectShop(currentUser);
            }
        });
        mSelectGroupTextView.setOnClickListener(view -> {
            mContentView.findViewById(R.id.rv_shop).setVisibility(View.GONE);
            mContentView.findViewById(R.id.rv_guide).setVisibility(View.GONE);
            View v = mContentView.findViewById(R.id.rv_group);
            if (v.getVisibility() != View.VISIBLE) {
                if (mCurrentGroupList != null && !mCurrentGroupList.isEmpty()) {
                    onSelectGroup();
                }
            } else {
                v.setVisibility(View.GONE);
            }
        });
        mSelectGuideTextView.setOnClickListener(view -> {
            mContentView.findViewById(R.id.rv_shop).setVisibility(View.GONE);
            mContentView.findViewById(R.id.rv_group).setVisibility(View.GONE);
            View v = mContentView.findViewById(R.id.rv_guide);
            if (v.getVisibility() != View.VISIBLE) {
                if (mCurrentGuideList != null && !mCurrentGuideList.isEmpty()) {
                    onSelectGuide();
                }
            } else {
                v.setVisibility(View.GONE);
            }
        });
        mConfirmTextView = mContentView.findViewById(R.id.tv_confirm);
        mConfirmTextView.setOnClickListener(view -> {
            if (mListener != null) {
                mListener.onPicker(mSelectShopId, mSelectGroupId, mSelectGuideId);
            }
            dismiss();
        });
    }

    private void getUserInfo() {
        mDisposables.add(MyHttpClient.getByTimeout(CustomerUrlPath.URL_GET_USER_INFO, 60, new RequestCallBack<CurrentUserBean>() {
            @Override
            public void onStart(Disposable d) {
                mSelectShopTextView.setVisibility(View.GONE);
                mShopProgressBar.setVisibility(View.VISIBLE);
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
                mSelectShopTextView.setVisibility(View.VISIBLE);
                mShopProgressBar.setVisibility(View.GONE);
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
                    mSelectShopId = bean.shopId;
                    mSelectShopTextView.setText(bean.shopName);
                    mSelectShopTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.textColor4));
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
        resetGuide();
    }

    private void resetGroup() {
        mSelectGroupId = null;
        mCurrentGroupList = null;
        mSelectGroupTextView.setText(getContext().getString(R.string.tips_please_select));
        mSelectGroupTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.textColor6));
        mGroupLayout.setVisibility(View.GONE);
        mConfirmTextView.setEnabled(false);
    }

    private void resetGuide() {
        mSelectGuideId = null;
        mCurrentGuideList = null;
        mSelectGuideTextView.setText(getContext().getString(R.string.tips_please_select));
        mSelectGuideTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.textColor6));
        mGuideLayout.setVisibility(View.GONE);
    }

    /*获取门店分组信息*/
    private void getShopGroup() {
        Map<String, String> params = new HashMap<>();
        params.put("shopId", ParamHelper.noNullWrap(mSelectShopId));
        mDisposables.add(MyHttpClient.get(CustomerUrlPath.URL_GET_SHOP_GROUP, params, new RequestCallBack<List<ShopGroupBean>>() {

            @Override
            public void onStart(Disposable d) {
                mGroupProgressBar.setVisibility(View.VISIBLE);
                mGroupLayout.setVisibility(View.GONE);
            }

            @Override
            public void onFailed(String failReason) {
                showToast(failReason);
            }

            @Override
            public void onSuccess(List<ShopGroupBean> result) {
                if (result == null || result.isEmpty()) { //如果没有门店组织
                    getShopGuide();
                } else {
                    mCurrentGroupList = new ArrayList<>(result);
                    mGroupLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFinished() {
                mGroupProgressBar.setVisibility(View.GONE);
            }
        }));
    }

    /*选择组织*/
    private void onSelectGroup() {
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
                    mSelectGroupId = bean.id;
                    mSelectGroupTextView.setText(bean.groupName);
                    mSelectGroupTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.textColor4));
                    rvGroup.setVisibility(View.GONE);
                    resetGuide();
                    getGroupGuide();
                });
            }
        });
    }

    /*获取分组下的导购人员*/
    private void getGroupGuide() {
        Map<String, String> params = new HashMap<>();
        params.put("groupId", ParamHelper.noNullWrap(mSelectGroupId));
        MyHttpClient.get(CustomerUrlPath.URL_GET_GROUP_GUIDE, params, new RequestCallBack<List<ShopRoleUserBean.UserBean>>() {
            @Override
            public void onStart(Disposable d) {
                mGuideProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailed(String failReason) {
                showToast(failReason);
            }

            @Override
            public void onSuccess(List<ShopRoleUserBean.UserBean> result) {
                if (result != null && !result.isEmpty()) {
                    mCurrentGuideList = new ArrayList<>(result);
                    mGuideLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFinished() {
                mGuideProgressBar.setVisibility(View.GONE);
            }
        });
    }

    /*获取门店导购*/
    private void getShopGuide() {
        Map<String, String> params = new HashMap<>();
        params.put("shopId", ParamHelper.noNullWrap(mSelectShopId));
        MyHttpClient.get(CustomerUrlPath.URL_GET_SHOP_GUIDE, params, new RequestCallBack<List<ShopRoleUserBean.UserBean>>() {
            @Override
            public void onStart(Disposable d) {
                mGuideProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailed(String failReason) {
                showToast(failReason);
            }

            @Override
            public void onSuccess(List<ShopRoleUserBean.UserBean> result) {
                if (result != null && !result.isEmpty()) {
                    mCurrentGuideList = new ArrayList<>(result);
                    mGuideLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFinished() {
                mGuideProgressBar.setVisibility(View.GONE);
            }
        });
    }

    /*选择导购*/
    private void onSelectGuide() {
        final RecyclerView rvGuide = mContentView.findViewById(R.id.rv_guide);
        rvGuide.setVisibility(View.VISIBLE);
        rvGuide.setLayoutManager(new LinearLayoutManager(mContext));
        LinearLayout.LayoutParams params;
        if (mCurrentGuideList.size() > 6) {  //最多显示6条
            Resources resources = mContext.getResources();
            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) ((resources.getDimensionPixelSize(R.dimen.dp_40) + resources.getDimensionPixelSize(R.dimen.dp_0_5)) * 6.5f));
        } else {
            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        rvGuide.setLayoutParams(params);
        rvGuide.setAdapter(new CommonAdapter<ShopRoleUserBean.UserBean>(getContext(), mCurrentGuideList) {
            @Override
            protected int bindView(int viewType) {
                return R.layout.dialog_item_distribution_shop;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, ShopRoleUserBean.UserBean bean, int position) {
                holder.setText(R.id.tv_name, bean.userName);
                holder.itemView.setOnClickListener(view -> {
                    mSelectGuideId = bean.userId;
                    mSelectGuideTextView.setText(bean.userName);
                    mSelectGuideTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.textColor4));
                    rvGuide.setVisibility(View.GONE);
                    mConfirmTextView.setEnabled(true);
                });
            }
        });
    }

    private void showToast(String text) {
        AppToastCompat.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDetachedFromWindow() {
        mDisposables.dispose();
        super.onDetachedFromWindow();
    }

    public interface OnConfirmListener {
        void onPicker(String shopId, String groupId, String guideId);
    }
}
