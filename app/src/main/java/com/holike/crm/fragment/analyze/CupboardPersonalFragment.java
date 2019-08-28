package com.holike.crm.fragment.analyze;

import android.os.Bundle;

import com.holike.crm.R;
import com.holike.crm.bean.CupboardBean;
import com.holike.crm.util.Constants;

/**
 * Created by wqj on 2018/6/27.
 * 橱柜业绩报表-经销商
 * （继承成品交易报表-经销商）
 */

public class CupboardPersonalFragment extends ProductPersonalFragment {

    @Override
    protected void init() {
        super.init();
        setTitle(getString(R.string.report_item9_title));
        setLeft(getString(R.string.report_title));
        Bundle bundle = getArguments();
        if (bundle != null) {
            CupboardBean bean = (CupboardBean) bundle.getSerializable(Constants.CUPBOARD_BEAN);
            if (bean != null && bean.getIsOrder().equals("1")) {
                tvComplete.setText(bean.getTotal());
                tvDescribe.setText(bean.getDescribe());
                lineChartView.setDatas(bean.getDealerList(), false);
                setList(bean.getDealerList());
            } else {
                noData(R.drawable.no_result, R.string.tips_noorderdata, false);
            }
        } else {
            setLeft(getString(R.string.report_title));
        }
    }
}
