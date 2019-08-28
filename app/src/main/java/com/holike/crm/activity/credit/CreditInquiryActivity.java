package com.holike.crm.activity.credit;

import android.os.Bundle;
import android.text.TextUtils;

import com.holike.crm.R;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.base.IntentValue;
import com.holike.crm.base.MyFragmentActivity;
import com.holike.crm.fragment.bank.BillListFragment;
import com.holike.crm.fragment.bank.CreditInquiryFragment;
import com.holike.crm.fragment.bank.OnlineDeclarationFragment;
import com.holike.crm.fragment.bank.PayListFragment;
import com.holike.crm.fragment.employee.EmployeeListFragment;
import com.holike.crm.util.Constants;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CreditInquiryActivity extends MyFragmentActivity {

    @Override
    protected BasePresenter attachPresenter() {
        return null;
    }

    @Override
    protected int setContentViewId() {
        return R.layout.activity_fragment_depend;
    }

    @Override
    protected void init() {
        super.init();
        Bundle bundle = getIntent().getExtras();
        Map<String, Serializable> params = new HashMap<>();
        if (bundle != null) {
            String type = bundle.getString(Constants.CREDIT_TYPE);
            if (TextUtils.equals(type, "1")) {
                startFragment(new CreditInquiryFragment());
            } else if (TextUtils.equals(type, "2")) {
                params.put(Constants.PAGE_TYPE, "账单列表");
                startFragment(params, new BillListFragment(), false);
            } else if (TextUtils.equals(type, "3")) {
                startFragment(new OnlineDeclarationFragment());
            } else if (TextUtils.equals(type, "4")) {
                startFragment(params, new PayListFragment(), false);
            } else if (TextUtils.equals(type, "5")) {
                startFragment(new EmployeeListFragment());
            }
//            switch (type) {
//                case "1":
//                    startFragment(new CreditInquiryFragment());
//                    break;
//                case "2":
//                    params.put(Constants.PAGE_TYPE, "账单列表");
//                    startFragment(params, new BillListFragment(), false);
//                    break;
//                case "3":
//                    startFragment(new OnlineDeclarationFragment());
//                    break;
//                case "4":
//                    if (bundle.getSerializable(Constants.TYPE_LIST) != null) {
//                        HomepageBean.TypeListBean typeListBean = (HomepageBean.TypeListBean) bundle.getSerializable(Constants.TYPE_LIST);
//                        params.put(Constants.TYPE_LIST, typeListBean);
//                    }
//                    startFragment(params, new PayListFragment(), false);
//                    break;
//            }
        }
    }
}
