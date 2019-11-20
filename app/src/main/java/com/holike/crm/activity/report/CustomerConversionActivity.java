package com.holike.crm.activity.report;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;

import com.holike.crm.R;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.bean.CustomerConversionBean;
import com.holike.crm.dialog.PlainTextTipsDialog;

import java.util.Date;


/**
 * Created by pony on 2019/10/31.
 * Version v3.0 app报表
 * 客户转化
 */
public class CustomerConversionActivity extends GeneralReportActivity<CustomerConversionHelper> implements CustomerConversionHelper.Callback {

    public static void start(BaseActivity<?, ?> activity, String title) {
        Intent intent = new Intent(activity, CustomerConversionActivity.class);
        intent.putExtra("title", title);
        activity.openActivity(intent);
    }

    @NonNull
    @Override
    protected CustomerConversionHelper newHelper() {
        return new CustomerConversionHelper(this, this);
    }

    @Override
    protected int setContentViewId() {
        return R.layout.activity_customer_conversion;
    }

    @Override
    protected void setup(Bundle savedInstanceState) {
        String title = getIntent().getStringExtra("title");
        if (TextUtils.isEmpty(title)) {
            title = getString(R.string.title_customer_conversion);
        }
        setTitle(title);
        setRightMenu(R.string.the_data_description);
    }

    @Override
    protected void clickRightMenu(CharSequence menuText, View actionView) {
        new PlainTextTipsDialog(this).setData(R.array.customer_conversion_tips).show();
    }

    @Override
    public void onRequest(Date startDate, Date endDate, String shopId) {
        showLoading();
        mPresenter.getCustomerConversion(startDate, endDate, shopId);
    }

    @Override
    public void onSuccess(Object obj) {
        super.onSuccess(obj);
        mActivityHelper.onSuccess((CustomerConversionBean) obj);
    }

    @Override
    public void onFailure(String failReason) {
        super.onFailure(failReason);
        mActivityHelper.onFailure(failReason);
    }

    @Override
    public void reload() {
        mActivityHelper.doRequest();
    }
}
