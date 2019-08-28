package com.holike.crm.activity.homepage;


import com.holike.crm.R;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.base.MyFragmentActivity;
import com.holike.crm.util.Constants;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by wqj on 2018/8/2.
 * 消息
 */

public class MessageV2Activity extends MyFragmentActivity {
    @Override
    protected BasePresenter attachPresenter() {
        return null;
    }

    @Override
    protected int setContentViewId() {
        return R.layout.activity_messagev2;
    }

    @Override
    protected void init() {
        MobclickAgent.onEvent(this, "message");
        setTitle(getString(R.string.message_title));
        MessageHelper mHelper = new MessageHelper(this);
        if (getIntent().getIntExtra(Constants.PUSH_TYPE, 0) == Constants.PUSH_TYPE_NOTIFY) {
            mHelper.setTab(0);
        } else {
            mHelper.setTab(1);
        }
    }
}
