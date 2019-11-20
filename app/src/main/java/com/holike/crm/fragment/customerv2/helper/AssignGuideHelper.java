package com.holike.crm.fragment.customerv2.helper;

import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.holike.crm.R;
import com.holike.crm.base.BaseFragment;
import com.holike.crm.base.IntentValue;
import com.holike.crm.bean.CurrentUserBean;
import com.holike.crm.bean.DictionaryBean;
import com.holike.crm.bean.ShopGroupBean;
import com.holike.crm.bean.ShopRoleUserBean;
import com.holike.crm.helper.PickerHelper;
import com.holike.crm.http.ParamHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pony on 2019/8/1.
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
    private int mSelectType = 1;
    private int mRequestType = 1;

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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_store:
                mSelectType = 1;
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
                mSelectType = 2;
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
        if (mSelectType == 1) {
            onSelectGuideShop();
        } else {
            onSelectSalesmanShop();
        }
    }

    /*选择导购门店*/
    private void onSelectGuideShop() {
        List<CurrentUserBean.ShopInfo> shopInfoList = mCurrentUser.getShopInfo();
        if (shopInfoList.isEmpty()) return;
        List<DictionaryBean> optionItems = new ArrayList<>();
        for (CurrentUserBean.ShopInfo shopInfo : shopInfoList) {
            optionItems.add(new DictionaryBean(shopInfo.shopId, shopInfo.shopName));
        }
        PickerHelper.showOptionsPicker(mContext, optionItems, ((position, bean) -> {
            mShopId = bean.id;
            mGuideShopTextView.setText(bean.name);
            resetValues();
            mCallback.onQueryShopGroup(mShopId);
        }));
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
        mGroupTextView.setText(null);
        mGroupLayout.setVisibility(View.GONE);
    }

    /*重置导购，恢复初始状态*/
    private void resetGuide() {
        mGuideId = null;
        mCurrentGroupGuideList = null;
        mGuideUserTextView.setText(null);
        mGuideUserTextView.setHint(mContext.getString(R.string.tips_please_select));
        mGuideLayout.setVisibility(View.GONE);
    }

    /*查找门店下是否有组织*/
    public void setShopGroup(List<ShopGroupBean> shopGroupList) {
        if (shopGroupList != null && !shopGroupList.isEmpty()) {
            mCurrentShopGroupList = new ArrayList<>(shopGroupList);
            mGroupLayout.setVisibility(View.VISIBLE);
            mGuideLayout.setVisibility(View.GONE);
        } else {  //没有组织信息，直接查找导购人员
            mRequestType = 1;
            mCallback.onQueryShopGuide(mShopId);
        }
    }

    /*选择门店组织*/
    private void onSelectShopGroup() {
        List<DictionaryBean> optionItems = new ArrayList<>();
        for (ShopGroupBean bean : mCurrentShopGroupList) {
            optionItems.add(new DictionaryBean(bean.id, bean.groupName));
        }
        PickerHelper.showOptionsPicker(mContext, optionItems, ((position, bean) -> {
            mGroupId = bean.id;
            mGroupTextView.setText(bean.name);
            resetGuide();
            mRequestType = 2;
            mCallback.onQueryGroupGuide(mGroupId);
        }));
    }

    /*查询门店下或门店分组下的导购人员数据成功*/
    public void setGuideList(List<ShopRoleUserBean.UserBean> list) {
        mGuideLayout.setVisibility(View.VISIBLE);
        if (list != null && !list.isEmpty()) {
            mCurrentGroupGuideList = new ArrayList<>(list);
            mGuideUserTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.choice_down), null);
            mGuideUserTextView.setEnabled(true);
        } else {
            if (mRequestType == 1) {
                mGuideUserTextView.setHint(mContext.getString(R.string.empty_shop_guide));
            } else {
                mGuideUserTextView.setHint(mContext.getString(R.string.empty_group_guide));
            }
            mGuideUserTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            mGuideUserTextView.setEnabled(false);
        }
    }

    /*选择导购人员*/
    private void onSelectGroupGuide() {
        List<DictionaryBean> optionItems = new ArrayList<>();
        for (ShopRoleUserBean.UserBean bean : mCurrentGroupGuideList) {
            optionItems.add(new DictionaryBean(bean.userId, bean.userName));
        }
        PickerHelper.showOptionsPicker(mContext, optionItems, ((position, bean) -> {
            mGuideId = bean.id;
            mGuideUserTextView.setText(bean.name);
        }));
    }

    /*选择业务员门店*/
    private void onSelectSalesmanShop() {
        List<CurrentUserBean.ShopInfo> shopInfoList = mCurrentUser.getShopInfo();
        if (shopInfoList.isEmpty()) return;
        List<DictionaryBean> optionItems = new ArrayList<>();
        for (CurrentUserBean.ShopInfo shopInfo : shopInfoList) {
            optionItems.add(new DictionaryBean(shopInfo.shopId, shopInfo.shopName));
        }
        PickerHelper.showOptionsPicker(mContext, optionItems, ((position, bean) -> {
            mPromoterShopId = bean.id;
            mPromoterShopTextView.setText(bean.name);
            resetSalesman();
            mCallback.onQuerySalesman(mPromoterShopId);
        }));
    }

    private void resetSalesman() {
        mPromoterId = null;
        mCurrentSalesmanList = null;
        mPromoterUserTextView.setText(null);
        mPromoterUserTextView.setHint(mContext.getString(R.string.tips_please_select));
        mSalesmanLayout.setVisibility(View.GONE);
    }

    /*获取选择的业务门店下的业务人员成功*/
    public void setSalesmanList(ShopRoleUserBean bean) {
        List<ShopRoleUserBean.InnerBean> list = bean.getPromoter();
        mSalesmanLayout.setVisibility(View.VISIBLE);
        if (!list.isEmpty() && !list.get(0).getUserList().isEmpty()) {  //取第一条数据
            mCurrentSalesmanList = new ArrayList<>(list.get(0).getUserList());
            mPromoterUserTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.choice_down), null);
            mPromoterUserTextView.setEnabled(true);
        } else {
            mPromoterUserTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            mPromoterUserTextView.setHint(mContext.getString(R.string.empty_shop_salesman));
            mPromoterUserTextView.setEnabled(false);
        }
    }

    /*选择业务人员*/
    private void onSelectSalesman() {
        if (mCurrentSalesmanList == null || mCurrentSalesmanList.isEmpty()) return;
        List<DictionaryBean> optionItems = new ArrayList<>();
        for (ShopRoleUserBean.UserBean bean : mCurrentSalesmanList) {
            optionItems.add(new DictionaryBean(bean.userId, bean.userName));
        }
        PickerHelper.showOptionsPicker(mContext, optionItems, ((position, bean) -> {
            mPromoterId = bean.id;
            mPromoterUserTextView.setText(bean.name);
        }));
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
