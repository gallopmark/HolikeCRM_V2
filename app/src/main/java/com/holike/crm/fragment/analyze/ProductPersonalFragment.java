package com.holike.crm.fragment.analyze;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.TextView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.OriginalBoardBean;
import com.holike.crm.bean.ProductTradingBean;
import com.holike.crm.customView.LineChartView;
import com.holike.crm.util.Constants;

import java.util.List;

import butterknife.BindView;

/**
 * Created by wqj on 2018/6/21.
 * 成品交易报表-经销商
 */

public class ProductPersonalFragment extends MyFragment {
    @BindView(R.id.tv_product_personal_complete)
    TextView tvComplete;
    @BindView(R.id.tv_product_personal_describe)
    TextView tvDescribe;
    @BindView(R.id.lcv_product_personal)
    LineChartView lineChartView;
    @BindView(R.id.rv_product_personal)
    RecyclerView rv;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_product_personal;
    }

    @Override
    protected BasePresenter attachPresenter() {
        return null;
    }

    @Override
    protected void init() {
        super.init();
        setStatusBar();
        setTitle(getString(R.string.report_item5_title));
        setLeft(getString(R.string.report_title));
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setNestedScrollingEnabled(false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            ProductTradingBean bean = (ProductTradingBean) bundle.getSerializable(Constants.PRODUCT_TRADING_BEAN);
            if (bean != null) {
                tvComplete.setText(bean.getTotal());
                tvDescribe.setText(bean.getActivity());
                lineChartView.setDatas(bean.getDealerList(), false);
                OriginalBoardBean.DealerDataBean dealerDataBean = new OriginalBoardBean.DealerDataBean();
                dealerDataBean.setMonth("全年");
                dealerDataBean.setCountsComplete(bean.getTotal());
                bean.getDealerList().add(0, dealerDataBean);
                setList(bean.getDealerList());
            }
        } else {
            setLeft(getString(R.string.report_title));
        }
    }

    /**
     * 显示列表
     *
     */
    public void setList(List<OriginalBoardBean.DealerDataBean> list) {
        rv.setAdapter(new CommonAdapter<OriginalBoardBean.DealerDataBean>(mContext, list) {
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
