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
import com.holike.crm.bean.DictionaryBean;
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
 * Created by pony on 2019/8/2.
 * Copyright holike possess 2019.
 * 客户新建或编辑房屋
 */
public class CustomerEditHouseHelper extends CheckInputHelper implements SelectRegionDialog.OnRegionSelectedListener, View.OnClickListener {

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
    private SysCodeItemBean mSysCodeItemBean;   //全局保存的业务字典实体类
    private CurrentUserBean mCurrentUserBean;   //全局保存的当前登录用户相关信息实体类
    private String mShopId, mGroupId, mBudgetTypeCode; //选择后的门店id，定制预算code

    private String mHouseId, mRecordStatus, mVersionNumber; //编辑房屋时传，从客户管理房屋信息带过来

    private List<ShopGroupBean> mCurrentShopGroupList;
    private int mRequestType;

    public CustomerEditHouseHelper(BaseActivity<?, ?> activity, CustomerEditHouseCallback callback) {
        super(activity);
        this.mCallback = callback;
        mSysCodeItemBean = IntentValue.getInstance().getSystemCode();
        mCurrentUserBean = IntentValue.getInstance().getCurrentUser();
        initViews();
        Bundle bundle = mActivity.getIntent().getExtras();
        if (bundle != null) {
            mPersonalId = bundle.getString(CustomerValue.PERSONAL_ID);
            mIsEditHouse = bundle.getBoolean("isEdit", false);
            if (mIsEditHouse) {
                setHouseDetail(bundle);
            }
        }
        if (mIsEditHouse) {
            mActivity.setTitle(R.string.customer_manage_edit_house);
            setShopGroupUnSelectable();
        } else {
            mActivity.setTitle(R.string.customer_adding_houses);
        }
        addTextWatcher(mAddressEditText);
    }

    private void initViews() {
        getView(R.id.tv_title_tips).setVisibility(View.GONE);
        mAreaTextView = getView(R.id.tv_select_area);
        mAddressEditText = getView(R.id.et_address);
        mShopTextView = getView(R.id.tv_select_shop);
        mGroupLayout = getView(R.id.ll_group_layout);
        mGroupTextView = getView(R.id.tv_select_group);
        mBudgetTextView = getView(R.id.tv_select_budget);
        mContactEditText = getView(R.id.et_spare_contact);
        mPhoneEditText = getView(R.id.et_spare_phone);
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

    private void setHouseDetail(Bundle bundle) {
        mHouseId = bundle.getString("houseId");
        mRecordStatus = bundle.getString("recordStatus");
        mVersionNumber = bundle.getString("versionNumber");
        mProvinceCode = bundle.getString("provinceCode");
        mCityCode = bundle.getString("cityCode");
        mDistrictCode = bundle.getString("districtCode");
        String provinceName = bundle.getString("provinceName");
        String cityName = bundle.getString("cityName");
        String districtName = bundle.getString("districtName");
        setArea(provinceName, cityName, districtName);
        mShopId = bundle.getString("shopId");
        mGroupId = bundle.getString("groupId");
        mBudgetTypeCode = bundle.getString("budgetTypeCode");
        String address = bundle.getString("address");
        mOldAddress = address;
        mAddressEditText.setText(address);
        mShopTextView.setText(bundle.getString("shopName"));
        String groupName = bundle.getString("groupName");
        mGroupTextView.setText(groupName);
        if (!TextUtils.isEmpty(mGroupId) && !TextUtils.isEmpty(groupName)) {
            mGroupLayout.setVisibility(View.VISIBLE);
        }
        if (mSysCodeItemBean != null && !TextUtils.isEmpty(mBudgetTypeCode)) {
            mBudgetTextView.setText(mSysCodeItemBean.getCustomerBudgetType().get(mBudgetTypeCode));
        }
        mContactEditText.setText(bundle.getString("spareContact"));
        mPhoneEditText.setText(bundle.getString("spareContactPhone"));
        mRemarkEditText.setText(bundle.getString("remark"));
    }

    private void setArea(String provinceName, String cityName, String districtName) {
        String region = (TextUtils.isEmpty(provinceName) ? "" : provinceName)
                + (TextUtils.isEmpty(cityName) ? "" : "\u3000" + cityName) +
                (TextUtils.isEmpty(districtName) ? "" : "\u3000" + districtName);
        mAreaTextView.setText(region);
    }

    /*如果是编辑房屋，门店和组织不能被选择*/
    private void setShopGroupUnSelectable() {
        mShopTextView.setCompoundDrawables(null, null, null, null);
        mShopTextView.setOnClickListener(null);
        mGroupTextView.setCompoundDrawables(null, null, null, null);
        mGroupTextView.setOnClickListener(null);
    }

    @Override
    void onDistributionMsgPush(String body) {
        mCallback.onDistributionMsgPush(body);
    }

    @Override
    void onReceivingCustomer(String personalId, String houseId, String shopId, String groupId) {
        mCallback.onReceivingCustomer(personalId, houseId, shopId, groupId);
    }

    @Override
    void onActivationCustomer(String personalId, String houseId, String shopId, String groupId) {
        mCallback.onActivationCustomer(personalId,houseId, shopId, groupId);
    }

    @Override
    public void onClick(View view) {
        KeyBoardUtil.hideSoftInput(mActivity);
        switch (view.getId()) {
            case R.id.tv_select_area:
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
            case R.id.tv_select_budget:
                if (mSysCodeItemBean == null) {
                    mCallback.onQuerySysCode();
                } else {
                    onSelectBudget(mSysCodeItemBean.getCustomerBudgetType());
                }
                break;
            case R.id.tvSave:
                onSave();
                break;
        }
    }

    public void setSysCode(SysCodeItemBean bean) {
        mSysCodeItemBean = bean;
        onSelectBudget(mSysCodeItemBean.getCustomerBudgetType());
    }

    public void setCurrentUser(CurrentUserBean userBean) {
        mCurrentUserBean = userBean;
        onSelectShop(mCurrentUserBean.getShopInfo());
    }

    /*选择门店组织*/
    private void onSelectShop(final List<CurrentUserBean.ShopInfo> shopInfoList) {
        if (shopInfoList == null || shopInfoList.isEmpty()) return;
        List<DictionaryBean> optionItems = new ArrayList<>();
        for (CurrentUserBean.ShopInfo info : shopInfoList) {
            optionItems.add(new DictionaryBean(info.shopId, info.shopName));
        }
        PickerHelper.showOptionsPicker(mActivity, optionItems, (position, bean) -> {
            mShopId = bean.id;
            mShopTextView.setText(bean.name);
            resetGroup();
            mRequestType = 1;
            mCallback.onQueryShopGroup(mShopId);
        });
    }

    private void resetGroup() {
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
        List<DictionaryBean> optionItems = new ArrayList<>();
        for (ShopGroupBean bean : mCurrentShopGroupList) {
            optionItems.add(new DictionaryBean(bean.id, bean.groupName));
        }
        PickerHelper.showOptionsPicker(mActivity, optionItems, (position, bean) -> {
            mGroupId = bean.id;
            mGroupTextView.setText(bean.name);
        });
    }

    /*选择预算*/
    private void onSelectBudget(Map<String, String> budgetType) {
        if (budgetType == null) return;
        List<DictionaryBean> optionItems = new ArrayList<>();
        for (Map.Entry<String, String> entry : budgetType.entrySet()) {
            optionItems.add(new DictionaryBean(entry.getKey(), entry.getValue()));
        }
        PickerHelper.showOptionsPicker(mActivity, optionItems, (position, bean) -> {
            mBudgetTypeCode = bean.id;
            mBudgetTextView.setText(bean.name);
        });
    }

    public void onDetached() {
        if (mRegionDialog != null) {
            mRegionDialog.dismiss();
            mRegionDialog = null;
        }
        release();
    }

    @Override
    public void onRegionSelected(String provinceName, String provinceCode, String cityName, String cityCode, String districtName, String districtCode, String address) {
        mProvinceCode = provinceCode;
        mCityCode = cityCode;
        mDistrictCode = districtCode;
        setArea(provinceName, cityName, districtName);
        String cAddress = mAddressEditText.getText().toString();
        if (!TextUtils.equals(cAddress, mOldAddress) && !TextUtils.isEmpty(cAddress)) {
            checkAddress(mAddressEditText.getText().toString());
        }
    }

    private void onSave() {
        if (TextUtils.isEmpty(mShopId)) {
            mActivity.showShortToast(mActivity.getString(R.string.tips_customer_store_belong_hint));
        } else {
            /*有组织情况下，未选择组织提示*/
            if (mCurrentShopGroupList != null && !mCurrentShopGroupList.isEmpty() && TextUtils.isEmpty(mGroupId)) {
                mActivity.showShortToast(mActivity.getString(R.string.tips_customer_store_group_hint));
            } else {
                String sparePhone = mPhoneEditText.getText().toString();
                //备用电话限制 11-15位数
                if (!TextUtils.isEmpty(sparePhone) && (sparePhone.length() < 11 || sparePhone.length() > 15)) {
                    mActivity.showLongToast(R.string.tips_customer_standby_phone_error);
                } else {
                    String body;
                    String address = mAddressEditText.getText().toString();
                    String spareContact = mContactEditText.getText().toString();
                    String remark = mRemarkEditText.getText().toString();
                    if (!mIsEditHouse) {  //非房屋（新增房屋）
                        body = ParamHelper.Customer.addHouseInfo(mPersonalId, mProvinceCode, mCityCode, mDistrictCode,
                                address, mShopId, mGroupId, mBudgetTypeCode, spareContact, sparePhone, remark);
                    } else { //编辑房屋信息
                        body = ParamHelper.Customer.updateHouseInfo(mPersonalId, mHouseId, mRecordStatus, mVersionNumber, mProvinceCode,
                                mCityCode, mDistrictCode, address, mShopId, mGroupId, mBudgetTypeCode, spareContact, sparePhone, remark);
                    }
                    mCallback.onSave(mIsEditHouse, body);
                }
            }
        }
    }

    public interface CustomerEditHouseCallback {
        void onQuerySysCode();

        void onQueryUserInfo();

        void onQueryShopGroup(String shopId);

        void onSave(boolean isEditHouse, String body);

        void onReceivingCustomer(String personalId, String houseId, String shopId, String groupId);

        void onActivationCustomer(String personalId, String houseId, String shopId, String groupId);

        void onDistributionMsgPush(String requestBody);
    }
}
