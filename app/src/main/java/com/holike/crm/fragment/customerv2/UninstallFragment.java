package com.holike.crm.fragment.customerv2;

import com.holike.crm.R;
import com.holike.crm.bean.DealerInfoBean;
import com.holike.crm.fragment.customerv2.helper.UninstallHelper;

import java.util.List;


/**
 * Created by pony on 2019/7/25.
 * Copyright holike possess 2019.
 * 预约安装
 */
public class UninstallFragment extends GeneralCustomerFragment implements UninstallHelper.Callback {

    private UninstallHelper mHelper;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_customer_uninstall;
    }

    @Override
    protected void init() {
        mHelper = new UninstallHelper(this, this);
    }

    @Override
    public void onQueryInstallers(String shopId, boolean showLoading) {
        if (showLoading) {
            showLoading();
        }
        mPresenter.getShopInstallers(shopId);
    }

    @Override
    public void onRequired(CharSequence text) {
        showShortToast(text);
    }

    @Override
    public void onSaved(String body) {
        showLoading();
        mPresenter.saveUninstall(body);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onSuccess(Object object) {
        super.onSuccess(object);
        if (object instanceof List) {
            List<DealerInfoBean.UserBean> list = (List<DealerInfoBean.UserBean>) object;
            mHelper.setInstaller(list);
        } else {
            setResultOk(object);
        }
    }
}
