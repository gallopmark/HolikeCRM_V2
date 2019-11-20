package com.holike.crm.fragment.employee;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.grallopmark.tablayout.CommonTabLayout;
import com.grallopmark.tablayout.TabEntity;
import com.grallopmark.tablayout.listener.CustomTabEntity;
import com.grallopmark.tablayout.listener.OnTabSelectListener;
import com.holike.crm.R;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.base.MyFragment;

import java.util.ArrayList;
import java.util.List;

/*员工排行*/
abstract class EmployeeRankFragment extends MyFragment implements OnTabSelectListener {
    private CommonTabLayout mTabLayout;
    private FragmentManager mFragmentManager;
    private Fragment mShowingFragment;
    private static final String TAG_ONE = "tag-one";
    private static final String TAG_TWO = "tag-two";
    private static final String TAG_THREE = "tag-three";
    private static final String TAG_FOUR = "tag-four";

    @Override
    protected BasePresenter attachPresenter() {
        return null;
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_employee_ranking;
    }

    @Override
    protected void init() {
        mFragmentManager = getChildFragmentManager();
        mTabLayout = mContentView.findViewById(R.id.tab_layout);
        setTabLayout();
        setCurrentTab(0);
    }

    private void setTabLayout() {
        List<CustomTabEntity> tabEntities = new ArrayList<>();
        tabEntities.add(new TabEntity(mContext.getString(R.string.personal_signing)));
        tabEntities.add(new TabEntity(mContext.getString(R.string.transaction)));
        tabEntities.add(new TabEntity(mContext.getString(R.string.payback)));
        tabEntities.add(new TabEntity(mContext.getString(R.string.order)));
        mTabLayout.setTabData(tabEntities);
        mTabLayout.setOnTabSelectListener(this);
    }

    @Override
    public void onTabSelect(int position) {
        setCurrentTab(position);
    }

    @Override
    public void onTabReselect(int position) {

    }

    private void setCurrentTab(int tabPosition) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (mShowingFragment != null) {
            transaction.hide(mShowingFragment);
        }
        String tag = getFragmentTag(tabPosition);
        Fragment fragment = mFragmentManager.findFragmentByTag(tag);
        if (fragment != null) {
            transaction.show(fragment);
        } else {
            if (tabPosition == 0) {
                fragment = getTagOne();
            } else if (tabPosition == 1) {
                fragment = getTagTwo();
            } else if (tabPosition == 2) {
                fragment = getTagThree();
            } else {
                fragment = getTagFour();
            }
            transaction.add(R.id.fl_container, fragment, tag);
        }
        mShowingFragment = fragment;
        transaction.commitAllowingStateLoss();
    }

    abstract Fragment getTagOne();

    abstract Fragment getTagTwo();

    abstract Fragment getTagThree();

    abstract Fragment getTagFour();

    private String getFragmentTag(int tabPosition) {
        if (tabPosition == 0) {
            return TAG_ONE;
        } else if (tabPosition == 1) {
            return TAG_TWO;
        } else if (tabPosition == 2) {
            return TAG_THREE;
        } else {
            return TAG_FOUR;
        }
    }
}
