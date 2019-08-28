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
import android.widget.LinearLayout;

import com.holike.crm.R;

/**
 * Created by wqj on 2018/3/14.
 * 加载中状态dialog
 */

public class LoadingDialog extends Dialog {
    public LoadingDialog(Context context) {
        super(context, R.style.Dialog);
        setCanceledOnTouchOutside(false);
        setCancelable(true);
        View v = LayoutInflater.from(context).inflate(R.layout.dialog_loading, new LinearLayout(getContext()), false);
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
//        if (getActivity() == null) return super.onCreateDialog(savedInstanceState);
//        View v = LayoutInflater.makeText(getActivity()).inflate(R.layout.dialog_loading, new LinearLayout(getContext()), false);
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
