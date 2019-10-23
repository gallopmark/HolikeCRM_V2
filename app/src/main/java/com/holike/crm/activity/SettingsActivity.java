package com.holike.crm.activity;


import android.os.Bundle;

import androidx.annotation.NonNull;

import com.holike.crm.R;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.base.BaseFragment;
import com.holike.crm.base.CommonActivity;
import com.holike.crm.presenter.SettingsPresenter;
import com.holike.crm.view.SettingsView;

/*设置页面*/
public class SettingsActivity extends CommonActivity<SettingsPresenter, SettingsView, SettingsHelper>
        implements SettingsHelper.Callback, SettingsView {

    public static void startRuleSettings(BaseActivity<?, ?> activity, String id, String param2) {
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("param2", param2);
        activity.startActivity(SettingsActivity.class, bundle);
    }

    @Override
    protected SettingsPresenter attachPresenter() {
        return new SettingsPresenter();
    }

    @Override
    protected int setContentViewId() {
        return R.layout.activity_settings;
    }

    @NonNull
    @Override
    protected SettingsHelper newHelper() {
        return new SettingsHelper(this, this);
    }

    @Override
    protected void setup(Bundle savedInstanceState) {
        setTitle(getString(R.string.settings));
    }

    @Override
    public void openOrClose(String id, String param2) {
        mPresenter.setRule(id, param2);
    }

    @Override
    public void onSetupSuccess(String id, String message) {
        mActivityHelper.onSettingSuccess(id, message);
    }

    @Override
    public void onSetupFailure(String failReason) {
        mActivityHelper.onSettingsFailure(failReason);
    }

    @Override
    public void finish() {
        mActivityHelper.setResult();
        super.finish();
    }
}
