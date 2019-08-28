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

import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.holike.crm.R;
import com.holike.crm.adapter.MultipleChoiceAdapter;
import com.holike.crm.base.BaseFragment;
import com.holike.crm.base.CashierInputFilter;
import com.holike.crm.base.IntentValue;
import com.holike.crm.bean.CurrentUserBean;
import com.holike.crm.bean.DictionaryBean;
import com.holike.crm.bean.ShopRoleUserBean;
import com.holike.crm.bean.SysCodeItemBean;
import com.holike.crm.dialog.MaterialDialog;
import com.holike.crm.enumeration.CustomerValue;
import com.holike.crm.helper.PickerHelper;
import com.holike.crm.http.ParamHelper;
import com.holike.crm.manager.FlowLayoutManager;
import com.holike.crm.util.KeyBoardUtil;
import com.holike.crm.util.TimeUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by gallop on 2019/7/24.
 * Copyright holike possess 2019.
 * 收款帮助类
 */
public class ReceiptHelper extends IBaseHelper implements View.OnClickListener {
    private TextView mCategoryTextView, //收款类别选择
            mPayTimeTextView,   //选择收款时间
            mReceiverTextView;   //选择收款人员
    private TextView mReceiptTimeTipTextView,
            mReceiptPersonTipTextView,
            mMajorTipsTextView,
            mReceiptAmountTipTextView;
    private LinearLayout mMajorLayout;  //尾款layout（收尾款时候显示）
    private TextView mMajorTextView; //显示还剩尾款或者显示已收款
    private EditText mAmountEditText; //输入收款金额

    private LinearLayout mProductLayout; //定制品类（收定金或退款时显示）
    private RecyclerView mProductRecyclerView;
    private EditText mRemarkEditText;

    private boolean mIsContract; //是否已经合同登记  没有登记合同不能收尾款
    private String mPersonalId; //客户id
    private String mHouseId; //房屋id
    private String mShopId; //门店id，从客户详情带过来
    //    private String mSalesAmount = "0.00";  //成交金额 合同登记后的成交金额
    private double mTotalAmount; //合同总金额
    //    private String mPayAmount = "0.00"; //总收款
    private double mTotalReceipt; //总收款
    private String mType; //收款类型
    private String mPaymentTime; //收款时间
    private String mReceiverId; //收款人id
    private String mReceiver; //收款人姓名
    private String mCustomProduct; //定制品类
    private String mCustomProductName; //定制品类名称
    private int mReceiptType = 0; //收款类型 1尾款 2定金 3退款
    private double mLastRemain; //还剩尾款
    private MultipleChoiceAdapter mAdapter;


    private OnHelperCallback mCallback;
    private SysCodeItemBean mSystemCode;
    private List<ShopRoleUserBean.UserBean> mPaymentUserList; //门店收款人

    public ReceiptHelper(BaseFragment<?, ?> fragment, OnHelperCallback callback) {
        super(fragment);
        this.mCallback = callback;
        mSystemCode = IntentValue.getInstance().getSystemCode();
        initView(fragment.getContentView());
        obtainBundleValue(fragment.getArguments());
        setDefaultType();
    }

    private void initView(View contentView) {
        mCategoryTextView = contentView.findViewById(R.id.tv_receipt_category);
        mReceiptTimeTipTextView = contentView.findViewById(R.id.tv_receipt_time_tips); //收款时间提示语
        mPayTimeTextView = contentView.findViewById(R.id.tv_receipt_time);
        mReceiptPersonTipTextView = contentView.findViewById(R.id.tv_receipt_person_tips); //收款人员提示语
        mReceiverTextView = contentView.findViewById(R.id.tv_receipt_person);
        mMajorLayout = contentView.findViewById(R.id.ll_major);
        mMajorTipsTextView = contentView.findViewById(R.id.tv_major_tips);
        mMajorTextView = contentView.findViewById(R.id.tv_major);
        mReceiptAmountTipTextView = contentView.findViewById(R.id.tv_receipt_amount_tips);
        mAmountEditText = contentView.findViewById(R.id.et_receipt_amount);
        mProductLayout = contentView.findViewById(R.id.ll_custom_product);
        mProductRecyclerView = contentView.findViewById(R.id.rv_product);
        mRemarkEditText = contentView.findViewById(R.id.et_remark);
        RecyclerView picturesRecyclerView = contentView.findViewById(R.id.rv_pictures);
        setupSelectImages(picturesRecyclerView, R.string.tips_add_payment_pictures, true);
        mCategoryTextView.setOnClickListener(this);
        mReceiptTimeTipTextView.setOnClickListener(this);
        mReceiverTextView.setOnClickListener(this);
        contentView.findViewById(R.id.tvSave).setOnClickListener(this);
        mAmountEditText.setFilters(new InputFilter[]{new CashierInputFilter()});
        setDefaultValue();
        setProduct();
    }

    /*设置默认收款时间为当天，默认收款人为本账号人员*/
    private void setDefaultValue() {
        mPaymentTime = TimeUtil.timeMillsFormat(new Date(), "yyyy-MM-dd"); //收款时间默认为当天
        mPayTimeTextView.setText(TimeUtil.timeMillsFormat(new Date()));
        CurrentUserBean currentUser = IntentValue.getInstance().getCurrentUser();
        if (currentUser != null && currentUser.getUserInfo() != null) {
            mReceiverId = currentUser.getUserInfo().userId;  //默认收款人
            mReceiver = currentUser.getUserInfo().userName;
            mReceiverTextView.setText(mReceiver);
        }
    }

    private void setProduct() {
        if (mSystemCode != null) {
            mProductRecyclerView.setLayoutManager(new FlowLayoutManager());
            List<DictionaryBean> list = new ArrayList<>();
            //定制品类
            for (Map.Entry<String, String> entry : mSystemCode.getCustomerEarnestHouse().entrySet()) {
                list.add(new DictionaryBean(entry.getKey(), entry.getValue()));
            }
            mAdapter = new MultipleChoiceAdapter(mContext, list);
            mProductRecyclerView.setAdapter(mAdapter);
        }
    }

    private void obtainBundleValue(Bundle bundle) {
        if (bundle != null) {
            mPersonalId = bundle.getString(CustomerValue.PERSONAL_ID);
            mIsContract = bundle.getBoolean("isContractRegistration");
            mHouseId = bundle.getString(CustomerValue.HOUSE_ID);
            mShopId = bundle.getString("shopId");
            String salesAmount = bundle.getString("salesAmount");  //登记合同之后的成交金额
            try {
                mTotalAmount = Double.parseDouble(salesAmount);
            } catch (Exception ignored) {
            }
            String payAmount = bundle.getString("payAmount");  //总收款，还剩尾款 = 成交总金额 - 总收款 - 本次收款
            try {
                mTotalReceipt = Double.parseDouble(payAmount);
            } catch (Exception ignored) {
            }
            mLastRemain = mTotalAmount - mTotalReceipt - 0.0;
            mMajorTextView.setText(String.valueOf(mLastRemain));
        }
    }


    /**
     * 合同登记前，默认收款类别是订金，滑动可以选择退款；合同登记了之后，收款类别默认为尾款，但是仍然可以选择订金或者退款。定制品类可多选
     * 收款人默认显示本人账号，滑动可选本店其他导购、经销商旗下财务
     */
    private void setDefaultType() {
        if (mSystemCode != null) {
            Map<String, String> paymentMap = mSystemCode.getPaymentType();
            String type = null;
            String typeName = null;
            for (Map.Entry<String, String> entry : paymentMap.entrySet()) {
                if (mIsContract) {  //合同登记后，默认收款类型为尾款
                    if (TextUtils.equals(entry.getKey(), "尾款") || TextUtils.equals(entry.getValue(), "尾款")) {
                        type = entry.getKey();
                        typeName = entry.getValue();
                        setTail();
                        mReceiptType = 1;
                        break;
                    }
                } else { //合同登记前，默认收款类别是订金
                    if (TextUtils.equals(entry.getKey(), "订金") || TextUtils.equals(entry.getValue(), "订金")) {
                        type = entry.getKey();
                        typeName = entry.getValue();
                        setDeposit();
                        mReceiptType = 2;
                        break;
                    }
                }
            }
            mType = type;
            mCategoryTextView.setText(typeName);
        }
    }

    @Override
    public void onClick(View view) {
        KeyBoardUtil.hideSoftInput((Activity) mContext);
        switch (view.getId()) {
            case R.id.tv_receipt_category:  //选择收款类别
                if (mSystemCode == null) {
                    mCallback.onQuerySystemCode();
                } else {
                    selectReceiptCategory();
                }
                break;
            case R.id.tv_receipt_time:
                showTimePickerView();
                break;
            case R.id.tv_receipt_person:
                if (mPaymentUserList == null || mPaymentUserList.isEmpty()) {
                    mCallback.onQueryPaymentUsers(mShopId);
                } else {
                    selectReceiptPerson();
                }
                break;
            case R.id.tvSave:
                onSave();
                break;
        }
    }

    /*选择收款类别*/
    private void selectReceiptCategory() {
        List<String> keyList;
        List<String> valueList;
        Map<String, String> paymentMap = mSystemCode.getPaymentType();
        if (mIsContract) { //登记合同 才可选择尾款类型
            keyList = new ArrayList<>(paymentMap.keySet());
            valueList = new ArrayList<>(paymentMap.values());
        } else {
            keyList = new ArrayList<>();
            valueList = new ArrayList<>();
            for (Map.Entry<String, String> entry : mSystemCode.getPaymentType().entrySet()) {
                if (!(TextUtils.equals(entry.getKey(), "尾款") || TextUtils.equals(entry.getValue(), "尾款"))) {
                    keyList.add(entry.getKey());
                    valueList.add(entry.getValue());
                }
            }
        }
        showOptionsPickerView(valueList, (options1, options2, options3, v) -> {
            mType = keyList.get(options1);
            String typeName = valueList.get(options1);
            mCategoryTextView.setText(typeName);
            resetInitialState();
            if (TextUtils.equals(mType, "退款") || TextUtils.equals(typeName, "退款")) {
                setRefund();
            } else if (TextUtils.equals(mType, "订金") || TextUtils.equals(typeName, "订金")) {
                setDeposit();
            } else if (TextUtils.equals(mType, "尾款") || TextUtils.equals(typeName, "尾款")) {
                setTail();
            } else {
                setIdentical();
            }
        });
    }

    /*回复到初始状态*/
    private void resetInitialState() {
        setDefaultValue();
        mCustomProduct = null;
        mCustomProductName = null;
        mAmountEditText.setText("");
        mReceiptType = 0;
    }

    /*退款*/
    private void setRefund() {
        mReceiptTimeTipTextView.setText(mContext.getString(R.string.followup_receipt_refund_time));
        mReceiptPersonTipTextView.setText(mContext.getString(R.string.followup_receipt_refund_person));
        mMajorTipsTextView.setText(mContext.getString(R.string.followup_receipt_current_payment));
        mReceiptAmountTipTextView.setText(mContext.getString(R.string.followup_receipt_refund_amount));
        mMajorLayout.setVisibility(View.VISIBLE);
        mProductLayout.setVisibility(View.GONE);
        mMajorTextView.setText(String.valueOf(mTotalReceipt));
        mReceiptType = 3;
    }

    private void setIdentical() {
        mReceiptTimeTipTextView.setText(mContext.getString(R.string.followup_receipt_time_tips2));
        mReceiptPersonTipTextView.setText(mContext.getString(R.string.followup_receipt_person_tips2));
        mMajorTipsTextView.setText(mContext.getString(R.string.followup_remain_tail_tips));
        mReceiptAmountTipTextView.setText(mContext.getString(R.string.followup_receipt_amount_tips2));
    }

    /*订金*/
    private void setDeposit() {
        setIdentical();
        mMajorLayout.setVisibility(View.GONE);
        mProductLayout.setVisibility(View.VISIBLE);
        mReceiptType = 2;
    }

    /*尾款*/
    private void setTail() {
        setIdentical();
        mMajorLayout.setVisibility(View.VISIBLE);
        mProductLayout.setVisibility(View.GONE);
        mMajorTextView.setText(String.valueOf(mLastRemain));
        mReceiptType = 1;
    }

    public void setSystemCode(SysCodeItemBean bean) {
        this.mSystemCode = bean;
        selectReceiptCategory();
        setDefaultType();
        setProduct();
    }

    /*选择时间*/
    private void showTimePickerView() {
        PickerHelper.showTimePicker(mContext, (date, v) -> {
            if (date.after(new Date())) { //收款时间不可大于当前时间
                mPaymentTime = TimeUtil.timeMillsFormat(new Date(), "yyyy-MM-dd");
                mPayTimeTextView.setText(TimeUtil.timeMillsFormat(new Date()));
            } else {
                mPaymentTime = TimeUtil.timeMillsFormat(date, "yyyy-MM-dd");
                mPayTimeTextView.setText(TimeUtil.timeMillsFormat(date));
            }
        });
    }

    public void setPaymentUserList(List<ShopRoleUserBean.UserBean> list) {
        if (list != null && !list.isEmpty()) {
            mPaymentUserList = new ArrayList<>(list);
            selectReceiptPerson();
        }
    }

    /*选择收款人*/
    private void selectReceiptPerson() {
        final List<String> optionsItems = new ArrayList<>();
        for (ShopRoleUserBean.UserBean bean : mPaymentUserList) {
            optionsItems.add(bean.userName);
        }
        showOptionsPickerView(optionsItems, (options1, options2, options3, v) -> {
            mReceiverId = mPaymentUserList.get(options1).userId;
            mReceiver = mPaymentUserList.get(options1).userName;
            mReceiverTextView.setText(mReceiver);
        });
    }

    private void showOptionsPickerView(List<String> optionsItems, OnOptionsSelectListener listener) {
        PickerHelper.showOptionsPicker(mContext, optionsItems, listener);
    }

    private void onSave() {
        if (TextUtils.isEmpty(mType)) {
            String tips = mContext.getString(R.string.tips_please_select) + mContext.getString(R.string.followup_receipt_category_tips2);
            mCallback.onRequired(tips);
        } else {
            if (TextUtils.isEmpty(mPaymentTime)) {
                String tips = mContext.getString(R.string.tips_please_select) + mContext.getString(R.string.followup_receipt_time_tips2);
                mCallback.onRequired(tips);
            } else {
                if (TextUtils.isEmpty(mReceiverId)) {
                    String tips = mContext.getString(R.string.tips_please_select) + mContext.getString(R.string.followup_receipt_person_tips2);
                    mCallback.onRequired(tips);
                } else {
                    String amount = mAmountEditText.getText().toString();
                    if (TextUtils.isEmpty(amount)) {
                        mCallback.onRequired(mContext.getString(R.string.followup_this_payment_error_amount));
                    } else {
                        double a;
                        try {
                            a = Double.parseDouble(amount);
                        } catch (Exception ignored) {
                            mCallback.onRequired(mContext.getString(R.string.followup_this_payment_error_amount));
                            return;
                        }
                        if (a <= 0) {
                            mCallback.onRequired(mContext.getString(R.string.followup_this_payment_error_amount));
                        } else {
                              /*尾款不能为负数、收订金、收尾款、退款都不可以输入负数，并且收取的款项不能大于当前还剩尾款。
                        收款金额大于当前还剩尾款，保存时应提示：请输入正确金额。
                        退款输入金额不可以大于当前全部收款总和，收款金额大于当前全部收款总和，保存时应提示：请输入正确金额。*/
                            if (mReceiptType == 1 && a > mLastRemain) { //收取尾款 金额不能大于还剩尾款
                                mCallback.onRequired(mContext.getString(R.string.followup_this_payment_error_amount));
                            } else if (mReceiptType == 3 && a > mTotalReceipt) { //退款 退款金额不能大于当前总收款金额
                                mCallback.onRequired(mContext.getString(R.string.followup_this_payment_error_amount));
                            } else {
                                if (mReceiptType == 2) {  //收取订金 需要选择定制品类
                                    if (mAdapter == null || mAdapter.getSelectedItems().isEmpty()) {
                                        String tips = mContext.getString(R.string.tips_please_select) + mContext.getString(R.string.followup_custom_products2);
                                        mCallback.onRequired(tips);
                                    } else {
                                        List<DictionaryBean> list = mAdapter.getSelectedItems();
                                        StringBuilder sb = new StringBuilder();
                                        StringBuilder sbName = new StringBuilder();
                                        for (int i = 0; i < list.size(); i++) {
                                            sb.append(list.get(i).id);
                                            sbName.append(list.get(i).name);
                                            if (i != list.size() - 1) {
                                                sb.append(",");
                                                sbName.append("、");
                                            }
                                        }
                                        this.mCustomProduct = sb.toString();
                                        this.mCustomProductName = sbName.toString();
                                        lastStep();
                                    }
                                } else {
                                    lastStep();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void lastStep() {
        if (mImageHelper.getSelectedImages().isEmpty()) {
            String tips = mContext.getString(R.string.please) + mContext.getString(R.string.tips_add_payment_pictures);
            mCallback.onRequired(tips);
        } else {
            showConfirmDialog();
        }
    }

    private void showConfirmDialog() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_content_receipt, new LinearLayout(mContext), false);
        TextView tvReceiptTime = view.findViewById(R.id.tv_receipt_time);
        TextView tvReceiptPerson = view.findViewById(R.id.tv_receipt_person);
        TextView tvReceiptCategory = view.findViewById(R.id.tv_receipt_category);
        TextView tvRemainTail = view.findViewById(R.id.tv_remain_tail);
        TextView tvReceiptAmount = view.findViewById(R.id.tv_receipt_amount);
        LinearLayout llCustomProduct = view.findViewById(R.id.ll_custom_product);
        TextView tvCustomProduct = view.findViewById(R.id.tv_custom_product);
        TextView rvRemark = view.findViewById(R.id.tv_remark);
        if (mReceiptType == 1 || mReceiptType == 2) { //收尾款、订金
            if (mReceiptType == 1) { //收尾款
                String text = String.valueOf(mLastRemain);
                try {
                    String amount = mAmountEditText.getText().toString();
                    double a = Double.parseDouble(amount);
                    text = String.valueOf(mLastRemain - a);
                } catch (Exception ignored) {
                }
                tvRemainTail.setText(getText(R.string.followup_remain_tail_tips, text, true));
                tvRemainTail.setVisibility(View.VISIBLE);
                llCustomProduct.setVisibility(View.GONE);
            } else {
                tvRemainTail.setVisibility(View.GONE);
                llCustomProduct.setVisibility(View.VISIBLE);
                tvCustomProduct.setText(getText(0, mCustomProductName, false));
            }
            tvReceiptTime.setText(getText(R.string.followup_receipt_time_tips, mPayTimeTextView.getText().toString(), false));
            tvReceiptPerson.setText(getText(R.string.followup_receipt_person_tips, mReceiverTextView.getText().toString(), false));
            tvReceiptCategory.setText(getText(R.string.followup_receipt_category_tips, mCategoryTextView.getText().toString(), false));
            tvReceiptAmount.setText(getText(R.string.followup_receipt_amount_tips, "¥ " + mAmountEditText.getText().toString(), false));
        } else if (mReceiptType == 3) {
            tvRemainTail.setVisibility(View.GONE);
            llCustomProduct.setVisibility(View.GONE);
            tvReceiptTime.setText(getText(R.string.followup_receipt_refund_time_tips, mPayTimeTextView.getText().toString(), false));
            tvReceiptPerson.setText(getText(R.string.followup_receipt_refund_person_tips, mReceiverTextView.getText().toString(), false));
            tvReceiptCategory.setText(getText(R.string.followup_receipt_category_tips, mCategoryTextView.getText().toString(), false));
            tvReceiptAmount.setText(getText(R.string.followup_receipt_refund_amount_tips, "¥ " + mAmountEditText.getText().toString(), false));
        }
        rvRemark.setText(getText(R.string.tips_customer_remark2, mRemarkEditText.getText().toString(), false));
        new MaterialDialog.Builder(mContext)
                .message(R.string.tips_message_confirm_receipt_info)
                .messageGravity(Gravity.START)
                .customView(view)
                .negativeButton(R.string.back, null)
                .positiveButton(R.string.confirm, (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    mCallback.onSaved(ParamHelper.Customer.savePayment(mPersonalId, mHouseId, mShopId, mType, mPaymentTime,
                            mReceiverId, mReceiver, mAmountEditText.getText().toString(), mCustomProduct,
                            mRemarkEditText.getText().toString()), mImageHelper.getSelectedImages());
                }).show();
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

    public interface OnHelperCallback {
        void onQuerySystemCode();

        void onQueryPaymentUsers(String shopId);

        void onRequired(String tips);

        void onSaved(Map<String, Object> params, List<String> imagePaths);
    }
}
