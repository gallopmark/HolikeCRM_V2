package com.holike.crm.fragment.report;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.holike.crm.R;
import com.holike.crm.base.BaseFragment;
import com.holike.crm.base.FragmentHelper;

/**
 * Created by pony on 2019/11/2.
 * Version v3.0 app报表
 */
abstract class GeneralReportHelper extends FragmentHelper {

    GeneralReportHelper(BaseFragment<?, ?> fragment) {
        super(fragment);
    }

    protected String getStringValue(String key) {
        return getBundle() == null ? "" : getBundle().getString(key);
    }

    @SuppressWarnings("SameParameterValue")
    boolean getBooleanValue(String key) {
        return getBundle() != null && getBundle().getBoolean(key, false);
    }

    @Nullable
    protected Bundle getBundle() {
        return mFragment.getArguments();
    }

    public FrameLayout getContainer() {
        FrameLayout flContainer = obtainView(R.id.fl_container);
        if (flContainer.getChildCount() > 0) {
            flContainer.removeAllViews();
        }
        return flContainer;
    }

    public void onFailure(String failReason) {
        LayoutInflater.from(mContext).inflate(R.layout.include_empty_page, getContainer(), true);
        mFragment.noNetwork(failReason);
    }
}
