package com.holike.crm.fragment.customerv2.helper;

import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.holike.crm.R;
import com.holike.crm.base.BaseFragment;
import com.holike.crm.bean.CurrentUserBean;
import com.holike.crm.bean.DictionaryBean;
import com.holike.crm.bean.ShopRoleUserBean;
import com.holike.crm.helper.PickerHelper;
import com.holike.crm.http.ParamHelper;

import java.util.ArrayList;
import java.util.List;


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
        List<DictionaryBean> optionItems = new ArrayList<>();
        for (CurrentUserBean.ShopInfo bean : mCurrentUser.getShopInfo()) {
            optionItems.add(new DictionaryBean(bean.shopId, bean.shopName));
        }
        PickerHelper.showOptionsPicker(mContext, optionItems, mShopIndex, (position, bean) -> {
            mShopIndex = position;
            String shopId = bean.id;
            if (!TextUtils.equals(mDesignerShopId, shopId)) {  //选择的门店发生了改变
                reset();
                mDesignerShopId = shopId;
                mShopTextView.setText(bean.name);
                mCallback.onQueryDesigner(mDesignerShopId); //回调方法，查询当前所选门店下的所有设计师
            }
        });
    }

    private void reset() {
        mDesignerId = null;
        mDesignerTextView.setText(null);
        mDesignerTextView.setHint(mContext.getString(R.string.tips_please_select));
        mDesignerLayout.setVisibility(View.GONE);
    }

    //选择设计师
    private void onSelectDesigner() {
        List<DictionaryBean> optionItems = new ArrayList<>();
        for (ShopRoleUserBean.UserBean userBean : mCurrentDesignerList) {
            optionItems.add(new DictionaryBean(userBean.userId, userBean.userName));
        }
        PickerHelper.showOptionsPicker(mContext, optionItems, mDesignerIndex, (position, bean) -> {
            mDesignerIndex = position;
            mDesignerId = bean.id;
            mDesignerTextView.setText(bean.name);
        });
    }

    /*选择设计师*/
    public void onGetDesigner(List<ShopRoleUserBean.InnerBean> list) {
        List<ShopRoleUserBean.UserBean> userList = new ArrayList<>();
        for (ShopRoleUserBean.InnerBean bean : list) {
            userList.addAll(bean.getUserList());
        }
        mDesignerLayout.setVisibility(View.VISIBLE);
        if (!userList.isEmpty()) {
            mCurrentDesignerList = new ArrayList<>(userList);
            mDesignerTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.choice_down), null);
            mDesignerTextView.setEnabled(true);
        } else {
            mDesignerTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            mDesignerTextView.setHint(mContext.getString(R.string.empty_shop_designer));
            mDesignerTextView.setEnabled(false);
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
