package com.holike.crm.fragment.customer.workflow;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.bean.CollectionBean;
import com.holike.crm.fragment.customer.WorkflowFragment;
import com.holike.crm.http.MyJsonParser;
import com.holike.crm.util.ParseUtils;
import com.holike.crm.util.TimeUtil;
import com.holike.crm.view.fragment.WorkflowView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wqj on 2018/8/6.
 * 收尾款
 */
@Deprecated
public class CollectMoneyFragment extends WorkflowFragment implements WorkflowView {
    @BindView(R.id.rv_collect_money)
    RecyclerView rvCollectMoney;
    @BindView(R.id.et_collect_money_collect)
    EditText etCollectMoney;
    @BindView(R.id.tv_collect_money_uncollect)
    TextView tvUncollect;
    @BindView(R.id.et_collect_money_note)
    EditText etNote;
    @BindView(R.id.rv_collect_money_img)
    RecyclerView rv;

    private int uncollect;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_collect_money;
    }

    @Override
    protected void init() {
        super.init();
        setTitle(getString(R.string.house_manage_collect_money));
        rvCollectMoney.setNestedScrollingEnabled(false);
        rvCollectMoney.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setNestedScrollingEnabled(false);
        rv.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        initImgs(rv, getString(R.string.house_manage_add_collect_money_img));
        mPresenter.getCollection(houseInfoBean.getHouseId(), personalId);
        dismissLoading();
        setTextchange(etCollectMoney);
    }

    /**
     * 计算未收尾款
     *
     * @param textView
     */
    private void setTextchange(TextView textView) {
        textView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvUncollect.setText("");
                String collectMoneyText = getText(etCollectMoney);
                int collectMoney = TextUtils.isEmpty(collectMoneyText) ? 0 : ParseUtils.parseInt(collectMoneyText);
                if (uncollect >= collectMoney) {
                    tvUncollect.setText("￥" + (uncollect - collectMoney));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /**
     * 获取已收款列表/保存成功
     *
     * @param success
     */
    @Override
    public void success(Object success) {
        dismissLoading();
        if (success instanceof CollectionBean) {
            uncollect = ((CollectionBean) success).getBalance();
            tvUncollect.setText("￥" + uncollect);
            showCollectList((CollectionBean) success);
        } else if (success instanceof String) {
            showShortToast(MyJsonParser.getShowMessage((String) success));
            operateSuccess();
        }
    }

    /**
     * 获取已收款列表/保存失败
     */
    @Override
    public void failed(String failed) {
        dismissLoading();
        showShortToast(failed);
    }


    @OnClick(R.id.tv_collect_money_save)
    public void onViewClicked() {
        if (isTextEmpty(etCollectMoney) || imgs.size() == 0) {
            showShortToast(R.string.tips_complete_information);
        } else if (isTextEmpty(tvUncollect)) {
            showShortToast("请输入正确金额");
        } else {
            mPresenter.collectMoney(mContext, getText(etNote), customerStatus, houseId, getText(tvUncollect), operateCode, personalId, prepositionRuleStatus, getText(etCollectMoney), imgs);
            showLoading();
        }
    }

    /**
     * 显示已收款列表
     */
    private void showCollectList(CollectionBean collectionBean) {
        if (collectionBean != null) {
            CollectionBean.PaymentBean deposit = collectionBean.getDepoist();
            List<String> list = new ArrayList<>();
            list.add("订单金额：￥" + collectionBean.getMoney());
            if (deposit != null && !TextUtils.isEmpty(deposit.getDepositAmount()) && !TextUtils.isEmpty(deposit.getOperateTime())) {
                list.add(TimeUtil.stampToString(deposit.getOperateTime(), "yyyy年MM月dd日") + "收订金：￥" + deposit.getDepositAmount());
            }
            for (CollectionBean.PaymentBean paymentBean : collectionBean.getPayment()) {
                list.add(TimeUtil.stampToString(paymentBean.getOperateTime(), "yyyy年MM月dd日") + "收款：￥" + paymentBean.getDepositAmount());
            }
            rvCollectMoney.setAdapter(new CommonAdapter<String>(mContext, list) {
                @Override
                protected int bindView(int viewType) {
                    return R.layout.item_rv_collect_money;
                }

                @Override
                public void onBindHolder(RecyclerHolder holder, String s, int position) {
                    TextView tv = holder.obtainView(R.id.tv_item_rv_collect_money);
                    tv.setText(s);
                }
            });
        }
    }
}
