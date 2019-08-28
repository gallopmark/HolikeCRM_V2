package com.holike.crm.activity.customer.helper;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.holike.crm.R;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.base.IntentValue;
import com.holike.crm.bean.CurrentUserBean;
import com.holike.crm.bean.CustomerManagerV2Bean;
import com.holike.crm.bean.ShopGroupBean;
import com.holike.crm.bean.SysCodeItemBean;
import com.holike.crm.dialog.SelectRegionDialog;
import com.holike.crm.enumeration.CustomerValue;
import com.holike.crm.helper.PickerHelper;
import com.holike.crm.http.ParamHelper;
import com.holike.crm.util.KeyBoardUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by gallop on 2019/8/2.
 * Copyright holike possess 2019.
 * 客户新建或编辑房屋
 */
public class CustomerEditHouseHelper implements SelectRegionDialog.OnRegionSelectedListener, View.OnClickListener {

    private BaseActivity<?, ?> mActivity;
    private boolean mIsEditHouse; //是否是编辑房屋信息
    private TextView mAreaTextView; //选择所在地区
    private EditText mAddressEditText;  //地址输入框
    private TextView mShopTextView; //选择门店
    private LinearLayout mGroupLayout; //显示组织布局
    private TextView mGroupTextView; //选择组织
    private TextView mBudgetTextView;   //选择定制预算
    private EditText mContactEditText;  //备用联系人
    private EditText mPhoneEditText;    //备用电话
    private EditText mRemarkEditText;   //备注信息
    private SelectRegionDialog mRegionDialog;   //选择省市区对话框
    private CustomerEditHouseCallback mCallback;    //回调
    private String mPersonalId; //从客户管理传递过来的personalId
    private String mProvinceCode, mCityCode, mDistrictCode; //选择后的省市区编码-
    private SysCodeItemBean mSysCodeItemBean;   //全局保存的业务字典实体类
    private CurrentUserBean mCurrentUserBean;   //全局保存的当前登录用户相关信息实体类
    private String mShopId, mGroupId, mBudgetTypeCode; //选择后的门店id，定制预算code

    private String mHouseId, mRecordStatus, mVersionNumber; //编辑房屋时传，从客户管理房屋信息带过来

    private List<ShopGroupBean> mCurrentShopGroupList;
    private int mRequestType;

    public CustomerEditHouseHelper(BaseActivity<?, ?> activity, CustomerEditHouseCallback callback) {
        this.mActivity = activity;
        mSysCodeItemBean = IntentValue.getInstance().getSystemCode();
        mCurrentUserBean = IntentValue.getInstance().getCurrentUser();
        initViews();
        Bundle bundle = mActivity.getIntent().getExtras();
        if (bundle != null) {
            mPersonalId = bundle.getString(CustomerValue.PERSONAL_ID);
            mIsEditHouse = bundle.getBoolean("isEdit", false);
            CustomerManagerV2Bean.HouseDetailBean detailBean = (CustomerManagerV2Bean.HouseDetailBean) bundle.getSerializable("detail");
            if (detailBean != null) {
                setHouseDetail(detailBean);
            }
        }
        if (mIsEditHouse) {
            mActivity.setTitle(R.string.customer_manage_edit_house);
        } else {
            mActivity.setTitle(R.string.customer_adding_houses);
        }
        this.mCallback = callback;
    }

    private void initViews() {
        mAreaTextView = getView(R.id.tv_area);
        mAddressEditText = getView(R.id.et_address);
        mShopTextView = getView(R.id.tv_select_shop);
        mGroupLayout = getView(R.id.ll_group_layout);
        mGroupTextView = getView(R.id.tv_select_group);
        mBudgetTextView = getView(R.id.tv_budget);
        mContactEditText = getView(R.id.et_standby_contact);
        mPhoneEditText = getView(R.id.et_standby_phone);
        mRemarkEditText = getView(R.id.et_remark);
        TextView saveTextView = getView(R.id.tvSave);
        mAreaTextView.setOnClickListener(this);
        mShopTextView.setOnClickListener(this);
        mGroupTextView.setOnClickListener(this);
        mBudgetTextView.setOnClickListener(this);
        saveTextView.setOnClickListener(this);
    }

    private <T extends View> T getView(int id) {
        return mActivity.findViewById(id);
    }

    private void setHouseDetail(CustomerManagerV2Bean.HouseDetailBean bean) {
        mHouseId = bean.houseId;
        mRecordStatus = bean.recordStatus;
        mVersionNumber = bean.versionNumber;
        mProvinceCode = bean.provinceCode;
        mCityCode = bean.cityCode;
        mDistrictCode = bean.districtCode;
        setArea(bean.provinceName, bean.cityName, bean.districtName);
        mShopId = bean.shopId;
        mGroupId = bean.groupId;
        mBudgetTypeCode = bean.budgetTypeCode;
        mAddressEditText.setText(bean.address);
        mShopTextView.setText(bean.shopName);
        mGroupTextView.setText(bean.groupName);
        if (!TextUtils.isEmpty(mGroupId) && !TextUtils.isEmpty(bean.groupName)) {
            mGroupLayout.setVisibility(View.VISIBLE);
        }
        if (mSysCodeItemBean != null && !TextUtils.isEmpty(bean.budgetTypeCode)) {
            mBudgetTextView.setText(mSysCodeItemBean.getCustomerBudgetType().get(bean.budgetTypeCode));
        }
        mContactEditText.setText(bean.spareContact);
        mPhoneEditText.setText(bean.spareContactPhone);
        mRemarkEditText.setText(bean.remark);
    }

    private void setArea(String provinceName, String cityName, String districtName) {
        String region = provinceName + (TextUtils.isEmpty(cityName) ? "" : "\u3000" + cityName) + (TextUtils.isEmpty(districtName) ? "" : "\u3000" + districtName);
        mAreaTextView.setText(region);
    }

    @Override
    public void onClick(View view) {
        KeyBoardUtil.hideSoftInput(mActivity);
        switch (view.getId()) {
            case R.id.tv_area:
                if (mRegionDialog == null) {
                    mRegionDialog = new SelectRegionDialog(mActivity);
                }
                mRegionDialog.setOnRegionSelectedListener(this);
                mRegionDialog.show();
                break;
            case R.id.tv_select_shop:
                if (mCurrentUserBean == null) {
                    mCallback.onQueryUserInfo();
                } else {
                    onSelectShop(mCurrentUserBean.getShopInfo());
                }
                break;
            case R.id.tv_select_group:
                if (mCurrentShopGroupList == null && !TextUtils.isEmpty(mShopId)) {  //带有房屋信息的地方进来  有门店id
                    mRequestType = 2;
                    mCallback.onQueryShopGroup(mShopId);
                } else {
                    onSelectGroup();
                }
                break;
            case R.id.tv_budget:
                if (mSysCodeItemBean == null) {
                    mCallback.onQuerySysCode();
                } else {
                    onSelectBudget(mSysCodeItemBean.customerBudgetType);
                }
                break;
            case R.id.tvSave:
                onSave();
                break;
        }
    }

    public void setSysCode(SysCodeItemBean bean) {
        mSysCodeItemBean = bean;
        onSelectBudget(mSysCodeItemBean.customerBudgetType);
    }

    public void setCurrentUser(CurrentUserBean userBean) {
        mCurrentUserBean = userBean;
        onSelectShop(mCurrentUserBean.getShopInfo());
    }

    /*选择门店组织*/
    private void onSelectShop(final List<CurrentUserBean.ShopInfo> shopInfoList) {
        if (shopInfoList == null || shopInfoList.isEmpty()) return;
        List<String> optionItems = new ArrayList<>();
        for (CurrentUserBean.ShopInfo info : shopInfoList) {
            optionItems.add(info.shopName);
        }
        PickerHelper.showOptionsPicker(mActivity, optionItems, (options1, options2, options3, v) -> {
            mShopId = shopInfoList.get(options1).shopId;
            mShopTextView.setText(optionItems.get(options1));
            resetGroup();
            mRequestType = 1;
            mCallback.onQueryShopGroup(mShopId);
        });
    }

    private void resetGroup(){
        mGroupId = null;
        mCurrentShopGroupList = null;
        mGroupTextView.setText("");
        mGroupLayout.setVisibility(View.GONE);
    }

    public void setShopGroupArray(List<ShopGroupBean> list) {
        if (list != null && !list.isEmpty()) {
            mCurrentShopGroupList = new ArrayList<>(list);
            mGroupLayout.setVisibility(View.VISIBLE);
            if (mRequestType == 2) {
                onSelectGroup();
            }
        }
    }

    /*选择组织*/
    private void onSelectGroup() {
        List<String> optionItems = new ArrayList<>();
        for (ShopGroupBean bean : mCurrentShopGroupList) {
            optionItems.add(bean.groupName);
        }
        PickerHelper.showOptionsPicker(mActivity, optionItems, (options1, options2, options3, v) -> {
            if (options1 >= 0 && options1 < mCurrentShopGroupList.size()) {
                mGroupId = mCurrentShopGroupList.get(options1).id;
                mGroupTextView.setText(mCurrentShopGroupList.get(options1).groupName);
            }
        });
    }

    /*选择预算*/
    private void onSelectBudget(Map<String, String> budgetType) {
        if (budgetType == null) return;
        List<String> list = new ArrayList<>(budgetType.keySet());
        List<String> optionItems = new ArrayList<>(budgetType.values());
        PickerHelper.showOptionsPicker(mActivity, optionItems, (options1, options2, options3, v) -> {
            mBudgetTypeCode = list.get(options1);
            mBudgetTextView.setText(optionItems.get(options1));
        });
    }

    public void onDetached() {
        if (mRegionDialog != null) {
            mRegionDialog.dismiss();
            mRegionDialog = null;
        }
    }

    @Override
    public void onRegionSelected(String provinceName, String provinceCode, String cityName, String cityCode, String districtName, String districtCode, String address) {
        mProvinceCode = provinceCode;
        mCityCode = cityCode;
        mDistrictCode = districtCode;
        setArea(provinceName, cityName, districtName);
    }

    private void onSave() {
        if (TextUtils.isEmpty(mShopId)) {
            mActivity.showShortToast(mActivity.getString(R.string.tips_customer_store_belong_hint));
        } else {
            /*有组织情况下，未选择组织提示*/
            if (mCurrentShopGroupList != null && !mCurrentShopGroupList.isEmpty() && TextUtils.isEmpty(mGroupId)) {
                mActivity.showShortToast(mActivity.getString(R.string.tips_customer_store_group_hint));
            } else {
                String body;
                String address = mAddressEditText.getText().toString();
                String spareContact = mContactEditText.getText().toString();
                String sparePhone = mPhoneEditText.getText().toString();
                String remark = mRemarkEditText.getText().toString();
                if (!mIsEditHouse) {  //非房屋（新增房屋）
                    body = ParamHelper.Customer.addHouseInfo(mPersonalId, mProvinceCode, mCityCode, mDistrictCode,
                            address, mShopId, mBudgetTypeCode, spareContact, sparePhone, remark);
                } else { //编辑房屋信息
                    body = ParamHelper.Customer.updateHouseInfo(mPersonalId, mHouseId, mRecordStatus, mVersionNumber, mProvinceCode,
                            mCityCode, mDistrictCode, address, mShopId, mBudgetTypeCode, spareContact, sparePhone, remark);
                }
                mCallback.onSave(mIsEditHouse, body);
            }
        }
    }

    public interface CustomerEditHouseCallback {
        void onQuerySysCode();

        void onQueryUserInfo();

        void onQueryShopGroup(String shopId);

        void onSave(boolean isEditHouse, String body);
    }
}
