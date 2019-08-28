package com.holike.crm.activity.employee;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grallopmark.tablayout.CommonTabLayout;
import com.holike.crm.R;
import com.holike.crm.base.MyFragmentActivity;
import com.holike.crm.bean.EmployeeDetailBean;
import com.holike.crm.dialog.MaterialDialog;
import com.holike.crm.presenter.activity.EmployeeDetailsPresenter;
import com.holike.crm.view.activity.EmployeeDetailsView;

import butterknife.BindView;

/*员工详情页面*/
public class EmployeeDetailsActivity extends MyFragmentActivity<EmployeeDetailsPresenter, EmployeeDetailsView> implements EmployeeDetailsView {

    @BindView(R.id.rlContainer)
    RelativeLayout rlContainer;
    @BindView(R.id.mTabLayout)
    CommonTabLayout mTabLayout;
    @BindView(R.id.mSaveTextView)
    TextView mSaveTextView;

    @Override
    protected int setContentViewId() {
        return R.layout.activity_employee_details;
    }

    @Override
    protected EmployeeDetailsPresenter attachPresenter() {
        return new EmployeeDetailsPresenter();
    }

    @Override
    protected void init() {
        super.init();
        setTitle(getString(R.string.employee_details_title));
        mPresenter.init(this, fragmentManager, mTabLayout);
        mSaveTextView.setOnClickListener(view -> mPresenter.saveEmployee(this));
    }

    @Override
    public void onShowLoading() {
        showLoading();
    }

    @Override
    public void onSuccess(EmployeeDetailBean bean) {
        rlContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFailure(String message) {
        dealWithFailed(message, true);
    }

    @Override
    public void onHideLoading() {
        dismissLoading();
    }

    @Override
    public void reload() {
        super.reload();
        rlContainer.setVisibility(View.GONE);
        mPresenter.getEmployeeDetails();
    }

    @Override
    public void onDataChanged(boolean isChanged) {
        if (isChanged) {
            mSaveTextView.setEnabled(true);
        } else {
            mSaveTextView.setEnabled(false);
        }
    }

    @Override
    public void onSaveSuccess() {
        showShortToast(R.string.employee_save_success);
//        ToastUtils.showToast(getString(R.string.employee_save_success));
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onSaveFailure(String errorMessage) {
        showShortToast(errorMessage);
//        ToastUtils.showToast(errorMessage);
    }

    @Override
    public void onBackPressed() {
        if (mSaveTextView.isEnabled()) {
            new MaterialDialog.Builder(this)
                    .title(R.string.dialog_title_default)
                    .message(R.string.give_up_changed)
                    .negativeButton(R.string.cancel,null)
                    .positiveButton(R.string.call, (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                        EmployeeDetailsActivity.super.onBackPressed();
                    }).show();
//            new SimpleDialog(this).setDate(R.string.tips, R.string.give_up_changed, R.string.cancel, R.string.confirm)
//                    .setListener(new SimpleDialog.ClickListener() {
//                        @Override
//                        public void left() {
//
//                        }
//
//                        @Override
//                        public void right() {
//                            EmployeeDetailsActivity.super.onBackPressed();
//                        }
//                    }).show();
        } else {
            super.onBackPressed();
        }
    }
}
