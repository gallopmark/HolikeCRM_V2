package com.holike.crm.fragment.main;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.holike.crm.R;
import com.holike.crm.activity.homepage.MessageV2Activity;
import com.holike.crm.activity.mine.ChangePasswordActivity;
import com.holike.crm.base.BaseFragment;
import com.holike.crm.bean.UpdateBean;
import com.holike.crm.bean.UserInfoBean;
import com.holike.crm.dialog.MaterialDialog;
import com.holike.crm.dialog.UpdateAppDialog;
import com.holike.crm.presenter.fragment.HomePagePresenter2;
import com.holike.crm.presenter.fragment.MinePresenter;
import com.holike.crm.service.VersionUpdateService;
import com.holike.crm.util.AppUtils;
import com.holike.crm.view.fragment.MineView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;


import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wqj on 2018/2/24.
 * 我的
 */

public class MineFragment extends BaseFragment<MinePresenter, MineView> implements MineView {
    @BindView(R.id.iv_my_bg)
    ImageView ivBg;
    @BindView(R.id.iv_my_userface)
    ImageView ivUserface;
    @BindView(R.id.tv_my_username)
    TextView tvUsername;
    @BindView(R.id.tv_my_login_account)
    TextView tvLoginAccount;
    @BindView(R.id.tv_my_update_check)
    TextView tvUpdateCheck;
    @BindView(R.id.tv_my_version_msg)
    TextView tvVersionMsg;
    @BindView(R.id.tv_my_phone)
    TextView tvPhone;
    @BindView(R.id.tv_my_usertype)
    TextView tvUserType;
    @BindView(R.id.tv_my_create_time)
    TextView tvCreateTime;
    @BindView(R.id.srl_mine)
    SmartRefreshLayout refreshLayout;
    private boolean mFromUser;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected MinePresenter attachPresenter() {
        return new MinePresenter();
    }

    @Override
    protected void init() {
        tvVersionMsg.setText(AppUtils.getVersionName(true));
        initData(true);
        refreshLayout.setOnRefreshListener(refreshLayout -> initData(false));
    }

    private void initData(boolean showLoading) {
        if (showLoading) {
            showLoading();
        }
        mPresenter.checkForUpdate();
        mPresenter.getUserInfo();
    }

    @Override
    public void onResume() {
        super.onResume();
        setRightMenuMsg(HomePagePresenter2.isNewMsg());
    }

    @Override
    protected void clickRightMenu(CharSequence menuText, View actionView) {
        startActivity(MessageV2Activity.class);
    }

    @OnClick({R.id.tv_change_password, R.id.btn_my_logout, R.id.ll_my_update_check})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_change_password:
                startActivity(ChangePasswordActivity.class);
                break;
            case R.id.btn_my_logout:
                logout();
                break;
            case R.id.ll_my_update_check:
                mFromUser = true;
                mPresenter.checkForUpdate();
                break;
        }
    }

    public void logout() {
        new MaterialDialog.Builder(mContext)
                .title(R.string.my_logout)
                .message(R.string.dialog_logout_content)
                .negativeButton(R.string.cancel, null)
                .positiveButton(R.string.confirm, (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    MinePresenter.logout((Activity) mContext);
                }).show();
    }

    @Override
    public void getUserInfoSuccess(UserInfoBean userInfoBean) {
        dismissLoading();
        refreshLayout.finishRefresh();
        tvUsername.setText(userInfoBean.getName());
        tvPhone.setText(userInfoBean.getPhone());
        tvUserType.setText(userInfoBean.getCustomerTypeName());
        tvCreateTime.setText(userInfoBean.getCreateDate());
        tvLoginAccount.setText(userInfoBean.getUserId());
    }

    @Override
    public void getUserInfoFailed(String failed) {
        refreshLayout.finishRefresh();
        dismissLoading();
        showShortToast(failed);
    }

    @Override
    public void checkForUpdateSuccess(UpdateBean updateBean, boolean hasNewVersion) {
        if (!hasNewVersion) {
            tvUpdateCheck.setText(mContext.getString(R.string.my_last_version));
        } else {
            tvUpdateCheck.setText(mContext.getString(R.string.my_new_version));
            if (mFromUser) {
                showUpdateAppDialog(mContext, updateBean);
                mFromUser = false;
            }
        }
    }

    @Override
    public void checkForUpdateFailure(String failReason) {
        dismissLoading();
        showShortToast(failReason);
    }

    public void showUpdateAppDialog(final Context context, final UpdateBean updateBean) {
        new UpdateAppDialog(context, updateBean).setUpdateButtonClickListener(dialog -> {
            dialog.dismiss();
            VersionUpdateService.start(context, updateBean.getUpdatepath());
//            if (updateBean.getType() == UpdateBean.TYPE_DOWNLOAD) {
//                showLongToast("正在下载...");
////                Toast.makeText(MyApplication.getInstance(), "正在下载...", Toast.LENGTH_SHORT).show();
//                DownloadFileBean downloadFileBean = new DownloadFileBean(updateBean.getUpdatepath(), "CRM.apk");
//                Intent intent = new Intent(context, UpdateService.class);
//                intent.putExtra(UpdateService.DOWNLOADFILEBEAN, downloadFileBean);
//                context.startService(intent);
//            } else if (updateBean.getType() == UpdateBean.TYPE_INSTALL) {
//                UpdateService.install(IOUtil.getCachePath() + "/" + "CRM.apk");
//            }
        }).show();
    }
}
