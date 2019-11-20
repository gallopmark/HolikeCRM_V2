package com.holike.crm.activity.report;

import android.content.Intent;
import android.os.Bundle;

import com.holike.crm.R;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.fragment.report.HomeDollDealerFragment;
import com.holike.crm.fragment.report.HomeDollFragment;

/**
 * Created by pony on 2019/11/2.
 * Version v3.0 app报表
 * 家装渠道-经销商或营销人员
 */
public class HomeDollChannelActivity extends BaseActivity {

    public static void start(BaseActivity<?, ?> activity, boolean isDealer) {
        Intent intent = new Intent(activity, HomeDollChannelActivity.class);
        intent.putExtra("isDealer", isDealer);
        activity.openActivity(intent);
    }

    @Override
    protected BasePresenter attachPresenter() {
        return null;
    }

    @Override
    protected int setContentViewId() {
        return R.layout.activity_common;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        boolean isDealer = getIntent().getBooleanExtra("isDealer", false);
        if (isDealer) {
            startFragment(new HomeDollDealerFragment());
        } else {
            startFragment(new HomeDollFragment());
        }
    }
}
