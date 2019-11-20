package com.holike.crm.adapter;

import android.content.Context;
import android.view.View;

import androidx.annotation.IdRes;

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

    protected void setClickableCell(RecyclerHolder holder, int position, @IdRes int cellId, boolean isClickable, View.OnClickListener listener) {
        holder.setTextColorRes(cellId, isClickable ? R.color.colorAccent : (position == 0 ? R.color.textColor4 : R.color.textColor8));
        holder.setEnabled(cellId, isClickable);
        holder.setOnClickListener(cellId, listener);
    }

    protected abstract void bindViewHolder(RecyclerHolder holder, T t, int position);
}
