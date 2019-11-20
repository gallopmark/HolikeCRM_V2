package com.holike.crm.fragment.report.target;

import androidx.annotation.NonNull;

import com.holike.crm.R;
import com.holike.crm.base.IntentValue;
import com.holike.crm.bean.BusinessTargetBean;
import com.holike.crm.fragment.report.GeneralReportFragment;


/**
 * Created by pony on 2019/10/28.
 * Version v3.0 app报表
 * 设置目标-员工目标
 */
public class SetEmployeeTargetFragment extends GeneralReportFragment<SetEmployeeTargetHelper> implements SetEmployeeTargetHelper.Callback {

    public static SetEmployeeTargetFragment newInstance(BusinessTargetBean bean) {
        IntentValue.getInstance().put("businessTargetBean", bean);
        return new SetEmployeeTargetFragment();
    }

    @NonNull
    @Override
    protected SetEmployeeTargetHelper newHelper() {
        return new SetEmployeeTargetHelper(this, this);
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_set_emplpyee_target;
    }

    @Override
    public void onSave(String param, String ids) {
        showLoading();
        mPresenter.setBusinessTarget(param, ids);
    }

    @Override
    public void onSuccess(Object object) {
        super.onSuccess(object);
        mHelper.onSaveSuccess((String) object);
    }

    @Override
    public void onFailure(String failReason) {
        super.onFailure(failReason);
        showShortToast(failReason);
    }

}
