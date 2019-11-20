package com.holike.crm.fragment.customerv2;


import androidx.recyclerview.widget.RecyclerView;

import com.holike.crm.R;
import com.holike.crm.bean.CurrentUserBean;
import com.holike.crm.bean.ShopRoleUserBean;
import com.holike.crm.fragment.customerv2.helper.UnmeasuredHelper;
import com.holike.crm.presenter.fragment.GeneralCustomerPresenter;
import com.holike.crm.view.fragment.GeneralCustomerView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by pony on 2019/7/22.
 * Copyright holike possess 2019.
 * 预约量尺
 */
public class UnmeasuredFragment extends GeneralCustomerFragment implements GeneralCustomerView, UnmeasuredHelper.Callback, GeneralCustomerPresenter.OnQueryRoleUserCallback {
    @BindView(R.id.rv_space)
    RecyclerView mSpaceRecyclerView;
    private UnmeasuredHelper mHelper;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_customer_unmeasure;
    }

    @Override
    protected void init() {
        mHelper = new UnmeasuredHelper(this, this);
        mHelper.setMeasureSpace(mSpaceRecyclerView);
    }

    @Override
    public void onQueryUserInfo() {
        showLoading();
        mPresenter.getUserInfo();
    }

    @Override
    public void onQueryMeasurer(String shopId) {
        showLoading();
        mPresenter.getMeasurePerson(shopId, this);
    }

    /*获取量尺人员成功*/
    @Override
    public void onSuccess(List<ShopRoleUserBean.UserBean> list) {
        dismissLoading();
        mHelper.onSelectRuler(list);
    }

    @Override
    public void onSave(String body) {
        showLoading();
        mPresenter.appointMeasure(body);
    }

    @Override
    public void onSuccess(Object object) {
        super.onSuccess(object);
        if (object == null) return;
        if (object instanceof CurrentUserBean) {
            mHelper.setUserInfo((CurrentUserBean) object);
        }else {
            setResultOk(object);
        }
    }
}
