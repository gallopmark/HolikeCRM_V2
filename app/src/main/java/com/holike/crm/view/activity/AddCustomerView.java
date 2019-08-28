package com.holike.crm.view.activity;

import android.view.View;
import android.widget.TextView;

import com.holike.crm.base.BaseView;
import com.holike.crm.bean.TypeIdBean;

import java.util.Date;
import java.util.List;

/**
 * Created by wqj on 2018/2/25.
 * 增加/编辑客户
 */

public interface AddCustomerView extends BaseView {
    void optionsSelect(int position);

    void showPickerView(List<? extends TypeIdBean.TypeIdItem> dates, String select);

    void getTypeIdSuccess(TypeIdBean typeIdBean);

    void getTypeIdFailed(String failed);

    void addCustomerSuccess();

    void addCustomerFailed(String failed);

    void getAssociateSuccess(List<? extends TypeIdBean.TypeIdItem> associates);

    void getAssociateFailed(String failed);

    void selectTime(Date date);


    void loading();
}
