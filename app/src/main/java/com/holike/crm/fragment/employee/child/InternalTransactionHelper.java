package com.holike.crm.fragment.employee.child;

import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.holike.crm.R;
import com.holike.crm.base.BaseFragment;
import com.holike.crm.bean.EmployeeRankingBean;
import com.holike.crm.helper.TextSpanHelper;

import java.util.List;

class InternalTransactionHelper extends CommonInternalHelper{

    InternalTransactionHelper(BaseFragment<?, ?> fragment, Callback callback) {
        super(fragment, callback);
        doRequest();
    }

    @Override
    void requestUpdate(@Nullable EmployeeRankingBean.MyRankingBean bean) {
        String textSum = "";
        if (bean != null) {
            textSum += TextUtils.isEmpty(bean.myselfSum) ? "" : bean.myselfSum;
        }
        mDataTextView.setText(TextSpanHelper.from(mContext).obtainColorBoldStyle(R.string.tips_myself_transaction_amount, textSum, R.color.textColor6));
    }

    @Override
    int requestRowId() {
        return R.layout.viewstub_employee_rank_transaction;
    }

    @Override
    void setAdapter(RecyclerView recyclerView, List<EmployeeRankingBean.RankingDataBean> dataList) {
        recyclerView.setAdapter(new TransactionAdapter(mContext, dataList));
    }
}
