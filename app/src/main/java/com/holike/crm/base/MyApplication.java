package com.holike.crm.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDexApplication;

import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import com.github.moduth.blockcanary.BlockCanary;
import com.github.moduth.blockcanary.BlockCanaryContext;
import com.holike.crm.BuildConfig;
import com.holike.crm.R;
import com.holike.crm.bean.CurrentUserBean;
import com.holike.crm.bean.SysCodeItemBean;
import com.holike.crm.helper.MyLifecycleHelper;
import com.holike.crm.http.CustomerUrlPath;
import com.holike.crm.http.MyHttpClient;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.util.SharedPreferencesUtils;
import com.liulishuo.filedownloader.FileDownloader;
import com.scwang.smartrefresh.header.WaterDropHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.commonsdk.framework.UMModuleRegister;


import cn.jpush.android.api.JPushInterface;


/*
 *                   _ooOoo_
 *                  o8888888o
 *                  88" . "88
 *                  (| -_- |)
 *                  O\  =  /O
 *               ____/`---'\____
 *             .'  \\|     |//  `.
 *            /  \\|||  :  |||//  \
 *           /  _||||| -:- |||||-  \
 *           |   | \\\  -  /// |   |
 *           | \_|  ''\---/''  |   |
 *           \  .-\__  `-`  ___/-. /
 *         ___`. .'  /--.--\  `. . __
 *      ."" '<  `.___\_<|>_/___.'  >'"".
 *     | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 *     \  \ `-.   \_ __\ /__ _/   .-` /  /
 *======`-.____`-.___\_____/___.-`____.-'======
 *                   `=---='
 *^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 *         佛祖保佑       永无BUG
 */

public class MyApplication extends MultiDexApplication {
    public int screenWidth, screenHeight;
    private static MyApplication myApplication;
    public MyLifecycleHelper mLifecycleHelper;
    private Handler mHandler;

    public static MyApplication getInstance() {
        if (myApplication != null)
            return myApplication;
        return (MyApplication) getApplicationByReflect();
    }

    private static Application getApplicationByReflect() {
        try {
            @SuppressLint("PrivateApi")
            Class<?> activityThread = Class.forName("android.app.ActivityThread");
            Object thread = activityThread.getMethod("currentActivityThread").invoke(null);
            Object app = activityThread.getMethod("getApplication").invoke(thread);
            return (Application) app;
        } catch (Exception e) {
            return myApplication;
        }
    }

    static {
        /*
         * 兼容5.0以下系统
         */
        /*获取当前系统的android版本号*/
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //适配android5.0以下
            /*解决低版本手机vectorDrawable不支持儿闪退问题*/
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        }
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> new WaterDropHeader(context));
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> new BallPulseFooter(context));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        mLifecycleHelper = new MyLifecycleHelper();
        getDisplay();
        registerActivityLifecycleCallbacks(mLifecycleHelper);
        initJpush();
        initUm();
        BlockCanary.install(this, new AppContext()).start();
//        CrashHandler.getInstance().init();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
//        SophixManager.getInstance().queryAndLoadNewPatch();
//        startService(new Intent(this, PhoneStateService.class));
        initBugly();
        mHandler = new Handler();
        /*文件下载器 初始化*/
        FileDownloader.setupOnApplicationOnCreate(this);
        getSystemCodeItems();
    }

    private void initJpush() {
        JPushInterface.setDebugMode(BuildConfig.LOG_DEBUG);
        JPushInterface.init(this);
    }

    /**
     * 初始化友盟
     */
    private void initUm() {
        UMConfigure.setLogEnabled(BuildConfig.LOG_DEBUG);
        UMConfigure.setEncryptEnabled(true);
        UMConfigure.init(this, BuildConfig.UM_KEY, "holike", UMConfigure.DEVICE_TYPE_PHONE, null);
    }

    /**
     * 获取屏幕分辨率
     */
    private void getDisplay() {
        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
    }

    /**
     * 获取当前activity
     */
    @Nullable
    public Activity getCurrentActivity() {
        return mLifecycleHelper.getCurrentActivity();
    }

    @Nullable
    public Activity getTopActivity() {
        return mLifecycleHelper.getTopActivity();
    }

    /**
     * 判断应用是否前台运行
     */
    public boolean isForeground() {
        return mLifecycleHelper.isApplicationInForeground();
    }

    /**
     * 判断是否退出了应用
     */
    public boolean isExit() {
        return mLifecycleHelper.isExit();
    }

    //参数设置
    public class AppContext extends BlockCanaryContext {
        private static final String TAG = "AppContext";

        @Override
        public String provideQualifier() {
            String qualifier = "";
            try {
                PackageInfo info = UMModuleRegister.getAppContext().getPackageManager()
                        .getPackageInfo(UMModuleRegister.getAppContext().getPackageName(), 0);
                qualifier += info.versionCode + "_" + info.versionName + "_YYB";
            } catch (PackageManager.NameNotFoundException e) {
                Log.e(TAG, "provideQualifier exception", e);
            }
            return qualifier;
        }

        @Override
        public int provideBlockThreshold() {
            return 500;
        }

        @Override
        public boolean displayNotification() {
            return BuildConfig.DEBUG;
        }

        @Override
        public boolean stopWhenDebugging() {
            return false;
        }
    }

    private void initBugly() {
        if (!BuildConfig.DEBUG) {
            CrashReport.initCrashReport(this, getString(R.string.bugly_AppID), false);
        }
    }

    private boolean mIsGetSystemCode;

    /*获取业务字典*/
    public void getSystemCodeItems() {
        if (TextUtils.isEmpty(SharedPreferencesUtils.getUserId()) || mIsGetSystemCode) {  //用户未登录
            return;
        }
        MyHttpClient.get(CustomerUrlPath.URL_GET_SYSTEM_CODE_ITEM, new RequestCallBack<SysCodeItemBean>() {
            @Override
            public void onFailed(String failReason) {
                mHandler.postDelayed(() -> getSystemCodeItems(), 3000);  //获取失败后3s重试
            }

            @Override
            public void onSuccess(SysCodeItemBean sysCodeItem) {
                IntentValue.getInstance().setSystemCode(sysCodeItem);
                mIsGetSystemCode = true;
            }
        });
    }

    private boolean mIsGetUserInfo;

    /*获取当前登录用户信息*/
    public void getUserInfo() {
        if (TextUtils.isEmpty(SharedPreferencesUtils.getUserId()) || mIsGetUserInfo) {
            return;
        }
        MyHttpClient.getByTimeout(CustomerUrlPath.URL_GET_USER_INFO, 60, new RequestCallBack<CurrentUserBean>() {
            @Override
            public void onFailed(String failReason) {
                mHandler.postDelayed(() -> getUserInfo(), 3000);  //获取失败后3s重试
            }

            @Override
            public void onSuccess(CurrentUserBean bean) {
                IntentValue.getInstance().setCurrentUserInfo(bean);
                mIsGetUserInfo = true;
            }
        });
    }

    /*用户退出登录时调用*/
    public void resetSystem() {
        mIsGetSystemCode = false;
        mIsGetUserInfo = false;
        finishAllActivities();
    }

    public void finishAllActivities() {
        mLifecycleHelper.finishAllActivities();
    }

    @Override
    public void onTerminate() {
        mHandler.removeCallbacksAndMessages(null);
        super.onTerminate();  // 程序终止的时候执行
    }
}
