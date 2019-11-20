package com.holike.crm.fragment.main;

import android.view.View;

import androidx.annotation.NonNull;

import com.holike.crm.R;
import com.holike.crm.activity.homepage.MessageV2Activity;
import com.holike.crm.bean.ReportV3IconBean;
import com.holike.crm.fragment.report.GeneralReportFragment;
import com.holike.crm.presenter.fragment.HomePagePresenter2;
import com.holike.crm.view.fragment.ReportV3View;

import java.util.List;

/**
 * Created by pony on 2019/10/31.
 * Version v3.0 app报表
 * 报表
 */
public class ReportV3Fragment extends GeneralReportFragment<ReportV3Helper> implements ReportV3View {

    @NonNull
    @Override
    protected ReportV3Helper newHelper() {
        return new ReportV3Helper(this);
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_reportv3;
    }

    @Override
    protected void setup() {
        setTitle(R.string.report_title);
        initData();
    }

    private void initData() {
        showLoading();
        mPresenter.getPowInfo(this);
    }

    @Override
    public void onSuccess(List<ReportV3IconBean> dataList) {
        dismissLoading();
        mHelper.onSuccess(dataList);
    }

    @Override
    public void onFailure(String failReason) {
        super.onFailure(failReason);
        mHelper.onFailure(failReason);
    }

    @Override
    protected void reload() {
        initData();
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

}
