package com.holike.crm.fragment.report.target;


import androidx.annotation.NonNull;

import com.holike.crm.R;
import com.holike.crm.base.IntentValue;
import com.holike.crm.bean.BusinessObjectivesBean;
import com.holike.crm.fragment.report.GeneralReportFragment;

/**
 * Created by pony on 2019/10/30.
 * Version v3.0 app报表
 * 经营目标
 */
public class BusinessTargetFragment extends GeneralReportFragment<BusinessTargetHelper> {
    
    public static BusinessTargetFragment newInstance(BusinessObjectivesBean bean) {
        IntentValue.getInstance().put("business-objectives-bean", bean);
        return new BusinessTargetFragment();
    }

    @NonNull
    @Override
    protected BusinessTargetHelper newHelper() {
        return new BusinessTargetHelper(this);
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_business_target;
    }
}
