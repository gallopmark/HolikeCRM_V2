package com.holike.crm.fragment.analyze;

import android.os.Bundle;
import android.widget.TextView;

import com.holike.crm.R;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.MonthPkBean;
import com.holike.crm.util.Constants;

import butterknife.BindView;

/**
 * Created by wqj on 2018/6/7.
 * 月度PK-中心经理
 */

public class MonthPkPersonalFragment extends MyFragment {
    @BindView(R.id.tv_month_pk_personal_city)
    TextView tvCity;
    @BindView(R.id.tv_month_pk_personal_rang)
    TextView tvRang;
    @BindView(R.id.tv_month_pk_personal_score)
    TextView tvScore;
    @BindView(R.id.tv_month_pk_personal_rankPre)
    TextView tvRankPre;
    @BindView(R.id.tv_month_pk_personal_count)
    TextView tvCount;
    @BindView(R.id.tv_month_pk_personal_pre_city)
    TextView tvPreCity;
    @BindView(R.id.tv_month_pk_personal_achievement_year)
    TextView tvAchievementYear;
    @BindView(R.id.tv_month_pk_personal_complete_year)
    TextView tvCompleteYear;
    @BindView(R.id.tv_month_pk_personal_task_month)
    TextView tvTaskMonth;
    @BindView(R.id.tv_month_pk_personal_achievement_month)
    TextView tvAchievementMonth;
    @BindView(R.id.tv_month_pk_personal_complete_month)
    TextView tvCompleteMonth;
    @BindView(R.id.tv_month_pk_personal_task_year)
    TextView tvTaskYear;
    @BindView(R.id.tv_month_pk_personal_rank_no2)
    TextView tvRankNo2;
    @BindView(R.id.tv_month_pk_personal_rank_no1)
    TextView tvRankNo1;
    @BindView(R.id.tv_month_pk_personal_rank_no3)
    TextView tvRankNo3;
    @BindView(R.id.tv_month_pk_personal_rank)
    TextView tvRank;
    @BindView(R.id.tv_month_pk_personal_rank_top_three)
    TextView tvRankTopThree;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_month_pk_personal;
    }

    @Override
    protected BasePresenter attachPresenter() {
        return null;
    }

    @Override
    protected void init() {
        super.init();
        setStatusBar();
        setTitle(getString(R.string.report_item15_title));
        Bundle bundle = getArguments();
        if (bundle != null) {
            MonthPkBean monthPkBean = (MonthPkBean) bundle.getSerializable(Constants.MONTH_PK);
            if(monthPkBean !=null){
                MonthPkBean.ProvinceDataBean provinceDataBean = monthPkBean.getProvinceData();
                tvCity.setText(provinceDataBean.getCity());
                tvRang.setText(provinceDataBean.getGroup());
                tvScore.setText(provinceDataBean.getPercent());
                tvRankPre.setText(provinceDataBean.getRankPre());
                tvRank.setText(provinceDataBean.getGroup() + getString(R.string.report_ranking));
                tvCount.setText(provinceDataBean.getRankAft());
                tvPreCity.setText(provinceDataBean.getPreCity());
                tvTaskYear.setText(provinceDataBean.getYearTarget());
                tvAchievementYear.setText(provinceDataBean.getYearComplete());
                tvCompleteYear.setText(provinceDataBean.getYearPercent());
                tvTaskMonth.setText(provinceDataBean.getMonthTarget());
                tvAchievementMonth.setText(provinceDataBean.getMonthComplete());
                tvCompleteMonth.setText(provinceDataBean.getMonthPercent());
                tvRankTopThree.setText(provinceDataBean.getGroup() + getString(R.string.report_top_three));
                tvRankNo1.setText(provinceDataBean.getFirstThree().size() > 0 ? provinceDataBean.getFirstThree().get(0).getProvince() : "");
                tvRankNo2.setText(provinceDataBean.getFirstThree().size() > 1 ? provinceDataBean.getFirstThree().get(1).getProvince() : "");
                tvRankNo3.setText(provinceDataBean.getFirstThree().size() > 2 ? provinceDataBean.getFirstThree().get(2).getProvince() : "");
            }
        }
    }

}
