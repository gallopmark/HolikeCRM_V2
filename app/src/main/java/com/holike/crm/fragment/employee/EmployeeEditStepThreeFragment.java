package com.holike.crm.fragment.employee;

import androidx.recyclerview.widget.RecyclerView;

import com.holike.crm.R;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.EmployeeDetailBean;
import com.holike.crm.bean.RoleDataBean;
import com.holike.crm.fragment.OnFragmentDataChangedListener;
import com.holike.crm.presenter.fragment.EmployeeAuthInfoPresenter;
import com.holike.crm.view.fragment.EmployeeAuthInfoView;


import java.util.List;

import butterknife.BindView;

public class EmployeeEditStepThreeFragment extends MyFragment<EmployeeAuthInfoPresenter, EmployeeAuthInfoView>
        implements EmployeeAuthInfoView, OnFragmentDataChangedListener {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    private static List<RoleDataBean.AuthInfoBean> authInfoBeans;
    private boolean isDataUpdated = false;
    private OnFragmentDataChangedListener onFragmentDataChangedListener;

    public void setOnFragmentDataChangedListener(OnFragmentDataChangedListener onFragmentDataChangedListener) {
        this.onFragmentDataChangedListener = onFragmentDataChangedListener;
    }

    public static EmployeeEditStepThreeFragment newInstance(List<RoleDataBean.AuthInfoBean> beans) {
        authInfoBeans = beans;
        return new EmployeeEditStepThreeFragment();
    }

    @Override
    protected EmployeeAuthInfoPresenter attachPresenter() {
        return new EmployeeAuthInfoPresenter();
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_employee_edit_step_three;
    }

    @Override
    protected void init() {
        super.init();
        mPresenter.setAdapter(mRecyclerView, this);
        initViewData();
    }

    private void initViewData() {
        mPresenter.getAuthInfo(mContext);
    }

    @Override
    public void onShowLoading() {
        showLoading();
    }

    @Override
    public void onGetAuthInfo(List<RoleDataBean.AuthInfoBean> infoBeans) {
        if (authInfoBeans != null) {
            update(authInfoBeans);
        } else {
            mPresenter.addAll(infoBeans);
        }
    }

    public boolean isDataUpdated() {
        return isDataUpdated;
    }

    public void update(List<RoleDataBean.AuthInfoBean> infoBeans) {
        mPresenter.addAll(infoBeans);
        mPresenter.setSelectItems(infoBeans);
        isDataUpdated = true;
    }

    @Override
    public void onGetAuthInfoFail(String message) {
        showShortToast(message);
    }

    @Override
    public void onHideLoading() {
        dismissLoading();
    }

    @Override
    public void onFragmentDataChanged(boolean isChanged) {
        if (onFragmentDataChangedListener != null) {
            onFragmentDataChangedListener.onFragmentDataChanged(isChanged);
        }
    }

    public String getAuthIds() {
        StringBuilder stringBuilder = new StringBuilder();
        for (RoleDataBean.AuthInfoBean.PArrBean bean : mPresenter.getSelectItems()) {
            if (bean.getIsSelect() == 1) {
                stringBuilder.append(bean.actionId);
                stringBuilder.append(",");
            }
        }
        if (stringBuilder.lastIndexOf(",") >= 0) {
            stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(","));
        }
        return stringBuilder.toString();
    }

    @Override
    public void onDestroyView() {
        if (authInfoBeans != null) {
            authInfoBeans.clear();
            authInfoBeans = null;
        }
        super.onDestroyView();
    }
}
