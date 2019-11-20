package com.holike.crm.fragment.employee;

import androidx.fragment.app.Fragment;

import com.holike.crm.base.IntentValue;
import com.holike.crm.bean.EmployeeRankingBean;
import com.holike.crm.fragment.employee.child.InternalOrderFragment;
import com.holike.crm.fragment.employee.child.InternalPaybackFragment;
import com.holike.crm.fragment.employee.child.InternalSigningFragment;
import com.holike.crm.fragment.employee.child.InternalTransactionFragment;


/**
 * Created by pony on 2019/10/24.
 * Copyright holike possess 2019.
 * 员工排行-内部人员（营销人员）
 */
public class EmployeeRankInternalFragment extends EmployeeRankFragment {

    public static EmployeeRankInternalFragment newInstance(EmployeeRankingBean bean) {
        IntentValue.getInstance().put("employeeRankBean", bean);
        return new EmployeeRankInternalFragment();
    }

    @Override
    Fragment getTagOne() {
        return new InternalSigningFragment();
    }

    @Override
    Fragment getTagTwo() {
        return new InternalTransactionFragment();
    }

    @Override
    Fragment getTagThree() {
        return new InternalPaybackFragment();
    }

    @Override
    Fragment getTagFour() {
        return new InternalOrderFragment();
    }
}
