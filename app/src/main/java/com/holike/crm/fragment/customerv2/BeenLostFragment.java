package com.holike.crm.fragment.customerv2;

import android.app.Activity;

import com.holike.crm.R;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.bean.SysCodeItemBean;
import com.holike.crm.enumeration.CustomerValue;
import com.holike.crm.fragment.customerv2.helper.BeenLostHelper;
import com.holike.crm.rxbus.MessageEvent;
import com.holike.crm.rxbus.RxBus;

/**
 * Created by pony on 2019/7/22.
 * Copyright holike possess 2019.
 * 已流失
 */
public class BeenLostFragment extends GeneralCustomerFragment implements BeenLostHelper.Callback {
    private BeenLostHelper mBeenLostHelper;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_customer_been_lost;
    }

    @Override
    protected void init() {
        mBeenLostHelper = new BeenLostHelper(this, this);
    }

    @Override
    public void onQuerySystemCode() {
        showLoading();
        mPresenter.getSystemCode();
    }

    /*点击保存*/
    @Override
    public void onSaved(String body) {
        showLoading();
        mPresenter.leaveHouse(body);
    }

    @Override
    public void onSuccess(Object object) {
        super.onSuccess(object);
        if (object instanceof SysCodeItemBean) {
            mBeenLostHelper.setSystemCode((SysCodeItemBean) object);
        } else if (object instanceof String) {
            showShortToast((String) object);
            BaseActivity<?, ?> activity = (BaseActivity<?, ?>) mContext;
            activity.setResult(Activity.RESULT_OK);
            RxBus.getInstance().post(new MessageEvent(CustomerValue.EVENT_TYPE_LOST_HOUSE));
            activity.finish();
        }
    }
}
