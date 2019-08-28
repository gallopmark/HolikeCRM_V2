package com.holike.crm.base;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.view.TimePickerView;
import com.holike.crm.R;
import com.holike.crm.customView.AppToastCompat;
import com.holike.crm.customView.CompatToast;
import com.holike.crm.dialog.MaterialDialog;
import com.holike.crm.fragment.FragmentBackHandler;
import com.holike.crm.util.AppUtils;
import com.holike.crm.util.CopyUtil;
import com.holike.crm.util.KeyBoardUtil;
import com.holike.crm.util.LogCat;
import com.holike.crm.util.TimeUtil;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by wqj on 2017/9/20.
 */

public abstract class BaseFragment<P extends BasePresenter, V extends BaseView> extends Fragment
        implements View.OnTouchListener, FragmentBackHandler {
    protected final int REQUEST_CODE = new Random().nextInt(65536);
    protected final int REQUEST_PERMISSION_CODE = new Random().nextInt(65536);
    protected View mContentView;
    protected P mPresenter;
    protected Context mContext;
    private int mRequestPermissionCode;
    private OnRequestPermissionsCallback mRequestPermissionsCallback;

    @Override
    public void onAttach(Context context) {
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

    protected Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (mContentView == null) {
            mContentView = inflater.inflate(setContentViewId(), container, false);
            mContentView.setClickable(true);
            unbinder = ButterKnife.bind(this, mContentView);
            setupToolbar();
            init();
            mContentView.setOnTouchListener(this);

        }
        return mContentView;
    }

    protected abstract int setContentViewId();

    protected void init() {

    }

    public View getContentView() {
        return mContentView;
    }

    @Nullable
    protected Toolbar getToolbar() {
        return mContentView.findViewById(R.id.app_toolbar);
    }

    private void setupToolbar() {
        Toolbar toolbar = getToolbar();
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(view -> {
                KeyBoardUtil.hideKeyboard(view);
                back();
            });
        }
    }

    /**
     * 设置主题
     *
     * @param title a
     */
    protected void setTitle(CharSequence title) {
//        TextView tvTitle = mContentView.findViewById(R.id.tv_title);
//        if (tvTitle != null) {
//            tvTitle.setText(title);
//        }
        Toolbar toolbar = getToolbar();
        if (toolbar != null) {
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
     * 设置左边按钮文字
     */
    @Deprecated
    protected void setLeft(String left) {
//        TextView tvLeft = mContentView.findViewById(R.id.tv_left);
//        if (tvLeft != null) {
//            if (left == null || left.equals("")) {
//                tvLeft.setVisibility(View.GONE);
//            } else
//                tvLeft.setText(getString(R.string.back));
//            tvLeft.setVisibility(View.GONE);
//        }
    }

    /**
     * 设置标题背景
     */
    protected void setTitleBg(@DrawableRes int resId) {
//        View flTitle = mContentView.findViewById(R.id.fl_fragment_title_bar);
//        if (flTitle != null) {
//            flTitle.setBackgroundResource(resId);
//        }
        Toolbar toolbar = getToolbar();
        if (toolbar != null) {
            toolbar.setBackgroundResource(resId);
        }
    }

    /**
     * 设置搜索栏
     */
    protected EditText setSearchBar(@StringRes int resHint) {
        return setSearchBar(mContext.getString(resHint));
    }

    /**
     * 设置搜索栏
     */
    protected EditText setSearchBar(CharSequence hint) {
//        LinearLayout mSearchLayout = mContentView.findViewById(R.id.mSearchLayout);
//        if (mSearchLayout != null) mSearchLayout.setVisibility(View.VISIBLE);
//        EditText editText = mContentView.findViewById(R.id.et_search);
//        ImageView ivClear = mContentView.findViewById(R.id.iv_clear);
//        if (editText != null) {
//            editText.setVisibility(View.VISIBLE);
//            editText.setHint(hint);
//            ivClear.setOnClickListener(v -> editText.setText(""));
//            editText.setOnEditorActionListener((v, actionId, event) -> {
//                        if ((actionId == EditorInfo.IME_NULL || actionId == EditorInfo.IME_ACTION_SEARCH) && event == null) {
//                            doSearch();
//                        }
//                        return false;
//                    }
//            );
//            editText.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    if (!TextUtils.isEmpty(s.toString().trim())) {
//                        ivClear.setVisibility(View.VISIBLE);
//                    } else {
//                        ivClear.setVisibility(View.GONE);
//                    }
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//
//                }
//            });
//        }
//        return editText;
        Toolbar toolbar = getToolbar();
        if (toolbar == null) return null;
        return ToolbarHelper.addSearchContainer(toolbar, hint, (searchView, actionId, event) -> doSearch());
    }

    /**
     * 开始搜索
     */
    protected void doSearch() {

    }

    public void setRightMenu(@StringRes int id) {
        setRightMenu(id, null);
    }

    public void setRightMenu(@StringRes int id, View.OnClickListener listener) {
        setRightMenu(mContext.getString(id), listener);
    }

    /**
     * 设置右边菜单文字
     */
    public void setRightMenu(final String text) {
        setRightMenu(text, null);
    }

    public void setRightMenu(final String text, @Nullable View.OnClickListener listener) {
        final Toolbar toolbar = getToolbar();
        if (toolbar != null) {
            View view = getMenuLayout(toolbar);
            if (view != null) {
                view.setVisibility(View.VISIBLE);
            }
            TextView tvMenu = toolbar.findViewById(R.id.tv_menu);
            tvMenu.setText(text);
            if (listener != null) {
                tvMenu.setOnClickListener(listener);
            } else {
                tvMenu.setOnClickListener(v -> clickRightMenu(text, tvMenu));
            }
        }
    }

    /*隐藏右边菜单栏*/
    public void hideRightMenu() {
        final Toolbar toolbar = getToolbar();
        if (toolbar != null) {
            View view = getMenuLayout(toolbar);
            if (view != null) {
                view.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void setOptionsMenu(@MenuRes int menuId) {
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
    private View getMenuLayout(Toolbar toolbar) {
        return toolbar.findViewById(R.id.fl_menu_layout);
    }

    /**
     * 设置右边菜单图标
     */
    public void setRightMenuMsg(final boolean isNewMsg) {
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
            String text = mContext.getString(R.string.message_title);
            tvMenu.setText(text);
            tvMenu.setOnClickListener(v -> clickRightMenu(text, tvMenu));
        }
    }

    public void onOptionsMenuClick(Toolbar toolbar, MenuItem menuItem) {
        clickRightMenu("", toolbar);
    }

    /**
     * 点击右边菜单
     */
    protected void clickRightMenu(String menuText, View actionView) {
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
        if (mContext instanceof MyFragmentActivity) {
            ((MyFragmentActivity) mContext).startFragment(fragment);
        }
    }

    public void startFragment(Fragment fragment, boolean needAnim) {
        if (mContext instanceof MyFragmentActivity) {
            ((MyFragmentActivity) mContext).startFragment(null, fragment, needAnim);
        }
    }

    public void startFragment(Map<String, Serializable> params, Fragment fragment) {
        if (mContext instanceof MyFragmentActivity) {
            ((MyFragmentActivity) mContext).startFragment(params, fragment);
        }
    }

    /**
     * 关闭fragment
     */
    protected void finishFragment() {
        if (mContext instanceof MyFragmentActivity)
            ((MyFragmentActivity) mContext).finishFragment();
    }

    protected void finishFragment(int requestCode, int resultCode, Map<String, Serializable> result) {
        if (mContext instanceof MyFragmentActivity)
            ((MyFragmentActivity) mContext).finishFragment(requestCode, resultCode, result);
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

    private TimePickerView pvTime;

    /**
     * 选择时间
     */
    protected void showTimePickerView(Context context, String time, View view) {
        if (view != null) {
            hideSoftInput(view);
        }
        if (pvTime == null) {
            pvTime = new TimePickerBuilder(context, (date, v) ->
                    selectTime(date)).setType(new boolean[]{true, true, true, false, false, false})
                    .setBgColor(getResources().getColor(R.color.bg_transparent1)).build();
        }
        if (TextUtils.isEmpty(time) || time.equals("无法显示时间")) {
            pvTime.setDate(Calendar.getInstance());
        } else {
            pvTime.setDate(TimeUtil.stringToCalendar(time, "yyyy.MM.dd"));

        }
        pvTime.show();
    }

    protected void onFinishResult(int requestCode, int resultCode, Map<String, Serializable> result) {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null) {
            mPresenter.deAttach();
        }
        unbinder.unbind();
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
        if (getActivity() == null) return;
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
        if (getActivity() == null) return;
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
        if (statusView != null) {
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
    protected void noData(int imgId, int strId, boolean needReload) {
        if (isContainEmptyView()) {
            mContentView.findViewById(R.id.ll_empty_page).setVisibility(View.VISIBLE);
            ((ImageView) mContentView.findViewById(R.id.iv_empty_page)).setImageResource(imgId);
            ((TextView) mContentView.findViewById(R.id.tv_empty_page)).setText(strId);
            if (needReload) {
                mContentView.findViewById(R.id.btn_empty_page_reload).setVisibility(View.VISIBLE);
                mContentView.findViewById(R.id.btn_empty_page_reload).setOnClickListener(v -> {
                    hasData();
                    reload();
                });
            } else {
                mContentView.findViewById(R.id.btn_empty_page_reload).setVisibility(View.GONE);
            }
        }
    }

    /**
     * 没有数据
     */
    protected void noData(int imgId, String str, boolean needReload) {
        if (isContainEmptyView()) {
            mContentView.findViewById(R.id.ll_empty_page).setVisibility(View.VISIBLE);
            ((ImageView) mContentView.findViewById(R.id.iv_empty_page)).setImageResource(imgId);
            ((TextView) mContentView.findViewById(R.id.tv_empty_page)).setText(str);
            if (needReload) {
                mContentView.findViewById(R.id.btn_empty_page_reload).setVisibility(View.VISIBLE);
                mContentView.findViewById(R.id.btn_empty_page_reload).setOnClickListener(v -> {
                    hasData();
                    reload();
                });
            } else {
                mContentView.findViewById(R.id.btn_empty_page_reload).setVisibility(View.GONE);
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
     * 显示键盘
     */
    protected void showSoftInput(View view) {
        if (getActivity() != null) {
            ((BaseActivity) getActivity()).showSoftInput(view);
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
    protected void onRequestCallPhone(String phoneNumber) {
        requestPermission(Manifest.permission.CALL_PHONE, new OnRequestPermissionsCallback() {
            @Override
            public void onGranted(int requestCode, @NonNull String[] permissions) {  //用户同意
                onActionCall(phoneNumber);
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

//        InputMethodManager manager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            KeyBoardUtil.hideSoftInput((Activity) mContext);
//            if (manager != null
//                    && ((Activity) mContext).getCurrentFocus() != null
//                    && ((Activity) mContext).getCurrentFocus().getWindowToken() != null) {
//                manager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//            }
        }
        return false;
    }

    @SuppressWarnings("unused")
    protected void showShortToast(@StringRes int resId) {
        showShortToast(resId, -1);
    }

    protected void showShortToast(@StringRes int resId, int gravity) {
        showShortToast(mContext.getString(resId), gravity);
    }

    protected void showShortToast(CharSequence text) {
        showShortToast(text, -1);
    }

    protected void showShortToast(CharSequence text, int gravity) {
        if (TextUtils.isEmpty(text)) return;
        AppToastCompat.from(mContext.getApplicationContext())
                .with(text)
                .setDuration(Toast.LENGTH_SHORT)
                .setGravity(gravity).show();
    }

    @SuppressWarnings("unused")
    protected void showLongToast(@StringRes int resId) {
        showLongToast(resId, -1);
    }

    public void showLongToast(@StringRes int resId, int gravity) {
        showLongToast(mContext.getString(resId), gravity);
    }

    @SuppressWarnings("unused")
    protected void showLongToast(CharSequence text) {
        showLongToast(text, -1);
    }

    protected void showLongToast(CharSequence text, int gravity) {
        if (TextUtils.isEmpty(text)) return;
        AppToastCompat.from(mContext.getApplicationContext())
                .with(text)
                .setDuration(Toast.LENGTH_LONG)
                .setGravity(gravity).show();
    }

    public void requestPermission(@NonNull String permission, OnRequestPermissionsCallback callback) {
        requestPermission(permission, REQUEST_PERMISSION_CODE, callback);
    }

    protected void requestPermission(@NonNull String permission, int requestCode, OnRequestPermissionsCallback callback) {
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

    /*打开app设置页面*/
    public void openSettings() {
        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", mContext.getApplicationContext().getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        } catch (Exception ignored) {
        }
    }
}
