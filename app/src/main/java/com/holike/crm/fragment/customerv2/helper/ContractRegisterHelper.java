package com.holike.crm.fragment.customerv2.helper;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.holike.crm.R;
import com.holike.crm.base.BaseFragment;
import com.holike.crm.base.CashierInputFilter;
import com.holike.crm.base.SimpleTextWatcher;
import com.holike.crm.bean.ShopRoleUserBean;
import com.holike.crm.dialog.MaterialDialog;
import com.holike.crm.enumeration.CustomerValue;
import com.holike.crm.helper.PickerHelper;
import com.holike.crm.http.ParamHelper;
import com.holike.crm.util.KeyBoardUtil;
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
public class ContractRegisterHelper extends IBaseHelper implements View.OnClickListener {
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
    //    private String mAmount; //本次收款
    private String mLastRemaining; //还剩尾款
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
        initEditText();
        setupSelectImages(picturesRecyclerView, R.string.tips_add_contract_images, true);
    }

    private void obtainBundleValue(Bundle bundle) {
        if (bundle != null) {
            mHouseId = bundle.getString(CustomerValue.HOUSE_ID);
            mShopId = bundle.getString("shopId");
//            String contractDate = bundle.getString("contractDate");
//            if (!TextUtils.isEmpty(contractDate)) {
//                mContractDate = TimeUtil.timeMillsFormat(contractDate, "yyyy-MM-dd");
//                mSignDateTextView.setText(TimeUtil.timeMillsFormat(contractDate));
//            }
//            mSalesAmount = bundle.getString("salesAmount");
//            mTurnoverEditText.setText(bundle.getString("salesAmount")); //成交金额
            String earnestHouse = bundle.getString("earnestHouse");  //已收订金
            if (TextUtils.isEmpty(earnestHouse)) {
                mEarnestHouse = "0.00";
            } else {
                mEarnestHouse = earnestHouse;
            }
            String receipt = mContext.getString(R.string.followup_deposit_received_tips2) + mEarnestHouse;
            mReceiptTextView.setText(receipt);
//            mThisPaymentEditText.setText(bundle.getString("contractPayAmount")); //本次收款
//            mRemainTailTextView.setText(bundle.getString("lastRemaining")); // 还剩尾款
//            mContractor = bundle.getString("contractor"); //签约人id
//            mSignName = bundle.getString("signName"); //签约人
//            mContractorTextView.setText(mSignName);
//            mRemarkEditText.setText(bundle.getString("remark"));
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
        try {
            if (TextUtils.isEmpty(total)) {
                total = "0.0";
            }
            if (TextUtils.isEmpty(thisPayment)) {
                thisPayment = "0.0";
            }
            double totalMoney = Double.parseDouble(total.toString());
            double receiptMoney = Double.parseDouble(mEarnestHouse);
            double thisPayMoney = Double.parseDouble(thisPayment.toString());
            mLastRemaining = String.valueOf(totalMoney - receiptMoney - thisPayMoney);
            mRemainTailTextView.setText(mLastRemaining);
        } catch (Exception ignored) {
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
                    mCallback.onQueryPaymentUsers(mShopId);
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
        final List<String> optionsItems = new ArrayList<>();
        for (ShopRoleUserBean.UserBean bean : mPaymentUserList) {
            optionsItems.add(bean.userName);
        }
        PickerHelper.showOptionsPicker(mContext, optionsItems, (options1, options2, options3, v) -> {
            mContractor = mPaymentUserList.get(options1).userId;
            mSignName = mPaymentUserList.get(options1).userName;
            mContractorTextView.setText(mSignName);
        });
    }

    private void showTimePickerView(final int type) {
        PickerHelper.showTimePicker(mContext, (date, v) -> {
            if (type == 1) {
                if (date.after(new Date())) { //合同登记-签约时间不能是未来时间,选择未来时间 默认为当前时间
                    mContractDate = TimeUtil.timeMillsFormat(new Date(), "yyyy-MM-dd");
                    mSignDateTextView.setText(TimeUtil.timeMillsFormat(new Date()));
                } else {
                    mContractDate = TimeUtil.timeMillsFormat(date, "yyyy-MM-dd");
                    mSignDateTextView.setText(TimeUtil.timeMillsFormat(date));
                }
            } else {
                mAppDeliveryDate = TimeUtil.timeMillsFormat(date, "yyyy-MM-dd");
                mDeliveryTimeTextView.setText(TimeUtil.timeMillsFormat(date));
            }
        });
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
                if (total <= 0.00) {  //成交金额不可以为0或负数.
                    mCallback.onRequired(mContext.getString(R.string.followup_turnover_tips3));
                } else {
                    String amount = mThisPaymentEditText.getText().toString();
                    if (TextUtils.isEmpty(amount)) {   //本次收款 必填
                        mCallback.onRequired(mContext.getString(R.string.enter) + mContext.getString(R.string.followup_this_payment_tips2));
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

    private void showConfirmDialog(String salesAmount, String amount) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_content_contractregist, new LinearLayout(mContext), false);
        TextView tvSignDate = view.findViewById(R.id.tv_sign_date);
        TextView tvTurnover = view.findViewById(R.id.tv_turnover);
        TextView tvReceipt = view.findViewById(R.id.tv_receipt_amount);
        TextView tvThisPayment = view.findViewById(R.id.tv_this_payment);
        TextView tvRemainTail = view.findViewById(R.id.tv_remain_tail);
        TextView tvDeliveryTime = view.findViewById(R.id.tv_delivery_time);
        TextView tvContractor = view.findViewById(R.id.tv_contractor);
        TextView tvRemark = view.findViewById(R.id.tv_remark);
        tvSignDate.setText(getText(R.string.customer_date_of_signing_tips, mSignDateTextView.getText().toString()));
        tvTurnover.setText(getText(R.string.followup_turnover_tips, salesAmount));
        tvReceipt.setText(getText(R.string.followup_deposit_received_tips, mEarnestHouse));
        tvThisPayment.setText(getText(R.string.followup_this_payment_tips, amount));
        tvRemainTail.setText(getText(R.string.followup_remain_tail_tips, mRemainTailTextView.getText().toString()));
        tvDeliveryTime.setText(getText(R.string.customer_contract_delivery_time_tips2, mDeliveryTimeTextView.getText().toString()));
        tvContractor.setText(getText(R.string.followup_contractor_tips, mContractorTextView.getText().toString()));
        tvRemark.setText(getText(R.string.tips_customer_remark2, mRemarkEditText.getText().toString()));
        new MaterialDialog.Builder(mContext).message(R.string.tips_message_confirm_house_info)
                .messageGravity(Gravity.START)
                .customView(view)
                .negativeButton(R.string.back, null)
                .positiveButton(R.string.confirm, (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    mCallback.onSaved(ParamHelper.Customer.contractRegister(mHouseId, mContractDate,
                            salesAmount, amount, mLastRemaining, mAppDeliveryDate, mContractor, mSignName, mRemarkEditText.getText().toString()),
                            mImageHelper.getSelectedImages());
                })
                .show();
    }

    private SpannableString getText(int stringRes, String content) {
        String source = mContext.getString(stringRes);
        int start = source.length();
        String notFilled = mContext.getString(R.string.not_filled_in);
        content = TextUtils.isEmpty(content) ? notFilled : content;
        if (TextUtils.equals(content, notFilled)) {
            start = 0;
        }
        source += content;
        int end = source.length();
        SpannableString ss = new SpannableString(source);
        int flags = SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE;
        if (TextUtils.equals(content, notFilled)) {
            ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.textColor21)), start, end, flags);
        } else {
            ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.textColor4)), start, end, flags);
            ss.setSpan(new StyleSpan(Typeface.BOLD), start, end, flags);
        }
        return ss;
    }

    public interface OnHelperCallback {
        void onQueryPaymentUsers(String shopId);

        void onRequired(CharSequence text);

        void onSaved(Map<String, Object> params, List<String> imagePaths);
    }
}
