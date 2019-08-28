package com.holike.crm.dialog;

import android.content.Context;

import androidx.annotation.NonNull;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.holike.crm.R;

/**
 * Created by wqj on 2018/9/3.
 * 撤销dialog
 */

public class CancelDialog extends CommonDialog {
    private EditText etContent;
    private ClickListener listener;

    public CancelDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int bindContentView() {
        return R.layout.dialog_cancel;
    }

    @Override
    protected void initView(View view) {
        etContent = mContentView.findViewById(R.id.et_number);
        TextView btnLeft = mContentView.findViewById(R.id.btn_dialog_cancel_left);
        TextView btnRight = mContentView.findViewById(R.id.btn_dialog_cancel_right);
        btnLeft.setOnClickListener(v -> dismiss());
        btnRight.setOnClickListener(v -> {
            if (listener != null) {
                dismiss();
                listener.onClick(etContent.getText().toString());
            }
        });
    }

    public CancelDialog setListener(ClickListener listener) {
        this.listener = listener;
        return this;
    }

    public interface ClickListener {
        void onClick(String string);
    }
}
