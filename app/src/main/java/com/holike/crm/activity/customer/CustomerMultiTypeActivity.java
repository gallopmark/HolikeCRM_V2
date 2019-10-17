package com.holike.crm.activity.customer;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.holike.crm.R;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.base.MyFragmentActivity;
import com.holike.crm.enumeration.CustomerValue;
import com.holike.crm.fragment.customerv2.AssignDesignerFragment;
import com.holike.crm.fragment.customerv2.AssignGuideFragment;
import com.holike.crm.fragment.customerv2.BeenLostFragment;
import com.holike.crm.fragment.customerv2.CallLogsFragment;
import com.holike.crm.fragment.customerv2.ContractRegisterFragment;
import com.holike.crm.fragment.customerv2.InstalledFragment;
import com.holike.crm.fragment.customerv2.InvalidReturnFragment;
import com.holike.crm.fragment.customerv2.MeasureResultFragment;
import com.holike.crm.fragment.customerv2.ReceiptFragment;
import com.holike.crm.fragment.customerv2.SupervisorRoundsFragment;
import com.holike.crm.fragment.customerv2.UninstallFragment;
import com.holike.crm.fragment.customerv2.UnmeasuredFragment;
import com.holike.crm.fragment.customerv2.UploadInstallDrawingFragment;
import com.holike.crm.fragment.customerv2.UploadPlanFragment;
import com.holike.crm.util.KeyBoardUtil;

/**
 * Created by gallop on 2019/7/22.
 * Copyright holike possess 2019.
 */
public class CustomerMultiTypeActivity extends MyFragmentActivity {

    @Override
    protected BasePresenter attachPresenter() {
        return null;
    }

    @Override
    protected int setContentViewId() {
        return R.layout.activity_customer_multitype;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        String type = getIntent().getType();
        if (TextUtils.equals(type, CustomerValue.TYPE_ASSIGN_GUIDE)) { //分配导购
            setTitle(getString(R.string.house_manage_divide_guide));
            startFragment(new AssignGuideFragment(), getIntent().getExtras(), false);
        } else if (TextUtils.equals(type, CustomerValue.TYPE_ASSIGN_DESIGNER)) { //分配设计师
            setTitle(getString(R.string.house_manage_divide_designer));
            startFragment(new AssignDesignerFragment(), getIntent().getExtras(), false);
        } else if (TextUtils.equals(type, CustomerValue.TYPE_CALL_LOGS)) { //通话记录
            setTitle(getString(R.string.tips_call_logs));
            startFragment(new CallLogsFragment(), getIntent().getExtras(), false);
        } else if (TextUtils.equals(type, CustomerValue.TYPE_BEEN_LOST)) { //已流失
            setTitle(getString(R.string.house_manage_lossed));
            startFragment(new BeenLostFragment(), getIntent().getExtras(), false);
        } else if (TextUtils.equals(type, CustomerValue.TYPE_MEASURE_RESULT)) { //量尺结果
            setTitle(getString(R.string.customer_measure_result_tips));
            startFragment(new MeasureResultFragment(), getIntent().getExtras(), false);
        } else if (TextUtils.equals(type, CustomerValue.TYPE_UPLOAD_PLAN)) { //上传方案
            setTitle(getString(R.string.followup_upload_scheme_title));
            startFragment(new UploadPlanFragment(), getIntent().getExtras(), false);
        } else if (TextUtils.equals(type, CustomerValue.TYPE_UNMEASURED)) { //预约量尺
            setTitle(getString(R.string.customer_appointment_ruler_tips2));
            startFragment(new UnmeasuredFragment(), getIntent().getExtras(), false);
        } else if (TextUtils.equals(type, CustomerValue.TYPE_ROUNDS)) { //主管查房
            setTitle(getString(R.string.followup_supervisor_rounds_title));
            startFragment(new SupervisorRoundsFragment(), getIntent().getExtras(), false);
        } else if (TextUtils.equals(type, CustomerValue.TYPE_RECEIPT)) { //收款
            setTitle(getString(R.string.followup_receipt_title));
            startFragment(new ReceiptFragment(), getIntent().getExtras(), false);
        } else if (TextUtils.equals(type, CustomerValue.TYPE_CONTRACT_REGISTER)) { //合同登记
            setTitle(getString(R.string.followup_contract_title));
            startFragment(new ContractRegisterFragment(), getIntent().getExtras(), false);
        } else if (TextUtils.equals(type, CustomerValue.TYPE_UNINSTALL)) {
            setTitle(getString(R.string.customer_reservation_install_tips2));
            startFragment(new UninstallFragment(), getIntent().getExtras(), false);
        } else if (TextUtils.equals(type, CustomerValue.TYPE_INSTALL_DRAWING)) {
            setTitle(getString(R.string.followup_upload_install_drawing_title));
            startFragment(new UploadInstallDrawingFragment(), getIntent().getExtras(), false);
        } else if (TextUtils.equals(type, CustomerValue.TYPE_INSTALLED)) { //安装完成
            setTitle(getString(R.string.house_manage_installed));
            startFragment(new InstalledFragment(), getIntent().getExtras(), false);
        } else if (TextUtils.equals(type, CustomerValue.TYPE_INVALID_RETURN)) { //无效退回
            setTitle(getString(R.string.customer_invalid_return_title));
            startFragment(new InvalidReturnFragment(), getIntent().getExtras(), false);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (KeyBoardUtil.isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }
}
