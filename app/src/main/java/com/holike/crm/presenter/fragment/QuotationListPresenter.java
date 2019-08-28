package com.holike.crm.presenter.fragment;

import android.content.Context;
import android.graphics.Typeface;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gallopmark.recycler.adapterhelper.BaseRecyclerAdapter;
import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.bean.QuotationBean;
import com.holike.crm.customView.ObservableHorizontalScrollView;
import com.holike.crm.customView.WrapContentLinearLayoutManager;
import com.holike.crm.model.fragment.QuotationListModel;
import com.holike.crm.view.fragment.QuotationListView;

import java.util.ArrayList;
import java.util.List;


public class QuotationListPresenter extends BasePresenter<QuotationListView, QuotationListModel> {
    private List<QuotationBean.DataListBean> beans = new ArrayList<>();
    private QuotationListAdapter mAdapter;

    public static class QuotationListAdapter extends CommonAdapter<QuotationBean.DataListBean> {
        private boolean isProduct;
        private List<RecyclerHolder> mViewHolderList = new ArrayList<>();
        private OnContentScrollListener onContentScrollListener;

        public interface OnContentScrollListener {
            void onScroll(int scrollX);
        }

        void setOnContentScrollListener(OnContentScrollListener onContentScrollListener) {
            this.onContentScrollListener = onContentScrollListener;
        }

        List<RecyclerHolder> getViewHolderCacheList() {
            return mViewHolderList;
        }

        String isEmpty(String s) {
            return TextUtils.isEmpty(s) ? "-" : s;
        }

        void addAll(List<QuotationBean.DataListBean> beans, boolean isProduct) {
            this.isProduct = isProduct;
            this.mDatas.clear();
            this.mDatas.addAll(beans);
            notifyDataSetChanged();
        }

        QuotationListAdapter(Context context, List<QuotationBean.DataListBean> mDatas) {
            super(context, mDatas);
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_quotation_list;
        }

        @Override
        public void onBindHolder(RecyclerHolder itemViewHolder, QuotationBean.DataListBean dataBean, int position) {
            //缓存当前holder
            if (!mViewHolderList.contains(itemViewHolder)) {
                mViewHolderList.add(itemViewHolder);
            }
            if (isProduct) {  //1
                itemViewHolder.setVisibility(R.id.item_tv_side_number, View.GONE);
                itemViewHolder.setVisibility(R.id.mQuotationContent1, View.GONE);
                itemViewHolder.setVisibility(R.id.mQuotationContent2, View.VISIBLE);
                itemViewHolder.setText(R.id.mSizeTv, isEmpty(dataBean.getSize()));
                itemViewHolder.setText(R.id.mStandardPrice2, isEmpty(dataBean.getStandardPrice()));
                itemViewHolder.setText(R.id.mAccountTv, isEmpty(dataBean.getAccountCoefficient()));
                itemViewHolder.setText(R.id.mColorTv2, isEmpty(dataBean.getColorName()));
                itemViewHolder.setText(R.id.mActivityDisTv2, isEmpty(dataBean.getActivityDis()));
                itemViewHolder.setText(R.id.mFpdTv2, isEmpty(dataBean.getFactoryPriceAfDis()));
                itemViewHolder.setText(R.id.mFpbTv2, isEmpty(dataBean.getFactoryPriceBeDis()));
                itemViewHolder.setText(R.id.mUnitTv, isEmpty(dataBean.getUnit()));
                itemViewHolder.setText(R.id.mSalePrice2, isEmpty(dataBean.getSalePrice()));
                itemViewHolder.setText(R.id.mTotalPriceTv, isEmpty(dataBean.getTotalPrice()));
                itemViewHolder.setText(R.id.mQtyTv2, isEmpty(dataBean.getQty()));
            } else {
                itemViewHolder.setVisibility(R.id.item_tv_side_number, View.VISIBLE);
                itemViewHolder.setVisibility(R.id.mQuotationContent1, View.VISIBLE);
                itemViewHolder.setVisibility(R.id.mQuotationContent2, View.GONE);
                itemViewHolder.setText(R.id.mMaterialTv, dataBean.getMaterialName());
                itemViewHolder.setText(R.id.mColorTv, isEmpty(dataBean.getColorName()));
                itemViewHolder.setText(R.id.mLengthTv, dataBean.getLength());
                itemViewHolder.setText(R.id.mMatTv, isEmpty(dataBean.getMat()));
                itemViewHolder.setText(R.id.mWidthTv, isEmpty(dataBean.getWidth()));
                itemViewHolder.setText(R.id.mHeightTv, isEmpty(dataBean.getHeight()));
                itemViewHolder.setText(R.id.mQtyTv, isEmpty(dataBean.getQty()));
                itemViewHolder.setText(R.id.mAreaTv, isEmpty(dataBean.getArea()));
                itemViewHolder.setText(R.id.mTotalAreaTv, isEmpty(dataBean.getTotalArea()));
                itemViewHolder.setText(R.id.mStandardPriceTv, isEmpty(dataBean.getStandardPrice()));
                itemViewHolder.setText(R.id.mSalePriceTv, isEmpty(dataBean.getSalePrice()));
                itemViewHolder.setText(R.id.mFpbTv, isEmpty(dataBean.getFactoryPriceBeDis()));
                itemViewHolder.setText(R.id.mFpdTv, dataBean.getFactoryPriceAfDis());
                itemViewHolder.setText(R.id.mActivityDisTv, isEmpty(dataBean.getActivityDis()));
                itemViewHolder.setText(R.id.mFapTv, isEmpty(dataBean.getFactoryActivityPrice()));
                itemViewHolder.setText(R.id.mRebateDisTv, isEmpty(dataBean.getRebateDis()));
                itemViewHolder.setText(R.id.mRebatePriceTv, isEmpty(dataBean.getRebatePrice()));
                itemViewHolder.setText(R.id.mUrgentPriceTv, isEmpty(dataBean.getUrgentPrice()));
                itemViewHolder.setText(R.id.mMainNameTv, isEmpty(dataBean.getMainName()));
                itemViewHolder.setText(R.id.mOrFreeTv, isEmpty(dataBean.getOrFree()));
                itemViewHolder.setText(R.id.mRemarkTv, isEmpty(dataBean.getRemark()));
                itemViewHolder.setText(R.id.mMessageTv, isEmpty(dataBean.getMsg()));
            }
            itemViewHolder.setText(R.id.item_tv_side, isEmpty(dataBean.getMaterialName()));
            itemViewHolder.setText(R.id.item_tv_side_number, isEmpty(dataBean.getPoRowNumber()));
            itemViewHolder.setBackgroundColor(R.id.llLeft, ContextCompat.getColor(mContext, position % 2 == 0 ? R.color.light_text5 : R.color.color_while));
            itemViewHolder.setBackgroundColor(R.id.mQuotationContent1, ContextCompat.getColor(mContext, position % 2 == 0 ? R.color.light_text5 : R.color.color_while));
            itemViewHolder.setBackgroundColor(R.id.mQuotationContent2, ContextCompat.getColor(mContext, position % 2 == 0 ? R.color.light_text5 : R.color.color_while));
            //缓存当前holder
            if (!mViewHolderList.contains(itemViewHolder)) {
                mViewHolderList.add(itemViewHolder);
            }
            final ObservableHorizontalScrollView horItemScrollview = itemViewHolder.obtainView(R.id.horItemScrollview);
            horItemScrollview.setScrollViewListener((scrollView, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                //记录滑动距离,便于处理下拉刷新之后的还原操作
                if (null != onContentScrollListener) onContentScrollListener.onScroll(scrollX);
            });
        }
    }

    public void getData(String orderId) {
        model.getData(orderId, new QuotationListModel.QuotationListListener() {
            @Override
            public void success(QuotationBean beans) {
                if (getView() != null)
                    getView().onSuccess(beans);
            }

            @Override
            public void fail(String errorMsg) {
                if (getView() != null)
                    getView().onFail(errorMsg);
            }
        });
    }

    public void initTitleLayout(Context mContext, LinearLayout mQuotationContent1, LinearLayout mQuotationContent2) {
        for (int i = 0; i < mQuotationContent1.getChildCount(); i++) {
            View view = mQuotationContent1.getChildAt(i);
            if (view instanceof FrameLayout) {
                FrameLayout frameLayout = (FrameLayout) view;
                for (int j = 0; j < frameLayout.getChildCount(); j++) {
                    if (frameLayout.getChildAt(j) instanceof TextView) {
                        TextView tv = (TextView) frameLayout.getChildAt(j);
                        tv.setTextColor(ContextCompat.getColor(mContext, R.color.textColor4));
                        tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    }
                }
            } else {
                if (view instanceof TextView) {
                    TextView tv = (TextView) view;
                    tv.setTextColor(ContextCompat.getColor(mContext, R.color.textColor4));
                    tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                }
            }
        }
        for (int i = 0; i < mQuotationContent2.getChildCount(); i++) {
            View view = mQuotationContent2.getChildAt(i);
            if (view instanceof TextView) {
                TextView tv = (TextView) view;
                tv.setTextColor(ContextCompat.getColor(mContext, R.color.textColor4));
                tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            }
        }
    }

    public void initContent(ObservableHorizontalScrollView mHsvTopTab, RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);
        mAdapter = new QuotationListAdapter(recyclerView.getContext(), beans);
        recyclerView.setAdapter(mAdapter);
        //滚动RV时,同步所有横向位移的item
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                List<BaseRecyclerAdapter.RecyclerHolder> viewHolderCacheList = mAdapter.getViewHolderCacheList();
//                if (null != viewHolderCacheList) {
//                    int size = viewHolderCacheList.size();
//                    for (int i = 0; i < size; i++) {
//                        viewHolderCacheList.getInstance(i).obtainView(R.id.horItemScrollview).scrollTo(mAdapter.getScrollX(), 0);
//                    }
//                }
//            }
//        });
        mHsvTopTab.setScrollViewListener((scrollView, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            //代码重复,可以抽取/////
            List<BaseRecyclerAdapter.RecyclerHolder> viewHolderCacheList = mAdapter.getViewHolderCacheList();
            if (null != viewHolderCacheList) {
                int size = viewHolderCacheList.size();
                for (int i = 0; i < size; i++) {
                    viewHolderCacheList.get(i).obtainView(R.id.horItemScrollview).scrollTo(scrollX, 0);
                }
            }
        });
        mAdapter.setOnContentScrollListener(scrollX -> mHsvTopTab.scrollTo(scrollX, 0));
    }

    public void update(List<QuotationBean.DataListBean> beans, boolean isProduct) {
        mAdapter.addAll(beans, isProduct);
    }

    public void setSideList(final Context ctx, final RecyclerView rv, final QuotationBean dataBean) {
        final List<QuotationBean.DataListBean> datas = dataBean.getDataList();
        rv.setLayoutManager(new WrapContentLinearLayoutManager(ctx));
        rv.setAdapter(new CommonAdapter<QuotationBean.DataListBean>(ctx, datas) {

            @Override
            protected int bindView(int viewType) {
                return R.layout.item_quotation_info_side;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, QuotationBean.DataListBean bean, int position) {
                if (TextUtils.equals(dataBean.isProduct, "1")) {
                    holder.setVisibility(R.id.item_tv_side_number, false);
                } else {
                    holder.setVisibility(R.id.item_tv_side_number, true);
                }
                holder.setText(R.id.item_tv_side, isEmpty(bean.getMaterialName()));
                holder.setText(R.id.item_tv_side_number, isEmpty(bean.getPoRowNumber()));
                holder.setBackgroundColor(R.id.item_tv_side, ctx.getResources().getColor(position % 2 == 0 ? R.color.light_text5 : R.color.color_while));
                holder.setBackgroundColor(R.id.item_tv_side_number, ctx.getResources().getColor(position % 2 == 0 ? R.color.light_text5 : R.color.color_while));
            }
        });


    }


    public void setContentList(final Context ctx, final RecyclerView rv, final QuotationBean dataBean) {
        final List<QuotationBean.DataListBean> datas = dataBean.getDataList();
        rv.setLayoutManager(new WrapContentLinearLayoutManager(ctx));
        rv.setAdapter(new CommonAdapter<QuotationBean.DataListBean>(ctx, datas) {

            @Override
            protected int bindView(int viewType) {
                return  R.layout.item_quotation_info_content;
            }

            @Override
            public void onBindHolder(RecyclerHolder h, QuotationBean.DataListBean bean, int position) {
                switch (dataBean.isProduct) {
                    case "0":
                        h.setVisibility(R.id.ll_quotation_info_finish_content_parent, false);
                        h.setText(R.id.tv_quotation_info_product_name_fake, isEmpty(bean.getMaterialName()));
                        h.setText(R.id.tv_quotation_info_Design_and_color, isEmpty(bean.getColorName()));
                        h.setText(R.id.tv_quotation_info_length, isEmpty(bean.getLength()));
                        h.setText(R.id.tv_quotation_info_material, isEmpty(bean.getMat()));
                        h.setText(R.id.tv_quotation_info_width, isEmpty(bean.getWidth()));
                        h.setText(R.id.tv_quotation_info_thickness, isEmpty(bean.getHeight()));
                        h.setText(R.id.tv_quotation_info_count, isEmpty(bean.getQty()));
                        h.setText(R.id.tv_quotation_info_area, isEmpty(bean.getArea()));
                        h.setText(R.id.tv_quotation_info_total_area, isEmpty(bean.getTotalArea()));
                        h.setText(R.id.tv_quotation_info_benchmark, isEmpty(bean.getStandardPrice()));
                        h.setText(R.id.tv_quotation_info_retail_price, isEmpty(bean.getSalePrice()));
                        h.setText(R.id.tv_quotation_info_price_before, isEmpty(bean.getFactoryPriceBeDis()));
                        h.setText(R.id.tv_quotation_info_price_after, isEmpty(bean.getFactoryPriceAfDis()));
                        h.setText(R.id.tv_quotation_info_discount_activity, isEmpty(bean.getActivityDis()));
                        h.setText(R.id.tv_quotation_info_discount_amount, isEmpty(bean.getFactoryActivityPrice()));
                        h.setText(R.id.tv_quotation_info_rebate_discount, isEmpty(bean.getRebateDis()));
                        h.setText(R.id.tv_quotation_info_rebate_amount, isEmpty(bean.getRebatePrice()));
                        h.setText(R.id.tv_quotation_info_urgent_fee_emergency, isEmpty(bean.getUrgentPrice()));
                        h.setText(R.id.tv_quotation_info_branding, isEmpty(bean.getMainName()));
                        h.setText(R.id.tv_quotation_info_free_logo, isEmpty(bean.getOrFree()));
                        h.setText(R.id.tv_quotation_info_remark, isEmpty(bean.getRemark()));
                        h.setText(R.id.tv_quotation_info_error_message, isEmpty(bean.getMsg()));
                        break;
                    case "1":
                        h.setVisibility(R.id.ll_quotation_info_content_parent, false);
                        h.setText(R.id.tv_quotation_info_material_fake, isEmpty(bean.getMaterialName()));
                        h.setText(R.id.tv_quotation_info_finish_size, isEmpty(bean.getSize()));
                        h.setText(R.id.tv_quotation_info_finish_benchmark, isEmpty(bean.getStandardPrice()));
                        h.setText(R.id.tv_quotation_info_finish_coefficient_product, isEmpty(bean.getAccountCoefficient()));
                        h.setText(R.id.tv_quotation_info_finish_color, isEmpty(bean.getColorName()));
                        h.setText(R.id.tv_quotation_info_finish_discount, isEmpty(bean.getActivityDis()));
                        h.setText(R.id.tv_quotation_info_finish_price_after, isEmpty(bean.getFactoryPriceAfDis()));
                        h.setText(R.id.tv_quotation_info_finish_price_before, isEmpty(bean.getFactoryPriceBeDis()));
                        h.setText(R.id.tv_quotation_info_finish_unit, isEmpty(bean.getUnit()));
                        h.setText(R.id.tv_quotation_info_finish_retail_price, isEmpty(bean.getSalePrice()));
                        h.setText(R.id.tv_quotation_info_finish_total, isEmpty(bean.getTotalPrice()));
                        h.setText(R.id.tv_quotation_info_finish_count, isEmpty(bean.getQty()));
                        break;
                }


                h.setBackgroundColor(R.id.ll_quotation_info_content_parent, ctx.getResources().getColor(position % 2 == 0 ? R.color.light_text5 : R.color.color_while));
                h.setBackgroundColor(R.id.ll_quotation_info_finish_content_parent, ctx.getResources().getColor(position % 2 == 0 ? R.color.light_text5 : R.color.color_while));

            }
        });
    }


}
