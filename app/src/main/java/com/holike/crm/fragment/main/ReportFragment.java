package com.holike.crm.fragment.main;

import android.view.View;

import androidx.annotation.NonNull;

import com.holike.crm.R;
import com.holike.crm.activity.homepage.MessageV2Activity;
import com.holike.crm.base.CommonFragment;
import com.holike.crm.bean.ReportPermissionsBean;
import com.holike.crm.presenter.fragment.HomePagePresenter2;
import com.holike.crm.presenter.fragment.ReportPresenter;
import com.holike.crm.view.fragment.ReportView;

import java.util.List;

/**
 * Created by wqj on 2018/2/24.
 * 分析
 */
public class ReportFragment extends CommonFragment<ReportPresenter, ReportView, ReportHelper> implements ReportView {

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_report;
    }

    @Override
    protected ReportPresenter attachPresenter() {
        return new ReportPresenter();
    }

    @NonNull
    @Override
    protected ReportHelper newHelper() {
        return new ReportHelper(this);
    }

    @Override
    protected void setup() {
        setTitle(mContext.getString(R.string.report_title));
        initData();
    }

    private void initData() {
        showLoading();
        mPresenter.getPermissions();
    }

    @Override
    public void onResume() {
        super.onResume();
        setRightMenuMsg(HomePagePresenter2.isNewMsg());
    }

    @Override
    protected void clickRightMenu(CharSequence menuText, View actionView) {
        startActivity(MessageV2Activity.class);
    }

    @Override
    public void getPermissionsSuccess(final List<ReportPermissionsBean> list) {
        dismissLoading();
        mHelper.onSuccess(list);
    }

    @Override
    public void getPermissionsFailed(String failed) {
        dismissLoading();
        mHelper.onFailure(failed);
    }

    @Override
    protected void reload() {
        initData();
    }
}
