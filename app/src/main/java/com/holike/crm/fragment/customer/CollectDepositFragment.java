package com.holike.crm.fragment.customer;

import android.content.Intent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.activity.homepage.AddCustomerActivity;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.CollectDepositListBean;
import com.holike.crm.bean.CustomerDetailBean;
import com.holike.crm.model.event.MessageEvent;
import com.holike.crm.presenter.activity.CollectDepositPresenter;
import com.holike.crm.util.Constants;
import com.holike.crm.view.activity.CollectDepositView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wqj on 2018/8/20.
 * 首页进入收取订金页
 */
public class CollectDepositFragment extends MyFragment<CollectDepositPresenter, CollectDepositView> implements CollectDepositView {
    @BindView(R.id.rv_collect_deposit)
    RecyclerView rv;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_collect_deposit_list;
    }

    @Override
    protected CollectDepositPresenter attachPresenter() {
        return new CollectDepositPresenter();
    }

    @Override
    protected void init() {
        super.init();

        setLeft(getString(R.string.homepage));
        setTitle(getString(R.string.receive_deposit_title));
        setRightMenu(getString(R.string.receive_deposit_add_customer));
        rv.setLayoutManager(new LinearLayoutManager(mContext));
        rv.setNestedScrollingEnabled(false);
        mPresenter.getData(Constants.DEFAULT_PAGE, "");
        showLoading();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(MessageEvent messageEvent) {
        if (Constants.EVENT_REFRESH.equals(messageEvent.getMessage())) {
            mPresenter.getData(Constants.DEFAULT_PAGE, "");
        }
    }

    /**
     * 新建客户
     */
    @Override
    protected void clickRightMenu(String text, View actionView) {
        Intent intent = new Intent(mContext, AddCustomerActivity.class);   //v2.0 CustomerEditActivity
        intent.putExtra(Constants.IS_EARNEST, "1");
        startActivityForResult(intent, REQUEST_CODE);
    }

    @OnClick(R.id.tv_collect_deposit_search)
    public void onViewClicked() {
        startFragment(null, new SearchCollectDepositFragment());
    }

    /**
     * 获取数据成功
     *
     * @param beans
     */
    @Override
    public void success(List<CollectDepositListBean> beans) {
        dismissLoading();
        rv.setAdapter(new CommonAdapter<CollectDepositListBean>(mContext, beans) {
            @Override
            protected int bindView(int viewType) {
                return R.layout.item_rv_collect_deposit_list;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, CollectDepositListBean bean, int position) {
                TextView tvName = holder.obtainView(R.id.tv_item_rv_collect_deposit_name);
                TextView tvPhone = holder.obtainView(R.id.tv_item_rv_collect_deposit_phone);
                TextView tvAddress = holder.obtainView(R.id.tv_item_rv_collect_deposit_address);
                tvName.setText(bean.getUserName());
                tvPhone.setText(bean.getPhoneNumber());
                tvAddress.setText(bean.getAddress());
                holder.itemView.setOnClickListener(v -> {
                    Map<String, Serializable> params = new HashMap<>();
                    params.put(Constants.COLLECT_DEPOSIT_LIST_BEAN, bean);
                    startFragment(params, new com.holike.crm.fragment.customer.workflow.CollectDepositFragment());
                });
            }

        });
    }

    /**
     * 获取数据失败
     */
    @Override
    public void failed(String failed) {
        dismissLoading();
        showShortToast(failed);
    }

    @Override
    public void getCustomerFailed(String failed) {

    }

    @Override
    public void getCustomerSuccess(CustomerDetailBean customerDetailBean) {

    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

}
