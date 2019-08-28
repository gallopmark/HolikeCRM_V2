package com.holike.crm.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.holike.crm.R;
import com.holike.crm.customView.AppToastCompat;
import com.holike.crm.customView.CompatToast;
import com.holike.crm.dialog.LoadingDialog;
import com.holike.crm.dialog.MaterialDialog;
import com.holike.crm.helper.BackHandlerHelper;
import com.holike.crm.util.CheckUtils;
import com.holike.crm.util.CopyUtil;
import com.holike.crm.util.LogCat;
import com.umeng.analytics.MobclickAgent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.ButterKnife;

public abstract class BaseActivity<P extends BasePresenter, V extends BaseView> extends AppCompatActivity {
    public final int REQUEST_CODE = new Random().nextInt(65536);
    protected CountDownTimer timer;
    protected P mPresenter;
    protected Dialog loadingDialog;
    /*权限申请请求码*/
    private int mPermissionsRequestCode;
    /*权限申请回调*/
    private OnRequestPermissionsCallback mRequestPermissionsCallback;
    protected FragmentManager fragmentManager;
    protected List<Fragment> fragmentList = new ArrayList<>();

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setOrientation();
        setContentView(setContentViewId());
        ButterKnife.bind(this);
        fragmentManager = getSupportFragmentManager();
        try {
            mPresenter = attachPresenter();
//            mPresenter = ((Class<P>) GenericsUtils.getSuperClassGenricType(getClass())).newInstance();
            if (this instanceof BaseView) {
                if (mPresenter != null) mPresenter.attach((V) this);
            }
        } catch (Exception e) {
            LogCat.e(e);
        }
        if (isFullScreen()) {
            fullScreen();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setStatusBarColor(R.color.color_while);
            } else {
                setStatusBarColor(R.color.bg_state_bar);
            }
            SystemTintHelper.setStatusBarLightMode(this);
        }
        setupToolbar();
        init();
    }

    protected abstract P attachPresenter();

    /**
     * 设置layout
     */
    protected abstract int setContentViewId();

    @Nullable
    protected Toolbar getToolbar() {
        return findViewById(R.id.app_toolbar);
    }

    private void setupToolbar() {
        Toolbar toolbar = getToolbar();
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(view -> onBackPressed());
        }
    }

    /***
     * 初始化
     */
    protected void init() {
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

    protected void setRightMenu(@StringRes int id) {
        setRightMenu(getString(id));
    }

    /**
     * 设置右边菜单文字
     */
    protected void setRightMenu(final String text) {
        final Toolbar toolbar = getToolbar();
        if (toolbar != null) {
            View view = getMenuLayout(toolbar);
            if (view != null) {
                view.setVisibility(View.VISIBLE);
            }
            TextView tvMenu = toolbar.findViewById(R.id.tv_menu);
            if (tvMenu != null) {
                tvMenu.setText(text);
                tvMenu.setOnClickListener(v -> clickRightMenu());
            }
        }
    }

    protected void setOptionsMenu(@MenuRes int menuId) {
        final Toolbar toolbar = getToolbar();
        if (toolbar != null) {
            View view = getMenuLayout(toolbar);
            if (view != null) {
                view.setVisibility(View.GONE);
            }
            ToolbarHelper.inflateMenu(toolbar, menuId);
            toolbar.setOnMenuItemClickListener(item -> {
                onOptionsMenuClick(toolbar, item);
                return true;
            });
        }
    }

    @Nullable
    protected View getMenuLayout(Toolbar toolbar) {
        return toolbar.findViewById(R.id.fl_menu_layout);
    }

    protected void onOptionsMenuClick(Toolbar toolbar, MenuItem menuItem) {
        clickRightMenu();
    }

    /**
     * 设置右边菜单文字
     */
    protected void setRightMsg(final boolean isNewMsg) {
        final Toolbar toolbar = getToolbar();
        if (toolbar != null) {
            View view = getMenuLayout(toolbar);
            if (view != null) {
                view.setVisibility(View.VISIBLE);
            }
            ImageView ivNewMsg = toolbar.findViewById(R.id.iv_new_tips);
            if (ivNewMsg != null) {
                ivNewMsg.setVisibility(isNewMsg ? View.VISIBLE : View.GONE);
            }
            TextView tvMenu = toolbar.findViewById(R.id.tv_menu);
            String text = getString(R.string.message_title);
            tvMenu.setText(text);
            tvMenu.setOnClickListener(v -> clickRightMenu());
        }
    }

    /**
     * 点击右边菜单
     */
    protected void clickRightMenu() {
    }

    /**
     * 设置左边菜单文字
     */
    @Deprecated
    protected void setLeft(String left) {
//        TextView tvLeft = findViewById(R.id.tv_left);
//        if (tvLeft != null) {
//            if (left == null || left.equals("")) {
//                tvLeft.setVisibility(View.GONE);
//            } else
//                tvLeft.setText(getString(R.string.back));
//            if (tvLeft.getVisibility() != View.GONE) {
//                tvLeft.setVisibility(View.GONE);
//            }
//        }
    }

    public void setTitle(@StringRes int titleId) {
        setTitle(getString(titleId));
    }

    /**
     * 设置标题
     */
    public void setTitle(CharSequence title) {
        TextView tvTitle = findViewById(R.id.tv_title);
        if (tvTitle != null) {
            tvTitle.setText(title);
        }
        Toolbar toolbar = getToolbar();
        if (toolbar != null) {
            toolbar.setTitle(title);
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
        Toolbar toolbar = getToolbar();
        if (toolbar == null) return null;
        return ToolbarHelper.addSearchContainer(toolbar, hint, (searchView, actionId, event) -> doSearch());
    }

    /**
     * 开始搜索
     */
    protected void doSearch() {

    }

    /**
     * 设置标题背景
     */
    protected void setTitleBg(int resId) {
        Toolbar toolbar = getToolbar();
        if (toolbar != null) {
            toolbar.setBackgroundResource(resId);
        }
    }

    /**
     * 获取当前activity
     */
    protected BaseActivity getActivity() {
        return this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (mPresenter != null) {
            mPresenter.deAttach();
            mPresenter = null;
        }
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

    /**
     * 是否修改导航栏颜色
     */
    protected boolean isSetStatusBarColor() {
        return true;
    }

    public void openActivity(@NonNull Intent intent) {
        openActivity(intent, null);
    }

    public void openActivity(@NonNull Intent intent, @Nullable Bundle options) {
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
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (needAnimation) {
            transaction.setCustomAnimations(R.anim.push_left_in, R.anim.push_right_out);
        }
        transaction.add(R.id.fl_fragment_main, fragment, fragment.getTag()).commitAllowingStateLoss();
        fragmentList.add(fragment);
    }

    public void startFragment(Fragment fragment, @Nullable Bundle options) {
        startFragment(fragment, options, true);
    }

    public void startFragment(Fragment fragment, @Nullable Bundle options, boolean needAnimation) {
        if (options != null) {
            fragment.setArguments(options);
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (needAnimation) {
            transaction.setCustomAnimations(R.anim.push_left_in, R.anim.push_right_out);
        }
        transaction.add(R.id.fl_fragment_main, fragment, fragment.getTag()).commitAllowingStateLoss();
        fragmentList.add(fragment);
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
        int position = fragmentList.size() - 1;
        if (position > 0) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            if (needAnimation) {
                transaction.setCustomAnimations(R.anim.push_left_in, R.anim.push_right_out);
            }
            transaction.remove(fragmentList.get(position)).commitAllowingStateLoss();
            fragmentList.remove(position);
            ((MyFragment) fragmentList.get(position - 1)).onFinishResult(requestCode, resultCode, result);
        } else {
            finish();
        }
//        if (position == 0) {
//            finish();
//        } else {
//            FragmentTransaction transaction = fragmentManager.beginTransaction();
//            if (needAnimation) {
//                transaction.setCustomAnimations(R.anim.push_left_in, R.anim.push_right_out);
//            }
//            transaction.remove(fragmentList.getInstance(position)).commitAllowingStateLoss();
//            fragmentList.remove(position);
//            ((MyFragment) fragmentList.getInstance(position - 1)).onFinishResult(requestCode, resultCode, result);
//        }
    }

    /**
     * 设置屏幕方向
     */
    protected void setOrientation() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * 返回
     */
    public void back(View view) {
        hideSoftInput(getWindow().getDecorView());
        finish();
    }

    /**
     * 显示键盘
     */
    protected void showSoftInput(View view) {
        if (view instanceof EditText) {
            ((EditText) view).setSelection(((EditText) view).getText().toString().length());
        }
        view.requestFocus();
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 隐藏键盘
     */
    protected void hideSoftInput(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 通过设置全屏，设置状态栏透明
     */
    protected void fullScreen() {
        SystemTintHelper.fullScreen(this);
//        SystemBarTintHelper.fullScreen(this);
    }

    /**
     * 设置状态栏颜色
     */
    public void setStatusBarColor(int colorId) {
        if (!isFullScreen()) {
            SystemTintHelper.setStatusBarColor(this, ContextCompat.getColor(this, colorId));
        } else {
            View statusView = findViewById(R.id.statusView);
            if (statusView != null && colorId != 0) {
                ViewGroup.LayoutParams params = statusView.getLayoutParams();
                params.height = SystemTintHelper.getStatusBarHeight(this);
                statusView.setBackgroundResource(colorId);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CODE == requestCode) {
            onActivityResult(resultCode, data);
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
    public void noData(int imgId, int strId, boolean needReload) {
        if (isContainEmptyView()) {
            noDataImg(imgId, needReload);
            ((TextView) findViewById(R.id.tv_empty_page)).setText(strId);
        }
    }

    /**
     * 没数据
     */
    public void noData(int imgId, String strId, boolean needReload) {
        if (isContainEmptyView()) {
            noDataImg(imgId, needReload);
            ((TextView) findViewById(R.id.tv_empty_page)).setText(strId);
        }
    }

    public void noDataImg(int imgId, boolean needReload) {
        if (isContainEmptyView()) {
            findViewById(R.id.ll_empty_page).setVisibility(View.VISIBLE);
            ((ImageView) findViewById(R.id.iv_empty_page)).setImageResource(imgId);
            if (needReload) {
                findViewById(R.id.btn_empty_page_reload).setVisibility(View.VISIBLE);
                findViewById(R.id.btn_empty_page_reload).setOnClickListener(v -> {
                    hasData();
                    reload();
                });
            } else {
                findViewById(R.id.btn_empty_page_reload).setVisibility(View.GONE);
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
        if (loadingDialog == null) {
            loadingDialog = getLoadingDialog();
        }
        loadingDialog.show();
//        loadingDialog.show(getSupportFragmentManager(), "loading");
    }

    /**
     * 隐藏正在加载
     */
    public void dismissLoading() {
//        if ((loadingDialog = (DialogFragment) getSupportFragmentManager().findFragmentByTag("loading")) != null) {
//            loadingDialog.dismissAllowingStateLoss();
//        }
        if (loadingDialog != null) loadingDialog.dismiss();
    }

    protected Dialog getLoadingDialog() {
        return new LoadingDialog(this);
    }

    @SuppressWarnings("unused")
    protected void showShortToast(@StringRes int resId) {
        showShortToast(resId, -1);
    }

    protected void showShortToast(@StringRes int resId, int gravity) {
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
     * 判断TextView是否空
     */
    public boolean textEmpty(TextView textView) {
        return textView.getText().toString().length() == 0;
    }

    /**
     * 获取TextView内容
     */
    public String getText(TextView textView) {
        return textView.getText().toString();
    }

    /*是否授予了权限*/
    protected boolean isPermissionGranted(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 申请单个权限
     *
     * @param permission  需要申请的权限
     * @param requestCode 请求码
     * @param callback    callback
     */
    protected void requestPermission(@NonNull String permission, int requestCode, OnRequestPermissionsCallback callback) {
        requestPermissions(new String[]{permission}, requestCode, callback);
    }

    /**
     * 申请权限
     *
     * @param permissions 需要申请的权限数组
     * @param requestCode 请求码
     * @param callback    请求回调
     */
    protected void requestPermissions(@NonNull final String[] permissions, final int requestCode, OnRequestPermissionsCallback callback) {
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

    /**
     * 做法是为了快速点击按钮，多次触发启动activity等问题
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (CheckUtils.isFastDoubleClick()) {
                return true;
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}
