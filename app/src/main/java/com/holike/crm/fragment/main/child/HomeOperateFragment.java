package com.holike.crm.fragment.main.child;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.activity.customer.CustomerEditActivity;
import com.holike.crm.activity.customer.CustomerStatusListActivity;
import com.holike.crm.activity.homepage.FeedbackRecordActivity;
import com.holike.crm.activity.homepage.ReceivingScanActivity;
import com.holike.crm.activity.mine.FeedbackActivity;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.HomepageBean;
import com.holike.crm.enumeration.CustomerValue;
import com.holike.crm.util.Constants;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class HomeOperateFragment extends MyFragment {
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private List<HomepageBean.NewDataBean.ItemListBean> mBeans = new ArrayList<>();
    private OperateAdapter mAdapter;

    private class OperateAdapter extends CommonAdapter<HomepageBean.NewDataBean.ItemListBean> {

        OperateAdapter(Context context, List<HomepageBean.NewDataBean.ItemListBean> mDatas) {
            super(context, mDatas);
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, HomepageBean.NewDataBean.ItemListBean bean, int position) {
            holder.setText(R.id.tv_operate_name,bean.getName());
            Glide.with(mContext).load(bean.getIcon()).into((ImageView) holder.obtainView(R.id.iv_operate_icon));
            holder.itemView.setOnClickListener(v -> {
                String name = bean.getName();
                if (TextUtils.equals(name, CustomerValue.ADD_V1) || TextUtils.equals(name, CustomerValue.ADD_V2)) {
                    MobclickAgent.onEvent(mContext, "homepage_add_customer");
                    Intent intent = new Intent(mContext, CustomerEditActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.IS_EARNEST, "0");
                    openActivity(intent, bundle);
                } else if (TextUtils.equals(name, CustomerValue.FEEDBACK)) { //售后反馈
                    MobclickAgent.onEvent(mContext, "homepage_feedback");
                    startActivity(FeedbackActivity.class);
                } else if (TextUtils.equals(name, CustomerValue.FEEDBACK_RECORD)) { //反馈记录
                    MobclickAgent.onEvent(mContext, "homepage_feedback_record");
                    startActivity(FeedbackRecordActivity.class);
                } else if (TextUtils.equals(name, CustomerValue.RECEIPT_SCAN)) { //收货扫码
                    MobclickAgent.onEvent(mContext, "homepage_receiving_scan");
                    startActivity(ReceivingScanActivity.class);
                } else {
                    openCustomerStatusListActivity(name);
                }
            });
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_homepage_operate;
        }
    }

    private void openCustomerStatusListActivity(String name) {
        onEvent(name);
        Intent intent = new Intent(mContext, CustomerStatusListActivity.class); //v2.0 ->CustomerStatusListActivity
        intent.putExtra("statusName", name);
        openActivity(intent);
    }

    /*友盟统计点击次数*/
    private void onEvent(String name) {
        if (TextUtils.equals(name, CustomerValue.STAY_DRAWING)) { //待出图客户
            MobclickAgent.onEvent(mContext, "homepage_unfigure_out_customer");
        } else if (TextUtils.equals(name, CustomerValue.STAY_SIGN)) { //待签约客户
            MobclickAgent.onEvent(mContext, "homepage_unsign_customer");
        } else if (TextUtils.equals(name, CustomerValue.STAY_COLLECT_AMOUNT)) { //待收全款客户
            MobclickAgent.onEvent(mContext, "homepage_unpayment_customer");
        } else if (TextUtils.equals(name, CustomerValue.ORDER)) { //待下单客户
            MobclickAgent.onEvent(mContext, "homepage_unorder_customer");
        } else if (TextUtils.equals(name, CustomerValue.STAY_INSTALL)) {  //待安装客户
            MobclickAgent.onEvent(mContext, "homepage_uninstall_customer");
        } else if (TextUtils.equals(name, CustomerValue.INSTALLED)) {//已安装客户
            MobclickAgent.onEvent(mContext, "homepage_installed_customer");
        }
    }

    @Override
    protected BasePresenter attachPresenter() {
        return null;
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_homepage_operate;
    }

    @Override
    protected void init() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
        mRecyclerView.setNestedScrollingEnabled(false);
        mAdapter = new OperateAdapter(mContext, mBeans);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void update(@NonNull List<HomepageBean.NewDataBean.ItemListBean> beans) {
        this.mBeans.clear();
        this.mBeans.addAll(beans);
        mAdapter.notifyDataSetChanged();
    }
}
