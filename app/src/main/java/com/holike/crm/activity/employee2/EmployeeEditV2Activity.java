package com.holike.crm.activity.employee2;

import android.os.Bundle;
import android.view.View;

import com.holike.crm.R;
import com.holike.crm.base.MyFragmentActivity;
import com.holike.crm.bean.EmployeeEditResultBean;
import com.holike.crm.presenter.activity.EmployeeEditV2Presenter;
import com.holike.crm.view.activity.EmployeeEditV2View;

import java.util.Map;

import butterknife.OnClick;

/**
 * Created by gallop on 2019/8/6.
 * Copyright holike possess 2019.
 * 新建员工或编辑员工信息 v2.0
 */
public class EmployeeEditV2Activity extends MyFragmentActivity<EmployeeEditV2Presenter, EmployeeEditV2View>
        implements EmployeeEditV2View, EmployeeEditHelper.Callback {

    private EmployeeEditHelper mHelper;

    @Override
    protected EmployeeEditV2Presenter attachPresenter() {
        return new EmployeeEditV2Presenter();
    }

    @Override
    protected int setContentViewId() {
        return R.layout.activity_employeeeditv2;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setTitle(getString(R.string.employee_new_employees));
        mHelper = new EmployeeEditHelper(this, this);
    }

    @OnClick({R.id.tv_previous_step, R.id.tv_next_step})
    public void onViewClicked(View view) {
        mHelper.onViewClicked(view);
    }

    @Override
    public void onTabChanged(int position) {
        if (position == 0) {
            setTitle(getString(R.string.employee_new_employees));
        } else if (position == 1) {
            setTitle(getString(R.string.employee_associate_shop));
        } else {
            setTitle(getString(R.string.employee_role_permission));
        }
    }

    @Override
    public void onSave(Map<String, String> params) {
        showLoading();
        mPresenter.saveEmployee(-1, params);
    }

    @Override
    public void onSaveSuccess(EmployeeEditResultBean resultBean) {
        dismissLoading();
        showLongToast(resultBean.msg);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onSaveFailure(String failReason) {
        dismissLoading();
        showLongToast(failReason);
    }

    @Override
    public void onBackPressed() {
        mHelper.onBackPressed();
    }
}
