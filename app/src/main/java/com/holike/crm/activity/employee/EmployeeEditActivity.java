package com.holike.crm.activity.employee;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.holike.crm.R;
import com.holike.crm.base.MyFragmentActivity;
import com.holike.crm.presenter.activity.EmployeeEditPresenter;
import com.holike.crm.util.KeyBoardUtil;
import com.holike.crm.view.activity.EmployeeEditView;

import butterknife.BindView;
/*新增员工*/
@Deprecated
public class EmployeeEditActivity extends MyFragmentActivity<EmployeeEditPresenter, EmployeeEditView>
        implements View.OnClickListener, EmployeeEditView {

    @BindView(R.id.mStep1Tv)
    TextView mStep1Tv;
    @BindView(R.id.mLineV1)
    View mLineV1;
    @BindView(R.id.mStep2Tv)
    TextView mStep2Tv;
    @BindView(R.id.mLineV2)
    View mLineV2;
    @BindView(R.id.mStep3Tv)
    TextView mStep3Tv;
    @BindView(R.id.mBasicTv)
    TextView mBasicTv;
    @BindView(R.id.mRelatedTv)
    TextView mRelatedTv;
    @BindView(R.id.mSettingsTv)
    TextView mSettingsTv;
    @BindView(R.id.mPreviousStepTv)
    TextView mPreviousStepTv;
    @BindView(R.id.mNextStepTv)
    TextView mNextStepTv;

    @Override
    protected int setContentViewId() {
        return R.layout.activity_employee_edit;
    }

    @Override
    protected EmployeeEditPresenter attachPresenter() {
        return new EmployeeEditPresenter();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setStatusBarColor(R.color.textColor14);
        findViewById(R.id.mBackIv).setOnClickListener(v -> onBackPressed());
        mPresenter.init(this,fragmentManager);
        mPreviousStepTv.setOnClickListener(this);
        mNextStepTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mPreviousStepTv:
                mPresenter.onPreviousStep();
                break;
            case R.id.mNextStepTv:
                if (TextUtils.equals(mNextStepTv.getText().toString(), getString(R.string.employee_save))) {
                    //保存
                    mPresenter.saveEmployee();
                } else { //下一步
                    mPresenter.onNextStep();
                }
                break;
        }
    }

    @Override
    public void onTabChanged(int currentTab) {
        KeyBoardUtil.hideSoftInput(this);
        mPresenter.setSelected(false, mLineV1, mLineV2);
        mPreviousStepTv.setVisibility(View.GONE);
        mNextStepTv.setText(getString(R.string.next_step));
        if (currentTab == 1) {
            mPresenter.setSelected(true, mStep1Tv, mBasicTv);
            mPresenter.setSelected(false, mStep2Tv, mRelatedTv, mStep3Tv, mSettingsTv);
        } else if (currentTab == 2) {
            mPresenter.setSelected(true, mLineV1, mStep1Tv, mBasicTv, mStep2Tv, mRelatedTv);
            mPresenter.setSelected(false, mStep3Tv, mSettingsTv);
            mPreviousStepTv.setVisibility(View.VISIBLE);
        } else {
            mPresenter.setSelected(true, mLineV1, mLineV2, mStep1Tv, mBasicTv, mStep2Tv, mRelatedTv, mStep3Tv, mSettingsTv);
            mNextStepTv.setText(getString(R.string.employee_save));
            mPreviousStepTv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onContentFill(boolean isFill) {
        mNextStepTv.setEnabled(isFill);
    }

    @Override
    public void onShowLoading() {
        showLoading();
    }

    @Override
    public void onSaveSuccess() {
        showShortToast(R.string.employee_save_success);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onSaveFailure(String message) {
        showShortToast(message);
//        ToastUtils.showToast(message);
    }

    @Override
    public void onHideLoading() {
        dismissLoading();
    }
}
