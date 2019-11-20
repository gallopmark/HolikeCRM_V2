package com.holike.crm.dialog;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.holike.crm.R;

/**
 * Created by pony on 2019/9/26.
 * Copyright holike possess 2019.
 * 合同登记二次确认提示框
 */
public class ContractConfirmDialog extends CommonDialog {
    private OnConfirmListener mOnConfirmListener;

    public ContractConfirmDialog(Context context) {
        super(context);
    }

    public ContractConfirmDialog setOnConfirmListener(OnConfirmListener onConfirmListener) {
        this.mOnConfirmListener = onConfirmListener;
        return this;
    }

    @Override
    public ViewGroup.MarginLayoutParams getLayoutParams() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(getWidth(), LinearLayout.LayoutParams.WRAP_CONTENT);
        params.topMargin = mContext.getResources().getDimensionPixelSize(R.dimen.dp_50);
        params.bottomMargin = mContext.getResources().getDimensionPixelSize(R.dimen.dp_50);
        return params;
    }

    @Override
    protected int bindContentView() {
        return R.layout.dialog_contract_confirm;
    }

    @Override
    protected void initView(View contentView) {
        contentView.findViewById(R.id.tv_cancel).setOnClickListener(view -> dismiss());
        contentView.findViewById(R.id.tv_confirm).setOnClickListener(view -> {
            dismiss();
            if (mOnConfirmListener != null) {
                mOnConfirmListener.onConfirm();
            }
        });
    }

    /**
     * @param signDate     签约日期
     * @param salesAmount  成交金额
     * @param earnestHouse 已收订金
     * @param amount       本次收款
     * @param tail         还剩尾款
     * @param deliveryTime 约定交货时间
     * @param contractor   签约人
     * @param remark       备注信息
     */
    public ContractConfirmDialog setContractInfo(String signDate, String salesAmount,
                                                 String earnestHouse, String amount,
                                                 String tail, String deliveryTime,
                                                 String contractor, String remark) {
        TextView tvSignDate = mContentView.findViewById(R.id.tv_sign_date);
        TextView tvTurnover = mContentView.findViewById(R.id.tv_turnover);
        TextView tvReceipt = mContentView.findViewById(R.id.tv_receipt_amount);
        TextView tvThisPayment = mContentView.findViewById(R.id.tv_this_payment);
        TextView tvRemainTail = mContentView.findViewById(R.id.tv_remain_tail);
        TextView tvDeliveryTime = mContentView.findViewById(R.id.tv_delivery_time);
        TextView tvContractor = mContentView.findViewById(R.id.tv_contractor);
        TextView tvRemark = mContentView.findViewById(R.id.tv_remark);
        tvSignDate.setText(getText(R.string.customer_date_of_signing_tips, signDate));
        tvTurnover.setText(getText(R.string.followup_turnover_tips, salesAmount));
        tvReceipt.setText(getText(R.string.followup_deposit_received_tips, earnestHouse));
        tvThisPayment.setText(getText(R.string.followup_this_payment_tips, amount));
        tvRemainTail.setText(getText(R.string.followup_remain_tail_tips, tail));
        tvDeliveryTime.setText(getText(R.string.customer_contract_delivery_time_tips2, deliveryTime));
        tvContractor.setText(getText(R.string.followup_contractor_tips, contractor));
        tvRemark.setText(getText(R.string.tips_customer_remark2, remark));
        return this;
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

    public interface OnConfirmListener {
        void onConfirm();
    }
}
