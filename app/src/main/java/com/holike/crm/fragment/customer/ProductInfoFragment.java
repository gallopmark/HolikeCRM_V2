package com.holike.crm.fragment.customer;

import android.os.Handler;
import android.os.Looper;
import androidx.recyclerview.widget.RecyclerView;

import com.holike.crm.R;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.ProductInfoBean;
import com.holike.crm.presenter.fragment.ProductInfoPresenter;
import com.holike.crm.util.Constants;
import com.holike.crm.util.RecyclerUtils;
import com.holike.crm.view.fragment.ProductInfoView;

import java.util.List;

import butterknife.BindView;

/**
 * 产品信息
 */
public class ProductInfoFragment extends MyFragment<ProductInfoPresenter, ProductInfoView> implements ProductInfoView {
    @BindView(R.id.rv_product_info_tab)
    RecyclerView rvTab;
    @BindView(R.id.rv_product_info_side)
    RecyclerView rvSide;
    @BindView(R.id.rv_product_info_center)
    RecyclerView rvCenter;
    @BindView(R.id.rv_product_info_content_name)
    RecyclerView rvContentName;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_product_info;
    }

    @Override
    protected ProductInfoPresenter attachPresenter() {
        return new ProductInfoPresenter();
    }

    @Override
    protected void init() {
        super.init();
        setTitle(getString(R.string.product_info));
        if (getArguments() != null && getArguments().getSerializable(Constants.ORDER_ID) != null) {
            String orderId = (String) getArguments().getSerializable(Constants.ORDER_ID);
            showLoading();
            mPresenter.getData(orderId);
        } else {
            onFail(getString(R.string.tips_no_data));
        }
    }

    @Override
    public void onSuccess(final List<ProductInfoBean> bean) {
        dismissLoading();
        if (bean.size() == 0) {
            onFail(getString(R.string.tips_no_data));
        }
        mPresenter.setTabAdapter(mContext, rvTab, bean);
        new Handler().postDelayed(() -> {
            if (bean.size() == 0)
                return;
            mPresenter.setSideAdapter(mContext, rvSide, bean, 0);
            mPresenter.setContentNameAdapter(mContext, rvContentName, bean.get(0));
            mPresenter.setContentAdapter(mContext, rvCenter, bean, 0);
        }, 0);
        RecyclerUtils.setScrollSynchronous(rvSide,rvCenter);
    }


    @Override
    public void onFail(String errorMsg) {
        dismissLoading();
        noResult(getString(R.string.tips_no_corresponding_product_information));
    }

    @Override
    public void onTagSelect(final int position, final List<ProductInfoBean> listBean) {

        mPresenter.setContentNameAdapter(mContext, rvContentName, listBean.get(position));
        mPresenter.setContentAdapter(mContext, rvCenter, listBean, position);
        mPresenter.setSideAdapter(mContext, rvSide, listBean, position);


    }

    @Override
    public void onTagClickStart(boolean showDialog) {
        if (showDialog)
            showLoading();

        new Handler(Looper.getMainLooper()).postDelayed(this::dismissLoading, 1000);
    }
}
