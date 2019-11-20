package com.holike.crm.fragment.customerv2.helper;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.holike.crm.R;
import com.holike.crm.adapter.MultipleChoiceAdapter;
import com.holike.crm.base.BaseFragment;
import com.holike.crm.base.IntentValue;
import com.holike.crm.bean.DictionaryBean;
import com.holike.crm.bean.SysCodeItemBean;
import com.holike.crm.helper.FlexboxManagerHelper;
import com.holike.crm.http.ParamHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by pony on 2019/7/23.
 * Copyright holike possess 2019.
 * 已流失帮助类
 */
public class BeenLostHelper extends GeneralHelper {
    private RecyclerView mReasonRecyclerView;
//    private RecyclerView mSeriesRecyclerView;
    private EditText mGoEditText;
    private EditText mRemarkEditText;
    private TextView mSaveTextView;

    private Callback mCallback;
    private List<DictionaryBean> mReasonItems;
//    private List<DictionaryBean> mSeriesItems;
    private SysCodeItemBean mSystemBean;
    private MultipleChoiceAdapter mReasonAdapter;

    public BeenLostHelper(BaseFragment<?, ?> fragment, Callback callback) {
        super(fragment);
        this.mCallback = callback;
        mReasonItems = new ArrayList<>();
//        mSeriesItems = new ArrayList<>();
        initView(fragment.getContentView());
    }

    private void initView(View contentView) {
        mReasonRecyclerView = contentView.findViewById(R.id.rv_reason);
        mGoEditText = contentView.findViewById(R.id.et_go);
        mRemarkEditText = contentView.findViewById(R.id.et_remark);
        mSaveTextView = contentView.findViewById(R.id.tvSave);
        setup();
        mSystemBean = IntentValue.getInstance().getSystemCode();
        if (mSystemBean == null) {
            mCallback.onQuerySystemCode();
        } else {
            setAdapter();
        }
    }

    public void setup() {
        mReasonRecyclerView.setLayoutManager(FlexboxManagerHelper.getDefault(mContext));
//        mSeriesRecyclerView.setLayoutManager(FlexboxManagerHelper.getDefault(mContext));
        mReasonAdapter = new MultipleChoiceAdapter(mContext, mReasonItems);
        mReasonRecyclerView.setAdapter(mReasonAdapter);
//        mSeriesAdapter = new MultipleChoiceAdapter(mContext, mSeriesItems);
//        mSeriesRecyclerView.setAdapter(mSeriesAdapter);
        mSaveTextView.setOnClickListener(view -> onSaved());
    }

    private void setAdapter() {
        for (Map.Entry<String, String> entry : mSystemBean.getLoseReason().entrySet()) {
            mReasonItems.add(new DictionaryBean(entry.getKey(), entry.getValue()));
        }
        mReasonAdapter.notifyDataSetChanged();
//        for (Map.Entry<String, String> entry : mSystemBean.getCustomerBrand().entrySet()) {
//            mSeriesItems.add(new DictionaryBean(entry.getKey(), entry.getValue()));
//        }
//        mSeriesAdapter.notifyDataSetChanged();
    }

    public void setSystemCode(SysCodeItemBean bean) {
        this.mSystemBean = bean;
        setAdapter();
    }

    private void onSaved() {
        String leaveReason;
        List<DictionaryBean> list = mReasonAdapter.getSelectedItems();
        if (list.isEmpty()) {
            leaveReason = "";
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                sb.append(list.get(i).id);
                if (i < list.size() - 1) {
                    sb.append(",");
                }
            }
            leaveReason = sb.toString();
        }
        if (TextUtils.isEmpty(leaveReason)) {
            showToast(mContext.getString(R.string.tips_please_select) + mContext.getString(R.string.followup_lose_reason_tips));
        } else {
            String leaveToSeries = mGoEditText.getText().toString();
            if(TextUtils.isEmpty(leaveToSeries.trim())){
                showToast(mGoEditText.getHint());
            } else {
                String body = ParamHelper.Customer.leaveHouse(mHouseId, leaveReason, leaveToSeries, mRemarkEditText.getText().toString());
                mCallback.onSaved(body);
            }
//            list = mSeriesAdapter.getSelectedItems();
//            if (list.isEmpty()) {
//                leaveToSeries = "";
//            } else {
//                StringBuilder sb = new StringBuilder();
//                for (int i = 0; i < list.size(); i++) {
//                    sb.append(list.get(i).id);
//                    if (i < list.size() - 1) {
//                        sb.append(",");
//                    }
//                }
//                leaveToSeries = sb.toString();
//            }
//            if (TextUtils.isEmpty(leaveToSeries)) {
//                showToast(mContext.getString(R.string.tips_please_select) + mContext.getString(R.string.followup_lose_series_tips));
//            } else {
//                String body = ParamHelper.Customer.leaveHouse(mHouseId, leaveReason, leaveToSeries, mRemarkEditText.getText().toString());
//                mCallback.onSaved(body);
//            }
        }
    }

    public interface Callback {
        void onQuerySystemCode();

        void onSaved(String body);
    }
}
