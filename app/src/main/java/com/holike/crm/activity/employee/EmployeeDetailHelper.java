package com.holike.crm.activity.employee;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.holike.crm.R;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.base.IntentValue;
import com.holike.crm.bean.DistributionStoreBean;
import com.holike.crm.bean.EmployeeDetailV2Bean;
import com.holike.crm.bean.EmployeeEditResultBean;
import com.holike.crm.bean.RoleDataBean;
import com.holike.crm.fragment.employee.EmployeeBasicDetailsFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pony on 2019/8/7.
 * Copyright holike possess 2019.
 */
class EmployeeDetailHelper implements EmployeeBasicDetailsFragment.OnViewClickListener {
    private BaseActivity<?, ?> mActivity;

    private FragmentManager mFragmentManager;
    private Callback mCallback;
    private String mUserId;
    private static final String TAG = "tag-detail";
    private EmployeeDetailV2Bean mDetailBean;

    EmployeeDetailHelper(BaseActivity<?, ?> activity, Callback callback) {
        this.mActivity = activity;
        mFragmentManager = mActivity.getSupportFragmentManager();
        this.mCallback = callback;
        Bundle bundle = mActivity.getIntent().getExtras();
        if (bundle != null) {
            mUserId = bundle.getString("userId");
        }
        onQuery();
    }

    void onQuery() {
        mCallback.onQuery(mUserId);
    }

    void onSuccess(EmployeeDetailV2Bean bean) {
        mDetailBean = bean;
        startFragment();
    }

    void onActivityResult(EmployeeEditResultBean resultBean) {
        if (resultBean != null) {
            EmployeeEditResultBean.ResultBean bean = resultBean.getResult();
            if (bean != null) {
                if (!bean.getShopArr().isEmpty()) {
                    mDetailBean.setShopInfo(bean.getShopArr());
                }
                if (!bean.getRoleArr().isEmpty()) {
                    mDetailBean.setRoleData(bean.getRoleArr());
                }
                if (!bean.getAuthInfo().isEmpty()) {
                    mDetailBean.setAuthInfo(bean.getAuthInfo());
                }
                Fragment fragment = mFragmentManager.findFragmentByTag(TAG);
                if (fragment != null) {
                    EmployeeBasicDetailsFragment detailsFragment = (EmployeeBasicDetailsFragment) fragment;
                    detailsFragment.setRelatedInfo(spliceShop(), spliceRole());
                }
            }
        }
        IntentValue.getInstance().removeEmployeeEditResult();
    }

    private void startFragment() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        Fragment fragment = mFragmentManager.findFragmentByTag(TAG);
        if (fragment != null) {
            transaction.show(fragment);
        } else {
            Bundle bundle = new Bundle();
            bundle.putSerializable("userInfo", mDetailBean.getUserInfo());
            bundle.putString("shopInfo", spliceShop());
            bundle.putString("roleInfo", spliceRole());
            fragment = new EmployeeBasicDetailsFragment();
            fragment.setArguments(bundle);
            ((EmployeeBasicDetailsFragment) fragment).setOnViewClickListener(this);
            transaction.add(R.id.container, fragment, TAG);
        }
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void onResetPassword() {
        mActivity.startActivity(EmployeeResetPwActivity.class, mActivity.getIntent().getExtras());
    }

    /*关联门店*/
    @Override
    public void onAssociateShop() {
        EmployeeStrikeActivity.open(mActivity, mDetailBean.getShopInfo(), mUserId, mDetailBean.isBoss());
    }

    /*选择角色、权限*/
    @Override
    public void onSelectRole() {
        EmployeeStrikeActivity.open(mActivity, mDetailBean.getRoleData(), mDetailBean.getAuthInfo(), mUserId);
    }

    /*已选门店、组织拼接(1.当门店内无分组，勾选后显示店名，如XXX店；2.当门店内有分组，
    勾选了该门店内的全部分组，仅显示店名，如XXX店；当门店内有分组，勾选了部分分组，显示店名-小组名，如XXX店-XX组)*/
    private String spliceShop() {
        List<DistributionStoreBean> shopList = mDetailBean.getShopInfo();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < shopList.size(); i++) {
            DistributionStoreBean bean = shopList.get(i);
            if (bean.isSelected()) {
                if (!bean.isGroupSelectAll()) {
                    for (int j = 0; j < bean.getGroupList().size(); j++) {
                        DistributionStoreBean.Group group = bean.getGroupList().get(j);
                        if (group.isSelected()) {
                            sb.append(bean.shopName);
                            sb.append("-");
                            sb.append(group.groupName);
                            sb.append("、");
                        }
                    }
                } else {
                    sb.append(bean.shopName);
                    sb.append("、");
                }
            }
        }
        int index = sb.lastIndexOf("、");
        if (index > 0) {
            sb.deleteCharAt(index);
        }
        return sb.toString();
    }

    /*角色分割*/
    private String spliceRole() {
        List<RoleDataBean> list = mDetailBean.getRoleData();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            RoleDataBean bean = list.get(i);
            if (bean.isSelected()) {
                sb.append(list.get(i).roleName);
                sb.append("、");
            }
        }
        int index = sb.lastIndexOf("、");
        if (index > 0) {
            sb.deleteCharAt(index);
        }
        return sb.toString();
    }

    void onSave() {
        Fragment fragment = mFragmentManager.findFragmentByTag(TAG);
        if (fragment != null) {
            EmployeeBasicDetailsFragment detailsFragment = (EmployeeBasicDetailsFragment) fragment;
            if (detailsFragment.canGoNext()) {
                Map<String, String> params = new HashMap<>();
                params.put("userId", mUserId);
                params.putAll(detailsFragment.obtain());
                mCallback.onSave(params);
            }
        }
    }

    interface Callback {
        void onQuery(String userId);

        void onSave(Map<String, String> params);
    }
}
