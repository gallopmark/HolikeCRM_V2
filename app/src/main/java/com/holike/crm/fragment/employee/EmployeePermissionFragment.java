package com.holike.crm.fragment.employee;

import android.os.Bundle;

import com.gallopmark.recycler.widgetwrapper.WrapperRecyclerView;
import com.holike.crm.R;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.base.IntentValue;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.RoleDataBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by gallop on 2019/8/6.
 * Copyright holike possess 2019.
 * 员工权限
 */
public class EmployeePermissionFragment extends MyFragment {

    @BindView(R.id.recyclerView)
    WrapperRecyclerView mRecyclerView;
    private EmployeePermissionHelper mHelper;

    public static EmployeePermissionFragment newInstance(String roleKey, String authKey) {
        EmployeePermissionFragment fragment = new EmployeePermissionFragment();
        Bundle bundle = new Bundle();
        bundle.putString("roleKey", roleKey);
        bundle.putString("authKey", authKey);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected BasePresenter attachPresenter() {
        return null;
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_employee_edit_permission2;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void init() {
        Bundle bundle = getArguments();
        List<RoleDataBean> roleList = null;
        List<RoleDataBean.AuthInfoBean> authList = null;
        if (bundle != null) {
            roleList = (List<RoleDataBean>) IntentValue.getInstance().get(bundle.getString("roleKey"));
            authList = (List<RoleDataBean.AuthInfoBean>) IntentValue.getInstance().get(bundle.getString("authKey"));
        }
        mHelper = new EmployeePermissionHelper(mContext, roleList, authList);
        mHelper.attach(mRecyclerView);
    }

    /*选择是否发生了改变*/
    public boolean isDataChanged() {
        return mHelper.isDataChanged();
    }

    /*是否勾选了角色*/
    public boolean isSelectRoles() {
        if (!mHelper.isSelectRoles()) {
            showShortToast(R.string.employee_role_unselected_tips);
            return false;
        }
        return true;
    }

    public Map<String, String> obtain() {
        Map<String, String> params = new HashMap<>();
        params.put("stationId", mHelper.getStationIds());
        params.put("authIds", mHelper.getAuthIds());
        return params;
    }
}
