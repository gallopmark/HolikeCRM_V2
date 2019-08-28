package com.holike.crm.fragment.customer.workflow;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.holike.crm.R;
import com.holike.crm.bean.TypeIdBean;
import com.holike.crm.fragment.customer.WorkflowFragment;
import com.holike.crm.http.MyJsonParser;
import com.holike.crm.presenter.activity.AddCustomerPresenter;
import com.holike.crm.view.fragment.WorkflowView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wqj on 2018/8/6.
 * 店长查房
 */

public class ShoperCheckFragment extends WorkflowFragment implements WorkflowView {
    @BindView(R.id.tv_shoper_check_result)
    TextView tvResult;
    @BindView(R.id.et_shoper_check_note)
    EditText etNote;

    private TypeIdBean.TypeIdItem result;
    private List<TypeIdBean.TypeIdItem> results;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_shoper_check;
    }

    @Override
    protected void init() {
        super.init();
        setTitle(getString(R.string.house_manage_shoper_check));
    }

    @Override
    public void success(Object success) {
        dismissLoading();
        if (success instanceof TypeIdBean) {
            results = AddCustomerPresenter.getTypeIdItems(((TypeIdBean) success).getCUSTOMER_PATIENT_ROUNDS());
            showPickerView(results, getText(tvResult), etNote);
        } else if (success instanceof String) {
            showShortToast(MyJsonParser.getShowMessage((String) success));
            operateSuccess();
        }
    }

    @Override
    public void failed(String failed) {
        dismissLoading();
        showShortToast(failed);
    }


    @OnClick({R.id.tv_shoper_check_result, R.id.tv_shoper_check_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_shoper_check_result:
                if (results == null) {
                    mPresenter.getTypeId();
                    showLoading();
                } else {
                    showPickerView(results, getText(tvResult), etNote);
                }
                break;
            case R.id.tv_shoper_check_save:
                if (result == null) {
                    showShortToast(R.string.tips_complete_information);
                } else {
                    mPresenter.shoperCheck(getText(etNote), customerStatus, houseId, result.getId(), operateCode, personalId, prepositionRuleStatus);
                    showLoading();
                }
                break;
        }
    }

    @Override
    protected void optionsSelect(int options) {
        result = results.get(options);
        tvResult.setText(result.getName());
    }
}
