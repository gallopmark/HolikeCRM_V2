package com.holike.crm.dialog;

import android.content.Context;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.holike.crm.R;

/**
 * Created by wqj on 2018/2/28.
 * 查询账号结果dialog
 */

public class CheckAccountDialog extends CommonDialog {
    private TextView tvDealers;
    private TextView tvAccount;
    private ClickListener listener;

    public CheckAccountDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int bindContentView() {
        return R.layout.dialog_check_account;
    }

    @Override
    protected void initView(View view) {
        tvDealers = mContentView.findViewById(R.id.tv_dialog_check_account_newDealers);
        tvAccount = mContentView.findViewById(R.id.tv_dialog_check_account_newAccount);
        TextView btnLogin = mContentView.findViewById(R.id.btn_dialog_check_account_login);
        btnLogin.setOnClickListener(v -> {
            if (listener != null) {
                dismiss();
                listener.login();
            }
        });
    }

    public CheckAccountDialog setData(String dealers, String account) {
        String textDealers = mContext.getString(R.string.checkAccount_tips1);
        if (!TextUtils.isEmpty(dealers)) {
            textDealers += dealers;
        }
        tvDealers.setText(textDealers);
        String textAccount = mContext.getString(R.string.checkAccount_tips2);
        if (!TextUtils.isEmpty(account)) {
            textAccount += account;
        }
        tvAccount.setText(textAccount);
        return this;
    }

    public CheckAccountDialog setListener(ClickListener listener) {
        this.listener = listener;
        return this;
    }

    public interface ClickListener {
        void login();
    }
}
