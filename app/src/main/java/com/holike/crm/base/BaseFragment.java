package com.holike.crm.base;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.holike.crm.R;
import com.holike.crm.customView.AppToastCompat;
import com.holike.crm.customView.CompatToast;
import com.holike.crm.dialog.MaterialDialog;
import com.holike.crm.fragment.FragmentBackHandler;
import com.holike.crm.helper.PickerHelper;
import com.holike.crm.util.AppUtils;
import com.holike.crm.util.CheckUtils;
import com.holike.crm.util.CopyUtil;
import com.holike.crm.util.KeyBoardUtil;
import com.holike.crm.util.LogCat;
import com.holike.crm.util.TimeUtil;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import galloped.xcode.widget.TitleBar;

/**
 * Created by wqj on 2017/9/20.
 */

public abstract class BaseFragment<P extends BasePresenter, V extends BaseView> extends Fragment
        implements View.OnTouchListener, FragmentBackHandler {
    protected final int REQUEST_CODE = new Random().nextInt(65536);
    @SuppressWarnings("WeakerAccess")
    protected final int REQUEST_PERMISSION_CODE = new Random().nextInt(65536);
    protected View mContentView;
    private Unbinder mUnbinder;
    protected P mPresenter;
    protected Context mContext;
    private int mRequestPermissionCode;
    private OnRequestPermissionsCallback mRequestPermissionsCallback;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
//            mPresenter = ((Class<P>) GenericsUtils.getSuperClassGenricType(getClass())).newInstance();
            mPresenter = attachPresenter();
            if (this instanceof BaseView) {
                if (mPresenter != null)
                    mPresenter.attach((V) this);
            }
        } catch (Exception e) {
            LogCat.e(e);
        }
    }

    protected abstract P attachPresenter();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (setContentViewId() == 0) {
            return super.onCreateView(inflater, container, savedInstanceState);
        } else {
            if (mContentView == null) {
                mContentView = inflater.inflate(setContentViewId(), container, false);
                mContentView.setClickable(true);
                mUnbinder = ButterKnife.bind(this, mContentView);
                setupTitleBar();
                init();
                mContentView.setOnTouchListener(this);
            }
            return mContentView;
        }
    }

    protected abstract int setContentViewId();

    protected void init() {

    }

    public View getContentView() {
        return mContentView;
    }

    @Nullable
    protected TitleBar getTitleBar() {
        return mContentView.findViewById(R.id.app_toolbar);
    }

    private void setupTitleBar() {
        TitleBar titleBar = getTitleBar();
        if (titleBar != null) {
            titleBar.setNavigationOnClickListener(this::onNavigationClick);
        }
    }

    protected void setNavigationIcon(@DrawableRes int resId) {
        Drawable icon = null;
        if (resId != 0) {
            icon = ContextCompat.getDrawable(mContext, resId);
        }
        setNavigationIcon(icon);
    }

    protected void setNavigationIcon(@Nullable Drawable icon) {
        TitleBar titleBar = getTitleBar();
        if (titleBar != null) {
            titleBar.setNavigationIcon(icon);
        }
    }

    protected void onNavigationClick(View view) {
        KeyBoardUtil.hideKeyboard(view);
        back();
    }

    public void setTitle(@StringRes int resId) {
        setTitle(mContext.getString(resId));
    }

    /**
     * 设置主题
     *
     * @param title a
     */
    public void setTitle(CharSequence title) {
//        TextView tvTitle = mFragmentView.findViewById(R.id.tv_title);
//        if (tvTitle != null) {
//            tvTitle.setText(title);
//        }
        TitleBar toolbar = getTitleBar();
        if (toolbar != null) {
//            ((TextView) toolbar.findViewById(R.id.tv_appbar_title)).setText(title);
            toolbar.setTitle(title);
        }
    }

    protected void copy(String content) {
        if (TextUtils.isEmpty(content)) return;
        CopyUtil.copy(mContext, content);
        showShortToast("已复制：" + content + " 到粘贴板", CompatToast.Gravity.CENTER);
    }

    /**
     * 复制文字到剪贴板
     *
     * @param view    需要长按的控件
     * @param content 内容
     */
    protected void copy(View view, String content) {
        view.setOnLongClickListener(v -> {
            copy(content);
            return true;
        });

    }

    /**
     * 设置标题背景
     */
    @SuppressWarnings("SameParameterValue")
    protected void setTitleBackground(@DrawableRes int resId) {
        ToolbarHelper.setTitleBackgroundResource(getTitleBar(), resId);
    }

    /**
     * 设置搜索栏
     */
    protected EditText setSearchBar(@StringRes int resHint) {
        return setSearchBar(mContext.getString(resHint));
    }

    protected EditText setSearchBar(@StringRes int resHint, @DrawableRes int resId) {
        return setSearchBar(mContext.getString(resHint), resId);
    }

    /**
     * 设置搜索栏
     */
    protected EditText setSearchBar(CharSequence hint) {
        return setSearchBar(hint, -1);
    }

    /**
     * 设置搜索栏
     */
    protected EditText setSearchBar(CharSequence hint, @DrawableRes int resId) {
        return ToolbarHelper.addSearchContainer(getTitleBar(), hint, resId, (searchView, actionId, event) -> doSearch());
    }

    /**
     * 开始搜索
     */
    protected void doSearch() {

    }

    @Nullable
    public FrameLayout setRightMenu(@StringRes int id) {
        return setRightMenu(id, null);
    }

    @Nullable
    public FrameLayout setRightMenu(@StringRes int id, View.OnClickListener listener) {
        return setRightMenu(mContext.getString(id), listener);
    }

    /**
     * 设置右边菜单文字
     */
    @Nullable
    public FrameLayout setRightMenu(final CharSequence text) {
        return setRightMenu(text, null);
    }

    @Nullable
    public FrameLayout setRightMenu(final CharSequence text, @Nullable View.OnClickListener listener) {
        if (listener == null) {
            return ToolbarHelper.setRightMenu(getTitleBar(), text, view -> clickRightMenu(text, view));
        } else {
            return ToolbarHelper.setRightMenu(getTitleBar(), text, listener);
        }
    }

    /*隐藏右边菜单栏*/
    public void hideRightMenu() {
        ToolbarHelper.hideOptionsMenu(getTitleBar());
    }

    public void setOptionsMenu(@MenuRes int menuId) {
        final TitleBar titleBar = getTitleBar();
        ToolbarHelper.setOptionsMenu(getTitleBar(), menuId, item -> {
            onOptionsMenuClick(titleBar, item);
            return true;
        });
    }

    /**
     * 设置右边菜单图标
     */
    protected void setRightMenuMsg(final boolean isNewMsg) {
        FrameLayout menuLayout = setRightMenu(getString(R.string.message_title));
        ToolbarHelper.setMessageMenu(menuLayout, isNewMsg);
    }

    /*显示消息红点*/
    @SuppressWarnings("unused")
    protected void setRightDot(@Nullable FrameLayout menuLayout) {
        ToolbarHelper.setMessageRedDot(menuLayout);
    }

    /*移除消息红点*/
    @SuppressWarnings("unused")
    protected void hideRightDot(@Nullable FrameLayout menuLayout) {
        ToolbarHelper.hideMessageRedDot(menuLayout);
    }

    public void onOptionsMenuClick(Toolbar toolbar, MenuItem menuItem) {

    }

    /**
     * 点击右边菜单
     */
    protected void clickRightMenu(CharSequence menuText, View actionView) {
    }

    /**
     * 设置返回
     */
    public void setBack() {
        LinearLayout llBack = mContentView.findViewById(R.id.ll_back);
        if (llBack != null) {
            llBack.setOnClickListener(v -> {
                KeyBoardUtil.hideKeyboard(v);
                back();
            });
        }
    }

    /**
     * 返回
     */
    protected void back() {
        finishFragment();
    }

    @Override
    public boolean onBackPressed() {
        finishFragment();
        return true;
    }

    /**
     * 打开fragment
     */
    public void startFragment(Fragment fragment) {
        if (mContext instanceof BaseActivity) {
            ((BaseActivity) mContext).startFragment(fragment);
        }
    }

    public void startFragment(Fragment fragment, boolean needAnim) {
        if (mContext instanceof BaseActivity) {
            ((BaseActivity) mContext).startFragment(null, fragment, needAnim);
        }
    }

    public void startFragment(Map<String, Serializable> params, Fragment fragment) {
        if (mContext instanceof BaseActivity) {
            ((BaseActivity) mContext).startFragment(params, fragment);
        }
    }

    public void startFragment(Fragment fragment, @Nullable Bundle options) {
        if (mContext instanceof BaseActivity) {
            ((BaseActivity) mContext).startFragment(fragment, options);
        }
    }

    public void startFragment(Fragment fragment, @Nullable Bundle options, boolean needAnimation) {
        if (mContext instanceof BaseActivity) {
            ((BaseActivity) mContext).startFragment(fragment, options, needAnimation);
        }
    }

    /**
     * 关闭fragment
     */
    protected void finishFragment() {
        if (mContext instanceof BaseActivity)
            ((BaseActivity) mContext).finishFragment();
    }

    protected void finishFragment(int requestCode, int resultCode, Map<String, Serializable> result) {
        if (mContext instanceof BaseActivity)
            ((BaseActivity) mContext).finishFragment(requestCode, resultCode, result);
    }

    /**
     * 显示loading
     */
    protected void showLoading() {
        if (mContext instanceof BaseActivity)
            ((BaseActivity) mContext).showLoading();
    }

    /**
     * 隐藏loading
     */
    protected void dismissLoading() {
        if (mContext instanceof BaseActivity)
            ((BaseActivity) mContext).dismissLoading();
    }

    protected void selectTime(Date date) {
    }

//    private TimePickerView pvTime;

    /**
     * 选择时间
     */
    @Deprecated
    protected void showTimePickerView(Context context, String time, View view) {
        if (view != null) {
            hideSoftInput(view);
        }
        Date selectDate;
        if (TextUtils.isEmpty(time) || TextUtils.equals(time, "无法显示时间")) {
            selectDate = new Date();
        } else {
            selectDate = TimeUtil.stringToCalendar(time, "yyyy.MM.dd").getTime();
        }
        PickerHelper.showTimePicker2(mContext, selectDate, this::selectTime);
    }

    protected void showTimePickerView(String time, View view) {
        if (view != null) {
            hideSoftInput(view);
        }
        Date selectDate;
        if (TextUtils.isEmpty(time) || TextUtils.equals(time, "无法显示时间")) {
            selectDate = new Date();
        } else {
            selectDate = TimeUtil.stringToCalendar(time, "yyyy.MM.dd").getTime();
        }
        PickerHelper.showTimePicker2(mContext, selectDate, this::selectTime);
    }

    protected void onFinishResult(int requestCode, int resultCode, Map<String, Serializable> result) {
    }

    @Override
    public void onDestroyView() {
        if (mPresenter != null) {
            mPresenter.deAttach();
        }
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (KeyBoardUtil.isKeyboardShown(mContentView))
            KeyBoardUtil.hideKeyboard(mContentView);
    }

    public BaseFragment<?, ?> getFragment() {
        return this;
    }

    public void openActivity(Intent intent) {
        openActivity(intent, null);
    }

    public void openActivity(Intent intent, @Nullable Bundle options) {
        if (getActivity() == null) return;
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);  //栈顶模式启动activity，避免快速点击按钮启动多次的问题
        if (options != null) {
            intent.putExtras(options);
        }
        startActivity(intent);
    }

    public void openActivityForResult(Intent intent) {
        openActivityForResult(intent, null);
    }

    public void openActivityForResult(Intent intent, Bundle options) {
        openActivityForResult(intent, REQUEST_CODE, options);
    }

    public void openActivityForResult(Intent intent, int requestCode, Bundle options) {
        if (getActivity() == null || CheckUtils.isFastDoubleClick()) return;
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if (options != null) {
            intent.putExtras(options);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * 启动activity
     */
    public void startActivity(Class c) {
        if (getActivity() == null) return;
        startActivityForResult(c, REQUEST_CODE);
    }

    /**
     * 启动activity
     */
    public void startActivity(Class c, Bundle bundle) {
        if (getActivity() == null) return;
        startActivityForResult(c, bundle, REQUEST_CODE);
    }

    public void startActivityForResult(Class c, int requestCode) {
        if (getActivity() == null) return;
        startActivityForResult(c, null, requestCode);
    }

    public void startActivityForResult(Class c, Bundle bundle, int requestCode) {
        if (getActivity() == null || CheckUtils.isFastDoubleClick()) return;
        Intent intent = new Intent(getActivity(), c);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); //栈顶模式 启动activity
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * 设置导航栏颜色
     */
    protected void setStatusBar() {
        setStatusBar(0);
    }

    protected void setStatusBar(int resid) {
        View statusView = mContentView.findViewById(R.id.statusView);
        if (statusView != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ViewGroup.LayoutParams params = statusView.getLayoutParams();
            params.height = SystemTintHelper.getStatusBarHeight(mContext);
            if (resid != 0) {
                statusView.setBackgroundResource(resid);
            }
        }
    }

    protected void setTextColor(TextView view, int colorId) {
        view.setTextColor(getResources().getColor(colorId));
    }

    private boolean isContainEmptyView() {
        return mContentView.findViewById(R.id.ll_empty_page) != null;
    }

    /**
     * 没有数据
     */
    protected void noData(@DrawableRes int imgId, @StringRes int strId, boolean needReload) {
        noData(imgId, mContext.getString(strId), needReload);
    }

    /**
     * 没有数据
     */
    protected void noData(@DrawableRes int imgId, @Nullable CharSequence text, boolean needReload) {
        if (isContainEmptyView()) {
            mContentView.findViewById(R.id.ll_empty_page).setVisibility(View.VISIBLE);
            TextView tv = mContentView.findViewById(R.id.tv_empty_page);
            tv.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(mContext, imgId), null, null);
            tv.setText(text);
            TextView tvReload = mContentView.findViewById(R.id.btn_empty_page_reload);
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
     * 重新加载，隐藏没有数据提示
     */
    protected void hasData() {
        if (isContainEmptyView()) {
            mContentView.findViewById(R.id.ll_empty_page).setVisibility(View.GONE);
        }
    }

    /**
     * 重新加载
     */
    protected void reload() {

    }

    /**
     * 隐藏键盘
     */
    protected void hideSoftInput(View view) {
        if (getActivity() != null) {
            ((BaseActivity) getActivity()).hideSoftInput(view);
        }
    }

    /**
     * 没有查询结果
     */
    public void noResult() {
        noData(R.drawable.no_result, R.string.tips_noresult, false);
    }

    /**
     * 没有查询结果
     * strRes  内容
     */
    public void noResult(String strRes) {
        noData(R.drawable.no_result, strRes, false);
    }

    /**
     * 没有网络
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
    protected boolean isNoAuth(String failed) {
        return TextUtils.equals(failed, mContext.getString(R.string.noAuthority)) || TextUtils.equals(failed, mContext.getString(R.string.tips_nopermissions));
    }

    @SuppressWarnings("WeakerAccess")
    public void dealWithFailed(String failed, boolean showToast) {
        dealWithFailed(failed, showToast, -1);
    }

    @SuppressWarnings("WeakerAccess")
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
     * 打电话
     */
    public void call(final String phoneNumber) {
        if (getActivity() == null) return;
        ((BaseActivity) getActivity()).call(phoneNumber);
    }

    public void callPhone(final String phoneNumber) {
        if (!TextUtils.isEmpty(phoneNumber)) {
            new MaterialDialog.Builder(mContext)
                    .title(R.string.tips_call)
                    .message(phoneNumber)
                    .negativeButton(R.string.cancel, null)
                    .positiveButton(R.string.call, (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                        if (hasCallPermission()) {
                            onActionCall(phoneNumber);
                        } else {
                            onRequestCallPhone(phoneNumber);
                        }
                    }).show();
        }
    }

    /*是否已经注册了拨打电话权限*/
    @SuppressWarnings("WeakerAccess")
    public boolean hasCallPermission() {
        return ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED;
    }

    @SuppressLint("MissingPermission")
    public void onActionCall(String phoneNumber) {
        try {
            Intent intent = new Intent(Intent.ACTION_CALL);
            Uri data = Uri.parse("tel:" + phoneNumber);
            intent.setData(data);
            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*请求拨打电话的权限*/
    @SuppressWarnings("WeakerAccess")
    protected void onRequestCallPhone(String phoneNumber) {
        requestPermission(Manifest.permission.CALL_PHONE, new OnRequestPermissionsCallback() {

            @Override
            public void onGranted(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
                onActionCall(phoneNumber);  //用户同意
            }

            @Override
            public void onDenied(int requestCode, @NonNull String[] permissions, boolean isProhibit) {
                /*用户禁止或不再访问*/
                onDismissCallPermission(isProhibit);
            }
        });
    }

    /*用户禁止拨打电话权限*/
    protected void onDismissCallPermission(boolean isProhibit) {
        if (isProhibit) {
            new MaterialDialog.Builder(mContext)
                    .title(R.string.dialog_title_default)
                    .message(R.string.tips_dismiss_callPhone)
                    .negativeButton(R.string.cancel, null)
                    .positiveButton(R.string.call, (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                        AppUtils.openSettings(mContext);
                    }).show();
        }
    }

    /**
     * 判断TextView是否空
     */
    public boolean isTextEmpty(TextView textView) {
        return textView.getText().toString().length() == 0;
    }

    /**
     * 获取TextView内容
     */
    public String getText(TextView textView) {
        return textView.getText().toString();
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            KeyBoardUtil.hideSoftInput((Activity) mContext);
        }
        return false;
    }

    @SuppressWarnings("unused")
    public void showShortToast(@StringRes int resId) {
        showShortToast(resId, -1);
    }

    public void showShortToast(@StringRes int resId, int gravity) {
        showShortToast(mContext.getString(resId), gravity);
    }

    public void showShortToast(CharSequence text) {
        showShortToast(text, -1);
    }

    public void showShortToast(CharSequence text, int gravity) {
        if (TextUtils.isEmpty(text)) return;
        AppToastCompat.from(mContext.getApplicationContext())
                .with(text)
                .setDuration(Toast.LENGTH_SHORT)
                .setGravity(gravity).show();
    }

    @SuppressWarnings("unused")
    public void showLongToast(@StringRes int resId) {
        showLongToast(resId, -1);
    }

    @SuppressWarnings("WeakerAccess")
    public void showLongToast(@StringRes int resId, int gravity) {
        showLongToast(mContext.getString(resId), gravity);
    }

    @SuppressWarnings("unused")
    public void showLongToast(CharSequence text) {
        showLongToast(text, -1);
    }

    @SuppressWarnings("WeakerAccess")
    public void showLongToast(CharSequence text, int gravity) {
        if (TextUtils.isEmpty(text)) return;
        AppToastCompat.from(mContext.getApplicationContext())
                .with(text)
                .setDuration(Toast.LENGTH_LONG)
                .setGravity(gravity).show();
    }

    @SuppressWarnings("WeakerAccess")
    public void requestPermission(@NonNull String permission, OnRequestPermissionsCallback callback) {
        requestPermission(permission, REQUEST_PERMISSION_CODE, callback);
    }

    @SuppressWarnings("WeakerAccess")
    public void requestPermission(@NonNull String permission, int requestCode, OnRequestPermissionsCallback callback) {
        requestPermissions(new String[]{permission}, requestCode, callback);
    }

    protected void requestPermissions(String[] permissions, int requestCode, OnRequestPermissionsCallback callback) {
        this.mRequestPermissionCode = requestCode;
        this.mRequestPermissionsCallback = callback;
        requestPermissions(permissions, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == mRequestPermissionCode) {
            PermissionHelper.convert(this, requestCode, permissions, grantResults, mRequestPermissionsCallback);
        }
    }
}
