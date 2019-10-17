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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wqj on 2018/8/3.
 * 添加沟通记录
 */
@Deprecated
public class AddRecordFragment extends WorkflowFragment implements WorkflowView {
    @BindView(R.id.tv_add_record_way)
    TextView tvWay;
    @BindView(R.id.et_add_record_result)
    EditText etResult;
    @BindView(R.id.tv_add_record_next_time)
    TextView tvNextTime;

    private TypeIdBean.TypeIdItem way;
    private List<TypeIdBean.TypeIdItem> ways;

    @Override
    protected void init() {
        super.init();
        setTitle(getString(R.string.house_manage_add_record));
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_add_record;
    }

    @OnClick({R.id.tv_add_record_way, R.id.tv_add_record_next_time, R.id.tv_add_record_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_add_record_way:
                if (typeIdBean == null) {
                    mPresenter.getTypeId();
                    showLoading();
                } else {
                    showPickerView(ways, getText(tvWay), etResult);
                }
                break;
            case R.id.tv_add_record_next_time:
                showTimePickerView(getActivity(), getText(tvNextTime), etResult);
                break;
            case R.id.tv_add_record_save:
                if (way == null || isTextEmpty(etResult)) {
                    showShortToast(R.string.tips_complete_information);
                } else {
                    mPresenter.addRecord(getText(etResult), customerStatus, houseId, getText(tvNextTime), operateCode, personalId, prepositionRuleStatus, way.getId());
                    showLoading();
                }
                break;
        }
    }

    /**
     * 选中沟通方式
     *
     * @param options
     */
    @Override
    protected void optionsSelect(int options) {
        way = ways.get(options);
        tvWay.setText(way.getName());
    }

    @Override
    protected void selectTime(Date date) {
        tvNextTime.setText(new SimpleDateFormat("yyyy.MM.dd").format(date));
    }

    /**
     * 获取沟通方式/保存成功
     *
     * @param success
     */
    @Override
    public void success(Object success) {
        dismissLoading();
        if (success instanceof TypeIdBean) {
            typeIdBean = (TypeIdBean) success;
            ways = AddCustomerPresenter.getTypeIdItems(typeIdBean.getCUSTOMER_CONTACT_CODE());
            showPickerView(ways, getText(tvWay), etResult);
        } else if (success instanceof String) {
            showShortToast(MyJsonParser.getShowMessage((String) success));
            operateSuccess();
        }
    }

    /**
     * 获取沟通方式/保存失败
     *
     * @param failed
     */
    @Override
    public void failed(String failed) {
        dismissLoading();
        showShortToast(failed);
    }
}
