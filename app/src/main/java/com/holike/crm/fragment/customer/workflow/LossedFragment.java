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
 * 已流失
 */

public class LossedFragment extends WorkflowFragment implements WorkflowView {
    private final int TYPE_RESON = 1;
    private final int TYPE_GO = 2;
    @BindView(R.id.tv_lossed_reason)
    TextView tvReason;
    @BindView(R.id.tv_lossed_go)
    TextView tvGo;
    @BindView(R.id.et_lossed_note)
    EditText etNote;

    private int type;
    private TypeIdBean.TypeIdItem reason, go;
    private List<TypeIdBean.TypeIdItem> reasons, gos;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_lossed;
    }

    @Override
    protected void init() {
        super.init();
        setTitle(getString(R.string.house_manage_lossed));
    }

    @Override
    public void success(Object success) {
        dismissLoading();
        if (success instanceof TypeIdBean) {
            if (type == TYPE_RESON) {
                reasons = AddCustomerPresenter.getTypeIdItems(((TypeIdBean) success).getCUSTOMER_LOST_REASON());
                showPickerView(reasons, getText(tvReason), etNote);
            } else {
                gos = AddCustomerPresenter.getTypeIdItems(((TypeIdBean) success).getLOSE_OF_THE_BRAND());
                showPickerView(gos, getText(tvGo), etNote);
            }
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


    @OnClick({R.id.tv_lossed_reason, R.id.tv_lossed_go, R.id.tv_lossed_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_lossed_reason:
                type = TYPE_RESON;
                if (typeIdBean == null) {
                    mPresenter.getTypeId();
                    showLoading();
                } else {
                    showPickerView(reasons, getText(tvReason), etNote);
                }
                break;
            case R.id.tv_lossed_go:
                type = TYPE_GO;
                if (typeIdBean == null) {
                    mPresenter.getTypeId();
                    showLoading();
                } else {
                    showPickerView(gos, getText(tvGo), etNote);
                }
                break;
            case R.id.tv_lossed_save:
                if (isTextEmpty(tvReason) || isTextEmpty(tvGo)) {
                    showShortToast(R.string.tips_complete_information);
                } else {
                    mPresenter.lossed(getText(etNote), customerStatus, houseId, go.getId(), operateCode, personalId, prepositionRuleStatus, reason.getId());
                    showLoading();
                }
                break;
        }
    }

    @Override
    protected void optionsSelect(int options) {
        if (type == TYPE_RESON) {
            reason = reasons.get(options);
            tvReason.setText(reason.getName());
        } else {
            go = gos.get(options);
            tvGo.setText(go.getName());
        }
    }
}
