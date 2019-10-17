package com.holike.crm.fragment.customerv2.helper;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.holike.crm.R;
import com.holike.crm.base.BaseFragment;
import com.holike.crm.base.CashierInputFilter;
import com.holike.crm.base.IntentValue;
import com.holike.crm.base.SimpleTextWatcher;
import com.holike.crm.bean.CurrentUserBean;
import com.holike.crm.bean.ShopRoleUserBean;
import com.holike.crm.dialog.ContractConfirmDialog;
import com.holike.crm.enumeration.CustomerValue;
import com.holike.crm.helper.IImageSelectHelper;
import com.holike.crm.helper.PickerHelper;
import com.holike.crm.http.ParamHelper;
import com.holike.crm.util.KeyBoardUtil;
import com.holike.crm.util.NumberUtil;
import com.holike.crm.util.ParseUtils;
import com.holike.crm.util.TimeUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by gallop on 2019/7/24.
 * Copyright holike possess 2019.
 * 合同登记帮助类
 */
public class ContractRegisterHelper extends IImageSelectHelper implements View.OnClickListener {
    private TextView mSignDateTextView;
    private EditText mTurnoverEditText;
    private TextView mReceiptTextView;
    private EditText mThisPaymentEditText;
    private TextView mRemainTailTextView;
    private TextView mDeliveryTimeTextView;
    private TextView mContractorTextView;
    private EditText mRemarkEditText;
    private String mHouseId; //房屋id
    private String mShopId; //门店id
    private String mContractDate;   //签约日期
    //    private String mSalesAmount; //成交金额
    private String mEarnestHouse = "0.00";  //已收订金
    private double mDeposit; //已收订金
    //    private String mAmount; //本次收款
    private String mLastRemaining; //还剩尾款
    private double mLastTail; //还剩尾款
    private String mAppDeliveryDate; //预定交货日期(必填)
    private String mContractor; //签约人id
    private String mSignName; //签约人名字

    private List<ShopRoleUserBean.UserBean> mPaymentUserList; //门店收款人数据集合
    private OnHelperCallback mCallback;

    public ContractRegisterHelper(BaseFragment<?, ?> fragment, OnHelperCallback callback) {
        super(fragment);
        this.mCallback = callback;
        initView(fragment.getContentView());
        obtainBundleValue(fragment.getArguments());
    }

    private void initView(View contentView) {
        mSignDateTextView = contentView.findViewById(R.id.tv_sign_date);
        mTurnoverEditText = contentView.findViewById(R.id.et_turnover);
        mReceiptTextView = contentView.findViewById(R.id.tv_receipt);
        mThisPaymentEditText = contentView.findViewById(R.id.et_this_payment);
        mRemainTailTextView = contentView.findViewById(R.id.tv_remain_tail);
        mDeliveryTimeTextView = contentView.findViewById(R.id.tv_delivery_time);
        mContractorTextView = contentView.findViewById(R.id.tv_contractor);
        mRemarkEditText = contentView.findViewById(R.id.et_remark);
        RecyclerView picturesRecyclerView = contentView.findViewById(R.id.rv_pictures);
        mSignDateTextView.setOnClickListener(this);
        mDeliveryTimeTextView.setOnClickListener(this);
        mContractorTextView.setOnClickListener(this);
        contentView.findViewById(R.id.tvSave).setOnClickListener(this);
        mContractDate = TimeUtil.timeMillsFormat(new Date(), "yyyy-MM-dd");
        mSignDateTextView.setText(TimeUtil.timeMillsFormat(new Date())); //签约日期默认为今日
        setDefaultValue();
        initEditText();
        setupSelectImages(picturesRecyclerView, R.string.tips_add_contract_images, true);
    }

    /*合同登记签约人默认本账号*/
    private void setDefaultValue() {
        CurrentUserBean bean = IntentValue.getInstance().getCurrentUser();
        if (bean != null && bean.getUserInfo() != null) {
            mContractor = bean.getUserInfo().userId;
            mContractorTextView.setText(bean.getUserInfo().userName);
        }
    }

    private void initEditText() {
        mTurnoverEditText.setFilters(new InputFilter[]{new CashierInputFilter()});
        mThisPaymentEditText.setFilters(new InputFilter[]{new CashierInputFilter()});
        mTurnoverEditText.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int i, int i1, int i2) {
                delivery(cs, mThisPaymentEditText.getText());
            }
        });
        mThisPaymentEditText.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int i, int i1, int i2) {
                delivery(mTurnoverEditText.getText(), cs);
            }
        });
    }

    private void delivery(CharSequence total, CharSequence thisPayment) {
        if (TextUtils.isEmpty(total)) {
            total = "0.0";
        }
        if (TextUtils.isEmpty(thisPayment)) {
            thisPayment = "0.0";
        }
        double totalMoney = NumberUtil.doubleValue(total.toString());
        double thisPayMoney = NumberUtil.doubleValue(thisPayment.toString());
        mLastTail = NumberUtil.doubleValue(totalMoney - mDeposit - thisPayMoney);
        mLastRemaining = String.valueOf(mLastTail);
        mRemainTailTextView.setText(NumberUtil.decimals(mLastTail));
    }

    private void obtainBundleValue(Bundle bundle) {
        if (bundle != null) {
            mHouseId = bundle.getString(CustomerValue.HOUSE_ID);
            mShopId = bundle.getString("shopId");
            String earnestHouse = bundle.getString("earnestHouse");  //已收订金
            if (TextUtils.isEmpty(earnestHouse)) {
                mEarnestHouse = "0.00";
            } else {
                mEarnestHouse = earnestHouse;
            }
            mDeposit = ParseUtils.parseDouble(mEarnestHouse);
            String receipt = mContext.getString(R.string.followup_deposit_received_tips2) + NumberUtil.decimals(mDeposit);
            mReceiptTextView.setText(receipt);
        }
    }

    @Override
    public void onClick(View view) {
        KeyBoardUtil.hideSoftInput((Activity) mContext);
        switch (view.getId()) {
            case R.id.tv_sign_date:
                showTimePickerView(1);
                break;
            case R.id.tv_delivery_time:
                showTimePickerView(2);
                break;
            case R.id.tv_contractor:
                if (mPaymentUserList == null || mPaymentUserList.isEmpty()) {
                    mCallback.onQueryContractUsers(mShopId);
                } else {
                    showOptionsView();
                }
                break;
            case R.id.tvSave:
                onSave();
                break;
        }
    }

    public void setPaymentUserList(List<ShopRoleUserBean.UserBean> list) {
        if (list != null && !list.isEmpty()) {
            mPaymentUserList = new ArrayList<>(list);
            showOptionsView();
        }
    }

    private void showOptionsView() {
        PickerHelper.showOptionsPicker(mContext, PickerHelper.list2OptionsItems(mPaymentUserList), (position, bean) -> {
            mContractor = bean.id;
            mSignName = bean.name;
            mContractorTextView.setText(mSignName);
        });
    }

    private void showTimePickerView(final int type) {
        if (type == 1) {  //合同登记不能选择未来时间，只能选择当天或过去时间
            PickerHelper.showTimePicker(mContext, null, new Date(), date -> {
                if (date.after(new Date())) {
                    mContractDate = TimeUtil.timeMillsFormat(new Date(), "yyyy-MM-dd");
                    mSignDateTextView.setText(TimeUtil.timeMillsFormat(new Date()));
                } else {
                    mContractDate = TimeUtil.timeMillsFormat(date, "yyyy-MM-dd");
                    mSignDateTextView.setText(TimeUtil.timeMillsFormat(date));
                }
            });
        } else {
            PickerHelper.showTimePicker(mContext, date -> {
                mAppDeliveryDate = TimeUtil.timeMillsFormat(date, "yyyy-MM-dd");
                mDeliveryTimeTextView.setText(TimeUtil.timeMillsFormat(date));
            });
        }
    }

    private void onSave() {
        if (TextUtils.isEmpty(mContractDate)) { //签约日期 必填项
            mCallback.onRequired(mContext.getString(R.string.tips_please_select) + mContext.getString(R.string.customer_date_of_signing_tips2));
        } else {
            String salesAmount = mTurnoverEditText.getText().toString();
            if (TextUtils.isEmpty(salesAmount)) {    //成交金额 必填
                mCallback.onRequired(mContext.getString(R.string.enter) + mContext.getString(R.string.followup_turnover_tips2));
            } else {
                double total;
                try {
                    total = Double.parseDouble(salesAmount);
                } catch (Exception ignored) {
                    mCallback.onRequired(mContext.getString(R.string.followup_turnover_tips3));
                    return;
                }
                if (total <= 0.00) {  //成交金额不可以为0或负数.还剩尾款不能为负数
                    mCallback.onRequired(mContext.getString(R.string.followup_turnover_tips3));
                } else {
                    String amount = mThisPaymentEditText.getText().toString();
                    if (TextUtils.isEmpty(amount)) {   //本次收款 必填
                        mCallback.onRequired(mContext.getString(R.string.enter) + mContext.getString(R.string.followup_this_payment_tips2));
                    } else {
                        double a;
                        try {
                            a = Double.parseDouble(amount);
                        } catch (Exception e) {
                            mCallback.onRequired(mContext.getString(R.string.followup_this_payment_error_amount));
                            return;
                        }
                        /*1、本次收款不能大于成交金额
                        2、本次收款+已收订金不能大于成交金额
                        3、还剩尾款不能为负数
                        */
                        if (a > total || (a + mDeposit) > total || mLastTail < 0.0) {
                            mCallback.onRequired(mContext.getString(R.string.followup_this_payment_error_amount));
                        } else {
                            if (TextUtils.isEmpty(this.mAppDeliveryDate)) {  //约定交货时间 必填
                                mCallback.onRequired(mContext.getString(R.string.tips_please_select) + mContext.getString(R.string.customer_contract_delivery_time_tips));
                            } else {
                                if (TextUtils.isEmpty(this.mContractor)) {    //签约人 必填
                                    mCallback.onRequired(mContext.getString(R.string.tips_please_select) + mContext.getString(R.string.followup_contractor_tips2));
                                } else {
                                    if (mImageHelper.getSelectedImages().isEmpty()) {
                                        mCallback.onRequired(mContext.getString(R.string.please) + mContext.getString(R.string.tips_add_contract_images));
                                    } else {
                                        showConfirmDialog(salesAmount, amount);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void showConfirmDialog(String salesAmount, String amount) {
        new ContractConfirmDialog(mContext).setContractInfo(mSignDateTextView.getText().toString(),
                NumberUtil.decimals(salesAmount), NumberUtil.decimals(mEarnestHouse),
                NumberUtil.decimals(amount), mRemainTailTextView.getText().toString(), mDeliveryTimeTextView.getText().toString(),
                mContractorTextView.getText().toString(), mRemarkEditText.getText().toString()).setOnConfirmListener(() ->
                mCallback.onSaved(ParamHelper.Customer.contractRegister(mHouseId, mContractDate,
                        salesAmount, amount, mLastRemaining, mAppDeliveryDate, mContractor, mSignName, mRemarkEditText.getText().toString()),
                        mImageHelper.getSelectedImages())).show();
    }

    public interface OnHelperCallback {
        void onQueryContractUsers(String shopId);

        void onRequired(CharSequence text);

        void onSaved(Map<String, Object> params, List<String> imagePaths);
    }
}
