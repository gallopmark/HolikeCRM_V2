package com.holike.crm.customView;

import android.content.Context;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

public class InputEditText extends androidx.appcompat.widget.AppCompatEditText {
    public InputEditText(Context context) {
        super(context);
        initView();
    }

    public InputEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public InputEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        if (getText() != null && !TextUtils.isEmpty(getText()))
            setSelection(getText().length());
    }

    @Override
    public void addTextChangedListener(TextWatcher watcher) {
        super.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(editable.toString())) {
                    InputEditText.this.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                    InputEditText.this.setTextDirection(View.TEXT_DIRECTION_RTL);
                } else {
                    InputEditText.this.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                    InputEditText.this.setTextDirection(View.TEXT_DIRECTION_LTR);
                }

            }
        });
    }
}
