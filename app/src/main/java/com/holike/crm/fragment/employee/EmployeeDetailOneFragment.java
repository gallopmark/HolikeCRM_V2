package com.holike.crm.fragment.employee;

import androidx.collection.ArrayMap;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.holike.crm.R;
import com.holike.crm.activity.employee.ResetPasswordActivity;
import com.holike.crm.base.BaseView;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.EmployeeBean;
import com.holike.crm.fragment.OnFragmentDataChangedListener;
import com.holike.crm.presenter.fragment.EmployeePresenter;
import com.holike.crm.view.fragment.EmployeeView;

import butterknife.BindView;
import butterknife.OnClick;

//v2.0版本以前
@Deprecated
public class EmployeeDetailOneFragment extends MyFragment<EmployeePresenter, BaseView> implements EmployeeView, EmployeePresenter.OnStatusSelectListener {

    @BindView(R.id.mLoginIDTextView)
    TextView mLoginIDTextView;
    @BindView(R.id.mNameEditText)
    EditText mNameEditText;
    @BindView(R.id.mPhoneEditText)
    EditText mPhoneEditText;
    @BindView(R.id.mJobNumEditText)
    EditText mJobNumEditText;
    @BindView(R.id.mSexTv)
    TextView mSexTv;
    @BindView(R.id.mStatusTv)
    TextView mStatusTv;
    @BindView(R.id.mResetPwdTv)
    TextView mResetPwdTv;
    @BindView(R.id.mCreateTimeTv)
    TextView mCreateTimeTv;
    @BindView(R.id.mCreatorTv)
    TextView mCreatorTv;
    @BindView(R.id.mLastAlterTv)
    TextView mLastAlterTv;
    @BindView(R.id.mModifierTv)
    TextView mModifierTv;
    private String employeeName, employeePhone, employeeNumber, employeeSex, employeeStatus;
    private OnFragmentDataChangedListener onFragmentDataChangedListener;
    private boolean isDataUpdated = false;

    @Override
    protected EmployeePresenter attachPresenter() {
        return new EmployeePresenter();
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_employee_detailone;
    }

    @OnClick({R.id.mSexTv, R.id.mStatusTv, R.id.mResetPwdTv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mResetPwdTv:
                startActivity(ResetPasswordActivity.class, getArguments());
                break;
            case R.id.mStatusTv:
                mPresenter.showStatusDialog(mContext, mStatusTv.getText().toString(), this);
                break;
            case R.id.mSexTv:
                mPresenter.showSexDialog(mContext, mSexTv.getText().toString());
                break;
        }
    }

    @Override
    public void onSexSelected(String sex) {
        mSexTv.setText(sex);
        onEmployeeTextChanged();
    }

    @Override
    public void onStatusSelected(String status) {
        mStatusTv.setText(status);
        onEmployeeTextChanged();
    }

    public boolean isDataUpdated() {
        return isDataUpdated;
    }

    public void update(EmployeeBean bean) {
        employeeName = bean.getUserName();
        employeePhone = bean.getPhone();
        employeeNumber = bean.getStaffId();
        employeeSex = bean.getGender();
        employeeStatus = bean.getStatus();
        mLoginIDTextView.setText(bean.getUserId());
        mNameEditText.setText(employeeName);
        mPhoneEditText.setText(employeePhone);
        mJobNumEditText.setText(employeeNumber);
        mSexTv.setText(employeeSex);
        mStatusTv.setText(bean.getStatus());
        mCreatorTv.setText(bean.getCreateBy());
        mCreateTimeTv.setText(bean.getCreateTime());
        mLastAlterTv.setText(bean.getUpdateTime());
        mModifierTv.setText(bean.getUpdateBy());
        addTextWatcher();
        isDataUpdated = true;
    }

    private void addTextWatcher() {
        mNameEditText.addTextChangedListener(new MTextWatcher());
        mPhoneEditText.addTextChangedListener(new MTextWatcher());
        mJobNumEditText.addTextChangedListener(new MTextWatcher());
    }

    private boolean isTextChanged() {
        boolean isNameChanged = !TextUtils.equals(employeeName, mNameEditText.getText().toString().trim());
        boolean isPhoneChanged = !TextUtils.equals(employeePhone, mPhoneEditText.getText().toString().trim());
        boolean isJobNumberChanged = !TextUtils.equals(employeeNumber, mJobNumEditText.getText().toString().trim());
        boolean isSexChanged = !TextUtils.equals(employeeSex, mSexTv.getText().toString());
        boolean isStatusChanged = !TextUtils.equals(employeeStatus, mStatusTv.getText().toString());
        return isNameChanged || isPhoneChanged || isJobNumberChanged || isSexChanged || isStatusChanged;
    }

    private void onEmployeeTextChanged() {
        if (onFragmentDataChangedListener != null) {
            onFragmentDataChangedListener.onFragmentDataChanged(isTextChanged());
        }
    }

    private class MTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence cs, int i, int i1, int i2) {
            onEmployeeTextChanged();
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

    public ArrayMap<String, String> getDataMap() {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("userId", mLoginIDTextView.getText().toString());
        map.put("userName", mNameEditText.getText().toString().trim());
        map.put("phone", mPhoneEditText.getText().toString().trim());
        map.put("staffId", mJobNumEditText.getText().toString().trim());
        if (TextUtils.equals(mSexTv.getText().toString(), mContext.getString(R.string.sex_man))) {
            map.put("gender", "2");
        } else if (TextUtils.equals(mSexTv.getText().toString(), mContext.getString(R.string.sex_woman))) {
            map.put("gender", "1");
        } else {
            map.put("gender", "3");
        }
        if (TextUtils.equals(mStatusTv.getText().toString(), mContext.getString(R.string.valid))) {
            map.put("status", "1");
        } else {
            map.put("status", "0");
        }
        return map;
    }

    public void setOnFragmentDataChangedListener(OnFragmentDataChangedListener onFragmentDataChangedListener) {
        this.onFragmentDataChangedListener = onFragmentDataChangedListener;
    }
}
