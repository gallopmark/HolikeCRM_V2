package com.holike.crm.activity.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.widget.Toast;

import com.galloped.tablayout.TabLayout;
import com.galloped.tablayout.tab.Tab;
import com.galloped.tablayout.tab.TabEntity;
import com.holike.crm.R;
import com.holike.crm.activity.login.LoginActivity;
import com.holike.crm.base.MyApplication;
import com.holike.crm.base.MyFragmentActivity;
import com.holike.crm.bean.DownloadFileBean;
import com.holike.crm.bean.UpdateBean;
import com.holike.crm.dialog.UpdateAppDialog;
import com.holike.crm.fragment.main.MineFragment;
import com.holike.crm.fragment.analyze.ReportFragment;
import com.holike.crm.fragment.main.CustomerV2Fragment;
import com.holike.crm.fragment.main.HomepageV2Fragment;
import com.holike.crm.fragment.main.OrderV2Fragment;
import com.holike.crm.presenter.activity.MainPresenter;
import com.holike.crm.service.UpdateService;
import com.holike.crm.util.Constants;
import com.holike.crm.util.IOUtil;
import com.holike.crm.util.SharedPreferencesUtils;
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
    TabLayout tabMain;

    private long mExitTime;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private FragmentManager mFragmentManager;
    private static final String TAG_HOME = "home";
    private static final String TAG_CUSTOMER = "customer";
    private static final String TAG_ORDER = "order";
    private static final String TAG_REPORT = "report";
    private static final String TAG_MINE = "mine";
    private String currentTab = TAG_HOME;
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
    protected void init() {
        super.init();
        setStatusBarColor(R.color.textColor14);
        if (getIntent().getBooleanExtra("logout", false)) {//退出登录
            startActivity(LoginActivity.class);
            finish();
            return;
        }
        MainPresenter.setAlias();
        mFragmentManager = getSupportFragmentManager();
        setupTab();
        tabMain.setOnTabSelectListener(new TabLayout.OnTabSelectListener() {
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
        setCurrentTab(currentTab);
        mHelper = new MainHelper(this, mHandler);
        checkVersion();
        getGlobalData();
    }

    private void setupTab() {
        List<Tab> tabList = new ArrayList<>();
        String[] titles = getResources().getStringArray(R.array.names_main_tab);
        Drawable[] selectedDrawables = new Drawable[]{
                ContextCompat.getDrawable(this, R.drawable.tab_home_sel),
                ContextCompat.getDrawable(this, R.drawable.tab_customer_sel),
                ContextCompat.getDrawable(this, R.drawable.tab_order_sel),
                ContextCompat.getDrawable(this, R.drawable.tab_analysis_sel),
                ContextCompat.getDrawable(this, R.drawable.tab_mine_sel),
        };
        Drawable[] unSelectDrawables = new Drawable[]{
                ContextCompat.getDrawable(this, R.drawable.tab_home_nor),
                ContextCompat.getDrawable(this, R.drawable.tab_customer_nor),
                ContextCompat.getDrawable(this, R.drawable.tab_order_nor),
                ContextCompat.getDrawable(this, R.drawable.tab_analysis_nor),
                ContextCompat.getDrawable(this, R.drawable.tab_mine_nor),
        };
        for (int i = 0; i < titles.length; i++) {
            tabList.add(new TabEntity(titles[i], selectedDrawables[i], unSelectDrawables[i]));
        }
        tabMain.setupTab(tabList);
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
        this.currentTab = tab;
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        hideFragments(transaction);
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

    private void hideFragments(FragmentTransaction transaction) {
        if (mShowingFragment != null) {
            transaction.hide(mShowingFragment);
        }
    }

    private void checkVersion() {
        mPresenter.checkVersion();
    }

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(this, getString(R.string.tips_exit), Toast.LENGTH_SHORT).show();
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
    public void hasNewVersion(UpdateBean updateBean) {
        long now = System.currentTimeMillis();
        long old = SharedPreferencesUtils.getLong(Constants.CHECK_VERSION_TIME, 0);
        if ((now - old) > Constants.UPDATE_DIALOG_TIME) {
            SharedPreferencesUtils.saveLong(Constants.CHECK_VERSION_TIME, now);
            showUpdateAppDialog(MainActivity.this, updateBean);
        }
    }

    @Override
    public void onFailure() {
        mHandler.postDelayed(retryRun, 5000);
    }

    private Runnable retryRun = this::checkVersion;

    /**
     * 版本更新弹窗
     */
    public void showUpdateAppDialog(final Context context, final UpdateBean updateBean) {
        new UpdateAppDialog(context, updateBean).setUpdateButtonClickListener(dialog -> {
            dialog.dismiss();
            if (updateBean.getType() == UpdateBean.TYPE_DOWNLOAD) {
                showLongToast("正在下载...");
//                Toast.makeText(MyApplication.getInstance(), "正在下载...", Toast.LENGTH_SHORT).show();
                DownloadFileBean downloadFileBean = new DownloadFileBean(updateBean.getUpdatepath(), "CRM.apk");
                Intent intent = new Intent(context, UpdateService.class);
                intent.putExtra(UpdateService.DOWNLOADFILEBEAN, downloadFileBean);
                context.startService(intent);
            } else if (updateBean.getType() == UpdateBean.TYPE_INSTALL) {
                UpdateService.install(IOUtil.getCachePath() + "/" + "CRM.apk");
            }
        }).show();
    }

    /*获取全局数据*/
    private void getGlobalData() {
        MyApplication.getInstance().getSystemCodeItems();
        MyApplication.getInstance().getUserInfo();
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
