package com.holike.crm.helper;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import com.holike.crm.R;


/**
 * Created by wqj on 2017/9/28.
 * 绑定输入框与按钮状态
 */

public class BindViewHelper {

    /**
     * 输入指定长度字体颜色变化
     *
     * @param editTexts
     * @param textView
     * @param textLength
     */
    public static void bindTextColorView(TextView textView, int textLength, TextView... editTexts) {
        textChangeListener(textView, -1, -1, R.color.textColor5, R.color.textColor4, textLength, editTexts);
    }


    /**
     * 绑定输入框,按钮背景变化
     *
     * @param editTexts
     * @param textView
     */
    public static void bindBgView(TextView textView, TextView... editTexts) {
        bindBgView(textView, 1, editTexts);
    }

    public static void bindBgView(TextView textView, int length, TextView... editTexts) {
        textChangeListener(textView, R.drawable.bg_btn_login_can_click, R.drawable.bg_btn_login_cannot_click, -1, -1, length, editTexts);
    }


    /**
     * 输入框发生变化时，字体颜色变化
     *
     * @param editText
     * @param textView
     */
    public static void bindEditText(final TextView editText, final TextView textView) {
        editTextChange(editText, new TextChangeLintener() {
            @Override
            public void callBack() {
                changeTextView(true, textView, -1, R.color.textColor5);
            }
        });
    }

    /**
     * 输入框发生变化监听
     */
    public static void textChangeListener(final TextView textView, final int bgClickResourceId, final int bgUnClickResourceId, final int textClickColorId, final int textUnClickColorId, final int textLength, final TextView... editTexts) {
        for (final TextView editText : editTexts) {
            editTextChange(editText, new TextChangeLintener() {
                @Override
                public void callBack() {
                    if (editText.getText().length() >= textLength && isAllInput(editTexts)) {
                        changeTextView(true, textView, bgClickResourceId, textClickColorId);
                    } else {
                        changeTextView(false, textView, bgUnClickResourceId, textUnClickColorId);
                    }
                }
            });
        }
    }

    /**
     * 输入框发生变化
     *
     * @param editText
     * @param lintener
     */
    public static void editTextChange(TextView editText, final TextChangeLintener lintener) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                lintener.callBack();
            }
        });
    }

    /**
     * 判断是否所有输入框都有输入
     *
     * @param editTexts
     * @return
     */
    private static boolean isAllInput(TextView... editTexts) {
        boolean isAllInput = true;
        for (TextView editText : editTexts) {
            if (editText.getText().toString().length() == 0) {
                isAllInput = false;
            }
        }
        return isAllInput;
    }

    /**
     * 改变按钮状态
     *
     * @param canClick
     * @param textView
     * @param bgResourceId
     * @param textColorId
     */
    public static void changeTextView(boolean canClick, TextView textView, int bgResourceId, int textColorId) {
        textView.setClickable(canClick);
        textView.setEnabled(canClick);
        if (bgResourceId > 0) {
            textView.setBackgroundResource(bgResourceId);
        }
        if (textColorId > 0) {
            textView.setTextColor(textView.getContext().getResources().getColor(textColorId));
        }
    }

    /**
     * 输入框发生变化回调接口
     */
    public static interface TextChangeLintener {
        void callBack();
    }
}
