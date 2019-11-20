package com.holike.crm.fragment.report.business;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.grallopmark.tablayout.SlidingTabLayout;
import com.grallopmark.tablayout.listener.OnTabSelectListener;
import com.holike.crm.R;
import com.holike.crm.base.BaseFragment;
import com.holike.crm.base.FragmentHelper;
import com.holike.crm.bean.BusinessReferenceTypeBean;
import com.holike.crm.bean.DictionaryBean;
import com.holike.crm.helper.PickerHelper;

import java.util.ArrayList;
import java.util.List;

abstract class DealerMultiPerformanceHelper extends FragmentHelper {

    private Callback mCallback;
    private LinearLayout mContentLayout;
    protected FrameLayout mContainerLayout;
    private String mDimensionOf;// 1:系列 2:花色
    private String mDimensionOfCli; //1定制 2木门 3橱柜
    private String mMonth; //时间: 全年,11月,10月.......
    private String mShopCode;
    private boolean mIncludeTabLayout = false;
    private String mSelectShopName;
    private int mSelectDimensionOfCli;

    DealerMultiPerformanceHelper(BaseFragment<?, ?> fragment, Callback callback) {
        super(fragment);
        this.mCallback = callback;
        mContentLayout = obtainView(R.id.ll_content_layout);
        mContainerLayout = obtainView(R.id.fl_container);
        mDimensionOf = getDimensionOf();
        onHttpRequest();
    }

    abstract String getDimensionOf();

    private void onHttpRequest() {
        mCallback.doRequest(mMonth, mDimensionOf, mDimensionOfCli, mShopCode);
    }

    void onSuccess(BusinessReferenceTypeBean bean) {
        mContainerLayout.removeAllViews();
        if (bean == null) {
            noResult();
        } else {
            setTabLayout(bean.getMonList());
            View view = LayoutInflater.from(mContext).inflate(R.layout.include_business_reference, mContainerLayout, true);
            ((TextView) view.findViewById(R.id.tv_time_detail)).setText(bean.dateToDate);
            setShopLayout(view.findViewById(R.id.tv_shop), bean.getShopList());
            setDimensionTabList(view.findViewById(R.id.rv_tab), bean.getDimensionOfCliList());
            ViewStub vs = view.findViewById(R.id.vs_first_row);
            vs.setLayoutResource(getFirstRowLayoutResource());
            vs.inflate();
            setFormData(view.findViewById(R.id.rv_content), bean.getResultList());
        }
    }

    /*设置月份tab*/
    private void setTabLayout(List<String> monthList) {
        if (monthList.isEmpty()) {
            return;
        }
        if (!mIncludeTabLayout) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.include_common_slidingtablayout, mContentLayout, false);
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
                        onHttpRequest();
                    }
                }

                @Override
                public void onTabReselect(int position) {

                }
            });
            mContentLayout.addView(view, 1);
            mIncludeTabLayout = true;
        }
    }

    /*门店*/
    private void setShopLayout(TextView tvShop, List<BusinessReferenceTypeBean.ShopBean> shopList) {
        if (shopList.isEmpty()) {  //没有可选门店
            tvShop.setVisibility(View.GONE);
        } else {
            tvShop.setVisibility(View.VISIBLE);
            tvShop.setText(TextUtils.isEmpty(mSelectShopName) ? mContext.getString(R.string.all_stores) : mSelectShopName);
            tvShop.setOnClickListener(v -> selectShop(tvShop, shopList));
        }
    }

    /*选择门店*/
    private void selectShop(final TextView tvShop, List<BusinessReferenceTypeBean.ShopBean> shopList) {
        List<DictionaryBean> optionItems = new ArrayList<>();
        for (BusinessReferenceTypeBean.ShopBean bean : shopList) {
            optionItems.add(new DictionaryBean(bean.shopCode, bean.shopName));
        }
        PickerHelper.showOptionsPicker(mContext, optionItems, ((position, bean) -> {
            mShopCode = bean.id;
            mSelectShopName = bean.name;
            tvShop.setText(mSelectShopName);
            onHttpRequest();
        }));
    }

    private void setDimensionTabList(RecyclerView rvTab, List<BusinessReferenceTypeBean.DimensionOfCliBean> dataList) {
        if (dataList.isEmpty()) {
            rvTab.setVisibility(View.GONE);
        } else {
            rvTab.setVisibility(View.VISIBLE);
            rvTab.setAdapter(new DimensionTabAdapter(mContext, dataList, mSelectDimensionOfCli));
        }
    }

    private final class DimensionTabAdapter extends CommonAdapter<BusinessReferenceTypeBean.DimensionOfCliBean> {

        private int selectPosition;

        DimensionTabAdapter(Context context, List<BusinessReferenceTypeBean.DimensionOfCliBean> dataList, int selectPosition) {
            super(context, dataList);
            this.selectPosition = selectPosition;
        }

        void setSelectPosition(int position) {
            mSelectDimensionOfCli = position;
            this.selectPosition = position;
            notifyDataSetChanged();
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_form_data_tabhost;
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, BusinessReferenceTypeBean.DimensionOfCliBean bean, int position) {
            if (selectPosition == position) {
                holder.setTextColorRes(R.id.tv_role, R.color.color_while);
                holder.itemView.setBackgroundResource(R.drawable.bg_corners5dp_top_coloraccent);
            } else {
                holder.setTextColorRes(R.id.tv_role, R.color.textColor5);
                holder.itemView.setBackgroundResource(R.drawable.bg_corners5dp_top_bg);
            }
            holder.setText(R.id.tv_role, bean.name);
            holder.itemView.setOnClickListener(view -> {
                mDimensionOfCli = bean.type;
                setSelectPosition(position);
                onHttpRequest();
            });
        }
    }

    private void noResult() {
        LayoutInflater.from(mContext).inflate(R.layout.include_empty_page, mContainerLayout, true);
        mFragment.noResult();
    }

    abstract int getFirstRowLayoutResource();

    abstract void setFormData(RecyclerView rvContent, List<BusinessReferenceTypeBean.DataBean> dataList);

    void onFailure(String failReason) {
        mContainerLayout.removeAllViews();
        LayoutInflater.from(mContext).inflate(R.layout.include_empty_page, mContainerLayout, true);
        mFragment.noNetwork(failReason);
    }

    void reload() {
        onHttpRequest();
    }

    interface Callback {
        void doRequest(String month, String dimensionOf, String dimensionOfCli, String shopCode);
    }
}
