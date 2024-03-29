package com.holike.crm.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.holike.crm.R;
import com.holike.crm.customView.AppToastCompat;
import com.holike.crm.customView.CompatToast;
import com.holike.crm.dialog.LoadingDialog;
import com.holike.crm.dialog.MaterialDialog;
import com.holike.crm.helper.BackHandlerHelper;
import com.holike.crm.service.VersionUpdateService;
import com.holike.crm.util.CheckUtils;
import com.holike.crm.util.CopyUtil;
import com.umeng.analytics.MobclickAgent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.ButterKnife;
import galloped.xcode.widget.TitleBar;

public abstract class BaseActivity<P extends BasePresenter, V extends BaseView> extends AppCompatActivity {
    public final int REQUEST_CODE = new Random().nextInt(65536);
    protected P mPresenter;
    protected Dialog mLoadingDialog;
    /*权限申请请求码*/
    private int mPermissionsRequestCode;
    /*权限申请回调*/
    private OnRequestPermissionsCallback mRequestPermissionsCallback;
    protected FragmentManager mFragmentManager;
    protected List<Fragment> mFragmentList = new ArrayList<>();

    protected int mActivityCloseEnterAnimation = -1;

    protected int mActivityCloseExitAnimation = -1;

    public static void start(BaseActivity<?, ?> activity, Class<? extends Activity> clz, String title) {
        Intent intent = new Intent(activity, clz);
        intent.putExtra("title", title);
        activity.openActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupWindow();
        setOrientation();
        setContentView(setContentViewId());
        ButterKnife.bind(this);
        mFragmentManager = getSupportFragmentManager();
        setupPresenter();
        setupScreenStyle();
        setupTitleBar();
        init(savedInstanceState);
    }

    /*activity动画兼容性,style 退出动画 解决退出动画无效问题*/
    protected void setupWindow() {
        Resources.Theme theme = getTheme();
        if (theme != null) {
            TypedArray activityStyle = theme.obtainStyledAttributes(new int[]{android.R.attr.windowAnimationStyle});
            int windowAnimationStyleResId = activityStyle.getResourceId(0, 0);
            activityStyle.recycle();
            activityStyle = theme.obtainStyledAttributes(windowAnimationStyleResId, new int[]{android.R.attr.activityCloseEnterAnimation, android.R.attr.activityCloseExitAnimation});
            mActivityCloseEnterAnimation = activityStyle.getResourceId(0, 0);
            mActivityCloseExitAnimation = activityStyle.getResourceId(1, 0);
            activityStyle.recycle();
        }
    }

    public void setStatusBarLightMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setStatusBarColor(R.color.color_while);
        } else {
            setStatusBarColor(R.color.bg_state_bar);
        }
        SystemTintHelper.setStatusBarLightMode(this);
    }

    protected abstract P attachPresenter();

    /**
     * 设置layout
     */
    protected abstract int setContentViewId();

    @SuppressWarnings("unchecked")
    private void setupPresenter() {
        mPresenter = attachPresenter();
        if (this instanceof BaseView && mPresenter != null) {
            mPresenter.attach((V) this);
        }
    }

    private void setupScreenStyle() {
        if (isFullScreen()) {
            requestFullScreen();
        } else {
            setStatusBarLightMode();
        }
    }

    @Nullable
    protected TitleBar getTitleBar() {
        return findViewById(R.id.app_toolbar);
    }

    private void setupTitleBar() {
        TitleBar titleBar = getTitleBar();
        if (titleBar != null) {
            titleBar.setNavigationOnClickListener(view -> onBackPressed());
        }
    }

    /***
     * 初始化
     */
    protected void init(Bundle savedInstanceState) {
    }

    /**
     * 设置返回
     */
    @Deprecated
    public void setBack() {
        LinearLayout llBack = findViewById(R.id.ll_back);
        if (llBack != null) {
            llBack.setOnClickListener(v -> onBackPressed());
        }
    }

    public FrameLayout setRightMenu(@StringRes int id) {
        return setRightMenu(getString(id));
    }

    /**
     * 设置右边菜单文字
     */
    @Nullable
    public FrameLayout setRightMenu(final CharSequence menuText) {
        return ToolbarHelper.setRightMenu(getTitleBar(), menuText, view -> clickRightMenu(menuText, view));
    }

    /*设置菜单*/
    public void setOptionsMenu(@MenuRes int menuId) {
        ToolbarHelper.setOptionsMenu(getTitleBar(), menuId, item -> {
            onOptionsMenuClick(item);
            return true;
        });
    }

    @SuppressWarnings("unused")
    protected void onOptionsMenuClick(MenuItem menuItem) {

    }

    /**
     * 设置右边菜单文字
     */
    public void setRightMsg(final boolean hasNewMsg) {
        FrameLayout menuLayout = setRightMenu(getString(R.string.message_title));
        ToolbarHelper.setMessageMenu(menuLayout, hasNewMsg);
    }

    /*显示消息红点*/
    @SuppressWarnings("unused")
    public void setRightDot(@Nullable FrameLayout menuLayout) {
        ToolbarHelper.setMessageRedDot(menuLayout);
    }

    /*移除消息红点*/
    @SuppressWarnings("unused")
    public void hideRightDot(@Nullable FrameLayout menuLayout) {
        ToolbarHelper.hideMessageRedDot(menuLayout);
    }

    /**
     * 点击右边菜单
     */
    protected void clickRightMenu(CharSequence menuText, View actionView) {
    }

    public void setTitle(@StringRes int titleId) {
        setTitle(getString(titleId));
    }

    /**
     * 设置标题
     */
    public void setTitle(CharSequence title) {
        TitleBar titleBar = getTitleBar();
        if (titleBar != null) {
            titleBar.setTitle(title);
        }
    }

    /**
     * 设置搜索栏
     */
    protected EditText setSearchBar(@StringRes int resHint) {
        return setSearchBar(getString(resHint));
    }

    /**
     * 设置搜索栏
     */
    protected EditText setSearchBar(CharSequence hint) {
        return ToolbarHelper.addSearchContainer(getTitleBar(), hint, -1, (searchView, actionId, event) -> doSearch());
    }

    /**
     * 开始搜索
     */
    protected void doSearch() {

    }

    /**
     * 设置标题背景
     */
    @SuppressWarnings("unused")
    protected void setTitleBackground(int resId) {
        TitleBar titleBar = getTitleBar();
        if (titleBar != null) {
            titleBar.setBackgroundResource(resId);
        }
    }

    /**
     * 获取当前activity
     */
    protected BaseActivity getActivity() {
        return this;
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.deAttach();
            mPresenter = null;
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (!BackHandlerHelper.handleBackPress(this)) {
            super.onBackPressed();
        }
    }

    /**
     * 是否充满全屏
     */
    protected boolean isFullScreen() {
        return false;
    }

    public void openActivity(@NonNull Intent intent) {
        openActivity(intent, null);
    }

    public void openActivity(@NonNull Intent intent, @Nullable Bundle options) {
        if (CheckUtils.isFastDoubleClick()) return;
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if (options != null) {
            intent.putExtras(options);
        }
        startActivityForResult(intent, REQUEST_CODE);
    }

    public void startActivity(Class<? extends Activity> activity) {
        startActivity(activity, null);
    }

    public void startActivity(Class<? extends Activity> activity, @Nullable Bundle options) {
        if (CheckUtils.isFastDoubleClick()) return;
        Intent intent = new Intent(this, activity);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if (options != null) {
            intent.putExtras(options);
        }
        startActivityForResult(intent, REQUEST_CODE);
    }

    /**
     * 打开fragment
     */
    public void startFragment(Fragment fragment) {
        startFragment(null, fragment, false);
    }

    public void startFragment(Map<String, Serializable> params, Fragment fragment) {
        startFragment(params, fragment, true);
    }

    public void startFragment(Map<String, Serializable> params, Fragment fragment, boolean needAnimation) {
        if (params != null) {
            Bundle bundle = new Bundle();
            for (Map.Entry<String, Serializable> entry : params.entrySet()) {
                bundle.putSerializable(entry.getKey(), entry.getValue());
            }
            fragment.setArguments(bundle);
        }
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (needAnimation) {
            transaction.setCustomAnimations(R.anim.push_left_in, R.anim.push_right_out);
        }
        transaction.add(R.id.fl_fragment_main, fragment, fragment.getTag()).commitAllowingStateLoss();
        mFragmentList.add(fragment);
    }

    public void startFragment(Fragment fragment, @Nullable Bundle options) {
        startFragment(R.id.fl_fragment_main, fragment, options, true);
    }

    public void startFragment(@IdRes int containerViewId, Fragment fragment, @Nullable Bundle options) {
        startFragment(containerViewId, fragment, options, true);
    }

    public void startFragment(Fragment fragment, @Nullable Bundle options, boolean needAnimation) {
        startFragment(R.id.fl_fragment_main, fragment, options, needAnimation);
    }

    public void startFragment(@IdRes int containerViewId, Fragment fragment, @Nullable Bundle options, boolean needAnimation) {
        if (options != null) {
            fragment.setArguments(options);
        }
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (needAnimation) {
            transaction.setCustomAnimations(R.anim.push_left_in, R.anim.push_right_out);
        }
        transaction.add(containerViewId, fragment, fragment.getTag()).commitAllowingStateLoss();
        mFragmentList.add(fragment);
    }

    /*以下构造方法-当activity只有一个fragment时，采用replace方法*/
    public void replace(Fragment fragment) {
        replace(fragment, false);
    }

    public void replace(Fragment fragment, boolean smoothScroll) {
        replace(fragment, null, smoothScroll);
    }

    public void replace(Fragment fragment, @Nullable Bundle args) {
        replace(fragment, args, false);
    }

    public void replace(Fragment fragment, @Nullable Bundle args, boolean smoothScroll) {
        replace(R.id.fl_fragment_main, fragment, args, smoothScroll);
    }

    public void replace(@IdRes int containerViewId, Fragment fragment, @Nullable Bundle args, boolean smoothScroll) {
        if (args != null) {
            fragment.setArguments(args);
        }
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (smoothScroll) {
            transaction.setCustomAnimations(R.anim.push_left_in, R.anim.push_right_out);
        }
        final String tag = "tag-replace";
        Fragment f = mFragmentManager.findFragmentByTag(tag);
        if (f != null) {
            transaction.show(f);
        } else {
            transaction.replace(containerViewId, fragment, tag);
        }
        transaction.commitAllowingStateLoss();
    }

    /**
     * 关闭fragment
     */
    protected void finishFragment() {
        finishFragment(-1, -1, null);
    }

    protected void finishFragment(int requestCode, int resultCode, Map<String, Serializable> result) {
        finishFragment(requestCode, resultCode, result, true);
    }

    protected void finishFragment(int requestCode, int resultCode, Map<String, Serializable> result, boolean needAnimation) {
        int position = mFragmentList.size() - 1;
        if (position > 0) {
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            if (needAnimation) {
                transaction.setCustomAnimations(R.anim.push_left_in, R.anim.push_right_out);
            }
            transaction.remove(mFragmentList.remove(position)).commitAllowingStateLoss();
            ((BaseFragment) mFragmentList.get(position - 1)).onFinishResult(requestCode, resultCode, result);
        } else {
            finish();
        }
    }

    /**
     * 设置屏幕方向
     */
    protected void setOrientation() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * 隐藏键盘
     */
    protected void hideSoftInput(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && view.getWindowToken() != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 通过设置全屏，设置状态栏透明
     */
    protected void requestFullScreen() {
        SystemTintHelper.fullScreen(this);
    }

    /**
     * 设置状态栏颜色
     */
    public void setStatusBarColor(@ColorRes int colorId) {
        if (!isFullScreen()) {
            SystemTintHelper.setStatusBarColor(this, ContextCompat.getColor(this, colorId));
        } else {
            View statusView = findViewById(R.id.statusView);
            if (statusView != null && colorId != 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                ViewGroup.LayoutParams params = statusView.getLayoutParams();
                params.height = SystemTintHelper.getStatusBarHeight(this);
                statusView.setBackgroundResource(colorId);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VersionUpdateService.REQUEST_CODE_APP_INSTALL) {
            Intent intent = new Intent(VersionUpdateService.ACTION_INSTALL);
            sendBroadcast(intent);
        } else {
            if (requestCode == REQUEST_CODE) {
                onActivityResult(resultCode, data);
            }
        }
    }

    protected void onActivityResult(int resultCode, Intent data) {
    }

    private boolean isContainEmptyView() {
        return findViewById(R.id.ll_empty_page) != null;
    }

    /**
     * 没数据
     */
    public void noData(@DrawableRes int imgId, @StringRes int strId, boolean needReload) {
        noData(imgId, getString(strId), needReload);
    }

    /**
     * 没数据
     */
    public void noData(@DrawableRes int imgId, @Nullable CharSequence text, boolean needReload) {
        if (isContainEmptyView()) {
            findViewById(R.id.ll_empty_page).setVisibility(View.VISIBLE);
            TextView tv = findViewById(R.id.tv_empty_page);
            tv.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(this, imgId), null, null);
            tv.setText(text);
            TextView tvReload = findViewById(R.id.btn_empty_page_reload);
            if (needReload) {
                tvReload.setVisibility(View.VISIBLE);
                tvReload.setOnClickListener(view -> {
                    hasData();
                    reload();
                });
            } else {
                tvReload.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 没有查询结果
     */
    public void noResult() {
        noData(R.drawable.no_result, R.string.tips_noresult, false);
    }

    /**
     * 网络加载出错
     */
    public void noNetwork() {
        noNetwork(null);
    }

    public void noNetwork(String failReason) {
        if (TextUtils.isEmpty(failReason)) {
            noData(R.drawable.no_network, R.string.tips_nonetwork, true);
        } else {
            noData(R.drawable.no_network, failReason, true);
        }
    }

    /*是否是无权限*/
    public boolean isNoAuth(String failed) {
        return TextUtils.equals(failed, getString(R.string.noAuthority)) || TextUtils.equals(failed, getString(R.string.tips_nopermissions));
    }

    public void dealWithFailed(String failed, boolean showToast) {
        dealWithFailed(failed, showToast, -1);
    }

    /*处理接口返回的结果 如果是无权限则展示“当前角色无操作权限”页面*/
    public void dealWithFailed(String failed, boolean showToast, int gravity) {
        if (isNoAuth(failed)) {
            noAuthority();
        } else {
            if (showToast) showShortToast(failed, gravity);
            noNetwork();
        }
    }

    /*无权限*/
    public void noAuthority() {
        noData(R.drawable.no_power, R.string.tips_nopermissions, false);
    }

    /**
     * 重新加载，隐藏没数据提示
     */
    public void hasData() {
        if (isContainEmptyView()) {
            findViewById(R.id.ll_empty_page).setVisibility(View.GONE);
        }
    }

    /**
     * 重新加载
     */
    public void reload() {

    }

    /**
     * 显示正在加载
     */
    public void showLoading() {
        dismissLoading();
        if (mLoadingDialog == null) {
            mLoadingDialog = getLoadingDialog();
        }
        mLoadingDialog.show();
//        mLoadingDialog.show(getSupportFragmentManager(), "loading");
    }

    /**
     * 隐藏正在加载
     */
    public void dismissLoading() {
//        if ((mLoadingDialog = (DialogFragment) getSupportFragmentManager().findFragmentByTag("loading")) != null) {
//            mLoadingDialog.dismissAllowingStateLoss();
//        }
        if (mLoadingDialog != null) mLoadingDialog.dismiss();
    }

    protected Dialog getLoadingDialog() {
        return new LoadingDialog(this);
    }

    @SuppressWarnings("unused")
    public void showShortToast(@StringRes int resId) {
        showShortToast(resId, -1);
    }

    public void showShortToast(@StringRes int resId, int gravity) {
        showShortToast(getString(resId), gravity);
    }

    public void showShortToast(CharSequence text) {
        showShortToast(text, -1);
    }

    public void showShortToast(CharSequence text, int gravity) {
        if (TextUtils.isEmpty(text)) return;
        AppToastCompat.from(getApplicationContext())
                .with(text)
                .setDuration(Toast.LENGTH_SHORT)
                .setGravity(gravity).show();
    }

    @SuppressWarnings("unused")
    public void showLongToast(@StringRes int resId) {
        showLongToast(resId, -1);
    }

    public void showLongToast(@StringRes int resId, int gravity) {
        showLongToast(getString(resId), gravity);
    }

    @SuppressWarnings("unused")
    public void showLongToast(CharSequence text) {
        showLongToast(text, -1);
    }

    public void showLongToast(CharSequence text, int gravity) {
        if (TextUtils.isEmpty(text)) return;
        AppToastCompat.from(getApplicationContext())
                .with(text)
                .setDuration(Toast.LENGTH_LONG)
                .setGravity(gravity).show();
    }

    /**
     * 拨打电话
     */
    public void call(final String phoneNumber) {
        new MaterialDialog.Builder(this).title(R.string.tips_call)
                .message(phoneNumber)
                .negativeButton(R.string.cancel, null).positiveButton(R.string.call, (dialogInterface, i) -> {
            dialogInterface.dismiss();
            callPhone(phoneNumber);
        }).show();
    }

    public void callPhone(String phoneNumber) {
        try {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            Uri data = Uri.parse("tel:" + phoneNumber);
            intent.setData(data);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void copy(String content) {
        if (TextUtils.isEmpty(content)) return;
        CopyUtil.copy(this, content);
        showShortToast("已复制：" + content + " 到粘贴板", CompatToast.Gravity.CENTER);
    }

    /**
     * 获取TextView内容
     */
    public String getText(TextView textView) {
        return textView.getText().toString();
    }

    /*是否授予了权限*/
    public boolean isPermissionGranted(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermission(@NonNull String permission, OnRequestPermissionsCallback callback) {
        requestPermissions(new String[]{permission}, 10086, callback);
    }

    /**
     * 申请单个权限
     *
     * @param permission  需要申请的权限
     * @param requestCode 请求码
     * @param callback    callback
     */
    @SuppressWarnings("unused")
    public void requestPermission(@NonNull String permission, int requestCode, OnRequestPermissionsCallback callback) {
        requestPermissions(new String[]{permission}, requestCode, callback);
    }

    public void requestPermissions(@NonNull final String[] permissions, OnRequestPermissionsCallback callback) {
        requestPermissions(permissions, 10086, callback);
    }

    /**
     * 申请权限
     *
     * @param permissions 需要申请的权限数组
     * @param requestCode 请求码
     * @param callback    请求回调
     */
    public void requestPermissions(@NonNull final String[] permissions, final int requestCode, OnRequestPermissionsCallback callback) {
        this.mPermissionsRequestCode = requestCode;
        this.mRequestPermissionsCallback = callback;
        ActivityCompat.requestPermissions(this, permissions, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == mPermissionsRequestCode) {
            PermissionHelper.convert(this, requestCode, permissions, grantResults, mRequestPermissionsCallback);
        }
    }

    @Override
    public void finish() {
        super.finish();
        if (mActivityCloseEnterAnimation != -1 || mActivityCloseExitAnimation != -1) {
            overridePendingTransition(mActivityCloseEnterAnimation, mActivityCloseExitAnimation);
        }
    }
}
