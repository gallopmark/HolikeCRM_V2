package com.holike.crm.fragment.analyze;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.ActiveMarketRankBean;
import com.holike.crm.dialog.CalendarPickerDialog;
import com.holike.crm.presenter.fragment.ActiveMarketRankPresenter;
import com.holike.crm.presenter.fragment.OrderReportPresenter;
import com.holike.crm.util.Constants;
import com.holike.crm.util.ParseUtils;
import com.holike.crm.util.TimeUtil;
import com.holike.crm.view.fragment.ActiveMarketRankView;

import java.util.Date;
import java.util.List;

import butterknife.BindView;

/**
 * Created by wqj on 2018/6/21.
 * 主动营销排行榜
 */

public class ActiveMarketRankFragment extends MyFragment<ActiveMarketRankPresenter, ActiveMarketRankView> implements ActiveMarketRankView {
    @BindView(R.id.ll_active_market_rank)
    LinearLayout llMain;
    @BindView(R.id.tv_active_market_rank_data)
    TextView tvData;
    @BindView(R.id.rv_active_market_rank)
    RecyclerView rv;

    private String startDate;
    private String endDate;

    @Override
    protected void init() {
        super.init();
        setStatusBar(R.color.bg_state_bar);
        setRightMenu(getString(R.string.report_select_date));
        setTitle(getString(R.string.report_item17_title));
        rv.setLayoutManager(new LinearLayoutManager(mContext));
        Bundle bundle = getArguments();
        if (bundle != null) {
            ActiveMarketRankBean bean = (ActiveMarketRankBean) bundle.get(Constants.ACTIVE_MARKET_RANK_BEAN);
            getDataSuccess(bean);
        }
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_active_market_rank;
    }

    @Override
    protected ActiveMarketRankPresenter attachPresenter() {
        return new ActiveMarketRankPresenter();
    }

    /**
     * 查询日期
     */
    private List<Date> mSelectedDates;

    @Override
    protected void clickRightMenu(String text, View actionView) {
        OrderReportPresenter.selectDate(mContext, mSelectedDates, new CalendarPickerDialog.OnCalendarRangeSelectedListener() {
            @Override
            public void onLeftClicked(CalendarPickerDialog dialog) {
//                dialog.dismiss();
//                startDate = null;
//                endDate = null;
//                getData();
            }

            @Override
            public void onRightClick(CalendarPickerDialog dialog, List<Date> selectedDates, Date start, Date end) {
                dialog.dismiss();
                mSelectedDates = selectedDates;
                if (selectedDates.size() >= 1) {
                    startDate = TimeUtil.dateToStamp(start, false);
                    endDate = TimeUtil.dateToStamp(end, true);
//                    startDate = TimeUtil.dataToStamp(start, "yyyy年MM月dd日");
//                    endDate = TimeUtil.dataToStamp(end, "yyyy年MM月dd日");
                } else {
                    startDate = null;
                    endDate = null;
                }
                getData();
            }
        });
    }

    /**
     * 获取数据
     */
    @Override
    public void getData() {
        showLoading();
        mPresenter.getData(startDate, endDate);
    }

    /**
     * 获取数据成功
     *
     * @param bean
     */
    @Override
    public void getDataSuccess(ActiveMarketRankBean bean) {
        dismissLoading();
        setTitle(getString(R.string.report_item17_title) + (TextUtils.isEmpty(bean.getSelectName()) ? "" : "—" + bean.getSelectName()));
        tvData.setText(bean.getTimeData());
        rv.setAdapter(new CommonAdapter<ActiveMarketRankBean.DataListBean>(mContext, bean.getDataList()) {
            @Override
            protected int bindView(int viewType) {
                return R.layout.item_rv_active_market_rank;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, ActiveMarketRankBean.DataListBean dataListBean, int position) {
                LinearLayout ll = holder.obtainView(R.id.ll_item_rv_active_market_rank);
                TextView tvRank = holder.obtainView(R.id.tv_item_rv_active_market_rank);
                ImageView ivRank = holder.obtainView(R.id.iv_item_rv_active_market_rank);
                TextView tvDivide = holder.obtainView(R.id.tv_item_rv_active_market_rank_divide);
                TextView tvCenter = holder.obtainView(R.id.tv_item_rv_active_market_rank_center);
                TextView tvActiveMarket = holder.obtainView(R.id.tv_item_rv_active_market_rank_active_market);
                TextView tvAchievements = holder.obtainView(R.id.tv_item_rv_active_market_rank_achievements);
                ll.setBackgroundResource(position % 2 == 0 ? R.color.bg_item_order_report : R.color.color_while);
                ivRank.setVisibility(View.GONE);
                tvRank.setVisibility(View.GONE);
                if (!dataListBean.getRank().equals("-")) {
                    switch (ParseUtils.parseInt(dataListBean.getRank())) {
                        case 1:
                            ivRank.setVisibility(View.VISIBLE);
                            ivRank.setBackgroundResource(R.drawable.ranking_first);
                            break;
                        case 2:
                            ivRank.setVisibility(View.VISIBLE);
                            ivRank.setBackgroundResource(R.drawable.ranking_second);
                            break;
                        case 3:
                            ivRank.setVisibility(View.VISIBLE);
                            ivRank.setBackgroundResource(R.drawable.ranking_third);
                            break;
                        default:
                            tvRank.setVisibility(View.VISIBLE);
                            tvRank.setText(dataListBean.getRank());
                            break;
                    }
                } else {
                    tvRank.setVisibility(View.VISIBLE);
                    tvRank.setText(dataListBean.getRank());
                }

                tvDivide.setText(dataListBean.getSales());
                tvCenter.setText(dataListBean.getArea());
                tvActiveMarket.setText(dataListBean.getName());
                tvAchievements.setText(dataListBean.getAchievement());
            }
        });
    }

    /**
     * 获取数据失败
     */
    @Override
    public void getDataFailed(String failed) {
        showShortToast(failed);
    }
}
