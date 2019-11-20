package com.holike.crm.activity.report;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.grallopmark.tablayout.CommonTabLayout;
import com.grallopmark.tablayout.TabEntity;
import com.grallopmark.tablayout.listener.CustomTabEntity;
import com.grallopmark.tablayout.listener.OnTabSelectListener;
import com.holike.crm.R;
import com.holike.crm.base.ActivityHelper;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.bean.BusinessTargetBean;
import com.holike.crm.fragment.report.target.SetBusinessTargetFragment;
import com.holike.crm.fragment.report.target.SetEmployeeTargetFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pony on 2019/10/28.
 * Version v3.0 app报表
 */
class BusinessSetTargetHelper extends ActivityHelper {

    private FrameLayout mContainer;
    private FragmentManager mFragmentManager;
    private Fragment mShowingFragment;
    private static final String TAG_SHOP = "tag-shop";
    private static final String TAG_PERSON = "tag-person";

    BusinessSetTargetHelper(BaseActivity<?, ?> activity) {
        super(activity);
        mFragmentManager = activity.getSupportFragmentManager();
        mContainer = obtainView(R.id.container);
    }

    void onSuccess(BusinessTargetBean bean) {
        mContainer.removeAllViews();
        //不能设置“经营目标”也不能设置“员工目标” 展示缺省页
        if (!bean.canSetShop() && !bean.canSetPerson()) {
            LayoutInflater.from(mActivity).inflate(R.layout.include_empty_textview, mContainer, true);
        } else {
            View view = LayoutInflater.from(mActivity).inflate(R.layout.include_business_settarget, mContainer, true);
            invalidate(bean, view);
        }
    }

    private void invalidate(final BusinessTargetBean bean, View contentView) {
        LinearLayout contentLayout = contentView.findViewById(R.id.ll_content_layout);
        List<CustomTabEntity> tabEntities = new ArrayList<>();
        final List<String> tagList = new ArrayList<>();
        if (bean.canSetShop()) {  //经营目标
            tabEntities.add(new TabEntity(mActivity.getString(R.string.business_target)));
            tagList.add(TAG_SHOP);
        }
        if (bean.canSetPerson()) {
            tabEntities.add(new TabEntity(mActivity.getString(R.string.employee_target)));
            tagList.add(TAG_PERSON);
        }
        if (tabEntities.size() > 1) {
            View view = LayoutInflater.from(mActivity).inflate(R.layout.include_common_tablayout, contentLayout, false);
            CommonTabLayout tabLayout = view.findViewById(R.id.tab_layout);
            tabLayout.setTabData(tabEntities);
            tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
                @Override
                public void onTabSelect(int position) {
                    setCurrentTab(bean, tagList.get(position));
                }

                @Override
                public void onTabReselect(int position) {

                }
            });
            contentLayout.addView(view, 0);
        }
        setCurrentTab(bean, tagList.get(0));
    }

    private void setCurrentTab(BusinessTargetBean bean, String tag) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (mShowingFragment != null) {
            transaction.hide(mShowingFragment);
        }
        Fragment fragment = mFragmentManager.findFragmentByTag(tag);
        if (fragment != null) {
            transaction.show(fragment);
        } else {
            if (TextUtils.equals(tag, TAG_SHOP)) {
                fragment = SetBusinessTargetFragment.newInstance(bean);
            } else {
                fragment = SetEmployeeTargetFragment.newInstance(bean);
            }
            transaction.add(R.id.fl_container, fragment, tag);
        }
        mShowingFragment = fragment;
        transaction.commitAllowingStateLoss();
    }

    void onFailure(String failReason) {
        mContainer.removeAllViews();
        LayoutInflater.from(mActivity).inflate(R.layout.include_empty_page, mContainer, true);
        mActivity.noNetwork(failReason);
    }
}
