package com.holike.crm.activity.employee2;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.holike.crm.R;
import com.holike.crm.dialog.MaterialDialog;
import com.holike.crm.fragment.employee2.EmployeeBasicFragment;
import com.holike.crm.fragment.employee2.EmployeePermissionFragment;
import com.holike.crm.fragment.employee2.EmployeeStoreFragment;
import com.holike.crm.util.KeyBoardUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gallop on 2019/8/6.
 * Copyright holike possess 2019.
 * 新增员工帮助类
 */
class EmployeeEditHelper {
    private EmployeeEditV2Activity mActivity;
    private FragmentManager mFragmentManager;
    private static final String TAG_ONE = "tag-one";
    private static final String TAG_TWO = "tag-two";
    private static final String TAG_THREE = "tag-three";
    private String mCurrentTab;
    private Fragment mShowingFragment; //正在显示的fragment
    private Callback mCallback;
    private boolean isGoNext;

    EmployeeEditHelper(EmployeeEditV2Activity activity, Callback callback) {
        this.mActivity = activity;
        this.mCallback = callback;
        mFragmentManager = activity.getSupportFragmentManager();
        setCurrentTab(TAG_ONE);
    }

    void setCurrentTab(String tag) {
        mCurrentTab = tag;
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (isGoNext && !TextUtils.equals(mCurrentTab, TAG_ONE)) { //fragment右侧滑入
            transaction.setCustomAnimations(R.anim.fragment_from_right, 0);
        }
        if (mShowingFragment != null) {
            transaction.hide(mShowingFragment);
        }
        Fragment fragment = mFragmentManager.findFragmentByTag(tag);
        if (fragment != null) {
            transaction.show(fragment);
        } else {
            if (TextUtils.equals(tag, TAG_ONE)) {
                fragment = new EmployeeBasicFragment();
                transaction.add(R.id.container, fragment, TAG_ONE);
            } else if (TextUtils.equals(tag, TAG_TWO)) {
                fragment = new EmployeeStoreFragment();
                transaction.add(R.id.container, fragment, TAG_TWO);
            } else {
                fragment = new EmployeePermissionFragment();
                transaction.add(R.id.container, fragment, TAG_THREE);
            }
        }
        mShowingFragment = fragment;
        int position;
        if (TextUtils.equals(mCurrentTab, TAG_ONE)) {
            position = 0;
        } else if (TextUtils.equals(mCurrentTab, TAG_TWO)) {
            position = 1;
        } else {
            position = 2;
        }
        mCallback.onTabChanged(position);
        transaction.commitAllowingStateLoss();
    }

    public void onViewClicked(View view) {
        KeyBoardUtil.hideSoftInput(mActivity);
        switch (view.getId()) {
            case R.id.tv_previous_step:
                previous();
                break;
            case R.id.tv_next_step:
                if (TextUtils.equals(((TextView) view).getText(), mActivity.getString(R.string.employee_save))) {  //点击保存
                    onSave();
                } else {
                    next();
                }
                break;
        }
    }

    private void previous() {
        if (TextUtils.equals(mCurrentTab, TAG_THREE)) {
            isGoNext = false;
            setCurrentTab(TAG_TWO);
        } else if (TextUtils.equals(mCurrentTab, TAG_TWO)) {
            isGoNext = false;
            setCurrentTab(TAG_ONE);
        }
        ((TextView) mActivity.findViewById(R.id.tv_next_step)).setText(mActivity.getString(R.string.next_step));
    }

    /*下一步*/
    private void next() {
        if (TextUtils.equals(mCurrentTab, TAG_ONE)) {  //当前是基础信息fragment
            Fragment fragment = mFragmentManager.findFragmentByTag(TAG_ONE);
            if (fragment != null && ((EmployeeBasicFragment) fragment).canGoNext()) {
                isGoNext = true;
                setCurrentTab(TAG_TWO);
                setPrevious();
            }
        } else {
            Fragment fragment = mFragmentManager.findFragmentByTag(TAG_TWO);
            if (fragment != null && ((EmployeeStoreFragment) fragment).isSelected()) {
                isGoNext = true;
                setCurrentTab(TAG_THREE);
                setPrevious();
                setSave();
            }
        }
    }

    private void setSave() {
        ((TextView) mActivity.findViewById(R.id.tv_next_step)).setText(mActivity.getString(R.string.employee_save));
    }

    private void setPrevious() {
        mActivity.findViewById(R.id.tv_previous_step).setVisibility(View.VISIBLE);
    }

    private void onSave() {
        Fragment permissionFragment = mFragmentManager.findFragmentByTag(TAG_THREE);
        if (permissionFragment != null && ((EmployeePermissionFragment) permissionFragment).isSelectRoles()) {
            Fragment basicFragment = mFragmentManager.findFragmentByTag(TAG_ONE);
            Map<String, String> params = new HashMap<>();
            if (basicFragment != null) {
                params.putAll(((EmployeeBasicFragment) basicFragment).obtain());
            }
            Fragment storeFragment = mFragmentManager.findFragmentByTag(TAG_TWO);
            if (storeFragment != null) {
                params.putAll(((EmployeeStoreFragment) storeFragment).obtain());
            }
            params.putAll((((EmployeePermissionFragment) permissionFragment).obtain()));
            params.put("type", "-1"); //新增传-1
            for (Map.Entry<String, String> entry : params.entrySet()) {
                System.out.println(entry.getKey() + "->" + entry.getValue());
            }
            mCallback.onSave(params);
        }
    }

    interface Callback {
        void onTabChanged(int position);

        void onSave(Map<String, String> params);
    }

    void onBackPressed() {
        /*到了第三步*/
        if (mFragmentManager.findFragmentByTag(TAG_THREE) != null) {
            new MaterialDialog.Builder(mActivity)
                    .message(R.string.employee_edit_not_saved_tips)
                    .positiveButton(R.string.cancel, (dialogInterface, i) -> dialogInterface.dismiss())
                    .negativeButton(R.string.confirm, (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                        mActivity.finish();
                    }).show();
        } else {
            mActivity.finish();
        }
    }
}
