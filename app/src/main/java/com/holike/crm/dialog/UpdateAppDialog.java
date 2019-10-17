package com.holike.crm.dialog;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.TextView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.bean.UpdateBean;
import com.holike.crm.util.AppUtils;

import java.util.List;

/**
 * Created by wqj on 2018/2/8.
 * 版本更新dialog
 */

public class UpdateAppDialog extends CommonDialog {
    private OnUpdateButtonClickListener updateButtonClickListener;

    public UpdateAppDialog(@NonNull Context context, UpdateBean updateBean) {
        super(context, R.style.Dialog);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        setup(updateBean);
    }

    public UpdateAppDialog setUpdateButtonClickListener(OnUpdateButtonClickListener updateButtonClickListener) {
        this.updateButtonClickListener = updateButtonClickListener;
        return this;
    }

    @Override
    protected int bindContentView() {
        return R.layout.dialog_update_app;
    }

    private void setup(UpdateBean updateBean) {
        TextView tvCurrentVersion = mContentView.findViewById(R.id.tv_current_version);
        String currentVersion = mContext.getString(R.string.tips_current_version) + AppUtils.getVersionName(true);
        tvCurrentVersion.setText(currentVersion);
        TextView tvNewestVersion = mContentView.findViewById(R.id.tv_newest_version);
        RecyclerView recyclerView = mContentView.findViewById(R.id.rv_message);
        TextView tvUpdate = mContentView.findViewById(R.id.tv_update_now);
        if (updateBean.getDetailArray() != null && !updateBean.getDetailArray().isEmpty()) {
            TipsAdapter adapter = new TipsAdapter(getContext(), updateBean.getDetailArray());
            recyclerView.setAdapter(adapter);
        }
        String source = mContext.getString(R.string.dialog_update_version);
        if (!TextUtils.isEmpty(updateBean.getActualversion())) {
            source += updateBean.getActualversion();
        }
        tvNewestVersion.setText(source);
        tvUpdate.setOnClickListener(view -> {
            if (updateButtonClickListener != null) {
                updateButtonClickListener.onClick(UpdateAppDialog.this);
            }
        });
    }

    @Override
    public Drawable getBackgroundDrawable() {
        return ContextCompat.getDrawable(mContext, R.drawable.bg_corners_white_5dp);
    }

    private static class TipsAdapter extends CommonAdapter<String> {

        TipsAdapter(Context context, List<String> mDatas) {
            super(context, mDatas);
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_version_update;
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, String content, int position) {
            holder.setText(R.id.mTipsTv, content);
        }
    }

    public interface OnUpdateButtonClickListener {
        void onClick(UpdateAppDialog dialog);
    }
}
