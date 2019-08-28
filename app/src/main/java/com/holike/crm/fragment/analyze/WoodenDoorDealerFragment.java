package com.holike.crm.fragment.analyze;

import android.os.Bundle;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.OriginalBoardBean;
import com.holike.crm.bean.WoodenDoorBean;
import com.holike.crm.customView.LineChartView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by gallop on 2019/8/28.
 * Copyright holike possess 2019.
 * 木门业绩报表-经销商
 */
public class WoodenDoorDealerFragment extends MyFragment {

    @BindView(R.id.tv_complete)
    TextView tvComplete;
    @BindView(R.id.tv_describe)
    TextView tvDescribe;
    @BindView(R.id.chart_view)
    LineChartView lineChartView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_woodendoor_dealer;
    }

    @Override
    protected BasePresenter attachPresenter() {
        return null;
    }

    @Override
    protected void init() {
        super.init();
        setStatusBar();
        setTitle(getString(R.string.report_item18_title));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setNestedScrollingEnabled(false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            WoodenDoorBean bean = (WoodenDoorBean) bundle.getSerializable("bean");
            if (bean != null && bean.getDealerData() != null) {
                WoodenDoorBean.DealerDataBean dataBean = bean.getDealerData();
                tvComplete.setText(dataBean.totalComplete);
                tvDescribe.setText(dataBean.dealerTime);
                List<OriginalBoardBean.DealerDataBean> list = new ArrayList<>();
                for (WoodenDoorBean.DealerListBean listBean : dataBean.getDealerList()) {
                    list.add(new OriginalBoardBean.DealerDataBean(listBean.month, listBean.achievement));
                }
                lineChartView.setDatas(list, false);
                WoodenDoorBean.DealerListBean dealerDataBean = new WoodenDoorBean.DealerListBean("全年", dataBean.totalComplete);
                dataBean.getDealerList().add(0, dealerDataBean);
                setList(dataBean.getDealerList());
            }
        } else {
            setLeft(getString(R.string.report_title));
        }
    }

    /**
     * 显示列表
     */
    public void setList(List<WoodenDoorBean.DealerListBean> list) {
        recyclerView.setAdapter(new CommonAdapter<WoodenDoorBean.DealerListBean>(mContext, list) {
            @Override
            protected int bindView(int viewType) {
                return R.layout.item_rv_performance_personal;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, WoodenDoorBean.DealerListBean dealerDataBean, int position) {
                holder.setText(R.id.tv_item_rv_performance_personal_time, dealerDataBean.month);
                holder.setText(R.id.tv_item_rv_performance_personal_completed, dealerDataBean.achievement);
            }
        });
    }
}
