package com.holike.crm.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.bean.DictionaryBean;

import java.util.List;

/**
 * Created by pony on 2019/7/25.
 * Copyright holike possess 2019.
 */
public class SingleChoiceAdapter extends CommonAdapter<DictionaryBean> {
    private int mSelectPosition = -1;
    private OnSingleChoiceListener mSingleChoiceListener;

    public SingleChoiceAdapter(Context context, List<DictionaryBean> mDatas) {
        super(context, mDatas);
    }

    public SingleChoiceAdapter(Context context, List<DictionaryBean> mDatas,int selectPosition) {
        super(context, mDatas);
        this.mSelectPosition = selectPosition;
    }

    public void setSingleChoiceListener(OnSingleChoiceListener listener) {
        this.mSingleChoiceListener = listener;
    }

    @Override
    protected int bindView(int viewType) {
        return R.layout.item_multiplechoice;
    }

    @Override
    public void onBindHolder(RecyclerHolder holder, final DictionaryBean bean, int position) {
        ImageView ivSelect = holder.obtainView(R.id.iv_select);
        TextView tvContent = holder.obtainView(R.id.tv_content);
        if (mSelectPosition == position) {
            ivSelect.setImageResource(R.drawable.ic_radio_button_checked);
            tvContent.setTextColor(ContextCompat.getColor(mContext, R.color.textColor4));
        } else {
            ivSelect.setImageResource(R.drawable.ic_radio_button_unchecked);
            tvContent.setTextColor(ContextCompat.getColor(mContext, R.color.textColor6));
        }
        tvContent.setText(bean.name);
        holder.itemView.setOnClickListener(view -> setSelectPosition(position));
    }

    private void setSelectPosition(int position) {
        this.mSelectPosition = position;
        notifyDataSetChanged();
        if (mSingleChoiceListener != null) {
            mSingleChoiceListener.onSingleChoice(mDatas.get(mSelectPosition));
        }
    }

    public interface OnSingleChoiceListener {
        void onSingleChoice(DictionaryBean bean);
    }
}
