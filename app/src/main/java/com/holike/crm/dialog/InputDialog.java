package com.holike.crm.dialog;

import android.content.Context;

import androidx.annotation.NonNull;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.holike.crm.R;
import com.holike.crm.util.KeyBoardUtil;

public class InputDialog extends CommonDialog implements View.OnClickListener {
    private TextView tvReceivePackCount;
    private EditText etNumber;
    private OnInputListener inputListener;

    public InputDialog(@NonNull Context context) {
        super(context);
    }

    public InputDialog(@NonNull Context context, OnInputListener inputListener) {
        super(context);
        this.inputListener = inputListener;
    }

    @Override
    protected int bindContentView() {
        return R.layout.dialog_input;
    }

    @Override
    protected void initView(View view) {
        tvReceivePackCount = mContentView.findViewById(R.id.tv_dialog_cancel_title_content);
        etNumber = mContentView.findViewById(R.id.et_number);
        TextView tvConfirm = mContentView.findViewById(R.id.btn_dialog_cancel_right);
        TextView tvCancel = mContentView.findViewById(R.id.btn_dialog_cancel_left);
        tvConfirm.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        String packCount = "0" + mContext.getString(R.string.receiving_scan_result_package);
        tvReceivePackCount.setText(packCount);
    }

    public interface OnInputListener {
        void inputResult(String result);
    }


    public void setReceivePackCount(String s) {
        String textResult = mContext.getString(R.string.receiving_scan_result_package);
        if (!TextUtils.isEmpty(s)) {
            textResult = s + textResult;
        } else {
            textResult = "0" + textResult;
        }
        tvReceivePackCount.setText(textResult);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_dialog_cancel_right:
                if (inputListener != null) {
                    inputListener.inputResult(etNumber.getText().toString());
                    etNumber.setText("");
                }
                break;
            case R.id.btn_dialog_cancel_left:
                KeyBoardUtil.hideKeyboard(v);
                dismiss();
                break;
        }
    }
}
