package com.holike.crm.fragment.customerv2.helper;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.holike.crm.base.IntentValue;
import com.holike.crm.bean.CurrentUserBean;
import com.holike.crm.customView.AppToastCompat;
import com.holike.crm.enumeration.CustomerValue;

/**
 * Created by pony on 2019/8/1.
 * Copyright holike possess 2019.
 */
abstract class GeneralHelper {
    protected Context mContext;
    String mHouseId; //房屋id

    CurrentUserBean mCurrentUser;

    GeneralHelper(Fragment fragment) {
        this.mContext = fragment.getContext();
        Bundle bundle = fragment.getArguments();
        if (bundle != null) {
            mHouseId = bundle.getString(CustomerValue.HOUSE_ID);
        }
        mCurrentUser = IntentValue.getInstance().getCurrentUser();
    }

    void showToast(CharSequence text) {
        AppToastCompat.makeText(mContext, text, Toast.LENGTH_SHORT).show();
    }
}
