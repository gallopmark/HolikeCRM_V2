package com.holike.crm.fragment.analyze;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.holike.crm.R;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.DealerRankBean;
import com.holike.crm.util.Constants;
import com.holike.crm.util.DensityUtil;

import butterknife.BindView;

/**
 * Created by wqj on 2018/6/1.
 * 经销商排行榜-经销商人员
 */

public class DealerRankPersonalFragment extends MyFragment {
    @BindView(R.id.tv_dealer_rank_personal_city)
    TextView tvCity;
    @BindView(R.id.tv_dealer_rank_personal_citycode)
    TextView tvCityCode;
    @BindView(R.id.tv_dealer_rank_personal_after)
    TextView tvAfter;
    @BindView(R.id.tv_dealer_rank_personal_rank)
    TextView tvRank;
    @BindView(R.id.tv_dealer_rank_personal_count)
    TextView tvCount;
    @BindView(R.id.tv_dealer_rank_personal_pre_city)
    TextView tvPreCity;
    @BindView(R.id.tv_dealer_rank_personal_rank_no1)
    TextView tvRankNo1;
    @BindView(R.id.tv_dealer_rank_personal_rank_no2)
    TextView tvRankNo2;
    @BindView(R.id.tv_dealer_rank_personal_rank_no3)
    TextView tvRankNo3;

    private int[] tvYourLocationId = new int[]{R.id.tv_dealer_rank_personal_your_location1, R.id.tv_dealer_rank_personal_your_location2, R.id.tv_dealer_rank_personal_your_location3, R.id.tv_dealer_rank_personal_your_location4, R.id.tv_dealer_rank_personal_your_location5};
    private int[] ivYourLocationId = new int[]{R.id.iv_dealer_rank_personal_your_location1, R.id.iv_dealer_rank_personal_your_location2, R.id.iv_dealer_rank_personal_your_location3, R.id.iv_dealer_rank_personal_your_location4, R.id.iv_dealer_rank_personal_your_location5};
    private int[] tvAreId = new int[]{R.id.tv_dealer_rank_personal_are1, R.id.tv_dealer_rank_personal_are2, R.id.tv_dealer_rank_personal_are3, R.id.tv_dealer_rank_personal_are4, R.id.tv_dealer_rank_personal_are5};

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_dealer_rank_personal;
    }

    @Override
    protected BasePresenter attachPresenter() {
        return null;
    }

    @Override
    protected void init() {
        super.init();
        setStatusBar();
        setTitle(getString(R.string.report_item11_title));
        setLeft(getString(R.string.report_title));
        Bundle bundle = getArguments();
        if (bundle != null) {
            DealerRankBean dealerDataBean = ((DealerRankBean) bundle.getSerializable(Constants.DEALER_RANK));
            if (dealerDataBean != null && dealerDataBean.getDealerData() != null) {
                DealerRankBean.DealerDataBean bean = dealerDataBean.getDealerData();
                tvCity.setText(bean.getCity());
                tvCityCode.setText(bean.getCityCode());
                tvAfter.setText(bean.getAchievementAfter() + "万");
                tvRank.setText(bean.getRankPre());
                tvCount.setText("/" + bean.getRankAft());
                tvPreCity.setText(bean.getPreCity());
                tvRankNo1.setText(bean.getFirstThree().size() > 0 ? bean.getFirstThree().get(0).getCity() : "");
                tvRankNo2.setText(bean.getFirstThree().size() > 1 ? bean.getFirstThree().get(1).getCity() : "");
                tvRankNo3.setText(bean.getFirstThree().size() > 2 ? bean.getFirstThree().get(2).getCity() : "");
                for (int i = 0, size = bean.getRang().size(); i < size; i++) {
                    DealerRankBean.DealerDataBean.RangBean rangBean = bean.getRang().get(i);
                    ((TextView) mContentView.findViewById(tvAreId[i])).setText(rangBean.getName());
                    if (rangBean.getIsIn().equals("1")) {
                        mContentView.findViewById(tvYourLocationId[i]).setVisibility(View.VISIBLE);
                        ImageView imageView = mContentView.findViewById(ivYourLocationId[i]);
                        imageView.setVisibility(View.VISIBLE);
                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) imageView.getLayoutParams();
                        layoutParams.leftMargin = (int) (DensityUtil.dp2px(68 - 16) * (Float.parseFloat(rangBean.getPosition().replace("%", "")) / 100));
                    }
                }
            }
        }
    }
}
