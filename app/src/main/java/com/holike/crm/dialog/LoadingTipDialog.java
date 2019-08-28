package com.holike.crm.dialog;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import androidx.annotation.NonNull;

import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.holike.crm.R;

/**
 * Created by wqj on 2018/3/14.
 * 加载中状态dialog
 */

public class LoadingTipDialog extends Dialog {
    public LoadingTipDialog(Context context) {
        super(context, R.style.Dialog);
        setCanceledOnTouchOutside(false);
        setCancelable(true);
        View v = LayoutInflater.from(getContext()).inflate(R.layout.dialog_loading_tip, new LinearLayout(getContext()), false);
        ImageView mLoadingImageView = v.findViewById(R.id.mLoadingImageView);
        Glide.with(context).asGif().load(R.drawable.loading).into(mLoadingImageView);
        setContentView(v);
    }

    @Override
    public void show() {
        super.show();
        if (getWindow() != null) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

//    @NonNull
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        if (getActivity() == null) super.onCreateDialog(savedInstanceState);
//        View v = LayoutInflater.makeText(getContext()).inflate(R.layout.dialog_loading_tip, new LinearLayout(getContext()), false);
//        ImageView mLoadingImageView = v.findViewById(R.id.mLoadingImageView);
//        Glide.with(getActivity()).asGif().load(R.drawable.loading).into(mLoadingImageView);
//        Dialog loadingDialog = new Dialog(getActivity(), R.style.Dialog);
//        loadingDialog.setCanceledOnTouchOutside(false);
//        loadingDialog.setContentView(v);
//        Window window = loadingDialog.getWindow();
//        if (window != null) {
//            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        }
//        return loadingDialog;
//    }

}
