package com.holike.crm.dialog;

import android.content.Context;
import android.content.res.Resources;
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
import com.holike.crm.bean.ShopGroupBean;
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
 * Created by gallop on 2019/8/26.
 * Copyright holike possess 2019.
 * 领取客户
 */
public class ReceivingCustomerDialog extends CommonDialog {

    private String mShopId, mGroupId;
    private TextView mShopTextView;
    private ProgressBar mShopProgressBar;
    private LinearLayout mGroupLayout;
    private TextView mGroupTextView;
    private TextView mConfirmTextView;
    private CompositeDisposable mDisposables;
    private List<ShopGroupBean> mCurrentGroupList; //当前门店下的组织

    private OnSelectedListener mSelectedListener;

    public ReceivingCustomerDialog(Context context) {
        super(context);
        mDisposables = new CompositeDisposable();
        setCanceledOnTouchOutside(true);
        setup();
    }

    public void setOnSelectedListener(OnSelectedListener listener) {
        mSelectedListener = listener;
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

    private void getUserInfo() {
        mDisposables.add(MyHttpClient.getByTimeout(CustomerUrlPath.URL_GET_USER_INFO, 60, new RequestCallBack<CurrentUserBean>() {
            @Override
            public void onStart(Disposable d) {
                mShopTextView.setVisibility(View.GONE);
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
                mShopTextView.setVisibility(View.VISIBLE);
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

    private void onSelected() {
        mConfirmTextView.setEnabled(true);
    }

    @Override
    protected int bindContentView() {
        return R.layout.dialog_receiving_customer;
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


    private void showToast(String text) {
        AppToastCompat.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDetachedFromWindow() {
        mDisposables.dispose();
        super.onDetachedFromWindow();
    }

    public interface OnSelectedListener {
        void onSelected(String shopId, String groupId);
    }
}
