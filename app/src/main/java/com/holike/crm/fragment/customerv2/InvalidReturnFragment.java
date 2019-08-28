package com.holike.crm.fragment.customerv2;

import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.holike.crm.R;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.base.MyFragment;
import com.holike.crm.fragment.customerv2.helper.InvalidReturnHelper;
import com.holike.crm.presenter.fragment.GeneralCustomerPresenter;
import com.holike.crm.view.fragment.GeneralCustomerView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by gallop on 2019/7/22.
 * Copyright holike possess 2019.
 * 无效退回
 */
public class InvalidReturnFragment extends MyFragment<GeneralCustomerPresenter, GeneralCustomerView> {
    @BindView(R.id.rv_reason)
    RecyclerView mRecyclerView;
    @BindView(R.id.tvSave)
    TextView mSaveTextView;

    private InvalidReturnHelper mHelper;

    @Override
    protected GeneralCustomerPresenter attachPresenter() {
        return new GeneralCustomerPresenter();
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_customer_invalidreturn;
    }

    @Override
    protected void init() {

    }

    @OnClick(R.id.tvSave)
    public void onViewClicked() {

    }
}
