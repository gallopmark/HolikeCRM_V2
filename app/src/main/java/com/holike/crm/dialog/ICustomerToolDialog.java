package com.holike.crm.dialog;

import android.content.Context;
import android.content.res.Resources;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.holike.crm.R;
import com.holike.crm.bean.CurrentUserBean;
import com.holike.crm.bean.ShopGroupBean;
import com.holike.crm.customView.AppToastCompat;
import com.holike.crm.http.CustomerUrlPath;
import com.holike.crm.http.MyHttpClient;
import com.holike.crm.http.ParamHelper;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.util.SharedPreferencesUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by pony on 2019/8/30.
 * Copyright holike possess 2019.
 * 客户分配门店组织工具类
 */
abstract class ICustomerToolDialog extends CommonDialog {
    private CompositeDisposable mDisposables;

    OnSelectedListener mSelectedListener;
    List<ShopGroupBean> mCurrentGroupList; //当前门店下的组织
    static final int SHOW_ENTRY = 5; //列表展开显示条目
    static int MAX_HEIGHT;

    ICustomerToolDialog(Context context) {
        super(context);
        Resources resources = mContext.getResources();
        MAX_HEIGHT = (resources.getDimensionPixelSize(R.dimen.dp_40) + resources.getDimensionPixelSize(R.dimen.dp_0_5)) * 5;
        mDisposables = new CompositeDisposable();
        setCanceledOnTouchOutside(true);
    }

    public void setOnSelectedListener(OnSelectedListener listener) {
        mSelectedListener = listener;
    }

    /*获取用户信息*/
    void getUserInfo() {
        mDisposables.add(MyHttpClient.getByTimeout(CustomerUrlPath.URL_GET_USER_INFO, 60, new RequestCallBack<CurrentUserBean>() {

            @Override
            public void onStart(Disposable d) {
                onQueryStart(1);
            }

            @Override
            public void onFailed(String failReason) {
                onQueryUserInfoFailure(failReason);
            }

            @Override
            public void onSuccess(CurrentUserBean result) {
                onQueryUserInfoSuccess(result);
            }

            @Override
            public void onFinished() {
                onQueryCompleted(1);
            }
        }));
    }

    /*获取门店下用户所在的组织*/
    void getShopGroup(String shopId) {
        Map<String, String> params = new HashMap<>();
        params.put("shopId", ParamHelper.noNullWrap(shopId));
        params.put("userId", SharedPreferencesUtils.getUserId());
        mDisposables.add(MyHttpClient.getByTimeout(CustomerUrlPath.URL_GET_SHOP_USER_GROUP, null, params, 60, new RequestCallBack<List<ShopGroupBean>>() {

            @Override
            public void onStart(Disposable d) {
                onQueryStart(2);
            }

            @Override
            public void onFailed(String failReason) {
                onQueryShopGroupFailure(failReason);
            }

            @Override
            public void onSuccess(List<ShopGroupBean> result) {
                onQueryShopGroupSuccess(result);
            }

            @Override
            public void onFinished() {
                onQueryCompleted(2);
            }
        }));
    }

    void onQueryStart(int type) {

    }

    /*获取用户登录信息成功*/
    void onQueryUserInfoSuccess(CurrentUserBean userBean) {

    }

    /*获取用户登录信息失败*/
    private void onQueryUserInfoFailure(String failReason) {
        showToast(failReason);
    }

    void onQueryCompleted(int type) {

    }

    /*获取门店用户分组信息成功*/
    void onQueryShopGroupSuccess(List<ShopGroupBean> list) {

    }

    /*获取门店用户分组失败*/
    private void onQueryShopGroupFailure(String failReason) {
        showToast(failReason);
    }

    @Nullable
    @Override
    public ViewGroup.MarginLayoutParams getLayoutParams() {
        return new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public int getWindowAnimations() {
        return R.style.Dialog_Anim;
    }

    @Override
    protected boolean fullWidth() {
        return true;
    }

    @Override
    public int getGravity() {
        return Gravity.BOTTOM;
    }

    private void showToast(String text) {
        AppToastCompat.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDetachedFromWindow() {
        mDisposables.dispose();
        super.onDetachedFromWindow();
    }

    public interface OnSelectedListener {
        void onSelected(String shopId, String groupId);
    }
}
