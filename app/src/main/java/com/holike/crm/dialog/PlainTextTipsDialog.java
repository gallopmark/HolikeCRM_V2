package com.holike.crm.dialog;

import android.content.Context;

import androidx.annotation.ArrayRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by pony on 2019/11/1.
 * Version v3.0 app报表
 * 纯文本提示框
 */
public class PlainTextTipsDialog extends CommonDialog {

    public PlainTextTipsDialog(Context context) {
        super(context);
        mContentView.findViewById(R.id.tv_know).setOnClickListener(view -> dismiss());
    }

    @Override
    protected int bindContentView() {
        return R.layout.dialog_plain_text_tips;
    }

    public PlainTextTipsDialog setData(@ArrayRes int arrayId) {
        return setData(Arrays.asList(mContext.getResources().getStringArray(arrayId)));
    }

    public PlainTextTipsDialog setData(String[] textArray) {
        return setData(Arrays.asList(textArray));
    }

    public PlainTextTipsDialog setData(@NonNull List<String> dataList) {
        RecyclerView recyclerView = mContentView.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(new PlainTextAdapter(mContext, dataList));
        return this;
    }

    private static final class PlainTextAdapter extends CommonAdapter<String> {

        PlainTextAdapter(Context context, List<String> mDatas) {
            super(context, mDatas);
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.dialog_item_plain_text;
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, String text, int position) {
            holder.setText(R.id.tv_title, text);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
            if (position == getItemCount() - 1) {
                params.bottomMargin = 0;
            } else {
                params.bottomMargin = mContext.getResources().getDimensionPixelSize(R.dimen.dp_16);
            }
        }
    }
}
