package com.holike.crm.activity.employee;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.holike.crm.R;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.dialog.MaterialDialog;
import com.holike.crm.fragment.employee.EmployeePermissionFragment;
import com.holike.crm.fragment.employee.EmployeeStoreFragment;

import java.util.Map;


/**
 * Created by gallop on 2019/8/8.
 * Copyright holike possess 2019.
 */
class EmployeeStrikeHelper {
    private BaseActivity<?, ?> mActivity;
    private FragmentManager mFragmentManager;
    private String mUserId;
    private boolean mIsBoss; //是否是老板
    private Callback mCallback;

    EmployeeStrikeHelper(BaseActivity<?, ?> activity, Callback callback) {
        this.mActivity = activity;
        mFragmentManager = mActivity.getSupportFragmentManager();
        mUserId = mActivity.getIntent().getStringExtra("userId");
        mIsBoss = mActivity.getIntent().getBooleanExtra("isBoss", false);
        this.mCallback = callback;
    }

    void toShop() {
        mFragmentManager.beginTransaction()
                .replace(R.id.container, EmployeeStoreFragment.newInstance(EmployeeStrikeActivity.SHOP_INFO, mIsBoss), "tag-shop")
                .commitAllowingStateLoss();
    }

    void toRole() {
        mFragmentManager.beginTransaction()
                .replace(R.id.container, EmployeePermissionFragment.newInstance(EmployeeStrikeActivity.ROLE_INFO,
                        EmployeeStrikeActivity.AUTH_INFO), "tag-role")
                .commitAllowingStateLoss();
    }

    @Nullable
    private EmployeeStoreFragment getStoreFragment() {
        Fragment fragment = mFragmentManager.findFragmentByTag("tag-shop");
        if (fragment == null)
            return null;
        return (EmployeeStoreFragment) fragment;
    }

    @Nullable
    private EmployeePermissionFragment getPermissionFragment() {
        Fragment fragment = mFragmentManager.findFragmentByTag("tag-role");
        if (fragment == null) {
            return null;
        }
        return (EmployeePermissionFragment) fragment;
    }

    void onBackPressed() {
        EmployeeStoreFragment storeFragment = getStoreFragment();
        if (storeFragment != null) {
            if (storeFragment.isDataChanged()) {
                showTipsDialog();
            } else {
                mActivity.finish();
            }
        } else {
            EmployeePermissionFragment permissionFragment = getPermissionFragment();
            if (permissionFragment != null) {
                if (permissionFragment.isDataChanged()) {
                    showTipsDialog();
                } else {
                    mActivity.finish();
                }
            } else {
                mActivity.finish();
            }
        }
    }

    private void showTipsDialog() {
        new MaterialDialog.Builder(mActivity)
                .message(R.string.employee_edit_not_saved_tips)
                .positiveButton(R.string.cancel, (dialogInterface, i) -> dialogInterface.dismiss())
                .negativeButton(R.string.confirm, (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    mActivity.finish();
                }).show();
    }

    void onSave() {
        Map<String, String> params;
        EmployeeStoreFragment storeFragment = getStoreFragment();
        if (storeFragment != null && storeFragment.isSelected()) {
            params = storeFragment.obtain();
            params.put("userId", mUserId); //修改需要传递userId
            mCallback.onSave(2, params);
        } else {
            EmployeePermissionFragment permissionFragment = getPermissionFragment();
            if (permissionFragment != null && permissionFragment.isSelectRoles()) {
                params = permissionFragment.obtain();
                params.put("userId", mUserId); //修改需要传递userId
                mCallback.onSave(3, params);
            }
        }
    }

    interface Callback {
        void onSave(int type, Map<String, String> params);
    }
}
