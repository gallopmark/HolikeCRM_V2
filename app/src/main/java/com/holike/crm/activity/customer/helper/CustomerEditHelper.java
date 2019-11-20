package com.holike.crm.activity.customer.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewStub;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.adapter.MultipleChoiceAdapter;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.base.CashierInputFilter;
import com.holike.crm.base.IntentValue;
import com.holike.crm.base.SimpleTextWatcher;
import com.holike.crm.bean.ActivityPoliceBean;
import com.holike.crm.bean.CurrentUserBean;
import com.holike.crm.bean.DictionaryBean;
import com.holike.crm.bean.ShopGroupBean;
import com.holike.crm.bean.SysCodeItemBean;
import com.holike.crm.dialog.OptionsPickerDialog;
import com.holike.crm.dialog.SelectRegionDialog;
import com.holike.crm.enumeration.CustomerValue;
import com.holike.crm.helper.FlexboxManagerHelper;
import com.holike.crm.helper.PickerHelper;
import com.holike.crm.helper.SelectImageHelper;
import com.holike.crm.http.CustomerUrlPath;
import com.holike.crm.http.MyHttpClient;
import com.holike.crm.http.MyJsonParser;
import com.holike.crm.http.ParamHelper;
import com.holike.crm.http.SimpleRequestCallback;
import com.holike.crm.rxbus.MessageEvent;
import com.holike.crm.rxbus.RxBus;
import com.holike.crm.util.CheckUtils;
import com.holike.crm.util.KeyBoardUtil;
import com.holike.crm.util.SharedPreferencesUtils;
import com.holike.crm.util.TimeUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.disposables.Disposable;

/**
 * Created by pony 2019/7/8
 * Copyright (c) 2019 holike
 */
public class CustomerEditHelper extends CheckInputHelper implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private LinearLayout mActivityPoliceLayout;
    private EditText mNameEditText;
    //    private RadioGroup mLinkupRadioGroup;
    private EditText mPhoneEditText, mWechatEditText;
    //    private EditText mPhoneWxEditText; //手机号码或微信号 输入框
    //    private RadioGroup mGenderRadioGroup;
    private CheckBox mManCheckbox, mWomenCheckbox;
    private TextView mSelectAgeTextView, mSelectSourceTextView,
            mSelectIntentLevelTextView, mSelectTimeTextView;
    private LinearLayout mGroupLayout; //选择组织的layout
    private List<ShopGroupBean> mCurrentShopGroupList;  //当前门店下的组织信息
    private EditText mAddressEditText;  //地址输入框
    private Disposable mCheckPhoneDisposable, mCheckWxDisposable;

    private String mOldPhoneNumber,
            mOldWxNumber;  //编辑客户时，检测手机号、微信号重复，需判断新输入的手机号、微信号等不一致再进行检测

    private Handler mHandler = new Handler();

    @Override
    void onJumpDetail() {
        mActivity.finish();
    }

    @Override
    void onDistributionMsgPush(String body) {
        mCustomerEditCallback.onDistributionMsgPush(body);
    }

    @Override
    void onReceivingCustomer(String personalId, String houseId, String shopId, String groupId) {
        mCustomerEditCallback.onReceivingCustomer(personalId, houseId, shopId, groupId);
    }

    @Override
    void onActivationCustomer(String personalId, String houseId, String shopId, String groupId) {
        mCustomerEditCallback.onActivationCustomer(personalId, houseId, shopId, groupId);
    }

    class ActivityPoliceAdapter extends CommonAdapter<ActivityPoliceBean> {
        private List<ActivityPoliceBean> mSelectedItems;

        ActivityPoliceAdapter(Context context, List<ActivityPoliceBean> mDatas, List<ActivityPoliceBean> selectItems) {
            super(context, mDatas);
            if (selectItems != null && !selectItems.isEmpty()) {
                mSelectedItems = new ArrayList<>(selectItems);
            } else {
                mSelectedItems = new ArrayList<>();
            }
        }

        public List<ActivityPoliceBean> getSelectedItems() {
            return mSelectedItems;
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_multiplechoicev2;
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, ActivityPoliceBean bean, int position) {
            ImageView ivSelect = holder.obtainView(R.id.iv_select);
            TextView tvContent = holder.obtainView(R.id.tv_content);
            setChecked(ivSelect, tvContent, mSelectedItems.contains(bean));
            holder.itemView.setOnClickListener(view -> {
                if (mSelectedItems.contains(bean)) {
                    mSelectedItems.remove(bean);
                    setChecked(ivSelect, tvContent, false);
                } else {
                    mSelectedItems.add(bean);
                    setChecked(ivSelect, tvContent, true);
                }
            });
            tvContent.setText(bean.activityPolicy);
        }

        private void setChecked(ImageView iv, TextView tv, boolean isChecked) {
            if (isChecked) {
                iv.setImageResource(R.drawable.cus_scale_space_sel);
                tv.setTextColor(ContextCompat.getColor(mContext, R.color.textColor4));
            } else {
                iv.setImageResource(R.drawable.cus_scale_space_nor);
                tv.setTextColor(ContextCompat.getColor(mContext, R.color.textColor6));
            }
        }
    }

    private SysCodeItemBean mSysCodeItemBean;   //业务字典（在application加载）
    private CurrentUserBean mCurrentUserBean; //当前登录用户信息
    private ActivityPoliceAdapter mActivityPoliceAdapter;
    private boolean mEditCustomer; //是否是编辑房屋
    private View mAddHouseView, mDepositReceivedView;
    //    private int mLinkupType = 1; //默认为手机类型
    private String mGender = "3"; //客户性别 默认为未知
    private String mAgeType; //年龄段
    private String mSource; //客户来源
    private String mIntentLevel; //意向评级
    private String mNextFollowTime; //下次跟进时间
    //    private String mPolicy; //活动优惠政策
    private String mShopId, mGroupId; //所属门店、所属组织
    private String mBudgetTypeCode; //定制预算

    private SelectRegionDialog mSelectRegionDialog;

    private String mPersonalId, mRecordStatus, mVersionNumber; //客户id，状态，版本（修改客户信息用到）
    private String mActivityPolice;

    private int mAgeIndex, mSourceIndex, mIntentIndex,
            mStoreIndex, mBudgetIndex;

    private boolean mDepositReceived;  //是否是从收取订金进来到当前页面（新建客户时需要收取订金、定制品类、添加订金票据等）
    private MultipleChoiceAdapter mCustomerProductAdapter; //定制品类适配器
    private SelectImageHelper mImageHelper;

    private CustomerEditCallback mCustomerEditCallback;

    public CustomerEditHelper(BaseActivity<?, ?> activity, CustomerEditCallback callback) {
        super(activity);
        mImageHelper = SelectImageHelper.with(activity);
        this.mCustomerEditCallback = callback;
        initViews();
        mSysCodeItemBean = IntentValue.getInstance().getSystemCode();
        mCurrentUserBean = IntentValue.getInstance().getCurrentUser();
        mEditCustomer = mActivity.getIntent().getBooleanExtra("isEdit", false);
        mDepositReceived = mActivity.getIntent().getBooleanExtra("isDeposit", false);
        if (!mEditCustomer) {
            mActivity.setTitle(R.string.customer_manager_newInstance);
            setPhoneInputType();
            ViewStub viewStub = activity.findViewById(R.id.vs_addHouse);
            setup(viewStub);
        } else {
            mActivity.setTitle(R.string.customer_manage_edit_customer);
            getView(R.id.tv_title_top).setVisibility(View.GONE);
            Bundle bundle = mActivity.getIntent().getExtras();
            if (bundle != null) {
                mPersonalId = bundle.getString(CustomerValue.PERSONAL_ID, "");
                mRecordStatus = bundle.getString("recordStatus", "");
                mVersionNumber = bundle.getString("versionNumber", "");
                mNameEditText.setText(bundle.getString("userName"));
                String phoneNumber = bundle.getString("phoneNumber");
                mOldPhoneNumber = phoneNumber;
                String wxNumber = bundle.getString("wxNumber");
                mOldWxNumber = wxNumber;
                mPhoneEditText.setText(phoneNumber);
                mWechatEditText.setText(wxNumber);
                boolean isOnline = bundle.getBoolean("isOnline");
                mPhoneEditText.setEnabled(!isOnline);
//                mPhoneEditText.setEnabled(bundle.getBoolean("isValidCustomer", true));
                mGender = bundle.getString("gender");
                if (TextUtils.equals(mGender, "1")) { //女士
                    mWomenCheckbox.setChecked(true);
                } else if (TextUtils.equals(mGender, "2")) { //先生
                    mManCheckbox.setChecked(true);
                }
                mAgeType = bundle.getString("ageType");
                mSelectAgeTextView.setText(bundle.getString("ageTypeName"));
                mSource = bundle.getString("source");
                if (TextUtils.equals(mSource, "09")) {  //线上引流客户不能编辑“客户来源”
                    mSelectSourceTextView.setOnClickListener(null);
                    mSelectSourceTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                }
                mSelectSourceTextView.setText(bundle.getString("sourceName"));
                mIntentLevel = bundle.getString("intentLevel");
                mSelectIntentLevelTextView.setText(bundle.getString("intentLevelName"));
                String nextFollowTime = bundle.getString("nextFollowTime");
                mNextFollowTime = TimeUtil.timeMillsFormat(nextFollowTime, "yyyy-MM-dd");
                mSelectTimeTextView.setText(TimeUtil.timeMillsFormat(nextFollowTime));
                mActivityPolice = bundle.getString("activityPolicy");
            }
        }
        setPhoneInputType();
        setWechatInputType();
        mManCheckbox.setOnCheckedChangeListener(this);
        mWomenCheckbox.setOnCheckedChangeListener(this);
        mCustomerEditCallback.onRequestActivityPolicy(SharedPreferencesUtils.getDealerId());
    }

    private void initViews() {
        mActivityPoliceLayout = getView(R.id.ll_activity_police);
        mNameEditText = getView(R.id.etName);
//        mLinkupRadioGroup = getView(R.id.rgLinkup);
//        mPhoneWxEditText = getView(R.id.etPhoneWx);
        mPhoneEditText = getView(R.id.et_phone);
        mWechatEditText = getView(R.id.et_wechat);
        mManCheckbox = getView(R.id.cb_man);
        mWomenCheckbox = getView(R.id.cb_women);
        mSelectAgeTextView = getView(R.id.tvSelectAge);
        mSelectSourceTextView = getView(R.id.tvSelectSource);
        mSelectIntentLevelTextView = getView(R.id.tvSelectIntent);
        mSelectTimeTextView = getView(R.id.tvSelectTime);
        mSelectAgeTextView.setOnClickListener(this);
        mSelectSourceTextView.setOnClickListener(this);
        mSelectIntentLevelTextView.setOnClickListener(this);
        mSelectTimeTextView.setOnClickListener(this);
    }

    private <T extends View> T getView(int id) {
        return mActivity.findViewById(id);
    }

    private void setup(ViewStub viewStub) {
        if (mAddHouseView == null) {
            mAddHouseView = viewStub.inflate();
            final TextView tvSelectArea = mAddHouseView.findViewById(R.id.tv_select_area);
            final TextView tvSelectShop = mAddHouseView.findViewById(R.id.tv_select_shop);
            mGroupLayout = mAddHouseView.findViewById(R.id.ll_group_layout);
            final TextView tvSelectGroup = mAddHouseView.findViewById(R.id.tv_select_group);
            final TextView tvSelectBudget = mAddHouseView.findViewById(R.id.tv_select_budget);
            View.OnClickListener listener = view -> {
                KeyBoardUtil.hideSoftInput(mActivity);
                switch (view.getId()) {
                    case R.id.tv_select_area:
                        onSelected(tvSelectArea, 6);
                        break;
                    case R.id.tv_select_shop:
                        if (mCurrentUserBean == null) {
                            mCustomerEditCallback.onRequestUserInfo();
                        } else {
                            onSelectShop(tvSelectShop);
                        }
                        break;
                    case R.id.tv_select_group:
                        if (mCurrentShopGroupList != null && !mCurrentShopGroupList.isEmpty()) {
                            onSelectGroup(tvSelectGroup);
                        }
                        break;
                    case R.id.tv_select_budget:
                        onSelected(tvSelectBudget, 8);
                        break;
                }
            };
            tvSelectArea.setOnClickListener(listener);
            tvSelectShop.setOnClickListener(listener);
            tvSelectGroup.setOnClickListener(listener);
            tvSelectBudget.setOnClickListener(listener);
            mAddressEditText = mAddHouseView.findViewById(R.id.et_address); //输入地址
            addTextWatcher(mAddressEditText);
            if (mDepositReceived) {
                ViewStub vsDeposit = mAddHouseView.findViewById(R.id.vs_receipt_deposit);
                mDepositReceivedView = vsDeposit.inflate();
                EditText etAmount = mDepositReceivedView.findViewById(R.id.et_receipt_amount);
                etAmount.setFilters(new InputFilter[]{new CashierInputFilter()});
                if (mSysCodeItemBean == null) {
                    mCustomerEditCallback.onRequestSysCode();
                } else {
                    setCustomProduct();
                }
                ViewStub vsImage = mAddHouseView.findViewById(R.id.vs_select_deposit_image);
                View vImage = vsImage.inflate();
                RecyclerView recyclerView = vImage.findViewById(R.id.rv_select_image);
                mImageHelper.tipsText(mActivity.getString(R.string.tips_add_payment_pictures)).required(true)
                        .maxSize(9).spanCount(3).attachRecyclerView(recyclerView);
            }
        }
    }

    public void setSysCodeItemBean(SysCodeItemBean bean) {
        this.mSysCodeItemBean = bean;
        if (mDepositReceived && mDepositReceivedView != null) {
            setCustomProduct();
        }
    }

    /*定制品类*/
    private void setCustomProduct() {
        RecyclerView rvProduct = mDepositReceivedView.findViewById(R.id.rv_product);
        rvProduct.setNestedScrollingEnabled(false);
        rvProduct.setLayoutManager(FlexboxManagerHelper.getDefault(mActivity));
        List<DictionaryBean> list = new ArrayList<>();
        for (Map.Entry<String, String> entry : mSysCodeItemBean.getCustomerEarnestHouse().entrySet()) {
            list.add(new DictionaryBean(entry.getKey(), entry.getValue()));
        }
        mCustomerProductAdapter = new MultipleChoiceAdapter(mActivity, list);
        rvProduct.setAdapter(mCustomerProductAdapter);
    }

    public void setCurrentUserBean(CurrentUserBean bean) {
        this.mCurrentUserBean = bean;
        onSelectShop(mAddHouseView.findViewById(R.id.tv_select_shop));
    }

    public void setActivityPoliceBean(List<ActivityPoliceBean> list) {
        if (list != null && !list.isEmpty()) {
            mActivityPoliceLayout.setVisibility(View.VISIBLE);
            RecyclerView recyclerView = getView(R.id.rv_activity_police);
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setLayoutManager(FlexboxManagerHelper.getDefault(mActivity));
            List<ActivityPoliceBean> selectItems = null;
            if (!TextUtils.isEmpty(mActivityPolice)) {
                try {
                    String[] array = mActivityPolice.split(",");
                    selectItems = new ArrayList<>();
                    for (String name : array) {
                        ActivityPoliceBean bean = new ActivityPoliceBean(name);
                        if (!list.contains(bean)) {  //如果活动政策集合中不包含详情带过来的活动政策，则将详情的活动政策添加到集合中
                            list.add(bean);
                        }
                        selectItems.add(bean);
                    }
                } catch (Exception ignored) {
                }
            }
            mActivityPoliceAdapter = new ActivityPoliceAdapter(mActivity, list, selectItems);
            recyclerView.setAdapter(mActivityPoliceAdapter);
        } else {
            mActivityPoliceLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        KeyBoardUtil.hideSoftInput(mActivity);
        switch (view.getId()) {
            case R.id.tvSelectAge:
                onSelected((TextView) view, 1);
                break;
            case R.id.tvSelectSource:
                onSelected((TextView) view, 2);
                break;
            case R.id.tvSelectIntent:
                onSelected((TextView) view, 3);
                break;
            case R.id.tvSelectTime:
                onSelected((TextView) view, 4);
                break;
        }
    }

    private void onSelected(TextView textView, int type) {
        if (mSysCodeItemBean == null) {
            mCustomerEditCallback.onRequestSysCode();
        } else {
            switch (type) {
                case 1:  //选择年龄段
                    onSelectAge(textView);
                    break;
                case 2: //选择客户来源
                    onSelectSource(textView);
                    break;
                case 3: //选择意向评级
                    onSelectIntent(textView);
                    break;
                case 4: //下次跟进时间
                    onSelectTime(textView);
                    break;
                case 6: //选择区域
                    onSelectArea(textView);
                    break;
                case 8:
                    onSelectBudget(textView); //定制预算
                    break;
            }
        }
    }

    private void onSelectAge(final TextView textView) {
        optionsPicker(mSysCodeItemBean.getCustomerAgeType(), mAgeIndex, (position, bean) -> {
            mAgeIndex = position;
            mAgeType = bean.id;
            textView.setText(bean.name);
        });
    }

    /*选择客户来源*/
    private void onSelectSource(final TextView textView) {
        Map<String, String> sourceMap = new HashMap<>();
        for (Map.Entry<String, String> entry : mSysCodeItemBean.getCustomerSourceCode().entrySet()) {
            //过滤掉“总部线上引流”
            if (TextUtils.equals(entry.getKey(), "09") || TextUtils.equals(entry.getValue(), "总部线上引流")) {
                continue;
            }
            sourceMap.put(entry.getKey(), entry.getValue());
        }
        optionsPicker(sourceMap, mSourceIndex, (position, bean) -> {
            mSourceIndex = position;
            mSource = bean.id;
            textView.setText(bean.name);
        });
    }

    private void onSelectIntent(final TextView textView) {
        optionsPicker(mSysCodeItemBean.getIntentionLevel(), mIntentIndex, (position, bean) -> {
            mIntentIndex = position;
            mIntentLevel = bean.id;
            textView.setText(bean.name);
        });
    }

    //下次跟进时间 不能选过去时间
    private void onSelectTime(final TextView textView) {
        PickerHelper.showTimePicker(mActivity, new Date(), date -> {
            mNextFollowTime = TimeUtil.dateToString(date, "yyyy-MM-dd");
            textView.setText(TimeUtil.dateToString(date, "yyyy.MM.dd"));
        });
    }

    private void onSelectArea(final TextView textView) {
        if (mSelectRegionDialog == null) {
            mSelectRegionDialog = new SelectRegionDialog(mActivity);
        }
        mSelectRegionDialog.setOnRegionSelectedListener((provinceName, provinceCode, cityName, cityCode, districtName, districtCode, address) -> {
            mProvinceCode = provinceCode;
            mCityCode = cityCode;
            mDistrictCode = districtCode;
            textView.setText(address);
            if (!TextUtils.isEmpty(mAddressEditText.getText().toString())) {
                checkAddress(mAddressEditText.getText().toString());
            }
        });
        mSelectRegionDialog.show();
    }

    /*选择门店*/
    private void onSelectShop(final TextView textView) {
        final List<CurrentUserBean.ShopInfo> list = mCurrentUserBean.getShopInfo();
        if (list.isEmpty()) return;
        List<DictionaryBean> optionsItems = new ArrayList<>();
        for (CurrentUserBean.ShopInfo bean : list) {
            optionsItems.add(new DictionaryBean(bean.shopId, bean.shopName));
        }
        PickerHelper.showOptionsPicker(mActivity, optionsItems, mStoreIndex, (position, bean) -> {
            mStoreIndex = position;
            mShopId = bean.id;
            textView.setText(bean.name);
            mGroupId = null;
            mCurrentShopGroupList = null;
            mGroupLayout.setVisibility(View.GONE);
            mCustomerEditCallback.onQueryShopGroup(mShopId);
        });
    }

    public void setShopGroupArray(List<ShopGroupBean> list) {
        if (list == null || list.isEmpty()) return;
        mCurrentShopGroupList = new ArrayList<>(list);
        mGroupLayout.setVisibility(View.VISIBLE);
    }

    /*选择组织信息*/
    private void onSelectGroup(TextView tv) {
        List<DictionaryBean> optionsItems = new ArrayList<>();
        for (ShopGroupBean bean : mCurrentShopGroupList) {
            optionsItems.add(new DictionaryBean(bean.id, bean.groupName));
        }
        PickerHelper.showOptionsPicker(mActivity, optionsItems, (position, bean) -> {
            mGroupId = bean.id;
            tv.setText(bean.name);
        });
    }

    private void onSelectBudget(final TextView textView) {
        optionsPicker(mSysCodeItemBean.getCustomerBudgetType(), mBudgetIndex, (position, bean) -> {
            mBudgetIndex = position;
            mBudgetTypeCode = bean.id;
            textView.setText(bean.name);
        });
    }

    private void optionsPicker(@NonNull Map<String, String> source, int selectIndex, final OptionsPickerDialog.OnOptionPickerListener selectListener) {
        PickerHelper.showOptionsPicker(mActivity, PickerHelper.map2OptionItems(source), selectIndex, selectListener);
    }

    /*设置为手机输入方式*/
    private void setPhoneInputType() {
        mPhoneEditText.removeTextChangedListener(mPhoneTextWatcher);
        mPhoneEditText.setSelection(mPhoneEditText.getText().length());
        mPhoneEditText.addTextChangedListener(mPhoneTextWatcher);
    }

    private TextWatcher mPhoneTextWatcher = new SimpleTextWatcher() {
        @Override
        public void onTextChanged(CharSequence cs, int i, int i1, int i2) {
            String content = cs.toString();
            if (TextUtils.isEmpty(content.trim())) {
                cancelCheckPhone();
            } else {
                if (!TextUtils.equals(content, mOldPhoneNumber) && content.length() >= 11) {
                    cancelCheckPhone();
                    postCheckPhone();
                } else {
                    cancelCheckPhone();
                }
            }
        }
    };

    private void cancelCheckPhone() {
        mHandler.removeCallbacks(mCheckPhoneTask);
        if (mCheckPhoneDisposable != null) {
            mCheckPhoneDisposable.dispose();
        }
    }

    private void postCheckPhone() {
        mHandler.postDelayed(mCheckPhoneTask, 500);
    }

    private Runnable mCheckPhoneTask = new Runnable() {
        @Override
        public void run() {
            String params = ParamHelper.Customer.checkPhone(mPhoneEditText.getText().toString());
            mCheckPhoneDisposable = MyHttpClient.postByBodyString(CustomerUrlPath.URL_CHECK_PHONE_WX, params,
                    new SimpleRequestCallback<String>() {
                        @Override
                        public void onSuccess(String json) {
                            isExistError(json);
                        }
                    });
        }
    };

    /*设置为文本输入方式*/
    private void setWechatInputType() {
        mWechatEditText.removeTextChangedListener(mWechatTextWatcher);
        mWechatEditText.setSelection(mWechatEditText.getText().length());
        mWechatEditText.addTextChangedListener(mWechatTextWatcher);
    }

    /*输入手机号或微信号监听，检查是否重复*/
    private TextWatcher mWechatTextWatcher = new SimpleTextWatcher() {
        @Override
        public void onTextChanged(CharSequence cs, int i, int i1, int i2) {
            String editable = mWechatEditText.getText().toString();
            String regex = "[^a-zA-Z0-9-_]";  //只能输入字母、数字、下划线、减号
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(editable);
            String source = m.replaceAll("").trim();    //删掉不是字母或数字的字符
            if (!editable.equals(source)) {
                mWechatEditText.setText(source);  //设置EditText的字符
                mWechatEditText.setSelection(source.length()); //因为删除了字符，要重写设置新的光标所在位置
            }
            String content = cs.toString();
            if (TextUtils.isEmpty(content.trim())) {
                cancelCheckWechat();
            } else {
                if (!TextUtils.equals(content, mOldWxNumber) && content.length() > 5) {
                    cancelCheckWechat();
                    postCheckWechat();
                } else {
                    cancelCheckWechat();
                }
            }
        }
    };

    private void cancelCheckWechat() {
        mHandler.removeCallbacks(mCheckWechatTask);
        if (mCheckWxDisposable != null) {
            mCheckWxDisposable.dispose();
        }
    }

    private void postCheckWechat() {
        mHandler.postDelayed(mCheckWechatTask, 500);
    }

    private Runnable mCheckWechatTask = new Runnable() {
        @Override
        public void run() {
            mCheckWxDisposable = MyHttpClient.postByBodyString(CustomerUrlPath.URL_CHECK_PHONE_WX,
                    ParamHelper.Customer.checkWechat(mWechatEditText.getText().toString()),
                    new SimpleRequestCallback<String>() {
                        @Override
                        public void onSuccess(String json) {
                            isExistError(json);
                        }
                    });
        }
    };

    @Override
    public void onCheckedChanged(CompoundButton cb, boolean isChecked) {
        if (cb.getId() == R.id.cb_man) {
            if (isChecked) {
                mWomenCheckbox.setChecked(false);
                mGender = "2";
            } else {
                mGender = "3";
            }
        } else {
            if (isChecked) {
                mManCheckbox.setChecked(false);
                mGender = "1";
            } else {
                mGender = "3";
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mImageHelper.onActivityResult(requestCode, resultCode, data);
    }

    public void onSave() {
        String name = mNameEditText.getText().toString();
        if (TextUtils.isEmpty(name.trim())) {
            mCustomerEditCallback.onRequired(mNameEditText.getHint());
        } else {
            String phone = mPhoneEditText.getText().toString();
            String wxNumber = mWechatEditText.getText().toString();
            if (TextUtils.isEmpty(phone) && TextUtils.isEmpty(wxNumber.trim())) { //电话、微信均未填写情况
                mCustomerEditCallback.onRequired(mActivity.getString(R.string.tips_customer_empty_linkup));
            } else if (!TextUtils.isEmpty(phone) && !CheckUtils.isMobile(phone)) { //填了手机号、手机格式不正确正确
                mCustomerEditCallback.onRequired(mActivity.getString(R.string.please_input_correct_phone));
            } else { //只填微信号
                nextStep(phone, wxNumber, name);
            }
        }
    }

    private void nextStep(String phone, String wxNumber, String name) {
        if (TextUtils.isEmpty(this.mSource)) {
            mCustomerEditCallback.onRequired(mActivity.getString(R.string.tips_customer_source_hint));
        } else {
            if (TextUtils.isEmpty(this.mIntentLevel)) {
                mCustomerEditCallback.onRequired(mActivity.getString(R.string.tips_customer_intent_hint));
            } else {
                if (mEditCustomer) {  //如果是编辑客户
                    doNoNext(phone, wxNumber, name);
                } else { //新建客户（需要选择门店、组织信息）
                    if (TextUtils.isEmpty(this.mShopId)) {
                        mCustomerEditCallback.onRequired(mActivity.getString(R.string.tips_customer_store_belong_hint));
                    } else {
                        /*门店下有组织信息 没有选择组织情况下*/
                        if (mCurrentShopGroupList != null && !mCurrentShopGroupList.isEmpty() && TextUtils.isEmpty(mGroupId)) {
                            mCustomerEditCallback.onRequired(mActivity.getString(R.string.tips_customer_store_group_hint));
                        } else {
                            doNoNext(phone, wxNumber, name);
                        }
                    }
                }
            }
        }
    }

    private void doNoNext(String phone, String wxNumber, String name) {
        String activityPolicy = null;
        if (mActivityPoliceAdapter != null && !mActivityPoliceAdapter.mSelectedItems.isEmpty()) {
            List<ActivityPoliceBean> list = mActivityPoliceAdapter.mSelectedItems;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                sb.append(list.get(i).activityPolicy);
                if (i < list.size() - 1) {
                    sb.append(",");
                }
            }
            activityPolicy = sb.toString();
        }
        if (!mEditCustomer) { //新建客户
            newCustomer(phone, wxNumber, name, activityPolicy);
//            if (mDepositReceived) { //如果是收取订金
//                EditText amountEditText = mDepositReceivedView.findViewById(R.id.et_receipt_amount);
//                String amount = amountEditText.getText().toString();
//                if (TextUtils.isEmpty(amount)) {  //未输入收款金额
//                    mCustomerEditCallback.onRequired(amountEditText.getHint());
//                } else {
//                    try {
//                        double a = Double.parseDouble(amount);
//                        if (a <= 0.00) {
//                            mCustomerEditCallback.onRequired(mActivity.getString(R.string.followup_this_payment_error_amount));
//                        } else {
//                            if (mCustomerProductAdapter == null || mCustomerProductAdapter.getSelectedItems().isEmpty()) { //未选择定制品类
//                                String tips = mActivity.getString(R.string.tips_please_select) + mActivity.getString(R.string.followup_custom_products2);
//                                mCustomerEditCallback.onRequired(tips);
//                            } else {
//                                List<DictionaryBean> list = mCustomerProductAdapter.getSelectedItems();
//                                StringBuilder sb = new StringBuilder();
//                                StringBuilder sbName = new StringBuilder();
//                                for (int i = 0; i < list.size(); i++) {
//                                    sb.append(list.get(i).id);
//                                    sbName.append(list.get(i).name);
//                                    if (i != list.size() - 1) {
//                                        sb.append(",");
//                                        sbName.append("、");
//                                    }
//                                }
//                                String customProduct = sb.toString();
//                                String customProductName = sbName.toString();
//                                List<String> images = mImageHelper.getSelectedImages();
//                                if (images.isEmpty()) { //未选择收款图片
//                                    mCustomerEditCallback.onRequired(mActivity.getString(R.string.please) + mActivity.getString(R.string.tips_add_payment_pictures));
//                                } else {
//                                    newCustomer(phone, wxNumber, name, activityPolicy, amount, customProduct, customProductName, images);
//                                }
//                            }
//                        }
//                    } catch (Exception e) {
//                        mCustomerEditCallback.onRequired(mActivity.getString(R.string.followup_this_payment_error_amount));
//                    }
//                }
//            } else {
//                newCustomer(phone, wxNumber, name, activityPolicy);
//            }
        } else {
            String body = ParamHelper.Customer.alterCustomer(mPersonalId, mRecordStatus, mVersionNumber, name, phone, wxNumber, mGender
                    , mAgeType, mSource, mIntentLevel, mNextFollowTime, activityPolicy);
            mCustomerEditCallback.onSave(mEditCustomer, body);
        }
    }

    private void newCustomer(String phone, String wxNumber, String name, String activityPolicy) {
//        newCustomer(phone, wxNumber, name, activityPolicy, null, null, null, null);
        String address = null, spareContact = null, spareContactPhone = null, remark = null;
        if (mAddHouseView != null) {
            EditText addressEditText = mAddHouseView.findViewById(R.id.et_address);
            address = addressEditText.getText().toString(); //详细地址
            EditText contactEditText = mAddHouseView.findViewById(R.id.et_spare_contact);
            spareContact = contactEditText.getText().toString(); //备用联系人
            EditText phoneEditText = mAddHouseView.findViewById(R.id.et_spare_phone);
            spareContactPhone = phoneEditText.getText().toString(); //备用联系电话
            EditText remarkEditText = mAddHouseView.findViewById(R.id.et_remark);
            remark = remarkEditText.getText().toString(); //备注
        }
        String body = ParamHelper.Customer.createCustomer(name, phone, wxNumber, mGender
                , mAgeType, mSource, mIntentLevel, mNextFollowTime, activityPolicy,
                mProvinceCode, mCityCode, mDistrictCode, address, mShopId, mGroupId, mBudgetTypeCode,
                spareContact, spareContactPhone, remark);
        mCustomerEditCallback.onSave(mEditCustomer, body);
    }

    /*收取订金，新建客户（延后处理）*/
//    private void newCustomer(String phone, String wxNumber, String name, String activityPolicy,
//                             String amount, String product, String productName, List<String> imagePaths) {
//        String address = null, spareContact = null, spareContactPhone = null, remark = null;
//        if (mAddHouseView != null) {
//            EditText addressEditText = mAddHouseView.findViewById(R.id.et_address);
//            address = addressEditText.getText().toString(); //详细地址
//            EditText contactEditText = mAddHouseView.findViewById(R.id.et_spare_contact);
//            spareContact = contactEditText.getText().toString(); //备用联系人
//            EditText phoneEditText = mAddHouseView.findViewById(R.id.et_spare_phone);
//            spareContactPhone = phoneEditText.getText().toString(); //备用联系电话
//            EditText remarkEditText = mAddHouseView.findViewById(R.id.et_remark);
//            remark = remarkEditText.getText().toString(); //备注
//        }
//        String body = ParamHelper.Customer.createCustomer(name, phone, wxNumber, mGender
//                , mAgeType, mSource, mIntentLevel, mNextFollowTime, activityPolicy,
//                mProvinceCode, mCityCode, mDistrictCode, address, mShopId, mGroupId, mBudgetTypeCode,
//                spareContact, spareContactPhone, remark);
//        mCustomerEditCallback.onSave(mEditCustomer, body);
//    }

    /*新增或修改结果*/
    @Override
    public void onSaveResult(String json) {
        if (MyJsonParser.getCode(json) == 0) {
            mActivity.showShortToast(MyJsonParser.getShowMessage(json));
            MessageEvent event;
            if (mEditCustomer) {  //编辑成功，返回上一级刷新列表
                event = new MessageEvent(CustomerValue.EVENT_TYPE_ALTER_CUSTOMER);
            } else {
                event = new MessageEvent(CustomerValue.EVENT_TYPE_ADD_CUSTOMER);
            }
            RxBus.getInstance().post(event);
            mActivity.setResult(Activity.RESULT_OK);
            mActivity.finish();
        } else {
            if (!isExistError(json)) {
                mActivity.showShortToast(MyJsonParser.getShowMessage(json));
            }
        }
    }

    private void cancelCheckPhoneWx() {
        cancelCheckPhone();
        cancelCheckWechat();
    }

    public void onDestroy() {
        cancelCheckPhoneWx();
        release();
        if (mSelectRegionDialog != null) {
            mSelectRegionDialog.dismiss();
            mSelectRegionDialog = null;
        }
    }

    public interface CustomerEditCallback {

        void onRequestSysCode();

        void onRequestUserInfo();

        void onRequestActivityPolicy(String dealerId);

        void onQueryShopGroup(String shopId);

        void onReceivingCustomer(String personalId, String houseId, String shopId, String groupId);

        void onActivationCustomer(String personalId, String houseId, String shopId, String groupId);

        void onDistributionMsgPush(String requestBody);

        void onRequired(CharSequence failed);

        void onSave(boolean isEdit, String body);
    }
}
