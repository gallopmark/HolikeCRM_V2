package com.holike.crm.fragment.employee;

import androidx.collection.ArrayMap;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.holike.crm.R;
import com.holike.crm.base.MyFragment;
import com.holike.crm.presenter.fragment.EmployeePresenter;
import com.holike.crm.view.fragment.EmployeeView;

import butterknife.BindView;
import butterknife.OnClick;

/*新增员工步骤一*/
//v2.0版本以前
@Deprecated
public class EmployeeEditStepOneFragment extends MyFragment<EmployeePresenter, EmployeeView> implements EmployeeView {

    @BindView(R.id.mNameEditText)
    EditText mNameEditText;
    @BindView(R.id.mPhoneEditText)
    EditText mPhoneEditText;
    @BindView(R.id.mJobNumEditText)
    EditText mJobNumEditText;
    @BindView(R.id.mSexTv)
    TextView mSexTv;
    @BindView(R.id.mPwdEditText)
    EditText mPwdEditText;
    @BindView(R.id.mClearPwdIv)
    ImageView mClearPwdIv;
    @BindView(R.id.mPwdEyeIv)
    ImageView mPwdEyeIv;
    @BindView(R.id.mPwdTwiceEditText)
    EditText mPwdTwiceEditText;
    @BindView(R.id.mClearPwdTwiceIv)
    ImageView mClearPwdTwiceIv;
    @BindView(R.id.mPwdTwiceEyeIv)
    ImageView mPwdTwiceEyeIv;

    private OnFillListener onFillListener;

    @Override
    protected EmployeePresenter attachPresenter() {
        return new EmployeePresenter();
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_employee_edit_step_one;
    }


    @Override
    protected void init() {
        super.init();
        mNameEditText.addTextChangedListener(new FillWatcher(R.id.mNameEditText));
        mPhoneEditText.addTextChangedListener(new FillWatcher(R.id.mPhoneEditText));
        mPwdEditText.addTextChangedListener(new FillWatcher(R.id.mPwdEditText));
        mPwdTwiceEditText.addTextChangedListener(new FillWatcher(R.id.mPwdTwiceEditText));
    }

    @Override
    public void onSexSelected(String sex) {
        mSexTv.setText(sex);
    }

    /*监听输入框文本变化*/
    private class FillWatcher implements TextWatcher {
        private int viewId;

        private FillWatcher(int viewId) {
            this.viewId = viewId;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            onWatcher(s);
            if (TextUtils.isEmpty(s.toString().trim())) {
                if (onFillListener != null) {
                    onFillListener.onFill(false, isPasswordCorrect());
                }
            } else {
                if (onFillListener != null) {
                    onFillListener.onFill(mPresenter.isContentFill(viewId, mNameEditText.getText().toString().trim(),
                            mPhoneEditText.getText().toString().trim(),
                            mPwdEditText.getText().toString(),
                            mPwdTwiceEditText.getText().toString()
                    ), isPasswordCorrect());
                }
            }
        }

        private boolean isPasswordCorrect() {
            return TextUtils.equals(mPwdEditText.getText().toString(), mPwdTwiceEditText.getText().toString());
        }

        /*密码输入框有内容输入则显示删除ImageView，否则隐藏*/
        private void onWatcher(CharSequence s) {
            if (TextUtils.isEmpty(s.toString().trim())) {
                if (viewId == R.id.mPwdEditText) {
                    mClearPwdIv.setVisibility(View.INVISIBLE);
                } else if (viewId == R.id.mPwdTwiceEditText) {
                    mClearPwdTwiceIv.setVisibility(View.INVISIBLE);
                }
            } else {
                if (viewId == R.id.mPwdEditText) {
                    mClearPwdIv.setVisibility(View.VISIBLE);
                } else if (viewId == R.id.mPwdTwiceEditText) {
                    mClearPwdTwiceIv.setVisibility(View.VISIBLE);
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    @OnClick({R.id.mSexTv, R.id.mClearPwdIv, R.id.mPwdEyeIv, R.id.mClearPwdTwiceIv, R.id.mPwdTwiceEyeIv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mSexTv:
                mPresenter.showSexDialog(mContext, mSexTv.getText().toString());
                break;
            case R.id.mClearPwdIv:
                mPwdEditText.setText("");
                break;
            case R.id.mPwdEyeIv:
                mPresenter.onShowPassword(mPwdEditText, mPwdEyeIv);
                break;
            case R.id.mClearPwdTwiceIv:
                mPwdTwiceEditText.setText("");
                break;
            case R.id.mPwdTwiceEyeIv:
                mPresenter.onShowPassword(mPwdTwiceEditText, mPwdTwiceEyeIv);
                break;
        }
    }

    public interface OnFillListener {
        void onFill(boolean isFill, boolean isPasswordCorrect);
    }

    public void setOnFillListener(OnFillListener onFillListener) {
        this.onFillListener = onFillListener;
    }

    /*activity参数回调的方法*/
    public ArrayMap<String, String> getDataMap() {
        ArrayMap<String, String> map = new ArrayMap<>();
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
        map.put("password", mPwdEditText.getText().toString());
        return map;
    }
}
