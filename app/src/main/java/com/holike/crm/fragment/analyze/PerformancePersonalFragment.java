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
import com.holike.crm.bean.PerformanceBean;
import com.holike.crm.bean.staticbean.JumpBean;
import com.holike.crm.customView.ScaleProgressView;
import com.holike.crm.util.Constants;

import java.util.List;

import butterknife.BindView;

/**
 * Created by wqj on 2018/5/16.
 * 业绩报表-经销商
 */

public class PerformancePersonalFragment extends MyFragment {

    @BindView(R.id.tv_performance_personal_year)
    TextView tvYear;
    @BindView(R.id.spv_performance_personal)
    ScaleProgressView spv;
    @BindView(R.id.tv_performance_personal_complete_percent)
    TextView tvCompletePercent;
    @BindView(R.id.tv_performance_personal_complete)
    TextView tvComplete;
    @BindView(R.id.tv_performance_personal_target)
    TextView tvTarget;
    @BindView(R.id.tv_performance_personal_growth)
    TextView tvGrowth;
    @BindView(R.id.rv_performance_personal)
    RecyclerView rv;

    @Override
    protected void init() {
        super.init();
        setStatusBar();
        setTitle(getString(R.string.report_item8_title));
        if (TextUtils.equals(JumpBean.getJumpBack(), getString(R.string.homepage))) {
            setLeft(getString(R.string.homepage));
        } else {
            setLeft(getString(R.string.report_title));
        }
        rv.setLayoutManager(new LinearLayoutManager(mContext));
        Bundle bundle = getArguments();
        if (bundle != null) {
            PerformanceBean bean = (PerformanceBean) bundle.getSerializable(Constants.PERFORMANCE_BEAN);
            String completePercent = mContext.getString(R.string.report_table_complete);
            if (bean != null && bean.getDealerData() != null) {
                if (TextUtils.equals(getComplete(bean.getDealerData().getPercentComplete()), "100")) {
                    completePercent += "";
                } else {
                    completePercent += bean.getDealerData().getPercentComplete();
                }
            }
            tvCompletePercent.setText(completePercent);
            tvYear.setText(bean != null && bean.getDealerData() != null && !TextUtils.isEmpty(bean.getDealerData().getDealerTime()) ? bean.getDealerData().getDealerTime() : "");
            tvComplete.setText(bean != null && bean.getDealerData() != null && !TextUtils.isEmpty(bean.getDealerData().getComplete()) ? bean.getDealerData().getComplete() : "");
            String target = mContext.getString(R.string.homepage_month_data_target);
            if (bean != null && bean.getDealerData() != null) {
                target += (TextUtils.isEmpty(bean.getDealerData().getPercentComplete()) ? "暂无" : bean.getDealerData().getTarget() + "万");
            }
            tvTarget.setText(target);
            if (bean != null) {
                tvGrowth.setText(TextUtils.isEmpty(bean.getDealerData().getPercent()) ? "0%" : bean.getDealerData().getPercent());
                spv.setProgress(bean.getDealerData().getPercentComplete());
                setList(bean.getDealerData().getDealerList());
            }
        }
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_performance_personal;
    }

    @Override
    protected BasePresenter attachPresenter() {
        return null;
    }

    /**
     * 显示数据列表
     */
    public void setList(List<PerformanceBean.DealerDataBean.DealerListBean> list) {
        rv.setAdapter(new CommonAdapter<PerformanceBean.DealerDataBean.DealerListBean>(mContext, list) {
            @Override
            protected int bindView(int viewType) {
                return R.layout.item_rv_performance_personal;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, PerformanceBean.DealerDataBean.DealerListBean dealerListBean, int position) {
                TextView tvTime = holder.obtainView(R.id.tv_item_rv_performance_personal_time);
                TextView tvCompleted = holder.obtainView(R.id.tv_item_rv_performance_personal_completed);
                tvTime.setText(dealerListBean.getMonth());
                tvCompleted.setText(dealerListBean.getAchievement());
            }
        });
    }

    private String getComplete(String percen) {
        if (TextUtils.isEmpty(percen)) return "";
        float f = Float.parseFloat(percen);
        if (f > 100) {
            return "100%";
        } else {
            return percen + "%";
        }
    }
}
