package com.holike.crm.fragment.analyze;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.TextView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.OriginalBoardBean;
import com.holike.crm.customView.LineChartView;
import com.holike.crm.util.Constants;

import java.util.List;

import butterknife.BindView;

/**
 * Created by wqj on 2018/6/25.
 * 原态板占比-经销商
 */

public class OriginalBoardPersonalFragment extends MyFragment {
    @BindView(R.id.lcv_originalboard_personal)
    LineChartView lcv;
    @BindView(R.id.rv_originalboard_personal)
    RecyclerView rv;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_original_board_personal;
    }

    @Override
    protected BasePresenter attachPresenter() {
        return null;
    }

    @Override
    protected void init() {
        super.init();
        setStatusBar();
        setTitle(getString(R.string.report_item10_title));
        setLeft(getString(R.string.report_title));
        rv.setLayoutManager(new LinearLayoutManager(mContext));
        Bundle bundle = getArguments();
        if (bundle != null) {
            OriginalBoardBean bean = (OriginalBoardBean) bundle.getSerializable(Constants.ORIGINAL_BOARD_BEAN);
            if (bean != null) {
                setList(bean.getDealerData());
                lcv.setDatas(bean.getDealerData(), true);
            }
        }
    }

    /**
     * 显示数据列表
     *
     * @param list
     */
    public void setList(List<OriginalBoardBean.DealerDataBean> list) {
        rv.setAdapter(new CommonAdapter<OriginalBoardBean.DealerDataBean>(mContext, list) {
            @Override
            protected int bindView(int viewType) {
                return  R.layout.item_rv_performance_personal;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, OriginalBoardBean.DealerDataBean dealerDataBean, int position) {
                TextView tvTime = holder.obtainView(R.id.tv_item_rv_performance_personal_time);
                TextView tvCompleted = holder.obtainView(R.id.tv_item_rv_performance_personal_completed);
                tvTime.setText(dealerDataBean.getMonth());
                tvCompleted.setText(TextUtils.isEmpty(dealerDataBean.getCountsComplete()) ? "-" : dealerDataBean.getCountsComplete());
            }
        });
    }

}
