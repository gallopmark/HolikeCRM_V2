package com.holike.crm.fragment.analyze;

import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.gallopmark.recycler.widgetwrapper.WrapperRecyclerView;
import com.holike.crm.R;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.base.IntentValue;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.CupboardBean;
import com.holike.crm.bean.OriginalBoardBean;
import com.holike.crm.util.Constants;
import com.holike.crm.util.NumberUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import pony.xcode.chart.LineChartView;
import pony.xcode.chart.data.LineChartData;

/**
 * Created by wqj on 2018/6/27.
 * 橱柜业绩报表-经销商
 * （继承成品交易报表-经销商）
 */

public class CupboardPerformanceDealerFragment extends MyFragment {

    public static CupboardPerformanceDealerFragment newInstance(CupboardBean bean) {
        IntentValue.getInstance().put(Constants.CUPBOARD_BEAN, bean);
        return new CupboardPerformanceDealerFragment();
    }

    @BindView(R.id.wrapperRecyclerView)
    WrapperRecyclerView mWrapperRecyclerView;

    @Override
    protected BasePresenter attachPresenter() {
        return null;
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_cupboard_performance_dealer;
    }

    @Override
    protected void init() {
        setStatusBar();
        setTitle(getString(R.string.report_item9_title));
        CupboardBean bean = (CupboardBean) IntentValue.getInstance().removeBy(Constants.CUPBOARD_BEAN);
        mWrapperRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        if (bean != null && TextUtils.equals(bean.getIsOrder(), "1")) {
            setHeader(bean);
            setList(bean.getDealerList());
        } else {
            noData(R.drawable.no_result, R.string.tips_noorderdata, false);
        }
    }

    private void setHeader(CupboardBean bean) {
        View headerView = LayoutInflater.from(mContext).inflate(R.layout.header_common_dealer_report, new LinearLayout(mContext), false);
        TextView tvComplete = headerView.findViewById(R.id.tv_complete);
        tvComplete.setText(obtainTotal(bean.getTotal()));
        TextView tvDescription = headerView.findViewById(R.id.tv_description);
        tvDescription.setText(bean.getDescribe());
        tvDescription.setVisibility(View.VISIBLE);
        LineChartView lineChartView = headerView.findViewById(R.id.lineChartView);
        List<LineChartData> dataList = new ArrayList<>();
        for (OriginalBoardBean.DealerDataBean dataBean : bean.getDealerList()) {
            if (!TextUtils.equals(dataBean.getMonth(), "全年")) {
                dataList.add(new LineChartData(NumberUtil.doubleValue(dataBean.getCountsComplete())));
            }
        }
        Collections.reverse(dataList);
        lineChartView.withData(dataList, R.array.line_chart_months).start();
        mWrapperRecyclerView.addHeaderView(headerView);
    }

    private SpannableString obtainTotal(String totalComplete) {
        String origin = TextUtils.isEmpty(totalComplete) ? "" : totalComplete;
        int start = origin.length();
        String source = origin + " 万";
        SpannableString ss = new SpannableString(source);
        ss.setSpan(new AbsoluteSizeSpan(mContext.getResources().getDimensionPixelSize(R.dimen.textSize_14), false), start, source.length(),
                SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    /**
     * 显示列表
     */
    public void setList(List<OriginalBoardBean.DealerDataBean> list) {
        mWrapperRecyclerView.setAdapter(new CommonAdapter<OriginalBoardBean.DealerDataBean>(mContext, list) {
            @Override
            protected int bindView(int viewType) {
                return R.layout.item_rv_performance_personal;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, OriginalBoardBean.DealerDataBean dealerDataBean, int position) {
                holder.setText(R.id.tv_item_rv_performance_personal_time, dealerDataBean.getMonth());
                holder.setText(R.id.tv_item_rv_performance_personal_completed, dealerDataBean.getCountsComplete());
            }
        });
    }
}
