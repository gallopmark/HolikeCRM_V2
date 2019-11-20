package com.holike.crm.fragment.customerv2;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.holike.crm.R;
import com.holike.crm.bean.CurrentUserBean;
import com.holike.crm.bean.ShopRoleUserBean;
import com.holike.crm.bean.SysCodeItemBean;
import com.holike.crm.enumeration.CustomerValue;
import com.holike.crm.fragment.customerv2.helper.MeasureResultHelper;
import com.holike.crm.presenter.fragment.GeneralCustomerPresenter;
import com.holike.crm.rxbus.MessageEvent;
import com.holike.crm.rxbus.RxBus;

import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by pony on 2019/7/22.
 * Copyright holike possess 2019.
 * 量尺结果
 */
public class MeasureResultFragment extends GeneralCustomerFragment implements MeasureResultHelper.MeasureResultCallback, GeneralCustomerPresenter.OnQueryRoleUserCallback {
    @BindView(R.id.ll_content)
    LinearLayout mContentLayout;
    @BindView(R.id.tvSave)
    TextView mSaveTextView;
    private MeasureResultHelper mHelper;

    @Override
    protected GeneralCustomerPresenter attachPresenter() {
        return new GeneralCustomerPresenter();
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_customer_measuteresult;
    }

    @Override
    protected void init() {
        super.init();
        mHelper = new MeasureResultHelper(this, this);
        mSaveTextView.setOnClickListener(view -> mHelper.onSave());
    }

    @Override
    public void onRequestSysCode() {
        showLoading();
        mPresenter.getSystemCode();
    }

    @Override
    public void onShowContentView() {
        mContentLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRequired(String text) {
        showShortToast(text);
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

    /**
     * @param images 量尺图集合
     */
    @Override
    public void onSaved(Map<String, Object> params, List<String> images) {
        showLoading();
        mPresenter.saveMeasureResult(mContext, params, images);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSuccess(List<ShopRoleUserBean.UserBean> list) {
        dismissLoading();
        mHelper.onSelectMeasureUser(list);
    }

    @Override
    public void onSuccess(Object object) {
        super.onSuccess(object);
        if (object instanceof SysCodeItemBean) {
            mHelper.setSysCodeItemBean((SysCodeItemBean) object);
            onShowContentView();
        } else if (object instanceof CurrentUserBean) {
            mHelper.setCurrentUserBean((CurrentUserBean) object);
        } else if (object instanceof String) {
            setResultOk(object);
        }
    }
}
