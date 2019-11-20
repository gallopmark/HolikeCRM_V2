package com.holike.crm.fragment.employee.child;

import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.holike.crm.R;
import com.holike.crm.base.BaseFragment;
import com.holike.crm.base.IntentValue;
import com.holike.crm.bean.EmployeeRankingBean;
import com.holike.crm.helper.TextSpanHelper;

import java.util.List;

class InternalSigningHelper extends CommonInternalHelper {

    InternalSigningHelper(BaseFragment<?, ?> fragment, Callback callback) {
        super(fragment, callback);
        Object object = IntentValue.getInstance().removeBy("employeeRankBean");
        if (object instanceof EmployeeRankingBean) {
            EmployeeRankingBean bean = (EmployeeRankingBean) object;
            onSuccess(bean);
        } else {
            doRequest();
        }
    }

    @Override
    void requestUpdate(@Nullable EmployeeRankingBean.MyRankingBean bean) {
        String textCounts = "";
        if (bean != null) {
            textCounts += TextUtils.isEmpty(bean.myselfCounts) ? "" : bean.myselfCounts;
        }
        mDataTextView.setText(TextSpanHelper.from(mContext).obtainColorBoldStyle(R.string.tips_myself_number_of_orders, textCounts, R.color.textColor6));
    }

    @Override
    int requestRowId() {
        return R.layout.viewstub_employee_rank_signing;
    }

    @Override
    void setAdapter(RecyclerView recyclerView, List<EmployeeRankingBean.RankingDataBean> dataList) {
        recyclerView.setAdapter(new SigningAdapter(mContext, dataList));
    }
}
