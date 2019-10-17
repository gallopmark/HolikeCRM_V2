package com.holike.crm.fragment.customerv2.helper;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxItemDecoration;
import com.holike.crm.R;
import com.holike.crm.adapter.SingleChoiceAdapter;
import com.holike.crm.base.BaseFragment;
import com.holike.crm.base.IntentValue;
import com.holike.crm.bean.DictionaryBean;
import com.holike.crm.bean.SysCodeItemBean;
import com.holike.crm.enumeration.CustomerValue;
import com.holike.crm.helper.FlexboxManagerHelper;
import com.holike.crm.http.ParamHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 无效退回帮助类
 */
public class InvalidReturnHelper {
    private InvalidReturnCallback mCallback;
    private String mHouseId, mEfficientCause;
    private List<DictionaryBean> mDataList;
    private SingleChoiceAdapter mAdapter;

    public InvalidReturnHelper(BaseFragment<?, ?> fragment, InvalidReturnCallback callback) {
        this.mCallback = callback;
        obtainBundleValue(fragment.getArguments());
        initView(fragment, fragment.getContentView());
    }

    private void obtainBundleValue(Bundle bundle) {
        if (bundle != null) {
            mHouseId = bundle.getString(CustomerValue.HOUSE_ID);
        }
    }

    private void initView(final BaseFragment<?, ?> fragment, View contentView) {
        RecyclerView recyclerView = contentView.findViewById(R.id.rv_reason);
        final Context context = recyclerView.getContext();
        recyclerView.setLayoutManager(FlexboxManagerHelper.getDefault(context));
        mDataList = new ArrayList<>();
        SysCodeItemBean bean = IntentValue.getInstance().getSystemCode();
        if (bean == null) {
            mCallback.onQuerySystemCode();
        } else {
            for (Map.Entry<String, String> entry : bean.getInvalidReturn().entrySet()) {
                mDataList.add(new DictionaryBean(entry.getKey(), entry.getValue()));
            }
        }
        mAdapter = new SingleChoiceAdapter(context, mDataList);
        mAdapter.setSingleChoiceListener(b -> mEfficientCause = b.id);
        recyclerView.setAdapter(mAdapter);
        TextView tvSave = contentView.findViewById(R.id.tvSave);
        tvSave.setOnClickListener(view -> {
            if (TextUtils.isEmpty(mEfficientCause)) {
                String text = context.getString(R.string.tips_please_select) + context.getString(R.string.customer_invalid_return_reason);
                fragment.showShortToast(text);
            } else {
                mCallback.onSave(ParamHelper.Customer.invalidReturn(mHouseId, mEfficientCause));
            }
        });
    }

    public void setSystemCode(SysCodeItemBean bean) {
        mDataList.clear();
        for (Map.Entry<String, String> entry : bean.getInvalidReturn().entrySet()) {
            mDataList.add(new DictionaryBean(entry.getKey(), entry.getValue()));
        }
        mAdapter.notifyDataSetChanged();
    }

    public interface InvalidReturnCallback {
        void onQuerySystemCode();

        void onSave(String body);
    }
}
