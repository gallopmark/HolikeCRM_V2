package com.holike.crm.fragment.customerv2.helper;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.holike.crm.R;
import com.holike.crm.base.BaseFragment;
import com.holike.crm.base.IntentValue;
import com.holike.crm.bean.CurrentUserBean;
import com.holike.crm.bean.ShopGroupBean;
import com.holike.crm.bean.ShopRoleUserBean;
import com.holike.crm.helper.PickerHelper;
import com.holike.crm.http.ParamHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gallop on 2019/8/1.
 * Copyright holike possess 2019.
 * 分配导购帮助类
 */
public class AssignGuideHelper extends GeneralHelper implements View.OnClickListener {
    private TextView mGuideShopTextView,  //选择导购门店
            mGroupTextView, //选择组织
            mGuideUserTextView, //选择导购人员
            mPromoterShopTextView, //选择业务员门店
            mPromoterUserTextView; //选择业务员
    private LinearLayout mGroupLayout; //组织（门店下有组织时显示，否则隐藏）
    private LinearLayout mGuideLayout; //导购（选择门店或组织后显示）
    private LinearLayout mSalesmanLayout; //选择业务员门店后显示

    private String mShopId, //导购门店id
            mGroupId, //导购分组id(导购存在分组时传入)
            mGuideId, //导购id
            mPromoterShopId, //业务员门店id
            mPromoterId; //业务员id

    private AssignGuideCallback mCallback;
    private CurrentUserBean mCurrentUser;
    private List<ShopGroupBean> mCurrentShopGroupList; //门店下的组织
    private List<ShopRoleUserBean.UserBean> mCurrentGroupGuideList;  //组织下的导购人员
    private List<ShopRoleUserBean.UserBean> mCurrentSalesmanList; //当前选择业务门店下的业务人员集合

    public AssignGuideHelper(BaseFragment<?, ?> fragment, AssignGuideCallback callback) {
        super(fragment);
        this.mCallback = callback;
        initView(fragment.getContentView());
//        obtainBundleValue(fragment.getArguments());
        mCurrentUser = IntentValue.getInstance().getCurrentUser();
    }

    private void initView(View contentView) {
        mGuideShopTextView = contentView.findViewById(R.id.tv_store);
        mGroupLayout = contentView.findViewById(R.id.ll_organization_layout);
        mGroupTextView = contentView.findViewById(R.id.tv_organization);
        mGuideLayout = contentView.findViewById(R.id.ll_guide_layout);
        mGuideUserTextView = contentView.findViewById(R.id.tv_guide);
        mPromoterShopTextView = contentView.findViewById(R.id.tv_store2);
        mSalesmanLayout = contentView.findViewById(R.id.ll_salesman_layout);
        mPromoterUserTextView = contentView.findViewById(R.id.tv_salesman);
        mGuideShopTextView.setOnClickListener(this);
        mGroupTextView.setOnClickListener(this);
        mGuideUserTextView.setOnClickListener(this);
        mPromoterShopTextView.setOnClickListener(this);
        mPromoterUserTextView.setOnClickListener(this);
        contentView.findViewById(R.id.tvSave).setOnClickListener(this);
    }

//    private void obtainBundleValue(Bundle bundle) {
//        if (bundle != null) {
//            mShopId = bundle.getString("shopId");
////            if (!TextUtils.isEmpty(mShopId)) {
////                mCallback.onQueryShopGroup(mShopId);
////            }
//            mGuideShopTextView.setText(bundle.getString("shopName"));
//            mGroupId = bundle.getString("groupId");
//            String groupName = bundle.getString("groupName");
//            if (!TextUtils.isEmpty(mGroupId) && !TextUtils.isEmpty(groupName)) {
//                mGroupTextView.setText(groupName);
//                mGroupLayout.setVisibility(View.VISIBLE);
//            }
//            mGuideId = bundle.getString("guideId");
//            String guideName = bundle.getString("guideName");
//            if (!TextUtils.isEmpty(mGuideId) && !TextUtils.isEmpty(guideName)) {
//                mGuideUserTextView.setText(guideName);
//                mGuideLayout.setVisibility(View.VISIBLE);
//            }
//            mPromoterShopId = bundle.getString("promoterShopId");
////            if (!TextUtils.isEmpty(mPromoterShopId)) {
////                mCallback.onQuerySalesman(mPromoterShopId);
////            }
//            String promoterShopName = bundle.getString("promoterShopName");
//            if (!TextUtils.isEmpty(mPromoterShopId) && !TextUtils.isEmpty(promoterShopName)) {
//                mPromoterShopTextView.setText(promoterShopName);
//            }
//            mPromoterId = bundle.getString("promoterId");
//            String promoterName = bundle.getString("promoterName");
//            if (!TextUtils.isEmpty(mPromoterId) && !TextUtils.isEmpty(promoterName)) {
//                mPromoterUserTextView.setText(promoterName);
//                mSalesmanLayout.setVisibility(View.VISIBLE);
//            }
//        }
//    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_store:
                if (mCurrentUser == null) {
                    mCallback.onQueryUserInfo();
                } else {
                    onSelectGuideShop();
                }
                break;
            case R.id.tv_organization:  //选择组织
                onSelectShopGroup();
                break;
            case R.id.tv_guide:
                onSelectGroupGuide();
                break;
            case R.id.tv_store2:
                if (mCurrentUser == null) {
                    mCallback.onQueryUserInfo();
                } else {
                    onSelectSalesmanShop();
                }
                break;
            case R.id.tv_salesman:
                onSelectSalesman();
                break;
            case R.id.tvSave:
                onSave();
                break;
        }
    }

    public void setCurrentUserInfo(CurrentUserBean bean) {
        this.mCurrentUser = bean;
        onSelectGuideShop();
    }

    /*选择导购门店*/
    private void onSelectGuideShop() {
        List<CurrentUserBean.ShopInfo> shopInfoList = mCurrentUser.getShopInfo();
        if (shopInfoList.isEmpty()) return;
        List<String> optionItems = new ArrayList<>();
        for (CurrentUserBean.ShopInfo shopInfo : shopInfoList) {
            optionItems.add(shopInfo.shopName);
        }
        PickerHelper.showOptionsPicker(mContext, optionItems, (options1, options2, options3, v) -> {
            mShopId = shopInfoList.get(options1).shopId;
            mGuideShopTextView.setText(shopInfoList.get(options1).shopName);
            resetValues();
            mCallback.onQueryShopGroup(mShopId);
        });
    }

    /*切换门店时，重置数据*/
    private void resetValues() {
        resetGroup();
        resetGuide();
    }

    /*重置分组信息，恢复初始状态*/
    private void resetGroup() {
        mGroupId = null;
        mCurrentShopGroupList = null;
        mGroupTextView.setText("");
        mGroupLayout.setVisibility(View.GONE);
    }

    /*重置导购，恢复初始状态*/
    private void resetGuide() {
        mGuideId = null;
        mCurrentGroupGuideList = null;
        mGuideUserTextView.setText("");
        mGuideLayout.setVisibility(View.GONE);
    }

    /*查找门店下是否有组织*/
    public void setShopGroup(List<ShopGroupBean> shopGroupList) {
        if (shopGroupList != null && !shopGroupList.isEmpty()) {
            mCurrentShopGroupList = new ArrayList<>(shopGroupList);
            mGroupLayout.setVisibility(View.VISIBLE);
            mGuideLayout.setVisibility(View.GONE);
        } else {  //没有组织信息，直接查找导购人员
            mCallback.onQueryShopGuide(mShopId);
        }
    }

    /*选择门店组织*/
    private void onSelectShopGroup() {
        List<String> optionItems = new ArrayList<>();
        for (ShopGroupBean bean : mCurrentShopGroupList) {
            optionItems.add(bean.groupName);
        }
        PickerHelper.showOptionsPicker(mContext, optionItems, (options1, options2, options3, v) -> {
            mGroupId = mCurrentShopGroupList.get(options1).id;
            mGroupTextView.setText(mCurrentShopGroupList.get(options1).groupName);
            resetGuide();
            mCallback.onQueryGroupGuide(mGroupId);
        });
    }

    /*查询门店下或门店分组下的导购人员数据成功*/
    public void setGuideList(List<ShopRoleUserBean.UserBean> list) {
        if (list != null && !list.isEmpty()) {
            mCurrentGroupGuideList = new ArrayList<>(list);
            mGuideLayout.setVisibility(View.VISIBLE);
        } else {
            mGuideLayout.setVisibility(View.GONE);
        }
    }

    /*选择导购人员*/
    private void onSelectGroupGuide() {
        List<String> optionItems = new ArrayList<>();
        for (ShopRoleUserBean.UserBean bean : mCurrentGroupGuideList) {
            optionItems.add(bean.userName);
        }
        PickerHelper.showOptionsPicker(mContext, optionItems, (options1, options2, options3, v) -> {
            mGuideId = mCurrentGroupGuideList.get(options1).userId;
            mGuideUserTextView.setText(mCurrentGroupGuideList.get(options1).userName);
        });
    }

    /*选择业务员门店*/
    private void onSelectSalesmanShop() {
        List<CurrentUserBean.ShopInfo> shopInfoList = mCurrentUser.getShopInfo();
        if (shopInfoList.isEmpty()) return;
        List<String> optionItems = new ArrayList<>();
        for (CurrentUserBean.ShopInfo shopInfo : shopInfoList) {
            optionItems.add(shopInfo.shopName);
        }
        PickerHelper.showOptionsPicker(mContext, optionItems, (options1, options2, options3, v) -> {
            mPromoterShopId = shopInfoList.get(options1).shopId;
            mPromoterShopTextView.setText(shopInfoList.get(options1).shopName);
            resetSalesman();
            mCallback.onQuerySalesman(mPromoterShopId);
        });
    }

    private void resetSalesman() {
        mPromoterId = null;
        mCurrentSalesmanList = null;
        mPromoterUserTextView.setText("");
        mSalesmanLayout.setVisibility(View.GONE);
    }

    /*获取选择的业务门店下的业务人员成功*/
    public void setSalesmanList(ShopRoleUserBean bean) {
        List<ShopRoleUserBean.InnerBean> list = bean.getPromoter();
        if (!list.isEmpty() && !list.get(0).getUserList().isEmpty()) {  //取第一条数据
            mCurrentSalesmanList = new ArrayList<>(list.get(0).getUserList());
            mSalesmanLayout.setVisibility(View.VISIBLE);
        } else {
            mSalesmanLayout.setVisibility(View.GONE);
        }
    }

    /*选择业务人员*/
    private void onSelectSalesman() {
        if (mCurrentSalesmanList == null || mCurrentSalesmanList.isEmpty()) return;
        List<String> optionItems = new ArrayList<>();
        for (ShopRoleUserBean.UserBean bean : mCurrentSalesmanList) {
            optionItems.add(bean.userName);
        }
        PickerHelper.showOptionsPicker(mContext, optionItems, (options1, options2, options3, v) -> {
            mPromoterId = mCurrentSalesmanList.get(options1).userId;
            mPromoterUserTextView.setText(mCurrentSalesmanList.get(options1).userName);
        });
    }

    private void onSave() {
        if (TextUtils.isEmpty(mShopId)) {
            showToast(mContext.getString(R.string.tips_please_select) + mContext.getString(R.string.store));
        } else {
            if (TextUtils.isEmpty(mGuideId)) {
                showToast(mContext.getString(R.string.tips_please_select) + mContext.getString(R.string.shopping_guide));
            } else {
                mCallback.onSave(ParamHelper.Customer.assignGuide(mHouseId, mShopId, mGroupId,
                        mGuideId, mPromoterShopId, mPromoterId));
            }
        }
    }

    public interface AssignGuideCallback {
        void onQueryUserInfo();

        void onQueryShopGroup(String shopId);

        void onQueryShopGuide(String shopId);

        void onQueryGroupGuide(String groupId);

        void onQuerySalesman(String shopId);

        void onSave(String body);
    }
}
