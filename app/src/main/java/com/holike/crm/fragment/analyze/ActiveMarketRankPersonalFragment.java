package com.holike.crm.fragment.analyze;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.holike.crm.R;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.ActiveMarketRankBean;
import com.holike.crm.util.Constants;

import butterknife.BindView;

/**
 * Created by wqj on 2018/6/21.
 * 主动营销排行榜-个人
 */

public class ActiveMarketRankPersonalFragment extends MyFragment {
    @BindView(R.id.tv_active_market_rank_personal_name)
    TextView tvName;
    @BindView(R.id.tv_active_market_rank_personal_city)
    TextView tvCity;
    @BindView(R.id.tv_active_market_rank_personal_data)
    TextView tvData;
    @BindView(R.id.tv_active_market_rank_personal_score)
    TextView tvScore;
    @BindView(R.id.tv_active_market_rank_personal_rankPre)
    TextView tvRankPre;
    @BindView(R.id.tv_active_market_rank_personal_count)
    TextView tvCount;
    @BindView(R.id.tv_active_market_rank_personal_pre_name)
    TextView tvPreName;
    @BindView(R.id.tv_active_market_rank_personal_rank_no2)
    TextView tvRankNo2;
    @BindView(R.id.tv_active_market_rank_personal_rank_no1)
    TextView tvRankNo1;
    @BindView(R.id.tv_active_market_rank_personal_rank_no3)
    TextView tvRankNo3;

    @Override
    protected void init() {
        super.init();
        setStatusBar();
        setTitleBg(R.color.bg_transparent);
        setTitle(getString(R.string.report_item17_title));
        Bundle bundle = getArguments();
        if (bundle != null) {
            ActiveMarketRankBean bean = (ActiveMarketRankBean) bundle.get(Constants.ACTIVE_MARKET_RANK_BEAN);
            if (bean != null && bean.getActiveData() != null) {
                tvName.setText(bean.getActiveData().getName());
                tvCity.setText(bean.getActiveData().getCity());
                String time = "";
                if (!TextUtils.isEmpty(bean.getActiveData().getTime())) {
                    time += bean.getActiveData().getTime();
                }
                time += "期间绩效得分";
                tvData.setText(time);
                tvScore.setText(bean.getActiveData().getAchievement());
                tvRankPre.setText(bean.getActiveData().getRank());
                String totalRank = "/" + bean.getActiveData().getTotalRank();
                tvCount.setText(totalRank);
                tvPreName.setText(bean.getActiveData().getPreName());
                tvRankNo1.setText(bean.getActiveData().getFirstThree().size() > 0 ? bean.getActiveData().getFirstThree().get(0).getName() : "");
                tvRankNo2.setText(bean.getActiveData().getFirstThree().size() > 1 ? bean.getActiveData().getFirstThree().get(1).getName() : "");
                tvRankNo3.setText(bean.getActiveData().getFirstThree().size() > 2 ? bean.getActiveData().getFirstThree().get(2).getName() : "");
            }
        }
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_active_market_rank_personal;
    }

    @Override
    protected BasePresenter attachPresenter() {
        return null;
    }

}
