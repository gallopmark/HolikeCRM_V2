package com.holike.crm.activity.report;


import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.holike.crm.R;
import com.holike.crm.base.ActivityHelper;
import com.holike.crm.base.BaseActivity;

abstract class GeneralReportHelper extends ActivityHelper {

    GeneralReportHelper(BaseActivity<?, ?> activity) {
        super(activity);
        setTitle();
    }

    private void setTitle() {
        String title = mActivity.getIntent().getStringExtra("title");
        if (TextUtils.isEmpty(title)) {
            title = getTitle();
        }
        mActivity.setTitle(title);
    }

    String getTitle() {
        return null;
    }

    void requestUpdate() {
        ((LinearLayout) obtainView(R.id.ll_content_layout)).removeAllViews();
    }

    void onFailure(String failReason) {
        FrameLayout flContainer = obtainView(R.id.fl_container);
        if (flContainer.getChildCount() > 0) {
            flContainer.removeAllViews();
        }
        LayoutInflater.from(mActivity).inflate(R.layout.include_empty_page, flContainer, true);
        mActivity.noNetwork(failReason);
    }

}
