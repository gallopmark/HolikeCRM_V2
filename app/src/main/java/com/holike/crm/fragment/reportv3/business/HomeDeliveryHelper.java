package com.holike.crm.fragment.reportv3.business;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.holike.crm.R;
import com.holike.crm.adapter.AbsFormAdapter;
import com.holike.crm.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

class HomeDeliveryHelper extends BusinessReferenceHelper {

    HomeDeliveryHelper(BaseFragment<?, ?> fragment) {
        super(fragment);
        timer();
    }

    private void timer() {
        mContainerLayout.postDelayed(() -> onSuccess(""), 1000);
    }

    @Override
    void onHttpRequest() {

    }

    @Override
    int getFirstRowLayoutResource() {
        return R.layout.include_firstrow_homedelivery;
    }

    @Override
    void requestUpdate(@NonNull Object obj, TextView tvDatetime, TextView tvShop, RecyclerView rvTab, RecyclerView rvContent) {
        List<Bean> dataList = new ArrayList<>();
        dataList.add(new Bean("-", "合计", "500", "98.4%", "100"));
        dataList.add(new Bean("1", "沙发", "100", "98.4%", "100"));
        dataList.add(new Bean("2", "餐桌", "60", "98.4%", "100"));
        dataList.add(new Bean("3", "餐椅", "55", "98.4%", "100"));
        dataList.add(new Bean("4", "床", "50", "98.4%", "100"));
        dataList.add(new Bean("5", "床垫", "66", "98.4%", "100"));
        dataList.add(new Bean("6", "休闲椅", "26", "98.4%", "100"));
        dataList.add(new Bean("7", "茶几", "26", "98.4%", "100"));
        dataList.add(new Bean("8", "角几", "22", "98.4%", "100"));
        dataList.add(new Bean("9", "窗帘", "33", "98.4%", "100"));
        dataList.add(new Bean("10", "书椅", "44", "98.4%", "100"));
        dataList.add(new Bean("11", "沙发", "55", "98.4%", "100"));
        dataList.add(new Bean("12", "餐桌", "66", "98.4%", "100"));
        dataList.add(new Bean("13", "餐椅", "77", "98.4%", "100"));
        dataList.add(new Bean("14", "床", "88", "98.4%", "100"));
        dataList.add(new Bean("15", "床垫", "99", "98.4%", "100"));
        dataList.add(new Bean("16", "休闲椅", "11", "98.4%", "100"));
        dataList.add(new Bean("17", "茶几", "12", "98.4%", "100"));
        dataList.add(new Bean("18", "窗帘", "23", "98.4%", "100"));
        dataList.add(new Bean("19", "书桌", "34", "98.4%", "100"));
        dataList.add(new Bean("20", "橱柜", "56", "98.4%", "100"));
        dataList.add(new Bean("21", "衣柜", "67", "98.4%", "100"));
        dataList.add(new Bean("22", "木门", "78", "98.4%", "100"));
        rvContent.setAdapter(new FormDataAdapter(mContext, dataList));
    }

    static class Bean {
        String ranking;
        String category;
        String performance;
        String proportion;
        String salesVolume;

        Bean(String ranking, String category, String performance, String proportion, String salesVolume) {
            this.ranking = ranking;
            this.category = category;
            this.performance = performance;
            this.proportion = proportion;
            this.salesVolume = salesVolume;
        }
    }

    private final class FormDataAdapter extends AbsFormAdapter<Bean> {

        FormDataAdapter(Context context, List<Bean> dataList) {
            super(context, dataList);
        }

        @Override
        protected void bindViewHolder(RecyclerHolder holder, Bean bean, int position) {
            holder.setText(R.id.tv_ranking, bean.ranking);
            holder.setText(R.id.tv_category, bean.category);
            holder.setText(R.id.tv_performance, bean.performance);
            holder.setText(R.id.tv_proportion, bean.proportion);
            holder.setText(R.id.tv_sales_volume, bean.salesVolume);
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_homedelivery_performance;
        }
    }

    interface Callback {

    }
}
