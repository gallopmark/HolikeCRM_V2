package com.holike.crm.activity.report;

import android.content.Context;
import android.graphics.Paint;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.grallopmark.tablayout.SlidingTabLayout;
import com.grallopmark.tablayout.listener.OnTabSelectListener;
import com.holike.crm.R;
import com.holike.crm.adapter.AbsFormAdapter;
import com.holike.crm.base.ActivityHelper;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.bean.FactoryPerformanceBean;
import com.holike.crm.helper.FormDataHelper;

import java.util.List;

/**
 * Created by pony on 2019/11/6.
 * Version v3.0 app报表
 * 出产业绩报表-经销商
 */
class FactoryPerformanceHelper extends ActivityHelper {

    private Callback mCallback;
    private LinearLayout mContentLayout;
    private FrameLayout mContainer;
    private String mDimensionOf;
    private String mMonth;
    private boolean mIncludeMonth;
    private Handler mHandler;
    private int mCurrentDimen = 0;

    FactoryPerformanceHelper(BaseActivity<?, ?> activity, Callback callback) {
        super(activity);
        this.mCallback = callback;
        String title = mActivity.getIntent().getStringExtra("title");
        if (TextUtils.isEmpty(title)) {
            title = mActivity.getString(R.string.title_factory_performance);
        }
        activity.setTitle(title);
        mContentLayout = obtainView(R.id.ll_content_layout);
        mContainer = obtainView(R.id.fl_container);
        doRequest();
    }

    void doRequest() {
        mCallback.onRequest(mDimensionOf, mMonth);
    }

    void onSuccess(FactoryPerformanceBean bean) {
        if (!bean.getMonList().isEmpty() && !mIncludeMonth) {
            setTabLayout(bean.getMonList());
        }
        setContentLayout(bean);
    }

    private void setTabLayout(final List<String> monthList) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.include_common_slidingtablayout, mContentLayout, false);
        SlidingTabLayout tabLayout = view.findViewById(R.id.sliding_tab_layout);
        tabLayout.setupViewPager(view.findViewById(R.id.view_pager), monthList.toArray(new String[0]));
        if (monthList.size() > 2) {  //全年、本月...... 默认显示本月
            tabLayout.setCurrentTab(1);
        }
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                if (position >= 0 && position < monthList.size()) {
                    mMonth = monthList.get(position);
                    requestByDelayed();
                }
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        mContentLayout.addView(view, 1);
        mIncludeMonth = true;
    }

    private void requestByDelayed() {
        if (mHandler == null) {
            mHandler = new Handler();
        }
        mHandler.removeCallbacks(requestTask);
        mHandler.postDelayed(requestTask, 150);
    }

    private Runnable requestTask = this::doRequest;

    private void setContentLayout(final FactoryPerformanceBean bean) {
        mContainer.removeAllViews();
        final View contentView = LayoutInflater.from(mActivity).inflate(R.layout.include_factory_performance_content, mContainer, true);
        ((TextView) contentView.findViewById(R.id.tv_time_detail)).setText(bean.dateToDate);
        final List<FactoryPerformanceBean.DimensionOfBean> tabList = bean.getDimensionOfList();
        if (tabList.isEmpty()) return;
        RecyclerView rvTab = contentView.findViewById(R.id.rv_tab);
        FrameLayout flForm = contentView.findViewById(R.id.fl_form_content);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) flForm.getLayoutParams();
        params.leftMargin = mActivity.getResources().getDimensionPixelSize(R.dimen.dp_10);
        final TabAdapter tabAdapter = new TabAdapter(mActivity, tabList);
        tabAdapter.setOnItemClickListener((adapter, holder, view, position) -> {
            tabAdapter.setSelectPosition(position);
            mCurrentDimen = position;
            mDimensionOf = tabList.get(mCurrentDimen).type;
            doRequest();
        });
        rvTab.setAdapter(tabAdapter);
        tabAdapter.setSelectPosition(mCurrentDimen);
        updateFormContent(contentView, bean, tabList.get(mCurrentDimen));
    }

    private final class TabAdapter extends CommonAdapter<FactoryPerformanceBean.DimensionOfBean> {
        int selectPosition;

        TabAdapter(Context context, List<FactoryPerformanceBean.DimensionOfBean> dataList) {
            super(context, dataList);
        }

        void setSelectPosition(int selectPosition) {
            this.selectPosition = selectPosition;
            notifyDataSetChanged();
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_form_data_tabhost;
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, FactoryPerformanceBean.DimensionOfBean bean, int position) {
            if (selectPosition == position) {
                holder.setTextColorRes(R.id.tv_role, R.color.color_while);
                holder.itemView.setBackgroundResource(R.drawable.bg_corners5dp_top_coloraccent);
            } else {
                holder.setTextColorRes(R.id.tv_role, R.color.textColor5);
                holder.itemView.setBackgroundResource(R.drawable.bg_corners5dp_top_bg);
            }
            holder.setText(R.id.tv_role, bean.name);
        }
    }

    private void updateFormContent(View contentView, FactoryPerformanceBean bean, FactoryPerformanceBean.DimensionOfBean dimensionOfBean) {
        FrameLayout container = contentView.findViewById(R.id.fl_form_content);
        container.removeAllViews();
        String type = dimensionOfBean.type;
        if (!TextUtils.equals(type, "1") && !TextUtils.equals(type, "2") && !TextUtils.equals(type, "3")) {
            return;
        }
        View formView = LayoutInflater.from(mActivity).inflate(R.layout.include_form_data_content, container, true);
        FormDataHelper.attachView(formView, updateFormByType(bean, type));
    }

    private FormDataHelper.FormDataBinder updateFormByType(FactoryPerformanceBean bean, String type) {
        final int firstColumnLayoutRes = R.layout.include_form_data_column_90dp_h44;
        final String sideTitle = mActivity.getString(R.string.store);
        final int firstRowLayoutRes;
        final RecyclerView.Adapter sideAdapter;
        final RecyclerView.Adapter contentAdapter;
        if (TextUtils.equals(type, "1")) {  //品类
            firstRowLayoutRes = R.layout.include_factory_performance_firstrow_category;
            sideAdapter = new CategoryAdapter(mActivity, bean.getCategoryData(), true);
            contentAdapter = new CategoryAdapter(mActivity, bean.getCategoryData(), false);
        } else if (TextUtils.equals(type, "2")) { //空间
            firstRowLayoutRes = R.layout.include_factory_performance_firstrow_space;
            sideAdapter = new SpaceAdapter(mActivity, bean.getSpaceData(), true);
            contentAdapter = new SpaceAdapter(mActivity, bean.getSpaceData(), false);
        } else {  //渠道
            firstRowLayoutRes = R.layout.include_factory_performance_firstrow_channel;
            sideAdapter = new ChannelAdapter(mActivity, bean.getChannelData(), true);
            contentAdapter = new ChannelAdapter(mActivity, bean.getChannelData(), false);
        }
        return new FormDataHelper.FormDataBinder() {
            @Override
            public int bindFirstColumnLayoutRes() {
                return firstColumnLayoutRes;
            }

            @Override
            public CharSequence bindSideTitle() {
                return sideTitle;
            }

            @Override
            public int bindFirstRowLayoutRes() {
                return firstRowLayoutRes;
            }

            @Override
            public RecyclerView.Adapter bindSideAdapter() {
                return sideAdapter;
            }

            @Override
            public RecyclerView.Adapter bindContentAdapter() {
                return contentAdapter;
            }
        };
    }

    abstract class GeneralAdapter<T> extends AbsFormAdapter<T> {
        int mMaxTextWidth;

        GeneralAdapter(Context context, List<T> dataList) {
            super(context, dataList);
            mMaxTextWidth = mContext.getResources().getDimensionPixelSize(R.dimen.dp_90)
                    - mContext.getResources().getDimensionPixelSize(R.dimen.dp_4) * 2;
        }

        void setTextRatio(RecyclerHolder holder, int viewId, String origin, String showText) {
            TextView tv = holder.obtainView(viewId);
            if (getTextWidth(tv.getPaint(), origin) > mMaxTextWidth) {
                tv.setText(showText);
            } else {
                tv.setText(origin);
            }
        }

        float getTextWidth(Paint paint, String text) {
            if (TextUtils.isEmpty(text)) return 0;
            return paint.measureText(text);
        }
    }

    /*品类数据*/
    private final class CategoryAdapter extends GeneralAdapter<FactoryPerformanceBean.CategoryDataBean> {
        private int mLayoutResourceId;

        CategoryAdapter(Context context, List<FactoryPerformanceBean.CategoryDataBean> dataList, boolean isSide) {
            super(context, dataList);
            mLayoutResourceId = isSide ? R.layout.item_factory_performance_category_form_side
                    : R.layout.item_factory_performance_category_form_content;
        }

        @Override
        protected void bindViewHolder(RecyclerHolder holder, FactoryPerformanceBean.CategoryDataBean bean, int position) {
            holder.setText(R.id.tv_shop, bean.shopName); //门店
            setTextRatio(holder, R.id.tv_custom, bean.customize, bean.getShowCustomize()); //定制(占比)
            setTextRatio(holder, R.id.tv_cupboard, bean.cupboard, bean.getShowCupboard()); //橱柜(占比)
            setTextRatio(holder, R.id.tv_woodendoor, bean.woodenDoor, bean.getShowWoodenDoor()); //木门(占比)
            setTextRatio(holder, R.id.tv_product, bean.finished, bean.getShowFinished()); //成品(占比)
            setTextRatio(holder, R.id.tv_curtain, bean.curtain, bean.getShowCurtain()); //窗帘(占比)
            setTextRatio(holder, R.id.tv_bighome, bean.bigHouse, bean.getShowBigHouse()); //大家居(占比)
        }

        @Override
        protected int bindView(int viewType) {
            return mLayoutResourceId;
        }
    }

    /*空间数据*/
    private final class SpaceAdapter extends GeneralAdapter<FactoryPerformanceBean.SpaceDataBean> {
        private int mLayoutResourceId;

        SpaceAdapter(Context context, List<FactoryPerformanceBean.SpaceDataBean> dataList, boolean isSide) {
            super(context, dataList);
            mLayoutResourceId = isSide ? R.layout.item_factory_performance_space_form_side :
                    R.layout.item_factory_performance_space_form_content;
        }

        @Override
        protected void bindViewHolder(RecyclerHolder holder, FactoryPerformanceBean.SpaceDataBean bean, int position) {
            holder.setText(R.id.tv_shop, bean.shopName); //门店
            setTextRatio(holder, R.id.tv_guestrestaurant, bean.restaurant, bean.getShowRestaurant()); //客餐厅(占比)
            setTextRatio(holder, R.id.tv_hall, bean.hall, bean.getShowHall()); //门厅(占比)
            setTextRatio(holder, R.id.tv_balcony, bean.balcony, bean.getShowBalcony()); //阳台(占比)
            setTextRatio(holder, R.id.tv_masterbedroom, bean.masterBedroom, bean.getShowMasterBedroom()); //主卧房(占比)
            setTextRatio(holder, R.id.tv_guest_bedroom, bean.guestBedroom, bean.getShowGuestBedroom()); //客卧房(占比))
            setTextRatio(holder, R.id.tv_childrenroom, bean.child, bean.getShowChild()); //儿童房(占比)
            setTextRatio(holder, R.id.tv_elderroom, bean.elder, bean.getShowElder()); //长辈房(占比)
            setTextRatio(holder, R.id.tv_studyroom, bean.study, bean.getShowStudy()); //书房(占比)
            setTextRatio(holder, R.id.tv_multfunroom, bean.multifunction, bean.getShowMultifunction()); //多功能房(占比)
            setTextRatio(holder, R.id.tv_kitchen, bean.kitchen, bean.getShowKitchen()); //厨房(占比)
            setTextRatio(holder, R.id.tv_other, bean.other, bean.getShowOther()); //其他(占比)
        }

        @Override
        protected int bindView(int viewType) {
            return mLayoutResourceId;
        }
    }

    /*客户渠道数据*/
    private final class ChannelAdapter extends GeneralAdapter<FactoryPerformanceBean.ChannelDataBean> {
        private int mLayoutResourceId;

        ChannelAdapter(Context context, List<FactoryPerformanceBean.ChannelDataBean> dataList, boolean isSide) {
            super(context, dataList);
            mLayoutResourceId = isSide ? R.layout.item_factory_performance_channel_form_side :
                    R.layout.item_factory_performance_channel_form_content;
        }

        @Override
        protected void bindViewHolder(RecyclerHolder holder, FactoryPerformanceBean.ChannelDataBean bean, int position) {
            holder.setText(R.id.tv_shop, bean.shopName); //门店
            setTextRatio(holder, R.id.tv_conventional, bean.conventional, bean.getShowConventional()); //常规(占比)
            setTextRatio(holder, R.id.tv_onlinedrainage, bean.onLine, bean.getShowOnLine()); //总部线上引流(占比)
            setTextRatio(holder, R.id.tv_shack, bean.bag, bean.getShowBag()); //拎包入住(占比)
            setTextRatio(holder, R.id.tv_homedoll, bean.home, bean.getShowHome()); ////家装(占比)
        }

        @Override
        protected int bindView(int viewType) {
            return mLayoutResourceId;
        }
    }

    void onFailure(String failReason) {
        mContainer.removeAllViews();
        LayoutInflater.from(mActivity).inflate(R.layout.include_empty_page, mContainer, true);
        mActivity.noNetwork(failReason);
    }

    interface Callback {
        void onRequest(String dimensionOf, String month);
    }

    void destroy() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }
}
