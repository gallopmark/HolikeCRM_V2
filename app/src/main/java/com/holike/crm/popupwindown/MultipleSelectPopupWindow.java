package com.holike.crm.popupwindown;

import android.content.Context;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pony on 2019/8/5.
 * Copyright holike possess 2019.
 */
public class MultipleSelectPopupWindow extends CommonPopupWindow {

    public static class Item {
        public String code;
        public String name;

        public Item(String code, String name) {
            this.code = code;
            this.name = name;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) return false;
            if (obj == this) return true;
            if (obj instanceof Item) {
                return TextUtils.equals(code, ((Item) obj).code);
            } else {
                return false;
            }
        }
    }

    private List<Item> mItems;
    private OnMultipleChoiceListener mMultipleChoiceListener;

    @Override
    public int getWidth() {
        return ViewGroup.LayoutParams.MATCH_PARENT;
    }

    @Override
    public int getHeight() {
        return ViewGroup.LayoutParams.MATCH_PARENT;
    }

    @Override
    public int getBottomMargin() {
        return mContext.getResources().getDimensionPixelSize(R.dimen.dp_50);
    }

    @Override
    public int bindLayoutId() {
        return R.layout.popupwindow_multiple_select;
    }

    public MultipleSelectPopupWindow(Context context, List<Item> items) {
        this(context, items, null);
    }

    public MultipleSelectPopupWindow(Context context, List<Item> items, List<Item> selectItems) {
        this(context, items, selectItems, 0);
    }

    public MultipleSelectPopupWindow(Context context, List<Item> items, List<Item> selectItems, int bottomMargin) {
        super(context);
        this.mItems = items;
        initView(selectItems);
        setBottomMargin(bottomMargin);
    }

    private void initView(List<Item> selectItems) {
        RecyclerView recyclerView = mContentView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
        final MultipleSelectAdapter adapter = new MultipleSelectAdapter(mContext, mItems, selectItems);
        recyclerView.setAdapter(adapter);
        TextView tvCancel = mContentView.findViewById(R.id.tv_negative);
        tvCancel.setOnClickListener(view -> dismiss());
        TextView tvConfirm = mContentView.findViewById(R.id.tv_positive);
        tvConfirm.setOnClickListener(view -> {
            if (mMultipleChoiceListener != null) {
                mMultipleChoiceListener.onMultipleChoice(adapter.mSelectedItems);
            }
            dismiss();
        });
        mContentView.findViewById(R.id.view_outside).setOnClickListener(view -> dismiss());
    }

    class MultipleSelectAdapter extends CommonAdapter<Item> {
        private List<Item> mSelectedItems;

        MultipleSelectAdapter(Context context, List<Item> mDatas, List<Item> selectItems) {
            super(context, mDatas);
            if (selectItems != null) {
                mSelectedItems = new ArrayList<>(selectItems);
            } else {
                mSelectedItems = new ArrayList<>();
            }
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.popupwindow_item_selectable;
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, Item item, int position) {
            ImageView ivSelect = holder.obtainView(R.id.iv_select);
            TextView tvContent = holder.obtainView(R.id.tv_content);
            setChecked(ivSelect, tvContent, mSelectedItems.contains(item));
            holder.itemView.setOnClickListener(view -> {
                if (mSelectedItems.contains(item)) {
                    mSelectedItems.remove(item);
                    setChecked(ivSelect, tvContent, false);
                } else {
                    mSelectedItems.add(item);
                    setChecked(ivSelect, tvContent, true);
                }
            });
            tvContent.setText(item.name);
        }

        private void setChecked(ImageView iv, TextView tv, boolean isChecked) {
            if (isChecked) {
                iv.setImageResource(R.drawable.cus_scale_space_sel);
                tv.setTextColor(ContextCompat.getColor(mContext, R.color.textColor4));
            } else {
                iv.setImageResource(R.drawable.cus_scale_space_nor);
                tv.setTextColor(ContextCompat.getColor(mContext, R.color.textColor6));
            }
        }
    }

    public void setMultipleChoiceListener(OnMultipleChoiceListener multipleChoiceListener) {
        this.mMultipleChoiceListener = multipleChoiceListener;
    }

    public interface OnMultipleChoiceListener {
        void onMultipleChoice(List<Item> selectedItems);
    }
}
