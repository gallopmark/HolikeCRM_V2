package com.holike.crm.presenter.activity;


import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;

import com.holike.crm.R;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.bean.DistributionStoreBean;
import com.holike.crm.fragment.employee.EmployeeEditStepOneFragment;
import com.holike.crm.fragment.employee.EmployeeEditStepThreeFragment;
import com.holike.crm.fragment.employee.EmployeeEditStepTwoFragment;
import com.holike.crm.model.fragment.EmployeeModel;
import com.holike.crm.util.Constants;
import com.holike.crm.util.SharedPreferencesUtils;
import com.holike.crm.view.activity.EmployeeEditView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeEditPresenter extends BasePresenter<EmployeeEditView, EmployeeModel> implements EmployeeEditStepOneFragment.OnFillListener, EmployeeEditStepTwoFragment.OnShopSelectListener {

    private Context context;
    private FragmentManager fragmentManager;
    private static final String TAG_ONE = "step-one";
    private static final String TAG_TWO = "step-two";
    private static final String TAG_THREE = "step-three";
    private String targetTag = TAG_ONE;
    private boolean isStepOneEnable, isPasswordCorrect, isStepTwoEnable;

    public void init(Context context, FragmentManager fragmentManager) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        setCurrentTab(targetTag);
    }

    /*上一步*/
    public void onPreviousStep() {
        if (TextUtils.equals(targetTag, TAG_TWO)) {
            setCurrentTab(TAG_ONE);
        } else {
            setCurrentTab(TAG_TWO);
        }
    }

    /*下一步*/
    public void onNextStep() {
        if (TextUtils.equals(targetTag, TAG_ONE)) {
            if (!isPasswordCorrect) {
                if (getView() != null) getView().onSaveFailure(context.getString(R.string.reset_password_error_tips));
            } else {
                setCurrentTab(TAG_TWO);
            }
        } else if (TextUtils.equals(targetTag, TAG_TWO)) {
            setCurrentTab(TAG_THREE);
        }
    }

    public void setCurrentTab(String tag) {
        if (fragmentManager == null) return;
        targetTag = tag;
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        int currentTab = covertTag(tag);
        if (fragment != null) {
            transaction.show(fragment);
        } else {
            if (currentTab == 1) {
                EmployeeEditStepOneFragment oneFragment = new EmployeeEditStepOneFragment();
                oneFragment.setOnFillListener(this);
                transaction.add(R.id.mContainer, oneFragment, tag);
            } else if (currentTab == 2) {
                EmployeeEditStepTwoFragment twoFragment = new EmployeeEditStepTwoFragment();
                twoFragment.setOnShopSelectListener(this);
                transaction.add(R.id.mContainer, twoFragment, tag);
            } else {
                transaction.add(R.id.mContainer, new EmployeeEditStepThreeFragment(), tag);
            }
        }
        transaction.commitAllowingStateLoss();
        if (getView() != null) getView().onTabChanged(currentTab);
    }

    private int covertTag(String tag) {
        if (TextUtils.equals(tag, TAG_ONE)) {
            if (getView() != null)
                getView().onContentFill(isStepOneEnable);
            return 1;
        } else if (TextUtils.equals(tag, TAG_TWO)) {
            if (getView() != null)
                getView().onContentFill(isStepTwoEnable);
            return 2;
        } else {
            return 3;
        }
    }

    private void hideFragments(FragmentTransaction transaction) {
        for (Fragment fragment : fragmentManager.getFragments()) {
            transaction.hide(fragment);
        }
    }

    @Override
    public void onFill(boolean isFill, boolean isPasswordCorrect) {
        isStepOneEnable = isFill;
        this.isPasswordCorrect = isPasswordCorrect;
        if (getView() != null)
            getView().onContentFill(isStepOneEnable);
    }

    @Override
    public void onShopSelected(List<DistributionStoreBean> mSelected) {
        isStepTwoEnable = !mSelected.isEmpty();
        if (getView() != null)
            getView().onContentFill(isStepTwoEnable);
    }

    public void setSelected(boolean isSelected, View... views) {
        for (View view : views) {
            view.setSelected(isSelected);
        }
    }

    /*点击保存 获取fragment中参数*/
    private Map<String, String> getFragmentParams() {
        Map<String, String> params = new HashMap<>();
        params.put("crmAccount", SharedPreferencesUtils.getString(Constants.USER_ID, ""));
        params.put("crmPassword", SharedPreferencesUtils.getString(Constants.USER_PSW, ""));
        Fragment oneFragment = fragmentManager.findFragmentByTag(TAG_ONE);
        if (oneFragment != null) {
            for (Map.Entry<String, String> entry : ((EmployeeEditStepOneFragment) oneFragment).getDataMap().entrySet()) {
                params.put(entry.getKey(), entry.getValue());
            }
        }
        Fragment twoFragment = fragmentManager.findFragmentByTag(TAG_TWO);
        if (twoFragment != null) {
            String shopIds = ((EmployeeEditStepTwoFragment) twoFragment).getShopIds();
            params.put("shopIds", shopIds);
        }
        Fragment threeFragment = fragmentManager.findFragmentByTag(TAG_THREE);
        if (threeFragment != null) {
            String authIds = ((EmployeeEditStepThreeFragment) threeFragment).getAuthIds();
            params.put("authIds", authIds);
        }
        return params;
    }

    /*发起请求，保存员工信息*/
    public void saveEmployee() {
        Map<String, String> params = getFragmentParams();
        model.saveEmployee(params, new EmployeeModel.OnSaveEmployeeCallback() {
            @Override
            public void onSaveStart() {
                if (getView() != null) getView().onShowLoading();
            }

            @Override
            public void onSaveSuccess() {
                if (getView() != null) getView().onSaveSuccess();
            }

            @Override
            public void onSaveFailure(String message) {
                if (getView() != null) getView().onSaveFailure(message);
            }

            @Override
            public void onSaveComplete() {
                if (getView() != null) getView().onHideLoading();
            }
        });
    }

}
