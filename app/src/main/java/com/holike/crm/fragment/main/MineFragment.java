package com.holike.crm.fragment.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.holike.crm.R;
import com.holike.crm.activity.homepage.MessageV2Activity;
import com.holike.crm.activity.mine.ChangePasswordActivity;
import com.holike.crm.base.BaseFragment;
import com.holike.crm.bean.DownloadFileBean;
import com.holike.crm.bean.UpdateBean;
import com.holike.crm.bean.UserInfoBean;
import com.holike.crm.dialog.MaterialDialog;
import com.holike.crm.dialog.UpdateAppDialog;
import com.holike.crm.presenter.fragment.HomePagePresenter;
import com.holike.crm.presenter.fragment.MinePresenter;
import com.holike.crm.service.UpdateService;
import com.holike.crm.util.IOUtil;
import com.holike.crm.util.PackageUtil;
import com.holike.crm.view.fragment.MineView;
import com.scwang.smartrefresh.header.MaterialHeader;
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
    @BindView(R.id.tv_mine_msg)
    TextView tvMsg;
    @BindView(R.id.iv_red_point_msg)
    ImageView ivRedPoint;
    @BindView(R.id.srl_mine)
    SmartRefreshLayout refreshLayout;

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
        mPresenter.checkVersion(false);
        mPresenter.getUserInfo();
        showLoading();
//        refreshLayout.setRefreshFooter(new BallPulseFooter(mContext));
        refreshLayout.setRefreshHeader(new MaterialHeader(mContext));
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            mPresenter.checkVersion(false);
            mPresenter.getUserInfo();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ivRedPoint.setVisibility(HomePagePresenter.isNewMsg() ? View.VISIBLE : View.GONE);
    }

    @OnClick({R.id.ll_my_change_password, R.id.btn_my_logout, R.id.ll_my_update_check, R.id.tv_mine_msg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_my_change_password:
                startActivity(ChangePasswordActivity.class);
                break;
            case R.id.btn_my_logout:
                logout();
                break;
            case R.id.ll_my_update_check:
                mPresenter.checkVersion(true);
                break;
            case R.id.tv_mine_msg:
                startActivity(MessageV2Activity.class);
                break;
        }
    }

    public void logout() {
        new MaterialDialog.Builder(mContext)
                .title(R.string.my_logout)
                .message(R.string.dialog_logout_content)
                .negativeButton(R.string.cancel,null)
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
        tvVersionMsg.setText(PackageUtil.getVersionName());
    }

    @Override
    public void getUserInfoFailed(String failed) {
        refreshLayout.finishRefresh();
        dismissLoading();
        showShortToast(failed);
    }

    @Override
    public void isLastVersion() {
        tvUpdateCheck.setText(mContext.getString(R.string.my_last_version));
    }

    @Override
    public void hasNewVersion(UpdateBean updateBean, boolean isDownload) {
        tvUpdateCheck.setText(mContext.getString(R.string.my_new_version));
        if (isDownload) {
            showUpdateAppDialog(mContext, updateBean);
        }
    }

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
}
