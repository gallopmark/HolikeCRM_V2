package com.holike.crm.popupwindown;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;

import java.util.List;

/**
 * Created by pony on 2019/7/15.
 * Copyright holike possess 2019.
 */
public class StringItemPopupWindow extends CommonPopupWindow {

    protected List<String> mStringItems;
    private int mSelectPosition;
    private StringItemAdapter mAdapter;
    private OnMenuItemClickListener mListener;

    public StringItemPopupWindow(Context context, @NonNull List<String> item) {
        this(context, item, -1);
    }

    public StringItemPopupWindow(Context context, @NonNull List<String> items, int selectPosition) {
        this(context, items, selectPosition, 0);
    }

    public StringItemPopupWindow(Context context, @NonNull List<String> items, int selectPosition, int bottomMargin) {
        super(context);
        this.mStringItems = items;
        this.mSelectPosition = selectPosition;
        setBottomMargin(bottomMargin);
        initView();
    }

    private void initView() {
        RecyclerView recyclerView = mContentView.findViewById(R.id.mRecyclerView);
        if (mStringItems.size() > 5) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) recyclerView.getLayoutParams();
            Resources resources = mContext.getResources();
            params.height = ((resources.getDimensionPixelSize(R.dimen.dp_40) + resources.getDimensionPixelSize(R.dimen.dp_0_5)) * 5);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new StringItemAdapter(mContext, mStringItems);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, holder, view, position) -> {
            if (position >= 0 && position < mStringItems.size()) {
                setSelectPosition(position);
                if (mListener != null) {
                    mListener.onMenuItemClick(StringItemPopupWindow.this, position, mStringItems.get(position));
                }
            }
        });
        mContentView.setOnClickListener(view -> dismiss());
    }

    public void setItems(@NonNull List<String> items, int selectPosition) {
        this.mSelectPosition = selectPosition;
        if (!items.isEmpty()) {
            this.mStringItems.clear();
            this.mStringItems.addAll(items);
            mAdapter.notifyDataSetChanged();
        }
    }

    public StringItemPopupWindow setOnMenuItemClickListener(OnMenuItemClickListener listener) {
        this.mListener = listener;
        return this;
    }

    public void setSelectPosition(int selectPosition) {
        this.mSelectPosition = selectPosition;
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public int getWidth() {
        return ViewGroup.LayoutParams.MATCH_PARENT;
    }

    @Override
    public int getHeight() {
        return ViewGroup.LayoutParams.MATCH_PARENT;
    }

    @Override
    public int bindLayoutId() {
        return R.layout.popupwindow_string_menulist;
    }

    private class StringItemAdapter extends CommonAdapter<String> {

        StringItemAdapter(Context context, List<String> mDatas) {
            super(context, mDatas);
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_rv_popup_filter;
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, String content, int position) {
            if (position < getItemCount()) {
                holder.setVisibility(R.id.dv_item_rv_popup_filter, View.VISIBLE);
            } else {
                holder.setVisibility(R.id.dv_item_rv_popup_filter, View.GONE);
            }
            if (position == mSelectPosition) {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.textColor5));
                holder.setTextColor(R.id.tv_item_rv_popup_filter, ContextCompat.getColor(mContext, R.color.color_while));
            } else {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_while));
                holder.setTextColor(R.id.tv_item_rv_popup_filter, ContextCompat.getColor(mContext, R.color.textColor8));
            }
            holder.setText(R.id.tv_item_rv_popup_filter, content);
        }
    }

    public interface OnMenuItemClickListener {
        void onMenuItemClick(StringItemPopupWindow pw, int position, String content);
    }
}
