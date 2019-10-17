package com.holike.crm.fragment.customer;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.holike.crm.R;
import com.holike.crm.bean.TypeIdBean;
import com.holike.crm.presenter.activity.AddCustomerPresenter;
import com.holike.crm.view.fragment.WorkflowView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wqj on 2018/8/8.
 * 编辑客户信息
 */
@Deprecated
public class EditCustomerFragment extends WorkflowFragment implements WorkflowView {
    protected final int TYPE_CUSTOMER_GENDER = 1;
    protected final int TYPE_CUSTOMER_SOURCE = 2;
    protected final int TYPE_SPECIAL_CUSTOMER = 3;

    @BindView(R.id.et_baseCustomer_customerName)
    EditText etName;
    @BindView(R.id.et_baseCustomer_customerPhone)
    EditText etPhone;
    @BindView(R.id.tv_baseCustomer_customerGender)
    TextView tvGender;
    @BindView(R.id.tv_baseCustomer_customerSource)
    TextView tvSource;
    @BindView(R.id.tv_baseCustomer_specialSource)
    TextView tvSpecialSource;

    protected OptionsPickerView mPickerView;
    protected int type;
    protected TypeIdBean typeIdBean;
    protected TypeIdBean.TypeIdItem gender, customerSource, specialCustomer;
    protected List<? extends TypeIdBean.TypeIdItem> genders, customerSources, specialCustomers, shops;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_edit_customer;
    }

    @Override
    protected void init() {
        super.init();
        setTitle(getString(R.string.customer_manage_edit_customer));
        etName.setText(personal.getUserName());
        etPhone.setText(personal.getPhoneNumber());
        tvGender.setText(personal.getGenderName());
        tvSource.setText(personal.getSourceName());
        tvSpecialSource.setText(personal.getSpecialCustomersName());
        etPhone.setEnabled(false);
        etPhone.setTextColor(getResources().getColor(R.color.textColor8));
        if (!TextUtils.isEmpty(personal.getGender())) {
            gender = new TypeIdBean.TypeIdItem(personal.getGender(), personal.getGenderName());
        }
        if (!TextUtils.isEmpty(personal.getSource())) {
            customerSource = new TypeIdBean.TypeIdItem(personal.getSource(), personal.getSourceName());
        }
        if (!TextUtils.isEmpty(personal.getSpecialCustomers())) {
            specialCustomer = new TypeIdBean.TypeIdItem(personal.getSpecialCustomers(), personal.getSpecialCustomersName());
        } else {
            specialCustomer = new TypeIdBean.TypeIdItem();
        }
    }

    @OnClick(R.id.btn_baseCustomer_save)
    public void onViewClicked() {
    }


    @OnClick({R.id.tv_baseCustomer_customerGender, R.id.tv_baseCustomer_customerSource, R.id.tv_baseCustomer_specialSource, R.id.btn_baseCustomer_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_baseCustomer_customerGender:
                type = TYPE_CUSTOMER_GENDER;
                showPickerView(genders, getText(tvGender));
                break;
            case R.id.tv_baseCustomer_customerSource:
                type = TYPE_CUSTOMER_SOURCE;
                showPickerView(customerSources, getText(tvSource));
                break;
            case R.id.tv_baseCustomer_specialSource:
                type = TYPE_SPECIAL_CUSTOMER;
                showPickerView(specialCustomers, getText(tvSpecialSource));
                break;
            case R.id.btn_baseCustomer_save:
                if (isTextEmpty(etName) || isTextEmpty(tvSource)) {
                    showShortToast(R.string.tips_complete_information);
                } else {
                    mPresenter.editCustomer("", gender == null ? "" : gender.getId(), personalId, getText(etPhone), "", customerSource.getId(), specialCustomer.getId(), getText(etName));
                    showLoading();
                }
                break;
        }
    }

    /**
     * 选中类型
     *
     * @param position
     */
    public void optionsSelect(int position) {
        switch (type) {
            case TYPE_CUSTOMER_GENDER:
                gender = genders.get(position);
                tvGender.setText(gender.getName());
                break;
            case TYPE_CUSTOMER_SOURCE:
                customerSource = customerSources.get(position);
                tvSource.setText(customerSource.getName());
                break;
            case TYPE_SPECIAL_CUSTOMER:
                specialCustomer = specialCustomers.get(position);
                tvSpecialSource.setText(specialCustomer.getName());
                break;
        }
    }

    /**
     * 设置类型id数据
     */
    private void setTypeId() {
        if (typeIdBean != null) {
            genders = AddCustomerPresenter.getTypeIdItems(typeIdBean.getCUSTOMER_GENDER_CODE());
            customerSources = AddCustomerPresenter.getTypeIdItems(typeIdBean.getCUSTOMER_SOURCE_CODE());
            specialCustomers = AddCustomerPresenter.getTypeIdItems(typeIdBean.getCUSTOMER_ESPECIALLY_TYPE());
            shops = AddCustomerPresenter.getTypeIdItems(typeIdBean.getShopId());
            switch (type) {
                case TYPE_CUSTOMER_GENDER:
                    showPickerView(genders, getText(tvGender));
                    break;
                case TYPE_CUSTOMER_SOURCE:
                    tvSource.setText(customerSource.getName());
                    break;
                case TYPE_SPECIAL_CUSTOMER:
                    showPickerView(specialCustomers, getText(tvSpecialSource));
                    break;
            }
        }
    }

    /**
     * 条件选择弹窗
     */
    public void showPickerView(List<? extends TypeIdBean.TypeIdItem> dates, String select) {
        if (mPickerView == null) {
            mPickerView = new OptionsPickerBuilder(mContext, (options1, options2, options3, v) -> optionsSelect(options1)).build();
        }
        if (dates != null && dates.size() > 0) {
            hideSoftInput(etName);
            mPickerView.setPicker(dates);
            mPickerView.setSelectOptions(AddCustomerPresenter.getSelectOption(dates, select));
            mPickerView.show();
        } else if (dates == null) {
            showLoading();
            mPresenter.getTypeId();
        } else {
            showShortToast("没有选择数据！");
        }
    }

    /**
     * 获取类型id/所属人/保存成功
     */
    @Override
    public void success(Object success) {
        dismissLoading();
        if (success instanceof TypeIdBean) {
            typeIdBean = (TypeIdBean) success;
            setTypeId();
        } else if (success instanceof String) {
            showShortToast((String) success);
            operateSuccess();
        }
    }

    /**
     * 获取类型id/所属人/保存失败
     */
    @Override
    public void failed(String failed) {
        dismissLoading();
        showShortToast(failed);
    }
}
