package com.holike.crm.fragment.analyze;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.NetDetailBean;
import com.holike.crm.util.Constants;

import butterknife.BindView;

/**
 * Created by wqj on 2018/6/6.
 * 拉网明细
 */

public class NetDetailFragment extends MyFragment {
    @BindView(R.id.tv_net_detail_date)
    TextView tvDate;
    @BindView(R.id.rv_net_detail)
    RecyclerView rv;

    @Override
    protected void init() {
        super.init();
        setTitle(getString(R.string.report_item14_title_detail));
        rv.setLayoutManager(new LinearLayoutManager(mContext));
        Bundle bundle = getArguments();
        if (bundle != null) {
            NetDetailBean netDetailBean = (NetDetailBean) bundle.getSerializable(Constants.NET_DETAIL);
            if (netDetailBean == null) return;
            tvDate.setText(netDetailBean.getDate());
            rv.setAdapter(new CommonAdapter<NetDetailBean.DataListBean>(mContext, netDetailBean.getDataList()) {
                @Override
                protected int bindView(int viewType) {
                    return R.layout.item_rv_net_detail_report;
                }

                @Override
                public void onBindHolder(RecyclerHolder holder, NetDetailBean.DataListBean dataListBean, int position) {
                    LinearLayout ll = holder.obtainView(R.id.ll_item_rv_net_detail_report);
                    TextView tvArea = holder.obtainView(R.id.tv_item_rv_net_detail_report_area);
                    TextView tvCenter = holder.obtainView(R.id.tv_item_rv_net_detail_report_center);
                    TextView tvName = holder.obtainView(R.id.tv_item_rv_net_detail_report_name);
                    TextView tvMarketLevel = holder.obtainView(R.id.tv_item_rv_net_detail_report_market_level);
                    TextView tvNeedCity = holder.obtainView(R.id.tv_item_rv_net_detail_report_need_city);
                    TextView tvProgress = holder.obtainView(R.id.tv_item_rv_net_detail_report_progress);
                    ll.setBackgroundResource(position % 2 == 0 ? R.color.bg_item_order_report : R.color.color_while);
                    tvArea.setText(dataListBean.getSales());
                    tvCenter.setText(dataListBean.getArea());
                    tvName.setText(dataListBean.getName());
                    tvNeedCity.setText(dataListBean.getDealerName());
                    tvMarketLevel.setText(dataListBean.getLevel());
                    tvProgress.setText(dataListBean.getStatus());
                }
            });
        }
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_net_detail;
    }

    @Override
    protected BasePresenter attachPresenter() {
        return null;
    }

}
