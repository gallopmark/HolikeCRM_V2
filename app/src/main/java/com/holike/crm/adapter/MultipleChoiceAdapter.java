package com.holike.crm.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.bean.DictionaryBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gallop on 2019/7/23.
 * Copyright holike possess 2019.
 */
public class MultipleChoiceAdapter extends CommonAdapter<DictionaryBean> {
    private List<DictionaryBean> mSelectedItems;
    private OnMultipleChoiceListener mMultipleChoiceListener;

    public MultipleChoiceAdapter(Context context, List<DictionaryBean> mDatas) {
        this(context, mDatas, null);
    }

    /**
     * @param selectItems 已选的字典集合
     */
    public MultipleChoiceAdapter(Context context, List<DictionaryBean> mDatas, List<DictionaryBean> selectItems) {
        super(context, mDatas);
        if (selectItems != null && !selectItems.isEmpty()) {
            mSelectedItems = new ArrayList<>(selectItems);
        } else {
            mSelectedItems = new ArrayList<>();
        }
    }

    public void setMultipleChoiceListener(OnMultipleChoiceListener multipleChoiceListener) {
        this.mMultipleChoiceListener = multipleChoiceListener;
    }

    public List<DictionaryBean> getSelectedItems() {
        return mSelectedItems;
    }

    @Override
    protected int bindView(int viewType) {
        return R.layout.item_multiplechoice;
    }

    @Override
    public void onBindHolder(RecyclerHolder holder, final DictionaryBean bean, int position) {
        ImageView ivSelect = holder.obtainView(R.id.iv_select);
        TextView tvContent = holder.obtainView(R.id.tv_content);
        setChecked(ivSelect, tvContent, mSelectedItems.contains(bean));
        holder.itemView.setOnClickListener(view -> {
            if (mSelectedItems.contains(bean)) {
                mSelectedItems.remove(bean);
                setChecked(ivSelect, tvContent, false);
            } else {
                mSelectedItems.add(bean);
                setChecked(ivSelect, tvContent, true);
            }
        });
        tvContent.setText(bean.name);
    }

    private void setChecked(ImageView iv, TextView tv, boolean isChecked) {
        if (isChecked) {
            iv.setImageResource(R.drawable.cus_scale_space_sel);
            tv.setTextColor(ContextCompat.getColor(mContext, R.color.textColor4));
        } else {
            iv.setImageResource(R.drawable.cus_scale_space_nor);
            tv.setTextColor(ContextCompat.getColor(mContext, R.color.textColor6));
        }
        if (mMultipleChoiceListener != null) {
            mMultipleChoiceListener.onMultipleChoice(mSelectedItems);
        }
    }

    public interface OnMultipleChoiceListener {
        void onMultipleChoice(List<DictionaryBean> selectedItems);
    }
}
