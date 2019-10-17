package com.holike.crm.fragment.analyze;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.widget.TextView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.base.IntentValue;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.OriginalBoardBean;
import com.holike.crm.util.Constants;
import com.holike.crm.util.NumberUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import pony.xcode.chart.LineChartView;
import pony.xcode.chart.data.LineChartData;

/**
 * Created by wqj on 2018/6/25.
 * 原态板占比-经销商
 */
public class OriginalBoardRatioDealerFragment extends MyFragment {
    public static OriginalBoardRatioDealerFragment newInstance(OriginalBoardBean bean) {
        IntentValue.getInstance().put(Constants.ORIGINAL_BOARD_BEAN, bean);
        return new OriginalBoardRatioDealerFragment();
    }

    @BindView(R.id.lineChartView)
    LineChartView mLineChartView;
    @BindView(R.id.rv_original_board)
    RecyclerView mRecyclerView;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_original_board_dealer;
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
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        Object obj = IntentValue.getInstance().removeBy(Constants.ORIGINAL_BOARD_BEAN);
        if (obj instanceof OriginalBoardBean) {
            OriginalBoardBean bean = (OriginalBoardBean) obj;
            setLineChart(bean.getDealerData());
            setList(bean.getDealerData());
        }
    }

    private void setLineChart(List<OriginalBoardBean.DealerDataBean> list) {
        List<LineChartData> dataList = new ArrayList<>();
        for (int i = list.size() - 1; i >= 0; i--) {
            OriginalBoardBean.DealerDataBean bean = list.get(i);
            if (!TextUtils.equals(bean.getMonth(), "全年")) {
                double value = NumberUtil.doubleValue(bean.getCountsComplete().replace("%", ""), 1);
                String description = (TextUtils.isEmpty(bean.getMonth()) ? "" : bean.getMonth()) + "："
                        + (TextUtils.isEmpty(bean.getCountsComplete()) ? "-" : bean.getCountsComplete());
                dataList.add(new LineChartData(null, value, description));
            }
        }
        mLineChartView.withData(dataList, R.array.line_chart_months, "%").start();
    }

    /**
     * 显示数据列表
     */
    private void setList(List<OriginalBoardBean.DealerDataBean> list) {
        mRecyclerView.setAdapter(new CommonAdapter<OriginalBoardBean.DealerDataBean>(mContext, list) {
            @Override
            protected int bindView(int viewType) {
                return R.layout.item_rv_performance_personal;
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
