package com.holike.crm.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
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
    private View mCustomView;
    private boolean isDividerEnabled;
    private CharSequence mNegativeText;
    private CharSequence mPositiveText;
    private DialogInterface.OnClickListener mNegativeClickListener;
    private DialogInterface.OnClickListener mPositiveClickListener;

    public MaterialDialog(Builder builder) {
        super(builder.mContext, builder.mStyle);
        this.mTitle = builder.mTitle == null ? mContext.getString(R.string.dialog_title_default) : builder.mTitle;
        this.mMessage = builder.mMessage;
        this.mGravity = builder.mGravity;
        this.mCustomView = builder.mCustomView;
        this.isDividerEnabled = builder.isDividerEnabled;
        this.mNegativeText = builder.mNegativeText == null ? mContext.getString(R.string.cancel) : builder.mNegativeText;
        this.mNegativeClickListener = builder.mNegativeClickListener;
        this.mPositiveText = builder.mPositiveText == null ? mContext.getString(R.string.confirm) : builder.mPositiveText;
        this.mPositiveClickListener = builder.mPositiveClickListener;
        setup();
    }

    private void setup() {
        setTitle(mTitle);
        setMessage(mMessage);
        setCustomContentView(mCustomView);
        setDividerEnabled(isDividerEnabled);
        setNegativeButton(mNegativeText, mNegativeClickListener);
        setPositiveButton(mPositiveText, mPositiveClickListener);
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
        if (tvMessage != null) {
            tvMessage.setGravity(mGravity);
            tvMessage.setText(message);
        }
    }

    public void setCustomContentView(@Nullable View view) {
        if (view == null) return;
        LinearLayout llContentLayout = mContentView.findViewById(R.id.ll_content_layout);
        if (llContentLayout.getChildCount() > 1) {
            llContentLayout.removeViewAt(1);
        }
        llContentLayout.addView(view);
    }

    public void setDividerEnabled(boolean isEnabled) {
        View vDivider = mContentView.findViewById(R.id.v_divider);
        vDivider.setVisibility(isEnabled ? View.VISIBLE : View.GONE);
    }

    public void setNegativeButton(CharSequence text, @Nullable final DialogInterface.OnClickListener listener) {
        TextView tvNegative = mContentView.findViewById(R.id.tv_negative);
        if (TextUtils.isEmpty(text)) {
            tvNegative.setVisibility(View.INVISIBLE);
        } else {
            tvNegative.setVisibility(View.VISIBLE);
            tvNegative.setText(text);
            tvNegative.setOnClickListener(view -> {
                if (listener != null) {
                    listener.onClick(MaterialDialog.this, DialogInterface.BUTTON_NEGATIVE);
                } else {
                    dismiss();
                }
            });
        }
    }

    public void setPositiveButton(CharSequence text, @Nullable final DialogInterface.OnClickListener listener) {
        TextView tvPositive = mContentView.findViewById(R.id.tv_positive);
        if (TextUtils.isEmpty(text)) {
            tvPositive.setVisibility(View.INVISIBLE);
        } else {
            tvPositive.setText(text);
            tvPositive.setOnClickListener(view -> {
                if (listener != null) {
                    listener.onClick(MaterialDialog.this, DialogInterface.BUTTON_POSITIVE);
                } else {
                    dismiss();
                }
            });
        }
    }

    public static class Builder {
        private Context mContext;
        private int mStyle;
        private CharSequence mTitle;
        private CharSequence mMessage;
        private int mGravity = Gravity.CENTER;
        private View mCustomView;
        private boolean isDividerEnabled = true;
        private CharSequence mNegativeText;
        private CharSequence mPositiveText;
        private DialogInterface.OnClickListener mNegativeClickListener;
        private DialogInterface.OnClickListener mPositiveClickListener;

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

        public Builder customView(@Nullable View view) {
            this.mCustomView = view;
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

        public MaterialDialog build() {
            return new MaterialDialog(this);
        }

        public void show() {
            build().show();
        }
    }
}
