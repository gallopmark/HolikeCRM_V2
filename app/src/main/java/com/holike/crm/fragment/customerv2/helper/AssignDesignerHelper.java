package com.holike.crm.fragment.customerv2.helper;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.holike.crm.R;
import com.holike.crm.base.BaseFragment;
import com.holike.crm.bean.CurrentUserBean;
import com.holike.crm.bean.ShopRoleUserBean;
import com.holike.crm.helper.PickerHelper;
import com.holike.crm.http.ParamHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by gallop on 2019/8/1.
 * Copyright holike possess 2019.
 * 分配设计师帮助类
 */
public class AssignDesignerHelper extends GeneralHelper {
    private TextView mShopTextView;
    private LinearLayout mDesignerLayout;
    private TextView mDesignerTextView;
    private String mDesignerShopId, mDesignerId; //选择的门店，选择的设计师
    private int mShopIndex = 0, mDesignerIndex = 0;
    private AssignDesignerCallback mCallback;
    private List<ShopRoleUserBean.UserBean> mCurrentDesignerList;

    public AssignDesignerHelper(BaseFragment<?, ?> fragment, AssignDesignerCallback callback) {
        super(fragment);
        this.mCallback = callback;
        initView(fragment.getContentView());
//        obtainBundleValue(fragment.getArguments());
    }

    private void initView(View contentView) {
        mShopTextView = contentView.findViewById(R.id.tv_store);
        mDesignerLayout = contentView.findViewById(R.id.ll_designer_layout);
        mDesignerTextView = contentView.findViewById(R.id.tv_designer);
        mShopTextView.setOnClickListener(view -> {
            if (mCurrentUser == null) {
                mCallback.onQueryUserInfo();
            } else {
                onSelectShop();
            }
        });
        mDesignerTextView.setOnClickListener(view -> onSelectDesigner());
        contentView.findViewById(R.id.tvSave).setOnClickListener(view -> onSave());
    }

    /*从详情传递过来的值*/
//    private void obtainBundleValue(Bundle bundle) {
//        if (bundle != null) {
//            mDesignerShopId = bundle.getString("designerShopId");
//            mShopTextView.setText(bundle.getString("designerShopName"));
//            mDesignerId = bundle.getString("designerId");
//            mDesignerTextView.setText(bundle.getString("designerName"));
//        }
//    }

    public void onGetCurrentUser(CurrentUserBean bean) {
        mCurrentUser = bean;
        onSelectShop();
    }

    /*选择门店*/
    private void onSelectShop() {
        final List<CurrentUserBean.ShopInfo> list = mCurrentUser.getShopInfo();
        if (list.isEmpty()) return;
        List<String> optionItems = new ArrayList<>();
        for (CurrentUserBean.ShopInfo bean : mCurrentUser.getShopInfo()) {
            optionItems.add(bean.shopName);
        }
        PickerHelper.showOptionsPicker(mContext, optionItems, (options1, options2, options3, v) -> {
            mShopIndex = options1;
            String shopId = list.get(options1).shopId;
            if (!TextUtils.equals(mDesignerShopId, shopId)) {  //选择的门店发生了改变
                reset();
                mDesignerShopId = list.get(options1).shopId;
                mShopTextView.setText(list.get(options1).shopName);
                mCallback.onQueryDesigner(mDesignerShopId); //回调方法，查询当前所选门店下的所有设计师
            }
        }, mShopIndex);
    }

    private void reset() {
        mDesignerId = null;
        mDesignerTextView.setText("");
        mDesignerLayout.setVisibility(View.GONE);
    }

    //选择设计师
    private void onSelectDesigner() {
        List<String> optionItems = new ArrayList<>();
        for (ShopRoleUserBean.UserBean userBean : mCurrentDesignerList) {
            optionItems.add(userBean.userName);
        }
        PickerHelper.showOptionsPicker(mContext, optionItems, (options1, options2, options3, v) -> {
            if (options1 >= 0 && options1 < mCurrentDesignerList.size()) {
                mDesignerIndex = options1;
                mDesignerId = mCurrentDesignerList.get(options1).userId;
                mDesignerTextView.setText(mCurrentDesignerList.get(options1).userName);
            }
        }, mDesignerIndex);
    }

    /*选择设计师*/
    public void onGetDesigner(List<ShopRoleUserBean.InnerBean> list) {
        List<ShopRoleUserBean.UserBean> userList = new ArrayList<>();
        for (ShopRoleUserBean.InnerBean bean : list) {
            userList.addAll(bean.getUserList());
        }
        if (!userList.isEmpty()) {
            mCurrentDesignerList = new ArrayList<>(userList);
            mDesignerLayout.setVisibility(View.VISIBLE);
        } else {
            mDesignerLayout.setVisibility(View.GONE);
        }
    }

    public void onSave() {
        if (TextUtils.isEmpty(mDesignerShopId)) {
            showToast(mContext.getString(R.string.tips_please_select) + mContext.getString(R.string.store));
        } else {
            if (TextUtils.isEmpty(mDesignerId)) {
                showToast(mContext.getString(R.string.tips_please_select) + mContext.getString(R.string.designer));
            } else {
                mCallback.onSave(ParamHelper.Customer.assignDesigner(mHouseId, mDesignerShopId, mDesignerId));
            }
        }
    }

    public interface AssignDesignerCallback {
        void onQueryUserInfo();

        void onQueryDesigner(String shopId);

        void onSave(String body);
    }
}
