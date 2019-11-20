package com.holike.crm.fragment.report;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.gallopmark.recycler.widgetwrapper.WrapperRecyclerView;
import com.grallopmark.tablayout.CommonTabLayout;
import com.grallopmark.tablayout.TabEntity;
import com.grallopmark.tablayout.listener.CustomTabEntity;
import com.grallopmark.tablayout.listener.OnTabSelectListener;
import com.holike.crm.R;
import com.holike.crm.activity.report.DealerMultiPerformanceActivity;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.base.BaseFragment;
import com.holike.crm.base.FragmentHelper;
import com.holike.crm.bean.BusinessReferenceMainBean;
import com.holike.crm.bean.MultiItem;
import com.holike.crm.dialog.CalendarPickerDialog;
import com.holike.crm.dialog.PlainTextTipsDialog;
import com.holike.crm.helper.CalendarPickerHelper;
import com.holike.crm.helper.CenterAlignImageSpan;
import com.holike.crm.helper.PickerHelper;
import com.holike.crm.helper.TextSpanHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pony.xcode.chart.LineChartView;
import pony.xcode.chart.PieChartView;
import pony.xcode.chart.data.LineChartData;
import pony.xcode.chart.data.PieChartData;

/**
 * Created by pony on 2019/11/4.
 * Version v3.0 app报表
 * 生意内参首页
 */
class BusinessReferenceMainHelper extends FragmentHelper {
    private ImageView mBackTopImageView; //回到顶部按钮
    private List<MultipleItem> mDataList;
    private MultipleItemAdapter mAdapter;
    private View mHeaderView;   //头部布局
    private TextSpanHelper mTextHelper;
    private StyleSpan mStyleSpan;
    private ForegroundColorSpan mColorSpan;
    private AbsoluteSizeSpan mSizeSpan;
    private static final int FLAGS = SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE;
    private static final String CODE_2000 = "\u2000";
    private static final String CODE_3000 = "\u3000";
    private int[] mColorArray;
    private String mTime;
    private Date mStartDate, mEndDate;
    private List<Date> mSelectDate;
    private String mShopId; //选择的门店id
    private String mSelectShopName;

    BusinessReferenceMainHelper(BaseFragment<?, ?> fragment) {
        super(fragment);
        initTitle(fragment.getArguments());
        mBackTopImageView = obtainView(R.id.iv_back_top);
        initRender();
        mSelectShopName = mContext.getString(R.string.all_stores);
        initRecycler();
        onSuccess(new BusinessReferenceMainBean());
    }

    private void initTitle(Bundle bundle) {
        String title = mContext.getString(R.string.title_business_reference);
        if (bundle != null) {
            title = bundle.getString("title", mContext.getString(R.string.title_business_reference));
        }
        mFragment.setTitle(title);
    }

    private void initRender() {
        mTextHelper = TextSpanHelper.from(mContext);
        mStyleSpan = new StyleSpan(Typeface.BOLD);
        mColorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.textColor4));
        mSizeSpan = new AbsoluteSizeSpan(mContext.getResources().getDimensionPixelSize(R.dimen.textSize_16), false);
        mColorArray = new int[]{ContextCompat.getColor(mContext, R.color.ring_one),
                ContextCompat.getColor(mContext, R.color.ring_tow),
                ContextCompat.getColor(mContext, R.color.ring_three),
                ContextCompat.getColor(mContext, R.color.ring_four),
                ContextCompat.getColor(mContext, R.color.ring_five),
                ContextCompat.getColor(mContext, R.color.ring_six)
        };
    }

    private void initRecycler() {
        final WrapperRecyclerView wrapperRv = obtainView(R.id.rv_wrapper);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        wrapperRv.setLayoutManager(layoutManager);
        initHeader(wrapperRv);
        wrapperRv.addHeaderView(mHeaderView);
        mDataList = new ArrayList<>();
        mAdapter = new MultipleItemAdapter(mContext, mDataList);
        wrapperRv.setAdapter(mAdapter);
        wrapperRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                //获取RecyclerView当前顶部显示的第一个条目对应的索引
                int position = layoutManager.findFirstVisibleItemPosition();
                //根据索引来获取对应的itemView
                View firstChildView = layoutManager.findViewByPosition(position);
                if (firstChildView != null) {
                    //获取当前显示条目的高度
                    int itemHeight = firstChildView.getHeight();
                    //获取当前RecyclerView 偏移量
                    int flag = (position) * itemHeight - firstChildView.getTop();
                    //注意事项：recyclerView不要设置padding
                    if (flag == 0)
                        mBackTopImageView.setVisibility(View.GONE);
                    else
                        mBackTopImageView.setVisibility(View.VISIBLE);
                }
            }
        });
        mBackTopImageView.setOnClickListener(view -> {
            mBackTopImageView.setVisibility(View.GONE);
            wrapperRv.scrollToPosition(0);
        });
    }

    private void initHeader(WrapperRecyclerView wrapperRv) {
        mHeaderView = LayoutInflater.from(mContext).inflate(R.layout.header_business_reference, wrapperRv, false);
        CommonTabLayout tabLayout = mHeaderView.findViewById(R.id.tab_layout);
        List<CustomTabEntity> tabEntities = new ArrayList<>();
        tabEntities.add(new TabEntity(mContext.getString(R.string.this_month)));
        tabEntities.add(new TabEntity(mContext.getString(R.string.tab_annual)));
        tabEntities.add(new TabEntity(mContext.getString(R.string.tab_query_date)));
        tabLayout.setTabData(tabEntities);
        if (TextUtils.equals(mTime, "1")) {
            tabLayout.setCurrentTab(1);
        } else if (TextUtils.equals(mTime, "2")) {
            tabLayout.setCurrentTab(2);
        }
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                if (position == 0) {
                    mTime = "0";
                    doRequest();
                } else if (position == 1) {
                    mTime = "1";
                    doRequest();
                } else {
                    mTime = "2";
                    onSelectDates();
                }
            }

            @Override
            public void onTabReselect(int position) {
                if (position == 2) {
                    onSelectDates();
                }
            }
        });
    }

    private void onSelectDates() {
        CalendarPickerHelper.showCalendarDialog(mContext, mSelectDate, new CalendarPickerHelper.SimpleCalendarPickerListener() {

            @Override
            public void onCalendarPicker(CalendarPickerDialog dialog, List<Date> selectedDates, Date start, Date end) {
                mSelectDate = selectedDates;
                if (!selectedDates.isEmpty()) {
                    mStartDate = start;
                    mEndDate = end;
                    doRequest();
                } else {
                    mStartDate = null;
                    mEndDate = null;
                }
            }
        });
    }

    private void doRequest() {
        onSuccess(new BusinessReferenceMainBean());
    }

    /*加载失败*/
    void onFailure(String failReason) {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) mHeaderView.getLayoutParams();
        params.height = RecyclerView.LayoutParams.MATCH_PARENT;
        View view = LayoutInflater.from(mContext).inflate(R.layout.include_empty_page, refactor(), true);
        view.findViewById(R.id.ll_empty_page).setBackgroundResource(R.color.color_while);
        TextView tv = view.findViewById(R.id.tv_empty_page);
        tv.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(mContext, R.drawable.no_network), null, null);
        tv.setText(failReason);
        TextView tvReload = view.findViewById(R.id.btn_empty_page_reload);
        tvReload.setVisibility(View.VISIBLE);
        tvReload.setOnClickListener(v -> reload());
        mDataList.clear();
        mAdapter.notifyDataSetChanged();
    }

    private FrameLayout refactor() {
        FrameLayout container = mHeaderView.findViewById(R.id.fl_header_container);
        if (container.getChildCount() > 0) {
            container.removeAllViews();
        }
        return container;
    }

    private void reload() {
        refactor();
        doRequest();
    }

    void onSuccess(BusinessReferenceMainBean bean) {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) mHeaderView.getLayoutParams();
        params.height = RecyclerView.LayoutParams.WRAP_CONTENT;
        View view = LayoutInflater.from(mContext).inflate(R.layout.header_business_reference_child, refactor(), true);
        TextView tvSelectShop = view.findViewById(R.id.tv_select_shop);
        tvSelectShop.setText(mSelectShopName);
        tvSelectShop.setOnClickListener(v -> onSelectShop(tvSelectShop));
        ((TextView) mHeaderView.findViewById(R.id.tv_time_detail)).setText("");  //显示时间段
        dataUpdate(bean);
    }

    private void onSelectShop(final TextView tvShop) {
        PickerHelper.showOptionsPicker(mContext, new ArrayList<>(), (position, bean) -> {
            mShopId = bean.id;
            mSelectShopName = bean.name;
            tvShop.setText(bean.name);
        });
    }

    private void dataUpdate(BusinessReferenceMainBean bean) {
        List<MultipleItem> dataList = new ArrayList<>();
        dataList.add(new MultipleItem(MultipleItem.TYPE_PERFORMANCE_RATIO_TITLE, mContext.getString(R.string.performance_ratio), "")); //业绩占比-title
        dataList.add(new PerformanceRatioItem()); //业绩占比-content
        dataList.add(new MultipleItem(MultipleItem.TYPE_COLOR_PERFORMANCE_TITLE, mContext.getString(R.string.top5_color_performance), mContext.getString(R.string.more_colors_arrow))); //花色业绩-title
        dataList.add(new ColorPerformanceItem()); //花色业绩-content
        dataList.add(new MultipleItem(MultipleItem.TYPE_SERIES_PERFORMANCE_TITLE, mContext.getString(R.string.top5_series_performance), mContext.getString(R.string.more_series_arrow))); //系列业绩-title
        dataList.add(new SeriesPerformanceItem()); //系列业绩-content
        dataList.add(new MultipleItem(MultipleItem.TYPE_CUSTOMER_SOURCE_TITLE, mContext.getString(R.string.tips_customer_source), "")); //客户来源-title
        dataList.add(new CustomerSourceItem());  //客户来源-content
        dataList.add(new MultipleItem(MultipleItem.TYPE_CUSTOMER_ANALYSIS_TITLE, mContext.getString(R.string.customer_analysis), mContext.getString(R.string.the_data_description)));  //客户分析-title
        dataList.add(new CustomerAnalysisItem()); //客户分析-content
        dataList.add(new MultipleItem(MultipleItem.TYPE_CUSTOMER_SATISFACTION_TITLE, mContext.getString(R.string.customer_satisfaction_tips), "")); //客户满意度-title
        dataList.add(new CustomerSatisfactionItem()); //客户满意度-content
        dataList.add(new MultipleItem(MultipleItem.TYPE_SHOP_BENEFIT_TITLE, mContext.getString(R.string.shop_benefit), mContext.getString(R.string.the_data_description))); //门店效益-title
        dataList.add(new ShopBenefitItem()); //门店效益-content
//        dataList.add(new MultipleItem(MultipleItem.TYPE_PROFITABILITY_TITLE, mContext.getString(R.string.profitability), mContext.getString(R.string.the_data_description))); //盈利状况-title
//        dataList.add(new ProfitabilityItem()); //盈利状况-content
        dataList.add(new MultipleItem(MultipleItem.TYPE_DEALER_RANKING_TITLE, mContext.getString(R.string.dealer_ranking), "")); //经销商排名-title
        dataList.add(new DealerRankingOneItem());
        dataList.add(new MultipleItem(MultipleItem.TYPE_DEALER_RANKING_TWO));
        dataList.add(new MultipleItem(MultipleItem.TYPE_DEALER_RANKING_THREE));
        dataList.add(new MultipleItem(MultipleItem.TYPE_DEALER_RANKING_FOUR));
        mDataList.clear();
        mDataList.addAll(dataList);
        mAdapter.notifyDataSetChanged();
    }

    class MultipleItem implements MultiItem {
        static final int TYPE_PERFORMANCE_RATIO_TITLE = 0; //业绩占比
        static final int TYPE_PERFORMANCE_RATIO = 1;
        static final int TYPE_COLOR_PERFORMANCE_TITLE = 2; //花色业绩
        static final int TYPE_COLOR_PERFORMANCE = 3;
        static final int TYPE_SERIES_PERFORMANCE_TITLE = 4; //系列业绩
        static final int TYPE_SERIES_PERFORMANCE = 5;
        static final int TYPE_CUSTOMER_SOURCE_TITLE = 6; //客户来源
        static final int TYPE_CUSTOMER_SOURCE = 7;
        static final int TYPE_CUSTOMER_ANALYSIS_TITLE = 8; //客户分析
        static final int TYPE_CUSTOMER_ANALYSIS = 9;
        static final int TYPE_CUSTOMER_SATISFACTION_TITLE = 10; //客户满意度
        static final int TYPE_CUSTOMER_SATISFACTION = 11;
        static final int TYPE_SHOP_BENEFIT_TITLE = 12;  //门店效益
        static final int TYPE_SHOP_BENEFIT = 13;
        static final int TYPE_PROFITABILITY_TITLE = 14; //盈利状况
        static final int TYPE_PROFITABILITY = 15;
        static final int TYPE_DEALER_RANKING_TITLE = 16; //经销商排行
        static final int TYPE_DEALER_RANKING_ONE = 17;
        static final int TYPE_DEALER_RANKING_TWO = 18;
        static final int TYPE_DEALER_RANKING_THREE = 19;
        static final int TYPE_DEALER_RANKING_FOUR = 20;

        int itemType;
        CharSequence title;
        CharSequence menu;

        MultipleItem(int itemType) {
            this.itemType = itemType;
        }

        MultipleItem(int itemType, CharSequence title, CharSequence menu) {
            this(itemType);
            this.title = title;
            this.menu = menu;
        }

        @Override
        public int getItemType() {
            return itemType;
        }

        int getColor(boolean isRise) {
            if (isRise) return ContextCompat.getColor(mContext, R.color.green);
            return ContextCompat.getColor(mContext, R.color.bg_homepage_new);
        }

        Drawable getDrawable(boolean isRise) {
            if (isRise) return ContextCompat.getDrawable(mContext, R.drawable.ic_rise);
            return ContextCompat.getDrawable(mContext, R.drawable.ic_decline);
        }
    }

    class MultiPerformanceItem extends MultipleItem {
        CharSequence performance;
        CharSequence theFirst;
        CharSequence theSecond;
        CharSequence theThird;
        CharSequence theFourth;
        CharSequence theFifth;
        CharSequence theOther;

        MultiPerformanceItem(int itemType) {
            super(itemType);
        }

        SpannableString getTextSpan(String name, String performance, String percent) {
            String source = TextUtils.isEmpty(name) ? "" : name + CODE_3000;
            int start = source.length();
            source += TextUtils.isEmpty(performance) ? "" : performance + CODE_3000;
            source += TextUtils.isEmpty(percent) ? "" : percent;
            SpannableString ss = new SpannableString(source);
            ss.setSpan(mStyleSpan, start, source.length(), FLAGS);
            return ss;
        }
    }

    /*业绩占比*/
    class PerformanceRatioItem extends MultiPerformanceItem {
        CharSequence yearOnYear;  //同比增长
        CharSequence ringGrowth; //环比增长

        PerformanceRatioItem() {
            super(MultipleItem.TYPE_PERFORMANCE_RATIO);
            performance = mTextHelper.obtain(R.string.this_month_performance, "\n200万",
                    mColorSpan, mStyleSpan, new AbsoluteSizeSpan(mContext.getResources().getDimensionPixelSize(R.dimen.textSize_16), false));
            yearOnYear = mTextHelper.obtain(R.string.growth_year_on_year, CODE_2000 + "-0.18%",
                    new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.bg_homepage_new)), mStyleSpan);
            ringGrowth = mTextHelper.obtain(R.string.growth_in_the_ring, CODE_2000 + "+12.06%",
                    new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.green)), mStyleSpan);
            theFirst = getTextSpan("KA店-红星美凯龙", "80.66万", "40.33%");
            theSecond = getTextSpan("KA店-居然之家", "40.6万", "20.3%");
            theThird = getTextSpan("商超店-其他商超", "32万", "16%");
            theFourth = getTextSpan("经销商分销门店", "24万", "12%");
            theFifth = getTextSpan("KA店-其他KA渠道", "16万", "8%");
            theOther = getTextSpan("其他", "6.74万", "3.37%");
        }
    }

    /*花色业绩*/
    class ColorPerformanceItem extends MultiPerformanceItem {
        ColorPerformanceItem() {
            super(ColorPerformanceItem.TYPE_COLOR_PERFORMANCE);
            performance = mTextHelper.obtain(R.string.top5_performance, "\n100万", mStyleSpan,
                    mColorSpan, mSizeSpan);
            theFirst = getTextSpan("密苏里胡桃", "37.98万", "37.98%");
            theSecond = getTextSpan("赤杨-曼物宁", "25.56万", "25.56%");
            theThird = getTextSpan("北欧阳光", "10万", "10%");
            theFourth = getTextSpan("贝加尔灰橡", "9.33万", "9.33%");
            theFifth = getTextSpan("简雅", "9万", "9%");
            theOther = getTextSpan("其他", "8.13万", "8.13%");
        }
    }

    /*系列业绩*/
    class SeriesPerformanceItem extends MultiPerformanceItem {

        SeriesPerformanceItem() {
            super(ColorPerformanceItem.TYPE_SERIES_PERFORMANCE);
            performance = mTextHelper.obtain(R.string.top5_performance, "\n100万", mStyleSpan,
                    mColorSpan, mSizeSpan);
            theFirst = getTextSpan("北欧风情", "37.98万", "37.98%");
            theSecond = getTextSpan("新中式", "25.56万", "25.56%");
            theThird = getTextSpan("波尔多庄园", "10万", "10%");
            theFourth = getTextSpan("贝加尔湖畔", "9.33万", "9.33%");
            theFifth = getTextSpan("那不勒斯轻奢", "9万", "9%");
            theOther = getTextSpan("其他", "8.13万", "8.13%");
        }
    }

    /*客户来源*/
    class CustomerSourceItem extends MultiPerformanceItem {
        CharSequence yearOnYear;  //同比增长
        CharSequence ringGrowth; //环比增长

        CustomerSourceItem() {
            super(MultipleItem.TYPE_CUSTOMER_SOURCE);
            performance = mTextHelper.obtain(R.string.number_of_new_customers, "\n1000人"
                    , mColorSpan, mStyleSpan, mSizeSpan);
            yearOnYear = mTextHelper.obtain(R.string.growth_year_on_year, CODE_2000 + "+12%",
                    new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.green))
                    , mStyleSpan);
            ringGrowth = mTextHelper.obtain(R.string.growth_in_the_ring, CODE_2000 + "-16%",
                    new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.bg_homepage_new))
                    , mStyleSpan);
            theFirst = getTextSpan("电话资源", "400人", "35.68%");
            theSecond = getTextSpan("老客户介绍", "260人", "28.56%");
            theThird = getTextSpan("家装公司合作", "140人", "14.76%");
            theFourth = getTextSpan("商超自然人流", "106人", "12%");
            theFifth = getTextSpan("微信爆破&社群营销", "100", "10%");
            theOther = getTextSpan("其他", "94人", "9.4%");
        }
    }

    /*客户分析item*/
    class CustomerAnalysisItem extends MultipleItem {
        CharSequence customerPrice; //客单价
        CharSequence period; //平均成交周期
        CharSequence signOrder; //签约-下单
        CharSequence orderInstall; //下单-已安装

        CustomerAnalysisItem() {
            super(MultipleItem.TYPE_CUSTOMER_ANALYSIS);
            customerPrice = mTextHelper.obtain(R.string.customer_price, CODE_3000 + "8.05万",
                    new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.bg_homepage_new)), mStyleSpan); //客单价
            period = mTextHelper.obtain(R.string.average_turnover_period2, CODE_3000 + "15天",
                    new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.green)), mStyleSpan);
            signOrder = getTextSpan(R.string.signing, R.string.order, "2天");
            orderInstall = getTextSpan(R.string.order, R.string.installed, "3天");
        }

        SpannableString getTextSpan(int leftId, int rightId, String content) {
            String source = mContext.getString(leftId);
            source += " [image] " + mContext.getString(rightId);
            int start = source.length();
            source += CODE_3000 + content;
            SpannableString ss = new SpannableString(source);
            ss.setSpan(new CenterAlignImageSpan(mContext, R.drawable.ic_step_right), source.indexOf("["), source.indexOf("]") + 1, FLAGS);
            ss.setSpan(mStyleSpan, start, source.length(), FLAGS);
            return ss;
        }
    }

    /*客户满意度item*/
    class CustomerSatisfactionItem extends MultipleItem {
        CharSequence satisfaction;  //客户满意度
        Drawable drawableRight;
        CharSequence service;  //服务满意度
        CharSequence delivery; //配送满意度
        CharSequence design; //设计满意度
        CharSequence install; //安装满意度

        CustomerSatisfactionItem() {
            super(CustomerSatisfactionItem.TYPE_CUSTOMER_SATISFACTION);
            String source = mContext.getString(R.string.customer_satisfaction_tips);
            int start = source.length();
            source += CODE_3000 + "4.68" + " ";
            int end = source.length();
            source += "[star]";
            int startEnd = source.length();
            source += CODE_2000 + "@image";
            SpannableString ss = new SpannableString(source);
            ss.setSpan(mStyleSpan, start, end, FLAGS);
            ss.setSpan(new CenterAlignImageSpan(mContext, R.drawable.ic_star), end, startEnd, FLAGS);
            ss.setSpan(new CenterAlignImageSpan(mContext, R.drawable.ic_rise), source.lastIndexOf("@"), source.length(), FLAGS);
            satisfaction = ss;
            drawableRight = ContextCompat.getDrawable(mContext, R.drawable.ic_star_gray);
            service = mTextHelper.obtain(R.string.customer_satisfaction_service2, CODE_3000 + "4.88");
            delivery = mTextHelper.obtain(R.string.customer_satisfaction_delivery2, CODE_3000 + "4.8");
            design = mTextHelper.obtain(R.string.customer_satisfaction_design2, CODE_3000 + "4.35");
            install = mTextHelper.obtain(R.string.customer_satisfaction_install2, CODE_3000 + "4.68");
        }
    }

    /*门店效益item*/
    class ShopBenefitItem extends MultipleItem {
        CharSequence effect; //坪效
        boolean isEffectRise = false;  //坪效是否上升
        CharSequence totalEffect; //总人效
        boolean isTotalEffectRise = true; //总人效是否上升
        CharSequence salesEffect; //销售人效
        boolean isSalesEffectRise = true;

        ShopBenefitItem() {
            super(MultipleItem.TYPE_SHOP_BENEFIT);
            effect = mTextHelper.obtain(R.string.effect, CODE_2000 + "1.56万", mStyleSpan,
                    new ForegroundColorSpan(getColor(isEffectRise)));
            totalEffect = mTextHelper.obtain(R.string.total_human_effect, CODE_2000 + "5.62万", mStyleSpan,
                    new ForegroundColorSpan(getColor(isTotalEffectRise)));
            salesEffect = mTextHelper.obtain(R.string.total_human_effect, CODE_2000 + "9.6万", mStyleSpan,
                    new ForegroundColorSpan(getColor(isSalesEffectRise)));
        }
    }

    /*盈利状况item-延期*/
//    class ProfitabilityItem extends MultipleItem {
//        CharSequence payback; //回款
//        boolean isPaybackRise = false;
//        CharSequence grossProfit; //毛利
//        CharSequence grossProfitRate; //毛利率
//        CharSequence growthLeft; //左边-环比增长
//        CharSequence operatingCost; //运营成本
//        CharSequence netIncome; //净利
//        boolean isNetIncomeRise = true;
//        CharSequence netIncomeRate; //净利率
//        boolean isNetIncomeRateRise = true;
//        CharSequence growthRight; //右边-环比增长
//        boolean isGrowthRightRise = true;
//
//        ProfitabilityItem() {
//            super(MultipleItem.TYPE_PROFITABILITY);
//            payback = mTextHelper.obtain(R.string.payback, CODE_3000 + "89.56万", mStyleSpan,
//                    new ForegroundColorSpan(getColor(isPaybackRise)));
//            grossProfit = mTextHelper.obtain(R.string.gross_profit, CODE_3000 + "26.5万", mStyleSpan);
//            grossProfitRate = mTextHelper.obtain(R.string.gross_profit_rate, CODE_3000 + "76.82%", mStyleSpan);
//            growthLeft = mTextHelper.obtain(R.string.growth_in_the_ring, CODE_3000 + "10.18%", mStyleSpan);
//            operatingCost = mTextHelper.obtain(R.string.operating_cost, CODE_3000 + "35.62万", mStyleSpan);
//            netIncome = mTextHelper.obtain(R.string.net_income, CODE_3000 + "38.68万", mStyleSpan,
//                    new ForegroundColorSpan(getColor(isNetIncomeRise)));
//            netIncomeRate = mTextHelper.obtain(R.string.net_income_rate, CODE_3000 + "78.26%", mStyleSpan,
//                    new ForegroundColorSpan(getColor(isNetIncomeRateRise)));
//            growthRight = mTextHelper.obtain(R.string.growth_in_the_ring, CODE_3000 + "10.18%", mStyleSpan,
//                    new ForegroundColorSpan(getColor(isGrowthRightRise)));
//        }
//    }

    /*经销商排名-数据排名*/
    class DealerRankingOneItem extends MultipleItem {
        CharSequence performance; //业绩
        CharSequence turnoverRate; //进店成交率
        CharSequence effect; //坪效
        CharSequence customerPrice; //客单价
        CharSequence scaleRate; //量尺成交率
        CharSequence humanEffect; //人效

        ForegroundColorSpan colorSpan;
        AbsoluteSizeSpan sizeSpan;
        StyleSpan styleSpan;

        DealerRankingOneItem() {
            super(MultipleItem.TYPE_DEALER_RANKING_ONE);
            colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.textColor26));
            sizeSpan = new AbsoluteSizeSpan(mContext.getResources().getDimensionPixelSize(R.dimen.textSize_15), false);
            styleSpan = new StyleSpan(Typeface.BOLD);
            performance = getRanking(R.string.performance_ranking, "10/100");
            turnoverRate = getRanking(R.string.entry_store_rate_ranking, "15/100");
            effect = getRanking(R.string.effect_ranking, "13/100");
            customerPrice = getRanking(R.string.customer_price_ranking, "20/100");
            scaleRate = getRanking(R.string.scale_turnover_rate_ranking, "09/100");
            humanEffect = getRanking(R.string.human_effect_ranking, "12/100");
        }

        SpannableString getRanking(int originId, String value) {
            String source = mContext.getString(originId) + "\n";
            int start = source.length();
            source += (TextUtils.isEmpty(value) ? "" : value);
            int end = source.length();
            SpannableString ss = new SpannableString(source);
            int flags = FLAGS;
            ss.setSpan(colorSpan, start, end, flags);
            ss.setSpan(sizeSpan, start, end, flags);
            ss.setSpan(styleSpan, start, end, flags);
            return ss;
        }
    }

    private final class MultipleItemAdapter extends CommonAdapter<MultipleItem> {

        MultipleItemAdapter(Context context, List<MultipleItem> dataList) {
            super(context, dataList);
        }

        @Override
        public int getItemViewType(int position) {
            return mDatas.get(position).getItemType();
        }

        @Override
        protected int bindView(int viewType) {
            if (viewType == MultipleItem.TYPE_PERFORMANCE_RATIO_TITLE ||
                    viewType == MultipleItem.TYPE_COLOR_PERFORMANCE_TITLE ||
                    viewType == MultipleItem.TYPE_SERIES_PERFORMANCE_TITLE ||
                    viewType == MultipleItem.TYPE_CUSTOMER_SOURCE_TITLE ||
                    viewType == MultipleItem.TYPE_CUSTOMER_ANALYSIS_TITLE ||
                    viewType == MultipleItem.TYPE_CUSTOMER_SATISFACTION_TITLE ||
                    viewType == MultipleItem.TYPE_SHOP_BENEFIT_TITLE ||
                    viewType == MultipleItem.TYPE_PROFITABILITY_TITLE ||
                    viewType == MultipleItem.TYPE_DEALER_RANKING_TITLE) {
                return R.layout.item_business_reference_main_title;
            } else {
                if (viewType == MultipleItem.TYPE_PERFORMANCE_RATIO) {
                    return R.layout.item_business_reference_performanceratio;
                } else if (viewType == MultipleItem.TYPE_COLOR_PERFORMANCE) {
                    return R.layout.item_business_reference_colorperformance;
                } else if (viewType == MultipleItem.TYPE_SERIES_PERFORMANCE) {
                    return R.layout.item_business_reference_seriesperformance;
                } else if (viewType == MultipleItem.TYPE_CUSTOMER_SOURCE) {
                    return R.layout.item_business_reference_customer_source;
                } else if (viewType == MultipleItem.TYPE_CUSTOMER_ANALYSIS) {
                    return R.layout.item_business_reference_customeranalysis;
                } else if (viewType == MultipleItem.TYPE_CUSTOMER_SATISFACTION) {
                    return R.layout.item_business_reference_customersatisfaction;
                } else if (viewType == MultipleItem.TYPE_SHOP_BENEFIT) {
                    return R.layout.item_business_reference_shopbenefit;
                } else if (viewType == MultipleItem.TYPE_PROFITABILITY) {
                    return R.layout.item_business_reference_profitability;
                } else if (viewType == MultipleItem.TYPE_DEALER_RANKING_ONE) {
                    return R.layout.item_business_reference_dealerranking_one;
                } else if (viewType == MultipleItem.TYPE_DEALER_RANKING_TWO) {
                    return R.layout.item_business_reference_dealerranking_two;
                } else if (viewType == MultipleItem.TYPE_DEALER_RANKING_THREE) {
                    return R.layout.item_business_reference_dealerranking_three;
                } else {
                    return R.layout.item_business_reference_dealerranking_four;
                }
            }
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, MultipleItem multipleItem, int position) {
            int itemType = holder.getItemViewType();
            if (itemType == MultipleItem.TYPE_PERFORMANCE_RATIO_TITLE ||
                    itemType == MultipleItem.TYPE_COLOR_PERFORMANCE_TITLE ||
                    itemType == MultipleItem.TYPE_SERIES_PERFORMANCE_TITLE ||
                    itemType == MultipleItem.TYPE_CUSTOMER_SOURCE_TITLE ||
                    itemType == MultipleItem.TYPE_CUSTOMER_ANALYSIS_TITLE ||
                    itemType == MultipleItem.TYPE_CUSTOMER_SATISFACTION_TITLE ||
                    itemType == MultipleItem.TYPE_SHOP_BENEFIT_TITLE ||
                    itemType == MultipleItem.TYPE_PROFITABILITY_TITLE ||
                    itemType == MultipleItem.TYPE_DEALER_RANKING_TITLE) {
                holder.setText(R.id.tv_title, multipleItem.title);
                holder.setText(R.id.tv_right_menu, multipleItem.menu);
                holder.setEnabled(R.id.tv_right_menu, !TextUtils.isEmpty(multipleItem.menu));
                holder.setOnClickListener(R.id.tv_right_menu, view -> onMenuClick(itemType));
            } else if (itemType == MultipleItem.TYPE_PERFORMANCE_RATIO) {  //业绩占比
                PerformanceRatioItem item = (PerformanceRatioItem) multipleItem;
                setPerformanceRatio(holder, item);
            } else if (itemType == MultipleItem.TYPE_COLOR_PERFORMANCE) { //花色业绩
                ColorPerformanceItem item = (ColorPerformanceItem) multipleItem;
                setColorPerformance(holder, item);
            } else if (itemType == MultipleItem.TYPE_SERIES_PERFORMANCE) { //系列业绩
                SeriesPerformanceItem item = (SeriesPerformanceItem) multipleItem;
                setSeriesPerformance(holder, item);
            } else if (itemType == MultipleItem.TYPE_CUSTOMER_SOURCE) { //客户来源
                CustomerSourceItem item = (CustomerSourceItem) multipleItem;
                setCustomerSource(holder, item);
            } else if (itemType == MultipleItem.TYPE_CUSTOMER_ANALYSIS) { //客户分析
                CustomerAnalysisItem item = (CustomerAnalysisItem) multipleItem;
                setCustomerAnalysis(holder, item);
            } else if (itemType == MultipleItem.TYPE_CUSTOMER_SATISFACTION) { //客户满意度
                CustomerSatisfactionItem item = (CustomerSatisfactionItem) multipleItem;
                setCustomerSatisfaction(holder, item);
            } else if (itemType == MultipleItem.TYPE_SHOP_BENEFIT) { //门店效益
                ShopBenefitItem item = (ShopBenefitItem) multipleItem;
                setShopBenefit(holder, item);
            }
//            else if (itemType == MultipleItem.TYPE_PROFITABILITY) { //盈利状况
//                ProfitabilityItem item = (ProfitabilityItem) multipleItem;
//                setProfitability(holder, item);
//            }
            else if (itemType == MultipleItem.TYPE_DEALER_RANKING_ONE) {  //经销商排名-城市排名
                DealerRankingOneItem item = (DealerRankingOneItem) multipleItem;
                setDealerRankingOne(holder, item);
            } else if (itemType == MultipleItem.TYPE_DEALER_RANKING_TWO) {
                setDealerRankingTwo(holder);
            }
        }

        private void onMenuClick(int itemType) {
            if (itemType == MultipleItem.TYPE_COLOR_PERFORMANCE_TITLE) {  //更多花色业绩
                DealerMultiPerformanceActivity.renderColors((BaseActivity<?, ?>) mContext);
            } else if (itemType == MultipleItem.TYPE_SERIES_PERFORMANCE_TITLE) { //更多系列业绩
                DealerMultiPerformanceActivity.renderSeries((BaseActivity<?, ?>) mContext);
            } else if (itemType == MultipleItem.TYPE_PERFORMANCE_RATIO_TITLE) { // 点击品类的时候，下方会出现详细宅配报表，选择其他维度业绩时，无详细宅配报表出现
                DealerMultiPerformanceActivity.renderHomeDelivery((BaseActivity<?, ?>) mContext);
            } else if (itemType == MultipleItem.TYPE_CUSTOMER_ANALYSIS_TITLE) { //客户分析-数据说明
                new PlainTextTipsDialog(mContext).setData(R.array.business_reference_customer_analysis_tips).show();
            } else if (itemType == MultipleItem.TYPE_SHOP_BENEFIT_TITLE) {  //门店效益-数据说明
                new PlainTextTipsDialog(mContext).setData(R.array.business_reference_shop_benefit_tips).show();
            }
        }

        /*业绩占比*/
        private void setPerformanceRatio(final RecyclerHolder holder, PerformanceRatioItem item) {
            holder.setOnClickListener(R.id.tv_plate, view -> hideHomeDelivery(holder, R.id.tv_plate));
            //点击品类的时候，下方会出现详细宅配报表，选择其他维度业绩时，无详细宅配报表出现
            holder.setOnClickListener(R.id.tv_category, view -> showHomeDelivery(holder));
            holder.setOnClickListener(R.id.tv_space, view -> hideHomeDelivery(holder, R.id.tv_space));
            holder.setOnClickListener(R.id.tv_customer, view -> hideHomeDelivery(holder, R.id.tv_customer));
            holder.setOnClickListener(R.id.tv_shop, view -> hideHomeDelivery(holder, R.id.tv_shop));
            PieChartView pieChart = holder.obtainView(R.id.pieChart);
            List<PieChartData> dataList = new ArrayList<>();
            dataList.add(new PieChartData(88.66f, mColorArray[0]));
            dataList.add(new PieChartData(40.6f, mColorArray[1]));
            dataList.add(new PieChartData(32f, mColorArray[2]));
            dataList.add(new PieChartData(24f, mColorArray[3]));
            dataList.add(new PieChartData(16f, mColorArray[4]));
            dataList.add(new PieChartData(6.74f, mColorArray[5]));
            pieChart.setPieChartDataList(dataList);
            holder.setText(R.id.tv_performance, item.performance); //本月业绩
            holder.setText(R.id.tv_yoy_growth, item.yearOnYear);   //同比增长
            holder.setText(R.id.tv_ring_growth, item.ringGrowth);   //环比增长
            setMultiPerformance(holder, item, false);
        }

        private void showHomeDelivery(RecyclerHolder holder) {
            setSelected(holder, R.id.tv_category);
            for (MultipleItem item : mDatas) {
                if (item.itemType == MultipleItem.TYPE_PERFORMANCE_RATIO_TITLE) {
                    item.menu = mContext.getString(R.string.more_homedelivery_arrow);
                    break;
                }
            }
            notifyDataSetChanged();
        }

        private void hideHomeDelivery(RecyclerHolder holder, int selectId) {
            setSelected(holder, selectId);
            for (MultipleItem item : mDatas) {
                if (item.itemType == MultipleItem.TYPE_PERFORMANCE_RATIO_TITLE) {
                    item.menu = "";
                    break;
                }
            }
            notifyDataSetChanged();
        }

        /*花色业绩*/
        private void setColorPerformance(RecyclerHolder holder, ColorPerformanceItem item) {
            holder.setOnClickListener(R.id.tv_custom, view -> setSelected(holder, R.id.tv_custom));  //选中“定制”
            holder.setOnClickListener(R.id.tv_cupboard, view -> setSelected(holder, R.id.tv_cupboard)); //选中“橱柜”
            holder.setOnClickListener(R.id.tv_wooden_door, view -> setSelected(holder, R.id.tv_wooden_door)); //选中“木门”
            holder.setText(R.id.tv_performance, item.performance);  //TOP5业绩
            PieChartView pieChart = holder.obtainView(R.id.pieChart);
            List<PieChartData> dataList = new ArrayList<>();
            dataList.add(new PieChartData(37.98f, mColorArray[0]));
            dataList.add(new PieChartData(25.56f, mColorArray[1]));
            dataList.add(new PieChartData(10f, mColorArray[2]));
            dataList.add(new PieChartData(9.33f, mColorArray[3]));
            dataList.add(new PieChartData(9f, mColorArray[4]));
            dataList.add(new PieChartData(8.13f, mColorArray[5]));
            pieChart.setPieChartDataList(dataList);
            setMultiPerformance(holder, item, true);
        }

        /*系列业绩*/
        private void setSeriesPerformance(RecyclerHolder holder, SeriesPerformanceItem item) {
            holder.setOnClickListener(R.id.tv_custom, view -> setSelected(holder, R.id.tv_custom));  //选中“定制”
            holder.setOnClickListener(R.id.tv_cupboard, view -> setSelected(holder, R.id.tv_cupboard)); //选中“橱柜”
            holder.setOnClickListener(R.id.tv_wooden_door, view -> setSelected(holder, R.id.tv_wooden_door)); //选中“木门”
            holder.setText(R.id.tv_performance, item.performance);  //TOP5业绩
            PieChartView pieChart = holder.obtainView(R.id.pieChart);
            List<PieChartData> dataList = new ArrayList<>();
            dataList.add(new PieChartData(37.98f, mColorArray[0]));
            dataList.add(new PieChartData(25.56f, mColorArray[1]));
            dataList.add(new PieChartData(10f, mColorArray[2]));
            dataList.add(new PieChartData(9.33f, mColorArray[3]));
            dataList.add(new PieChartData(9f, mColorArray[4]));
            dataList.add(new PieChartData(8.13f, mColorArray[5]));
            pieChart.setPieChartDataList(dataList);
            setMultiPerformance(holder, item, true);
        }

        /*花色、系列通用*/
        private void setMultiPerformance(RecyclerHolder holder, MultiPerformanceItem item, boolean showIcon) {
            int visibility = showIcon ? View.VISIBLE : View.GONE;
            //first
            holder.setVisibility(R.id.iv_the_first, visibility);
            holder.setText(R.id.tv_the_first, item.theFirst);
            holder.setDrawableLeft(R.id.tv_the_first, getColorDrawable(0));
            //second
            holder.setVisibility(R.id.iv_the_second, visibility);
            holder.setText(R.id.tv_the_second, item.theSecond);
            holder.setDrawableLeft(R.id.tv_the_second, getColorDrawable(1));
            //third
            holder.setVisibility(R.id.iv_third, visibility);
            holder.setText(R.id.tv_third, item.theThird);
            holder.setDrawableLeft(R.id.tv_third, getColorDrawable(2));
            //fourth
            holder.setVisibility(R.id.tv_ranking_four, visibility);
            holder.setText(R.id.tv_ranking_four, "4");
            holder.setText(R.id.tv_fourth, item.theFourth);
            holder.setDrawableLeft(R.id.tv_fourth, getColorDrawable(3));
            //fifth
            holder.setVisibility(R.id.tv_ranking_five, visibility);
            holder.setText(R.id.tv_ranking_five, "5");
            holder.setText(R.id.tv_fifth, item.theFifth);
            holder.setDrawableLeft(R.id.tv_fifth, getColorDrawable(4));
            //other
            holder.setText(R.id.tv_other, item.theOther);
            holder.setDrawableLeft(R.id.tv_other, getColorDrawable(5));
        }

        /*客户来源*/
        private void setCustomerSource(RecyclerHolder holder, CustomerSourceItem item) {
            holder.setOnClickListener(R.id.tv_tab_one, view -> setSelected(holder, R.id.tv_tab_one));
            holder.setOnClickListener(R.id.tv_tab_two, view -> setSelected(holder, R.id.tv_tab_two));
            PieChartView pieChart = holder.obtainView(R.id.pieChart);
            List<PieChartData> dataList = new ArrayList<>();
            dataList.add(new PieChartData(13.56f, mColorArray[0]));
            dataList.add(new PieChartData(11.03f, mColorArray[1]));
            dataList.add(new PieChartData(9.35f, mColorArray[2]));
            dataList.add(new PieChartData(10.03f, mColorArray[3]));
            dataList.add(new PieChartData(9f, mColorArray[4]));
            pieChart.setPieChartDataList(dataList);
            holder.setText(R.id.tv_performance, item.performance); //新建客户数
            holder.setText(R.id.tv_yoy_growth, item.yearOnYear);   //同比增长
            holder.setText(R.id.tv_ring_growth, item.ringGrowth);   //环比增长
            setMultiPerformance(holder, item, false);
        }

        /*客户分析*/
        private void setCustomerAnalysis(RecyclerHolder holder, CustomerAnalysisItem item) {
            holder.setText(R.id.tv_customer_price, item.customerPrice); //客单价
            holder.setDrawableRight(R.id.tv_customer_price, ContextCompat.getDrawable(mContext, R.drawable.ic_decline));
            holder.setText(R.id.tv_period, item.period); //平均成交周期
            holder.setDrawableRight(R.id.tv_period, ContextCompat.getDrawable(mContext, R.drawable.ic_rise));
            holder.setText(R.id.tv_sign_order, item.signOrder);
            holder.setText(R.id.tv_order_install, item.orderInstall);
        }

        /*客户满意度*/
        private void setCustomerSatisfaction(RecyclerHolder holder, CustomerSatisfactionItem item) {
            holder.setText(R.id.tv_satisfaction, item.satisfaction);
            holder.setText(R.id.tv_service_satisfaction, item.service);
            holder.setDrawableRight(R.id.tv_service_satisfaction, item.drawableRight);
            holder.setText(R.id.tv_delivery_satisfaction, item.delivery);
            holder.setDrawableRight(R.id.tv_delivery_satisfaction, item.drawableRight);
            holder.setText(R.id.tv_design_satisfaction, item.design);
            holder.setDrawableRight(R.id.tv_design_satisfaction, item.drawableRight);
            holder.setText(R.id.tv_install_satisfaction, item.install);
            holder.setDrawableRight(R.id.tv_install_satisfaction, item.drawableRight);
        }

        /*门店效益*/
        private void setShopBenefit(RecyclerHolder holder, ShopBenefitItem item) {
            holder.setText(R.id.tv_effect, item.effect);
            holder.setDrawableRight(R.id.tv_effect, item.getDrawable(item.isEffectRise));
            holder.setText(R.id.tv_total_human_effect, item.totalEffect);
            holder.setDrawableRight(R.id.tv_total_human_effect, item.getDrawable(item.isTotalEffectRise));
            holder.setText(R.id.tv_sales_effectiveness, item.salesEffect);
            holder.setDrawableRight(R.id.tv_sales_effectiveness, item.getDrawable(item.isSalesEffectRise));
        }

        /*盈利状况*/
//        private void setProfitability(RecyclerHolder holder, ProfitabilityItem item) {
//            holder.setText(R.id.tv_payback, item.payback);
//            holder.setDrawableRight(R.id.tv_payback, item.getDrawable(item.isPaybackRise));
//            holder.setText(R.id.tv_gross_profit, item.grossProfit);
//            holder.setText(R.id.tv_gross_profit_rate, item.grossProfitRate);
//            holder.setText(R.id.tv_growth_left, item.growthLeft);
//            holder.setText(R.id.tv_operating_cost, item.operatingCost);
//            holder.setText(R.id.tv_net_income, item.netIncome);
//            holder.setDrawableRight(R.id.tv_net_income, item.getDrawable(item.isNetIncomeRise));
//            holder.setText(R.id.tv_net_income_rate, item.netIncomeRate);
//            holder.setDrawableRight(R.id.tv_net_income_rate, item.getDrawable(item.isNetIncomeRateRise));
//            holder.setText(R.id.tv_growth_right, item.growthRight);
//            holder.setDrawableRight(R.id.tv_growth_right, item.getDrawable(item.isGrowthRightRise));
//        }

        /*经销商排名-排名数据*/
        private void setDealerRankingOne(RecyclerHolder holder, DealerRankingOneItem item) {
            holder.setText(R.id.tv_performance_ranking, item.performance); //业绩排名
            holder.setText(R.id.tv_entry_turnover_rate_rinking, item.turnoverRate); //进店成交率排名
            holder.setText(R.id.tv_effect_ranking, item.effect); //坪效排名
            holder.setText(R.id.tv_customer_price_ranking, item.customerPrice); //客单价排名
            holder.setText(R.id.tv_scale_turnover_rate_rinking, item.scaleRate); //量尺成交率排名
            holder.setText(R.id.tv_human_effect_ranking, item.humanEffect); //人效排名
        }

        /*经销商排名-折线图*/
        private void setDealerRankingTwo(RecyclerHolder holder) {
            LineChartView lineChartView = holder.obtainView(R.id.lineChartView);
            List<LineChartData> dataList = new ArrayList<>();
            dataList.add(new LineChartData("", 34, "排名：" + 34));
            dataList.add(new LineChartData("", 5, "排名：" + 5));
            dataList.add(new LineChartData("", 17, "排名：" + 17));
            dataList.add(new LineChartData("", 14, "排名：" + 14));
            dataList.add(new LineChartData("", 22, "排名：" + 22));
            dataList.add(new LineChartData("", 20, "排名：" + 20));
            dataList.add(new LineChartData("", 24, "排名：" + 24));
            dataList.add(new LineChartData("", 10, "排名：" + 10));
            lineChartView.setData(dataList);
        }

        /*点击后选中处理*/
        private void setSelected(RecyclerHolder holder, int selectViewId) {
            holder.obtainView(selectViewId).setSelected(true);
            LinearLayout llTab = holder.obtainView(R.id.ll_tab);
            for (int i = 0; i < llTab.getChildCount(); i++) {
                View view = llTab.getChildAt(i);
                if (view instanceof TextView && view.getId() != selectViewId) {
                    view.setSelected(false);
                }
            }
        }
    }

    private Drawable getColorDrawable(int position) {
        if (position < 0 || position >= mColorArray.length) return null;
        GradientDrawable drawable = (GradientDrawable) ContextCompat.getDrawable(mContext, R.drawable.bg_corners2dp_multiple);
        if (drawable != null) {
            drawable.setColor(mColorArray[position]);
        }
        return drawable;
    }
}
