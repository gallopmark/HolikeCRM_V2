package com.holike.crm.fragment.customer;

import android.os.Handler;
import android.os.Looper;
import androidx.recyclerview.widget.RecyclerView;

import com.holike.crm.R;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.ProductInfoBean;
import com.holike.crm.presenter.fragment.ProductInfoPresenter;
import com.holike.crm.util.Constants;
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
        mPresenter.setTabAdapter(getActivity(), rvTab, bean);
        new Handler().postDelayed(() -> {
            if (bean.size() == 0)
                return;
            mPresenter.setSideAdapter(getActivity(), rvSide, bean, 0);
            mPresenter.setContentNameAdapter(getActivity(), rvContentName, bean.get(0));
            mPresenter.setContentAdapter(getActivity(), rvCenter, bean, 0);
        }, 0);

        setScrollSynchronous(rvCenter, rvSide);
        setScrollSynchronous(rvSide, rvCenter);
    }


    @Override
    public void onFail(String errorMsg) {
        dismissLoading();
        noResult(getString(R.string.tips_no_corresponding_product_information));
    }

    @Override
    public void onTagSelect(final int position, final List<ProductInfoBean> listBean) {

        mPresenter.setContentNameAdapter(getActivity(), rvContentName, listBean.get(position));
        mPresenter.setContentAdapter(getActivity(), rvCenter, listBean, position);
        mPresenter.setSideAdapter(getActivity(), rvSide, listBean, position);


    }

    @Override
    public void onTagClickStart(boolean showDialog) {
        if (showDialog)
            showLoading();

        new Handler(Looper.getMainLooper()).postDelayed(() -> dismissLoading(), 1000);
    }

    /**
     * 设置2个 RecyclerView 同步滚动
     *
     * @param hostRv   导向者
     * @param syncerRv 被导向者
     */
    public void setScrollSynchronous(RecyclerView hostRv, final RecyclerView syncerRv) {
        hostRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
                    syncerRv.scrollBy(dx, dy);
                }

            }
        });
    }
}
