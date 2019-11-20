package com.holike.crm.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.contrarywind.adapter.WheelAdapter;
import com.contrarywind.view.WheelView;
import com.holike.crm.R;
import com.holike.crm.bean.DictionaryBean;

import java.util.ArrayList;
import java.util.List;

public class OptionsPickerDialog extends CommonDialog {
    private List<DictionaryBean> mDataList;
    private WheelView mOptionWv;
    private OnOptionPickerListener mListener;

    public OptionsPickerDialog(Context context) {
        super(context);
        mDataList = new ArrayList<>();
    }

    @Override
    protected void initView(View contentView) {
        mOptionWv = contentView.findViewById(R.id.wv_options);
        mOptionWv.setCyclic(false);
        setNegativeButton(mContext.getString(R.string.cancel));
        setPositiveButton(mContext.getString(R.string.confirm));
    }

    @Override
    protected int bindContentView() {
        return R.layout.dialog_options_picker;
    }

    @SuppressWarnings("unused")
    public OptionsPickerDialog withData(@Nullable List<DictionaryBean> dataList) {
        return withData(dataList, 0);
    }

    public OptionsPickerDialog withData(@Nullable List<DictionaryBean> dataList, @Nullable DictionaryBean selectBean) {
        if (dataList != null && !dataList.isEmpty()) {
            mDataList = new ArrayList<>(dataList);
            int selectPosition = 0;
            if (selectBean != null) {
                selectPosition = dataList.indexOf(selectBean);
            }
            invalidate(selectPosition);
        }
        return this;
    }

    public OptionsPickerDialog withData(@Nullable List<DictionaryBean> dataList, int selectPosition) {
        if (dataList != null && !dataList.isEmpty()) {
            mDataList = new ArrayList<>(dataList);
            invalidate(selectPosition);
        }
        return this;
    }

    private void invalidate(int selectPosition) {
        mOptionWv.setAdapter(new OptionsAdapter(mDataList));
        if (selectPosition >= 0 && selectPosition < mDataList.size()) {
            mOptionWv.setCurrentItem(selectPosition);
        }
    }

    public OptionsPickerDialog listener(@Nullable OnOptionPickerListener listener) {
        this.mListener = listener;
        return this;
    }

    public void setNegativeButton(@Nullable CharSequence text) {
        TextView tvNegative = mContentView.findViewById(R.id.tv_negative);
        if (TextUtils.isEmpty(text)) {
            tvNegative.setVisibility(View.INVISIBLE);
        } else {
            tvNegative.setVisibility(View.VISIBLE);
            tvNegative.setText(text);
            tvNegative.setOnClickListener(view -> dismiss());
        }
    }

    public void setPositiveButton(@Nullable CharSequence text) {
        TextView tvPositive = mContentView.findViewById(R.id.tv_positive);
        if (TextUtils.isEmpty(text)) {
            tvPositive.setVisibility(View.INVISIBLE);
        } else {
            tvPositive.setVisibility(View.VISIBLE);
            tvPositive.setText(text);
            tvPositive.setOnClickListener(view -> {
                int position = mOptionWv.getCurrentItem();
                if (mListener != null && position >= 0 && position < mDataList.size()) {
                    mListener.optionPicker(position, mDataList.get(position));
                }
                dismiss();
            });
        }
    }

    @Nullable
    @Override
    public ViewGroup.MarginLayoutParams getLayoutParams() {
        return new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected boolean fullWidth() {
        return true;
    }

    @Override
    public int getWindowAnimations() {
        return R.style.Dialog_Anim;
    }

    @Override
    public int getGravity() {
        return Gravity.BOTTOM;
    }

    static class OptionsAdapter implements WheelAdapter<String> {
        List<String> mDataList;

        OptionsAdapter(List<DictionaryBean> dataList) {
            mDataList = new ArrayList<>();
            if (dataList != null) {
                for (DictionaryBean bean : dataList) {
                    mDataList.add(bean.name);
                }
            }
        }

        @Override
        public int getItemsCount() {
            return mDataList.size();
        }

        @Override
        public String getItem(int index) {
            return mDataList.get(index);
        }

        @Override
        public int indexOf(String o) {
            return mDataList.indexOf(o);
        }
    }

    public interface OnOptionPickerListener {
        void optionPicker(int position, DictionaryBean bean);
    }
}
