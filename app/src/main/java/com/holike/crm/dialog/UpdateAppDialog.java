package com.holike.crm.dialog;

import android.app.Dialog;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.bean.UpdateBean;
import com.holike.crm.util.DensityUtil;

import java.util.List;

/**
 * Created by wqj on 2018/2/8.
 * 版本更新dialog
 */

public class UpdateAppDialog extends Dialog {
    private UpdateBean updateBean;
    private OnUpdateButtonClickListener updateButtonClickListener;

    public UpdateAppDialog(@NonNull Context context, UpdateBean updateBean) {
        super(context, R.style.Dialog);
        this.updateBean = updateBean;
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    public UpdateAppDialog setUpdateButtonClickListener(OnUpdateButtonClickListener updateButtonClickListener) {
        this.updateButtonClickListener = updateButtonClickListener;
        return this;
    }

    @Override
    public void show() {
        super.show();
        if (getWindow() != null) {
            View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_update_app, new ConstraintLayout(getContext()), false);
            TextView mVersionTv = contentView.findViewById(R.id.mVersionTv);
            RecyclerView mMessageRv = contentView.findViewById(R.id.mMessageRv);
            TextView mUpdateBottomTv = contentView.findViewById(R.id.mUpdateBottomTv);
            if (updateBean.getDetailArray() != null && !updateBean.getDetailArray().isEmpty()) {
                TipsAdapter adapter = new TipsAdapter(getContext(), updateBean.getDetailArray());
                mMessageRv.setAdapter(adapter);
            }
            String source = getContext().getString(R.string.dialog_update_version);
            if (!TextUtils.isEmpty(updateBean.getActualversion())) {
                source += updateBean.getActualversion();
            }
            mVersionTv.setText(source);
            mUpdateBottomTv.setOnClickListener(view -> {
                if (updateButtonClickListener != null) {
                    updateButtonClickListener.onClick(UpdateAppDialog.this);
                }
            });
            getWindow().setContentView(contentView);
            int width = (DensityUtil.getScreenWidth(getContext()) - DensityUtil.dp2px(getContext().getResources().getDimension(R.dimen.dp_48)));
            getWindow().setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);
            getWindow().setWindowAnimations(R.style.Dialog_Anim);
            getWindow().setBackgroundDrawableResource(R.drawable.bg_corners_white_5dp);
            getWindow().setGravity(Gravity.CENTER);
        }
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
