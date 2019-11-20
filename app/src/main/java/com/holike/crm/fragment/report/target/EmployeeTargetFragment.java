package com.holike.crm.fragment.report.target;

import androidx.annotation.NonNull;

import com.holike.crm.R;
import com.holike.crm.base.IntentValue;
import com.holike.crm.bean.BusinessObjectivesBean;
import com.holike.crm.fragment.report.GeneralReportFragment;

/**
 * Created by pony on 2019/10/30.
 * Version v3.0 app报表
 * 员工目标
 */
public class EmployeeTargetFragment extends GeneralReportFragment<EmployeeTargetHelper> implements EmployeeTargetHelper.Callback {
    public static EmployeeTargetFragment newInstance(BusinessObjectivesBean bean) {
        IntentValue.getInstance().put("business-objectives-bean", bean);
        return new EmployeeTargetFragment();
    }

    @NonNull
    @Override
    protected EmployeeTargetHelper newHelper() {
        return new EmployeeTargetHelper(this, this);
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_employee_target;
    }

    @Override
    public void onSelectShop(String shopId) {
        showLoading();
        mPresenter.getBusinessObjectives(shopId);
    }

    @Override
    public void onSuccess(Object object) {
        super.onSuccess(object);
        mHelper.onSuccess(object);
    }

    @Override
    public void onFailure(String failReason) {
        super.onFailure(failReason);
        mHelper.onFailure(failReason);
    }

    @Override
    protected void reload() {
        mHelper.doRequest();
    }
}
