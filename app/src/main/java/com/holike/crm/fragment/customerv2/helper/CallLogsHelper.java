package com.holike.crm.fragment.customerv2.helper;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.base.IntentValue;
import com.holike.crm.bean.CustomerManagerV2Bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pony on 2019/7/23.
 * Copyright holike possess 2019.
 */
public class CallLogsHelper {
    private List<CustomerManagerV2Bean.PhoneRecordBean> mBeans;
    private Context mContext;

    @SuppressWarnings("unchecked")
    public CallLogsHelper(Context context) {
        this.mContext = context;
        mBeans = new ArrayList<>();
        List<CustomerManagerV2Bean.PhoneRecordBean> list = (List<CustomerManagerV2Bean.PhoneRecordBean>)
                IntentValue.getInstance().get("phoneRecord");
        if (list != null && !list.isEmpty()) {
            mBeans.addAll(list);
        }
    }

    public void setup(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        CallLogsAdapter mAdapter = new CallLogsAdapter(mContext, mBeans);
        recyclerView.setAdapter(mAdapter);
    }

    class CallLogsAdapter extends CommonAdapter<CustomerManagerV2Bean.PhoneRecordBean> {
        String mTipsCallTime, mTipsCallDuration, mTipsDialer;

        CallLogsAdapter(Context context, List<CustomerManagerV2Bean.PhoneRecordBean> mDatas) {
            super(context, mDatas);
            mTipsCallTime = context.getString(R.string.tips_call_time);
            mTipsCallDuration = context.getString(R.string.tips_call_duration);
            mTipsDialer = context.getString(R.string.tips_call_dialer);
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_calllogs;
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, CustomerManagerV2Bean.PhoneRecordBean bean, int position) {
            holder.setText(R.id.tv_call_time, obtain(mTipsCallTime, bean.getCreateDate()));
            holder.setText(R.id.tv_call_duration, obtain(mTipsCallDuration, bean.talkTime));
            holder.setText(R.id.tv_dialer, obtain(mTipsDialer, bean.dailPerson));
            holder.setVisibility(R.id.v_divider, position != getItemCount() - 1);
        }

        SpannableString obtain(String origin, String content) {
            String source = origin;
            int start = source.length();
            source += (TextUtils.isEmpty(content) ? "" : content);
            int end = source.length();
            SpannableString ss = new SpannableString(source);
            int flags = SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE;
            ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.textColor4)), start, end, flags);
            ss.setSpan(new StyleSpan(Typeface.BOLD), start, end, flags);
            return ss;
        }
    }
}
