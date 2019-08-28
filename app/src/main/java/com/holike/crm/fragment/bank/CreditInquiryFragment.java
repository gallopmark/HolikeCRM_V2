package com.holike.crm.fragment.bank;

import androidx.recyclerview.widget.RecyclerView;

import android.widget.TextView;

import com.holike.crm.R;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.CreditInquiryBean;
import com.holike.crm.presenter.fragment.CreditInquiryPresenter;
import com.holike.crm.util.CopyUtil;
import com.holike.crm.view.fragment.CreditInquiryView;
import com.scwang.smartrefresh.header.WaterDropHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;

import butterknife.BindView;

public class CreditInquiryFragment extends MyFragment<CreditInquiryPresenter, CreditInquiryView> implements CreditInquiryView {

    @BindView(R.id.t1)
    TextView tvCreditAmount;
    @BindView(R.id.t2)
    TextView tvOrderTot;
    @BindView(R.id.t3)
    TextView tvSurplusAmount;
    @BindView(R.id.rv_detail_list)
    RecyclerView rv;
    @BindView(R.id.srl_detail_list)
    SmartRefreshLayout srlDetailList;
    @BindView(R.id.tv_factory_discount_bf)
    TextView tvFactoryDiscountBf;
    @BindView(R.id.tv_factory_discount_af)
    TextView tvFactoryDiscountAf;


    private int pageNo = 1;
    private int loadType;
    private static final int LOAD_TYPE_REFRESH = 0;
    private static final int LOAD_TYPE_LOAD_MORE = 1;


    @Override
    protected CreditInquiryPresenter attachPresenter() {
        return new CreditInquiryPresenter();
    }

    @Override
    protected void init() {
        super.init();
        setTitle(getString(R.string.credit_inquiry));
        mPresenter.setAdapter(rv);
        srlDetailList.setRefreshHeader(new WaterDropHeader(mContext));
        srlDetailList.setRefreshFooter(new BallPulseFooter(mContext));
        srlDetailList.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                loadmore();
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refresh(false);
            }
        });
        initData();
    }

    private void initData() {
        showLoading();
        mPresenter.getData(pageNo);
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_credit_survey;
    }

    @Override
    public void onSuccess(CreditInquiryBean bean) {
        srlDetailList.finishLoadMore();
        srlDetailList.finishRefresh();
        tvCreditAmount.setText(textEmptyNumber(bean.getCreditAmount()));
        tvOrderTot.setText(textEmptyNumber(bean.getOrderTot()));
        tvSurplusAmount.setText(textEmptyNumber(bean.getSurplusAmount()));
        tvFactoryDiscountBf.setText(textEmptyNumber(bean.getPlantTotPriBe()));
        tvFactoryDiscountAf.setText(textEmptyNumber(bean.getPlantTotPriAf()));
        if (loadType == LOAD_TYPE_LOAD_MORE) {
            if (bean.getPageData().size() > 0) {
                loadmoreSuccess(bean.getPageData());
            } else {
                loadAllSuccess();
            }
        } else {
            refreshSuccess(bean.getPageData());
        }
    }

    @Override
    public void onFail(String errorMsg) {
        dismissLoading();
        showShortToast(errorMsg);
        if (pageNo == 1 && loadType != LOAD_TYPE_REFRESH && loadType != LOAD_TYPE_LOAD_MORE) {
            noData(R.drawable.no_result, errorMsg, false);
        }
    }

    @Override
    public void refresh(boolean showLoading) {
        if (showLoading) {
            showLoading();
        }
        pageNo = 1;
        mPresenter.getData(pageNo);
        loadType = LOAD_TYPE_REFRESH;
    }

    @Override
    public void refreshSuccess(List<CreditInquiryBean.PageDataBean> listBeans) {
        dismissLoading();
        srlDetailList.setEnableLoadMore(true);
        if (listBeans.size() > 0) {
            hasData();
            mPresenter.onRefreshCompleted(listBeans);
        } else {
            noResult();
        }
    }

    @Override
    public void loadmore() {
        pageNo++;
        loadType = LOAD_TYPE_LOAD_MORE;
        mPresenter.getData(pageNo);
    }

    @Override
    public void loadmoreSuccess(List<CreditInquiryBean.PageDataBean> listBeans) {
        srlDetailList.setEnableLoadMore(true);
        mPresenter.onLoadMoreCompleted(listBeans);
    }

    @Override
    public void onItemLongClick(String orderId) {
        CopyUtil.copy(mContext, orderId);
        showShortToast("订单号已复制到粘贴板");
    }

    @Override
    public void loadAllSuccess() {
        srlDetailList.setEnableLoadMore(false);
        mPresenter.noMoreData();
    }

}
