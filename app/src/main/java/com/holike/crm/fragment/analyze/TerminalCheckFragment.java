package com.holike.crm.fragment.analyze;

import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.TerminalCheckBean;
import com.holike.crm.presenter.fragment.TerminalCheckPresenter;
import com.holike.crm.util.Constants;
import com.holike.crm.view.fragment.TerminalCheckView;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by wqj on 2018/5/31.
 * 终端大检查
 */

public class TerminalCheckFragment extends MyFragment<TerminalCheckPresenter, TerminalCheckView> implements TerminalCheckView {

    @BindView(R.id.tv_terminal_check_date)
    TextView tvDate;
    @BindView(R.id.rv_terminal_check)
    RecyclerView rv;
    private String cityCode;
    private String type;
    private String title;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_terminal_check;
    }

    @Override
    protected TerminalCheckPresenter attachPresenter() {
        return new TerminalCheckPresenter();
    }

    @Override
    protected void init() {
        super.init();
        rv.setLayoutManager(new LinearLayoutManager(mContext));
        Bundle bundle = getArguments();
        if (bundle != null) {
            cityCode = bundle.getString(Constants.CITY_CODE);
            type = bundle.getString(Constants.TYPE);
            title = bundle.getString(Constants.TITLE);
        } else {
            setLeft(getString(R.string.report_title));
        }
        setTitle(getString(R.string.report_item12_title) + (TextUtils.isEmpty(title) ? "" : "—" + title));
        getData();
    }

    @Override
    public void getData() {
        showLoading();
        mPresenter.getData(cityCode, type);
    }

    /**
     * 获取数据成功
     *
     * @param terminalCheckBean
     */
    @Override
    public void getDataSuccess(TerminalCheckBean terminalCheckBean) {
        dismissLoading();
        tvDate.setText(terminalCheckBean.getTimeData());
        rv.setAdapter(new CommonAdapter<TerminalCheckBean.PercentDataBean>(mContext, terminalCheckBean.getPercentData()) {
            @Override
            protected int bindView(int viewType) {
                return R.layout.item_rv_terminal_check;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, TerminalCheckBean.PercentDataBean percentDataBean, int position) {
                LinearLayout ll = holder.obtainView(R.id.ll_item_rv_terminal_check);
                TextView tvArea = holder.obtainView(R.id.tv_item_rv_terminal_check_area);
                TextView tvName = holder.obtainView(R.id.tv_item_rv_terminal_check_name);
                TextView tvCheckShop = holder.obtainView(R.id.tv_item_rv_terminal_check_shop);
                TextView tvReorganized = holder.obtainView(R.id.tv_item_rv_terminal_check_reorganized);
                TextView tvUnreorganize = holder.obtainView(R.id.tv_item_rv_terminal_check_unreorganize);
                TextView tvCompletePercen = holder.obtainView(R.id.tv_item_rv_terminal_check_complete_percen);
                TextView tvRank = holder.obtainView(R.id.tv_item_rv_terminal_check_rank);
                ll.setBackgroundResource(position % 2 == 0 ? R.color.bg_item_order_report : R.color.color_while);
                tvArea.setTextColor(ContextCompat.getColor(mContext, percentDataBean.getIsClick() == 1 ? R.color.textColor5 : R.color.textColor8));
                tvArea.setText(percentDataBean.getArea());
                tvName.setText(percentDataBean.getName());
                tvCheckShop.setText(String.valueOf(percentDataBean.getCountsTarget()));
                tvReorganized.setText(percentDataBean.getCountsComplete());
                tvUnreorganize.setText(String.valueOf(percentDataBean.getCountsNotComplete()));
                tvCompletePercen.setText(percentDataBean.getPercent());
                tvRank.setText(percentDataBean.getRank());
                tvArea.setOnClickListener(v -> {
                    if (percentDataBean.getIsClick() == 1) {
                        Map<String, Serializable> params = new HashMap<>();
                        params.put(Constants.CITY_CODE, percentDataBean.getCityCode());
                        params.put(Constants.TYPE, String.valueOf(percentDataBean.getType()));
                        params.put(Constants.TITLE, percentDataBean.getArea());
                        startFragment(params, new TerminalCheckFragment());
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


}
