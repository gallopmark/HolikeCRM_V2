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

class SeriesPerformanceHelper extends BusinessReferenceHelper {

    SeriesPerformanceHelper(BaseFragment<?, ?> fragment) {
        super(fragment);
        onSuccess("");
    }

    @Override
    void onHttpRequest() {

    }

    @Override
    int getFirstRowLayoutResource() {
        return R.layout.include_firstrow_seriesperformance;
    }

    @Override
    void requestUpdate(@NonNull Object obj, TextView tvDatetime, TextView tvShop, RecyclerView rvTab, RecyclerView rvContent) {
        List<Bean> dataList = new ArrayList<>();
        dataList.add(new Bean("1", "北欧风情", "300.63", "98.4%"));
        dataList.add(new Bean("2", "阿尔卑斯", "100", "98.4%"));
        dataList.add(new Bean("3", "那不勒斯轻奢", "60", "98.4%"));
        dataList.add(new Bean("4", "格瑞拾光", "50", "-"));
        dataList.add(new Bean("5", "贝加尔湖畔", "11", "-"));
        dataList.add(new Bean("6", "挪威森林", "22", "-"));
        dataList.add(new Bean("7", "蒙特里安", "33", "-"));
        dataList.add(new Bean("8", "天空之城", "44", "-"));
        dataList.add(new Bean("9", "波尔多庄园", "44", "-"));
        dataList.add(new Bean("10", "梵居极简", "55", "-"));
        dataList.add(new Bean("11", "北欧琉年", "66", "-"));
        dataList.add(new Bean("12", "布诺", "77", "-"));
        dataList.add(new Bean("13", "驳影", "88", "-"));
        dataList.add(new Bean("14", "简欧", "99", "-"));
        dataList.add(new Bean("15", "新中式", "110", "-"));
        rvContent.setAdapter(new AbsAdapter(mContext, dataList));
    }

    static class Bean {
        String ranking;
        String series;
        String performance;
        String proportion;

        Bean(String ranking, String series, String performance, String proportion) {
            this.ranking = ranking;
            this.series = series;
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
            holder.setText(R.id.tv_series, bean.series);
            holder.setText(R.id.tv_performance, bean.performance);
            holder.setText(R.id.tv_proportion, bean.proportion);
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_seriesperformance;
        }
    }
}
