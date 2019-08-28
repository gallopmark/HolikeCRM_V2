package com.holike.crm.fragment.analyze;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.NetBean;
import com.holike.crm.bean.NetDetailBean;
import com.holike.crm.presenter.fragment.NetPresenter;
import com.holike.crm.util.Constants;
import com.holike.crm.view.fragment.NetView;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by wqj on 2018/6/5.
 * 拉网
 */

public class NetFragment extends MyFragment<NetPresenter, NetView> implements NetView {
    @BindView(R.id.tv_net_date)
    TextView tvDate;
    @BindView(R.id.rv_net)
    RecyclerView rv;

    protected String cityCode;
    protected String type;

    @Override
    protected void init() {
        super.init();
        setTitle(getString(R.string.report_item14_title));
        setLeft(getString(R.string.report_title));
        rv.setLayoutManager(new LinearLayoutManager(mContext));
        getData();
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_net;
    }

    @Override
    protected NetPresenter attachPresenter() {
        return new NetPresenter();
    }

    @Override
    public void getData() {
        showLoading();
        mPresenter.getData(cityCode, type);
    }

    /**
     * 获取数据成功
     *
     * @param netBean
     */
    @Override
    public void getDataSuccess(NetBean netBean) {
        dismissLoading();
        tvDate.setText(netBean.getTimeData());
        rv.setAdapter(new CommonAdapter<NetBean.PercentDataBean>(mContext, netBean.getPercentData()) {
            @Override
            protected int bindView(int viewType) {
                return R.layout.item_rv_net_report;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, NetBean.PercentDataBean percentDataBean, int position) {
                LinearLayout ll = holder.obtainView(R.id.ll_item_rv_net_report);
                TextView tvArea = holder.obtainView(R.id.tv_item_rv_net_report_area);
                TextView tvName = holder.obtainView(R.id.tv_item_rv_net_report_name);
                TextView tvTarget = holder.obtainView(R.id.tv_item_rv_net_report_target);
                TextView tvComplete = holder.obtainView(R.id.tv_item_rv_net_report_complete);
                TextView tvCompletePercen = holder.obtainView(R.id.tv_item_rv_net_report_complete_percen);
                TextView tvRank = holder.obtainView(R.id.tv_item_rv_net_report_rank);
                TextView tvDetail = holder.obtainView(R.id.tv_item_rv_net_report_detail);
                ll.setBackgroundResource(position % 2 == 0 ? R.color.bg_item_order_report : R.color.color_while);
                tvArea.setText(percentDataBean.getArea());
                tvName.setText(percentDataBean.getName());
                tvTarget.setText(percentDataBean.getCountsTarget());
                tvCompletePercen.setText(percentDataBean.getPercent());
                tvComplete.setText(String.valueOf(percentDataBean.getCountsComplete()));
                tvRank.setText(percentDataBean.getRank());
                tvDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showLoading();
                        mPresenter.getNetDetail(percentDataBean.getCityCode(), String.valueOf(percentDataBean.getType()));
                    }
                });
            }
        });
    }

    /**
     * 获取数据失败
     */
    @Override
    public void getDataFailed(String failed) {
        dismissLoading();
        showShortToast(failed);
    }

    /**
     * 获取拉网明细数据成功
     */
    @Override
    public void getNetDetailSuccess(NetDetailBean bean) {
        dismissLoading();
        Map<String, Serializable> params = new HashMap<>();
        params.put(Constants.NET_DETAIL, bean);
        startFragment(params, new NetDetailFragment());
    }

    /**
     * 获取拉网明细数据失败
     */
    @Override
    public void getNetDetailFailed(String failed) {
        dismissLoading();
        showShortToast(failed);
    }

}
