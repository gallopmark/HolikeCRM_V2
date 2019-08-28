package com.holike.crm.fragment.employee2;

import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;

import com.holike.crm.R;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.base.IntentValue;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.DistributionStoreBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by gallop on 2019/8/6.
 * Copyright holike possess 2019.
 * 员工所在组织
 */
public class EmployeeStoreFragment extends MyFragment {
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private EmployeeStoreHelper mHelper;

    public static EmployeeStoreFragment newInstance(String intentKey, boolean isBoss) {
        EmployeeStoreFragment fragment = new EmployeeStoreFragment();
        Bundle bundle = new Bundle();
        bundle.putString("key", intentKey);
        bundle.putBoolean("isBoss", isBoss);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected BasePresenter attachPresenter() {
        return null;
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_employee_edit_store2;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void init() {
        Bundle bundle = getArguments();
        List<DistributionStoreBean> list = null;
        boolean isBoss = false;
        if (bundle != null) {
            isBoss = bundle.getBoolean("isBoss", false);
            String key = bundle.getString("key");
            list = (List<DistributionStoreBean>) IntentValue.getInstance().get(key);
        }
        mHelper = new EmployeeStoreHelper(mContext, list, isBoss);
        mHelper.attach(mRecyclerView);
    }

    public boolean isDataChanged() {
        return mHelper.isDataChanged();
    }

    /*是否选择了门店 至少选择一个*/
    public boolean isSelected() {
        if (!mHelper.isSelected()) {
            showShortToast(R.string.employee_shop_unselected_tips);
            return false;
        } else {
            return true;
        }
    }

    public Map<String, String> obtain() {
        Map<String, String> params = new HashMap<>();
        params.put("shopIds", mHelper.getShopIds());
        params.put("groupIds", mHelper.getGroupIds());
        return params;
    }
}
