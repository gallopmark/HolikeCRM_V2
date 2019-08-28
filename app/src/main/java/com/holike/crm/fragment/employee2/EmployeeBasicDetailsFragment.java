package com.holike.crm.fragment.employee2;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;


import com.holike.crm.R;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.EmployeeDetailV2Bean;
import com.holike.crm.util.CheckUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by gallop on 2019/8/7.
 * Copyright holike possess 2019.
 * 员工详情（基础信息）
 */
public class EmployeeBasicDetailsFragment extends MyFragment implements CompoundButton.OnCheckedChangeListener, RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.tv_account)
    TextView mAccountTextView;
    @BindView(R.id.mNameEditText)
    EditText mNameEditText;
    @BindView(R.id.mPhoneEditText)
    EditText mPhoneEditText;
    @BindView(R.id.cb_man)
    CheckBox mManCheckbox;
    @BindView(R.id.cb_women)
    CheckBox mWomenCheckbox;
    @BindView(R.id.radioGroup)
    RadioGroup mRadioGroup;
    @BindView(R.id.tv_shop_selected)
    TextView mShopSelectedTextView;
    @BindView(R.id.tv_role_selected)
    TextView mRoleSelectedTextView;
    @BindView(R.id.mCreateTimeTv)
    TextView mCreateTimeTextView;
    @BindView(R.id.mCreatorTv)
    TextView mCreatorTextView;
    @BindView(R.id.mLastAlterTv)
    TextView mLastAlterTextView;
    @BindView(R.id.mModifierTv)
    TextView mModifierTextView;

    private String mGender; //性别
    private String mStatus; //是否有效
    private OnViewClickListener mListener;

    @Override
    protected BasePresenter attachPresenter() {
        return null;
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_employee_detail_basic2;
    }

    @Override
    protected void init() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            setData(bundle);
        }
        mManCheckbox.setOnCheckedChangeListener(this);
        mWomenCheckbox.setOnCheckedChangeListener(this);
        mRadioGroup.setOnCheckedChangeListener(this);
    }

    private void setData(Bundle bundle) {
        EmployeeDetailV2Bean.InfoBean infoBean = (EmployeeDetailV2Bean.InfoBean) bundle.getSerializable("userInfo");
        if (infoBean != null) {
            mAccountTextView.setText(infoBean.userId);
            mNameEditText.setText(infoBean.userName);
            mPhoneEditText.setText(infoBean.phone);
            if (infoBean.isMr()) {
                mManCheckbox.setChecked(true);
            } else if (infoBean.isMs()) {
                mWomenCheckbox.setChecked(true);
            }
            if (infoBean.isValid()) {
                mRadioGroup.check(R.id.rb_valid);
            } else {
                mRadioGroup.check(R.id.rb_invalid);
            }
            mCreateTimeTextView.setText(infoBean.createTime);
            mCreatorTextView.setText(infoBean.createBy);
            mLastAlterTextView.setText(infoBean.updateTime);
            mModifierTextView.setText(infoBean.updateBy);
        }
        setRelatedInfo(bundle.getString("shopInfo"), bundle.getString("roleInfo"));
    }

    public void setRelatedInfo(String shopName, String roleName) {
        String chosen = mContext.getString(R.string.tips_chosen);
        String shopInfo = chosen + (TextUtils.isEmpty(shopName) ? "" : shopName);
        mShopSelectedTextView.setText(shopInfo);
        String roleInfo = chosen + (TextUtils.isEmpty(roleName) ? "" : roleName);
        mRoleSelectedTextView.setText(roleInfo);
    }

    @OnClick({R.id.tv_reset_password, R.id.tv_associated_shop, R.id.tv_role})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_reset_password:
                if (mListener != null) {
                    mListener.onResetPassword();
                }
                break;
            case R.id.tv_associated_shop:
                if (mListener != null) {
                    mListener.onAssociateShop();
                }
                break;
            case R.id.tv_role:
                if (mListener != null) {
                    mListener.onSelectRole();
                }
                break;
        }
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

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
        if (checkId == R.id.rb_valid) { //有效
            mStatus = "1";
        } else {
            mStatus = "0";  //无效
        }
    }

    /*是否可以走下一部流程*/
    public boolean canGoNext() {
        String userName = mNameEditText.getText().toString();
        if (TextUtils.isEmpty(userName.trim())) {
            showShortToast(mNameEditText.getHint());
            return false;
        } else {
            String phone = mPhoneEditText.getText().toString();
            if (TextUtils.isEmpty(phone)) {
                showShortToast(mPhoneEditText.getHint());
                return false;
            } else {
                if (!CheckUtils.isMobile(phone)) {
                    showShortToast(mContext.getString(R.string.please_input_correct_phone));
                    return false;
                } else {
                    return true;
                }
            }
        }
    }

    public Map<String, String> obtain() {
        Map<String, String> params = new HashMap<>();
        params.put("userName", mNameEditText.getText().toString().trim());
        params.put("phone", mPhoneEditText.getText().toString());
        params.put("gender", mGender);
        params.put("status", mStatus);
        return params;
    }

    public void setOnViewClickListener(OnViewClickListener listener) {
        this.mListener = listener;
    }


    public interface OnViewClickListener {
        void onResetPassword();

        void onAssociateShop();

        void onSelectRole();
    }
}
