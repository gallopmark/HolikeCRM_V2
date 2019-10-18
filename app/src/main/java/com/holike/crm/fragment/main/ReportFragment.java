package com.holike.crm.fragment.main;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.activity.analyze.ActiveMarketActivity;
import com.holike.crm.activity.analyze.ActiveMarketRankActivity;
import com.holike.crm.activity.analyze.BuildStoreActivity;
import com.holike.crm.activity.analyze.CupboardActivity;
import com.holike.crm.activity.analyze.DealerRankActivity;
import com.holike.crm.activity.analyze.FastLiveActivity;
import com.holike.crm.activity.analyze.InstallEvaluateActivity;
import com.holike.crm.activity.analyze.MonthPkActivity;
import com.holike.crm.activity.analyze.NetActivity;
import com.holike.crm.activity.analyze.NewRetailActivity;
import com.holike.crm.activity.analyze.OnlineAttractReportActivity;
import com.holike.crm.activity.analyze.OrderRankingActivity;
import com.holike.crm.activity.analyze.OrderReportActivity;
import com.holike.crm.activity.analyze.OriginalBoardActivity;
import com.holike.crm.activity.analyze.PerformanceActivity;
import com.holike.crm.activity.analyze.ProductTransactionReportActivity;
import com.holike.crm.activity.analyze.TerminalCheckActivity;
import com.holike.crm.activity.analyze.TranslateReportActivity;
import com.holike.crm.activity.analyze.OrderTradingTrendActivity;
import com.holike.crm.activity.analyze.WoodenDoorActivity;
import com.holike.crm.activity.homepage.MessageV2Activity;
import com.holike.crm.activity.analyze.CustomerSatisfactionActivity;
import com.holike.crm.base.BaseFragment;
import com.holike.crm.bean.ReportPermissionsBean;
import com.holike.crm.presenter.fragment.HomePagePresenter2;
import com.holike.crm.presenter.fragment.ReportPresenter;
import com.holike.crm.view.fragment.ReportView;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import butterknife.BindView;

/**
 * Created by wqj on 2018/2/24.
 * 分析
 */
public class ReportFragment extends BaseFragment<ReportPresenter, ReportView> implements ReportView {

    @BindView(R.id.rv_report)
    RecyclerView rv;

    private Class[] classes = new Class[]{OrderTradingTrendActivity.class, OrderReportActivity.class,
            TranslateReportActivity.class, OrderRankingActivity.class,
            ProductTransactionReportActivity.class, BuildStoreActivity.class,
            InstallEvaluateActivity.class, PerformanceActivity.class,
            CupboardActivity.class, OriginalBoardActivity.class,
            DealerRankActivity.class, TerminalCheckActivity.class,
            NewRetailActivity.class, NetActivity.class,
            MonthPkActivity.class, ActiveMarketActivity.class,
            ActiveMarketRankActivity.class, FastLiveActivity.class,
            OnlineAttractReportActivity.class, WoodenDoorActivity.class,
            CustomerSatisfactionActivity.class};

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_report;
    }

    @Override
    protected ReportPresenter attachPresenter() {
        return new ReportPresenter();
    }

    @Override
    protected void init() {
        setTitle(mContext.getString(R.string.report_title));
        rv.setNestedScrollingEnabled(false);
        rv.setLayoutManager(new GridLayoutManager(mContext, 3));
        initData();
    }

    private void initData() {
        showLoading();
        mPresenter.getPermissions();
    }

    @Override
    public void onResume() {
        super.onResume();
        setRightMenuMsg(HomePagePresenter2.isNewMsg());
    }

    @Override
    protected void clickRightMenu(CharSequence menuText, View actionView) {
        startActivity(MessageV2Activity.class);
    }

    @Override
    public void noPermissions() {
        dismissLoading();
        noAuthority();
    }

    @Override
    public void getPermissionsSuccess(final List<ReportPermissionsBean> list) {
        dismissLoading();
        rv.setAdapter(new CommonAdapter<ReportPermissionsBean>(mContext, list) {
            @Override
            protected int bindView(int viewType) {
                return R.layout.item_rv_fragment_report;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, final ReportPermissionsBean bean, int position) {
                ImageView iv = holder.obtainView(R.id.iv_icon);
                TextView tv = holder.obtainView(R.id.tv_title);
                Glide.with(mContext).load(bean.getImageUrl())
                        .apply(new RequestOptions().placeholder(R.drawable.analysis_default)
                                .error(R.drawable.analysis_default))
                        .into(iv);
//                Glide.with(mContext).load(reportPermissionsBean.getImageUrl()).placeholder(R.drawable.analysis_default).error(R.drawable.analysis_default).into(iv);
                tv.setText(bean.getTitle());
                holder.itemView.setOnClickListener(v -> {
                    int type = bean.getType() - 1;
                    if (type >= 0 && type < classes.length) {
                        startActivity(classes[type]);
                        switch (type) {
                            case 1:
                                MobclickAgent.onEvent(mContext, "analyze_order_trend");
                                break;
                            case 2:
                                MobclickAgent.onEvent(mContext, "analyze_order_report");
                                break;
                            case 3:
                                MobclickAgent.onEvent(mContext, "analyze_order_translate_report");
                                break;
                            case 4:
                                MobclickAgent.onEvent(mContext, "analyze_sign_order_ranking");
                                break;
                            case 5:
                                MobclickAgent.onEvent(mContext, "analyze_product_trading");
                                break;
                            case 6:
                                MobclickAgent.onEvent(mContext, "analyze_build_shop");
                                break;
                            case 7:
                                break;
                            case 8:
                                MobclickAgent.onEvent(mContext, "analyze_performance");
                                break;
                            case 9:
                                MobclickAgent.onEvent(mContext, "analyze_cupboard");
                                break;
                            case 10:
                                MobclickAgent.onEvent(mContext, "analyze_original_board");
                                break;
                            case 11:
                                MobclickAgent.onEvent(mContext, "analyze_dealers_ranking");
                                break;
                            case 12:
                                MobclickAgent.onEvent(mContext, "analyze_terminal_check");
                                break;
                            case 13:
                                MobclickAgent.onEvent(mContext, "analyze_new_retail");
                                break;
                            case 14:
                                MobclickAgent.onEvent(mContext, "analyze_net");
                                break;
                            case 15:
                                MobclickAgent.onEvent(mContext, "analyze_month_pk");
                                break;
                            case 16:
                                MobclickAgent.onEvent(mContext, "analyze_active_marketing");
                                break;
                            case 17:
                                MobclickAgent.onEvent(mContext, "analyze_active_marketing_ranking");
                                break;
                            case 18:
                                MobclickAgent.onEvent(mContext, "analyze_fast_live");
                                break;
                            case 19:
                                MobclickAgent.onEvent(mContext, "analyze_online_attract_report");
                                break;
                        }
                    }
                });
            }
        });
    }

    @Override
    public void getPermissionsFailed(String failed) {
        dismissLoading();
        if (isNoAuth(failed)) {
            noAuthority();
        } else {
            noNetwork(failed);
        }
    }

    @Override
    protected void reload() {
        initData();
    }
}
