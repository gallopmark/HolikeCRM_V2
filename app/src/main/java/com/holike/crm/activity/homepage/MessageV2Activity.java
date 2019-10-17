package com.holike.crm.activity.homepage;


import android.content.Intent;
import android.os.Bundle;

import com.holike.crm.R;
import com.holike.crm.activity.customer.CustomerDetailV2Activity;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.base.MyFragmentActivity;
import com.holike.crm.enumeration.CustomerValue;
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
    protected void init(Bundle savedInstanceState) {
        MobclickAgent.onEvent(this, "message");
        setTitle(getString(R.string.message_title));
        MessageHelper mHelper = new MessageHelper(this);
        if (getIntent().getIntExtra(Constants.PUSH_TYPE, 0) == Constants.PUSH_TYPE_NOTIFY) {
            mHelper.setTab(0);
        } else {
            mHelper.setTab(1);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((resultCode == CustomerValue.RESULT_CODE_ACTIVATION ||
                resultCode == CustomerValue.RESULT_CODE_HIGH_SEAS) && data != null) {
            String personalId = data.getStringExtra(CustomerValue.PERSONAL_ID);
            String houseId = data.getStringExtra(CustomerValue.HOUSE_ID);
            CustomerDetailV2Activity.open(this, personalId, houseId, false);
        }
    }
}
