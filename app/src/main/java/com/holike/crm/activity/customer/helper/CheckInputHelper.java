package com.holike.crm.activity.customer.helper;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.holike.crm.R;
import com.holike.crm.activity.customer.CustomerDetailV2Activity;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.base.SimpleTextWatcher;
import com.holike.crm.bean.ErrorInfoBean;
import com.holike.crm.dialog.CustomerRedistributionDialog;
import com.holike.crm.dialog.DistributionDialog;
import com.holike.crm.dialog.ExistHighSeasDialog;
import com.holike.crm.dialog.MaterialDialog;
import com.holike.crm.dialog.RepeatDigitalDialog;
import com.holike.crm.enumeration.CustomerValue;
import com.holike.crm.http.CustomerUrlPath;
import com.holike.crm.http.MyHttpClient;
import com.holike.crm.http.MyJsonParser;
import com.holike.crm.http.ParamHelper;
import com.holike.crm.http.SimpleRequestCallback;

import io.reactivex.disposables.Disposable;

/**
 * Created by gallop on 2019/9/4.
 * Copyright holike possess 2019.
 * 客户房屋信息地址检查
 */
abstract class CheckInputHelper {
    BaseActivity<?, ?> mActivity;
    private EditText mEditText;
    String mProvinceCode, mCityCode, mDistrictCode;  //省市区代码
    String mOldAddress;
    private Disposable mCheckAddressDisposable;
    private CheckAddressTask mCheckAddressTask;

    CheckInputHelper(BaseActivity<?, ?> activity) {
        this.mActivity = activity;
    }

    void addTextWatcher(EditText editText) {
        mEditText = editText;
        mEditText.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int i, int i1, int i2) {
                String content = cs.toString();
                if (!TextUtils.equals(content, mOldAddress) && !TextUtils.isEmpty(content.trim())) {
                    cancelCheckAddress();
                    checkAddress(content);
                } else {
                    cancelCheckAddress();
                }
            }
        });
    }

    private void cancelCheckAddress() {
        if (mCheckAddressTask != null) {
            mEditText.removeCallbacks(mCheckAddressTask);
            mCheckAddressTask = null;
        }
        if (mCheckAddressDisposable != null) {
            mCheckAddressDisposable.dispose();
        }
    }

    void checkAddress(String address) {
        mCheckAddressTask = new CheckAddressTask(address);
        mEditText.postDelayed(mCheckAddressTask, 500);
    }

    /*检测地址是否存在*/
    class CheckAddressTask implements Runnable {
        String address;

        CheckAddressTask(String address) {
            this.address = address;
        }

        @Override
        public void run() {
            mCheckAddressDisposable = MyHttpClient.postByBodyString(CustomerUrlPath.URL_CHECK_ADDRESS,
                    ParamHelper.Customer.checkAddress(mProvinceCode, mCityCode, mDistrictCode, address), new SimpleRequestCallback<String>() {
                        @Override
                        public void onSuccess(String json) {
                            isExistError(json);
                        }
                    });
        }
    }

    boolean isExistError(String json) {
        String msg = MyJsonParser.getMsgAsString(json);
        if (TextUtils.equals(msg, "repeat-self")) { //个人客户重复
            showSelfError(MyJsonParser.getMoreInfoAsString(json));
            return true;
        } else if (TextUtils.equals(msg, "repeat-other")) { //他人重复
            ErrorInfoBean bean = MyJsonParser.fromJson(MyJsonParser.getErrorInfoAsString(json), ErrorInfoBean.class);
            existedError(bean, MyJsonParser.getMoreInfoAsString(json));
            return true;
        } else if (TextUtils.equals(msg, "repeat-high_seas_person")) {
            existHighSeasError(MyJsonParser.getMoreInfoAsString(json), MyJsonParser.getErrorInfoAsString(json));  //errorInfo里是houseId，moreInfo里是personalId
            return true;
        } else if (TextUtils.equals(msg, "repeat-digital")) {  //线上引流客户重复(弹出激活弹窗)
            ErrorInfoBean bean = MyJsonParser.fromJson(MyJsonParser.getErrorInfoAsString(json), ErrorInfoBean.class);
            onRepeatDigitalError(bean, MyJsonParser.getMoreInfoAsString(json), MyJsonParser.getErrorInfoAsString(json));
        } else if (TextUtils.equals(msg, "digital_no_add")) {  //线上引流客户新建房屋
            digitalNoAddError(MyJsonParser.getErrorInfoAsString(json));
            return true;
        }
        return false;
    }

    /*若客户被同一导购/业务员创建*/
    private void showSelfError(final String personalId) {
        new MaterialDialog.Builder(mActivity)
                .message(R.string.dialog_customer_repeat_self)
                .negativeButton(R.string.no, null)
                .positiveButton(R.string.yes, (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    Bundle bundle = new Bundle();
                    bundle.putString(CustomerValue.PERSONAL_ID, personalId);
                    mActivity.startActivity(CustomerDetailV2Activity.class, bundle);
                    onJumpDetail();
                }).show();
    }

    void onJumpDetail() {

    }

    /*地址或者手机号已存在，弹窗提示，并显示最新状态、时间、是否收取订金等*/
    private void existedError(final ErrorInfoBean bean, final String personalId) {
        if (bean == null) return;
        new CustomerRedistributionDialog(mActivity, bean).setOnConfirmListener((dialog, reason, shopId, groupId) ->
                onDistributionMsgPush(ParamHelper.Customer.distributionMsgPush(personalId, bean.houseId, bean.status, bean.statusTime, reason, shopId, groupId)))
                .show();
    }

    /*若该客户为公海客户，则弹框内容为“该客户已存在于公海，是否领取该客户”，点击是，
    则领取该客户，领取后的公海客户存在于该导购/业务员的跟进中客户列表，点击否，则退出当前弹框*/
    private void existHighSeasError(final String personalId, final String houseId) {
        ExistHighSeasDialog dialog = new ExistHighSeasDialog(mActivity);
        dialog.setOnSelectedListener((shopId, groupId) -> onReceivingCustomer(personalId, houseId, shopId, groupId));
        dialog.show();
    }

    private void onRepeatDigitalError(ErrorInfoBean bean, final String personalId, final String houseId) {
        RepeatDigitalDialog dialog = new RepeatDigitalDialog(mActivity, bean);
        dialog.setOnConfirmListener((shopId, groupId, guideId) -> onActivationCustomer(personalId, houseId, shopId, groupId, guideId));
        dialog.show();
    }

    private void digitalNoAddError(String errorMsg) {
        new MaterialDialog.Builder(mActivity)
                .message(errorMsg)
                .neutralButton(R.string.i_know, (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }

    abstract void onDistributionMsgPush(String body);

    abstract void onReceivingCustomer(String personalId, String houseId, String shopId, String groupId);

    abstract void onActivationCustomer(String personalId, String houseId, String shopId, String groupId, String guideId);

    /*新增或修改结果*/
    public void onSaveResult(String json) {
        if (MyJsonParser.getCode(json) == 0) {
            mActivity.showShortToast(MyJsonParser.getShowMessage(json));
            mActivity.setResult(Activity.RESULT_OK);
            mActivity.finish();
        } else {
            if (!isExistError(json)) {
                mActivity.showShortToast(MyJsonParser.getShowMessage(json));
            }
        }
    }

    void release() {
        if (mCheckAddressTask != null) {
            mEditText.removeCallbacks(mCheckAddressTask);
        }
        if (mCheckAddressDisposable != null) {
            mCheckAddressDisposable.dispose();
        }
    }
}
