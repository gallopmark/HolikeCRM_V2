package com.holike.crm.activity.customer;


import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.holike.crm.R;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.base.MyFragmentActivity;
import com.holike.crm.enumeration.CustomerValue;
import com.holike.crm.fragment.customerv2.CustomerManagerV2Fragment;
import com.holike.crm.util.KeyBoardUtil;

/*客户管理*/
public class CustomerDetailV2Activity extends MyFragmentActivity {

    /**
     * 客户详情
     *
     * @param personalId      客户id
     * @param houseId         房屋id
     * @param isHighSeasHouse 是否是公海房屋
     */
    public static void open(BaseActivity<?, ?> activity, String personalId, String houseId, boolean isHighSeasHouse) {
        Bundle bundle = new Bundle();
        bundle.putString(CustomerValue.PERSONAL_ID, personalId);
        bundle.putString(CustomerValue.HOUSE_ID, houseId);
        bundle.putBoolean(CustomerValue.HIGH_SEAS_HOUSE_FLAG, isHighSeasHouse);
        activity.startActivity(CustomerDetailV2Activity.class, bundle);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
//        startFragment(new CustomerDetailFragment2(), getIntent().getExtras(), false);
        startFragment(new CustomerManagerV2Fragment(), getIntent().getExtras(), false);  //v2.0
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
////        setRightMenu(HomePagePresenter.getMsgNum());
//        setRightMsg(HomePagePresenter.isNewMsg());
//    }
//
//    @Override
//    protected void clickRightMenu() {
//        startActivity(MessageV2Activity.class);
//    }

    @Override
    protected BasePresenter attachPresenter() {
        return null;
    }

    @Override
    protected int setContentViewId() {
        return R.layout.activity_customer_detail;
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
