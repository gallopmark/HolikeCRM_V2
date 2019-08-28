package com.holike.crm.activity.customer.helper;

import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.activity.customer.CustomerDetailV2Activity;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.base.IntentValue;
import com.holike.crm.base.SimpleTextWatcher;
import com.holike.crm.bean.ActivityPoliceBean;
import com.holike.crm.bean.CurrentUserBean;
import com.holike.crm.bean.ErrorInfoBean;
import com.holike.crm.bean.ShopGroupBean;
import com.holike.crm.bean.SysCodeItemBean;
import com.holike.crm.dialog.CustomerEditDialog;
import com.holike.crm.dialog.MaterialDialog;
import com.holike.crm.dialog.ReceivingCustomerDialog;
import com.holike.crm.dialog.SelectRegionDialog;
import com.holike.crm.enumeration.CustomerValue;
import com.holike.crm.helper.PickerHelper;
import com.holike.crm.http.CustomerUrlPath;
import com.holike.crm.http.MyHttpClient;
import com.holike.crm.http.MyJsonParser;
import com.holike.crm.http.ParamHelper;
import com.holike.crm.http.SimpleRequestCallback;
import com.holike.crm.manager.FlowLayoutManager;
import com.holike.crm.util.CheckUtils;
import com.holike.crm.util.KeyBoardUtil;
import com.holike.crm.util.SharedPreferencesUtils;
import com.holike.crm.util.TimeUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;

/**
 * Created by gallop 2019/7/8
 * Copyright (c) 2019 holike
 */
public class CustomerEditHelper implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, CompoundButton.OnCheckedChangeListener {

    private BaseActivity<?, ?> mActivity;
    private EditText mNameEditText;
    private RadioGroup mLinkupRadioGroup;
    private EditText mPhoneWxEditText; //手机号码或微信号 输入框
    //    private RadioGroup mGenderRadioGroup;
    private CheckBox mManCheckbox, mWomenCheckbox;
    private TextView mSelectAgeTextView, mSelectSourceTextView,
            mSelectIntentLevelTextView, mSelectTimeTextView;
    private LinearLayout mGroupLayout; //选择组织的layout
    private List<ShopGroupBean> mCurrentShopGroupList;  //当前门店下的组织信息
    private EditText mAddressEditText;  //地址输入框
    private Disposable mCheckPhoneWxDisposable;
    private Runnable mCheckPhoneWxTask;
    private Disposable mCheckAddressDisposable;
    private Runnable mCheckAddressTask;

    /*检测手机号是否存在，任务task*/
    class CheckPhoneWxTask implements Runnable {
        String content;

        CheckPhoneWxTask(String content) {
            this.content = content;
        }

        @Override
        public void run() {
            String phone = "", wxNumber = "";
            if (mLinkupType == 1) {
                phone = content;
            } else {
                wxNumber = content;
            }
            mCheckPhoneWxDisposable = MyHttpClient.postByBodyString(CustomerUrlPath.URL_CHECK_PHONE_WX,
                    ParamHelper.Customer.checkPhoneWx(SharedPreferencesUtils.getDealerId(), phone, wxNumber),
                    new SimpleRequestCallback<String>() {
                        @Override
                        public void onSuccess(String json) {
                            onExistError(json);
                        }
                    });
        }
    }

    /*检测地址是否存在*/
    class CheckAddressTask implements Runnable {
        String address;

        CheckAddressTask(String address) {
            this.address = address;
        }

        @Override
        public void run() {
            mCheckAddressDisposable = MyHttpClient.postByBodyString(CustomerUrlPath.URL_CHECK_ADDRESS,
                    ParamHelper.Customer.checkAddress(mProvinceCode, mCityCode, mDistrictCode, address), new SimpleRequestCallback<String>() {
                        @Override
                        public void onSuccess(String json) {
                            onExistError(json);
                        }
                    });
        }
    }

    private void onExistError(String json) {
        String msg = MyJsonParser.getMsgAsString(json);
        if (TextUtils.equals(msg, "repeat-self")) { //个人客户重复
            showSelfError(MyJsonParser.getMoreInfoAsString(json));
        } else if (TextUtils.equals(msg, "repeat-other")) { //他人重复
            ErrorInfoBean bean = MyJsonParser.fromJson(MyJsonParser.getErrorInfoAsString(json), ErrorInfoBean.class);
            existedError(bean, MyJsonParser.getMoreInfoAsString(json));
        } else if (TextUtils.equals(msg, "repeat-high_seas_person")) {
            existHighSeasError(MyJsonParser.getErrorInfoAsString(json));  //errorInfo里是houseId，moreInfo里是personalId
        }
    }

    /*若客户被同一导购/业务员创建*/
    private void showSelfError(final String personalId) {
        new MaterialDialog.Builder(mActivity)
                .message(R.string.dialog_customer_repeat_self)
                .negativeButton(R.string.no, null)
                .positiveButton(R.string.yes, (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    Bundle bundle = new Bundle();
                    bundle.putString(CustomerValue.PERSONAL_ID, personalId);
                    mActivity.startActivity(CustomerDetailV2Activity.class, bundle);
                }).show();
    }

    /*地址或者手机号已存在，弹窗提示，并显示最新状态、时间、是否收取订金等*/
    private void existedError(final ErrorInfoBean bean, final String personalId) {
        if (bean == null) return;
        new CustomerEditDialog(mActivity, bean).setOnConfirmListener((dialog, reason, shopId, groupId) ->
                mCustomerEditCallback.onDistributionMsgPush(ParamHelper.Customer.distributionMsgPush(personalId, bean.houseId, bean.status,
                        bean.statusTime, reason, shopId, groupId)))
                .show();
    }

    /*若该客户为公海客户，则弹框内容为“该客户已存在于公海，是否领取该客户”，点击是，
    则领取该客户，领取后的公海客户存在于该导购/业务员的跟进中客户列表，点击否，则退出当前弹框*/
    private void existHighSeasError(final String houseId) {
        new MaterialDialog.Builder(mActivity)
                .message(R.string.dialog_customer_repeat_high_seas_person)
                .negativeButton(R.string.no, null)
                .positiveButton(R.string.yes, (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    tipsReceivingCustomer(houseId);
//                    Bundle bundle = new Bundle();
//                    bundle.putString("personalId", personalId);
//                    mActivity.startActivity(CustomerDetailV2Activity.class, bundle);
                }).show();
    }

    /*弹出领取客户提示框，需要选择门店和组织*/
    private void tipsReceivingCustomer(final String houseId) {
        ReceivingCustomerDialog dialog = new ReceivingCustomerDialog(mActivity);
        dialog.setOnSelectedListener((shopId, groupId) -> mCustomerEditCallback.onReceivingCustomer(houseId, shopId, groupId));
        dialog.show();
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
    private boolean isEdit; //是否是编辑房屋
    private View mAddHouseView;
    private int mLinkupType = 1; //默认为手机类型
    private String mLastPhoneNumber, mLastWxNumber;  //记录上次输入的手机号、微信号，在来回切换时填充editText文本
    private String mGender = "3"; //客户性别 默认为未知
    private String mAgeType; //年龄段
    private String mSource; //客户来源
    private String mIntentLevel; //意向评级
    private String mNextFollowTime; //下次跟进时间
    //    private String mPolicy; //活动优惠政策
    private String mShopId, mGroupId; //所属门店、所属组织
    private String mBudgetTypeCode; //定制预算

    private SelectRegionDialog mSelectRegionDialog;
    private String mProvinceCode; //省份代码
    private String mCityCode; //cityCode
    private String mDistrictCode; //区(县)代码

    private String mPersonalId, mRecordStatus, mVersionNumber; //客户id，状态，版本（修改客户信息用到）
    private String mActivityPolice;

    private int mAgeIndex, mSourceIndex, mIntentIndex,
            mStoreIndex, mBudgetIndex;

    private CustomerEditCallback mCustomerEditCallback;

    public CustomerEditHelper(BaseActivity<?, ?> activity, CustomerEditCallback callback) {
        this.mActivity = activity;
        this.mCustomerEditCallback = callback;
        initViews();
        mSysCodeItemBean = IntentValue.getInstance().getSystemCode();
        CurrentUserBean userBean = IntentValue.getInstance().getCurrentUser();
        if (userBean == null) {
            mCustomerEditCallback.onRequestUserInfo();
        } else {
            setCurrentUserBean(userBean);
        }
        isEdit = mActivity.getIntent().getBooleanExtra("isEdit", false);
        if (!isEdit) {
            mActivity.setTitle(R.string.customer_manager_newInstance);
            ViewStub viewStub = activity.findViewById(R.id.vs_addHouse);
            setup(viewStub);
        } else {
            mActivity.setTitle(R.string.customer_manage_edit_customer);
            Bundle bundle = mActivity.getIntent().getExtras();
            if (bundle != null) {
                mPersonalId = bundle.getString(CustomerValue.PERSONAL_ID, "");
                mRecordStatus = bundle.getString("recordStatus", "");
                mVersionNumber = bundle.getString("versionNumber", "");
                mNameEditText.setText(bundle.getString("userName"));
                String phoneNumber = bundle.getString("phoneNumber");
                String wxNumber = bundle.getString("wxNumber");
                if (TextUtils.isEmpty(wxNumber)) {  //微信号为空  ，则默认手机号输入
                    setPhoneInputType();
                    mPhoneWxEditText.setText(phoneNumber);
                    mLinkupType = 1;
                    mLinkupRadioGroup.check(R.id.rbPhone);
                } else {
                    setTextInputType();
                    mPhoneWxEditText.setText(wxNumber);
                    mLinkupType = 2;
                    mLinkupRadioGroup.check(R.id.rbWx);
                }
                mGender = bundle.getString("gender");
                if (TextUtils.isEmpty(mGender)) {
                    mGender = "2";
                }
                if (TextUtils.equals(mGender, "1")) { //女士
                    mWomenCheckbox.setChecked(true);
                } else if (TextUtils.equals(mGender, "2")) { //先生
                    mManCheckbox.setChecked(true);
                }
                mAgeType = bundle.getString("ageType");
                mSelectAgeTextView.setText(bundle.getString("ageTypeName"));
                mSource = bundle.getString("source");
                mSelectSourceTextView.setText(bundle.getString("sourceName"));
                mIntentLevel = bundle.getString("intentLevel");
                mSelectIntentLevelTextView.setText(bundle.getString("intentLevelName"));
                String nextFollowTime = bundle.getString("nextFollowTime");
                mNextFollowTime = TimeUtil.timeMillsFormat(nextFollowTime, "yyyy-MM-dd");
                mSelectTimeTextView.setText(TimeUtil.timeMillsFormat(nextFollowTime));
                mActivityPolice = bundle.getString("activityPolicy");
            }
        }
        mLinkupRadioGroup.setOnCheckedChangeListener(this);
        mManCheckbox.setOnCheckedChangeListener(this);
        mWomenCheckbox.setOnCheckedChangeListener(this);
        watcherPhoneWx(mPhoneWxEditText);
        mCustomerEditCallback.onRequestActivityPolicy(SharedPreferencesUtils.getDealerId());
    }

    private void initViews() {
        mNameEditText = getView(R.id.etName);
        mLinkupRadioGroup = getView(R.id.rgLinkup);
        mPhoneWxEditText = getView(R.id.etPhoneWx);
        mManCheckbox = getView(R.id.cb_man);
        mWomenCheckbox = getView(R.id.cb_women);
        mSelectAgeTextView = getView(R.id.tvSelectAge);
        mSelectSourceTextView = getView(R.id.tvSelectSource);
        mSelectIntentLevelTextView = getView(R.id.tvSelectIntent);
        mSelectTimeTextView = getView(R.id.tvSelectTime);
        mLinkupRadioGroup.check(R.id.rbPhone);
        mLinkupType = 1;
        setPhoneInputType();
        mSelectAgeTextView.setOnClickListener(this);
        mSelectSourceTextView.setOnClickListener(this);
        mSelectIntentLevelTextView.setOnClickListener(this);
        mSelectTimeTextView.setOnClickListener(this);
    }

    private <T extends View> T getView(int id) {
        return mActivity.findViewById(id);
    }

    /*输入手机号或微信号监听，检查是否重复*/
    private void watcherPhoneWx(EditText editText) {
        this.mPhoneWxEditText = editText;
        editText.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int i, int i1, int i2) {
                String content = cs.toString();
                if (!TextUtils.isEmpty(content.trim())) {
                    cancelCheckPhoneWx();
                    checkPhoneWx(content);
                } else {
                    cancelCheckPhoneWx();
                }
            }
        });
    }

    private void cancelCheckPhoneWx() {
        if (mCheckPhoneWxTask != null) {
            mPhoneWxEditText.removeCallbacks(mCheckPhoneWxTask);
            mCheckPhoneWxTask = null;
        }
        if (mCheckPhoneWxDisposable != null) {
            mCheckPhoneWxDisposable.dispose();
        }
    }

    private void checkPhoneWx(String content) {
        mCheckPhoneWxTask = new CheckPhoneWxTask(content);
        mPhoneWxEditText.postDelayed(mCheckPhoneWxTask, 500);
    }

    private void setup(ViewStub viewStub) {
        if (mAddHouseView == null) {
            mAddHouseView = viewStub.inflate();
            final TextView tvSelectArea = mAddHouseView.findViewById(R.id.tvSelectArea);
            final TextView tvSelectShop = mAddHouseView.findViewById(R.id.tvSelectStore);
            mGroupLayout = mAddHouseView.findViewById(R.id.ll_group_layout);
            final TextView tvSelectGroup = mAddHouseView.findViewById(R.id.tv_select_group);
            final TextView tvSelectBudget = mAddHouseView.findViewById(R.id.tvSelectBudget);
            View.OnClickListener listener = view -> {
                KeyBoardUtil.hideSoftInput(mActivity);
                switch (view.getId()) {
                    case R.id.tvSelectArea:
                        onSelected(tvSelectArea, 6);
                        break;
                    case R.id.tvSelectStore:
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
                    case R.id.tvSelectBudget:
                        onSelected(tvSelectBudget, 8);
                        break;
                }
            };
            tvSelectArea.setOnClickListener(listener);
            tvSelectShop.setOnClickListener(listener);
            tvSelectGroup.setOnClickListener(listener);
            tvSelectBudget.setOnClickListener(listener);
            mAddressEditText = mAddHouseView.findViewById(R.id.etAddress); //输入地址
            watcherAddressInput();
        }
    }

    /*输入地址监听，检查地址是否重复*/
    private void watcherAddressInput() {
        mAddressEditText.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int i, int i1, int i2) {
                String content = cs.toString();
                if (!TextUtils.isEmpty(content.trim())) {
                    cancelCheckAddress();
                    checkAddress(content);
                } else {
                    cancelCheckAddress();
                }
            }
        });
    }

    private void cancelCheckAddress() {
        if (mCheckAddressTask != null) {
            mAddressEditText.removeCallbacks(mCheckAddressTask);
            mCheckAddressTask = null;
        }
        if (mCheckAddressDisposable != null) {
            mCheckAddressDisposable.dispose();
        }
    }

    private void checkAddress(String address) {
        mCheckAddressTask = new CheckAddressTask(address);
        mAddressEditText.postDelayed(mCheckAddressTask, 500);
    }

    public void setSysCodeItemBean(SysCodeItemBean bean) {
        this.mSysCodeItemBean = bean;
    }

    public void setCurrentUserBean(CurrentUserBean bean) {
        this.mCurrentUserBean = bean;
    }

    public void setActivityPoliceBean(List<ActivityPoliceBean> list) {
        RecyclerView recyclerView = mActivity.findViewById(R.id.rv_activity_police);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new FlowLayoutManager());
        List<ActivityPoliceBean> selectItems = null;
        if (!TextUtils.isEmpty(mActivityPolice)) {
            try {
                String[] array = mActivityPolice.split(",");
                selectItems = new ArrayList<>();
                for (String name : array) {
                    selectItems.add(new ActivityPoliceBean(name));
                }
            } catch (Exception ignored) {
            }
        }
        mActivityPoliceAdapter = new ActivityPoliceAdapter(mActivity, list, selectItems);
        recyclerView.setAdapter(mActivityPoliceAdapter);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
        setLinkupType(checkId);
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
        optionsPicker(mSysCodeItemBean.getCustomerAgeType(), mAgeIndex, (position, key, value) -> {
            mAgeIndex = position;
            mAgeType = key;
            textView.setText(value);
        });
    }

    /*选择客户来源*/
    private void onSelectSource(final TextView textView) {
        Map<String, String> sourceMap = new HashMap<>();  //过滤掉“总部线上引流”
        for (Map.Entry<String, String> entry : mSysCodeItemBean.getCustomerSourceCode().entrySet()) {
            if (!TextUtils.equals(entry.getValue(), "总部线上引流")) {
                sourceMap.put(entry.getKey(), entry.getValue());
            }
        }
        optionsPicker(sourceMap, mSourceIndex, (position, key, value) -> {
            mSourceIndex = position;
            mSource = key;
            textView.setText(value);
        });
    }

    private void onSelectIntent(final TextView textView) {
        optionsPicker(mSysCodeItemBean.getIntentionLevel(), mIntentIndex, (position, key, value) -> {
            mIntentIndex = position;
            mIntentLevel = key;
            textView.setText(value);
        });
    }

    private void onSelectTime(final TextView textView) {
        TimePickerView pickerView = new TimePickerBuilder(mActivity, (date, v) -> {
            mNextFollowTime = TimeUtil.dateToString(date, "yyyy-MM-dd");
            textView.setText(TimeUtil.dateToString(date, "yyyy.MM.dd"));
        }).setType(new boolean[]{true, true, true, false, false, false}).build();
        pickerView.show();
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
        List<String> optionsItems = new ArrayList<>();
        for (CurrentUserBean.ShopInfo bean : list) {
            optionsItems.add(bean.shopName);
        }
        PickerHelper.showOptionsPicker(mActivity, optionsItems, (options1, options2, options3, v) -> {
            mStoreIndex = options1;
            mShopId = list.get(options1).shopId;
            textView.setText(list.get(options1).shopName);
            mGroupId = null;
            mCurrentShopGroupList = null;
            mGroupLayout.setVisibility(View.GONE);
            mCustomerEditCallback.onQueryShopGroup(mShopId);
        }, mStoreIndex);
    }

    public void setShopGroupArray(List<ShopGroupBean> list) {
        if (list == null || list.isEmpty()) return;
        mCurrentShopGroupList = new ArrayList<>(list);
        mGroupLayout.setVisibility(View.VISIBLE);
    }

    /*选择组织信息*/
    private void onSelectGroup(TextView tv) {
        List<String> optionsItems = new ArrayList<>();
        for (ShopGroupBean bean : mCurrentShopGroupList) {
            optionsItems.add(bean.groupName);
        }
        PickerHelper.showOptionsPicker(mActivity, optionsItems, (options1, options2, options3, v) -> {
            if (options1 >= 0 && options1 < mCurrentShopGroupList.size()) {
                mGroupId = mCurrentShopGroupList.get(options1).id;
                tv.setText(mCurrentShopGroupList.get(options1).groupName);
            }
        });
    }

    private void onSelectBudget(final TextView textView) {
        optionsPicker(mSysCodeItemBean.getCustomerBudgetType(), mBudgetIndex, (position, key, value) -> {
            mBudgetIndex = position;
            mBudgetTypeCode = key;
            textView.setText(value);
        });
    }

    private void optionsPicker(@NonNull Map<String, String> source, int selectIndex, final OptionSelectListener selectListener) {
        final List<String> keyList = new ArrayList<>(source.keySet());
        final List<String> optionsItems = new ArrayList<>(source.values());
        OptionsPickerView<String> pickerView = new OptionsPickerBuilder(mActivity, (options1, options2, options3, v) -> selectListener.onSelect(options1, keyList.get(options1), optionsItems.get(options1))).build();
        pickerView.setPicker(optionsItems);
        pickerView.setSelectOptions(selectIndex);
        pickerView.show();
    }

    private void setLinkupType(int checkId) {
        if (checkId == R.id.rbPhone) {
            mLastWxNumber = mPhoneWxEditText.getText().toString();
            mLinkupType = 1;
            mPhoneWxEditText.setText(mLastPhoneNumber);
            setPhoneInputType();
        } else {
            mLastPhoneNumber = mPhoneWxEditText.getText().toString();
            mLinkupType = 2;
            mPhoneWxEditText.setText(mLastWxNumber);
            setTextInputType();
        }
    }

    /*设置为手机输入方式*/
    private void setPhoneInputType() {
        mPhoneWxEditText.setInputType(InputType.TYPE_CLASS_PHONE);
        mPhoneWxEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
        mPhoneWxEditText.setHint(mActivity.getString(R.string.tips_customer_linkup_phone_hint));
    }

    /*设置为文本输入方式*/
    private void setTextInputType() {
        mPhoneWxEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        mPhoneWxEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
        mPhoneWxEditText.setHint(mActivity.getString(R.string.tips_customer_linkup_wx_hint));
    }

    @Override
    public void onCheckedChanged(CompoundButton cb, boolean isChecked) {
        if (cb.getId() == R.id.cb_man) {
            if (isChecked) {
                mGender = "2";
                mWomenCheckbox.setChecked(false);
            } else {
                mGender = "3";
            }
        } else {
            if (isChecked) {
                mGender = "1";
                mManCheckbox.setChecked(false);
            } else {
                mGender = "3";
            }
        }
    }

    public void onSave() {
        String name = mNameEditText.getText().toString();
        if (TextUtils.isEmpty(name.trim())) {
            mCustomerEditCallback.onRequired(mNameEditText.getHint());
        } else {
            String phoneWx = mPhoneWxEditText.getText().toString();
            if (TextUtils.isEmpty(phoneWx.trim())) {
                mCustomerEditCallback.onRequired(mPhoneWxEditText.getHint());
            } else {
                if (mLinkupType == 1 && !CheckUtils.isMobile(phoneWx)) {  //输入手机检测
                    mCustomerEditCallback.onRequired(mActivity.getString(R.string.tips_customer_linkup_phone_correct));
                } else {
                    if (TextUtils.isEmpty(this.mSource)) {
                        mCustomerEditCallback.onRequired(mActivity.getString(R.string.tips_customer_source_hint));
                    } else {
                        if (TextUtils.isEmpty(this.mIntentLevel)) {
                            mCustomerEditCallback.onRequired(mActivity.getString(R.string.tips_customer_intent_hint));
                        } else {
                            if (isEdit) {  //如果是新建客户
                                doNoNext(phoneWx, name);
                            } else {
                                if (TextUtils.isEmpty(this.mShopId)) {
                                    mCustomerEditCallback.onRequired(mActivity.getString(R.string.tips_customer_store_belong_hint));
                                } else {
                                    /*门店下有组织信息 没有选择组织情况下*/
                                    if (mCurrentShopGroupList != null && !mCurrentShopGroupList.isEmpty() && TextUtils.isEmpty(mGroupId)) {
                                        mCustomerEditCallback.onQueryShopGroup(mActivity.getString(R.string.tips_customer_store_group_hint));
                                    } else {
                                        doNoNext(phoneWx, name);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void doNoNext(String phoneWx, String name) {
        String phone = null, wxNumber = null;
        if (mLinkupType == 1) {
            phone = phoneWx;
        } else {
            wxNumber = phoneWx;
        }
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
        String body;
        if (!isEdit) { //新建客户
            String address = null, spareContact = null, spareContactPhone = null, remark = null;
            if (mAddHouseView != null) {
                EditText addressEditText = mAddHouseView.findViewById(R.id.etAddress);
                address = addressEditText.getText().toString(); //详细地址
                EditText contactEditText = mAddHouseView.findViewById(R.id.etContact);
                spareContact = contactEditText.getText().toString(); //备用联系人
                EditText phoneEditText = mAddHouseView.findViewById(R.id.etPhone);
                spareContactPhone = phoneEditText.getText().toString(); //备用联系电话
                EditText remarkEditText = mAddHouseView.findViewById(R.id.etRemark);
                remark = remarkEditText.getText().toString(); //备注
            }
            body = ParamHelper.Customer.createCustomer(name, phone, wxNumber, mGender
                    , mAgeType, mSource, mIntentLevel, mNextFollowTime, activityPolicy,
                    mProvinceCode, mCityCode, mDistrictCode, address, mShopId, mGroupId, mBudgetTypeCode,
                    spareContact, spareContactPhone, remark);
        } else {
            body = ParamHelper.Customer.alterCustomer(mPersonalId, mRecordStatus, mVersionNumber, name, phone, wxNumber, mGender
                    , mAgeType, mSource, mIntentLevel, mNextFollowTime, activityPolicy);
        }
        mCustomerEditCallback.onSave(isEdit, body);
    }

    public void onDestroy() {
        cancelCheckPhoneWx();
        cancelCheckAddress();
        if (mSelectRegionDialog != null) {
            mSelectRegionDialog.dismiss();
            mSelectRegionDialog = null;
        }
    }

    interface OptionSelectListener {
        void onSelect(int position, String key, String value);
    }

    public interface CustomerEditCallback {

        void onRequestSysCode();

        void onRequestUserInfo();

        void onRequestActivityPolicy(String dealerId);

        void onQueryShopGroup(String shopId);

        void onReceivingCustomer(String houseId, String shopId, String groupId);

        void onDistributionMsgPush(String requestBody);

        void onRequired(CharSequence failed);

        void onSave(boolean isEdit, String body);
    }
}
