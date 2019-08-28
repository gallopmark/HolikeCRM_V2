package com.holike.crm.dialog;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.util.DensityUtil;

import java.util.List;

public class TipViewDialog extends Dialog {

    private List<String> items;
    private OnItemClickListener onItemClickListener;

    public TipViewDialog(Context context) {
        super(context, R.style.Dialog);
    }

    public TipViewDialog setItems(@NonNull List<String> items) {
        this.items = items;
        return this;
    }

    public TipViewDialog setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        return this;
    }

    @Override
    public void show() {
        super.show();
        if (getWindow() != null) {
            View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_tipview, new LinearLayout(getContext()), false);
            RecyclerView recyclerView = contentView.findViewById(R.id.mRecyclerView);
            TipViewAdapter mAdapter = new TipViewAdapter(getContext(), items);
            recyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener((adapter, holder, view, position) -> {
                if (position >= 0 && position < items.size()) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(TipViewDialog.this, items, position);
                    }
                }
            });
            getWindow().setContentView(contentView);
            int width = DensityUtil.getScreenWidth(getContext()) - getContext().getResources().getDimensionPixelSize(R.dimen.dp_48) * 2;
            getWindow().setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);
            getWindow().setBackgroundDrawableResource(R.drawable.bg_corners_white_5dp);
            getWindow().setGravity(Gravity.CENTER);
        }
    }

    private static class TipViewAdapter extends CommonAdapter<String> {

        TipViewAdapter(Context context, List<String> mDatas) {
            super(context, mDatas);
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_dialog_tipview;
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, String content, int position) {
            holder.setText(R.id.mTipsView, content);
            if (position == 0) {
                holder.setBackgroundResource(R.id.mTipsView, R.drawable.bg_dialog_item_top_selector);
            } else if (position == getItemCount() - 1) {
                holder.setBackgroundResource(R.id.mTipsView, R.drawable.bg_dialog_item_bottom_selector);
            } else {
                holder.setBackgroundResource(R.id.mTipsView, R.drawable.bg_dialog_item_center_selector);
            }
            if (position == getItemCount() - 1) {
                holder.setVisibility(R.id.vDivider, View.GONE);
            } else {
                holder.setVisibility(R.id.vDivider, View.VISIBLE);
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(TipViewDialog dialog, List<String> items, int position);
    }
}
