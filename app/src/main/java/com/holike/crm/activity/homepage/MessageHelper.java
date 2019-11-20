package com.holike.crm.activity.homepage;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.grallopmark.tablayout.CommonTabLayout;
import com.grallopmark.tablayout.TabEntity;
import com.grallopmark.tablayout.listener.CustomTabEntity;
import com.grallopmark.tablayout.listener.OnTabSelectListener;
import com.holike.crm.R;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.bean.MessageResultBean;
import com.holike.crm.fragment.message.AnnouncementFragment;
import com.holike.crm.fragment.message.IMessageFragment;
import com.holike.crm.fragment.message.PendingMsgFragment;
import com.holike.crm.presenter.fragment.HomePagePresenter2;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pony on 2019/8/21.
 * Copyright holike possess 2019.
 */
class MessageHelper implements OnTabSelectListener, IMessageFragment.IMessageRequestCallback {

    private BaseActivity<?, ?> mActivity;
    private FragmentManager mFragmentManager;

    private CommonTabLayout mTabLayout;
    private Fragment mShowingFragment;
    private static final String TAG_PENDING = "pending";
    private static final String TAG_SYSTEM = "system";

    MessageHelper(BaseActivity<?, ?> activity) {
        this.mActivity = activity;
        this.mFragmentManager = mActivity.getSupportFragmentManager();
        mTabLayout = mActivity.findViewById(R.id.tab_layout);
        initTab();
    }

    private void initTab() {
        List<CustomTabEntity> tabList = new ArrayList<>();
        tabList.add(new TabEntity(mActivity.getString(R.string.message_pending_event)));
        tabList.add(new TabEntity(mActivity.getString(R.string.message_system_message)));
        mTabLayout.setTabData(tabList);
        mTabLayout.setOnTabSelectListener(this);
    }

    @Override
    public void onTabSelect(int position) {
        if (position == 1) {
            MobclickAgent.onEvent(mActivity, "message_announce");
        } else {
            MobclickAgent.onEvent(mActivity, "message_notify");
        }
        setCurrentTab(position);
    }

    @Override
    public void onTabReselect(int position) {

    }

    public void setTab(int tab){
        mTabLayout.setCurrentTab(tab);
        setCurrentTab(tab);
    }

    private void setCurrentTab(int tab) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (mShowingFragment != null) {
            transaction.hide(mShowingFragment);
        }
        Fragment fragment;
        if (tab == 0) {
            fragment = mFragmentManager.findFragmentByTag(TAG_PENDING);
            if (fragment == null) {
                fragment = new PendingMsgFragment();
                ((PendingMsgFragment) fragment).setCallback(this);
                transaction.add(R.id.container, fragment, TAG_PENDING);
            } else {
                transaction.show(fragment);
            }
        } else {
            fragment = mFragmentManager.findFragmentByTag(TAG_SYSTEM);
            if (fragment == null) {
                fragment = new AnnouncementFragment();
                ((AnnouncementFragment) fragment).setCallback(this);
                transaction.add(R.id.container, fragment, TAG_SYSTEM);
            } else {
                transaction.show(fragment);
            }
        }
        mShowingFragment = fragment;
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void onResponse(MessageResultBean bean) {
        showUnreadMessage(bean.noticeRead, bean.announcementRead);
    }

    /**
     * 显示未读消息数
     */
    private void showUnreadMessage(int notifyNum, int announceNum) {
        HomePagePresenter2.setMsgNum(String.valueOf(notifyNum + announceNum));
        if (notifyNum > 0) {
            mTabLayout.showMsg(0, notifyNum);
        } else {
            mTabLayout.hideMsg(0);
        }
        if (announceNum > 0) {
            mTabLayout.showMsg(1, announceNum);
        } else {
            mTabLayout.hideMsg(1);
        }
    }
}
