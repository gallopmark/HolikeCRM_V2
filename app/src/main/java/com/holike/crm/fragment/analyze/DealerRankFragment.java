package com.holike.crm.fragment.analyze;

import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.grallopmark.tablayout.SlidingTabLayout;
import com.grallopmark.tablayout.listener.OnTabSelectListener;
import com.holike.crm.R;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.DealerRankBean;
import com.holike.crm.presenter.fragment.DealerRankPresenter;
import com.holike.crm.presenter.fragment.PerformancePresenter;
import com.holike.crm.util.Constants;
import com.holike.crm.view.fragment.DealerRankView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by wqj on 2018/5/28.
 * 经销商排行
 */

public class DealerRankFragment extends MyFragment<DealerRankPresenter, DealerRankView> implements DealerRankView {
    @BindView(R.id.tab_dealer_rank_type)
    SlidingTabLayout tabType;
    @BindView(R.id.tv_dealer_rank_table_time)
    TextView tvTime;
    @BindView(R.id.rv_dealer_rank)
    RecyclerView rv;
    @BindView(R.id.vp_dealer_rank)
    ViewPager vp;

    protected List<String> tabTitles;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_dealer_rank;
    }

    @Override
    protected DealerRankPresenter attachPresenter() {
        return new DealerRankPresenter();
    }

    @Override
    protected void init() {
        super.init();
        setStatusBar(R.color.bg_state_bar);
        setTitle(getString(R.string.report_item11_title));
        setLeft(getString(R.string.report_title));
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        Bundle bundle = getArguments();
        if (bundle != null) {
            enterRank((DealerRankBean) bundle.getSerializable(Constants.DEALER_RANK));
        }
    }

    @Override
    public void getData(String cityCode) {
        showLoading();
        mPresenter.getData(cityCode);
    }

    @Override
    public void enterRank(DealerRankBean bean) {
        dismissLoading();
        tvTime.setText(bean.getTimeData());
        initTab(bean.getSelectData());
        showList(bean);
    }

    /**
     * 获取数据失败
     */
    @Override
    public void getDataFailed(String failed) {
        dismissLoading();
        showShortToast(failed);
    }

    @Override
    public void enterPersonal(DealerRankBean bean) {
        dismissLoading();
    }

    /**
     * 切换导航
     */
    private void initTab(final List<DealerRankBean.SelectDataBean> selectDataBeans) {
        if (tabTitles == null) {
            tabTitles = new ArrayList<>();
            for (DealerRankBean.SelectDataBean bean : selectDataBeans) {
                tabTitles.add(bean.getCityCode());
            }
            vp.setAdapter(DealerRankPresenter.getPagerAdapter(tabTitles.size()));
//            PerformancePresenter.setTabWidth(tabType, tabTitles.size());
            String[] titles = tabTitles.toArray(new String[0]);
            tabType.setupViewPager(vp, titles);
            tabType.setOnTabSelectListener(new OnTabSelectListener() {
                @Override
                public void onTabSelect(int position) {
                    getData(selectDataBeans.get(position).getCityCode());
                }

                @Override
                public void onTabReselect(int position) {
                }
            });
        }
    }

    /**
     * 显示列表
     *
     */
    private void showList(DealerRankBean bean) {
        rv.setAdapter(new CommonAdapter<DealerRankBean.RankListBean>(mContext, bean.getRankList()) {
            @Override
            protected int bindView(int viewType) {
                return R.layout.item_rv_dealer_rank;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, DealerRankBean.RankListBean rankListBean, int position) {
                LinearLayout ll = holder.obtainView(R.id.ll_item_rv_dealer_rank);
                TextView tvRank = holder.obtainView(R.id.tv_item_rv_dealer_rank_rank);
                ImageView ivRanking = holder.obtainView(R.id.iv_item_rv_dealer_rank);
                TextView tvCenter = holder.obtainView(R.id.tv_item_rv_dealer_rank_center);
                TextView tvCity = holder.obtainView(R.id.tv_item_rv_dealer_rank_city);
                TextView tvName = holder.obtainView(R.id.tv_item_rv_dealer_rank_name);
                TextView tvPerformance = holder.obtainView(R.id.tv_item_rv_dealer_rank_performance);
                TextView tvAdjustedPerformance = holder.obtainView(R.id.tv_item_rv_dealer_rank_adjusted_performance);
                ll.setBackgroundResource(position % 2 == 0 ? R.color.bg_item_order_report : R.color.color_while);
                ivRanking.setVisibility(View.GONE);
                tvRank.setVisibility(View.GONE);
                if (!rankListBean.getRank().equals("-")) {
                    switch (Integer.parseInt(rankListBean.getRank())) {
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
                            tvRank.setVisibility(View.VISIBLE);
                            tvRank.setText(rankListBean.getRank());
                            break;
                    }
                } else {
                    tvRank.setVisibility(View.VISIBLE);
                    tvRank.setText(rankListBean.getRank());
                }
                tvCenter.setText(rankListBean.getArea());
                tvCity.setText(rankListBean.getCity());
                tvName.setText(rankListBean.getName());
                tvPerformance.setText(rankListBean.getAchievementBefore());
                tvAdjustedPerformance.setText(rankListBean.getAchievementAfter());
            }
        });
    }

}
