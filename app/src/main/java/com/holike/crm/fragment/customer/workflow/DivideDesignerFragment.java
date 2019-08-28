package com.holike.crm.fragment.customer.workflow;

import android.view.View;
import android.widget.TextView;

import com.holike.crm.R;
import com.holike.crm.bean.DivideGuideBean;
import com.holike.crm.fragment.customer.WorkflowFragment;
import com.holike.crm.http.MyJsonParser;
import com.holike.crm.view.fragment.WorkflowView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wqj on 2018/8/3.
 * 分配设计师
 */

public class DivideDesignerFragment extends WorkflowFragment implements WorkflowView {
    @BindView(R.id.tv_divide_designer_select)
    TextView tvSelect;
    @BindView(R.id.tv_divide_designer_current)
    TextView tvCurrent;
    @BindView(R.id.tv_divide_designer_time)
    TextView tvTime;

    private DivideGuideBean divideGuideBean;
    private DivideGuideBean.ListBean listBean;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_divide_designer;
    }

    @Override
    protected void init() {
        super.init();
        setTitle(getString(R.string.house_manage_divide_designer));
        mPresenter.getDivideDesigner(houseInfoBean.getHouseId());
        showLoading();
    }

    /**
     * 选中设计师
     *
     * @param options
     */
    @Override
    protected void optionsSelect(int options) {
        listBean = divideGuideBean.getList().get(options);
        tvSelect.setText(listBean.getPickerViewText());
    }

    /**
     * 获取分配设计师/分配设计师成功
     *
     * @param success
     */
    @Override
    public void success(Object success) {
        dismissLoading();
        if (success instanceof DivideGuideBean) {
            divideGuideBean = (DivideGuideBean) success;
            tvCurrent.setText(getString(R.string.house_manage_current_designer) + divideGuideBean.getName());
            tvTime.setText(divideGuideBean.getTime());
        } else if (success instanceof String) {
            showShortToast(MyJsonParser.getShowMessage((String) success));
            operateSuccess();
        }
    }

    /**
     * 获取分配设计师/分配设计师失败
     */
    @Override
    public void failed(String failed) {
        dismissLoading();
        showShortToast(failed);
    }

    @OnClick({R.id.tv_divide_designer_select, R.id.tv_divide_designer_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_divide_designer_select:
                if (divideGuideBean != null)
                    showPickerView(divideGuideBean.getList(), getText(tvSelect), tvCurrent);
                break;
            case R.id.tv_divide_designer_save:
                if (listBean == null) {
                    showShortToast(R.string.tips_complete_information);
                } else {
                    mPresenter.divideDesigner(customerStatus, houseId, operateCode, personalId, prepositionRuleStatus, listBean.getUserId());
                    showLoading();
                }
                break;
        }
    }
}
