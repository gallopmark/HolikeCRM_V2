package com.holike.crm.fragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

/**
 * Created by wqj on 2017/10/11.
 * 通用fragment切换管理类
 */

public class FragmentManagerHelper {
    private FragmentManager fragmentManager;
    private List<Class<?>> fragments;
    private int layoutId;

    public FragmentManagerHelper(FragmentManager fragmentManager, int layoutId, List<Class<?>> fragments) {
        this.fragmentManager = fragmentManager;
        this.layoutId = layoutId;
        this.fragments = fragments;
    }

    public Fragment changeFragment(int position, Fragment mFragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (mFragment == null) {
            mFragment = getChangeFragment(0);
            transaction.show(mFragment).commitAllowingStateLoss();
        } else {
            Fragment changeFragment = getChangeFragment(position);
            transaction.hide(mFragment).show(changeFragment).commitAllowingStateLoss();
            mFragment = changeFragment;
        }
        return mFragment;
    }

    public Fragment getChangeFragment(int position) {
        Fragment changeFragment;
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        changeFragment = fragmentManager.findFragmentByTag(fragments.get(position).getName());
        if (changeFragment == null) {
            try {
                changeFragment = (Fragment) fragments.get(position).newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            transaction.add(layoutId, changeFragment, fragments.get(position).getName());
            transaction.commitAllowingStateLoss();
        }
        return changeFragment;
    }
}
