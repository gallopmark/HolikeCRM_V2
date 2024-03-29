package com.holike.crm.fragment.main;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.holike.crm.R;
import com.holike.crm.activity.credit.CreditInquiryActivity;
import com.holike.crm.activity.customer.CustomerDetailV2Activity;
import com.holike.crm.activity.employee.EmployeeListV2Activity;
import com.holike.crm.activity.homepage.MessageV2Activity;
import com.holike.crm.activity.message.MessageDetailsActivity;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.base.BaseFragment;
import com.holike.crm.bean.HomepageBean;
import com.holike.crm.presenter.fragment.HomePagePresenter2;
import com.holike.crm.util.Constants;
import com.holike.crm.view.fragment.HomePageView2;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import butterknife.BindView;
import butterknife.OnClick;

/*新首页，旧版为HomepageFragment*/
public class HomepageV2Fragment extends BaseFragment<HomePagePresenter2, HomePageView2> implements HomePageView2, HomepageHelper.Callback {
    @BindView(R.id.tv_role)
    TextView mRoleTextView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.ll_content_layout)
    LinearLayout mContentLayout;

    private HomepageHelper mHomepageHelper;

    @Override
    protected HomePagePresenter2 attachPresenter() {
        return new HomePagePresenter2();
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_homepage2;
    }

    @Override
    protected void init() {
        setTitle(mContext.getString(R.string.homepage));
        mHomepageHelper = new HomepageHelper((BaseActivity<?, ?>) mContext, mContentLayout, getChildFragmentManager(), this);
        mRefreshLayout.setOnRefreshListener(refreshLayout -> initData(false));
        initData(true);
    }

    private void initData(boolean showLoading) {
        if (showLoading) showLoading();
        mPresenter.getHomepageData();
    }

    @Override
    protected void reload() {
        super.reload();
        initData(true);
    }

    @Override
    public void onResume() {
        super.onResume();
//        tvMsg.setText(HomePagePresenter.getMsgNum());
        setRightMenuMsg(HomePagePresenter2.isNewMsg());
    }

    @Override
    protected void clickRightMenu(CharSequence menuText, View actionView) {
        startActivity(MessageV2Activity.class);
    }

    @OnClick({R.id.tv_role, R.id.mMoreTextView})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_role:
                mHomepageHelper.switchRole(mRoleTextView);
                break;
            case R.id.mMoreTextView:
                startActivity(MessageV2Activity.class);
                break;
        }
    }

    @Override
    public void getHomepageDataSuccess(HomepageBean bean) {
        dismissLoading();
        setContentEnabled(true);
        mHomepageHelper.onHttpOk(bean, mRoleTextView);
        setRightMenuMsg(HomePagePresenter2.isNewMsg());
    }

    private void setContentEnabled(boolean isEnabled) {
        mRefreshLayout.finishRefresh();
        if (isEnabled) {
            if (mRefreshLayout.getVisibility() != View.VISIBLE) {
                mRefreshLayout.setVisibility(View.VISIBLE);
            }
        } else {
            mRefreshLayout.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onMessageItemClick(boolean isOpenMore, HomepageBean.MessageListBean bean) {
        if (isOpenMore) {
            startActivity(MessageV2Activity.class);
        } else {
            if (bean.getType() == 2) {
                MessageDetailsActivity.open(this, bean.getMessageId(), REQUEST_CODE);
//                AnnounceFragment.startMessageDetailsActivity(HomepageV2Fragment.this, bean.getMessageId(), REQUEST_CODE);
            } else {
                CustomerDetailV2Activity.open((BaseActivity) mContext, bean.getPersonalId(), bean.getMessageId(), bean.isHighSeasHouse());
            }
        }
    }

    @Override
    public void onViewClicked(View view, Intent intent) {
        openActivityForResult(intent);
    }

    @Override
    public void onMenuClick(String type, String name) {
        if (TextUtils.equals(type, "5")) { //员工管理
            startActivity(EmployeeListV2Activity.class);
        } else {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.CREDIT_TYPE, type);
            startActivity(CreditInquiryActivity.class, bundle);
        }
    }

    /**
     * 获取首页数据失败
     */
    @Override
    public void getHomepageDataFailed(String failed) {
        dismissLoading();
        setContentEnabled(false);
        if (isNoAuth(failed)) {
            noAuthority();
        } else {
            noNetwork(failed);
        }
    }
}
