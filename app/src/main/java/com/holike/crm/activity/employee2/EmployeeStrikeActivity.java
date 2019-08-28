package com.holike.crm.activity.employee2;

import android.content.Intent;
import android.text.TextUtils;


import androidx.annotation.Nullable;

import com.holike.crm.R;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.base.IntentValue;
import com.holike.crm.base.MyFragmentActivity;
import com.holike.crm.bean.DistributionStoreBean;
import com.holike.crm.bean.EmployeeEditResultBean;
import com.holike.crm.bean.RoleDataBean;
import com.holike.crm.presenter.activity.EmployeeEditV2Presenter;
import com.holike.crm.view.activity.EmployeeEditV2View;

import java.util.List;
import java.util.Map;

import butterknife.OnClick;

/**
 * Created by gallop on 2019/8/8.
 * Copyright holike possess 2019.
 */
public class EmployeeStrikeActivity extends MyFragmentActivity<EmployeeEditV2Presenter, EmployeeEditV2View>
        implements EmployeeStrikeHelper.Callback, EmployeeEditV2View {
    public static final String TYPE_SHOP = "shop";
    public static final String TYPE_PERMISSION = "permission";
    public static final String SHOP_INFO = "intent_shopInfo";   //门店组织信息
    public static final String ROLE_INFO = "intent-roleInfo";   //角色信息
    public static final String AUTH_INFO = "intent-authInfo";   //权限信息

    public static void open(BaseActivity<?, ?> activity, List<DistributionStoreBean> shopInfo,
                            String userId, boolean isBoss) {
        IntentValue.getInstance().put(SHOP_INFO, shopInfo);
        Intent intent = new Intent(activity, EmployeeStrikeActivity.class);
        intent.setType(TYPE_SHOP);
        intent.putExtra("userId", userId);
        intent.putExtra("isBoss", isBoss);
        activity.openActivity(intent);
    }

    public static void open(BaseActivity<?, ?> activity, List<RoleDataBean> roleInfo, List<RoleDataBean.AuthInfoBean> authInfo, String userId) {
        IntentValue.getInstance().put(ROLE_INFO, roleInfo);
        IntentValue.getInstance().put(AUTH_INFO, authInfo);
        Intent intent = new Intent(activity, EmployeeStrikeActivity.class);
        intent.setType(TYPE_PERMISSION);
        intent.putExtra("userId", userId);
        activity.openActivity(intent);
    }

    //    public static void open(BaseActivity<?, ?> activity, List<DistributionStoreBean> shopInfo) {
//        mShopInfo = shopInfo;
//        Intent intent = new Intent(activity,EmployeeStrikeActivity.class);
//        intent.setType(TYPE_SHOP);
//        activity.openActivity(intent);
//    }
    private EmployeeStrikeHelper mHelper;

    @Override
    protected EmployeeEditV2Presenter attachPresenter() {
        return new EmployeeEditV2Presenter();
    }

    @Override
    protected int setContentViewId() {
        return R.layout.activity_employee_strike;
    }

    @Override
    protected void init() {
        mHelper = new EmployeeStrikeHelper(this, this);
        String type = getIntent().getType();
        if (TextUtils.equals(type, TYPE_SHOP)) {
            setTitle(getString(R.string.employee_associate_shop));
            mHelper.toShop();
        } else {
            setTitle(getString(R.string.employee_role_permission));
            mHelper.toRole();
        }
    }

    @Override
    protected void onDestroy() {
        IntentValue.getInstance().remove(SHOP_INFO);
        IntentValue.getInstance().remove(ROLE_INFO);
        IntentValue.getInstance().remove(AUTH_INFO);
        super.onDestroy();
    }

    @OnClick(R.id.tvSave)
    public void onViewClicked() {
        mHelper.onSave();
    }

    @Override
    public void onBackPressed() {
        mHelper.onBackPressed();
    }

    @Override
    public void onSave(int type, Map<String, String> params) {
        showLoading();
        mPresenter.saveEmployee(type, params);
    }

    @Override
    public void onSaveSuccess(@Nullable EmployeeEditResultBean resultBean) {
        dismissLoading();
//        showShortToast(message);
        IntentValue.getInstance().setEmployeeEditResult(resultBean);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onSaveFailure(String failReason) {
        dismissLoading();
        showShortToast(failReason);
    }
}
