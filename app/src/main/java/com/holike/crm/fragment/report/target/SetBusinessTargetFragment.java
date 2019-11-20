package com.holike.crm.fragment.report.target;

import androidx.annotation.NonNull;

import com.holike.crm.R;
import com.holike.crm.base.IntentValue;
import com.holike.crm.bean.BusinessTargetBean;
import com.holike.crm.fragment.report.GeneralReportFragment;

/**
 * Created by pony on 2019/10/28.
 * Version v3.0 app报表
 * 设置目标-经营目标
 */
public class SetBusinessTargetFragment extends GeneralReportFragment<SetBusinessTargetHelper> implements SetBusinessTargetHelper.Callback {

    public static SetBusinessTargetFragment newInstance(BusinessTargetBean bean) {
        IntentValue.getInstance().put("businessTargetBean", bean);
        return new SetBusinessTargetFragment();
    }

    @NonNull
    @Override
    protected SetBusinessTargetHelper newHelper() {
        return new SetBusinessTargetHelper(this, this);
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_set_business_target;
    }

    @Override
    public void onSave(String param, String ids) {
        showLoading();
        mPresenter.setBusinessTarget(param, ids);
    }

    @Override
    public void onSuccess(Object object) {
        super.onSuccess(object);
        showShortToast((String) object);
    }

    @Override
    public void onFailure(String failReason) {
        super.onFailure(failReason);
        showShortToast(failReason);
    }
}
