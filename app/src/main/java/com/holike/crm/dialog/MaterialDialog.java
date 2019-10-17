package com.holike.crm.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.annotation.StyleRes;

import com.holike.crm.R;

/**
 * Created by gallop on 2019/7/24.
 * Copyright holike possess 2019.
 */
public class MaterialDialog extends CommonDialog {
    private CharSequence mTitle;
    private CharSequence mMessage;
    private int mGravity;
    private boolean isDividerEnabled;
    private CharSequence mNegativeText;
    private CharSequence mPositiveText;
    private DialogInterface.OnClickListener mNegativeClickListener;
    private DialogInterface.OnClickListener mPositiveClickListener;

    private CharSequence mNeutralText;
    private DialogInterface.OnClickListener mNeutralClickListener;

    public MaterialDialog(Builder builder) {
        super(builder.mContext, builder.mStyle);
        this.mTitle = builder.mTitle == null ? mContext.getString(R.string.dialog_title_default) : builder.mTitle;
        this.mMessage = builder.mMessage;
        this.mGravity = builder.mGravity;
        this.isDividerEnabled = builder.isDividerEnabled;
        this.mNegativeText = builder.mNegativeText == null ? mContext.getString(R.string.cancel) : builder.mNegativeText;
        this.mNegativeClickListener = builder.mNegativeClickListener;
        this.mPositiveText = builder.mPositiveText == null ? mContext.getString(R.string.confirm) : builder.mPositiveText;
        this.mPositiveClickListener = builder.mPositiveClickListener;
        this.mNeutralText = builder.mNeutralText;
        this.mNeutralClickListener = builder.mNeutralClickListener;
        setup();
    }

    private void setup() {
        setTitle(mTitle);
        setMessage(mMessage);
        setDividerEnabled(isDividerEnabled);
        setButton();
    }

    @Override
    public ViewGroup.MarginLayoutParams getLayoutParams() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(getWidth(), LinearLayout.LayoutParams.WRAP_CONTENT);
        params.topMargin = mContext.getResources().getDimensionPixelSize(R.dimen.dp_50);
        params.bottomMargin = mContext.getResources().getDimensionPixelSize(R.dimen.dp_50);
        return params;
    }

    @Override
    protected int bindContentView() {
        return R.layout.dialog_material;
    }

    @Override
    public void setTitle(@StringRes int titleId) {
        setTitle(mContext.getString(titleId));
    }

    @Override
    public void setTitle(@Nullable CharSequence title) {
        ((TextView) mContentView.findViewById(R.id.tv_title)).setText(title);
    }

    public void setMessage(@StringRes int messageId) {
        setMessage(mContext.getString(messageId));
    }

    public void setMessage(@Nullable CharSequence message) {
        TextView tvMessage = mContentView.findViewById(R.id.tv_message);
        tvMessage.setGravity(mGravity);
        tvMessage.setText(message);
        tvMessage.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    public void setDividerEnabled(boolean isEnabled) {
        View vDivider = mContentView.findViewById(R.id.v_divider);
        vDivider.setVisibility(isEnabled ? View.VISIBLE : View.GONE);
    }

    private void setButton() {
        TextView tvNeutral = mContentView.findViewById(R.id.tv_neutral);
        if (!TextUtils.isEmpty(mNeutralText)) {
            mContentView.findViewById(R.id.tv_negative).setVisibility(View.GONE);
            mContentView.findViewById(R.id.tv_positive).setVisibility(View.GONE);
            tvNeutral.setVisibility(View.VISIBLE);
            tvNeutral.setText(mNeutralText);
            tvNeutral.setOnClickListener(view -> {
                if (mNeutralClickListener != null) {
                    mNeutralClickListener.onClick(MaterialDialog.this, DialogInterface.BUTTON_NEUTRAL);
                }
            });
        } else {
            tvNeutral.setVisibility(View.GONE);
            setNegativeButton(mNegativeText, mNegativeClickListener);
            setPositiveButton(mPositiveText, mPositiveClickListener);
        }
    }

    private void setNegativeButton(@Nullable CharSequence text, @Nullable final DialogInterface.OnClickListener listener) {
        TextView tvNegative = mContentView.findViewById(R.id.tv_negative);
        if (!TextUtils.isEmpty(text)) {
            tvNegative.setVisibility(View.VISIBLE);
            tvNegative.setText(text);
            tvNegative.setOnClickListener(view -> {
                if (listener != null) {
                    listener.onClick(MaterialDialog.this, DialogInterface.BUTTON_NEGATIVE);
                } else {
                    dismiss();
                }
            });
        } else {
            tvNegative.setVisibility(View.GONE);
        }
    }

    private void setPositiveButton(@Nullable CharSequence text, @Nullable final DialogInterface.OnClickListener listener) {
        TextView tvPositive = mContentView.findViewById(R.id.tv_positive);
        if (!TextUtils.isEmpty(text)) {
            tvPositive.setVisibility(View.VISIBLE);
            tvPositive.setText(text);
            tvPositive.setOnClickListener(view -> {
                if (listener != null) {
                    listener.onClick(MaterialDialog.this, DialogInterface.BUTTON_POSITIVE);
                } else {
                    dismiss();
                }
            });
        } else {
            tvPositive.setVisibility(View.GONE);
        }
    }

    public static class Builder {
        private Context mContext;
        private int mStyle;
        private CharSequence mTitle;
        private CharSequence mMessage;
        private int mGravity = Gravity.CENTER;
        private boolean isDividerEnabled = true;
        private CharSequence mNegativeText;
        private CharSequence mPositiveText;
        private DialogInterface.OnClickListener mNegativeClickListener;
        private DialogInterface.OnClickListener mPositiveClickListener;

        private CharSequence mNeutralText;
        private DialogInterface.OnClickListener mNeutralClickListener;

        public Builder(@NonNull Context context) {
            this(context, R.style.Dialog);
        }

        public Builder(@NonNull Context context, @StyleRes int style) {
            this.mContext = context;
            this.mStyle = style;
        }

        public Builder title(@StringRes int titleId) {
            return title(mContext.getString(titleId));
        }

        public Builder title(@Nullable CharSequence title) {
            this.mTitle = title;
            return this;
        }

        public Builder message(@StringRes int messageId) {
            return message(mContext.getString(messageId));
        }

        public Builder message(@Nullable CharSequence message) {
            this.mMessage = message;
            return this;
        }

        public Builder messageGravity(int gravity) {
            this.mGravity = gravity;
            return this;
        }

        public Builder dividerEnabled(boolean dividerEnabled) {
            this.isDividerEnabled = dividerEnabled;
            return this;
        }

        public Builder negativeButton(@StringRes int negativeTextId, @Nullable DialogInterface.OnClickListener listener) {
            return negativeButton(mContext.getString(negativeTextId), listener);
        }

        public Builder negativeButton(@Nullable CharSequence negativeText, @Nullable DialogInterface.OnClickListener listener) {
            this.mNegativeText = negativeText;
            this.mNegativeClickListener = listener;
            return this;
        }

        public Builder positiveButton(@StringRes int positiveTextId, @Nullable DialogInterface.OnClickListener listener) {
            return positiveButton(mContext.getString(positiveTextId), listener);
        }

        public Builder positiveButton(@Nullable CharSequence positiveText, @Nullable DialogInterface.OnClickListener listener) {
            this.mPositiveText = positiveText;
            this.mPositiveClickListener = listener;
            return this;
        }

        public Builder neutralButton(@StringRes int neutralTextId, @Nullable DialogInterface.OnClickListener listener) {
            return neutralButton(mContext.getString(neutralTextId), listener);
        }

        public Builder neutralButton(@Nullable CharSequence neutralText, @Nullable DialogInterface.OnClickListener listener) {
            this.mNeutralText = neutralText;
            this.mNeutralClickListener = listener;
            return this;
        }

        public MaterialDialog build() {
            return new MaterialDialog(this);
        }

        public void show() {
            build().show();
        }
    }
}
