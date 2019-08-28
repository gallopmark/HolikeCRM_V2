package com.holike.crm.fragment.bank;

import android.content.Intent;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gallopmark.imagepicker.model.ImagePicker;
import com.holike.crm.R;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.PayListBean;
import com.holike.crm.bean.UploadByRelationBean;
import com.holike.crm.helper.UploadImgHelper;
import com.holike.crm.presenter.fragment.PayInfoPresenter;
import com.holike.crm.util.Constants;
import com.holike.crm.util.TimeUtil;
import com.holike.crm.view.fragment.PayInfoView;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class AddOrModifyPayInfoFragment extends MyFragment<PayInfoPresenter, PayInfoView> implements PayInfoView {
    PayListBean payListBean;
    @BindView(R.id.et_details_bank_line_number)
    EditText etDetailsBankLineNumber;
    @BindView(R.id.et_details_bank_name)
    EditText etDetailsBankName;
    @BindView(R.id.et_details_bank_account_name)
    EditText etDetailsBankAccountName;
    @BindView(R.id.et_details_amount_of_money)
    EditText etDetailsAmountOfMoney;
    @BindView(R.id.et_details_bank_account_number)
    EditText etDetailsBankAccountNumber;
    @BindView(R.id.tv_details_trading_hours)
    TextView tvDetailsTradingHours;
    @BindView(R.id.rv_add_customer)
    RecyclerView rv;
    @BindView(R.id.ll_order_details_order_dealer_factor)
    LinearLayout llModifyPz;
    private UploadImgHelper.OnClickListener clickImgListener;
    private List<String> imgs = new ArrayList<>();
    private String TYPE;
    private static String TYPE_ADD = "add";
    private static String TYPE_MODIFY = "modify";
    private String relationId = "";

    @Override
    protected PayInfoPresenter attachPresenter() {
        return new PayInfoPresenter();
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_bank_modify;
    }

    @Override
    protected void init() {
        super.init();
        if (getArguments() != null) {
            setTitle(getString(R.string.play_details_modify));
            payListBean = (PayListBean) getArguments().getSerializable(Constants.PAY_LIST);
            initModifyView();
            TYPE = TYPE_MODIFY;
        } else {
            setTitle(getString(R.string.play_details_add));
            TYPE = TYPE_ADD;
            llModifyPz.setVisibility(View.VISIBLE);
        }
        etDetailsAmountOfMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (s.length() == 1 && s.toString().equals(".")) {
                    etDetailsAmountOfMoney.setText("");
                } else if (s.length() > 1 && etDetailsAmountOfMoney.getText().toString().contains(".")) {
                    if (etDetailsAmountOfMoney.getText().toString().indexOf(".", etDetailsAmountOfMoney.getText().toString().indexOf(".") + 1) > 0) {
                        etDetailsAmountOfMoney.setText(etDetailsAmountOfMoney.getText().toString().substring(0, etDetailsAmountOfMoney.getText().toString().length() - 1));
                        etDetailsAmountOfMoney.setSelection(etDetailsAmountOfMoney.getText().toString().length());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        setImageList();
    }

    private void setImageList() {
        rv.setNestedScrollingEnabled(false);
        rv.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        clickImgListener = new UploadImgHelper.OnClickListener() {
            @Override
            public void addImg() {
                ImagePicker.builder().maxSelectCount(9 - imgs.size()).start(AddOrModifyPayInfoFragment.this);
            }

            @Override
            public void delImg(String img) {
                imgs.remove(img);
                UploadImgHelper.setImgList(getActivity(), rv, imgs, getString(R.string.feedback_add_deposit_receipt), 9, clickImgListener);

            }
        };
        UploadImgHelper.setImgList(getActivity(), rv, imgs, getString(R.string.feedback_add_deposit_receipt), 9, clickImgListener);
    }

    private void initModifyView() {
        etDetailsBankLineNumber.setText(textEmpty(payListBean.getRecip_bk_no()));
        etDetailsBankName.setText(textEmpty(payListBean.getRecip_bk_name()));
        etDetailsBankAccountNumber.setText(textEmpty(payListBean.getRecip_acc_no()));
        etDetailsBankAccountName.setText(textEmpty(payListBean.getRecip_name()));
        etDetailsAmountOfMoney.setText(textEmpty(payListBean.getCredit_amount()));
        tvDetailsTradingHours.setText(TimeUtil.stringToString(textEmpty(payListBean.getTransaction_date()), "yyyy-MM-dd", "yyyy.MM.dd"));
        setImageData();
        llModifyPz.setVisibility(View.GONE);
        relationId = payListBean.getRelationId();
    }

    private void setImageData() {
        if (payListBean.getImageList() != null) {
            for (int i = 0; i < payListBean.getImageList().size(); i++) {
                imgs.add(i, payListBean.getImageList().get(i).getImageUrl());
            }
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImagePicker.DEFAULT_REQUEST_CODE && data != null) {
            ArrayList<String> images = data.getStringArrayListExtra(ImagePicker.SELECT_RESULT);
//                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            imgs.addAll(images);
//                for (ImageItem imageItem : images) {
//                    imgs.add(imageItem.path);
//
//                }

            UploadImgHelper.setImgList(getActivity(), rv, imgs, getString(R.string.details_confidential_information_add), 9, clickImgListener);
        }
    }

    @OnClick({R.id.tv_details_trading_hours, R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_details_trading_hours:
                showTimePickerView(getActivity(), TimeUtil.stampToString(getText(tvDetailsTradingHours), "yyyy.MM.dd"), tvDetailsTradingHours);
                break;
            case R.id.btn_save:

                if (isTextEmpty(etDetailsAmountOfMoney) ||
                        isTextEmpty(tvDetailsTradingHours) ||
                        isTextEmpty(etDetailsBankAccountNumber) ||
                        isTextEmpty(etDetailsBankName) ||
                        isTextEmpty(etDetailsBankAccountName) || rv.getAdapter().getItemCount() < 2) {
                    showShortToast(R.string.tips_complete_information);
                } else {
                    showLoading();
                    if (TYPE.equals(TYPE_MODIFY)) {
                        mPresenter.modifyByOnline(getText(etDetailsAmountOfMoney),
                                TimeUtil.stringToStamp(getText(tvDetailsTradingHours), "yyyy.MM.dd"),
                                payListBean.getId(),
                                getText(etDetailsBankAccountNumber),
                                getText(etDetailsBankName),
                                getText(etDetailsBankLineNumber),
                                getText(etDetailsBankAccountName),
                                relationId);
                    } else {

                        mPresenter.selectImg(mContext, relationId, imgs.get(i), i);
                    }


                }
                break;
        }
    }


    /**
     * 选择日期
     *
     * @param date
     */
    @Override
    protected void selectTime(Date date) {
        tvDetailsTradingHours.setText(new SimpleDateFormat("yyyy.MM.dd").format(date));
    }


    private void updateInfo() {
        payListBean.setCreate_date(getText(tvDetailsTradingHours));
        payListBean.setCredit_amount(getText(etDetailsAmountOfMoney));
        payListBean.setRecip_acc_no(getText(etDetailsBankAccountNumber));
        payListBean.setRecip_bk_name(getText(etDetailsBankName));
        payListBean.setRecip_bk_no(getText(etDetailsBankLineNumber));
        payListBean.setRecip_name(getText(etDetailsBankAccountName));
    }

    @Override
    public void onFail(String errorMsg) {
        showShortToast(errorMsg);
        i = 0;
    }

    @Override
    public void onSuccess(String stateCode) {
        dismissLoading();
        if (TYPE.equals(TYPE_MODIFY)) {
            updateInfo();
            Map<String, Serializable> params = new HashMap<>();
            params.put(Constants.PAY_LIST, payListBean);
            finishFragment(0, Constants.RESULT_CODE_OPERATE_SUCCESS, params);
        } else {
            finishFragment(0, Constants.RESULT_CODE_OPERATE_SUCCESS, null);
        }

    }

    int i = 0;

    @Override
    public void onRelationSuccess(UploadByRelationBean bean, int uploadIndex) {
        if (TYPE.equals(TYPE_MODIFY)) {
            mPresenter.modifyByOnline(getText(etDetailsAmountOfMoney),
                    TimeUtil.stringToStamp(getText(tvDetailsTradingHours), "yyyy.MM.dd"),
                    payListBean.getId(),
                    getText(etDetailsBankAccountNumber),
                    getText(etDetailsBankName),
                    getText(etDetailsBankLineNumber),
                    getText(etDetailsBankAccountName),
                    bean.getRelationId());
        } else {
            i++;
            if (i == imgs.size()) {
                mPresenter.addByOnline(getText(etDetailsAmountOfMoney),
                        TimeUtil.stringToStamp(getText(tvDetailsTradingHours), "yyyy.MM.dd"),
                        getText(etDetailsBankAccountNumber),
                        getText(etDetailsBankName),
                        getText(etDetailsBankLineNumber),
                        getText(etDetailsBankAccountName),
                        bean.getRelationId());
            } else {
                mPresenter.selectImg(mContext, bean.getRelationId(), imgs.get(i), i);
            }
        }
    }

    @Override
    public void onRelationFailed(String failed) {
        showShortToast(failed);
    }
}
