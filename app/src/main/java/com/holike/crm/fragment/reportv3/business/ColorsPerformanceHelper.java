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

class ColorsPerformanceHelper extends BusinessReferenceHelper {

    ColorsPerformanceHelper(BaseFragment<?, ?> fragment) {
        super(fragment);
        onSuccess("");
    }

    @Override
    void onHttpRequest() {

    }

    @Override
    int getFirstRowLayoutResource() {
        return R.layout.include_firstrow_colorsperformance;
    }

    @Override
    void requestUpdate(@NonNull Object obj, TextView tvDatetime, TextView tvShop, RecyclerView rvTab, RecyclerView rvContent) {
        List<Bean> dataList = new ArrayList<>();
        dataList.add(new Bean("1", "贝加尔灰橡", "300.63", "98.4%"));
        dataList.add(new Bean("2", "密苏里胡桃", "100", "98.4%"));
        dataList.add(new Bean("3", "淡妃布纹", "60", "98.4%"));
        dataList.add(new Bean("4", "简雅", "50", "-"));
        dataList.add(new Bean("5", "格调白", "11", "-"));
        dataList.add(new Bean("6", "西子红", "22", "-"));
        dataList.add(new Bean("7", "北欧阳光", "33", "-"));
        dataList.add(new Bean("8", "白宫经典", "44", "-"));
        dataList.add(new Bean("9", "雅士白", "44", "-"));
        dataList.add(new Bean("10", "峡谷樱桃", "55", "-"));
        dataList.add(new Bean("11", "白沙榆", "66", "-"));
        dataList.add(new Bean("12", "杏白", "77", "-"));
        dataList.add(new Bean("13", "柏林岁月", "88", "-"));
        dataList.add(new Bean("14", "爵士白", "99", "-"));
        dataList.add(new Bean("15", "金科楠", "110", "-"));
        rvContent.setAdapter(new AbsAdapter(mContext, dataList));
    }

    static class Bean {
        String ranking;
        String color;
        String performance;
        String proportion;

        Bean(String ranking, String color, String performance, String proportion) {
            this.ranking = ranking;
            this.color = color;
            this.performance = performance;
            this.proportion = proportion;
        }
    }

    private final class AbsAdapter extends AbsFormAdapter<Bean> {

        AbsAdapter(Context context, List<Bean> dataList) {
            super(context, dataList);
        }

        @Override
        protected void bindViewHolder(RecyclerHolder holder, Bean bean, int position) {
            holder.setText(R.id.tv_ranking, bean.ranking);
            holder.setText(R.id.tv_colors, bean.color);
            holder.setText(R.id.tv_performance, bean.performance);
            holder.setText(R.id.tv_proportion, bean.proportion);
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_colorsperformance;
        }
    }
}
