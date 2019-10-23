package com.holike.crm.adapter;

import android.content.Context;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;

import java.util.List;

/*通用表格适配器*/
public abstract class AbsFormAdapter<T> extends CommonAdapter<T> {

    public AbsFormAdapter(Context context, List<T> dataList) {
        super(context, dataList);
    }

    @Override
    public void onBindHolder(RecyclerHolder holder, T t, int position) {
        if (position % 2 == 0) {
            holder.itemView.setBackgroundResource(R.color.textColor24);
        } else {
            holder.itemView.setBackgroundResource(R.color.color_while);
        }
        bindViewHolder(holder, t, position);
    }

    protected abstract void bindViewHolder(RecyclerHolder holder, T t, int position);
}
