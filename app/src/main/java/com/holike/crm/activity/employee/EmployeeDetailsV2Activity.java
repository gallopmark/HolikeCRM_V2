package com.holike.crm.activity.employee;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.holike.crm.R;
import com.holike.crm.activity.employee.EmployeeDetailHelper;
import com.holike.crm.base.IntentValue;
import com.holike.crm.base.MyFragmentActivity;
import com.holike.crm.bean.EmployeeDetailV2Bean;
import com.holike.crm.bean.EmployeeEditResultBean;
import com.holike.crm.presenter.activity.EmployeeDetailV2Presenter;
import com.holike.crm.presenter.activity.EmployeeEditV2Presenter;
import com.holike.crm.view.activity.EmployeeDetailV2View;
import com.holike.crm.view.activity.EmployeeEditV2View;

import java.util.Map;

import butterknife.OnClick;

/**
 * Created by pony on 2019/8/7.
 * Copyright holike possess 2019.
 * 员工详情 v2.0
 */
public class EmployeeDetailsV2Activity extends MyFragmentActivity<EmployeeDetailV2Presenter, EmployeeDetailV2View>
        implements EmployeeDetailHelper.Callback, EmployeeDetailV2View, EmployeeEditV2View {

    private EmployeeEditV2Presenter mEditPresenter;
    private EmployeeDetailHelper mHelper;

    @Override
    protected EmployeeDetailV2Presenter attachPresenter() {
        mEditPresenter = new EmployeeEditV2Presenter();
        mEditPresenter.attach(this);
        return new EmployeeDetailV2Presenter();
    }

    @Override
    protected int setContentViewId() {
        return R.layout.activity_employee_detailv2;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setTitle(getString(R.string.employee_details_title));
        mHelper = new EmployeeDetailHelper(this, this);
    }

    @OnClick(R.id.tvSave)
    public void onViewClicked() {
        mHelper.onSave();
    }

    @Override
    public void onQuery(String userId) {
        showLoading();
        mPresenter.getEmployeeInfo(userId);
    }

    @Override
    public void onSuccess(EmployeeDetailV2Bean bean) {
        dismissLoading();
        mHelper.onSuccess(bean);
    }

    @Override
    public void onFailure(String failReason) {
        dismissLoading();
        if (isNoAuth(failReason)) {
            noAuthority();
        } else {
            noNetwork(failReason);
        }
    }

    @Override
    public void reload() {
        mHelper.onQuery();
    }

    /*修改成功刷新页面*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
//            mHelper.onQuery();
            EmployeeEditResultBean resultBean = IntentValue.getInstance().getEmployeeEditResult();
            mHelper.onActivityResult(resultBean);
            setResult(RESULT_OK);
        }
    }

    @Override
    public void onSave(Map<String, String> params) {
        showLoading();
        mEditPresenter.saveEmployee(1, params);
    }

    @Override
    public void onSaveSuccess(@Nullable EmployeeEditResultBean resultBean) {
        dismissLoading();
        if (resultBean != null) {
            showShortToast(resultBean.msg);
        }
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onSaveFailure(String failReason) {
        dismissLoading();
        showShortToast(failReason);
    }
}
