package com.holike.crm.activity.report;

import android.content.Context;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.gallopmark.recycler.decorationhepler.LinearItemDecoration;
import com.grallopmark.tablayout.CommonTabLayout;
import com.grallopmark.tablayout.TabEntity;
import com.grallopmark.tablayout.listener.CustomTabEntity;
import com.grallopmark.tablayout.listener.OnTabSelectListener;
import com.holike.crm.R;
import com.holike.crm.base.ActivityHelper;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.bean.BusinessObjectivesBean;
import com.holike.crm.fragment.report.target.BusinessTargetFragment;
import com.holike.crm.fragment.report.target.EmployeeTargetFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pony on 2019/10/30.
 * Version v3.0 app报表
 * 经营目标
 */
class BusinessTargetHelper extends ActivityHelper {

    private FrameLayout mContainer;
    private FragmentManager mFragmentManager;
    private Fragment mShowingFragment;
    private static final String TAG_SHOP = "tag-shop";
    private static final String TAG_PERSON = "tag-person";

    BusinessTargetHelper(BaseActivity<?, ?> activity) {
        super(activity);
        mContainer = obtainView(R.id.fl_fragment_main);
    }

    void onSuccess(BusinessObjectivesBean bean) {
        mContainer.removeAllViews();
        if (TextUtils.equals(bean.type, "1")) { //展示经营目标
            mActivity.setTitle(R.string.business_target);
            mActivity.setRightMenu(R.string.title_set_target);
            updateBusiness(bean);
        } else if (TextUtils.equals(bean.type, "2")) { //展示个人目标
            mActivity.setTitle(R.string.target);
            updatePersonal(bean);
        } else {  //其他情况展示缺省页
            LayoutInflater.from(mActivity).inflate(R.layout.include_empty_textview, mContainer, true);
        }
    }

    /*经营目标*/
    private void updateBusiness(BusinessObjectivesBean bean) {
        mFragmentManager = mActivity.getSupportFragmentManager();
        View contentView = LayoutInflater.from(mActivity).inflate(R.layout.include_business_objectives, mContainer, true);
        ((TextView) contentView.findViewById(R.id.tv_time_detail)).setText(bean.time);
        String target = mActivity.getString(R.string.tips_transaction_target);
        if (!TextUtils.isEmpty(bean.moneyTotal)) {
            target += bean.moneyTotal;
        }
        ((TextView) contentView.findViewById(R.id.tv_target_performance)).setText(target);
        String finish = mActivity.getString(R.string.tips_transaction_completion);
        if (!TextUtils.isEmpty(bean.countTotal)) {
            finish += bean.countTotal;
        }
        ((TextView) contentView.findViewById(R.id.tv_finish_performance)).setText(finish);
        ((TextView) contentView.findViewById(R.id.tv_percent)).setText(bean.getPercent());
        ProgressBar progressBar = contentView.findViewById(R.id.progressBar);
        progressBar.setMax(bean.getMoneyTotal());
        progressBar.setProgress(bean.getCountTotal());
        List<CustomTabEntity> tabEntities = new ArrayList<>();
        final List<String> tagList = new ArrayList<>();
        if (bean.isShop()) {  //经营目标
            tabEntities.add(new TabEntity(mActivity.getString(R.string.business_target)));
            tagList.add(TAG_SHOP);
        }
        if (bean.isPerson()) {
            tabEntities.add(new TabEntity(mActivity.getString(R.string.employee_target)));
            tagList.add(TAG_PERSON);
        }
        FrameLayout flContainer = obtainView(R.id.fl_container);
        flContainer.removeAllViews();
        if (tabEntities.isEmpty()) {
            LayoutInflater.from(mActivity).inflate(R.layout.include_empty_textview, flContainer, true);
        } else {
            LinearLayout contentLayout = obtainView(R.id.ll_content_layout);
            if (tabEntities.size() > 1) {
                View view = LayoutInflater.from(mActivity).inflate(R.layout.include_common_tablayout, contentLayout, false);
                CommonTabLayout tabLayout = view.findViewById(R.id.tab_layout);
                tabLayout.setTabData(tabEntities);
                tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
                    @Override
                    public void onTabSelect(int position) {
                        setCurrentTab(bean, tagList.get(position));
                    }

                    @Override
                    public void onTabReselect(int position) {

                    }
                });
                contentLayout.addView(view, 1);
            }
            setCurrentTab(bean, tagList.get(0));
        }
    }


    private void setCurrentTab(BusinessObjectivesBean bean, String tag) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (mShowingFragment != null) {
            transaction.hide(mShowingFragment);
        }
        Fragment fragment = mFragmentManager.findFragmentByTag(tag);
        if (fragment != null) {
            transaction.show(fragment);
        } else {
            if (TextUtils.equals(tag, TAG_SHOP)) {
                fragment = BusinessTargetFragment.newInstance(bean);
            } else {
                fragment = EmployeeTargetFragment.newInstance(bean);
            }
            transaction.add(R.id.fl_container, fragment, tag);
        }
        mShowingFragment = fragment;
        transaction.commitAllowingStateLoss();
    }

    /*个人目标*/
    private void updatePersonal(BusinessObjectivesBean bean) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.include_personal_target, mContainer, true);
        ((TextView) view.findViewById(R.id.tv_time_detail)).setText(bean.time);
        LinearLayout contentLayout = view.findViewById(R.id.ll_personal_content);
        BusinessObjectivesBean.PersonDataBean personDataBean = bean.getPersonData();
        if (personDataBean == null || !personDataBean.isShow()) {
            contentLayout.removeViewAt(contentLayout.getChildCount() - 1);
            LayoutInflater.from(mActivity).inflate(R.layout.include_empty_textview, contentLayout, true);
        } else {
            RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
            recyclerView.addItemDecoration(new LinearItemDecoration(RecyclerView.VERTICAL,
                    mActivity.getResources().getDimensionPixelSize(R.dimen.dp_10),
                    ContextCompat.getColor(mActivity, R.color.bg_transparent)));
            List<InnerBean> dataList = new ArrayList<>();
            //新建客户数
            dataList.add(new InnerBean(getTitle(R.string.number_of_new_customers, R.string.slanting_rod_count),
                    getTargetOrPerformance(R.string.tips_new_customer_target, personDataBean.newCountTarget),
                    getTargetOrPerformance(R.string.tips_new_customer_completed, personDataBean.newCount),
                    personDataBean.getNewCountPercent(), personDataBean.getNewCountTarget(),
                    personDataBean.getNewCount(), R.drawable.bg_progress_drawable_lightgreen));
            //预约量尺数
            dataList.add(new InnerBean(getTitle(R.string.number_of_reservation_scale, R.string.slanting_rod_count),
                    getTargetOrPerformance(R.string.tips_appointment_gauge_target, personDataBean.prescaleCountTarget),
                    getTargetOrPerformance(R.string.tips_appointment_gauge_completed, personDataBean.prescaleCount),
                    personDataBean.getPrescaleCountPercent(), personDataBean.getPrescaleCountTarget(),
                    personDataBean.getPrescaleCount(), R.drawable.bg_progress_drawable_blue));
            //量尺数
            dataList.add(new InnerBean(getTitle(R.string.number_of_measuring, R.string.slanting_rod_count),
                    getTargetOrPerformance(R.string.tips_gauge_target, personDataBean.scaleCountTarget),
                    getTargetOrPerformance(R.string.tips_gauge_completed, personDataBean.scaleCount),
                    personDataBean.getScaleCountPercent(), personDataBean.getScaleCountTarget(),
                    personDataBean.getScaleCount(), R.drawable.bg_progress_drawable_auntred));
            //出图数
            dataList.add(new InnerBean(getTitle(R.string.number_of_output_graphs, R.string.slanting_rod_count),
                    getTargetOrPerformance(R.string.tips_drawing_target, personDataBean.picCountTarget),
                    getTargetOrPerformance(R.string.tips_drawing_completed, personDataBean.picCount),
                    personDataBean.getPicCountPercent(), personDataBean.getPicCountTarget(),
                    personDataBean.getPicCount(), R.drawable.bg_progress_drawable_lightyellow));
            //成交
            dataList.add(new InnerBean(getTitle(R.string.transaction, R.string.slanting_rod_million),
                    getTargetOrPerformance(R.string.tips_transaction_target, personDataBean.contractTotalTarget),
                    getTargetOrPerformance(R.string.tips_transaction_completion, personDataBean.contractTotal),
                    personDataBean.getContractTotalPercent(), personDataBean.getContractTotalTarget(),
                    personDataBean.getContractTotal(), R.drawable.bg_progress_drawable_purple));
            //回款
            dataList.add(new InnerBean(getTitle(R.string.payback, R.string.slanting_rod_million),
                    getTargetOrPerformance(R.string.tips_payback_target, personDataBean.receiverTarget),
                    getTargetOrPerformance(R.string.tips_payback_completed, personDataBean.receiver),
                    personDataBean.getReceiverPercent(), personDataBean.getReceiverTarget(),
                    personDataBean.getReceiver(), R.drawable.bg_progress_drawable_lightgreen));
            //下单
            dataList.add(new InnerBean(getTitle(R.string.order, R.string.slanting_rod_million),
                    getTargetOrPerformance(R.string.tips_order_target, personDataBean.orderTotalTarget),
                    getTargetOrPerformance(R.string.tips_order_completed, personDataBean.orderTotal),
                    personDataBean.getOrderTotalPercent(), personDataBean.getOrderTotalTarget(),
                    personDataBean.getOrderTotal(), R.drawable.bg_progress_drawable_orange));
            recyclerView.setAdapter(new PersonalDataAdapter(mActivity, dataList));
        }
    }

    private CharSequence getTitle(int origin, int content) {
        String source = mActivity.getString(origin);
        int start = source.length();
        source += mActivity.getString(content);
        SpannableString ss = new SpannableString(source);
        ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mActivity, R.color.textColor8)), start, source.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    private CharSequence getTargetOrPerformance(int origin, String content) {
        String source = mActivity.getString(origin);
        int start = source.length();
        source += (TextUtils.isEmpty(content) ? "" : content);
        SpannableString ss = new SpannableString(source);
        ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mActivity, R.color.textColor4)), start, source.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    private final class InnerBean {
        CharSequence title;
        CharSequence target;
        CharSequence performance;
        String percent;
        int max;
        int progress;
        int progressDrawable;

        InnerBean(CharSequence title, CharSequence target, CharSequence performance,
                  String percent, int max, int progress, int progressDrawable) {
            this.title = title;
            this.target = target;
            this.performance = performance;
            this.percent = percent;
            this.max = max;
            this.progress = progress;
            this.progressDrawable = progressDrawable;
        }
    }

    private final class PersonalDataAdapter extends CommonAdapter<InnerBean> {

        PersonalDataAdapter(Context context, List<InnerBean> dataList) {
            super(context, dataList);
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_personal_target;
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, InnerBean bean, int position) {
            holder.setText(R.id.tv_title, bean.title);
            holder.setText(R.id.tv_target, bean.target);
            holder.setText(R.id.tv_performance, bean.performance);
            holder.setText(R.id.tv_percent, bean.percent);
            ProgressBar pb = holder.obtainView(R.id.progressBar);
            pb.setProgressDrawable(ContextCompat.getDrawable(mContext, bean.progressDrawable));
            pb.setMax(bean.max);
            pb.setProgress(bean.progress);
        }
    }

    void onFailure(String failReason) {
        mContainer.removeAllViews();
        LayoutInflater.from(mActivity).inflate(R.layout.include_empty_page, mContainer, true);
        mActivity.noNetwork(failReason);
    }
}
