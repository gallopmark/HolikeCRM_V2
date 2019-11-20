package com.holike.crm.fragment.report;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.holike.crm.R;
import com.holike.crm.adapter.AbsFormAdapter;
import com.holike.crm.base.BaseFragment;
import com.holike.crm.bean.ShopAnalysisBean;
import com.holike.crm.dialog.PlainTextTipsDialog;
import com.holike.crm.helper.FormDataHelper;
import com.holike.crm.helper.TextSpanHelper;

import java.util.Date;
import java.util.List;

/**
 * Created by pony on 2019/11/4.
 * Version v3.0 app报表
 * 门店分析
 */
class ShopAnalysisHelper extends AbsReportHelper {
    private Callback mCallback;

    ShopAnalysisHelper(BaseFragment<?, ?> fragment, Callback callback) {
        super(fragment);
        this.mCallback = callback;
        if (getBooleanValue("entry")) {
            doRequest();
        } else {
            requestByDelay();
        }
    }

    @Override
    protected int getTitleId() {
        return R.string.title_shop_analysis;
    }

    @Override
    protected boolean withSubTitle() {
        return false;
    }

    @Override
    public void doRequest() {
        mCallback.onRequest(mTime, mType, mCityCode, mStartDate, mEndDate);
    }

    void onSuccess(final ShopAnalysisBean bean) {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.include_shopanalysis_content, getContainer(), true);
        ((TextView) contentView.findViewById(R.id.tv_time_detail)).setText(bean.time);  //显示时间段
        TextView tvShopDetail = contentView.findViewById(R.id.tv_shop_detail);
        contentView.findViewById(R.id.tv_question_mark).setOnClickListener(view -> showDataDescription(bean.isShop()));
        if (!TextUtils.isEmpty(mSpliceTit)) {
            tvShopDetail.setText(mSpliceTit);
            tvShopDetail.setVisibility(View.VISIBLE);
        } else {
            tvShopDetail.setVisibility(View.INVISIBLE);
        }
        ViewStub viewStub = contentView.findViewById(R.id.viewStub);
        List<ShopAnalysisBean.DataBean> dataList = bean.getDatas();
        if (dataList.isEmpty()) {
            viewStub.setLayoutResource(R.layout.include_empty_textview);
            viewStub.inflate();
        } else {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) viewStub.getLayoutParams();
            lp.leftMargin = mContext.getResources().getDimensionPixelSize(R.dimen.dp_10);
            viewStub.setLayoutResource(R.layout.include_form_data_content);
            View formView = viewStub.inflate();
            FormDataHelper.attachView(formView, getBinder(bean.isShop(), dataList), getCallback(bean.isShop()));
            TextSpanHelper.setSquareText(formView.findViewById(R.id.tv_shop_area));
        }
    }

    private void showDataDescription(boolean isShop) {
        int arrayId = isShop ? R.array.shop_analysis_child_tips : R.array.shop_analysis_tips;
        new PlainTextTipsDialog(mContext).setData(arrayId).show();
    }

    private FormDataHelper.FormDataBinder getBinder(boolean isShop, List<ShopAnalysisBean.DataBean> dataList) {
        final int firstColumnLayoutRes;
        final String sideTitle = mContext.getString(R.string.division);
        final int firstRowLayoutRes;
        RecyclerView.Adapter<?> sideAdapter;
        RecyclerView.Adapter<?> contentAdapter;
        if (isShop) {
            firstColumnLayoutRes = R.layout.include_form_data_column_70dp_h44;
            firstRowLayoutRes = R.layout.include_shopanalysis_child_firstrow;
            sideAdapter = new NextFormAdapter(mContext, dataList, true);
            contentAdapter = new NextFormAdapter(mContext, dataList, false);
        } else {
            firstColumnLayoutRes = R.layout.include_form_data_column_special_h44;
            firstRowLayoutRes = R.layout.include_shopanalysis_firstrow;
            sideAdapter = new DefaultFormAdapter(mContext, dataList, true);
            contentAdapter = new DefaultFormAdapter(mContext, dataList, false);
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

    private FormDataHelper.FormDataCallback getCallback(boolean isShop) {
        if (isShop) {
            return null;
        }
        return new FormDataHelper.SimpleFormDataCallback() {
            @Override
            public void bindFirstColumn(View view) {
                ((TextView) view.findViewById(R.id.tv_second_name)).setText(mContext.getString(R.string.principal));
            }
        };
    }

    private final class DefaultFormAdapter extends AbsFormAdapter<ShopAnalysisBean.DataBean> {
        private boolean mSide;
        private int mLayoutResourceId;

        DefaultFormAdapter(Context context, List<ShopAnalysisBean.DataBean> dataList, boolean isSide) {
            super(context, dataList);
            mSide = isSide;
            mLayoutResourceId = mSide ? R.layout.item_shopanalysis_form_side : R.layout.item_shopanalysis_form_content;
        }

        @Override
        protected void bindViewHolder(RecyclerHolder holder, ShopAnalysisBean.DataBean bean, int position) {
            holder.setText(R.id.tv_division, bean.area); //划分
            holder.setText(R.id.tv_principal, bean.name); //负责人
            holder.setText(R.id.tv_number_stores, bean.storeCount); //门店数
            holder.setText(R.id.tv_number_dealers, bean.dealerCount); //经销商数
            holder.setText(R.id.tv_shop_area, bean.storeArea); //门店面积
            holder.setText(R.id.tv_average_value, bean.storeAge); //平均门店价值
            holder.setText(R.id.tv_performance, bean.money); //业绩
            holder.setText(R.id.tv_effect, bean.plateau); //坪效
            holder.setText(R.id.tv_total_effect, bean.humanTotal); //总人效
            holder.setText(R.id.tv_sales_effect, bean.humanSales); //销售人效
            holder.setText(R.id.tv_customer_price, bean.customerMoney); //客单价
            holder.setText(R.id.tv_number_customers, bean.newCount); //新建客户数
            holder.setText(R.id.tv_number_preScale, bean.preScalesCount); //预约量尺数
            holder.setText(R.id.tv_number_scale, bean.scalesCount); //量尺数
            holder.setText(R.id.tv_number_deposit, bean.earnestCount); //订金数
            holder.setText(R.id.tv_number_signatures, bean.signingCount); //签约数
            setClickableCell(holder, position, R.id.tv_division, mSide && bean.isClickable(), view -> startNextLevel(bean.area, bean.type, bean.cityCode));
        }

        @Override
        protected int bindView(int viewType) {
            return mLayoutResourceId;
        }
    }

    private void startNextLevel(String title, String type, String cityCode) {
        Bundle bundle = new Bundle();
        String tit = TextUtils.isEmpty(title) ? "" : title;
        String spliceTit = TextUtils.isEmpty(mSpliceTit) ? tit : mSpliceTit + "—" + tit;
        bundle.putString("spliceTit", spliceTit);  //把点击的内容传到下一个fragment展示
        bundle.putString("title", title);
        bundle.putString("time", mTime);
        bundle.putString("type", type);
        bundle.putString("cityCode", cityCode);
        bundle.putSerializable("startDate", mStartDate);
        bundle.putSerializable("endDate", mEndDate);
        mFragment.startFragment(new ShopAnalysisFragment(), bundle, true);
    }

    private final class NextFormAdapter extends AbsFormAdapter<ShopAnalysisBean.DataBean> {
        private int mLayoutResourceId;

        NextFormAdapter(Context context, List<ShopAnalysisBean.DataBean> dataList, boolean isSide) {
            super(context, dataList);
            if (isSide) {
                mLayoutResourceId = R.layout.item_shopanalysis_child_form_side;
            } else {
                mLayoutResourceId = R.layout.item_shopanalysis_child_form_content;
            }
        }

        @Override
        protected void bindViewHolder(RecyclerHolder holder, ShopAnalysisBean.DataBean bean, int position) {
            holder.setText(R.id.tv_division, bean.area); //划分
            holder.setText(R.id.tv_performance, bean.money); //业绩
            holder.setText(R.id.tv_effect, bean.plateau); //坪效
            holder.setText(R.id.tv_total_effect, bean.humanTotal); //总人效
            holder.setText(R.id.tv_sales_effect, bean.humanSales); //销售人效
            holder.setText(R.id.tv_payback, bean.receiver); //回款
            holder.setText(R.id.tv_customer_price, bean.customerMoney); //客单价
            holder.setText(R.id.tv_shop_area, bean.storeArea); //门店面积
            holder.setText(R.id.tv_average_value, bean.storeAge); //平均门店价值
            holder.setText(R.id.tv_average_period, bean.day); //平均成交周期(天）
            holder.setText(R.id.tv_satisfaction, bean.evaluate); //客户满意 度(1-5)
            holder.setText(R.id.tv_scale_rate, bean.scalesChangePercent); //量尺转化率
            holder.setText(R.id.tv_deposit_rate, bean.earnestCountPercent); //订金转化率
            holder.setText(R.id.tv_store_entry_rate, bean.signingCountPercent); //进店成交率
            holder.setText(R.id.tv_scale_turnover_rate, bean.scalesCountPercent); //量尺成交率
        }

        @Override
        protected int bindView(int viewType) {
            return mLayoutResourceId;
        }
    }

    interface Callback {
        void onRequest(String time, String type, String cityCode, Date startDate, Date endDate);
    }
}
