package com.holike.crm.fragment.bank;

import android.os.Bundle;
import android.widget.TextView;

import com.holike.crm.R;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.BillListBean;
import com.holike.crm.util.Constants;

import butterknife.BindView;

public class BillDetialFragment extends MyFragment {
    @BindView(R.id.tv_bill_detail_date)
    TextView tvBillDetailDate;
    @BindView(R.id.tv_bill_detail_opt)
    TextView tvBillDetailOpt;
    @BindView(R.id.tv_bill_detail_order)
    TextView tvBillDetailOrder;
    @BindView(R.id.tv_bill_list_detail_sqrdd1)
    TextView tvBillListDetailSqrdd1;
    @BindView(R.id.tv_bill_list_detail_dqrdd_2)
    TextView tvBillListDetailDqrdd2;
    @BindView(R.id.tv_bill_list_detail_sxed_3)
    TextView tvBillListDetailSxed3;
    @BindView(R.id.tv_bill_list_detail_sxye4)
    TextView tvBillListDetailSxye4;
    @BindView(R.id.tv_bill_list_detail_qcye5)
    TextView tvBillListDetailQcye5;
    @BindView(R.id.tv_bill_list_detail_crmhk6)
    TextView tvBillListDetailCrmhk6;
    @BindView(R.id.tv_bill_list_detail_crmflje7)
    TextView tvBillListDetailCrmflje7;
    @BindView(R.id.tv_bill_list_detail_ccjzq8)
    TextView tvBillListDetailCcjzq8;
    @BindView(R.id.tv_bill_list_detail_hdzkje9)
    TextView tvBillListDetailHdzkje9;
    @BindView(R.id.tv_bill_list_detail_ccjzh10)
    TextView tvBillListDetailCcjzh10;
    @BindView(R.id.tv_bill_list_detail_kf11)
    TextView tvBillListDetailKf11;
    @BindView(R.id.tv_bill_list_detail_kyje12)
    TextView tvBillListDetailKyje12;
    @BindView(R.id.tv_bill_list_detail_kscddje13)
    TextView tvBillListDetailKscddje13;
    @BindView(R.id.tv_bill_list_detail_cwflqcye14)
    TextView tvBillListDetailCwflqcye14;
    @BindView(R.id.tv_bill_list_detail_cwfldqzj15)
    TextView tvBillListDetailCwfldqzj15;
    @BindView(R.id.tv_bill_list_detail_cwfldqjs16)
    TextView tvBillListDetailCwfldqjs16;
    private Bundle bundle;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_bill_detail;
    }

    @Override
    protected BasePresenter attachPresenter() {
        return null;
    }

    @Override
    protected void init() {
        super.init();
        bundle = getArguments();
        setTitle(getString(R.string.bill_list_bill_detail));
        if (bundle != null) {
            BillListBean.PageDataBean bean = (BillListBean.PageDataBean) bundle.getSerializable(Constants.BILL_DETAIL);
            if (bean != null) {
                tvBillDetailDate.setText("日期："+textEmpty(bean.getZdate()));
                tvBillDetailOpt.setText("操作："+textEmpty(bean.getCztxt()));
                tvBillDetailOrder.setText("订单号："+textEmpty(bean.getBstkd()));
                tvBillListDetailSqrdd1.setText(textEmptyNumber(bean.getDqrso()));
                tvBillListDetailDqrdd2.setText(textEmptyNumber(bean.getZqcye()));
                tvBillListDetailSxed3.setText(textEmptyNumber(bean.getZhuok()));
                tvBillListDetailSxye4.setText(textEmptyNumber(bean.getZflje()));
                tvBillListDetailQcye5.setText(textEmptyNumber(bean.getCcjzh()));
                tvBillListDetailCrmhk6.setText(textEmptyNumber(bean.getHdzkje()));
                tvBillListDetailCrmflje7.setText(textEmptyNumber(bean.getCcjzq()));
                tvBillListDetailCcjzq8.setText(textEmptyNumber(bean.getKoufei()));
                tvBillListDetailHdzkje9.setText(textEmptyNumber(bean.getZkyje()));
                tvBillListDetailCcjzh10.setText(textEmptyNumber(bean.getZflqc()));
                tvBillListDetailKf11.setText(textEmptyNumber(bean.getZfladd()));
                tvBillListDetailKyje12.setText(textEmptyNumber(bean.getZfldow()));
                tvBillListDetailKscddje13.setText(textEmptyNumber(bean.getZflqm()));
                tvBillListDetailCwflqcye14.setText(textEmptyNumber(bean.getZysqc()));
                tvBillListDetailCwfldqzj15.setText(textEmptyNumber(bean.getZysfs()));
                tvBillListDetailCwfldqjs16.setText(textEmptyNumber(bean.getZysqm()));
            }

        }
    }
}
