package com.holike.crm.fragment.employee;

import androidx.fragment.app.Fragment;

import com.grallopmark.tablayout.TabEntity;
import com.grallopmark.tablayout.listener.CustomTabEntity;
import com.holike.crm.R;
import com.holike.crm.base.IntentValue;
import com.holike.crm.bean.EmployeeRankingBean;
import com.holike.crm.fragment.employee.child.DealerOrderFragment;
import com.holike.crm.fragment.employee.child.DealerPaybackFragment;
import com.holike.crm.fragment.employee.child.DealerSigningFragment;
import com.holike.crm.fragment.employee.child.DealerTransactionFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pony on 2019/10/23.
 * Copyright holike possess 2019.
 * 员工排行榜-经销商
 */
public class EmployeeRankDealerFragment extends EmployeeRankFragment {

    public static EmployeeRankDealerFragment newInstance(EmployeeRankingBean bean) {
        IntentValue.getInstance().put("employeeRankBean", bean);
        return new EmployeeRankDealerFragment();
    }

    @Override
    Fragment getTagOne() {
        return new DealerSigningFragment();
    }

    @Override
    Fragment getTagTwo() {
        return new DealerTransactionFragment();
    }

    @Override
    Fragment getTagThree() {
        return new DealerPaybackFragment();
    }

    @Override
    Fragment getTagFour() {
        return new DealerOrderFragment();
    }
}
