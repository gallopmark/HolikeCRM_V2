package com.holike.crm.activity.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.galloped.tablayout.TabLayout;
import com.galloped.tablayout.tab.Tab;
import com.galloped.tablayout.tab.TabEntity;
import com.holike.crm.R;
import com.holike.crm.activity.customer.CustomerDetailV2Activity;
import com.holike.crm.activity.login.LoginActivity;
import com.holike.crm.base.MyApplication;
import com.holike.crm.base.MyFragmentActivity;
import com.holike.crm.bean.UpdateBean;
import com.holike.crm.dialog.UpdateAppDialog;
import com.holike.crm.enumeration.CustomerValue;
import com.holike.crm.fragment.main.MineFragment;
import com.holike.crm.fragment.main.ReportFragment;
import com.holike.crm.fragment.main.CustomerV2Fragment;
import com.holike.crm.fragment.main.HomepageV2Fragment;
import com.holike.crm.fragment.main.OrderV2Fragment;
import com.holike.crm.presenter.activity.MainPresenter;
import com.holike.crm.service.VersionUpdateService;
import com.holike.crm.util.KeyBoardUtil;
import com.holike.crm.view.activity.MainView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 主页
 */

public class MainActivity extends MyFragmentActivity<MainPresenter, MainView> implements MainView {
    @BindView(R.id.tab_main)
    TabLayout mTabLayout;

    private long mExitTime;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private FragmentManager mFragmentManager;
    private static final String TAG_HOME = "home";
    private static final String TAG_CUSTOMER = "customer";
    private static final String TAG_ORDER = "order";
    private static final String TAG_REPORT = "report";
    private static final String TAG_MINE = "mine";
    private String mCurrentTab = TAG_HOME;
    private Fragment mShowingFragment;
    private MainHelper mHelper;

    @Override
    protected MainPresenter attachPresenter() {
        return new MainPresenter();
    }

    @Override
    protected int setContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setStatusBarColor(R.color.textColor14);
        if (getIntent().getBooleanExtra("logout", false)) {//退出登录
            startActivity(LoginActivity.class);
            finish();
            return;
        }
        MainPresenter.setAlias();
        mFragmentManager = getSupportFragmentManager();
        setupTab();
        if (savedInstanceState != null) {
            mCurrentTab = savedInstanceState.getString("currentTab", TAG_HOME);
            setTabIndex();
        }
        mTabLayout.setOnTabSelectListener(new TabLayout.OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                statistics(position);
                setCurrentTab(position);
            }

            @Override
            public void onTabReselect(int position) {
                statistics(position);
            }
        });
        setCurrentTab(mCurrentTab);
        mHelper = new MainHelper(this, mHandler);
        checkVersion();
        getGlobalData();
    }

    private void setupTab() {
        List<Tab> tabList = new ArrayList<>();
        String[] titles = getResources().getStringArray(R.array.names_main_tab);
        Drawable[] selectedDrawables = new Drawable[]{
                ContextCompat.getDrawable(this, R.drawable.ic_home_sel),
                ContextCompat.getDrawable(this, R.drawable.ic_customer_sel),
                ContextCompat.getDrawable(this, R.drawable.ic_order_sel),
                ContextCompat.getDrawable(this, R.drawable.ic_analysis_sel),
                ContextCompat.getDrawable(this, R.drawable.ic_mine_sel),
        };
        Drawable[] unSelectDrawables = new Drawable[]{
                ContextCompat.getDrawable(this, R.drawable.ic_home_nor),
                ContextCompat.getDrawable(this, R.drawable.ic_customer_nor),
                ContextCompat.getDrawable(this, R.drawable.ic_order_nor),
                ContextCompat.getDrawable(this, R.drawable.ic_analysis_nor),
                ContextCompat.getDrawable(this, R.drawable.ic_mine_nor),
        };
        for (int i = 0; i < titles.length; i++) {
            tabList.add(new TabEntity(titles[i], selectedDrawables[i], unSelectDrawables[i]));
        }
        mTabLayout.setupTab(tabList);
    }

    private void setTabIndex() {
        if (TextUtils.equals(mCurrentTab, TAG_HOME)) {
            mTabLayout.setCurrentTab(0);
        } else if (TextUtils.equals(mCurrentTab, TAG_CUSTOMER)) {
            mTabLayout.setCurrentTab(1);
        } else if (TextUtils.equals(mCurrentTab, TAG_ORDER)) {
            mTabLayout.setCurrentTab(2);
        } else if (TextUtils.equals(mCurrentTab, TAG_REPORT)) {
            mTabLayout.setCurrentTab(3);
        } else if (TextUtils.equals(mCurrentTab, TAG_MINE)) {
            mTabLayout.setCurrentTab(4);
        }
    }

    private void statistics(int position) {
        switch (position) {
            case 0:
                MobclickAgent.onEvent(this, "homepage");
                break;
            case 1:
                MobclickAgent.onEvent(this, "customer");
                break;
            case 2:
                MobclickAgent.onEvent(this, "order");
                break;
            case 3:
                MobclickAgent.onEvent(this, "analyze");
                break;
            case 4:
                MobclickAgent.onEvent(this, "my_mine");
                break;
        }
    }

    private void setCurrentTab(int position) {
        if (position == 0) {
            setCurrentTab(TAG_HOME);
        } else if (position == 1) {
            setCurrentTab(TAG_CUSTOMER);
        } else if (position == 2) {
            setCurrentTab(TAG_ORDER);
        } else if (position == 3) {
            setCurrentTab(TAG_REPORT);
        } else if (position == 4) {
            setCurrentTab(TAG_MINE);
        }
    }

    private void setCurrentTab(String tab) {
        this.mCurrentTab = tab;
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (mShowingFragment != null) {
            transaction.hide(mShowingFragment);
        }
        Fragment fragment = mFragmentManager.findFragmentByTag(tab);
        if (fragment != null) {
            transaction.show(fragment);
        } else {
            if (TextUtils.equals(tab, TAG_HOME)) {
                fragment = new HomepageV2Fragment();
            } else if (TextUtils.equals(tab, TAG_CUSTOMER)) {
                fragment = new CustomerV2Fragment();
            } else if (TextUtils.equals(tab, TAG_ORDER)) {
                fragment = new OrderV2Fragment();
            } else if (TextUtils.equals(tab, TAG_REPORT)) {
                fragment = new ReportFragment();
            } else {
                fragment = new MineFragment();
            }
            transaction.add(R.id.fl_main_fragment, fragment, tab);
        }
        this.mShowingFragment = fragment;
        transaction.commitAllowingStateLoss();
    }

    private void checkVersion() {
        mPresenter.checkVersion();
    }

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            showShortToast(R.string.tips_exit);
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
            Process.killProcess(Process.myPid());
//            SophixManager.getInstance().killProcessSafely();
        }
    }

    /**
     * 发现新版本
     */
    @Override
    public void onGetVersion(UpdateBean updateBean, boolean hasNewVersion) {
        if (hasNewVersion) { //有新版本，则弹出更新提示窗
            showUpdateAppDialog(MainActivity.this, updateBean);
        }
    }

    /*请求失败，则间隔5s再次请求*/
    @Override
    public void onFailure() {
        mHandler.postDelayed(this::checkVersion, 5000);
    }

    /**
     * 版本更新弹窗
     */
    public void showUpdateAppDialog(final Context context, final UpdateBean updateBean) {
        new UpdateAppDialog(context, updateBean).setUpdateButtonClickListener(dialog -> {
            dialog.dismiss();
            VersionUpdateService.start(context, updateBean.getUpdatepath());
        }).show();
    }

    /*获取全局数据*/
    private void getGlobalData() {
        MyApplication.getInstance().getSystemCodeItems();
        MyApplication.getInstance().getUserInfo();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mHelper != null) {
            mHelper.onRestart();
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (KeyBoardUtil.isShouldHideInput(v, ev)) {
                v.clearFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("currentTab", mCurrentTab);  //状态发生改变 保存当前切换tab位置
    }

    @Override
    protected void onDestroy() {
        if (mHelper != null) {
            mHelper.deDeath();
        }
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
