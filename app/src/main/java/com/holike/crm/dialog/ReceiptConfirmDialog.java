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
 * 收款二次确认提示框
 */
public class ReceiptConfirmDialog extends CommonDialog {
    private OnConfirmListener mOnConfirmListener;

    public ReceiptConfirmDialog(Context context) {
        super(context);
    }

    public ReceiptConfirmDialog setOnConfirmListener(OnConfirmListener onConfirmListener) {
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
        return R.layout.dialog_receipt_confirm;
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
     * @param receiptType   收款类型 //1、收尾款2、订金3、退款
     * @param tailText      尾款
     * @param receiptTime   收款时间
     * @param receiptPerson 收款人员
     * @param amount        收款金额
     * @param category      收款类别
     * @param product       定制品类
     * @param remark        备注信息
     */
    public ReceiptConfirmDialog setReceiptInfo(int receiptType, String tailText, String receiptTime, String receiptPerson,
                                               String amount, String category, String product, String remark) {
        TextView tvReceiptTime = mContentView.findViewById(R.id.tv_receipt_time);
        TextView tvReceiptPerson = mContentView.findViewById(R.id.tv_receipt_person);
        TextView tvReceiptCategory = mContentView.findViewById(R.id.tv_receipt_category);
        TextView tvRemainTail = mContentView.findViewById(R.id.tv_remain_tail);
        TextView tvReceiptAmount = mContentView.findViewById(R.id.tv_receipt_amount);
        LinearLayout llCustomProduct = mContentView.findViewById(R.id.ll_custom_product);
        TextView tvCustomProduct = mContentView.findViewById(R.id.tv_custom_product);
        TextView rvRemark = mContentView.findViewById(R.id.tv_remark);
        tvReceiptTime.setText(getText(R.string.followup_receipt_time_tips, receiptTime, false));
        tvReceiptPerson.setText(getText(R.string.followup_receipt_person_tips, receiptPerson, false));
        tvReceiptCategory.setText(getText(R.string.followup_receipt_category_tips, category, false));
        tvReceiptAmount.setText(getText(R.string.followup_receipt_amount_tips, amount, false));
        if (receiptType == 1 || receiptType == 2) { //收尾款、订金
            if (receiptType == 1) { //收尾款
                tvRemainTail.setText(getText(R.string.followup_remain_tail_tips, tailText, true));
                tvRemainTail.setVisibility(View.VISIBLE);
                llCustomProduct.setVisibility(View.GONE);
            } else {
                tvRemainTail.setVisibility(View.GONE);
                llCustomProduct.setVisibility(View.VISIBLE);
                tvCustomProduct.setText(getText(0, product, false));
            }
        } else if (receiptType == 3) { //退款
            tvRemainTail.setVisibility(View.GONE);
            llCustomProduct.setVisibility(View.GONE);
        }
        rvRemark.setText(getText(R.string.tips_customer_remark2, remark, false));
        return this;
    }

    private SpannableString getText(int stringResId, String content, boolean isTextRed) {
        String source;
        if (stringResId == 0) {
            source = "";
        } else {
            source = mContext.getString(stringResId);
        }
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
            if (isTextRed) {
                ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.textColor15)), start, end, flags);
            } else {
                ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.textColor4)), start, end, flags);
            }
            ss.setSpan(new StyleSpan(Typeface.BOLD), start, end, flags);
        }
        return ss;
    }

    public interface OnConfirmListener {
        void onConfirm();
    }
}
