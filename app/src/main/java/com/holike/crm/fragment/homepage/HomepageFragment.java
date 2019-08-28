package com.holike.crm.fragment.homepage;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.activity.analyze.OrderReportActivity;
import com.holike.crm.activity.analyze.PerformanceActivity;
import com.holike.crm.activity.credit.CreditInquiryActivity;
import com.holike.crm.activity.customer.CustomerDetailV2Activity;
import com.holike.crm.activity.homepage.AddCustomerActivity;
import com.holike.crm.activity.homepage.CollectDepositActivity;
import com.holike.crm.activity.homepage.FeedbackRecordActivity;
import com.holike.crm.activity.homepage.MessageV2Activity;
import com.holike.crm.activity.homepage.ReceivingScanActivity;
import com.holike.crm.activity.message.MessageDetailsActivity;
import com.holike.crm.activity.mine.FeedbackActivity;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.base.BaseFragment;
import com.holike.crm.base.IntentValue;
import com.holike.crm.bean.HomepageBean;
import com.holike.crm.customView.CircleProgressBar;
import com.holike.crm.http.MyJsonParser;
import com.holike.crm.imageloader.MyBannerImageLoader;
import com.holike.crm.local.MainDataSource;
import com.holike.crm.presenter.fragment.HomePagePresenter;
import com.holike.crm.util.Constants;
import com.holike.crm.util.DensityUtil;
import com.holike.crm.util.SharedPreferencesUtils;
import com.holike.crm.view.fragment.HomePageView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.umeng.analytics.MobclickAgent;
import com.xcode.banner.widget.Banner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wqj on 2018/2/24.
 * 首页
 */
@Deprecated
public class HomepageFragment extends BaseFragment<HomePagePresenter, HomePageView> implements HomePageView {
    @BindView(R.id.ll_homepage_main)
    LinearLayout llMain;
    @BindView(R.id.banner_homepage)
    Banner banner;
    @BindView(R.id.vs_homepage_collect_deposit)
    ViewStub vsCollectDeposit;
    @BindView(R.id.srl_homepage)
    SmartRefreshLayout srl;
    @BindView(R.id.sl_homepage_rv)
    FrameLayout slRv;
    @BindView(R.id.rv_homepage)
    RecyclerView rv;
    @BindView(R.id.rv_homepage_message)
    RecyclerView rvMessage;
    @BindView(R.id.rv_pay_menu)
    RecyclerView rvPayMenu;
    @BindView(R.id.mMenuProgressLayout)
    FrameLayout mMenuProgressLayout;
    @BindView(R.id.mProgressView)
    View mProgressView;
    //    @BindView(R.id.tv_homepage_msg)
//    TextView tvMsg;
    @BindView(R.id.tv_homepage_new)
    TextView tvNew;
    @BindView(R.id.iv_home_red_point_msg)
    ImageView ivRedPoint;
    @BindView(R.id.ll_homepage_new)
    LinearLayout llNew;
    @BindView(R.id.tv_homepage_month_data_date)
    TextView tvMonthDataDate;
    //    @BindView(R.id.ll_homepage_month_data)
//    LinearLayout llMonthData;
    @BindView(R.id.fl_homepage_month_data)
    FrameLayout flMonthData;
    @BindView(R.id.fl_new_msg)
    FrameLayout flNewMsg;
    @BindView(R.id.vs_homepage_month_data_installer)
    ViewStub vsMonthDateInstaller;
    @BindView(R.id.vs_homepage_month_data_dealer)
    ViewStub vsMonthDateDealer;
    @BindView(R.id.vs_homepage_month_data_salesman)
    ViewStub vsMonthDateSalesman;

    private HomepageBean homepageBean;
    private boolean isBoss;
    private boolean isInflateCollectDeposit, isInflateInstaller, isInflateDealer, isInflateSalesman;
    private View viewCollectDeposit, viewInstaller, viewDealer, viewSalesman;

    @Override
    protected HomePagePresenter attachPresenter() {
        return new HomePagePresenter();
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_homepage;
    }

    @Override
    protected void init() {
        rv.setLayoutManager(new GridLayoutManager(mContext, 4));
        rv.setNestedScrollingEnabled(false);
//        mBanner.setIndicatorGravity(BannerConfig.RIGHT);
//        mBanner.setImageLoader(new BannerImageLoader());
//        srl.setRefreshHeader(new WaterDropHeader(mContext));
        srl.setOnRefreshListener(refreshLayout -> initData(false));
        MainDataSource.clearShopData(mContext);
        MainDataSource.clearAuthInfo(mContext);
        initData(true);
    }

    private void initData(boolean showLoading) {
        if (showLoading) showLoading();
        mPresenter.getHomepageData();
    }

    @Override
    protected void reload() {
        super.reload();
        initData(true);
    }

    @Override
    public void onResume() {
        super.onResume();
//        tvMsg.setText(HomePagePresenter.getMsgNum());
        ivRedPoint.setVisibility(HomePagePresenter.isNewMsg() ? View.VISIBLE : View.GONE);
    }

    @OnClick({R.id.ll_homepage_new, R.id.tv_homepage_msg, R.id.fl_new_msg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_homepage_new:
                MobclickAgent.onEvent(mContext, "homepage_latest_announce");
                if (homepageBean != null && !TextUtils.isEmpty(homepageBean.getMessageId())) {
                    Intent intent = new Intent(mContext, MessageDetailsActivity.class);
                    intent.putExtra(Constants.MESSAGE_ID, homepageBean.getMessageId());
                    startActivityForResult(intent, REQUEST_CODE);
                }
                break;
            case R.id.tv_homepage_msg:
            case R.id.fl_new_msg:
                startActivity(MessageV2Activity.class);
                break;
        }
    }

    @Override
    public void getHomepageDataSuccess(HomepageBean bean) {
        srl.finishRefresh();
        dismissLoading();
        homepageBean = bean;
        if (bean.getOrderData() != null)
            SharedPreferencesUtils.saveString(Constants.COOKIE, bean.getOrderData().getCookie());
//        tvMsg.setText(HomePagePresenter.getMsgNum(bean.getNewData().getNewsCount()));
        if (bean.getNewData() != null)
            HomePagePresenter.setMsgNum(bean.getNewData().getNewsCount());
        ivRedPoint.setVisibility(HomePagePresenter.isNewMsg() ? View.VISIBLE : View.GONE);
        llMain.setVisibility(View.VISIBLE);
        flNewMsg.setVisibility(View.GONE);
        tvNew.setText(homepageBean.getMessageContent());
        HomepageBean.NewDataBean newDataBean = bean.getNewData();
        if (newDataBean != null) {
            String userType = newDataBean.getUsrType();
            if (TextUtils.equals(userType, "0")) {
                noPermissions(bean.getMessageList());
            } else if (TextUtils.equals(userType, "1")) {
                HomepageBean.OrderDataBean orderDataBean = bean.getOrderData();
                if (orderDataBean != null) salesman(orderDataBean);
            } else if (TextUtils.equals(userType, "2")) {
                installer(newDataBean);
            } else if (TextUtils.equals(userType, "3")) {
                stores(newDataBean, true);
            } else if (TextUtils.equals(userType, "4")) {
                isBoss = true;
                stores(newDataBean, false);
            }
        }
        if (newDataBean != null) {
            if (newDataBean.getCreditItem() == null || newDataBean.getCreditItem().isEmpty())
                return;
            List<HomepageBean.NewDataBean.CreditItem> menuItems = new ArrayList<>();
            if (TextUtils.equals(newDataBean.getCreate(), "0")) {   //是否显示在线申报按钮 1显示 0隐藏
                for (HomepageBean.NewDataBean.CreditItem item : newDataBean.getCreditItem()) {
                    if (!TextUtils.equals(item.getType(), "3")) {   //移除在线申报
                        menuItems.add(item);
                    }
                }
            } else {
                menuItems.addAll(newDataBean.getCreditItem());
            }
            int size = menuItems.size();
            rvPayMenu.setNestedScrollingEnabled(false);
            if (size > 4) {
                int totalWidth = mContext.getResources().getDimensionPixelSize(R.dimen.dp_50);
                LinearLayout.LayoutParams llParams = (LinearLayout.LayoutParams) mMenuProgressLayout.getLayoutParams();
                llParams.width = totalWidth;
                mMenuProgressLayout.setLayoutParams(llParams);
                mMenuProgressLayout.setVisibility(View.VISIBLE);
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mProgressView.getLayoutParams();
                params.width = (int) (totalWidth * ((float) 4 / 5));
                mProgressView.setLayoutParams(params);
                int canScrollX = totalWidth - params.width;
                rvPayMenu.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    }

                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                        if (layoutManager != null) {
                            int position = layoutManager.findLastCompletelyVisibleItemPosition();
                            if (position >= 0 && position < size) {
                                if (position == size - 1) {
                                    mProgressView.setTranslationX(canScrollX);
                                } else {
                                    int pos = layoutManager.findFirstCompletelyVisibleItemPosition();
                                    if (pos == 0) {
                                        mProgressView.setTranslationX(0);
                                    } else {
                                        float x = (float) position / size;
                                        mProgressView.setTranslationX(canScrollX * x);
                                    }
                                }
                            }
                        }
                    }
                });
            } else {
                mMenuProgressLayout.setVisibility(View.GONE);
            }
            rvPayMenu.setAdapter(new CommonAdapter<HomepageBean.NewDataBean.CreditItem>(mContext, menuItems) {

                @Override
                protected int bindView(int viewType) {
                    return R.layout.item_rv_homepage_operate;
                }

                @Override
                public void onBindHolder(RecyclerHolder holder, HomepageBean.NewDataBean.CreditItem data, int position) {
                    RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
                    params.width = ((DensityUtil.getScreenWidth(mContext) - (int) (2 * mContext.getResources().getDimension(R.dimen.dp_12))) / 4);
                    holder.itemView.setLayoutParams(params);
                    holder.setVisibility(R.id.tv_item_rv_homepage_operate_num, false);
                    holder.setText(R.id.tv_item_rv_homepage_operate_name, data.getName());
                    Glide.with(mContext).load(data.getIcon()).into((ImageView) holder.obtainView(R.id.iv_item_rv_homepage_operate_icon));
                    holder.itemView.setOnClickListener(v -> {
                        Bundle bundle = new Bundle();
                        bundle.putString(Constants.CREDIT_TYPE, data.getType());
                        startActivity(CreditInquiryActivity.class, bundle);
                    });
                }
            });
        }
        /*店铺数据*/
        if (bean.getTypeList() != null  && !bean.getTypeList().getShopData().isEmpty()) {
            MainDataSource.saveShopData(mContext, MyJsonParser.fromBeanToJson(bean.getTypeList().getShopData()));
        }
        if (bean.getAuthInfo() != null && !bean.getAuthInfo().isEmpty()) {
            MainDataSource.saveAuthInfo(mContext, bean.getAuthInfo());
        }
        IntentValue.getInstance().setHomeBean(homepageBean);
    }

    /**
     * 获取首页数据失败
     */
    @Override
    public void getHomepageDataFailed(String failed) {
        dismissLoading();
        dealWithFailed(failed, true);
        llMain.setVisibility(View.INVISIBLE);
    }

    /**
     * 老板/导购/设计师
     *
     * @param isDesigner 是否是设计师
     */
    @Override
    public void stores(HomepageBean.NewDataBean newDataBean, boolean isDesigner) {
        tvMonthDataDate.setText(newDataBean.getTime());
        slRv.setVisibility(View.VISIBLE);
        showOperateList(newDataBean.getItemList());
        if (!isInflateCollectDeposit) {
            isInflateCollectDeposit = true;
            viewCollectDeposit = vsCollectDeposit.inflate();
        }
        if (!isInflateDealer) {
            isInflateDealer = true;
            viewDealer = vsMonthDateDealer.inflate();
        }
        setOrderCountText(newDataBean.getOrderCount());
        viewCollectDeposit.findViewById(R.id.btn_homepage_collect_deposit).setOnClickListener(v -> {
            MobclickAgent.onEvent(mContext, "homepage_receive_deposit");
            startActivity(CollectDepositActivity.class);
        });
        TextView tvCollected = viewDealer.findViewById(R.id.tv_homepage_month_data_date_dealer_collected);
        TextView tvIntention = viewDealer.findViewById(R.id.tv_homepage_month_data_date_dealer_intention);
        TextView tvMeasure = viewDealer.findViewById(R.id.tv_homepage_month_data_date_dealer_measure);
        TextView tvImg = viewDealer.findViewById(R.id.tv_homepage_month_data_date_dealer_img);
        TextView tvSigning = viewDealer.findViewById(R.id.tv_homepage_month_data_date_dealer_signing);
        tvCollected.setText(newDataBean.getGetMoney());
        tvIntention.setText(newDataBean.getLikeCount());
        tvMeasure.setText(newDataBean.getScaleCount());
        tvImg.setText(newDataBean.getPicCount());
        tvSigning.setText(newDataBean.getContractCount());
        if (isDesigner) {
            LinearLayout llCustomer = viewDealer.findViewById(R.id.ll_homepage_month_data_date_dealer_install_customer);
            LinearLayout llSquare = viewDealer.findViewById(R.id.ll_homepage_month_data_date_dealer_install_square);
            llCustomer.setVisibility(View.GONE);
            llSquare.setVisibility(View.GONE);
        } else {
            TextView tvCustomer = viewDealer.findViewById(R.id.tv_homepage_month_data_date_dealer_install_customer);
            TextView tvSquare = viewDealer.findViewById(R.id.tv_homepage_month_data_date_dealer_install_square);
            tvCustomer.setText(newDataBean.getInstalled());
            tvSquare.setText(newDataBean.getArea());
        }
    }

    private void setOrderCountText(String orderCount) {
        TextView tvOrderNum = viewCollectDeposit.findViewById(R.id.tv_homepage_month_order);
        String source = mContext.getString(R.string.orders_for_this_month);
        int start = source.length();
        if (!TextUtils.isEmpty(orderCount)) {
            source += orderCount;
        }
        int end = source.length();
        source += mContext.getString(R.string.bill);
        SpannableString ss = new SpannableString(source);
        ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.textColor13)), start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new AbsoluteSizeSpan(mContext.getResources().getDimensionPixelSize(R.dimen.textSize_20), false), start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvOrderNum.setText(ss);
    }

    /**
     * 营销人员
     */
    @Override
    public void salesman(HomepageBean.OrderDataBean orderDataBean) {
        tvMonthDataDate.setText(orderDataBean.getTime());
        startBanner(homepageBean.getMainPicture());
        if (!isInflateSalesman) {
            isInflateSalesman = true;
            viewSalesman = vsMonthDateSalesman.inflate();
        }
        CircleProgressBar pbMonthDataMoney = viewSalesman.findViewById(R.id.pb_homepage_month_data_money);
        CircleProgressBar pbMonthDataOrder = viewSalesman.findViewById(R.id.pb_homepage_month_data_order);
        String targetMoney = mContext.getString(R.string.homepage_month_data_target);
        if (!TextUtils.isEmpty(orderDataBean.getTargetOrderMoney())) {
            targetMoney += orderDataBean.getTargetOrderMoney() + "万";
        }
        ((TextView) viewSalesman.findViewById(R.id.tv_homepage_month_data_target_money)).setText(targetMoney);
        String completed = mContext.getString(R.string.homepage_month_data_completed);
        if (!TextUtils.isEmpty(orderDataBean.getOrderMoney())) {
            completed += orderDataBean.getOrderMoney() + "万";
        }
        ((TextView) viewSalesman.findViewById(R.id.tv_homepage_month_data_target_money_completed)).setText(completed);
        String targetOrder = mContext.getString(R.string.homepage_month_data_target);
        if (!TextUtils.isEmpty(orderDataBean.getTargetOrder())) {
            targetOrder += orderDataBean.getTargetOrder() + "单";
        }
        ((TextView) viewSalesman.findViewById(R.id.tv_homepage_month_data_target_order)).setText(targetOrder);
        String orderCompleted = mContext.getString(R.string.homepage_month_data_completed);
        if (!TextUtils.isEmpty(orderDataBean.getCounts())) {
            orderCompleted += orderDataBean.getCounts() + "单";
        }
        ((TextView) viewSalesman.findViewById(R.id.tv_homepage_month_data_target_order_completed)).setText(orderCompleted);
        String text = TextUtils.isEmpty(orderDataBean.getTimeDescribe()) ? "" + mContext.getString(R.string.homepage_month_data_translate) : orderDataBean.getTimeDescribe() + getString(R.string.homepage_month_data_translate);
        ((TextView) viewSalesman.findViewById(R.id.tv_homepage_month_data_target_order_completion)).setText(text);
        ((TextView) viewSalesman.findViewById(R.id.tv_homepage_month_data_area)).setText(orderDataBean.getArea());
        pbMonthDataMoney.setProgress(orderDataBean.getTargetOrderMoneyPercent());
        pbMonthDataOrder.setProgress(orderDataBean.getTargetOrderPercent());
        pbMonthDataMoney.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.BACK, "home");
            startActivity(PerformanceActivity.class, bundle);
        });
        pbMonthDataOrder.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.BACK, "home");
            startActivity(OrderReportActivity.class, bundle);
        });
    }

    private void startBanner(List<String> pictures) {
        if (pictures == null || pictures.isEmpty()) return;
        banner.setVisibility(View.VISIBLE);
        banner.with(homepageBean.getMainPicture())
                .withImageLoader(new MyBannerImageLoader())
                .startup();
//        mBanner.setImages(homepageBean.getMainPicture());
//        mBanner.start();
    }

    /**
     * 安装师傅
     */
    @Override
    public void installer(HomepageBean.NewDataBean newDataBean) {
        tvMonthDataDate.setText(newDataBean.getTime());
        slRv.setVisibility(View.VISIBLE);
        showOperateList(newDataBean.getItemList());
        if (!isInflateInstaller) {
            isInflateInstaller = true;
            viewInstaller = vsMonthDateInstaller.inflate();
        }
        TextView customerNum = viewInstaller.findViewById(R.id.tv_homepage_month_data_installer_customer_num);
        TextView squareNum = viewInstaller.findViewById(R.id.tv_homepage_month_data_installer_square_num);
        TextView orderNum = viewInstaller.findViewById(R.id.tv_homepage_month_data_installer_order_num);
        customerNum.setText(newDataBean.getInstalled());
        squareNum.setText(newDataBean.getArea());
        orderNum.setText(newDataBean.getAmount());
    }

    /**
     * 没有权限
     */
    @Override
    public void noPermissions(final List<HomepageBean.MessageListBean> listBeans) {
        startBanner(homepageBean.getMainPicture());
        llNew.setVisibility(View.GONE);
        flMonthData.setVisibility(View.GONE);
        flNewMsg.setVisibility(View.VISIBLE);
        rvMessage.setLayoutManager(new LinearLayoutManager(mContext));
        rvMessage.setNestedScrollingEnabled(false);
        rvMessage.setAdapter(new CommonAdapter<HomepageBean.MessageListBean>(mContext, listBeans) {

            @Override
            protected int bindView(int viewType) {
                return R.layout.item_rv_homepage_message;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, HomepageBean.MessageListBean bean, int position) {
                holder.setText(R.id.tv_title, bean.getTitle());
                holder.setText(R.id.tv_date, bean.getTime());
                if (bean.getType() == 2) {
                    holder.setText(R.id.tv_type, mContext.getString(R.string.message_system_message));
                    holder.setBackgroundResource(R.id.tv_type, R.drawable.bg_corners2dp_accent);
                } else {
                    holder.setText(R.id.tv_type, mContext.getString(R.string.message_notify_message));
                    holder.setBackgroundResource(R.id.tv_type, R.drawable.bg_corners2dp_red);
                }
                holder.itemView.setOnClickListener(v -> {
                    if (bean.getType() == 2) {
                        MessageDetailsActivity.open(getFragment(),bean.getMessageId(), REQUEST_CODE);
//                        AnnounceFragment.startMessageDetailsActivity(HomepageFragment.this, bean.getMessageId(), REQUEST_CODE);
                    } else {
                        CustomerDetailV2Activity.open((BaseActivity) mContext, bean.getPersonalId(), bean.getMessageId());
                    }
                });
            }
        });
    }

    /**
     * 显示操作列表
     */
    @Override
    public void showOperateList(List<HomepageBean.NewDataBean.ItemListBean> listBeans) {
        rv.setAdapter(new CommonAdapter<HomepageBean.NewDataBean.ItemListBean>(mContext, listBeans) {
            @Override
            protected int bindView(int viewType) {
                return R.layout.item_rv_homepage_operate;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, HomepageBean.NewDataBean.ItemListBean bean, int position) {
                ImageView ivIcon = holder.obtainView(R.id.iv_item_rv_homepage_operate_icon);
                TextView tvName = holder.obtainView(R.id.tv_item_rv_homepage_operate_name);
                TextView tvNum = holder.obtainView(R.id.tv_item_rv_homepage_operate_num);
                tvName.setText(bean.getName());
                switch (bean.getColor()) {
                    case "1":
                        tvNum.setBackgroundResource(R.drawable.bg_homepage_customer_num_blue);
                        break;
                    case "2":
                        tvNum.setBackgroundResource(R.drawable.bg_homepage_customer_num_orange);
                        break;
                    case "3":
                        tvNum.setBackgroundResource(R.drawable.bg_homepage_customer_num_pink);
                        break;
                }
                tvNum.setVisibility(bean.getCount() == null || bean.getCount().equals("0") ? View.GONE : View.VISIBLE);
                tvNum.setText(bean.getCount());
                Glide.with(mContext).load(bean.getIcon()).into(ivIcon);
                holder.itemView.setOnClickListener(v -> {
                    if (TextUtils.isEmpty(bean.getName())) return;
                    switch (bean.getName()) {
                        case "添加客户":
                            MobclickAgent.onEvent(mContext, "homepage_add_customer");
                            Bundle bundle = new Bundle();
                            bundle.putString(Constants.IS_EARNEST, "0");
                            startActivity(AddCustomerActivity.class, bundle);
                            break;
                        case "意向客户跟进":
                            MobclickAgent.onEvent(mContext, "homepage_intention_customer");
                            mPresenter.customerStateList(mContext, bean.getName(), isBoss);
                            break;
                        case "待量房客户":
                            MobclickAgent.onEvent(mContext, "homepage_unmeasure_customer");
                            mPresenter.customerStateList(mContext, bean.getName(), isBoss);
                            break;
                        case "待出图客户":
                            MobclickAgent.onEvent(mContext, "homepage_unfigure_out_customer");
                            mPresenter.customerStateList(mContext, bean.getName(), isBoss);
                            break;
                        case "待签约客户":
                            MobclickAgent.onEvent(mContext, "homepage_unsign_customer");
                            mPresenter.customerStateList(mContext, bean.getName(), isBoss);
                            break;
                        case "待收全款客户":
                            MobclickAgent.onEvent(mContext, "homepage_unpayment_customer");
                            mPresenter.customerStateList(mContext, bean.getName(), isBoss);
                            break;
                        case "待复尺客户":
                            MobclickAgent.onEvent(mContext, "homepage_unremeasure_customer");
                            mPresenter.customerStateList(mContext, bean.getName(), isBoss);
                            break;
                        case "待下单客户":
                            MobclickAgent.onEvent(mContext, "homepage_unorder_customer");
                            mPresenter.customerStateList(mContext, bean.getName(), isBoss);
                            break;
                        case "待安装客户":
                            MobclickAgent.onEvent(mContext, "homepage_uninstall_customer");
                            mPresenter.customerStateList(mContext, bean.getName(), isBoss);
                            break;
                        case "已安装客户":
                            MobclickAgent.onEvent(mContext, "homepage_installed_customer");
                            mPresenter.customerStateList(mContext, bean.getName(), isBoss);
                            break;
                        case "已流失客户":
                            MobclickAgent.onEvent(mContext, "homepage_loose_customer");
                            mPresenter.customerStateList(mContext, bean.getName(), isBoss);
                            break;
                        case "售后反馈":
                            MobclickAgent.onEvent(mContext, "homepage_feedback");
                            startActivity(FeedbackActivity.class);
                            break;
                        case "反馈记录":
                            MobclickAgent.onEvent(mContext, "homepage_feedback_record");
                            startActivity(FeedbackRecordActivity.class);
                            break;
                        case "收货扫码":
                            MobclickAgent.onEvent(mContext, "homepage_receiving_scan");
                            startActivity(ReceivingScanActivity.class);
                            break;
                    }
                });
            }
        });
    }
}
