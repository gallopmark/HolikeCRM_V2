package com.holike.crm.presenter.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.text.TextUtils;

import com.grallopmark.tablayout.CommonTabLayout;
import com.grallopmark.tablayout.TabEntity;
import com.grallopmark.tablayout.listener.CustomTabEntity;
import com.grallopmark.tablayout.listener.OnTabSelectListener;
import com.holike.crm.R;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.bean.DistributionStoreBean;
import com.holike.crm.bean.EmployeeDetailBean;
import com.holike.crm.bean.RoleDataBean;
import com.holike.crm.fragment.OnFragmentDataChangedListener;
import com.holike.crm.fragment.employee.EmployeeDetailOneFragment;
import com.holike.crm.fragment.employee.EmployeeEditStepThreeFragment;
import com.holike.crm.fragment.employee.EmployeeEditStepTwoFragment;
import com.holike.crm.model.fragment.EmployeeModel;
import com.holike.crm.util.CheckUtils;
import com.holike.crm.util.Constants;
import com.holike.crm.util.KeyBoardUtil;
import com.holike.crm.util.SharedPreferencesUtils;
import com.holike.crm.view.activity.EmployeeDetailsView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeDetailsPresenter extends BasePresenter<EmployeeDetailsView, EmployeeModel> implements OnTabSelectListener, OnFragmentDataChangedListener {
    private static final String TAG_ONE = "tab-one";
    private static final String TAG_TWO = "tab-two";
    private static final String TAG_THREE = "tab-three";
    private String targetTag = TAG_ONE;
    private Activity activity;
    private FragmentManager fragmentManager;
    private CommonTabLayout mTabLayout;
    private String employeeId;
    private EmployeeDetailBean detailBean;
    private Map<String, Boolean> dataChangedMap = new HashMap<>();
    private int tabSize;

    public void init(Activity activity, FragmentManager fragmentManager, CommonTabLayout mTabLayout) {
        this.activity = activity;
        this.fragmentManager = fragmentManager;
        this.mTabLayout = mTabLayout;
        Bundle bundle = activity.getIntent().getExtras();
        if (bundle != null) {
            employeeId = bundle.getString("employeeId");
        }
        initTab(activity, mTabLayout);
        setCurrentTab(targetTag);
        getEmployeeDetails();
    }

    private void initTab(Activity activity, CommonTabLayout mTabLayout) {
        ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
        mTabEntities.add(new TabEntity(activity.getString(R.string.employee_basic_info), 0, 0));
        if (detailBean != null && !detailBean.isBoss())
            mTabEntities.add(new TabEntity(activity.getString(R.string.employee_related_stores), 0, 0));
        mTabEntities.add(new TabEntity(activity.getString(R.string.employee_setting_permissions), 0, 0));
        tabSize = mTabEntities.size();
        mTabLayout.setTabData(mTabEntities);
        mTabLayout.setOnTabSelectListener(this);
        mTabLayout.setCurrentTab(0);
    }

    @Override
    public void onTabSelect(int position) {
        KeyBoardUtil.hideSoftInput(activity);
        setCurrentTab(position);
    }

    @Override
    public void onTabReselect(int position) {

    }

    public void setCurrentTab(int tab) {
        if (tab == 0) {
            setCurrentTab(TAG_ONE);
            update(TAG_ONE);
        } else {
            if (tabSize == 2) {   //经销商老板隐藏TAB-TWO
                setCurrentTab(TAG_THREE);
            } else {
                if (tab == 1) {
                    setCurrentTab(TAG_TWO);
                    update(TAG_TWO);
                } else if (tab == 2) {
                    setCurrentTab(TAG_THREE);
                    update(TAG_THREE);
                }
            }
        }
    }

    private void setCurrentTab(String tag) {
        if (fragmentManager == null) return;
        targetTag = tag;
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment != null) {
            transaction.show(fragment);
        } else {
            if (TextUtils.equals(tag, TAG_ONE)) {
                EmployeeDetailOneFragment oneFragment = new EmployeeDetailOneFragment();
                Bundle bundle = new Bundle();
                bundle.putString("employeeId", employeeId);
                oneFragment.setArguments(bundle);
                oneFragment.setOnFragmentDataChangedListener(this);
                transaction.add(R.id.mContainer, oneFragment, tag);
            } else if (TextUtils.equals(tag, TAG_TWO)) {
                EmployeeEditStepTwoFragment twoFragment;
                Bundle bundle = new Bundle();
                bundle.putBoolean("isDetail", true);
                if (detailBean != null && detailBean.getShopInfo() != null) {
                    twoFragment = EmployeeEditStepTwoFragment.newInstance(detailBean.getShopInfo());
                    bundle.putBoolean("isBoss", detailBean.isBoss());
                } else {
                    twoFragment = new EmployeeEditStepTwoFragment();
                }
                twoFragment.setOnFragmentDataChangedListener(this);
                twoFragment.setArguments(bundle);
                transaction.add(R.id.mContainer, twoFragment, tag);
            } else {
                EmployeeEditStepThreeFragment threeFragment;
                if (detailBean != null && detailBean.getAuthInfo() != null) {
                    threeFragment = EmployeeEditStepThreeFragment.newInstance(detailBean.getAuthInfo());
                } else {
                    threeFragment = new EmployeeEditStepThreeFragment();
                }
                threeFragment.setOnFragmentDataChangedListener(this);
                transaction.add(R.id.mContainer, threeFragment, tag);
            }
        }
        transaction.commitAllowingStateLoss();
    }

    private void hideFragments(FragmentTransaction transaction) {
        for (Fragment fragment : fragmentManager.getFragments()) {
            transaction.hide(fragment);
        }
    }

    public void getEmployeeDetails() {
        model.getEmployeeDetails(employeeId, new EmployeeModel.OnGetEmployeeCallback() {
            @Override
            public void onGetStart() {
                if (getView() != null) getView().onShowLoading();
            }

            @Override
            public void onGetEmployee(EmployeeDetailBean bean) {
                detailBean = bean;
                onResponse();
                if (getView() != null) {
                    getView().onSuccess(bean);
                    getView().onHideLoading();
                }
            }

            @Override
            public void onFailure(String message) {
                if (getView() != null) {
                    getView().onFailure(message);
                    getView().onHideLoading();
                }
            }

            @Override
            public void onGetComplete() {
                if (getView() != null) getView().onHideLoading();
            }
        });
    }

    private void onResponse() {
        initTab(activity, mTabLayout);
        setCurrentTab(targetTag);
        update(targetTag);
    }

    private void update(String currentTab) {
        Fragment fragment = fragmentManager.findFragmentByTag(currentTab);
        if (fragment == null || detailBean == null) return;
        switch (currentTab) {
            case TAG_ONE:
                if (detailBean.getUserInfo() != null && !((EmployeeDetailOneFragment) fragment).isDataUpdated()) {
                    ((EmployeeDetailOneFragment) fragment).update(detailBean.getUserInfo());
                }
                break;
            case TAG_TWO:
                if (detailBean.getShopInfo() != null && !((EmployeeEditStepTwoFragment) fragment).isDataUpdated()) {
                    ((EmployeeEditStepTwoFragment) fragment).update(detailBean.getShopInfo(), detailBean.isBoss());
                }
                break;
            case TAG_THREE:
                if (detailBean.getAuthInfo() != null && !((EmployeeEditStepThreeFragment) fragment).isDataUpdated()) {
                    ((EmployeeEditStepThreeFragment) fragment).update(detailBean.getAuthInfo());
                }
                break;
        }
    }

    private Map<String, String> getFragmentDataMap() {
        Map<String, String> params = new HashMap<>();
        params.put("crmAccount", SharedPreferencesUtils.getString(Constants.USER_ID, ""));
        params.put("crmPassword", SharedPreferencesUtils.getString(Constants.USER_PSW, ""));
        Fragment oneFragment = fragmentManager.findFragmentByTag(TAG_ONE);
        if (oneFragment != null) {
            for (Map.Entry<String, String> entry : ((EmployeeDetailOneFragment) oneFragment).getDataMap().entrySet()) {
                params.put(entry.getKey(), entry.getValue());
            }
        }
        Fragment twoFragment = fragmentManager.findFragmentByTag(TAG_TWO);
        String shopIds;
        if (twoFragment != null) {
            shopIds = ((EmployeeEditStepTwoFragment) twoFragment).getShopIds();
        } else {
            shopIds = getShopIds();
        }
        params.put("shopIds", shopIds);
        Fragment threeFragment = fragmentManager.findFragmentByTag(TAG_THREE);
        String authIds;
        if (threeFragment != null) {
            authIds = ((EmployeeEditStepThreeFragment) threeFragment).getAuthIds();
        } else {
            authIds = getAuthIds();
        }
        params.put("authIds", authIds);
        return params;
    }

    private String getShopIds() {
        StringBuilder stringBuilder = new StringBuilder();
        if (detailBean != null && detailBean.getShopInfo() != null) {
            for (DistributionStoreBean storeBean : detailBean.getShopInfo()) {
                if (storeBean.getIsSelect() == 1) {
                    stringBuilder.append(storeBean.getShopId());
                    stringBuilder.append(",");
                }
            }
            if (stringBuilder.lastIndexOf(",") >= 0) {
                stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(","));
            }
        }
        return stringBuilder.toString();
    }

    private String getAuthIds() {
        StringBuilder stringBuilder = new StringBuilder();
        if (detailBean != null && detailBean.getAuthInfo() != null) {
            for (RoleDataBean.AuthInfoBean infoBean : detailBean.getAuthInfo()) {
                List<RoleDataBean.AuthInfoBean.PArrBean> arrBeans = infoBean.getAuthData();
                if (arrBeans != null && !arrBeans.isEmpty()) {
                    for (RoleDataBean.AuthInfoBean.PArrBean arrBean : arrBeans) {
                        if (arrBean.getIsSelect() == 1) {
                            stringBuilder.append(arrBean.actionId);
                            stringBuilder.append(",");
                        }
                    }
                }
            }
            if (stringBuilder.lastIndexOf(",") >= 0) {
                stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(","));
            }
        }
        return stringBuilder.toString();
    }

    /*发起请求，保存员工信息*/
    public void saveEmployee(Context context) {
        Map<String, String> params = getFragmentDataMap();
        String phone = params.get("phone");
        String errorMessage = "";
        if (TextUtils.isEmpty(params.get("userName"))) { //用户名为必填项
            errorMessage = context.getString(R.string.employee_name_hint);
        } else if (TextUtils.isEmpty(phone)) {   //手机号为必填项
            errorMessage = context.getString(R.string.employee_phone_hint);
        } else if (!CheckUtils.isMobile(phone)) {  //手机号码以1开始11位数
            errorMessage = context.getString(R.string.please_input_correct_phone);
        }
        if (!TextUtils.isEmpty(errorMessage)) {
            if (getView() != null)
                getView().onSaveFailure(errorMessage);
            if (!TextUtils.equals(targetTag, TAG_ONE)) {      //如果不是“基础信息”tab则切换到第一个
                mTabLayout.setCurrentTab(0);
                onTabSelect(0);
            }
            return;
        }
        boolean isBoss = detailBean != null && detailBean.isBoss();
        String shopIds = params.get("shopIds");
        if (!isBoss && TextUtils.isEmpty(shopIds)) {   //非经销商老板至少选择一家门店
            if (getView() != null)
                getView().onSaveFailure(context.getString(R.string.employee_empty_shop_tips));
            if (!TextUtils.equals(targetTag, TAG_TWO)) {    //如果不是“关联门店”tab则切换到第二个
                mTabLayout.setCurrentTab(1);
                onTabSelect(1);
            }
            return;
        }
        String authIds = params.get("authIds");
        if (isBoss && TextUtils.isEmpty(authIds)) {     //经销商老板必须勾选一个权限
            if (getView() != null)
                getView().onSaveFailure(context.getString(R.string.employee_empty_auth_tips));
            if (!TextUtils.equals(targetTag, TAG_THREE)) {  //如果不是“设置权限”tab则切换到第三个
                mTabLayout.setCurrentTab(2);
                onTabSelect(2);
            }
            return;
        }
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

    @Override
    public void onFragmentDataChanged(boolean isChanged) {
        dataChangedMap.put(targetTag, isChanged);
        boolean isDataChanged = false;
        for (Map.Entry<String, Boolean> entry : dataChangedMap.entrySet()) {
            if (entry.getValue() != null && entry.getValue()) {
                isDataChanged = true;
                break;
            }
        }
        if (getView() != null) {
            getView().onDataChanged(isDataChanged);
        }
    }
}
