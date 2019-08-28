package com.holike.crm.fragment.customer.workflow;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.holike.crm.R;
import com.holike.crm.bean.CollectionBean;
import com.holike.crm.bean.TypeIdBean;
import com.holike.crm.fragment.customer.WorkflowFragment;
import com.holike.crm.http.MyJsonParser;
import com.holike.crm.util.NumberUtil;
import com.holike.crm.view.fragment.WorkflowView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wqj on 2018/8/6.
 * 已签约
 */

public class SignedFragment extends WorkflowFragment implements WorkflowView {
    @BindView(R.id.et_signed_order_money)
    EditText etOrderMoney;
    @BindView(R.id.tv_signed_deposit_money)
    TextView tvDepositMoney;
    @BindView(R.id.et_signed_collected_money)
    EditText etCollectedMoney;
    @BindView(R.id.tv_signed_uncollect_money)
    TextView tvUncollectMoney;
    @BindView(R.id.tv_signed_install_time)
    TextView tvInstallTime;
    @BindView(R.id.tv_signed_need_remeasure)
    TextView tvNeedRemeasure;
    @BindView(R.id.et_signed_note)
    EditText etNote;
    @BindView(R.id.rv_signed_img)
    RecyclerView rv;
    private double unCollectMoney;
    private TypeIdBean.TypeIdItem remeasure;
    private List<TypeIdBean.TypeIdItem> remeasures;
    private String deposit;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_signed;
    }


    @Override
    protected void init() {
        super.init();
        setTitle(getString(R.string.house_manage_signing));
        rv.setNestedScrollingEnabled(false);
        rv.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        initImgs(rv, getString(R.string.house_manage_add_contract_img));
        mPresenter.getCollection(houseId, personalId);
        showLoading();
        remeasures = new ArrayList<>();
        remeasures.add(new TypeIdBean.TypeIdItem("1", "需要"));
        remeasures.add(new TypeIdBean.TypeIdItem("0", "不需要"));
        setTextChange(etOrderMoney);
        setTextChange(etCollectedMoney);
    }

    /**
     * 计算未收尾款
     */
    private void setTextChange(TextView textView) {
        textView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (textView.getId() == R.id.et_signed_order_money && TextUtils.isEmpty(s.toString().trim())) {
                    tvUncollectMoney.setText("");
                } else {
                    String orderMoneyS = getText(etOrderMoney);
                    String collectMoneyS = getText(etCollectedMoney);
                    double orderMoney = parseDouble(orderMoneyS);
                    double collectMoney = parseDouble(collectMoneyS);
                    double depositMoney = parseDouble(deposit);
                    unCollectMoney = orderMoney - depositMoney - collectMoney;
                    String text = "￥" + getFormatText(String.valueOf(unCollectMoney));
                    tvUncollectMoney.setText(text);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private double parseDouble(String source) {
        if (TextUtils.isEmpty(source)) return 0;
        try {
            return Double.parseDouble(source);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 获取收款记录/保存成功
     */
    @Override
    public void success(Object success) {
        dismissLoading();
        if (success instanceof CollectionBean) {
            deposit = ((CollectionBean) success).getDepoist().getDepositAmount();
            String text = mContext.getString(R.string.house_manage_deposit_money) + getFormatText(deposit);
            tvDepositMoney.setText(text);
        } else if (success instanceof String) {
            showShortToast(MyJsonParser.getShowMessage((String) success));
            operateSuccess();
        }
    }

    private String getFormatText(String content) {
        try {
            return TextUtils.isEmpty(content) ? "-" : NumberUtil.format(content);
        } catch (Exception e) {
            return content;
        }
    }

    /**
     * 获取收款记录/保存失败
     */
    @Override
    public void failed(String failed) {
        dismissLoading();
        showShortToast(failed);
    }

    @OnClick({R.id.tv_signed_install_time, R.id.tv_signed_need_remeasure, R.id.tv_signed_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_signed_install_time:
                showTimePickerView(getActivity(), getText(tvInstallTime), etNote);
                break;
            case R.id.tv_signed_need_remeasure:
                showPickerView(remeasures, getText(tvNeedRemeasure), etNote);
                break;
            case R.id.tv_signed_save:
                if (isTextEmpty(etOrderMoney) || isTextEmpty(etCollectedMoney) || isTextEmpty(tvInstallTime) || remeasure == null) {
                    showShortToast(R.string.tips_complete_information);
                } else if (isTextEmpty(tvUncollectMoney)) {
                    showShortToast("请输入正确金额");
                } else if (unCollectMoney < 0) {
                    showShortToast("总收款大于订单金额");
                } else {
                    showLoading();
                    mPresenter.signed(mContext, getText(etNote), customerStatus, houseId, getText(etCollectedMoney), getText(tvInstallTime), operateCode, remeasure.getId(), personalId, prepositionRuleStatus, getText(etOrderMoney), deposit, String.valueOf(unCollectMoney), imgs);
                }
                break;
        }
    }

    /**
     * 选择是否需要复尺
     */
    @Override
    protected void optionsSelect(int options) {
        remeasure = remeasures.get(options);
        tvNeedRemeasure.setText(remeasure.getName());
    }

    /**
     * 选择日期
     */
    @Override
    protected void selectTime(Date date) {
        tvInstallTime.setText(new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).format(date));
    }
}
