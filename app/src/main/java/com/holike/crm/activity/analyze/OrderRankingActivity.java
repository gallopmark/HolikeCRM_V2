package com.holike.crm.activity.analyze;

import android.app.Dialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.base.MyFragmentActivity;
import com.holike.crm.bean.OrderRankingBean;
import com.holike.crm.dialog.CalendarPickerDialog;
import com.holike.crm.dialog.LoadingTipDialog;
import com.holike.crm.dialog.SelectAreaDialog;
import com.holike.crm.presenter.activity.OrderRankingPresenter;
import com.holike.crm.presenter.fragment.OrderReportPresenter;
import com.holike.crm.util.LogCat;
import com.holike.crm.util.TimeUtil;
import com.holike.crm.view.activity.OrderRankingView;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 签单排行榜
 */
public class OrderRankingActivity extends MyFragmentActivity<OrderRankingPresenter, OrderRankingView> implements OrderRankingView {

    @BindView(R.id.ll_order_ranking_main)
    LinearLayout llMain;
    @BindView(R.id.tv_order_ranking_area)
    TextView tvArea;
    @BindView(R.id.tv_order_ranking_time)
    TextView tvTime;
    @BindView(R.id.tv_order_ranking_my_order)
    TextView tvMyOrder;
    @BindView(R.id.tv_order_ranking_my_ranking)
    TextView tvMyRanking;
    @BindView(R.id.tv_order_ranking_no_ranking)
    TextView tvNoRanking;
    @BindView(R.id.rv_order_ranking)
    RecyclerView rv;

    private String cityCode;
    private String type;
    private String startTime;
    private String endTime;
    private OrderRankingBean orderRankingBean;

    @Override
    protected OrderRankingPresenter attachPresenter() {
        return new OrderRankingPresenter();
    }

    @Override
    protected int setContentViewId() {
        return R.layout.activity_order_ranking;
    }

    @Override
    protected void init() {
        super.init();
        setTitle(getString(R.string.report_item4_title));
        setLeft(getString(R.string.report_title));
        setRightMenu(getString(R.string.report_select_date));
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setNestedScrollingEnabled(false);
        getData();
    }

    private void getData() {
        mPresenter.getData(cityCode, type, startTime, endTime);
        showLoading();
    }

    /**
     * 查询日期
     */
    private List<Date> mSelectedDates;

    @Override
    protected void clickRightMenu() {
        OrderReportPresenter.selectDate(this, mSelectedDates, new CalendarPickerDialog.OnCalendarRangeSelectedListener() {
            @Override
            public void onLeftClicked(CalendarPickerDialog dialog) {
//                dialog.dismiss();
            }

            @Override
            public void onRightClick(CalendarPickerDialog dialog, List<Date> selectedDates, Date start, Date end) {
                dialog.dismiss();
                mSelectedDates = selectedDates;
                if (selectedDates.size() >= 2) {
                    startTime = String.valueOf(Long.parseLong(TimeUtil.dataToStamp(start, "yyyy年MM月dd日")));
                    endTime = String.valueOf(Long.parseLong(TimeUtil.dataToStamp(end, "yyyy年MM月dd日")));
                } else {
                    startTime = null;
                    endTime = null;
                }
                getData();
            }
        });
    }

    @OnClick({R.id.btn_order_ranking_area})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_order_ranking_area:
                if (orderRankingBean != null && orderRankingBean.getSelectData() != null) {
                    new SelectAreaDialog(this, getString(R.string.dialog_title_select_area), cityCode, orderRankingBean.getSelectData(), new SelectAreaDialog.SelectAreaListener() {
                        @Override
                        public void select(OrderRankingBean.SelectDataBean selectDataBean) {
                            cityCode = selectDataBean.getCityCode();
                            type = selectDataBean.getType();
                            getData();
                        }
                    }).show();
                }
                break;
        }
    }

    /**
     * 获取数据成功
     *
     * @param orderRankingBean
     */
    @Override
    public void getDataSuccess(OrderRankingBean orderRankingBean) {
        dismissLoading();
        this.orderRankingBean = orderRankingBean;
        cityCode = orderRankingBean.getSelect().getCityCode();
        type = orderRankingBean.getSelect().getType();
        startTime = orderRankingBean.getStartTime();
        endTime = orderRankingBean.getEndTime();
        tvArea.setText(orderRankingBean.getSelect().getName());
        tvTime.setText(orderRankingBean.getTitle());
        String order = getString(R.string.report_order_ranking_my_order);
        if (orderRankingBean.getMyRank() != null && !TextUtils.isEmpty(orderRankingBean.getMyRank().getMyselfCounts())) {
            order += orderRankingBean.getMyRank().getMyselfCounts();
        }
        tvMyOrder.setText(order);
        String rank = getString(R.string.report_order_ranking_my_ranking);
        if (orderRankingBean.getMyRank() != null && !TextUtils.isEmpty(orderRankingBean.getMyRank().getMyselfRank())) {
            rank += orderRankingBean.getMyRank().getMyselfRank();
        }
        tvMyRanking.setText(rank);
        if (orderRankingBean.getRankData() == null || orderRankingBean.getRankData().size() == 0) {
            tvNoRanking.setVisibility(View.VISIBLE);
            rv.setVisibility(View.GONE);
        } else {
            LogCat.i("getRankData()", orderRankingBean.getRankData().toString());
            tvNoRanking.setVisibility(View.GONE);
            rv.setVisibility(View.VISIBLE);
            rv.setAdapter(new CommonAdapter<OrderRankingBean.RankDataBean>(this, orderRankingBean.getRankData()) {
                @Override
                protected int bindView(int viewType) {
                    return R.layout.item_rv_order_ranking;
                }

                @Override
                public void onBindHolder(RecyclerHolder holder, OrderRankingBean.RankDataBean rankDataBean, int position) {
                    LinearLayout ll = holder.obtainView(R.id.ll_item_rv_order_ranking);
                    TextView tvRanking = holder.obtainView(R.id.tv_item_rv_order_ranking);
                    ImageView ivRanking = holder.obtainView(R.id.iv_item_rv_order_ranking);
                    TextView tvCenter = holder.obtainView(R.id.tv_item_rv_order_ranking_center);
                    TextView tvCity = holder.obtainView(R.id.tv_item_rv_order_ranking_city);
                    TextView tvName = holder.obtainView(R.id.tv_item_rv_order_ranking_name);
                    TextView tvOrderNum = holder.obtainView(R.id.tv_item_rv_order_ranking_num);
                    ll.setBackgroundResource(position % 2 == 0 ? R.color.bg_item_order_report : R.color.color_while);
                    ivRanking.setVisibility(View.GONE);
                    tvRanking.setVisibility(View.GONE);
                    if (!rankDataBean.getRank().equals("-")) {
                        switch (parseValue(rankDataBean.getRank())) {
                            case 1:
                                ivRanking.setVisibility(View.VISIBLE);
                                ivRanking.setBackgroundResource(R.drawable.ranking_first);
                                break;
                            case 2:
                                ivRanking.setVisibility(View.VISIBLE);
                                ivRanking.setBackgroundResource(R.drawable.ranking_second);
                                break;
                            case 3:
                                ivRanking.setVisibility(View.VISIBLE);
                                ivRanking.setBackgroundResource(R.drawable.ranking_third);
                                break;
                            default:
                                tvRanking.setVisibility(View.VISIBLE);
                                tvRanking.setText(rankDataBean.getRank());
                                break;
                        }
                    } else {
                        tvRanking.setVisibility(View.VISIBLE);
                        tvRanking.setText(rankDataBean.getRank());
                    }
                    tvCenter.setText(rankDataBean.getArea());
                    tvCity.setText(rankDataBean.getCity());
                    tvName.setText(rankDataBean.getUserName());
                    tvOrderNum.setText(String.valueOf(rankDataBean.getCounts()));
                }
            });
        }
    }

    private int parseValue(String source) {
        try {
            return (int) Double.parseDouble(source);
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 获取数据失败
     */
    @Override
    public void getDataFailed(String failed) {
        dismissLoading();
        showLongToast(failed);
    }

    @Override
    protected Dialog getLoadingDialog() {
        return new LoadingTipDialog(this);
    }
}
