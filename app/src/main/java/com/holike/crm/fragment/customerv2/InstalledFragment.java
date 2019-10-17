package com.holike.crm.fragment.customerv2;

import android.content.Intent;


import com.holike.crm.R;
import com.holike.crm.bean.DealerInfoBean;
import com.holike.crm.bean.SysCodeItemBean;
import com.holike.crm.fragment.customerv2.helper.InstalledHelper;

import java.util.List;
import java.util.Map;


/**
 * Created by gallop on 2019/7/23.
 * Copyright holike possess 2019.
 * 安装完成
 */
public class InstalledFragment extends GeneralCustomerFragment implements InstalledHelper.Callback {

    private InstalledHelper mHelper;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_customer_installed;
    }

    @Override
    protected void init() {
        mHelper = new InstalledHelper(this, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onQuerySystemCode() {
        showLoading();
        mPresenter.getSystemCode();
    }

    /*获取经销商安装工*/
    @Override
    public void onQueryInstaller() {
        showLoading();
        mPresenter.getDealerInstaller();
    }

    @Override
    public void onRequired(CharSequence text) {
        showShortToast(text);
    }

    @Override
    public void onSaved(Map<String, Object> params, List<String> imagePaths) {
        showLoading();
        mPresenter.finishInstall(mContext, params, imagePaths);
    }

    @Override
    public void onSuccess(Object object) {
        super.onSuccess(object);
        if (object == null) return;
        if (object instanceof SysCodeItemBean) {
            mHelper.setSystemCode((SysCodeItemBean) object);
        }
//        else if (object instanceof List) {
//            List<DealerInfoBean.UserBean> list = (List<DealerInfoBean.UserBean>) object;
//            mHelper.setInstaller(list);
//        }
        else {
            setResultOk(object);
        }
    }
}
