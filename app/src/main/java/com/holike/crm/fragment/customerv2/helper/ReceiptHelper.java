package com.holike.crm.fragment.customerv2.helper;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.holike.crm.R;
import com.holike.crm.adapter.MultipleChoiceAdapter;
import com.holike.crm.base.BaseFragment;
import com.holike.crm.base.CashierInputFilter;
import com.holike.crm.base.IntentValue;
import com.holike.crm.base.SimpleTextWatcher;
import com.holike.crm.bean.CurrentUserBean;
import com.holike.crm.bean.DictionaryBean;
import com.holike.crm.bean.ShopRoleUserBean;
import com.holike.crm.bean.SysCodeItemBean;
import com.holike.crm.dialog.OptionsPickerDialog;
import com.holike.crm.dialog.ReceiptConfirmDialog;
import com.holike.crm.enumeration.CustomerValue;
import com.holike.crm.helper.FlexboxManagerHelper;
import com.holike.crm.helper.IImageSelectHelper;
import com.holike.crm.helper.PickerHelper;
import com.holike.crm.helper.TextSpanHelper;
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
 * 收款帮助类
 */
public class ReceiptHelper extends IImageSelectHelper implements View.OnClickListener {
    private ViewStub mInfoViewStub; //收取订金（客户信息）
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
    private String mTailText; //显示尾款的文本
    private MultipleChoiceAdapter mAdapter;

    private boolean mCanOnlyDeposit; //是否是只能收取订金（首页点击“收取订金”按钮，进入最新联系客户列表，点击列表item进来，只能收取订金）

    private OnHelperCallback mCallback;
    private SysCodeItemBean mSystemCode;
    private List<ShopRoleUserBean.UserBean> mPaymentUserList; //门店收款人

    public ReceiptHelper(BaseFragment<?, ?> fragment, OnHelperCallback callback) {
        super(fragment);
        this.mCallback = callback;
        mSystemCode = IntentValue.getInstance().getSystemCode();
        View contentView = fragment.getContentView();
        initView(contentView);
        obtainBundleValue(fragment.getArguments());
        setDefaultType();
        setImageSelect(contentView);
    }

    private void initView(View contentView) {
        mInfoViewStub = contentView.findViewById(R.id.vs_info);
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
        mCategoryTextView.setOnClickListener(this);
        mReceiverTextView.setOnClickListener(this);
        mPayTimeTextView.setOnClickListener(this);
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

    /*收取订金展示“定制品类”*/
    private void setProduct() {
        if (mSystemCode != null) {
            mProductRecyclerView.setLayoutManager(FlexboxManagerHelper.getDefault(mContext));
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
            mIsContract = bundle.getBoolean("isContractRegistration", false);
            mHouseId = bundle.getString(CustomerValue.HOUSE_ID);
            mShopId = bundle.getString("shopId");
//            String salesAmount = bundle.getString("salesAmount");  //登记合同之后的成交金额
            //    private String mSalesAmount = "0.00";  //成交金额 合同登记后的成交金额
            //合同总金额
//            double totalAmount = ParseUtils.parseDouble(salesAmount);
            String lastRemaining = bundle.getString("lastRemaining");  //还剩尾款
            String payAmount = bundle.getString("payAmount");  //总收款，还剩尾款 = 成交总金额 - 总收款 - 本次收款
            mTotalReceipt = ParseUtils.parseDouble(payAmount);
            mLastRemain = ParseUtils.parseDouble(lastRemaining);
//            mLastRemain = totalAmount - mTotalReceipt - 0.0;
            mTailText = NumberUtil.decimals(mLastRemain);
            mMajorTextView.setText(mTailText);
            mCanOnlyDeposit = bundle.getBoolean("canOnlyDeposit", false);
            setCanOnlyDeposit(bundle);
        }
    }

    /*收取订金客户进来，头部展示客户信息，并且不能选择其他收款类型*/
    private void setCanOnlyDeposit(Bundle bundle) {
        if (mCanOnlyDeposit) {
            //只能收取订金时 不可选其他收款类型
            mCategoryTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            mCategoryTextView.setEnabled(false);
            View view = mInfoViewStub.inflate();
            final TextSpanHelper helper = TextSpanHelper.from(mContext);
            TextView tvName = view.findViewById(R.id.tv_name);
            tvName.setText(helper.generalStyle(R.string.customer_name_tips, bundle.getString("name"), R.color.textColor4));
            TextView tvPhoneWx = view.findViewById(R.id.tv_phoneWx);
            String wxNumber = bundle.getString("wxNumber");
            if (!TextUtils.isEmpty(wxNumber)) {
                tvPhoneWx.setText(helper.generalStyle(R.string.customer_wechat_tips, wxNumber, R.color.textColor4));
            } else {
                tvPhoneWx.setText(helper.generalStyle(R.string.customer_phone_tips, bundle.getString("phone"), R.color.textColor4));
            }
            TextView tvAddress = view.findViewById(R.id.tv_address);
            tvAddress.setText(helper.generalStyle(R.string.customer_address_tips, bundle.getString("address"), R.color.textColor4));
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
                if (mCanOnlyDeposit) {
                    if (TextUtils.equals(entry.getKey(), "订金") || TextUtils.equals(entry.getValue(), "订金")) {
                        type = entry.getKey();
                        typeName = entry.getValue();
                        setDeposit();
                        break;
                    }
                } else {
                    if (mIsContract) {  //合同登记后，默认收款类型为尾款
                        if (TextUtils.equals(entry.getKey(), "尾款") || TextUtils.equals(entry.getValue(), "尾款")) {
                            type = entry.getKey();
                            typeName = entry.getValue();
                            setTail();
                            break;
                        }
                    } else { //合同登记前，默认收款类别是订金
                        if (TextUtils.equals(entry.getKey(), "订金") || TextUtils.equals(entry.getValue(), "订金")) {
                            type = entry.getKey();
                            typeName = entry.getValue();
                            setDeposit();
                            break;
                        }
                    }
                }
            }
            mType = type;
            mCategoryTextView.setText(typeName);
        }
    }

    private void setImageSelect(View contentView) {
        RecyclerView picturesRecyclerView = contentView.findViewById(R.id.rv_pictures);
        setupSelectImages(picturesRecyclerView, R.string.tips_add_payment_pictures, true);
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
//        List<String> keyList;
//        List<String> valueList;
        List<DictionaryBean> optionsItem = new ArrayList<>();
        Map<String, String> paymentMap = mSystemCode.getPaymentType();
        if (mIsContract) { //登记合同 才可选择尾款类型
            for (Map.Entry<String, String> entry : paymentMap.entrySet()) {
                optionsItem.add(new DictionaryBean(entry.getKey(), entry.getValue()));
            }
        } else {
            for (Map.Entry<String, String> entry : paymentMap.entrySet()) {
                if (!(TextUtils.equals(entry.getKey(), "尾款") || TextUtils.equals(entry.getValue(), "尾款"))) {
                    optionsItem.add(new DictionaryBean(entry.getKey(), entry.getValue()));
                }
            }
        }
        showOptionsPickerView(optionsItem, (position, bean) -> {
            mType = bean.id;
            String typeName = bean.name;
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
        mAmountEditText.removeTextChangedListener(mAmountTextWatcher);
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
        mMajorTextView.setText(NumberUtil.decimals(mLastRemain));
        mReceiptType = 1;
        mAmountEditText.addTextChangedListener(mAmountTextWatcher);
    }

    private TextWatcher mAmountTextWatcher = new SimpleTextWatcher() {
        @Override
        public void onTextChanged(CharSequence cs, int i, int i1, int i2) {
            setLastRemain(cs);
        }
    };

    private void setLastRemain(CharSequence cs) {
        double a = 0.0;
        if (!TextUtils.isEmpty(cs)) {
            String amount = cs.toString();
            a = ParseUtils.parseDouble(amount);
        }
        mTailText = NumberUtil.decimals(mLastRemain - a);
        mMajorTextView.setText(mTailText);
    }

    public void setSystemCode(SysCodeItemBean bean) {
        this.mSystemCode = bean;
        selectReceiptCategory();
        setDefaultType();
        setProduct();
    }

    /*选择时间 收款时间不能选未来时间*/
    private void showTimePickerView() {
        PickerHelper.showTimePicker(mContext, null, new Date(), date -> {
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
        final List<DictionaryBean> optionsItems = new ArrayList<>();
        for (ShopRoleUserBean.UserBean bean : mPaymentUserList) {
            optionsItems.add(new DictionaryBean(bean.userId, bean.userName));
        }
        showOptionsPickerView(optionsItems, (position, bean) -> {
            mReceiverId = bean.id;
            mReceiver = bean.name;
            mReceiverTextView.setText(mReceiver);
        });
    }

    private void showOptionsPickerView(List<DictionaryBean> optionsItems, OptionsPickerDialog.OnOptionPickerListener listener) {
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
                        mCallback.onRequired(mAmountEditText.getHint());
                    } else {
                        double a = ParseUtils.parseDouble(amount);
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
                                    if (mIsContract) { //合同登记之后 ，收取订金不能大于还剩尾款
                                        if (a > mLastRemain) {
                                            mCallback.onRequired(mContext.getString(R.string.followup_this_payment_error_amount));
                                        } else {
                                            nextStep();
                                        }
                                    } else { //未登记合同之前收取订金  随便填随便收 产品没逼数
                                        nextStep();
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

    /*收定金需要定制品类*/
    private void nextStep() {
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
    }

    /*最后一步添加收款图片*/
    private void lastStep() {
        if (mImageHelper.getSelectedImages().isEmpty()) {
            mCallback.onRequired(mContext.getString(R.string.please) + mContext.getString(R.string.tips_add_payment_pictures));
        } else {
            showConfirmDialog();
        }
    }

    /*二次确认弹窗提醒*/
    private void showConfirmDialog() {
        String amountText = "¥ " + NumberUtil.decimals(mAmountEditText.getText().toString());
        new ReceiptConfirmDialog(mContext).setReceiptInfo(mReceiptType,
                mTailText, mPayTimeTextView.getText().toString(), mReceiverTextView.getText().toString(),
                amountText, mCategoryTextView.getText().toString(),
                mCustomProductName, mRemarkEditText.getText().toString()).setOnConfirmListener(() ->
                mCallback.onSaved(ParamHelper.Customer.savePayment(mPersonalId, mHouseId, mShopId, mType, mPaymentTime,
                        mReceiverId, mReceiver, mAmountEditText.getText().toString(), mCustomProduct,
                        mRemarkEditText.getText().toString()), mImageHelper.getSelectedImages())).show();
    }

    public interface OnHelperCallback {
        void onQuerySystemCode();

        void onQueryPaymentUsers(String shopId);

        void onRequired(CharSequence tips);

        void onSaved(Map<String, Object> params, List<String> imagePaths);
    }
}
