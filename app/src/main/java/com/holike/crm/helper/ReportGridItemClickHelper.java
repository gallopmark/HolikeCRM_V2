package com.holike.crm.helper;


import com.holike.crm.activity.analyze.ActiveMarketActivity;
import com.holike.crm.activity.analyze.ActiveMarketRankActivity;
import com.holike.crm.activity.analyze.BuildStoreActivity;
import com.holike.crm.activity.analyze.CupboardActivity;
import com.holike.crm.activity.analyze.CustomerSatisfactionActivity;
import com.holike.crm.activity.analyze.DealerRankActivity;
import com.holike.crm.activity.analyze.FastLiveActivity;
import com.holike.crm.activity.analyze.InstallEvaluateActivity;
import com.holike.crm.activity.analyze.MonthPkActivity;
import com.holike.crm.activity.analyze.NetActivity;
import com.holike.crm.activity.analyze.NewRetailActivity;
import com.holike.crm.activity.analyze.OnlineAttractReportActivity;
import com.holike.crm.activity.analyze.OrderRankingActivity;
import com.holike.crm.activity.analyze.OrderReportActivity;
import com.holike.crm.activity.analyze.OrderTradingTrendActivity;
import com.holike.crm.activity.analyze.OriginalBoardActivity;
import com.holike.crm.activity.analyze.PerformanceActivity;
import com.holike.crm.activity.analyze.ProductTransactionReportActivity;
import com.holike.crm.activity.analyze.TerminalCheckActivity;
import com.holike.crm.activity.analyze.TranslateReportActivity;
import com.holike.crm.activity.analyze.WoodenDoorActivity;
import com.holike.crm.activity.report.EmployeePerformanceActivity;
import com.holike.crm.activity.report.EmployeeRankingActivity;
import com.holike.crm.activity.report.BusinessReferenceMainActivity;
import com.holike.crm.activity.report.BusinessTargetActivity;
import com.holike.crm.activity.report.CustomerConversionActivity;
import com.holike.crm.activity.report.FactoryPerformanceActivity;
import com.holike.crm.activity.report.HomeDollChannelActivity;
import com.holike.crm.activity.report.MultiPerformanceAnalysisActivity;
import com.holike.crm.activity.report.OnlineDrainageActivity;
import com.holike.crm.activity.report.SheetAnalysisActivity;
import com.holike.crm.activity.report.ShopAnalysisActivity;
import com.holike.crm.base.BaseActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by pony on 2019/10/31.
 * Version v3.0 app报表
 */
public class ReportGridItemClickHelper {

    public static void dealWith(BaseActivity<?, ?> activity, int type, String title) {
        switch (type) {
            case 1:
                MobclickAgent.onEvent(activity, "analyze_order_trend");
                activity.startActivity(OrderTradingTrendActivity.class);
                break;
            case 2:
                MobclickAgent.onEvent(activity, "analyze_order_report");
                activity.startActivity(OrderReportActivity.class);
                break;
            case 3:
                MobclickAgent.onEvent(activity, "analyze_order_translate_report");
                activity.startActivity(TranslateReportActivity.class);
                break;
            case 4:
                MobclickAgent.onEvent(activity, "analyze_sign_order_ranking");
                activity.startActivity(OrderRankingActivity.class);
                break;
            case 5:
                MobclickAgent.onEvent(activity, "analyze_product_trading");
                activity.startActivity(ProductTransactionReportActivity.class);
                break;
            case 6:
                MobclickAgent.onEvent(activity, "analyze_build_shop");
                activity.startActivity(BuildStoreActivity.class);
                break;
            case 7:
                activity.startActivity(InstallEvaluateActivity.class);
                break;
            case 8:
                MobclickAgent.onEvent(activity, "analyze_performance");
                activity.startActivity(PerformanceActivity.class);
                break;
            case 9:
                MobclickAgent.onEvent(activity, "analyze_cupboard");
                activity.startActivity(CupboardActivity.class);
                break;
            case 10:
                MobclickAgent.onEvent(activity, "analyze_original_board");
                activity.startActivity(OriginalBoardActivity.class);
                break;
            case 11:
                MobclickAgent.onEvent(activity, "analyze_dealers_ranking");
                activity.startActivity(DealerRankActivity.class);
                break;
            case 12:
                MobclickAgent.onEvent(activity, "analyze_terminal_check");
                activity.startActivity(TerminalCheckActivity.class);
                break;
            case 13:
                MobclickAgent.onEvent(activity, "analyze_new_retail");
                activity.startActivity(NewRetailActivity.class);
                break;
            case 14:
                MobclickAgent.onEvent(activity, "analyze_net");
                activity.startActivity(NetActivity.class);
                break;
            case 15:
                MobclickAgent.onEvent(activity, "analyze_month_pk");
                activity.startActivity(MonthPkActivity.class);
                break;
            case 16:
                MobclickAgent.onEvent(activity, "analyze_active_marketing");
                activity.startActivity(ActiveMarketActivity.class);
                break;
            case 17: //主动营销排行
                MobclickAgent.onEvent(activity, "analyze_active_marketing_ranking");
                activity.startActivity(ActiveMarketRankActivity.class);
                break;
            case 18: //订金交易报表
                MobclickAgent.onEvent(activity, "analyze_fast_live");
                activity.startActivity(FastLiveActivity.class);
                break;
            case 19: //线上引流
                MobclickAgent.onEvent(activity, "analyze_online_attract_report");
                activity.startActivity(OnlineAttractReportActivity.class);
                break;
            case 20: //木门业绩报表
                activity.startActivity(WoodenDoorActivity.class);
                break;
            case 21: //客户满意度
                activity.startActivity(CustomerSatisfactionActivity.class);
                break;
            case 22:  //员工个人排行
                activity.startActivity(EmployeeRankingActivity.class);
                break;
            case 23:
                break;
            case 24:
                activity.startActivity(BusinessTargetActivity.class); //经营目标
                break;
            case 25:
                activity.startActivity(EmployeePerformanceActivity.class);  //员工绩效
                break;
            case 28:
                activity.startActivity(FactoryPerformanceActivity.class); //经销商出厂业绩
                break;
            case 29:  //完成率及同比-业绩
                MultiPerformanceAnalysisActivity.renderRatioPerformance(activity, title);
                break;
            case 30:  //完成率及同比-定制
                MultiPerformanceAnalysisActivity.renderRatioCustom(activity, title);
                break;
            case 31: //完成率及同比-橱柜
                MultiPerformanceAnalysisActivity.renderRatioCupboard(activity, title);
                break;
            case 32: //完成率及同比-木门
                MultiPerformanceAnalysisActivity.renderRatioWoodendoor(activity, title);
                break;
            case 33: //完成率及同比-成品
                MultiPerformanceAnalysisActivity.renderRatioProduct(activity, title);
                break;
            case 34: //完成率及同比-大家居
                MultiPerformanceAnalysisActivity.renderRatioBighome(activity, title);
                break;
            case 35: //多维度业绩分析-品类
                MultiPerformanceAnalysisActivity.renderCategory(activity, title);
                break;
            case 36: //多维度业绩分析-渠道
                MultiPerformanceAnalysisActivity.renderChannel(activity, title);
                break;
            case 37: //多维度业绩分析-花色
                MultiPerformanceAnalysisActivity.renderColors(activity, title);
                break;
            case 38:  //多维度业绩分析-空间
                MultiPerformanceAnalysisActivity.renderSpace(activity, title);
                break;
            case 39: //多维度业绩分析-系列
                MultiPerformanceAnalysisActivity.renderSeries(activity, title);
                break;
            case 40: //多维度业绩分析-宅配
                MultiPerformanceAnalysisActivity.renderDelivery(activity, title);
                break;
            case 41:
                activity.startActivity(CustomerConversionActivity.class);  //客户转化
                break;
            case 42: //线上引流
                OnlineDrainageActivity.start(activity, title);
                break;
            case 43:  //家装渠道-营销人员
                HomeDollChannelActivity.start(activity, false);
                break;
            case 44:  //门店分析
                activity.startActivity(ShopAnalysisActivity.class);
                break;
            case 45:  //板材分析
                SheetAnalysisActivity.start(activity, title);
                break;
            case 46: //生意内参
                BusinessReferenceMainActivity.start(activity, title);
                break;
            case 47: //家装渠道-经销商
                HomeDollChannelActivity.start(activity, true);
                break;
        }
    }
}
