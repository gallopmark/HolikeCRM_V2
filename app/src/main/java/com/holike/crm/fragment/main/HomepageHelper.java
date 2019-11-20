package com.holike.crm.fragment.main;

import android.content.Intent;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.DimenRes;
import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.activity.analyze.OrderReportActivity;
import com.holike.crm.activity.analyze.PerformanceActivity;
import com.holike.crm.activity.customer.CustomerChargeDepositActivity;
import com.holike.crm.activity.homepage.ThisMonthDataActivity;
import com.holike.crm.activity.message.MessageDetailsActivity;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.base.IntentValue;
import com.holike.crm.bean.HomepageBean;
import com.holike.crm.customView.CircleProgressBar;
import com.holike.crm.enumeration.UserTypeValue;
import com.holike.crm.fragment.main.child.HomeMenuFragment;
import com.holike.crm.fragment.main.child.HomeOperateFragment;
import com.holike.crm.imageloader.MyBannerImageLoader;
import com.holike.crm.popupwindown.RoleListPopupWindow;
import com.holike.crm.presenter.fragment.HomePagePresenter2;
import com.holike.crm.util.Constants;
import com.holike.crm.util.DensityUtil;
import com.holike.crm.util.SharedPreferencesUtils;
import com.umeng.analytics.MobclickAgent;
import com.xcode.banner.widget.Banner;

import java.util.ArrayList;
import java.util.List;

/*首页帮助类*/
class HomepageHelper implements HomeMenuFragment.OnMenuClickListener, View.OnClickListener {

    private static final String TAG_OPERATE = "operate";
    private static final String TAG_MENU = "menu";
    private BaseActivity<?, ?> mContext;
    private LayoutInflater mLayoutInflater;
    private FragmentManager mFragmentManager;
    private Callback mCallback;
    private HomepageBean mHomepageBean;
    //    private boolean isBoss;
    private LinearLayout mContentLayout;
    private FrameLayout mOperateLayout;
    private LinearLayout mNewsLayout;
    private TextView mNewsContentTextView;
    private FrameLayout mMenuContainer;
    private View mBannerView, mViewCollectDeposit, mMessageView, mMonthDataView;
    private int mCurrentRoleIndex;

    HomepageHelper(BaseActivity<?, ?> context, LinearLayout contentLayout, FragmentManager fragmentManager, Callback callback) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.mContentLayout = contentLayout;
        this.mOperateLayout = mContentLayout.findViewById(R.id.flOperate);
        this.mNewsLayout = mContentLayout.findViewById(R.id.llNewsLayout);
        this.mNewsLayout.setOnClickListener(this);
        this.mNewsContentTextView = mContentLayout.findViewById(R.id.tvNewsContent);
        this.mMenuContainer = mContentLayout.findViewById(R.id.mMenuContainer);
        this.mFragmentManager = fragmentManager;
        this.mCallback = callback;
        setupFragments();
    }

    private void setupFragments() {
        mFragmentManager.beginTransaction().replace(R.id.flOperate, new HomeOperateFragment(), TAG_OPERATE).commitAllowingStateLoss();
        HomeMenuFragment menuFragment = new HomeMenuFragment();
        menuFragment.setOnMenuClickListener(this);
        mFragmentManager.beginTransaction().replace(R.id.mMenuContainer, menuFragment, TAG_MENU).commitAllowingStateLoss();
    }

    void onHttpOk(HomepageBean homepageBean, TextView tvRole) {
        resetStatus();
        this.mHomepageBean = homepageBean;
        if (mViewCollectDeposit != null) {
            mViewCollectDeposit.setVisibility(View.GONE);
        }
        HomepageBean.OrderDataBean orderDataBean = mHomepageBean.getOrderData();
        if (orderDataBean != null) {  //本地保存cookie
            if (!TextUtils.isEmpty(orderDataBean.getCookie())) {
                SharedPreferencesUtils.saveString(Constants.COOKIE, orderDataBean.getCookie());
            }
            if (!TextUtils.isEmpty(orderDataBean.Cookie2)) {
                SharedPreferencesUtils.saveString(Constants.COOKIE2, orderDataBean.Cookie2);
            }
        }
//        tvMsg.setText(HomePagePresenter.getMsgNum(bean.getNewData().getNewsCount()));
        if (this.mHomepageBean.getNewData() != null)
            HomePagePresenter2.setMsgNum(this.mHomepageBean.getNewData().getNewsCount());
        mNewsContentTextView.setText(this.mHomepageBean.getMessageContent());
        tvRole.setVisibility(View.GONE);
        HomepageBean.NewDataBean newDataBean = mHomepageBean.getNewData();
        if (newDataBean != null) {
            List<HomepageBean.NewDataBean.CreditItem> creditItems = obtainMenuList(mHomepageBean.getNewData().getCreditItem(), mHomepageBean.getNewData().getCreate());
            if (TextUtils.equals(newDataBean.usrType, UserTypeValue.NO_PERMISSION)) {  //无权限
                requestNoPermission(creditItems.isEmpty());
            } else if (TextUtils.equals(newDataBean.usrType, UserTypeValue.MARKETING)) {  //营销人员
                requestMarketing();
            } else {
                if (newDataBean.getRoleList().isEmpty()) { //空数据，默认为无权限
                    requestNoPermission(creditItems.isEmpty());
                } else {
                    if (newDataBean.getRoleList().size() > 1) {  //多个角色显示左上角“角色切换”按钮
                        tvRole.setVisibility(View.VISIBLE);
                    } else { //单角色 不显示切换角色按钮
                        tvRole.setVisibility(View.GONE);
                    }
                    setSelectRole(mCurrentRoleIndex, tvRole); //默认选择第一个角色
                }
            }
            updateMenuList(creditItems);
        }
        IntentValue.getInstance().setHomeBean(homepageBean);
//        MainDataSource.saveHomepageBean(mContext, this.mHomepageBean);
    }

    /*切换角色*/
    void switchRole(final TextView tv) {
        if (mHomepageBean == null || mHomepageBean.getNewData() == null || mHomepageBean.getNewData().getRoleList().isEmpty()) {
            return;
        }
        RoleListPopupWindow popupWindow = new RoleListPopupWindow(mContext, mHomepageBean.getNewData().getRoleList());
        popupWindow.setOnRoleSelectedListener((position, bean) -> setSelectRole(position, tv));
        popupWindow.showAtLocation(tv, Gravity.START | Gravity.TOP, mContext.getResources().getDimensionPixelSize(R.dimen.dp_6), mContext.getResources().getDimensionPixelSize(R.dimen.dp_35) + DensityUtil.getStatusHeight(mContext));
    }

    /*切换角色*/
    private void setSelectRole(int position, TextView tvRole) {
        if (mHomepageBean == null || mHomepageBean.getNewData() == null
                || mHomepageBean.getNewData().getRoleList().isEmpty()) {
            return;
        }
        List<HomepageBean.NewDataBean.RoleBean> roleList = mHomepageBean.getNewData().getRoleList();
        if (position < 0 || position > roleList.size()) {
            return;
        }
        mCurrentRoleIndex = position;
        final HomepageBean.NewDataBean.RoleBean roleBean = roleList.get(position);
        tvRole.setText(roleBean.roleName);
        List<HomepageBean.NewDataBean.CreditItem> creditItems = obtainMenuList(mHomepageBean.getNewData().getCreditItem(),
                mHomepageBean.getNewData().getCreate());
        updatePage(roleBean, creditItems.isEmpty());
        if (mMonthDataView != null) {
            mMonthDataView.setOnClickListener(view -> {
                String userType = roleBean.usrType;
                if (TextUtils.equals(userType, UserTypeValue.BOSS_V2) && roleBean.isClick()) {
                    //跳转老板本月数据页面
                    ThisMonthDataActivity.start(mContext, ThisMonthDataActivity.TYPE_BOSS);
                } else if (TextUtils.equals(userType, UserTypeValue.FINANCE)) {
                    //跳转财务本月数据
                    ThisMonthDataActivity.start(mContext, ThisMonthDataActivity.TYPE_FINANCE);
                } else if (TextUtils.equals(userType, UserTypeValue.INSTALL_MANAGER)) {
                    //跳转安装经理本月数据
                    ThisMonthDataActivity.start(mContext, ThisMonthDataActivity.TYPE_INSTALL_MANAGER);
                }
//                else {  //v3.0
//                    if (TextUtils.equals(userType, UserTypeValue.DESIGN_MANAGER)) {
//                        //跳转设计经理本月数据
//                        ThisMonthDataActivity.start(mContext, ThisMonthDataActivity.TYPE_DESIGN_MANAGER);
//                    } else {
//                        Bundle bundle = new Bundle();
//                        bundle.putString("userType", userType);
//                        ThisMonthDataActivity.start(mContext, ThisMonthDataActivity.TYPE_PERSONAL_PERFORMANCE, bundle);
//                    }
//                }
            });
        }
    }

    private void updatePage(HomepageBean.NewDataBean.RoleBean roleBean, boolean isEmptyMenu) {
        /*	1代表营销人员2安装工 3导购设计师 4店长老板 5仓库 6安装工 61安装经理7财务8设计师/经理*/
        /*5仓库 6安装工 61安装经理 7财务 8设计师/设计经理 9导购/业务员 10老板 11售后*/
        String userType = roleBean.usrType;
        if (TextUtils.equals(userType, UserTypeValue.NO_PERMISSION)) {  //无权限
            requestNoPermission(isEmptyMenu);
        } else if (TextUtils.equals(userType, UserTypeValue.MARKETING)) {   //营销人员
            requestMarketing();
        } else if (TextUtils.equals(userType, UserTypeValue.WAREHOUSE)) {  //仓库
            updateOperateList(roleBean.getItemList());
            updateWarehouse(roleBean);
        } else if (TextUtils.equals(userType, UserTypeValue.INSTALLER_V2)) {//安装工
            updateOperateList(roleBean.getItemList());
            updateInstall(roleBean);
        } else if (TextUtils.equals(userType, UserTypeValue.INSTALL_MANAGER)) { //安装经理
            updateOperateList(roleBean.getItemList());
            updateInstall(roleBean);
        } else if (TextUtils.equals(userType, UserTypeValue.FINANCE)) {   //财务
            updateOperateList(roleBean.getItemList());
            updateFinance(roleBean);
        } else if (TextUtils.equals(userType, UserTypeValue.DESIGNER)
                || TextUtils.equals(userType, UserTypeValue.DESIGN_MANAGER)) {    //设计师、设计经理
            updateOperateList(roleBean.getItemList());
            updateDesigner(roleBean);
        } else if (TextUtils.equals(userType, UserTypeValue.GUIDE_SALESMAN_V2)) { //导购/业务
            updateOperateList(roleBean.getItemList());
            updateGuideBusiness(roleBean);
        } else if (TextUtils.equals(userType, UserTypeValue.BOSS_V2)) { //店长老板
//            isBoss = true;
            updateOperateList(roleBean.getItemList());
            updateBoss(roleBean);
        } else if (TextUtils.equals(userType, UserTypeValue.AFTER_SALE)) {  //售后
            updateOperateList(roleBean.getItemList());
            updateAfterSale(roleBean);
        }
    }

    private List<HomepageBean.NewDataBean.CreditItem> obtainMenuList(List<HomepageBean.NewDataBean.CreditItem> originList, String create) {
        List<HomepageBean.NewDataBean.CreditItem> menuItems = new ArrayList<>();
        if (originList == null || originList.isEmpty()) return menuItems;
        if (TextUtils.equals(create, "0")) {   //是否显示在线申报按钮 1显示 0隐藏
            for (HomepageBean.NewDataBean.CreditItem item : originList) {
                if (!TextUtils.equals(item.getType(), "3")) {   //移除在线申报
                    menuItems.add(item);
                }
            }
        } else {
            menuItems.addAll(originList);
        }
        return menuItems;
    }

    /*恢复最原始状态*/
    private void resetStatus() {
        mOperateLayout.setVisibility(View.GONE);
        mMenuContainer.setVisibility(View.GONE);
        mNewsLayout.setVisibility(View.VISIBLE);
        mNewsLayout.setBackgroundResource(R.color.color_while);
        mMenuContainer.setBackgroundResource(R.color.color_while);
        setNewsLayoutTopMargin(R.dimen.dp_4);
        setMenuLayoutTopMargin(R.dimen.dp_4);
        removeViewsDynamic();
    }

    /*最新公告布局 topMargin*/
    private void setNewsLayoutTopMargin(@DimenRes int dp) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mNewsLayout.getLayoutParams();
        params.topMargin = mContext.getResources().getDimensionPixelSize(dp);
    }

    private void setMenuLayoutTopMargin(@DimenRes int dp) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mMenuContainer.getLayoutParams();
        params.topMargin = mContext.getResources().getDimensionPixelSize(dp);
    }

    /*移除动态添加的view(banner、消息列表view、本月数据view)*/
    private void removeViewsDynamic() {
        clearDepositView();
        removeBanner();
        removeMessageView();
        removeMonthDataView();
    }

    /*移除banner*/
    private void removeBanner() {
        if (mBannerView != null) {
            mContentLayout.removeView(mBannerView);
            mBannerView = null;
        }
    }

    /*更新首页轮播图 banner动态添加*/
    private void addBanner(List<String> pictures) {
        if (pictures == null || pictures.isEmpty()) return;
        removeBanner();
        mBannerView = LayoutInflater.from(mContext).inflate(R.layout.include_homepage_banner, mContentLayout, false);
        Banner banner = mBannerView.findViewById(R.id.banner);
        banner.with(pictures)
                .withImageLoader(new MyBannerImageLoader())
                .startup();
        mContentLayout.addView(mBannerView, 0);
    }

    /*更新操作列表*/
    private void updateOperateList(List<HomepageBean.NewDataBean.ItemListBean> listBeans) {
        if (listBeans == null || listBeans.isEmpty()) {
            mOperateLayout.setVisibility(View.GONE);
            setNewsLayoutTopMargin(R.dimen.dp_10);
        } else {
            if (mOperateLayout.getVisibility() != View.VISIBLE) {
                mOperateLayout.setVisibility(View.VISIBLE);
            }
            Fragment fragment = mFragmentManager.findFragmentByTag(TAG_OPERATE);
            if (fragment != null) {
                ((HomeOperateFragment) fragment).update(listBeans);
            }
        }
    }

    /*更新菜单列表*/
    private void updateMenuList(List<HomepageBean.NewDataBean.CreditItem> items) {
        if (items == null || items.isEmpty()) {
            mMenuContainer.setVisibility(View.GONE);
        } else {
            if (mMenuContainer.getVisibility() != View.VISIBLE) {
                mMenuContainer.setVisibility(View.VISIBLE);
            }
            Fragment fragment = mFragmentManager.findFragmentByTag(TAG_MENU);
            if (fragment != null) {
                ((HomeMenuFragment) fragment).update(items);
            }
        }
    }

    /*移除消息列表view*/
    private void removeMessageView() {
        if (mMessageView != null) {
            mContentLayout.removeView(mMessageView);
            mMessageView = null;
        }
    }

    /*无权限人员，显示消息列表*/
    private void addMessageView(List<HomepageBean.MessageListBean> messageList, boolean isEmptyMenu) {
        removeMessageView();
        mMessageView = mLayoutInflater.inflate(R.layout.include_homepage_message, mContentLayout, false);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mMessageView.getLayoutParams();
        if (isEmptyMenu) {
            params.topMargin = mContext.getResources().getDimensionPixelSize(R.dimen.dp_10);
            mMessageView.setBackgroundResource(R.drawable.bg_corners_white_5dp);
        } else {
            mMenuContainer.setBackgroundResource(R.drawable.bg_corners_top_white_5dp);
            params.topMargin = mContext.getResources().getDimensionPixelSize(R.dimen.dp_4);
            mMessageView.setBackgroundResource(R.drawable.bg_corners_bottom_white_5dp);
        }
        TextView tvMore = mMessageView.findViewById(R.id.mMoreTextView);
        tvMore.setOnClickListener(view -> mCallback.onMessageItemClick(true, null));
        RecyclerView recyclerView = mMessageView.findViewById(R.id.mMsgRecyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(new CommonAdapter<HomepageBean.MessageListBean>(mContext, messageList) {

            @Override
            protected int bindView(int viewType) {
                return R.layout.item_rv_homepage_message;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, HomepageBean.MessageListBean bean, int position) {
                holder.setText(R.id.tv_title, bean.getTitle());
                holder.setText(R.id.tv_date, bean.getTime());
                if (bean.getType() == 2) {
                    holder.setTextColorRes(R.id.tv_type, R.color.colorAccent);
                    holder.setText(R.id.tv_type, mContext.getString(R.string.message_system_message));
                    holder.setBackgroundResource(R.id.tv_type, R.drawable.bg_corners2dp_accent);
                } else {
                    holder.setTextColorRes(R.id.tv_type, R.color.bg_homepage_new);
                    holder.setText(R.id.tv_type, mContext.getString(R.string.message_notify_message));
                    holder.setBackgroundResource(R.id.tv_type, R.drawable.bg_corners4dp_red);
                }
                holder.itemView.setOnClickListener(v -> mCallback.onMessageItemClick(false, bean));
            }
        });
        mContentLayout.addView(mMessageView);
    }

    /*无权限人员*/
    private void requestNoPermission(boolean isEmptyMenu) {
        addBanner(this.mHomepageBean.getMainPicture());
        mNewsLayout.setVisibility(View.GONE);
        setMenuLayoutTopMargin(R.dimen.dp_10);
        addMessageView(this.mHomepageBean.getMessageList(), isEmptyMenu);
    }

    /*营销人员*/
    private void requestMarketing() {
        addBanner(this.mHomepageBean.getMainPicture());
        HomepageBean.OrderDataBean orderDataBean = this.mHomepageBean.getOrderData();
        if (orderDataBean != null) {
            setNewsLayoutTopMargin(R.dimen.dp_10);
            mNewsLayout.setBackgroundResource(R.drawable.bg_corners_top_white_5dp);
            updateMarketing(orderDataBean);
        }
    }

    /*老板、经理人、经理、店长、组长等本月数据*/
    private void updateBoss(HomepageBean.NewDataBean.RoleBean roleBean) {
        setupDepositView(roleBean);
        removeMonthDataView();
        mMonthDataView = mLayoutInflater.inflate(R.layout.include_homepage_month_data_boss, mContentLayout, false);
        setMonthDateText(mMonthDataView, roleBean.time);
        setupAmount(mMonthDataView, roleBean);
        TextView tvCustomers = obtainView(mMonthDataView, R.id.tv_customers);
        tvCustomers.setText(obtain(mContext.getString(R.string.number_of_new_customers), roleBean.newsCount, false));
        TextView tvDeposits = obtainView(mMonthDataView, R.id.tv_deposits);
        tvDeposits.setText(obtain(mContext.getString(R.string.number_of_deposit_customers), roleBean.ordersCustomer, false));
        TextView tvMeasures = obtainView(mMonthDataView, R.id.tv_measures);
        tvMeasures.setText(obtain(mContext.getString(R.string.number_of_measuring), roleBean.scaleCount, false));
        TextView tvSignatures = obtainView(mMonthDataView, R.id.tv_signatures);
        tvSignatures.setText(obtain(mContext.getString(R.string.number_of_signatures), roleBean.contractCount, false));
        TextView tvGraphs = obtainView(mMonthDataView, R.id.tv_graphs);
        tvGraphs.setText(obtain(mContext.getString(R.string.number_of_output_graphs), roleBean.picCount, false));
//        TextView tvStoreRate = obtainView(mMonthDataView, R.id.tv_storeRate);
//        tvStoreRate.setText(obtain(mContext.getString(R.string.store_entry_turnover_rate), roleBean.enterPercent, false));
//        TextView tvRuleRate = obtainView(mMonthDataView, R.id.tv_ruleRate);
//        tvRuleRate.setText(obtain(mContext.getString(R.string.scale_turnover_rate), roleBean.scalePercent, false));
//        obtainView(mMonthDataView, R.id.lineOrange).setVisibility(View.VISIBLE);
//        obtainView(mMonthDataView, R.id.linePink).setVisibility(View.VISIBLE);
        TextView tvSeasCount = obtainView(mMonthDataView, R.id.tv_seasCount);
        tvSeasCount.setText(obtain(mContext.getString(R.string.number_of_customers_into_seas), roleBean.seaCustomer, false));
        mContentLayout.addView(mMonthDataView);
    }

    /*设计师、设计经理本月数据，与上述角色相比，没有（订金客户数、流入公海客户数）*/
    private void updateDesigner(HomepageBean.NewDataBean.RoleBean roleBean) {
//        setupDepositView(roleBean);
        removeMonthDataView();
        mMonthDataView = mLayoutInflater.inflate(R.layout.include_homepage_month_data_designer, mContentLayout, false);
        setMonthDateText(mMonthDataView, roleBean.time);
        setupAmount(mMonthDataView, roleBean);
        TextView tvMeasures = obtainView(mMonthDataView, R.id.tv_measures);
        tvMeasures.setText(obtain(mContext.getString(R.string.number_of_measuring), roleBean.scaleCount, false));
        TextView tvOrders = obtainView(mMonthDataView, R.id.tv_orders);
        tvOrders.setText(obtain(mContext.getString(R.string.number_of_place_orders), roleBean.orders, false));
        TextView tvGraphs = obtainView(mMonthDataView, R.id.tv_graphs);
        tvGraphs.setText(obtain(mContext.getString(R.string.number_of_output_graphs), roleBean.picCount, false));
//        TextView tvInstall = obtainView(mMonthDataView, R.id.tv_install);
//        tvInstall.setText(obtain(mContext.getString(R.string.number_of_install), roleBean.installed, false));
        TextView tvSignatures = obtainView(mMonthDataView, R.id.tv_signatures);
        tvSignatures.setText(obtain(mContext.getString(R.string.number_of_signatures), roleBean.contractCount, false));
//        TextView tvRuleRate = obtainView(mMonthDataView, R.id.tv_ruleRate);
//        tvRuleRate.setText(obtain(mContext.getString(R.string.scale_turnover_rate), roleBean.scalePercent, false));
        mContentLayout.addView(mMonthDataView);
    }

    /*通用部分（已收款、成交总金额）*/
    private void setupAmount(View view, HomepageBean.NewDataBean.RoleBean roleBean) {
        TextView tvReceived = view.findViewById(R.id.tv_received);
        tvReceived.setText(obtainAmount(mContext.getString(R.string.payments_received), roleBean.receivables));
        TextView tvAmount = view.findViewById(R.id.tv_amount);
        tvAmount.setText(obtainAmount(mContext.getString(R.string.total_contract_amount), roleBean.contractTotal));
    }

    /*显示本月数据日期*/
    private void setMonthDateText(View view, String time) {
        ((TextView) view.findViewById(R.id.tv_month_date)).setText(time);
    }

    private void clearDepositView() {
        if (mViewCollectDeposit != null) {
            mContentLayout.removeView(mViewCollectDeposit);
            mViewCollectDeposit = null;
        }
    }

    /*首页顶部，订单数量，收取订金*/
    private void setupDepositView(HomepageBean.NewDataBean.RoleBean roleBean) {
        clearDepositView();
        mViewCollectDeposit = mLayoutInflater.inflate(R.layout.include_homepage_collect_depositv2, mContentLayout, false);
        setOrderCustomerCount(roleBean.ordersCustomer);
        mViewCollectDeposit.findViewById(R.id.tv_collect_deposit).setOnClickListener(this);
        mContentLayout.addView(mViewCollectDeposit, 0);
    }

    private void setOrderCustomerCount(String ordersCustomer) {
        TextView tvOrderNum = mViewCollectDeposit.findViewById(R.id.tv_deposit_order);
        String source = mContext.getString(R.string.number_of_deposit_customers_this_month);
        int start = source.length();
        if (!TextUtils.isEmpty(ordersCustomer)) {
            source += ordersCustomer;
        }
        int end = source.length();
        SpannableString ss = new SpannableString(source);
        ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.textColor13)), start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new AbsoluteSizeSpan(mContext.getResources().getDimensionPixelSize(R.dimen.textSize_20), false), start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvOrderNum.setText(ss);
    }

    /*营销人员最新数据*/
    private void updateMarketing(HomepageBean.OrderDataBean orderDataBean) {
        removeMonthDataView();
        mMonthDataView = mLayoutInflater.inflate(R.layout.include_homepage_month_data_marketing, mContentLayout, false);
        CircleProgressBar pbLeft = obtainView(mMonthDataView, R.id.pb_left);
        CircleProgressBar pbRight = obtainView(mMonthDataView, R.id.pb_right);
        TextView tvDateLeft = obtainView(mMonthDataView, R.id.tv_date_left);
        TextView tvDateRight = obtainView(mMonthDataView, R.id.tv_date_right);
        TextView tvTarget = obtainView(mMonthDataView, R.id.tv_target);
        TextView tvCompleted = obtainView(mMonthDataView, R.id.tv_completed);
        TextView tvTargetOrder = obtainView(mMonthDataView, R.id.tv_target_order);
        TextView tvCompletedOrder = obtainView(mMonthDataView, R.id.tv_completed_order);
        TextView tvArea = obtainView(mMonthDataView, R.id.tv_area);
        tvDateLeft.setText(orderDataBean.getTime());
        tvDateRight.setText(orderDataBean.getTimeDescribe());
        String targetMoney = mContext.getString(R.string.homepage_month_data_target);
        if (!TextUtils.isEmpty(orderDataBean.getTargetOrderMoney())) {
            targetMoney += orderDataBean.getTargetOrderMoney() + "万";
        }
        tvTarget.setText(targetMoney);
        String completed = mContext.getString(R.string.homepage_month_data_completed);
        if (!TextUtils.isEmpty(orderDataBean.getOrderMoney())) {
            completed += orderDataBean.getOrderMoney() + "万";
        }
        tvCompleted.setText(completed);
        String targetOrder = mContext.getString(R.string.homepage_month_data_target);
        if (!TextUtils.isEmpty(orderDataBean.getTargetOrder())) {
            targetOrder += orderDataBean.getTargetOrder() + "单";
        }
        tvTargetOrder.setText(targetOrder);
        String orderCompleted = mContext.getString(R.string.homepage_month_data_completed);
        if (!TextUtils.isEmpty(orderDataBean.getCounts())) {
            orderCompleted += orderDataBean.getCounts() + "单";
        }
        tvCompletedOrder.setText(orderCompleted);
        pbLeft.setProgress(orderDataBean.getTargetOrderMoneyPercent());
        pbRight.setProgress(orderDataBean.getTargetOrderPercent());
        pbLeft.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, PerformanceActivity.class);
            mCallback.onViewClicked(v, intent);
        });
        pbRight.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, OrderReportActivity.class);
            mCallback.onViewClicked(v, intent);
        });
        tvArea.setText(orderDataBean.getArea());
        mContentLayout.addView(mMonthDataView);
    }

    /*安装工或安装经理本月数据*/
    private void updateInstall(HomepageBean.NewDataBean.RoleBean roleBean) {
        removeMonthDataView();
        mMonthDataView = mLayoutInflater.inflate(R.layout.include_homepage_month_data_install_manager, mContentLayout, false);
        setMonthDateText(mMonthDataView, roleBean.time);
        TextView tvInstallNumber = mMonthDataView.findViewById(R.id.tvInstallNumber);
        TextView tvRate = mMonthDataView.findViewById(R.id.tvRate);
        TextView tvInstallSquares = mMonthDataView.findViewById(R.id.tvInstallSquares);
//        TextView tvSatisfaction = mMonthDataView.findViewById(R.id.tvSatisfaction);
        tvInstallNumber.setText(obtain(mContext.getString(R.string.number_of_install_customers), roleBean.installed, true));
        tvRate.setText(obtain(mContext.getString(R.string.once_install_completion_rate), roleBean.firstSuccess, true));
        tvInstallSquares.setText(obtain(mContext.getString(R.string.number_of_install_squares), roleBean.area, true));
//        tvSatisfaction.setText(obtainSatisfaction(mContext.getString(R.string.customer_satisfaction_tips), roleBean.Satisfied));
        mContentLayout.addView(mMonthDataView);
    }

    private void updateWarehouse(HomepageBean.NewDataBean.RoleBean roleBean) {
        removeMonthDataView();
        mMonthDataView = mLayoutInflater.inflate(R.layout.include_homepage_month_data_warehouse, mContentLayout, false);
        setMonthDateText(mMonthDataView, roleBean.time);
        TextView tvPartsOfInput = mMonthDataView.findViewById(R.id.tvPartsOfInput);
        tvPartsOfInput.setText(obtain(mContext.getString(R.string.number_of_scanned_input_parts), roleBean.scan, true));
        mContentLayout.addView(mMonthDataView);
    }

    private void removeMonthDataView() {
        if (mMonthDataView != null) {
            mContentLayout.removeView(mMonthDataView);
            mMonthDataView = null;
        }
    }

    /*财务人员本月数据*/
    private void updateFinance(HomepageBean.NewDataBean.RoleBean bean) {
        removeMonthDataView();
        mMonthDataView = mLayoutInflater.inflate(R.layout.include_homepage_month_data_finance, mContentLayout, false);
        setMonthDateText(mMonthDataView, bean.time);
        TextView tvDeposit = obtainView(mMonthDataView, R.id.tvDeposit);
        tvDeposit.setText(obtain(mContext.getString(R.string.deposit_tips), bean.deposit, false));
        TextView tvPayment = obtainView(mMonthDataView, R.id.tvPayment);
        tvPayment.setText(obtain(mContext.getString(R.string.contract_payment_tips), bean.contract, false));
        TextView tvTail = obtainView(mMonthDataView, R.id.tvTail);
        tvTail.setText(obtain(mContext.getString(R.string.final_paragraph_tips), bean.tail, false));
        setupAmount(mMonthDataView, bean);
        mContentLayout.addView(mMonthDataView);
    }

    /*导购、业务本月数据*/
    private void updateGuideBusiness(HomepageBean.NewDataBean.RoleBean roleBean) {
        setupDepositView(roleBean);
        removeMonthDataView();
        mMonthDataView = mLayoutInflater.inflate(R.layout.include_homepage_month_data_guidesalesman, mContentLayout, false);
        setMonthDateText(mMonthDataView, roleBean.time);
        setupAmount(mMonthDataView, roleBean);
        TextView tvCustomers = obtainView(mMonthDataView, R.id.tv_customers);
        tvCustomers.setText(obtain(mContext.getString(R.string.number_of_new_customers), roleBean.newsCount, false));
        TextView tvSignatures = obtainView(mMonthDataView, R.id.tv_signatures);
        tvSignatures.setText(obtain(mContext.getString(R.string.number_of_signatures), roleBean.contractCount, false));
        TextView tvReservation = obtainView(mMonthDataView, R.id.tv_reservation);
        tvReservation.setText(obtain(mContext.getString(R.string.number_of_reservation_scale), roleBean.prescaleCount, false));
//        TextView tvStoreRate = obtainView(mMonthDataView, R.id.tv_storeRate);
//        tvStoreRate.setText(obtain(mContext.getString(R.string.store_entry_turnover_rate), roleBean.enterPercent, false));
        TextView tvDepositCustomers = obtainView(mMonthDataView, R.id.tv_deposit_customers);
        tvDepositCustomers.setText(obtain(mContext.getString(R.string.number_of_deposit_customers), roleBean.ordersCustomer, false));
//        TextView tvRuleRate = obtainView(mMonthDataView, R.id.tv_ruleRate);
//        tvRuleRate.setText(obtain(mContext.getString(R.string.scale_turnover_rate), roleBean.scalePercent, false));
        mContentLayout.addView(mMonthDataView);
    }

    /*售后本月数据*/
    private void updateAfterSale(HomepageBean.NewDataBean.RoleBean bean) {
        removeMonthDataView();
        mMonthDataView = mLayoutInflater.inflate(R.layout.include_homepage_month_data_aftersale, mContentLayout, false);
        setMonthDateText(mMonthDataView, bean.time);
        TextView tvRate = obtainView(mMonthDataView, R.id.tvRate);
        tvRate.setText(obtain(mContext.getString(R.string.once_install_completion_rate), bean.firstSuccess, true));
//        TextView tvSatisfaction = obtainView(mMonthDataView, R.id.tvSatisfaction);
//        tvSatisfaction.setText(obtainSatisfaction(mContext.getString(R.string.customer_satisfaction_tips), bean.Satisfied));
        mContentLayout.addView(mMonthDataView);
    }

    /*金额 字体加粗、大小24sp*/
    private SpannableString obtainAmount(String origin, String content) {
        int start = origin.length();
        content = TextUtils.isEmpty(content) ? "-" : content;
        String source = origin + "\n" + content;
        int end = source.length();
        SpannableString ss = new SpannableString(source);
        ss.setSpan(new AbsoluteSizeSpan(mContext.getResources().getDimensionPixelSize(R.dimen.textSize_24), false), start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new StyleSpan(Typeface.BOLD), start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    /**
     * @param origin       提示文字
     * @param content      内容
     * @param isTextSize20 文字大小是否是20sp
     */
    private SpannableString obtain(String origin, String content, boolean isTextSize20) {
        int start = origin.length();
        String source = origin + "\n" + (TextUtils.isEmpty(content) ? "-" : content);
        int end = source.length();
        SpannableString ss = new SpannableString(source);
        int flags = SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE;
        if (isTextSize20) {
            ss.setSpan(new AbsoluteSizeSpan(mContext.getResources().getDimensionPixelSize(R.dimen.textSize_20), false), start, end, flags);
        } else {
            ss.setSpan(new AbsoluteSizeSpan(mContext.getResources().getDimensionPixelSize(R.dimen.textSize_14), false), start, end, flags);
        }
        ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.textColor4)), start, end, flags);
        return ss;
    }

    private <T extends View> T obtainView(View view, @IdRes int id) {
        return view.findViewById(id);
    }

    public String getMessageId() {
        if (mHomepageBean == null) return null;
        return mHomepageBean.getMessageId();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv_collect_deposit) {
            MobclickAgent.onEvent(mContext, "homepage_receive_deposit");
            Intent intent = new Intent(mContext, CustomerChargeDepositActivity.class);
            mCallback.onViewClicked(view, intent);
        } else if (view.getId() == R.id.llNewsLayout) {
            MobclickAgent.onEvent(mContext, "homepage_latest_announce");
            String messageId = getMessageId();
            if (!TextUtils.isEmpty(messageId)) {
                Intent intent = new Intent(mContext, MessageDetailsActivity.class);
                intent.putExtra(Constants.MESSAGE_ID, messageId);
                mCallback.onViewClicked(view, intent);
            }
        }
    }

    @Override
    public void onMenuClick(String type, String name) {
        if (mHomepageBean != null) {
            mCallback.onMenuClick(type, name);
        }
    }

    interface Callback {
        void onViewClicked(View view, @Nullable Intent intent);

        void onMessageItemClick(boolean isOpenMore, HomepageBean.MessageListBean bean);

        void onMenuClick(String type, String name);
    }
}
